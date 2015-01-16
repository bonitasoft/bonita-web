/**
 * Copyright (C) 2011 BonitaSoft S.A.
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
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.actor.ActorCriterion;
import org.bonitasoft.engine.bpm.actor.ActorInstance;
import org.bonitasoft.engine.bpm.bar.BarResource;
import org.bonitasoft.engine.bpm.bar.BusinessArchive;
import org.bonitasoft.engine.bpm.bar.BusinessArchiveBuilder;
import org.bonitasoft.engine.bpm.bar.InvalidBusinessArchiveFormatException;
import org.bonitasoft.engine.bpm.document.DocumentsSearchDescriptor;
import org.bonitasoft.engine.bpm.flownode.ActivityInstanceCriterion;
import org.bonitasoft.engine.bpm.flownode.FlowNodeExecutionException;
import org.bonitasoft.engine.bpm.process.ArchivedProcessInstancesSearchDescriptor;
import org.bonitasoft.engine.bpm.process.DesignProcessDefinition;
import org.bonitasoft.engine.bpm.process.InvalidProcessDefinitionException;
import org.bonitasoft.engine.bpm.process.ProcessActivationException;
import org.bonitasoft.engine.bpm.process.ProcessDefinition;
import org.bonitasoft.engine.bpm.process.ProcessDefinitionNotFoundException;
import org.bonitasoft.engine.bpm.process.ProcessDeployException;
import org.bonitasoft.engine.bpm.process.ProcessEnablementException;
import org.bonitasoft.engine.bpm.process.impl.ProcessDefinitionBuilder;
import org.bonitasoft.engine.exception.AlreadyExistsException;
import org.bonitasoft.engine.exception.CreationException;
import org.bonitasoft.engine.exception.DeletionException;
import org.bonitasoft.engine.exception.UpdateException;
import org.bonitasoft.engine.expression.ExpressionBuilder;
import org.bonitasoft.engine.expression.ExpressionType;
import org.bonitasoft.engine.expression.InvalidExpressionException;
import org.bonitasoft.engine.search.SearchFilterOperation;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.engine.search.impl.SearchFilter;
import org.bonitasoft.forms.client.model.Expression;
import org.bonitasoft.forms.client.model.FormFieldValue;
import org.bonitasoft.forms.client.model.FormURLComponents;
import org.bonitasoft.forms.client.model.FormWidget;
import org.bonitasoft.forms.server.FormsTestCase;
import org.bonitasoft.forms.server.WaitUntil;
import org.bonitasoft.forms.server.accessor.IApplicationFormDefAccessor;
import org.bonitasoft.forms.server.api.impl.FormWorkflowAPIImpl;
import org.bonitasoft.forms.server.exception.NoCredentialsInSessionException;
import org.bonitasoft.forms.server.provider.FormServiceProvider;
import org.bonitasoft.forms.server.provider.impl.util.FormServiceProviderFactory;
import org.bonitasoft.forms.server.provider.impl.util.FormServiceProviderUtil;
import org.bonitasoft.web.rest.model.user.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;

/**
 * @author QiXiang Zhang, Yongtao Guo
 */
public class FormServiceProviderImplIT extends FormsTestCase {

    protected ProcessDefinition processDefinition;

    protected Expression expression = null;

    protected ProcessAPI processAPI;

    @Override
    public void testSetUp() throws Exception {
        super.testSetUp();
        final ProcessDefinitionBuilder processBuilder = new ProcessDefinitionBuilder().createNewInstance("firstProcess", "1.0");
        final ExpressionBuilder expressionBuilder = new ExpressionBuilder();
        processBuilder.addData(
                "application",
                String.class.getName(),
                expressionBuilder.createNewInstance("word").setContent("Word").setExpressionType(ExpressionType.TYPE_CONSTANT)
                        .setReturnType(String.class.getName()).done());
        processBuilder.addDocumentDefinition("doc1").addUrl("www.bonitasoft.org");
        processBuilder.addDocumentDefinition("doc2").addContentFileName("filename.txt").addFile("barFilename.txt");
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
        processDefinition = processAPI.deploy(businessArchive);

        final org.bonitasoft.engine.identity.User user = getInitiator().getUser();
        final ActorInstance processActor = processAPI.getActors(processDefinition.getId(), 0, 1, ActorCriterion.NAME_ASC).get(0);
        processAPI.addUserToActor(processActor.getId(), user.getId());

        processAPI.enableProcess(processDefinition.getId());

        processAPI = TenantAPIAccessor.getProcessAPI(getSession());

        final List<Expression> dependencies = new ArrayList<Expression>();
        dependencies.add(new Expression(null, "field_application", ExpressionType.TYPE_INPUT.name(), String.class.getName(), null, null));
        expression = new Expression(null, "\"application:\" + field_application", ExpressionType.TYPE_READ_ONLY_SCRIPT.name(), String.class.getName(),
                "GROOVY", dependencies);
    }

    @Override
    @After
    public void tearDown() throws Exception {

        processAPI.disableProcess(processDefinition.getId());
        processAPI.deleteProcess(processDefinition.getId());
        super.tearDown();
    }

    @Test
    public void testGetFormDefinitionDocument() throws Exception {

        final long processInstanceId = processAPI.startProcess(processDefinition.getId()).getId();

        final FormServiceProvider formServiceProvider = FormServiceProviderFactory.getFormServiceProvider(getSession().getTenantId());
        final Map<String, Object> urlContext = new HashMap<String, Object>();
        urlContext.put(FormServiceProviderUtil.INSTANCE_UUID, processInstanceId);
        urlContext.put(FormServiceProviderUtil.LOCALE, Locale.ENGLISH);
        final Map<String, Object> context = new HashMap<String, Object>();
        context.put(FormServiceProviderUtil.URL_CONTEXT, urlContext);
        context.put(FormServiceProviderUtil.LOCALE, Locale.ENGLISH);
        context.put(FormServiceProviderUtil.API_SESSION, getSession());
        final Document document = formServiceProvider.getFormDefinitionDocument(context);
        Assert.assertNotNull(document);
    }

    @Test(expected = NoCredentialsInSessionException.class)
    public void testIsAllowed() throws Exception {

        final long processInstanceId = processAPI.startProcess(processDefinition.getId()).getId();

        final FormServiceProvider formServiceProvider = FormServiceProviderFactory.getFormServiceProvider(getSession().getTenantId());
        final Map<String, Object> urlContext = new HashMap<String, Object>();
        urlContext.put(FormServiceProviderUtil.INSTANCE_UUID, processInstanceId);
        final User user = new User(getSession().getUserName(), Locale.ENGLISH.toString());
        final Map<String, Object> context = new HashMap<String, Object>();
        context.put(FormServiceProviderUtil.USER, user);
        context.put(FormServiceProviderUtil.URL_CONTEXT, urlContext);
        context.put(FormServiceProviderUtil.LOCALE, Locale.ENGLISH);
        context.put(FormServiceProviderUtil.API_SESSION, getSession());
        // TODO re-establish this once it will be possible to give some access rights to the overview form
        // boolean isAllowed1 = formServiceProvider.isAllowed(processDefinitionUUIDStr + FormServiceProviderUtil.FORM_ID_SEPARATOR
        // + FormServiceProviderUtil.RECAP_FORM_TYPE, FormServiceProviderUtil.INSTANCE_UUID + "#" + processDefinitionUUIDStr, "6.0", "6.0", context, true);
        // Assert.assertEquals(true, isAllowed1);
        context.remove(FormServiceProviderUtil.USER);
        // expected = NoCredentialsInSessionException.class
        formServiceProvider.isAllowed(processDefinition.getName() + "--" + processDefinition.getVersion() + FormServiceProviderUtil.FORM_ID_SEPARATOR
                + FormServiceProviderUtil.RECAP_FORM_TYPE,
                FormServiceProviderUtil.PROCESS_UUID + "#" + processDefinition.getId(), "6.0", "6.0", context, true);
    }

    @Test
    public void testResolveExpression() throws Exception {

        final long processInstanceId = processAPI.startProcess(processDefinition.getId()).getId();

        final FormServiceProvider formServiceProvider = FormServiceProviderFactory.getFormServiceProvider(getSession().getTenantId());
        final Map<String, Object> urlContext = new HashMap<String, Object>();
        urlContext.put(FormServiceProviderUtil.INSTANCE_UUID, processInstanceId);
        urlContext.put(FormServiceProviderUtil.LOCALE, Locale.ENGLISH);
        urlContext.put(FormServiceProviderUtil.IS_CURRENT_VALUE, true);
        final Map<String, Object> context = new HashMap<String, Object>();
        context.put(FormServiceProviderUtil.URL_CONTEXT, urlContext);
        context.put(FormServiceProviderUtil.LOCALE, Locale.ENGLISH);
        context.put(FormServiceProviderUtil.API_SESSION, getSession());
        final Map<String, FormFieldValue> fieldValues = new HashMap<String, FormFieldValue>();
        fieldValues.put("application", new FormFieldValue("Excel", String.class.getName()));
        context.put(FormServiceProviderUtil.FIELD_VALUES, fieldValues);
        final Object result = formServiceProvider.resolveExpression(expression, context);
        Assert.assertEquals("application:Excel", result);
    }

    @Test
    public void testResolveExpressions() throws Exception {

        final long processInstanceId = processAPI.startProcess(processDefinition.getId()).getId();

        final FormServiceProvider formServiceProvider = FormServiceProviderFactory.getFormServiceProvider(getSession().getTenantId());
        final Map<String, Object> urlContext = new HashMap<String, Object>();
        urlContext.put(FormServiceProviderUtil.INSTANCE_UUID, processInstanceId);
        urlContext.put(FormServiceProviderUtil.LOCALE, Locale.ENGLISH);
        urlContext.put(FormServiceProviderUtil.IS_CURRENT_VALUE, true);
        final Map<String, Object> context = new HashMap<String, Object>();
        context.put(FormServiceProviderUtil.URL_CONTEXT, urlContext);
        context.put(FormServiceProviderUtil.LOCALE, Locale.ENGLISH);
        context.put(FormServiceProviderUtil.API_SESSION, getSession());
        final Map<String, FormFieldValue> fieldValues = new HashMap<String, FormFieldValue>();
        fieldValues.put("application", new FormFieldValue("Excel", String.class.getName()));
        context.put(FormServiceProviderUtil.FIELD_VALUES, fieldValues);
        final List<Expression> expressions = new ArrayList<Expression>();
        expression.setName("1");
        expressions.add(expression);
        expressions.add(new Expression("2", "good", "TYPE_CONSTANT", String.class.getName(), null, null));
        final Map<String, Serializable> result = formServiceProvider.resolveExpressions(expressions, context);
        Assert.assertEquals("application:Excel", result.get("1"));
        Assert.assertEquals("good", result.get("2"));
    }

    @Test
    public void testGetDeployementDate() throws Exception {
        final FormServiceProvider formServiceProvider = FormServiceProviderFactory.getFormServiceProvider(getSession().getTenantId());
        final Map<String, Object> urlContext = new HashMap<String, Object>();
        urlContext.put(FormServiceProviderUtil.FORM_ID, processDefinition.getName() + FormWorkflowAPIImpl.UUID_SEPARATOR + processDefinition.getVersion()
                + FormServiceProviderUtil.FORM_ID_SEPARATOR
                + FormServiceProviderUtil.RECAP_FORM_TYPE);
        final Map<String, Object> context = new HashMap<String, Object>();
        context.put(FormServiceProviderUtil.URL_CONTEXT, urlContext);
        context.put(FormServiceProviderUtil.LOCALE, Locale.ENGLISH);
        context.put(FormServiceProviderUtil.API_SESSION, getSession());
        final Date date = formServiceProvider.getDeployementDate(context);
        Assert.assertNotNull(date);
    }

    @Test
    public void testGetAttributesToInsert() throws Exception {

        processAPI.startProcess(processDefinition.getId()).getId();

        final FormServiceProvider formServiceProvider = FormServiceProviderFactory.getFormServiceProvider(getSession().getTenantId());
        final Map<String, Object> urlContext = new HashMap<String, Object>();
        final long activityInstanceId = waitForPendingTask();
        urlContext.put(FormServiceProviderUtil.TASK_UUID, activityInstanceId);
        urlContext.put(FormServiceProviderUtil.LOCALE, Locale.ENGLISH);
        final Map<String, Object> context = new HashMap<String, Object>();
        context.put(FormServiceProviderUtil.URL_CONTEXT, urlContext);
        context.put(FormServiceProviderUtil.LOCALE, Locale.ENGLISH);
        context.put(FormServiceProviderUtil.API_SESSION, getSession());
        final Map<String, String> attributes = formServiceProvider.getAttributesToInsert(context);
        Assert.assertNotNull(attributes);
    }

    @Test
    public void testSkipForm() throws Exception {

        processAPI.startProcess(processDefinition.getId()).getId();

        final FormServiceProvider formServiceProvider = FormServiceProviderFactory.getFormServiceProvider(getSession().getTenantId());
        final Map<String, Object> urlContext = new HashMap<String, Object>();
        final long activityInstanceId = waitForPendingTask();
        urlContext.put(FormServiceProviderUtil.TASK_UUID, activityInstanceId);
        final Map<String, Object> context = new HashMap<String, Object>();
        context.put(FormServiceProviderUtil.URL_CONTEXT, urlContext);
        context.put(FormServiceProviderUtil.LOCALE, Locale.ENGLISH);
        context.put(FormServiceProviderUtil.API_SESSION, getSession());
        processAPI.assignUserTask(activityInstanceId, getSession().getUserId());
        final Map<String, Object> result = formServiceProvider.skipForm("myProcess--1.0--task1$entry", context);
        Assert.assertNotNull(result);
    }

    @Test
    public void testGetApplicationFormDefinitionFromXML() throws Exception {

        processAPI.startProcess(processDefinition.getId()).getId();

        final FormServiceProvider formServiceProvider = FormServiceProviderFactory.getFormServiceProvider(getSession().getTenantId());
        final Map<String, Object> urlContext = new HashMap<String, Object>();
        final long activityInstanceId = waitForPendingTask();
        final String formId = "firstProcess--1.0--Request$entry";
        urlContext.put(FormServiceProviderUtil.FORM_ID, formId);
        urlContext.put(FormServiceProviderUtil.TASK_UUID, activityInstanceId);
        final Map<String, Object> context = new HashMap<String, Object>();
        context.put(FormServiceProviderUtil.URL_CONTEXT, urlContext);
        context.put(FormServiceProviderUtil.LOCALE, Locale.ENGLISH);
        context.put(FormServiceProviderUtil.API_SESSION, getSession());
        final InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("forms.xml");
        final DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        final Document document = builder.parse(inputStream);
        inputStream.close();
        final Date deployedDate = processAPI.getProcessDeploymentInfo(processDefinition.getId()).getDeploymentDate();
        context.put(FormServiceProviderUtil.APPLICATION_DEPLOYMENT_DATE, deployedDate);
        context.put(FormServiceProviderUtil.IS_EDIT_MODE, true);
        final IApplicationFormDefAccessor applicationFormDefAccessor = formServiceProvider.getApplicationFormDefinition(formId,
                document,
                context);
        Assert.assertNull("the first page expression should be null because the entry form is empty in the forms.xml",
                applicationFormDefAccessor.getFirstPageExpression());
    }

    @Test
    public void testGetApplicationFormDefinitionFromEngine() throws Exception {

        processAPI.startProcess(processDefinition.getId()).getId();

        long activityInstanceId = waitForPendingTask();
        processAPI.assignUserTask(activityInstanceId, getSession().getUserId());
        processAPI.executeFlowNode(activityInstanceId);
        Assert.assertTrue("no pending user task instances are found", new WaitUntil(50, 1000) {

            @Override
            protected boolean check() throws Exception {
                return processAPI.getPendingHumanTaskInstances(FormServiceProviderImplIT.this.getSession().getUserId(), 0, 10,
                        null).size() >= 1;
            }
        }.waitUntil());
        activityInstanceId = processAPI.getPendingHumanTaskInstances(getSession().getUserId(), 0, 1, ActivityInstanceCriterion.NAME_ASC).get(0).getId();

        final FormServiceProvider formServiceProvider = FormServiceProviderFactory.getFormServiceProvider(getSession().getTenantId());
        final Map<String, Object> urlContext = new HashMap<String, Object>();
        final String formId = "firstProcess--1.0--Approval$entry";
        urlContext.put(FormServiceProviderUtil.FORM_ID, formId);
        urlContext.put(FormServiceProviderUtil.TASK_UUID, activityInstanceId);
        final Map<String, Object> context = new HashMap<String, Object>();
        context.put(FormServiceProviderUtil.URL_CONTEXT, urlContext);
        context.put(FormServiceProviderUtil.LOCALE, Locale.ENGLISH);
        context.put(FormServiceProviderUtil.API_SESSION, getSession());
        final InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("forms.xml");
        final DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        final Document document = builder.parse(inputStream);
        inputStream.close();
        final Date deployedDate = processAPI.getProcessDeploymentInfo(processDefinition.getId()).getDeploymentDate();
        context.put(FormServiceProviderUtil.APPLICATION_DEPLOYMENT_DATE, deployedDate);
        context.put(FormServiceProviderUtil.IS_EDIT_MODE, true);
        final IApplicationFormDefAccessor applicationFormDefAccessor = formServiceProvider.getApplicationFormDefinition(formId,
                document,
                context);
        final Expression firstPage = applicationFormDefAccessor.getFirstPageExpression();
        Assert.assertNotNull(
                "the first page expression should not be null because the entry form is not in the forms.xml, and so it should be generated from the engine",
                firstPage);
        final List<FormWidget> widgets = applicationFormDefAccessor.getPageWidgets(firstPage.getContent());
        Assert.assertEquals("there is only one data in the process so there should be 2 widgets (one for the data and one for the submit button)", 2,
                widgets.size());
        Assert.assertEquals("application", widgets.get(0).getInitialValueExpression().getContent());
    }

    @Test
    public void testGetAttachmentFormFieldValue() throws Exception {

        processAPI.startProcess(processDefinition.getId()).getId();

        final FormServiceProvider formServiceProvider = FormServiceProviderFactory.getFormServiceProvider(getSession().getTenantId());
        final Map<String, Object> urlContext = new HashMap<String, Object>();
        final long activityInstanceId = waitForPendingTask();
        urlContext.put(FormServiceProviderUtil.TASK_UUID, activityInstanceId);
        final Map<String, Object> context = new HashMap<String, Object>();
        context.put(FormServiceProviderUtil.URL_CONTEXT, urlContext);
        context.put(FormServiceProviderUtil.LOCALE, Locale.ENGLISH);
        context.put(FormServiceProviderUtil.API_SESSION, getSession());
        final FormFieldValue urlFieldValue = formServiceProvider.getAttachmentFormFieldValue("doc1", context);
        Assert.assertEquals("the value type for a URL document should be a String", String.class.getName(), urlFieldValue.getValueType());
        Assert.assertEquals("the URL is not right", "www.bonitasoft.org", urlFieldValue.getValue());
        Assert.assertNotSame(0, urlFieldValue.getDocumentId());

        final FormFieldValue fileFieldValue = formServiceProvider.getAttachmentFormFieldValue("doc2", context);
        Assert.assertEquals("the value type for a File document should be a File", File.class.getName(), fileFieldValue.getValueType());
        Assert.assertEquals("the filename is not right", "filename.txt", fileFieldValue.getValue());
        Assert.assertNotSame(0, fileFieldValue.getDocumentId());
    }

    @Test
    public void testGetArchivedAttachmentFormFieldValue() throws Exception {

        final long processInstanceId = processAPI.startProcess(processDefinition.getId()).getId();

        final FormServiceProvider formServiceProvider = FormServiceProviderFactory.getFormServiceProvider(getSession().getTenantId());
        final Map<String, Object> urlContext = new HashMap<String, Object>();
        terminateProcessInstance(processInstanceId);
        urlContext.put(FormServiceProviderUtil.INSTANCE_UUID, processInstanceId);
        urlContext.put(FormServiceProviderUtil.RECAP_FORM_TYPE, Boolean.TRUE.toString());
        final Map<String, Object> context = new HashMap<String, Object>();
        context.put(FormServiceProviderUtil.URL_CONTEXT, urlContext);
        context.put(FormServiceProviderUtil.LOCALE, Locale.ENGLISH);
        context.put(FormServiceProviderUtil.API_SESSION, getSession());
        final FormFieldValue urlFieldValue = formServiceProvider.getAttachmentFormFieldValue("doc1", context);
        Assert.assertEquals("the value type for a URL document should be a String", String.class.getName(), urlFieldValue.getValueType());
        Assert.assertEquals("the URL is not right", "www.bonitasoft.org", urlFieldValue.getValue());
        Assert.assertNotSame(0, urlFieldValue.getDocumentId());

        final FormFieldValue fileFieldValue = formServiceProvider.getAttachmentFormFieldValue("doc2", context);
        Assert.assertEquals("the value type for a File document should be a File", File.class.getName(), fileFieldValue.getValueType());
        Assert.assertEquals("the filename is not right", "filename.txt", fileFieldValue.getValue());
        Assert.assertNotSame(0, fileFieldValue.getDocumentId());
    }

    @Test
    public void testGetAttachmentFormFieldValueFromDocument() throws Exception {

        final long processInstanceId = processAPI.startProcess(processDefinition.getId()).getId();

        final FormServiceProvider formServiceProvider = FormServiceProviderFactory.getFormServiceProvider(getSession().getTenantId());
        final Map<String, Object> urlContext = new HashMap<String, Object>();
        final long activityInstanceId = waitForPendingTask();
        urlContext.put(FormServiceProviderUtil.TASK_UUID, activityInstanceId);
        final Map<String, Object> context = new HashMap<String, Object>();
        context.put(FormServiceProviderUtil.URL_CONTEXT, urlContext);
        context.put(FormServiceProviderUtil.LOCALE, Locale.ENGLISH);
        context.put(FormServiceProviderUtil.API_SESSION, getSession());

        final SearchOptionsBuilder searchOptionsBuilder = new SearchOptionsBuilder(0, 2);
        final List<SearchFilter> filters = new ArrayList<SearchFilter>();
        filters.add(new SearchFilter(DocumentsSearchDescriptor.PROCESSINSTANCE_ID, SearchFilterOperation.EQUALS, processInstanceId));
        searchOptionsBuilder.setFilters(filters);
        final SearchResult<org.bonitasoft.engine.bpm.document.Document> result = processAPI.searchDocuments(searchOptionsBuilder.done());
        final List<org.bonitasoft.engine.bpm.document.Document> documents = result.getResult();
        Assert.assertEquals(2, documents.size());
        for (final org.bonitasoft.engine.bpm.document.Document document : documents) {
            if (document.getName().equals("doc1")) {
                final FormFieldValue urlFieldValue = formServiceProvider.getAttachmentFormFieldValue(document, context);
                Assert.assertEquals("the value type for a URL document should be a String", String.class.getName(), urlFieldValue.getValueType());
                Assert.assertEquals("the URL is not right", "www.bonitasoft.org", urlFieldValue.getValue());
                Assert.assertNotSame(0, urlFieldValue.getDocumentId());
            } else if (document.getName().equals("doc2")) {
                final FormFieldValue fileFieldValue = formServiceProvider.getAttachmentFormFieldValue(document, context);
                Assert.assertEquals("the value type for a File document should be a File", File.class.getName(), fileFieldValue.getValueType());
                Assert.assertEquals("the filename is not right", "filename.txt", fileFieldValue.getValue());
                Assert.assertNotSame(0, fileFieldValue.getDocumentId());
            } else {
                Assert.fail("shouldn't happen.");
            }
        }
    }

    @Test
    public void testGetNextFormURLParametersFromSubprocessTask() throws Exception {

        final org.bonitasoft.engine.identity.User user = getInitiator().getUser();

        final ProcessDefinition subProcessDefinition = createSubprocess(user);

        final ProcessDefinition intermediateSubProcessDefinition = createIntermediateSubprocess(user);

        final ProcessDefinition processDefinition = createParentProcess(user);

        processAPI = TenantAPIAccessor.getProcessAPI(getSession());

        final long parentProcessInstanceId = processAPI.startProcess(processDefinition.getId()).getId();

        try {
            final long activityInstanceId = waitForPendingTask();
            processAPI.assignUserTask(activityInstanceId, getSession().getUserId());
            processAPI.executeFlowNode(activityInstanceId);

            final long subprocessActivityInstanceId = waitForPendingTask();

            //Create a thread to simulate a call to the forms servlet (and have the task archived)
            final Callable<Void> task = new Callable<Void>() {

                @Override
                public Void call() throws Exception {
                    final long activityInstanceId = waitForPendingTask();
                    processAPI.assignUserTask(activityInstanceId, getSession().getUserId());
                    processAPI.executeFlowNode(activityInstanceId);
                    return null;
                }
            };
            final ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.submit(task).get();

            waitForPendingTask();

            final Map<String, Object> urlContext = new HashMap<String, Object>();
            final String formId = "subProcess--1.0--Approval$entry";
            urlContext.put(FormServiceProviderUtil.FORM_ID, formId);
            urlContext.put(FormServiceProviderUtil.TASK_UUID, subprocessActivityInstanceId);
            final Map<String, Object> context = new HashMap<String, Object>();
            context.put(FormServiceProviderUtil.URL_CONTEXT, urlContext);
            context.put(FormServiceProviderUtil.LOCALE, Locale.ENGLISH);
            context.put(FormServiceProviderUtil.API_SESSION, getSession());

            final FormServiceProvider formServiceProvider = FormServiceProviderFactory.getFormServiceProvider(getSession().getTenantId());
            final FormURLComponents formURLComponents = formServiceProvider.getNextFormURLParameters(formId, context);

            Assert.assertNotNull(formURLComponents);
            Assert.assertEquals("Confirmation", formURLComponents.getTaskName());

        } finally {
            removeProcessAndSubProcess(subProcessDefinition, intermediateSubProcessDefinition, processDefinition, parentProcessInstanceId);
        }
    }

    @Test
    public void testGetNextFormURLParametersFromParentprocessTask() throws Exception {

        final org.bonitasoft.engine.identity.User user = getInitiator().getUser();

        final ProcessDefinition subProcessDefinition = createSubprocess(user);

        final ProcessDefinition intermediateSubProcessDefinition = createIntermediateSubprocess(user);

        final ProcessDefinition processDefinition = createParentProcess(user);

        processAPI = TenantAPIAccessor.getProcessAPI(getSession());

        final long parentProcessInstanceId = processAPI.startProcess(processDefinition.getId()).getId();

        try {
            final long activityInstanceId = waitForPendingTask();

            //Create a thread to simulate a call to the forms servlet (and have the task archived)
            final Callable<Void> task = new Callable<Void>() {

                @Override
                public Void call() throws Exception {
                    final long activityInstanceId = waitForPendingTask();
                    processAPI.assignUserTask(activityInstanceId, getSession().getUserId());
                    processAPI.executeFlowNode(activityInstanceId);
                    return null;
                }
            };
            final ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.submit(task).get();

            waitForPendingTask();

            final Map<String, Object> urlContext = new HashMap<String, Object>();
            final String formId = "parentProcess--1.0--Request$entry";
            urlContext.put(FormServiceProviderUtil.FORM_ID, formId);
            urlContext.put(FormServiceProviderUtil.TASK_UUID, activityInstanceId);
            final Map<String, Object> context = new HashMap<String, Object>();
            context.put(FormServiceProviderUtil.URL_CONTEXT, urlContext);
            context.put(FormServiceProviderUtil.LOCALE, Locale.ENGLISH);
            context.put(FormServiceProviderUtil.API_SESSION, getSession());

            final FormServiceProvider formServiceProvider = FormServiceProviderFactory.getFormServiceProvider(getSession().getTenantId());
            final FormURLComponents formURLComponents = formServiceProvider.getNextFormURLParameters(formId, context);

            Assert.assertNotNull(formURLComponents);
            Assert.assertEquals("Approval", formURLComponents.getTaskName());

        } finally {
            removeProcessAndSubProcess(subProcessDefinition, intermediateSubProcessDefinition, processDefinition, parentProcessInstanceId);
        }
    }

    @Test
    public void testGetNextFormURLParametersFromParentprocessInstance() throws Exception {

        final org.bonitasoft.engine.identity.User user = getInitiator().getUser();

        final ProcessDefinition subProcessDefinition = createSubprocess(user);

        final ProcessDefinition intermediateSubProcessDefinition = createIntermediateSubprocess(user);

        final ProcessDefinition processDefinition = createParentProcess(user);

        processAPI = TenantAPIAccessor.getProcessAPI(getSession());

        //Create a thread to simulate a call to the forms servlet
        final Callable<Long> task = new Callable<Long>() {

            @Override
            public Long call() throws Exception {
                return processAPI.startProcess(processDefinition.getId()).getId();
            }
        };
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        final Long parentProcessInstanceId = executorService.submit(task).get();

        try {
            waitForPendingTask();

            final Map<String, Object> urlContext = new HashMap<String, Object>();
            final String formId = "parentProcess--1.0$entry";
            urlContext.put(FormServiceProviderUtil.FORM_ID, formId);
            urlContext.put(FormServiceProviderUtil.INSTANCE_UUID, parentProcessInstanceId);
            final Map<String, Object> context = new HashMap<String, Object>();
            context.put(FormServiceProviderUtil.URL_CONTEXT, urlContext);
            context.put(FormServiceProviderUtil.LOCALE, Locale.ENGLISH);
            context.put(FormServiceProviderUtil.API_SESSION, getSession());

            final FormServiceProvider formServiceProvider = FormServiceProviderFactory.getFormServiceProvider(getSession().getTenantId());
            final FormURLComponents formURLComponents = formServiceProvider.getNextFormURLParameters(formId, context);

            Assert.assertNotNull(formURLComponents);
            Assert.assertEquals("Request", formURLComponents.getTaskName());

        } finally {
            removeProcessAndSubProcess(subProcessDefinition, intermediateSubProcessDefinition, processDefinition, parentProcessInstanceId);
        }
    }

    protected void removeProcessAndSubProcess(final ProcessDefinition subProcessDefinition, final ProcessDefinition intermediateSubProcessDefinition,
            final ProcessDefinition processDefinition,
            final Long parentProcessInstanceId) throws DeletionException, ProcessDefinitionNotFoundException, ProcessActivationException {

        processAPI.deleteProcessInstance(parentProcessInstanceId);
        processAPI.disableProcess(processDefinition.getId());
        processAPI.disableProcess(intermediateSubProcessDefinition.getId());
        processAPI.disableProcess(subProcessDefinition.getId());
        processAPI.deleteProcessDefinition(processDefinition.getId());
        processAPI.deleteProcessDefinition(intermediateSubProcessDefinition.getId());
        processAPI.deleteProcessDefinition(subProcessDefinition.getId());
    }

    protected ProcessDefinition createParentProcess(final org.bonitasoft.engine.identity.User user) throws InvalidExpressionException,
            InvalidProcessDefinitionException, InvalidBusinessArchiveFormatException, AlreadyExistsException, ProcessDeployException, CreationException,
            ProcessDefinitionNotFoundException, ProcessEnablementException {
        final ProcessDefinitionBuilder processBuilder = new ProcessDefinitionBuilder().createNewInstance("parentProcess", "1.0");
        processBuilder.addActor("myActor");
        final ExpressionBuilder expressionBuilder = new ExpressionBuilder();
        processBuilder.addCallActivity("CallActivity",
                expressionBuilder.createNewInstance("processName").setContent("intermediateSubProcess").setExpressionType(ExpressionType.TYPE_CONSTANT)
                        .setReturnType(String.class.getName()).done(), expressionBuilder.createNewInstance("processVersion").setContent("1.0")
                        .setExpressionType(ExpressionType.TYPE_CONSTANT)
                        .setReturnType(String.class.getName()).done());
        processBuilder.addUserTask("Request", "myActor");
        processBuilder.addTransition("Request", "CallActivity");
        processBuilder.addUserTask("Confirmation", "myActor");
        processBuilder.addTransition("CallActivity", "Confirmation");

        final DesignProcessDefinition designProcessDefinition = processBuilder.done();
        final BusinessArchiveBuilder businessArchiveBuilderProcess = new BusinessArchiveBuilder().createNewBusinessArchive();
        final BusinessArchive businessArchive = businessArchiveBuilderProcess.setProcessDefinition(designProcessDefinition).done();
        final ProcessDefinition processDefinition = processAPI.deploy(businessArchive);

        final ActorInstance processActor = processAPI.getActors(processDefinition.getId(), 0, 1, ActorCriterion.NAME_ASC).get(0);
        processAPI.addUserToActor(processActor.getId(), user.getId());

        processAPI.enableProcess(processDefinition.getId());
        return processDefinition;
    }

    protected ProcessDefinition createIntermediateSubprocess(final org.bonitasoft.engine.identity.User user)
            throws InvalidProcessDefinitionException,
            InvalidBusinessArchiveFormatException, AlreadyExistsException, ProcessDeployException, CreationException, ProcessDefinitionNotFoundException,
            ProcessEnablementException, InvalidExpressionException, IllegalArgumentException {
        final ProcessDefinitionBuilder subProcessBuilder = new ProcessDefinitionBuilder().createNewInstance("intermediateSubProcess", "1.0");
        final ExpressionBuilder expressionBuilder = new ExpressionBuilder();
        subProcessBuilder.addCallActivity("CallActivity",
                expressionBuilder.createNewInstance("processName").setContent("subProcess").setExpressionType(ExpressionType.TYPE_CONSTANT)
                        .setReturnType(String.class.getName()).done(), expressionBuilder.createNewInstance("processVersion").setContent("1.0")
                        .setExpressionType(ExpressionType.TYPE_CONSTANT)
                        .setReturnType(String.class.getName()).done());

        final DesignProcessDefinition designSubProcessDefinition = subProcessBuilder.done();
        final BusinessArchiveBuilder businessArchiveBuilderSubProcess = new BusinessArchiveBuilder().createNewBusinessArchive();
        final BusinessArchive businessArchiveSubProcess = businessArchiveBuilderSubProcess.setProcessDefinition(designSubProcessDefinition).done();
        final ProcessDefinition subProcessDefinition = processAPI.deploy(businessArchiveSubProcess);

        processAPI.enableProcess(subProcessDefinition.getId());
        return subProcessDefinition;
    }

    protected ProcessDefinition createSubprocess(final org.bonitasoft.engine.identity.User user)
            throws InvalidProcessDefinitionException,
            InvalidBusinessArchiveFormatException, AlreadyExistsException, ProcessDeployException, CreationException, ProcessDefinitionNotFoundException,
            ProcessEnablementException {
        final ProcessDefinitionBuilder subProcessBuilder = new ProcessDefinitionBuilder().createNewInstance("subProcess", "1.0");
        subProcessBuilder.addActor("myActor");
        subProcessBuilder.addUserTask("Approval", "myActor");

        final DesignProcessDefinition designSubProcessDefinition = subProcessBuilder.done();
        final BusinessArchiveBuilder businessArchiveBuilderSubProcess = new BusinessArchiveBuilder().createNewBusinessArchive();
        final BusinessArchive businessArchiveSubProcess = businessArchiveBuilderSubProcess.setProcessDefinition(designSubProcessDefinition).done();
        final ProcessDefinition subProcessDefinition = processAPI.deploy(businessArchiveSubProcess);

        final ActorInstance subProcessActor = processAPI.getActors(subProcessDefinition.getId(), 0, 1, ActorCriterion.NAME_ASC).get(0);
        processAPI.addUserToActor(subProcessActor.getId(), user.getId());

        processAPI.enableProcess(subProcessDefinition.getId());
        return subProcessDefinition;
    }

    protected void terminateProcessInstance(final long processInstanceId) throws Exception, UpdateException, FlowNodeExecutionException {
        Assert.assertTrue("no pending user task instances are found", new WaitUntil(50, 1000) {

            @Override
            protected boolean check() throws Exception {
                return processAPI.getPendingHumanTaskInstances(getSession().getUserId(), 0, 10, null).size() >= 1;
            }
        }.waitUntil());
        long activityInstanceId = processAPI.getPendingHumanTaskInstances(getSession().getUserId(), 0, 1, ActivityInstanceCriterion.NAME_ASC).get(0).getId();
        processAPI.assignUserTask(activityInstanceId, getSession().getUserId());
        processAPI.executeFlowNode(activityInstanceId);
        Assert.assertTrue("no pending user task instances are found", new WaitUntil(50, 1000) {

            @Override
            protected boolean check() throws Exception {
                return processAPI.getPendingHumanTaskInstances(getSession().getUserId(), 0, 10, null).size() >= 1;
            }
        }.waitUntil());
        activityInstanceId = processAPI.getPendingHumanTaskInstances(getSession().getUserId(), 0, 1, ActivityInstanceCriterion.NAME_ASC).get(0).getId();
        processAPI.assignUserTask(activityInstanceId, getSession().getUserId());
        processAPI.executeFlowNode(activityInstanceId);
        Assert.assertTrue("no archived process isnatnce was found", new WaitUntil(50, 1000) {

            @Override
            protected boolean check() throws Exception {
                final SearchOptionsBuilder builder = new SearchOptionsBuilder(0, 1);
                builder.filter(ArchivedProcessInstancesSearchDescriptor.SOURCE_OBJECT_ID, processInstanceId);
                return processAPI.searchArchivedProcessInstances(builder.done()).getCount() == 1;
            }
        }.waitUntil());
    }

    protected long waitForPendingTask() throws Exception {
        Assert.assertTrue("no pending user task instances are found", new WaitUntil(50, 1000) {

            @Override
            protected boolean check() throws Exception {
                return processAPI.getPendingHumanTaskInstances(getSession().getUserId(), 0, 10,
                        null).size() >= 1;
            }
        }.waitUntil());
        final long activityInstanceId = processAPI.getPendingHumanTaskInstances(getSession().getUserId(), 0, 1, ActivityInstanceCriterion.NAME_ASC)
                .get(0).getId();
        return activityInstanceId;
    }

}
