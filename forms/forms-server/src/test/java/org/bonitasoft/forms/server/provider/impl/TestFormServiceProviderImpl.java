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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.bonitasoft.console.common.server.preferences.properties.PropertiesFactory;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.actor.ActorCriterion;
import org.bonitasoft.engine.bpm.actor.ActorInstance;
import org.bonitasoft.engine.bpm.bar.BarResource;
import org.bonitasoft.engine.bpm.bar.BusinessArchive;
import org.bonitasoft.engine.bpm.bar.BusinessArchiveBuilder;
import org.bonitasoft.engine.bpm.flownode.ActivityInstanceCriterion;
import org.bonitasoft.engine.bpm.process.ArchivedProcessInstancesSearchDescriptor;
import org.bonitasoft.engine.bpm.process.DesignProcessDefinition;
import org.bonitasoft.engine.bpm.process.ProcessDefinition;
import org.bonitasoft.engine.bpm.process.impl.ProcessDefinitionBuilder;
import org.bonitasoft.engine.expression.ExpressionBuilder;
import org.bonitasoft.engine.expression.ExpressionType;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.forms.client.model.Expression;
import org.bonitasoft.forms.client.model.FormFieldValue;
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
public class TestFormServiceProviderImpl extends FormsTestCase {

    // protected static final String USERNAME = "dwight";
    //
    // protected static final String PASSWORD = "Schrute";

    protected ProcessDefinition processDefinition;

    protected long processInstanceId;

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
                expressionBuilder.createNewInstance("word").setContent("Word").setExpressionType(ExpressionType.TYPE_CONSTANT.name())
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

        processInstanceId = processAPI.startProcess(processDefinition.getId()).getId();

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
        final FormServiceProvider formServiceProvider = FormServiceProviderFactory.getFormServiceProvider(getSession().getTenantId());
        final Map<String, Object> urlContext = new HashMap<String, Object>();
        urlContext.put(FormServiceProviderUtil.INSTANCE_UUID, processInstanceId);
        final User user = new User(getSession().getUserName(), Locale.ENGLISH.toString());
        user.setUseCredentialTransmission(PropertiesFactory.getSecurityProperties(getSession().getTenantId()).useCredentialsTransmission());
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
        final FormServiceProvider formServiceProvider = FormServiceProviderFactory.getFormServiceProvider(getSession().getTenantId());
        final Map<String, Object> urlContext = new HashMap<String, Object>();
        Assert.assertTrue("no pending user task instances are found", new WaitUntil(50, 1000) {

            @Override
            protected boolean check() throws Exception {
                return processAPI.getPendingHumanTaskInstances(TestFormServiceProviderImpl.this.getSession().getUserId(), 0, 10,
                        null).size() >= 1;
            }
        }.waitUntil());
        final long activityInstanceId = processAPI.getPendingHumanTaskInstances(getSession().getUserId(), 0, 1, ActivityInstanceCriterion.NAME_ASC)
                .get(0).getId();
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
        final FormServiceProvider formServiceProvider = FormServiceProviderFactory.getFormServiceProvider(getSession().getTenantId());
        final Map<String, Object> urlContext = new HashMap<String, Object>();
        Assert.assertTrue("no pending user task instances are found", new WaitUntil(50, 1000) {

            @Override
            protected boolean check() throws Exception {
                return processAPI.getPendingHumanTaskInstances(TestFormServiceProviderImpl.this.getSession().getUserId(), 0, 10,
                        null).size() >= 1;
            }
        }.waitUntil());
        final long activityInstanceId = processAPI.getPendingHumanTaskInstances(getSession().getUserId(), 0, 1, ActivityInstanceCriterion.NAME_ASC)
                .get(0).getId();
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
        final FormServiceProvider formServiceProvider = FormServiceProviderFactory.getFormServiceProvider(getSession().getTenantId());
        final Map<String, Object> urlContext = new HashMap<String, Object>();
        Assert.assertTrue("no pending user task instances are found", new WaitUntil(50, 1000) {

            @Override
            protected boolean check() throws Exception {
                return processAPI.getPendingHumanTaskInstances(TestFormServiceProviderImpl.this.getSession().getUserId(), 0, 10,
                        null).size() >= 1;
            }
        }.waitUntil());
        final long activityInstanceId = processAPI.getPendingHumanTaskInstances(getSession().getUserId(), 0, 1, ActivityInstanceCriterion.NAME_ASC)
                .get(0).getId();
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
        Assert.assertTrue("no pending user task instances are found", new WaitUntil(50, 1000) {

            @Override
            protected boolean check() throws Exception {
                return processAPI.getPendingHumanTaskInstances(TestFormServiceProviderImpl.this.getSession().getUserId(), 0, 10,
                        null).size() >= 1;
            }
        }.waitUntil());
        long activityInstanceId = processAPI.getPendingHumanTaskInstances(getSession().getUserId(), 0, 1, ActivityInstanceCriterion.NAME_ASC).get(0)
                .getId();
        processAPI.assignUserTask(activityInstanceId, getSession().getUserId());
        processAPI.executeFlowNode(activityInstanceId);
        Assert.assertTrue("no pending user task instances are found", new WaitUntil(50, 1000) {

            @Override
            protected boolean check() throws Exception {
                return processAPI.getPendingHumanTaskInstances(TestFormServiceProviderImpl.this.getSession().getUserId(), 0, 10,
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
        final FormServiceProvider formServiceProvider = FormServiceProviderFactory.getFormServiceProvider(getSession().getTenantId());
        final Map<String, Object> urlContext = new HashMap<String, Object>();
        Assert.assertTrue("no pending user task instances are found", new WaitUntil(50, 1000) {

            @Override
            protected boolean check() throws Exception {
                return processAPI.getPendingHumanTaskInstances(TestFormServiceProviderImpl.this.getSession().getUserId(), 0, 10,
                        null).size() >= 1;
            }
        }.waitUntil());
        final long activityInstanceId = processAPI.getPendingHumanTaskInstances(getSession().getUserId(), 0, 1, ActivityInstanceCriterion.NAME_ASC)
                .get(0).getId();
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
        Assert.assertNotSame(0, urlFieldValue.getDocumentId());
    }

    @Test
    public void testGetArchivedAttachmentFormFieldValue() throws Exception {
        final FormServiceProvider formServiceProvider = FormServiceProviderFactory.getFormServiceProvider(getSession().getTenantId());
        final Map<String, Object> urlContext = new HashMap<String, Object>();
        Assert.assertTrue("no pending user task instances are found", new WaitUntil(50, 1000) {

            @Override
            protected boolean check() throws Exception {
                return processAPI.getPendingHumanTaskInstances(TestFormServiceProviderImpl.this.getSession().getUserId(), 0, 10, null).size() >= 1;
            }
        }.waitUntil());
        long activityInstanceId = processAPI.getPendingHumanTaskInstances(getSession().getUserId(), 0, 1, ActivityInstanceCriterion.NAME_ASC).get(0).getId();
        processAPI.assignUserTask(activityInstanceId, getSession().getUserId());
        processAPI.executeFlowNode(activityInstanceId);
        Assert.assertTrue("no pending user task instances are found", new WaitUntil(50, 1000) {

            @Override
            protected boolean check() throws Exception {
                return processAPI.getPendingHumanTaskInstances(TestFormServiceProviderImpl.this.getSession().getUserId(), 0, 10, null).size() >= 1;
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
        Assert.assertNotSame(0, urlFieldValue.getDocumentId());
    }

}
