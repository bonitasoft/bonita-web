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
package org.bonitasoft.console.server.api.bpm.flownode;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.console.client.model.bpm.flownode.HiddenUserTaskItem;
import org.bonitasoft.console.client.model.bpm.flownode.HumanTaskItem;
import org.bonitasoft.console.server.AbstractConsoleTest;
import org.bonitasoft.engine.session.InvalidSessionException;
import org.bonitasoft.test.toolkit.bpm.TestHumanTask;
import org.bonitasoft.test.toolkit.bpm.TestProcessFactory;
import org.bonitasoft.test.toolkit.organization.TestUser;
import org.bonitasoft.test.toolkit.organization.TestUserFactory;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Rohart Bastien
 * 
 */
public class APIHiddenUserTaskIntegrationTest extends AbstractConsoleTest {

    private APIHumanTask apiHumanTask;

    private TestHumanTask testHumanTask;

    private APIHiddenUserTask apiHiddenUserTask;

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
        createAPIHumanTask();
        createAPIHiddenUserTask();
    }

    /*
     * (non-Javadoc)
     * @see org.bonitasoft.test.toolkit.AbstractJUnitTest#getInitiator()
     */
    @Override
    protected TestUser getInitiator() {
        return TestUserFactory.getJohnCarpenter();
    }

    /**
     * Test method for a HiddenUserTask
     * 
     * @throws Exception
     */
    @Test
    public void testDeleteHiddenUserTask() throws Exception {
        this.testHumanTask.hide();

        final APIID apiid = APIID.makeAPIID(TestUserFactory.getJohnCarpenter().getId(), this.testHumanTask.getId());
        final List<APIID> apiids = new ArrayList<APIID>();
        apiids.add(apiid);
        this.apiHiddenUserTask.delete(apiids);

        Assert.assertTrue("Can't delete the HiddenUserTask with id=" + this.testHumanTask.getId() + " to the APIHiddenUserTAsk", this.testHumanTask.isPending());
        Assert.assertFalse("Can't delete the HiddenUserTask with id=" + this.testHumanTask.getId() + " to the APIHiddenUserTAsk", this.testHumanTask.isHidden());
    }

    /**
     * Test method for a Hidden a Task for a user and
     * assigned this task by the admin to this user
     * 
     * @throws Exception
     */
    @Test
    public void testUserHideAdminAssign() throws Exception {
        HiddenUserTaskItem hiddenUserTaskItem = new HiddenUserTaskItem();
        hiddenUserTaskItem.setUserId(TestUserFactory.getJohnCarpenter().getId());
        hiddenUserTaskItem.setTaskId(this.testHumanTask.getId());
        hiddenUserTaskItem = this.apiHiddenUserTask.add(hiddenUserTaskItem);

        Assert.assertFalse("Task still appears in pending tasks", this.testHumanTask.isPending());
        Assert.assertTrue("Task has disapeared", this.testHumanTask.isHidden());

        // change assignation
        this.testHumanTask.assignTo(TestUserFactory.getJohnCarpenter());
        Assert.assertFalse("Task is still hidden", this.testHumanTask.isHidden());
        Assert.assertFalse("Task should not be pending", this.testHumanTask.isPending());
        Assert.assertTrue("Task should be assigned",
                TestUserFactory.getJohnCarpenter().getAssignedTasks().contains(this.testHumanTask.getHumanTaskInstance()));
    }

    /**
     * Test method for getting a HiddenUserTaskItem
     * 
     * @throws TaskHidingException
     * @throws InvalidSessionException
     */
    @Test
    public void testGetHiddenUserTaskItem() throws Exception {
        this.testHumanTask.hide();
        final ArrayList<String> deploys = new ArrayList<String>();
        deploys.add(HumanTaskItem.ATTRIBUTE_PROCESS_ID);
        final ArrayList<String> counters = new ArrayList<String>();
        final APIID apiid = APIID.makeAPIID(
                TestUserFactory.getJohnCarpenter().getId(),
                this.testHumanTask.getId());

        final HiddenUserTaskItem hidden = this.apiHiddenUserTask.runGet(apiid, deploys, counters);

        assertEquals(
                "Can't get the HiddenUserTask",
                this.testHumanTask.getId(), (long) hidden.getId().getPartAsAPIID(HiddenUserTaskItem.ATTRIBUTE_TASK_ID).toLong());
    }

    /**
     * Test method for add a UserTask to the APIHiddenHumanTask.
     * 
     * @throws Exception
     */
    @Test
    public void testAddHiddenUserTaskItem() throws Exception {

        // Add the humanTaskItem to the APIHiddenUserTask
        HiddenUserTaskItem hiddenUserTaskItem = new HiddenUserTaskItem();
        hiddenUserTaskItem.setUserId(TestUserFactory.getJohnCarpenter().getId());
        hiddenUserTaskItem.setTaskId(this.testHumanTask.getId());
        hiddenUserTaskItem = this.apiHiddenUserTask.add(hiddenUserTaskItem);

        Assert.assertFalse("Can't hide the UserTask with id=" + this.testHumanTask.getId() + " to the APIHiddenUserTAsk", this.testHumanTask.isPending());
        Assert.assertTrue("Can't hide the UserTask with id=" + this.testHumanTask.getId() + " to the APIHiddenUserTAsk", this.testHumanTask.isHidden());
    }

    /**
     * Initialize APIHumanTask
     * 
     * @throws Exception
     */
    private void createAPIHumanTask() throws Exception {
        this.apiHumanTask = new APIHumanTask();
        this.apiHumanTask.setCaller(getAPICaller(TestUserFactory.getJohnCarpenter().getSession(),
                "API/bpm/humanTask"));
    }

    /**
     * Initialize the APIHiddenUserTask
     * 
     * @throws Exception
     */
    private void createAPIHiddenUserTask() throws Exception {
        this.apiHiddenUserTask = new APIHiddenUserTask();
        this.apiHiddenUserTask.setCaller(getAPICaller(TestUserFactory.getJohnCarpenter().getSession(),
                "API/bpm/hiddenUserTask"));
    }

}
