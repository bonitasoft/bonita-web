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
package org.bonitasoft.test.toolkit.bpm;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.data.DataInstance;
import org.bonitasoft.engine.bpm.data.DataNotFoundException;
import org.bonitasoft.engine.bpm.flownode.ActivityExecutionException;
import org.bonitasoft.engine.bpm.flownode.ActivityInstance;
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.test.toolkit.exception.TestToolkitException;
import org.bonitasoft.test.toolkit.organization.TestToolkitCtx;
import org.bonitasoft.test.toolkit.organization.TestUser;

/**
 * @author Vincent Elcrin
 * 
 */
public class TestHumanTask extends AbstractManualTask {

    private HumanTaskInstance humanTaskInstance;

    private final static int GET_NEXT_NB_ATTEMPT = 10;

    private final static int SLEEP_TIME_MS = 100;

    /**
     * Default Constructor.
     */
    public TestHumanTask(final ActivityInstance activityInstance) {
        assert activityInstance instanceof HumanTaskInstance;
        this.humanTaskInstance = (HumanTaskInstance) activityInstance;
    }

    public HumanTaskInstance getHumanTaskInstance() {
        return this.humanTaskInstance;
    }

    /**
     * @return the processInstance
     */
    private HumanTaskInstance fetchHumanTaskInstance(final APISession apiSession) {
        try {
            return TestProcess.getProcessAPI(apiSession).getHumanTaskInstance(getId());
        } catch (Exception e) {
            throw new TestToolkitException("Can't get humanTask instance for <" + getId() + ">", e);
        }
    }

    public TestHumanTask refreshHumanTaskInstanceInstance() {
        this.humanTaskInstance = fetchHumanTaskInstance(TestToolkitCtx.getInstance().getInitiator().getSession());
        return this;
    }

    public DataInstance getDataInstance(String dataName) {
        try {
            return TestProcess.getProcessAPI(TestToolkitCtx.getInstance().getInitiator().getSession())
                    .getActivityDataInstance(dataName, humanTaskInstance.getId());
        } catch (DataNotFoundException e) {
            throw new TestToolkitException("Unable to find dataInstance " + dataName, e);
        }
    }
    /*
     * (non-Javadoc)
     * @see org.bonitasoft.test.AbstractManualTask#getId()
     */
    @Override
    public long getId() {
        return this.humanTaskInstance.getId();
    }

    /*
     * (non-Javadoc)
     * @see org.bonitasoft.test.toolkit.bpm.AbstractManualTask#getDescription()
     */
    @Override
    public String getDescription() {
        return this.humanTaskInstance.getDescription();
    }

    /*
     * (non-Javadoc)
     * @see org.bonitasoft.test.toolkit.bpm.AbstractManualTask#getName()
     */
    @Override
    public String getName() {
        return this.humanTaskInstance.getName();
    }

    // /////////////////////////////////////////////////////////////////////////////
    // / Assign
    // /////////////////////////////////////////////////////////////////////////////

    private TestHumanTask assignTo(final APISession apiSession, final TestUser user) {
        final ProcessAPI processAPI = TestProcess.getProcessAPI(apiSession);
        try {
            processAPI.assignUserTask(this.humanTaskInstance.getId(), user.getId());
        } catch (final Exception e) {
            throw new TestToolkitException("Can't assgin user", e);
        }
        return this;
    }

    public TestHumanTask assignTo(final TestUser initiator, final TestUser user) {
        assignTo(initiator.getSession(), user);
        return this;
    }

    public TestHumanTask assignTo(final TestUser user) {
        return assignTo(TestToolkitCtx.getInstance().getInitiator(), user);
    }

    // ////////////////////////////////////////////////////////////////////////////
    // / Execute
    // ////////////////////////////////////////////////////////////////////////////

    public TestHumanTask execute(final APISession apiSession) {
        final ProcessAPI processAPI = TestProcess.getProcessAPI(apiSession);
        try {
            processAPI.executeFlowNode(this.humanTaskInstance.getId());
        } catch (final Exception e) {
            throw new TestToolkitException("Can't execute activity <" + this.humanTaskInstance.getId() + ">.", e);
        }
        return this;
    }

    public TestHumanTask execute(final TestUser initiator) {
        return execute(initiator.getSession());
    }

    public TestHumanTask execute() {
        return execute(TestToolkitCtx.getInstance().getInitiator());
    }

    public TestHumanTask archive(final APISession apiSession) {
        try {
            execute(apiSession);
        } catch (final TestToolkitException e) {
            if (!(e.getCause() instanceof ActivityExecutionException)) {
                throw e;
            }
        }
        return this;
    }

    public TestHumanTask archive(final TestUser initiator) {
        return archive(initiator.getSession());
    }

    public TestHumanTask archive() {
        return archive(TestToolkitCtx.getInstance().getInitiator());
    }

    // /////////////////////////////////////////////////////////////////////////////////
    // Convenient method
    // /////////////////////////////////////////////////////////////////////////////////

    /**
     * Wait for human task's state to move to state passed in parameter. Do not execute human task.
     * 
     * @param apiSession
     * @param state
     * @return
     */
    private TestHumanTask waitState(final APISession apiSession, final String state) {
        for (int i = 0; i < GET_NEXT_NB_ATTEMPT; i++) {
            try {
                Thread.sleep(SLEEP_TIME_MS);
            } catch (InterruptedException e) {
                throw new TestToolkitException("Can't get process instance <" + getId() + ">. Interrupted", e);
            }
            refreshHumanTaskInstanceInstance();
            if (getHumanTaskInstance() != null && state.equals(getHumanTaskInstance().getState())) {
                break;
            }
        }
        return this;
    }

    public TestHumanTask waitState(final TestUser initiator, final String state) {
        return waitState(initiator.getSession(), state);
    }

    public TestHumanTask waitState(final String state) {
        return waitState(TestToolkitCtx.getInstance().getInitiator(), state);
    }
}
