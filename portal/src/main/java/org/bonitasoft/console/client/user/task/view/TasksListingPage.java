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
package org.bonitasoft.console.client.user.task.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bonitasoft.console.client.admin.bpm.task.view.TaskListingAdminPage;
import org.bonitasoft.console.client.admin.process.view.ProcessListingAdminPage;
import org.bonitasoft.console.client.angular.AngularIFrameView;
import org.bonitasoft.console.client.common.formatter.OverdueDateCellFormatter;
import org.bonitasoft.console.client.user.application.view.ProcessListingPage;
import org.bonitasoft.console.client.user.cases.view.CaseListingPage;
import org.bonitasoft.console.client.user.task.action.TaskClaimAction;
import org.bonitasoft.console.client.user.task.action.TaskRelaseAction;
import org.bonitasoft.web.rest.model.bpm.flownode.ArchivedHumanTaskDefinition;
import org.bonitasoft.web.rest.model.bpm.flownode.ArchivedHumanTaskItem;
import org.bonitasoft.web.rest.model.bpm.flownode.HumanTaskDefinition;
import org.bonitasoft.web.rest.model.bpm.flownode.HumanTaskItem;
import org.bonitasoft.web.rest.model.bpm.flownode.TaskItem;
import org.bonitasoft.web.rest.model.bpm.process.ProcessDefinition;
import org.bonitasoft.web.rest.model.bpm.process.ProcessItem;
import org.bonitasoft.web.toolkit.client.Session;
import org.bonitasoft.web.toolkit.client.data.api.request.APISearchRequest;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.DateAttributeReader;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.FlowNodeContextAttributeReader;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.component.Link;
import org.bonitasoft.web.toolkit.client.ui.component.Title;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTable;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.AppResourceFilter;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingFilter;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingPage;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingResourceFilter;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingSort;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingTable;

/**
 * @author Séverin Moussel
 */
public class TasksListingPage extends ItemListingPage<HumanTaskItem> implements PluginTask {

    public static final String TOKEN = "tasklistinguser";

    public static final List<String> PRIVILEGES = new ArrayList<String>();

    static {
        PRIVILEGES.add(TasksListingPage.TOKEN);
        PRIVILEGES.add(TaskListingAdminPage.TOKEN); // FIX ME: we should create a humantaskmoredetails admin page so ill never need this
        PRIVILEGES.add(CaseListingPage.TOKEN);
        PRIVILEGES.add(AngularIFrameView.CASE_LISTING_ADMIN_TOKEN);
        PRIVILEGES.add(ProcessListingPage.TOKEN);
        PRIVILEGES.add(ProcessListingAdminPage.TOKEN);
        PRIVILEGES.add("reportlistingadminext");
    }

    private static String TABLE_AVAILABLE = "available";

    private static String TABLE_UNASSIGNED = "unassigned";

    private static String TABLE_ASSIGNED = "assigned";

    private static String TABLE_PERFORMED = "performed";

    private static String TABLE_IGNORED = "ignored";

    public static final String FILTER_AVAILABLE = "available";

    public static final String FILTER_ASSIGNED = "assigned";

    public static final String FILTER_UNASSIGNED = "unassigned";

    public static final String FILTER_PERFORMED = "performed";

    public static final String FILTER_IGNORED = "ignored";

    private final TaskButtonFactory taskButtonFactory = new TaskButtonFactory();

    @Override
    public void defineTitle() {
        this.setTitle(_("Tasks"));
    }

    @Override
    protected LinkedList<ItemListingTable> defineTables() {
        final LinkedList<ItemListingTable> tables = new LinkedList<ItemListingTable>();
        tables.add(availableTable());
        tables.add(unassignedTable());
        tables.add(assignedTable());
        tables.add(performedTasksTable());
        return tables;
    }

    private ItemListingTable availableTable() {
        return new ItemListingTable(new JsId(TABLE_AVAILABLE), _("Available"), availableItemTable(), new HumanTaskQuickDetailsPage());

    }

    private ItemTable availableItemTable() {
        final ItemTable table = buildItemTableColumns();
        table.addHiddenFilter(HumanTaskItem.ATTRIBUTE_STATE, HumanTaskItem.VALUE_STATE_READY);
        table.addAction(newRefreshButton(table));
        table.addGroupedAction(newAssignToMeButton());
        table.addGroupedAction(newUnasignButton());
        return table;
    }

    private ItemListingTable unassignedTable() {
        return new ItemListingTable(new JsId(TABLE_UNASSIGNED), _("Unassigned"), unasignedItemTable(), new HumanTaskQuickDetailsPage());
    }

    private ItemTable unasignedItemTable() {
        final ItemTable table = buildItemTableColumns();
        table.addHiddenFilter(HumanTaskItem.ATTRIBUTE_STATE, HumanTaskItem.VALUE_STATE_READY);
        table.addAction(newRefreshButton(table));
        table.addGroupedAction(newAssignToMeButton());
        return table;
    }

    private ItemListingTable assignedTable() {
        return new ItemListingTable(new JsId(TABLE_ASSIGNED), _("Assigned to me"), assignedItemTable(), new HumanTaskQuickDetailsPage());
    }

    private ItemTable assignedItemTable() {
        final ItemTable table = buildItemTableColumns();
        table.addHiddenFilter(HumanTaskItem.ATTRIBUTE_STATE, HumanTaskItem.VALUE_STATE_READY);
        table.addAction(newRefreshButton(table));
        table.addGroupedAction(newUnasignButton());
        return table;
    }

    private ItemTable buildItemTableColumns() {
        return new ItemTable(Definitions.get(HumanTaskDefinition.TOKEN))
                .addColumn(HumanTaskItem.ATTRIBUTE_PRIORITY, _("Priority"), true)
                .addColumn(HumanTaskItem.ATTRIBUTE_DISPLAY_NAME, _("Name"), true)
                .addColumn(HumanTaskItem.ATTRIBUTE_DUE_DATE, _("Due date"), true, true)
                .addColumn(
                        new FlowNodeContextAttributeReader(HumanTaskItem.ATTRIBUTE_CASE_ID, HumanTaskItem.ATTRIBUTE_ROOT_CONTAINER_ID,
                                ProcessItem.ATTRIBUTE_DISPLAY_NAME), _("Process"))
                .addCellFormatter(HumanTaskItem.ATTRIBUTE_DUE_DATE, new OverdueDateCellFormatter())
                .setOrder(HumanTaskItem.ATTRIBUTE_DUE_DATE, false)
                .setOrder(HumanTaskItem.ATTRIBUTE_PRIORITY, false);
    }

    private ItemListingTable performedTasksTable() {
        return new ItemListingTable(new JsId(TABLE_PERFORMED), _("Performed"), performedTasksItemTable(), new ArchivedHumanTaskQuickDetailsPage());
    }

    private ItemTable performedTasksItemTable() {
        return new ItemTable(Definitions.get(ArchivedHumanTaskDefinition.TOKEN))
                .addHiddenFilter(HumanTaskItem.ATTRIBUTE_ASSIGNED_USER_ID, Session.getUserId())
                .addColumn(ArchivedHumanTaskItem.ATTRIBUTE_DISPLAY_NAME, _("Name"), true)
                .addColumn(new DateAttributeReader(ArchivedHumanTaskItem.ATTRIBUTE_REACHED_STATE_DATE), _("Performed date"), true)
                .addColumn(
                        new FlowNodeContextAttributeReader(HumanTaskItem.ATTRIBUTE_CASE_ID, ArchivedHumanTaskItem.ATTRIBUTE_ROOT_CONTAINER_ID,
                                ProcessItem.ATTRIBUTE_DISPLAY_NAME), _("Process"))
                .setOrder(ArchivedHumanTaskItem.ATTRIBUTE_REACHED_STATE_DATE, false);
    }

    private Link newRefreshButton(final ItemTable table) {
        return taskButtonFactory.createRefreshButton(new Action() {

            @Override
            public void execute() {
                table.setPage(0);
                table.setDefaultSelectedLine(0);
                table.setDefaultSelectedId(null);
                table.setSearch("");
                table.refresh();
                TasksListingPage.this.tablesSearch.reset();
            }
        });
    }

    private Link newAssignToMeButton() {
        return taskButtonFactory.createClaimButton(new TaskClaimAction(Session.getUserId()));
    }

    private Link newUnasignButton() {
        return taskButtonFactory.createUnassignedButton(new TaskRelaseAction());
    }

    @Override
    protected Title defineResourceFiltersTitle() {
        final Title title = new Title(_("Processes"));
        title.setTooltip(_("Select a process in the list below to display the available tasks"));
        return title;
    }

    @Override
    protected ItemListingSort defineDefaultSort() {
        return new ItemListingSort(TaskItem.ATTRIBUTE_DISPLAY_NAME, true);
    }

    @Override
    protected Title definePrimaryFiltersTitle() {
        return new Title(_("Status"));
    }

    @Override
    protected LinkedList<ItemListingFilter> definePrimaryFilters() {
        final LinkedList<ItemListingFilter> filters = new LinkedList<ItemListingFilter>();
        filters.add(toDoFilter());
        filters.add(myTasksFilter());
        filters.add(availableTasksFilter());
        return filters;
    }

    private ItemListingFilter myTasksFilter() {
        return new ItemListingFilter(FILTER_ASSIGNED, _("My tasks"), _("Tasks assigned to me"), TABLE_ASSIGNED)
                .addFilter(HumanTaskItem.FILTER_USER_ID, Session.getUserId())
                .addFilter(HumanTaskItem.ATTRIBUTE_ASSIGNED_USER_ID, Session.getUserId());
    }

    private ItemListingFilter availableTasksFilter() {
        return new ItemListingFilter(FILTER_UNASSIGNED, _("Available tasks"), _("Unassigned tasks I can do"), TABLE_UNASSIGNED)
                .addFilter(HumanTaskItem.FILTER_USER_ID, Session.getUserId())
                .addFilter(HumanTaskItem.ATTRIBUTE_ASSIGNED_USER_ID, "0");
    }

    private ItemListingFilter toDoFilter() {
        return new ItemListingFilter(FILTER_AVAILABLE, _("To do"), _("Tasks I can do"), TABLE_AVAILABLE)
                .addFilter(HumanTaskItem.FILTER_USER_ID, Session.getUserId());
    }

    @Override
    protected LinkedList<ItemListingFilter> defineSecondaryFilters() {
        final LinkedList<ItemListingFilter> filters = new LinkedList<ItemListingFilter>();
        filters.add(doneFilter());
        return filters;
    }

    private ItemListingFilter doneFilter() {
        return new ItemListingFilter(FILTER_PERFORMED, _("Done"), _("Display tasks I have done"), TABLE_PERFORMED);
    }

    @Override
    protected ItemListingResourceFilter defineResourceFilters() {
        return new AppResourceFilter(
                new APISearchRequest(Definitions.get(ProcessDefinition.TOKEN)).addFilter(ProcessItem.FILTER_USER_ID, Session.getUserId().toString())
                        .addFilter(ProcessItem.FILTER_FOR_PENDING_OR_ASSIGNED_TASKS, "true"),
                TABLE_AVAILABLE)
                .addFilterMapping(HumanTaskItem.ATTRIBUTE_PROCESS_ID, ProcessItem.ATTRIBUTE_ID)
                .addFilter(HumanTaskItem.FILTER_USER_ID, Session.getUserId());
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
