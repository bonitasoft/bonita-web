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
package org.bonitasoft.console.client.user.cases.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bonitasoft.web.rest.model.bpm.cases.ArchivedCaseDefinition;
import org.bonitasoft.web.rest.model.bpm.cases.ArchivedCaseItem;
import org.bonitasoft.web.rest.model.bpm.cases.CaseDefinition;
import org.bonitasoft.web.rest.model.bpm.cases.CaseItem;
import org.bonitasoft.web.rest.model.bpm.process.ProcessItem;
import org.bonitasoft.web.toolkit.client.Session;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.DeployedAttributeReader;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTable;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemQuickDetailsPage;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingFilter;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingPage;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingResourceFilter;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingSort;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingTable;

/**
 * CaseListingPage - List all the cases
 * 
 * @author Paul AMAR
 * @author Colin PUY
 */
public class CaseListingPage extends ItemListingPage<CaseItem> implements PluginCase {

    public static final String TOKEN = "caselistinguser";
    
    public static final List<String> PRIVILEGES = new ArrayList<String>();
    
    static {
        PRIVILEGES.add(CaseListingPage.TOKEN);
    }

    protected static final String TABLE_CASES_ALL = "allcases";

    protected static final String TABLE_HISTORY_ALL = "allhistory";

    public static final String FILTER_PRIMARY_MINE = "mycases";

    public static final String FILTER_PRIMARY_WORKED_ON_CASES = "workedoncases";

    public static final String FILTER_SECONDARY_ARCHIVED_CASES = "archivedcases";

    public static final String FILTER_SECONDARY_WORKED_ON_CASES = "workedonarchivedcases";

    @Override
    public void defineTitle() {
        setTitle(_("Cases"));
    }

    @Override
    protected LinkedList<ItemListingFilter> definePrimaryFilters() {
        final LinkedList<ItemListingFilter> filters = new LinkedList<ItemListingFilter>();
        filters.add(casesUserStarted());
        filters.add(casesUserWorkedOn());
        return filters;
    }

    private ItemListingFilter casesUserWorkedOn() {
        return new ItemListingFilter(FILTER_PRIMARY_WORKED_ON_CASES, _("Worked on"), _("Cases I worked on"), TABLE_CASES_ALL)
                .addFilter(CaseItem.FILTER_USER_ID, Session.getUserId());
    }

    private ItemListingFilter casesUserStarted() {
        return new ItemListingFilter(FILTER_PRIMARY_MINE, _("My cases"), _("Cases I have started"), TABLE_CASES_ALL)
                .addFilter(CaseItem.ATTRIBUTE_STARTED_BY_USER_ID, Session.getUserId());
    }

    @Override
    protected LinkedList<ItemListingFilter> defineSecondaryFilters() {
        final LinkedList<ItemListingFilter> filters = new LinkedList<ItemListingFilter>();
        filters.add(userArchivedCases());
        return filters;
    }

    private ItemListingFilter userArchivedCases() {
        return new ItemListingFilter(FILTER_SECONDARY_ARCHIVED_CASES, _("Archived"), _("Cases I have started or I have worked on"), TABLE_HISTORY_ALL)
                .addFilter(CaseItem.FILTER_USER_ID, Session.getUserId());
    }

    @Override
    protected ItemListingResourceFilter defineResourceFilters() {
        return null;
    }

    @Override
    protected LinkedList<ItemListingTable> defineTables() {
        final LinkedList<ItemListingTable> tables = new LinkedList<ItemListingTable>();
        tables.add(allCasesTable(new CaseQuickDetailsPage()));
        tables.add(archivedCasesTable(new ArchivedCaseQuickDetailsPage()));
        return tables;
    }

    protected ItemListingTable allCasesTable(final ItemQuickDetailsPage<CaseItem> caseQuickDetailsPage) {
        return new ItemListingTable(new JsId(TABLE_CASES_ALL), _("All"),
                buildAllCasesItemTable(CaseDefinition.get(), CaseItem.ATTRIBUTE_ID),
                caseQuickDetailsPage);
    }

    protected ItemTable buildAllCasesItemTable(final ItemDefinition itemDefinition, final String idAttributeDefinition) {
        return new ItemTable(itemDefinition)
                .addColumn(idAttributeDefinition, _("ID"), true)
                // TODO: Change to display name once it has been supported by engine
                .addColumn(new DeployedAttributeReader(CaseItem.ATTRIBUTE_PROCESS_ID, ProcessItem.ATTRIBUTE_NAME), _("Process name"), true)
                .addColumn(CaseItem.ATTRIBUTE_START_DATE, _("Start date"));
        // .setOrder(CaseItem.ATTRIBUTE_START_DATE, true);
    }

    protected ItemListingTable archivedCasesTable(final ItemQuickDetailsPage<ArchivedCaseItem> caseQuickDetailsPage) {
        return new ItemListingTable(new JsId(TABLE_HISTORY_ALL), _("All"),
                new ItemTable(ArchivedCaseDefinition.get())
                        .addColumn(ArchivedCaseItem.ATTRIBUTE_SOURCE_OBJECT_ID, _("ID"), true)
                        // TODO: Change to display name once it has been supported by engine
                        .addColumn(new DeployedAttributeReader(ArchivedCaseItem.ATTRIBUTE_PROCESS_ID, ProcessItem.ATTRIBUTE_NAME), _("Process name"), true)
                        .addColumn(ArchivedCaseItem.ATTRIBUTE_ARCHIVED_DATE, _("Archived date"))
                // .setOrder(ArchivedCaseItem.ATTRIBUTE_ARCHIVED_DATE, false)
                , caseQuickDetailsPage);
    }

    @Override
    protected ItemListingSort defineDefaultSort() {
        // TODO: il faut le faire avec le start Date
        return new ItemListingSort(CaseItem.ATTRIBUTE_ID, true);
    }

    @Override
    public String defineToken() {
        return TOKEN;

    }

    @Override
    public String getPluginToken() {
        return PLUGIN_TOKEN;
    }
}
