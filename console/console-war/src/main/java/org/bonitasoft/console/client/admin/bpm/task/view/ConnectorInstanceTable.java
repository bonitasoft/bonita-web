/*
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

package org.bonitasoft.console.client.admin.bpm.task.view;

import org.bonitasoft.web.rest.model.bpm.connector.ConnectorInstanceDefinition;
import org.bonitasoft.web.rest.model.bpm.connector.ConnectorInstanceItem;
import org.bonitasoft.web.rest.model.bpm.flownode.IFlowNodeItem;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTable;
import org.bonitasoft.web.toolkit.client.ui.component.table.formatter.ItemTableCellFormatter;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

/**
 * Created by Vincent Elcrin
 * Date: 27/09/13
 * Time: 10:05
 */
public class ConnectorInstanceTable extends ItemTable {

    public ConnectorInstanceTable(final IFlowNodeItem item) {

        super(new JsId("connectors"),
                Definitions.get(ConnectorInstanceDefinition.TOKEN));

        addHiddenFilter(ConnectorInstanceItem.ATTRIBUTE_CONTAINER_ID, item.getId());
        addColumn(ConnectorInstanceItem.ATTRIBUTE_NAME, _("Name"));
        addColumn(ConnectorInstanceItem.ATTRIBUTE_STATE, _("State"));
    }

    public ConnectorInstanceTable addStateCellFormatter(ItemTableCellFormatter formatter) {
        addCellFormatter(ConnectorInstanceItem.ATTRIBUTE_STATE, formatter);
        return this;
    }
}
