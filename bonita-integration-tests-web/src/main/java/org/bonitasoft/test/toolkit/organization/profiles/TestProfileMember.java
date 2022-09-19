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
package org.bonitasoft.test.toolkit.organization.profiles;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.bonitasoft.test.toolkit.utils.CommandCaller;

/**
 * @author Vincent Elcrin
 * 
 *         Associate User, Role or Group to a membership
 * 
 */
public class TestProfileMember {

    private static final String COMMAND_ADD = "addProfileMember";

    public static final String PROFILE_ID = "profileId";

    private static final String ROLE_ID = "roleId";

    private static final String GROUP_ID = "groupId";

    public static final String USER_ID = "userId";

    private static final String PROFILE_MEMBER_ID = "profileMemberId";

    private final TestProfile profile;

    private long userId = -1L;

    private final long roleId = -1L;

    private long groupId = -1L;

    private long membershipId = -1L;

    private final CommandCaller commandCaller;

    TestProfileMember(CommandCaller commandCaller, TestProfile profile) {
        this.commandCaller = commandCaller;
        this.profile = profile;
    }

    public TestProfileMember create() {
        membershipId = MapUtils.getLongValue(createMembership(), PROFILE_MEMBER_ID);
        return this;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Serializable> createMembership() {
        return (Map<String, Serializable>) commandCaller.run(COMMAND_ADD, makeCreateParameters());
    }

    private Map<String, Serializable> makeCreateParameters() {
        Map<String, Serializable> parameters = new HashMap<>();
        parameters.put(PROFILE_ID, profile.getId());
        parameters.put(USER_ID, userId);
        parameters.put(ROLE_ID, roleId);
        parameters.put(GROUP_ID, groupId);
        return parameters;
    }

    /*
     * GETTER & SETTERS
     */

    public TestProfileMember setUserId(long userId) {
        this.userId = userId;
        return this;
    }

    public TestProfileMember setGroupId(long groupId) {
        this.groupId = groupId;
        return this;
    }

    public long getMembershipId() {
        return membershipId;
    }

}
