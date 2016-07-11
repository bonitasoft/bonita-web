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

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.bonitasoft.console.common.server.page.extension.PageResourceProviderImpl;
import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.bonitasoft.console.common.server.preferences.properties.CompoundPermissionsMapping;
import org.bonitasoft.console.common.server.preferences.properties.ConsoleProperties;
import org.bonitasoft.console.common.server.preferences.properties.PropertiesFactory;
import org.bonitasoft.console.common.server.preferences.properties.PropertiesWithSet;
import org.bonitasoft.console.common.server.preferences.properties.ResourcesPermissionsMapping;
import org.bonitasoft.console.common.server.utils.UnzipUtil;
import org.bonitasoft.engine.api.PageAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.exception.AlreadyExistsException;
import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.exception.InvalidPageTokenException;
import org.bonitasoft.engine.exception.InvalidPageZipInconsistentException;
import org.bonitasoft.engine.exception.InvalidPageZipMissingAPropertyException;
import org.bonitasoft.engine.exception.InvalidPageZipMissingIndexException;
import org.bonitasoft.engine.exception.InvalidPageZipMissingPropertiesException;
import org.bonitasoft.engine.page.ContentType;
import org.bonitasoft.engine.page.Page;
import org.bonitasoft.engine.page.PageNotFoundException;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.extension.page.PageResourceProvider;
import org.codehaus.groovy.control.CompilationFailedException;

import groovy.lang.GroovyClassLoader;

/**
 * @author Anthony Birembaut, Fabio Lombardi
 */
public class CustomPageService {

    public static final String GET_SYSTEM_SESSION = "GET|system/session";
    public static final String GET_PORTAL_PROFILE = "GET|portal/profile";
    public static final String GET_IDENTITY_USER = "GET|identity/user";
    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(CustomPageService.class.getName());

    private static final String PAGE_LIB_DIRECTORY = "lib";

    public static final String PAGE_CONTROLLER_FILENAME = "Index.groovy";

    public static final String PAGE_INDEX_NAME = "index";

    public static final String PAGE_INDEX_FILENAME = "index.html";

    private static final String LASTUPDATE_FILENAME = ".lastupdate";

    private static final Map<String, GroovyClassLoader> PAGES_CLASSLOADERS = new HashMap<>();

    public static final String RESOURCES_PROPERTY = "resources";
    public static final String PROPERTY_CONTENT_TYPE = "contentType";
    public static final String PROPERTY_API_EXTENSIONS = "apiExtensions";
    public static final String PROPERTY_METHOD_MASK = "%s.method";
    public static final String PROPERTY_PATH_TEMPLATE_MASK = "%s.pathTemplate";
    public static final String PROPERTY_PERMISSIONS_MASK = "%s.permissions";
    public static final String RESOURCE_PERMISSION_KEY_MASK = "%s|extension/%s";
    public static final String RESOURCE_PERMISSION_VALUE = "[%s]";
    public static final String EXTENSION_SEPARATOR = ",";

    public static final String NAME_PROPERTY = "name";

    public GroovyClassLoader getPageClassloader(final APISession apiSession, final PageResourceProvider pageResourceProvider)
            throws IOException, CompilationFailedException, BonitaException {
        return buildPageClassloader(apiSession, pageResourceProvider.getFullPageName(), pageResourceProvider.getPageDirectory());
    }

    public void ensurePageFolderIsPresent(final APISession apiSession, final PageResourceProvider pageResourceProvider) throws BonitaException, IOException {
        if (!pageResourceProvider.getPageDirectory().exists()) {
            retrievePageZipContent(apiSession, pageResourceProvider);
        }
    }

    public void ensurePageFolderIsUpToDate(final APISession apiSession, final PageResourceProvider pageResourceProvider) throws BonitaException, IOException {
        final File pageFolder = pageResourceProvider.getPageDirectory();
        if (!pageResourceProvider.getPageDirectory().exists()) {
            retrievePageZipContent(apiSession, pageResourceProvider);
        } else {
            final File timestampFile = getPageFile(pageFolder, LASTUPDATE_FILENAME);
            final long lastUpdateTimestamp = getPageLastUpdateDateFromEngine(apiSession, pageResourceProvider);
            if (timestampFile.exists()) {
                final String timestampString = FileUtils.readFileToString(timestampFile);
                final long timestamp = Long.parseLong(timestampString);
                if (lastUpdateTimestamp != timestamp) {
                    removePage(apiSession, pageResourceProvider.getPageName());
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

    public Class<?> registerRestApiPage(final GroovyClassLoader pageClassLoader, final File restApiControllerFile)
            throws CompilationFailedException, IOException {
        return pageClassLoader.parseClass(restApiControllerFile);
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

    public PageController loadPage(final Class<PageController> pageClass) throws InstantiationException, IllegalAccessException {
        return pageClass.newInstance();
    }

    public RestApiController loadRestApiPage(final Class<RestApiController> restApiControllerClass) throws InstantiationException, IllegalAccessException {
        return restApiControllerClass.newInstance();
    }

    public void removePage(final APISession apiSession, final String pageName) throws IOException {
        closeClassloader(pageName);
        final PageResourceProvider pageResourceProvider = new PageResourceProviderImpl(pageName, apiSession.getTenantId());
        removePageZipContent(apiSession, pageResourceProvider);
        CustomPageDependenciesResolver.removePageLibTempFolder(pageName);
    }

    public void removePage(final APISession apiSession, final Page page) throws IOException {
        final PageResourceProvider pageResourceProvider = new PageResourceProviderImpl(page, apiSession.getTenantId());
        final String pageName = pageResourceProvider.getFullPageName();
        closeClassloader(pageName);
        removePageZipContent(apiSession, pageResourceProvider);
        CustomPageDependenciesResolver.removePageLibTempFolder(pageName);
    }

    private static void closeClassloader(final String pageName) throws IOException {
        final GroovyClassLoader classloader = PAGES_CLASSLOADERS.remove(pageName);
        if (classloader != null) {
            classloader.clearCache();
            classloader.close();
        }
    }

    protected void retrievePageZipContent(final APISession apiSession, final String pageName) throws BonitaException, IOException {
        final PageResourceProviderImpl pageResourceProvider = new PageResourceProviderImpl(pageName, apiSession.getTenantId());
        retrievePageZipContent(apiSession, pageResourceProvider);
    }

    protected GroovyClassLoader buildPageClassloader(final APISession apiSession, final String pageName, final File pageDirectory)
            throws CompilationFailedException, IOException {
        GroovyClassLoader pageClassLoader = PAGES_CLASSLOADERS.get(pageName);
        final BDMClientDependenciesResolver bdmDependenciesResolver = new BDMClientDependenciesResolver(apiSession);
        if (pageClassLoader == null
                || getConsoleProperties(apiSession).isPageInDebugMode()
                || isOutdated(pageClassLoader, bdmDependenciesResolver)) {
            pageClassLoader = new GroovyClassLoader(getParentClassloader(pageName,
                    new CustomPageDependenciesResolver(pageName, pageDirectory, getWebBonitaConstantsUtils(apiSession)),
                    bdmDependenciesResolver));
            pageClassLoader.addClasspath(pageDirectory.getPath());
            PAGES_CLASSLOADERS.put(pageName, pageClassLoader);
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

    protected WebBonitaConstantsUtils getWebBonitaConstantsUtils(final APISession apiSession) {
        return WebBonitaConstantsUtils.getInstance(apiSession.getTenantId());
    }

    protected ClassLoader getParentClassloader(final String pageName,
            final CustomPageDependenciesResolver customPageDependenciesResolver,
            final BDMClientDependenciesResolver bdmDependenciesResolver) throws IOException {
        final CustomPageChildFirstClassLoader classLoader = new CustomPageChildFirstClassLoader(pageName, customPageDependenciesResolver,
                bdmDependenciesResolver, Thread.currentThread().getContextClassLoader());
        classLoader.addCustomPageResources();
        return classLoader;
    }

    protected void addLibsToClassPath(final File customPageLibDirectory, final GroovyClassLoader groovyClassLoader) {
        final File[] libFiles = customPageLibDirectory.listFiles();
        for (int i = 0; i < libFiles.length; i++) {
            final File libFile = libFiles[i];
            groovyClassLoader.addClasspath(libFile.getPath());
        }
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

    protected void removePageZipContent(final APISession apiSession, final PageResourceProvider pageResourceProvider) throws IOException {
        FileUtils.deleteDirectory(pageResourceProvider.getPageDirectory());
    }

    public File getGroovyPageFile(final File pageDirectory) {
        return getPageFile(pageDirectory, PAGE_CONTROLLER_FILENAME);
    }

    public File getPageFile(final File pageDirectory, final String fileName) {
        return new File(pageDirectory, fileName);
    }

    protected File getCustomPageLibDirectory(final File pageDirectory) {
        return getPageFile(pageDirectory, PAGE_LIB_DIRECTORY);
    }

    protected long getPageLastUpdateDateFromEngine(final APISession apiSession, final PageResourceProvider pageResourceProvider) throws BonitaException {
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

    public Properties getPageProperties(final APISession apiSession, final byte[] zipContent, final boolean checkIfItAlreadyExists,
            final Long processDefinitionId) throws InvalidPageZipMissingPropertiesException, InvalidPageZipMissingIndexException,
                    InvalidPageZipInconsistentException,
                    InvalidPageZipMissingAPropertyException, InvalidPageTokenException, AlreadyExistsException, BonitaException {
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
                    throw new AlreadyExistsException("A page with name " + pageName + " already exists for the process " + processDefinitionId);
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

    public Set<String> getCustomPagePermissions(final Properties pageProperties, final ResourcesPermissionsMapping resourcesPermissionsMapping,
            final boolean alsoReturnResourcesNotFound) {
        PropertiesWithSet pagePropertiesWithSet = new PropertiesWithSet(pageProperties);
        final Set<String> pageRestResources = new HashSet<>(pagePropertiesWithSet.getPropertyAsSet(RESOURCES_PROPERTY));
        // pageRestResources.addAll(Arrays.asList(GET_SYSTEM_SESSION, GET_PORTAL_PROFILE, GET_IDENTITY_USER));
        final Set<String> permissions = new HashSet<>();
        for (final String pageRestResource : pageRestResources) {
            final Set<String> resourcePermissions = resourcesPermissionsMapping.getPropertyAsSet(pageRestResource);
            if (Collections.emptySet().equals(resourcePermissions)) {
                if (alsoReturnResourcesNotFound) {
                    permissions.add("<" + pageRestResource + ">");
                } else {
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER.log(Level.WARNING, "Error while getting resources permissions. Unknown resource: " + pageRestResource
                                + " defined in page.properties");
                    }
                }
            }
            permissions.addAll(resourcePermissions);
        }
        return permissions;
    }

    public Set<String> getCustomPagePermissions(final File file, final ResourcesPermissionsMapping resourcesPermissionsMapping,
            final boolean alsoReturnResourcesNotFound) {
        final PropertiesWithSet pageProperties = new PropertiesWithSet(file);
        return getCustomPagePermissions(pageProperties, resourcesPermissionsMapping, alsoReturnResourcesNotFound);
    }

    public void addPermissionsToCompoundPermissions(final String pageName, final Set<String> customPagePermissions,
            final CompoundPermissionsMapping compoundPermissionsMapping, final ResourcesPermissionsMapping resourcesPermissionsMapping) throws IOException {
        customPagePermissions.addAll(resourcesPermissionsMapping.getPropertyAsSet(GET_SYSTEM_SESSION));
        customPagePermissions.addAll(resourcesPermissionsMapping.getPropertyAsSet(GET_PORTAL_PROFILE));
        customPagePermissions.addAll(resourcesPermissionsMapping.getPropertyAsSet(GET_IDENTITY_USER));
        compoundPermissionsMapping.setPropertyAsSet(pageName, customPagePermissions);
    }

    public Page getPage(final APISession apiSession, final String pageName, final long processDefinitionId) throws BonitaException {
        return getPageAPI(apiSession).getPageByNameAndProcessDefinitionId(pageName, processDefinitionId);
    }

    public Page getPage(final APISession apiSession, final long pageId) throws BonitaException {
        return getPageAPI(apiSession).getPage(pageId);
    }

    public void removeRestApiExtensionPermissions(final ResourcesPermissionsMapping resourcesPermissionsMapping,
            final PageResourceProvider pageResourceProvider, final APISession apiSession) throws IOException, BonitaException {
        ensurePageFolderIsUpToDate(apiSession, pageResourceProvider);
        final Map<String, String> permissionsMapping = getPermissionMapping(pageResourceProvider);
        for (final String key : permissionsMapping.keySet()) {
            resourcesPermissionsMapping.removeProperty(key);
        }

    }

    public void addRestApiExtensionPermissions(final ResourcesPermissionsMapping resourcesPermissionsMapping,
            final PageResourceProvider pageResourceProvider,
            final APISession apiSession) throws IOException, BonitaException {
        ensurePageFolderIsUpToDate(apiSession, pageResourceProvider);
        final Map<String, String> permissionsMapping = getPermissionMapping(pageResourceProvider);
        for (final String key : permissionsMapping.keySet()) {
            resourcesPermissionsMapping.setProperty(key, permissionsMapping.get(key));
        }
    }

    private Map<String, String> getPermissionMapping(final PageResourceProvider pageResourceProvider) {
        Map<String, String> permissionsMapping;
        final File pageProperties = pageResourceProvider.getResourceAsFile("page.properties");
        permissionsMapping = getApiExtensionResourcesPermissionsMapping(pageProperties);
        return permissionsMapping;
    }

    private Map<String, String> getApiExtensionResourcesPermissionsMapping(final File pagePropertyFile) {
        final Properties pageProperties = new PropertiesWithSet(pagePropertyFile);
        final Map<String, String> permissionsMap = new HashMap<>();
        if (ContentType.API_EXTENSION.equals(pageProperties.getProperty(PROPERTY_CONTENT_TYPE))) {
            final String apiExtensionList = pageProperties.getProperty(PROPERTY_API_EXTENSIONS);
            final String[] apiExtensions = apiExtensionList.split(EXTENSION_SEPARATOR);
            for (final String apiExtension : apiExtensions) {
                final String method = pageProperties.getProperty(String.format(PROPERTY_METHOD_MASK, apiExtension.trim()));
                final String pathTemplate = pageProperties.getProperty(String.format(PROPERTY_PATH_TEMPLATE_MASK, apiExtension.trim()));
                final String permissions = pageProperties.getProperty(String.format(PROPERTY_PERMISSIONS_MASK, apiExtension.trim()));
                permissionsMap.put(String.format(RESOURCE_PERMISSION_KEY_MASK, method, pathTemplate), String.format(RESOURCE_PERMISSION_VALUE, permissions));
            }
        }
        return permissionsMap;
    }

    public PageResourceProvider getPageResourceProvider(final Page page, final long tenantId) {
        return new PageResourceProviderImpl(page, tenantId);
    }

    public static void clearCachedClassloaders() throws IOException {
        for (final String page : PAGES_CLASSLOADERS.keySet()) {
            closeClassloader(page);
        }

    }
}
