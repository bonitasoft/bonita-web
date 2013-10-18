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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bonitasoft.console.client.admin.bpm.cases.view.CaseListingAdminPage;
import org.bonitasoft.console.client.admin.bpm.task.action.AssignTaskAndHistoryBackAction;
import org.bonitasoft.console.client.admin.process.view.ProcessListingAdminPage;
import org.bonitasoft.console.client.common.view.SelectItemAndDoEntry;
import org.bonitasoft.console.client.common.view.SelectItemAndDoForm;
import org.bonitasoft.console.client.data.item.attribute.reader.UserAttributeReader;
import org.bonitasoft.web.rest.model.identity.UserDefinition;
import org.bonitasoft.web.rest.model.identity.UserItem;
import org.bonitasoft.web.toolkit.client.ui.Page;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.action.form.FormAction;

/**
 * @author Vincent Elcrin
 * 
 */
public class SelectUserAndAssignTaskPage extends Page {

    public static final String TOKEN = "assignTaskTo";    
    
    public static final List<String> PRIVILEGES = new ArrayList<String>();
    
    static {
        PRIVILEGES.add(TaskListingAdminPage.TOKEN);
        PRIVILEGES.add(CaseListingAdminPage.TOKEN);
        PRIVILEGES.add(ProcessListingAdminPage.TOKEN);
        PRIVILEGES.add("reportlistingadminext");
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }

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

    protected String defineSubmitButtonLabel() {
        return _("Assign");
    }

    protected String defineSubmitButtonTooltip() {
        return _("Assign selected tasks to user");
    }

    public void defineTitle() {
        setTitle(_("Select user to assign task"));

    }

    /**
     * Override this method to change the name of the parameter passed to the callback action
     * 
     * @return This method must return the name of the parameter
     */
    protected String defineUserIdParameterName() {
        return "user_id";
    }

    protected final List<SelectItemAndDoEntry> defineEntries() {
        return Arrays.asList(
                new SelectItemAndDoEntry(
                        defineUserIdParameterName(),
                        _("Select a user"),
                        _("Select a user by typing a part of his name."),
                        UserDefinition.get(),
                        new UserAttributeReader(),
                        UserItem.ATTRIBUTE_ID)
                );
    }

    @Override
    public final void buildView() {
        // Before
        buildBefore();
    
        // Build form
        final SelectItemAndDoForm selectItemAndDoForm = new SelectItemAndDoForm(
                defineEntries(),
                defineSubmitButtonLabel(),
                defineSubmitButtonTooltip(),
                defineSubmitButtonAction());
        selectItemAndDoForm.setHiddenEntries(getParameters());
    
        addBody(selectItemAndDoForm);
    }

    protected void buildBefore() {
        // Do nothing if not overriden
    }

}
