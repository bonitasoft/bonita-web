/**
 * Copyright (C) 2009 BonitaSoft S.A.
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
package org.bonitasoft.forms.server.api;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map; 
import java.util.Set;

import org.bonitasoft.console.common.server.utils.BPMEngineException;
import org.bonitasoft.console.common.server.utils.BPMExpressionEvaluationException;
import org.bonitasoft.engine.bpm.actor.ActorNotFoundException;
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
import org.bonitasoft.engine.exception.RetrieveException;
import org.bonitasoft.engine.exception.SearchException;
import org.bonitasoft.engine.identity.UserNotFoundException;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.session.InvalidSessionException;
import org.bonitasoft.forms.client.model.ActivityEditState;
import org.bonitasoft.forms.client.model.Expression;
import org.bonitasoft.forms.client.model.FormAction;
import org.bonitasoft.forms.client.model.FormFieldValue;
import org.bonitasoft.forms.server.exception.FileTooBigException;
import org.bonitasoft.forms.server.exception.TaskAssignationException;

/**
 * Workflow service useful to bind the form to bonita server
 * 
 * @author Anthony Birembaut
 */
public interface IFormWorkflowAPI {

    /**
     * Check the child and parent pocesses and retrieve the next task uuid if it is in the user task list
     * 
     * @param session
     *            the API session
     * @param processInstanceId
     *            the UUID of the current process instance
     * @return the next task UUID or null there is no next task or if the next task is not in the user todolist
     * @throws ProcessInstanceNotFoundException
     * @throws BPMEngineException
     * @throws UserNotFoundException
     * @throws ProcessDefinitionNotFoundException
     */
    long getRelatedProcessesNextTask(final APISession session, final long processInstanceId) throws InvalidSessionException, RetrieveException,
            BPMEngineException, UserNotFoundException, SearchException, ProcessDefinitionNotFoundException;

    /**
     * Retrieve any task id of the user todolist. If the process UUID is valid, the task belong to the process otherwise any task from the user todolist can be
     * returned.
     * Return null if no task is found.
     * 
     * @param session
     *            the API session
     * @param processDefinitionID
     * @return the activity instance ID of one of the task of the user todolist
     * @throws BPMEngineException
     * @throws UserNotFoundException
     * @throws ProcessInstanceNotFoundException
     */
    long getAnyTodoListTaskForProcessDefinition(APISession session, long processDefinitionID) throws ProcessDefinitionNotFoundException, BPMEngineException,
            InvalidSessionException, UserNotFoundException, RetrieveException, ProcessInstanceNotFoundException;

    /**
     * Retrieve any task id of the user todolist. If the process instance UUID is valid, the task belong to the process instance otherwise any task from the
     * user todolist can be returned.
     * Return null if no task is found.
     * 
     * @param session
     *            the API session
     * @param processInstanceID
     * @return the activity instance ID of one of the task of the user todolist
     * @throws BPMEngineException
     * @throws ProcessInstanceNotFoundException
     * @throws UserNotFoundException
     * @throws InvalidSessionException
     */
    long getAnyTodoListTaskForProcessInstance(APISession session, long processInstanceID) throws BPMEngineException, ProcessInstanceNotFoundException,
            UserNotFoundException, RetrieveException, InvalidSessionException;

    /**
     * Retrieve the deployment date of a process definition
     * 
     * @param session
     *            the API session
     * @param processDefinitionID
     * @return the {@link Date} of the process deployment date
     * @throws ProcessDefinitionNotFoundException
     * @throws BPMEngineException
     */
    Date getProcessDefinitionDate(APISession session, long processDefinitionID) throws ProcessDefinitionNotFoundException, BPMEngineException,
            InvalidSessionException, RetrieveException;

    /**
     * Retrieve the migration date of a process definition
     * 
     * @param session
     *            the API session
     * @param processDefinitionID
     * @return the {@link Date} of the process deployment
     * @throws ProcessDefinitionNotFoundException
     * @throws BPMEngineException
     */
    Date getMigrationDate(APISession session, long processDefinitionID) throws ProcessDefinitionNotFoundException, BPMEngineException, InvalidSessionException,
            InvalidSessionException, RetrieveException;

    /**
     * Retrieve a field initial value
     * 
     * @param session
     *            the API session
     * @param activityInstanceID
     *            the activity instance ID
     * @param expression
     *            the initial value expression
     * @param locale
     *            the user's locale
     * @param isCurrentValue
     *            if true, value returned is the current value for the instance. otherwise, it's the value at step end
     * @return the initial value for this field
     * @throws BPMExpressionEvaluationException 
     * @throws BPMEngineException 
     */
    Serializable getActivityFieldValue(APISession session, long activityInstanceID, Expression expression, Locale locale, boolean isCurrentValue)
            throws InvalidSessionException, BPMExpressionEvaluationException, BPMEngineException;

    /**
     * Retrieve a field initial value
     * 
     * @param session
     *            the API session
     * @param activityInstanceID
     *            the activity instance ID
     * @param expression
     *            the initial value expression
     * @param locale
     *            the user's locale
     * @param isCurrentValue
     *            if true, value returned is the current value for the instance. otherwise, it's the value at step end
     * @return the initial value for this field
     * @throws BPMExpressionEvaluationException 
     * @throws BPMEngineException 
     */
    Serializable getActivityFieldValue(APISession session, long activityInstanceID, Expression expression, Locale locale, boolean isCurrentValue,
            Map<String, Serializable> context) throws InvalidSessionException, BPMExpressionEvaluationException, BPMEngineException;

    /**
     * Retrieve a field initial value
     * 
     * @param session
     *            the API session
     * @param processDefinitionID
     *            the process definition ID
     * @param expression
     *            the initial value expression
     * @param locale
     *            the user's locale
     * @return the initial value for this field
     * @throws BPMEngineException
     * @throws BPMExpressionEvaluationException 
     */
    Serializable getProcessFieldValue(APISession session, long processDefinitionID, Expression expression, Locale locale) throws BPMEngineException,
            InvalidSessionException, BPMExpressionEvaluationException;

    /**
     * Retrieve a field initial value
     * 
     * @param session
     *            the API session
     * @param processDefinitionID
     *            the process definition ID
     * @param expression
     *            the initial value expression
     * @param locale
     *            the user's locale
     * @return the initial value for this field
     * @throws BPMExpressionEvaluationException 
     * @throws BPMEngineException 
     */
    Serializable getProcessFieldValue(APISession session, long processDefinitionID, Expression expression, Locale locale, Map<String, Serializable> context)
            throws InvalidSessionException, BPMExpressionEvaluationException, BPMEngineException;

    /**
     * Retrieve a field initial value
     * 
     * @param session
     *            the API session
     * @param processInstanceID
     *            the process instance ID
     * @param expression
     *            the initial value expression
     * @param locale
     *            the user's locale
     * @param isCurrentValue
     *            if true, value returned is the current value for the instance. otherwise, it's the value at instantiation
     * @return the initial value for this field
     * @throws BPMExpressionEvaluationException 
     * @throws BPMEngineException 
     */
    Serializable getInstanceFieldValue(APISession session, long processInstanceID, Expression expression, Locale locale, boolean isCurrentValue)
            throws InvalidSessionException, BPMExpressionEvaluationException, BPMEngineException;

    /**
     * Retrieve a field initial value
     * 
     * @param session
     *            the API session
     * @param processInstanceID
     *            the process instance ID
     * @param expression
     *            the initial value expression
     * @param locale
     *            the user's locale
     * @param isCurrentValue
     *            if true, value returned is the current value for the instance. otherwise, it's the value at instantiation
     * @return the initial value for this field
     * @throws BPMExpressionEvaluationException 
     * @throws BPMEngineException 
     */
    Serializable getInstanceFieldValue(APISession session, long processInstanceID, Expression expression, Locale locale, boolean isCurrentValue,
            Map<String, Serializable> context) throws InvalidSessionException, BPMExpressionEvaluationException, BPMEngineException;

    /**
     * Retrieve a field value
     * 
     * @param session
     *            the API session
     * @param activityInstanceID
     *            the activity instance ID
     * @param expression
     *            the initial value expression
     * @param fieldValues
     *            some field values this field may depend on
     * @param locale
     *            the user's locale
     * @param isCurrentValue
     *            if true, value returned is the current value for the instance. otherwise, it's the value at step end
     * @return the value for this field
     * @throws IOException
     * @throws FileTooBigException
     * @throws BPMExpressionEvaluationException 
     * @throws BPMEngineException 
     */
    Serializable getActivityFieldValue(APISession session, long activityInstanceID, Expression expression, Map<String, FormFieldValue> fieldValues,
            Locale locale, boolean isCurrentValue) throws InvalidSessionException, FileTooBigException, IOException,
            BPMExpressionEvaluationException, BPMEngineException;

    /**
     * Retrieve a field value
     * 
     * @param session
     *            the API session
     * @param activityInstanceID
     *            the activity instance ID
     * @param expression
     *            the initial value expression
     * @param fieldValues
     *            some field values this field may depend on
     * @param locale
     *            the user's locale
     * @param isCurrentValue
     *            if true, value returned is the current value for the instance. otherwise, it's the value at step end
     * @return the value for this field
     * @throws IOException
     * @throws FileTooBigException
     * @throws BPMExpressionEvaluationException 
     * @throws BPMEngineException 
     */
    Serializable getActivityFieldValue(APISession session, long activityInstanceID, Expression expression, Map<String, FormFieldValue> fieldValues,
            Locale locale, boolean isCurrentValue, Map<String, Serializable> context) throws InvalidSessionException, FileTooBigException,
            IOException, BPMExpressionEvaluationException, BPMEngineException;

    /**
     * Retrieve a field value
     * 
     * @param session
     *            the API session
     * @param processDefinitionID
     *            the process definition ID
     * @param expression
     *            the initial value expression
     * @param fieldValues
     *            some field values this field may depend on
     * @param locale
     *            the user's locale
     * @return the value for this field
     * @throws IOException
     * @throws FileTooBigException
     * @throws BPMExpressionEvaluationException 
     * @throws BPMEngineException 
     */
    Serializable getProcessFieldValue(APISession session, long processDefinitionID, Expression expression, Map<String, FormFieldValue> fieldValues,
            Locale locale) throws InvalidSessionException, FileTooBigException, IOException, BPMExpressionEvaluationException, BPMEngineException;

    /**
     * Retrieve a field value
     * 
     * @param session
     *            the API session
     * @param processDefinitionID
     *            the process definition ID
     * @param expression
     *            the initial value expression
     * @param fieldValues
     *            some field values this field may depend on
     * @param locale
     *            the user's locale
     * @return the value for this field
     * @throws IOException
     * @throws FileTooBigException
     * @throws BPMExpressionEvaluationException 
     * @throws BPMEngineException 
     */
    Serializable getProcessFieldValue(APISession session, long processDefinitionID, Expression expression, Map<String, FormFieldValue> fieldValues,
            Locale locale, Map<String, Serializable> context) throws InvalidSessionException, FileTooBigException, IOException, BPMExpressionEvaluationException, BPMEngineException;

    /**
     * Retrieve a field value
     * 
     * @param session
     *            the API session
     * @param processInstanceID
     *            the process instance ID
     * @param expression
     *            the initial value expression
     * @param fieldValues
     *            some field values this field may depend on
     * @param locale
     *            the user's locale
     * @param isCurrentValue
     *            if true, value returned is the current value for the instance. otherwise, it's the value at instantiation
     * @return the value for this field
     * @throws IOException
     * @throws FileTooBigException
     * @throws BPMExpressionEvaluationException 
     * @throws BPMEngineException 
     */
    Serializable getInstanceFieldValue(APISession session, long processInstanceID, Expression expression, Map<String, FormFieldValue> fieldValues,
            Locale locale, boolean isCurrentValue) throws InvalidSessionException, FileTooBigException, IOException, BPMExpressionEvaluationException, BPMEngineException;

    /**
     * Retrieve a field value
     * 
     * @param session
     *            the API session
     * @param processInstanceID
     *            the process instance ID
     * @param expression
     *            the initial value expression
     * @param fieldValues
     *            some field values this field may depend on
     * @param locale
     *            the user's locale
     * @param isCurrentValue
     *            if true, value returned is the current value for the instance. otherwise, it's the value at instantiation
     * @return the value for this field
     * @throws IOException
     * @throws FileTooBigException
     * @throws BPMExpressionEvaluationException 
     * @throws BPMEngineException 
     */
    Serializable getInstanceFieldValue(APISession session, long processInstanceID, Expression expression, Map<String, FormFieldValue> fieldValues,
            Locale locale, boolean isCurrentValue, Map<String, Serializable> context) throws InvalidSessionException, FileTooBigException,
            IOException, BPMExpressionEvaluationException, BPMEngineException;

    /**
     * Start terminate a task and execute a number of actions specifying the pressed submit button id
     * (this way, only actions related to this button will be performed)
     * 
     * @param session
     *            the API session
     * @param activityInstanceID
     *            the activity instance ID
     * @param fieldValues
     *            a Map of the fields id and values
     * @param actions
     *            a list of {@link FormAction} to execute at form validation
     * @param submitButtonId
     *            the pressed submit button id
     * @param locale
     *            the user's locale
     * @throws ProcessInstanceNotFoundException
     * @throws ActivityInstanceNotFoundException
     * @throws IOException
     * @throws BPMExpressionEvaluationException 
     * @throws BPMEngineException 
     */
    void executeActionsAndTerminate(APISession session, long activityInstanceID, Map<String, FormFieldValue> fieldValues, List<FormAction> actions,
            Locale locale, String submitButtonId, Map<String, Serializable> context) throws InvalidSessionException,
            ActivityInstanceNotFoundException, ProcessInstanceNotFoundException, RetrieveException, FileTooBigException, IOException, BPMExpressionEvaluationException, BPMEngineException;

    /**
     * Instantiate a process and execute actions specifying the pressed submit button id
     * (this way, only actions related to this button will be performed).
     * 
     * @param session
     *            the API session
     * @param processDefinitionID
     *            the process definition ID
     * @param fieldValues
     *            a Map of the fields id and values
     * @param actions
     *            a list of {@link FormAction} to execute at form validation
     * @param submitButtonId
     *            the pressed submit button id
     * @param locale
     *            the user's locale
     * @return the process instance ID of the process instance created
     * @throws FileTooBigException
     * @throws IOException
     * @throws BPMExpressionEvaluationException 
     * @throws BPMEngineException 
     */
    long executeActionsAndStartInstance(APISession session, long userId, long processDefinitionID, Map<String, FormFieldValue> fieldValues,
            List<FormAction> actions, Locale locale, String submitButtonId, Map<String, Serializable> context) throws InvalidSessionException, FileTooBigException, IOException, BPMExpressionEvaluationException, BPMEngineException;

    /**
     * Retrieve the step attributes for the activity
     * 
     * @param session
     *            the API session
     * @param activityInstanceID
     *            the activity instance ID
     * @param locale
     *            the user's locale
     * @return a {@link List} of Candidates as Strings
     * @throws ActivityInstanceNotFoundException
     * @throws BPMEngineException
     * @throws ActivityInstanceNotFoundException
     */
    Map<String, String> getActivityAttributes(APISession session, long activityInstanceID, Locale locale) throws ActivityInstanceNotFoundException,
            BPMEngineException, InvalidSessionException, ActivityInstanceNotFoundException, RetrieveException;

    /**
     * Return the activity edition state
     * 
     * @param session
     *            the API session
     * @param activityInstanceID
     *            the activity instance ID
     * @return the {@link ActivityEditState}
     * @throws BPMEngineException
     * @throws ActivityInstanceNotFoundException
     * @throws InvalidSessionException
     */
    ActivityEditState getTaskEditState(APISession session, long activityInstanceID) throws BPMEngineException, ActivityInstanceNotFoundException,
            RetrieveException, InvalidSessionException;

    /**
     * Execute a task
     * 
     * @param session
     *            the API session
     * @param activityInstanceID
     *            the activity instance ID
     * @throws InvalidSessionException
     * @throws BPMEngineException
     * @throws FlowNodeExecutionException
     */
    void terminateTask(APISession session, long activityInstanceID) throws BPMEngineException, InvalidSessionException, FlowNodeExecutionException;

    /**
     * Start an instance
     * 
     * @param session
     *            the API session
     * @param processDefinitionID
     *            the process definition ID
     * @return the process instance ID
     * @throws ProcessDefinitionNotFoundException
     * @throws BPMEngineException
     * @throws ProcessDefinitionNotEnabledException
     * @throws CreationException
     * @throws ExecutionException
     * @throws ProcessActivationException
     */
    long startInstance(APISession session, long processDefinitionID) throws ProcessDefinitionNotFoundException, BPMEngineException, InvalidSessionException,
            RetrieveException, ProcessDefinitionNotEnabledException, RetrieveException, CreationException, ProcessActivationException, ExecutionException;

    /**
     * Check if the user can istantiate a process definition
     * 
     * @param session
     *            the API session
     * @param userProcessActors
     *            the process actors of the user
     * @param processDefinitionID
     *            the process definition UUID
     * @return true if the user is involved in the process instance. False otherwise
     * @throws ProcessInstanceNotFoundException
     * @throws BPMEngineException
     * @throws ActorNotFoundException
     */
    boolean canUserInstantiateProcessDefinition(APISession session, Map<Long, Set<Long>> userProcessActors, long processDefinitionID)
            throws ProcessDefinitionNotFoundException, BPMEngineException, InvalidSessionException, InvalidSessionException, ActorNotFoundException;

    /**
     * Check if a user can see a process instance
     * 
     * @param session
     *            the API session
     * @param userProcessActors
     *            the process actors of the user
     * @param processInstanceID
     *            the process instance UUID
     * @return true if the user is involved in the process instance. False otherwise
     * @throws ProcessInstanceNotFoundException
     * @throws BPMEngineException
     * @throws UserNotFoundException
     * @throws ProcessDefinitionNotFoundException
     */
    boolean canUserSeeProcessInstance(APISession session, Map<Long, Set<Long>> userProcessActors, long processInstanceID)
            throws ProcessInstanceNotFoundException, BPMEngineException, InvalidSessionException, RetrieveException, UserNotFoundException,
            ProcessDefinitionNotFoundException;

    /**
     * Check if the user is involved in an activity instance
     * 
     * @param session
     *            the API session
     * @param userProcessActors
     *            the process actors of the user
     * @param activityInstanceID
     *            the activity instance ID
     * @return true if the user is involved in the activity instance. False otherwise
     * @throws ActivityInstanceNotFoundException
     * @throws BPMEngineException
     * @throws ProcessDefinitionNotFoundException
     * @throws ArchivedFlowNodeInstanceNotFoundException
     */
    boolean isUserInvolvedInActivityInstance(APISession session, Map<Long, Set<Long>> userProcessActors, long activityInstanceID)
            throws ActivityInstanceNotFoundException, BPMEngineException, InvalidSessionException, RetrieveException, ProcessDefinitionNotFoundException,
            ArchivedFlowNodeInstanceNotFoundException;

    /**
     * Retrieve some fields initial value
     * 
     * @param session
     *            the API session
     * @param activityInstanceID
     *            the activity instance ID
     * @param expressions
     *            the initial values expressions
     * @param fieldValues
     *            some field values this field may depend on
     * @param locale
     *            the user's locale
     * @param isCurrentValue
     *            if true, value returned is the current value for the instance. otherwise, it's the value at step end
     * @param transientDataContext
     *            the context of transient data
     * @return the values for the fields as a Map
     * @throws IOException
     * @throws FileTooBigException
     * @throws BPMExpressionEvaluationException 
     * @throws BPMEngineException 
     */
    Map<String, Serializable> getActivityFieldsValues(APISession session, long activityInstanceID, List<Expression> expressions,
            Map<String, FormFieldValue> fieldValues, Locale locale, boolean isCurrentValue, Map<String, Serializable> transientDataContext)
            throws InvalidSessionException, FileTooBigException, IOException, BPMExpressionEvaluationException, BPMEngineException;

    /**
     * Retrieve some fields initial value
     * 
     * @param session
     *            the API session
     * @param activityInstanceID
     *            the activity instance ID
     * @param expressions
     *            the initial values expressions
     * @param fieldValues
     *            some field values this field may depend on
     * @param locale
     *            the user's locale
     * @param isCurrentValue
     *            if true, value returned is the current value for the instance. otherwise, it's the value at step end
     * @return the values for the fields as a Map
     * @throws IOException
     * @throws FileTooBigException
     * @throws BPMExpressionEvaluationException 
     * @throws BPMEngineException 
     */
    Map<String, Serializable> getActivityFieldsValues(APISession session, long activityInstanceID, List<Expression> expressions,
            Map<String, FormFieldValue> fieldValues, Locale locale, boolean isCurrentValue) throws InvalidSessionException,
            FileTooBigException, IOException, BPMExpressionEvaluationException, BPMEngineException;

    /**
     * Retrieve some fields initial value
     * 
     * @param session
     *            the API session
     * @param activityInstanceID
     *            the activity instance ID
     * @param expressions
     *            the initial values expressions
     * @param locale
     *            the user's locale
     * @param isCurrentValue
     *            if true, value returned is the current value for the instance. otherwise, it's the value at step end
     * @param transientDataContext
     * @return the values for the fields as a Map
     * @throws BPMExpressionEvaluationException 
     * @throws BPMEngineException 
     */
    Map<String, Serializable> getActivityFieldsValues(APISession session, long activityInstanceID, List<Expression> expressions, Locale locale,
            boolean isCurrentValue, Map<String, Serializable> transientDataContext) throws InvalidSessionException, BPMExpressionEvaluationException, BPMEngineException;

    /**
     * Retrieve some fields initial value
     * 
     * @param session
     *            the API session
     * @param activityInstanceID
     *            the activity instance ID
     * @param expressions
     *            the initial values expressions
     * @param locale
     *            the user's locale
     * @param isCurrentValue
     *            if true, value returned is the current value for the instance. otherwise, it's the value at step end
     * @return the values for the fields as a Map
     * @throws BPMExpressionEvaluationException 
     * @throws BPMEngineException 
     */
    Map<String, Serializable> getActivityFieldsValues(APISession session, long activityInstanceID, List<Expression> expressions, Locale locale,
            boolean isCurrentValue) throws InvalidSessionException, BPMExpressionEvaluationException, BPMEngineException;

    /**
     * Retrieve some fields initial value
     * 
     * @param session
     *            the API session
     * @param processInstanceID
     *            the process instance ID
     * @param expressions
     *            the initial values expressions
     * @param fieldValues
     *            some field values this field may depend on
     * @param locale
     *            the user's locale
     * @param isCurrentValue
     *            if true, value returned is the current value for the instance. otherwise, it's the value at instantiation
     * @param transientDataContext
     *            the context of transient data
     * @return the values for the fields as a Map
     * @throws IOException
     * @throws FileTooBigException
     * @throws BPMExpressionEvaluationException 
     * @throws BPMEngineException 
     */
    Map<String, Serializable> getInstanceFieldsValues(APISession session, long processInstanceID, List<Expression> expressions,
            Map<String, FormFieldValue> fieldValues, Locale locale, boolean isCurrentValue, Map<String, Serializable> transientDataContext)
            throws InvalidSessionException, FileTooBigException, IOException, BPMExpressionEvaluationException, BPMEngineException;

    /**
     * Retrieve some fields initial value
     * 
     * @param session
     *            the API session
     * @param processInstanceID
     *            the process instance ID
     * @param expressions
     *            the initial values expressions
     * @param fieldValues
     *            some field values this field may depend on
     * @param locale
     *            the user's locale
     * @param isCurrentValue
     *            if true, value returned is the current value for the instance. otherwise, it's the value at instantiation
     * @return the values for the fields as a Map
     * @throws IOException
     * @throws FileTooBigException
     * @throws BPMExpressionEvaluationException 
     * @throws BPMEngineException 
     */
    Map<String, Serializable> getInstanceFieldsValues(APISession session, long processInstanceID, List<Expression> expressions,
            Map<String, FormFieldValue> fieldValues, Locale locale, boolean isCurrentValue) throws InvalidSessionException,
            FileTooBigException, IOException, BPMExpressionEvaluationException, BPMEngineException;

    /**
     * Retrieve some fields initial value
     * 
     * @param session
     *            the API session
     * @param processInstanceID
     *            the process instance ID
     * @param expressions
     *            the initial values expressions
     * @param locale
     *            the user's locale
     * @param isCurrentValue
     *            if true, value returned is the current value for the instance. otherwise, it's the value at instantiation
     * @param transientDataContext
     *            the context of transient data
     * @return the values for the fields as a Map
     * @throws BPMExpressionEvaluationException 
     * @throws BPMEngineException 
     */
    Map<String, Serializable> getInstanceFieldsValues(APISession session, long processInstanceID, List<Expression> expressions, Locale locale,
            boolean isCurrentValue, Map<String, Serializable> transientDataContext) throws InvalidSessionException, BPMExpressionEvaluationException, BPMEngineException;

    /**
     * Retrieve some fields initial value
     * 
     * @param session
     *            the API session
     * @param processInstanceID
     *            the process instance ID
     * @param expressions
     *            the initial values expressions
     * @param locale
     *            the user's locale
     * @param isCurrentValue
     *            if true, value returned is the current value for the instance. otherwise, it's the value at instantiation
     * @return the values for the fields as a Map
     * @throws BPMExpressionEvaluationException 
     * @throws BPMEngineException 
     */
    Map<String, Serializable> getInstanceFieldsValues(APISession session, long processInstanceID, List<Expression> expressions, Locale locale,
            boolean isCurrentValue) throws InvalidSessionException, BPMExpressionEvaluationException, BPMEngineException;

    /**
     * Retrieve some fields initial value
     * 
     * @param session
     *            the API session
     * @param processDefinitionID
     *            the process definition ID
     * @param expressions
     *            the initial values expressions
     * @param fieldValues
     *            some field values this field may depend on
     * @param locale
     *            the user's locale
     * @param transientDataContext
     *            the context of transient data
     * @return the values for the fields as a Map
     * @throws IOException
     * @throws FileTooBigException
     * @throws BPMExpressionEvaluationException 
     * @throws BPMEngineException 
     */
    Map<String, Serializable> getProcessFieldsValues(APISession session, long processDefinitionID, List<Expression> expressions,
            Map<String, FormFieldValue> fieldValues, Locale locale, Map<String, Serializable> transientDataContext) throws InvalidSessionException, FileTooBigException, IOException, BPMExpressionEvaluationException, BPMEngineException;

    /**
     * Retrieve some fields initial value
     * 
     * @param session
     *            the API session
     * @param processDefinitionID
     *            the process definition ID
     * @param expressions
     *            the initial values expressions
     * @param fieldValues
     *            some field values this field may depend on
     * @param locale
     *            the user's locale
     * @return the values for the fields as a Map
     * @throws IOException
     * @throws FileTooBigException
     * @throws BPMExpressionEvaluationException 
     * @throws BPMEngineException 
     */
    Map<String, Serializable> getProcessFieldsValues(APISession session, long processDefinitionID, List<Expression> expressions,
            Map<String, FormFieldValue> fieldValues, Locale locale) throws InvalidSessionException, FileTooBigException, IOException, BPMExpressionEvaluationException, BPMEngineException;

    /**
     * Retrieve some fields initial value
     * 
     * @param session
     *            the API session
     * @param processDefinitionID
     *            the process definition ID
     * @param expressions
     *            the initial values expressions
     * @param locale
     *            the user's locale
     * @param transientDataContext
     *            the context of transient data
     * @return the values for the fields as a Map
     * @throws BPMExpressionEvaluationException 
     * @throws BPMEngineException 
     */
    Map<String, Serializable> getProcessFieldsValues(APISession session, long processDefinitionID, List<Expression> expressions, Locale locale,
            Map<String, Serializable> transientDataContext) throws InvalidSessionException, BPMExpressionEvaluationException, BPMEngineException;

    /**
     * Retrieve some fields initial value
     * 
     * @param session
     *            the API session
     * @param processDefinitionID
     *            the process definition ID
     * @param expressions
     *            the initial values expressions
     * @param locale
     *            the user's locale
     * @return the values for the fields as a Map
     * @throws BPMExpressionEvaluationException 
     * @throws BPMEngineException 
     */
    Map<String, Serializable> getProcessFieldsValues(APISession session, long processDefinitionID, List<Expression> expressions, Locale locale)
            throws InvalidSessionException, BPMExpressionEvaluationException, BPMEngineException;

    /**
     * Retrieve the process instance ID from an activity instance ID
     * 
     * @param session
     *            the API session
     * @param activityInstanceID
     *            the activity instance ID
     * @return the process instance ID
     * @throws BPMEngineException
     * @throws ProcessInstanceNotFoundException
     * @throws ArchivedFlowNodeInstanceNotFoundException
     */
    long getProcessInstanceIDFromActivityInstanceID(APISession session, long activityInstanceID) throws BPMEngineException, InvalidSessionException,
            ActivityInstanceNotFoundException;

    /**
     * Retrieve the process definition ID from an activity instance ID
     * 
     * @param session
     *            the API session
     * @param activityInstanceID
     *            the activity instance ID
     * @return the process definition ID
     * @throws BPMEngineException
     * @throws ActivityInstanceNotFoundException
     * @throws InvalidSessionException
     */
    long getProcessDefinitionIDFromActivityInstanceID(APISession session, long activityInstanceID) throws BPMEngineException, InvalidSessionException,
            ActivityInstanceNotFoundException;

    /**
     * Retrieve the process definition ID from a process instance ID
     * 
     * @param session
     *            the API session
     * @param processInstanceID
     *            the process instance ID
     * @return the process definition ID
     * @throws BPMEngineException
     * @throws ProcessInstanceNotFoundException
     * @throws ProcessDefinitionNotFoundException
     * @throws ArchivedProcessInstanceNotFoundException
     */
    long getProcessDefinitionIDFromProcessInstanceID(APISession session, long processInstanceID) throws BPMEngineException, InvalidSessionException,
            ProcessInstanceNotFoundException, ProcessDefinitionNotFoundException, ArchivedProcessInstanceNotFoundException;

    /**
     * Retrieve the process ID for a given process definition UUID
     * 
     * @param session
     *            the API session
     * @param processDefinitionUUIDStr
     *            the signature of a process containing its name and version
     * @return true if the process definition ID refers to a process with the same name and version as in the process definition UUID
     * @throws ProcessDefinitionNotFoundException
     * @throws BPMEngineException
     */
    long getProcessDefinitionIDFromUUID(APISession session, String processDefinitionUUIDStr) throws BPMEngineException, InvalidSessionException,
            ProcessDefinitionNotFoundException;

    /**
     * Retrieve an activity definition UUID for a given activity instance
     * 
     * @param session
     *            the API session
     * @param activityInstanceID
     *            the activity instance ID
     * @return the signature of an activity as an UUID containing the process' name and version and the activity name
     * @throws BPMEngineException
     * @throws InvalidSessionException
     * @throws ActivityInstanceNotFoundException
     * @throws ProcessDefinitionNotFoundException
     * @throws ArchivedFlowNodeInstanceNotFoundException
     * @throws ProcessDefinitionReadException
     */
    String getActivityDefinitionUUIDFromActivityInstanceID(APISession session, long activityInstanceID) throws BPMEngineException, InvalidSessionException,
            ActivityInstanceNotFoundException, RetrieveException, ProcessDefinitionNotFoundException;

    /**
     * Check if a process is enabled
     * 
     * @param session
     *            the API session
     * @param processDefinitionID
     *            the process definition ID
     * @return true if the process is enabled
     * @throws BPMEngineException
     * @throws InvalidSessionException
     * @throws ProcessDefinitionReadException
     * @throws ProcessDefinitionNotFoundException
     */
    boolean isProcessEnabled(APISession session, long processDefinitionID) throws InvalidSessionException, BPMEngineException,
            ProcessDefinitionNotFoundException, RetrieveException;

    /**
     * Retrieve the task name of an activity
     * 
     * @param session
     * @param activityInstanceID
     * @return the activity name
     * @throws BPMEngineException
     * @throws InvalidSessionException
     * @throws ActivityInstanceNotFoundException
     * @throws ArchivedFlowNodeInstanceNotFoundException
     */
    String getActivityName(APISession session, long activityInstanceID) throws InvalidSessionException, BPMEngineException, ActivityInstanceNotFoundException,
            RetrieveException;

    /**
     * Assign a task to the user of whom belong the session
     * 
     * @param session
     *            the user's session
     * @param taskId
     *            the task to assign
     * @throws TaskAssignationException
     *             if the task couldn't be assign
     * @throws InvalidSessionException
     *             if session isn't valid
     */
    void assignTask(APISession session, long taskId) throws TaskAssignationException, InvalidSessionException;

    /**
     * indicate if the activity instance ID passed matches a ready task
     * 
     * @param session
     *            the user's session
     * @param activityInstanceID
     *            the activity instance ID
     * @return true if the activity is a ready task
     * @throws InvalidSessionException
     * @throws BPMEngineException
     */
    boolean isTaskReady(APISession session, long activityInstanceID) throws BPMEngineException, InvalidSessionException;

}
