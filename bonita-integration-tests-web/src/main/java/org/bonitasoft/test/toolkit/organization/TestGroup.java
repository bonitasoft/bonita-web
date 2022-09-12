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
import org.bonitasoft.engine.identity.Group;
import org.bonitasoft.engine.identity.GroupCreator;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.test.toolkit.bpm.TestActor;
import org.bonitasoft.test.toolkit.exception.TestToolkitException;

/**
 * @author Vincent Elcrin
 * 
 */
public class TestGroup implements TestActor {

    private final Group group;

    public TestGroup(final APISession apiSession, final GroupCreator creator) {
        this.group = createGroup(apiSession, creator);
        /*
        System.err.println("\n\n");
        System.err.println("Building group: " + group.getName());
        Thread.dumpStack();
        System.err.println("\n\n");
        */
    }

    public TestGroup(final GroupCreator creator) {
        this.group = createGroup(TestToolkitCtx.getInstance().getInitiator().getSession(), creator);
        /*
        System.err.println("\n\n");
        System.err.println("Building group: " + group.getName());
        Thread.dumpStack();
        System.err.println("\n\n");
        */
    }

    private Group createGroup(APISession apiSession, GroupCreator creator) {
        final IdentityAPI identityAPI = new IdentityAccessor().getIdentityAPI(apiSession);
        try {
            return identityAPI.createGroup(creator);
        } catch (final Exception e) {
            throw new TestToolkitException("Can't create group", e);
        } 
    }

    public void delete() throws Exception {
        final IdentityAPI identityAPI = new IdentityAccessor().getIdentityAPI(TestToolkitCtx.getInstance().getInitiator().getSession());
        try {
            identityAPI.deleteGroup(this.group.getId());
        } catch (final Exception e) {
            throw new TestToolkitException("Can't delete group", e);
        }
    }

    public Group getGroup() {
        return this.group;
    }

    public long getId() {
        return this.group.getId();
    }
}
