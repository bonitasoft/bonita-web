/*
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
package org.bonitasoft.console.common.server.login.filter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import org.bonitasoft.console.common.server.auth.AuthenticationManager;
import org.bonitasoft.console.common.server.login.HttpServletRequestAccessor;
import org.bonitasoft.console.common.server.login.TenantIdAccessor;
import org.bonitasoft.console.common.server.login.utils.RedirectUrl;
import org.bonitasoft.console.common.server.utils.SessionUtil;
import org.bonitasoft.engine.session.APISession;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Created by Vincent Elcrin
 * Date: 28/08/13
 * Time: 17:52
 */
public class AuthenticationFilterTest {
    
    @Mock
    private FilterChain chain;

    @Mock
    private HttpServletRequest httpRequest;

    @Mock
    private HttpServletResponse httpResponse;

    @Mock
    private HttpSession httpSession;
    
    @Mock
    private APISession apiSession;

    @Mock
    private FilterConfig filterConfig;
    
    @Mock
    private ServletContext servletContext;
    
    @Spy
    AuthenticationFilter authenticationFilter;
    
    @Spy
    AuthenticationManager authenticationManager = new FakeAuthenticationManager(1L);

    HttpServletRequestAccessor request;
    
    TenantIdAccessor tenantIdAccessor;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        doReturn(httpSession).when(httpRequest).getSession();
        doReturn(authenticationManager).when(authenticationFilter).getAuthenticationManager(any(TenantIdAccessor.class));
        when(httpRequest.getRequestURL()).thenReturn(new StringBuffer());
        when(httpRequest.getMethod()).thenReturn("GET");
        when(httpRequest.getParameter(AuthenticationManager.TENANT)).thenReturn("1");
        when(servletContext.getContextPath()).thenReturn("");
        when(filterConfig.getServletContext()).thenReturn(servletContext);
        when(filterConfig.getInitParameterNames()).thenReturn(Collections.emptyEnumeration());
        
        request = spy(new HttpServletRequestAccessor(httpRequest));
        tenantIdAccessor = spy(new TenantIdAccessor(request));
        
        //remove default rules (already logged in) as they have their own tests
        authenticationFilter.getRules().clear();
        authenticationFilter.init(filterConfig);
        when(httpRequest.getParameter("tenant")).thenReturn("1");
        when(tenantIdAccessor.ensureTenantId()).thenReturn(1L);
        when(httpRequest.getMethod()).thenReturn("GET");
    }

    @Test
    public void testIfWeGoThroughFilterWhenAtLeastOneRulePass() throws Exception {
        when(httpRequest.getServletPath()).thenReturn("/apps");
        when(httpRequest.getPathInfo()).thenReturn("/app/home");
        AuthenticationRule passingRule = spy(createPassingRule());
        authenticationFilter.addRule(passingRule);
        authenticationFilter.addRule(createFailingRule());

        authenticationFilter.doAuthenticationFiltering(request, httpResponse, tenantIdAccessor, chain);

        verify(passingRule).proceedWithRequest(chain, httpRequest, httpResponse, 1L);
        verify(chain).doFilter(any(ServletRequest.class), any(ServletResponse.class));
    }
    
    @Test
    public void should_use_the_tenant_id_from_APISession_when_rule_passes() throws Exception {
        when(httpRequest.getServletPath()).thenReturn("/apps");
        when(httpRequest.getPathInfo()).thenReturn("/app/home");
        when(apiSession.getTenantId()).thenReturn(3L);
        when(httpSession.getAttribute(SessionUtil.API_SESSION_PARAM_KEY)).thenReturn(apiSession);
        AuthenticationRule passingRule = spy(createPassingRule());
        authenticationFilter.addRule(passingRule);

        authenticationFilter.doAuthenticationFiltering(request, httpResponse, tenantIdAccessor, chain);

        verify(passingRule).proceedWithRequest(chain, httpRequest, httpResponse, 3L);
    }

    @Test
    public void testIfWeAreNotRedirectedIfAtLeastOneRulePass() throws Exception {
        when(httpRequest.getServletPath()).thenReturn("/apps");
        when(httpRequest.getPathInfo()).thenReturn("/app/home");
        authenticationFilter.addRule(createFailingRule());
        authenticationFilter.addRule(createPassingRule());

        authenticationFilter.doAuthenticationFiltering(request, httpResponse, tenantIdAccessor, chain);

        verify(httpResponse, never()).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(httpResponse, never()).sendRedirect(anyString());
    }

    @Test
    public void testIfWeAreRedirectedIfAllRulesFail() throws Exception {
        AuthenticationRule firstFailingRule = spy(createFailingRule());
        authenticationFilter.addRule(firstFailingRule);
        AuthenticationRule secondFailingRule = spy(createFailingRule());
        authenticationFilter.addRule(secondFailingRule);
        when(httpRequest.getServletPath()).thenReturn("/apps");
        when(httpRequest.getPathInfo()).thenReturn("/app/home");
        when(httpRequest.getContextPath()).thenReturn("/bonita");
        authenticationFilter.doAuthenticationFiltering(request, httpResponse, tenantIdAccessor, chain);

        verify(firstFailingRule, never()).proceedWithRequest(chain, httpRequest, httpResponse, 1L);
        verify(secondFailingRule, never()).proceedWithRequest(chain, httpRequest, httpResponse, 1L);
        verify(httpResponse).sendRedirect(anyString());
    }
    
    @Test
    public void testIfWeGet401ErrorIfAllRulesFailAndRedirectParamIsFalse() throws Exception {
    	AuthenticationFilter authenticationFilterWithConfig = spy(new AuthenticationFilter());
        doReturn(authenticationManager).when(authenticationFilterWithConfig).getAuthenticationManager(any(TenantIdAccessor.class));
        FilterConfig filterConfig = mock(FilterConfig.class);
        when(filterConfig.getServletContext()).thenReturn(servletContext);
        List<String> initParams = new ArrayList<String>();
        initParams.add(AuthenticationFilter.REDIRECT_PARAM);
        when(filterConfig.getInitParameterNames()).thenReturn(Collections.enumeration(initParams));
        when(filterConfig.getInitParameter(AuthenticationFilter.REDIRECT_PARAM)).thenReturn(Boolean.FALSE.toString());
        authenticationFilterWithConfig.init(filterConfig);
        AuthenticationRule firstFailingRule = spy(createFailingRule());
        authenticationFilterWithConfig.addRule(firstFailingRule);
        AuthenticationRule secondFailingRule = spy(createFailingRule());
        authenticationFilterWithConfig.addRule(secondFailingRule);
        when(httpRequest.getServletPath()).thenReturn("/apps");
        when(httpRequest.getPathInfo()).thenReturn("/app/home");
        when(httpRequest.getContextPath()).thenReturn("/bonita");
        authenticationFilterWithConfig.doAuthenticationFiltering(request, httpResponse, tenantIdAccessor, chain);

        verify(firstFailingRule, never()).proceedWithRequest(chain, httpRequest, httpResponse, 1L);
        verify(secondFailingRule, never()).proceedWithRequest(chain, httpRequest, httpResponse, 1L);
        verify(httpResponse, never()).sendRedirect(anyString());
        verify(httpResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
    
    @Test
    public void testIfWeAreNotRedirectedIfRequestMethodIsNotGet() throws Exception {
        when(httpRequest.getMethod()).thenReturn("POST");
        authenticationFilter.addRule(createFailingRule());

        when(httpRequest.getServletPath()).thenReturn("/apps");
        when(httpRequest.getPathInfo()).thenReturn("/app/home");
        when(httpRequest.getContextPath()).thenReturn("/bonita");
        authenticationFilter.doAuthenticationFiltering(request, httpResponse, tenantIdAccessor, chain);

        verify(httpResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(httpResponse, never()).sendRedirect(anyString());
    }

    @Test
    public void testIfWeDontGoThroughTheChainWhenRulesFails() throws Exception {
        authenticationFilter.addRule(createFailingRule());
        authenticationFilter.addRule(createFailingRule());
        when(httpRequest.getServletPath()).thenReturn("/apps");
        when(httpRequest.getPathInfo()).thenReturn("/app/home");
        when(httpRequest.getContextPath()).thenReturn("/bonita");
        authenticationFilter.doAuthenticationFiltering(request, httpResponse, tenantIdAccessor, chain);

        verify(chain, never()).doFilter(any(ServletRequest.class), any(ServletResponse.class));
    }

    @Test
    public void testIfTenantIdIsAddedToRedirectUrlWhenInRequest() throws Exception {
        authenticationFilter.addRule(createFailingRule());
        when(httpRequest.getParameter("tenant")).thenReturn("12");

        when(httpRequest.getRequestURI()).thenReturn("/bonita/apps/app/home");
        when(httpRequest.getServletPath()).thenReturn("/apps");
        when(httpRequest.getPathInfo()).thenReturn("/app/home");
        when(httpRequest.getContextPath()).thenReturn("/bonita");
        
        authenticationFilter.doAuthenticationFiltering(request, httpResponse, tenantIdAccessor, chain);

        verify(httpResponse).sendRedirect("/bonita/login.jsp?tenant=12&redirectUrl=%2Fbonita%2Fapps%2Fapp%2Fhome");
    }

    @Test
    public void testFilter() throws Exception {
        when(httpRequest.getSession()).thenReturn(httpSession);
        doAnswer(new Answer<Object>() {

            @Override
            public Object answer(final InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(authenticationFilter).doAuthenticationFiltering(any(HttpServletRequestAccessor.class), any(HttpServletResponse.class),
                any(TenantIdAccessor.class), any(FilterChain.class));
        authenticationFilter.doFilter(httpRequest, httpResponse, chain);
        verify(authenticationFilter, times(1)).doAuthenticationFiltering(any(HttpServletRequestAccessor.class), any(HttpServletResponse.class),
                any(TenantIdAccessor.class), any(FilterChain.class));
    }
    
    @Test
    public void testFailedLoginOnDoFiltering() throws Exception {
        when(httpRequest.getServletPath()).thenReturn("/apps");
        when(httpRequest.getPathInfo()).thenReturn("/app/home");
        EngineUserNotFoundOrInactive engineUserNotFoundOrInactive = new EngineUserNotFoundOrInactive("login failed", 1L);
        authenticationFilter.addRule(createThrowingExceptionRule(engineUserNotFoundOrInactive));
        doReturn(tenantIdAccessor).when(authenticationFilter).getTenantAccessor(request);
        
        authenticationFilter.doAuthenticationFiltering(request, httpResponse, tenantIdAccessor, chain);

        verify(authenticationManager, never()).getLoginPageURL(eq(request), anyString());
        verify(chain, never()).doFilter(httpRequest, httpResponse);
        verify(authenticationFilter).handleUserNotFoundOrInactiveException(request, httpResponse, engineUserNotFoundOrInactive);
        verify(authenticationFilter).redirectTo(request, httpResponse, 1L, AuthenticationFilter.USER_NOT_FOUND_JSP);
    }
    
    @Test
    public void testTenantIsPausedOnDoFiltering() throws Exception {
        when(httpRequest.getServletPath()).thenReturn("/apps");
        when(httpRequest.getPathInfo()).thenReturn("/app/home");
        TenantIsPausedException tenantIsPausedException = new TenantIsPausedException("login failed", 1L);
        authenticationFilter.addRule(createThrowingExceptionRule(tenantIsPausedException));
        doReturn(tenantIdAccessor).when(authenticationFilter).getTenantAccessor(request);
        
        authenticationFilter.doAuthenticationFiltering(request, httpResponse, tenantIdAccessor, chain);

        verify(authenticationManager, never()).getLoginPageURL(eq(request), anyString());
        verify(chain, never()).doFilter(httpRequest, httpResponse);
        verify(authenticationFilter).handleTenantPausedException(request, httpResponse, tenantIsPausedException);
        verify(authenticationFilter).redirectTo(request, httpResponse, 1L, AuthenticationFilter.MAINTENANCE_JSP);
    }

    @Test
    public void testRedirectTo() throws Exception {
        final String context = "/bonita";
        when(httpRequest.getContextPath()).thenReturn(context);
        final long tenantId = 0L;
        authenticationFilter.redirectTo(request, httpResponse, tenantId, AuthenticationFilter.MAINTENANCE_JSP);
        verify(httpResponse, times(1)).sendRedirect(context + AuthenticationFilter.MAINTENANCE_JSP);
        verify(httpRequest, times(1)).getContextPath();
    }

    @Test
    public void testFilterWithExcludedURL() throws Exception {
        final String url = "test";
        when(httpRequest.getRequestURL()).thenReturn(new StringBuffer(url));
        doReturn(true).when(authenticationFilter).matchExcludePatterns(url);
        authenticationFilter.doFilter(httpRequest, httpResponse, chain);
        verify(authenticationFilter, times(0)).doAuthenticationFiltering(request, httpResponse, tenantIdAccessor, chain);
        verify(chain, times(1)).doFilter(httpRequest, httpResponse);
    }

    @Test
    public void testMatchExcludePatterns() throws Exception {
        matchExcludePattern("http://host/bonita/portal/resource/page/API/system/session/unusedId", true);
        matchExcludePattern("http://host/bonita/apps/app/API/system/session/unusedId", true);
        matchExcludePattern("http://host/bonita/portal/resource/page/content/", false);
        matchExcludePattern("http://host/bonita/apps/app/page/", false);
    }

    @Test
    public void testMakeRedirectUrl() throws Exception {
        when(httpRequest.getRequestURI()).thenReturn("/apps/appDirectoryBonita");
        final RedirectUrl redirectUrl = authenticationFilter.makeRedirectUrl(request);
        verify(httpRequest, times(1)).getRequestURI();
        assertThat(redirectUrl.getUrl()).isEqualToIgnoringCase("/apps/appDirectoryBonita");
    }

    @Test
    public void testCompileNullPattern() throws Exception {
        assertThat(authenticationFilter.compilePattern(null)).isNull();
    }

    @Test
    public void testCompileWrongPattern() throws Exception {
        assertThat(authenticationFilter.compilePattern("((((")).isNull();
    }

    @Test
    public void testCompileSimplePattern() throws Exception {
        final String patternToCompile = "test";
        assertThat(authenticationFilter.compilePattern(patternToCompile)).isNotNull().has(new Condition<Pattern>() {

            @Override
            public boolean matches(final Pattern pattern) {
                return pattern.pattern().equalsIgnoreCase(patternToCompile);
            }
        });
    }

    @Test
    public void testCompileExcludePattern() throws Exception {
        final String patternToCompile = "^/(bonita/)?(login.jsp$)|(images/)|(redirectCasToCatchHash.jsp)|(loginservice)|(serverAPI)|(maintenance.jsp$)|(API/platform/)|(platformloginservice$)|(/bonita/?$)|(logoutservice)";
        assertThat(authenticationFilter.compilePattern(patternToCompile)).isNotNull().has(new Condition<Pattern>() {

            @Override
            public boolean matches(final Pattern pattern) {
                return pattern.pattern().equalsIgnoreCase(patternToCompile);
            }
        });
    }

    private void matchExcludePattern(final String urlToMatch, final Boolean mustMatch) {
        if (authenticationFilter.matchExcludePatterns(urlToMatch) != mustMatch) {
            Assertions.fail("Matching excludePattern and the Url " + urlToMatch + " must return " + mustMatch);
        }
    }

    private AuthenticationRule createPassingRule() {
        return new AuthenticationRule() {

            @Override
            public boolean doAuthorize(final HttpServletRequestAccessor request, HttpServletResponse response, final TenantIdAccessor tenantIdAccessor) throws ServletException {
                return true;
            }
        };
    }

    private AuthenticationRule createFailingRule() {
        return new AuthenticationRule() {

            @Override
            public boolean doAuthorize(final HttpServletRequestAccessor request, HttpServletResponse response, final TenantIdAccessor tenantIdAccessor) throws ServletException {
                return false;
            }
        };
    }

    private AuthenticationRule createThrowingExceptionRule(RuntimeException e) {
        return new AuthenticationRule() {

            @Override
            public boolean doAuthorize(final HttpServletRequestAccessor request, HttpServletResponse response, final TenantIdAccessor tenantIdAccessor) throws ServletException {
                throw e;
            }
        };
    }

}
