/**
 * Copyright (C) 2012 BonitaSoft S.A.
 * 
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
package org.bonitasoft.console.client.admin.organization.group;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.web.rest.model.identity.GroupDefinition;
import org.bonitasoft.web.rest.model.identity.GroupItem;
import org.bonitasoft.web.toolkit.client.ui.component.Section;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTable;
import org.bonitasoft.web.toolkit.client.ui.component.table.Table.VIEW_TYPE;

/**
 * @author Colin PUY
 */
public class SubGroupSection extends Section {

    public SubGroupSection(GroupItem groupItem) {
        super(_("Sub groups"));
        addClass("subgroups");
        ItemTable table = new ItemTable(GroupDefinition.get())
            .addHiddenFilter(GroupItem.ATTRIBUTE_PARENT_PATH, groupItem.getPath())
            .addColumn(GroupItem.ATTRIBUTE_ICON, _("Icon"))
            .addColumn(GroupItem.ATTRIBUTE_DISPLAY_NAME, _("Name"))
            .setView(VIEW_TYPE.VIEW_LIST);
        addBody(table);
    }

}
