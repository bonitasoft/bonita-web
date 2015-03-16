/**
 * Copyright (C) 2012 BonitaSoft S.A.
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
package org.bonitasoft.console.common.server.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.bonitasoft.console.common.server.preferences.properties.ProcessIdentifier;
import org.bonitasoft.console.common.server.preferences.properties.SecurityProperties;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.api.TenantAdministrationAPI;
import org.bonitasoft.engine.bpm.process.ProcessDefinition;
import org.bonitasoft.engine.bpm.process.ProcessDefinitionNotFoundException;
import org.bonitasoft.engine.business.data.BusinessDataRepositoryException;
import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.bonitasoft.engine.exception.RetrieveException;
import org.bonitasoft.engine.exception.ServerAPIException;
import org.bonitasoft.engine.exception.UnknownAPITypeException;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.session.InvalidSessionException;

/**
 * @author Anthony Birembaut
 */
public class FormsResourcesUtils {

    /**
     * The forms directory name in the bar
     */
    public final static String FORMS_DIRECTORY_IN_BAR = "resources/forms";

    /**
     * The forms lib directory name in the bar
     */
    public final static String LIB_DIRECTORY_IN_BAR = "lib";

    /**
     * The forms validators directory name in the bar
     */
    public final static String VALIDATORS_DIRECTORY_IN_BAR = "validators";

    /**
     * Process UUID separator
     */
    public final static String UUID_SEPARATOR = "--";

    /**
     * A map used to store the classloaders that are used to load some libraries extracted from the business archive
     */
    private final static Map<Long, ClassLoader> PROCESS_CLASSLOADERS = new HashMap<Long, ClassLoader>();

    /**
     * Util class allowing to work with the BPM engine API
     */
    protected static BPMEngineAPIUtil bpmEngineAPIUtil = new BPMEngineAPIUtil();

    /**
     * Logger
     */
    protected static Logger LOGGER = Logger.getLogger(FormsResourcesUtils.class.getName());

    /**
     * Retrieve the web resources from the business archive and store them in a local directory
     *
     * @param session
     *            the engine API session
     * @param processDefinitionID
     *            the process definition ID
     * @param processDeployementDate
     *            the process deployement date
     * @throws java.io.IOException
     * @throws org.bonitasoft.engine.bpm.process.ProcessDefinitionNotFoundException
     * @throws org.bonitasoft.engine.session.InvalidSessionException
     * @throws org.bonitasoft.engine.exception.RetrieveException
     */
    public static synchronized void retrieveApplicationFiles(final APISession session, final long processDefinitionID, final Date processDeployementDate)
            throws IOException, ProcessDefinitionNotFoundException, InvalidSessionException, RetrieveException, BPMEngineException {

        final ProcessAccessor process = new ProcessAccessor(bpmEngineAPIUtil.getProcessAPI(session));
        final File formsDir = getApplicationResourceDir(session, processDefinitionID, processDeployementDate);
        if (!formsDir.exists()) {
            formsDir.mkdirs();
        }
        final Map<String, byte[]> formsResources = process.getResources(processDefinitionID, FORMS_DIRECTORY_IN_BAR + "/.*");
        for (final Entry<String, byte[]> formResource : formsResources.entrySet()) {
            final String filePath = formResource.getKey().substring(FORMS_DIRECTORY_IN_BAR.length() + 1);
            final byte[] fileContent = formResource.getValue();
            final File formResourceFile = new File(formsDir.getPath() + File.separator + filePath);
            final File formResourceFileDir = formResourceFile.getParentFile();
            if (!formResourceFileDir.exists()) {
                formResourceFileDir.mkdirs();
            }
            formResourceFile.createNewFile();
            if (fileContent != null) {
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(formResourceFile);
                    fos.write(fileContent);
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (final IOException e) {
                            if (LOGGER.isLoggable(Level.WARNING)) {
                                LOGGER.log(Level.WARNING, "unable to close file output stream for business archive resource " + formResourceFile.getPath(), e);
                            }
                        }
                    }
                }
            }
        }

        final ProcessDefinition definition = process.getDefinition(processDefinitionID);
        SecurityProperties.cleanProcessConfig(session.getTenantId(),
                new ProcessIdentifier(definition.getName(), definition.getVersion()));

        final File processApplicationsResourcesDir = FormsResourcesUtils.getApplicationResourceDir(session, processDefinitionID, processDeployementDate);
        final ClassLoader processClassLoader = createProcessClassloader(processDefinitionID, processApplicationsResourcesDir);
        PROCESS_CLASSLOADERS.put(processDefinitionID, processClassLoader);
    }

    /**
     * Create a classloader for the process
     *
     * @param processDefinitionID
     *            the process definition ID
     * @param processApplicationsResourcesDir
     *            the process application resources directory
     * @return a Classloader
     * @throws java.io.IOException
     */
    private static ClassLoader createProcessClassloader(final long processDefinitionID, final File processApplicationsResourcesDir) throws IOException {
        ClassLoader processClassLoader = null;
        try {
            final URL[] librariesURLs = getLibrariesURLs(processApplicationsResourcesDir);
            if (librariesURLs.length > 0) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE, "Creating the classloader for process " + processDefinitionID);
                }
                processClassLoader = new URLClassLoader(librariesURLs, Thread.currentThread().getContextClassLoader());
            }
        } catch (final IOException e) {
            final String message = "Unable to create the class loader for the process's libraries";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, message, e);
            }
            throw new IOException(message);
        }
        return processClassLoader;
    }

    /**
     * Get the URLs of the validators' jar and their dependencies
     *
     * @param processApplicationsResourcesDir
     *            the process application resources directory
     * @return an array of URL
     * @throws java.io.IOException
     */
    private static URL[] getLibrariesURLs(final File processApplicationsResourcesDir) throws IOException {
        final List<URL> urls = new ArrayList<URL>();
        final File libDirectory = new File(processApplicationsResourcesDir, FormsResourcesUtils.LIB_DIRECTORY_IN_BAR + File.separator);
        if (libDirectory.exists()) {
            final File[] libFiles = libDirectory.listFiles();
            for (int i = 0; i < libFiles.length; i++) {
                urls.add(libFiles[i].toURI().toURL());
            }
        }
        final File validatorsDirectory = new File(processApplicationsResourcesDir, FormsResourcesUtils.VALIDATORS_DIRECTORY_IN_BAR + File.separator);
        if (validatorsDirectory.exists()) {
            final File[] validatorsFiles = validatorsDirectory.listFiles();
            for (int i = 0; i < validatorsFiles.length; i++) {
                urls.add(validatorsFiles[i].toURI().toURL());
            }
        } else {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "The validators directory doesn't exists.");
            }
        }
        final URL[] urlArray = new URL[urls.size()];
        urls.toArray(urlArray);
        return urlArray;
    }

    protected static ClassLoader setCorrectHierarchicalClassLoader(ClassLoader processClassLoader, final ClassLoader parentClassLoader) {
        if (processClassLoader == null) {
            processClassLoader = parentClassLoader;
        }
        return processClassLoader;
    }

    /**
     * Retrieve the class loader associated with the process or create it if there is no classloader associated with this process yet
     *
     * @param session
     *            the API session
     * @param processDefinitionID
     *            the process definition ID
     * @return a {@link ClassLoader}, null if the process classloader doesn't exists and couldn't be created
     */
    public ClassLoader getProcessClassLoader(final APISession session, final long processDefinitionID) {
        final File currentBDMFolder = FormsResourcesUtils.getCurrentBDMFolder(session);
        if (PROCESS_CLASSLOADERS.containsKey(processDefinitionID)) {
            // CHECK BDM VERSION AND SEE IF CLASSLOADER IS UP TO DATE
            // IF NO RECREATE THE CLASSLOADER
            if (isClassloaderUpToDateWithCurrentBdm(currentBDMFolder)) {
                return PROCESS_CLASSLOADERS.get(processDefinitionID);
            } else {
                PROCESS_CLASSLOADERS.remove(processDefinitionID);
                cleanBDMFolder(currentBDMFolder);
                FormsResourcesUtils.createAndSaveProcessClassloader(session, processDefinitionID, currentBDMFolder);
                return PROCESS_CLASSLOADERS.get(processDefinitionID);
            }
        }
        FormsResourcesUtils.createAndSaveProcessClassloader(session, processDefinitionID, currentBDMFolder);
        return PROCESS_CLASSLOADERS.get(processDefinitionID);

    }

    protected boolean isClassloaderUpToDateWithCurrentBdm(final File currentBDMFolder) {
        return currentBDMFolder == null || currentBDMFolder.exists();
    }

    protected static ClassLoader createProcessClassloader(final APISession session, final long processDefinitionID) {
        ClassLoader processClassLoader = null;
        try {
            final ProcessDefinition processDefinition = bpmEngineAPIUtil.getProcessAPI(session).getProcessDefinition(processDefinitionID);

            final String processPath = WebBonitaConstantsUtils.getInstance(session.getTenantId()).getFormsWorkFolder() + File.separator;
            final File processDir = new File(processPath, processDefinition.getName() + UUID_SEPARATOR + processDefinition.getVersion());
            if (processDir.exists()) {
                final long lastDeployementDate = getLastDeployementDate(processDir);
                final File processApplicationsResourcesDir = new File(processDir, Long.toString(lastDeployementDate));
                processClassLoader = createProcessClassloader(processDefinitionID, processApplicationsResourcesDir);
            }
        } catch (final Exception e) {
            final String message = "Unable to create the class loader for the libraries of process " + processDefinitionID;
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, message, e);
            }
        }
        return processClassLoader;
    }

    private static long getLastDeployementDate(final File processDir) {
        final File[] directories = processDir.listFiles(new FileFilter() {

            @Override
            public boolean accept(final File pathname) {
                return pathname.isDirectory();
            }
        });
        long lastDeployementDate = 0L;
        for (final File directory : directories) {
            try {
                final long deployementDate = Long.parseLong(directory.getName());
                if (deployementDate > lastDeployementDate) {
                    lastDeployementDate = deployementDate;
                }
            } catch (final Exception e) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING,
                            "Process application resources deployment folder contains a directory that does not match a process deployement timestamp: "
                                    + directory.getName(), e);
                }
            }
        }
        if (lastDeployementDate == 0L) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING,
                        "Process application resources deployment folder contains no directory that match a process deployement timestamp.");
            }
        }
        return lastDeployementDate;
    }

    /**
     * Delete the the web resources directory if it exists
     *
     * @param session
     *            the API session
     * @param processDefinitionID
     *            the process definition ID
     */
    public static synchronized void removeApplicationFiles(final APISession session, final long processDefinitionID) {

        PROCESS_CLASSLOADERS.remove(processDefinitionID);
        try {
            final ProcessAPI processAPI = bpmEngineAPIUtil.getProcessAPI(session);
            final ProcessDefinition processDefinition = processAPI.getProcessDefinition(processDefinitionID);
            final String processUUID = processDefinition.getName() + UUID_SEPARATOR + processDefinition.getVersion();
            final File formsDir = new File(WebBonitaConstantsUtils.getInstance(session.getTenantId()).getFormsWorkFolder(), processUUID);
            final boolean deleted = deleteDirectory(formsDir);
            if (!deleted) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, "unable to delete the web resources directory " + formsDir.getCanonicalPath()
                            + ". You will be able to delete it manually once the JVM will shutdown");
                }
            }
        } catch (final Exception e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "Error while deleting the web resources directory for process " + processDefinitionID, e);
            }
        }
    }

    /**
     * Get the process resource directory
     *
     * @param session
     *            the API session
     * @param processDefinitionID
     * @param processDeployementDate
     * @return
     * @throws java.io.IOException
     * @throws org.bonitasoft.engine.session.InvalidSessionException
     * @throws org.bonitasoft.engine.bpm.process.ProcessDefinitionNotFoundException
     * @throws org.bonitasoft.engine.exception.RetrieveException
     */
    public static File getApplicationResourceDir(final APISession session, final long processDefinitionID, final Date processDeployementDate)
            throws IOException, InvalidSessionException, ProcessDefinitionNotFoundException, RetrieveException, BPMEngineException {
        final ProcessAccessor process = new ProcessAccessor(bpmEngineAPIUtil.getProcessAPI(session));
        final ProcessDefinition processDefinition = process.getDefinition(processDefinitionID);
        final String processUUID = processDefinition.getName() + UUID_SEPARATOR + processDefinition.getVersion();
        return new File(WebBonitaConstantsUtils.getInstance(session.getTenantId()).getFormsWorkFolder(), processUUID + File.separator
                + processDeployementDate.getTime());
    }

    /**
     * Delete a directory and its content
     *
     * @param directory
     *            the directory to delete
     * @return return true if the directory and its content were deleted successfully, false otherwise
     */
    private static boolean deleteDirectory(final File directory) {
        boolean success = true;;
        if (directory.exists()) {
            final File[] files = directory.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    success &= deleteDirectory(files[i]);
                } else {
                    success &= files[i].delete();
                }
            }
            success &= directory.delete();
        }
        return success;
    }

    protected static File getCurrentBDMFolder(final APISession session) {
        File bdmWorkDir = null;
        final String businessDataModelVersion = getBusinessDataModelVersion(session);
        if (businessDataModelVersion != null) {
            bdmWorkDir = new File(WebBonitaConstantsUtils.getInstance(session.getTenantId()).geBDMWorkFolder(),
                    businessDataModelVersion);
        }
        return bdmWorkDir;
    }

    protected static void cleanBDMFolder(final File currentBDMFolder) {
        if (currentBDMFolder != null) {
            final File parentFile = currentBDMFolder.getParentFile();
            if (parentFile != null && parentFile.exists()) {
                final File[] listFiles = currentBDMFolder.getParentFile().listFiles();
                if (listFiles != null) {
                    for (final File previousDeployedBDM : listFiles) {
                        if (previousDeployedBDM.isDirectory()) {
                            try {
                                FileUtils.deleteDirectory(previousDeployedBDM);
                            } catch (final IOException e) {
                                final String message = "Unable to delete obsolete bdm libraries";
                                if (LOGGER.isLoggable(Level.WARNING)) {
                                    LOGGER.log(Level.WARNING, message, e);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    protected static void updateBDMClientFolder(final APISession session, final File bdmWorkDir) {
        if (!bdmWorkDir.exists()) {
            bdmWorkDir.mkdirs();
        }
        try {
            final TenantAdministrationAPI tenantAdministrationAPI = TenantAPIAccessor.getTenantAdministrationAPI(session);
            final byte[] clientBDMZip = tenantAdministrationAPI.getClientBDMZip();
            unzipContentToFolder(clientBDMZip, bdmWorkDir);
        } catch (final BonitaHomeNotSetException e) {
            final String message = "Unable to create the class loader for the bdm libraries";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, message, e);
            }
        } catch (final ServerAPIException e) {
            final String message = "Unable to create the class loader for the bdm libraries";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, message, e);
            }
        } catch (final UnknownAPITypeException e) {
            final String message = "Unable to create the class loader for the bdm libraries";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, message, e);
            }
        } catch (final BusinessDataRepositoryException e) {
            final String message = "Unable to create the class loader for the bdm libraries, maybe no bdm has been installed";
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, message, e);
            }
        } catch (final IOException e) {
            final String message = "Unable to create the class loader for the bdm libraries";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, message, e);
            }
        }
    }

    private static void unzipContentToFolder(final byte[] zipContent, final File targetFolder) throws IOException {
        ByteArrayInputStream is = null;
        ZipInputStream zis = null;
        FileOutputStream out = null;
        try {
            is = new ByteArrayInputStream(zipContent);
            zis = new ZipInputStream(is);
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                final String entryName = entry.getName();
                if (entryName.endsWith(".jar")) {
                    final File file = new File(targetFolder, entryName);
                    if (file.exists()) {
                        file.delete();
                    }
                    file.createNewFile();
                    out = new FileOutputStream(file);
                    int len = 0;
                    final byte[] buffer = new byte[1024];
                    while ((len = zis.read(buffer)) > 0) {
                        out.write(buffer, 0, len);
                    }
                    out.close();
                }
            }
        } finally {
            if (is != null) {
                is.close();
            }
            if (zis != null) {
                zis.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }

    public static String getBusinessDataModelVersion(final APISession session) {
        try {
            final TenantAdministrationAPI tenantAdministrationAPI = TenantAPIAccessor.getTenantAdministrationAPI(session);
            final String lastBDMDeployementId = tenantAdministrationAPI.getBusinessDataModelVersion();
            return lastBDMDeployementId;
        } catch (final Exception e) {
            final String message = "Unable to retrieve business data model version";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, message, e);
            }
            return null;
        }
    }

    /**
     * Create a classloader for the process
     *
     * @param processDefinitionID
     *            the process definition ID
     * @param bdmFolder
     *            the process application resources directory
     * @return a Classloader
     * @throws java.io.IOException
     */
    protected static ClassLoader createProcessClassloaderWithBDM(final long processDefinitionID, final File bdmFolder, final ClassLoader parentClassloader)
            throws IOException {
        ClassLoader processClassLoader = null;
        try {
            final URL[] librariesURLs = getBDMLibrariesURLs(bdmFolder);
            if (librariesURLs.length > 0) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE, "Creating the classloader for process " + processDefinitionID);
                }
                if (parentClassloader == null) {
                    processClassLoader = new URLClassLoader(librariesURLs, Thread.currentThread().getContextClassLoader());
                } else {
                    processClassLoader = new URLClassLoader(librariesURLs, parentClassloader);
                }
            }
        } catch (final IOException e) {
            final String message = "Unable to create the class loader for the application's libraries";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, message, e);
            }
            throw new IOException(message);
        }
        return processClassLoader;
    }

    protected static URL[] getBDMLibrariesURLs(final File bdmFolder) throws IOException {
        final List<URL> urls = new ArrayList<URL>();
        if (bdmFolder.exists()) {
            final File[] bdmFiles = bdmFolder.listFiles(new FilenameFilter() {

                @Override
                public boolean accept(final File arg0, final String arg1) {
                    return arg1.endsWith(".jar");
                }
            });
            if (bdmFiles != null) {
                for (int i = 0; i < bdmFiles.length; i++) {
                    urls.add(bdmFiles[i].toURI().toURL());
                }
            }
        } else {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "The bdm directory doesn't exists.");
            }
        }
        final URL[] urlArray = new URL[urls.size()];
        urls.toArray(urlArray);
        return urlArray;
    }

    protected static synchronized ClassLoader createAndSaveProcessClassloader(final APISession session, final long processDefinitionID,
            final File currentBDMFolder) {

        final ClassLoader parentClassLoader = createProcessClassloader(session, processDefinitionID);
        ClassLoader processClassLoader = null;
        try {
            if (currentBDMFolder != null) {
                if (!currentBDMFolder.exists() || currentBDMFolder.listFiles().length == 0) {
                    updateBDMClientFolder(session, currentBDMFolder);
                }
                processClassLoader = createProcessClassloaderWithBDM(processDefinitionID, currentBDMFolder, parentClassLoader);
            }
            processClassLoader = setCorrectHierarchicalClassLoader(processClassLoader, parentClassLoader);
        } catch (final IOException e) {
            final String message = "Unable to create the class loader for the application's libraries";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, message, e);
            }
        }
        PROCESS_CLASSLOADERS.put(processDefinitionID, processClassLoader);
        return processClassLoader;
    }
}
