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

import java.util.Map;

import org.bonitasoft.console.client.data.item.attribute.reader.MemberAttributeReader;
import org.bonitasoft.web.rest.model.bpm.process.ActorDefinition;
import org.bonitasoft.web.rest.model.bpm.process.ActorItem;
import org.bonitasoft.web.rest.model.bpm.process.ActorMemberDefinition;
import org.bonitasoft.web.rest.model.bpm.process.ActorMemberItem;
import org.bonitasoft.web.rest.model.identity.MemberType;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.ItemHasDualNameAttributeReader;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.popup.ItemDeletePopupAction;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTable;
import org.bonitasoft.web.toolkit.client.ui.component.table.Table;
import org.bonitasoft.web.toolkit.client.ui.page.PageOnItem;

/**
 * Abstract Member List page
 * 
 * @author Julien Mege
 */
public abstract class ListProcessActorMemberPage extends PageOnItem<ActorItem> {

    public ListProcessActorMemberPage() {
        super(ActorDefinition.get());
    }

    protected ItemTable buildItemTable(final APIID actorId, final MemberType memberType, final Map<String, String> params) {
        return new ItemTable(ActorMemberDefinition.get())
                .addHiddenFilter(ActorMemberItem.ATTRIBUTE_ACTOR_ID, actorId)
                .addHiddenFilter(ActorMemberItem.FILTER_MEMBER_TYPE, memberType.name())
                .addColumn(new MemberAttributeReader(), _("Member"))
                .addGroupedAction(new JsId("remove"), _("Remove"), "Remove selected member", removeActorMemberAction())
                .setView(Table.VIEW_TYPE.VIEW_TABLE)
                .setShowSearch(false);
    }

    private ItemDeletePopupAction removeActorMemberAction() {
        ItemDeletePopupAction action = new ItemDeletePopupAction(ActorMemberDefinition.get());
        action.setMessage(_("Are you sur you want to remove selected actor members from the process ?"));
        action.setTitle(_("Confirm removing ?"));
        action.setConfirmButtonLabel(_("Remove"));
        return action;
    }
    
    protected String getDisplayName(ActorItem actor) {
        return new ItemHasDualNameAttributeReader().read(actor);
    }
}
