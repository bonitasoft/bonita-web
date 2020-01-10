/**
 * Copyright (C) 2012-2019 BonitaSoft S.A.
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
package org.bonitasoft.console.client.admin.process.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.web.rest.model.bpm.process.ProcessDefinition;
import org.bonitasoft.web.toolkit.client.ui.action.popup.DeleteMultipleItemsPopupAction;

/**
 * @author Anthony Birembaut
 * 
 */
public class DeleteMultipleProcessesPopupAction extends DeleteMultipleItemsPopupAction {

    /**
     * Default Constructor.
     */
    public DeleteMultipleProcessesPopupAction() {
        super(ProcessDefinition.get(), _("process"), _("processes"));
    }
    
    public String getWarningMessage(String question) {

        StringBuilder warningMessage = new StringBuilder();
        warningMessage.append("<div class=\"callout callout-warning\">");
        warningMessage.append("<h4>");
        warningMessage.append(_("Warning:"));
        warningMessage.append("</h4>");
        warningMessage.append("<p>");
        warningMessage.append(_("Deleting a process definition will also delete all open and archived cases of this process."));
        warningMessage.append("<br/>");
        warningMessage.append(_("They will be removed from the database permanently."));
        warningMessage.append("<br/>");
        warningMessage.append(_("In a production environment, this should be used with caution."));
        warningMessage.append("<br/>");
        warningMessage.append("</p>");
        warningMessage.append("</div>");
        warningMessage.append("<div>");
        warningMessage.append("<p>");
        warningMessage.append("<strong>");
        warningMessage.append(question);
        warningMessage.append("</strong>");
        warningMessage.append("</p>");
        warningMessage.append("</div>");
        return warningMessage.toString();
    }

    @Override
    protected String getItemDeleteMessage() {
        return getWarningMessage(_("Are you sure you want to delete it?"));
    }

    @Override
    protected String getItemsDeleteMessage() {
        return getWarningMessage(_("Are you sure you want to delete them?"));
    }

}
