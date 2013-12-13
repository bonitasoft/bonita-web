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

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.bonitasoft.engine.api.APIAccessor;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.actor.ActorCriterion;
import org.bonitasoft.engine.bpm.actor.ActorInstance;
import org.bonitasoft.engine.bpm.actor.ActorMember;
import org.bonitasoft.engine.bpm.bar.BarResource;
import org.bonitasoft.engine.bpm.bar.BusinessArchive;
import org.bonitasoft.engine.bpm.bar.BusinessArchiveBuilder;
import org.bonitasoft.engine.bpm.data.DataInstance;
import org.bonitasoft.engine.bpm.document.Document;
import org.bonitasoft.engine.bpm.document.DocumentValue;
import org.bonitasoft.engine.bpm.flownode.ActivityInstance;
import org.bonitasoft.engine.bpm.flownode.ActivityInstanceCriterion;
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance;
import org.bonitasoft.engine.bpm.process.DesignProcessDefinition;
import org.bonitasoft.engine.bpm.process.ProcessDefinition;
import org.bonitasoft.engine.bpm.process.impl.ProcessDefinitionBuilder;
import org.bonitasoft.engine.expression.ExpressionBuilder;
import org.bonitasoft.engine.expression.ExpressionType;
import org.bonitasoft.engine.identity.User;
import org.bonitasoft.engine.search.SearchOptions;
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
        processBuilder.addDocumentDefinition("doc1").addContentFileName("filename.txt").addFile("barFilename.txt");
        processBuilder.addActor("myActor");
        processBuilder.addUserTask("Request", "myActor");
        processBuilder.addUserTask("Approval", "myActor");
        processBuilder.addTransition("Request", "Approval");

        final DesignProcessDefinition designProcessDefinition = processBuilder.done();
        final BusinessArchiveBuilder businessArchiveBuilder = new BusinessArchiveBuilder().createNewBusinessArchive();
        final byte[] content = new byte[] { 5, 0, 1, 4, 6, 5, 2, 3, 1, 5, 6, 8, 4, 6, 6, 3, 2, 4, 5 };
        final BarResource barResource = new BarResource("barFilename.txt", content);
        businessArchiveBuilder.addDocumentResource(barResource);
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
        processAPI.deleteProcessInstances(bonitaProcess.getId(), 0, 100);
        processAPI.deleteProcessDefinition(bonitaProcess.getId());
        super.tearDown();
    }

    @Test
    public void testGetTaskFieldValue() throws Exception {
        Assert.assertTrue("no pending user task instances are found", new WaitUntil(50, 1000) {

            @Override
            protected boolean check() throws Exception {
                return processAPI.getPendingHumanTaskInstances(TestFormWorkflowAPIImpl.this.getSession().getUserId(), 0, 10, null).size() >= 1;
            }
        }.waitUntil());
        final HumanTaskInstance humanTaskInstance = processAPI.getPendingHumanTaskInstances(getSession().getUserId(), 0, 1, ActivityInstanceCriterion.NAME_ASC)
                .get(0);
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
                return processAPI.getPendingHumanTaskInstances(TestFormWorkflowAPIImpl.this.getSession().getUserId(), 0, 10, null).size() >= 1;
            }
        }.waitUntil());
        HumanTaskInstance humanTaskInstance = processAPI.getPendingHumanTaskInstances(getSession().getUserId(), 0, 1, ActivityInstanceCriterion.NAME_ASC)
                .get(0);
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
                return processAPI.getPendingHumanTaskInstances(TestFormWorkflowAPIImpl.this.getSession().getUserId(), 0, 10, null).size() >= 1;
            }
        }.waitUntil());
        humanTaskInstance = processAPI.getPendingHumanTaskInstances(getSession().getUserId(), 0, 1, ActivityInstanceCriterion.NAME_ASC).get(0);
        final String activityName = humanTaskInstance.getName();
        Assert.assertNotNull(activityName);
        Assert.assertEquals("Approval", activityName);
        final DataInstance dataInstance = processAPI.getProcessDataInstance("Application", processInstanceID);
        Assert.assertEquals("Excel", dataInstance.getValue().toString());
    }

    @Test
    public void testExecuteActionsAndTerminateWithExternalDocument() throws Exception {
        final IFormWorkflowAPI api = FormAPIFactory.getFormWorkflowAPI();
        Assert.assertTrue("no pending user task instances are found", new WaitUntil(50, 1000) {

            @Override
            protected boolean check() throws Exception {
                return processAPI.getPendingHumanTaskInstances(TestFormWorkflowAPIImpl.this.getSession().getUserId(), 0, 10, null).size() >= 1;
            }
        }.waitUntil());
        HumanTaskInstance humanTaskInstance = processAPI.getPendingHumanTaskInstances(getSession().getUserId(), 0, 1, ActivityInstanceCriterion.NAME_ASC)
                .get(0);
        final Map<String, FormFieldValue> fieldValues = new HashMap<String, FormFieldValue>();
        final FormFieldValue value1 = new FormFieldValue(new DocumentValue("http://www.bonitasoft.org"), String.class.getName());
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
                return processAPI.getPendingHumanTaskInstances(TestFormWorkflowAPIImpl.this.getSession().getUserId(), 0, 10, null).size() >= 1;
            }
        }.waitUntil());
        humanTaskInstance = processAPI.getPendingHumanTaskInstances(getSession().getUserId(), 0, 1, ActivityInstanceCriterion.NAME_ASC).get(0);
        final String activityName = humanTaskInstance.getName();
        Assert.assertNotNull(activityName);
        Assert.assertEquals("Approval", activityName);
        final Document createdDocument = processAPI.getLastDocument(processInstanceID, "DocumentToCreate");
        Assert.assertEquals("http://www.bonitasoft.org", createdDocument.getUrl());
    }

    @Test
    public void testExecuteActionsAndTerminateWithInternalDocument() throws Exception {
        final IFormWorkflowAPI api = FormAPIFactory.getFormWorkflowAPI();
        Assert.assertTrue("no pending user task instances are found", new WaitUntil(50, 1000) {

            @Override
            protected boolean check() throws Exception {
                return processAPI.getPendingHumanTaskInstances(TestFormWorkflowAPIImpl.this.getSession().getUserId(), 0, 10, null).size() >= 1;
            }
        }.waitUntil());
        HumanTaskInstance humanTaskInstance = processAPI.getPendingHumanTaskInstances(getSession().getUserId(), 0, 1, ActivityInstanceCriterion.NAME_ASC)
                .get(0);

        final ExpressionBuilder expressionBuilder = new ExpressionBuilder();
        final org.bonitasoft.engine.expression.Expression documentExpression = expressionBuilder.createNewInstance("doc1").setContent("doc1")
                .setExpressionType(ExpressionType.TYPE_DOCUMENT.name()).setReturnType(org.bonitasoft.engine.bpm.document.Document.class.getName()).done();
        final Map<org.bonitasoft.engine.expression.Expression, Map<String, Serializable>> expressions = new HashMap<org.bonitasoft.engine.expression.Expression, Map<String, Serializable>>();
        expressions.put(documentExpression, new HashMap<String, Serializable>());
        final Map<String, Serializable> evaluationResult = processAPI.evaluateExpressionsOnActivityInstance(humanTaskInstance.getId(), expressions);
        Assert.assertTrue(evaluationResult.containsKey("doc1"));
        final org.bonitasoft.engine.bpm.document.Document document = (org.bonitasoft.engine.bpm.document.Document) evaluationResult.get("doc1");
        Assert.assertNotNull(document);

        final Map<String, FormFieldValue> fieldValues = new HashMap<String, FormFieldValue>();
        final File file = File.createTempFile("testDoc", "txt");
        file.deleteOnExit();
        FileUtils.writeStringToFile(file, "new content");
        final FormFieldValue value1 = new FormFieldValue(file.getAbsolutePath(), File.class.getName());
        value1.setDocument(true);
        value1.setDocumentId(processAPI.getLastDocument(processInstanceID, "doc1").getId());
        value1.setDocumentName("doc1");
        value1.setDisplayedValue(file.getName());
        fieldValues.put("fieldId1", value1);
        final List<FormAction> formActions = new ArrayList<FormAction>();
        final Expression fieldExpression = new Expression(null, "field_fieldId1", ExpressionType.TYPE_INPUT.name(), DocumentValue.class.getName(), null,
                new ArrayList<Expression>());
        formActions.add(new FormAction(ActionType.DOCUMENT_CREATE_UPDATE, "doc1", false, "=", null, fieldExpression, "submitButtonId"));
        processAPI.assignUserTask(humanTaskInstance.getId(), getSession().getUserId());
        api.executeActionsAndTerminate(getSession(), humanTaskInstance.getId(), fieldValues, formActions, Locale.ENGLISH, "submitButtonId",
                new HashMap<String, Serializable>());
        Assert.assertTrue("no pending user task instances are found", new WaitUntil(50, 1000) {

            @Override
            protected boolean check() throws Exception {
                return processAPI.getPendingHumanTaskInstances(TestFormWorkflowAPIImpl.this.getSession().getUserId(), 0, 10, null).size() >= 1;
            }
        }.waitUntil());
        humanTaskInstance = processAPI.getPendingHumanTaskInstances(getSession().getUserId(), 0, 1, ActivityInstanceCriterion.NAME_ASC).get(0);
        final String activityName = humanTaskInstance.getName();
        Assert.assertNotNull(activityName);
        Assert.assertEquals("Approval", activityName);

        final Map<String, Serializable> evaluationResult1 = processAPI.evaluateExpressionsOnActivityInstance(humanTaskInstance.getId(), expressions);
        Assert.assertTrue(evaluationResult1.containsKey("doc1"));
        final org.bonitasoft.engine.bpm.document.Document updatedDocument1 = (org.bonitasoft.engine.bpm.document.Document) evaluationResult1.get("doc1");
        Assert.assertNotNull(updatedDocument1);
        Assert.assertEquals(file.getName(), updatedDocument1.getContentFileName());

        // Apparently if you set a document to null (using an operation) all the versions of the document are lost
        // final Map<String, Serializable> evaluationResult2 = processAPI.evaluateExpressionsAtProcessInstanciation(processInstanceID, expressions);
        // Assert.assertTrue(evaluationResult2.containsKey("doc1"));
        // final org.bonitasoft.engine.bpm.document.Document initialDocument = (org.bonitasoft.engine.bpm.document.Document) evaluationResult2.get("doc1");
        // Assert.assertNotNull(initialDocument);
    }

    @Test
    public void testExecuteActionsAndTerminateWithEmptyDocument() throws Exception {
        final IFormWorkflowAPI api = FormAPIFactory.getFormWorkflowAPI();
        Assert.assertTrue("no pending user task instances are found", new WaitUntil(50, 1000) {

            @Override
            protected boolean check() throws Exception {
                return processAPI.getPendingHumanTaskInstances(TestFormWorkflowAPIImpl.this.getSession().getUserId(), 0, 10, null).size() >= 1;
            }
        }.waitUntil());
        HumanTaskInstance humanTaskInstance = processAPI.getPendingHumanTaskInstances(getSession().getUserId(), 0, 1, ActivityInstanceCriterion.NAME_ASC)
                .get(0);

        final ExpressionBuilder expressionBuilder = new ExpressionBuilder();
        final org.bonitasoft.engine.expression.Expression documentExpression = expressionBuilder.createNewInstance("doc1").setContent("doc1")
                .setExpressionType(ExpressionType.TYPE_DOCUMENT.name()).setReturnType(org.bonitasoft.engine.bpm.document.Document.class.getName()).done();
        final Map<org.bonitasoft.engine.expression.Expression, Map<String, Serializable>> expressions = new HashMap<org.bonitasoft.engine.expression.Expression, Map<String, Serializable>>();
        expressions.put(documentExpression, new HashMap<String, Serializable>());
        final Map<String, Serializable> evaluationResult = processAPI.evaluateExpressionsOnActivityInstance(humanTaskInstance.getId(), expressions);
        Assert.assertTrue(evaluationResult.containsKey("doc1"));
        final org.bonitasoft.engine.bpm.document.Document document = (org.bonitasoft.engine.bpm.document.Document) evaluationResult.get("doc1");
        Assert.assertNotNull(document);

        final Map<String, FormFieldValue> fieldValues = new HashMap<String, FormFieldValue>();
        final FormFieldValue value1 = new FormFieldValue(null, File.class.getName());
        value1.setDocument(true);
        value1.setDocumentId(processAPI.getLastDocument(processInstanceID, "doc1").getId());
        value1.setDocumentName("doc1");
        fieldValues.put("fieldId1", value1);
        final List<FormAction> formActions = new ArrayList<FormAction>();
        final Expression fieldExpression = new Expression(null, "field_fieldId1", ExpressionType.TYPE_INPUT.name(), DocumentValue.class.getName(), null,
                new ArrayList<Expression>());
        formActions.add(new FormAction(ActionType.DOCUMENT_CREATE_UPDATE, "doc1", false, "=", null, fieldExpression, "submitButtonId"));
        processAPI.assignUserTask(humanTaskInstance.getId(), getSession().getUserId());
        api.executeActionsAndTerminate(getSession(), humanTaskInstance.getId(), fieldValues, formActions, Locale.ENGLISH, "submitButtonId",
                new HashMap<String, Serializable>());
        Assert.assertTrue("no pending user task instances are found", new WaitUntil(50, 1000) {

            @Override
            protected boolean check() throws Exception {
                return processAPI.getPendingHumanTaskInstances(TestFormWorkflowAPIImpl.this.getSession().getUserId(), 0, 10, null).size() >= 1;
            }
        }.waitUntil());
        humanTaskInstance = processAPI.getPendingHumanTaskInstances(getSession().getUserId(), 0, 1, ActivityInstanceCriterion.NAME_ASC).get(0);
        final String activityName = humanTaskInstance.getName();
        Assert.assertNotNull(activityName);
        Assert.assertEquals("Approval", activityName);

        final Map<String, Serializable> evaluationResult1 = processAPI.evaluateExpressionsOnActivityInstance(humanTaskInstance.getId(), expressions);
        Assert.assertTrue(evaluationResult1.containsKey("doc1"));
        final org.bonitasoft.engine.bpm.document.Document updatedDocument1 = (org.bonitasoft.engine.bpm.document.Document) evaluationResult1.get("doc1");
        Assert.assertNull(updatedDocument1);

        // Apparently if you set a document to null (using an operation) all the versions of the document are lost
        // final Map<String, Serializable> evaluationResult2 = processAPI.evaluateExpressionsAtProcessInstanciation(processInstanceID, expressions);
        // Assert.assertTrue(evaluationResult2.containsKey("doc1"));
        // final org.bonitasoft.engine.bpm.document.Document initialDocument = (org.bonitasoft.engine.bpm.document.Document) evaluationResult2.get("doc1");
        // Assert.assertNotNull(initialDocument);
    }

    @Test
    public void testExecuteActionsAndTerminateWithAPIAccessor() throws Exception {
        final IFormWorkflowAPI api = FormAPIFactory.getFormWorkflowAPI();
        Assert.assertTrue("no pending user task instances are found", new WaitUntil(50, 1000) {

            @Override
            protected boolean check() throws Exception {
                return processAPI.getPendingHumanTaskInstances(getSession().getUserId(), 0, 10, null).size() >= 1;
            }
        }.waitUntil());
        HumanTaskInstance humanTaskInstance = processAPI.getPendingHumanTaskInstances(getSession().getUserId(), 0, 1, ActivityInstanceCriterion.NAME_ASC)
                .get(0);
        final Map<String, FormFieldValue> fieldValues = new HashMap<String, FormFieldValue>();
        final List<FormAction> formActions = new ArrayList<FormAction>();
        final Expression apiAccessorExpression = new Expression("apiAccessor", "apiAccessor", ExpressionType.TYPE_ENGINE_CONSTANT.name(),
                APIAccessor.class.getName(), null, new ArrayList<Expression>());
        final Expression processInstanceIdExpression = new Expression("processInstanceId", "processInstanceId", ExpressionType.TYPE_ENGINE_CONSTANT.name(),
                Long.class.getName(), null, new ArrayList<Expression>());
        final ArrayList<Expression> dependencyExpressions = new ArrayList<Expression>();
        dependencyExpressions.add(apiAccessorExpression);
        dependencyExpressions.add(processInstanceIdExpression);
        final Expression fieldExpression = new Expression(null,
                "apiAccessor.getProcessAPI().getProcessDataInstance(\"Application\", processInstanceId).getValue().toString()",
                ExpressionType.TYPE_READ_ONLY_SCRIPT.name(), String.class.getName(), "GROOVY", dependencyExpressions);
        formActions.add(new FormAction(ActionType.ASSIGNMENT, "Application", false, "=", null, fieldExpression, "submitButtonId"));
        processAPI.assignUserTask(humanTaskInstance.getId(), getSession().getUserId());
        api.executeActionsAndTerminate(getSession(), humanTaskInstance.getId(), fieldValues, formActions, Locale.ENGLISH, "submitButtonId",
                new HashMap<String, Serializable>());
        Assert.assertTrue("no pending user task instances are found", new WaitUntil(50, 1000) {

            @Override
            protected boolean check() throws Exception {
                return processAPI.getPendingHumanTaskInstances(TestFormWorkflowAPIImpl.this.getSession().getUserId(), 0, 10, null).size() >= 1;
            }
        }.waitUntil());
        humanTaskInstance = processAPI.getPendingHumanTaskInstances(getSession().getUserId(), 0, 1, ActivityInstanceCriterion.NAME_ASC).get(0);
        final String activityName = humanTaskInstance.getName();
        Assert.assertNotNull(activityName);
        Assert.assertEquals("Approval", activityName);
        final Serializable variableValue = processAPI.getProcessDataInstance("Application", processInstanceID).getValue();
        Assert.assertEquals("Word", variableValue.toString());
    }

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

    @Test
    public void testGetRelatedProcessesNextTask() throws Exception {
        Assert.assertTrue("no pending user task instances are found", new WaitUntil(50, 1000) {

            @Override
            protected boolean check() throws Exception {
                return processAPI.getPendingHumanTaskInstances(TestFormWorkflowAPIImpl.this.getSession().getUserId(), 0, 10, null).size() >= 1;
            }
        }.waitUntil());
        HumanTaskInstance humanTaskInstance = processAPI.getPendingHumanTaskInstances(getSession().getUserId(), 0, 1, ActivityInstanceCriterion.NAME_ASC)
                .get(0);
        Assert.assertNotNull(humanTaskInstance);
        processAPI.assignUserTask(humanTaskInstance.getId(), getSession().getUserId());
        processAPI.executeFlowNode(humanTaskInstance.getId());
        Assert.assertTrue("no pending user task instances are found", new WaitUntil(50, 1000) {

            @Override
            protected boolean check() throws Exception {
                return processAPI.getPendingHumanTaskInstances(TestFormWorkflowAPIImpl.this.getSession().getUserId(), 0, 10, null).size() >= 1;
            }
        }.waitUntil());
        humanTaskInstance = processAPI.getPendingHumanTaskInstances(getSession().getUserId(), 0, 1, ActivityInstanceCriterion.NAME_ASC).get(0);
        processAPI.assignUserTask(humanTaskInstance.getId(), getSession().getUserId());
        final long activityInstanceID = formWorkflowApi.getRelatedProcessesNextTask(getSession(), processInstanceID);
        Assert.assertTrue(activityInstanceID >= 0);
        final ActivityInstance activityInstance = processAPI.getActivityInstance(activityInstanceID);
        Assert.assertEquals("Approval", activityInstance.getName());
    }

    @Test
    public void testGetRelatedProcessesNextTaskAfterInstantiation() throws Exception {
        Assert.assertTrue("no pending user task instances are found", new WaitUntil(50, 1000) {

            @Override
            protected boolean check() throws Exception {
                return processAPI.getPendingHumanTaskInstances(TestFormWorkflowAPIImpl.this.getSession().getUserId(), 0, 10, null).size() >= 1;
            }
        }.waitUntil());
        final HumanTaskInstance humanTaskInstance = processAPI.getPendingHumanTaskInstances(getSession().getUserId(), 0, 1, ActivityInstanceCriterion.NAME_ASC)
                .get(0);
        Assert.assertNotNull(humanTaskInstance);
        processAPI.assignUserTask(humanTaskInstance.getId(), getSession().getUserId());
        final long activityInstanceID = formWorkflowApi.getRelatedProcessesNextTask(getSession(), processInstanceID);
        Assert.assertTrue(activityInstanceID > 0);
        final ActivityInstance activityInstance = processAPI.getActivityInstance(activityInstanceID);
        Assert.assertEquals("Request", activityInstance.getName());
    }

    @Test
    public void testGetAnyTodoListTaskForProcessDefinition() throws Exception {
        Assert.assertTrue("no pending user task instances are found", new WaitUntil(50, 1000) {

            @Override
            protected boolean check() throws Exception {
                return processAPI.getPendingHumanTaskInstances(TestFormWorkflowAPIImpl.this.getSession().getUserId(), 0, 10, null).size() >= 1;
            }
        }.waitUntil());
        final HumanTaskInstance humanTaskInstance = processAPI.getPendingHumanTaskInstances(getSession().getUserId(), 0, 1, ActivityInstanceCriterion.NAME_ASC)
                .get(0);
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
                return processAPI.getPendingHumanTaskInstances(TestFormWorkflowAPIImpl.this.getSession().getUserId(), 0, 10, null).size() >= 1;
            }
        }.waitUntil());
        final HumanTaskInstance humanTaskInstance = processAPI.getPendingHumanTaskInstances(getSession().getUserId(), 0, 1, ActivityInstanceCriterion.NAME_ASC)
                .get(0);
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
    public void getProcessDefinitionDate_return_process_deployment_date() throws Exception {
        final TestProcess process = TestProcessFactory.getDefaultHumanTaskProcess();
        final Date expectedDate = process.getProcessDeploymentInfo().getDeploymentDate();

        final Date processDefinitionDate = formWorkflowApi.getProcessDefinitionDate(getSession(), process.getId());

        assertThat(processDefinitionDate, equalTo(expectedDate));
    }

    @Test
    public void isTaskReady_return_true_if_task_is_not_performed() throws Exception {
        final TestHumanTask task = TestProcessFactory.getDefaultHumanTaskProcess().addActor(getInitiator()).startCase().getNextHumanTask();
        task.assignTo(getInitiator());

        final boolean isTaskReady = formWorkflowApi.isTaskReady(getSession(), task.getId());

        assertThat(isTaskReady, is(true));
    }

    @Test
    public void isTaskReady_return_false_if_task_is_performed() throws Exception {
        final TestHumanTask task = TestProcessFactory.getDefaultHumanTaskProcess().addActor(getInitiator()).startCase().getNextHumanTask();
        task.assignTo(getInitiator()).execute();

        final boolean isTaskReady = formWorkflowApi.isTaskReady(getSession(), task.getId());

        assertThat(isTaskReady, is(false));
    }

    @Test
    public void getTaskEditState_return_EDITABLE_if_task_is_assigned() throws Exception {
        final TestHumanTask task = TestProcessFactory.getDefaultHumanTaskProcess().addActor(getInitiator()).startCase().getNextHumanTask();
        task.assignTo(getInitiator());

        final ActivityEditState state = formWorkflowApi.getTaskEditState(getSession(), task.getId());

        assertThat(state, is(ActivityEditState.EDITABLE));
    }

    @Test
    public void getTaskEditState_return_NOT_EDITABLE_if_task_is_performed_using_original_task_ID() throws Exception {
        final TestHumanTask task = TestProcessFactory.getDefaultHumanTaskProcess().addActor(getInitiator()).startCase().getNextHumanTask();
        task.assignTo(getInitiator()).execute();

        final ActivityEditState state = formWorkflowApi.getTaskEditState(getSession(), task.getId());

        assertThat(state, is(ActivityEditState.NOT_EDITABLE));
    }

    @Test
    public void testGetActivityAttributesActor() throws Exception {
        Assert.assertTrue("no pending user task instances are found", new WaitUntil(50, 1000) {

            @Override
            protected boolean check() throws Exception {
                return processAPI.getPendingHumanTaskInstances(TestFormWorkflowAPIImpl.this.getSession().getUserId(), 0, 10, null).size() >= 1;
            }
        }.waitUntil());
        final HumanTaskInstance humanTaskInstance = processAPI.getPendingHumanTaskInstances(getSession().getUserId(), 0, 1, ActivityInstanceCriterion.NAME_ASC)
                .get(0);
        Assert.assertNotNull(humanTaskInstance);
        processAPI.assignUserTask(humanTaskInstance.getId(), getSession().getUserId());
        final String assignee = formWorkflowApi.getActivityAttributes(getSession(), humanTaskInstance.getId(), Locale.ENGLISH).get(
                ActivityAttribute.assignee.name());
        Assert.assertEquals(getInitiator().getUserName(), assignee);
        final String actor = formWorkflowApi.getActivityAttributes(getSession(), humanTaskInstance.getId(), Locale.ENGLISH).get(ActivityAttribute.actor.name());
        Assert.assertEquals("myActor", actor);
    }

    @Test
    public void testWeCanGetPendingTaskForAUser() throws Exception {
        final ProcessAPI processAPI = Mockito.mock(ProcessAPI.class);

        formWorkflowApi.getProcessInstanceTaskAvailableForUser(processAPI, 1L, 1L);

        Mockito.verify(processAPI).searchMyAvailableHumanTasks(Mockito.eq(1L), Mockito.any(SearchOptions.class));
    }
}
