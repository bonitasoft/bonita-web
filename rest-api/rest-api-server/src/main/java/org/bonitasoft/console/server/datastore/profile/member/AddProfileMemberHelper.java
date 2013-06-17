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
package org.bonitasoft.console.server.datastore.profile.member;

import org.bonitasoft.console.server.engineclient.ProfileMemberEngineClient;
import org.bonitasoft.engine.profile.ProfileMember;
import org.bonitasoft.web.rest.api.model.portal.profile.ProfileMemberItem;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.server.api.DatastoreHasAdd;

/**
 * @author Vincent Elcrin
 * 
 */
public class AddProfileMemberHelper implements DatastoreHasAdd<ProfileMemberItem> {

    private static final Long UNSET = null;

    private ProfileMemberEngineClient engineClient;

    public AddProfileMemberHelper(ProfileMemberEngineClient engineClient) {
        this.engineClient = engineClient;
    }

    @Override
    public ProfileMemberItem add(ProfileMemberItem item) {
        ProfileMember addedProfileMember = 
                addProfileMember(item.getProfileId(), item.getUserId(), item.getGroupId(), item.getRoleId());
        return new ProfileMemberItemConverter().convert(addedProfileMember);
    }


    private ProfileMember addProfileMember(APIID profileId, APIID userId, APIID groupId, APIID roleId) {
        return engineClient.createProfileMember(toLong(profileId), toLong(userId), toLong(groupId), toLong(roleId));

    }

    private Long toLong(APIID apiId) {
        if (apiId == null) {
            return UNSET;
        }
        return apiId.toLong();
    }

}
