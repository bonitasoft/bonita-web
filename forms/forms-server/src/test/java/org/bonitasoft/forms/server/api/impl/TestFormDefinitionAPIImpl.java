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

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.FileInputStream;
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

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.bar.BusinessArchive;
import org.bonitasoft.engine.bpm.bar.BusinessArchiveBuilder;
import org.bonitasoft.engine.bpm.process.ProcessDefinition;
import org.bonitasoft.engine.bpm.process.impl.ProcessDefinitionBuilder;
import org.bonitasoft.forms.client.model.ApplicationConfig;
import org.bonitasoft.forms.client.model.Expression;
import org.bonitasoft.forms.client.model.FormAction;
import org.bonitasoft.forms.client.model.FormPage;
import org.bonitasoft.forms.client.model.HtmlTemplate;
import org.bonitasoft.forms.client.model.TransientData;
import org.bonitasoft.forms.server.FormsTestCase;
import org.bonitasoft.forms.server.accessor.impl.util.FormCacheUtil;
import org.bonitasoft.forms.server.accessor.impl.util.FormCacheUtilFactory;
import org.bonitasoft.forms.server.api.FormAPIFactory;
import org.bonitasoft.forms.server.api.IFormDefinitionAPI;
import org.bonitasoft.forms.server.builder.IFormBuilder;
import org.bonitasoft.forms.server.builder.impl.FormBuilderImpl;
import org.bonitasoft.forms.server.provider.impl.util.FormServiceProviderUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.w3c.dom.Document;

/**
 * Unit test for the implementation of the form definition API
 *
 * @author Anthony Birembaut, Haojie Yuan
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class TestFormDefinitionAPIImpl extends FormsTestCase {

    private ProcessDefinition bonitaProcess;

    private Date deployementDate;

    private final Map<String, Object> context = new HashMap<String, Object>();

    // private LoginContext loginContext;

    private IFormBuilder formBuilder;

    private File complexProcessDefinitionFile;

    private Document document;

    private final String formID = "processName--1.0$entry";

    private final String pageID = "processPage1";

    private ProcessAPI processAPI;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        formBuilder = FormBuilderImpl.getInstance();

        complexProcessDefinitionFile = buildComplexFormXML();
        final InputStream inputStream = new FileInputStream(complexProcessDefinitionFile);
        final DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        document = builder.parse(inputStream);
        inputStream.close();

        final String actorName = "actor 1";
        final ProcessDefinitionBuilder processBuilder = new ProcessDefinitionBuilder().createNewInstance("processName", "1.0");
        processBuilder.addActor(actorName).addDescription("actor 1 description").addUserTask("task1", actorName);
        final BusinessArchiveBuilder businessArchiveBuilder = new BusinessArchiveBuilder().createNewBusinessArchive();
        final BusinessArchive businessArchive = businessArchiveBuilder.setProcessDefinition(processBuilder.done()).done();
        processAPI = TenantAPIAccessor.getProcessAPI(getSession());
        bonitaProcess = processAPI.deploy(businessArchive);
        deployementDate = processAPI.getProcessDeploymentInfo(bonitaProcess.getId()).getDeploymentDate();

        final Map<String, Object> urlContext = new HashMap<String, Object>();
        urlContext.put(FormServiceProviderUtil.PROCESS_UUID, bonitaProcess.getId());
        urlContext.put(FormServiceProviderUtil.IS_EDIT_MODE, true);
        urlContext.put(FormServiceProviderUtil.DOCUMENT, document);
        urlContext.put(FormServiceProviderUtil.FORM_ID, formID);
        urlContext.put(FormServiceProviderUtil.LOCALE, Locale.ENGLISH);
        urlContext.put(FormServiceProviderUtil.APPLICATION_DEPLOYMENT_DATE, deployementDate);
        urlContext.put(FormServiceProviderUtil.MODE, "form");
        urlContext.put(FormServiceProviderUtil.TRANSIENT_DATA_CONTEXT, context);
        context.put(FormServiceProviderUtil.URL_CONTEXT, urlContext);
        context.put(FormServiceProviderUtil.LOCALE, Locale.ENGLISH);
        context.put(FormServiceProviderUtil.API_SESSION, getSession());

    }

    @Override
    @After
    public void tearDown() throws Exception {

        processAPI.deleteProcess(bonitaProcess.getId());
        super.tearDown();
    }

    @Test
    public void testGetProductVersion() throws Exception {
        final IFormDefinitionAPI api = FormAPIFactory.getFormDefinitionAPI(getSession().getTenantId(), document, null, Locale.ENGLISH.toString());
        final String result = api.getProductVersion(context);
        Assert.assertNotNull(result);
        Assert.assertEquals(FormBuilderImpl.PRODUCT_VERSION, result);
    }

    @Test
    public void testGetApplicationPermissions() throws Exception {
        final IFormDefinitionAPI api = FormAPIFactory.getFormDefinitionAPI(getSession().getTenantId(), document, null, Locale.ENGLISH.toString());
        final String result = api.getApplicationPermissions(formID, context);
        Assert.assertNotNull(result);
        Assert.assertEquals("application#test", result);
    }

    @Test
    public void testGetMigrationProductVersion() throws Exception {
        final IFormDefinitionAPI api = FormAPIFactory.getFormDefinitionAPI(getSession().getTenantId(), document, null, Locale.ENGLISH.toString());
        final String result = api.getMigrationProductVersion(formID, context);
        Assert.assertNotNull(result);
        Assert.assertEquals(FormBuilderImpl.PRODUCT_VERSION, result);
    }

    @Test
    public void testGetFormFirstPage() throws Exception {
        final IFormDefinitionAPI api = FormAPIFactory.getFormDefinitionAPI(getSession().getTenantId(), document, deployementDate, Locale.ENGLISH.toString());
        final Expression result = api.getFormFirstPage(formID, context);
        Assert.assertNotNull(result);
        Assert.assertEquals("processPage1", result.getContent());
    }

    @Test
    public void testGetFormPage() throws Exception {
        final IFormDefinitionAPI api = FormAPIFactory.getFormDefinitionAPI(getSession().getTenantId(), document, deployementDate, Locale.ENGLISH.toString());
        final FormPage result = api.getFormPage(formID, pageID, context);
        Assert.assertNotNull(result);
        Assert.assertEquals("processPage1", result.getPageId());
    }

    @Test
    public void testGetFormPageFromCache() throws Exception {
        final FormCacheUtil formCacheUtil = spy(FormCacheUtilFactory.getTenantFormCacheUtil(getSession().getTenantId()));
        final IFormDefinitionAPI api = new FormDefinitionAPIImpl(getSession().getTenantId(), document, formCacheUtil, deployementDate,
                Locale.ENGLISH.toString());
        final FormPage formPageFirstCall = api.getFormPage(formID, pageID, context);
        final FormPage formPage = api.getFormPage(formID, pageID, context);
        Assert.assertNotNull(formPage);
        verify(formCacheUtil, times(2)).getPage(formID, Locale.ENGLISH.toString(), deployementDate, pageID);
        verify(formCacheUtil, times(1)).storePage(formID, Locale.ENGLISH.toString(), deployementDate, formPageFirstCall);
        Assert.assertEquals("processPage1", formPage.getPageId());
    }

    @Test
    public void testFormPageLayout() throws Exception {
        final IFormDefinitionAPI api = FormAPIFactory.getFormDefinitionAPI(getSession().getTenantId(), document, deployementDate, Locale.ENGLISH.toString());
        final String result = api.getFormPageLayout(formID, pageID, context);
        Assert.assertNotNull(result);
        Assert.assertEquals("/process-page1-template.html", result);
    }

    @Test
    public void testGetApplicationConfig() throws Exception {
        final IFormDefinitionAPI api = FormAPIFactory.getFormDefinitionAPI(getSession().getTenantId(), document, deployementDate, Locale.ENGLISH.toString());
        final ApplicationConfig result = api.getApplicationConfig(context, formID, false);
        Assert.assertNotNull(result);
        Assert.assertEquals("mandatory-label", result.getMandatoryLabelExpression().getContent());
    }

    @Test
    public void testGetFormTransientData() throws Exception {
        final IFormDefinitionAPI api = FormAPIFactory.getFormDefinitionAPI(getSession().getTenantId(), document, deployementDate, Locale.ENGLISH.toString());
        final List<TransientData> result = api.getFormTransientData(formID, context);
        Assert.assertNotNull(result);
    }

    @Test
    public void testGetFormActions() throws Exception {

        final IFormDefinitionAPI api = FormAPIFactory.getFormDefinitionAPI(getSession().getTenantId(), document, deployementDate, Locale.ENGLISH.toString());
        final List<String> pageIds = new ArrayList<String>();
        pageIds.add(pageID);
        final List<FormAction> result = api.getFormActions(formID, pageIds, context);
        Assert.assertNotNull(result);
    }

    @Test
    public void testGetFormConfirmationLayout() throws Exception {
        final IFormDefinitionAPI api = FormAPIFactory.getFormDefinitionAPI(getSession().getTenantId(), document, deployementDate, Locale.ENGLISH.toString());
        final HtmlTemplate result = api.getFormConfirmationLayout(formID, context);
        Assert.assertNotNull(result);
    }

    @Test
    public void testGetTransientDataContext() throws Exception {
        final IFormDefinitionAPI api = FormAPIFactory.getFormDefinitionAPI(getSession().getTenantId(), document, deployementDate, Locale.ENGLISH.toString());
        final List<TransientData> transientData = api.getFormTransientData(formID, context);
        final Map<String, Serializable> result = api.getTransientDataContext(transientData, Locale.ENGLISH, context);
        Assert.assertNotNull(result);
    }

    @Test
    public void testGetApplicationErrorLayout() throws Exception {
        final IFormDefinitionAPI api = FormAPIFactory.getFormDefinitionAPI(getSession().getTenantId(), document, deployementDate, Locale.ENGLISH.toString());
        final HtmlTemplate result = api.getApplicationErrorLayout(context);
        Assert.assertNotNull(result);
    }

    private File buildComplexFormXML() throws Exception {
        formBuilder.createFormDefinition();
        formBuilder.addMigrationProductVersion("6.0");
        formBuilder.addApplication("processName", "1.0");
        formBuilder.addLabelExpression(null, "process label", "TYPE_CONSTANT", String.class.getName(), null);
        formBuilder.addLayout("/process-template.html");
        formBuilder.addPermissions("application#test");

        formBuilder.addEntryForm("processName--1.0$entry");
        formBuilder.addFirstPageIdExpression(null, "processPage1", "TYPE_CONSTANT", String.class.getName(), null);
        formBuilder.addPermissions("process#test1");
        formBuilder.addPage("processPage1");
        formBuilder.addLabelExpression(null, "page1 label", "TYPE_CONSTANT", String.class.getName(), null);
        formBuilder.addLayout("/process-page1-template.html");
        formBuilder.addMandatoryLabelExpression(null, "mandatory-label", "TYPE_CONSTANT", String.class.getName(), null);

        return formBuilder.done();
    }
}
