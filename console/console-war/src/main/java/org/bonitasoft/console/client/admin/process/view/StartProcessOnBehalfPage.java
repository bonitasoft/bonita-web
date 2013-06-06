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
package org.bonitasoft.console.client.admin.process.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.console.client.common.view.SelectMembershipAndDoPageOnItem;
import org.bonitasoft.console.client.model.bpm.process.ProcessDefinition;
import org.bonitasoft.console.client.model.bpm.process.ProcessItem;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.ui.action.form.FormAction;
import org.bonitasoft.web.toolkit.client.ui.component.Paragraph;
import org.bonitasoft.web.toolkit.client.ui.utils.Message;

/**
 * @author Julien Mege
 */
public class StartProcessOnBehalfPage extends SelectMembershipAndDoPageOnItem<ProcessItem> {

    public static final String TOKEN = "startprocessonbehalf";

    @Override
    public String defineToken() {
        return TOKEN;
    }

    public StartProcessOnBehalfPage(final APIID itemId) {
        super(itemId, ProcessDefinition.get());
    }

    public StartProcessOnBehalfPage() {
        super(ProcessDefinition.get());
    }

    public StartProcessOnBehalfPage(final String itemId) {
        super(itemId, ProcessDefinition.get());
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CONFIGURATION
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected FormAction defineSubmitButtonAction() {
        // FixMe : Call the right action

        return new FormAction() {

            @Override
            public void execute() {
                Message.info(getParameters().toString());
            }
        };
    }

    @Override
    protected void defineTitle(final ProcessItem item) {
        this.setTitle(_("Start a app on behalf of another user"));
    }

    @Override
    protected String defineSubmitButtonLabel(final ProcessItem item) {
        return _("Start");
    }

    @Override
    protected String defineSubmitButtonTooltip(final ProcessItem item) {
        return _("Start this app on behalf of selected user");
    }

    @Override
    protected void buildBefore(final ProcessItem item) {
        addBody(new Paragraph(
                _("Select a user to start app %app_name% %app_version%",
                        new Arg("app_name", item.getDisplayName()),
                        new Arg("app_version", item.getVersion()))));

        addBody(new Paragraph(_("The openned case will be marked as started by him.")));
    }
}
