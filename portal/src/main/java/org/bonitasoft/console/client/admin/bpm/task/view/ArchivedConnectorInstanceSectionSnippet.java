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

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.console.client.common.component.snippet.SectionSnippet;
import org.bonitasoft.web.rest.model.bpm.connector.ArchivedConnectorInstanceDefinition;
import org.bonitasoft.web.rest.model.bpm.connector.ArchivedConnectorInstanceItem;
import org.bonitasoft.web.rest.model.bpm.flownode.ArchivedFlowNodeItem;
import org.bonitasoft.web.rest.model.bpm.flownode.IFlowNodeItem;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.ui.CssId;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.component.Section;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTable;

/**
 * @author Julien mege
 * 
 */
public class ArchivedConnectorInstanceSectionSnippet implements SectionSnippet {

    private int nbLinesByPage = 10;

    private final ArchivedFlowNodeItem item;

    public ArchivedConnectorInstanceSectionSnippet(final IFlowNodeItem item) {
        this.item = (ArchivedFlowNodeItem) item;
    }

    @Override
    public Section build() {
        Section archivedConnectorSection = new Section(_("Connectors"));
        archivedConnectorSection.setId(CssId.MD_SECTION_CONNECTORS);
        return archivedConnectorSection
                .addBody(buildConnectorTable());
    }

    public int getNbLinesByPage() {
        return this.nbLinesByPage;
    }

    public ArchivedConnectorInstanceSectionSnippet setNbLinesByPage(final int nbLinesByPage) {
        this.nbLinesByPage = nbLinesByPage;
        return this;
    }

    private ItemTable buildConnectorTable() {
        return new ItemTable(new JsId("connectors"), Definitions.get(ArchivedConnectorInstanceDefinition.TOKEN))
                // filter
                .addHiddenFilter(ArchivedConnectorInstanceItem.ATTRIBUTE_CONTAINER_ID,
                        this.item.getAttributeValue(ArchivedFlowNodeItem.ATTRIBUTE_SOURCE_OBJECT_ID))

                // columns
                .addColumn(ArchivedConnectorInstanceItem.ATTRIBUTE_NAME, _("Name"))
                .addColumn(ArchivedConnectorInstanceItem.ATTRIBUTE_STATE, _("State"))

                .setNbLinesByPage(getNbLinesByPage());
    }
}
