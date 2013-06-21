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
package org.bonitasoft.console.client.admin.monitoring.report.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.LinkedList;

import org.bonitasoft.web.rest.model.monitoring.report.ReportDefinition;
import org.bonitasoft.web.rest.model.monitoring.report.ReportItem;
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
 * @author Paul AMAR
 * 
 */
public class ReportListingAdminPage extends ItemListingPage<ReportItem> {

    public static final String TOKEN = "reportlistingadmin";

    protected static String TABLE_REPORT = "reports";

    public static final String FILTER_REPORT = "reports";

    
    public ReportListingAdminPage() {
        super();
        showSearchBar = true;
    }
    
    @Override
    protected LinkedList<ItemListingFilter> definePrimaryFilters() {
        final LinkedList<ItemListingFilter> filters = new LinkedList<ItemListingFilter>();
        filters.add(new ItemListingFilter(FILTER_REPORT, _("Reports"), _("Display a summary of all reports"), TABLE_REPORT));
        return filters;
    }

    @Override
    protected ItemListingResourceFilter defineResourceFilters() {
        return null;
    }

    @Override
    protected ItemListingSort defineDefaultSort() {
        return new ItemListingSort(ReportItem.ATTRIBUTE_NAME, true);
    }

    @Override
    protected LinkedList<ItemListingTable> defineTables() {
        final LinkedList<ItemListingTable> tables = new LinkedList<ItemListingTable>();
        
        tables.add(new ItemListingTable(new JsId(TABLE_REPORT), _("All"),
                new ItemTable(Definitions.get(ReportDefinition.TOKEN))
                        .addColumn(ReportItem.ATTRIBUTE_NAME, _("Name"), true)
                        .addColumn(ReportItem.ATTRIBUTE_DESCRIPTION, _("Description"), false)
                        .addCellFormatter(ReportItem.ATTRIBUTE_NAME, new I18NCellFormatter())
                        .addCellFormatter(ReportItem.ATTRIBUTE_DESCRIPTION, new I18NCellFormatter())
                ,
                new ReportQuickDetailsAdminPage()
                ));
        return tables;
    }

    @Override
    public void defineTitle() {
        this.setTitle(_("Reports"));

    }

    @Override
    public String defineToken() {
        return TOKEN;
    }

}
