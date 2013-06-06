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

import org.bonitasoft.console.client.admin.process.view.section.entitymapping.action.ShowAddGroupToActorPageAction;
import org.bonitasoft.console.client.model.bpm.process.ActorItem;
import org.bonitasoft.console.client.model.bpm.process.ActorMemberItem;
import org.bonitasoft.console.client.model.identity.MemberType;
import org.bonitasoft.console.common.client.FilterKey;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.HistoryBackAction;
import org.bonitasoft.web.toolkit.client.ui.component.button.ButtonAction;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTable;
import org.bonitasoft.web.toolkit.client.ui.component.table.Table;

/**
 * Role List page
 * 
 * @author Julien Mege
 */
public class ListProcessActorGroupPage extends ListProcessActorMemberPage {

    public static final String TOKEN = "ListProcessActorGroup";

    @Override
    public void defineTitle(final ActorItem actor) {
        setTitle(_("Edit groups of actor %actor_name%", new Arg("actor_name", getDisplayName(actor))));
    }

    @Override
    public void buildView(final ActorItem actor) {
        addBody(groupsTable(actor));
    }

    private ItemTable groupsTable(final ActorItem actor) {
        final String actorId = actor.getId().toString();
        final String processId = this.getParameter(FilterKey.PROCESS_ID.name());
        final HashMap<String, String> parameters = buildItemTableParameters(actorId, processId);
        return groupItemTable(actor, actorId, parameters);
    }

    private HashMap<String, String> buildItemTableParameters(final String actorId, final String processId) {
        final HashMap<String, String> params = new HashMap<String, String>();
        params.put(ActorMemberItem.ATTRIBUTE_ACTOR_ID, actorId);
        params.put(DeleteActorMemberPage.PARAM_REDIRECT_TOKEN, TOKEN);
        params.put(FilterKey.PROCESS_ID.name(), processId);
        return params;
    }

    private ItemTable groupItemTable(final ActorItem actor, final String actorId, final HashMap<String, String> parameters) {
        return buildItemTable(actor.getId(), MemberType.GROUP, parameters)
                .addGroupedAction(new JsId(_("add")), _("Add a group"), "", new ShowAddGroupToActorPageAction(actorId), true)
                .addAction(new ButtonAction("btn-cancel", _("Close"), "", new HistoryBackAction()))
                .setView(Table.VIEW_TYPE.FORM);
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }
}
