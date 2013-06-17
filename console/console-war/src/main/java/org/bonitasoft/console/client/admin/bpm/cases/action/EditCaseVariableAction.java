package org.bonitasoft.console.client.admin.bpm.cases.action;

import org.bonitasoft.console.client.admin.bpm.cases.view.EditCaseVariablePopUp;
import org.bonitasoft.web.rest.api.model.bpm.cases.CaseVariableDefinition;
import org.bonitasoft.web.rest.api.model.bpm.cases.CaseVariableItem;
import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.ui.action.ItemAction;
import org.bonitasoft.web.toolkit.client.ui.component.form.generatedForm.EditorFactory;

public class EditCaseVariableAction extends ItemAction {

    private CaseVariableItem caseVariableItem;

    private EditorFactory factory;

    public EditCaseVariableAction(CaseVariableItem item, EditorFactory factory) {
        super(CaseVariableDefinition.get());
        this.caseVariableItem = item;
        this.factory = factory;
    }

    @Override
    public void execute() {
        ViewController.showPopup(new EditCaseVariablePopUp(this.caseVariableItem,
                this.factory));
    }

}
