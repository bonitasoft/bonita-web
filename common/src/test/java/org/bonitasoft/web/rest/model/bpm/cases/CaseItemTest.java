/**
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.web.rest.model.bpm.cases;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.bonitasoft.web.rest.model.bpm.flownode.FlowNodeItem;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.junit.Test;

/**
 * @author Celine Souchet
 */
public class CaseItemTest {

    /**
     * Test method for {@link org.bonitasoft.web.rest.model.bpm.cases.CaseItem#getFailedFlowNodes()}.
     */
    @Test
    public final void getFailedFlowNodes_should_return_list_of_failed_flow_nodes_when_exists_on_deploy() {
        // Given
        final FlowNodeItem flowNodeItem = new FlowNodeItem();
        final List<FlowNodeItem> flowNodes = Arrays.asList(flowNodeItem);
        final List<? extends IItem> items = Arrays.asList(flowNodeItem, new CaseItem());
        final CaseItem caseItem = new CaseItem();
        caseItem.setItemsDeploy(CaseItem.COUNTER_FAILED_FLOW_NODES, items);

        // When
        final List<FlowNodeItem> result = caseItem.getFailedFlowNodes();

        // Then
        assertEquals(flowNodes, result);
    }

    /**
     * Test method for {@link org.bonitasoft.web.rest.model.bpm.cases.CaseItem#getFailedFlowNodes()}.
     */
    @Test
    public final void getFailedFlowNodes_should_return_list_of_failed_flow_nodes_when_is_already_build() {
        // Given
        final List<? extends IItem> items = Arrays.asList(new FlowNodeItem());
        final CaseItem caseItem = new CaseItem();
        caseItem.setItemsDeploy(CaseItem.COUNTER_FAILED_FLOW_NODES, items);
        caseItem.getFailedFlowNodes();

        // When
        final List<FlowNodeItem> failedFlowNodes = caseItem.getFailedFlowNodes();

        // Then
        assertEquals(items, failedFlowNodes);
    }

}
