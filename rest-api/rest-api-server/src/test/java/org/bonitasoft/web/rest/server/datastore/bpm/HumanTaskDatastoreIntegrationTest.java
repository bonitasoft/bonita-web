package org.bonitasoft.web.rest.server.datastore.bpm;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.engine.bpm.flownode.TaskPriority;
import org.bonitasoft.test.toolkit.bpm.TestHumanTask;
import org.bonitasoft.test.toolkit.bpm.TestProcessFactory;
import org.bonitasoft.test.toolkit.organization.TestUser;
import org.bonitasoft.test.toolkit.organization.TestUserFactory;
import org.bonitasoft.web.rest.model.bpm.flownode.HumanTaskItem;
import org.bonitasoft.web.rest.server.AbstractConsoleTest;
import org.bonitasoft.web.rest.server.datastore.bpm.flownode.HumanTaskDatastore;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.junit.Test;

public class HumanTaskDatastoreIntegrationTest extends AbstractConsoleTest {

    private HumanTaskDatastore humanTaskDatastore;

    private TestHumanTask testHumanTask;

    /*
     * (non-Javadoc)
     * @see org.bonitasoft.console.server.AbstractJUnitWebTest#webTestSetUp()
     */
    @Override
    public void consoleTestSetUp() throws Exception {
        this.testHumanTask = TestProcessFactory.getDefaultHumanTaskProcess()
                .addActor(TestUserFactory.getJohnCarpenter())
                .startCase()
                .getNextHumanTask();

        this.humanTaskDatastore = new HumanTaskDatastore(TestUserFactory.getJohnCarpenter().getSession());
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
    public void changePriorityHumanTaskTest() {

        // Update the priority of the task to highest
        final APIID apiId = APIID.makeAPIID(this.testHumanTask.getId());
        final Map<String, String> attributes = new HashMap<String, String>();
        attributes.put(HumanTaskItem.ATTRIBUTE_PRIORITY, TaskPriority.HIGHEST.name());
        this.humanTaskDatastore.update(apiId, attributes);

        // Search the task
        final Map<String, String> filters = new HashMap<String, String>();
        filters.put(HumanTaskItem.ATTRIBUTE_ID, String.valueOf(this.testHumanTask.getId()));
        final HumanTaskItem humanTaskItem = this.humanTaskDatastore.search(0, 1, null, null, filters)
                .getResults().get(0);

        assertEquals("It's not possible to change the priority for a task",
                this.testHumanTask.getId(), (long) humanTaskItem.getId().toLong());
        assertEquals("It's not possible to change the priority for a task",
                HumanTaskItem.VALUE_PRIORITY_HIGHEST, humanTaskItem.getPriority());
    }

}
