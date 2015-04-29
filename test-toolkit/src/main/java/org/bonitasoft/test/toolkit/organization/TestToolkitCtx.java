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

import org.bonitasoft.test.toolkit.bpm.TestCategoryFactory;
import org.bonitasoft.test.toolkit.bpm.TestProcessFactory;

/**
 * @author Vincent Elcrin
 * 
 */
public class TestToolkitCtx {

    protected static final String PLATFORM = "platform";

    protected static final String ADMIN_USER = "adminuser";

    protected static final String INITIATOR = "initiator";

    protected static TestToolkitCtx instance;

    protected final Map<String, Object> sessionsVariables;

    protected TestToolkitCtx() {
        this.sessionsVariables = new HashMap<String, Object>();
    }

    /**
     * Singleton
     */
    public static TestToolkitCtx getInstance() {
        if (instance == null) {
            instance = new TestToolkitCtx();
        }

        return instance;
    }

    public void clearSession() throws Exception {
        /*
         * Clear Factories
         */
        clearFactories();

        /*
         * Clear platform from its tenants
         */
        //getPlatform().destroy();

        /*
         * Clear session's variables
         */
        this.sessionsVariables.clear();
    }

    protected void clearFactories() throws Exception {
        TestUserFactory.getInstance().clear();
        TestProcessFactory.getInstance().clear();
        TestGroupFactory.getInstance().clear();
        TestRoleFactory.getInstance().clear();
        TestCategoryFactory.getInstance().clear();
    }

    protected void checkFactories() throws Exception {
        TestUserFactory.getInstance().check();
        TestProcessFactory.getInstance().check();
        TestGroupFactory.getInstance().check();
        TestRoleFactory.getInstance().check();
        TestCategoryFactory.getInstance().check();
    }
    
    // ///////////////////////////////////////////////////////
    // / Session's variables
    // ///////////////////////////////////////////////////////

    /**
     * Get the unique platform of the session
     * 
     * @return
     */
    protected TestPlatform getPlatform() {
        if (!this.sessionsVariables.containsKey(PLATFORM)) {
            this.sessionsVariables.put(PLATFORM, new TestPlatform());
        }
        return (TestPlatform) this.sessionsVariables.get(PLATFORM);
    }

    /**
     * Admin user use login and password used during default tenant creation
     * 
     * @return The Admin user
     * @throws Exception
     */
    public AdminUser getAdminUser() {
        if (!this.sessionsVariables.containsKey(ADMIN_USER)) {
            this.sessionsVariables.put(ADMIN_USER, new AdminUser());
        }
        return (AdminUser) this.sessionsVariables.get(ADMIN_USER);
    }

    /**
     * Initiator is a convenient notion to set a test user as source of all transactions done with the framework via its API Session.
     * 
     * @param initiator
     */
    public void setInitiator(final TestUser initiator) {
        this.sessionsVariables.put(INITIATOR, initiator);
    }

    public TestUser getInitiator() {
        if (!this.sessionsVariables.containsKey(INITIATOR)) {
            setInitiator(getAdminUser());
        }
        return (TestUser) this.sessionsVariables.get(INITIATOR);
    }

    public void check() throws Exception {
        checkFactories();
    }
}
