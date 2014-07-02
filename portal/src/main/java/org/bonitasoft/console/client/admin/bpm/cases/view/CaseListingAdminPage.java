/**
 * Copyright (C) 2012 BonitaSoft S.A.
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
package org.bonitasoft.console.client.admin.bpm.cases.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bonitasoft.console.client.user.cases.view.CaseListingPage;
import org.bonitasoft.web.rest.model.bpm.cases.ArchivedCaseItem;
import org.bonitasoft.web.rest.model.bpm.cases.CaseDefinition;
import org.bonitasoft.web.rest.model.bpm.cases.CaseItem;
import org.bonitasoft.web.rest.model.bpm.process.ProcessItem;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.component.Title;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemQuickDetailsPage;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.AppResourceFilter;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingFilter;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingResourceFilter;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingTable;

/**
 * CaseListingPage - List all the cases in 3 panels view
 *
 * @author Nicolas Tith
 * @author Colin PUY
 */
public class CaseListingAdminPage extends CaseListingPage {

    public static final String TOKEN = "caselistingadmin";

    public static final List<String> PRIVILEGES = new ArrayList<String>();

    static {
        PRIVILEGES.add(CaseListingAdminPage.TOKEN);
    }

    public static final String FILTER_PRIMARY_OPENED_CASES = "openedcases";

    @Override
    protected LinkedList<ItemListingFilter> definePrimaryFilters() {
        final LinkedList<ItemListingFilter> filters = new LinkedList<ItemListingFilter>();
        filters.add(allOpenedCases());
        return filters;
    }

    protected ItemListingFilter allOpenedCases() {
        return new ItemListingFilter(FILTER_PRIMARY_OPENED_CASES, _("Opened"), _("Cases "), TABLE_CASES_ALL);
    }

    @Override
    protected LinkedList<ItemListingFilter> defineSecondaryFilters() {
        final LinkedList<ItemListingFilter> filters = new LinkedList<ItemListingFilter>();
        filters.add(allArchivedCases());
        return filters;
    }

    protected ItemListingFilter allArchivedCases() {
        return new ItemListingFilter(FILTER_SECONDARY_ARCHIVED_CASES, _("Archived"), _("Cases started or worked on by someone"), TABLE_HISTORY_ALL)
                .addFilter(CaseItem.ATTRIBUTE_STATE, CaseItem.VALUE_STATE_COMPLETED);
    }

    @Override
    protected Title defineResourceFiltersTitle() {
        return new Title(_("Apps"));
    }

    @Override
    protected ItemListingResourceFilter defineResourceFilters() {
        return new AppResourceFilter(TABLE_CASES_ALL)
                .addFilterMapping(CaseItem.ATTRIBUTE_PROCESS_ID, ProcessItem.ATTRIBUTE_ID);
    }

    @Override
    protected LinkedList<ItemListingTable> defineTables() {
        final LinkedList<ItemListingTable> tables = new LinkedList<ItemListingTable>();
        tables.add(allCasesAdminTable());
        tables.add(archivedCasesTable(getArchivedCaseQuickDetailsPage()));
        return tables;
    }

    protected ItemListingTable allCasesAdminTable() {
        return new ItemListingTable(new JsId(TABLE_CASES_ALL), _("All"),
                buildAllCasesItemTable(CaseDefinition.get(), CaseItem.ATTRIBUTE_ID)
                        .addGroupedMultipleDeleteAction(_("Delete selected cases"), CaseDefinition.get(), _("case"), _("cases")),
                getCaseQuickDetailsPage());
    }

    protected ItemQuickDetailsPage<CaseItem> getCaseQuickDetailsPage() {
        return new CaseQuickDetailsAdminPage();
    }

    protected ItemQuickDetailsPage<ArchivedCaseItem> getArchivedCaseQuickDetailsPage() {
        return new ArchivedCaseQuickDetailsAdminPage();
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    public String getPluginToken() {
        return TOKEN;
    }
}
