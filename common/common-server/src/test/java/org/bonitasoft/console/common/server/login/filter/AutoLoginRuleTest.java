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
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.bonitasoft.console.common.server.login.HttpServletRequestAccessor;
import org.bonitasoft.console.common.server.login.LoginManager;
import org.bonitasoft.console.common.server.login.TenantIdAccessor;
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
    private TenantIdAccessor tenantAccessor;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void testWeAreNotAutoLoggedWhenNotRequested() throws Exception {
        doReturn(false).when(request).isAutoLoginRequested();

        boolean authorized = rule.doAuthorize(request, tenantAccessor);

        assertFalse(authorized);
    }

    @Test
    public void testWeAreNotAutoLoggedWhenRequestedButNotConfigured() throws Exception {
        doReturn(false).when(request).isAutoLoginRequested();
        doReturn("process3--2.9").when(request).getAutoLoginScope();
        doReturn(1L).when(tenantAccessor).getRequestedTenantId();

        boolean authorized = rule.doAuthorize(request, tenantAccessor);

        assertFalse(authorized);
    }

    @Test
    public void testWeAreAutoLoggedWhenRequestedAndConfigured() throws Exception {
        doReturn(true).when(request).isAutoLoginRequested();
        doReturn("process1--1.0").when(request).getAutoLoginScope();
        doReturn(1L).when(tenantAccessor).ensureTenantId();
        // avoid having an exception result into an authorized false
        doReturn(mock(LoginManager.class)).when(rule).getLoginManager(anyLong());
        SecurityProperties secu = autoLoginAllowedSecurityConfig();
        doReturn(secu).when(rule).getSecurityProperties(any(HttpServletRequestAccessor.class), anyLong());
        
        boolean authorized = rule.doAuthorize(request, tenantAccessor);

        assertTrue(authorized);
    }

    private SecurityProperties autoLoginAllowedSecurityConfig() {
        SecurityProperties secu = mock(SecurityProperties.class);
        when(secu.allowAutoLogin()).thenReturn(true);
        when(secu.getAutoLoginPassword()).thenReturn("aPasswordForTenant");
        when(secu.getAutoLoginUserName()).thenReturn("aUserNameForTenant");
        return secu;
    }
}
