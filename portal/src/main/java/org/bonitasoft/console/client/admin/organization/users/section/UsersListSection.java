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
import org.bonitasoft.web.rest.model.identity.GroupItem;
import org.bonitasoft.web.rest.model.identity.RoleItem;
import org.bonitasoft.web.rest.model.identity.UserDefinition;
import org.bonitasoft.web.rest.model.identity.UserItem;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.ui.CssId;
import org.bonitasoft.web.toolkit.client.ui.component.Section;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTable;
import org.bonitasoft.web.toolkit.client.ui.component.table.Table.VIEW_TYPE;

public class UsersListSection extends Section {

    private ItemTable usersTable;

    public UsersListSection(String title) {
        super(title);
        addClass("usersmapping");
        setId(CssId.SECTION_USERLIST);
        usersTable = buildUsersTable();
        addBody(usersTable);
    }

    private ItemTable buildUsersTable() {
        return new ItemTable(Definitions.get(UserDefinition.TOKEN))
                .addHiddenFilter(UserItem.ATTRIBUTE_ENABLED, "true")
                .addColumn(UserItem.ATTRIBUTE_ICON, _("Icon"))
                .addColumn(UserItem.ATTRIBUTE_FIRSTNAME, _("First name"), true)
                .addColumn(UserItem.ATTRIBUTE_LASTNAME, _("Last name"), true)
                .setView(VIEW_TYPE.VIEW_LIST)
                .setDefaultAction(new ShowUserMoreDetailAction());
    }

    public UsersListSection filterByGroup(GroupItem group) {
        usersTable.addHiddenFilter(UserItem.FILTER_GROUP_ID, group.getId().toString());
        return this;
    }

    public UsersListSection filterByRole(RoleItem role) {
        usersTable.addHiddenFilter(UserItem.FILTER_ROLE_ID, role.getId().toString());
        return this;
    }

    public UsersListSection setNbLinesByPage(int nbLines) {
        usersTable.setNbLinesByPage(nbLines);
        return this;
    }
}
