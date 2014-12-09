package org.bonitasoft.console.client.admin.bpm.cases.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.console.client.admin.bpm.cases.action.EditCaseVariableAction;
import org.bonitasoft.web.rest.model.bpm.cases.CaseItem;
import org.bonitasoft.web.rest.model.bpm.cases.CaseVariableDefinition;
import org.bonitasoft.web.rest.model.bpm.cases.CaseVariableItem;
import org.bonitasoft.web.toolkit.client.ui.CssId;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.component.Section;
import org.bonitasoft.web.toolkit.client.ui.component.form.generatedForm.EditorFactory;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTable;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTableAction;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTableActionSet;

public class CaseVariableSection extends Section {

    public CaseVariableSection(CaseItem item) {
        super(new JsId("casevariable"), _("Case variables"));
        setId(CssId.MD_SECTION_CASE_VARIABLES);
        addBody(caseVariableTable(item));
    }

    private ItemTable caseVariableTable(final CaseItem item) {
        final ItemTable caseVariableTable = new ItemTable(new JsId("caseVariablesTable"), CaseVariableDefinition.get())
                // add filters
                .addHiddenFilter(CaseVariableItem.ATTRIBUTE_CASE_ID, item.getId())

                // set search
                .setShowSearch(false)

                // displayed columns
                .addColumn(CaseVariableItem.ATTRIBUTE_NAME, _("Name"))
                .addColumn(CaseVariableItem.ATTRIBUTE_TYPE, _("Type"))
                .addColumn(CaseVariableItem.ATTRIBUTE_VALUE, _("Value"))

                .setActions(
                        new ItemTableActionSet<CaseVariableItem>() {

                            @Override
                            protected void defineActions(final CaseVariableItem item) {
                                EditorFactory factory = new EditorFactory();
                                if (factory.isTypeEditable(item.getType())) {
                                    this.addAction(new ItemTableAction(_("Edit"), _("Edit variable"),
                                            new EditCaseVariableAction(item, factory)));
                                }

                            }
                        }
                );
        return caseVariableTable;
    }
}
