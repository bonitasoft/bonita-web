/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.console.client.admin.process.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.console.client.admin.process.action.DeleteProcessAction;
import org.bonitasoft.console.common.client.FilterKey;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.Page;
import org.bonitasoft.web.toolkit.client.ui.component.form.Form;

/**
 * @author Haojie Yuan
 */
public class DeleteProcessPage extends Page {

    public final static String TOKEN = "deleteProcessPage";

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    public void defineTitle() {
        this.setTitle(_("Confirm delete ?"));
    }

    @Override
    public void buildView() {
        final String processId = this.getParameter(FilterKey.PROCESS_ID.name());
        addBody(new Form(new JsId("deleteForm")).addCancelButton().addButton(new JsId("deleteActionForm"), _("Delete"),
                _("Confirm delete ?"), new DeleteProcessAction(processId, "")));

    }

}
