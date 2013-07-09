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
package bos.bonitasoft.test.toolkit;

import java.util.List;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.flownode.ActivityInstanceNotFoundException;
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance;
import org.bonitasoft.engine.bpm.process.ArchivedProcessInstance;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.test.toolkit.EngineSetup;
import org.bonitasoft.test.toolkit.bpm.TestCase;
import org.bonitasoft.test.toolkit.bpm.TestHumanTask;
import org.bonitasoft.test.toolkit.bpm.TestProcessFactory;
import org.bonitasoft.test.toolkit.organization.TestToolkitCtx;
import org.bonitasoft.test.toolkit.organization.TestUserFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Vincent Elcrin
 * 
 */
public class TestProcessValidation extends EngineSetup {

    private TestCase testCase;

    @Before
    public void setUp() throws Exception {
        TestToolkitCtx.getInstance().setInitiator(TestUserFactory.getJohnCarpenter());

        testCase = TestProcessFactory.getDefaultHumanTaskProcess()
                .addActor(TestUserFactory.getJohnCarpenter())
                .startCase();
    }

    @After
    public void tearDown() throws Exception {
        TestToolkitCtx.getInstance().clearSession();
    }

    @Test
    public void testArchivedTask() throws Exception {
        final TestHumanTask testHumanTask = testCase.getNextHumanTask();
        testHumanTask.assignTo(TestUserFactory.getJohnCarpenter());

        testHumanTask.archive();
        final ProcessAPI processAPI = TenantAPIAccessor.getProcessAPI(TestUserFactory.getJohnCarpenter().getSession());

        try {
            // Assert.assertEquals(testHumanTask.getId(), processAPI.getArchivedActivityInstance(testHumanTask.getId()).getId());
            processAPI.getArchivedActivityInstance(testHumanTask.getId());
        } catch (final ActivityInstanceNotFoundException e) {
            Assert.fail("Failed to archive instance");
        }
    }

    public void testAssignedTask(final int testNumber) throws Exception {
        testCase.getNextHumanTask().assignTo(TestUserFactory.getJohnCarpenter());

        final ProcessAPI processAPI = TenantAPIAccessor.getProcessAPI(TestUserFactory.getRidleyScott().getSession());
        final SearchOptionsBuilder builder = new SearchOptionsBuilder(0, 1);
        final SearchResult<HumanTaskInstance> result = processAPI.searchHumanTaskInstances(builder.done());

        Assert.assertEquals(testNumber + " failed", 1, result.getResult().size());
        Assert.assertEquals(testNumber + " failed", TestUserFactory.getJohnCarpenter().getId(), result.getResult().get(0).getAssigneeId());
    }

    /**
     * Test assignation on 10 set up tear down process.
     * 
     * @throws Exception
     */
    @Test
    public void testAssignedTask1() throws Exception {
        testAssignedTask(1);
    }

    @Test
    public void testAssignedTask2() throws Exception {
        testAssignedTask(2);
    }

    @Test
    public void testAssignedTask3() throws Exception {
        testAssignedTask(3);
    }

    @Test
    public void testAssignedTask4() throws Exception {
        testAssignedTask(4);
    }

    @Test
    public void testAssignedTask5() throws Exception {
        testAssignedTask(5);
    }

    @Test
    public void testAssignedTask6() throws Exception {
        testAssignedTask(6);
    }

    @Test
    public void testAssignedTask7() throws Exception {
        testAssignedTask(7);
    }

    @Test
    public void testAssignedTask8() throws Exception {
        testAssignedTask(8);
    }

    @Test
    public void testAssignedTask9() throws Exception {
        testAssignedTask(9);
    }

    @Test
    public void testAssignedTask10() throws Exception {
        testAssignedTask(10);
    }

    @Test
    public void testArchivedCase() throws Exception {
        testCase.getNextHumanTask().assignTo(TestUserFactory.getJohnCarpenter());
        testCase.execute()
                .execute()
                .execute();

        final ProcessAPI processAPI = TenantAPIAccessor.getProcessAPI(TestUserFactory.getJohnCarpenter().getSession());
        final List<ArchivedProcessInstance> archivedCaseList = processAPI.getArchivedProcessInstances(testCase.getId(), 0, 10);
        Assert.assertEquals(3, archivedCaseList.size()); // engine archive an instance every time this instance change state (initializing, started, completed)
    }

    @Test
    public void testProcessWithDocumentAttached() throws Exception {
        TestProcessFactory.getProcessWithDocumentAttached()
                .addActor(TestUserFactory.getJohnCarpenter())
                .startCase();
    }
}
