/**
 * Copyright (C) 2010 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.forms.server.accessor.impl.util;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bonitasoft.console.common.server.utils.BPMEngineException;
import org.bonitasoft.console.common.server.utils.FormsResourcesUtils;
import org.bonitasoft.engine.bpm.process.ProcessDefinitionNotFoundException;
import org.bonitasoft.engine.exception.RetrieveException;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.session.InvalidSessionException;
import org.bonitasoft.forms.server.accessor.DefaultFormsPropertiesFactory;
import org.bonitasoft.forms.server.exception.InvalidFormDefinitionException;
import org.w3c.dom.Document;

/**
 * The Form definition file document builder
 * 
 * @author Anthony Birembaut, Nicolas Chabanoles
 * 
 */
public class FormDocumentBuilder {

    /**
     * The form definition file name
     */
    public final static String FORM_DEFINITION_DEFAULT_FILE_NAME = "forms.xml";

    /**
     * The form definition file name prefixe
     */
    public final static String FORM_DEFINITION_FILE_PREFIX = "forms_";

    /**
     * The form definition file name suffixe
     */
    public final static String FORM_DEFINITION_FILE_SUFFIX = ".xml";

    /**
     * The document for the process definition UUID
     */
    protected Document document;

    /**
     * The process definition UUID
     */
    protected long processDefinitionID;

    /**
     * The locale as a string
     */
    protected String locale;

    /**
     * Last access to the current instance
     */
    protected Long lastAccess = new Date().getTime();

    /**
     * the {@link Date} of the process deployment
     */
    protected Date processDeployementDate;

    /**
     * indicate if the form definition file should be retrieved from the business archive only
     */
    protected final boolean getFormDefinitionFromBAR;

    /**
     * Logger
     */
    private static Logger LOGGER = Logger.getLogger(FormDocumentBuilder.class.getName());

    /**
     * Instances map by process
     */
    private static Map<String, Map<String, FormDocumentBuilder>> INSTANCES;

    /**
     * Separator for the instance map keys
     */
    protected final static String INSTANCES_MAP_SEPERATOR = "@";

    /**
     * Retrieve an instance of FormDocumentBuilder or create a new one if necessary.
     * The map contains a cache of instances. Each instance has a validity duration equals to the INSTANCE_EXPIRATION_TIME constant value
     * The deployment date is also check because a process can be undeployed and redeployed (after modifications) with the same UUID
     * 
     * @param session
     *            the engine API session
     * @param processDefinitionID
     *            the process definition ID
     * @param locale
     *            the user's locale
     * @param processDeployementDate
     *            the deployment date of the process
     * @throws IOException
     * @throws InvalidFormDefinitionException
     * @throws BPMEngineException
     * @throws InvalidSessionException
     */
    public static synchronized FormDocumentBuilder getInstance(final APISession session, final long processDefinitionID, final String locale,
            final Date processDeployementDate) throws ProcessDefinitionNotFoundException, IOException, InvalidFormDefinitionException, BPMEngineException,
            InvalidSessionException, RetrieveException {

        return getInstance(session, processDefinitionID, locale, processDeployementDate, false);
    }

    /**
     * Retrieve an instance of FormDocumentBuilder or create a new one if necessary.
     * The map contains a cache of instances. Each instance has a validity duration equals to the INSTANCE_EXPIRATION_TIME constant value
     * The deployment date is also check because a process can be undeployed and redeployed (after modifications) with the same UUID
     * 
     * @param session
     *            the engine API session
     * @param processDefinitionID
     *            the process definition ID
     * @param locale
     *            the user's locale
     * @param processDeployementDate
     *            the deployment date of the process
     * @param getFormDefinitionFromBAR
     *            indicate if the form definition file should be retrieved from the business archive only (if false, it's sought in the classpath first)
     * @throws IOException
     * @throws InvalidFormDefinitionException
     * @throws BPMEngineException
     * @throws InvalidSessionException
     */
    public static synchronized FormDocumentBuilder getInstance(final APISession session, final long processDefinitionID, final String locale,
            final Date processDeployementDate, final boolean getFormDefinitionFromBAR) throws ProcessDefinitionNotFoundException, IOException,
            InvalidFormDefinitionException, BPMEngineException, InvalidSessionException, RetrieveException {

        final long tenantID = session.getTenantId();
        if (INSTANCES == null) {
            INSTANCES = new LinkedHashMap<String, Map<String, FormDocumentBuilder>>(DefaultFormsPropertiesFactory.getDefaultFormProperties(tenantID)
                    .getMaxProcessesInCache(), .75F, true) {

                private static final long serialVersionUID = 7451370208143315146L;

                @Override
                protected boolean removeEldestEntry(final Map.Entry<String, Map<String, FormDocumentBuilder>> eldest) {
                    return size() > DefaultFormsPropertiesFactory.getDefaultFormProperties(tenantID).getMaxProcessesInCache();
                };
            };
        }

        FormDocumentBuilder instance = null;
        if (processDefinitionID == -1) {
            try {
                instance = new FormDocumentBuilder(session, -1, locale, processDeployementDate, getFormDefinitionFromBAR);
            } catch (final FileNotFoundException e) {
                if (locale != null) {
                    instance = new FormDocumentBuilder(session, -1, null, processDeployementDate, getFormDefinitionFromBAR);
                } else {
                    throw new FileNotFoundException("The forms definition file for process was not found.");
                }
            }
        } else {
            Map<String, FormDocumentBuilder> localeInstances = INSTANCES.get(processDefinitionID + INSTANCES_MAP_SEPERATOR + tenantID);
            if (localeInstances != null) {
                instance = localeInstances.get(locale);
            }
            boolean outOfDateDefinition = false;
            if (instance != null
                    && ((processDeployementDate != null && processDeployementDate.compareTo(instance.processDeployementDate) != 0) || instance
                            .hasExpired(tenantID))) {
                localeInstances.remove(locale);
                outOfDateDefinition = true;
            }
            if (instance == null || outOfDateDefinition) {
                if (localeInstances == null) {
                    localeInstances = new LinkedHashMap<String, FormDocumentBuilder>() {

                        private static final long serialVersionUID = -2092174987934309788L;

                        @Override
                        protected boolean removeEldestEntry(final java.util.Map.Entry<String, FormDocumentBuilder> eldest) {
                            return size() > DefaultFormsPropertiesFactory.getDefaultFormProperties(tenantID).getMaxLanguagesInCache();
                        }
                    };
                }
                try {
                    instance = new FormDocumentBuilder(session, processDefinitionID, locale, processDeployementDate, getFormDefinitionFromBAR);
                    localeInstances.put(locale, instance);
                    INSTANCES.put(processDefinitionID + INSTANCES_MAP_SEPERATOR + tenantID, localeInstances);
                } catch (final FileNotFoundException e) {
                    if (locale != null) {
                        instance = new FormDocumentBuilder(session, processDefinitionID, null, processDeployementDate, getFormDefinitionFromBAR);
                    } else {
                        throw new FileNotFoundException("The forms definition file for process " + processDefinitionID + "in tenant " + tenantID
                                + " was not found.");
                    }
                }
            } else {
                instance.lastAccess = new Date().getTime();
            }
        }
        return instance;
    }

    /**
     * Private constructor to prevent instantiation
     * 
     * @param session
     *            the engine APISession
     * @param processDefinitionID
     *            the process definition ID
     * @param locale
     *            the user's locale
     * @param processDeployementDate
     *            the deployment date of the process
     * @param getFormDefinitionFromBAR
     *            indicate if the form definition file should be retrieved from the business archive only
     * @throws IOException
     *             if the forms definition file is not found
     * @throws InvalidFormDefinitionException
     *             if the form definition file cannot be parsed
     * @throws BPMEngineException
     * @throws InvalidSessionException
     */
    protected FormDocumentBuilder(final APISession session, final long processDefinitionID, final String locale, final Date processDeployementDate,
            final boolean getFormDefinitionFromBAR) throws ProcessDefinitionNotFoundException, IOException, InvalidFormDefinitionException, BPMEngineException,
            InvalidSessionException, RetrieveException {
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "Building instance of the Form document builder for process " + processDefinitionID + " with locale " + locale
                    + " and deployment date " + processDeployementDate.getTime());
        }
        this.processDefinitionID = processDefinitionID;
        this.locale = locale;
        this.processDeployementDate = processDeployementDate;
        this.getFormDefinitionFromBAR = getFormDefinitionFromBAR;
        final InputStream formsDefinitionStream = getFormsDefinitionInputStream(session);
        try {
            final DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            this.document = builder.parse(formsDefinitionStream);
        } catch (final Exception e) {
            final String errorMessage = "Failed to parse the forms definition file";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage);
        } finally {
            if (formsDefinitionStream != null) {
                formsDefinitionStream.close();
            }
        }
    }

    /**
     * @return the form definition as an input stream
     * @param session
     *            the engine APISession
     * @throws IOException
     * @throws BPMEngineException
     * @throws InvalidSessionException
     */
    protected InputStream getFormsDefinitionInputStream(final APISession session) throws IOException, ProcessDefinitionNotFoundException, BPMEngineException,
            InvalidSessionException, RetrieveException {

        InputStream formsDefinitionInputStream = null;
        URL formsParametersURL = null;
        String localizedFileName = null;
        if (locale != null) {
            localizedFileName = FORM_DEFINITION_FILE_PREFIX + locale + FORM_DEFINITION_FILE_SUFFIX;
            if (!getFormDefinitionFromBAR) {
                formsParametersURL = Thread.currentThread().getContextClassLoader().getResource(localizedFileName);
            }
        }
        if (!getFormDefinitionFromBAR && formsParametersURL == null) {
            formsParametersURL = Thread.currentThread().getContextClassLoader().getResource(FORM_DEFINITION_DEFAULT_FILE_NAME);
        }
        if (formsParametersURL == null) {
            if (processDefinitionID == -1) {
                throw new FileNotFoundException("The forms definition file for the process was not found.");
            }
            // try to get the form file from the application resource directory where files from the bar are exctracted
            final File processApplicationsResourcesDir = FormsResourcesUtils.getApplicationResourceDir(session, processDefinitionID,
                    processDeployementDate);
            if (!processApplicationsResourcesDir.exists()) {
                FormsResourcesUtils.retrieveApplicationFiles(session, processDefinitionID, processDeployementDate);
            }
            File formsFile = null;
            if (locale != null) {
                formsFile = new File(processApplicationsResourcesDir, localizedFileName);
            }
            if (formsFile == null || !formsFile.exists()) {
                formsFile = new File(processApplicationsResourcesDir, FORM_DEFINITION_DEFAULT_FILE_NAME);
            }
            if (formsFile.exists()) {
                formsDefinitionInputStream = new FileInputStream(formsFile);
            } else {
                throw new FileNotFoundException("The forms definition file for process " + processDefinitionID + " was not found.");
            }
        } else {
            formsDefinitionInputStream = formsParametersURL.openStream();
        }
        return formsDefinitionInputStream;
    }

    /**
     * @return the document
     */
    public Document getDocument() {
        return document;
    }

    /**
     * Determinates whether the current instance has expired or not
     * 
     * @param tenantID
     *            the tenant ID
     * @return true if the current instance has expired, false otherwise
     */
    protected boolean hasExpired(final long tenantID) {
        final long now = new Date().getTime();
        return this.lastAccess + DefaultFormsPropertiesFactory.getDefaultFormProperties(tenantID).getProcessesTimeToLiveInCache() < now;
    }
}
