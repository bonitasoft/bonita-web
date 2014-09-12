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
package org.bonitasoft.console.client.admin.process.view.section.cases;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.web.rest.model.bpm.cases.CaseDefinition;
import org.bonitasoft.web.rest.model.bpm.cases.CaseItem;
import org.bonitasoft.web.rest.model.bpm.process.ProcessItem;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.DeployedAttributeReader;
import org.bonitasoft.web.toolkit.client.ui.CssClass;
import org.bonitasoft.web.toolkit.client.ui.CssId;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.component.Section;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTable;

/**
 * @author Colin PUY
 * 
 */
public class CasesSection extends Section {

    public CasesSection(ProcessItem process) {
        super(new JsId("cases"), _("Cases"));
        setId(CssId.MD_SECTION_PROCESS_CASES);
        addCssCaseType();
        addBody(caseTable(process));
    }

    private ItemTable caseTable(ProcessItem process) {
        ItemTable casesTable = buildCaseItemTable(process);
        casesTable.setNbLinesByPage(10);
        casesTable.setDefaultAction(getShowCaseMoreDetailAction());
        casesTable.addClass("cases");
        return casesTable;
    }

    protected ShowCaseMoreDetailAction getShowCaseMoreDetailAction() {
        return new ShowCaseMoreDetailAction();
    }

    private ItemTable buildCaseItemTable(ProcessItem process) {
        ItemTable casesTable = new ItemTable(CaseDefinition.get())
                .addColumn(CaseItem.ATTRIBUTE_ID, _("ID"), true)
                .addColumn(new DeployedAttributeReader(CaseItem.ATTRIBUTE_PROCESS_ID, ProcessItem.ATTRIBUTE_DISPLAY_NAME), _("Name"))
                .addHiddenFilter(CaseItem.ATTRIBUTE_PROCESS_ID, process.getId());
        return casesTable;
    }
}
