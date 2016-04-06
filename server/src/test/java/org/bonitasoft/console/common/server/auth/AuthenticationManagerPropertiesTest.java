/**
 * Copyright (C) 2012 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.console.common.server.auth;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Collections;

import org.bonitasoft.console.common.server.preferences.properties.ConfigurationFilesManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Rohart Bastien
 */
public class AuthenticationManagerPropertiesTest {

    public static final long TENANT_ID = 65432L;
    public AuthenticationManagerProperties loginManagerProperties;

    @Before
    public void setUp() throws IOException {
        ConfigurationFilesManager.getInstance().setTenantConfigurations(
                Collections.singletonMap("authenticationManager-config.properties",
                        ("login.LoginManager = org.bonitasoft.console.common.server.login.impl.standard.StandardLoginManagerImpl\n" +
                                "OAuth.serviceProvider = LinkedIn\n" +
                                "OAuth.consumerKey = ove2vcdjptar\n" +
                                "OAuth.consumerSecret = vdaBrCmHvkgJoYz1\n" +
                                "OAuth.callbackURL = http://127.0.0.1:8888/loginservice").getBytes()),
                TENANT_ID);
        loginManagerProperties = new AuthenticationManagerProperties(TENANT_ID);
    }

    @After
    public void tearDown() {
        loginManagerProperties = null;
    }

    @Test
    public void testGetOAuthServiceProviderName() {
        assertNotNull("Cannot get OAuth service provider name", loginManagerProperties.getOAuthServiceProviderName());
    }

    @Test
    public void testGetOAuthConsumerKey() {
        assertNotNull("Cannot get OAuth consumer key", loginManagerProperties.getOAuthConsumerKey());
    }

    @Test
    public void testGetOAuthConsumerSecret() {
        assertNotNull("Cannot get OAuth consumer secret", loginManagerProperties.getOAuthConsumerSecret());
    }

    @Test
    public void testGetOAuthCallbackURL() {
        assertNotNull("Cannot get OAuth callback URL", loginManagerProperties.getOAuthCallbackURL());
    }

}
