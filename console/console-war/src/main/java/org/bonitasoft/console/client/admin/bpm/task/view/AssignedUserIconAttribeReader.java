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
package org.bonitasoft.console.client.admin.bpm.task.view;

import org.bonitasoft.web.rest.model.bpm.flownode.HumanTaskItem;
import org.bonitasoft.web.rest.model.bpm.flownode.IFlowNodeItem;
import org.bonitasoft.web.rest.model.identity.UserItem;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.DeployedAttributeReader;

/**
 * @author Vincent Elcrin
 * 
 *         Assigned user icon attribute read
 * 
 *         Read default automatic task icon for an automatic icon
 *         Read assigned user icon url for any other items
 * 
 */
public class AssignedUserIconAttribeReader extends DeployedAttributeReader {

    /**
     * Default Constructor.
     * 
     * @param attributeToDeploy
     * @param attributeToRead
     */
    public AssignedUserIconAttribeReader() {
        super(HumanTaskItem.ATTRIBUTE_ASSIGNED_USER_ID,
                UserItem.ATTRIBUTE_ICON);
    }

    @Override
    protected String _read(final IItem item) {
        if (item instanceof IFlowNodeItem) {
            if (((IFlowNodeItem) item).isHumanTask()) {
                return super._read(item);
            } else if (((IFlowNodeItem) item).isCallActivity()) {
                return IFlowNodeItem.VALUE_ICON_CALL_ACTIVITY;
            } else if (((IFlowNodeItem) item).isMultiInsatnceActivity()) {
                return IFlowNodeItem.VALUE_ICON_MULTI_INSTANCE_ACTIVITY;
            } else if (((IFlowNodeItem) item).isLoopActivity()) {
                return IFlowNodeItem.VALUE_ICON_LOOP_ACTIVITY;
            }
        }
        return IFlowNodeItem.VALUE_ICON_AUTOMATIC_TASK;
    }

}
