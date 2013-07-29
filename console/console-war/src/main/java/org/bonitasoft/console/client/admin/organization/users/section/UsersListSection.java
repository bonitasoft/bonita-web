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
package org.bonitasoft.console.client.admin.organization.users.section;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.console.client.admin.organization.users.action.ShowUserMoreDetailAction;
import org.bonitasoft.web.rest.model.identity.RoleItem;
import org.bonitasoft.web.rest.model.identity.UserDefinition;
import org.bonitasoft.web.rest.model.identity.UserItem;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.ui.component.Section;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTable;
import org.bonitasoft.web.toolkit.client.ui.component.table.Table.VIEW_TYPE;

public class UsersListSection extends Section {

    public UsersListSection(RoleItem role) {
        super(_("List of users"));
        addClass("usersmapping");
        addBody(usersList(role));
    }

    private ItemTable usersList(RoleItem role) {
       return new ItemTable(Definitions.get(UserDefinition.TOKEN))
            .addHiddenFilter(UserItem.FILTER_ROLE_ID, role.getId().toString())
            .addColumn(UserItem.ATTRIBUTE_ICON, _("Icon"))
            .addColumn(UserItem.ATTRIBUTE_FIRSTNAME, _("First name"), true)
            .addColumn(UserItem.ATTRIBUTE_LASTNAME, _("Last name"), true)
            .setView(VIEW_TYPE.VIEW_LIST)
            .setDefaultAction(new ShowUserMoreDetailAction());
    }
}
