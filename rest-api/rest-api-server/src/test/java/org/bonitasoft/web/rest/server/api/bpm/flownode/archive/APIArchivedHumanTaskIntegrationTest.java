package org.bonitasoft.web.rest.server.api.bpm.flownode.archive;

import static org.bonitasoft.web.toolkit.client.data.APIID.makeAPIID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.flownode.ActivityInstanceCriterion;
import org.bonitasoft.engine.bpm.flownode.ArchivedActivityInstanceSearchDescriptor;
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance;
import org.bonitasoft.engine.bpm.process.ProcessInstance;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.test.toolkit.bpm.TestProcess;
import org.bonitasoft.test.toolkit.bpm.TestProcessFactory;
import org.bonitasoft.test.toolkit.organization.TestUser;
import org.bonitasoft.test.toolkit.organization.TestUserFactory;
import org.bonitasoft.web.rest.model.bpm.flownode.ArchivedHumanTaskItem;
import org.bonitasoft.web.rest.model.bpm.flownode.HumanTaskItem;
import org.bonitasoft.web.rest.server.AbstractConsoleTest;
import org.bonitasoft.web.rest.server.WaitUntil;
import org.bonitasoft.web.rest.server.framework.search.ItemSearchResult;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.common.util.MapUtil;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.junit.Assert;
import org.junit.Test;

/**
 * FIXME to be refactored using much more test-toolkit
 */
public class APIArchivedHumanTaskIntegrationTest extends AbstractConsoleTest {

    private APIArchivedHumanTask apiArchivedHumanTask;

    @Override
    public void consoleTestSetUp() throws Exception {
        this.apiArchivedHumanTask = new APIArchivedHumanTask();
        this.apiArchivedHumanTask.setCaller(getAPICaller(getInitiator().getSession(), "API/bpm/archivedHumanTask"));
    }

    @Override
    protected TestUser getInitiator() {
        return TestUserFactory.getJohnCarpenter();
    }

    private HumanTaskInstance initArchivedHumanTaskInstance() throws Exception {
        TestProcess defaultHumanTaskProcess = TestProcessFactory.getDefaultHumanTaskProcess();
        defaultHumanTaskProcess.addActor(getInitiator());
        ProcessInstance processInstance = defaultHumanTaskProcess.startCase(getInitiator()).getProcessInstance();
        
        waitPendingHumanTask();
        
        // Retrieve a humanTaskInstance
        HumanTaskInstance humanTaskInstance = getProcessAPI().getPendingHumanTaskInstances(getInitiator().getId(), 0, 10, null).get(0);
        getProcessAPI().assignUserTask(humanTaskInstance.getId(), getInitiator().getId());

        waitAssignedHumanTask();

        getProcessAPI().executeFlowNode(humanTaskInstance.getId());

        waitArchivedActivityInstance(processInstance.getId());
        
        return humanTaskInstance;
    }

    private ProcessAPI getProcessAPI() throws Exception {
        return TenantAPIAccessor.getProcessAPI(getInitiator().getSession());
    }

    private ArrayList<String> getProcessIdDeploy() {
        final ArrayList<String> deploys = new ArrayList<String>();
        deploys.add(HumanTaskItem.ATTRIBUTE_PROCESS_ID);
        return deploys;
    }

    private HashMap<String, String> getNameFilter(HumanTaskInstance humanTaskInstance) {
        final HashMap<String, String> filters = new HashMap<String, String>();
        filters.put(ArchivedHumanTaskItem.ATTRIBUTE_NAME, humanTaskInstance.getName());
        return filters;
    }

    private Map<String, String> buildArchivedHumanTaskStateCompletedForCaseIdFilter(APIID caseId) {
        return MapUtil.asMap(new Arg(ArchivedHumanTaskItem.ATTRIBUTE_CASE_ID, caseId));
    }

    /**
     * Wait the process contain PendingHumanTaskInstance
     */
    private void waitPendingHumanTask() throws Exception {
        Assert.assertTrue("no pending task instances are found", new WaitUntil(50, 1000) {
    
            @Override
            protected boolean check() throws Exception {
                return getProcessAPI().getPendingHumanTaskInstances(getInitiator().getId(), 0, 10, null).size() >= 1;
            }
        }.waitUntil());
    }

    private void waitAssignedHumanTask() throws Exception {
        Assert.assertTrue("Human task hasnt been assign", new WaitUntil(50, 3000) {
    
            @Override
            protected boolean check() throws Exception {
                return getProcessAPI().getAssignedHumanTaskInstances(getInitiator().getId(), 0, 10,
                        ActivityInstanceCriterion.DEFAULT).size() >= 1;
            }
        }.waitUntil());
    }

    /**
     * Wait the process contain ArchivedHumanTaskInstance
     */
    private void waitArchivedActivityInstance(final long processInstanceId) throws Exception {
        Assert.assertTrue("no archived task instances are found", new WaitUntil(50, 3000) {
    
            @Override
            protected boolean check() throws Exception {
                SearchOptionsBuilder builder = new SearchOptionsBuilder(0, 10);
                builder.filter(ArchivedActivityInstanceSearchDescriptor.ROOT_PROCESS_INSTANCE_ID, processInstanceId);
                return getProcessAPI().searchArchivedActivities(builder.done()).getCount() > 0;
            }
        }.waitUntil());
    }

    @Test
    public void testGetArchivedHumanTask() throws Exception {
        HumanTaskInstance humanTaskInstance = initArchivedHumanTaskInstance();
        ArrayList<String> deploys = getProcessIdDeploy();
        
        ArchivedHumanTaskItem archivedHumanTaskItem = 
                apiArchivedHumanTask.runGet(makeAPIID(humanTaskInstance.getId()), deploys, new ArrayList<String>());
        
        assertEquals("Can't get the good archivedTaskItem", archivedHumanTaskItem.getName(), humanTaskInstance.getName());
    }

    @Test
    public void testSearchArchivedHumanTask() throws Exception {
        HumanTaskInstance humanTaskInstance = initArchivedHumanTaskInstance();
        ArrayList<String> deploys = getProcessIdDeploy();
        HashMap<String, String> filters = getNameFilter(humanTaskInstance);

        ArchivedHumanTaskItem archivedHumanTaskItem = apiArchivedHumanTask.runSearch(0, 1, null, null,
                filters, deploys, new ArrayList<String>()).getResults().get(0);
        
        assertNotNull("Can't find the good archivedTaskItem", archivedHumanTaskItem);
    }

    @Test
    public void testGetDatastore() {
        assertNotNull("Can't get the Datastore", this.apiArchivedHumanTask.getDefaultDatastore());
    }
    
    @Test
    // FIXME : add an assertion
    public void archivedHumanTasksCanBeSortedByReachedStateDate() throws Exception {
        Map<String, String> filters = buildArchivedHumanTaskStateCompletedForCaseIdFilter(makeAPIID(1L));
        String orders = ArchivedHumanTaskItem.ATTRIBUTE_REACHED_STATE_DATE + " DESC";
        
        ItemSearchResult<ArchivedHumanTaskItem> search = apiArchivedHumanTask.runSearch(0, 1, null, orders, filters, null, null);
        
//        assertEquals(search.getResults().get(0).getSourceObjectId(), archive.getId());
    }
}
