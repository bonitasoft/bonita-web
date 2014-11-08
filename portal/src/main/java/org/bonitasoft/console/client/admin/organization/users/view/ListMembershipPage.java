/**
 * Copyright (C) 2011 BonitaSoft S.A.
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
package org.bonitasoft.console.client.admin.organization.users.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bonitasoft.console.client.admin.organization.group.GroupListingAdminPage;
import org.bonitasoft.console.client.admin.organization.role.RoleListingPage;
import org.bonitasoft.console.client.common.identity.view.PageOnUserItem;
import org.bonitasoft.console.client.data.item.attribute.reader.UserAttributeReader;
import org.bonitasoft.web.rest.model.identity.GroupItem;
import org.bonitasoft.web.rest.model.identity.MembershipDefinition;
import org.bonitasoft.web.rest.model.identity.MembershipItem;
import org.bonitasoft.web.rest.model.identity.RoleItem;
import org.bonitasoft.web.rest.model.identity.UserItem;
import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.DeployedAttributeReader;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.HistoryBackAction;
import org.bonitasoft.web.toolkit.client.ui.action.popup.PopupAction;
import org.bonitasoft.web.toolkit.client.ui.component.Button;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTable;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTableAction;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTableActionSet;

/**
 * popup
 *
 * @author Séverin Moussel
 */
public class ListMembershipPage extends PageOnUserItem {

    /**
     * the token of this page
     */
    public static final String TOKEN = "listmembership";
    
    public static final List<String> PRIVILEGES = new ArrayList<String>();
    
    static {
        PRIVILEGES.add(UserListingAdminPage.TOKEN);
        PRIVILEGES.add(GroupListingAdminPage.TOKEN);
        PRIVILEGES.add(RoleListingPage.TOKEN);
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CONTENT
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void defineTitle(final UserItem item) {
        this.setTitle(_("List memberships of %user%", new Arg("user", UserAttributeReader.readUser(item))));
    }

    @Override
    protected void buildView(final UserItem user) {
        showAddButton(user);
        showMembershipsList(user);
        showCloseButton();
    }

    /**
     * A button that open a popup to add a new membership
     * 
     * @param user
     */
    private void showAddButton(final UserItem user) {
        // Add button
        final HashMap<String, String> userMap = new HashMap<String, String>();
        userMap.put(MembershipItem.ATTRIBUTE_USER_ID, user.getId().toString());

        addBody(new Button(
                new JsId("add_membership"),
                _("Add"),
                _("Add new memberships for %user%", new Arg("user", UserAttributeReader.readUser(user))),
                new PopupAction(AddMembershipPage.TOKEN, userMap)));
    }

    /**
     * An ItemTable of memberships for the passed user.
     * 
     * @param user
     */
    private void showMembershipsList(final UserItem user) {
        // List of existing memberships
        addBody(new ItemTable(Definitions.get(MembershipDefinition.TOKEN))

                .addHiddenFilter(MembershipItem.ATTRIBUTE_USER_ID, user.getId())

                .addColumn(new DeployedAttributeReader(MembershipItem.ATTRIBUTE_ROLE_ID, RoleItem.ATTRIBUTE_NAME), _("Role"))
                .addColumn(new DeployedAttributeReader(MembershipItem.ATTRIBUTE_GROUP_ID, GroupItem.ATTRIBUTE_NAME), _("Group"))

                // FIXME need a Custom delete because generic delete doesn't allow to go back to the popup
                // TODO Change back to a generic delete when the history manage popups
                // .addGroupedDeleteAction(_("Delete selected memberships"), Definitions.get(MembershipDefinition.TOKEN))

                .addActions(new ItemTableActionSet<MembershipItem>() {

                    @Override
                    protected void defineActions(final MembershipItem item) {
                        // Custom delete because generic delete doesn't allow to go back to the popup
                        // TODO Change back to a generic delete when the history manage popups
                        this.addAction(new ItemTableAction(_("Delete"), _("Delete this membership"),
                                new PopupAction(DeleteMembershipPage.TOKEN, new Arg("id", item.getId().toString()))));
                    }
                }));
    }

    /**
     * The button used to close the popup
     */
    private void showCloseButton() {
        // Close button
        addBody(new Button(
                new JsId(_("Close")),
                _("Close"),
                _("Close"),
                new HistoryBackAction()));
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // STATIC SHOW METHODS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void showInPopup(final APIID userId) {
        final HashMap<String, String> params = new HashMap<String, String>();
        params.put(MembershipItem.ATTRIBUTE_USER_ID, userId.toString());

        ViewController.showPopup(ListMembershipPage.TOKEN, params);
    }
}
