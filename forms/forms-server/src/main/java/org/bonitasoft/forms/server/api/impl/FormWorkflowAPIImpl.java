/**
 * Copyright (C) 2009, 2014 BonitaSoft S.A.
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
package org.bonitasoft.forms.server.api.impl;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bonitasoft.console.common.server.utils.BPMEngineAPIUtil;
import org.bonitasoft.console.common.server.utils.BPMEngineException;
import org.bonitasoft.console.common.server.utils.BPMExpressionEvaluationException;
import org.bonitasoft.engine.api.CommandAPI;
import org.bonitasoft.engine.api.IdentityAPI;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.ProfileAPI;
import org.bonitasoft.engine.bpm.actor.ActorInstance;
import org.bonitasoft.engine.bpm.actor.ActorNotFoundException;
import org.bonitasoft.engine.bpm.flownode.ActivityInstance;
import org.bonitasoft.engine.bpm.flownode.ActivityInstanceCriterion;
import org.bonitasoft.engine.bpm.flownode.ActivityInstanceNotFoundException;
import org.bonitasoft.engine.bpm.flownode.ActivityStates;
import org.bonitasoft.engine.bpm.flownode.ArchivedActivityInstance;
import org.bonitasoft.engine.bpm.flownode.ArchivedHumanTaskInstance;
import org.bonitasoft.engine.bpm.flownode.FlowNodeExecutionException;
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance;
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstanceSearchDescriptor;
import org.bonitasoft.engine.bpm.process.ActivationState;
import org.bonitasoft.engine.bpm.process.ArchivedProcessInstance;
import org.bonitasoft.engine.bpm.process.ArchivedProcessInstanceNotFoundException;
import org.bonitasoft.engine.bpm.process.ArchivedProcessInstancesSearchDescriptor;
import org.bonitasoft.engine.bpm.process.ProcessActivationException;
import org.bonitasoft.engine.bpm.process.ProcessDefinition;
import org.bonitasoft.engine.bpm.process.ProcessDefinitionNotFoundException;
import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfo;
import org.bonitasoft.engine.bpm.process.ProcessInstance;
import org.bonitasoft.engine.bpm.process.ProcessInstanceNotFoundException;
import org.bonitasoft.engine.command.CommandExecutionException;
import org.bonitasoft.engine.command.CommandNotFoundException;
import org.bonitasoft.engine.command.CommandParameterizationException;
import org.bonitasoft.engine.exception.CreationException;
import org.bonitasoft.engine.exception.ExecutionException;
import org.bonitasoft.engine.exception.SearchException;
import org.bonitasoft.engine.exception.UpdateException;
import org.bonitasoft.engine.identity.User;
import org.bonitasoft.engine.identity.UserNotFoundException;
import org.bonitasoft.engine.operation.Operation;
import org.bonitasoft.engine.profile.Profile;
import org.bonitasoft.engine.profile.ProfileCriterion;
import org.bonitasoft.engine.search.Order;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.session.InvalidSessionException;
import org.bonitasoft.forms.client.model.ActionType;
import org.bonitasoft.forms.client.model.ActivityAttribute;
import org.bonitasoft.forms.client.model.ActivityEditState;
import org.bonitasoft.forms.client.model.Expression;
import org.bonitasoft.forms.client.model.FormAction;
import org.bonitasoft.forms.client.model.FormFieldValue;
import org.bonitasoft.forms.client.model.exception.ForbiddenFormAccessException;
import org.bonitasoft.forms.server.accessor.DefaultFormsProperties;
import org.bonitasoft.forms.server.accessor.DefaultFormsPropertiesFactory;
import org.bonitasoft.forms.server.api.FormAPIFactory;
import org.bonitasoft.forms.server.api.IFormExpressionsAPI;
import org.bonitasoft.forms.server.api.IFormWorkflowAPI;
import org.bonitasoft.forms.server.api.impl.util.FormActionAdapter;
import org.bonitasoft.forms.server.api.impl.util.FormWorkflowUtil;
import org.bonitasoft.forms.server.exception.FileTooBigException;
import org.bonitasoft.forms.server.exception.TaskAssignationException;

/**
 * implementation of {@link IFormWorkflowAPI}
 *
 * @author Anthony Birembaut
 * @author Celine Souchet
 */
public class FormWorkflowAPIImpl implements IFormWorkflowAPI {

    /**
     * Prefix for forms actions conditions expressions
     */
    private static final String FORM_ACTION_CONDITION = "FormActionCondition_";

    /**
     * Value returned when no activity has been found
     */
    private static final long NOT_FOUND = -1L;

    /**
     * Index of the search first page
     */
    private static final int SEARCH_FIRST_PAGE_INDEX = 0;

    public static final int SECONDS_IN_A_DAY = 86400;

    public static final int SECONDS_IN_AN_HOUR = 3600;

    public static final int SECONDS_IN_A_MINUTE = 60;

    public static final String UUID_SEPARATOR = "--";

    public static final String ADMIN_PROFILE_NAME = "Administrator";

    /**
     * Logger
     */
    private static Logger LOGGER = Logger.getLogger(FormWorkflowAPIImpl.class.getName());

    /**
     * Util class allowing to work with the BPM engine API
     */
    private final BPMEngineAPIUtil bpmEngineAPIUtil = new BPMEngineAPIUtil();

    @Override
    public Serializable getActivityFieldValue(final APISession session, final long activityInstanceID, final Expression expression, final Locale locale,
            final boolean isCurrentValue) throws BPMExpressionEvaluationException, InvalidSessionException, BPMEngineException {

        return getActivityFieldValue(session, activityInstanceID, expression, locale, isCurrentValue, new HashMap<String, Serializable>());
    }

    @Override
    public Serializable getActivityFieldValue(final APISession session, final long activityInstanceID, final Expression expression, final Locale locale,
            final boolean isCurrentValue, final Map<String, Serializable> context) throws BPMExpressionEvaluationException, InvalidSessionException,
                    BPMEngineException {

        final IFormExpressionsAPI formExpressionsAPI = FormAPIFactory.getFormExpressionsAPI();
        return formExpressionsAPI.evaluateActivityInitialExpression(session, activityInstanceID, expression, locale, isCurrentValue, context);
    }

    /**
     * {@inheritDoc}
     *
     * @throws BPMEngineException
     */
    @Override
    public Serializable getProcessFieldValue(final APISession session, final long processDefinitionID, final Expression expression, final Locale locale)
            throws BPMExpressionEvaluationException, InvalidSessionException, BPMEngineException {

        return getProcessFieldValue(session, processDefinitionID, expression, locale, new HashMap<String, Serializable>());
    }

    /**
     * {@inheritDoc}
     *
     * @throws BPMEngineException
     */
    @Override
    public Serializable getProcessFieldValue(final APISession session, final long processDefinitionID, final Expression expression, final Locale locale,
            final Map<String, Serializable> context) throws BPMExpressionEvaluationException, InvalidSessionException, BPMEngineException {

        final IFormExpressionsAPI formExpressionsAPI = FormAPIFactory.getFormExpressionsAPI();
        return formExpressionsAPI.evaluateProcessInitialExpression(session, processDefinitionID, expression, locale, context);
    }

    /**
     * {@inheritDoc}
     *
     * @throws BPMEngineException
     */
    @Override
    public Serializable getInstanceFieldValue(final APISession session, final long processInstanceID, final Expression expression, final Locale locale,
            final boolean isCurrentValue) throws BPMExpressionEvaluationException, InvalidSessionException, BPMEngineException {

        return getInstanceFieldValue(session, processInstanceID, expression, locale, isCurrentValue, new HashMap<String, Serializable>());
    }

    /**
     * {@inheritDoc}
     *
     * @throws BPMEngineException
     */
    @Override
    public Serializable getInstanceFieldValue(final APISession session, final long processInstanceID, final Expression expression, final Locale locale,
            final boolean isCurrentValue, final Map<String, Serializable> context) throws BPMExpressionEvaluationException, InvalidSessionException,
                    BPMEngineException {

        final IFormExpressionsAPI formExpressionsAPI = FormAPIFactory.getFormExpressionsAPI();
        return formExpressionsAPI.evaluateInstanceInitialExpression(session, processInstanceID, expression, locale, isCurrentValue, context);
    }

    @Override
    public Serializable getActivityFieldValue(final APISession session, final long activityInstanceID, final Expression expression,
            final Map<String, FormFieldValue> fieldValues, final Locale locale, final boolean isCurrentValue) throws BPMExpressionEvaluationException,
                    InvalidSessionException, FileTooBigException, IOException, BPMEngineException {

        return getActivityFieldValue(session, activityInstanceID, expression, fieldValues, locale, isCurrentValue, new HashMap<String, Serializable>());
    }

    @Override
    public Serializable getActivityFieldValue(final APISession session, final long activityInstanceID, final Expression expression,
            final Map<String, FormFieldValue> fieldValues, final Locale locale, final boolean isCurrentValue, final Map<String, Serializable> context)
                    throws BPMExpressionEvaluationException, InvalidSessionException, FileTooBigException, IOException, BPMEngineException {

        final IFormExpressionsAPI formExpressionsAPI = FormAPIFactory.getFormExpressionsAPI();
        return formExpressionsAPI.evaluateActivityExpression(session, activityInstanceID, expression, fieldValues, locale, isCurrentValue, context);
    }

    /**
     * {@inheritDoc}
     *
     * @throws BPMEngineException
     */
    @Override
    public Serializable getProcessFieldValue(final APISession session, final long processDefinitionID, final Expression expression,
            final Map<String, FormFieldValue> fieldValues, final Locale locale) throws BPMExpressionEvaluationException, InvalidSessionException,
                    FileTooBigException, IOException, BPMEngineException {

        return getProcessFieldValue(session, processDefinitionID, expression, fieldValues, locale, new HashMap<String, Serializable>());
    }

    /**
     * {@inheritDoc}
     *
     * @throws BPMEngineException
     */
    @Override
    public Serializable getProcessFieldValue(final APISession session, final long processDefinitionID, final Expression expression,
            final Map<String, FormFieldValue> fieldValues, final Locale locale, final Map<String, Serializable> context)
                    throws BPMExpressionEvaluationException, InvalidSessionException, FileTooBigException, IOException, BPMEngineException {

        final IFormExpressionsAPI formExpressionsAPI = FormAPIFactory.getFormExpressionsAPI();
        return formExpressionsAPI.evaluateProcessExpression(session, processDefinitionID, expression, fieldValues, locale, context);
    }

    /**
     * {@inheritDoc}
     *
     * @throws BPMEngineException
     */
    @Override
    public Serializable getInstanceFieldValue(final APISession session, final long processInstanceID, final Expression expression,
            final Map<String, FormFieldValue> fieldValues, final Locale locale, final boolean isCurrentValue) throws BPMExpressionEvaluationException,
                    InvalidSessionException, FileTooBigException, IOException, BPMEngineException {

        return getInstanceFieldValue(session, processInstanceID, expression, fieldValues, locale, isCurrentValue, new HashMap<String, Serializable>());
    }

    /**
     * {@inheritDoc}
     *
     * @throws IOException
     * @throws FileTooBigException
     * @throws BPMEngineException
     */
    @Override
    public Serializable getInstanceFieldValue(final APISession session, final long processInstanceID, final Expression expression,
            final Map<String, FormFieldValue> fieldValues, final Locale locale, final boolean isCurrentValue, final Map<String, Serializable> context)
                    throws BPMExpressionEvaluationException, InvalidSessionException, FileTooBigException, IOException, BPMEngineException {

        final IFormExpressionsAPI formExpressionsAPI = FormAPIFactory.getFormExpressionsAPI();
        return formExpressionsAPI.evaluateInstanceExpression(session, processInstanceID, expression, fieldValues, locale, isCurrentValue, context);
    }

    protected List<Operation> buildOperations(final APISession session, final List<FormAction> actions, final Locale locale,
            final Map<String, Serializable> context, final long processDefinitionID, final long activityInstanceID) throws InvalidSessionException,
                    BPMExpressionEvaluationException, BPMEngineException {

        final List<Operation> operations = new ArrayList<Operation>();
        final FormActionAdapter formActionAdapter = new FormActionAdapter();
        final IFormExpressionsAPI formExpressionsAPI = FormAPIFactory.getFormExpressionsAPI();
        final Map<String, Serializable> evaluateConditionExpressions = getEvaluateConditionExpressions(session, actions, locale, context, processDefinitionID,
                activityInstanceID, formExpressionsAPI);

        for (Integer i = 0; i < actions.size(); i++) {
            final FormAction action = actions.get(i);
            if (!action.getType().name().equals(ActionType.EXECUTE_CONNECTOR.name())) {
                final Expression conditionExpression = action.getConditionExpression();
                if (isActionConditionVerified(session, conditionExpression, (Boolean) evaluateConditionExpressions.get(FORM_ACTION_CONDITION + i))) {
                    operations.add(formActionAdapter.getEngineOperation(action));
                }
            }
        }
        return operations;
    }

    protected Map<String, Serializable> getEvaluateConditionExpressions(final APISession session, final List<FormAction> actions, final Locale locale,
            final Map<String, Serializable> context, final long processDefinitionID, final long activityInstanceID,
            final IFormExpressionsAPI formExpressionsAPI)
                    throws BPMExpressionEvaluationException, BPMEngineException {
        Map<String, Serializable> evaluateConditionExpressions;
        final List<Expression> conditionsList = new ArrayList<Expression>();

        for (Integer i = 0; i < actions.size(); i++) {
            final FormAction action = actions.get(i);
            final Expression conditionExpression = action.getConditionExpression();
            if (conditionExpression != null) {
                conditionExpression.setName(FORM_ACTION_CONDITION + i);
                conditionsList.add(conditionExpression);
            }
        }

        if (activityInstanceID != -1) {
            evaluateConditionExpressions = formExpressionsAPI.evaluateActivityInitialExpressions(session, activityInstanceID, conditionsList, locale,
                    true, context);
        } else if (processDefinitionID != -1) {
            evaluateConditionExpressions = formExpressionsAPI.evaluateProcessInitialExpressions(session, processDefinitionID, conditionsList, locale,
                    context);
        } else {
            evaluateConditionExpressions = new HashMap<String, Serializable>();
        }
        return evaluateConditionExpressions;
    }

    protected boolean isActionConditionVerified(final APISession session, final Expression conditionExpression, final Boolean evaluatedCondition) {
        final DefaultFormsProperties defaultFormProperties = DefaultFormsPropertiesFactory.getDefaultFormProperties(session.getTenantId());
        return !defaultFormProperties.enableFormsActionConditions() || conditionExpression == null || evaluatedCondition;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long executeActionsAndStartInstance(final APISession session, final long userId, final long processDefinitionID,
            final Map<String, FormFieldValue> fieldValues, final List<FormAction> actions, final Locale locale, final String submitButtonId,
            final Map<String, Serializable> context) throws BPMEngineException, BPMExpressionEvaluationException, InvalidSessionException, FileTooBigException,
                    IOException {
        final IFormExpressionsAPI formExpressionsAPI = FormAPIFactory.getFormExpressionsAPI();
        final Map<String, Serializable> evalContext = formExpressionsAPI.generateGroovyContext(session, fieldValues, locale, context, true);
        if (context != null) {
            evalContext.putAll(context);
        }
        long ProcessInstance = 0;
        final ProcessAPI processAPI = getBpmEngineAPIUtil().getProcessAPI(session);

        final List<FormAction> actionsToExecute = new ArrayList<FormAction>();
        for (final FormAction action : actions) {
            if (action.getSubmitButtonId() == null || action.getSubmitButtonId().length() == 0 || action.getSubmitButtonId().equals(submitButtonId)) {
                actionsToExecute.add(action);
            }
        }
        try {
            ProcessInstance = processAPI.startProcess(userId, processDefinitionID,
                    buildOperations(session, actionsToExecute, locale, evalContext, processDefinitionID, -1), evalContext).getId();
        } catch (final ProcessDefinitionNotFoundException e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, " ProcessDefinitionNotFoundException of processDefinitionID : " + processDefinitionID);
            }
            throw new BPMEngineException(e);
        } catch (final ProcessActivationException e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, " ProcessActivationException of processDefinitionID : " + processDefinitionID);
            }
            throw new BPMEngineException(e);
        } catch (final UserNotFoundException e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, " UserNotFoundException of processDefinitionID : " + processDefinitionID);
            }
            throw new BPMEngineException(e);
        } catch (final ExecutionException e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, " ExecutionException of processDefinitionID : " + processDefinitionID);
            }
            throw new BPMEngineException(e);
        }
        return ProcessInstance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void executeActionsAndTerminate(final APISession session, final long userID, final long activityInstanceID,
            final Map<String, FormFieldValue> fieldValues, final List<FormAction> actions, final Locale locale, final String submitButtonId,
            final Map<String, Serializable> context) throws BPMEngineException, BPMExpressionEvaluationException, InvalidSessionException,
                    ActivityInstanceNotFoundException, ProcessInstanceNotFoundException, FileTooBigException, IOException {
        final IFormExpressionsAPI formExpressionsAPI = FormAPIFactory.getFormExpressionsAPI();

        final List<FormAction> actionsToExecute = new ArrayList<FormAction>();
        for (final FormAction action : actions) {
            if (action.getSubmitButtonId() == null || action.getSubmitButtonId().length() == 0 || action.getSubmitButtonId().equals(submitButtonId)) {
                actionsToExecute.add(action);
            }
        }

        final CommandAPI commandAPI = getBpmEngineAPIUtil().getCommandAPI(session);
        final Map<String, Serializable> excuteParameters = new HashMap<String, Serializable>();
        final Map<String, Serializable> evalContext = formExpressionsAPI.generateGroovyContext(session, fieldValues, locale, context, true);
        if (context != null) {
            evalContext.putAll(context);
        }
        excuteParameters.put(FormWorkflowUtil.ACTIVITY_INSTANCE_ID_KEY, activityInstanceID);
        excuteParameters.put(FormWorkflowUtil.OPERATIONS_LIST_KEY,
                (Serializable) buildOperations(session, actionsToExecute, locale, evalContext, -1, activityInstanceID));
        excuteParameters.put(FormWorkflowUtil.OPERATIONS_INPUT_KEY, (Serializable) evalContext);
        excuteParameters.put(FormWorkflowUtil.USER_ID_KEY, userID);
        getBpmEngineAPIUtil().executeCommand(commandAPI, FormWorkflowUtil.EXECUTE_ACTION_AND_TERMINATE, excuteParameters);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getProcessDefinitionDate(final APISession session, final long processDefinitionID) throws ProcessDefinitionNotFoundException,
            BPMEngineException, InvalidSessionException {

        final ProcessAPI processAPI = getBpmEngineAPIUtil().getProcessAPI(session);
        return processAPI.getProcessDeploymentInfo(processDefinitionID).getDeploymentDate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getMigrationDate(final APISession session, final long processDefinitionID) throws ProcessDefinitionNotFoundException, BPMEngineException,
            InvalidSessionException {

        final ProcessAPI processAPI = getBpmEngineAPIUtil().getProcessAPI(session);
        final ProcessDeploymentInfo processDeploymentInfo = processAPI.getProcessDeploymentInfo(processDefinitionID);
        Date migrationDate = null;
        if (!processDeploymentInfo.getDeploymentDate().equals(processDeploymentInfo.getLastUpdateDate())) {
            migrationDate = processDeploymentInfo.getLastUpdateDate();
        }
        return migrationDate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActivityEditState getTaskEditState(final APISession session, final long activityInstanceID) throws ActivityInstanceNotFoundException,
            ActivityInstanceNotFoundException, BPMEngineException, InvalidSessionException {

        final ProcessAPI processAPI = getBpmEngineAPIUtil().getProcessAPI(session);
        String activityInstanceState;

        try {
            final HumanTaskInstance humanTaskInstance = processAPI.getHumanTaskInstance(activityInstanceID);
            activityInstanceState = humanTaskInstance.getState();
        } catch (final ActivityInstanceNotFoundException e) {
            final ArchivedActivityInstance archivedActivityInstance = processAPI.getArchivedActivityInstance(activityInstanceID);
            activityInstanceState = archivedActivityInstance.getState();
        }

        // re-establish this once the SUSPENDED state will be supported by the engine
        // if (ActivityStates.SUSPENDED.equals(activityInstanceState)) {
        // return ActivityEditState.SUSPENDED;
        // } else
        if (ActivityStates.CANCELLED_STATE.equals(activityInstanceState)) {
            return ActivityEditState.CANCELED;
        } else if (ActivityStates.SKIPPED_STATE.equals(activityInstanceState)) {
            return ActivityEditState.SKIPPED;
        } else if (ActivityStates.FAILED_STATE.equals(activityInstanceState)) {
            return ActivityEditState.FAILED;
        } else if (ActivityStates.ABORTED_STATE.equals(activityInstanceState)) {
            return ActivityEditState.ABORTED;
        } else if (ActivityStates.COMPLETED_STATE.equals(activityInstanceState)) {
            return ActivityEditState.NOT_EDITABLE;
        } else {
            return ActivityEditState.EDITABLE;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws BPMEngineException
     * @throws InvalidSessionException
     * @throws UserNotFoundException
     * @throws SearchException
     * @throws ProcessDefinitionNotFoundException
     */
    @Override
    public long getRelatedProcessesNextTask(final APISession session, final long processInstanceId, final long userId) throws InvalidSessionException,
            BPMEngineException, UserNotFoundException, SearchException, ProcessDefinitionNotFoundException {
        try {
            final ProcessAPI processAPI = getBpmEngineAPIUtil().getProcessAPI(session);
            final long rootProcessInstanceId = getRootProcessInstanceId(processAPI, processInstanceId);
            if (rootProcessInstanceId != NOT_FOUND) {
                return getProcessInstanceTaskAvailableForUser(processAPI, rootProcessInstanceId, userId);
            } else {
                return NOT_FOUND;
            }
        } catch (final ArchivedProcessInstanceNotFoundException e) {
            return NOT_FOUND;
        }

    }

    /**
     * Retrieve the root process instance ID
     *
     * @param processApi
     * @param processInstanceId
     * @return
     * @throws ProcessInstanceNotFoundException
     * @throws InvalidSessionException
     * @throws BPMEngineException
     * @throws ProcessDefinitionNotFoundException
     */
    private long getRootProcessInstanceId(final ProcessAPI processApi, final long processInstanceId) throws ArchivedProcessInstanceNotFoundException,
            InvalidSessionException, BPMEngineException, ProcessDefinitionNotFoundException {
        long rootProcessInstanceId;
        try {
            final ProcessInstance processInstance = processApi.getProcessInstance(processInstanceId);
            rootProcessInstanceId = processInstance.getRootProcessInstanceId();
        } catch (final ProcessInstanceNotFoundException e) {
            final ArchivedProcessInstance archivedProcessInstance = getLatestArchivedProcessInstance(processApi, processInstanceId);
            rootProcessInstanceId = archivedProcessInstance.getRootProcessInstanceId();
        }
        return identifyRootProcessInstanceId(processInstanceId, rootProcessInstanceId);
    }

    private ArchivedProcessInstance getLatestArchivedProcessInstance(final ProcessAPI processApi, final long processInstanceId)
            throws ArchivedProcessInstanceNotFoundException {
        ArchivedProcessInstance archivedProcessInstance = null;
        final SearchOptionsBuilder searchOptionsBuilder = new SearchOptionsBuilder(0, 1);
        searchOptionsBuilder.filter(ArchivedProcessInstancesSearchDescriptor.SOURCE_OBJECT_ID, processInstanceId);
        searchOptionsBuilder.sort(ArchivedProcessInstancesSearchDescriptor.ARCHIVE_DATE, Order.DESC);
        SearchResult<ArchivedProcessInstance> searchArchivedProcessInstances = null;
        try {
            searchArchivedProcessInstances = processApi.searchArchivedProcessInstancesInAllStates(searchOptionsBuilder.done());
        } catch (final SearchException se) {
            throw new ArchivedProcessInstanceNotFoundException(se);
        }
        if (searchArchivedProcessInstances != null && searchArchivedProcessInstances.getCount() > 0) {
            archivedProcessInstance = searchArchivedProcessInstances.getResult().get(0);
        } else {
            throw new ArchivedProcessInstanceNotFoundException(new Exception("Unable to find an archive process instance with source ID "
                    + processInstanceId));
        }
        return archivedProcessInstance;
    }

    private long identifyRootProcessInstanceId(final long processInstanceId, final long rootProcessInstanceId) {
        return rootProcessInstanceId != NOT_FOUND ? rootProcessInstanceId : processInstanceId;
    }

    public long getProcessInstanceTaskAvailableForUser(final ProcessAPI processAPI, final long processInstanceId, final long userId)
            throws UserNotFoundException, InvalidSessionException, SearchException {
        final SearchOptionsBuilder searchOptionBuilder = createProcessInstancePendingTasksSearchOption(1, processInstanceId);
        final SearchResult<HumanTaskInstance> searchPendingTasksForUser = processAPI.searchMyAvailableHumanTasks(userId, searchOptionBuilder.done());
        return extractFirstAcitivityId(searchPendingTasksForUser);
    }

    private long extractFirstAcitivityId(final SearchResult<HumanTaskInstance> searchPendingTasksForUser) {
        if (!isSearchResultEmpty(searchPendingTasksForUser)) {
            return searchPendingTasksForUser.getResult().get(0).getId();
        } else {
            return NOT_FOUND;
        }
    }

    private boolean isSearchResultEmpty(final SearchResult<HumanTaskInstance> searchPendingTasksForUser) {
        return searchPendingTasksForUser == null || searchPendingTasksForUser.getResult() == null || searchPendingTasksForUser.getResult().isEmpty();
    }

    private SearchOptionsBuilder createProcessInstancePendingTasksSearchOption(final int nbOfResult, final long processInstanceId) {
        return new SearchOptionsBuilder(SEARCH_FIRST_PAGE_INDEX, nbOfResult).filter(HumanTaskInstanceSearchDescriptor.STATE_NAME, ActivityStates.READY_STATE)
                .filter(HumanTaskInstanceSearchDescriptor.PROCESS_INSTANCE_ID, processInstanceId).sort(HumanTaskInstanceSearchDescriptor.DUE_DATE, Order.ASC);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getAnyTodoListTaskForProcessDefinition(final APISession session, final long processDefinitionID) throws ProcessDefinitionNotFoundException,
            BPMEngineException, UserNotFoundException, ProcessInstanceNotFoundException, InvalidSessionException {

        long activityInstanceID = -1;
        if (processDefinitionID != -1) {
            activityInstanceID = getBpmEngineAPIUtil().getProcessAPI(session).getOneAssignedUserTaskInstanceOfProcessDefinition(processDefinitionID,
                    session.getUserId());
        } else {
            final List<HumanTaskInstance> assignedTasks = getBpmEngineAPIUtil().getProcessAPI(session).getAssignedHumanTaskInstances(session.getUserId(), 0, 1,
                    ActivityInstanceCriterion.REACHED_STATE_DATE_ASC);
            if (assignedTasks != null && !assignedTasks.isEmpty()) {
                activityInstanceID = assignedTasks.get(0).getId();
            }
        }

        return activityInstanceID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getAnyTodoListTaskForProcessInstance(final APISession session, final long processInstanceID) throws ProcessInstanceNotFoundException,
            BPMEngineException, UserNotFoundException, InvalidSessionException {

        long activityInstanceID = -1;
        if (processInstanceID != -1) {
            activityInstanceID = getBpmEngineAPIUtil().getProcessAPI(session)
                    .getOneAssignedUserTaskInstanceOfProcessInstance(processInstanceID, session.getUserId());
        } else {
            final List<HumanTaskInstance> assignedTasks = getBpmEngineAPIUtil().getProcessAPI(session).getAssignedHumanTaskInstances(session.getUserId(), 0, 1,
                    ActivityInstanceCriterion.REACHED_STATE_DATE_ASC);
            if (assignedTasks != null && !assignedTasks.isEmpty()) {
                activityInstanceID = assignedTasks.get(0).getId();
            }
        }
        return activityInstanceID;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> getActivityAttributes(final APISession session, final long activityInstanceID, final Locale locale)
            throws ActivityInstanceNotFoundException, BPMEngineException, ActivityInstanceNotFoundException, InvalidSessionException {

        final Map<String, String> attributes = new HashMap<String, String>();

        ResourceBundle labels = ResourceBundle.getBundle("locale.i18n.ActivityAttributeLabels", locale);
        if (locale.getLanguage() != null && labels.getLocale() != null && !locale.getLanguage().equals(labels.getLocale().getLanguage())) {
            labels = ResourceBundle.getBundle("locale.i18n.ActivityAttributeLabels", Locale.ENGLISH);
        }

        DateFormat dateFormat = null;

        final ProcessAPI processAPI = getBpmEngineAPIUtil().getProcessAPI(session);
        final IdentityAPI identityAPI = getBpmEngineAPIUtil().getIdentityAPI(session);
        long activityInstanceId;
        String activityInstanceDisplayName;
        String activityInstanceName;
        String activityInstanceDisplayDesc;
        String activityInstanceType;
        String activityInstanceState;
        Date reachedStateDate;
        Date lastUpdateDate;
        try {
            final ActivityInstance activityInstance = processAPI.getActivityInstance(activityInstanceID);
            activityInstanceId = activityInstance.getId();
            activityInstanceDisplayName = activityInstance.getDisplayName();
            activityInstanceName = activityInstance.getName();
            activityInstanceDisplayDesc = activityInstance.getDisplayDescription();
            activityInstanceType = activityInstance.getType().name();
            activityInstanceState = activityInstance.getState().toLowerCase();
            reachedStateDate = activityInstance.getReachedStateDate();
            lastUpdateDate = activityInstance.getLastUpdateDate();
        } catch (final ActivityInstanceNotFoundException e) {
            final ArchivedActivityInstance archivedActivityInstance = processAPI.getArchivedActivityInstance(activityInstanceID);
            activityInstanceId = archivedActivityInstance.getId();
            activityInstanceDisplayName = archivedActivityInstance.getDisplayName();
            activityInstanceName = archivedActivityInstance.getName();
            activityInstanceDisplayDesc = archivedActivityInstance.getDisplayDescription();
            activityInstanceType = archivedActivityInstance.getType().name();
            activityInstanceState = archivedActivityInstance.getState().toLowerCase();
            reachedStateDate = archivedActivityInstance.getReachedStateDate();
            lastUpdateDate = archivedActivityInstance.getLastUpdateDate();
        }
        long actorID;
        long assigneeID;
        String priority;
        Date expectedEndDate;
        try {
            final HumanTaskInstance humanTaskInstance = processAPI.getHumanTaskInstance(activityInstanceID);
            actorID = humanTaskInstance.getActorId();
            assigneeID = humanTaskInstance.getAssigneeId();
            priority = humanTaskInstance.getPriority().toString();
            expectedEndDate = humanTaskInstance.getExpectedEndDate();
        } catch (final ActivityInstanceNotFoundException e) {
            final ArchivedHumanTaskInstance archivedHumanTaskInstance = (ArchivedHumanTaskInstance) processAPI.getArchivedActivityInstance(activityInstanceID);
            actorID = archivedHumanTaskInstance.getActorId();
            assigneeID = archivedHumanTaskInstance.getAssigneeId();
            priority = archivedHumanTaskInstance.getPriority().toString();
            expectedEndDate = archivedHumanTaskInstance.getExpectedEndDate();
        }
        try {
            attributes.put(ActivityAttribute.priority.name(), priority.toString());
            // the assignee ID field is 0 when there is no assignee in the engine
            if (assigneeID != 0) {
                User assignee;
                try {
                    assignee = identityAPI.getUser(assigneeID);
                    attributes.put(ActivityAttribute.assignee.name(), assignee.getUserName());
                } catch (final UserNotFoundException e) {
                    if (LOGGER.isLoggable(Level.INFO)) {
                        LOGGER.log(Level.INFO, "Unable to find the user with ID: " + assigneeID, e);
                    }
                }
            }
            ActorInstance actor;
            try {
                actor = processAPI.getActor(actorID);
                if (actor.getDisplayName() != null) {
                    attributes.put(ActivityAttribute.actor.name(), actor.getDisplayName());
                } else {
                    attributes.put(ActivityAttribute.actor.name(), actor.getName());
                }
            } catch (final ActorNotFoundException e) {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.log(Level.INFO, "Unable to find the actor with ID: " + actorID, e);
                }
            }
            if (expectedEndDate != null) {
                dateFormat = getInitializedDateFormat(dateFormat, locale);
                attributes.put(ActivityAttribute.expectedEndDate.name(), dateFormat.format(expectedEndDate));
                final long remainingTime = expectedEndDate.getTime() - new Date().getTime();
                final StringBuilder remainingTimeStr = new StringBuilder();
                if (remainingTime > 0) {
                    final Long duration = remainingTime / 1000;
                    final int days = duration.intValue() / SECONDS_IN_A_DAY;
                    if (days >= 1) {
                        remainingTimeStr.append(days);
                        if (days > 1) {
                            remainingTimeStr.append(labels.getString("label.days"));
                        } else {
                            remainingTimeStr.append(labels.getString("label.day"));
                            remainingTimeStr.append(" ");
                        }
                    }
                    if (days <= 1) {
                        int remaining = duration.intValue() % SECONDS_IN_A_DAY;
                        final int hours = remaining / SECONDS_IN_AN_HOUR;
                        if (hours > 0) {
                            remainingTimeStr.append(hours);
                            if (hours > 1) {
                                remainingTimeStr.append(labels.getString("label.hours"));
                            } else {
                                remainingTimeStr.append(labels.getString("label.hour"));
                                remainingTimeStr.append(" ");
                            }
                        }
                        if (hours <= 1) {
                            remaining = remaining % SECONDS_IN_AN_HOUR;
                            final int minutes = remaining / SECONDS_IN_A_MINUTE;
                            if (minutes > 0) {
                                remainingTimeStr.append(minutes);
                                remainingTimeStr.append(labels.getString("label.minutes"));
                            }
                        }
                    }
                    attributes.put(ActivityAttribute.remainingTime.name(), remainingTimeStr.toString());
                } else {
                    attributes.put(ActivityAttribute.remainingTime.name(), labels.getString("label.overdue"));
                }
            }
        } catch (final Exception e) {
            // the activity is not a user task
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "the activity " + activityInstanceId + " is not a user task.", e);
            }
        }
        if (activityInstanceDisplayName != null && activityInstanceDisplayName.length() > 0) {
            attributes.put(ActivityAttribute.name.name(), activityInstanceDisplayName);
        } else {
            attributes.put(ActivityAttribute.name.name(), activityInstanceName);
        }
        if (activityInstanceDisplayDesc != null) {
            attributes.put(ActivityAttribute.description.name(), activityInstanceDisplayDesc);
        }
        attributes.put(ActivityAttribute.type.name(), activityInstanceType);
        attributes.put(ActivityAttribute.state.name(), activityInstanceState);
        if (reachedStateDate != null) {
            dateFormat = getInitializedDateFormat(dateFormat, locale);
            attributes.put(ActivityAttribute.reachedStateDate.name(), dateFormat.format(reachedStateDate));
        }
        if (lastUpdateDate != null) {
            dateFormat = getInitializedDateFormat(dateFormat, locale);
            attributes.put(ActivityAttribute.lastUpdate.name(), dateFormat.format(lastUpdateDate));
        }
        return attributes;
    }

    protected DateFormat getInitializedDateFormat(DateFormat dateFormat, final Locale locale) {
        if (dateFormat == null) {
            dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, locale);
        }
        return dateFormat;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long startInstance(final APISession session, final long userID, final long processDefinitionID) throws ProcessDefinitionNotFoundException,
            BPMEngineException, InvalidSessionException, CreationException, ProcessActivationException,
            ExecutionException, UserNotFoundException {

        final ProcessAPI processAPI = getBpmEngineAPIUtil().getProcessAPI(session);
        return processAPI.startProcess(userID, processDefinitionID).getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void terminateTask(final APISession session, final long userID, final long activityInstanceID) throws BPMEngineException, InvalidSessionException,
            FlowNodeExecutionException {
        final ProcessAPI processAPI = getBpmEngineAPIUtil().getProcessAPI(session);
        processAPI.executeFlowNode(userID, activityInstanceID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isUserAdminOrProcessOwner(final APISession session, final long processDefinitionId) throws UserNotFoundException, InvalidSessionException,
            ProcessDefinitionNotFoundException, BPMEngineException {
        final ProfileAPI profileAPI = getBpmEngineAPIUtil().getProfileAPI(session);
        final boolean hasUserAdminProfile = hasUserAdminProfile(session, profileAPI);
        if (hasUserAdminProfile) {
            return true;
        }
        final ProcessAPI processAPI = getBpmEngineAPIUtil().getProcessAPI(session);
        return processAPI.isUserProcessSupervisor(processDefinitionId, session.getUserId());
    }

    protected boolean hasUserAdminProfile(final APISession session, final ProfileAPI profileAPI) {
        List<Profile> profiles;
        int startIndex = 0;
        final int maxResults = 10;
        do {
            profiles = profileAPI.getProfilesForUser(session.getUserId(), startIndex, maxResults, ProfileCriterion.NAME_ASC);
            for (final Profile profile : profiles) {
                if (ADMIN_PROFILE_NAME.equals(profile.getName())) {
                    return true;
                }
            }
            startIndex += maxResults;
        } while (!profiles.isEmpty());
        return false;
    }

    @Override
    public Boolean canStartProcessDefinition(final APISession session, final long userId, final long processDefinitionId) throws BPMEngineException {
        try {
            final CommandAPI commandAPI = getBpmEngineAPIUtil().getCommandAPI(session);
            final Map<String, Serializable> parameters = new HashMap<String, Serializable>();
            if (userId != -1) {
                parameters.put("USER_ID_KEY", userId);
            } else {
                parameters.put("USER_ID_KEY", session.getUserId());
            }
            parameters.put("PROCESS_DEFINITION_ID_KEY", processDefinitionId);
            return (Boolean) commandAPI.execute("canStartProcessDefinition", parameters);
        } catch (final CommandExecutionException e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE,
                        "The engine was not able to find out if the user can start the process. Error while executing command:");
            }
            throw new BPMEngineException(e);
        } catch (final CommandParameterizationException e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE,
                        "The engine was not able to find out if the user can start the process. Error in the parameters of the command:");
            }
            throw new BPMEngineException(e);
        } catch (final CommandNotFoundException e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "Unknown engine command:");
            }
            throw new BPMEngineException(e);
        }
    }

    @Override
    public Boolean canUserSeeHumanTask(final APISession session, final long userId, final long humanTaskInstanceId) throws BPMEngineException,
            ActivityInstanceNotFoundException, UserNotFoundException {
        final ProcessAPI processAPI = getBpmEngineAPIUtil().getProcessAPI(session);
        return processAPI.isInvolvedInHumanTaskInstance(session.getUserId(), humanTaskInstanceId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canUserSeeProcessInstance(final APISession session, final long processInstanceID)
            throws ProcessInstanceNotFoundException, BPMEngineException, InvalidSessionException, UserNotFoundException, ProcessDefinitionNotFoundException {
        final ProcessAPI processAPI = getBpmEngineAPIUtil().getProcessAPI(session);
        return processAPI.isInvolvedInProcessInstance(session.getUserId(), processInstanceID);
    }

    /**
     * {@inheritDoc}
     *
     * @throws BPMEngineException
     */
    @Override
    public Map<String, Serializable> getActivityFieldsValues(final APISession session, final long activityInstanceID, final List<Expression> expressions,
            final Map<String, FormFieldValue> fieldValues, final Locale locale, final boolean isCurrentValue,
            final Map<String, Serializable> transientDataContext) throws BPMExpressionEvaluationException, InvalidSessionException, FileTooBigException,
                    IOException, BPMEngineException {

        final IFormExpressionsAPI formExpressionsAPI = FormAPIFactory.getFormExpressionsAPI();
        return formExpressionsAPI.evaluateActivityExpressions(session, activityInstanceID, expressions, fieldValues, locale, isCurrentValue,
                transientDataContext);
    }

    /**
     * {@inheritDoc}
     *
     * @throws BPMEngineException
     */
    @Override
    public Map<String, Serializable> getActivityFieldsValues(final APISession session, final long activityInstanceID, final List<Expression> expressions,
            final Map<String, FormFieldValue> fieldValues, final Locale locale, final boolean isCurrentValue) throws BPMExpressionEvaluationException,
                    InvalidSessionException, FileTooBigException, IOException, BPMEngineException {

        return getActivityFieldsValues(session, activityInstanceID, expressions, fieldValues, locale, isCurrentValue, new HashMap<String, Serializable>());
    }

    /**
     * {@inheritDoc}
     *
     * @throws BPMEngineException
     */
    @Override
    public Map<String, Serializable> getActivityFieldsValues(final APISession session, final long activityInstanceID, final List<Expression> expressions,
            final Locale locale, final boolean isCurrentValue, final Map<String, Serializable> transientDataContext) throws BPMExpressionEvaluationException,
                    InvalidSessionException, BPMEngineException {

        final IFormExpressionsAPI formExpressionsAPI = FormAPIFactory.getFormExpressionsAPI();
        return formExpressionsAPI.evaluateActivityInitialExpressions(session, activityInstanceID, expressions, locale, isCurrentValue, transientDataContext);
    }

    /**
     * {@inheritDoc}
     *
     * @throws BPMEngineException
     */
    @Override
    public Map<String, Serializable> getActivityFieldsValues(final APISession session, final long activityInstanceID, final List<Expression> expressions,
            final Locale locale, final boolean isCurrentValue) throws BPMExpressionEvaluationException, InvalidSessionException, BPMEngineException {

        return getActivityFieldsValues(session, activityInstanceID, expressions, locale, isCurrentValue, new HashMap<String, Serializable>());
    }

    /**
     * {@inheritDoc}
     *
     * @throws BPMEngineException
     */
    @Override
    public Map<String, Serializable> getInstanceFieldsValues(final APISession session, final long processInstanceID, final List<Expression> expressions,
            final Map<String, FormFieldValue> fieldValues, final Locale locale, final boolean isCurrentValue,
            final Map<String, Serializable> transientDataContext) throws BPMExpressionEvaluationException, InvalidSessionException, FileTooBigException,
                    IOException, BPMEngineException {

        final IFormExpressionsAPI formExpressionsAPI = FormAPIFactory.getFormExpressionsAPI();
        return formExpressionsAPI.evaluateInstanceExpressions(session, processInstanceID, expressions, fieldValues, locale, isCurrentValue,
                transientDataContext);
    }

    /**
     * {@inheritDoc}
     *
     * @throws BPMEngineException
     */
    @Override
    public Map<String, Serializable> getInstanceFieldsValues(final APISession session, final long processInstanceID, final List<Expression> expressions,
            final Map<String, FormFieldValue> fieldValues, final Locale locale, final boolean isCurrentValue) throws BPMExpressionEvaluationException,
                    InvalidSessionException, FileTooBigException, IOException, BPMEngineException {

        return getInstanceFieldsValues(session, processInstanceID, expressions, fieldValues, locale, isCurrentValue, new HashMap<String, Serializable>());
    }

    /**
     * {@inheritDoc}
     *
     * @throws BPMEngineException
     */
    @Override
    public Map<String, Serializable> getInstanceFieldsValues(final APISession session, final long processInstanceID, final List<Expression> expressions,
            final Locale locale, final boolean isCurrentValue, final Map<String, Serializable> transientDataContext) throws BPMExpressionEvaluationException,
                    InvalidSessionException, BPMEngineException {

        final IFormExpressionsAPI formExpressionsAPI = FormAPIFactory.getFormExpressionsAPI();
        return formExpressionsAPI.evaluateInstanceInitialExpressions(session, processInstanceID, expressions, locale, isCurrentValue, transientDataContext);
    }

    /**
     * {@inheritDoc}
     *
     * @throws BPMEngineException
     */
    @Override
    public Map<String, Serializable> getInstanceFieldsValues(final APISession session, final long processInstanceID, final List<Expression> expressions,
            final Locale locale, final boolean isCurrentValue) throws BPMExpressionEvaluationException, InvalidSessionException, BPMEngineException {

        return getInstanceFieldsValues(session, processInstanceID, expressions, locale, isCurrentValue, new HashMap<String, Serializable>());
    }

    /**
     * {@inheritDoc}
     *
     * @throws BPMEngineException
     */
    @Override
    public Map<String, Serializable> getProcessFieldsValues(final APISession session, final long processDefinitionID, final List<Expression> expressions,
            final Map<String, FormFieldValue> fieldValues, final Locale locale, final Map<String, Serializable> transientDataContext)
                    throws BPMExpressionEvaluationException, InvalidSessionException, FileTooBigException, IOException, BPMEngineException {

        final IFormExpressionsAPI formExpressionsAPI = FormAPIFactory.getFormExpressionsAPI();
        return formExpressionsAPI.evaluateProcessExpressions(session, processDefinitionID, expressions, fieldValues, locale, transientDataContext);
    }

    /**
     * {@inheritDoc}
     *
     * @throws BPMEngineException
     */
    @Override
    public Map<String, Serializable> getProcessFieldsValues(final APISession session, final long processDefinitionID, final List<Expression> expressions,
            final Map<String, FormFieldValue> fieldValues, final Locale locale) throws BPMExpressionEvaluationException, InvalidSessionException,
                    FileTooBigException, IOException, BPMEngineException {

        return getProcessFieldsValues(session, processDefinitionID, expressions, fieldValues, locale, new HashMap<String, Serializable>());
    }

    /**
     * {@inheritDoc}
     *
     * @throws BPMEngineException
     */
    @Override
    public Map<String, Serializable> getProcessFieldsValues(final APISession session, final long processDefinitionID, final List<Expression> expressions,
            final Locale locale, final Map<String, Serializable> transientDataContext) throws BPMExpressionEvaluationException, InvalidSessionException,
                    BPMEngineException {

        final IFormExpressionsAPI formExpressionsAPI = FormAPIFactory.getFormExpressionsAPI();
        return formExpressionsAPI.evaluateProcessInitialExpressions(session, processDefinitionID, expressions, locale, transientDataContext);
    }

    /**
     * {@inheritDoc}
     *
     * @throws BPMEngineException
     */
    @Override
    public Map<String, Serializable> getProcessFieldsValues(final APISession session, final long processDefinitionID, final List<Expression> expressions,
            final Locale locale) throws BPMExpressionEvaluationException, InvalidSessionException, BPMEngineException {

        return getProcessFieldsValues(session, processDefinitionID, expressions, locale, new HashMap<String, Serializable>());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getProcessInstanceIDFromActivityInstanceID(final APISession session, final long activityInstanceID) throws BPMEngineException,
            InvalidSessionException, ActivityInstanceNotFoundException {

        final ProcessAPI processAPI = getBpmEngineAPIUtil().getProcessAPI(session);
        try {
            return processAPI.getActivityInstance(activityInstanceID).getParentProcessInstanceId();
        } catch (final ActivityInstanceNotFoundException e) {
            return processAPI.getArchivedActivityInstance(activityInstanceID).getProcessInstanceId();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getProcessDefinitionIDFromActivityInstanceID(final APISession session, final long activityInstanceID) throws ActivityInstanceNotFoundException,
            BPMEngineException, InvalidSessionException {

        final ProcessAPI processAPI = getBpmEngineAPIUtil().getProcessAPI(session);
        try {
            return processAPI.getActivityInstance(activityInstanceID).getProcessDefinitionId();
        } catch (final ActivityInstanceNotFoundException e) {
            return processAPI.getArchivedActivityInstance(activityInstanceID).getProcessDefinitionId();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getProcessDefinitionIDFromProcessInstanceID(final APISession session, final long processInstanceID) throws ProcessInstanceNotFoundException,
            BPMEngineException, ProcessDefinitionNotFoundException, InvalidSessionException, ArchivedProcessInstanceNotFoundException {

        final ProcessAPI processAPI = getBpmEngineAPIUtil().getProcessAPI(session);
        try {
            final ProcessInstance processInstance = processAPI.getProcessInstance(processInstanceID);
            return processInstance.getProcessDefinitionId();
        } catch (final ProcessInstanceNotFoundException e) {
            final SearchOptionsBuilder searchOptionsBuilder = new SearchOptionsBuilder(0, 1);
            searchOptionsBuilder.filter(ArchivedProcessInstancesSearchDescriptor.SOURCE_OBJECT_ID, processInstanceID);
            searchOptionsBuilder.sort(ArchivedProcessInstancesSearchDescriptor.ARCHIVE_DATE, Order.ASC);
            SearchResult<ArchivedProcessInstance> searchArchivedProcessInstances = null;
            try {
                searchArchivedProcessInstances = processAPI.searchArchivedProcessInstancesInAllStates(searchOptionsBuilder.done());
            } catch (final SearchException se) {
                throw new ArchivedProcessInstanceNotFoundException(se);
            }
            if (searchArchivedProcessInstances != null && searchArchivedProcessInstances.getCount() > 0) {
                return searchArchivedProcessInstances.getResult().get(0).getProcessDefinitionId();
            } else {
                throw new ArchivedProcessInstanceNotFoundException(new Exception("Unable to find an archive process instance with ID " + processInstanceID));
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getProcessDefinitionIDFromUUID(final APISession session, final String processDefinitionUUIDStr) throws ProcessDefinitionNotFoundException,
            BPMEngineException, ProcessDefinitionNotFoundException, InvalidSessionException {

        final ProcessAPI processAPI = getBpmEngineAPIUtil().getProcessAPI(session);
        final String[] splittedProcessDefinitionUUID = processDefinitionUUIDStr.split(UUID_SEPARATOR);
        if (splittedProcessDefinitionUUID.length == 2) {
            return processAPI.getProcessDefinitionId(splittedProcessDefinitionUUID[0], splittedProcessDefinitionUUID[1]);
        } else if (splittedProcessDefinitionUUID.length == 1) {
            return Long.valueOf(processDefinitionUUIDStr);
        } else {
            throw new RuntimeException("Invalid process definition UUID " + processDefinitionUUIDStr);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getActivityDefinitionUUIDFromActivityInstanceID(final APISession session, final long activityInstanceID)
            throws ActivityInstanceNotFoundException, ProcessDefinitionNotFoundException, BPMEngineException, InvalidSessionException {

        final ProcessAPI processAPI = getBpmEngineAPIUtil().getProcessAPI(session);
        String activityName;
        ProcessDefinition processDefinition;
        try {
            final ActivityInstance activityInstance = processAPI.getActivityInstance(activityInstanceID);
            activityName = activityInstance.getName();
            processDefinition = processAPI.getProcessDefinition(activityInstance.getProcessDefinitionId());
        } catch (final ActivityInstanceNotFoundException e) {
            final ArchivedActivityInstance archivedActivityInstance = processAPI.getArchivedActivityInstance(activityInstanceID);
            activityName = archivedActivityInstance.getName();
            processDefinition = processAPI.getProcessDefinition(archivedActivityInstance.getProcessDefinitionId());
        }
        return processDefinition.getName() + UUID_SEPARATOR + processDefinition.getVersion() + UUID_SEPARATOR + activityName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getActivityDefinitionKeyFromActivityInstanceID(final APISession session, final long activityInstanceID)
            throws ActivityInstanceNotFoundException, ProcessDefinitionNotFoundException, BPMEngineException, InvalidSessionException {

        final ProcessAPI processAPI = getBpmEngineAPIUtil().getProcessAPI(session);
        String activityName;
        ProcessDefinition processDefinition;
        try {
            final ActivityInstance activityInstance = processAPI.getActivityInstance(activityInstanceID);
            activityName = activityInstance.getName();
            processDefinition = processAPI.getProcessDefinition(activityInstance.getProcessDefinitionId());
        } catch (final ActivityInstanceNotFoundException e) {
            final ArchivedActivityInstance archivedActivityInstance = processAPI.getArchivedActivityInstance(activityInstanceID);
            activityName = archivedActivityInstance.getName();
            processDefinition = processAPI.getProcessDefinition(archivedActivityInstance.getProcessDefinitionId());
        }
        return urlEncode(processDefinition.getName()) + "/" + urlEncode(processDefinition.getVersion()) + "/" + urlEncode(activityName);
    }

    protected String urlEncode(final String stringToEncode) {
        try {
            return URLEncoder.encode(stringToEncode, "UTF-8").replaceAll("\\+", "%20").replaceAll("%2F", "/");
        } catch (final UnsupportedEncodingException e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "Unable to URL encode the process name, version and/or task name", e);
            }
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isProcessEnabled(final APISession session, final long processDefinitionID) throws InvalidSessionException, BPMEngineException,
            ProcessDefinitionNotFoundException {
        final ProcessAPI processAPI = getBpmEngineAPIUtil().getProcessAPI(session);
        return ActivationState.ENABLED.equals(processAPI.getProcessDeploymentInfo(processDefinitionID).getActivationState());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getActivityName(final APISession session, final long activityInstanceID) throws InvalidSessionException, BPMEngineException,
            ActivityInstanceNotFoundException {
        final ProcessAPI processAPI = getBpmEngineAPIUtil().getProcessAPI(session);
        String displayName;
        try {
            final ActivityInstance activityInstance = processAPI.getActivityInstance(activityInstanceID);
            displayName = activityInstance.getDisplayName();
            if (displayName == null) {
                displayName = activityInstance.getName();
            }
        } catch (final ActivityInstanceNotFoundException e) {
            final ArchivedActivityInstance archivedActivityInstance = processAPI.getArchivedActivityInstance(activityInstanceID);
            displayName = archivedActivityInstance.getDisplayName();
            if (displayName == null) {
                displayName = archivedActivityInstance.getName();
            }
        }
        return displayName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void assignTaskIfNotAssigned(final APISession session, final long taskId, final long userId) throws TaskAssignationException,
            InvalidSessionException,
            ForbiddenFormAccessException {
        try {
            final ProcessAPI processAPI = getBpmEngineAPIUtil().getProcessAPI(session);
            final HumanTaskInstance humanTaskInstance = processAPI.getHumanTaskInstance(taskId);
            if (humanTaskInstance.getAssigneeId() <= 0) {
                processAPI.assignUserTask(taskId, userId);
            } else if (humanTaskInstance.getAssigneeId() != userId) {
                throw new ForbiddenFormAccessException("The task " + taskId + " is already assigned to another user.");
            }
        } catch (final BPMEngineException e) {
            throw new TaskAssignationException("An error occured while communicating with the engine", e);
        } catch (final UpdateException e) {
            throw new TaskAssignationException("Couldn't assign task " + taskId + " to user " + session.getUserId(), e);
        } catch (final ActivityInstanceNotFoundException e) {
            throw new TaskAssignationException("Couldn't assign task " + taskId + " to user " + session.getUserId() + " : Task not found !", e);
        }
    }

    @Override
    public boolean isTaskReady(final APISession session, final long activityInstanceID) throws BPMEngineException, InvalidSessionException {
        try {
            final HumanTaskInstance taskInstance = getBpmEngineAPIUtil().getProcessAPI(session).getHumanTaskInstance(activityInstanceID);
            return ActivityStates.READY_STATE.equals(taskInstance.getState());
        } catch (final ActivityInstanceNotFoundException e) {
            return false;
        }
    }

    protected BPMEngineAPIUtil getBpmEngineAPIUtil() {
        return bpmEngineAPIUtil;
    }

}
