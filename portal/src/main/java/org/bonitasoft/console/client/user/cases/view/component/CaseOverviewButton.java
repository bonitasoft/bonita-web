/**
 * Copyright (C) 2015 BonitaSoft S.A.
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
package org.bonitasoft.console.client.user.cases.view.component;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.console.client.user.cases.view.AbstractOverviewFormMappingRequester;
import org.bonitasoft.console.client.user.cases.view.DisplayCaseFormPage;
import org.bonitasoft.web.rest.model.bpm.cases.CaseItem;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.ActionShowView;
import org.bonitasoft.web.toolkit.client.ui.component.Button;

import com.google.gwt.core.client.GWT;

public class CaseOverviewButton extends Button {

    public CaseOverviewButton(final CaseItem caseItem) {
        super("btn-overview", new JsId("btn-action"), _("Overview"), _("Display the case overview"), new ActionShowView(new DisplayCaseFormPage(caseItem)));
        final String processId = caseItem.getProcessId().toString();
        //Check form mapping to ensure there is an overview form to display
        final AbstractOverviewFormMappingRequester overviewFormMappingRequester = new AbstractOverviewFormMappingRequester() {

            @Override
            public void onMappingNotFound() {
                GWT.log("There is no overview mapping for process " + processId);
                disableButton();
            }

            @Override
            public void onMappingFound() {
                //do nothing
            }
        };
        overviewFormMappingRequester.searchFormMappingForInstance(processId);
    }


    protected void disableButton() {
        //disable the button
        setEnabled(false);
        setTooltip(_("No overview page has been defined for this process"));
    }

}
