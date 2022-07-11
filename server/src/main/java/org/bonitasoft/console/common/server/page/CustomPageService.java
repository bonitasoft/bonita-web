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
import java.util.logging.Level;
import java.util.logging.Logger;

import groovy.lang.GroovyClassLoader;
import org.apache.commons.io.FileUtils;
import org.bonitasoft.console.common.server.page.extension.PageResourceProviderImpl;
import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.bonitasoft.console.common.server.preferences.properties.ConsoleProperties;
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

    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(CustomPageService.class.getName());

    private static final String PAGE_LIB_DIRECTORY = "lib";

    public static final String PAGE_CONTROLLER_FILENAME = "Index.groovy";

    public static final String PAGE_INDEX_NAME = "index";

    public static final String PAGE_INDEX_FILENAME = "index.html";

    public static final String LASTUPDATE_FILENAME = ".lastupdate";

    private static final Map<String, GroovyClassLoader> PAGES_CLASSLOADERS = new HashMap<>();

    public static final String RESOURCES_PROPERTY = "resources";
    public static final String PROPERTY_CONTENT_TYPE = "contentType";

    public static final String NAME_PROPERTY = "name";

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
        final File pageFolder = pageResourceProvider.getPageDirectory();
        if (!pageFolder.exists() || pageFolder.list().length == 0) {
            retrievePageZipContent(apiSession, pageResourceProvider);
        } else {
            final File timestampFile = getPageFile(pageFolder, LASTUPDATE_FILENAME);
            final long lastUpdateTimestamp = getPageLastUpdateDateFromEngine(apiSession, pageResourceProvider);
            if (timestampFile.exists()) {
                final String timestampString = FileUtils.readFileToString(timestampFile);
                final long timestamp = Long.parseLong(timestampString);
                if (lastUpdateTimestamp != timestamp) {
                    removePageLocally(pageResourceProvider.getPageName());
                    retrievePageZipContent(apiSession, pageResourceProvider);
                }
            } else {
                FileUtils.writeStringToFile(timestampFile, String.valueOf(lastUpdateTimestamp), false);
            }
        }
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

    public void removePageLocally(final String pageName) throws IOException {
        closeClassloader(pageName);
        final PageResourceProvider pageResourceProvider = new PageResourceProviderImpl(pageName);
        removePageZipContent(pageResourceProvider);
        CustomPageDependenciesResolver.removePageLibTempFolder(pageName);
    }

    public void removePageLocally(final Page page) throws IOException {
        final PageResourceProvider pageResourceProvider = new PageResourceProviderImpl(page);
        final String pageName = pageResourceProvider.getFullPageName();
        closeClassloader(pageName);
        removePageZipContent(pageResourceProvider);
        CustomPageDependenciesResolver.removePageLibTempFolder(pageName);
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
        GroovyClassLoader pageClassLoader = PAGES_CLASSLOADERS.get(pageName);
        final BDMClientDependenciesResolver bdmDependenciesResolver = new BDMClientDependenciesResolver(apiSession);
        if (pageClassLoader == null
                || getConsoleProperties(apiSession).isPageInDebugMode()
                || isOutdated(pageClassLoader, bdmDependenciesResolver)) {
            synchronized (CustomPageService.class) {//Handle multiple queries to create several classloaders at the same time
                pageClassLoader = new GroovyClassLoader(getParentClassloader(pageName,
                        new CustomPageDependenciesResolver(pageName, pageDirectory, getWebBonitaConstantsUtils()),
                        bdmDependenciesResolver));
                pageClassLoader.addClasspath(pageDirectory.getPath());
                PAGES_CLASSLOADERS.put(pageName, pageClassLoader);
            }
        }
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

    protected ConsoleProperties getConsoleProperties(final APISession apiSession) {
        return PropertiesFactory.getConsoleProperties(apiSession.getTenantId());
    }

    protected WebBonitaConstantsUtils getWebBonitaConstantsUtils() {
        return WebBonitaConstantsUtils.getTenantInstance();
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
        final File timestampFile = getPageFile(pageResourceProvider.getPageDirectory(), LASTUPDATE_FILENAME);
        long lastUpdateTimestamp = 0L;
        if (page.getLastModificationDate() != null) {
            lastUpdateTimestamp = page.getLastModificationDate().getTime();
        }
        FileUtils.writeStringToFile(timestampFile, String.valueOf(lastUpdateTimestamp), false);
    }

    protected PageAPI getPageAPI(final APISession apiSession) throws BonitaException {
        return TenantAPIAccessor.getCustomPageAPI(apiSession);
    }

    protected PermissionAPI getPermissionAPI(final APISession apiSession) throws BonitaException {
        return TenantAPIAccessor.getPermissionAPI(apiSession);
    }

    protected void removePageZipContent(final PageResourceProvider pageResourceProvider) throws IOException {
        FileUtils.deleteDirectory(pageResourceProvider.getPageDirectory());
    }

    public File getGroovyPageFile(final File pageDirectory) {
        return getPageFile(pageDirectory, PAGE_CONTROLLER_FILENAME);
    }

    public File getPageFile(final File pageDirectory, final String fileName) {
        return new File(pageDirectory, fileName);
    }

    protected long getPageLastUpdateDateFromEngine(final APISession apiSession,
                                                   final PageResourceProvider pageResourceProvider) throws BonitaException {
        try {
            final PageAPI pageAPI = getPageAPI(apiSession);
            final Date lastUpdateDate = pageResourceProvider.getPage(pageAPI).getLastModificationDate();
            if (lastUpdateDate != null) {
                return lastUpdateDate.getTime();
            }
        } catch (final PageNotFoundException e) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Unable to find the page " + pageResourceProvider);
            }
        }
        return 0L;
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

    public PageResourceProvider getPageResourceProvider(final Page page) {
        return new PageResourceProviderImpl(page);
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
        Files.write(Paths.get(pageDirectory.getPath(), LASTUPDATE_FILENAME), String.valueOf(lastUpdateTimestamp).getBytes());
    }

}
