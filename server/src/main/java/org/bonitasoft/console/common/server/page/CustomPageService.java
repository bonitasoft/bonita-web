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

import groovy.lang.GroovyClassLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import org.apache.commons.io.IOUtils;
import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.bonitasoft.console.common.server.preferences.properties.CompoundPermissionsMapping;
import org.bonitasoft.console.common.server.preferences.properties.PropertiesFactory;
import org.bonitasoft.console.common.server.preferences.properties.ResourcesPermissionsMapping;
import org.bonitasoft.console.common.server.preferences.properties.SimpleProperties;
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
import org.codehaus.groovy.control.CompilationFailedException;

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

    private static final Map<String, File> PAGES_LIB_DIRECTORIES = new HashMap<>();

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
        return buildPageClassloader(apiSession, pageResourceProvider);
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
    public Class<PageController> registerPage(final GroovyClassLoader pageClassLoader, final PageResourceProvider pageResourceProvider)
            throws CompilationFailedException, IOException {
        final File pageControllerFile = getGroovyPageFile(pageResourceProvider.getPageDirectory());
        return pageClassLoader.parseClass(pageControllerFile);
    }

    public Class<RestApiController> registerRestApiPage(final GroovyClassLoader pageClassLoader, final PageResourceProvider pageResourceProvider, final String classFileName)
            throws CompilationFailedException, IOException {
        final File PageControllerFile = getPageFile(pageResourceProvider.getPageDirectory(), classFileName);
        return pageClassLoader.parseClass(PageControllerFile);
    }

    public void verifyPageClass(final File tempPageDirectory) throws IOException, CompilationFailedException {
        final File pageControllerFile = getPageFile(tempPageDirectory, PAGE_CONTROLLER_FILENAME);
        if (pageControllerFile.exists()) {
            final GroovyClassLoader pageClassLoader = new GroovyClassLoader();
            final File customPageLibDirectory = getCustomPageLibDirectory(tempPageDirectory);
            if (customPageLibDirectory.exists()) {
                addLibsToClassPath(customPageLibDirectory, pageClassLoader);
            }
            pageClassLoader.parseClass(pageControllerFile);
        }
    }

    public PageController loadPage(final Class<PageController> pageClass) throws InstantiationException, IllegalAccessException {
        return pageClass.newInstance();
    }

    public RestApiController loadRestApiPage(final Class<RestApiController> restApiControllerClass) throws InstantiationException, IllegalAccessException {
        return restApiControllerClass.newInstance();
    }

    public void removePage(final APISession apiSession, final String pageName) throws IOException {
        PAGES_CLASSLOADERS.remove(pageName);
        final PageResourceProvider pageResourceProvider = new PageResourceProvider(pageName, apiSession.getTenantId());
        removePageZipContent(apiSession, pageResourceProvider);
        removePageLibTempFolder(pageName);
    }

    protected void removePageLibTempFolder(final String pageName) {
        final File libTempFolder = PAGES_LIB_DIRECTORIES.get(pageName);
        if (libTempFolder != null) {
            try {
                FileUtils.deleteDirectory(libTempFolder);
            } catch (final IOException e) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, "The custom page temporary lib directory " + libTempFolder.getPath()
                            + " cannot be deleted. This is likely to be due to a JDK bug on Windows. You can safely delete it after a server restart.");
                }
            }
        }
    }

    protected void retrievePageZipContent(final APISession apiSession, final String pageName) throws BonitaException, IOException {
        final PageResourceProvider pageResourceProvider = new PageResourceProvider(pageName, apiSession.getTenantId());
        retrievePageZipContent(apiSession, pageResourceProvider);
    }

    protected GroovyClassLoader buildPageClassloader(final APISession apiSession, final PageResourceProvider pageResourceProvider)
            throws CompilationFailedException, IOException {
        GroovyClassLoader pageClassLoader = PAGES_CLASSLOADERS.get(pageResourceProvider.getFullPageName());
        if (pageClassLoader == null || PropertiesFactory.getConsoleProperties(apiSession.getTenantId()).isPageInDebugMode()) {
            final File customPageLibDirectory = getCustomPageLibDirectory(pageResourceProvider.getPageDirectory());
            final ClassLoader parentClassLoader;
            if (customPageLibDirectory.exists()) {
                File libTempFolder = null;
                libTempFolder = getPageFile(WebBonitaConstantsUtils.getInstance(apiSession.getTenantId()).getTempFolder(), pageResourceProvider.getPageName()
                        + Long.toString(new Date().getTime()));
                // FileUtils.copyDirectory(customPageLibDirectory, libTempFolder);
                removePageLibTempFolder(pageResourceProvider.getPageName());
                PAGES_LIB_DIRECTORIES.put(pageResourceProvider.getPageName(), libTempFolder);
                parentClassLoader = getParentClassloader(pageResourceProvider.getPageName(), customPageLibDirectory, libTempFolder.getAbsolutePath());
            } else {
                parentClassLoader = Thread.currentThread().getContextClassLoader();
            }
            pageClassLoader = new GroovyClassLoader(parentClassLoader);
            PAGES_CLASSLOADERS.put(pageResourceProvider.getPageName(), pageClassLoader);
        }
        return pageClassLoader;
    }

    protected ClassLoader getParentClassloader(final String pageName, final File customPageLibDirectory, final String libTempFolder) throws IOException {
        return new ChildFirstClassLoader(loadLibraries(customPageLibDirectory), pageName, libTempFolder, Thread.currentThread().getContextClassLoader());
    }

    private static Map<String, byte[]> loadLibraries(final File clientLibDirectory) throws IOException {

        if (clientLibDirectory != null && clientLibDirectory.exists()) {
            final File[] libFiles = clientLibDirectory.listFiles();
            final Map<String, byte[]> result = new HashMap<String, byte[]>(libFiles.length);
            for (int i = 0; i < libFiles.length; i++) {

                final File currentFile = libFiles[i];
                final String name = currentFile.getName();

                InputStream currentInputStream = null;
                try {
                    currentInputStream = new FileInputStream(currentFile);
                    final byte[] content = IOUtils.toByteArray(currentInputStream);

                    result.put(name, content);
                } finally {
                    if (currentInputStream != null) {
                        currentInputStream.close();
                    }
                }
            }
            return result;
        } else {
            return Collections.emptyMap();
        }
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
        FileUtils.writeByteArrayToFile(pageResourceProvider.getTempPageFile(), pageContent);
        UnzipUtil.unzip(pageResourceProvider.getTempPageFile(), pageResourceProvider.getPageDirectory().getPath(), true);
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

    public Set<String> getCustomPagePermissions(final Properties properties, final ResourcesPermissionsMapping resourcesPermissionsMapping,
            final boolean alsoReturnResourcesNotFound) {
        final SimpleProperties pageProperties = new SimpleProperties(properties);
        return getCustomPagePermissions(pageProperties, resourcesPermissionsMapping, alsoReturnResourcesNotFound);
    }

    protected Set<String> getCustomPagePermissions(final SimpleProperties pageProperties, final ResourcesPermissionsMapping resourcesPermissionsMapping,
            final boolean alsoReturnResourcesNotFound) {
        final Set<String> pageRestResources = new HashSet<String>(pageProperties.getPropertyAsSet(RESOURCES_PROPERTY));
        // pageRestResources.addAll(Arrays.asList(GET_SYSTEM_SESSION, GET_PORTAL_PROFILE, GET_IDENTITY_USER));
        final Set<String> permissions = new HashSet<String>();
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
        final SimpleProperties pageProperties = new SimpleProperties(file);
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

    public void removeRestApiExtensionPermissions(final ResourcesPermissionsMapping resourcesPermissionsMapping, final PageResourceProvider pageResourceProvider, final APISession apiSession) throws IOException, BonitaException {
        ensurePageFolderIsUpToDate(apiSession,pageResourceProvider);
        final Map<String, String> permissionsMapping = getPermissionMapping(pageResourceProvider);
        for (final String key : permissionsMapping.keySet()) {
            resourcesPermissionsMapping.removeProperty(key);
        }

    }

    public void addRestApiExtensionPermissions(final ResourcesPermissionsMapping resourcesPermissionsMapping, final PageResourceProvider pageResourceProvider, final APISession apiSession) throws IOException, BonitaException {
        ensurePageFolderIsUpToDate(apiSession,pageResourceProvider);
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
        final SimpleProperties pageProperties = new SimpleProperties(pagePropertyFile);
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
        return new PageResourceProvider(page,tenantId);
    }
}
