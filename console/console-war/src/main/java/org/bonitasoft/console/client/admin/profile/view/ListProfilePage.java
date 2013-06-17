/**
 * Copyright (C) 2011 BonitaSoft S.A.
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
package org.bonitasoft.console.client.admin.profile.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.LinkedList;

import org.bonitasoft.web.rest.api.model.portal.profile.ProfileDefinition;
import org.bonitasoft.web.rest.api.model.portal.profile.ProfileItem;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTable;
import org.bonitasoft.web.toolkit.client.ui.component.table.formatter.I18NCellFormatter;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingFilter;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingPage;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingResourceFilter;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingSort;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingTable;

/**
 * @author Zhiheng Yang
 * @author Bastien ROHART
 */
public class ListProfilePage extends ItemListingPage<ProfileItem> {

    public static final String TOKEN = "profilelisting";

    //public static final String EXPORT_REST_API_URL = "../REST/profileImportAndExportAPI/export";

    private static final String TABLE_ALL_PROFILES = "allusers";

    private static final String FILTER_PRIMARY_ALL_PROFILES = "allusersfilter";

    @Override
    protected LinkedList<ItemListingFilter> definePrimaryFilters() {
        final LinkedList<ItemListingFilter> filters = new LinkedList<ItemListingFilter>();
        filters.add(new ItemListingFilter(FILTER_PRIMARY_ALL_PROFILES, _("All"), _("All Profiles"), TABLE_ALL_PROFILES));
        return filters;
    }

    @Override
    public void defineTitle() {
        this.setTitle(_("Manage profiles"));
    }

    @Override
    public String defineJsId() {
        return "profileList";
    }

    @Override
    public String defineToken() {
        return TOKEN;
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
        return new ItemListingSort(ProfileItem.ATTRIBUTE_NAME, true);
    }

    @Override
    protected LinkedList<ItemListingTable> defineTables() {
        final LinkedList<ItemListingTable> tables = new LinkedList<ItemListingTable>();
        tables.add(allProfileTable());
        return tables;
    }

    protected ItemListingTable allProfileTable() {
        return new ItemListingTable(new JsId(TABLE_ALL_PROFILES), _("Enabled"), itemTable(), getItemQuickDetailPage());
    }

    protected ItemTable itemTable() {
        showSearchBar = false;
        return new ItemTable(new JsId("profile"), Definitions.get(ProfileDefinition.TOKEN))
                .addColumn(ProfileItem.ATTRIBUTE_ICON, _("Icon"))
                .addColumn(ProfileItem.ATTRIBUTE_NAME, _("Name"), true, true)
                .addColumn(ProfileItem.ATTRIBUTE_DESCRIPTION, _("Description"), false)
                .addCellFormatter(ProfileItem.ATTRIBUTE_NAME, new I18NCellFormatter())
                .addCellFormatter(ProfileItem.ATTRIBUTE_DESCRIPTION, new I18NCellFormatter())
                .setShowSearch(false);
    }
    
    protected ProfileQuickDetailsPage getItemQuickDetailPage() {
        return new ProfileQuickDetailsPage();
    }
}
