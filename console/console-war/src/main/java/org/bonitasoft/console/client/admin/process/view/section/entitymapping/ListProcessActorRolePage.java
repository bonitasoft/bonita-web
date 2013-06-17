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

import java.util.HashMap;

import org.bonitasoft.console.client.admin.process.view.section.entitymapping.action.ShowAddRoleToActorPageAction;
import org.bonitasoft.console.client.common.view.ViewParameter;
import org.bonitasoft.web.rest.api.model.bpm.process.ActorItem;
import org.bonitasoft.web.rest.api.model.bpm.process.ActorMemberItem;
import org.bonitasoft.web.rest.api.model.identity.MemberType;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.ui.action.HistoryBackAction;
import org.bonitasoft.web.toolkit.client.ui.component.button.ButtonAction;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTable;
import org.bonitasoft.web.toolkit.client.ui.component.table.Table;

/**
 * Role List page
 * 
 * @author Julien Mege
 */
public class ListProcessActorRolePage extends ListProcessActorMemberPage {

    public static final String TOKEN = "ListProcessActorRole";

    @Override
    public void defineTitle(final ActorItem actor) {
        setTitle(_("Edit roles of actor %actor_name%", new Arg("actor_name", getDisplayName(actor))));
    }

    @Override
    public void buildView(final ActorItem actor) {
        addBody(rolesTable(actor));
    }

    private ItemTable rolesTable(final ActorItem actor) {
        final String actorId = actor.getId().toString();
        final HashMap<String, String> params = buildItemTableParameters(actorId);
        return rolesItemTable(actor, actorId, params);
    }

    private HashMap<String, String> buildItemTableParameters(final String actorId) {
        final String processId = this.getParameter(ViewParameter.PROCESS_ID);
        final HashMap<String, String> params = new HashMap<String, String>();
        params.put(ActorMemberItem.ATTRIBUTE_ACTOR_ID, actorId);
        params.put(DeleteActorMemberPage.PARAM_REDIRECT_TOKEN, TOKEN);
        params.put(ViewParameter.PROCESS_ID, processId);
        return params;
    }

    private ItemTable rolesItemTable(final ActorItem actor, final String actorId, final HashMap<String, String> params) {
        return buildItemTable(actor.getId(), MemberType.ROLE, params)
                .addAction(new ButtonAction("btn-add-role", _("Add a role"), "", new ShowAddRoleToActorPageAction(actorId)))
                .addAction(new ButtonAction("btn-close", _("Close"), "", new HistoryBackAction()))
                .setView(Table.VIEW_TYPE.FORM);
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }
}
