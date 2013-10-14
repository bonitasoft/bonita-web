/**
 * Copyright (C) 2012 BonitaSoft S.A.
 * 
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
package org.bonitasoft.console.client.admin.organization.users.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.console.client.admin.organization.users.action.ChangeUsersStateAction;
import org.bonitasoft.console.client.admin.organization.users.action.ChangeUsersStateAction.STATE;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.ui.Page;
import org.bonitasoft.web.toolkit.client.ui.action.ClosePopUpAction;
import org.bonitasoft.web.toolkit.client.ui.component.Button;
import org.bonitasoft.web.toolkit.client.ui.component.Paragraph;
import org.bonitasoft.web.toolkit.client.ui.component.button.ButtonAction;
import org.bonitasoft.web.toolkit.client.ui.component.containers.Container;

public class DeactivateUserWarningPopUp extends Page {

    public static final String TOKEN = "deactivateuserwarningpopup";
    
    public DeactivateUserWarningPopUp() {
        // used by page factory - to be deleted when we will be able to not do a Page for a Popup
    }
    
    public DeactivateUserWarningPopUp(APIID userId) {
        addParameter("id", userId.toString());
    }
    
    @Override
    public void defineTitle() {
        setTitle(_("Warning"));
        
    }

    @Override
    public void buildView() {
        addBody(warningText());
        addBody(buttons());
    }

    private Container<Paragraph> warningText() {
        Container<Paragraph> container = new Container<Paragraph>();
        container.append(new Paragraph(_("You risk interrupting one or more apps.\n\n" +
                "Deactivating the only user able to perform a task(s), will cause the interruption of an app(s).\n\n" +
                "Before proceeding, you may want to go to the 'More' page of the app and check the actor mapping.\n\n" +
                "Are you sure you want to deactivate this user now ?")));
        return container;
    }
    
    private Container<Button> buttons() {
        Container<Button> formactions = new Container<Button>();
        formactions.addClass("formactions");
        formactions.append(deactivateButton(), closeButon());
        return formactions;
    }

    private Button closeButon() {
        return new Button(_("Cancel"), _("Cancel"), new ClosePopUpAction());
    }

    private ButtonAction deactivateButton() {
        return new ButtonAction(_("Deactivate"), _("Dactivate selected user"), new ChangeUsersStateAction(getParameter("id"), STATE.DISABLED));
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }
}
