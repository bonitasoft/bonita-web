/**
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.console.common.server.page;

import static java.util.Collections.emptySet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import groovy.lang.GroovyClassLoader;
import org.apache.commons.io.FileUtils;
import org.bonitasoft.console.common.server.page.extension.PageResourceProviderImpl;
import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.bonitasoft.console.common.server.preferences.properties.PropertiesFactory;
import org.bonitasoft.console.common.server.preferences.properties.PropertiesWithSet;
import org.bonitasoft.console.common.server.utils.UnzipUtil;
import org.bonitasoft.engine.api.PageAPI;
import org.bonitasoft.engine.api.PermissionAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.exception.AlreadyExistsException;
import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.page.Page;
import org.bonitasoft.engine.page.PageNotFoundException;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.extension.page.PageController;
import org.bonitasoft.web.extension.page.PageResourceProvider;
import org.bonitasoft.web.extension.rest.RestApiController;
import org.bonitasoft.web.rest.server.api.extension.ControllerClassName;
import org.codehaus.groovy.control.CompilationFailedException;

/**
 * @author Anthony Birembaut, Fabio Lombardi
 */
public class CustomPageService {


    private static final String PAGE_LIB_DIRECTORY = "lib";

    public static final String PAGE_CONTROLLER_FILENAME = "Index.groovy";

    public static final String PAGE_INDEX_NAME = "index";

    public static final String PAGE_INDEX_FILENAME = "index.html";

    private static final ConcurrentMap<String, GroovyClassLoader> PAGES_CLASSLOADERS = new ConcurrentHashMap<>();
    
    private static final ConcurrentMap<String, Long> PAGES_UPDATE_TIMESTAMPS = new ConcurrentHashMap<>();
    
    private static final ConcurrentMap<String, Long> PAGES_LAST_UPDATE_DB_CHECK = new ConcurrentHashMap<>();
    
    private static final ConcurrentMap<String, Object> PAGES_LOCKS = new ConcurrentHashMap<>();

    public static final String RESOURCES_PROPERTY = "resources";
    public static final String PROPERTY_CONTENT_TYPE = "contentType";

    public static final String NAME_PROPERTY = "name";

    private static final Logger LOGGER = Logger.getLogger(CustomPageService.class.getName());

    public GroovyClassLoader getPageClassloader(final APISession apiSession, final PageResourceProvider pageResourceProvider)
            throws IOException, CompilationFailedException, BonitaException {
        return buildPageClassloader(apiSession, pageResourceProvider.getFullPageName(),
                pageResourceProvider.getPageDirectory());
    }

    public void ensurePageFolderIsPresent(final APISession apiSession, final PageResourceProvider pageResourceProvider)
            throws BonitaException, IOException {
        File pageDirectory = pageResourceProvider.getPageDirectory();
        if (!pageDirectory.exists() || pageDirectory.list().length == 0) {
            retrievePageZipContent(apiSession, pageResourceProvider);
        }
    }

    public void ensurePageFolderIsUpToDate(final APISession apiSession, final PageResourceProvider pageResourceProvider)
            throws BonitaException, IOException {
        final String fullPageName = pageResourceProvider.getFullPageName();
        synchronized (getPageLock(fullPageName)) {
            final Long pageTimestampFromCache = getPageTimestampFromMemoryCache(fullPageName);
            if (pageTimestampFromCache != null) {
                if (shouldVerifyLastUpdateDateInDatabaseAndFolderHealthy(apiSession, fullPageName)) {
                    //if it has been less than a certain time since the last database check do not check again
                    final long databaseLastUpdateTimestamp = getPageLastUpdateDateFromEngine(apiSession, pageResourceProvider);
                    if (databaseLastUpdateTimestamp != pageTimestampFromCache) {
                        removePage(apiSession, pageResourceProvider, true);
                        retrievePageZipContent(apiSession, pageResourceProvider);
                    } else {
                        //make sure the page temp directory has not been altered
                        ensurePageTempFolderIsHealthy(apiSession, pageResourceProvider);
                    }
                }
            } else {
                //if the last update date is not in the cache, the page has not been retrieved yet
                retrievePageZipContent(apiSession, pageResourceProvider);
            }
        }
    }

    protected void ensurePageTempFolderIsHealthy(final APISession apiSession,
            final PageResourceProvider pageResourceProvider) throws IOException, BonitaException {
        final File pageFolder = pageResourceProvider.getPageDirectory();
        if (!pageFolder.exists() || pageFolder.list().length == 0) {
            removePage(apiSession, pageResourceProvider, true);
            retrievePageZipContent(apiSession, pageResourceProvider);
        }
    }

    protected boolean shouldVerifyLastUpdateDateInDatabaseAndFolderHealthy(final APISession apiSession, final String fullPageName) {
        Long lastTimePageUpdateWasCheckedInDB = getLastTimePageUpdateWasCheckedInDB(fullPageName);
        if (lastTimePageUpdateWasCheckedInDB != null) {
            return System.currentTimeMillis() - lastTimePageUpdateWasCheckedInDB > getPageLastUpdateCheckInterval(apiSession);
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public Class<?> registerPage(final GroovyClassLoader pageClassLoader, final PageResourceProvider pageResourceProvider)
            throws CompilationFailedException, IOException {
        final File pageControllerFile = getGroovyPageFile(pageResourceProvider.getPageDirectory());
        return pageClassLoader.parseClass(pageControllerFile);
    }

    public Class<?> registerRestApiPage(final GroovyClassLoader pageClassLoader,
                                        PageResourceProviderImpl pageResourceProvider, ControllerClassName restApiControllerClassName, String mappingKey)
            throws BonitaException {
        try {
            if (restApiControllerClassName.isSource()) {
                File groovyFile = toFile(pageResourceProvider, restApiControllerClassName.getName());
                if (groovyFile.exists()) {
                    return pageClassLoader.parseClass(groovyFile);
                }
                LOGGER.log(Level.SEVERE, "resource does not exists:" + mappingKey);
                throw new BonitaException("unable to handle rest api call to " + mappingKey);
            }
            return pageClassLoader.loadClass(restApiControllerClassName.getName());
        } catch (CompilationFailedException | IOException | ClassNotFoundException e) {
            throw new BonitaException(e.getMessage(), e);
        }
    }

    protected File toFile(PageResourceProviderImpl pageResourceProvider, String classFileName) {
        if (classFileName.startsWith("/")) {
            classFileName = classFileName.substring(1);
        }
        final String[] paths = classFileName.split("/");
        final Path restApiControllerPath = paths.length == 1 ? Paths.get(paths[0])
                : Paths.get(paths[0], Arrays.copyOfRange(paths, 1, paths.length));
        return pageResourceProvider.getPageDirectory().toPath().resolve(restApiControllerPath).toFile();
    }

    public void verifyPageClass(final File tempPageDirectory, APISession session) throws IOException {
        final File pageControllerFile = getPageFile(tempPageDirectory, PAGE_CONTROLLER_FILENAME);
        if (pageControllerFile.exists()) {
            final String classloaderName = String.valueOf(System.currentTimeMillis());
            final GroovyClassLoader pageClassLoader = buildPageClassloader(session, classloaderName, tempPageDirectory);
            try {
                pageClassLoader.parseClass(pageControllerFile);
            } catch (final CompilationFailedException ex) {
                LOGGER.log(Level.SEVERE, "Failed to compile Index.groovy ", ex);
            } finally {
                final GroovyClassLoader classLoader = PAGES_CLASSLOADERS.remove(classloaderName);
                if (classLoader != null) {
                    classLoader.close();
                }
            }
        }
    }

    public PageController loadPage(final Class<PageController> pageClass)
            throws InstantiationException, IllegalAccessException {
        return pageClass.newInstance();
    }

    public RestApiController loadRestApiPage(final Class<RestApiController> restApiControllerClass)
            throws InstantiationException, IllegalAccessException {
        return restApiControllerClass.newInstance();
    }

    protected void removePage(final APISession apiSession, final PageResourceProvider pageResourceProvider, final boolean ignoreErrorOnPageDirectoryDelete) throws IOException {
        String fullPageName = pageResourceProvider.getFullPageName();
        closeClassloader(fullPageName);
        removePageZipContent(pageResourceProvider.getPageDirectory(), ignoreErrorOnPageDirectoryDelete);
        CustomPageDependenciesResolver.removePageLibTempFolder(fullPageName);
        removePageTimestampsFromMemoryCache(fullPageName);
    }

    public void removePageLocally(final APISession apiSession, final Page page) throws IOException {
        final PageResourceProvider pageResourceProvider = new PageResourceProviderImpl(page, apiSession.getTenantId(), false);
        removePageLocally(apiSession, pageResourceProvider);
    }
    
    public void removePageLocally(final APISession apiSession, final PageResourceProvider pageResourceProvider) throws IOException {
        removePage(apiSession, pageResourceProvider, false);
        removePageLock(pageResourceProvider.getFullPageName());
    }

    protected Object getPageLock(final String fullPageName) {
        return PAGES_LOCKS.computeIfAbsent(fullPageName, k -> new Object());
    }
    
    protected void removePageLock(final String fullPageName) {
        PAGES_LOCKS.remove(fullPageName);
    }
    
    protected void addPageTimestampToMemoryCache(final String fullPageName, long timestamp) {
        PAGES_UPDATE_TIMESTAMPS.put(fullPageName, timestamp);
    }
    
    protected void setTimePageUpdateWasCheckedInDB(final String fullPageName) {
        PAGES_LAST_UPDATE_DB_CHECK.put(fullPageName, System.currentTimeMillis());
    }

    protected Long getPageTimestampFromMemoryCache(final String fullPageName) {
        return PAGES_UPDATE_TIMESTAMPS.get(fullPageName);
    }

    protected Long getLastTimePageUpdateWasCheckedInDB(final String fullPageName) {
        return PAGES_LAST_UPDATE_DB_CHECK.get(fullPageName);
    }
    
    protected void removePageTimestampsFromMemoryCache(final String fullPageName) {
        PAGES_LAST_UPDATE_DB_CHECK.remove(fullPageName);
        PAGES_UPDATE_TIMESTAMPS.remove(fullPageName);
    }
    
    protected void clearPageTimestampsMemoryCache() {
        PAGES_UPDATE_TIMESTAMPS.clear();
        PAGES_LAST_UPDATE_DB_CHECK.clear();
    }

    private static void closeClassloader(final String pageName) throws IOException {
        final GroovyClassLoader classloader = PAGES_CLASSLOADERS.remove(pageName);
        if (classloader != null) {
            classloader.clearCache();
            classloader.close();
        }
    }

    protected GroovyClassLoader buildPageClassloader(final APISession apiSession, final String pageName,
                                                     final File pageDirectory)
            throws CompilationFailedException, IOException {
        final BDMClientDependenciesResolver bdmDependenciesResolver = new BDMClientDependenciesResolver(apiSession);
        if (isPageInDebugMode(apiSession)) {
            synchronized (CustomPageService.class) {//Handle multiple queries to create several classloaders at the same time
                return createPageClassloader(apiSession, pageName, pageDirectory, bdmDependenciesResolver);
            }
        } else {
            //not putting the get in the synchronized block to avoid performance cost when the classloader is already in the map
            GroovyClassLoader pageClassLoader = PAGES_CLASSLOADERS.get(pageName);
            if (pageClassLoader == null || isOutdated(pageClassLoader, bdmDependenciesResolver)) {
                synchronized (CustomPageService.class) {//Handle multiple queries to create several classloaders at the same time
                    if (pageClassLoader == null) {
                        //double check in synchronize block to avoid creating the classloader twice if a creation is already in progress in another thread
                        pageClassLoader = PAGES_CLASSLOADERS.get(pageName);
                        if (pageClassLoader == null || isOutdated(pageClassLoader, bdmDependenciesResolver)) {
                            pageClassLoader = createPageClassloader(apiSession, pageName, pageDirectory, bdmDependenciesResolver);
                            PAGES_CLASSLOADERS.put(pageName, pageClassLoader);
                        }
                    } else {
                        //classloader is outdated
                        pageClassLoader.clearCache();
                        pageClassLoader.close();
                        pageClassLoader = createPageClassloader(apiSession, pageName, pageDirectory, bdmDependenciesResolver);
                        PAGES_CLASSLOADERS.put(pageName, pageClassLoader);
                    }
                }
            }
            return pageClassLoader;
        }
    }

    private GroovyClassLoader createPageClassloader(final APISession apiSession, final String pageName, final File pageDirectory,
            final BDMClientDependenciesResolver bdmDependenciesResolver) throws IOException {
        GroovyClassLoader pageClassLoader = new GroovyClassLoader(getParentClassloader(pageName,
                new CustomPageDependenciesResolver(pageName, pageDirectory, getWebBonitaConstantsUtils(apiSession)),
                bdmDependenciesResolver));
        pageClassLoader.addClasspath(pageDirectory.getPath());
        return pageClassLoader;
    }

    private boolean isOutdated(GroovyClassLoader pageClassLoader, BDMClientDependenciesResolver bdmDependenciesResolver) {
        final ClassLoader parent = pageClassLoader.getParent();
        if (!(parent instanceof VersionedClassloader)) {
            throw new IllegalStateException("Parent classloader should be versioned.");
        }
        final VersionedClassloader cachedClassloader = (VersionedClassloader) parent;
        return !cachedClassloader.hasVersion(bdmDependenciesResolver.getBusinessDataModelVersion());
    }

    public boolean isPageInDebugMode(final APISession apiSession) {
        return PropertiesFactory.getConsoleProperties(apiSession.getTenantId()).isPageInDebugMode();
    }
    
    public long getPageLastUpdateCheckInterval(final APISession apiSession) {
        return PropertiesFactory.getConsoleProperties(apiSession.getTenantId()).getPageLastUpdateCheckInterval();
    }
    
    protected WebBonitaConstantsUtils getWebBonitaConstantsUtils(final APISession apiSession) {
        return WebBonitaConstantsUtils.getInstance(apiSession.getTenantId());
    }

    protected ClassLoader getParentClassloader(final String pageName,
                                               final CustomPageDependenciesResolver customPageDependenciesResolver,
                                               final BDMClientDependenciesResolver bdmDependenciesResolver) throws IOException {
        final CustomPageChildFirstClassLoader classLoader = new CustomPageChildFirstClassLoader(pageName,
                customPageDependenciesResolver,
                bdmDependenciesResolver, Thread.currentThread().getContextClassLoader());
        classLoader.addCustomPageResources();
        return classLoader;
    }

    protected void retrievePageZipContent(final APISession apiSession, final PageResourceProvider pageResourceProvider)
            throws BonitaException, IOException {
        final PageAPI pageAPI = getPageAPI(apiSession);
        // retrieve page zip content from engine and cache it
        final Page page = pageResourceProvider.getPage(pageAPI);
        final byte[] pageContent = pageAPI.getPageContent(page.getId());
        if (pageContent.length == 0) {
            throw new BonitaException("No content available for page: " + page.getName());
        }
        final File tempPageFile = ((PageResourceProviderImpl) pageResourceProvider).getTempPageFile();
        FileUtils.writeByteArrayToFile(tempPageFile, pageContent);
        UnzipUtil.unzip(tempPageFile, pageResourceProvider.getPageDirectory().getPath(), true);
        long lastUpdateTimestamp = 0L;
        if (page.getLastModificationDate() != null) {
            lastUpdateTimestamp = page.getLastModificationDate().getTime();
        }
        String fullPageName = pageResourceProvider.getFullPageName();
        addPageTimestampToMemoryCache(fullPageName, lastUpdateTimestamp);
        setTimePageUpdateWasCheckedInDB(fullPageName);
    }
    
    protected PageAPI getPageAPI(final APISession apiSession) throws BonitaException {
        return TenantAPIAccessor.getCustomPageAPI(apiSession);
    }

    protected PermissionAPI getPermissionAPI(final APISession apiSession) throws BonitaException {
        return TenantAPIAccessor.getPermissionAPI(apiSession);
    }

    protected void removePageZipContent(final File pageDirectory, final boolean ignoreErrorOnPageDirectoryDelete) throws IOException {
        try {
            FileUtils.deleteDirectory(pageDirectory);
        } catch (IOException e) {
            //in some circumstances with java 8 page directory cannot be removed.
            //this catch and the method ignoreErrorOnPageDirectoryDelete can probably be removed in 7.13.x
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Unable delete page folder. " + e.getMessage());
            }
            if (!ignoreErrorOnPageDirectoryDelete) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE, "Page folder will be removed when jvm shuts down.");
                }
                pageDirectory.deleteOnExit();
            }
        }
    }

    public File getGroovyPageFile(final File pageDirectory) {
        return getPageFile(pageDirectory, PAGE_CONTROLLER_FILENAME);
    }

    public File getPageFile(final File pageDirectory, final String fileName) {
        return new File(pageDirectory, fileName);
    }

    protected long getPageLastUpdateDateFromEngine(final APISession apiSession,
                                                   final PageResourceProvider pageResourceProvider) throws BonitaException {
        long lastUpdate = 0L;
        try {
            final PageAPI pageAPI = getPageAPI(apiSession);
            final Date lastUpdateDate = pageResourceProvider.getPage(pageAPI).getLastModificationDate();
            if (lastUpdateDate != null) {
                lastUpdate = lastUpdateDate.getTime();
            }
        } catch (final PageNotFoundException e) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Unable to find the page " + pageResourceProvider);
            }
        }
        setTimePageUpdateWasCheckedInDB(pageResourceProvider.getFullPageName());
        return lastUpdate;
    }

    public Properties getPageProperties(final APISession apiSession, final byte[] zipContent,
                                        final boolean checkIfItAlreadyExists,
                                        final Long processDefinitionId) throws BonitaException {
        final PageAPI pageAPI = getPageAPI(apiSession);
        Properties properties;
        if (processDefinitionId == null) {
            properties = pageAPI.getPageProperties(zipContent, checkIfItAlreadyExists);
        } else {
            properties = pageAPI.getPageProperties(zipContent, false);
            if (checkIfItAlreadyExists) {
                final String pageName = properties.getProperty(NAME_PROPERTY);
                try {
                    pageAPI.getPageByNameAndProcessDefinitionId(pageName, processDefinitionId);
                    throw new AlreadyExistsException(
                            "A page with name " + pageName + " already exists for the process " + processDefinitionId);
                } catch (final PageNotFoundException e) {
                    try {
                        pageAPI.getPageByName(pageName);
                        throw new AlreadyExistsException("A page with name " + pageName + " already exists for the tenant");
                    } catch (final PageNotFoundException e1) {
                        //Do nothing (if the page was not found, it means a page with the same name doesn't already exist)
                    }
                    //Do nothing (if the page was not found, it means a page with the same name doesn't already exist)
                }
            }
        }
        return properties;
    }

    public Set<String> getCustomPagePermissions(final Properties pageProperties, APISession apiSession) throws BonitaException {
        final PropertiesWithSet pagePropertiesWithSet = new PropertiesWithSet(pageProperties);
        final Set<String> pageRestResources = new HashSet<>(pagePropertiesWithSet.getPropertyAsSet(RESOURCES_PROPERTY));
        final Set<String> permissions = new HashSet<>();
        for (final String pageRestResource : pageRestResources) {
            final Set<String> resourcePermissions = getPermissionAPI(apiSession).getResourcePermissions(pageRestResource);
            if (emptySet().equals(resourcePermissions)) {
                permissions.add("<" + pageRestResource + ">");
            }
            permissions.addAll(resourcePermissions);
        }
        return permissions;
    }

    public Page getPage(final APISession apiSession, final String pageName, final long processDefinitionId)
            throws BonitaException {
        return getPageAPI(apiSession).getPageByNameAndProcessDefinitionId(pageName, processDefinitionId);
    }

    public Page getPage(final APISession apiSession, final long pageId) throws BonitaException {
        return getPageAPI(apiSession).getPage(pageId);
    }

    public PageResourceProvider getPageResourceProvider(final Page page, final long tenantId) {
        return new PageResourceProviderImpl(page, tenantId);
    }
    
    public PageResourceProvider getPageResourceProvider(final Page page, final long tenantId, final boolean buildPageTempFile) {
        return new PageResourceProviderImpl(page, tenantId, buildPageTempFile);
    }

    public static void clearCachedClassloaders() throws IOException {
        for (final String page : PAGES_CLASSLOADERS.keySet()) {
            closeClassloader(page);
        }
    }

    public void writePageToPageDirectory(Page page,
                                         PageResourceProvider pageResourceProvider,
                                         File unzipPageTempFolder,
                                         APISession session) throws IOException {
        verifyPageClass(unzipPageTempFolder, session);
        File pageDirectory = pageResourceProvider.getPageDirectory();
        FileUtils.copyDirectory(unzipPageTempFolder, pageDirectory);
        long lastUpdateTimestamp = 0L;
        if (page.getLastModificationDate() != null) {
            lastUpdateTimestamp = page.getLastModificationDate().getTime();
        }
        String fullPageName = pageResourceProvider.getFullPageName();
        addPageTimestampToMemoryCache(fullPageName, lastUpdateTimestamp);
        setTimePageUpdateWasCheckedInDB(fullPageName);
    }

}
