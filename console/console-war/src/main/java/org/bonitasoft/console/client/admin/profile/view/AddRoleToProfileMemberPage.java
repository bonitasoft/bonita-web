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
package org.bonitasoft.console.client.admin.profile.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bonitasoft.console.client.admin.profile.action.AddProfileMemberAction;
import org.bonitasoft.web.rest.model.identity.MemberType;
import org.bonitasoft.web.rest.model.identity.RoleDefinition;
import org.bonitasoft.web.rest.model.identity.RoleItem;
import org.bonitasoft.web.rest.model.identity.UserItem;
import org.bonitasoft.web.rest.model.portal.profile.ProfileDefinition;
import org.bonitasoft.web.rest.model.portal.profile.ProfileItem;
import org.bonitasoft.web.toolkit.client.common.json.JSonItemReader;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.Page;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.action.HistoryBackAction;
import org.bonitasoft.web.toolkit.client.ui.component.Button;
import org.bonitasoft.web.toolkit.client.ui.component.Strong;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTable;
import org.bonitasoft.web.toolkit.client.ui.component.table.Table;

/**
 * User List page
 * 
 * @author Yongtao Guo
 */
public class AddRoleToProfileMemberPage extends Page {

    private static final String PARAMETER_PROFILE_ID = "profileId";
    
    public static final List<String> PRIVILEGES = new ArrayList<String>();
    
    static {
        PRIVILEGES.add(ProfileListingPage.TOKEN);
    }

    /**
     * the token of this page
     */
    public static final String TOKEN = "addRoleToProfileMember";

    public AddRoleToProfileMemberPage() {
    }

    public AddRoleToProfileMemberPage(APIID profileId) {
        addParameter(PARAMETER_PROFILE_ID, profileId.toString());
    }

    @Override
    public void defineTitle() {
        this.setTitle("");
        Definitions.get(ProfileDefinition.TOKEN).getAPICaller().get(this.getParameter(PARAMETER_PROFILE_ID), new APICallback() {

            @Override
            public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
                final ProfileItem item = (ProfileItem) new JSonItemReader().getItem(response, Definitions.get(ProfileDefinition.TOKEN));
                final String profileName = item.getAttributeValue(ProfileItem.ATTRIBUTE_NAME);
                setTitle(_("Add a role to profile %%"), new Strong(_(profileName)));
            }
        });
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    public void buildView() {

        final String profileId = this.getParameter(PARAMETER_PROFILE_ID);
        final HashMap<String, String> params = new HashMap<String, String>();
        params.put(UserItem.FILTER_PROFILE_ID, profileId);
        final AddProfileMemberAction action = new AddProfileMemberAction(profileId, MemberType.ROLE.name(), null);

        addBody(createRoleTable(action, new HistoryBackAction()));

    }

    public ItemTable createRoleTable(final Action saveAction, final Action cancelAction) {

        final ItemTable itemTable = new ItemTable(Definitions.get(RoleDefinition.TOKEN)).addColumn(RoleItem.ATTRIBUTE_ICON, _("Icon"))
                .addColumn(RoleItem.ATTRIBUTE_NAME, _("Original name"), true, true)

                .addGroupedAction(new JsId("add"), _("Add"), _("Add selected role(s) to this profile"), saveAction)
                .addAction(new Button("btn-cancel", _("Cancel"), _("Close without saving"), new HistoryBackAction()))

                // Define the default view
                .setView(Table.VIEW_TYPE.FORM);

        return itemTable;

    }

}
