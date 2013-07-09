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

import org.bonitasoft.engine.api.IdentityAPI;
import org.bonitasoft.engine.identity.UserMembership;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.test.toolkit.bpm.TestActor;
import org.bonitasoft.test.toolkit.exception.TestToolkitException;

/**
 * @author Séverin Moussel
 * 
 */
public class TestMembership implements TestActor {

    private final UserMembership membership;

    /**
     * Default Constructor.
     * 
     * @throws Exception
     */
    public TestMembership(final APISession apiSession, final long userId, final long groupId, final long roleId) {
        this.membership = createMembership(apiSession, userId, groupId, roleId);
    }

    public TestMembership(final long userId, final long groupId, final long roleId) {
        this(TestToolkitCtx.getInstance().getInitiator().getSession(), userId, groupId, roleId);
    }

    // //////////////////////////////////////////////////////////////////////////////////
    // / Membership creation
    // //////////////////////////////////////////////////////////////////////////////////

    private UserMembership createMembership(final APISession apiSession, final long userId, final long groupId, final long roleId) {
        final IdentityAPI identityAPI = TestUser.getIdentityAPI(apiSession);
        try {
            return identityAPI.addUserMembership(userId, groupId, roleId);
        } catch (final Exception e) {
            throw new TestToolkitException("Can't create membership <" + userId + "/" + groupId + "/" + roleId + "/" + ">", e);
        }
    }

    // //////////////////////////////////////////////////////////////////////////////////
    // / Getter
    // //////////////////////////////////////////////////////////////////////////////////

    /**
     * @return the membership
     */
    public UserMembership getMembership() {
        return this.membership;
    }

    /*
     * (non-Javadoc)
     * @see org.bonitasoft.test.bpm.TestActor#getId()
     */
    public long getId() {
        return this.membership.getId();
    }
}
