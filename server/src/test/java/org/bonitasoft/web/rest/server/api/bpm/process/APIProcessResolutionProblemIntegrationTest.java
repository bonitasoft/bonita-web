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
package org.bonitasoft.web.rest.server.api.bpm.process;

import junit.framework.Assert;

import org.bonitasoft.test.toolkit.bpm.TestProcess;
import org.bonitasoft.test.toolkit.bpm.TestProcessFactory;
import org.bonitasoft.test.toolkit.organization.TestUser;
import org.bonitasoft.test.toolkit.organization.TestUserFactory;
import org.bonitasoft.web.rest.model.bpm.process.ProcessResolutionProblemItem;
import org.bonitasoft.web.rest.server.AbstractConsoleTest;
import org.bonitasoft.web.rest.server.framework.search.ItemSearchResult;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.common.util.MapUtil;
import org.junit.Test;

/**
 * @author Séverin Moussel
 * 
 */
public class APIProcessResolutionProblemIntegrationTest extends AbstractConsoleTest {

    @Override
    public void consoleTestSetUp() throws Exception {
    }

    @Override
    protected TestUser getInitiator() {
        return TestUserFactory.getJohnCarpenter();
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // UTILS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private APIProcessResolutionProblem getAPI() {
        final APIProcessResolutionProblem api = new APIProcessResolutionProblem();
        api.setCaller(getAPICaller(getInitiator().getSession(), "API/bpm/processResolutionProblem"));
        return api;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // TESTS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    public void testSearchWithResults() {
        final TestProcess process = TestProcessFactory.getDefaultHumanTaskProcess();
        final ItemSearchResult<ProcessResolutionProblemItem> results = getAPI().runSearch(
                0, 100,
                null,
                null,
                MapUtil.asMap(new Arg(ProcessResolutionProblemItem.FILTER_PROCESS_ID, process.getId())),
                null,
                null);

        Assert.assertFalse("No resolution issues found", results.getResults().size() == 0);
        Assert.assertTrue("Wrong number of resolution issues found", results.getResults().size() == 1);
    }

    @Test
    public void testSearchWithoutResults() {
        final TestProcess process = TestProcessFactory.getDefaultHumanTaskProcess();
        process.addActor(getInitiator());

        final ItemSearchResult<ProcessResolutionProblemItem> results = getAPI().runSearch(
                0, 100,
                null,
                null,
                MapUtil.asMap(new Arg(ProcessResolutionProblemItem.FILTER_PROCESS_ID, process.getId())),
                null,
                null);

        Assert.assertTrue("Resolution issues found", results.getResults().size() == 0);
    }

}
