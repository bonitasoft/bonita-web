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
package org.bonitasoft.console.client.admin.bpm.task.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.console.client.admin.bpm.task.action.AssignTaskAndHistoryBackAction;
import org.bonitasoft.console.client.common.view.SelectUserAndDoPage;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.action.form.FormAction;

/**
 * @author Vincent Elcrin
 * 
 */
public class SelectUserAndAssignTaskPage extends SelectUserAndDoPage {

    public static final String TOKEN = "assignTaskTo";

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    protected Action defineSubmitButtonAction() {
        // need to use a form action otherwise hidden field are not passed though to the action
        return new FormAction() {

            @Override
            public void execute() {
                final AssignTaskAndHistoryBackAction action = new AssignTaskAndHistoryBackAction();
                action.setParameters(getParameters());
                action.execute();
            }

        };
    }

    @Override
    protected String defineSubmitButtonLabel() {
        return _("Assign");
    }

    @Override
    protected String defineSubmitButtonTooltip() {
        return _("Assign selected tasks to user");
    }

    @Override
    public void defineTitle() {
        setTitle(_("Select user to assign task"));

    }

}
