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
package org.bonitasoft.console.client.admin.process.view;

import static java.util.Arrays.asList;
import static org.bonitasoft.web.rest.model.bpm.process.ProcessItem.ATTRIBUTE_ACTIVATION_STATE;
import static org.bonitasoft.web.rest.model.bpm.process.ProcessItem.VALUE_ACTIVATION_STATE_DISABLED;
import static org.bonitasoft.web.rest.model.bpm.process.ProcessItem.VALUE_ACTIVATION_STATE_ENABLED;
import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.bonitasoft.console.client.admin.process.action.DisableProcessAction;
import org.bonitasoft.console.client.admin.process.action.EnableProcessAction;
import org.bonitasoft.web.rest.model.bpm.process.CategoryDefinition;
import org.bonitasoft.web.rest.model.bpm.process.CategoryItem;
import org.bonitasoft.web.rest.model.bpm.process.ProcessDefinition;
import org.bonitasoft.web.rest.model.bpm.process.ProcessItem;
import org.bonitasoft.web.toolkit.client.data.api.request.APISearchRequest;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.DateAttributeReader;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.DescriptionAttributeReader;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.ActionShowPopup;
import org.bonitasoft.web.toolkit.client.ui.action.CheckValidSessionBeforeAction;
import org.bonitasoft.web.toolkit.client.ui.component.Clickable;
import org.bonitasoft.web.toolkit.client.ui.component.Link;
import org.bonitasoft.web.toolkit.client.ui.component.Title;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTable;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemQuickDetailsPage;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingFilter;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingPage;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingResourceFilter;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingSort;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingTable;
import org.bonitasoft.web.toolkit.client.ui.utils.DateFormat;

/**
 * @author Haojie Yuan, Zhiheng Yang
 */
public class ProcessListingAdminPage extends ItemListingPage<ProcessItem> {

    public static final String TOKEN = "processlistingadmin";

    public static final List<String> PRIVILEGES = Arrays.asList(ProcessListingAdminPage.TOKEN, "reportlistingadminext");

    private static final String TABLE_NO_ACTION = "noactionprocesses";
    private static final String TABLE_ACTION_DISABLE = "actiondisableprocesses";
    protected static final String TABLE_ACTION_ENABLE = "actionenableprocesses";
    protected static final String TABLE_ACTION_DELETE = "actiondeleteprocesses";

    @Override
    public void defineTitle() {
        this.setTitle(_("App"));
    }

    @Override
    protected List<Clickable> defineFilterPanelActions() {
        return asList(installNewAppLink());
    }

    private Clickable installNewAppLink() {
        return new Link(_("Install"), _("Opens a popup to install an app"),
                new CheckValidSessionBeforeAction(new ActionShowPopup(new UploadProcessPage())));
    }

    @Override
    protected LinkedList<ItemListingFilter> definePrimaryFilters() {
        final LinkedList<ItemListingFilter> primaryFilters = new LinkedList<ItemListingFilter>();
        primaryFilters.add(newEnabledProcessesFilter());
        primaryFilters.add(newDisabledProcessesFilter());
        primaryFilters.add(newResolvedProcessesFilter());
        primaryFilters.add(newUnresolvedProcessesFilter());
        return primaryFilters;
    }

    private ItemListingFilter newEnabledProcessesFilter() {
        return new ItemListingFilter("enabledprocesses", _("Enabled"), _("Enabled Apps"), TABLE_ACTION_DISABLE)
                .addFilter(ATTRIBUTE_ACTIVATION_STATE, VALUE_ACTIVATION_STATE_ENABLED);
    }

    private ItemListingFilter newDisabledProcessesFilter() {
        return new ItemListingFilter("disabledprocesses", _("Disabled"), _("Disabled Apps"), TABLE_ACTION_DELETE)
                .addFilter(ATTRIBUTE_ACTIVATION_STATE, VALUE_ACTIVATION_STATE_DISABLED);
    }

    private ItemListingFilter newResolvedProcessesFilter() {
        return new ItemListingFilter("resolvedprocesses", _("Resolved"), _("Resolved Apps"), TABLE_ACTION_ENABLE)
                .addFilter(ATTRIBUTE_ACTIVATION_STATE, VALUE_ACTIVATION_STATE_DISABLED)
                .addFilter(ProcessItem.ATTRIBUTE_CONFIGURATION_STATE, ProcessItem.VALUE_CONFIGURATION_STATE_RESOLVED);
    }

    private ItemListingFilter newUnresolvedProcessesFilter() {
        return new ItemListingFilter("unresolvedprocesses", _("Unresolved"), _("Unresolved Apps"), TABLE_ACTION_DELETE)
                .addFilter(ATTRIBUTE_ACTIVATION_STATE, VALUE_ACTIVATION_STATE_DISABLED)
                .addFilter(ProcessItem.ATTRIBUTE_CONFIGURATION_STATE, ProcessItem.VALUE_CONFIGURATION_STATE_UNRESOLVED);
    }

    @Override
    protected ItemListingResourceFilter defineResourceFilters() {
        final ItemListingResourceFilter categories = new ItemListingResourceFilter(
                new APISearchRequest(Definitions.get(CategoryDefinition.TOKEN)),
                CategoryItem.ATTRIBUTE_NAME, "default-image" /* fake attribute to display default image */, TABLE_NO_ACTION)
                .addFilterMapping(ProcessItem.FILTER_CATEGORY_ID, CategoryItem.ATTRIBUTE_ID);
        return categories;
    }

    @Override
    protected Title defineResourceFiltersTitle() {
        return new Title(_("Categories"));
    }

    @Override
    protected ItemListingSort defineDefaultSort() {
        return new ItemListingSort(ProcessItem.ATTRIBUTE_DISPLAY_NAME, true);

    }

    @Override
    protected LinkedList<ItemListingTable> defineTables() {
        final LinkedList<ItemListingTable> tables = new LinkedList<ItemListingTable>();
        tables.add(buildEnabledProcessesTable());
        tables.add(buildDisabledProcessesTable());
        tables.add(buildCategoryProcessesTable());
        tables.add(buildDeletableProcessesTable());
        return tables;
    }

    private ItemListingTable buildEnabledProcessesTable() {
        return new ItemListingTable(new JsId(TABLE_ACTION_DISABLE),
                _("Enabled"),
                createItemTable()
                        .addGroupedAction(
                                new JsId("disable"), _("Disable"), _("Disable selected apps"), new DisableProcessAction()),
                getQuickDetailsTargetPage());
    }

    private ItemListingTable buildCategoryProcessesTable() {
        return new ItemListingTable(new JsId(TABLE_NO_ACTION),
                _("Enabled"), createItemTable(),
                getQuickDetailsTargetPage());
    }

    protected ItemListingTable buildDisabledProcessesTable() {
        return new ItemListingTable(new JsId(TABLE_ACTION_ENABLE),
                _("Disabled"),
                createItemTable()
                        .addGroupedAction(
                                new JsId("enable"), _("Enable"), _("Enable selected apps"), new EnableProcessAction())
                        .addGroupedMultipleDeleteAction(_("Delete selected apps"), ProcessDefinition.get(), _("app"), _("apps")),
                getQuickDetailsTargetPage());
    }

    protected ItemQuickDetailsPage<ProcessItem> getQuickDetailsTargetPage() {
        return new ProcessQuickDetailsAdminPage();
    }

    protected ItemListingTable buildDeletableProcessesTable() {
        return new ItemListingTable(new JsId(TABLE_ACTION_DELETE),
                _("Deletable"),
                createItemTable()
                        .addGroupedMultipleDeleteAction(_("Delete selected apps"), ProcessDefinition.get(), _("app"), _("apps")),
                getQuickDetailsTargetPage());
    }

    protected ItemTable createItemTable() {
        return new ItemTable(Definitions.get(ProcessDefinition.TOKEN))
                .addColumn(ProcessItem.ATTRIBUTE_DISPLAY_NAME, _("Name"), true)
                .addColumn(ProcessItem.ATTRIBUTE_VERSION, _("Version"))
                .addColumn(ProcessItem.ATTRIBUTE_CONFIGURATION_STATE,
                        _("State"))
                .addColumn(
                        new DescriptionAttributeReader(
                                ProcessItem.ATTRIBUTE_DISPLAY_DESCRIPTION,
                                ProcessItem.ATTRIBUTE_DESCRIPTION),
                        _("Description"), false)

                .addColumn(
                        new DateAttributeReader(
                                ProcessItem.ATTRIBUTE_DEPLOYMENT_DATE,
                                DateFormat.FORMAT.FORM),
                        _("Installed on"))
                .addColumn(ProcessItem.ATTRIBUTE_DISPLAY_DESCRIPTION,
                        _("Description"));
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }
}
