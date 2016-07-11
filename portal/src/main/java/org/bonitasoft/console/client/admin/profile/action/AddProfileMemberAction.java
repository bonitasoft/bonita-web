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
package org.bonitasoft.console.client.admin.profile.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bonitasoft.web.rest.model.identity.MemberType;
import org.bonitasoft.web.rest.model.portal.profile.ProfileMemberDefinition;
import org.bonitasoft.web.rest.model.portal.profile.ProfileMemberItem;
import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.ItemAction;
import org.bonitasoft.web.toolkit.client.ui.component.form.Form;
import org.bonitasoft.web.toolkit.client.ui.utils.Message;

/**
 * @author Yongtao Guo
 */
public class AddProfileMemberAction extends ItemAction {

    private static final String PROFILE_ID = "profile_id";

    private static final String MEMBER_TYPE = "member_type";

    private final String profileId;

    private final String type;

    private String groupId;

    public AddProfileMemberAction(final String id, final String type, final String groupId) {
        super(Definitions.get(ProfileMemberDefinition.TOKEN));
        this.profileId = id;
        this.type = type;
        if (MemberType.MEMBERSHIP.name().equals(type)) {
            this.groupId = groupId;
        }
    }

    @Override
    public void execute() {
        final List<String> ids = this.getArrayParameter("id");
        final HashMap<String, String> params = new HashMap<String, String>();
        params.put(PROFILE_ID, this.profileId);
        if (MemberType.MEMBERSHIP.name().equals(this.type) && !ids.isEmpty()) {
            final Form form = new Form(new JsId("addMemberToActor"));
            form.addHiddenEntry(PROFILE_ID, this.profileId);
            form.addHiddenEntry(MEMBER_TYPE, this.type);
            form.addHiddenEntry(ProfileMemberItem.ATTRIBUTE_GROUP_ID, this.groupId);
            form.addHiddenEntry(ProfileMemberItem.ATTRIBUTE_ROLE_ID, ids.get(0));
            sendRequest(form, params);
        } else {
            for (final String id : ids) {
                final Form form = new Form(new JsId("addMemberToProfile"));
                form.addHiddenEntry(PROFILE_ID, this.profileId);
                form.addHiddenEntry(MEMBER_TYPE, this.type);
                if (MemberType.USER.name().equals(this.type)) {
                    form.addHiddenEntry(ProfileMemberItem.ATTRIBUTE_USER_ID, id);
                } else if (MemberType.ROLE.name().equals(this.type)) {
                    form.addHiddenEntry(ProfileMemberItem.ATTRIBUTE_ROLE_ID, id);
                } else if (MemberType.GROUP.name().equals(this.type)) {
                    form.addHiddenEntry(ProfileMemberItem.ATTRIBUTE_GROUP_ID, id);
                }
                sendRequest(form, params);
            }
        }
    }

    private void sendRequest(final Form form, final HashMap<String, String> params) {
        new APICaller<ProfileMemberItem>(Definitions.get(ProfileMemberDefinition.TOKEN)).add(form, new APICallback() {

            @Override
            public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
                ViewController.refreshCurrentPage();
            }

            @Override
            protected void on403Forbidden(String message) {
                Message.warning("A mapping already exists");
            }
        });
    }

}
