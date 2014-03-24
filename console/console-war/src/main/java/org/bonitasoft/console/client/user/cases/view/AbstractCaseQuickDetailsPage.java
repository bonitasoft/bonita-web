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
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.bonitasoft.console.client.common.component.snippet.CommentSectionSnippet;
import org.bonitasoft.console.client.common.formatter.ArchivedFlowNodeDateFormatter;
import org.bonitasoft.console.client.common.formatter.FlowNodeDisplayNameFormatter;
import org.bonitasoft.console.client.data.item.attribute.reader.DeployedUserReader;
import org.bonitasoft.console.client.user.cases.view.AbstractCaseQuickDetailsPage.CommentsTableAsyncBuilder;
import org.bonitasoft.console.client.user.cases.view.snippet.ArchivedTasksSection;
import org.bonitasoft.console.client.user.task.view.AbstractTaskDetailsPage;
import org.bonitasoft.console.client.user.task.view.more.HumanTaskMoreDetailsPage;
import org.bonitasoft.web.rest.model.bpm.cases.ArchivedCaseDefinition;
import org.bonitasoft.web.rest.model.bpm.cases.ArchivedCommentDefinition;
import org.bonitasoft.web.rest.model.bpm.cases.CaseDefinition;
import org.bonitasoft.web.rest.model.bpm.cases.CaseItem;
import org.bonitasoft.web.rest.model.bpm.cases.CommentDefinition;
import org.bonitasoft.web.rest.model.bpm.flownode.ArchivedHumanTaskItem;
import org.bonitasoft.web.rest.model.bpm.flownode.HumanTaskItem;
import org.bonitasoft.web.rest.model.bpm.process.ProcessItem;
import org.bonitasoft.web.toolkit.client.Session;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.api.request.APIRequest;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.DateAttributeReader;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.DeployedAttributeReader;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.DescriptionAttributeReader;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.RedirectionAction;
import org.bonitasoft.web.toolkit.client.ui.component.Section;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTable;
import org.bonitasoft.web.toolkit.client.ui.component.table.Table.VIEW_TYPE;
import org.bonitasoft.web.toolkit.client.ui.component.table.formatter.SpanPrepender;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemDetailsMetadata;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemQuickDetailsPage;

/**
 * @author Séverin Moussel
 * 
 */
abstract class AbstractCaseQuickDetailsPage<T extends CaseItem> extends ItemQuickDetailsPage<T> implements PluginCase {

    public AbstractCaseQuickDetailsPage(final boolean archived) {
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
        metadatas.add(startDate());
        metadatas.add(startedBy());
        return metadatas;
    }

    private ItemDetailsMetadata processVersion() {
        return new ItemDetailsMetadata(new DeployedAttributeReader(CaseItem.ATTRIBUTE_PROCESS_ID, ProcessItem.ATTRIBUTE_VERSION),
                _("App version"), _("The version of the app that created this case"));
    }

    private ItemDetailsMetadata startDate() {
        return new ItemDetailsMetadata(CaseItem.ATTRIBUTE_START_DATE, _("Started on"), _("The date while the case has been started"));
    }

    private ItemDetailsMetadata startedBy() {
        return new ItemDetailsMetadata(new DeployedUserReader(CaseItem.ATTRIBUTE_STARTED_BY_USER_ID),
                _("Started by"), _("The user that has started this case"));
    }

    @Override
    protected void buildBody(final CaseItem item) {
        buildAvailableTasks(item);
        addBody(new ArchivedTasksSection(_("Done tasks"), item, 5));
        buildComments(item.getId(), new CommentsTableAsyncBuilder());

    }

    protected void buildAvailableTasks(final CaseItem item) {
        final ItemTable tasksTable = getTaskTable(item);
        preparetasksTable(tasksTable);
        addBody(new Section(_("Tasks to do"), tasksTable.setView(VIEW_TYPE.VIEW_LIST)).addClass("tasks"));
    }

    protected ItemTable getTaskTable(final CaseItem item) {
        return new ItemTable(new JsId("available"), getTasksDefinition())
                .addHiddenFilter(HumanTaskItem.ATTRIBUTE_CASE_ID, item.getId())
                .addHiddenFilter(HumanTaskItem.FILTER_USER_ID, Session.getUserId())
                .addHiddenFilter(HumanTaskItem.ATTRIBUTE_STATE, HumanTaskItem.VALUE_STATE_READY)
                .addColumn(HumanTaskItem.ATTRIBUTE_DISPLAY_NAME, _("Name"))
                .addColumn(HumanTaskItem.ATTRIBUTE_DUE_DATE, _("Due date"))
                .addColumn(new DescriptionAttributeReader(HumanTaskItem.ATTRIBUTE_DISPLAY_DESCRIPTION, HumanTaskItem.ATTRIBUTE_DESCRIPTION), _("Description"))
                .addCellFormatter(HumanTaskItem.ATTRIBUTE_DISPLAY_NAME, new FlowNodeDisplayNameFormatter())
                .addCellFormatter(HumanTaskItem.ATTRIBUTE_DUE_DATE, new SpanPrepender(_("Due in:")))
                .addCellFormatter(HumanTaskItem.ATTRIBUTE_DISPLAY_DESCRIPTION, new SpanPrepender(_("Description:")));
    }

    protected void preparetasksTable(final ItemTable tasksTable) {
        tasksTable.setNbLinesByPage(5);
        tasksTable.setDefaultAction(new RedirectionAction(HumanTaskMoreDetailsPage.TOKEN));
    }

    protected ItemTable getPerformedTaskTable(final CaseItem item) {
        return new ItemTable(new JsId("performed"), getArchivedTasksDefinition())
                .addHiddenFilter(ArchivedHumanTaskItem.ATTRIBUTE_CASE_ID, item.getId())
                .addHiddenFilter(ArchivedHumanTaskItem.FILTER_USER_ID, Session.getUserId())

                .addColumn(ArchivedHumanTaskItem.ATTRIBUTE_DISPLAY_NAME, _("Name"))
                .addColumn(new DateAttributeReader(ArchivedHumanTaskItem.ATTRIBUTE_ARCHIVED_DATE), _("Performed on"))
                .addColumn(new DescriptionAttributeReader(ArchivedHumanTaskItem.ATTRIBUTE_DISPLAY_DESCRIPTION, ArchivedHumanTaskItem.ATTRIBUTE_DESCRIPTION),
                        _("Description"))

                .addCellFormatter(ArchivedHumanTaskItem.ATTRIBUTE_DISPLAY_NAME, new FlowNodeDisplayNameFormatter())
                .addCellFormatter(ArchivedHumanTaskItem.ATTRIBUTE_ARCHIVED_DATE, new ArchivedFlowNodeDateFormatter())
                .addCellFormatter(ArchivedHumanTaskItem.ATTRIBUTE_DISPLAY_DESCRIPTION, new SpanPrepender(_("Description:")))
                .setOrder(ArchivedHumanTaskItem.ATTRIBUTE_REACHED_STATE_DATE, false);
    }

    private void buildComments(final APIID caseId, final CommentsTableAsyncBuilder commentsTableAsyncBuilder) {

        APIRequest.get(caseId, Definitions.get(CaseDefinition.TOKEN), new APICallback() {

            @Override
            public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
                commentsTableAsyncBuilder.build(Definitions.get(CommentDefinition.TOKEN), caseId);
            }

            @Override
            public void onError(final String message, final Integer errorCode) {
                if (errorCode.equals(HttpServletResponse.SC_NOT_FOUND) && message.isEmpty()) {
                    commentsTableAsyncBuilder.build(Definitions.get(ArchivedCommentDefinition.TOKEN), caseId, false);
                } else {
                    super.onError(message, errorCode);
                }
            }
        }).run();
    }

    protected abstract ItemDefinition getCommentDefinition();

    protected abstract ItemDefinition getTasksDefinition();

    protected abstract ItemDefinition getArchivedTasksDefinition();

    /**
     * @param subtasksTable
     */
    protected void prepareCommentsTable(final ItemTable commentsTable) {
        commentsTable.setNbLinesByPage(3);

    }

    /**
     * Class helper to build CommentsTable Asynchronously.
     * 
     * An instance of this class is used to build the common comments
     * table with {@link AbstractTaskDetailsPage#buildComments(APIID, CommentsTableAsyncBuilder)}
     * 
     * @author Vincent Elcrin
     * 
     */
    public class CommentsTableAsyncBuilder {

        /**
         * Build comments section with table and form
         * 
         * @param itemDefinition
         * @param item
         */
        public void build(final ItemDefinition itemDefinition, final APIID caseId, final boolean addForm) {

            // // Comments section
            // final Section section = new Section(_("Comments"));
            //
            // // Comments table
            // final ItemTable commentTable = getCommentsTable(itemDefinition, caseId);
            // // Comments form
            // if (addForm) {
            // final Form commentForm = new Form(new JsId("commentForm"))
            // .addHiddenEntry(CommentItem.ATTRIBUTE_PROCESS_INSTANCE_ID, caseId.toString())
            // .addTextEntryWithPlaceholderWithMaxLength(new JsId("content"), _("Discuss :"),
            // _("Type new comment"), "Type new comment", 200)
            // .addButton(new JsId("addcomment"), _("Add Comment"), _("Add a comment"), new AddCommentAction(commentTable));
            // section.addBody(commentForm);
            // }
            // prepareCommentsTable(commentTable);
            // section.addBody(commentTable.setView(VIEW_TYPE.VIEW_LIST)).addClass("commentsSection");

            addBody(new CommentSectionSnippet(caseId).setNbLinesByPage(3).build());
        }

        public void build(final ItemDefinition itemDefinition, final APIID caseId) {
            build(itemDefinition, caseId, true);
        }
    }

    @Override
    public String getPluginToken() {
        return PLUGIN_TOKEN;
    }
}
