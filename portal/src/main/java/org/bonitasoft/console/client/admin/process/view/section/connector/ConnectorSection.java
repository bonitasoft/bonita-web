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
package org.bonitasoft.console.client.admin.process.view.section.connector;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.console.client.admin.process.view.section.configuration.ConfigurationState;
import org.bonitasoft.console.client.admin.process.view.section.configuration.ConfigurationStateText;
import org.bonitasoft.console.client.common.event.handler.HideComponentOnEmptyTableHandler;
import org.bonitasoft.web.rest.model.bpm.process.ProcessConnectorDefinition;
import org.bonitasoft.web.rest.model.bpm.process.ProcessConnectorItem;
import org.bonitasoft.web.rest.model.bpm.process.ProcessItem;
import org.bonitasoft.web.toolkit.client.ui.CssId;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.component.Section;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTable;

/**
 * @author Colin PUY
 * 
 */
public class ConnectorSection extends Section {

    public ConnectorSection(ProcessItem process, ConfigurationState state) {
        super(new JsId("connectors"), _("Connector definitions"));
        setId(CssId.MD_SECTION_CONNECTORS);
        ConfigurationStateText configurationStateText = new ConfigurationStateText(state);
        ItemTable connectorsItemTable = connectorsItemTable(process);
        connectorsItemTable.addItemTableLoadedHandler(
                new HideComponentOnEmptyTableHandler(configurationStateText));

        addHeader(configurationStateText);
        addBody(connectorsItemTable);
    }

    /**
     * Overridden in SP
     */
    protected ItemTable connectorsItemTable(final ProcessItem process) {
        return new ItemTable(ProcessConnectorDefinition.get())
                .addHiddenFilter(ProcessConnectorItem.ATTRIBUTE_PROCESS_ID, process.getId())
                .addColumn(ProcessConnectorItem.ATTRIBUTE_NAME, _("Type"))
                .addColumn(ProcessConnectorItem.ATTRIBUTE_VERSION, _("Version"))
                .addColumn(ProcessConnectorItem.ATTRIBUTE_CLASSNAME, _("Class name"))
                .setShowSearch(false);
    }
}
