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
package org.bonitasoft.console.client.admin.organization.role;

import static java.util.Arrays.asList;
import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bonitasoft.console.client.admin.process.view.ProcessListingAdminPage;
import org.bonitasoft.web.rest.model.identity.RoleDefinition;
import org.bonitasoft.web.rest.model.identity.RoleItem;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.ItemHasDualNameAttributeReader;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.ActionShowPopup;
import org.bonitasoft.web.toolkit.client.ui.action.CheckValidSessionBeforeAction;
import org.bonitasoft.web.toolkit.client.ui.component.Clickable;
import org.bonitasoft.web.toolkit.client.ui.component.Link;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTable;
import org.bonitasoft.web.toolkit.client.ui.component.table.formatter.DefaultImageCellFormatter;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingFilter;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingPage;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingResourceFilter;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingSort;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingTable;

/**
 * @author Colin PUY
 * 
 */
public class RoleListingPage extends ItemListingPage<RoleItem> {

    public static final String TOKEN = "rolelistingadmin";
    
    public static final List<String> PRIVILEGES = new ArrayList<String>();
    
    static {
        PRIVILEGES.add(RoleListingPage.TOKEN);
    }


    private static final String TABLE_ALL_ROLES = "allroles";

    private static final String FILTER_PRIMARY_ALL_ROLES = "allrolesfilter";

    @Override
    public void defineTitle() {
        this.setTitle(_("Roles"));
    }

    @Override
    protected List<Clickable> defineFilterPanelActions() {
        return asList(addRoleLink());
    }

    private Clickable addRoleLink() {
        return new Link(_("Create a role"), _("Opens a popup to create a role"), 
                new CheckValidSessionBeforeAction(new ActionShowPopup(new AddRolePage())));
    }

    @Override
    protected LinkedList<ItemListingFilter> definePrimaryFilters() {
        final LinkedList<ItemListingFilter> filters = new LinkedList<ItemListingFilter>();
        filters.add(new ItemListingFilter(FILTER_PRIMARY_ALL_ROLES, _("All"), _("All Roles"), TABLE_ALL_ROLES));
        return filters;
    }

    @Override
    protected LinkedList<ItemListingTable> defineTables() {
        final LinkedList<ItemListingTable> tables = new LinkedList<ItemListingTable>();
        tables.add(allRolesTable());
        return tables;
    }

    private ItemListingTable allRolesTable() {
        return new ItemListingTable(new JsId(TABLE_ALL_ROLES), _("All Roles"), itemTable(), new RoleQuickDetailsPage());
    }

    private ItemTable itemTable() {
        return new ItemTable(RoleDefinition.get())
                .addColumn(RoleItem.ATTRIBUTE_ICON, _("Icon"))
                .addColumn(new ItemHasDualNameAttributeReader(), _("Name"), true)
                .addColumn(RoleItem.ATTRIBUTE_CREATION_DATE, _("Creation date"))
                .addCellFormatter(RoleItem.ATTRIBUTE_ICON, new DefaultImageCellFormatter())
                .addGroupedMultipleDeleteAction(_("Delete selected roles"), RoleDefinition.get(), _("Role"), _("Roles"));
    }

    @Override
    protected ItemListingResourceFilter defineResourceFilters() {
        return null;
    }

    @Override
    protected ItemListingSort defineDefaultSort() {
        return new ItemListingSort(RoleItem.ATTRIBUTE_NAME, true);
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }
}
