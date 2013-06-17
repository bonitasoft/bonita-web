package org.bonitasoft.console.server.api.bpm.cases;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.console.server.AbstractConsoleTest;
import org.bonitasoft.engine.bpm.process.ProcessInstance;
import org.bonitasoft.test.toolkit.bpm.TestCase;
import org.bonitasoft.test.toolkit.bpm.TestProcess;
import org.bonitasoft.test.toolkit.bpm.TestProcessFactory;
import org.bonitasoft.test.toolkit.organization.TestUser;
import org.bonitasoft.test.toolkit.organization.TestUserFactory;
import org.bonitasoft.web.rest.api.model.bpm.cases.CaseItem;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.server.search.ItemSearchResult;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Séverin Moussel
 */
public class APICaseIntegrationTest extends AbstractConsoleTest {

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

    private void assertEquals(final String message, final ProcessInstance engineItem, final CaseItem consoleItem) {

        Assert.assertEquals(message, engineItem.getId(), consoleItem.getId().toLong().longValue());
        Assert.assertEquals(message, engineItem.getLastUpdate(), consoleItem.getLastUpdateDate());
        Assert.assertEquals(message, engineItem.getState(), consoleItem.getState());
        Assert.assertEquals(message, engineItem.getStartDate(), consoleItem.getStartDate());
        Assert.assertEquals(message, engineItem.getStartedBy(), (long) consoleItem.getStartedByUserId().toLong());
        Assert.assertEquals(message, engineItem.getEndDate(), consoleItem.getEndDate());
        Assert.assertEquals(message, engineItem.getProcessDefinitionId(), (long) consoleItem.getProcessId().toLong());
    }

    @Test
    public void testGetCase() {
        final TestCase testCase = TestProcessFactory.getDefaultHumanTaskProcess()
                .addActor(getInitiator())
                .startCase();

        final CaseItem caseItem = getAPICase().runGet(APIID.makeAPIID(testCase.getId()), new ArrayList<String>(), new ArrayList<String>());

        Assert.assertNotNull("Case not found", caseItem);
        assertEquals("Wrong case found", testCase.getProcessInstance(), caseItem);
    }

    /**
     * @return
     */
    private APICase getAPICase() {
        final APICase apiCase = new APICase();
        apiCase.setCaller(getAPICaller(getInitiator().getSession(), "API/bpm/case"));
        return apiCase;
    }

    @Test
    public void testGetCaseWithDeploys() {
        final TestProcess testProcess = TestProcessFactory.getDefaultHumanTaskProcess().addActor(getInitiator());
        final TestCase testCase = testProcess.startCase();

        final CaseItem caseItem = getAPICase().runGet(APIID.makeAPIID(testCase.getId()),
                Arrays.asList(CaseItem.ATTRIBUTE_PROCESS_ID, CaseItem.ATTRIBUTE_STARTED_BY_USER_ID), new ArrayList<String>());

        Assert.assertNotNull("Failed to deploy process", caseItem.getProcess());
        Assert.assertEquals("Wrong process deployed", testCase.getProcessInstance().getName(), caseItem.getProcess().getName());

        Assert.assertNotNull("Failed to deploy intiator user", caseItem.getStartedByUserId());
        Assert.assertEquals("Wrong process deployed", getInitiator().getUserName(), caseItem.getStartedByUser().getUserName());
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // SEARCH
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Init cases for search
     */
    private void initCasesForSearch() {
        final TestProcess testProcess = TestProcessFactory.getDefaultHumanTaskProcess();
        testProcess.addActor(TestUserFactory.getMrSpechar());

        // 13 cases for current User and 1 for another user
        testProcess.startCases(13);
        testProcess.startCase(TestUserFactory.getMrSpechar());

        // Another process
        final TestProcess testProcess2 = TestProcessFactory.getHumanTaskProcess("SecondProcess");
        testProcess2.addActor(getInitiator());

        // 1 case for current User
        testProcess2.startCase();
    }

    /**
     * Test search for user (Initiator)
     */
    @Test
    public void testSearchByInitiator() throws Exception {

        initCasesForSearch();

        // Search for current User
        final Map<String, String> filters = new HashMap<String, String>();
        filters.put(CaseItem.ATTRIBUTE_STARTED_BY_USER_ID, String.valueOf(getInitiator().getId()));

        final ItemSearchResult<CaseItem> caseItems = getAPICase().runSearch(0, 10, null, null, filters, null, null);

        checkSearchResults(caseItems, 10, 14);
    }

    /**
     * Test search for user, with only worked on cases
     */
    @Test
    public void testSearchUserWorkedOn() {
        initCasesForSearch();

        // Search for current User
        final Map<String, String> filters = new HashMap<String, String>();
        filters.put(CaseItem.FILTER_USER_ID, String.valueOf(getInitiator().getId()));

        final ItemSearchResult<CaseItem> caseItems = getAPICase().runSearch(0, 10, null, null, filters, null, null);

        checkSearchResults(caseItems, 10, 14);
    }

    /**
     * Test search for user by process
     */
    @Test
    public void testSearchByProcess() throws Exception {

        initCasesForSearch();

        final TestProcess testProcess3 = TestProcessFactory.getHumanTaskProcess("ThirdProcess");
        testProcess3.addActor(getInitiator());
        testProcess3.startCases(5);

        // Search for current User
        final Map<String, String> filters = new HashMap<String, String>();
        filters.put(CaseItem.ATTRIBUTE_PROCESS_ID, String.valueOf(testProcess3.getId()));

        final ItemSearchResult<CaseItem> caseItems = getAPICase().runSearch(0, 3, null, null, filters, null, null);

        checkSearchResults(caseItems, 3, 5);

    }

    /**
     * Admin test cases
     */
    @Test
    public void testSearchAdministrator() throws Exception {

        initCasesForSearch();

        final ItemSearchResult<CaseItem> caseItems = getAPICase().runSearch(0, 10, null, null, null, null, null);

        // On more process (testProcess3)
        checkSearchResults(caseItems, 10, 15);
    }

    /**
     * Process Owner test cases
     */
    @Test
    public void testSearchProcessOwner() throws Exception {

        initCasesForSearch();

        final TestProcess testProcess3 = TestProcessFactory.getHumanTaskProcess("ThirdProcess");
        testProcess3.addActor(getInitiator());
        testProcess3.addSupervisor(TestUserFactory.getRidleyScott());
        testProcess3.startCases(5);

        // Search for current User
        final Map<String, String> filters = new HashMap<String, String>();
        filters.put(CaseItem.FILTER_SUPERVISOR_ID, String.valueOf(TestUserFactory.getRidleyScott().getId()));

        final ItemSearchResult<CaseItem> caseItems = getAPICase().runSearch(0, 10, null, null, filters, null, null);

        checkSearchResults(caseItems, 10, 5);
    }
    
    /**
     * Process Owner test cases
     */
    public void testSearchTeamManager() throws Exception {

        Map<String, TestUser> managedList = TestUserFactory.getManagedUsers(1);

        final TestProcess testProcess4 = TestProcessFactory.getHumanTaskProcess("ProcessTeamManager");
        testProcess4.addActor(managedList.get("managed.user0"));
        testProcess4.startCases(25);
        
        final TestProcess testProcess5 = TestProcessFactory.getHumanTaskProcess("Process five");
        testProcess5.addActor(TestUserFactory.getRidleyScott());
        testProcess5.startCases(10);

        // Search for current User
        final Map<String, String> filters = new HashMap<String, String>();
        filters.put(CaseItem.FILTER_TEAM_MANAGER_ID, String.valueOf(TestUserFactory.getTeamManagerUser().getId()));

        final ItemSearchResult<CaseItem> caseItems = getAPICase().runSearch(0, 100, null, null, filters, null, null);
        checkSearchResults(caseItems, 100, 25);
    }

    /**
     * @param caseItems
     */
    private void checkSearchResults(final ItemSearchResult<CaseItem> caseItems, final int nbResultsByPageExpected, final int nbTotalResultsExpected) {
        Assert.assertTrue("Empty search results", caseItems.getLength() > 0);
        Assert.assertTrue("Wrong page size", caseItems.getLength() == nbResultsByPageExpected);
        Assert.assertTrue("Wrong Total size", caseItems.getTotal() == nbTotalResultsExpected);
    }
}
