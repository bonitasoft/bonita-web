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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.bonitasoft.console.common.server.login.HttpServletRequestAccessor;
import org.bonitasoft.console.common.server.login.LoginManager;
import org.bonitasoft.console.common.server.login.TenantIdAccessor;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.model.user.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.Spy;

/**
 * Created by Vincent Elcrin
 * Date: 30/08/13
 * Time: 15:00
 */
public class AlreadyLoggedInRuleTest {

    @Mock
    private HttpServletRequestAccessor request;

    @Mock
    private TenantIdAccessor tenantAccessor;

    @Mock
    private APISession apiSession;

    @Mock
    private HttpSession httpSession;

    @Mock
    HttpServletRequest httpServletRequest;
    
    @Spy
    AlreadyLoggedInRule rule;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        doReturn(true).when(rule).useCredentialsTransmission(any(APISession.class));
        doReturn(httpSession).when(request).getHttpSession();
        doReturn(httpServletRequest).when(request).asHttpServletRequest();
    }

    @Test
    public void testIfRuleAuthorizeAlreadyLoggedUser() throws Exception {
        doReturn(apiSession).when(request).getApiSession();
        // ensure we won't recreate user session
        doReturn("").when(httpSession).getAttribute(LoginManager.USER_SESSION_PARAM_KEY);

        boolean authorization = rule.doAuthorize(request, tenantAccessor);

        assertThat(authorization, is(true));
    }

    @Test
    public void testIfRuleDoesntAuthorizeNullSession() throws Exception {
        doReturn(null).when(request).getApiSession();

        boolean authorization = rule.doAuthorize(request, tenantAccessor);

        assertFalse(authorization);
    }

    @Test
    public void testIfUserSessionIsRecreatedWhenMissing() throws Exception {
        doReturn(apiSession).when(request).getApiSession();
        doReturn(null).when(httpSession).getAttribute(LoginManager.USER_SESSION_PARAM_KEY);
        // configure user that will be created
        doReturn(new Locale("en")).when(httpServletRequest).getLocale();
        doReturn("myUser").when(apiSession).getUserName();

        rule.doAuthorize(request, tenantAccessor);

        verify(httpSession).setAttribute(
                eq(LoginManager.USER_SESSION_PARAM_KEY),
                argThat(new UserMatcher("myUser", "en")));
    }

    class UserMatcher extends  ArgumentMatcher<User> {

        private String username;
        private String local;

        UserMatcher(String username, String local) {
            this.username = username;
            this.local = local;
        }

        @Override
        public boolean matches(Object arg) {
            User user = (User) arg;
            return username.equals(user.getUsername())
                    && local.equals(user.getLocale().toString());
        }
    }
}
