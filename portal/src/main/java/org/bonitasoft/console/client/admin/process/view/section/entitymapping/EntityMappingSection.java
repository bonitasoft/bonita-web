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
package org.bonitasoft.console.client.admin.process.view.section.entitymapping;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.console.client.admin.process.view.section.configuration.ConfigurationState;
import org.bonitasoft.console.client.admin.process.view.section.configuration.ConfigurationStateText;
import org.bonitasoft.console.client.admin.profile.view.ProfileListingPage;
import org.bonitasoft.console.client.common.event.handler.HideComponentOnEmptyTableHandler;
import org.bonitasoft.web.rest.model.bpm.process.ActorDefinition;
import org.bonitasoft.web.rest.model.bpm.process.ActorItem;
import org.bonitasoft.web.rest.model.bpm.process.ProcessItem;
import org.bonitasoft.web.toolkit.client.AvailableTokens;
import org.bonitasoft.web.toolkit.client.SHA1;
import org.bonitasoft.web.toolkit.client.Session;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.common.util.MapUtil;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.ui.CssId;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.popup.PopupAction;
import org.bonitasoft.web.toolkit.client.ui.component.Link;
import org.bonitasoft.web.toolkit.client.ui.component.Paragraph;
import org.bonitasoft.web.toolkit.client.ui.component.Section;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTable;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTableAction;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTableActionSet;
import org.bonitasoft.web.toolkit.client.ui.component.table.Table;

/**
 * @author Colin PUY
 * 
 */
public class EntityMappingSection extends Section {

    public EntityMappingSection(final ProcessItem process, final ConfigurationState state) {
        super(new JsId("Entity mapping"), _("Actor mapping"));

        setId(CssId.MD_SECTION_ENTITY_MAPPING);

        final ConfigurationStateText configurationStateText = new ConfigurationStateText(state);
        final ItemTable entityMappingTable = newEntityMappingTable(process);
        entityMappingTable.addItemTableLoadedHandler(
                new HideComponentOnEmptyTableHandler(configurationStateText));

        addHeader(configurationStateText);
        addBody(new Paragraph(
                _("Select the entities (users, groups, roles, memberships) to map to the actors. These entities will do the human tasks in the app."))
                .addClass("section_description"));
        addBody(entityMappingTable);
        final String explanations = _("For each actor, check that each entity (user, group, role, membership) has the relevant user profile in the Portal.");

        if (AvailableTokens.tokens.contains(hasRightToAccessProfilePage())) {
            addBody(new Paragraph(
                    explanations + _(" To do so, go to %entitymappingprofilelink%."),
                    new Link(new JsId("entitymappingprofilelink"), _("Profiles"), _("Click here to go to Profile listing page"), ProfileListingPage.TOKEN)));

        } else {
            addBody(new Paragraph(explanations + _("To do so, contact your administrator")));
        }

    }

    protected String hasRightToAccessProfilePage() {
        return new String(SHA1.calcSHA1(ProfileListingPage.TOKEN.concat(new String(Session.getParameter("session_id"))))).toUpperCase();
    }

    private ItemTable newEntityMappingTable(final ProcessItem process) {
        return new ItemTable(Definitions.get(ActorDefinition.TOKEN))
                .addColumn(ActorItem.ATTRIBUTE_NAME, _("Actor name"), true, true)
                .addColumn(ActorItem.ATTRIBUTE_DISPLAY_NAME, _("Display name"))
                // .addColumn(ActorItem.ATTRIBUTE_DESCRIPTION, _("Description"))
                .addHiddenFilter(ActorItem.ATTRIBUTE_PROCESS_ID, process.getId())
                .addActions(new EntityMappingTableActionSet())
                .setView(Table.VIEW_TYPE.VIEW_TABLE)
                .setShowSearch(false);
    }

    private class EntityMappingTableActionSet extends ItemTableActionSet<ActorItem> {

        public EntityMappingTableActionSet() {
            super();
            /*
             * Define counters for the ItemTableActionSet.
             * It is done out of the defineAction because it is only call after the get request and we want the counters to be sent with it.
             */
            addCounter(ActorItem.COUNTER_USERS);
            addCounter(ActorItem.COUNTER_GROUPS);
            addCounter(ActorItem.COUNTER_ROLES);
            addCounter(ActorItem.COUNTER_MEMBERSHIPS);
        }

        @Override
        protected void defineActions(final ActorItem actor) {
            addAction(userTableAction(actor));
            addAction(groupTableAction(actor));
            addAction(roleTableAction(actor));
            addAction(membershipTableAction(actor));
        }

        private ItemTableAction userTableAction(final ActorItem actor) {
            return new ItemTableAction(_("User (%nb_user%)", new Arg("nb_user", actor.getNbSelectedUsers())),
                    _("Add a user"), newListProcessActorAction(actor, ListProcessActorUserPage.TOKEN));
        }

        private ItemTableAction groupTableAction(final ActorItem actor) {
            return new ItemTableAction(
                    _("Group (%nb_group%)", new Arg("nb_group", actor.getNbSelectedGroups())),
                    _("Add a group"), newListProcessActorAction(actor, ListProcessActorGroupPage.TOKEN));
        }

        private ItemTableAction roleTableAction(final ActorItem actor) {
            return new ItemTableAction(
                    _("Role (%nb_role%)", new Arg("nb_role", actor.getNbSelectedRoles())),
                    _("Add a role"), newListProcessActorAction(actor, ListProcessActorRolePage.TOKEN));
        }

        private ItemTableAction membershipTableAction(final ActorItem actor) {
            return new ItemTableAction(
                    _("Membership (%nb_membership%)", new Arg("nb_membership", actor.getNbSelectedMembershipss())),
                    _("Add a membership"), newListProcessActorAction(actor, ListProcessActorMembershipPage.TOKEN));
        }

        private PopupAction newListProcessActorAction(final ActorItem actor, final String token) {
            return new PopupAction(token, MapUtil.asMap(new Arg(ActorItem.ATTRIBUTE_PROCESS_ID, actor.getId())));
        }
    }
}
