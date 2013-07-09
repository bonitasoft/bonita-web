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
package org.bonitasoft.test.toolkit.organization;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Séverin Moussel
 * 
 */
public class TestMembershipFactory {

    private final Map<String, TestMembership> membershipList;

    private static TestMembershipFactory instance;

    /**
     * Default Constructor.
     */
    public TestMembershipFactory() {
        this.membershipList = new HashMap<String, TestMembership>();
    }

    public static TestMembershipFactory getInstance() {
        if (instance == null) {
            instance = new TestMembershipFactory();
        }
        return instance;
    }

    public void clear() {
        this.membershipList.clear();
    }

    /**
     * @return the userList
     */
    private Map<String, TestMembership> getMembershipList() {
        return this.membershipList;
    }

    // ////////////////////////////////////////////////////////////////////////////////
    // / Membership's creation
    // ////////////////////////////////////////////////////////////////////////////////

    /**
     * @param name
     * @param description
     * @return
     */
    public static TestMembership assignMembership(final long userId, final long groupId, final long roleId) {
        return new TestMembership(userId, groupId, roleId);
    }

    /**
     * @param name
     * @param description
     * @return
     */
    public static TestMembership assignMembership(final TestUser user, final TestGroup group, final TestRole role) {
        return new TestMembership(user.getId(), group.getId(), role.getId());
    }

}
