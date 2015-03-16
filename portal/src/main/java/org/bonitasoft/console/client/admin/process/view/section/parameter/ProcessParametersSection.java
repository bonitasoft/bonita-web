/*
 * Copyright (C) 2015 BonitaSoft S.A.
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
package org.bonitasoft.console.client.admin.process.view.section.parameter;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.console.client.admin.process.view.section.configuration.ConfigurationState;
import org.bonitasoft.console.client.admin.process.view.section.configuration.ConfigurationStateText;
import org.bonitasoft.console.client.common.event.handler.HideComponentOnEmptyTableHandler;
import org.bonitasoft.web.rest.model.bpm.process.ProcessItem;
import org.bonitasoft.web.rest.model.bpm.process.ProcessParameterDefinition;
import org.bonitasoft.web.rest.model.bpm.process.ProcessParameterItem;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.FixedLengthReader;
import org.bonitasoft.web.toolkit.client.ui.component.Section;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTable;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTableLoadedEvent;


/**
 * @author Colin PUY
 * @author Fabio Lombardi
 * @author Baptiste Mesta
 */
public class ProcessParametersSection extends Section {

    public ProcessParametersSection(final ProcessItem item, ConfigurationState state) {
        super(_("Parameters"));

        ConfigurationStateText configurationStateText = new ConfigurationStateText(state);
        ItemTable parametersItemTable = parametersItemTable(item);
        parametersItemTable.addHandler(new HideComponentOnEmptyTableHandler(configurationStateText), ItemTableLoadedEvent.TYPE);

        addHeader(configurationStateText);
        addBody(parametersItemTable);
    }

    protected ItemTable parametersItemTable(final ProcessItem item) {
        ItemTable table = new ItemTable(ProcessParameterDefinition.get())
                .addHiddenFilter(ProcessParameterItem.FILTER_PROCESS_ID, item.getId())
                .addColumn(ProcessParameterItem.ATTRIBUTE_NAME, _("Name"), true, true)
                .addColumn(ProcessParameterItem.ATTRIBUTE_DESCRIPTION, _("Description"), false)
                .addColumn(ProcessParameterItem.ATTRIBUTE_TYPE, _("Type"), false)
                .addColumn(new FixedLengthReader(ProcessParameterItem.ATTRIBUTE_VALUE, 150), _("Value"), false)
                .setShowSearch(false);
        return table;
    }
}
