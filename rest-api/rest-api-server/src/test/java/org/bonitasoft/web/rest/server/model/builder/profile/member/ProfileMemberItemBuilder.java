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
package org.bonitasoft.web.rest.server.model.builder.profile.member;

import org.bonitasoft.engine.profile.ProfileMember;
import org.bonitasoft.web.rest.model.portal.profile.ProfileMemberItem;

/**
 * @author Vincent Elcrin
 * 
 */
public class ProfileMemberItemBuilder extends AbstractProfileMemberBuilder<ProfileMemberItem> {

    public static ProfileMemberItemBuilder aProfileMemberItem() {
        return new ProfileMemberItemBuilder();
    }

    public ProfileMemberItem build() {
        ProfileMemberItem item = new ProfileMemberItem();
        item.setId(id);
        item.setProfileId(profileId);
        item.setUserId(userId);
        item.setGroupId(groupId);
        item.setRoleId(roleId);
        return item;
    }

    public ProfileMemberItemBuilder from(ProfileMember profileMember) {
        id = profileMember.getId();
        profileId = profileMember.getProfileId();
        userId = profileMember.getUserId();
        groupId = profileMember.getGroupId();
        roleId = profileMember.getRoleId();
        return this;
    }

}
