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
package org.bonitasoft.test.toolkit;

import java.util.List;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.flownode.ActivityInstanceCriterion;
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance;
import org.bonitasoft.engine.exception.SearchException;
import org.bonitasoft.engine.search.SearchOptions;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.session.InvalidSessionException;
import org.bonitasoft.test.toolkit.exception.TestToolkitException;
import org.bonitasoft.test.toolkit.organization.TestToolkitCtx;

/**
 * @author Vincent Elcrin
 * 
 */
public final class TestToolkitUtils {

    private static class Util {

        private static TestToolkitUtils instance;

        public static TestToolkitUtils getInstance() {
            if (instance == null) {
                instance = new TestToolkitUtils();
            }

            return instance;
        }
    }

    private TestToolkitUtils() {
    }

    /**
     * Singleton
     */
    public static TestToolkitUtils getInstance() {
        return Util.getInstance();
    }

    private ProcessAPI getProcessAPI(final APISession apiSession) {
        ProcessAPI processAPI = null;
        try {
            processAPI = TenantAPIAccessor.getProcessAPI(apiSession);
        } catch (Exception e) {
            throw new TestToolkitException("Can't get process API.", e);
        }
        return processAPI;
    }

    // ////////////////////////////////////////////////////////////////////////////
    // / Search
    // ////////////////////////////////////////////////////////////////////////////

    /**
     * Search pending tasks for user defined by the api session
     * 
     * @param apiSession
     * @param pageIndex
     * @param numberOfResults
     * @return
     */
    public List<HumanTaskInstance> searchPendingTasksForUser(final APISession apiSession, int pageIndex, int numberOfResults) {
        final ProcessAPI processAPI = getProcessAPI(apiSession);

        SearchOptionsBuilder searchBuilder = new SearchOptionsBuilder(pageIndex, numberOfResults);
        SearchOptions searchOptions = searchBuilder.done();

        List<HumanTaskInstance> result;
        try {
            result = processAPI.searchPendingTasksForUser(apiSession.getUserId(), searchOptions)
                    .getResult();
        } catch (Exception e) {
            throw new TestToolkitException("Can't search pending tasks for user.", e);
        }

        return result;
    }

    /**
     * Search pending tasks for the initiator
     * 
     * @param pageIndex
     * @param numberOfResults
     * @return
     */
    public List<HumanTaskInstance> searchPendingTasksForUser(int pageIndex, int numberOfResults) {
        return searchPendingTasksForUser(TestToolkitCtx.getInstance().getInitiator().getSession(), pageIndex, numberOfResults);
    }

    /**
     * Get tasks assigned to the user defined by api session
     * 
     * @param apiSession
     * @param pageIndex
     * @param numberPerPage
     * @param criterion
     * @return
     */
    public List<HumanTaskInstance> getAssignedHumanTaskInstances(final APISession apiSession, int pageIndex, int numberPerPage,
            ActivityInstanceCriterion criterion) {
        final ProcessAPI processAPI = getProcessAPI(apiSession);

        List<HumanTaskInstance> result;
        try {
            result = processAPI.getAssignedHumanTaskInstances(apiSession.getUserId(), pageIndex, numberPerPage,
                    criterion);
        } catch (Exception e) {
            throw new TestToolkitException("Can't search task assigned to <" + apiSession.getUserId() + ">", e);
        }

        return result;
    }

    /**
     * Get tasks assigned to the initiator
     * 
     * @param pageIndex
     * @param numberPerPage
     * @param criterion
     * @return
     */
    public List<HumanTaskInstance> getAssignedHumanTaskInstances(int pageIndex, int numberPerPage,
            ActivityInstanceCriterion criterion) {
        return getAssignedHumanTaskInstances(TestToolkitCtx.getInstance().getInitiator().getSession(), pageIndex, numberPerPage, criterion);
    }

}
