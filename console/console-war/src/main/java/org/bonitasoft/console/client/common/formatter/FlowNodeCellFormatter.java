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
package org.bonitasoft.console.client.common.formatter;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.web.rest.model.bpm.flownode.IFlowNodeItem;
import org.bonitasoft.web.toolkit.client.ui.component.table.formatter.ItemTableCellFormatter;

/**
 * Formatter for archived Display Name
 * Add different CSS Class for the Type
 * 
 * @author Julien MEGE
 */
abstract class FlowNodeCellFormatter extends ItemTableCellFormatter {

    @SuppressWarnings("unused")
    protected String getText() {
        return this.attributeReader.read(this.item);
    }

    @SuppressWarnings("unused")
    protected String getTooltip() {

        if (item instanceof IFlowNodeItem) {
            if (((IFlowNodeItem) item).isHumanTask()) {
                return _("human task");
            } else if (((IFlowNodeItem) item).isCallActivity()) {
                return _("Call activity (launches another process)");
            } else if (((IFlowNodeItem) item).isMultiInsatnceActivity()) {
                return _("Multi-instanciation activity (launches and controls instances execution)");
            } else if (((IFlowNodeItem) item).isLoopActivity()) {
                return _("Loop activity (launches and controls loop execution)");
            }
        }
        return _("Service task (performed automatically)");
    }

}
