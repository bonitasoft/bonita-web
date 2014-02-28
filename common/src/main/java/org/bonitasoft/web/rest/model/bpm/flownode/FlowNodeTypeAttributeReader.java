/**
 * Copyright (C) 2013 BonitaSoft S.A.
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
package org.bonitasoft.web.rest.model.bpm.flownode;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.AbstractAttributeReader;

/**
 * @author Anthony Birembaut
 * 
 */
public class FlowNodeTypeAttributeReader extends AbstractAttributeReader {

    public FlowNodeTypeAttributeReader() {
        super(IFlowNodeItem.ATTRIBUTE_TYPE);
    }

    @Override
    protected String _read(final IItem item) {
        if (item instanceof IFlowNodeItem) {
            if (((IFlowNodeItem) item).isHumanTask()) {
                className = "human_task";
                return _("Human task");
            } else if (((IFlowNodeItem) item).isCallActivity()) {
                className = "call_activity_task";
                return _("Call activity");
            } else if (((IFlowNodeItem) item).isMultiInsatnceActivity()) {
                className = "multi_instance_task";
                return _("Multi-instanciation activity");
            } else if (((IFlowNodeItem) item).isLoopActivity()) {
                className = "loop_task";
                return _("Loop activity");
            } else if (((IFlowNodeItem) item).isAutomaticTask()) {
                className = "automatic_task";
                return _("Service task");
            }
        }
        className = "automatic_task";
        return ((IFlowNodeItem) item).getType();
    }

}
