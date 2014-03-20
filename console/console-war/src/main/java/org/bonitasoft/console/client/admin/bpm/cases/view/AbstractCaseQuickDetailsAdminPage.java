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
package org.bonitasoft.console.client.admin.bpm.cases.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bonitasoft.console.client.admin.bpm.cases.filler.AttachmentsFiller;
import org.bonitasoft.console.client.admin.bpm.cases.filler.LastExecutedTaskFiller;
import org.bonitasoft.console.client.admin.bpm.cases.filler.OpenTasksFiller;
import org.bonitasoft.console.client.admin.bpm.task.view.TaskListingAdminPage;
import org.bonitasoft.console.client.admin.bpm.task.view.TaskMoreDetailsAdminPage;
import org.bonitasoft.console.client.common.formatter.FlowNodeDisplayNameFormatter;
import org.bonitasoft.console.client.data.item.attribute.reader.DeployedUserReader;
import org.bonitasoft.console.client.user.task.view.more.HumanTaskMoreDetailsPage;
import org.bonitasoft.web.rest.model.bpm.cases.ArchivedCaseDefinition;
import org.bonitasoft.web.rest.model.bpm.cases.CaseDefinition;
import org.bonitasoft.web.rest.model.bpm.cases.CaseItem;
import org.bonitasoft.web.rest.model.bpm.flownode.ActivityDefinition;
import org.bonitasoft.web.rest.model.bpm.flownode.ActivityItem;
import org.bonitasoft.web.rest.model.bpm.flownode.ArchivedTaskItem;
import org.bonitasoft.web.rest.model.bpm.flownode.FlowNodeDefinition;
import org.bonitasoft.web.rest.model.bpm.flownode.TaskDefinition;
import org.bonitasoft.web.rest.model.bpm.flownode.TaskItem;
import org.bonitasoft.web.rest.model.bpm.process.ProcessItem;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.DeployedAttributeReader;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.DescriptionAttributeReader;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.RedirectionAction;
import org.bonitasoft.web.toolkit.client.ui.component.Definition;
import org.bonitasoft.web.toolkit.client.ui.component.Link;
import org.bonitasoft.web.toolkit.client.ui.component.Section;
import org.bonitasoft.web.toolkit.client.ui.component.Text;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTable;
import org.bonitasoft.web.toolkit.client.ui.component.table.Table.VIEW_TYPE;
import org.bonitasoft.web.toolkit.client.ui.component.table.formatter.SpanPrepender;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemDetailsMetadata;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemQuickDetailsPage;

/**
 * @author Nicolas Tith
 * 
 */
public abstract class AbstractCaseQuickDetailsAdminPage<T extends CaseItem> extends ItemQuickDetailsPage<T> {

    public AbstractCaseQuickDetailsAdminPage(final boolean archived) {
        super(archived ? Definitions.get(ArchivedCaseDefinition.TOKEN) : Definitions.get(CaseDefinition.TOKEN));
    }

    @Override
    protected void defineTitle(final CaseItem item) {
        setTitle(_("Case id") + ": " + item.getId() + " - App: "
                + item.getDeploy(CaseItem.ATTRIBUTE_PROCESS_ID).getAttributeValue(ProcessItem.ATTRIBUTE_DISPLAY_NAME));
    }

    @Override
    protected List<String> defineDeploys() {
        final List<String> defineDeploys = new ArrayList<String>();
        defineDeploys.add(CaseItem.ATTRIBUTE_STARTED_BY_USER_ID);
        defineDeploys.add(CaseItem.ATTRIBUTE_PROCESS_ID);
        return defineDeploys;
    }

    @Override
    protected LinkedList<ItemDetailsMetadata> defineMetadatas(final T item) {
        final LinkedList<ItemDetailsMetadata> metadatas = new LinkedList<ItemDetailsMetadata>();
        metadatas.add(processVersion());
        metadatas.add(startedOn());
        metadatas.add(startedBy());
        return metadatas;
    }

    protected ItemDetailsMetadata startedBy() {
        return new ItemDetailsMetadata(new DeployedUserReader(CaseItem.ATTRIBUTE_STARTED_BY_USER_ID), _("Started by"),
                _("The user that has started this case"));
    }

    protected ItemDetailsMetadata startedOn() {
        return new ItemDetailsMetadata(CaseItem.ATTRIBUTE_START_DATE, _("Started on"), _("The date while the case has been started"));
    }

    protected ItemDetailsMetadata processVersion() {
        return new ItemDetailsMetadata(new DeployedAttributeReader(CaseItem.ATTRIBUTE_PROCESS_ID, ProcessItem.ATTRIBUTE_VERSION),
                _("App version"), _("The version of the app that created this case"));
    }

    protected abstract String getMoreDetailsPageToken();

    @Override
    protected void buildBody(final CaseItem item) {
        addBody(technicalDetailsSection(item));
        addBody(failedTaskSection(item));
    }

    private Section technicalDetailsSection(final CaseItem item) {
        return new Section(_("Technical details"))
                .addBody(lastExecutedTaskDefinition(item))
                .addBody(numberOfOpenedTasksDefinition(item))
                .addBody(numberOfAttachmentDefinition(item));
    }

    private Definition lastExecutedTaskDefinition(final CaseItem item) {
        final Text lastExecutedTask = new Text(_("No task done"));
        lastExecutedTask.addFiller(new LastExecutedTaskFiller(item));
        return new Definition(_("Last executed task : ", new Arg("last_executed_Task", "")), "%%", lastExecutedTask);
    }

    private Definition numberOfOpenedTasksDefinition(final CaseItem item) {
        return new Definition(_("Number of open tasks : %nb_openTask%", new Arg("nb_openTask", "")), "%%",
                new Link(_("No opened task"), _("Link to the tasks"), TaskListingAdminPage.TOKEN).addFiller(new OpenTasksFiller(item)));
    }

    private Definition numberOfAttachmentDefinition(final CaseItem item) {
        final Text nbAttachments = new Text(_("No attachments"));
        nbAttachments.addFiller(new AttachmentsFiller(item));
        return new Definition(_("Number of attachments : %nb_attachments%", new Arg("nb_attachments", "")), "%%", nbAttachments);
    }

    private Section failedTaskSection(final CaseItem item) {
        final ItemTable failedTasksTable = getFailedTaskTable(item);
        prepareFailedTasksTable(failedTasksTable);
        final Section section = new Section(_("Failed tasks"), failedTasksTable.setView(VIEW_TYPE.VIEW_LIST));
        section.addClass("tasks");
        section.addClass("failed");
        return section;
    }

    protected ItemTable getFailedTaskTable(final CaseItem item) {
        return new ItemTable(new JsId("failedtasks"), TaskDefinition.get())
                .addHiddenFilter(TaskItem.ATTRIBUTE_CASE_ID, item.getId())
                .addHiddenFilter(TaskItem.ATTRIBUTE_STATE, TaskItem.VALUE_STATE_FAILED)
                .addColumn(TaskItem.ATTRIBUTE_DISPLAY_NAME, _("Name"))
                .addColumn(TaskItem.ATTRIBUTE_LAST_UPDATE_DATE, _("Update date"))
                .addColumn(new DeployedUserReader(TaskItem.ATTRIBUTE_EXECUTED_BY_USER_ID), _("Executed by"))
                .addColumn(new DescriptionAttributeReader(TaskItem.ATTRIBUTE_DISPLAY_DESCRIPTION, TaskItem.ATTRIBUTE_DESCRIPTION), _("Description"))
                .addCellFormatter(TaskItem.ATTRIBUTE_DISPLAY_NAME, new FlowNodeDisplayNameFormatter())
                .addCellFormatter(TaskItem.ATTRIBUTE_EXECUTED_BY_USER_ID + "_" + TaskItem.ATTRIBUTE_EXECUTED_BY_USER_ID, new SpanPrepender(_("Executed by:")))
                .addCellFormatter(TaskItem.ATTRIBUTE_LAST_UPDATE_DATE, new SpanPrepender(_("Failed")))
                .addCellFormatter(TaskItem.ATTRIBUTE_DISPLAY_DESCRIPTION, new SpanPrepender(_("Description:")))
                .setOrder(TaskItem.ATTRIBUTE_LAST_UPDATE_DATE, false)
                .setDefaultAction(new RedirectionAction(TaskMoreDetailsAdminPage.TOKEN, new Arg(TaskItem.ATTRIBUTE_ID, item.getId().toString())));
    }

    protected abstract ItemDefinition getHumanTasksDefinition();

    protected void preparetasksTable(final ItemTable tasksTable) {
        tasksTable.setNbLinesByPage(5);
        tasksTable.setDefaultAction(new RedirectionAction(HumanTaskMoreDetailsPage.TOKEN));
    }

    /**
     * @param tasksTable
     */
    protected void prepareFailedTasksTable(final ItemTable tasksTable) {
        tasksTable.setNbLinesByPage(5);
    }

}
