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

import java.util.ArrayList;
import java.util.Arrays;

import org.bonitasoft.engine.bpm.process.ArchivedProcessInstance;
import org.bonitasoft.test.toolkit.bpm.TestCase;
import org.bonitasoft.test.toolkit.bpm.TestProcessFactory;
import org.bonitasoft.test.toolkit.organization.TestUser;
import org.bonitasoft.test.toolkit.organization.TestUserFactory;
import org.bonitasoft.web.rest.model.bpm.cases.ArchivedCaseItem;
import org.bonitasoft.web.rest.server.AbstractConsoleTest;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Séverin Moussel
 * 
 */
public class APIArchivedCaseIntegrationTest extends AbstractConsoleTest {

    /**
     * @return
     */
    private APIArchivedCase getAPIArchivedCase() {
        final APIArchivedCase api = new APIArchivedCase();
        api.setCaller(getAPICaller(getInitiator().getSession(), "API/bpm/archivedCase"));
        return api;
    }

    @Override
    public void consoleTestSetUp() throws Exception {
    }

    @Override
    protected TestUser getInitiator() {
        return TestUserFactory.getJohnCarpenter();
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // GET
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @return
     */
    private TestCase initArchivedCaseForGet() {
        final TestCase testArchivedCase = TestProcessFactory.getDefaultHumanTaskProcess()
                .addActor(getInitiator())
                .startCase();

        testArchivedCase.getNextHumanTask().assignTo(getInitiator()).archive();

        return testArchivedCase;
    }

    private void assertEquals(final String message, final ArchivedProcessInstance engineItem, final ArchivedCaseItem consoleItem) {
        Assert.assertEquals(message, engineItem.getId(), consoleItem.getId().toLong().longValue());
        Assert.assertEquals(message, engineItem.getLastUpdate().toString(), consoleItem.getLastUpdateDate().toString());
        Assert.assertEquals(message, engineItem.getState(), consoleItem.getState());
        Assert.assertEquals(message, engineItem.getStartDate(), consoleItem.getStartDate());
        Assert.assertEquals(message, engineItem.getStartedBy(), (long) consoleItem.getStartedByUserId().toLong());
        Assert.assertEquals(message, engineItem.getEndDate(), consoleItem.getEndDate());
        Assert.assertEquals(message, engineItem.getProcessDefinitionId(), (long) consoleItem.getProcessId().toLong());
        Assert.assertNotNull(message, consoleItem.getArchivedDate());

    }

    @Test
    public void testGetArchivedCase() {
        final TestCase testCase = initArchivedCaseForGet();
        final ArchivedProcessInstance archivedProcessInstance = testCase.getArchive();

        final ArchivedCaseItem caseItem = getAPIArchivedCase().runGet(APIID.makeAPIID(archivedProcessInstance.getId()), new ArrayList<String>(),
                new ArrayList<String>());

        Assert.assertNotNull("ArchivedCase not found", caseItem);

        assertEquals("Wrong case found" + ". Expected=" + archivedProcessInstance + ". Found=" + caseItem.toString(), archivedProcessInstance, caseItem);
    }

    @Test
    public void testGetArchivedCaseWithDeploys() {
        final TestCase testCase = initArchivedCaseForGet();
        final ArchivedProcessInstance archivedProcessInstance = testCase.getArchive();

        final ArchivedCaseItem caseItem = getAPIArchivedCase().runGet(APIID.makeAPIID(archivedProcessInstance.getId()),
                Arrays.asList(ArchivedCaseItem.ATTRIBUTE_PROCESS_ID, ArchivedCaseItem.ATTRIBUTE_STARTED_BY_USER_ID), new ArrayList<String>());

        Assert.assertNotNull("Failed to deploy process", caseItem.getProcess());
        Assert.assertEquals("Wrong process deployed", testCase.getProcessInstance().getName(), caseItem.getProcess().getName());

        Assert.assertNotNull("Failed to deploy intiator user", caseItem.getStartedByUserId());
        Assert.assertEquals("Wrong process deployed", getInitiator().getUserName(), caseItem.getStartedByUser().getUserName());
    }
}
