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
package org.bonitasoft.console.common.server.auth.impl.standard;

import static org.junit.Assert.fail;

import org.bonitasoft.console.common.server.AbstractJUnitWebTest;
import org.bonitasoft.console.common.server.auth.AuthenticationFailedException;
import org.bonitasoft.console.common.server.login.HttpServletRequestAccessor;
import org.bonitasoft.console.common.server.login.credentials.StandardCredentials;
import org.bonitasoft.test.toolkit.organization.TestUser;
import org.bonitasoft.test.toolkit.organization.TestUserFactory;
import org.bonitasoft.test.toolkit.server.MockHttpServletRequest;
import org.junit.Test;

/**
 * @author Rohart Bastien
 *
 */
public class StandardAuthenticationManagerImplIntegrationTest extends AbstractJUnitWebTest {

    private static final String TECHNICAL_USER_USERNAME = "install";

    private static final String TECHNICAL_USER_PASSWORD = "install";

    @Override
    public void webTestSetUp() throws Exception {

    }

    @Override
    protected TestUser getInitiator() {
        return TestUserFactory.getJohnCarpenter();
    }

    @Test
    public void testLogin() {
        final HttpServletRequestAccessor request = new HttpServletRequestAccessor(new MockHttpServletRequest());
        try {
            new StandardAuthenticationManagerImpl()
                    .authenticate(request,
                            new StandardCredentials(TECHNICAL_USER_USERNAME, TECHNICAL_USER_PASSWORD, -1L));
        } catch (final AuthenticationFailedException e) {
            fail("Cannot login " + e);
        }

    }

}
