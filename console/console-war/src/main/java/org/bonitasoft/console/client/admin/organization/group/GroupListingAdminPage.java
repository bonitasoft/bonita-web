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
package org.bonitasoft.console.client.admin.organization.group;

import static java.util.Arrays.asList;
import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bonitasoft.console.client.admin.organization.role.RoleListingPage;
import org.bonitasoft.web.rest.model.identity.GroupDefinition;
import org.bonitasoft.web.rest.model.identity.GroupItem;
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

/**
 * @author Julien Mege
 * 
 */
public class GroupListingAdminPage extends ItemListingPage<GroupItem> {

    public static final String TOKEN = "grouplistingadmin";
    
    public static final List<String> PRIVILEGES = new ArrayList<String>();
    
    static {
        PRIVILEGES.add(GroupListingAdminPage.TOKEN);
    }

    private static final String TABLE_ALL_GROUPS = "allgroups";

    private static final String FILTER_PRIMARY_ALL_GROUPS = "allgroupsfilter";

    @Override
    public void defineTitle() {
        this.setTitle(_("Groups"));
    }

    @Override
    protected LinkedList<ItemListingFilter> definePrimaryFilters() {
        final LinkedList<ItemListingFilter> filters = new LinkedList<ItemListingFilter>();
        filters.add(allFilter());
        return filters;
    }

    private ItemListingFilter allFilter() {
        return new ItemListingFilter(FILTER_PRIMARY_ALL_GROUPS, _("All"), _("All Groups"), TABLE_ALL_GROUPS);
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
        return new ItemListingTable(new JsId(TABLE_ALL_GROUPS), _("All groups"), itemTable(), new GroupQuickDetailsAdminPage());
    }

    protected ItemTable itemTable() {
        return new ItemTable(GroupDefinition.get())
                .addColumn(GroupItem.ATTRIBUTE_ICON, _("Icon"))
                .addColumn(GroupItem.ATTRIBUTE_DISPLAY_NAME, _("Name"), true)
                .addColumn(GroupItem.ATTRIBUTE_CREATION_DATE, _("Creation date"), false)
                .addGroupedMultipleDeleteAction(_("Delete selected groups"), GroupDefinition.get(), _("group"), _("groups"));
    }

    @Override
    protected List<Clickable> defineFilterPanelActions() {
        return asList(addGroupLink());
    }

    private Clickable addGroupLink() {
        return new Link(_("Create"), _("Opens a popup to create a group"), 
                new CheckValidSessionBeforeAction(new ActionShowPopup(new AddGroupPage())));
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }
}
