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
import org.bonitasoft.console.client.user.cases.view.CaseMoreDetailsPage;
import org.bonitasoft.web.rest.model.bpm.cases.CaseDefinition;
import org.bonitasoft.web.rest.model.bpm.cases.CaseItem;
import org.bonitasoft.web.toolkit.client.Session;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
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
public class MyCasesSnippet implements SectionSnippet {

    private int nbLinesByPage = 5;

    private boolean showStartedBy = true;

    private final APIID processId;

    /**
     * Default Constructor.
     */
    public MyCasesSnippet(final APIID processId) {
        this.processId = processId;
    }

    /*
     * (non-Javadoc)
     * @see org.bonitasoft.console.client.user.task.view.snippet.SectionSnippet#build()
     */
    @Override
    public Section build() {
        final ItemTable caseTable = new ItemTable(new JsId("my_started_cases"), Definitions.get(CaseDefinition.TOKEN))
                .addHiddenFilter(CaseItem.ATTRIBUTE_PROCESS_ID, this.processId)
                .addHiddenFilter(CaseItem.ATTRIBUTE_STARTED_BY_USER_ID, Session.getUserId())
                .addColumn(new AttributeReader(CaseItem.ATTRIBUTE_ID), _("Case id"))
                .addColumn(new DateAttributeReader(CaseItem.ATTRIBUTE_START_DATE), _("Started on"))
                .addCellFormatter(CaseItem.ATTRIBUTE_ID, new SpanPrepender(_("Id:")))
                .addCellFormatter(CaseItem.ATTRIBUTE_START_DATE, new SpanPrepender(_("Started on:")))
                .setOrder(CaseItem.ATTRIBUTE_ID, true)
                .setNbLinesByPage(getNbLinesByPage())
                .setDefaultAction(new RedirectionAction(CaseMoreDetailsPage.TOKEN));

        caseTable.addColumn(new AttributeReader(CaseItem.ATTRIBUTE_STATE), _("State"));
        caseTable.addCellFormatter(CaseItem.ATTRIBUTE_STATE, new SpanPrepender(_("State:")));

        if (this.showStartedBy) {
            caseTable.addColumn(new DeployedUserReader(CaseItem.ATTRIBUTE_STARTED_BY_USER_ID), _("Started by"));
            caseTable
                    .addCellFormatter(CaseItem.ATTRIBUTE_STARTED_BY_USER_ID + "_" + CaseItem.ATTRIBUTE_STARTED_BY_USER_ID, new SpanPrepender(_("Started by:")));
        }

        final Section section = new Section(_("My cases"));
        section.addClass("cases");
        section.addCssCaseType();
        section.setId(CssId.QD_SECTION_MY_CASES);
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
    public MyCasesSnippet setNbLinesByPage(final int nbLinesByPage) {
        this.nbLinesByPage = nbLinesByPage;
        return this;
    }

    public MyCasesSnippet setShowStartedBy(final boolean show) {
        this.showStartedBy = show;
        return this;
    }

}
