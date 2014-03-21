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
package org.bonitasoft.console.client.admin.bpm.cases.section;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.console.client.admin.bpm.cases.action.ArchivedTaskRedirectionAction;
import org.bonitasoft.console.client.common.formatter.ArchivedFlowNodeDateFormatter;
import org.bonitasoft.console.client.common.formatter.FlowNodeDisplayNameFormatter;
import org.bonitasoft.web.rest.model.bpm.cases.ArchivedCaseItem;
import org.bonitasoft.web.rest.model.bpm.flownode.ArchivedFlowNodeDefinition;
import org.bonitasoft.web.rest.model.bpm.flownode.ArchivedFlowNodeItem;
import org.bonitasoft.web.rest.model.bpm.flownode.TaskItem;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.DateAttributeReader;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.DescriptionAttributeReader;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.component.Section;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTable;
import org.bonitasoft.web.toolkit.client.ui.component.table.Table.VIEW_TYPE;
import org.bonitasoft.web.toolkit.client.ui.component.table.formatter.SpanPrepender;

/**
 * @author Colin PUY
 * 
 */
public class ArchivedTasksSection extends Section {

    private ItemTable tasksTable;

    public ArchivedTasksSection(ArchivedCaseItem item) {
        super(_("Done tasks"));
        addClass("tasks");
        tasksTable = buildTasksTable(item);
        addBody(tasksTable);
    }

    private ItemTable buildTasksTable(ArchivedCaseItem item) {
        ItemTable tasksTable = getTaskTable(item);
        tasksTable.addClass("archived");
        tasksTable.setView(VIEW_TYPE.VIEW_LIST);
        tasksTable.addActions(new ArchivedTaskRedirectionAction());
        return tasksTable;
    }

    protected ItemTable getTaskTable(final ArchivedCaseItem item) {
        return new ItemTable(new JsId("tasks"), ArchivedFlowNodeDefinition.get())
                .addHiddenFilter(ArchivedFlowNodeItem.ATTRIBUTE_CASE_ID, item.getSourceObjectId())
                .addHiddenFilter(ArchivedFlowNodeItem.FILTER_IS_TERMINAL, ArchivedFlowNodeItem.VALUE_IS_TERMINAL_TRUE)

                .addColumn(TaskItem.ATTRIBUTE_DISPLAY_NAME, _("Name"))
                .addColumn(new DateAttributeReader(ArchivedFlowNodeItem.ATTRIBUTE_ARCHIVED_DATE), _("Archived date"))
                .addColumn(new DescriptionAttributeReader(TaskItem.ATTRIBUTE_DISPLAY_DESCRIPTION, TaskItem.ATTRIBUTE_DESCRIPTION), _("Description"))

                .addCellFormatter(TaskItem.ATTRIBUTE_DISPLAY_NAME, new FlowNodeDisplayNameFormatter())
                .addCellFormatter(ArchivedFlowNodeItem.ATTRIBUTE_ARCHIVED_DATE, new ArchivedFlowNodeDateFormatter())
                .addCellFormatter(TaskItem.ATTRIBUTE_DISPLAY_DESCRIPTION, new SpanPrepender(_("Description:")))
                .setOrder(ArchivedFlowNodeItem.ATTRIBUTE_ARCHIVED_DATE, false);
    }

    public void setNbLinesByPages(int nbLinesByPage) {
        tasksTable.setNbLinesByPage(nbLinesByPage);
    }
}
