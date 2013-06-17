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
import java.util.Map;

import org.bonitasoft.web.rest.api.model.bpm.process.ActorMemberItem;
import org.bonitasoft.web.rest.api.model.identity.UserItem;
import org.bonitasoft.web.rest.api.model.portal.profile.ProfileMemberDefinition;
import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.ui.action.ItemAction;

import com.google.gwt.user.client.Window;

/**
 * @author Yongtao Guo
 */
public class DeleteProfileMemberAction extends ItemAction {

    private final String profileMemberId;

    private final String profileId;

    public DeleteProfileMemberAction(final String profileId, final String profileMemberId) {
        super(Definitions.get(ProfileMemberDefinition.TOKEN));
        this.profileId = profileId;
        this.profileMemberId = profileMemberId;
    }

    @Override
    public void execute() {

        final HashMap<String, String> params = new HashMap<String, String>();
        params.put(UserItem.FILTER_PROFILE_ID, this.profileId);
        new APICaller<ActorMemberItem>(Definitions.get(ProfileMemberDefinition.TOKEN)).delete(this.profileMemberId, new APICallback() {

            @Override
            public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
                ViewController.refreshCurrentPage();
            }

            @Override
            public void onError(final String message, final Integer errorCode) {
                Window.alert(message);
            }
        });
    }

}
