/*
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

package org.bonitasoft.console.common.server.login.filter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import javax.servlet.http.HttpServletResponse;

import org.bonitasoft.console.common.server.auth.AuthenticationManager;
import org.bonitasoft.console.common.server.login.HttpServletRequestAccessor;
import org.bonitasoft.console.common.server.login.LoginManager;
import org.bonitasoft.console.common.server.login.TenantIdAccessor;
import org.bonitasoft.console.common.server.login.credentials.AutoLoginCredentials;
import org.bonitasoft.console.common.server.login.credentials.AutoLoginCredentialsFinder;
import org.bonitasoft.console.common.server.login.credentials.StandardCredentials;
import org.bonitasoft.console.common.server.login.credentials.UserLogger;
import org.bonitasoft.console.common.server.preferences.properties.ProcessIdentifier;
import org.bonitasoft.console.common.server.preferences.properties.SecurityProperties;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Spy;

/**
 * Created by Vincent Elcrin
 * Date: 30/08/13
 * Time: 15:54
 */
public class AutoLoginRuleTest {

    @Spy
    AutoLoginRule rule;

    @Mock
    private HttpServletRequestAccessor request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private TenantIdAccessor tenantAccessor;

    @Mock
    private AutoLoginCredentialsFinder autoLoginCredentialsFinder;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        when(rule.getAutoLoginCredentialsFinder()).thenReturn(autoLoginCredentialsFinder);
    }

    @Test
    public void testWeAreNotAutoLoggedWhenNotConfigured() throws Exception {
        doReturn("process3--2.9").when(request).getAutoLoginScope();
        doReturn(1L).when(tenantAccessor).getRequestedTenantId();

        when(autoLoginCredentialsFinder.getCredential(new ProcessIdentifier("process3--2.9"),1L)).thenReturn(null);

        final boolean authorized = rule.doAuthorize(request, response, tenantAccessor);

        assertFalse(authorized);
    }

    @Test
    public void testWeAreAutoLoggedWhenRequestedAndConfigured() throws Exception {
        when(autoLoginCredentialsFinder.getCredential(any(ProcessIdentifier.class), eq(1L))).thenReturn(new AutoLoginCredentials());
        doReturn(1L).when(tenantAccessor).ensureTenantId();
        // avoid having an exception result into an authorized false
        doReturn(mock(AuthenticationManager.class)).when(rule).getAuthenticationManager(anyLong());
        doReturn(mock(UserLogger.class)).when(rule).createUserLogger();
        doReturn(mock(LoginManager.class)).when(rule).getLoginManager();



        final boolean authorized = rule.doAuthorize(request, response, tenantAccessor);

        assertTrue(authorized);
    }

}
