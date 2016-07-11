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
package org.bonitasoft.test.toolkit;

import org.bonitasoft.test.toolkit.organization.TestToolkitCtx;
import org.bonitasoft.test.toolkit.organization.TestUser;
import org.junit.After;
import org.junit.Before;

/**
 * @author Vincent Elcrin, Anthony Birembaut
 * 
 *         Template of JUnit Test. Do initialization as well as necessary clean up.
 */
public abstract class AbstractJUnitTest extends EngineSetup {

    @Before
    public final void aaSetUp() throws Exception {
        getContext().check();
        getContext().setInitiator(getInitiator());
        testSetUp();
    }


    @After
    public final void zzTearDown() throws Exception {
        testTearDown();
        getContext().clearSession();
    }


    /**
     * Initiator is a convenient notion to set a test user as source of all transactions done with the framework via its API Session.
     * It still can be overridden for all methods of the framework and also can be set for the session with {@link TestToolkitCtx#setInitiator(TestUser)}
     * 
     * @return
     * @throws Exception
     */
    protected abstract TestUser getInitiator();

    /**
     * Define context to use during the test. Two implementations exist. TestToolkitCtx and TestToolkitCtx (SP version)
     * 
     * @return
     */
    protected abstract TestToolkitCtx getContext();

    /**
     * JUnit's {@link Before} implementation.
     * 
     * @throws Exception
     */
    protected abstract void testSetUp() throws Exception;

    /**
     * JUnit's {@link After} implementation.
     * 
     * @throws Exception
     */
    protected abstract void testTearDown() throws Exception;
}
