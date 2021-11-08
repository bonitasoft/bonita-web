/*
 * Copyright (C) 2014 BonitaSoft S.A.
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
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bonitasoft.console.common.server.preferences.properties.DynamicPermissionsChecks;
import org.bonitasoft.console.common.server.preferences.properties.ResourcesPermissionsMapping;
import org.bonitasoft.console.common.server.utils.SessionUtil;
import org.bonitasoft.engine.api.permission.APICallContext;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.session.InvalidSessionException;
import org.bonitasoft.engine.session.PlatformSession;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RestAPIAuthorizationFilterTest {

    @Mock
    private ResourcesPermissionsMapping resourcesPermissionsMapping;
    @Mock
    private DynamicPermissionsChecks dynamicPermissionsChecks;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private APISession apiSession;
    @Mock
    private HttpSession httpSession;

    private final RestAPIAuthorizationFilter restAPIAuthorizationFilter = new RestAPIAuthorizationFilter(false);

    @Before
    public void before() {
        doReturn(httpSession).when(request).getSession();
        doReturn("").when(request).getQueryString();
        doReturn(apiSession).when(httpSession).getAttribute(SessionUtil.API_SESSION_PARAM_KEY);
        doReturn(1L).when(apiSession).getTenantId();
        doReturn(9L).when(apiSession).getUserId();
        doReturn(false).when(apiSession).isTechnicalUser();
        doReturn("john").when(apiSession).getUserName();
    }

    private Set<String> initSpy(final RestAPIAuthorizationFilter restAPIAuthorizationFilterSpy) throws ServletException {
        final Set<String> permissions = new HashSet<>(List.of("plop"));
        return initSpy(restAPIAuthorizationFilterSpy, permissions);
    }

    private Set<String> initSpy(final RestAPIAuthorizationFilter restAPIAuthorizationFilterSpy, final Set<String> permissions) throws ServletException {
        doReturn("GET").when(request).getMethod();
        doReturn(permissions).when(httpSession).getAttribute(SessionUtil.PERMISSIONS_SESSION_PARAM_KEY);
        doReturn("").when(restAPIAuthorizationFilterSpy).getRequestBody(request);
        return permissions;
    }

    @Test
    public void should_checkPermissions_call_engine_check_if_secu_is_enabled() throws Exception {
        final RestAPIAuthorizationFilter restAPIAuthorizationFilterSpy = spy(restAPIAuthorizationFilter);
        final Set<String> permissions = initSpy(restAPIAuthorizationFilterSpy);
        doReturn(true).when(restAPIAuthorizationFilterSpy).isApiAuthorizationsCheckEnabled(1L);
        doReturn(true).when(restAPIAuthorizationFilterSpy).enginePermissionsCheck(new APICallContext("GET", "bpm", "case", null, "", ""), permissions, apiSession);

        //when
        final boolean isAuthorized = restAPIAuthorizationFilterSpy.checkPermissions(request, "bpm", "case", null);

        //then
        assertThat(isAuthorized).isTrue();
        verify(restAPIAuthorizationFilterSpy).enginePermissionsCheck(new APICallContext("GET", "bpm", "case", null, "", ""), permissions, apiSession);
    }

    @Test
    public void should_not_call_engine_check_if_secu_is_enabled_but_session_call_is_always_authorized() throws Exception {
        final RestAPIAuthorizationFilter restAPIAuthorizationFilterSpy = spy(restAPIAuthorizationFilter);
        doReturn("GET").when(request).getMethod();
        doReturn(true).when(restAPIAuthorizationFilterSpy).isApiAuthorizationsCheckEnabled(1L);
        doReturn("").when(restAPIAuthorizationFilterSpy).getRequestBody(any());
        //when
        final boolean isAuthorized = restAPIAuthorizationFilterSpy.checkPermissions(request, "system", "session", null);

        //then
        assertThat(isAuthorized).isTrue();
        verify(restAPIAuthorizationFilterSpy).isAlwaysAuthorizedResource(any(APICallContext.class));
        verify(restAPIAuthorizationFilterSpy, never()).enginePermissionsCheck(any(APICallContext.class),
                anySetOf(String.class), any(APISession.class));
    }

    @Test
    public void should_checkPermissions_do_not_call_check_if_technical() throws Exception {
        doReturn(true).when(apiSession).isTechnicalUser();
        final RestAPIAuthorizationFilter restAPIAuthorizationFilterSpy = spy(restAPIAuthorizationFilter);
        initSpy(restAPIAuthorizationFilterSpy);
        doReturn(true).when(restAPIAuthorizationFilterSpy).isApiAuthorizationsCheckEnabled(1L);

        //when
        final boolean isAuthorized = restAPIAuthorizationFilterSpy.checkPermissions(request, "bpm", "case", null);

        //then
        assertThat(isAuthorized).isTrue();
        verify(restAPIAuthorizationFilterSpy, never()).enginePermissionsCheck(any(APICallContext.class),
                anySetOf(String.class), any(APISession.class));
    }

    @Test
    public void should_checkPermissions_do_nothing_if_secu_is_disabled() throws Exception {
        final RestAPIAuthorizationFilter restAPIAuthorizationFilterSpy = spy(restAPIAuthorizationFilter);
        initSpy(restAPIAuthorizationFilterSpy);
        doReturn(false).when(restAPIAuthorizationFilterSpy).isApiAuthorizationsCheckEnabled(1L);

        //when
        final boolean isAuthorized = restAPIAuthorizationFilterSpy.checkPermissions(request, "bpm", "case", null);

        //then
        assertThat(isAuthorized).isTrue();
        verify(restAPIAuthorizationFilterSpy, never()).enginePermissionsCheck(any(APICallContext.class),
                anySetOf(String.class), any(APISession.class));
    }

    @Test
    public void should_checkPermissions_parse_the_request() throws Exception {
        doReturn("API/bpm/case/15").when(request).getPathInfo();
        final RestAPIAuthorizationFilter restAPIAuthorizationFilterSpy = spy(restAPIAuthorizationFilter);
        doReturn(true).when(restAPIAuthorizationFilterSpy).checkPermissions(eq(request), eq("bpm"), eq("case"), eq(APIID.makeAPIID(15L)));

        //when
        restAPIAuthorizationFilterSpy.checkPermissions(request);

        //then
        verify(restAPIAuthorizationFilterSpy).checkPermissions(eq(request), eq("bpm"), eq("case"), eq(APIID.makeAPIID(15L)));
    }

    @Test
    public void should_checkValidCondition_check_session_is_platform() throws ServletException {
        doReturn("API/platform/plop").when(request).getRequestURI();
        doReturn(mock(PlatformSession.class)).when(httpSession).getAttribute(RestAPIAuthorizationFilter.PLATFORM_SESSION_PARAM_KEY);
        //when
        final boolean isValid = restAPIAuthorizationFilter.checkValidCondition(request, response);

        assertThat(isValid).isTrue();
    }

    @Test
    public void should_checkValidCondition_check_session_is_platform_with_API_toolkit() throws ServletException {
        doReturn("APIToolkit/platform/plop").when(request).getRequestURI();
        doReturn(mock(PlatformSession.class)).when(httpSession).getAttribute(RestAPIAuthorizationFilter.PLATFORM_SESSION_PARAM_KEY);
        //when
        final boolean isValid = restAPIAuthorizationFilter.checkValidCondition(request, response);

        assertThat(isValid).isTrue();
    }

    @Test
    public void should_checkValidCondition_check_unauthorized_if_no_platform_session() throws ServletException {
        doReturn("API/platform/plop").when(request).getRequestURI();
        doReturn(null).when(httpSession).getAttribute(RestAPIAuthorizationFilter.PLATFORM_SESSION_PARAM_KEY);
        //when
        final boolean isValid = restAPIAuthorizationFilter.checkValidCondition(request, response);

        assertThat(isValid).isFalse();
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    public void should_checkValidCondition_check_unauthorized_if_no_tenant_session() throws ServletException {
        doReturn(null).when(httpSession).getAttribute(SessionUtil.API_SESSION_PARAM_KEY);
        doReturn("API/bpm/case/15").when(request).getRequestURI();
        //when
        final boolean isValid = restAPIAuthorizationFilter.checkValidCondition(request, response);

        assertThat(isValid).isFalse();
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    public void should_checkValidCondition_check_unauthorized_if_session_is_invalid() throws ServletException {
        final RestAPIAuthorizationFilter restAPIAuthorizationFilterSpy = spy(restAPIAuthorizationFilter);
        doReturn("API/bpm/case/15").when(request).getRequestURI();
        doThrow(InvalidSessionException.class).when(restAPIAuthorizationFilterSpy).checkPermissions(request);
        //when
        final boolean isValid = restAPIAuthorizationFilterSpy.checkValidCondition(request, response);

        assertThat(isValid).isFalse();
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    public void should_checkValidCondition_check_permission_if_is_tenant_is_forbidden() throws ServletException {
        final RestAPIAuthorizationFilter restAPIAuthorizationFilterSpy = spy(restAPIAuthorizationFilter);
        doReturn("API/bpm/case/15").when(request).getRequestURI();
        doReturn(false).when(restAPIAuthorizationFilterSpy).checkPermissions(request);

        //when
        final boolean isValid = restAPIAuthorizationFilterSpy.checkValidCondition(request, response);

        assertThat(isValid).isFalse();
        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }


    @Test
    public void should_checkValidCondition_check_permission_if_is_tenant_is_ok() throws ServletException {
        final RestAPIAuthorizationFilter restAPIAuthorizationFilterSpy = spy(restAPIAuthorizationFilter);
        doReturn("API/bpm/case/15").when(request).getRequestURI();
        doReturn(true).when(restAPIAuthorizationFilterSpy).checkPermissions(request);

        //when
        final boolean isValid = restAPIAuthorizationFilterSpy.checkValidCondition(request, response);

        assertThat(isValid).isTrue();
    }

    @Test(expected = ServletException.class)
    public void should_checkValidCondition_catch_runtime() throws ServletException {
        doThrow(new RuntimeException()).when(request).getRequestURI();

        //when
        restAPIAuthorizationFilter.checkValidCondition(request, response);
    }

}
