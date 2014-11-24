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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.forms.server.accessor.impl.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Date;

import org.bonitasoft.console.common.server.utils.FormsResourcesUtils;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.bar.BarResource;
import org.bonitasoft.engine.bpm.bar.BusinessArchive;
import org.bonitasoft.engine.bpm.bar.BusinessArchiveBuilder;
import org.bonitasoft.engine.bpm.process.DesignProcessDefinition;
import org.bonitasoft.engine.bpm.process.ProcessDefinition;
import org.bonitasoft.engine.bpm.process.impl.ProcessDefinitionBuilder;
import org.bonitasoft.forms.server.FormsTestCase;
import org.bonitasoft.forms.server.accessor.IApplicationFormDefAccessor;
import org.bonitasoft.forms.server.accessor.impl.XMLApplicationFormDefAccessorImpl;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;

/**
 * Test for the implementation of the form document builder
 *
 * @author Anthony Birembaut
 *
 */
public class TestFormDocumentBuilder extends FormsTestCase {

    @Test
    public void testInterpreteFormXMLWithI18n() throws Exception {
        final Document document = FormDocumentBuilderFactory.getFormDocumentBuilder(getSession(), -1, "fr", null).getDocument();

        final IApplicationFormDefAccessor formDefAccessor = new XMLApplicationFormDefAccessorImpl(0, document, "firstProcess--1.0--task1$entry", null, null);

        Assert.assertEquals("Commentaire", formDefAccessor.getPageWidgets("0").get(0).getTitleExpression().getContent());
    }

    @Test
    public void testInterpreteFormXMLWithI18nDefault() throws Exception {
        final Document document = FormDocumentBuilderFactory.getFormDocumentBuilder(getSession(), -1, null, null).getDocument();

        final IApplicationFormDefAccessor formDefAccessor = new XMLApplicationFormDefAccessorImpl(0, document, "firstProcess--1.0--task1$entry", null, null);

        Assert.assertEquals("Comment", formDefAccessor.getPageWidgets("0").get(0).getTitleExpression().getContent());
    }

    @Test
    public void testInterpreteFormXMLWithI18nNotExisting() throws Exception {
        final Document document = FormDocumentBuilderFactory.getFormDocumentBuilder(getSession(), -1, "de", null).getDocument();

        final IApplicationFormDefAccessor formDefAccessor = new XMLApplicationFormDefAccessorImpl(0, document, "firstProcess--1.0--task1$entry", null, null);

        Assert.assertEquals("Comment", formDefAccessor.getPageWidgets("0").get(0).getTitleExpression().getContent());
    }

    @Test
    public void testExtractResourcesFromBar() throws Exception {

        final ProcessDefinitionBuilder processBuilder = new ProcessDefinitionBuilder();

        final DesignProcessDefinition simpleProcess = processBuilder.createNewInstance("simple_process", "1.0").addActor("john")
                .addUserTask("task1", "john").getProcess();

        final InputStream inputStream = getClass().getResourceAsStream("/" + FormDocumentBuilder.FORM_DEFINITION_DEFAULT_FILE_NAME);
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] content;
        try {
            int b = inputStream.read();
            while (b >= 0) {
                byteArrayOutputStream.write(b);
                b = inputStream.read();
            }
            byteArrayOutputStream.flush();
            content = byteArrayOutputStream.toByteArray();
        } finally {
            inputStream.close();
            byteArrayOutputStream.close();
        }
        final BusinessArchiveBuilder businessArchiveBuilder = new BusinessArchiveBuilder().createNewBusinessArchive();
        final BusinessArchive businessArchive = businessArchiveBuilder.setProcessDefinition(simpleProcess)
                .addExternalResource(new BarResource("forms/" + FormDocumentBuilder.FORM_DEFINITION_DEFAULT_FILE_NAME, content)).done();
        final ProcessAPI processAPI = TenantAPIAccessor.getProcessAPI(getSession());
        final ProcessDefinition bonitaProcess = processAPI.deploy(businessArchive);
        final Date deploymentDate = processAPI.getProcessDeploymentInfo(bonitaProcess.getId()).getDeploymentDate();

        try {
            FormDocumentBuilder.getInstance(getSession(), bonitaProcess.getId(), "en", deploymentDate, true).getDocument();

            final File resourcesDir = FormsResourcesUtils.getApplicationResourceDir(getSession(), bonitaProcess.getId(), deploymentDate);
            final File formsFile = new File(resourcesDir, FormDocumentBuilder.FORM_DEFINITION_DEFAULT_FILE_NAME);
            if (!formsFile.exists()) {
                Assert.fail();
            }

            FormsResourcesUtils.removeApplicationFiles(getSession(), bonitaProcess.getId());
            if (formsFile.exists()) {
                Assert.fail();
            }
        } finally {
            processAPI.deleteProcess(bonitaProcess.getId());
        }
    }
}
