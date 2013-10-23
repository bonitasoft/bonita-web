package org.bonitasoft.web.rest.server.api.bpm.process;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.bar.BusinessArchive;
import org.bonitasoft.engine.bpm.bar.BusinessArchiveBuilder;
import org.bonitasoft.engine.bpm.bar.BusinessArchiveFactory;
import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfoCriterion;
import org.bonitasoft.engine.bpm.process.impl.ProcessDefinitionBuilder;
import org.bonitasoft.test.toolkit.bpm.TestProcess;
import org.bonitasoft.test.toolkit.bpm.TestProcessFactory;
import org.bonitasoft.test.toolkit.organization.TestUser;
import org.bonitasoft.test.toolkit.organization.TestUserFactory;
import org.bonitasoft.web.rest.model.bpm.process.ProcessItem;
import org.bonitasoft.web.rest.server.AbstractConsoleTest;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.junit.Test;

public class APIProcessIntegrationTest extends AbstractConsoleTest {

    private APIProcess apiProcess;

    @Override
    protected TestUser getInitiator() {
        return TestUserFactory.getJohnCarpenter();
    }

    @Override
    public void consoleTestSetUp() throws Exception {
        this.apiProcess = new APIProcess();
        this.apiProcess.setCaller(getAPICaller(TestUserFactory.getJohnCarpenter().getSession(), "API/bpm/process"));

    }

    /**
     * Add a process uploaded
     * 
     * @throws Exception
     */
    @Test
    public void testAddProcessItem() throws Exception {
        // upload process archive
        final BusinessArchive businessArchive = new BusinessArchiveBuilder().createNewBusinessArchive()
                .setProcessDefinition(new ProcessDefinitionBuilder().createNewInstance("Test process", "1.0").done()).done();
        final File file = writeBarToFolder("addProcessTest", businessArchive);

        // use api to deploy process uploaded
        final ProcessItem item = new ProcessItem();
        item.setAttribute("fileupload", file.getAbsolutePath());
        this.apiProcess.add(item);

        // check the process has been correctly uploaded
        final ProcessAPI processAPI = TenantAPIAccessor.getProcessAPI(TestUserFactory.getJohnCarpenter().getSession());
        assertEquals("Can't add a ProcessItem to APIProcess",
                processAPI.getProcessDeploymentInfos(0, 10, ProcessDeploymentInfoCriterion.DEFAULT).size(), 1);
    }

    /**
     * Update state of an enabled process to disabled
     * 
     * @throws Exception
     */
    @Test
    public void testUpdateProcessItem() throws Exception {
        final APIID processDefinitionId = APIID.makeAPIID(TestProcessFactory.getDefaultHumanTaskProcess()
                .addActor(getInitiator())
                .setEnable(true)
                .getId());

        // assert process is well enabled
        final ProcessAPI processAPI = TenantAPIAccessor.getProcessAPI(TestUserFactory.getJohnCarpenter().getSession());
        final String expectedState = processAPI.getProcessDeploymentInfos(0, 1, ProcessDeploymentInfoCriterion.DEFAULT).get(0).getActivationState().name();
        assertEquals("Process should start enabled", ProcessItem.VALUE_ACTIVATION_STATE_ENABLED, expectedState);

        // use process api to update the state
        final Map<String, String> attributes = new HashMap<String, String>();
        attributes.put(ProcessItem.ATTRIBUTE_ACTIVATION_STATE, ProcessItem.VALUE_ACTIVATION_STATE_DISABLED);
        final ProcessItem processItem = this.apiProcess.update(processDefinitionId, attributes);

        // check the process is disabled (resolved)
        assertEquals("Can't update a processItem with APIProcess <" + processItem.getActivationState() + " - " + ProcessItem.VALUE_ACTIVATION_STATE_DISABLED
                + ">",
                processItem.getActivationState(), ProcessItem.VALUE_ACTIVATION_STATE_DISABLED);
    }

    /**
     * Get a process
     * 
     * @throws Exception
     */
    @Test
    public void testGetProcessItem() throws Exception {
        final APIID processDefinitionId = APIID.makeAPIID(TestProcessFactory.getDefaultHumanTaskProcess()
                .addActor(getInitiator())
                .getId());

        final ArrayList<String> deploys = new ArrayList<String>();
        final ArrayList<String> counters = new ArrayList<String>();

        assertEquals("Can't get a processItem with APIProcess", this.apiProcess.runGet(processDefinitionId, deploys, counters).getName(),
                TestProcessFactory.getDefaultHumanTaskProcess().getProcessDefinition().getName());
        assertEquals("Can't get a processItem with APIProcess", this.apiProcess.runGet(processDefinitionId, deploys, counters).getDescription(),
                TestProcessFactory.getDefaultHumanTaskProcess().getProcessDefinition().getDescription());
    }

    /**
     * Search process by its id
     * 
     * @throws Exception
     */
    @Test
    public void testSearchProcessItemForUser() throws Exception {
        final APIID processDefinitionId = APIID.makeAPIID(TestProcessFactory.getDefaultHumanTaskProcess()
                .addActor(getInitiator())
                .setEnable(true)
                .getId());

        // Set the filters
        final HashMap<String, String> filters = new HashMap<String, String>();
        filters.put(ProcessItem.FILTER_USER_ID, String.valueOf(TestUserFactory.getJohnCarpenter().getId()));

        // Search the ProcessItem
        final ArrayList<String> deploys = new ArrayList<String>();
        final ArrayList<String> counters = new ArrayList<String>();
        final ProcessItem processItem = this.apiProcess.runSearch(0, 10, null, ProcessItem.ATTRIBUTE_DISPLAY_NAME + " ASC", filters, deploys, counters)
                .getResults().get(0);
        assertEquals("Can't search a processItem with APIProcess <" + processDefinitionId + " - " + processItem.getId().toLong() + ">", processDefinitionId,
                processItem.getId().toLong());
    }

    /*
     * Create a temporary file, contain a businessArchive
     * @return File
     * the create temporary file
     * @param String
     * prefix path for the temporary file
     * @param BusinessArchive
     * businessArchive write in the temporary file
     */
    private static File writeBarToFolder(final String barName, final BusinessArchive businessArchive) {
        File tempFile = null;
        try {
            tempFile = File.createTempFile(barName, ".bar");
            tempFile.delete();
            BusinessArchiveFactory.writeBusinessArchiveToFile(businessArchive, tempFile);
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return tempFile;
    }

    /**
     * Get the latest process version
     * 
     * @throws Exception
     */
    @Test
    public void testGetLastProcessVersion() throws Exception {
        // create 3 version of a process
        TestProcess p1 = new TestProcess(TestProcessFactory.getDefaultProcessDefinitionBuilder("multipleVersionsProcess", "aVersion"));
        TestProcess p2 = new TestProcess(TestProcessFactory.getDefaultProcessDefinitionBuilder("multipleVersionsProcess", "aVersion2"));
        TestProcess p3 = new TestProcess(TestProcessFactory.getDefaultProcessDefinitionBuilder("multipleVersionsProcess", "anOtherVersion"));

        // map actor John Carpenter on the created processes, then set enable
        p1.addActor(TestUserFactory.getJohnCarpenter()).setEnable(true);
        p2.addActor(TestUserFactory.getJohnCarpenter()).setEnable(true);
        p3.addActor(TestUserFactory.getJohnCarpenter()).setEnable(true);

        // Set the filters
        final HashMap<String, String> filters = new HashMap<String, String>();
        filters.put(ProcessItem.FILTER_USER_ID, String.valueOf(TestUserFactory.getJohnCarpenter().getId()));
        filters.put(ProcessItem.ATTRIBUTE_DISPLAY_NAME, "multipleVersionsProcess");

        // search the last version of a process
        final List<ProcessItem> resultList = this.apiProcess.runSearch(0, 1, null, ProcessItem.ATTRIBUTE_DEPLOYMENT_DATE + " DESC", filters, null, null)
                .getResults();

        // get the first element
        ProcessItem searchedProcessItem = resultList.get(0);
        assertEquals("multipleVersionsProcess", searchedProcessItem.getDisplayName());
        assertEquals("anOtherVersion", searchedProcessItem.getVersion());

    }

}
