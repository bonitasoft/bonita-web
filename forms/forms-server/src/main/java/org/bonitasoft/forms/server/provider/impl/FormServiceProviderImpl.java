/**
 * Copyright (C) 2011, 2014 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.forms.server.provider.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.bonitasoft.console.common.server.utils.BPMEngineException;
import org.bonitasoft.console.common.server.utils.BPMExpressionEvaluationException;
import org.bonitasoft.console.common.server.utils.FormsResourcesUtils;
import org.bonitasoft.engine.api.CommandAPI;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.flownode.ActivityInstanceNotFoundException;
import org.bonitasoft.engine.bpm.flownode.ArchivedFlowNodeInstanceNotFoundException;
import org.bonitasoft.engine.bpm.flownode.FlowNodeExecutionException;
import org.bonitasoft.engine.bpm.process.ArchivedProcessInstanceNotFoundException;
import org.bonitasoft.engine.bpm.process.ProcessActivationException;
import org.bonitasoft.engine.bpm.process.ProcessDefinitionNotEnabledException;
import org.bonitasoft.engine.bpm.process.ProcessDefinitionNotFoundException;
import org.bonitasoft.engine.bpm.process.ProcessInstanceNotFoundException;
import org.bonitasoft.engine.exception.CreationException;
import org.bonitasoft.engine.exception.ExecutionException;
import org.bonitasoft.engine.exception.SearchException;
import org.bonitasoft.engine.expression.ExpressionType;
import org.bonitasoft.engine.identity.UserNotFoundException;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.session.InvalidSessionException;
import org.bonitasoft.forms.client.model.ActivityEditState;
import org.bonitasoft.forms.client.model.Expression;
import org.bonitasoft.forms.client.model.FormAction;
import org.bonitasoft.forms.client.model.FormFieldValue;
import org.bonitasoft.forms.client.model.FormURLComponents;
import org.bonitasoft.forms.client.model.FormValidator;
import org.bonitasoft.forms.client.model.exception.AbortedFormException;
import org.bonitasoft.forms.client.model.exception.CanceledFormException;
import org.bonitasoft.forms.client.model.exception.ForbiddenApplicationAccessException;
import org.bonitasoft.forms.client.model.exception.ForbiddenFormAccessException;
import org.bonitasoft.forms.client.model.exception.FormAlreadySubmittedException;
import org.bonitasoft.forms.client.model.exception.FormInErrorException;
import org.bonitasoft.forms.client.model.exception.IllegalActivityTypeException;
import org.bonitasoft.forms.client.model.exception.MigrationProductVersionNotIdenticalException;
import org.bonitasoft.forms.client.model.exception.SessionTimeoutException;
import org.bonitasoft.forms.client.model.exception.SkippedFormException;
import org.bonitasoft.forms.client.model.exception.SuspendedFormException;
import org.bonitasoft.forms.server.accessor.FormDefAccessorFactory;
import org.bonitasoft.forms.server.accessor.IApplicationConfigDefAccessor;
import org.bonitasoft.forms.server.accessor.IApplicationFormDefAccessor;
import org.bonitasoft.forms.server.accessor.impl.EngineApplicationConfigDefAccessorImpl;
import org.bonitasoft.forms.server.accessor.impl.XMLApplicationConfigDefAccessorImpl;
import org.bonitasoft.forms.server.accessor.impl.util.FormCacheUtilFactory;
import org.bonitasoft.forms.server.accessor.impl.util.FormDocumentBuilderFactory;
import org.bonitasoft.forms.server.api.FormAPIFactory;
import org.bonitasoft.forms.server.api.IFormValidationAPI;
import org.bonitasoft.forms.server.api.IFormWorkflowAPI;
import org.bonitasoft.forms.server.api.impl.FormWorkflowAPIImpl;
import org.bonitasoft.forms.server.exception.ApplicationFormDefinitionNotFoundException;
import org.bonitasoft.forms.server.exception.FileTooBigException;
import org.bonitasoft.forms.server.exception.FormInitializationException;
import org.bonitasoft.forms.server.exception.FormNotFoundException;
import org.bonitasoft.forms.server.exception.FormSubmissionException;
import org.bonitasoft.forms.server.exception.FormValidationException;
import org.bonitasoft.forms.server.exception.FormWorflowApiException;
import org.bonitasoft.forms.server.exception.InvalidFormDefinitionException;
import org.bonitasoft.forms.server.exception.NoCredentialsInSessionException;
import org.bonitasoft.forms.server.exception.TaskAssignationException;
import org.bonitasoft.forms.server.provider.FormServiceProvider;
import org.bonitasoft.forms.server.provider.impl.util.ApplicationURLUtils;
import org.bonitasoft.forms.server.provider.impl.util.FormServiceProviderUtil;
import org.bonitasoft.forms.server.util.FormContextUtil;
import org.bonitasoft.forms.server.util.FormLogger;
import org.bonitasoft.forms.server.util.IFormLogger;
import org.bonitasoft.web.rest.model.user.User;
import org.w3c.dom.Document;

/**
 * Implementation of FormServiceProvider based on Bonita execution engine
 * 
 * @author QiXiang Zhang, Anthony Birembaut, Haojie Yuan, Vincent Elcrin, Julien Mege, Celine Souchet
 */
public class FormServiceProviderImpl implements FormServiceProvider {

    /**
     *
     */
    private static final String UI_FORM_MODE = "form";

    /**
     * Logger
     */
    protected static FormLogger LOGGER;

    protected static Logger defaultLogger = Logger.getLogger(FormServiceProviderImpl.class.getName());

    protected static SimpleDateFormat DATE_FORMAT;

    private final static int DEFAULT_NUM_VALUE = 0;

    static {
        if (Logger.getLogger(FormServiceProviderImpl.class.getName()).isLoggable(Level.WARNING)) {
            DATE_FORMAT = new SimpleDateFormat("HH:mm:ss:SSS");
        }
    }

    /**
     * execute actions mode : if true indicates that this is a redirection request and the actions should be executed
     */
    public static final String EXECUTE_ACTIONS_PARAM = "executeActions";

    /**
     * Default constructor.
     */
    public FormServiceProviderImpl() {
        super();
    }

    protected IFormLogger getLogger() {
        if (LOGGER == null) {
            LOGGER = new FormLogger(FormServiceProviderImpl.class.getName());
        }
        return LOGGER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Document getFormDefinitionDocument(final Map<String, Object> context) throws IOException, InvalidFormDefinitionException, FormNotFoundException,
    SessionTimeoutException {

        final FormContextUtil ctxu = new FormContextUtil(context);
        if (defaultLogger.isLoggable(Level.FINEST)) {
            final String time = DATE_FORMAT.format(new Date());
            defaultLogger.log(Level.FINEST, "### " + time + " - getFormDefinitionDocument - start ");
        }
        final Locale locale = ctxu.getLocale();
        long processDefinitionID = -1;
        Document formDefinitionDocument = null;
        Date processDeployementDate = null;
        try {
            processDefinitionID = getProcessDefinitionID(context);
            final APISession session = ctxu.getAPISessionFromContext();
            processDeployementDate = getDeployementDate(session, processDefinitionID);
            String localeString = null;
            if (locale != null) {
                localeString = locale.getLanguage();
            }
            formDefinitionDocument = FormDocumentBuilderFactory.getFormDocumentBuilder(session, processDefinitionID, localeString, processDeployementDate)
                    .getDocument();
        } catch (final ProcessDefinitionNotFoundException e) {
            if (processDefinitionID != -1) {
                final String message = "Cannot find a process with ID " + processDefinitionID;
                logSevereWithContext(message, e, context);
                throw new FormNotFoundException(message);
            }
        } catch (final BPMEngineException e) {
            final String message = "Error while communicating with the engine.";
            logSevereWithContext(message, e, context);
            throw new FormNotFoundException(message);
        } catch (final InvalidSessionException e) {
            final String message = "The engine session is invalid.";
            logSevereWithContext(message, e, context);
            throw new SessionTimeoutException(message);
        } catch (final FileNotFoundException e) {
            logInfoMessage("No form definition descriptor was found for process " + processDefinitionID, e);
        }
        if (defaultLogger.isLoggable(Level.FINEST)) {
            final String time = DATE_FORMAT.format(new Date());
            defaultLogger.log(Level.FINEST, "### " + time + " - getFormDefinitionDocument - end");
        }
        return formDefinitionDocument;
    }

    protected void logSevereWithContext(final String message, final Throwable e, final Map<String, Object> context) {
        if (getLogger().isLoggable(Level.SEVERE)) {
            getLogger().log(Level.SEVERE, message, e, context);
        }
    }

    /**
     * Return the process definition ID based on the context map
     *
     * @param context
     *        Map of context
     * @return the ProcessDefinitionID
     * @throws FormNotFoundException
     * @throws SessionTimeoutException
     */
    protected long getProcessDefinitionID(final Map<String, Object> context) throws InvalidSessionException {
        final FormContextUtil ctxu = new FormContextUtil(context);
        if (defaultLogger.isLoggable(Level.FINEST)) {
            final String time = DATE_FORMAT.format(new Date());
            defaultLogger.log(Level.FINEST, "### " + time + " - getProcessDefinitionID - start");
        }
        final Map<String, Object> urlContext = getUrlContext(context);
        long processDefinitionID = -1;
        if (urlContext != null) {
            try {
                if (urlContext.get(FormServiceProviderUtil.PROCESS_UUID) != null) {
                    processDefinitionID = Long.valueOf(urlContext.get(FormServiceProviderUtil.PROCESS_UUID).toString());
                } else if (urlContext.get(FormServiceProviderUtil.FORM_ID) != null) {
                    final IFormWorkflowAPI workflowAPI = getFormWorkFlowApi();
                    final APISession session = ctxu.getAPISessionFromContext();
                    final String formId = (String) urlContext.get(FormServiceProviderUtil.FORM_ID);
                    final String processDefinitionUUID = extractProcessDefinitionUUID(formId);
                    try {
                        processDefinitionID = workflowAPI.getProcessDefinitionIDFromUUID(session, processDefinitionUUID);
                    } catch (final ProcessDefinitionNotFoundException e) {
                        final String message = "The process definition for process definition with UUID " + processDefinitionUUID + " was not found!";
                        logSevereWithContext(message, e, context);
                        throw new RuntimeException(message, e);
                    }
                }
                else {
                    if (defaultLogger.isLoggable(Level.FINE)) {
                        defaultLogger.log(Level.FINE,
                                "The URL context does not contain any BPM entity parameter. Unable to retrieve the process definition UUID.");
                    }
                }
            } catch (final BPMEngineException e) {
                final String message = "Error while communicating with the engine.";
                logSevereWithContext(message, e, context);
                throw new RuntimeException(message, e);
            }
        } else {
            if (defaultLogger.isLoggable(Level.WARNING)) {
                defaultLogger.log(Level.WARNING, "The URL context is null. Unable to retrieve the process definition UUID.");
            }
        }
        if (defaultLogger.isLoggable(Level.FINEST)) {
            final String time = DATE_FORMAT.format(new Date());
            defaultLogger.log(Level.FINEST, "### " + time + " - getProcessDefinitionID - end");
        }
        return processDefinitionID;
    }

    protected String extractProcessDefinitionUUID(final String formId) {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\\");
        stringBuilder.append(FormServiceProviderUtil.FORM_ID_SEPARATOR);
        final String processDefinitionUUID;
        final String formUUID = formId.split(stringBuilder.toString())[0];
        final String[] splitedFormUUID = formUUID.split(FormWorkflowAPIImpl.UUID_SEPARATOR);
        final StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(splitedFormUUID[0]);
        stringBuilder2.append(FormWorkflowAPIImpl.UUID_SEPARATOR);
        stringBuilder2.append(splitedFormUUID[1]);
        processDefinitionUUID = stringBuilder2.toString();
        return processDefinitionUUID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAllowed(final String formId, final String permissions, final String productVersion, final String migrationProductVersion,
            final Map<String, Object> context, final boolean isFormPermissions) throws ForbiddenFormAccessException, SuspendedFormException,
            CanceledFormException, FormNotFoundException, FormAlreadySubmittedException, ForbiddenApplicationAccessException, FormInErrorException,
            MigrationProductVersionNotIdenticalException, NoCredentialsInSessionException, SkippedFormException, SessionTimeoutException,
            TaskAssignationException, AbortedFormException {
        final FormContextUtil ctxu = new FormContextUtil(context);
        if (defaultLogger.isLoggable(Level.FINEST)) {
            final String time = DATE_FORMAT.format(new Date());
            defaultLogger.log(Level.FINEST, "### " + time + " - isAllowed - start");
        }
        final Map<String, Object> urlContext = getUrlContext(context);
        final User user = (User) context.get(FormServiceProviderUtil.USER);
        if (user == null) {
            final String message = "Can't find the user.";
            if (defaultLogger.isLoggable(Level.INFO)) {
                defaultLogger.log(Level.INFO, message);
            }
            throw new NoCredentialsInSessionException(message);
        }
        // No migration to perform in 6.1 since the forms.xml model didn't change between 6.0 and 6.1
        // if the model changes in a later version, restore this check and provide the migration scripts in the distribution
        // final String currentProductVersion = FormBuilderImpl.PRODUCT_VERSION;
        // if (productVersion != null) {
        // if (migrationProductVersion == null && !currentProductVersion.split("-")[0].equals(productVersion.split("-")[0])
        // || migrationProductVersion != null && !currentProductVersion.split("-")[0].equals(migrationProductVersion.split("-")[0])) {
        // final String message = "The migration product version not identical with current product version.";
        // if (getLogger().isLoggable(Level.WARNING)) {
        // getLogger().log(Level.WARNING, message);
        // }
        // throw new MigrationProductVersionNotIdenticalException(message);
        // }
        // }
        if (permissions != null) {
            final IFormWorkflowAPI workflowAPI = getFormWorkFlowApi();
            final APISession session = ctxu.getAPISessionFromContext();
            final String uuidType = permissions.split("#")[0];
            try {
                if (FormServiceProviderUtil.ACTIVITY_UUID.equals(uuidType)) {
                    // The user has the permission for this task (= the task UUID is in the <perison/> of the form)
                    final String activityDefinitionUUIDStr = permissions.split("#")[1];
                    if (urlContext.get(FormServiceProviderUtil.TASK_UUID) != null) {
                        // Trying to display a Form for a TASK.
                        final long activityInstanceID = getActivityInstanceId(urlContext);
                        try {
                            if (!getActivityDefinitionUUID(session, workflowAPI, activityInstanceID).equals(activityDefinitionUUIDStr)) {
                                final String message = "User tried to access an unauthorized activity <" + activityDefinitionUUIDStr + ">";
                                if (getLogger().isLoggable(Level.INFO)) {
                                    getLogger().log(Level.INFO, message, context);
                                }
                                throw new ForbiddenFormAccessException(message);
                            }
                        } catch (final FormWorflowApiException e) {
                            logInfoMessageWithContext(e.getMessage(), e, context);
                            throw new FormNotFoundException(e);
                        }
                        if (isFormPermissions) {
                            canUserViewActivityInstanceForm(session, user, workflowAPI, activityInstanceID, formId, ctxu.getUserId(false));
                            // If assignTask=true in the contextURL assign the task to the user
                            if (isAssignTask(urlContext)) {
                                try {
                                    getFormWorkFlowApi().assignTask(session, activityInstanceID);
                                } catch (final TaskAssignationException e) {
                                    logSevereWithContext(e.getMessage(), e, context);
                                    throw e;
                                }
                            }
                        }
                    } else {
                        final String message = "A task parameter is required to display the form for activity " + activityDefinitionUUIDStr;
                        if (getLogger().isLoggable(Level.INFO)) {
                            getLogger().log(Level.INFO, message);
                        }
                        throw new ForbiddenFormAccessException(message);
                    }
                } else if (FormServiceProviderUtil.PROCESS_UUID.equals(uuidType)) {
                    // The user has the permission for this Process (= the process UUID is in the <perison/> on the form)
                    final String processDefinitionUUIDStr = permissions.split("#")[1];
                    try {
                        final long processDefinitionID = workflowAPI.getProcessDefinitionIDFromUUID(session, processDefinitionUUIDStr);
                        if (urlContext.get(FormServiceProviderUtil.TASK_UUID) != null) {
                            // trying to display a form for a task (
                            final long activityInstanceID = getActivityInstanceId(urlContext);
                            try {
                                final long activityInstanceProcessDefinitionID = getProcessDefinitionId(session, workflowAPI, activityInstanceID);
                                if (activityInstanceProcessDefinitionID != processDefinitionID) {
                                    final String message = "The task required is not an instance of an activity of process" + processDefinitionUUIDStr;
                                    if (getLogger().isLoggable(Level.INFO)) {
                                        getLogger().log(Level.INFO, message);
                                    }
                                    throw new ForbiddenFormAccessException(message);
                                }
                            } catch (final FormWorflowApiException e) {
                                logSevereMessageWithContext(e, e.getMessage(), context);
                                throw new FormNotFoundException(e);
                            }
                            if (isFormPermissions) {
                                canUserViewActivityInstanceForm(session, user, workflowAPI, activityInstanceID, formId, ctxu.getUserId(false));
                            }
                        } else if (urlContext.get(FormServiceProviderUtil.INSTANCE_UUID) != null) {
                            // Trying to display the overview form
                            final long processInstanceID = getProcessInstanceId(urlContext);
                            try {
                                final long processInstanceProcessDefinitionID = workflowAPI.getProcessDefinitionIDFromProcessInstanceID(session,
                                        processInstanceID);
                                if (processInstanceProcessDefinitionID != processDefinitionID) {
                                    final String message = "The process instance required is not an instance of process" + processDefinitionUUIDStr;
                                    if (getLogger().isLoggable(Level.INFO)) {
                                        getLogger().log(Level.INFO, message);
                                    }
                                    throw new ForbiddenFormAccessException(message);
                                }
                            } catch (final ProcessInstanceNotFoundException e) {
                                final String message = "The process instance with ID " + processInstanceID + " does not exist!";
                                logInfoMessageWithContext(message, e, context);
                                throw new FormNotFoundException(message);
                            } catch (final ArchivedProcessInstanceNotFoundException e) {
                                final String message = "Archived process instance not found";
                                logSevereWithContext(message, e, context);
                                throw new FormNotFoundException(message);
                            }
                            if (isFormPermissions) {
                                canUserViewInstanceForm(session, user, workflowAPI, processInstanceID, formId, ctxu.getUserId());
                            }
                        } else if (urlContext.get(FormServiceProviderUtil.PROCESS_UUID) != null) {
                            // Trying to display the Instantiation Form for a process
                            final long processDefinitionIDFromURL = Long.valueOf(urlContext.get(FormServiceProviderUtil.PROCESS_UUID).toString());
                            if (processDefinitionIDFromURL != processDefinitionID) {
                                final String message = "The process required does not match the form required " + processDefinitionUUIDStr;
                                logInfoMessageWithContext(message, new ForbiddenFormAccessException(), context);
                                throw new ForbiddenFormAccessException(message);
                            }
                            if (!workflowAPI.isProcessEnabled(session, processDefinitionID)) {
                                final String message = "The process definition with ID " + processDefinitionID + " is not enabled.";
                                logInfoMessageWithContext(message, new ForbiddenFormAccessException(), context);
                                throw new ForbiddenFormAccessException(message);
                            }
                            if (isFormPermissions) {
                                canUserInstantiateProcess(session, user, processDefinitionID, ctxu.getUserId());
                            }
                        }
                    } catch (final ProcessDefinitionNotFoundException e) {
                        final String message = "The process definition " + processDefinitionUUIDStr + " does not exist!";
                        if (getLogger().isLoggable(Level.INFO)) {
                            getLogger().log(Level.INFO, message, e);
                        }
                        throw new FormNotFoundException(message);
                    }
                }
            } catch (final BPMEngineException e) {
                final String message = "Error while communicating with the engine.";
                logSevereWithContext(message, e, context);
                throw new FormNotFoundException(message);
            } catch (final InvalidSessionException e) {
                final String message = "The engine session is invalid.";
                logSevereWithContext(message, e, context);
                throw new SessionTimeoutException(message);
            }
        } else {
            final String message = "The permissions are undefined for form " + formId;
            if (getLogger().isLoggable(Level.INFO)) {
                getLogger().log(Level.INFO, message);
            }
            throw new ForbiddenApplicationAccessException(message);
        }
        if (getLogger().isLoggable(Level.FINEST)) {
            final String time = DATE_FORMAT.format(new Date());
            getLogger().log(Level.FINEST, "### " + time + " - isAllowed - end");
        }
        return true;
    }

    /**
     * Check if a user can view an activity instance form
     *
     * @param session
     *        the API session
     * @param user
     *        the user
     * @param workflowAPI
     *        the workflow API
     * @param activityInstanceID
     *        the activity instance ID
     * @param formId
     *        the form Id
     * @param userId
     *        the userId used to "Start for" and "Do for"
     * @throws BPMEngineException
     * @throws ForbiddenFormAccessException
     * @throws SuspendedFormException
     * @throws CanceledFormException
     * @throws FormInErrorException
     * @throws SkippedFormException
     * @throws FormNotFoundException
     * @throws FormAlreadySubmittedException
     */
    protected void canUserViewActivityInstanceForm(final APISession session, final User user, final IFormWorkflowAPI workflowAPI,
            final long activityInstanceID, final String formId, final long userId) throws BPMEngineException, InvalidSessionException,
            ForbiddenFormAccessException, SuspendedFormException, CanceledFormException, FormInErrorException, SkippedFormException, FormNotFoundException,
            FormAlreadySubmittedException, AbortedFormException {
        try {
            // TODO verify if the user is admin. In this case, he can access the form
            // TODO verify if a user is process supervisor of the process. In this case, he can access the form
            if (!isInvolvedInHumanTask(session, userId, activityInstanceID)) {
                final String message = "An attempt was made by user " + user.getUsername() + " to access the form of activity instance " + activityInstanceID;
                if (getLogger().isLoggable(Level.INFO)) {
                    getLogger().log(Level.INFO, message);
                }
                throw new ForbiddenFormAccessException(message);
            }
            final ActivityEditState activityEditState = workflowAPI.getTaskEditState(session, activityInstanceID);
            if (ActivityEditState.SUSPENDED.equals(activityEditState)) {
                throw new SuspendedFormException();
            } else if (ActivityEditState.CANCELED.equals(activityEditState)) {
                throw new CanceledFormException();
            } else if (ActivityEditState.FAILED.equals(activityEditState)) {
                throw new FormInErrorException();
            } else if (ActivityEditState.SKIPPED.equals(activityEditState)) {
                throw new SkippedFormException();
            } else if (ActivityEditState.ABORTED.equals(activityEditState)) {
                throw new AbortedFormException();
            } else if (ActivityEditState.NOT_EDITABLE.equals(activityEditState)) {
                if (FormServiceProviderUtil.ENTRY_FORM_TYPE.equals(getFormType(formId))) {
                    final String message = "The activity instance with ID " + activityInstanceID
                            + " cannot be executed anymore. It's either finished or aborted";
                    if (getLogger().isLoggable(Level.INFO)) {
                        getLogger().log(Level.INFO, message);
                    }
                    throw new FormAlreadySubmittedException(message);
                }
            }
        } catch (final ActivityInstanceNotFoundException e) {
            final String message = "The activity instance with ID " + activityInstanceID + " does not exist!";
            if (getLogger().isLoggable(Level.INFO)) {
                getLogger().log(Level.INFO, message, e);
            }
            throw new FormNotFoundException(message);
        }
    }

    /**
     * Check if a user can view a process instance form
     *
     * @param session
     *        the API session
     * @param user
     *        the user
     * @param workflowAPI
     *        the workflow API
     * @param processInstanceID
     *        the process instance ID
     * @param formId
     *        the form Id
     * @param userId
     *        the userId to performe a "Start For" or a "Do for"
     * @throws InvalidSessionException
     * @throws BonitaHomeNotSetException
     * @throws ServerAPIException
     * @throws UnknownAPITypeException
     * @throws FormNotFoundException
     * @throws ForbiddenFormAccessException
     * @throws SessionTimeoutException
     */
    protected void canUserViewInstanceForm(final APISession session, final User user, final IFormWorkflowAPI workflowAPI, final long processInstanceID,
            final String formId, final long userId) throws InvalidSessionException, BPMEngineException, FormNotFoundException, ForbiddenFormAccessException,
            SessionTimeoutException {

        try {
            if (!workflowAPI.isUserAdminOrProcessOwner(session, processInstanceID)
                    && !workflowAPI.canUserSeeProcessInstance(session, isInvolvedInProcessInstance(session, userId, processInstanceID), processInstanceID)) {
                final String message = "An attempt was made by user " + user.getUsername() + " to access the " + getFormType(formId)
                        + " form of process instance " + processInstanceID;
                if (getLogger().isLoggable(Level.INFO)) {
                    getLogger().log(Level.INFO, message);
                }
                throw new ForbiddenFormAccessException(message);
            }
        } catch (final ProcessInstanceNotFoundException e) {
            final String message = "The process instance with ID " + processInstanceID + " does not exist!";
            if (getLogger().isLoggable(Level.INFO)) {
                getLogger().log(Level.INFO, message, e);
            }
            throw new FormNotFoundException(message);
        } catch (final ArchivedProcessInstanceNotFoundException e) {
            final String message = "The archived process instance with ID " + processInstanceID + " does not exist!";
            if (getLogger().isLoggable(Level.INFO)) {
                getLogger().log(Level.INFO, message, e);
            }
            throw new FormNotFoundException(message);
        } catch (final ProcessDefinitionNotFoundException e) {
            final String message = "The process definition of process instance with ID " + processInstanceID + " could not be found.";
            if (getLogger().isLoggable(Level.INFO)) {
                getLogger().log(Level.INFO, message, e);
            }
            throw new FormNotFoundException(message);
        } catch (final UserNotFoundException e) {
            final String message = "The user with ID " + session.getUserId() + " does not exist!";
            if (getLogger().isLoggable(Level.SEVERE)) {
                logSevereMessage(e, message);
            }
            throw new SessionTimeoutException(message);
        }
    }

    /**
     * Check if a user can access a process instantiation form
     *
     * @param session
     *        the API session
     * @param user
     *        the user
     * @param workflowAPI
     *        the workflow API
     * @param processDefinitionID
     *        the process definition ID
     * @param userId
     *        userId used for "Start for" and "Do for"
     * @throws InvalidSessionException
     * @throws ForbiddenFormAccessException
     * @throws FormNotFoundException
     * @throws BPMEngineException
     */
    protected void canUserInstantiateProcess(final APISession session, final User user, final long processDefinitionID, final long userId)
            throws InvalidSessionException, BPMEngineException, ForbiddenFormAccessException {
        // TODO verify if the user is admin. In this case, he can access the form
        // TODO verify if a user is process supervisor of the process. In this case, he can access the form
        if (!canStartProcessDefinition(session, userId, processDefinitionID)) {
            final String message = "An attempt was made by user " + user.getUsername() + " to access the instantiation form of process "
                    + processDefinitionID;
            if (getLogger().isLoggable(Level.INFO)) {
                getLogger().log(Level.INFO, message);
            }
            throw new ForbiddenFormAccessException(message);
        }
    }

    /**
     * Parse the formId to extract the form type
     *
     * @param formId
     *        the form ID
     * @return "entry", "view" or "recap"
     * @throws FormNotFoundException
     */
    protected String getFormType(final String formId) throws FormNotFoundException {
        String formType = null;
        if (formId == null) {
            final String message = "the Form ID is null. The parameter 'form' is probably missing from the URL.";
            if (getLogger().isLoggable(Level.SEVERE)) {
                getLogger().log(Level.SEVERE, message);
            }
            throw new FormNotFoundException(message);
        } else {
            final String[] formIdComponents = formId.split("\\" + FormServiceProviderUtil.FORM_ID_SEPARATOR);
            if (formIdComponents.length > 1) {
                formType = formIdComponents[1];
            } else {
                final String message = "Wrong FormId " + formId + ". It doesn't contain the form type.";
                if (getLogger().isLoggable(Level.SEVERE)) {
                    getLogger().log(Level.SEVERE, message);
                }
                throw new IllegalArgumentException(message);
            }
        }
        return formType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public Serializable resolveExpression(final Expression expression, final Map<String, Object> context) throws FormNotFoundException,
    FormInitializationException, SessionTimeoutException, FileTooBigException, IOException {
        final FormContextUtil ctxu = new FormContextUtil(context);
        Serializable result = null;
        if (expression != null) {
            if (getLogger().isLoggable(Level.FINEST)) {
                final String time = DATE_FORMAT.format(new Date());
                getLogger().log(Level.FINEST, "### " + time + " - resolveExpression - start" + expression.getExpressionType() + " " + expression.getContent());
            }
            long activityInstanceID = -1;
            long processDefinitionID = -1;
            long processInstanceID = -1;
            boolean isCurrentValue = true;
            final Locale locale = ctxu.getLocale();
            if (context.get(FormServiceProviderUtil.IS_CURRENT_VALUE) != null) {
                isCurrentValue = (Boolean) context.get(FormServiceProviderUtil.IS_CURRENT_VALUE);
            }
            final Map<String, Serializable> transientDataContext = (Map<String, Serializable>) context.get(FormServiceProviderUtil.TRANSIENT_DATA_CONTEXT);
            final APISession session = ctxu.getAPISessionFromContext();
            final Map<String, FormFieldValue> fieldValues = convertFormFieldValues(
                    (Map<String, FormFieldValue>) context.get(FormServiceProviderUtil.FIELD_VALUES), true);
            final IFormWorkflowAPI workflowAPI = getFormWorkFlowApi();
            try {
                final Map<String, Object> urlContext = getUrlContext(context);
                if (Boolean.TRUE.equals(context.get(FormServiceProviderUtil.IS_CONFIG_CONTEXT))) {
                    resolveAndSetProcessDefinitionID(session, workflowAPI, urlContext);
                }
                if (urlContext.get(FormServiceProviderUtil.TASK_UUID) != null) {
                    activityInstanceID = getActivityInstanceId(urlContext);
                    if (fieldValues != null) {
                        if (transientDataContext != null) {
                            result = workflowAPI.getActivityFieldValue(session, activityInstanceID, expression, fieldValues, locale, isCurrentValue,
                                    transientDataContext);
                        } else {
                            result = workflowAPI.getActivityFieldValue(session, activityInstanceID, expression, fieldValues, locale, isCurrentValue);
                        }
                    } else {
                        if (transientDataContext != null) {
                            result = workflowAPI.getActivityFieldValue(session, activityInstanceID, expression, locale, isCurrentValue, transientDataContext);
                        } else {
                            result = workflowAPI.getActivityFieldValue(session, activityInstanceID, expression, locale, isCurrentValue);
                        }
                    }
                } else if (urlContext.get(FormServiceProviderUtil.INSTANCE_UUID) != null) {
                    processInstanceID = getProcessInstanceId(urlContext);
                    if (fieldValues != null) {
                        if (transientDataContext != null) {
                            result = workflowAPI.getInstanceFieldValue(session, processInstanceID, expression, fieldValues, locale, isCurrentValue,
                                    transientDataContext);
                        } else {
                            result = workflowAPI.getInstanceFieldValue(session, processInstanceID, expression, fieldValues, locale, isCurrentValue);
                        }
                    } else {
                        if (transientDataContext != null) {
                            result = workflowAPI.getInstanceFieldValue(session, processInstanceID, expression, locale, isCurrentValue, transientDataContext);
                        } else {
                            result = workflowAPI.getInstanceFieldValue(session, processInstanceID, expression, locale, isCurrentValue);
                        }
                    }
                } else if (urlContext.get(FormServiceProviderUtil.PROCESS_UUID) != null) {
                    processDefinitionID = Long.valueOf(urlContext.get(FormServiceProviderUtil.PROCESS_UUID).toString());
                    if (fieldValues != null) {
                        if (transientDataContext != null) {
                            result = workflowAPI.getProcessFieldValue(session, processDefinitionID, expression, fieldValues, locale, transientDataContext);
                        } else {
                            result = workflowAPI.getProcessFieldValue(session, processDefinitionID, expression, fieldValues, locale);
                        }
                    } else {
                        if (transientDataContext != null) {
                            result = workflowAPI.getProcessFieldValue(session, processDefinitionID, expression, locale, transientDataContext);
                        } else {
                            result = workflowAPI.getProcessFieldValue(session, processDefinitionID, expression, locale);
                        }
                    }
                } else {
                    if (getLogger().isLoggable(Level.SEVERE)) {
                        getLogger().log(Level.SEVERE, "Unable to resolve expression: " + expression + ". No process entity specified in the context");
                    }
                }
            } catch (final BPMEngineException e) {
                final String message = "Error while communicating with the engine.";
                logSevereWithContext(message, e, context);
                throw new FormNotFoundException(message);
            } catch (final BPMExpressionEvaluationException e) {
                logSevereWithContext(e.getMessage(), e, context);
                throw new FormInitializationException(e.getMessage());

            } catch (final InvalidSessionException e) {
                final String message = "The engine session is invalid.";
                if (getLogger().isLoggable(Level.FINE)) {
                    getLogger().log(Level.FINE, message, context);
                }
                throw new SessionTimeoutException(message);

            }
            if (getLogger().isLoggable(Level.FINEST)) {
                final String time = DATE_FORMAT.format(new Date());
                getLogger().log(Level.FINEST, "### " + time + " - resolveExpression - end" + expression.getExpressionType() + " " + expression.getContent());
            }
        }
        return result;
    }

    /**
     * @param session
     * @param workflowAPI
     * @param urlContext
     * @throws BPMEngineException
     * @throws FormNotFoundException
     * @throws SessionTimeoutException
     * @throws ActivityInstanceNotFoundException
     * @throws ProcessDefinitionNotFoundException
     * @throws ProcessInstanceNotFoundException
     * @throws ArchivedProcessInstanceNotFoundException
     */
    private void resolveAndSetProcessDefinitionID(final APISession session, final IFormWorkflowAPI workflowAPI, final Map<String, Object> urlContext)
            throws BPMEngineException, FormNotFoundException, SessionTimeoutException {
        long activityInstanceID = -1;
        long processDefinitionID = -1;
        long processInstanceID = -1;
        try {
            if (urlContext.containsKey(FormServiceProviderUtil.TASK_UUID)) {
                activityInstanceID = getActivityInstanceId(urlContext);
                processDefinitionID = workflowAPI.getProcessDefinitionIDFromActivityInstanceID(session, activityInstanceID);
                urlContext.put(FormServiceProviderUtil.PROCESS_UUID, Long.toString(processDefinitionID));
            } else if (urlContext.containsKey(FormServiceProviderUtil.INSTANCE_UUID)) {
                processInstanceID = getProcessInstanceId(urlContext);
                processDefinitionID = workflowAPI.getProcessDefinitionIDFromProcessInstanceID(session, processInstanceID);
                urlContext.put(FormServiceProviderUtil.PROCESS_UUID, Long.toString(processDefinitionID));
            }
            urlContext.remove(FormServiceProviderUtil.TASK_UUID);
            urlContext.remove(FormServiceProviderUtil.INSTANCE_UUID);
        } catch (final InvalidSessionException e) {
            final String message = "The engine session is invalid.";
            if (getLogger().isLoggable(Level.FINE)) {
                getLogger().log(Level.FINE, message, e);
            }
            throw new SessionTimeoutException(message);
        } catch (final ActivityInstanceNotFoundException e) {
            final String message = "The activity instance with ID " + activityInstanceID + " does not exist!";
            logSevereMessage(e, message);
            throw new FormNotFoundException(message);
        } catch (final ProcessDefinitionNotFoundException e) {
            final String message = "The process definition with ID " + processDefinitionID + " does not exist!";
            if (getLogger().isLoggable(Level.SEVERE)) {
                logSevereMessage(e, message);
            }
            throw new FormNotFoundException(message);
        } catch (final ProcessInstanceNotFoundException e) {
            final String message = "The process instance with ID " + processInstanceID + " does not exist!";
            if (getLogger().isLoggable(Level.SEVERE)) {
                logSevereMessage(e, message);
            }
            throw new FormNotFoundException(message);
        } catch (final ArchivedProcessInstanceNotFoundException e) {
            final String message = "The archived process instance with ID " + processInstanceID + " does not exist!";
            if (getLogger().isLoggable(Level.SEVERE)) {
                logSevereMessage(e, message);
            }
            throw new FormNotFoundException(message);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws FormInitializationException
     * @throws BPMEngineEvaluationExpressionException
     */
    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Serializable> resolveExpressions(final List<Expression> expressions, final Map<String, Object> context) throws FormNotFoundException,
    FormInitializationException, SessionTimeoutException, FileTooBigException, IOException {
        final FormContextUtil ctxu = new FormContextUtil(context);
        if (getLogger().isLoggable(Level.FINEST)) {
            final String time = DATE_FORMAT.format(new Date());
            getLogger().log(Level.FINEST, "### " + time + " - resolveExpressions - start - nb of expressions " + expressions.size());
        }
        long activityInstanceID = -1;
        long processDefinitionID = -1;
        long processInstanceID = -1;
        boolean isCurrentValue = false;
        final Locale locale = ctxu.getLocale();
        if (context.get(FormServiceProviderUtil.IS_CURRENT_VALUE) != null) {
            isCurrentValue = (Boolean) context.get(FormServiceProviderUtil.IS_CURRENT_VALUE);
        }
        final APISession session = ctxu.getAPISessionFromContext();
        final Map<String, Serializable> transientDataContext = (Map<String, Serializable>) context.get(FormServiceProviderUtil.TRANSIENT_DATA_CONTEXT);
        final Map<String, FormFieldValue> fieldValues = (Map<String, FormFieldValue>) context.get(FormServiceProviderUtil.FIELD_VALUES);
        Map<String, Serializable> results;
        final IFormWorkflowAPI workflowAPI = getFormWorkFlowApi();
        try {
            final Map<String, Object> urlContext = getUrlContext(context);
            if (Boolean.TRUE.equals(context.get(FormServiceProviderUtil.IS_CONFIG_CONTEXT))) {
                resolveAndSetProcessDefinitionID(session, workflowAPI, urlContext);
            }
            if (urlContext.get(FormServiceProviderUtil.TASK_UUID) != null) {
                activityInstanceID = getActivityInstanceId(urlContext);
                if (fieldValues != null) {
                    if (transientDataContext != null) {
                        results = workflowAPI.getActivityFieldsValues(session, activityInstanceID, expressions, fieldValues, locale, isCurrentValue,
                                transientDataContext);
                    } else {
                        results = workflowAPI.getActivityFieldsValues(session, activityInstanceID, expressions, fieldValues, locale, isCurrentValue);
                    }
                } else {
                    if (transientDataContext != null) {
                        results = workflowAPI.getActivityFieldsValues(session, activityInstanceID, expressions, locale, isCurrentValue, transientDataContext);
                    } else {
                        results = workflowAPI.getActivityFieldsValues(session, activityInstanceID, expressions, locale, isCurrentValue);
                    }
                }
            } else if (urlContext.get(FormServiceProviderUtil.INSTANCE_UUID) != null) {
                processInstanceID = getProcessInstanceId(urlContext);
                if (fieldValues != null) {
                    if (transientDataContext != null) {
                        results = workflowAPI.getInstanceFieldsValues(session, processInstanceID, expressions, fieldValues, locale, isCurrentValue,
                                transientDataContext);
                    } else {
                        results = workflowAPI.getInstanceFieldsValues(session, processInstanceID, expressions, fieldValues, locale, isCurrentValue);
                    }
                } else {
                    if (transientDataContext != null) {
                        results = workflowAPI.getInstanceFieldsValues(session, processInstanceID, expressions, locale, isCurrentValue, transientDataContext);
                    } else {
                        results = workflowAPI.getInstanceFieldsValues(session, processInstanceID, expressions, locale, isCurrentValue);
                    }
                }
            } else if (urlContext.get(FormServiceProviderUtil.PROCESS_UUID) != null) {
                processDefinitionID = Long.valueOf(urlContext.get(FormServiceProviderUtil.PROCESS_UUID).toString());
                if (fieldValues != null) {
                    if (transientDataContext != null) {
                        results = workflowAPI.getProcessFieldsValues(session, processDefinitionID, expressions, fieldValues, locale, transientDataContext);
                    } else {
                        results = workflowAPI.getProcessFieldsValues(session, processDefinitionID, expressions, fieldValues, locale);
                    }
                } else {
                    if (transientDataContext != null) {
                        results = workflowAPI.getProcessFieldsValues(session, processDefinitionID, expressions, locale, transientDataContext);
                    } else {
                        results = workflowAPI.getProcessFieldsValues(session, processDefinitionID, expressions, locale);
                    }
                }
            } else {
                if (getLogger().isLoggable(Level.SEVERE)) {
                    getLogger().log(Level.SEVERE, "Unable to resolve expressions: " + expressions + ". No process entity specified in the context");
                }
                results = new HashMap<String, Serializable>();
            }
        } catch (final BPMEngineException e) {
            final String message = "Error while communicating with the engine.";
            logSevereWithContext(message, e, context);
            throw new FormNotFoundException(message);

        } catch (final BPMExpressionEvaluationException e) {
            logSevereWithContext(e.getMessage(), e, context);
            throw new FormInitializationException(e.getMessage());

        } catch (final InvalidSessionException e) {
            final String message = "The engine session is invalid.";
            if (getLogger().isLoggable(Level.FINE)) {
                getLogger().log(Level.FINE, message, e);
            }
            throw new SessionTimeoutException(message);
        }
        if (getLogger().isLoggable(Level.FINEST)) {
            final String time = DATE_FORMAT.format(new Date());
            getLogger().log(Level.FINEST, "### " + time + " - resolveExpressions - end - nb of expressions " + expressions.size());
        }
        return results;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> executeActions(final List<FormAction> actions, final Map<String, Object> context) throws FileTooBigException,
    FormNotFoundException, FormAlreadySubmittedException, FormSubmissionException, SessionTimeoutException, IOException {
        final FormContextUtil formContextUtil = new FormContextUtil(context);
        if (getLogger().isLoggable(Level.FINEST)) {
            final String time = DATE_FORMAT.format(new Date());
            getLogger().log(Level.FINEST, "### " + time + " - executeActions - start - nb of actions " + actions.size());
        }
        // init vars
        long activityInstanceID = -1;
        long processDefinitionID = -1;
        long userID = -1;
        String submitButtonId = null;
        final Map<String, Object> urlContext = formContextUtil.getUrlContext();
        // retrieve the locale from the context
        final Locale locale = formContextUtil.getLocale();
        FormLogger.setContext(context);

        // retrieve session from the context
        final APISession session = formContextUtil.getAPISessionFromContext();

        final Map<String, FormFieldValue> fieldValues = convertFormFieldValues((Map<String, FormFieldValue>) context.get(FormServiceProviderUtil.FIELD_VALUES),
                true);
        final Map<String, Serializable> transientDataContext = (Map<String, Serializable>) context.get(FormServiceProviderUtil.TRANSIENT_DATA_CONTEXT);
        if (context.get(FormServiceProviderUtil.SUBMIT_BUTTON_ID) != null) {
            submitButtonId = (String) context.get(FormServiceProviderUtil.SUBMIT_BUTTON_ID);
        }
        final IFormWorkflowAPI workflowAPI = getFormWorkFlowApi();
        long newProcessInstanceID;

        try {
            if (urlContext.get(FormServiceProviderUtil.PROCESS_UUID) != null) {
                // retrieve the userid from the context
                userID = formContextUtil.getUserId();
                // retrieve the process definition id from the context
                processDefinitionID = formContextUtil.getProcessDefinitionId();

                newProcessInstanceID = workflowAPI.executeActionsAndStartInstance(session, userID, processDefinitionID, fieldValues, actions, locale,
                        submitButtonId, transientDataContext);
                urlContext.remove(FormServiceProviderUtil.PROCESS_UUID);
                urlContext.remove(FormServiceProviderUtil.TASK_UUID);
                urlContext.put(FormServiceProviderUtil.INSTANCE_UUID, String.valueOf(newProcessInstanceID));
                // update the context in the contextutil
                formContextUtil.setUrlContext(urlContext);

            } else if (urlContext.get(FormServiceProviderUtil.TASK_UUID) != null) {
                // retrieve the userid from the context
                userID = formContextUtil.getUserId();

                activityInstanceID = getActivityInstanceId(urlContext);
                boolean executeActions = false;
                if (urlContext.containsKey(EXECUTE_ACTIONS_PARAM)) {
                    final String executeActionsStr = urlContext.get(EXECUTE_ACTIONS_PARAM).toString();
                    executeActions = Boolean.parseBoolean(executeActionsStr);
                }
                if (submitButtonId != null || executeActions) {
                    final boolean isTaskReady = workflowAPI.isTaskReady(session, activityInstanceID);
                    if (isTaskReady) {
                        workflowAPI.executeActionsAndTerminate(session, userID, activityInstanceID, fieldValues, actions, locale, submitButtonId,
                                transientDataContext);
                    } else {
                        throw new FormAlreadySubmittedException();
                    }
                }
            }
        } catch (final FormAlreadySubmittedException e) {
            final String message = "The task with ID " + activityInstanceID + " has already been executed";
            if (getLogger().isLoggable(Level.FINE)) {
                getLogger().log(Level.FINE, message);
            }
            throw new FormAlreadySubmittedException(message);
        } catch (final ProcessInstanceNotFoundException e) {
            final String message = "The process instance for activity instance with ID " + activityInstanceID + " does not exist!";
            logSevereWithContext(message, e, context);
            throw new FormNotFoundException(message);
        } catch (final ActivityInstanceNotFoundException e) {
            final String message = "The activity instance with ID " + activityInstanceID + " does not exist!";
            logSevereMessageWithContext(e, e.getMessage(), context);
            throw new FormNotFoundException(message);
        } catch (final FileTooBigException e) {
            if (getLogger().isLoggable(Level.WARNING)) {
                getLogger().log(Level.WARNING, e.getMessage(), e);
            }
            throw new FileTooBigException(e.getMessage(), e.getFileName(), e.getMaxSize());
        } catch (final BPMEngineException e) {
            final String message = "Error while communicating with the engine.";
            logSevereWithContext(message, e, context);
            throw new FormNotFoundException(message);
        } catch (final InvalidSessionException e) {
            final String message = "The engine session is invalid.";
            if (getLogger().isLoggable(Level.FINE)) {
                getLogger().log(Level.FINE, message, e);
            }
            throw new SessionTimeoutException(message);
        } catch (final Exception e) {
            if (getLogger().isLoggable(Level.SEVERE)) {
                getLogger().log(Level.SEVERE, "Error while executing Actions task", e);
            }
            throw new FormSubmissionException(e.getMessage(), e);
        }
        if (getLogger().isLoggable(Level.FINEST)) {
            final String time = DATE_FORMAT.format(new Date());
            getLogger().log(Level.FINEST, "### " + time + " - executeActions - end - nb of actions " + actions.size());
        }
        return urlContext;
    }

    /**
     * Convert type of all fields contains in formFieldValues
     *
     * @param formFieldValues
     * @param throwException
     * @return
     * @throws FileTooBigException
     * @throws IOException
     */
    protected Map<String, FormFieldValue> convertFormFieldValues(final Map<String, FormFieldValue> formFieldValues, final boolean throwException) {
        if (formFieldValues != null && !formFieldValues.isEmpty()) {
            for (final FormFieldValue formFieldValue : formFieldValues.values()) {
                convertValueType(formFieldValue, throwException);
            }
        }
        return formFieldValues;
    }

    /**
     * Convert type if a working modifier is found in {@link FormFieldValue}
     *
     * @param formFieldValue
     * @param throwException
     * @return
     * @throws Exception
     */
    protected FormFieldValue convertValueType(final FormFieldValue formFieldValue, final boolean throwException) {
        final String modifier = formFieldValue.getModifier();

        if (!isStringEmptyOrBlank(modifier)) {
            Serializable convertedObj = null;
            final String valueAsString = formFieldValue.getValue() == null ? "" : String.valueOf(formFieldValue.getValue());
            try {
                if (Boolean.class.getName().equals(modifier)) {
                    convertedObj = isStringEmptyOrBlank(valueAsString) ? false : Boolean.valueOf(valueAsString);
                } else if (Long.class.getName().equals(modifier)) {
                    convertedObj = isStringEmptyOrBlank(valueAsString) ? DEFAULT_NUM_VALUE : Long.valueOf(valueAsString);
                } else if (Double.class.getName().equals(modifier)) {
                    convertedObj = isStringEmptyOrBlank(valueAsString) ? DEFAULT_NUM_VALUE : Double.valueOf(valueAsString);
                } else if (Integer.class.getName().equals(modifier)) {
                    convertedObj = isStringEmptyOrBlank(valueAsString) ? DEFAULT_NUM_VALUE : Integer.valueOf(valueAsString);
                } else if (Float.class.getName().equals(modifier)) {
                    convertedObj = isStringEmptyOrBlank(valueAsString) ? DEFAULT_NUM_VALUE : Float.valueOf(valueAsString);
                } else if (Short.class.getName().equals(modifier)) {
                    convertedObj = isStringEmptyOrBlank(valueAsString) ? DEFAULT_NUM_VALUE : Short.valueOf(valueAsString);
                } else if (Character.class.getName().equals(modifier)) {
                    convertedObj = isStringEmptyOrBlank(valueAsString) ? Character.valueOf(' ') : Character.valueOf(valueAsString.charAt(0));
                } else if (String.class.getName().equals(modifier)) {
                    convertedObj = valueAsString;
                } else {
                    final String message = "Type " + modifier + " is not handled.";
                    if (getLogger().isLoggable(Level.WARNING)) {
                        getLogger().log(Level.WARNING, message);
                    }
                }
            } catch (final IllegalArgumentException e) {
                // if no exception is requested, the value of the field is not converted
                if (throwException) {
                    throw e;
                }
            }
            if (convertedObj != null) {
                formFieldValue.setValue(convertedObj);
            }
        }
        return formFieldValue;
    }

    /**
     * Check if str is empty or blank
     *
     * @param str
     * @return
     */
    private boolean isStringEmptyOrBlank(final String str) {
        return str == null || str.trim().isEmpty() ? true : false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FormURLComponents getNextFormURLParameters(final String formId, final Map<String, Object> context) throws FormNotFoundException,
    SessionTimeoutException {
        final FormContextUtil ctxu = new FormContextUtil(context);
        logTime("getNextFormURLParameters - start");
        final Map<String, Object> urlContext = getUrlContext(context);
        final APISession session = ctxu.getAPISessionFromContext();
        long processInstanceID = -1;
        FormURLComponents urlComponents = null;
        final IFormWorkflowAPI workflowAPI = getFormWorkFlowApi();
        try {
            if (urlContext.get(FormServiceProviderUtil.TASK_UUID) != null) {
                processInstanceID = getProcessInstanceIdParent(session, workflowAPI, getActivityInstanceId(urlContext));
            } else if (urlContext.get(FormServiceProviderUtil.INSTANCE_UUID) != null) {
                processInstanceID = getProcessInstanceId(urlContext);
            }

            final long activityInstanceId = getNextActivityInstanceId(getFormWorkFlowApi(), session, processInstanceID);
            if (activityInstanceId != -1) {
                urlComponents = new FormURLComponents();
                urlComponents.setApplicationURL(getAppDedicatedUrl(session, workflowAPI, activityInstanceId, UI_FORM_MODE));
                if (urlContext != null) {
                    final String activityDefinitionUuid = getActivityDefinitionUUID(session, workflowAPI, activityInstanceId);
                    final Map<String, Object> newURLContext = new HashMap<String, Object>(urlContext);
                    newURLContext.remove(FormServiceProviderUtil.PROCESS_UUID);
                    newURLContext.remove(FormServiceProviderUtil.INSTANCE_UUID);
                    newURLContext.put(FormServiceProviderUtil.FORM_ID, createFormIdFromUuid(activityDefinitionUuid, FormServiceProviderUtil.ENTRY_FORM_TYPE));
                    newURLContext.put(FormServiceProviderUtil.TASK_UUID, String.valueOf(activityInstanceId));
                    urlComponents.setUrlContext(newURLContext);
                }

                final String taskName = getActivityName(session, workflowAPI, activityInstanceId);
                urlComponents.setTaskName(taskName);

                urlComponents.setTaskId(activityInstanceId);
            }
        } catch (final FormWorflowApiException e) {
            logSevereMessageWithContext(e, e.getMessage(), context);
        } catch (final InvalidSessionException e) {
            final String message = "The engine session is invalid.";
            logInfoMessageWithContext(message, e, context);
            throw new SessionTimeoutException(message);
        }
        logTime("getNextFormURLParameters - end");
        return urlComponents;
    }

    private String getAppDedicatedUrl(final APISession session, final IFormWorkflowAPI workflowAPI, final long activityInstanceId, final String uiMode)
            throws FormWorflowApiException, InvalidSessionException {
        final long processDefinitionId = getProcessDefinitionId(session, workflowAPI, activityInstanceId);
        return ApplicationURLUtils.getInstance().getDedicatedApplicationUrl(processDefinitionId, uiMode);
    }

    private long getProcessDefinitionId(final APISession session, final IFormWorkflowAPI workflowAPI, final long activityInstanceId)
            throws FormWorflowApiException, InvalidSessionException {
        try {
            return workflowAPI.getProcessDefinitionIDFromActivityInstanceID(session, activityInstanceId);
        } catch (final ActivityInstanceNotFoundException e) {
            final String message = "The activity instance with ID " + activityInstanceId + " does not exist!";
            throw new FormWorflowApiException(message, e);
        } catch (final BPMEngineException e) {
            final String message = "Error while communicating with the engine.";
            throw new FormWorflowApiException(message, e);
        }
    }

    private String getActivityName(final APISession session, final IFormWorkflowAPI workflowAPI, final long activityInstanceId) throws InvalidSessionException,
    FormWorflowApiException {
        try {
            return workflowAPI.getActivityName(session, activityInstanceId);
        } catch (final ActivityInstanceNotFoundException e) {
            final String message = "The activity instance with ID " + activityInstanceId + " does not exist!";
            throw new FormWorflowApiException(message, e);
        } catch (final BPMEngineException e) {
            final String message = "Error while communicating with the engine.";
            throw new FormWorflowApiException(message, e);
        }
    }

    private String getActivityDefinitionUUID(final APISession session, final IFormWorkflowAPI workflowAPI, final long activityInstanceId)
            throws InvalidSessionException, FormWorflowApiException {
        try {
            return workflowAPI.getActivityDefinitionUUIDFromActivityInstanceID(session, activityInstanceId);
        } catch (final ActivityInstanceNotFoundException e) {
            final String message = "The activity instance with ID " + activityInstanceId + " does not exist!";
            throw new FormWorflowApiException(message, e);
        } catch (final ProcessDefinitionNotFoundException e) {
            final String message = "The engine was not able to read activity instance with ID " + activityInstanceId;
            throw new FormWorflowApiException(message, e);
        } catch (final BPMEngineException e) {
            final String message = "Error while communicating with the engine.";
            throw new FormWorflowApiException(message, e);
        }
    }

    private String createFormIdFromUuid(final String uuid, final String entryFormType) {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(uuid);
        stringBuilder.append(FormServiceProviderUtil.FORM_ID_SEPARATOR);
        stringBuilder.append(entryFormType);
        return stringBuilder.toString();
    }

    /**
     * Fetch process instance id from which the activity instance is coming from
     */
    private long getProcessInstanceIdParent(final APISession session, final IFormWorkflowAPI workflowAPI, final long activityInstanceID)
            throws FormWorflowApiException, InvalidSessionException {
        try {
            return workflowAPI.getProcessInstanceIDFromActivityInstanceID(session, activityInstanceID);
        } catch (final ActivityInstanceNotFoundException e) {
            final String message = "The activity instance with ID " + activityInstanceID + " does not exist!";
            throw new FormWorflowApiException(message, e);
        } catch (final BPMEngineException e) {
            final String message = "Error while communicating with the engine.";
            throw new FormWorflowApiException(message, e);
        }
    }

    private long getActivityInstanceId(final Map<String, Object> urlContext) {
        return getParsedLongSafely(urlContext, FormServiceProviderUtil.TASK_UUID);
    }

    private long getProcessInstanceId(final Map<String, Object> urlContext) {
        return getParsedLongSafely(urlContext, FormServiceProviderUtil.INSTANCE_UUID);
    }

    private long getParsedLongSafely(final Map<String, Object> urlContext, final String key) {
        long id = -1;
        if (urlContext.get(key) != null) {
            id = Long.parseLong(urlContext.get(key).toString());
        }
        return id;
    }

    private long getNextActivityInstanceId(final IFormWorkflowAPI formWorkflowApi, final APISession session, final long processInstanceId)
            throws FormWorflowApiException {
        long activityInstanceId = -1L;
        try {
            activityInstanceId = formWorkflowApi.getRelatedProcessesNextTask(session, processInstanceId);
        } catch (final UserNotFoundException e) {
            final String message = "The user with ID " + session.getUserId() + " does not exist!";
            throw new FormWorflowApiException(message, e);
        } catch (final InvalidSessionException e) {
            final String message = "The engine session is invalid.";
            throw new FormWorflowApiException(message, e);
        } catch (final BPMEngineException e) {
            final String message = "Error while communicating with the engine.";
            throw new FormWorflowApiException(message, e);
        } catch (final SearchException e) {
            final String message = "Error while searching available task.";
            throw new FormWorflowApiException(message, e);
        } catch (final ProcessDefinitionNotFoundException e) {
            final String message = "The engine was not able to read process definition";
            throw new FormWorflowApiException(message, e);
        }
        return activityInstanceId;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getUrlContext(final Map<String, Object> context) {
        return (Map<String, Object>) context.get(FormServiceProviderUtil.URL_CONTEXT);
    }

    protected IFormWorkflowAPI getFormWorkFlowApi() {
        return FormAPIFactory.getFormWorkflowAPI();
    }

    private boolean isAssignTask(final Map<String, Object> context) {

        if (context.containsKey(FormServiceProviderUtil.ASSIGN_TASK)) {
            return Boolean.parseBoolean((String) context.get(FormServiceProviderUtil.ASSIGN_TASK));
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> getAttributesToInsert(final Map<String, Object> context) throws FormNotFoundException, SessionTimeoutException {
        final FormContextUtil ctxu = new FormContextUtil(context);
        if (getLogger().isLoggable(Level.FINEST)) {
            final String time = DATE_FORMAT.format(new Date());
            getLogger().log(Level.FINEST, "### " + time + " - getAttributesToInsert - start");
        }
        final Map<String, Object> urlContext = getUrlContext(context);
        long activityInstanceID = -1;
        if (urlContext.get(FormServiceProviderUtil.TASK_UUID) != null) {
            activityInstanceID = getActivityInstanceId(urlContext);
        }
        final Locale locale = ctxu.getLocale();
        final IFormWorkflowAPI workflowAPI = getFormWorkFlowApi();
        Map<String, String> attributes = null;
        try {
            if (activityInstanceID != -1) {
                final APISession session = ctxu.getAPISessionFromContext();
                attributes = workflowAPI.getActivityAttributes(session, activityInstanceID, locale);
            }
        } catch (final ActivityInstanceNotFoundException e) {
            final String message = "The activity instance with ID " + activityInstanceID + " does not exist!";
            logSevereMessageWithContext(e, e.getMessage(), context);
            throw new FormNotFoundException(message);
        } catch (final BPMEngineException e) {
            final String message = "Error while communicating with the engine.";
            logSevereWithContext(message, e, context);
            throw new FormNotFoundException(message);
        } catch (final InvalidSessionException e) {
            final String message = "The engine session is invalid.";
            if (getLogger().isLoggable(Level.FINE)) {
                getLogger().log(Level.FINE, message, e);
            }
            throw new SessionTimeoutException(message);
        }
        if (getLogger().isLoggable(Level.FINEST)) {
            final String time = DATE_FORMAT.format(new Date());
            getLogger().log(Level.FINEST, "### " + time + " - getAttributesToInsert - end");
        }
        return attributes;
    }

    /**
     * {@inheritDoc}
     *
     * @throws BPMExpressionEvaluationException
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<FormValidator> validateField(final List<FormValidator> validators, final String fieldId, final FormFieldValue fieldValue,
            final String submitButtonId, final Map<String, Object> context) throws FormValidationException, FormNotFoundException, SessionTimeoutException,
            FileTooBigException, IOException, BPMExpressionEvaluationException {
        final FormContextUtil ctxu = new FormContextUtil(context);
        if (getLogger().isLoggable(Level.FINEST)) {
            final String time = DATE_FORMAT.format(new Date());
            getLogger().log(Level.FINEST, "### " + time + " - validateField - start " + fieldId);
        }
        long activityInstanceID = -1;
        long processDefinitionID = -1;
        long processInstanceID = -1;
        final Locale locale = ctxu.getLocale();
        final IFormValidationAPI validationAPI = FormAPIFactory.getFormValidationAPI();
        final Map<String, Serializable> transientDataContext = (Map<String, Serializable>) context.get(FormServiceProviderUtil.TRANSIENT_DATA_CONTEXT);
        List<FormValidator> nonCompliantFieldValidators = null;
        convertValueType(fieldValue, false);
        try {
            final APISession session = ctxu.getAPISessionFromContext();
            final Map<String, Object> urlContext = getUrlContext(context);
            if (urlContext.get(FormServiceProviderUtil.TASK_UUID) != null) {
                activityInstanceID = getActivityInstanceId(urlContext);
                nonCompliantFieldValidators = validationAPI.validateActivityField(session, activityInstanceID, validators, fieldId, fieldValue, submitButtonId,
                        locale, transientDataContext);
            } else if (urlContext.get(FormServiceProviderUtil.PROCESS_UUID) != null) {
                processDefinitionID = Long.valueOf(urlContext.get(FormServiceProviderUtil.PROCESS_UUID).toString());
                nonCompliantFieldValidators = validationAPI.validateProcessField(session, processDefinitionID, validators, fieldId, fieldValue, submitButtonId,
                        locale, transientDataContext);
            } else if (urlContext.get(FormServiceProviderUtil.INSTANCE_UUID) != null) {
                processInstanceID = getProcessInstanceId(urlContext);
                nonCompliantFieldValidators = validationAPI.validateInstanceField(session, processInstanceID, validators, fieldId, fieldValue, submitButtonId,
                        locale, transientDataContext);
            }
        } catch (final ProcessInstanceNotFoundException e) {
            final String message = "The process instance with ID " + processInstanceID + " does not exist!";
            logSevereWithContext(message, e, context);
            throw new FormNotFoundException(message);
        } catch (final ActivityInstanceNotFoundException e) {
            final String message = "The activity instance with ID " + activityInstanceID + " does not exist!";
            logSevereMessageWithContext(e, e.getMessage(), context);
            throw new FormNotFoundException(message);
        } catch (final ArchivedFlowNodeInstanceNotFoundException e) {
            final String message = "The archived activity instance with ID " + activityInstanceID + " does not exist!";
            logSevereMessageWithContext(e, e.getMessage(), context);
            throw new FormNotFoundException(message);
        } catch (final ProcessDefinitionNotFoundException e) {
            final String message = "The process definition with ID " + processDefinitionID + " does not exist!";
            logSevereWithContext(message, e, context);
            throw new FormNotFoundException(message);
        } catch (final BPMEngineException e) {
            final String message = "Error while communicating with the engine.";
            logSevereWithContext(message, e, context);
            throw new FormNotFoundException(message);
        } catch (final InvalidSessionException e) {
            final String message = "The engine session is invalid.";
            if (getLogger().isLoggable(Level.FINE)) {
                getLogger().log(Level.FINE, message, e);
            }
            throw new SessionTimeoutException(message);

        } catch (final ArchivedProcessInstanceNotFoundException e) {
            final String message = "Archvied process instance not foud";
            logSevereWithContext(message, e, context);
            throw new FormNotFoundException(message);
        }
        if (getLogger().isLoggable(Level.FINEST)) {
            final String time = DATE_FORMAT.format(new Date());
            getLogger().log(Level.FINEST, "### " + time + " - validateField - end " + fieldId);
        }
        return nonCompliantFieldValidators;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IOException
     * @throws FileTooBigException
     * @throws BPMExpressionEvaluationException
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<FormValidator> validatePage(final List<FormValidator> validators, final Map<String, FormFieldValue> fields, final String submitButtonId,
            final Map<String, Object> context) throws FormValidationException, FormNotFoundException, SessionTimeoutException, FileTooBigException,
            IOException, BPMExpressionEvaluationException {
        final FormContextUtil ctxu = new FormContextUtil(context);
        if (getLogger().isLoggable(Level.FINEST)) {
            final String time = DATE_FORMAT.format(new Date());
            getLogger().log(Level.FINEST, "### " + time + " - validatePage - start");
        }
        long activityInstanceID = -1;
        long processDefinitionID = -1;
        long processInstanceID = -1;
        final Locale locale = ctxu.getLocale();
        final IFormValidationAPI validationAPI = FormAPIFactory.getFormValidationAPI();
        final Map<String, Serializable> transientDataContext = (Map<String, Serializable>) context.get(FormServiceProviderUtil.TRANSIENT_DATA_CONTEXT);
        List<FormValidator> nonCompliantFieldValidators = null;
        final APISession session = ctxu.getAPISessionFromContext();
        convertFormFieldValues(fields, false);
        try {
            final Map<String, Object> urlContext = getUrlContext(context);
            if (urlContext.get(FormServiceProviderUtil.TASK_UUID) != null) {
                activityInstanceID = getActivityInstanceId(urlContext);
                nonCompliantFieldValidators = validationAPI.validateActivityPage(session, activityInstanceID, validators, fields, submitButtonId, locale,
                        transientDataContext);
            } else if (urlContext.get(FormServiceProviderUtil.PROCESS_UUID) != null) {
                processDefinitionID = Long.valueOf(urlContext.get(FormServiceProviderUtil.PROCESS_UUID).toString());
                nonCompliantFieldValidators = validationAPI.validateProcessPage(session, processDefinitionID, validators, fields, submitButtonId, locale,
                        transientDataContext);
            } else if (urlContext.get(FormServiceProviderUtil.INSTANCE_UUID) != null) {
                processInstanceID = getProcessInstanceId(urlContext);
                nonCompliantFieldValidators = validationAPI.validateInstancePage(session, processInstanceID, validators, fields, submitButtonId, locale,
                        transientDataContext);
            }
        } catch (final ProcessInstanceNotFoundException e) {
            final String message = "The process instance with ID " + processInstanceID + " does not exist!";
            logSevereWithContext(message, e, context);
            throw new FormNotFoundException(message);
        } catch (final ActivityInstanceNotFoundException e) {
            final String message = "The activity instance with ID " + activityInstanceID + " does not exist!";
            logSevereMessageWithContext(e, e.getMessage(), context);
            throw new FormNotFoundException(message);
        } catch (final ArchivedFlowNodeInstanceNotFoundException e) {
            final String message = "The archived activity instance with ID " + activityInstanceID + " does not exist!";
            logSevereMessageWithContext(e, e.getMessage(), context);
            throw new FormNotFoundException(message);
        } catch (final ProcessDefinitionNotFoundException e) {
            final String message = "The process definition with ID " + processDefinitionID + " does not exist!";
            logSevereWithContext(message, e, context);
            throw new FormNotFoundException(message);
        } catch (final BPMEngineException e) {
            final String message = "Error while communicating with the engine.";
            logSevereWithContext(message, e, context);
            throw new FormNotFoundException(message);
        } catch (final InvalidSessionException e) {
            final String message = "The engine session is invalid.";
            if (getLogger().isLoggable(Level.FINE)) {
                getLogger().log(Level.FINE, message, e);
            }
            throw new SessionTimeoutException(message);

        } catch (final ArchivedProcessInstanceNotFoundException e) {
            final String message = "Archived process instance not found";
            logSevereWithContext(message, e, context);
            throw new FormNotFoundException(message);
        }
        if (getLogger().isLoggable(Level.FINEST)) {
            final String time = DATE_FORMAT.format(new Date());
            getLogger().log(Level.FINEST, "### " + time + " - validatePage - end");
        }
        return nonCompliantFieldValidators;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getDeployementDate(final Map<String, Object> context) throws FormNotFoundException, IOException, SessionTimeoutException {
        final FormContextUtil ctxu = new FormContextUtil(context);
        if (getLogger().isLoggable(Level.FINEST)) {
            final String time = DATE_FORMAT.format(new Date());
            getLogger().log(Level.FINEST, "### " + time + " - getDeployementDate - start");
        }
        Date processDeployementDate = null;
        long processDefinitionID = -1;
        try {
            processDefinitionID = getProcessDefinitionID(context);
            final APISession session = ctxu.getAPISessionFromContext();
            processDeployementDate = getDeployementDate(session, processDefinitionID);
        } catch (final ProcessDefinitionNotFoundException e) {
            final String message = "The process with UUID " + processDefinitionID + " does not exist!";
            logSevereWithContext(message, e, context);
            throw new FormNotFoundException(message);
        } catch (final InvalidSessionException e) {
            final String message = "The engine session is invalid.";
            if (getLogger().isLoggable(Level.FINE)) {
                getLogger().log(Level.FINE, message, e);
            }
            throw new SessionTimeoutException(message);
        }
        if (getLogger().isLoggable(Level.FINEST)) {
            final String time = DATE_FORMAT.format(new Date());
            getLogger().log(Level.FINEST, "### " + time + " - getDeployementDate - end");
        }
        return processDeployementDate;
    }

    /**
     * Retrieve the process deployementDate
     *
     * @param session
     *        the API session
     * @param processDefinitionID
     *        the process definition ID
     * @return a {@link Date}
     * @throws ProcessDefinitionNotFoundException
     * @throws IOException
     * @throws SessionTimeoutException
     */
    protected Date getDeployementDate(final APISession session, final long processDefinitionID) throws ProcessDefinitionNotFoundException, IOException,
    SessionTimeoutException {
        Date processDeployementDate = null;
        if (processDefinitionID != -1) {
            try {
                final IFormWorkflowAPI workflowAPI = getFormWorkFlowApi();
                processDeployementDate = workflowAPI.getMigrationDate(session, processDefinitionID);
                if (processDeployementDate == null) {
                    processDeployementDate = workflowAPI.getProcessDefinitionDate(session, processDefinitionID);
                } else {
                    final File oldFormsDir = FormsResourcesUtils.getApplicationResourceDir(session, processDefinitionID,
                            workflowAPI.getProcessDefinitionDate(session, processDefinitionID));
                    if (oldFormsDir.exists()) {
                        FormsResourcesUtils.removeApplicationFiles(session, processDefinitionID);
                        FormCacheUtilFactory.getTenantFormCacheUtil(session.getTenantId()).clearAll();
                    }
                }
            } catch (final ProcessDefinitionNotFoundException e) {
                final String message = "The process definition with ID " + processDefinitionID + " does not exist!";
                if (getLogger().isLoggable(Level.SEVERE)) {
                    logSevereMessage(e, message);
                }
                throw new ProcessDefinitionNotFoundException(message);
            } catch (final BPMEngineException e) {
                final String message = "Error while communicating with the engine.";
                if (getLogger().isLoggable(Level.SEVERE)) {
                    logSevereMessage(e, message);
                }
                throw new ProcessDefinitionNotFoundException(message);
            } catch (final InvalidSessionException e) {
                final String message = "The engine session is invalid.";
                if (getLogger().isLoggable(Level.FINE)) {
                    getLogger().log(Level.FINE, message, e);
                }
                throw new SessionTimeoutException(message);
            }
            return processDeployementDate;
        }
        return processDeployementDate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IApplicationConfigDefAccessor getApplicationConfigDefinition(final Document formDefinitionDocument, final Map<String, Object> context)
            throws SessionTimeoutException, ApplicationFormDefinitionNotFoundException {
        final FormContextUtil ctxu = new FormContextUtil(context);
        if (getLogger().isLoggable(Level.FINEST)) {
            final String time = DATE_FORMAT.format(new Date());
            getLogger().log(Level.FINEST, "### " + time + " - getApplicationConfigDefinition - start");
        }
        final APISession session = ctxu.getAPISessionFromContext();
        IApplicationConfigDefAccessor applicationConfigDefAccessor = null;
        if (formDefinitionDocument == null) {
            try {
                final long processDefinitionID = getProcessDefinitionID(context);
                applicationConfigDefAccessor = new EngineApplicationConfigDefAccessorImpl(session, processDefinitionID);
            } catch (final InvalidSessionException e) {
                final String message = "The engine session is invalid.";
                if (getLogger().isLoggable(Level.FINE)) {
                    getLogger().log(Level.FINE, message, e);
                }
                throw new SessionTimeoutException(message);
            }
        } else {
            applicationConfigDefAccessor = new XMLApplicationConfigDefAccessorImpl(session.getTenantId(), formDefinitionDocument);
        }
        if (getLogger().isLoggable(Level.FINEST)) {
            final String time = DATE_FORMAT.format(new Date());
            getLogger().log(Level.FINEST, "### " + time + " - getApplicationConfigDefinition - end");
        }
        return applicationConfigDefAccessor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IApplicationFormDefAccessor getApplicationFormDefinition(final String formId, final Document formDefinitionDocument,
            final Map<String, Object> context) throws ApplicationFormDefinitionNotFoundException, InvalidFormDefinitionException, SessionTimeoutException {

        if (getLogger().isLoggable(Level.FINEST)) {
            final String time = DATE_FORMAT.format(new Date());
            getLogger().log(Level.FINEST, "### " + time + " - getApplicationFormDefinition - start");
        }
        IApplicationFormDefAccessor iApplicationDefAccessor = null;
        final Date applicationDeploymentDate = (Date) context.get(FormServiceProviderUtil.APPLICATION_DEPLOYMENT_DATE);
        try {
            iApplicationDefAccessor = getApplicationFormDefinition(formId, formDefinitionDocument, applicationDeploymentDate, context);
        } catch (final InvalidSessionException e) {
            final String message = "The engine session is invalid.";
            if (getLogger().isLoggable(Level.FINE)) {
                getLogger().log(Level.FINE, message, e);
            }
            throw new SessionTimeoutException(message);
        }
        if (getLogger().isLoggable(Level.FINEST)) {
            final String time = DATE_FORMAT.format(new Date());
            getLogger().log(Level.FINEST, "### " + time + " - getApplicationFormDefinition - end");
        }
        return iApplicationDefAccessor;
    }

    /**
     * Get a Form Definition Accessor object
     *
     * @param formId
     *        the form ID
     * @param formDefinitionDocument
     * @param applicationDeploymentDate
     * @param context
     *        the context of URL parameters
     * @return an instance of {@link IApplicationFormDefAccessor}
     * @throws ApplicationFormDefinitionNotFoundException
     * @throws InvalidFormDefinitionException
     * @throws InvalidSessionException
     */
    protected IApplicationFormDefAccessor getApplicationFormDefinition(final String formId, final Document formDefinitionDocument,
            final Date applicationDeploymentDate, final Map<String, Object> context) throws ApplicationFormDefinitionNotFoundException,
            InvalidSessionException {
        final FormContextUtil ctxu = new FormContextUtil(context);
        IApplicationFormDefAccessor formDefAccessor = null;
        long processDefinitionID = getProcessDefinitionID(context);
        final Map<String, Object> urlContext = getUrlContext(context);
        long activityInstanceID = -1;
        if (urlContext.get(FormServiceProviderUtil.TASK_UUID) != null) {
            activityInstanceID = getActivityInstanceId(urlContext);
        }
        String formType = null;
        try {
            formType = getFormType(formId);
        } catch (final FormNotFoundException e) {
            throw new ApplicationFormDefinitionNotFoundException(e);
        }
        boolean isCurrentValue = true;
        final Boolean isCurrentValueFromContext = (Boolean) context.get(FormServiceProviderUtil.IS_CURRENT_VALUE);
        if (isCurrentValueFromContext != null) {
            isCurrentValue = isCurrentValueFromContext.booleanValue();
        }
        boolean isEditMode = false;
        if (FormServiceProviderUtil.ENTRY_FORM_TYPE.equals(formType)) {
            isEditMode = true;
        }
        boolean isConfirmationPage = false;
        final Boolean isConfirmationPageFromContext = (Boolean) context.get(FormServiceProviderUtil.IS_CONFIRMATION_PAGE);
        if (isConfirmationPageFromContext != null) {
            isConfirmationPage = isConfirmationPageFromContext.booleanValue();
        }
        final APISession session = ctxu.getAPISessionFromContext();
        if (formDefinitionDocument == null) {
            formDefAccessor = FormDefAccessorFactory.getEngineApplicationFormDefAccessor(session, processDefinitionID, activityInstanceID, true, isEditMode,
                    isCurrentValue, isConfirmationPage);
        } else {
            try {
                final Locale locale = ctxu.getLocale();
                String localeString = null;
                if (locale != null) {
                    localeString = locale.getLanguage();
                }
                formDefAccessor = FormDefAccessorFactory.getXMLApplicationFormDefAccessor(session, processDefinitionID, formDefinitionDocument, formId,
                        localeString, applicationDeploymentDate);
            } catch (final ApplicationFormDefinitionNotFoundException e) {
                if (getLogger().isLoggable(Level.FINE)) {
                    getLogger().log(Level.FINE,
                            "No form definition was found for the form " + formId + ". The forms will be generated using the engine variables.");
                }
                formDefAccessor = FormDefAccessorFactory.getEngineApplicationFormDefAccessor(session, processDefinitionID, activityInstanceID, true,
                        isEditMode, isCurrentValue, isConfirmationPage);
            }
        }
        return formDefAccessor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File getApplicationResourceDir(final Date applicationDeploymentDate, final Map<String, Object> context)
            throws ApplicationFormDefinitionNotFoundException, SessionTimeoutException, IOException {
        final FormContextUtil ctxu = new FormContextUtil(context);
        if (getLogger().isLoggable(Level.FINEST)) {
            final String time = DATE_FORMAT.format(new Date());
            getLogger().log(Level.FINEST, "### " + time + " - getApplicationResourceDir - start");
        }
        long processDefinitionID = -1L;
        try {
            processDefinitionID = getProcessDefinitionID(context);
            final APISession session = ctxu.getAPISessionFromContext();
            return FormsResourcesUtils.getApplicationResourceDir(session, processDefinitionID, applicationDeploymentDate);
        } catch (final ProcessDefinitionNotFoundException e) {
            final String message = "The process definition with ID " + processDefinitionID + " does not exist!";
            logSevereWithContext(message, e, context);
            throw new ApplicationFormDefinitionNotFoundException(message, e);
        } catch (final BPMEngineException e) {
            final String message = "Error while communicating with the engine.";
            logSevereWithContext(message, e, context);
            throw new ApplicationFormDefinitionNotFoundException(message, e);
        } catch (final InvalidSessionException e) {
            final String message = "The engine session is invalid.";
            if (getLogger().isLoggable(Level.FINE)) {
                getLogger().log(Level.FINE, message, e);
            }
            throw new SessionTimeoutException(message);
        } finally {
            if (getLogger().isLoggable(Level.FINEST)) {
                final String time = DATE_FORMAT.format(new Date());
                getLogger().log(Level.FINEST, "### " + time + " - getApplicationResourceDir - end");
            }
        }
    }

    @Override
    public FormFieldValue getAttachmentFormFieldValue(final Object value, final Map<String, Object> context) throws SessionTimeoutException, IOException,
    FileTooBigException, FormInitializationException {
        if (getLogger().isLoggable(Level.FINEST)) {
            final String time = DATE_FORMAT.format(new Date());
            getLogger().log(Level.FINEST, "### " + time + " - getAttachmentFormFieldValue - start");
        }
        String documentValue = null;
        String valueType = null;
        String documentName = null;
        long documentId = -1;
        if (value != null) {
            documentName = (String) value;
            try {
                try {
                    final Expression documentExpression = new Expression(null, documentName, ExpressionType.TYPE_DOCUMENT.name(),
                            org.bonitasoft.engine.bpm.document.Document.class.getName(), null, null);
                    final Serializable evaluationResult = resolveExpression(documentExpression, context);
                    final org.bonitasoft.engine.bpm.document.Document document = (org.bonitasoft.engine.bpm.document.Document) evaluationResult;
                    if (document != null) {
                        if (document.hasContent()) {
                            documentValue = document.getContentFileName();
                            valueType = File.class.getName();
                        } else {
                            documentValue = document.getUrl();
                            valueType = String.class.getName();
                        }
                        documentId = document.getId();
                        documentName = document.getName();
                        if (getLogger().isLoggable(Level.FINE)) {
                            getLogger().log(Level.FINE, "Document " + documentId + " retrieved with value: " + documentValue);
                        }
                    }
                } catch (final FormNotFoundException e) {
                    final String message = "Error while trying to retrieve the document " + documentName;
                    logSevereWithContext(message, e, context);
                    throw new IllegalArgumentException(message);
                }
            } catch (final ClassCastException e) {
                final String message = "Error while setting the initial value of a file widget. A Document name is expected as initial value.";
                logSevereWithContext(message, e, context);
                throw new IllegalArgumentException(message);
            }
        }
        final FormFieldValue formFieldValue = new FormFieldValue(documentValue, valueType);
        formFieldValue.setDocumentId(documentId);
        formFieldValue.setDocumentName(documentName);
        formFieldValue.setDocument(true);
        if (getLogger().isLoggable(Level.FINEST)) {
            final String time = DATE_FORMAT.format(new Date());
            getLogger().log(Level.FINEST, "### " + time + " - getAttachmentFormFieldValue - end");
        }
        return formFieldValue;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEditMode(final String formID, final Map<String, Object> context) throws FormNotFoundException, SessionTimeoutException {
        final FormContextUtil ctxu = new FormContextUtil(context);
        long activityInstanceID = -1;
        boolean isEditMode = false;
        final IFormWorkflowAPI workflowAPI = getFormWorkFlowApi();
        try {
            final Map<String, Object> urlContext = getUrlContext(context);
            if (urlContext.get(FormServiceProviderUtil.TASK_UUID) != null) {
                activityInstanceID = getActivityInstanceId(urlContext);
                final APISession session = ctxu.getAPISessionFromContext();
                isEditMode = workflowAPI.isTaskReady(session, activityInstanceID);
            } else if (urlContext.get(FormServiceProviderUtil.PROCESS_UUID) != null) {
                isEditMode = true;
            } else if (urlContext.get(FormServiceProviderUtil.INSTANCE_UUID) != null) {
                isEditMode = false;
            }
        } catch (final BPMEngineException e) {
            final String message = "Error while communicating with the engine.";
            logSevereWithContext(message, e, context);
            throw new FormNotFoundException(message);
        } catch (final InvalidSessionException e) {
            final String message = "The engine session is invalid.";
            if (getLogger().isLoggable(Level.FINE)) {
                getLogger().log(Level.FINE, message, e);
            }
            throw new SessionTimeoutException(message);
        }
        return isEditMode;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCurrentValue(final Map<String, Object> context) throws FormNotFoundException, SessionTimeoutException {
        boolean isCurrentValue = false;
        try {
            final Map<String, Object> urlContext = getUrlContext(context);
            if (urlContext.get(FormServiceProviderUtil.TASK_UUID) != null) {
                final String formId = (String) urlContext.get(FormServiceProviderUtil.FORM_ID);
                isCurrentValue = FormServiceProviderUtil.ENTRY_FORM_TYPE.equals(getFormType(formId));
            } else if (urlContext.get(FormServiceProviderUtil.PROCESS_UUID) != null) {
                isCurrentValue = false;
            } else if (urlContext.get(FormServiceProviderUtil.INSTANCE_UUID) != null) {
                if (urlContext.get(FormServiceProviderUtil.RECAP_FORM_TYPE) != null) {
                    isCurrentValue = Boolean.valueOf((String) urlContext.get(FormServiceProviderUtil.RECAP_FORM_TYPE));
                } else {
                    isCurrentValue = false;
                }
            }
        } catch (final InvalidSessionException e) {
            final String message = "The engine session is invalid.";
            if (getLogger().isLoggable(Level.FINE)) {
                getLogger().log(Level.FINE, message, e);
            }
            throw new SessionTimeoutException(message);
        }
        return isCurrentValue;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> skipForm(final String formID, final Map<String, Object> context) throws FormNotFoundException, FormSubmissionException,
    FormAlreadySubmittedException, IllegalActivityTypeException, SessionTimeoutException {
        final FormContextUtil ctxu = new FormContextUtil(context);
        if (getLogger().isLoggable(Level.FINEST)) {
            final String time = DATE_FORMAT.format(new Date());
            getLogger().log(Level.FINEST, "### " + time + " - skipForm - start");
        }
        final IFormWorkflowAPI workflowAPI = getFormWorkFlowApi();
        final Map<String, Object> urlContext = new HashMap<String, Object>();

        if (context.get(FormServiceProviderUtil.URL_CONTEXT) != null) {
            urlContext.putAll(getUrlContext(context));
        }
        final APISession session = ctxu.getAPISessionFromContext();
        final Long userId = ctxu.getUserId();
        try {
            if (urlContext.get(FormServiceProviderUtil.TASK_UUID) != null) {
                final long activityInstanceID = getActivityInstanceId(urlContext);
                long processInstanceID = -1;
                try {
                    final boolean isTaskReady = workflowAPI.isTaskReady(session, activityInstanceID);
                    if (isTaskReady) {
                        workflowAPI.terminateTask(session, userId, activityInstanceID);
                        urlContext.remove(FormServiceProviderUtil.PROCESS_UUID);
                        urlContext.remove(FormServiceProviderUtil.INSTANCE_UUID);
                        processInstanceID = workflowAPI.getProcessInstanceIDFromActivityInstanceID(session, activityInstanceID);
                        long newTaskInstanceID;
                        newTaskInstanceID = workflowAPI.getAnyTodoListTaskForProcessInstance(session, processInstanceID);
                        if (newTaskInstanceID != -1) {
                            urlContext.put(FormServiceProviderUtil.TASK_UUID, String.valueOf(newTaskInstanceID));
                        }
                    } else {
                        throw new FormAlreadySubmittedException();
                    }
                } catch (final UserNotFoundException e) {
                    final String message = "The user with ID " + userId + " does not exist!";
                    logSevereWithContext(message, e, context);
                    throw new SessionTimeoutException(message);
                } catch (final ActivityInstanceNotFoundException e) {
                    final String message = "The activity instance with ID " + activityInstanceID + " does not exist!";
                    logSevereMessageWithContext(e, e.getMessage(), context);
                    throw new FormNotFoundException(message);
                } catch (final ProcessInstanceNotFoundException e) {
                    final String message = "The process instance with ID " + processInstanceID + " does not exist!";
                    logSevereWithContext(message, e, context);
                    throw new FormNotFoundException(message);
                } catch (final FlowNodeExecutionException e) {
                    final String message = "The engine was not able to execute the activity instance " + activityInstanceID;
                    logSevereWithContext(message, e, context);
                    throw new FormNotFoundException(message);
                }
            } else if (urlContext.get(FormServiceProviderUtil.PROCESS_UUID) != null) {
                final long processDefinitionID = Long.valueOf(urlContext.get(FormServiceProviderUtil.PROCESS_UUID).toString());
                try {
                    final long newProcessInstanceID = workflowAPI.startInstance(session, userId, processDefinitionID);
                    urlContext.remove(FormServiceProviderUtil.PROCESS_UUID);
                    urlContext.remove(FormServiceProviderUtil.TASK_UUID);
                    urlContext.put(FormServiceProviderUtil.INSTANCE_UUID, String.valueOf(newProcessInstanceID));
                } catch (final UserNotFoundException e) {
                    final String message = "The user with ID " + userId + " does not exist!";
                    logSevereWithContext(message, e, context);
                    throw new FormSubmissionException();
                } catch (final ProcessDefinitionNotFoundException e) {
                    final String message = "The process definition with ID " + processDefinitionID + " does not exist!";
                    logSevereWithContext(message, e, context);
                    throw new FormNotFoundException(message);
                } catch (final ProcessDefinitionNotEnabledException e) {
                    final String message = "The process instance creation is not enabled for the process with ID " + processDefinitionID;
                    logSevereWithContext(message, e, context);
                    throw new FormNotFoundException(message);
                } catch (final CreationException e) {
                    final String message = "It is not possible to start the apps. Please contact your administrator";
                    logSevereWithContext(message, e, context);
                    throw new FormNotFoundException(message);
                } catch (final ProcessActivationException e) {
                    final String message = "It is not possible to start the apps. Please contact your administrator";
                    logSevereWithContext(message, e, context);
                    throw new FormNotFoundException(message);
                } catch (final ExecutionException e) {
                    final String message = "It is not possible to start the apps. Please contact your administrator";
                    logSevereWithContext(message, e, context);
                    throw new FormNotFoundException(message);
                }
            } else {
                throw new FormNotFoundException("Unable to skip form " + formID + " The process ID or task ID are missing from the URL");
            }
        } catch (final BPMEngineException e) {
            final String message = "Error while communicating with the engine.";
            logSevereWithContext(message, e, context);
            throw new FormNotFoundException(message);
        } catch (final InvalidSessionException e) {
            final String message = "The engine session is invalid.";
            if (getLogger().isLoggable(Level.INFO)) {
                getLogger().log(Level.INFO, message, e);
            }
            throw new SessionTimeoutException(message);
        }
        if (getLogger().isLoggable(Level.FINEST)) {
            final String time = DATE_FORMAT.format(new Date());
            getLogger().log(Level.FINEST, "### " + time + " - skipForm - end");
        }
        return urlContext;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> getAnyTodoListForm(final Map<String, Object> context) throws FormNotFoundException, SessionTimeoutException {
        final FormContextUtil ctxu = new FormContextUtil(context);
        if (getLogger().isLoggable(Level.FINEST)) {
            final String time = DATE_FORMAT.format(new Date());
            getLogger().log(Level.FINEST, "### " + time + " - skipForm - start");
        }
        final Map<String, Object> urlContext = new HashMap<String, Object>();
        final IFormWorkflowAPI workflowAPI = getFormWorkFlowApi();
        if (context.get(FormServiceProviderUtil.URL_CONTEXT) != null) {
            urlContext.putAll(getUrlContext(context));
        }
        long activityInstanceID = -1;
        final APISession session = ctxu.getAPISessionFromContext();
        long processInstanceID = -1;
        long processDefinitionID = -1;
        try {
            try {
                if (urlContext.get(FormServiceProviderUtil.PROCESS_UUID) != null) {
                    processDefinitionID = Long.valueOf(urlContext.get(FormServiceProviderUtil.PROCESS_UUID).toString());
                    activityInstanceID = workflowAPI.getAnyTodoListTaskForProcessDefinition(session, processDefinitionID);
                } else if (urlContext.get(FormServiceProviderUtil.INSTANCE_UUID) != null) {
                    processInstanceID = getProcessInstanceId(urlContext);
                    activityInstanceID = workflowAPI.getAnyTodoListTaskForProcessInstance(session, processInstanceID);
                } else if (urlContext.get(FormServiceProviderUtil.THEME) != null) {
                    processDefinitionID = Long.valueOf(urlContext.get(FormServiceProviderUtil.THEME).toString());
                    activityInstanceID = workflowAPI.getAnyTodoListTaskForProcessDefinition(session, processDefinitionID);
                }
                if (activityInstanceID == -1) {
                    activityInstanceID = workflowAPI.getAnyTodoListTaskForProcessDefinition(session, -1);
                }
            } catch (final UserNotFoundException e) {
                final String message = "The user with ID " + session.getUserId() + " does not exist!";
                if (getLogger().isLoggable(Level.INFO)) {
                    getLogger().log(Level.INFO, message, e);
                }
                throw new SessionTimeoutException(message);
            } catch (final ProcessDefinitionNotFoundException e) {
                final String message = "The process definition with ID " + processDefinitionID + " does not exist!";
                logSevereWithContext(message, e, context);
                throw new FormNotFoundException(message);
            } catch (final ProcessInstanceNotFoundException e) {
                final String message = "The process instance with ID " + processInstanceID + " does not exist!";
                logSevereWithContext(message, e, context);
                throw new FormNotFoundException(message);
            }
            urlContext.remove(FormServiceProviderUtil.PROCESS_UUID);
            urlContext.remove(FormServiceProviderUtil.INSTANCE_UUID);
            urlContext.remove(FormServiceProviderUtil.TO_DO_LIST);
            if (activityInstanceID != -1) {
                String activitydefinitionUUID = null;
                try {
                    activitydefinitionUUID = getActivityDefinitionUUID(session, workflowAPI, activityInstanceID);
                    processDefinitionID = getProcessDefinitionId(session, workflowAPI, activityInstanceID);
                } catch (final FormWorflowApiException e) {
                    logSevereMessageWithContext(e, e.getMessage(), context);
                    throw new FormNotFoundException(e);
                }
                urlContext.put(FormServiceProviderUtil.TASK_UUID, String.valueOf(activityInstanceID));
                urlContext.put(FormServiceProviderUtil.THEME, String.valueOf(processDefinitionID));
                urlContext.put(FormServiceProviderUtil.FORM_ID, activitydefinitionUUID + FormServiceProviderUtil.FORM_ID_SEPARATOR
                        + FormServiceProviderUtil.ENTRY_FORM_TYPE);
            } else {
                final String message = "There are no steps waiting in inbox.";
                if (getLogger().isLoggable(Level.INFO)) {
                    getLogger().log(Level.INFO, message);
                }
                throw new FormNotFoundException(message);
            }
        } catch (final BPMEngineException e) {
            final String message = "Error while communicating with the engine.";
            logSevereWithContext(message, e, context);
            throw new FormNotFoundException(message);
        } catch (final InvalidSessionException e) {
            final String message = "The engine session is invalid.";
            if (getLogger().isLoggable(Level.INFO)) {
                getLogger().log(Level.INFO, message, e);
            }
            throw new SessionTimeoutException(message);
        }
        if (getLogger().isLoggable(Level.FINEST)) {
            final String time = DATE_FORMAT.format(new Date());
            getLogger().log(Level.FINEST, "### " + time + " - skipForm - end");
        }
        return urlContext;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ClassLoader getClassloader(final Map<String, Object> context) throws SessionTimeoutException, FormNotFoundException {
        final FormContextUtil ctxu = new FormContextUtil(context);
        if (getLogger().isLoggable(Level.FINEST)) {
            final String time = DATE_FORMAT.format(new Date());
            getLogger().log(Level.FINEST, "### " + time + " - getClassloader - start");
        }
        try {
            final long processDefinitionID = getProcessDefinitionID(context);
            final APISession session = ctxu.getAPISessionFromContext();
            return getProcessClassloader(processDefinitionID, session);
        } catch (final InvalidSessionException e) {
            final String message = "The engine session is invalid.";
            if (getLogger().isLoggable(Level.INFO)) {
                getLogger().log(Level.INFO, message, e);
            }
            throw new SessionTimeoutException(message);
        } finally {
            if (getLogger().isLoggable(Level.FINEST)) {
                final String time = DATE_FORMAT.format(new Date());
                getLogger().log(Level.FINEST, "### " + time + " - getClassloader - end");
            }
        }
    }

    protected ClassLoader getProcessClassloader(final long processDefinitionID, final APISession session) {
        return FormsResourcesUtils.getProcessClassLoader(session, processDefinitionID);
    }

    private Boolean canStartProcessDefinition(final APISession session, final long userId, final long processDefinitionId) throws BPMEngineException {
        try {
            final CommandAPI commandAPI = TenantAPIAccessor.getCommandAPI(session);
            final Map<String, Serializable> parameters = new HashMap<String, Serializable>();
            if (userId != -1) {
                parameters.put("USER_ID_KEY", userId);
            } else {
                parameters.put("USER_ID_KEY", session.getUserId());
            }
            parameters.put("PROCESS_DEFINITION_ID_KEY", processDefinitionId);
            return (Boolean) commandAPI.execute("canStartProcessDefinition", parameters);

        } catch (final Exception e) {
            final String message = "The engine was not able to know if the user can start the process. Error while executing command:";
            logSevereMessage(e, message);
            throw new BPMEngineException(message);
        }
    }

    private Boolean isInvolvedInHumanTask(final APISession session, final long userId, final long humanTaskInstanceId) throws BPMEngineException {
        try {
            final CommandAPI commandAPI = TenantAPIAccessor.getCommandAPI(session);
            final Map<String, Serializable> parameters = new HashMap<String, Serializable>();
            if (userId != -1) {
                parameters.put("USER_ID_KEY", userId);
            } else {
                parameters.put("USER_ID_KEY", session.getUserId());
            }
            parameters.put("HUMAN_TASK_INSTANCE_ID_KEY", humanTaskInstanceId);
            return (Boolean) commandAPI.execute("isInvolvedInHumanTask", parameters);

        } catch (final Exception e) {
            final String message = "The engine was not able to know if the user is involved in the human task instance. Error while executing command:";
            logSevereMessage(e, message);
            throw new BPMEngineException(message);
        }
    }

    private boolean isInvolvedInProcessInstance(final APISession session, final long userId, final long processInstanceId) throws BPMEngineException {
        try {
            final ProcessAPI processAPI = TenantAPIAccessor.getProcessAPI(session);
            return processAPI.isInvolvedInProcessInstance(userId, processInstanceId);
        } catch (final Exception e) {
            final String message = "The engine was not able to know if the user is involved in the process instance. Error while executing command:";
            logSevereMessage(e, message);
            throw new BPMEngineException(message);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void storeFormTransientDataContext(final HttpSession session, final String storageKey, final Map<String, Serializable> transientDataContext,
            final Map<String, Object> context) {
        final String id = getInstanceIdToUse(context);
        session.setAttribute(storageKey + "--" + id, transientDataContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Serializable> retrieveFormTransientDataContext(final HttpSession session, final String storageKey, final Map<String, Object> context) {
        final String id = getInstanceIdToUse(context);
        @SuppressWarnings("unchecked")
        Map<String, Serializable> transientDataContext = (Map<String, Serializable>) session.getAttribute(storageKey + "--" + id);
        if (transientDataContext == null) {
            transientDataContext = new HashMap<String, Serializable>();
        }
        return transientDataContext;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeFormTransientDataContext(final HttpSession session, final String storageKey, final Map<String, Object> context) {
        final String id = getInstanceIdToUse(context);
        session.removeAttribute(storageKey + "--" + id);
    }

    private String getInstanceIdToUse(final Map<String, Object> context) {
        String id = null;
        @SuppressWarnings("unchecked")
        final Map<String, Object> urlContext = (Map<String, Object>) context.get(FormServiceProviderUtil.URL_CONTEXT);
        if (urlContext.get(FormServiceProviderUtil.TASK_UUID) != null) {
            id = urlContext.get(FormServiceProviderUtil.TASK_UUID).toString();
        } else if (urlContext.get(FormServiceProviderUtil.PROCESS_UUID) != null) {
            id = urlContext.get(FormServiceProviderUtil.PROCESS_UUID).toString();
        } else if (urlContext.get(FormServiceProviderUtil.INSTANCE_UUID) != null) {
            id = urlContext.get(FormServiceProviderUtil.INSTANCE_UUID).toString();
        }
        return id;
    }

    private void logInfoMessageWithContext(final String message, final Throwable e, final Map<String, Object> context) {
        if (getLogger().isLoggable(Level.INFO)) {
            getLogger().log(Level.INFO, message, e, context);
        }
    }

    private void logInfoMessage(final String message, final Throwable e) {
        if (Logger.getLogger(FormServiceProviderImpl.class.getName()).isLoggable(Level.INFO)) {
            Logger.getLogger(FormServiceProviderImpl.class.getName()).log(Level.INFO, message, e);
        }
    }

    private void logSevereMessageWithContext(final Throwable e, final String message, final Map<String, Object> context) {
        if (getLogger().isLoggable(Level.SEVERE)) {
            getLogger().log(Level.SEVERE, message, e, context);
        }
    }

    private void logSevereMessage(final Throwable e, final String message) {
        if (Logger.getLogger(FormServiceProviderImpl.class.getName()).isLoggable(Level.SEVERE)) {
            Logger.getLogger(FormServiceProviderImpl.class.getName()).log(Level.SEVERE, message, e);
        }
    }

    private void logTime(final String message) {
        if (Logger.getLogger(FormServiceProviderImpl.class.getName()).isLoggable(Level.FINEST)) {
            final String time = DATE_FORMAT.format(new Date());
            Logger.getLogger(FormServiceProviderImpl.class.getName()).log(Level.FINEST, "### " + time + " - " + message);
        }
    }

}
