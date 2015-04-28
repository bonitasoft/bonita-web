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

import org.bonitasoft.engine.identity.Role;
import org.bonitasoft.engine.identity.RoleCreator;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.test.toolkit.bpm.TestActor;
import org.bonitasoft.test.toolkit.exception.TestToolkitException;

/**
 * @author Séverin Moussel
 * 
 */
public class TestRole extends IdentityAccessor implements TestActor  {

    private final Role role;
    private APISession apiSession;

    public TestRole(RoleCreator creator) {
        this(TestToolkitCtx.getInstance().getInitiator().getSession(), creator);
    }

    public TestRole(APISession apiSession, RoleCreator creator) {
        this.apiSession = apiSession;
        this.role = createRole(creator);
        /*
        System.err.println("\n\n");
        System.err.println("Building role: " + role.getName());
        Thread.dumpStack();
        System.err.println("\n\n");
        */
    }

    private Role createRole(RoleCreator creator) {
        try {
            return getIdentityAPI(apiSession).createRole(creator);
        } catch (final Exception e) {
            throw new TestToolkitException("Can't create role <" + creator + ">", e);
        }
    }

    public Role getRole() {
        return this.role;
    }

    public long getId() {
        return this.role.getId();
    }

    public void delete() {
        try {
            getIdentityAPI(apiSession).deleteRole(this.role.getId());
        } catch (final Exception e) {
            throw new TestToolkitException("Can't delete role <" + this.role.getId() + ">", e);
        }
    }
}
