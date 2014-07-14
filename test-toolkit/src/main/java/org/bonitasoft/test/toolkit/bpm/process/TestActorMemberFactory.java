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
package org.bonitasoft.test.toolkit.bpm.process;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.actor.ActorMember;
import org.bonitasoft.test.toolkit.organization.TestGroup;
import org.bonitasoft.test.toolkit.organization.TestRole;
import org.bonitasoft.test.toolkit.organization.TestToolkitCtx;
import org.bonitasoft.test.toolkit.organization.TestUser;

/**
 * @author Colin PUY
 * 
 */
public class TestActorMemberFactory {

    public static ActorMember createMembershipActorMember(long actorId, TestGroup testGroup, TestRole testRole) throws Exception {
        return getProcessAPI().addRoleAndGroupToActor(actorId, testRole.getId(), testGroup.getId());
    }

    public static ActorMember createUserActorMember(long actorId, TestUser testUser) throws Exception {
        return getProcessAPI().addUserToActor(actorId, testUser.getId());
    }
    
    public static ActorMember createGroupActorMember(long actorId, TestGroup testGroup) throws Exception {
        return getProcessAPI().addGroupToActor(actorId, testGroup.getId());
    }
    
    private static ProcessAPI getProcessAPI() throws Exception {
        return TenantAPIAccessor.getProcessAPI(TestToolkitCtx.getInstance().getInitiator().getSession());
    }

}
