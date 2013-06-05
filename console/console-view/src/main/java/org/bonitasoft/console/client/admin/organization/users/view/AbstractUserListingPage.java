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

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.LinkedList;

import org.bonitasoft.console.client.model.identity.UserDefinition;
import org.bonitasoft.console.client.model.identity.UserItem;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.DateAttributeReader;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTable;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingFilter;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingPage;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingResourceFilter;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingSort;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingTable;
import org.bonitasoft.web.toolkit.client.ui.utils.DateFormat.FORMAT;

/**
 * Abstarct class defining a User listing page default ui and behavior
 * 
 * @author Colin PUY
 */
public abstract class AbstractUserListingPage extends ItemListingPage<UserItem> {

    private static final String TABLE_ALL_USERS = "allusers";

    private static final String FILTER_PRIMARY_ALL_USERS = "allusersfilter";

    @Override
    protected LinkedList<ItemListingFilter> definePrimaryFilters() {
        final LinkedList<ItemListingFilter> filters = new LinkedList<ItemListingFilter>();
        filters.add(new ItemListingFilter(FILTER_PRIMARY_ALL_USERS, _("All"), _("All Users"), TABLE_ALL_USERS));
        return filters;
    }

    @Override
    protected LinkedList<ItemListingFilter> defineSecondaryFilters() {
        return null;
    }

    @Override
    protected ItemListingResourceFilter defineResourceFilters() {
        return null;
    }

    @Override
    protected ItemListingSort defineDefaultSort() {
        return new ItemListingSort(UserItem.ATTRIBUTE_USERNAME, true);
    }

    @Override
    protected LinkedList<ItemListingTable> defineTables() {
        final LinkedList<ItemListingTable> tables = new LinkedList<ItemListingTable>();
        tables.add(allUserTable());
        return tables;
    }

    protected ItemListingTable allUserTable() {
        return new ItemListingTable(new JsId(TABLE_ALL_USERS), _("Enabled"), itemTable(), getItemQuickDetailPage());
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
        return new UserQuickDetailsPage();
    }

    @Override
    public void defineTitle() {
        this.setTitle(_("Users"));
    }
}
