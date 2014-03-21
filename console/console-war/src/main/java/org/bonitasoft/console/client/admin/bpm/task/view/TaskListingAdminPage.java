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
package org.bonitasoft.console.client.admin.bpm.task.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bonitasoft.console.client.admin.bpm.task.view.formatter.PriorityCssCellFormatter;
import org.bonitasoft.console.client.common.formatter.FlowNodeIconFormatter;
import org.bonitasoft.console.client.common.formatter.OverdueDateCellFormatter;
import org.bonitasoft.console.client.user.task.action.TaskRelaseAction;
import org.bonitasoft.web.rest.model.bpm.cases.CaseItem;
import org.bonitasoft.web.rest.model.bpm.flownode.ActivityItem;
import org.bonitasoft.web.rest.model.bpm.flownode.ArchivedActivityItem;
import org.bonitasoft.web.rest.model.bpm.flownode.ArchivedHumanTaskItem;
import org.bonitasoft.web.rest.model.bpm.flownode.ArchivedTaskDefinition;
import org.bonitasoft.web.rest.model.bpm.flownode.ArchivedTaskItem;
import org.bonitasoft.web.rest.model.bpm.flownode.FlowNodeDefinition;
import org.bonitasoft.web.rest.model.bpm.flownode.FlowNodeItem;
import org.bonitasoft.web.rest.model.bpm.flownode.HumanTaskDefinition;
import org.bonitasoft.web.rest.model.bpm.flownode.HumanTaskItem;
import org.bonitasoft.web.rest.model.bpm.flownode.TaskItem;
import org.bonitasoft.web.rest.model.bpm.process.ProcessDefinition;
import org.bonitasoft.web.rest.model.bpm.process.ProcessItem;
import org.bonitasoft.web.rest.model.identity.UserItem;
import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.common.TreeIndexed;
import org.bonitasoft.web.toolkit.client.common.TreeNode;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.api.request.APISearchRequest;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.DateAttributeReader;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.FlowNodeContextAttributeReader;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.ActionOnItemIds;
import org.bonitasoft.web.toolkit.client.ui.component.Title;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTable;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingFilter;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingPage;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingResourceFilter;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingSort;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingTable;
import org.bonitasoft.web.toolkit.client.ui.utils.DateFormat.FORMAT;

/**
 * @author Vincent Elcrin
 * 
 */
public class TaskListingAdminPage extends ItemListingPage<CaseItem> {

    public static final String TOKEN = "tasklistingadmin";

    public static final List<String> PRIVILEGES = new ArrayList<String>();

    static {
        PRIVILEGES.add(TaskListingAdminPage.TOKEN);
    }

    public static final String TABLE_ALL = "all";

    public static final String TABLE_HUMAN_TASK = "humantask";

    public static final String TABLE_HISTORY = "history";

    public static final String FILTER_PRIMARY_HUMAN_TASK = "humanTask";

    public static final String FILTER_PRIMARY_FAILED = "failed";

    public static final String FILTER_PRIMARY_PERFORMED = "performed";

    @Override
    public void defineTitle() {
        setTitle(_("Tasks"));
    }

    @Override
    protected LinkedList<ItemListingFilter> definePrimaryFilters() {
        final LinkedList<ItemListingFilter> filters = new LinkedList<ItemListingFilter>();
        filters.add(humanFilter());
        filters.add(failedFilter());
        filters.add(performedFilter());
        return filters;
    }

    private ItemListingFilter humanFilter() {
        return new ItemListingFilter(FILTER_PRIMARY_HUMAN_TASK, _("Pending"), _("Tasks "), TABLE_HUMAN_TASK).addFilter(TaskItem.ATTRIBUTE_STATE,
                TaskItem.VALUE_STATE_READY);
    }

    private ItemListingFilter failedFilter() {
        return new ItemListingFilter(FILTER_PRIMARY_FAILED, _("Failed"), _("Tasks "), TABLE_ALL).addFilter(TaskItem.ATTRIBUTE_STATE,
                TaskItem.VALUE_STATE_FAILED);
    }

    private ItemListingFilter performedFilter() {
        return new ItemListingFilter(FILTER_PRIMARY_PERFORMED, _("Done"), _("Tasks "), TABLE_HISTORY);
    }

    @Override
    protected Title defineResourceFiltersTitle() {
        return new Title(_("Apps"));
    }

    @Override
    protected ItemListingResourceFilter defineResourceFilters() {
        return new ItemListingResourceFilter(new APISearchRequest(ProcessDefinition.get()),
                ProcessItem.ATTRIBUTE_DISPLAY_NAME, ProcessItem.ATTRIBUTE_ICON, TABLE_ALL)
                .addFilterMapping(TaskItem.ATTRIBUTE_PROCESS_ID, ProcessItem.ATTRIBUTE_ID);
    }

    @Override
    protected ItemListingSort defineDefaultSort() {
        return new ItemListingSort(TaskItem.ATTRIBUTE_DISPLAY_NAME, true);
    }

    @Override
    protected LinkedList<ItemListingTable> defineTables() {
        final LinkedList<ItemListingTable> tables = new LinkedList<ItemListingTable>();
        tables.add(createAllListingTable());
        tables.add(createHumanTaskListingTable());
        tables.add(createArchivedListingTable());
        return tables;
    }

    private ItemListingTable createAllListingTable() {
        return new ItemListingTable(new JsId(TABLE_ALL), _("All"), new ItemTable(FlowNodeDefinition.get())

                        // columns configuration
                        .addColumn(HumanTaskItem.ATTRIBUTE_PRIORITY, _("Priority"), false)
                        .addColumn(ActivityItem.ATTRIBUTE_DISPLAY_NAME, _("Name"), true)
                        .addColumn(new FlowNodeContextAttributeReader(FlowNodeItem.ATTRIBUTE_CASE_ID, FlowNodeItem.ATTRIBUTE_ROOT_CONTAINER_ID, ProcessItem.ATTRIBUTE_DISPLAY_NAME), _("App"))
                        .addColumn(new AssignedUserIconAttribeReader(), _("Icon"))
                        .addColumn(new DateAttributeReader(HumanTaskItem.ATTRIBUTE_DUE_DATE, FORMAT.DISPLAY_RELATIVE), _("Due date"), false)

                        // cell formatters
                        .addCellFormatter(HumanTaskItem.ATTRIBUTE_ASSIGNED_USER_ID + "_" + UserItem.ATTRIBUTE_ICON,
                                new FlowNodeIconFormatter(UserItem.DEFAULT_USER_ICON))
                        .addCellFormatter(HumanTaskItem.ATTRIBUTE_PRIORITY, new PriorityCssCellFormatter())
                        .addCellFormatter(HumanTaskItem.ATTRIBUTE_DUE_DATE, new OverdueDateCellFormatter())
                ,new TaskQuickDetailsAdminPage());
    }

    private ItemListingTable createHumanTaskListingTable() {
        return new ItemListingTable(new JsId(TABLE_HUMAN_TASK), _("Human Task"),
                new ItemTable(HumanTaskDefinition.get())

                        // columns configuration
                        .addColumn(HumanTaskItem.ATTRIBUTE_PRIORITY, _("Priority"), true)
                        .addColumn(HumanTaskItem.ATTRIBUTE_DISPLAY_NAME, _("Name"), true)
                        .addColumn(new FlowNodeContextAttributeReader(HumanTaskItem.ATTRIBUTE_CASE_ID, HumanTaskItem.ATTRIBUTE_ROOT_CONTAINER_ID, ProcessItem.ATTRIBUTE_DISPLAY_NAME), _("App"))
                        .addColumn(new DateAttributeReader(HumanTaskItem.ATTRIBUTE_DUE_DATE, FORMAT.DISPLAY_RELATIVE), _("Due date"), true, true)
                        .addColumn(new AssignedUserIconAttribeReader(), _("Icon"))

                        // Grouped actions
                        .addGroupedAction(new JsId("assign"), _("Assign"), _("Assign task to someone"), onAssignClick())
                        .addGroupedAction(new JsId("unassign"), _("Unassign"), _("Unassign this task. Other allowed users will see it"),
                                new TaskRelaseAction()).addAttributeToCheckForGroupedActions(HumanTaskItem.ATTRIBUTE_TYPE, "MANUAL_TASK")

                        // cell formatters
                        .addCellFormatter(HumanTaskItem.ATTRIBUTE_ASSIGNED_USER_ID + "_" + UserItem.ATTRIBUTE_ICON,
                                new FlowNodeIconFormatter(UserItem.DEFAULT_USER_ICON))
                        .addCellFormatter(HumanTaskItem.ATTRIBUTE_DUE_DATE, new OverdueDateCellFormatter())

                        .setOrder(HumanTaskItem.ATTRIBUTE_DUE_DATE, false)
                ,
                new TaskQuickDetailsAdminPage());
    }

    private ItemListingTable createArchivedListingTable() {
        return new ItemListingTable(new JsId(TABLE_HISTORY), _("All"),
                new ItemTable(ArchivedTaskDefinition.get())

                        .addColumn(ArchivedHumanTaskItem.ATTRIBUTE_PRIORITY, _("Priority"), false)
                        .addColumn(ArchivedActivityItem.ATTRIBUTE_DISPLAY_NAME, _("Name"), true)
                        .addColumn(new FlowNodeContextAttributeReader(FlowNodeItem.ATTRIBUTE_CASE_ID, ArchivedActivityItem.ATTRIBUTE_ROOT_CONTAINER_ID, ProcessItem.ATTRIBUTE_DISPLAY_NAME), _("App"))
                        .addColumn(new AssignedUserIconAttribeReader(), _("Icon"))
                        .addColumn(new DateAttributeReader(ArchivedTaskItem.ATTRIBUTE_REACHED_STATE_DATE), _("Performed date"), true)

                        .setOrder(ArchivedTaskItem.ATTRIBUTE_REACHED_STATE_DATE, false)

                        .addCellFormatter(ArchivedHumanTaskItem.ATTRIBUTE_ASSIGNED_USER_ID + "_" + UserItem.ATTRIBUTE_ICON,
                                new FlowNodeIconFormatter(UserItem.DEFAULT_USER_ICON))
                        .addCellFormatter(HumanTaskItem.ATTRIBUTE_PRIORITY, new PriorityCssCellFormatter())
                        .addCellFormatter(HumanTaskItem.ATTRIBUTE_DUE_DATE, new OverdueDateCellFormatter())
                ,
                new TaskQuickDetailsAdminPage(true));
    }

    /**
     * Executed action on assign button click
     * 
     * @return
     */
    protected ActionOnItemIds onAssignClick() {
        return new ActionOnItemIds() {

            @Override
            protected void execute(final List<APIID> itemIds) {
                // set items selected and show assign popup
                final TreeIndexed<String> parameters = TaskListingAdminPage.this.getParameters();
                parameters.addNode("id", new TreeNode<String>().addValues(convertAPIIDListIntoStringList(itemIds)));
                ViewController.showPopup(SelectUserAndAssignTaskPage.TOKEN, parameters);
            }

            /**
             * Convert an APIID list into a string list
             * 
             * @param itemIds
             * @return
             */
            private List<String> convertAPIIDListIntoStringList(final List<APIID> itemIds) {
                final List<String> convertedList = new ArrayList<String>();
                for (final APIID id : itemIds) {
                    convertedList.add(id.toString());
                }
                return convertedList;
            }

        };
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }
}
