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
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anySetOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bonitasoft.console.common.server.login.LoginManager;
import org.bonitasoft.console.common.server.preferences.properties.ResourcesPermissionsMapping;
import org.bonitasoft.engine.session.APISession;
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
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private APISession apiSession;
    @Mock
    private HttpSession httpSession;

    private RestAPIAuthorizationFilter restAPIAuthorizationFilter = new RestAPIAuthorizationFilter();

    private String username = "john";

    @Before
    public void before() {
        doReturn(httpSession).when(request).getSession();
        doReturn(apiSession).when(httpSession).getAttribute(LoginManager.API_SESSION_PARAM_KEY);
        doReturn(1l).when(apiSession).getTenantId();
        doReturn(false).when(apiSession).isTechnicalUser();
        doReturn("john").when(apiSession).getUserName();
    }

    @Test
    public void should_checkPermission_call_static_check_if_secu_is_enabled() throws IOException {
        RestAPIAuthorizationFilter restAPIAuthorizationFilterSpy = spy(restAPIAuthorizationFilter);
        Set<String> permissions = initSpy(restAPIAuthorizationFilterSpy);
        doReturn(true).when(restAPIAuthorizationFilterSpy).isApiAuthorizationsCheckEnabled(1l);
        doReturn(true).when(restAPIAuthorizationFilterSpy).staticCheck(anyString(), anyString(), anyString(), anyString(), anySetOf(String.class),
                any(ResourcesPermissionsMapping.class), eq(username));
        //when
        boolean isAuthorized = restAPIAuthorizationFilterSpy.checkPermissions(request, "bpm", "case", null);

        //then
        assertThat(isAuthorized).isTrue();
        verify(restAPIAuthorizationFilterSpy).staticCheck("GET", "bpm", "case", null, permissions, resourcesPermissionsMapping, username);
    }

    @Test
    public void should_checkPermission_do_not_call_check_if_technical() throws IOException {
        doReturn(true).when(apiSession).isTechnicalUser();
        RestAPIAuthorizationFilter restAPIAuthorizationFilterSpy = spy(restAPIAuthorizationFilter);
        Set<String> permissions = initSpy(restAPIAuthorizationFilterSpy);
        doReturn(true).when(restAPIAuthorizationFilterSpy).isApiAuthorizationsCheckEnabled(1l);
        doReturn(true).when(restAPIAuthorizationFilterSpy).staticCheck(anyString(), anyString(), anyString(), anyString(), anySetOf(String.class),
                any(ResourcesPermissionsMapping.class), eq(username));
        //when
        boolean isAuthorized = restAPIAuthorizationFilterSpy.checkPermissions(request, "bpm", "case", null);

        //then
        assertThat(isAuthorized).isTrue();
        verify(restAPIAuthorizationFilterSpy, times(0)).staticCheck("GET", "bpm", "case", null, permissions, resourcesPermissionsMapping, username);
    }

    private Set<String> initSpy(RestAPIAuthorizationFilter restAPIAuthorizationFilterSpy) {
        doReturn("GET").when(request).getMethod();

        Set<String> permissions = new HashSet<String>(Arrays.asList("plop"));
        doReturn(permissions).when(httpSession).getAttribute(LoginManager.PERMISSIONS_SESSION_PARAM_KEY);
        doReturn(resourcesPermissionsMapping).when(restAPIAuthorizationFilterSpy).getResourcesPermissionsMapping(1);
        return permissions;
    }

    @Test
    public void should_checkPermission_do_nothing_if_secu_is_disabled() throws IOException {
        RestAPIAuthorizationFilter restAPIAuthorizationFilterSpy = spy(restAPIAuthorizationFilter);
        Set<String> permissions = initSpy(restAPIAuthorizationFilterSpy);
        doReturn(false).when(restAPIAuthorizationFilterSpy).isApiAuthorizationsCheckEnabled(1l);

        //when
        boolean isAuthorized = restAPIAuthorizationFilterSpy.checkPermissions(request, "bpm", "case", null);

        //then
        assertThat(isAuthorized).isTrue();
        verify(restAPIAuthorizationFilterSpy, times(0)).staticCheck("GET", "bpm", "case", null, permissions, resourcesPermissionsMapping, username);
    }

    @Test
    public void should_checkPermission_throw_exception_if_not_authorized() throws IOException {
        RestAPIAuthorizationFilter restAPIAuthorizationFilterSpy = spy(restAPIAuthorizationFilter);
        Set<String> permissions = initSpy(restAPIAuthorizationFilterSpy);
        doReturn(false).when(restAPIAuthorizationFilterSpy).staticCheck("GET", "bpm", "case", null, permissions, resourcesPermissionsMapping, username);
        doReturn(true).when(restAPIAuthorizationFilterSpy).isApiAuthorizationsCheckEnabled(1l);

        //when
        boolean isAuthorized = restAPIAuthorizationFilterSpy.checkPermissions(request, "bpm", "case", null);

        //then
        assertThat(isAuthorized).isFalse();
    }

    @Test
    public void should_checkPermission_parse_the_request() throws IOException {
        doReturn("API/bpm/case/15").when(request).getPathInfo();
        RestAPIAuthorizationFilter restAPIAuthorizationFilterSpy = spy(restAPIAuthorizationFilter);
        doReturn(true).when(restAPIAuthorizationFilterSpy).checkPermissions(eq(request), eq("bpm"), eq("case"), eq(APIID.makeAPIID(15l)));

        //when
        restAPIAuthorizationFilterSpy.checkPermissions(request);

        //then
        verify(restAPIAuthorizationFilterSpy).checkPermissions(eq(request), eq("bpm"), eq("case"), eq(APIID.makeAPIID(15l)));
    }

    @Test
    public void testStaticCheck_authorized() throws Exception {
        Set<String> userPermissions = new HashSet<String>(Arrays.asList("MyPermission", "AnOtherPermission"));
        returnPermissionFor("GET", "bpm", "case", null, Arrays.asList("CasePermission", "AnOtherPermission"));

        boolean isAuthorized = restAPIAuthorizationFilter.staticCheck("GET", "bpm", "case", null, userPermissions, resourcesPermissionsMapping, username);

        assertThat(isAuthorized).isTrue();

    }

    @Test
    public void testStaticCheck_unauthorized() throws Exception {
        Set<String> userPermissions = new HashSet<String>(Arrays.asList("MyPermission", "AnOtherPermission"));
        returnPermissionFor("GET", "bpm", "case", null, Arrays.asList("CasePermission", "SecondPermission"));

        boolean isAuthorized = restAPIAuthorizationFilter.staticCheck("GET", "bpm", "case", null, userPermissions, resourcesPermissionsMapping, username);

        assertThat(isAuthorized).isFalse();

    }

    @Test
    public void testStaticCheck_unauthorized_on_resource_with_id_even_if_permission_in_general_is_there() throws Exception {
        Set<String> userPermissions = new HashSet<String>(Arrays.asList("MyPermission", "AnOtherPermission"));
        returnPermissionFor("GET", "bpm", "case", null, Arrays.asList("CasePermission", "AnOtherPermission"));
        returnPermissionFor("GET", "bpm", "case", "12", Arrays.asList("CasePermission", "SecondPermission"));

        boolean isAuthorized = restAPIAuthorizationFilter.staticCheck("GET", "bpm", "case", "12", userPermissions, resourcesPermissionsMapping, username);

        assertThat(isAuthorized).isFalse();

    }

    @Test
    public void testStaticCheck_authorized_on_resource_with_id() throws Exception {
        Set<String> userPermissions = new HashSet<String>(Arrays.asList("MyPermission", "AnOtherPermission"));
        returnPermissionFor("GET", "bpm", "case", "12", Arrays.asList("CasePermission", "MyPermission"));

        boolean isAuthorized = restAPIAuthorizationFilter.staticCheck("GET", "bpm", "case", "12", userPermissions, resourcesPermissionsMapping, username);

        assertThat(isAuthorized).isTrue();

    }

    @Test
    public void testStaticCheck_resource_with_id_should_check_parent_if_no_rule() throws Exception {
        Set<String> userPermissions = new HashSet<String>(Arrays.asList("MyPermission", "AnOtherPermission"));
        returnPermissionFor("GET", "bpm", "case", null, Arrays.asList("CasePermission", "MyPermission"));

        boolean isAuthorized = restAPIAuthorizationFilter.staticCheck("GET", "bpm", "case", "12", userPermissions, resourcesPermissionsMapping, username);

        assertThat(isAuthorized).isTrue();

    }

    private void returnPermissionFor(String method, String apiName, String resourceName, String resourceId, List<String> toBeReturned) {
        doReturn(toBeReturned).when(resourcesPermissionsMapping).getResourcePermissions(method, apiName, resourceName, resourceId);
    }

    @Test
    public void should_checkValidCondition_check_session_is_platform() throws ServletException {
        doReturn("API/platform/plop").when(request).getRequestURI();
        doReturn(mock(PlatformSession.class)).when(httpSession).getAttribute(RestAPIAuthorizationFilter.PLATFORM_SESSION_PARAM_KEY);
        //when
        boolean isValid = restAPIAuthorizationFilter.checkValidCondition(request, response);

        assertThat(isValid).isTrue();
    }

    @Test
    public void should_checkValidCondition_check_unauthorized_if_no_platform_session() throws ServletException {
        doReturn("API/platform/plop").when(request).getRequestURI();
        doReturn(null).when(httpSession).getAttribute(RestAPIAuthorizationFilter.PLATFORM_SESSION_PARAM_KEY);
        //when
        boolean isValid = restAPIAuthorizationFilter.checkValidCondition(request, response);

        assertThat(isValid).isFalse();
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    public void should_checkValidCondition_check_unauthorized_if_no_tenant_session() throws ServletException {
        doReturn(null).when(httpSession).getAttribute(LoginManager.API_SESSION_PARAM_KEY);
        doReturn("API/bpm/case/15").when(request).getRequestURI();
        //when
        boolean isValid = restAPIAuthorizationFilter.checkValidCondition(request, response);

        assertThat(isValid).isFalse();
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }


    @Test
    public void should_checkValidCondition_check_permission_if_is_tenant_is_forbidden() throws ServletException {
        RestAPIAuthorizationFilter restAPIAuthorizationFilterSpy = spy(restAPIAuthorizationFilter);
        doReturn("API/bpm/case/15").when(request).getRequestURI();
        doReturn(false).when(restAPIAuthorizationFilterSpy).checkPermissions(request);

        //when
        boolean isValid = restAPIAuthorizationFilterSpy.checkValidCondition(request, response);

        assertThat(isValid).isFalse();
        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }


    @Test
    public void should_checkValidCondition_check_permission_if_is_tenant_is_ok() throws ServletException {
        RestAPIAuthorizationFilter restAPIAuthorizationFilterSpy = spy(restAPIAuthorizationFilter);
        doReturn("API/bpm/case/15").when(request).getRequestURI();
        doReturn(true).when(restAPIAuthorizationFilterSpy).checkPermissions(request);

        //when
        boolean isValid = restAPIAuthorizationFilterSpy.checkValidCondition(request, response);

        assertThat(isValid).isTrue();
    }


    @Test(expected = ServletException.class)
    public void should_checkValidCondition_catch_runtime() throws ServletException {
        doThrow(new RuntimeException()).when(request).getRequestURI();

        //when
        restAPIAuthorizationFilter.checkValidCondition(request, response);

    }
}
