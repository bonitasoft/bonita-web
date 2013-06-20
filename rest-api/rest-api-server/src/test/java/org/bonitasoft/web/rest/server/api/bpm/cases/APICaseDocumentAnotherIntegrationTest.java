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

import static junit.framework.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.test.toolkit.bpm.TestCase;
import org.bonitasoft.test.toolkit.bpm.TestProcessFactory;
import org.bonitasoft.test.toolkit.organization.TestUser;
import org.bonitasoft.test.toolkit.organization.TestUserFactory;
import org.bonitasoft.web.rest.model.bpm.cases.CaseDocumentItem;
import org.bonitasoft.web.rest.server.AbstractConsoleTest;
import org.bonitasoft.web.rest.server.api.bpm.cases.APICaseDocument;
import org.bonitasoft.web.rest.server.framework.search.ItemSearchResult;
import org.junit.Test;

/**
 * @author Colin PUY
 * 
 */
public class APICaseDocumentAnotherIntegrationTest extends AbstractConsoleTest {

    private APICaseDocument apiCaseDocument;

    @Override
    public void consoleTestSetUp() throws Exception {
        apiCaseDocument = new APICaseDocument();
        apiCaseDocument.setCaller(getAPICaller(getInitiator().getSession(), "API/bpm/caseDocument"));
    }

    @Override
    protected TestUser getInitiator() {
        return TestUserFactory.getJohnCarpenter();
    }

    @Test
    public void caseDocumentsCanBeCountedByCaseId() throws Exception {
        TestCase startCase = TestProcessFactory.getProcessWithDocumentAttached().addActor(getInitiator()).startCase();
        Map<String, String> caseIdfilter = buildCaseIdFilter(startCase.getId());

        ItemSearchResult<CaseDocumentItem> searchResult = apiCaseDocument.runSearch(0, 0, null, null, caseIdfilter, null, null);

        assertEquals(1L, searchResult.getTotal());
    }

    private Map<String, String> buildCaseIdFilter(long caseId) {
        Map<String, String> filters = new HashMap<String, String>();
        filters.put(CaseDocumentItem.ATTRIBUTE_CASE_ID, String.valueOf(caseId));
        return filters;
    }

}
