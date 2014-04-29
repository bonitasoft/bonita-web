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
package org.bonitasoft.console.client.admin.organization.users.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.console.client.admin.organization.group.GroupListingAdminPage;
import org.bonitasoft.console.client.admin.organization.role.RoleListingPage;
import org.bonitasoft.console.client.admin.organization.users.action.AddMembershipAction;
import org.bonitasoft.console.client.common.identity.view.PageOnUserItem;
import org.bonitasoft.console.client.data.item.attribute.reader.UserAttributeReader;
import org.bonitasoft.web.rest.model.identity.GroupDefinition;
import org.bonitasoft.web.rest.model.identity.GroupItem;
import org.bonitasoft.web.rest.model.identity.MembershipDefinition;
import org.bonitasoft.web.rest.model.identity.MembershipItem;
import org.bonitasoft.web.rest.model.identity.RoleDefinition;
import org.bonitasoft.web.rest.model.identity.RoleItem;
import org.bonitasoft.web.rest.model.identity.UserItem;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.attribute.validator.MandatoryValidator;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.component.form.Form;

/**
 * @author Séverin Moussel
 * 
 */
public class AddMembershipPage extends PageOnUserItem {

    public static final String TOKEN = "addmembership";
    
    public static final List<String> PRIVILEGES = new ArrayList<String>();
    
    static {
        PRIVILEGES.add(UserListingAdminPage.TOKEN);
        PRIVILEGES.add(GroupListingAdminPage.TOKEN);
        PRIVILEGES.add(RoleListingPage.TOKEN);
    }

    public AddMembershipPage() {
    }

    public AddMembershipPage(final APIID userId) {
        addParameter(PARAMETER_ITEM_ID, userId.toString());
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    protected void defineTitle(final UserItem user) {
        this.setTitle(_("Add a membership to %user%", new Arg("user", UserAttributeReader.readUser(user))));
    }

    @Override
    protected void buildView(final UserItem item) {
        final JsId groupJsId = new JsId(MembershipItem.ATTRIBUTE_GROUP_ID);
        final JsId roleJsId = new JsId(MembershipItem.ATTRIBUTE_ROLE_ID);
        addBody(new Form(new JsId("addmembership"))

                .addHiddenEntry(MembershipItem.ATTRIBUTE_USER_ID, item.getId().toString())

                .addAutoCompleteEntry(
                        groupJsId,
                        _("Group"),
                        _("Select a group to set to this user"),
                        Definitions.get(GroupDefinition.TOKEN),
                        GroupItem.ATTRIBUTE_DISPLAY_NAME,
                        GroupItem.ATTRIBUTE_ID
                )
                .addAutoCompleteEntry(
                        roleJsId,
                        _("Role"),
                        _("Select the role of this user in the selected group"),
                        Definitions.get(RoleDefinition.TOKEN),
                        RoleItem.ATTRIBUTE_DISPLAY_NAME,
                        RoleItem.ATTRIBUTE_ID
                )
                .addValidator(groupJsId, new MandatoryValidator())
                .addValidator(roleJsId, new MandatoryValidator())
                .addButton(new JsId("add"), _("Add"), _("Add this membership"), new AddMembershipAction(MembershipDefinition.get()))
                .addCancelButton());

    }
}
