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
package org.bonitasoft.forms.server.api.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.actor.ActorCriterion;
import org.bonitasoft.engine.bpm.actor.ActorInstance;
import org.bonitasoft.engine.bpm.actor.ActorMember;
import org.bonitasoft.engine.bpm.bar.BusinessArchive;
import org.bonitasoft.engine.bpm.bar.BusinessArchiveBuilder;
import org.bonitasoft.engine.bpm.data.DataInstance;
import org.bonitasoft.engine.bpm.document.Document;
import org.bonitasoft.engine.bpm.document.DocumentValue;
import org.bonitasoft.engine.bpm.flownode.ActivityInstance;
import org.bonitasoft.engine.bpm.flownode.ActivityInstanceCriterion;
import org.bonitasoft.engine.bpm.flownode.ArchivedHumanTaskInstanceSearchDescriptor;
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance;
import org.bonitasoft.engine.bpm.process.DesignProcessDefinition;
import org.bonitasoft.engine.bpm.process.ProcessDefinition;
import org.bonitasoft.engine.bpm.process.impl.ProcessDefinitionBuilder;
import org.bonitasoft.engine.expression.ExpressionBuilder;
import org.bonitasoft.engine.expression.ExpressionType;
import org.bonitasoft.engine.identity.User;
import org.bonitasoft.engine.search.SearchOptions;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.forms.client.model.ActionType;
import org.bonitasoft.forms.client.model.ActivityAttribute;
import org.bonitasoft.forms.client.model.ActivityEditState;
import org.bonitasoft.forms.client.model.Expression;
import org.bonitasoft.forms.client.model.FormAction;
import org.bonitasoft.forms.client.model.FormFieldValue;
import org.bonitasoft.forms.server.FormsTestCase;
import org.bonitasoft.forms.server.WaitUntil;
import org.bonitasoft.forms.server.api.FormAPIFactory;
import org.bonitasoft.forms.server.api.IFormWorkflowAPI;
import org.bonitasoft.test.toolkit.bpm.TestHumanTask;
import org.bonitasoft.test.toolkit.bpm.TestProcess;
import org.bonitasoft.test.toolkit.bpm.TestProcessFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit test for the implementation of the form workflow API
 * 
 * @author Anthony Birembaut
 * 
 */
public class TestFormWorkflowAPIImpl extends FormsTestCase {

    private ProcessAPI processAPI = null;

    private ProcessDefinition bonitaProcess;

    private long processInstanceID;

    private Expression dataExpression;

    private ActorMember actorMember;

    private ActorInstance processActor;

    private FormWorkflowAPIImpl formWorkflowApi;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        formWorkflowApi = (FormWorkflowAPIImpl) FormAPIFactory.getFormWorkflowAPI();

        final ProcessDefinitionBuilder processBuilder = new ProcessDefinitionBuilder().createNewInstance("firstProcess", "1.0");
        final ExpressionBuilder expressionBuilder = new ExpressionBuilder();
        processBuilder.addData(
                "Application",
                String.class.getName(),
                expressionBuilder.createNewInstance("word").setContent("Word").setExpressionType(ExpressionType.TYPE_CONSTANT.name())
                        .setReturnType(String.class.getName()).done());
        processBuilder.addActor("myActor");
        processBuilder.addUserTask("Request", "myActor");
        processBuilder.addUserTask("Approval", "myActor");
        processBuilder.addTransition("Request", "Approval");

        final DesignProcessDefinition designProcessDefinition = processBuilder.done();
        final BusinessArchiveBuilder businessArchiveBuilder = new BusinessArchiveBuilder().createNewBusinessArchive();
        final BusinessArchive businessArchive = businessArchiveBuilder.setProcessDefinition(designProcessDefinition).done();
        processAPI = TenantAPIAccessor.getProcessAPI(getSession());
        bonitaProcess = processAPI.deploy(businessArchive);

        final User user = getInitiator().getUser();

        processActor = processAPI.getActors(bonitaProcess.getId(), 0, 1, ActorCriterion.NAME_ASC).get(0);
        actorMember = processAPI.addUserToActor(processActor.getId(), user.getId());

        processAPI.enableProcess(bonitaProcess.getId());

        processAPI = TenantAPIAccessor.getProcessAPI(getSession());
        processInstanceID = processAPI.startProcess(bonitaProcess.getId()).getId();
        final List<Expression> dependencies = new ArrayList<Expression>();
        dependencies.add(new Expression("Application", "Application", ExpressionType.TYPE_VARIABLE.name(), String.class.getName(), null,
                new ArrayList<Expression>()));
        dataExpression = new Expression(null, "Application", ExpressionType.TYPE_READ_ONLY_SCRIPT.name(), String.class.getName(), "GROOVY", dependencies);
    }

    @Override
    @After
    public void tearDown() throws Exception {
        processAPI.removeActorMember(actorMember.getId());
        processAPI.disableProcess(bonitaProcess.getId());
        processAPI.deleteProcess(bonitaProcess.getId());
        super.tearDown();
    }

    @Test
    public void testGetTaskFieldValue() throws Exception {
        Assert.assertTrue("no pending user task instances are found", new WaitUntil(50, 1000) {

            @Override
            protected boolean check() throws Exception {
                return processAPI.getPendingHumanTaskInstances(TestFormWorkflowAPIImpl.this.getSession().getUserId(), 0, 10, null)
                        .size() >= 1;
            }
        }.waitUntil());
        final HumanTaskInstance humanTaskInstance = processAPI.getPendingHumanTaskInstances(getSession().getUserId(), 0, 1,
                ActivityInstanceCriterion.NAME_ASC).get(0);
        final Object result = formWorkflowApi.getActivityFieldValue(getSession(), humanTaskInstance.getId(), dataExpression, Locale.ENGLISH, true);
        Assert.assertNotNull(result);
        Assert.assertEquals("Word", result);
    }

    @Test
    public void testGetInstanceFieldValue() throws Exception {
        final Object result = formWorkflowApi.getInstanceFieldValue(getSession(), processInstanceID, dataExpression, Locale.ENGLISH, true);
        Assert.assertEquals("Word", result);
    }

    @Test
    public void testExecuteActionsAndTerminate() throws Exception {
        Assert.assertTrue("no pending user task instances are found", new WaitUntil(50, 1000) {

            @Override
            protected boolean check() throws Exception {
                return processAPI.getPendingHumanTaskInstances(TestFormWorkflowAPIImpl.this.getSession().getUserId(), 0, 10, null)
                        .size() >= 1;
            }
        }.waitUntil());
        HumanTaskInstance humanTaskInstance = processAPI.getPendingHumanTaskInstances(getSession().getUserId(), 0, 1,
                ActivityInstanceCriterion.NAME_ASC).get(0);
        final Map<String, FormFieldValue> fieldValues = new HashMap<String, FormFieldValue>();
        final FormFieldValue value1 = new FormFieldValue("Excel", String.class.getName());
        fieldValues.put("fieldId1", value1);
        final List<FormAction> formActions = new ArrayList<FormAction>();
        final Expression fieldExpression = new Expression(null, "field_fieldId1", ExpressionType.TYPE_INPUT.name(), String.class.getName(), null,
                new ArrayList<Expression>());
        formActions.add(new FormAction(ActionType.ASSIGNMENT, "Application", false, "=", null, fieldExpression, "submitButtonId"));
        processAPI.assignUserTask(humanTaskInstance.getId(), getSession().getUserId());
        formWorkflowApi.executeActionsAndTerminate(getSession(), humanTaskInstance.getId(), fieldValues, formActions, Locale.ENGLISH, "submitButtonId",
                new HashMap<String, Serializable>());
        Assert.assertTrue("no pending user task instances are found", new WaitUntil(50, 1000) {

            @Override
            protected boolean check() throws Exception {
                return processAPI.getPendingHumanTaskInstances(TestFormWorkflowAPIImpl.this.getSession().getUserId(), 0, 10, null)
                        .size() >= 1;
            }
        }.waitUntil());
        humanTaskInstance = processAPI.getPendingHumanTaskInstances(getSession().getUserId(), 0, 1, ActivityInstanceCriterion.NAME_ASC).get(0);
        final String activityName = humanTaskInstance.getName();
        Assert.assertNotNull(activityName);
        Assert.assertEquals("Approval", activityName);
        final DataInstance dataInstance = processAPI.getProcessDataInstance("Application", processInstanceID);
        Assert.assertEquals("Excel", dataInstance.getValue().toString());
    }

    @Ignore
    // waiting ENGINE-975
    @Test
    public void testExecuteActionsAndTerminateWithDocument() throws Exception {
        final IFormWorkflowAPI api = FormAPIFactory.getFormWorkflowAPI();
        Assert.assertTrue("no pending user task instances are found", new WaitUntil(50, 1000) {

            @Override
            protected boolean check() throws Exception {
                return processAPI.getPendingHumanTaskInstances(TestFormWorkflowAPIImpl.this.getSession().getUserId(), 0, 10, null)
                        .size() >= 1;
            }
        }.waitUntil());
        HumanTaskInstance humanTaskInstance = processAPI.getPendingHumanTaskInstances(getSession().getUserId(), 0, 1,
                ActivityInstanceCriterion.NAME_ASC).get(0);
        final Map<String, FormFieldValue> fieldValues = new HashMap<String, FormFieldValue>();
        final FormFieldValue value1 = new FormFieldValue(new DocumentValue("http://www.bonitasoft.org"), DocumentValue.class.getName());
        fieldValues.put("fieldId1", value1);
        final List<FormAction> formActions = new ArrayList<FormAction>();
        final Expression fieldExpression = new Expression(null, "field_fieldId1", ExpressionType.TYPE_INPUT.name(), DocumentValue.class.getName(), null,
                new ArrayList<Expression>());
        formActions.add(new FormAction(ActionType.DOCUMENT_CREATE_UPDATE, "DocumentToCreate", false, "=", null, fieldExpression, "submitButtonId"));
        processAPI.assignUserTask(humanTaskInstance.getId(), getSession().getUserId());
        api.executeActionsAndTerminate(getSession(), humanTaskInstance.getId(), fieldValues, formActions, Locale.ENGLISH, "submitButtonId",
                new HashMap<String, Serializable>());
        Assert.assertTrue("no pending user task instances are found", new WaitUntil(50, 1000) {

            @Override
            protected boolean check() throws Exception {
                return processAPI.getPendingHumanTaskInstances(TestFormWorkflowAPIImpl.this.getSession().getUserId(), 0, 10, null)
                        .size() >= 1;
            }
        }.waitUntil());
        humanTaskInstance = processAPI.getPendingHumanTaskInstances(getSession().getUserId(), 0, 1, ActivityInstanceCriterion.NAME_ASC).get(0);
        final String activityName = humanTaskInstance.getName();
        Assert.assertNotNull(activityName);
        Assert.assertEquals("Approval", activityName);
        final Document createdDocument = processAPI.getLastDocument(processInstanceID, "DocumentToCreate");
        Assert.assertEquals("http://www.bonitasoft.org", createdDocument.getUrl());
    }

    // FIXME when it will be possible to call the engine API from a groovy script
    // @Test
    // public void testExecuteActionsAndTerminateWithAPIAccessor() throws Exception {
    // IFormWorkflowAPI api = FormAPIFactory.getFormWorkflowAPI();
    // Assert.assertTrue("no pending user task instances are found", new WaitUntil(50, 1000) {
    // @Override
    // protected boolean check() throws Exception {
    // return processAPI.getPendingHumanTaskInstances(getSession().getUserId(), 0, 10, null).size() >= 1;
    // }
    // }.waitUntil());
    // HumanTaskInstance humanTaskInstance = processAPI.getPendingHumanTaskInstances(getSession().getUserId(), 0, 1, ActivityInstanceCriterion.NAME_ASC).get(0);
    // Map<String, FormFieldValue> fieldValues = new HashMap<String, FormFieldValue>();
    // List<FormAction> formActions = new ArrayList<FormAction>();
    // Expression fieldExpression = new Expression(null, "processAPI.getProcessDataInstance(\"Applications\", processInstanceId)",
    // ExpressionType.TYPE_READ_ONLY_SCRIPT.name(), String.class.getName(), "GROOVY", new ArrayList<Expression>());
    // formActions.add(new FormAction(ActionType.ASSIGNMENT, "Applications", "=", fieldExpression, "submitButtonId"));
    // api.executeActionsAndTerminate(getSession(), humanTaskInstance.getId(), fieldValues, formActions, Locale.ENGLISH, "submitButtonId", new HashMap<String,
    // Serializable>());
    // humanTaskInstance = processAPI.getPendingHumanTaskInstances(getSession().getUserId(), 0, 1, ActivityInstanceCriterion.NAME_ASC).get(0);
    // String activityName = humanTaskInstance.getName();
    // Assert.assertNotNull(activityName);
    // Assert.assertEquals("Approval", activityName);
    // Object variableValue = processAPI.getProcessDataInstance("Applications", processInstanceID);
    // Assert.assertEquals("Word", variableValue.toString());
    // }

    @Test
    public void testExecuteActionsAndStartInstance() throws Exception {
        final Map<String, FormFieldValue> fieldValues = new HashMap<String, FormFieldValue>();
        final FormFieldValue value1 = new FormFieldValue("Excel", String.class.getName());
        fieldValues.put("fieldId1", value1);
        final List<FormAction> formActions = new ArrayList<FormAction>();
        final Expression fieldExpression = new Expression(null, "field_fieldId1", ExpressionType.TYPE_INPUT.name(), String.class.getName(), null,
                new ArrayList<Expression>());
        formActions.add(new FormAction(ActionType.ASSIGNMENT, "Application", false, "=", null, fieldExpression, "submitButtonId"));
        final long newProcessInstanceID = formWorkflowApi.executeActionsAndStartInstance(getSession(), 1l, bonitaProcess.getId(), fieldValues, formActions,
                Locale.ENGLISH, "submitButtonId", new HashMap<String, Serializable>());
        final DataInstance dataInstance = processAPI.getProcessDataInstance("Application", newProcessInstanceID);
        Assert.assertEquals("Excel", dataInstance.getValue().toString());
    }

    @Ignore("Fix in progress")
    @Test
    public void testGetRelatedProcessesNextTask() throws Exception {
        Assert.assertTrue("no pending user task instances are found", new WaitUntil(50, 1000) {

            @Override
            protected boolean check() throws Exception {
                return processAPI.getPendingHumanTaskInstances(TestFormWorkflowAPIImpl.this.getSession().getUserId(), 0, 10, null)
                        .size() >= 1;
            }
        }.waitUntil());
        HumanTaskInstance humanTaskInstance = processAPI.getPendingHumanTaskInstances(getSession().getUserId(), 0, 1,
                ActivityInstanceCriterion.NAME_ASC).get(0);
        Assert.assertNotNull(humanTaskInstance);
        processAPI.assignUserTask(humanTaskInstance.getId(), getSession().getUserId());
        processAPI.executeFlowNode(humanTaskInstance.getId());
        processAPI.executeFlowNode(humanTaskInstance.getId());
        Assert.assertTrue("no pending user task instances are found", new WaitUntil(50, 1000) {

            @Override
            protected boolean check() throws Exception {
                return processAPI.getPendingHumanTaskInstances(TestFormWorkflowAPIImpl.this.getSession().getUserId(), 0, 10, null)
                        .size() >= 1;
            }
        }.waitUntil());
        humanTaskInstance = processAPI.getPendingHumanTaskInstances(getSession().getUserId(), 0, 1, ActivityInstanceCriterion.NAME_ASC).get(0);
        processAPI.assignUserTask(humanTaskInstance.getId(), getSession().getUserId());
        final long activityInstanceID = formWorkflowApi.getRelatedProcessesNextTask(getSession(), processInstanceID);
        Assert.assertTrue(activityInstanceID >= 0);
        final ActivityInstance activityInstance = processAPI.getActivityInstance(activityInstanceID);
        Assert.assertEquals("Approval", activityInstance.getName());
    }

    @Ignore("Fix in progress")
    @Test
    public void testGetRelatedProcessesNextTaskAfterInstantiation() throws Exception {
        Assert.assertTrue("no pending user task instances are found", new WaitUntil(50, 1000) {

            @Override
            protected boolean check() throws Exception {
                return processAPI.getPendingHumanTaskInstances(TestFormWorkflowAPIImpl.this.getSession().getUserId(), 0, 10, null)
                        .size() >= 1;
            }
        }.waitUntil());
        final HumanTaskInstance humanTaskInstance = processAPI.getPendingHumanTaskInstances(getSession().getUserId(), 0, 1,
                ActivityInstanceCriterion.NAME_ASC).get(0);
        Assert.assertNotNull(humanTaskInstance);
        processAPI.assignUserTask(humanTaskInstance.getId(), getSession().getUserId());
        final long activityInstanceID = formWorkflowApi.getRelatedProcessesNextTask(getSession(), processInstanceID);
        Assert.assertTrue(activityInstanceID > 0);
        final ActivityInstance activityInstance = processAPI.getActivityInstance(activityInstanceID);
        Assert.assertEquals("Request", activityInstance.getName());
    }

    // @Test
    // public void testGetSubprocessNextTask() throws Exception {
    // RuntimeAPI runtimeAPI = AccessorUtil.getRuntimeAPI();
    // ManagementAPI managementAPI = AccessorUtil.getManagementAPI();
    //
    // ProcessDefinition childProcess = ProcessBuilder.createProcess("child_process", "1.0")
    // .addHuman("john")
    // .addHumanTask("childTask", "john")
    // .done();
    //
    // ProcessDefinition parentProcess = ProcessBuilder.createProcess("parent_process", "1.0")
    // .addHuman("john")
    // .addSubProcess("subTask", "child_process")
    // .addHumanTask("parentTask", "john")
    // .addTransition("transition", "subTask", "parentTask")
    // .done();
    //
    // BusinessArchive childBusinessArchive = BusinessArchiveFactory.getBusinessArchive(childProcess);
    // childProcess = managementAPI.deploy(childBusinessArchive);
    //
    // BusinessArchive parentBusinessArchive = BusinessArchiveFactory.getBusinessArchive(parentProcess);
    // parentProcess = managementAPI.deploy(parentBusinessArchive);
    //
    // ProcessInstanceUUID instanceUUID = runtimeAPI.instantiateProcess(parentProcess.getUUID());
    //
    // try {
    // IFormWorkflowAPI api = FormAPIFactory.getFormWorkflowAPI();
    // ActivityInstanceUUID activityInstanceUUID = api.getProcessInstanceNextTask(instanceUUID);
    // Assert.assertNull(activityInstanceUUID);
    // activityInstanceUUID = api.getRelatedProcessesNextTask(instanceUUID);
    // Assert.assertEquals("childTask", activityInstanceUUID.getActivityName());
    //
    // runtimeAPI.executeTask(activityInstanceUUID, true);
    // activityInstanceUUID = api.getRelatedProcessesNextTask(activityInstanceUUID.getProcessInstanceUUID());
    // Assert.assertEquals("parentTask", activityInstanceUUID.getActivityName());
    // } finally {
    // runtimeAPI.deleteProcessInstance(instanceUUID);
    //
    // managementAPI.deleteProcess(parentProcess.getUUID());
    // managementAPI.deleteProcess(childProcess.getUUID());
    // }
    // }
    //
    // @Test
    // public void testGetSubprocessLoopNextTask() throws Exception {
    // RuntimeAPI runtimeAPI = AccessorUtil.getRuntimeAPI();
    // ManagementAPI managementAPI = AccessorUtil.getManagementAPI();
    // QueryRuntimeAPI queryRuntimeAPI = AccessorUtil.getQueryRuntimeAPI();
    // IFormWorkflowAPI api = FormAPIFactory.getFormWorkflowAPI();
    //
    // ProcessDefinition childProcess = ProcessBuilder.createProcess("sub_process", "1.0")
    // .addIntegerData("counter")
    // .addHuman("john")
    // .addHumanTask("task1", "john")
    // .addHumanTask("task2", "john")
    // .addConnector(Event.taskOnFinish, SetVarConnector.class.getName(), true)
    // .addInputParameter("variableName", "counter")
    // .addInputParameter("value", "${++counter}")
    // .addTransition("transition1", "task1", "task2")
    // .done();
    //
    // ProcessDefinition processWithSubProcess = ProcessBuilder.createProcess("parent_process", "1.0")
    // .addHuman("john")
    // .addIntegerData("counter", 0)
    // .addSubProcess("subprocess", "sub_process")
    // .addSubProcessInParameter("counter", "counter")
    // .addSubProcessOutParameter("counter", "counter")
    // .addLoop("counter < 10", false)
    // .addHumanTask("task3", "john")
    // .addTransition("transition2", "subprocess", "task3")
    // .done();
    //
    // BusinessArchive childBusinessArchive = BusinessArchiveFactory.getBusinessArchive(childProcess);
    // childProcess = managementAPI.deploy(childBusinessArchive);
    //
    // BusinessArchive parentBusinessArchive = BusinessArchiveFactory.getBusinessArchive(processWithSubProcess);
    // processWithSubProcess = managementAPI.deploy(parentBusinessArchive);
    //
    // ProcessInstanceUUID processInstanceUUID = runtimeAPI.instantiateProcess(processWithSubProcess.getUUID());
    //
    // try {
    // for (int i = 0; i < 10; i++) {
    // Set<ActivityInstance> activityInstances = queryRuntimeAPI.getActivityInstances(processInstanceUUID);
    // Assert.assertEquals(i+1, activityInstances.size());
    // for (ActivityInstance activityInstance : activityInstances) {
    // Assert.assertEquals("subprocess", activityInstance.getActivityName());
    // }
    //
    // Set<ProcessInstanceUUID> childrenInstanceUUID = queryRuntimeAPI.getChildrenInstanceUUIDsOfProcessInstance(processInstanceUUID);
    // for (ProcessInstanceUUID childInstanceUUID : childrenInstanceUUID) {
    // LightProcessInstance childInstance = queryRuntimeAPI.getLightProcessInstance(childInstanceUUID);
    // if (InstanceState.STARTED.equals(childInstance.getInstanceState())) {
    // Collection<TaskInstance> taskInstances = queryRuntimeAPI.getTaskList(childInstanceUUID, ActivityState.READY);
    // Assert.assertEquals(1, taskInstances.size());
    // TaskInstance childTaskInstance = taskInstances.iterator().next();
    // Assert.assertEquals("task1", childTaskInstance.getActivityName());
    // Assert.assertEquals(ActivityState.READY, childTaskInstance.getState());
    // runtimeAPI.executeTask(childTaskInstance.getUUID(), true);
    //
    // taskInstances = queryRuntimeAPI.getTaskList(childInstanceUUID, ActivityState.READY);
    // Assert.assertEquals(1, taskInstances.size());
    // childTaskInstance = taskInstances.iterator().next();
    // Assert.assertEquals("task2", childTaskInstance.getActivityName());
    // Assert.assertEquals(ActivityState.READY, childTaskInstance.getState());
    // runtimeAPI.executeTask(childTaskInstance.getUUID(), true);
    //
    // if (i < 9) {
    // ActivityInstanceUUID activityInstanceUUID = api.getRelatedProcessesNextTask(childInstance.getUUID());
    // Assert.assertEquals("task1", activityInstanceUUID.getActivityName());
    // }
    //
    // childInstance = queryRuntimeAPI.getLightProcessInstance(childInstanceUUID);
    // Assert.assertEquals(InstanceState.FINISHED, childInstance.getInstanceState());
    // }
    // }
    // }
    //
    // Collection<TaskInstance> taskInstances = queryRuntimeAPI.getTaskList(processInstanceUUID, ActivityState.READY);
    // Assert.assertEquals(1, taskInstances.size());
    // TaskInstance taskInstance = taskInstances.iterator().next();
    // Assert.assertEquals("task3", taskInstance.getActivityName());
    // Assert.assertEquals(ActivityState.READY, taskInstance.getState());
    // runtimeAPI.executeTask(taskInstance.getUUID(), true);
    // } finally {
    // managementAPI.deleteProcess(processWithSubProcess.getUUID());
    // managementAPI.deleteProcess(childProcess.getUUID());
    // }
    // }
    //
    // private ProcessDefinition buildIterationProcess() throws Exception {
    // ProcessDefinition iterationProcess = ProcessBuilder.createProcess("complex_iteration_process", "1.0")
    // .addBooleanData("terminateit", false)
    // .addHuman("john")
    // .addHumanTask("task1", "john")
    // .addSubProcess("subprocess", "sub_process")
    // .addSubProcessInParameter("terminateit", "terminateit")
    // .addSubProcessOutParameter("terminateit", "terminateit")
    // .addHumanTask("task2", "john")
    // .addTransition("transition1", "task1", "subprocess")
    // .addTransition("transition2", "subprocess", "subprocess")
    // .addCondition("!terminateit")
    // .addTransition("transition3", "subprocess", "task2")
    // .addCondition("terminateit")
    // .done();
    //
    // BusinessArchive businessArchive = BusinessArchiveFactory.getBusinessArchive(iterationProcess);
    // return AccessorUtil.getManagementAPI().deploy(businessArchive);
    // }
    //
    // private ProcessDefinition buildSubProcess() throws Exception {
    // ProcessDefinition subProcess = ProcessBuilder.createProcess("sub_process", "1.0")
    // .addBooleanData("terminateit", false)
    // .addHuman("john")
    // .addHumanTask("task1sub", "john")
    // .addSystemTask("activity2sub")
    // .addTransition("transition1", "task1sub", "activity2sub")
    // .done();
    //
    // BusinessArchive businessArchive = BusinessArchiveFactory.getBusinessArchive(subProcess);
    // return AccessorUtil.getManagementAPI().deploy(businessArchive);
    // }
    //
    // @Test
    // public void testIterateOnSubProcess() throws Exception {
    // ProcessDefinition subProcess = buildSubProcess();
    // ProcessDefinition processWithSubProcess = buildIterationProcess();
    //
    // ProcessInstanceUUID processInstanceUUID = AccessorUtil.getRuntimeAPI().instantiateProcess(processWithSubProcess.getUUID());
    //
    // try {
    // Collection<TaskInstance> taskInstances = AccessorUtil.getQueryRuntimeAPI().getTaskList(processInstanceUUID, ActivityState.READY);
    // Assert.assertEquals(1, taskInstances.size());
    // AccessorUtil.getRuntimeAPI().executeTask(taskInstances.iterator().next().getUUID(), true);
    //
    // IFormWorkflowAPI api = FormAPIFactory.getFormWorkflowAPI();
    //
    // TaskInstance subprocessTaskInstance = null;
    //
    // taskInstances = AccessorUtil.getQueryRuntimeAPI().getTaskList(ActivityState.READY);
    // for (TaskInstance taskInstance : taskInstances) {
    // if (subProcess.getUUID().equals(taskInstance.getProcessDefinitionUUID())) {
    // subprocessTaskInstance = taskInstance;
    // }
    // }
    // Assert.assertNotNull(subprocessTaskInstance);
    // Assert.assertEquals("task1sub", subprocessTaskInstance.getActivityName());
    // AccessorUtil.getRuntimeAPI().executeTask(subprocessTaskInstance.getUUID(), true);
    // Assert.assertNotNull(api.getRelatedProcessesNextTask(subprocessTaskInstance.getProcessInstanceUUID()));
    //
    // subprocessTaskInstance = null;
    //
    // taskInstances = AccessorUtil.getQueryRuntimeAPI().getTaskList(ActivityState.READY);
    // for (TaskInstance taskInstance : taskInstances) {
    // if (subProcess.getUUID().equals(taskInstance.getProcessDefinitionUUID())) {
    // subprocessTaskInstance = taskInstance;
    // }
    // }
    // Assert.assertNotNull(subprocessTaskInstance);
    // Assert.assertEquals("task1sub", subprocessTaskInstance.getActivityName());
    // AccessorUtil.getRuntimeAPI().executeTask(subprocessTaskInstance.getUUID(), true);
    // Assert.assertNotNull(api.getRelatedProcessesNextTask(subprocessTaskInstance.getProcessInstanceUUID()));
    //
    // subprocessTaskInstance = null;
    //
    // taskInstances = AccessorUtil.getQueryRuntimeAPI().getTaskList(ActivityState.READY);
    // for (TaskInstance taskInstance : taskInstances) {
    // if (subProcess.getUUID().equals(taskInstance.getProcessDefinitionUUID())) {
    // subprocessTaskInstance = taskInstance;
    // }
    // }
    // Assert.assertNotNull(subprocessTaskInstance);
    // Assert.assertEquals("task1sub", subprocessTaskInstance.getActivityName());
    // AccessorUtil.getRuntimeAPI().setVariable(subprocessTaskInstance.getUUID(), "terminateit", true);
    // AccessorUtil.getRuntimeAPI().executeTask(subprocessTaskInstance.getUUID(), true);
    // Assert.assertNotNull(api.getRelatedProcessesNextTask(subprocessTaskInstance.getProcessInstanceUUID()));
    //
    // TaskInstance processTaskInstance = null;
    //
    // taskInstances = AccessorUtil.getQueryRuntimeAPI().getTaskList(ActivityState.READY);
    // for (TaskInstance taskInstance : taskInstances) {
    // if (processWithSubProcess.getUUID().equals(taskInstance.getProcessDefinitionUUID())) {
    // processTaskInstance = taskInstance;
    // }
    // }
    // Assert.assertNotNull(processTaskInstance);
    // Assert.assertEquals("task2", processTaskInstance.getActivityName());
    // AccessorUtil.getRuntimeAPI().executeTask(processTaskInstance.getUUID(), true);
    // } finally {
    // AccessorUtil.getRuntimeAPI().deleteAllProcessInstances(processWithSubProcess.getUUID());
    // AccessorUtil.getRuntimeAPI().deleteAllProcessInstances(subProcess.getUUID());
    // AccessorUtil.getManagementAPI().deleteProcess(processWithSubProcess.getUUID());
    // AccessorUtil.getManagementAPI().deleteProcess(subProcess.getUUID());
    // }
    // }
    //
    // @Test
    // public void testGetActivityAttachmentFileName() throws Exception {
    // RuntimeAPI runtimeAPI = AccessorUtil.getRuntimeAPI();
    // ManagementAPI managementAPI = AccessorUtil.getManagementAPI();
    // QueryRuntimeAPI queryRuntimeAPI = AccessorUtil.getQueryRuntimeAPI();
    //
    // File attachmentFile = File.createTempFile("attachment-test",".txt");
    // FileOutputStream fileOutputStream = new FileOutputStream(attachmentFile);
    // fileOutputStream.write("test".getBytes("UTF-8"));
    // fileOutputStream.close();
    //
    // ProcessDefinition attachmentProcess = ProcessBuilder.createProcess("attachment_process", "1.0")
    // .addAttachment("attachment", attachmentFile.getPath(), attachmentFile.getName())
    // .addHuman("john")
    // .addHumanTask("task1", "john")
    // .addHumanTask("task2", "john")
    // .addTransition("transition", "task1", "task2")
    // .done();
    //
    // BusinessArchive businessArchive = BusinessArchiveFactory.getBusinessArchive(attachmentProcess);
    // attachmentProcess = managementAPI.deploy(businessArchive);
    //
    // ProcessInstanceUUID instanceUUID = runtimeAPI.instantiateProcess(attachmentProcess.getUUID());
    //
    // try {
    // IFormWorkflowAPI api = FormAPIFactory.getFormWorkflowAPI();
    // ActivityInstanceUUID activityInstanceUUID = null;
    // Collection<TaskInstance> tasks = queryRuntimeAPI.getTaskList(instanceUUID, ActivityState.READY);
    // for (TaskInstance activityInstance : tasks) {
    // activityInstanceUUID = activityInstance.getUUID();
    // }
    // Assert.assertNotNull(activityInstanceUUID);
    // String fileName = api.getAttachmentFileName(activityInstanceUUID, "${attachment}", true);
    // Assert.assertEquals(attachmentFile.getName(), fileName);
    //
    // runtimeAPI.executeTask(activityInstanceUUID, true);
    // fileName = api.getAttachmentFileName(activityInstanceUUID, "${attachment}", true);
    // Assert.assertEquals(attachmentFile.getName(), fileName);
    //
    // File attachmentFile2 = File.createTempFile("new-attachment-test",".txt");
    // tasks = queryRuntimeAPI.getTaskList(instanceUUID, ActivityState.READY);
    // for (TaskInstance activityInstance : tasks) {
    // activityInstanceUUID = activityInstance.getUUID();
    // }
    // Assert.assertNotNull(activityInstanceUUID);
    // runtimeAPI.addAttachment(instanceUUID, "attachment", attachmentFile2.getName(), "test".getBytes("UTF-8"));
    // fileName = api.getAttachmentFileName(activityInstanceUUID, "${attachment}", true);
    // Assert.assertEquals(attachmentFile2.getName(), fileName);
    // } finally {
    // AccessorUtil.getCommandAPI().execute(new WebDeleteDocumentsOfProcessCommand(attachmentProcess.getUUID(), true));
    // AccessorUtil.getRuntimeAPI().deleteProcessInstance(instanceUUID);
    // AccessorUtil.getCommandAPI().execute(new WebDeleteProcessCommand(attachmentProcess.getUUID()));
    // }
    // }
    //
    // @Test
    // public void testGetInstanceAttachmentFileName() throws Exception {
    // RuntimeAPI runtimeAPI = AccessorUtil.getRuntimeAPI();
    // ManagementAPI managementAPI = AccessorUtil.getManagementAPI();
    // QueryRuntimeAPI queryRuntimeAPI = AccessorUtil.getQueryRuntimeAPI();
    //
    // File attachmentFile = File.createTempFile("attachment-test",".txt");
    // FileOutputStream fileOutputStream = new FileOutputStream(attachmentFile);
    // fileOutputStream.write("test".getBytes("UTF-8"));
    // fileOutputStream.close();
    //
    // ProcessDefinition attachmentProcess = ProcessBuilder.createProcess("attachment_process", "1.0")
    // .addAttachment("attachment", attachmentFile.getPath(), attachmentFile.getName())
    // .addHuman("john")
    // .addHumanTask("task1", "john")
    // .addHumanTask("task2", "john")
    // .addTransition("transition", "task1", "task2")
    // .done();
    //
    // BusinessArchive businessArchive = BusinessArchiveFactory.getBusinessArchive(attachmentProcess);
    // attachmentProcess = managementAPI.deploy(businessArchive);
    //
    // ProcessInstanceUUID instanceUUID = runtimeAPI.instantiateProcess(attachmentProcess.getUUID());
    //
    // try {
    // ActivityInstanceUUID activityInstanceUUID = null;
    // Collection<TaskInstance> tasks = queryRuntimeAPI.getTaskList(instanceUUID, ActivityState.READY);
    // for (TaskInstance activityInstance : tasks) {
    // activityInstanceUUID = activityInstance.getUUID();
    // }
    // Assert.assertNotNull(activityInstanceUUID);
    // //In case xcmis is deployed on another server and they don't have exactly the same time
    // Thread.sleep(1000);
    // File attachmentFile2 = File.createTempFile("new-attachment-test",".txt");
    // runtimeAPI.addAttachment(instanceUUID, "attachment", attachmentFile2.getName(), "test".getBytes("UTF-8"));
    // runtimeAPI.executeTask(activityInstanceUUID, true);
    //
    // IFormWorkflowAPI api = FormAPIFactory.getFormWorkflowAPI();
    // String fileName = api.getAttachmentFileName(instanceUUID, "${attachment}", false);
    // Assert.assertEquals(attachmentFile.getName(), fileName);
    // } finally {
    // AccessorUtil.getCommandAPI().execute(new WebDeleteDocumentsOfProcessCommand(attachmentProcess.getUUID(), true));
    // AccessorUtil.getRuntimeAPI().deleteProcessInstance(instanceUUID);
    // AccessorUtil.getCommandAPI().execute(new WebDeleteProcessCommand(attachmentProcess.getUUID()));
    // }
    // }
    //
    // @Test
    // public void testGetDefinitionAttachmentFileName() throws Exception {
    // ManagementAPI managementAPI = AccessorUtil.getManagementAPI();
    //
    // File attachmentFile = File.createTempFile("attachment-test",".txt");
    // FileOutputStream fileOutputStream = new FileOutputStream(attachmentFile);
    // fileOutputStream.write("test".getBytes("UTF-8"));
    // fileOutputStream.close();
    //
    // ProcessDefinition attachmentProcess = ProcessBuilder.createProcess("attachment_process", "1.0")
    // .addAttachment("attachment", attachmentFile.getPath(), attachmentFile.getName())
    // .addHuman("john")
    // .addHumanTask("task1", "john")
    // .addHumanTask("task2", "john")
    // .addTransition("transition", "task1", "task2")
    // .done();
    //
    // BusinessArchive businessArchive = BusinessArchiveFactory.getBusinessArchive(attachmentProcess);
    // attachmentProcess = managementAPI.deploy(businessArchive);
    //
    // try {
    // IFormWorkflowAPI api = FormAPIFactory.getFormWorkflowAPI();
    // String fileName = api.getAttachmentFileName(attachmentProcess.getUUID(), "${attachment}");
    // Assert.assertEquals(attachmentFile.getName(), fileName);
    // } finally {
    // AccessorUtil.getCommandAPI().execute(new WebDeleteDocumentsOfProcessCommand(attachmentProcess.getUUID()));
    // AccessorUtil.getCommandAPI().execute(new WebDeleteProcessCommand(attachmentProcess.getUUID()));
    // }
    // }

    @Test
    public void testGetAnyTodoListTaskForProcessDefinition() throws Exception {
        Assert.assertTrue("no pending user task instances are found", new WaitUntil(50, 1000) {

            @Override
            protected boolean check() throws Exception {
                return processAPI.getPendingHumanTaskInstances(TestFormWorkflowAPIImpl.this.getSession().getUserId(), 0, 10, null)
                        .size() >= 1;
            }
        }.waitUntil());
        final HumanTaskInstance humanTaskInstance = processAPI.getPendingHumanTaskInstances(getSession().getUserId(), 0, 1,
                ActivityInstanceCriterion.NAME_ASC).get(0);
        Assert.assertNotNull(humanTaskInstance);
        processAPI.assignUserTask(humanTaskInstance.getId(), getSession().getUserId());
        // TODO: assign the task to the logged in user first
        final long activityInstanceID = formWorkflowApi.getAnyTodoListTaskForProcessDefinition(getSession(), bonitaProcess.getId());
        // error activityInstanceID == -1 ;
        Assert.assertTrue(activityInstanceID > 0);
        final ActivityInstance activityInstance = processAPI.getActivityInstance(activityInstanceID);
        Assert.assertEquals("Request", activityInstance.getName());
    }

    @Test
    public void testGetAnyTodoListTaskForProcessInstance() throws Exception {
        Assert.assertTrue("no pending user task instances are found", new WaitUntil(50, 1000) {

            @Override
            protected boolean check() throws Exception {
                return processAPI.getPendingHumanTaskInstances(TestFormWorkflowAPIImpl.this.getSession().getUserId(), 0, 10, null)
                        .size() >= 1;
            }
        }.waitUntil());
        final HumanTaskInstance humanTaskInstance = processAPI.getPendingHumanTaskInstances(getSession().getUserId(), 0, 1,
                ActivityInstanceCriterion.NAME_ASC).get(0);
        Assert.assertNotNull(humanTaskInstance);
        processAPI.assignUserTask(humanTaskInstance.getId(), getSession().getUserId());
        // TODO: assign the task to the logged in user first
        final long activityInstanceID = formWorkflowApi.getAnyTodoListTaskForProcessInstance(getSession(), processInstanceID);
        // error activityInstanceID == -1 ;
        Assert.assertTrue(activityInstanceID > 0);
        final ActivityInstance activityInstance = processAPI.getActivityInstance(activityInstanceID);
        Assert.assertEquals("Request", activityInstance.getName());
    }

    @Test
    public void
            getProcessDefinitionDate_return_process_deployment_date() throws Exception {
        final TestProcess process = TestProcessFactory.getDefaultHumanTaskProcess();
        final Date expectedDate = process.getProcessDeploymentInfo().getDeploymentDate();

        final Date processDefinitionDate = formWorkflowApi.getProcessDefinitionDate(getSession(), process.getId());

        assertThat(processDefinitionDate, equalTo(expectedDate));
    }

    @Test
    public void
            isTaskReady_return_true_if_task_is_not_performed() throws Exception {
        final TestHumanTask task = TestProcessFactory.getDefaultHumanTaskProcess().addActor(getInitiator()).startCase().getNextHumanTask();
        task.assignTo(getInitiator());

        final boolean isTaskReady = formWorkflowApi.isTaskReady(getSession(), task.getId());

        assertThat(isTaskReady, is(true));
    }

    @Test
    public void
            isTaskReady_return_false_if_task_is_performed() throws Exception {
        final TestHumanTask task = TestProcessFactory.getDefaultHumanTaskProcess().addActor(getInitiator()).startCase().getNextHumanTask();
        task.assignTo(getInitiator()).execute();

        final boolean isTaskReady = formWorkflowApi.isTaskReady(getSession(), task.getId());

        assertThat(isTaskReady, is(false));
    }

    @Test
    public void
            getTaskEditState_return_EDITABLE_if_task_is_assigned() throws Exception {
        final TestHumanTask task = TestProcessFactory.getDefaultHumanTaskProcess().addActor(getInitiator()).startCase().getNextHumanTask();
        task.assignTo(getInitiator());

        final ActivityEditState state = formWorkflowApi.getTaskEditState(getSession(), task.getId(), false);

        assertThat(state, is(ActivityEditState.EDITABLE));
    }

    @Test
    public void
            getTaskEditState_return_NOT_EDITABLE_if_task_is_performed_using_original_task_ID() throws Exception {
        final TestHumanTask task = TestProcessFactory.getDefaultHumanTaskProcess().addActor(getInitiator()).startCase().getNextHumanTask();
        task.assignTo(getInitiator()).execute();

        final ActivityEditState state = formWorkflowApi.getTaskEditState(getSession(), task.getId(), false);

        assertThat(state, is(ActivityEditState.NOT_EDITABLE));
    }

    @Test
    public void
            getTaskEditState_return_NOT_EDITABLE_if_task_is_performed_using_archived_task_ID() throws Exception {
        final TestHumanTask task = TestProcessFactory.getDefaultHumanTaskProcess().addActor(getInitiator()).startCase().getNextHumanTask();
        task.assignTo(getInitiator()).execute();
        Assert.assertTrue("task not executed yet", new WaitUntil(50, 1000) {

            @Override
            protected boolean check() throws Exception {
                final SearchOptions searchOptions = new SearchOptionsBuilder(0, 10).filter(ArchivedHumanTaskInstanceSearchDescriptor.PROCESS_INSTANCE_ID,
                        task.getHumanTaskInstance().getParentProcessInstanceId()).done();
                return processAPI.searchArchivedHumanTasks(searchOptions).getCount() == 1;
            }
        }.waitUntil());

        final ActivityEditState state = formWorkflowApi.getTaskEditState(getSession(), processAPI.getArchivedActivityInstance(task.getId()).getId(), true);

        assertThat(state, is(ActivityEditState.NOT_EDITABLE));
    }

    @Test
    public void testGetActivityAttributesActor() throws Exception {
        Assert.assertTrue("no pending user task instances are found", new WaitUntil(50, 1000) {

            @Override
            protected boolean check() throws Exception {
                return processAPI.getPendingHumanTaskInstances(TestFormWorkflowAPIImpl.this.getSession().getUserId(), 0, 10, null)
                        .size() >= 1;
            }
        }.waitUntil());
        final HumanTaskInstance humanTaskInstance = processAPI.getPendingHumanTaskInstances(getSession().getUserId(), 0, 1,
                ActivityInstanceCriterion.NAME_ASC).get(0);
        Assert.assertNotNull(humanTaskInstance);
        processAPI.assignUserTask(humanTaskInstance.getId(), getSession().getUserId());
        final String assignee = formWorkflowApi.getActivityAttributes(getSession(), humanTaskInstance.getId(), Locale.ENGLISH).get(
                ActivityAttribute.assignee.name());
        Assert.assertEquals(getInitiator().getUserName(), assignee);
        final String actor = formWorkflowApi.getActivityAttributes(getSession(), humanTaskInstance.getId(), Locale.ENGLISH).get(ActivityAttribute.actor.name());
        Assert.assertEquals("myActor", actor);
    }

    // @Test
    // public void testGetActivityAttributesRemainingTime() throws Exception {
    // ManagementAPI managementAPI = AccessorUtil.getManagementAPI();
    //
    // ProcessDefinition simpleProcess = ProcessBuilder.createProcess("simple_process", "1.0")
    // .addHuman("john")
    // .addHumanTask("task1", "john")
    // .addActivityExecutingTime(100000000L)
    // .done();
    //
    // BusinessArchive businessArchive = BusinessArchiveFactory.getBusinessArchive(simpleProcess);
    // simpleProcess = managementAPI.deploy(businessArchive);
    //
    // ProcessInstanceUUID processInstanceUUID = AccessorUtil.getRuntimeAPI().instantiateProcess(simpleProcess.getUUID());
    //
    // try {
    // Collection<TaskInstance> tasks = AccessorUtil.getQueryRuntimeAPI().getTaskList(processInstanceUUID, ActivityState.READY);
    // ActivityInstanceUUID uuid = null;
    // for (TaskInstance activityInstance : tasks) {
    // uuid = activityInstance.getUUID();
    // }
    // Assert.assertNotNull(uuid);
    //
    // IFormWorkflowAPI api = FormAPIFactory.getFormWorkflowAPI();
    // String remainingTime = (String)api.getAttributes(uuid, Locale.ENGLISH).get(ActivityAttribute.remainingTime.name());
    // Assert.assertTrue(remainingTime.contains("1day 3hours"));
    // } finally {
    // AccessorUtil.getManagementAPI().deleteProcess(simpleProcess.getUUID());
    // }
    // }

    @Test
    public void testWeCanGetPendingTaskForAUser() throws Exception {
        final ProcessAPI processAPI = Mockito.mock(ProcessAPI.class);

        formWorkflowApi.getProcessInstanceTaskAvailableForUser(processAPI, 1L, 1L);

        Mockito.verify(processAPI).searchMyAvailableHumanTasks(Mockito.eq(1L), Mockito.any(SearchOptions.class));
    }
}
