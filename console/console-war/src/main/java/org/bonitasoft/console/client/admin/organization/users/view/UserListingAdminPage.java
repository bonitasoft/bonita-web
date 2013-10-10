/**
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * Copyright (C) 2012 BonitaSoft S.A.
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
package org.bonitasoft.console.client.admin.organization.users.view;

import static java.util.Arrays.asList;
import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bonitasoft.web.rest.model.identity.UserDefinition;
import org.bonitasoft.web.rest.model.identity.UserItem;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.DateAttributeReader;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.ActionShowPopup;
import org.bonitasoft.web.toolkit.client.ui.action.CheckValidSessionBeforeAction;
import org.bonitasoft.web.toolkit.client.ui.component.Clickable;
import org.bonitasoft.web.toolkit.client.ui.component.Link;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTable;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingFilter;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingPage;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingResourceFilter;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingSort;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingTable;
import org.bonitasoft.web.toolkit.client.ui.utils.DateFormat.FORMAT;

/**
 * @author Paul AMAR
 * 
 */
public class UserListingAdminPage extends ItemListingPage<UserItem> {
    
    public static final String TOKEN = "userlistingadmin";

    private static final String TABLE_ENABLED_USERS = "enabledusers";
    private static final String TABLE_DISABLED_USERS = "disabledusers";
    
    private static final String FILTER_PRIMARY_ENABLED_USERS = "enabledusers";
    private static final String FILTER_PRIMARY_DISABLED_USERS = "disabledusers";

	public static final List<String> PRIVILEGES = new ArrayList<String>();
    
    static {
        PRIVILEGES.add(UserListingAdminPage.TOKEN);
    }

    @Override
    public void defineTitle() {
        this.setTitle(_("Users"));
    }

    @Override
    protected List<Clickable> defineFilterPanelActions() {
        return asList(addUserLink());
    }

    private Clickable addUserLink() {
        return new Link(_("Create a user"), _("Opens a popup to create a user"), 
                new CheckValidSessionBeforeAction(new ActionShowPopup(new PopupAddUserPage())));
    }

    @Override
    protected LinkedList<ItemListingFilter> definePrimaryFilters() {
        LinkedList<ItemListingFilter> filters = new LinkedList<ItemListingFilter>();
        filters.add(enabledUsersFilter());
        filters.add(disabledUsersFilter());
        return filters;
    }

    private ItemListingFilter disabledUsersFilter() {
        return new ItemListingFilter(FILTER_PRIMARY_DISABLED_USERS, _("Inactive"), _("Inactive Users"), TABLE_DISABLED_USERS)
            .addFilter(UserItem.ATTRIBUTE_ENABLED, "false");
    }

    private ItemListingFilter enabledUsersFilter() {
        return new ItemListingFilter(FILTER_PRIMARY_ENABLED_USERS, _("Active"), _("Active Users"), TABLE_ENABLED_USERS)
            .addFilter(UserItem.ATTRIBUTE_ENABLED, "true");
    }

    @Override
    protected ItemListingSort defineDefaultSort() {
        return new ItemListingSort(UserItem.ATTRIBUTE_USERNAME, true);
    }

    @Override
    protected LinkedList<ItemListingTable> defineTables() {
        final LinkedList<ItemListingTable> tables = new LinkedList<ItemListingTable>();
        tables.add(enabledUserTable());
        tables.add(disabledUserTable());
        return tables;
    }

    protected ItemListingTable enabledUserTable() {
        ItemTable itemTable = itemTable();
        return new ItemListingTable(new JsId(TABLE_ENABLED_USERS), _("Users"), itemTable, getItemQuickDetailPage());
    }
    
    private ItemListingTable disabledUserTable() {
        ItemTable itemTable = itemTable();
        return new ItemListingTable(new JsId(TABLE_DISABLED_USERS), _("Users"), itemTable, getItemQuickDetailPage());
    }

    protected ItemTable itemTable() {
        return new ItemTable(Definitions.get(UserDefinition.TOKEN))
                .addColumn(UserItem.ATTRIBUTE_ICON, _("Icon"))
                .addColumn(UserItem.ATTRIBUTE_FIRSTNAME, _("First name"), true)
                .addColumn(UserItem.ATTRIBUTE_LASTNAME, _("Last name"), true)
                .addColumn(new DateAttributeReader(UserItem.ATTRIBUTE_LAST_CONNECTION_DATE, FORMAT.DISPLAY_RELATIVE), _("Last login date"))
                .addColumn(UserItem.ATTRIBUTE_JOB_TITLE, _("Job title"), false);
    }

    protected UserQuickDetailsPage getItemQuickDetailPage() {
        return new UserQuickDetailsAdminPage();
    }

    @Override
    protected ItemListingResourceFilter defineResourceFilters() {
        return null;
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }
}
