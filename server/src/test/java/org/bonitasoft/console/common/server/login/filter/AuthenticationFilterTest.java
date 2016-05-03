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
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.regex.Pattern;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import org.bonitasoft.console.common.server.auth.impl.standard.StandardAuthenticationManagerImpl;
import org.bonitasoft.console.common.server.login.HttpServletRequestAccessor;
import org.bonitasoft.console.common.server.login.TenantIdAccessor;
import org.bonitasoft.console.common.server.login.localization.Locator;
import org.bonitasoft.console.common.server.login.localization.RedirectUrl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
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

    private static final String excludeAuthenticationPattern = "^/(bonita/)?((mobile/)?login.jsp$)|(images/)|(loginservice)|(serverAPI)|(/mobile/js/)|(maintenance.jsp$)|(API/platform/)|(platformloginservice$)|(portal/themeResource$)|(portal/scripts)|(portal/formsService)|(/bonita/?$)|(logoutservice)";

    @Mock
    private FilterChain chain;

    @Mock
    private HttpServletRequestAccessor request;

    @Mock
    private HttpServletRequest httpRequest;

    @Mock
    private HttpServletResponse httpResponse;

    @Mock
    private HttpServletResponse response;

    @Mock
    private TenantIdAccessor tenantIdAccessor;

    @Mock
    private HttpSession httpSession;

    @Spy
    AuthenticationFilter authenticationFilter;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        doReturn(httpSession).when(request).getHttpSession();
        when(request.asHttpServletRequest()).thenReturn(httpRequest);
        doReturn(new FakeAuthenticationManager(1L)).when(authenticationFilter).getAuthenticationManager(any(TenantIdAccessor.class));
        when(httpRequest.getRequestURL()).thenReturn(new StringBuffer());
        when(request.getTenantId()).thenReturn("1");
    }

    @Test
    public void testIfWeGoThroughFilterWhenAtLeastOneRulePass() throws Exception {
        authenticationFilter.addRule(createPassingRule());
        authenticationFilter.addRule(createFailingRule());

        authenticationFilter.doAuthenticationFiltering(request, response, tenantIdAccessor, chain);

        verify(chain).doFilter(any(ServletRequest.class), any(ServletResponse.class));
    }

    @Test
    public void testIfWeAreNotRedirectedIfAtLeastOneRulePass() throws Exception {
        authenticationFilter.addRule(createFailingRule());
        authenticationFilter.addRule(createPassingRule());

        authenticationFilter.doAuthenticationFiltering(request, response, tenantIdAccessor, chain);

        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    public void testIfWeAreRedirectedIfAllRulesFail() throws Exception {
        authenticationFilter.addRule(createFailingRule());
        authenticationFilter.addRule(createFailingRule());
        when(httpRequest.getContextPath()).thenReturn("/bonita");
        when(httpRequest.getPathInfo()).thenReturn("/portal");
        authenticationFilter.doAuthenticationFiltering(request, response, tenantIdAccessor, chain);

        verify(response).sendRedirect(anyString());
    }

    @Test
    public void testIfWeDontGoThroughTheChainWhenRulesFails() throws Exception {
        authenticationFilter.addRule(createFailingRule());
        authenticationFilter.addRule(createFailingRule());
        when(httpRequest.getContextPath()).thenReturn("/bonita");
        when(httpRequest.getPathInfo()).thenReturn("/portal");
        authenticationFilter.doAuthenticationFiltering(request, response, tenantIdAccessor, chain);

        verify(chain, never()).doFilter(any(ServletRequest.class), any(ServletResponse.class));
    }

    @Test
    public void testIfTenantIdIsAddedToRedirectUrlWhenInRequest() throws Exception {
        authenticationFilter.addRule(createFailingRule());
        doReturn("12").when(request).getTenantId();

        when(httpRequest.getContextPath()).thenReturn("/bonita");
        when(httpRequest.getPathInfo()).thenReturn("/portal");
        authenticationFilter.doAuthenticationFiltering(request, response, tenantIdAccessor, chain);

        verify(response).sendRedirect("/bonita/login.jsp?tenant=12&redirectUrl=");
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
    public void testFilterWithExcludedURL() throws Exception {
        final String url = "test";
        when(httpRequest.getRequestURL()).thenReturn(new StringBuffer(url));
        doReturn(true).when(authenticationFilter).matchExcludePatterns(url);
        authenticationFilter.doFilter(httpRequest, httpResponse, chain);
        verify(authenticationFilter, times(0)).doAuthenticationFiltering(request, response, tenantIdAccessor, chain);
        verify(chain, times(1)).doFilter(httpRequest, httpResponse);
    }

    @Test
    public void testMatchExcludePatterns() throws Exception {

        matchExcludePattern("/login.jsp", true);
        matchExcludePattern("http://localhost:8080/login.jsp", true);
        matchExcludePattern("http://localhost:8080/bonita/mobile/login.jsp", true);
        matchExcludePattern("http://localhost:8080/portal/themeResource", true);
        matchExcludePattern("http://localhost:8080/portal/themeResource/poutpout", false);
        matchExcludePattern("http://localhost:8080/loginservice", true);
        matchExcludePattern("http://localhost:8080/portal/formsService", true);
    }

    @Test
    public void testMakeRedirectUrl() throws Exception {
        when(request.getRequestedUri()).thenReturn("/portal/homepage");
        final RedirectUrl redirectUrl = authenticationFilter.makeRedirectUrl(request);
        verify(request, times(1)).getRequestedUri();
        assertThat(redirectUrl.getUrl()).isEqualToIgnoringCase("/portal/homepage");
    }

    @Test
    public void testMakeRedirectUrlFromRequestUrl() throws Exception {
        when(request.getRequestedUri()).thenReturn("portal/homepage");
        when(httpRequest.getRequestURL()).thenReturn(new StringBuffer("http://127.0.1.1:8888/portal/homepage"));
        final RedirectUrl redirectUrl = authenticationFilter.makeRedirectUrl(request);
        verify(request, times(1)).getRequestedUri();
        verify(httpRequest, never()).getRequestURI();
        assertThat(redirectUrl.getUrl()).isEqualToIgnoringCase("portal/homepage");
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
        final String patternToCompile = "^/(bonita/)?((mobile/)?login.jsp$)|(images/)|(redirectCasToCatchHash.jsp)|(loginservice)|(serverAPI)|(/mobile/js/)|(maintenance.jsp$)|(API/platform/)|(platformloginservice$)|(portal/themeResource$)|(portal/scripts)|(/bonita/?$)|(logoutservice)";
        assertThat(authenticationFilter.compilePattern(patternToCompile)).isNotNull().has(new Condition<Pattern>() {

            @Override
            public boolean matches(final Pattern pattern) {
                return pattern.pattern().equalsIgnoreCase(patternToCompile);
            }
        });
    }

    private void matchExcludePattern(final String urlToMatch, final Boolean mustMatch) {
        authenticationFilter.excludePattern = Pattern.compile(excludeAuthenticationPattern);
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

}
