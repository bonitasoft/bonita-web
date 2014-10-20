/**
 * Copyright (C) 2012 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.web.rest.server.api.bpm.cases;

import java.io.File;
import java.util.HashMap;

import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.document.Document;
import org.bonitasoft.test.toolkit.bpm.TestCase;
import org.bonitasoft.test.toolkit.bpm.TestProcessFactory;
import org.bonitasoft.test.toolkit.organization.TestUser;
import org.bonitasoft.test.toolkit.organization.TestUserFactory;
import org.bonitasoft.web.rest.model.bpm.cases.CaseDocumentItem;
import org.bonitasoft.web.rest.server.AbstractConsoleTest;
import org.bonitasoft.web.rest.server.framework.search.ItemSearchResult;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Vincent Elcrin
 *
 */
public class APICaseDocumentIntegrationTest extends AbstractConsoleTest {

    private APICaseDocument apiCaseDocument;

    private Document expectedDocument;

    /*
     * (non-Javadoc)
     * @see org.bonitasoft.console.server.AbstractConsoleTest#consoleTestSetUp()
     */
    @Override
    public void consoleTestSetUp() throws Exception {
        apiCaseDocument = new APICaseDocument();
        apiCaseDocument.setCaller(getAPICaller(TestUserFactory.getJohnCarpenter().getSession(),
                "API/bpm/caseDocument"));

        // create process with attached document
        final TestCase testCase = TestProcessFactory.getProcessWithDocumentAttached()
                .addActor(getInitiator())
                .startCase();

        expectedDocument = TenantAPIAccessor.getProcessAPI(getInitiator().getSession()).getLastDocument(testCase.getId(), "Document 1667");
    }

    private void assertDocumentsMatch(final Document document, final CaseDocumentItem caseDocumentItem) {
        Assert.assertEquals("Name is different", document.getName(), caseDocumentItem.getName());
        Assert.assertEquals("File name is different", document.getContentFileName(), caseDocumentItem.getFileName());
        Assert.assertEquals("Author is different", document.getAuthor(), (long) caseDocumentItem.getSubmittedBy().toLong());
        Assert.assertEquals("Mime type is different", document.getContentMimeType(), caseDocumentItem.getMIMEType());
        Assert.assertEquals("Content storage id is different", document.getContentStorageId(), caseDocumentItem.getStorageId());
        Assert.assertEquals("Creation date is different", document.getCreationDate(),  caseDocumentItem.getCreationDate());
        Assert.assertEquals("Id is different", document.getId(), (long) caseDocumentItem.getId().toLong());
        Assert.assertEquals("Process instance id is different", document.getProcessInstanceId(), (long) caseDocumentItem.getCaseId().toLong());
        Assert.assertEquals("Url is different", document.getUrl(), caseDocumentItem.getURL());
    }

    /*
     * (non-Javadoc)
     * @see org.bonitasoft.test.toolkit.AbstractJUnitTest#getInitiator()
     */
    @Override
    protected TestUser getInitiator() {
        return TestUserFactory.getJohnCarpenter();
    }

    @Test
    public void testAPISearch() {
        final ItemSearchResult<CaseDocumentItem> res = apiCaseDocument.search(0, 10, null, null, new HashMap<String, String>());
        Assert.assertNotNull(res);
        Assert.assertNotNull(res.getResults());
        Assert.assertTrue(res.getResults().size() > 0);
        assertDocumentsMatch(expectedDocument, res.getResults().get(0));
    }

    @Test
    public void testAPISearchSupervisedResultEmpty() {
        final HashMap<String, String> filters = new HashMap<String, String>();
        filters.put(CaseDocumentItem.FILTER_SUPERVISOR_ID, String.valueOf(getInitiator().getId()));

        final ItemSearchResult<CaseDocumentItem> res = apiCaseDocument.search(0, 10, null, null, filters);
        Assert.assertNotNull(res);
        Assert.assertNotNull(res.getResults());
        Assert.assertTrue(res.getResults().size() == 0);
    }

    @Test
    public void testAPISearchSupervised() throws Exception {
        // set initiator as process supervisor
        TestProcessFactory.getProcessWithDocumentAttached()
        .addSupervisor(getInitiator());

        final HashMap<String, String> filters = new HashMap<String, String>();
        filters.put(CaseDocumentItem.FILTER_SUPERVISOR_ID, String.valueOf(getInitiator().getId()));

        final ItemSearchResult<CaseDocumentItem> res = apiCaseDocument.search(0, 10, null, null, filters);
        Assert.assertNotNull(res);
        Assert.assertNotNull(res.getResults());
        Assert.assertTrue(res.getResults().size() > 0);
        assertDocumentsMatch(expectedDocument, res.getResults().get(0));
    }

    @Test
    public void testAPIGet() {
        assertDocumentsMatch(expectedDocument, apiCaseDocument.get(APIID.makeAPIID(expectedDocument.getId())));
    }

    @Test
    public void testAPICaseDocumentUpdateUrl() {
        final HashMap<String, String> attributes = new HashMap<String, String>();
        attributes.put(CaseDocumentItem.ATTRIBUTE_CASE_ID, String.valueOf(expectedDocument.getProcessInstanceId()));
        attributes.put(CaseDocumentItem.ATTRIBUTE_NAME, expectedDocument.getName());
        attributes.put(CaseDocumentItem.ATTRIBUTE_CONTENT_FILENAME, expectedDocument.getContentFileName());
        attributes.put(CaseDocumentItem.ATTRIBUTE_CONTENT_MIMETYPE, expectedDocument.getContentMimeType());

        attributes.put(CaseDocumentItem.ATTRIBUTE_URL, "newurl");

        Assert.assertEquals("newurl", apiCaseDocument.update(APIID.makeAPIID(expectedDocument.getId()), attributes).getURL());
    }

    @Test
    public void testAPICaseDocumentUpdateFile() throws Exception {
        final HashMap<String, String> attributes = new HashMap<String, String>();
        attributes.put(CaseDocumentItem.ATTRIBUTE_CASE_ID, String.valueOf(expectedDocument.getProcessInstanceId()));
        attributes.put(CaseDocumentItem.ATTRIBUTE_NAME, expectedDocument.getName());
        attributes.put(CaseDocumentItem.ATTRIBUTE_CONTENT_FILENAME, expectedDocument.getContentFileName());
        attributes.put(CaseDocumentItem.ATTRIBUTE_CONTENT_MIMETYPE, expectedDocument.getContentMimeType());

        attributes.put(CaseDocumentItem.ATTRIBUTE_UPLOAD_PATH, File.createTempFile("thisismynewfile", ".doc").getPath());

        apiCaseDocument.update(APIID.makeAPIID(expectedDocument.getId()), attributes);
    }

    @Test(expected = APIException.class)
    public void testMalformedUpdate() {
        apiCaseDocument.update(APIID.makeAPIID(expectedDocument.getId()), new HashMap<String, String>());
    }
}
