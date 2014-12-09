/**
 * Copyright (C) 2012 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.console.common.server.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
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

import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.bonitasoft.console.common.server.preferences.properties.ProcessIdentifier;
import org.bonitasoft.console.common.server.preferences.properties.SecurityProperties;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.process.ProcessDefinition;
import org.bonitasoft.engine.bpm.process.ProcessDefinitionNotFoundException;
import org.bonitasoft.engine.exception.RetrieveException;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.session.InvalidSessionException;

/**
 * @author Anthony Birembaut
 *
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
    protected final static Map<Long, ClassLoader> PROCESS_CLASSLOADERS = new HashMap<Long, ClassLoader>();

    /**
     * Logger
     */
    private static Logger LOGGER = Logger.getLogger(FormsResourcesUtils.class.getName());

    /**
     * Util class allowing to work with the BPM engine API
     */
    protected static BPMEngineAPIUtil bpmEngineAPIUtil = new BPMEngineAPIUtil();

    /**
     * Retrieve the web resources from the business archive and store them in a local directory
     *
     * @param session
     *        the engine API session
     * @param processDefinitionID
     *        the process definition ID
     * @param processDeployementDate
     *        the process deployement date
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
     *        the process definition ID
     * @param processApplicationsResourcesDir
     *        the process application resources directory
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
     *        the process application resources directory
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

    /**
     * Retrieve the class loader associated with the process or create it if there is no classloader associated with this process yet
     *
     * @param session
     *        the API session
     * @param processDefinitionID
     *        the process definition ID
     * @return a {@link ClassLoader}, null if the process classloader doesn't exists and couldn't be created
     */
    public ClassLoader getProcessClassLoader(final APISession session, final long processDefinitionID) {

        ClassLoader processClassLoader = null;
        if (PROCESS_CLASSLOADERS.containsKey(processDefinitionID)) {
            processClassLoader = PROCESS_CLASSLOADERS.get(processDefinitionID);
        } else {
            processClassLoader = FormsResourcesUtils.createAndSaveProcessClassloader(session, processDefinitionID);
        }
        return processClassLoader;
    }

    protected static synchronized ClassLoader createAndSaveProcessClassloader(final APISession session, final long processDefinitionID) {
        final ClassLoader processClassLoader = createProcessClassloader(session, processDefinitionID);
        PROCESS_CLASSLOADERS.put(processDefinitionID, processClassLoader);
        return processClassLoader;
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
     *        the API session
     * @param processDefinitionID
     *        the process definition ID
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
     *        the API session
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
     *        the directory to delete
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
}
