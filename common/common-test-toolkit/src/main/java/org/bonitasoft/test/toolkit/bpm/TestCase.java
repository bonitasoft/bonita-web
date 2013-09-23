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
import org.bonitasoft.engine.bpm.flownode.ActivityStates;
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance;
import org.bonitasoft.engine.bpm.process.ArchivedProcessInstance;
import org.bonitasoft.engine.bpm.process.ProcessInstance;
import org.bonitasoft.engine.exception.SearchException;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.session.InvalidSessionException;
import org.bonitasoft.test.toolkit.exception.NextActivityIsNotAllowedStateException;
import org.bonitasoft.test.toolkit.exception.NoActivityLeftException;
import org.bonitasoft.test.toolkit.exception.TestToolkitException;
import org.bonitasoft.test.toolkit.organization.TestToolkitCtx;
import org.bonitasoft.test.toolkit.organization.TestUser;

/**
 * @author Vincent Elcrin
 * 
 */
public class TestCase {

    private ProcessInstance processInstance;

    private final static int GET_NEXT_NB_ATTEMPT = 10;

    private final static int SLEEP_TIME_MS = 100;

    public final static String READY_STATE = "started";

    public TestCase(ProcessInstance instance) {
        this.processInstance = instance;
    }

    /**
     * Wait until the process return the state in parameter
     * 
     * @param apiSession
     * @param state
     * @throws Exception
     */
    public void waitProcessState(final APISession apiSession, final String state) {
        final ProcessAPI processAPI = TestProcess.getProcessAPI(apiSession);
        for (int i = 0; i < GET_NEXT_NB_ATTEMPT; i++) {
            ProcessInstance instance;
            try {
                instance = processAPI.getProcessInstance(this.processInstance.getId());
                if (instance != null && state.equals(instance.getState())) {
                    break;
                }
                Thread.sleep(SLEEP_TIME_MS);
            } catch (final Exception e) {
                throw new TestToolkitException("Can't get process instance <" + this.processInstance.getId() + ">.", e);
            }
        }
    }

    /**
     * Search and get next human task
     * 
     * @param apiSession
     * @return
     * @throws Exception
     */
    public TestHumanTask getNextHumanTask(final APISession apiSession) {
        final ProcessAPI processAPI = TestProcess.getProcessAPI(apiSession);
        final SearchOptionsBuilder searchOptBuilder = new SearchOptionsBuilder(0, 1);

        /**
         * Get next workable human task. (e.g not in initialization state)
         */
        HumanTaskInstance humanTask = null;
        SearchResult<HumanTaskInstance> result = null;
        for (int i = 0; i < GET_NEXT_NB_ATTEMPT; i++) {
            try {
                result = processAPI.searchHumanTaskInstances(searchOptBuilder.done());
                if (!result.getResult().isEmpty() && isAllowedState(result.getResult().get(0))) {
                    humanTask = result.getResult().get(0);
                    break;
                }
                Thread.sleep(SLEEP_TIME_MS);
            } catch (final InvalidSessionException e) {
                throw new TestToolkitException("Can't search human task instances. Invalid session", e);
            } catch (final SearchException e) {
                throw new TestToolkitException("Can't search human task instances", e);
            } catch (final InterruptedException e) {
                throw new TestToolkitException("Interrupted during searching process", e);
            }
        }

        if (humanTask != null) {
            return new TestHumanTask(humanTask);
        } else {
            if (result != null && result.getResult().size() > 0) {
                throw new NextActivityIsNotAllowedStateException(result.getResult().get(0));
            } else {
                throw new NoActivityLeftException();
            }
        }
    }

    /**
     * Check if state is allowed to be returned
     * 
     * @param humanTask
     * @return
     */
    private boolean isAllowedState(final HumanTaskInstance humanTask) {
        if (humanTask != null && !ActivityStates.INITIALIZING_STATE.equals(humanTask.getState())) {
            return true;
        } else {
            return false;
        }
    }

    public TestHumanTask getNextHumanTask() {
        return getNextHumanTask(TestToolkitCtx.getInstance().getInitiator().getSession());
    }

    /**
     * @return the processInstance
     */
    private ProcessInstance fetchProcessInstance(final APISession apiSession) {
        final ProcessAPI processAPI = TestProcess.getProcessAPI(apiSession);
        try {
            return processAPI.getProcessInstance(getId());
        } catch (Exception e) {
            throw new TestToolkitException("Can't get process instance for <" + getId() + ">. Not found", e);
        }
    }

    public TestCase refreshProcessInstance() {
        this.processInstance = fetchProcessInstance(TestToolkitCtx.getInstance().getInitiator().getSession());
        return this;
    }

    public ProcessInstance getProcessInstance() {
        return this.processInstance;
    }

    private ArchivedProcessInstance getArchive(final APISession apiSession) {
        final ProcessAPI processAPI = TestProcess.getProcessAPI(apiSession);
        try {
            return processAPI.getArchivedProcessInstance(getId());
        } catch (Exception e) {
            throw new TestToolkitException("Can't get process instance archived for <" + getId() + ">", e);
        }
    }

    public ArchivedProcessInstance getArchive(TestUser initiator) {
        return getArchive(initiator.getSession());
    }

    public ArchivedProcessInstance getArchive() {
        return getArchive(TestToolkitCtx.getInstance().getInitiator());
    }
    
    public void archive() throws InterruptedException {
        TestUser user = TestToolkitCtx.getInstance().getInitiator();
        APISession session = user.getSession();
        try {
            while (true) {
                final TestHumanTask nextActivityInstance = getNextHumanTask(session);
                if (nextActivityInstance != null) {
                    nextActivityInstance.assignTo(user).execute(session);
                }
                Thread.sleep(SLEEP_TIME_MS);
            }
        } catch (NoActivityLeftException e) {
            // no more activity, finished
        }
    }

    public long getId() {
        return this.processInstance.getId();
    }

    // ///////////////////////////////////////////////////////////////////
    // / Execution
    // ///////////////////////////////////////////////////////////////////

    public TestCase execute(final APISession apiSession) {
        try {
            final TestHumanTask nextActivityInstance = getNextHumanTask(apiSession);
            if (nextActivityInstance != null) {
                nextActivityInstance.execute(apiSession);
            }
        } catch (final NoActivityLeftException e) {
            // there were no activity in the process
        }

        return this;
    }

    public TestCase execute(final TestUser user) {
        return execute(user.getSession());
    }

    public TestCase execute() throws Exception {
        return execute(TestToolkitCtx.getInstance().getInitiator());
    }

    // ///////////////////////////////////////////////////////////////////
    // / Comments
    // ///////////////////////////////////////////////////////////////////

    private void addComment(final APISession apiSession, final String content) {
        final ProcessAPI processAPI = TestProcess.getProcessAPI(apiSession);
        try {
            processAPI.addComment(this.processInstance.getId(), content);
        } catch (final Exception e) {
            throw new TestToolkitException("Can't add comment to <" + this.processInstance.getId() + ">", e);
        }
    }

    private void addComment(final TestUser initiator, final String content) {
        addComment(initiator.getSession(), content);
    }

    public void addComments(final TestUser initiator, final int nbOfComments, final String content) {
        for (int i = 0; i < nbOfComments; i++) {
            addComment(initiator, content + i);
        }
    }

    public void addComment(String content) {
        addComment(TestToolkitCtx.getInstance().getInitiator(), content);
    }
}
