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

import org.bonitasoft.console.client.admin.bpm.cases.action.ArchivedTaskRedirectionAction;
import org.bonitasoft.console.client.admin.bpm.cases.action.TaskRedirectionAction;
import org.bonitasoft.console.client.admin.process.view.ProcessListingAdminPage;
import org.bonitasoft.console.client.common.component.snippet.CommentSectionSnippet;
import org.bonitasoft.console.client.common.formatter.ArchivedFlowNodeDateFormatter;
import org.bonitasoft.console.client.common.formatter.ArchivedFlowNodeExecutedByFormatter;
import org.bonitasoft.console.client.common.formatter.FlowNodeDisplayNameFormatter;
import org.bonitasoft.console.client.data.item.attribute.reader.DeployedUserReader;
import org.bonitasoft.console.client.user.cases.view.DisplayCaseFormPage;
import org.bonitasoft.web.rest.model.bpm.cases.CaseItem;
import org.bonitasoft.web.rest.model.bpm.flownode.ArchivedFlowNodeDefinition;
import org.bonitasoft.web.rest.model.bpm.flownode.ArchivedTaskItem;
import org.bonitasoft.web.rest.model.bpm.flownode.HumanTaskItem;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.DateAttributeReader;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.DescriptionAttributeReader;
import org.bonitasoft.web.toolkit.client.ui.CssClass;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.ActionShowView;
import org.bonitasoft.web.toolkit.client.ui.component.Button;
import org.bonitasoft.web.toolkit.client.ui.component.Clickable;
import org.bonitasoft.web.toolkit.client.ui.component.Section;
import org.bonitasoft.web.toolkit.client.ui.component.button.ButtonBack;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTable;
import org.bonitasoft.web.toolkit.client.ui.component.table.Table.VIEW_TYPE;
import org.bonitasoft.web.toolkit.client.ui.component.table.formatter.SpanPrepender;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemDetailsMetadata;

/**
 * @author Nicolas Tith
 * 
 */
public class CaseMoreDetailsAdminPage extends CaseQuickDetailsAdminPage {

    public static final String TOKEN = "casemoredetailsadmin";

    public static final List<String> PRIVILEGES = new ArrayList<String>();

    static {
        PRIVILEGES.add(CaseListingAdminPage.TOKEN);
        PRIVILEGES.add(ProcessListingAdminPage.TOKEN);
        PRIVILEGES.add("reportlistingadminext");
    }

    public CaseMoreDetailsAdminPage() {
        addClass(CssClass.MORE_DETAILS);
    }

    @Override
    protected void buildToolbar(final CaseItem item) {
        addToolbarLink(new ButtonBack());
        addToolbarLink(newButtonDisplayCaseForm(item));
    }

    private Clickable newButtonDisplayCaseForm(final CaseItem item) {
        return new Button("btn-overview", _("Overview"), _("Display the case form"),
                new ActionShowView(new DisplayCaseFormPage(item)));
    }

    @Override
    protected List<String> defineDeploys() {
        final List<String> deploys = new ArrayList<String>(super.defineDeploys());
        deploys.add(CaseItem.ATTRIBUTE_PROCESS_ID);
        return deploys;
    }

    public CaseMoreDetailsAdminPage(final APIID caseId) {
        this();
        addParameter(PARAMETER_ITEM_ID, caseId.toString());
    }

    @Override
    protected boolean isDescriptionBeforeMetadatas() {
        return false;
    }

    @Override
    protected LinkedList<ItemDetailsMetadata> defineMetadatas(final CaseItem item) {
        final LinkedList<ItemDetailsMetadata> metadatas = super.defineMetadatas(item);
        metadatas.add(lastUpdateDate());
        metadatas.add(state());
        return metadatas;
    }

    private ItemDetailsMetadata state() {
        return new ItemDetailsMetadata(CaseItem.ATTRIBUTE_STATE, _("State"), _("The current state of the case"));
    }

    private ItemDetailsMetadata lastUpdateDate() {
        return new ItemDetailsMetadata(CaseItem.ATTRIBUTE_LAST_UPDATE_DATE, _("Last updated"), _("The date when the case was updated"));
    }

    @Override
    protected void buildBody(final CaseItem item) {
        super.buildBody(item);
        buildAvailableTasks(item);
        buildDoneTasks(item);
        buildComments(item.getId());
        buildCaseVariableSection(item);
    }

    protected void buildAvailableTasks(final CaseItem item) {
        final ItemTable tasksTable = getTaskTable(item);
        preparetasksTable(tasksTable);
        addBody(new Section(_("Pending tasks"), tasksTable.setView(VIEW_TYPE.VIEW_LIST)).addClass("tasks"));
    }

    protected ItemTable getTaskTable(final CaseItem item) {
        return new ItemTable(new JsId("tasks"), getHumanTasksDefinition())
                .addHiddenFilter(HumanTaskItem.ATTRIBUTE_CASE_ID, item.getId())
                .addColumn(HumanTaskItem.ATTRIBUTE_DISPLAY_NAME, _("Name"))
                .addColumn(new DateAttributeReader(HumanTaskItem.ATTRIBUTE_DUE_DATE), _("Due date"))
                .addColumn(new DescriptionAttributeReader(HumanTaskItem.ATTRIBUTE_DISPLAY_DESCRIPTION, HumanTaskItem.ATTRIBUTE_DESCRIPTION), _("Description"))

                .addCellFormatter(HumanTaskItem.ATTRIBUTE_DISPLAY_NAME, new FlowNodeDisplayNameFormatter())
                .addCellFormatter(HumanTaskItem.ATTRIBUTE_DUE_DATE, new SpanPrepender(_("Due in:")))
                .addCellFormatter(HumanTaskItem.ATTRIBUTE_DISPLAY_DESCRIPTION, new SpanPrepender(_("Description:")));
    }

    protected ItemTable getArchivedTaskTable(final CaseItem item) {
        return new ItemTable(new JsId("tasks"), ArchivedFlowNodeDefinition.get())
                .addHiddenFilter(ArchivedTaskItem.ATTRIBUTE_CASE_ID, item.getId())
                .addHiddenFilter(ArchivedTaskItem.FILTER_IS_TERMINAL, ArchivedTaskItem.VALUE_IS_TERMINAL_TRUE)

                .addColumn(ArchivedTaskItem.ATTRIBUTE_DISPLAY_NAME, _("Name"))
                .addColumn(new DateAttributeReader(ArchivedTaskItem.ATTRIBUTE_ARCHIVED_DATE), _("Performed date"))
                .addColumn(new DeployedUserReader(ArchivedTaskItem.ATTRIBUTE_EXECUTED_BY_USER_ID), _("Performed by"))
                .addColumn(new DescriptionAttributeReader(ArchivedTaskItem.ATTRIBUTE_DISPLAY_DESCRIPTION, ArchivedTaskItem.ATTRIBUTE_DESCRIPTION),
                        _("Description"))
                .addCellFormatter(ArchivedTaskItem.ATTRIBUTE_DISPLAY_NAME, new FlowNodeDisplayNameFormatter())
                .addCellFormatter(ArchivedTaskItem.ATTRIBUTE_ARCHIVED_DATE, new ArchivedFlowNodeDateFormatter())
                .addCellFormatter(ArchivedTaskItem.ATTRIBUTE_DISPLAY_DESCRIPTION, new SpanPrepender(_("Description:")))
                .addCellFormatter(ArchivedTaskItem.ATTRIBUTE_EXECUTED_BY_USER_ID + "_" + ArchivedTaskItem.ATTRIBUTE_EXECUTED_BY_USER_ID,
                        new ArchivedFlowNodeExecutedByFormatter())
                .setOrder(ArchivedTaskItem.ATTRIBUTE_ARCHIVED_DATE, false);
    }

    protected void prepareArchivedTasksTable(final ItemTable archivedTasksTable) {
        archivedTasksTable.setNbLinesByPage(10);
        archivedTasksTable.setActions(new ArchivedTaskRedirectionAction());
        archivedTasksTable.addClass("archived");
    }

    protected void buildDoneTasks(final CaseItem item) {
        final ItemTable doneTasksTable = getArchivedTaskTable(item);
        prepareArchivedTasksTable(doneTasksTable);
        final Section section = new Section(_("Done tasks"),
                doneTasksTable.setView(VIEW_TYPE.VIEW_LIST));
        section.addClass("tasks");
        section.addClass("performed");
        addBody(section);
    }

    @Override
    protected void preparetasksTable(final ItemTable subtasksTable) {
        subtasksTable.setNbLinesByPage(10);
        subtasksTable.setActions(new TaskRedirectionAction());
    }

    private void buildComments(final APIID caseId) {
        addBody(new CommentSectionSnippet(caseId)
                .build());
    }

    private void buildCaseVariableSection(final CaseItem item) {
        addBody(new CaseVariableSection(item));
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }
}
