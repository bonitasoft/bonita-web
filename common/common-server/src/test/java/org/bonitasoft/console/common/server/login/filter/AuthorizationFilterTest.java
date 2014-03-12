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

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;

import org.bonitasoft.console.common.server.login.HttpServletRequestAccessor;
import org.bonitasoft.console.common.server.login.HttpServletResponseAccessor;
import org.bonitasoft.console.common.server.login.TenantIdAccessor;
import org.bonitasoft.console.common.server.login.impl.standard.StandardLoginManagerImpl;
import org.bonitasoft.console.common.server.login.localization.Locator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.Spy;

/**
 * Created by Vincent Elcrin
 * Date: 28/08/13
 * Time: 17:52
 */
public class AuthorizationFilterTest {

    @Mock
    private FilterChain chain;

    @Mock
    private HttpServletRequestAccessor request;

    @Mock
    private HttpServletResponseAccessor response;

    @Mock
    private TenantIdAccessor tenantIdAccessor;

    @Mock
    private HttpSession httpSession;

    @Spy
    AuthorizationFilter authorizationFilter;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        doReturn(httpSession).when(request).getHttpSession();
        doReturn(new StandardLoginManagerImpl()).when(authorizationFilter).getLoginManager(anyLong());
    }

    @Test
    public void testIfWeGoThroughFilterWhenAtLeastOneRulePass() throws Exception {
        authorizationFilter.addRule(createPassingRule());
        authorizationFilter.addRule(createFailingRule());

        authorizationFilter.doAuthorizationFiltering(request, response, tenantIdAccessor, chain);

        verify(chain).doFilter(any(ServletRequest.class), any(ServletResponse.class));
    }

    @Test
    public void testIfWeAreNotRedirectedIfAtLeastOneRulePass() throws Exception {
        authorizationFilter.addRule(createFailingRule());
        authorizationFilter.addRule(createPassingRule());

        authorizationFilter.doAuthorizationFiltering(request, response, tenantIdAccessor, chain);

        verify(response, never()).redirect(any(Locator.class));
    }

    @Test
    public void testIfWeAreRedirectedIfAllRulesFail() throws Exception {
        authorizationFilter.addRule(createFailingRule());
        authorizationFilter.addRule(createFailingRule());

        authorizationFilter.doAuthorizationFiltering(request, response, tenantIdAccessor, chain);

        verify(response).redirect(any(Locator.class));
    }

    @Test
    public void testIfWeDontGoThroughTheChainWhenRulesFails() throws Exception {
        authorizationFilter.addRule(createFailingRule());
        authorizationFilter.addRule(createFailingRule());

        authorizationFilter.doAuthorizationFiltering(request, response, tenantIdAccessor, chain);

        verify(chain, never()).doFilter(any(ServletRequest.class), any(ServletResponse.class));
    }

    @Test
    public void testIfTenantIdIsNotAddedToRedirectUrlIfNotInRequest() throws Exception {
        authorizationFilter.addRule(createFailingRule());
        doReturn(-1L).when(tenantIdAccessor).getRequestedTenantId();

        authorizationFilter.doAuthorizationFiltering(request, response, tenantIdAccessor, chain);

        verify(response).redirect(argThat(new LocatorMatcher("../login.jsp?redirectUrl=")));
    }

    @Test
    public void testIfTenantIdIsAddedToRedirectUrlWhenInRequest() throws Exception {
        authorizationFilter.addRule(createFailingRule());
        doReturn(12L).when(tenantIdAccessor).getRequestedTenantId();

        authorizationFilter.doAuthorizationFiltering(request, response, tenantIdAccessor, chain);

        verify(response).redirect(argThat(new LocatorMatcher("../login.jsp?tenant=12&redirectUrl=")));
    }

    class LocatorMatcher extends ArgumentMatcher<Locator> {

        private String expectedUrl;

        LocatorMatcher(String expectedUrl) {
            this.expectedUrl = expectedUrl;
        }

        @Override
        public boolean matches(Object argument) {
            return expectedUrl.equals(((Locator) argument).getLocation());
        }
    }

    private AuthorizationRule createPassingRule() {
        return new AuthorizationRule() {
            @Override
            public boolean doAuthorize(HttpServletRequestAccessor request, TenantIdAccessor tenantIdAccessor) throws ServletException {
                return true;
            }
        };
    }

    private AuthorizationRule createFailingRule() {
        return new AuthorizationRule() {
            @Override
            public boolean doAuthorize(HttpServletRequestAccessor request, TenantIdAccessor tenantIdAccessor) throws ServletException {
                return false;
            }
        };
    }


}
