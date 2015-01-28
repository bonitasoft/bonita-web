/*******************************************************************************
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
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
    private static Logger LOGGER = Logger.getLogger(CustomPageService.class.getName());

    private static final String PAGE_LIB_DIRECTORY = "lib";

    private static final String PAGE_CONTROLLER_FILENAME = "Index.groovy";

    private static final String LASTUPDATE_FILENAME = ".lastupdate";

    private static final Map<String, GroovyClassLoader> PAGES_CLASSLOADERS = new HashMap<String, GroovyClassLoader>();

    private static final Map<String, File> PAGES_LIB_DIRECTORIES = new HashMap<String, File>();

    public static final String RESOURCES_PROPERTY = "resources";

    public static final String NAME_PROPERTY = "name";

    public GroovyClassLoader getPageClassloader(final APISession apiSession, final String pageName, final PageResourceProvider pageResourceProvider)
            throws IOException, CompilationFailedException, BonitaException {
        final File pageFolder = pageResourceProvider.getPageDirectory();
        if (!pageResourceProvider.getPageDirectory().exists()) {
            retrievePageZipContent(apiSession, pageName, pageResourceProvider);
        } else {
            final File timestampFile = new File(pageFolder, LASTUPDATE_FILENAME);
            final long lastUpdateTimestamp = getPageLastUpdateDateFromEngine(apiSession, pageName);
            if (timestampFile.exists()) {
                final String timestampString = FileUtils.readFileToString(timestampFile);
                final long timestamp = Long.parseLong(timestampString);
                if (lastUpdateTimestamp != timestamp) {
                    removePage(apiSession, pageName);
                    retrievePageZipContent(apiSession, pageName, pageResourceProvider);
                }
            } else {
                FileUtils.writeStringToFile(timestampFile, String.valueOf(lastUpdateTimestamp), false);
            }
        }
        return buildPageClassloader(apiSession, pageName, pageResourceProvider);
    }

    @SuppressWarnings("unchecked")
    public Class<PageController> registerPage(final GroovyClassLoader pageClassLoader, final PageResourceProvider pageResourceProvider)
            throws CompilationFailedException, IOException {
        final File PageControllerFile = getCustomPageFile(pageResourceProvider.getPageDirectory());
        return pageClassLoader.parseClass(PageControllerFile);
    }

    public void verifyPageClass(final String pageName, final File tempPageDirectory) throws IOException, CompilationFailedException {
        final File pageControllerFile = new File(tempPageDirectory, PAGE_CONTROLLER_FILENAME);
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
        retrievePageZipContent(apiSession, pageName, pageResourceProvider);
    }

    protected GroovyClassLoader buildPageClassloader(final APISession apiSession, final String pageName, final PageResourceProvider pageResourceProvider)
            throws CompilationFailedException, IOException {
        GroovyClassLoader pageClassLoader = PAGES_CLASSLOADERS.get(pageName);
        if (pageClassLoader == null || PropertiesFactory.getConsoleProperties(apiSession.getTenantId()).isPageInDebugMode()) {
            final File customPageLibDirectory = getCustomPageLibDirectory(pageResourceProvider.getPageDirectory());
            final ClassLoader parentClassLoader;
            if (customPageLibDirectory.exists()) {
                File libTempFolder = null;
                libTempFolder = new File(WebBonitaConstantsUtils.getInstance(apiSession.getTenantId()).getTempFolder(), pageName
                        + Long.toString(new Date().getTime()));
                // FileUtils.copyDirectory(customPageLibDirectory, libTempFolder);
                removePageLibTempFolder(pageName);
                PAGES_LIB_DIRECTORIES.put(pageName, libTempFolder);
                parentClassLoader = getParentClassloader(pageName, customPageLibDirectory, libTempFolder.getAbsolutePath());
            } else {
                parentClassLoader = Thread.currentThread().getContextClassLoader();
            }
            pageClassLoader = new GroovyClassLoader(parentClassLoader);
            PAGES_CLASSLOADERS.put(pageName, pageClassLoader);
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

    protected void retrievePageZipContent(final APISession apiSession, final String pageName, final PageResourceProvider pageResourceProvider)
            throws BonitaException, IOException {
        final PageAPI pageAPI = getPageAPI(apiSession);
        // retrieve page zip content from engine and cache it
        final Page page = pageAPI.getPageByName(pageName);
        final byte[] pageContent = pageAPI.getPageContent(page.getId());
        FileUtils.writeByteArrayToFile(pageResourceProvider.getTempPageFile(), pageContent);
        UnzipUtil.unzip(pageResourceProvider.getTempPageFile(), pageResourceProvider.getPageDirectory().getPath(), true);
        final File timestampFile = new File(pageResourceProvider.getPageDirectory(), LASTUPDATE_FILENAME);
        long lastUpdateTimestamp = 0L;
        if (page.getLastModificationDate() != null) {
            lastUpdateTimestamp = page.getLastModificationDate().getTime();
        }
        FileUtils.writeStringToFile(timestampFile, String.valueOf(lastUpdateTimestamp), false);
    }

    protected PageAPI getPageAPI(final APISession apiSession) throws BonitaException {
        return TenantAPIAccessor.getPageAPI(apiSession);
    }

    protected void removePageZipContent(final APISession apiSession, final PageResourceProvider pageResourceProvider) throws IOException {
        FileUtils.deleteDirectory(pageResourceProvider.getPageDirectory());
    }

    protected File getCustomPageFile(final File pageDirectory) {
        final File pageControllerFile = new File(pageDirectory, PAGE_CONTROLLER_FILENAME);
        if (pageControllerFile.exists()) {
            return pageControllerFile;
        } else {
            return new File(pageDirectory.getParent(), PAGE_CONTROLLER_FILENAME);
        }
    }

    protected File getCustomPageLibDirectory(final File pageDirectory) {
        return new File(pageDirectory, PAGE_LIB_DIRECTORY);
    }

    protected long getPageLastUpdateDateFromEngine(final APISession apiSession, final String pageName) throws BonitaException {
        final PageAPI pageAPI = getPageAPI(apiSession);
        try {
            final Date lastUpdateDate = pageAPI.getPageByName(pageName).getLastModificationDate();
            if (lastUpdateDate != null) {
                return lastUpdateDate.getTime();
            }
        } catch (final PageNotFoundException e) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Unable to find the page " + pageName);
            }
        }
        return 0L;
    }

    public Properties getPageProperties(final APISession apiSession, final byte[] zipContent, final boolean checkIfItAlreadyExists)
            throws InvalidPageZipMissingPropertiesException, InvalidPageZipMissingIndexException, InvalidPageZipInconsistentException,
            InvalidPageZipMissingAPropertyException, InvalidPageTokenException, AlreadyExistsException, BonitaException {
        final PageAPI pageAPI = getPageAPI(apiSession);
        return pageAPI.getPageProperties(zipContent, checkIfItAlreadyExists);
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

}
