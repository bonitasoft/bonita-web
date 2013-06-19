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
package org.bonitasoft.console.client.admin.organization.group.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.LinkedList;

import org.bonitasoft.web.rest.model.identity.GroupDefinition;
import org.bonitasoft.web.rest.model.identity.GroupItem;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTable;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingFilter;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingPage;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingResourceFilter;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingSort;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingTable;

/**
 * Abstarct class defining a User listing page default ui and behavior
 * 
 * @author Julien Mege
 */
public abstract class GroupListingPage extends ItemListingPage<GroupItem> {

    private static final String TABLE_ALL_GROUPS = "allgroups";

    private static final String FILTER_PRIMARY_ALL_GROUPS = "allgroupsfilter";

    @Override
    protected LinkedList<ItemListingFilter> definePrimaryFilters() {
        final LinkedList<ItemListingFilter> filters = new LinkedList<ItemListingFilter>();
        filters.add(new ItemListingFilter(FILTER_PRIMARY_ALL_GROUPS, _("All"), _("All Groups"), TABLE_ALL_GROUPS));
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
        return new ItemListingSort(GroupItem.ATTRIBUTE_DISPLAY_NAME, true);
    }

    @Override
    protected LinkedList<ItemListingTable> defineTables() {
        final LinkedList<ItemListingTable> tables = new LinkedList<ItemListingTable>();
        tables.add(allGroupTable());
        return tables;
    }

    protected ItemListingTable allGroupTable() {
        return new ItemListingTable(new JsId(TABLE_ALL_GROUPS), _("All groups"), itemTable(), getItemQuickDetailPage());
    }

    protected ItemTable itemTable() {
        return new ItemTable(Definitions.get(GroupDefinition.TOKEN))
                .addColumn(GroupItem.ATTRIBUTE_ICON, _("Icon"))
                .addColumn(GroupItem.ATTRIBUTE_DISPLAY_NAME, _("Name"), true)
                .addColumn(GroupItem.ATTRIBUTE_CREATION_DATE, _("Creation date"), false)
                .addGroupedMultipleDeleteAction(_("Delete selected groups"), GroupDefinition.get(), _("group"), _("groups"));
    }

    protected GroupQuickDetailsPage getItemQuickDetailPage() {
        return new GroupQuickDetailsPage();
    }

    @Override
    public void defineTitle() {
        this.setTitle(_("Groups"));
    }
}
