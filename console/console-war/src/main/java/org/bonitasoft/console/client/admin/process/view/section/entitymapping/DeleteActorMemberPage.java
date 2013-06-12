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
package org.bonitasoft.console.client.admin.process.view.section.entitymapping;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.console.client.admin.process.action.DeleteActorMemberAction;
import org.bonitasoft.console.client.common.view.ViewParameter;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.Page;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.action.HistoryBackAction;
import org.bonitasoft.web.toolkit.client.ui.component.form.Form;

/**
 * @author Yongtao Guo
 */
public class DeleteActorMemberPage extends Page {

    public final static String TOKEN = "deleteitempage";

    public final static String PARAM_REDIRECT_TOKEN = "redirecttoken";

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    public void defineTitle() {
        this.setTitle(_("Confirm deletion ?"));
    }

    @Override
    public void buildView() {
        final String actorMemberId = this.getParameter(ViewParameter.ID);
        final String redirectToken = this.getParameter(PARAM_REDIRECT_TOKEN);
        final String actorId = this.getParameter("actor_id");
        final String processId = this.getParameter(ViewParameter.PROCESS_ID);
        final DeleteActorMemberAction action = new DeleteActorMemberAction(actorId, actorMemberId, processId, redirectToken);

        addBody(createDeleteForm(action, new HistoryBackAction()));

    }

    public static Form createDeleteForm(final Action okAction, final Action cancelAction) {
        final Form form = new Form(new JsId("deleteForm")).addButton(new JsId("deleteActionForm"), _("Delete"),
                _("Delete"), okAction).addButton(new JsId("cancelActionForm"), _("Cancel"),
                _("Cancel"), cancelAction);

        return form;
    }

}
