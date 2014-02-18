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
package org.bonitasoft.console.client.admin.bpm.task.view;

import static java.util.Arrays.asList;
import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bonitasoft.console.client.admin.bpm.cases.view.CaseListingAdminPage;
import org.bonitasoft.console.client.admin.bpm.task.action.TaskSkipAction;
import org.bonitasoft.console.client.admin.process.view.ProcessListingAdminPage;
import org.bonitasoft.console.client.common.component.snippet.CommentSectionSnippet;
import org.bonitasoft.console.client.common.metadata.MetadataTaskBuilder;
import org.bonitasoft.console.client.user.task.action.TaskRelaseAction;
import org.bonitasoft.web.rest.model.bpm.flownode.ArchivedFlowNodeDefinition;
import org.bonitasoft.web.rest.model.bpm.flownode.FlowNodeDefinition;
import org.bonitasoft.web.rest.model.bpm.flownode.IActivityItem;
import org.bonitasoft.web.rest.model.bpm.flownode.IFlowNodeItem;
import org.bonitasoft.web.rest.model.bpm.flownode.IHumanTaskItem;
import org.bonitasoft.web.rest.model.bpm.flownode.IUserTaskItem;
import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.common.url.UrlOption;
import org.bonitasoft.web.toolkit.client.common.util.StringUtil;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.api.callback.APIUpdateCallback;
import org.bonitasoft.web.toolkit.client.ui.CssClass;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.action.ActionShowPopup;
import org.bonitasoft.web.toolkit.client.ui.component.Link;
import org.bonitasoft.web.toolkit.client.ui.component.Section;
import org.bonitasoft.web.toolkit.client.ui.component.button.ButtonAction;
import org.bonitasoft.web.toolkit.client.ui.component.button.ButtonBack;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTable;
import org.bonitasoft.web.toolkit.client.ui.page.PageOnItem;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemDetailsAction;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemDetailsMetadata;
import org.bonitasoft.web.toolkit.client.ui.utils.DateFormat.FORMAT;

/**
 * @author Vincent Elcrin
 */
public class TaskMoreDetailsAdminPage extends ArchivableItemDetailsPage<IFlowNodeItem> {

    public final static String TOKEN = "taskmoredetailsadmin";

    public static final List<String> PRIVILEGES = new ArrayList<String>();

    static {
        PRIVILEGES.add(TaskListingAdminPage.TOKEN);
        PRIVILEGES.add(CaseListingAdminPage.TOKEN);
        PRIVILEGES.add(ProcessListingAdminPage.TOKEN);
        PRIVILEGES.add("reportlistingadminext");
    }

    /**
     * Default Constructor.
     * Set item definition as well as archived item definition.
     */
    public TaskMoreDetailsAdminPage() {
        super(FlowNodeDefinition.get(), ArchivedFlowNodeDefinition.get());
        addClass(CssClass.MORE_DETAILS);
    }

    /**
     * Default Constructor.
     * Constructor which set archived parameter of the page.
     * 
     * @param archived
     */
    public TaskMoreDetailsAdminPage(final boolean archived) {
        this();
        setArchive(archived);
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    protected LinkedList<ItemDetailsAction> defineActions(final IFlowNodeItem flowNode) {
        addToolbarLink(new ButtonBack());

        if (!isArchived()) {
            if (flowNode.isHumanTask()) {
                if (StringUtil.isBlank(flowNode.getAttributeValue(IHumanTaskItem.ATTRIBUTE_ASSIGNED_USER_ID))) {
                    addToolbarLink(assignButton(flowNode.getId()));
                } else {
                    if (!flowNode.getType().equals(IUserTaskItem.VALUE_TYPE_MANUAL_TASK)) {
                        addToolbarLink(unasignButton(flowNode.getId()));
                    }
                }
            }
            if (IFlowNodeItem.VALUE_STATE_FAILED.equals(flowNode.getState()) && flowNode.isActivity()) {
                addToolbarLink(skipButton(flowNode.getId()));
            }
        }
        return new LinkedList<ItemDetailsAction>();
    }

    private ActionShowPopup newAssignTaskToUserAction(final APIID taskId) {
        final SelectUserAndAssignTaskPage page = new SelectUserAndAssignTaskPage();
        page.addParameter(PageOnItem.PARAMETER_ITEM_ID, taskId.toString());
        return new ActionShowPopup(page);
    }

    private Link skipButton(final APIID flowNodeId) {
        return new ButtonAction("btn-skip", _("Skip"), _("Skip this task"), createTaskSkipAction(flowNodeId));
    }

    private Link unasignButton(final APIID taskId) {
        return new ButtonAction("btn-unassign", _("Unassign"), _("Unassign this task. Other allowed users will see it"), new TaskRelaseAction(asList(taskId)));
    }

    private Link assignButton(final APIID taskId) {
        return new ButtonAction("btn-assign", _("Assign"), _("Assign task to someone"), newAssignTaskToUserAction(taskId));
    }

    private Action createTaskSkipAction(final APIID flowNodeId) {
        return new TaskSkipAction(flowNodeId, new APIUpdateCallback() {

            @Override
            public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
                final TaskListingAdminPage taskListingAdminPage = new TaskListingAdminPage();
                taskListingAdminPage.selectResourceFilter(APIID.makeAPIID(TaskListingAdminPage.FILTER_PRIMARY_FAILED));
                ViewController.showView(taskListingAdminPage);
            }

            @Override
            protected void on404NotFound(final String message) {
                showFailedTaskListingPage();
            }
        });
    }

    private void showFailedTaskListingPage() {
        ViewController.showView(TaskListingAdminPage.TOKEN, Collections.singletonMap(UrlOption.FILTER, TaskListingAdminPage.FILTER_PRIMARY_FAILED));
    }

    @Override
    protected void defineTitle(final IFlowNodeItem item) {
        this.setTitle(item.getDisplayName());

        String displayDescription = item.getDisplayDescription();
        if (StringUtil.isBlank(displayDescription)) {
            displayDescription = item.getDescription();
        }
        addDescription(StringUtil.isBlank(displayDescription) ? _("No description.") : displayDescription);

    }

    @Override
    protected boolean isDescriptionBeforeMetadatas() {
        return false;
    }

    @Override
    protected LinkedList<ItemDetailsMetadata> defineMetadatas(final IFlowNodeItem task) {
        final MetadataTaskBuilder metadatas = new MetadataTaskBuilder();
        metadatas.addCaseId(task, true);
        metadatas.addAppsName();
        metadatas.addAppsVersion();
        metadatas.addType();
        metadatas.addState();
        metadatas.addPriority();
        if (task.isHumanTask()) {
            metadatas.addAssignedTo();
            if (isArchived()) {
                metadatas.addExecutedBy();
            }
        }
        metadatas.addDueDate(getArchivedDateFormat());
        metadatas.addLastUpdateDate(FORMAT.DISPLAY);
        metadatas.addAssignedDate(FORMAT.DISPLAY);
        return metadatas.build();
    }

    protected FORMAT getArchivedDateFormat() {
        return isArchived() ? FORMAT.DISPLAY : FORMAT.DISPLAY_RELATIVE;
    }

    @Override
    protected void buildBody(final IFlowNodeItem item) {
        addBody(createConnectorSection(item));
        addBody(createCommentsSection(item));
    }

    protected Section createCommentsSection(final IFlowNodeItem item) {
        return new CommentSectionSnippet(item.getCaseId()).setNbLinesByPage(10).build();
    }

    protected Section createConnectorSection(final IFlowNodeItem item) {
        if (item.isArchived()) {
            return new ArchivedConnectorInstanceSectionSnippet(item).setNbLinesByPage(10).build();
        } else {
            return new Section(_("Connectors")).addBody(createConnectorInstanceTable(item).setNbLinesByPage(10));
        }
    }

    /**
     * Overridden in SP
     */
    protected ItemTable createConnectorInstanceTable(final IFlowNodeItem item) {
        return new ConnectorInstanceTable(item);
    }

    @Override
    protected List<String> defineDeploys() {
        final List<String> deploys = new ArrayList<String>(super.defineDeploys());
        deploys.add(IActivityItem.ATTRIBUTE_PROCESS_ID);
        deploys.add(IActivityItem.ATTRIBUTE_EXECUTED_BY_USER_ID);
        deploys.add(IHumanTaskItem.ATTRIBUTE_ASSIGNED_USER_ID);
        return deploys;
    }
}
