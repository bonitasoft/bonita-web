package org.bonitasoft.console.client.admin.bpm.cases.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.web.rest.model.bpm.cases.CaseVariableDefinition;
import org.bonitasoft.web.rest.model.bpm.cases.CaseVariableItem;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.Page;
import org.bonitasoft.web.toolkit.client.ui.action.form.UpdateItemFormAction;
import org.bonitasoft.web.toolkit.client.ui.component.form.Form;
import org.bonitasoft.web.toolkit.client.ui.component.form.generatedForm.EditorFactory;
import org.bonitasoft.web.toolkit.client.ui.component.form.generatedForm.ItemBinding;

public class EditCaseVariablePopUp extends Page {

    private static final String TOKEN = "editcasevariableadminpage";

    private CaseVariableItem caseVariableItem;

    private EditorFactory factory;

    public EditCaseVariablePopUp(CaseVariableItem item, EditorFactory factory) {
        this.caseVariableItem = item;
        this.factory = factory;
    }

    @Override
    public void defineTitle() {
        setTitle(_("Edit case variable"));

    }

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    public void buildView() {

        ItemBinding itemBinded = new ItemBinding() {

            @Override
            public String getValue() {
                return caseVariableItem.getValue();
            }

            @Override
            public String getTooltip() {
                return caseVariableItem.getDescription();
            }

            @Override
            public String getDescription() {
                return caseVariableItem.getDescription();
            }

            @Override
            public String getLabel() {
                return _("Type the new value for %variableName%", new Arg("variableName", caseVariableItem.getName()));
            }

            @Override
            public String getAttributeName() {
                return CaseVariableItem.ATTRIBUTE_VALUE;
            }

            @Override
            public String getNonEditableLabel() {
                return _("Sorry the variable %variableName% has a type which is not editable", new Arg("variableName", caseVariableItem.getName()));
            }

        };

        final Form form = new Form();
        form
                .addStaticTextEntry(new JsId(CaseVariableItem.ATTRIBUTE_NAME), _("Name"), _("Name of the variable"), caseVariableItem.getName())
                .addStaticTextEntry(new JsId(CaseVariableItem.ATTRIBUTE_TYPE), _("Type"), _("Type of the variable"), caseVariableItem.getType());

        form
                .addEntry(factory.createEntryFor(caseVariableItem.getType(), itemBinded));

        form
                .addHiddenEntry("type", this.caseVariableItem.getType())
                .addHiddenEntry("id", this.caseVariableItem.getId().toString());

        form
                .addButton(new JsId("edit"), _("Edit"), _("Edit this value"), new UpdateItemFormAction<CaseVariableItem>(CaseVariableDefinition.get()))
                .addCancelButton();
        addBody(form);

    }
}
