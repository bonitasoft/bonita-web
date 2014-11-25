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
package org.bonitasoft.console.client.common.metadata;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.Map;

import org.bonitasoft.console.client.data.item.attribute.reader.DeployedUserReader;
import org.bonitasoft.console.client.uib.SafeHtmlParser;
import org.bonitasoft.web.rest.model.bpm.cases.ArchivedCaseDefinition;
import org.bonitasoft.web.rest.model.bpm.cases.ArchivedCaseItem;
import org.bonitasoft.web.rest.model.bpm.cases.CaseDefinition;
import org.bonitasoft.web.rest.model.bpm.flownode.ArchivedHumanTaskItem;
import org.bonitasoft.web.rest.model.bpm.flownode.FlowNodeTypeAttributeReader;
import org.bonitasoft.web.rest.model.bpm.flownode.HumanTaskItem;
import org.bonitasoft.web.rest.model.bpm.flownode.IActivityItem;
import org.bonitasoft.web.rest.model.bpm.flownode.IFlowNodeItem;
import org.bonitasoft.web.rest.model.bpm.flownode.IHumanTaskItem;
import org.bonitasoft.web.rest.model.bpm.flownode.PriorityAttributeReader;
import org.bonitasoft.web.rest.model.bpm.process.ProcessItem;
import org.bonitasoft.web.rest.model.identity.UserItem;
import org.bonitasoft.web.toolkit.client.Session;
import org.bonitasoft.web.toolkit.client.common.json.JSonItemReader;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.api.request.APIGetRequest;
import org.bonitasoft.web.toolkit.client.data.api.request.APISearchRequest;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.DateAttributeReader;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.DeployedAttributeReader;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.DeployedJsId;
import org.bonitasoft.web.toolkit.client.ui.component.Html;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemDetailsMetadata;
import org.bonitasoft.web.toolkit.client.ui.utils.DateFormat.FORMAT;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.Element;

/**
 * @author Vincent Elcrin
 */
public class MetadataTaskBuilder extends MetadataBuilder {

    interface Templates extends SafeHtmlTemplates {

        @SafeHtmlTemplates.Template(
                "<a class='definition caseid' title='{0}'>" +
                        "<label>{1}: </label><span>{2}</span>" +
                        "</a>")
                SafeHtml caseId(String title, String label, String id);
    }

    private static Templates TEMPLATES = GWT.create(Templates.class);

    public void addAppsName() {
        add(createMetaAppsName());
    }

    public void addAppsVersion() {
        add(createMetaAppsVersion());
    }

    public void AddSubAppsName() {
        add(createMetaSubAppsName());
    }

    public void AddSubAppsVersion() {
        add(createMetaSubAppsVersion());
    }

    /**
     * @param anchor
     * @param task
     * @deprecated
     */
    @Deprecated
    public void addCaseId(final IFlowNodeItem task, final String targetPageToken, final String targetArchivedPageToken) {
        final AnchorElement anchor = AnchorElement.as(Element.as(SafeHtmlParser.parseFirst(TEMPLATES.caseId(
                _("The id of the related case"),
                _("Case"),
                task.getCaseId().toString()))));
        add(new ItemDetailsMetadata(ArchivedHumanTaskItem.ATTRIBUTE_CASE_ID, new Html(anchor)));
        setCaseHref(anchor, task, targetPageToken, targetArchivedPageToken);
    }

    /**
     * Is static to be accessible in HumanTaskMetadataView which is the same code but really shouldn't.
     *
     * @param anchor
     * @param task
     */
    public static void setCaseHref(final AnchorElement anchor, final IFlowNodeItem task, final String targetPageToken, final String targetArchivedPageToken) {
        final APIGetRequest request = new APIGetRequest(CaseDefinition.get()).setId(task.getCaseId());
        request.run(new APICallback() {

            @Override
            public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
                anchor.setHref("#?id=" + task.getCaseId() + "&_p=" + targetPageToken + "&_pf=" + Session.getCurrentProfile());
            }

            @Override
            protected void on404NotFound(final String message) {
                getArchivedCaseId(task, new APICallback() {

                    @Override
                    public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
                        final ArchivedCaseItem archive = JSonItemReader.parseItem(response, ArchivedCaseDefinition.get());
                        anchor.setHref("#?id=" + archive.getId() + "&_p=" + targetArchivedPageToken + "&_pf=" + Session.getCurrentProfile());
                    }
                });
            }
        });
    }

    public static void getArchivedCaseId(final IFlowNodeItem task, final APICallback callback) {
        final APISearchRequest request = new APISearchRequest(ArchivedCaseDefinition.get());
        request.addFilter(ArchivedCaseItem.ATTRIBUTE_SOURCE_OBJECT_ID, task.getCaseId().toString());
        request.run(callback);
    }

    public void addPriority() {
        add(createMetaPriority());
    }

    public void addAssignedTo() {
        add(createMetaAssignTo());
    }

    public void addDoneOn(final FORMAT format) {
        add(createMetaDoneOn(format));
    }

    public void addLastUpdateDate(final FORMAT format) {
        add(createMetaLastUpdateDate(format));
    }

    public void addAssignedDate(final FORMAT format) {
        add(createMetaAssignedDate(format));
    }

    public void addExecutedBy() {
        add(createMetaExecutedBy());
    }

    public void addDueDate(final FORMAT format) {
        add(createMetaDueDate(format));
    }

    public void addState() {
        add(createMetaState());
    }

    public void addType() {
        add(createMetaType());
    }

    private ItemDetailsMetadata createMetaType() {
        return new ItemDetailsMetadata(new FlowNodeTypeAttributeReader(), _("Type"), _("The type of the task"));
    }

    private ItemDetailsMetadata createMetaState() {
        return new ItemDetailsMetadata(IFlowNodeItem.ATTRIBUTE_STATE, _("State"), _("The state of the task"));
    }

    private ItemDetailsMetadata createMetaAppsName() {
        return new ItemDetailsMetadata(
                new DeployedAttributeReader(IFlowNodeItem.ATTRIBUTE_ROOT_CONTAINER_ID, ProcessItem.ATTRIBUTE_DISPLAY_NAME),
                _("Process"),
                _("The process responsible for the creation of this task"));
    }

    private ItemDetailsMetadata createMetaAppsVersion() {
        return new ItemDetailsMetadata(
                new DeployedAttributeReader(IFlowNodeItem.ATTRIBUTE_ROOT_CONTAINER_ID, ProcessItem.ATTRIBUTE_VERSION),
                _("Process version"),
                _("Version of the process"));
    }

    private ItemDetailsMetadata createMetaSubAppsName() {
        return new ItemDetailsMetadata(
                new DeployedAttributeReader(HumanTaskItem.ATTRIBUTE_PROCESS_ID, ProcessItem.ATTRIBUTE_DISPLAY_NAME),
                _("Subprocess"),
                _("The sub process responsible for the creation of this task"));
    }

    private ItemDetailsMetadata createMetaSubAppsVersion() {
        return new ItemDetailsMetadata(
                new DeployedAttributeReader(HumanTaskItem.ATTRIBUTE_PROCESS_ID, ProcessItem.ATTRIBUTE_VERSION),
                _("Subprocess version"),
                _("Version of the process"));
    }

    private ItemDetailsMetadata createMetaCaseId() {
        return new ItemDetailsMetadata(IActivityItem.ATTRIBUTE_CASE_ID, _("Case"), _("The id of the related case"));
    }

    private ItemDetailsMetadata createMetaPriority() {
        return new ItemDetailsMetadata(new PriorityAttributeReader(HumanTaskItem.ATTRIBUTE_PRIORITY), _("Priority"), _("The priority of the task"));
    }

    private ItemDetailsMetadata createMetaAssignTo() {
        return new ItemDetailsMetadata(
                new DeployedJsId(IHumanTaskItem.ATTRIBUTE_ASSIGNED_USER_ID, UserItem.ATTRIBUTE_FIRSTNAME, UserItem.ATTRIBUTE_LASTNAME),
                new DeployedUserReader(IHumanTaskItem.ATTRIBUTE_ASSIGNED_USER_ID).setDefaultValue(_("Unassigned")),
                _("Assigned to"),
                _("The username to which the task is assigned."));
    }

    private ItemDetailsMetadata createMetaDoneOn(final FORMAT format) {
        return new ItemDetailsMetadata(new DateAttributeReader(HumanTaskItem.ATTRIBUTE_LAST_UPDATE_DATE, format).setDefaultValue(_("No data")),
                _("Done on"), "");
    }

    private ItemDetailsMetadata createMetaLastUpdateDate(final FORMAT format) {
        return new ItemDetailsMetadata(new DateAttributeReader(HumanTaskItem.ATTRIBUTE_LAST_UPDATE_DATE, format).setDefaultValue(_("No data")),
                _("Last update date"), _("The date of the last modification"));
    }

    private ItemDetailsMetadata createMetaAssignedDate(final FORMAT format) {
        return new ItemDetailsMetadata(new DateAttributeReader(HumanTaskItem.ATTRIBUTE_ASSIGNED_DATE).setDefaultValue(_("No data")),
                _("Assigned date"),
                _("The date when the task was assigned"));
    }

    private ItemDetailsMetadata createMetaExecutedBy() {
        return new ItemDetailsMetadata(new DeployedUserReader(IActivityItem.ATTRIBUTE_EXECUTED_BY_USER_ID),
                _("Done by"),
                _("Name of the user who executed this task"));
    }

    private ItemDetailsMetadata createMetaDueDate(final FORMAT format) {
        return new ItemDetailsMetadata(
                new DateAttributeReader(HumanTaskItem.ATTRIBUTE_DUE_DATE, format).setDefaultValue("No data"),
                _("Due date"),
                _("The date when the task must be finished"));
    }

}
