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
package org.bonitasoft.console.server.api.bpm.process;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.console.server.AbstractConsoleTest;
import org.bonitasoft.test.toolkit.bpm.TestProcess;
import org.bonitasoft.test.toolkit.bpm.TestProcessFactory;
import org.bonitasoft.test.toolkit.bpm.process.TestProcessConnector;
import org.bonitasoft.test.toolkit.bpm.process.TestProcessConnectorFactory;
import org.bonitasoft.test.toolkit.organization.TestUser;
import org.bonitasoft.test.toolkit.organization.TestUserFactory;
import org.bonitasoft.web.rest.api.model.bpm.process.ProcessConnectorDefinition;
import org.bonitasoft.web.rest.api.model.bpm.process.ProcessConnectorItem;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.server.search.ItemSearchResult;
import org.junit.Test;

/**
 * @author Colin PUY
 * 
 */
public class APIProcessConnectorIntegrationTest extends AbstractConsoleTest {

    private APIProcessConnector apiProcessConnector;

    @Override
    public void consoleTestSetUp() throws Exception {
        apiProcessConnector = new APIProcessConnector();
        apiProcessConnector.setCaller(getAPICaller(getInitiator().getSession(), "API/bpm/processConnector"));
    }

    @Override
    protected TestUser getInitiator() {
        return TestUserFactory.getJohnCarpenter();
    }

    @Test
    public void testSearch() throws Exception {
        TestProcessConnector defaultConnector = TestProcessConnectorFactory.getDefaultConnector();
        TestProcess processWithConnector = TestProcessFactory.createProcessWithConnector(defaultConnector);
        Map<String, String> filters = new HashMap<String, String>();
        filters.put(ProcessConnectorItem.ATTRIBUTE_PROCESS_ID, String.valueOf(processWithConnector.getId()));

        ItemSearchResult<ProcessConnectorItem> search = apiProcessConnector.runSearch(0, 10, "", null, filters, null, null);

        ProcessConnectorItem expectedItem = toProcessConnectorItem(defaultConnector, processWithConnector.getId());
        assertTrue(areEquals(expectedItem, search.getResults().get(0)));
        assertEquals(1L, search.getTotal());
    }

    @Test
    public void testGet() throws Exception {
        TestProcessConnector defaultConnector = TestProcessConnectorFactory.getDefaultConnector();
        TestProcess processWithConnector = TestProcessFactory.createProcessWithConnector(defaultConnector);

        APIID apiid = anApiId(processWithConnector.getId(), defaultConnector.getId(), defaultConnector.getVersion());
        ProcessConnectorItem processConnectorItem = apiProcessConnector.runGet(apiid, null, null);

        ProcessConnectorItem expectedItem = toProcessConnectorItem(defaultConnector, processWithConnector.getId());
        assertTrue(areEquals(expectedItem, processConnectorItem));
    }

    private APIID anApiId(long processId, String connectorId, String connectorVersion) {
        APIID apiid = APIID.makeAPIID(String.valueOf(processId), connectorId, connectorVersion);
        apiid.setItemDefinition(ProcessConnectorDefinition.get());
        return apiid;
    }

    private ProcessConnectorItem toProcessConnectorItem(TestProcessConnector testProcessConnector, long processId) {
        ProcessConnectorItem item = new ProcessConnectorItem();
        item.setName(testProcessConnector.getId());
        item.setVersion(testProcessConnector.getVersion());
        item.setProcessId(processId);
        item.setImplementationName(testProcessConnector.getImplementationId());
        item.setImplementationVersion(testProcessConnector.getVersion());
        item.setClassname(testProcessConnector.getImplementationClassname());
        return item;
    }
}
