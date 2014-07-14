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
package org.bonitasoft.console.client.user.cases.view.snippet;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.console.client.common.formatter.ArchivedFlowNodeDateFormatter;
import org.bonitasoft.console.client.common.formatter.FlowNodeDisplayNameFormatter;
import org.bonitasoft.console.client.user.task.view.more.ArchivedHumanTaskMoreDetailsPage;
import org.bonitasoft.web.rest.model.bpm.cases.ArchivedCaseItem;
import org.bonitasoft.web.rest.model.bpm.cases.CaseItem;
import org.bonitasoft.web.rest.model.bpm.flownode.ArchivedHumanTaskDefinition;
import org.bonitasoft.web.rest.model.bpm.flownode.ArchivedHumanTaskItem;
import org.bonitasoft.web.toolkit.client.Session;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.DateAttributeReader;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.DescriptionAttributeReader;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.RedirectionAction;
import org.bonitasoft.web.toolkit.client.ui.component.Section;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTable;
import org.bonitasoft.web.toolkit.client.ui.component.table.Table.VIEW_TYPE;
import org.bonitasoft.web.toolkit.client.ui.component.table.formatter.SpanPrepender;

/**
 * @author Colin PUY
 * 
 */
public class ArchivedTasksSection extends Section {

    public ArchivedTasksSection(String title, CaseItem item, int nbLinesByPage) {
        super(title);
        addClass("tasks");
        ItemTable tasksTable = getTaskTable(item);
        tasksTable.setView(VIEW_TYPE.VIEW_LIST);
        tasksTable.setNbLinesByPage(nbLinesByPage);
        tasksTable.setDefaultAction(new RedirectionAction(ArchivedHumanTaskMoreDetailsPage.TOKEN));
        addBody(tasksTable);
    }

    protected ItemTable getTaskTable(final CaseItem item) {
        ItemTable table = new ItemTable(new JsId("tasks"), ArchivedHumanTaskDefinition.get());
        table.addHiddenFilter(ArchivedHumanTaskItem.ATTRIBUTE_CASE_ID, getCaseId(item))
                .addHiddenFilter(ArchivedHumanTaskItem.FILTER_USER_ID, Session.getUserId())
                .addColumn(ArchivedHumanTaskItem.ATTRIBUTE_DISPLAY_NAME, _("Name"))
                .addColumn(new DateAttributeReader(ArchivedHumanTaskItem.ATTRIBUTE_ARCHIVED_DATE), _("Archived date"))
                .addColumn(new DescriptionAttributeReader(ArchivedHumanTaskItem.ATTRIBUTE_DISPLAY_DESCRIPTION, ArchivedHumanTaskItem.ATTRIBUTE_DESCRIPTION),
                        _("Description"))
                .addCellFormatter(ArchivedHumanTaskItem.ATTRIBUTE_DISPLAY_NAME, new FlowNodeDisplayNameFormatter())
                .addCellFormatter(ArchivedHumanTaskItem.ATTRIBUTE_DISPLAY_DESCRIPTION, new SpanPrepender(_("Description:")))
                .addCellFormatter(ArchivedHumanTaskItem.ATTRIBUTE_ARCHIVED_DATE, new ArchivedFlowNodeDateFormatter())
                .setOrder(ArchivedHumanTaskItem.ATTRIBUTE_ARCHIVED_DATE, false);
        return table;
    }

    protected APIID getCaseId(final CaseItem item) {
        if (item instanceof ArchivedCaseItem) {
            return ((ArchivedCaseItem) item).getSourceObjectId();
        } else {
            return item.getId();
        }
    }
}
