package org.bonitasoft.console.client.user.cases.view.component;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.console.client.user.cases.view.DisplayCaseFormPage;
import org.bonitasoft.web.rest.model.bpm.cases.CaseItem;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.ActionShowView;
import org.bonitasoft.web.toolkit.client.ui.component.Button;

public class CaseOverviewButton extends Button {

    public CaseOverviewButton(final CaseItem aCase) {
        super("btn-overview", new JsId("btn-action"), _("Overview"), _("Display the case form"), new ActionShowView(new DisplayCaseFormPage(aCase)));
    }

}
