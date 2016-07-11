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

import org.bonitasoft.console.client.common.component.snippet.SectionSnippet;
import org.bonitasoft.console.client.data.item.attribute.reader.DeployedUserReader;
import org.bonitasoft.console.client.user.cases.view.ArchivedCaseMoreDetailsPage;
import org.bonitasoft.web.rest.model.bpm.cases.ArchivedCaseDefinition;
import org.bonitasoft.web.rest.model.bpm.cases.ArchivedCaseItem;
import org.bonitasoft.web.rest.model.bpm.cases.CaseItem;
import org.bonitasoft.web.toolkit.client.Session;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.AttributeReader;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.DateAttributeReader;
import org.bonitasoft.web.toolkit.client.ui.CssId;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.RedirectionAction;
import org.bonitasoft.web.toolkit.client.ui.component.Section;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTable;
import org.bonitasoft.web.toolkit.client.ui.component.table.Table.VIEW_TYPE;
import org.bonitasoft.web.toolkit.client.ui.component.table.formatter.SpanPrepender;

/**
 * @author Paul AMAR
 * 
 */
public class ArchivedCaseWorkedOnSnippet implements SectionSnippet {

    private int nbLinesByPage = 5;

    private final APIID processId;

    /**
     * Default Constructor.
     */
    public ArchivedCaseWorkedOnSnippet(final APIID processId) {
        this.processId = processId;
    }

    /*
     * (non-Javadoc)
     * @see org.bonitasoft.console.client.user.task.view.snippet.SectionSnippet#build()
     */
    @Override
    public Section build() {
        final ItemTable caseTable = new ItemTable(new JsId("cases_worked_on"), ArchivedCaseDefinition.get())
                .addHiddenFilter(CaseItem.ATTRIBUTE_PROCESS_ID, this.processId)
                .addHiddenFilter(CaseItem.FILTER_USER_ID, Session.getUserId())
                .addColumn(new AttributeReader(ArchivedCaseItem.ATTRIBUTE_SOURCE_OBJECT_ID), _("Case id"))
                .addColumn(new DateAttributeReader(ArchivedCaseItem.ATTRIBUTE_ARCHIVED_DATE), _("Archived date"))
                .addCellFormatter(ArchivedCaseItem.ATTRIBUTE_SOURCE_OBJECT_ID, new SpanPrepender(_("Id:")))
                .addCellFormatter(ArchivedCaseItem.ATTRIBUTE_ARCHIVED_DATE, new SpanPrepender(_("Performed on:")))
                .setNbLinesByPage(getNbLinesByPage())
                .setDefaultAction(new RedirectionAction(ArchivedCaseMoreDetailsPage.TOKEN))
                .setOrder(CaseItem.ATTRIBUTE_ID, true);

        caseTable.addColumn(new AttributeReader(CaseItem.ATTRIBUTE_STATE), _("State"));
        caseTable.addCellFormatter(CaseItem.ATTRIBUTE_STATE, new SpanPrepender(_("State:")));
        caseTable.addColumn(new DeployedUserReader(CaseItem.ATTRIBUTE_STARTED_BY_USER_ID), _("Started by"));
        caseTable.addCellFormatter(CaseItem.ATTRIBUTE_STARTED_BY_USER_ID + "_" + CaseItem.ATTRIBUTE_STARTED_BY_USER_ID, new SpanPrepender(_("Started by:")));

        final Section section = new Section(_("Archives"));
        section.addClass("cases");
        section.setId(CssId.QD_SECTION_CASE_WORKED_ON);
        section.addCssCaseType();
        section.addBody(caseTable.setView(VIEW_TYPE.VIEW_LIST));

        return section;
    }

    // ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    // / Public Getters
    // ///////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @return the nbLinesByPage
     */
    public int getNbLinesByPage() {
        return this.nbLinesByPage;
    }

    // ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    // / Public Setters
    // ///////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @param nbLinesByPage
     *        the nbLinesByPage to set
     */
    public ArchivedCaseWorkedOnSnippet setNbLinesByPage(final int nbLinesByPage) {
        this.nbLinesByPage = nbLinesByPage;
        return this;
    }
}
