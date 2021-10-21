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

import java.util.Arrays;
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

    private final String username = "john";

    private final long userId = 9L;

    @Before
    public void before() {
        doReturn(httpSession).when(request).getSession();
        doReturn("").when(request).getQueryString();
        doReturn(apiSession).when(httpSession).getAttribute(SessionUtil.API_SESSION_PARAM_KEY);
        doReturn(1L).when(apiSession).getTenantId();
        doReturn(userId).when(apiSession).getUserId();
        doReturn(false).when(apiSession).isTechnicalUser();
        doReturn(username).when(apiSession).getUserName();
    }

    private Set<String> initSpy(final RestAPIAuthorizationFilter restAPIAuthorizationFilterSpy) throws ServletException {
        final Set<String> permissions = new HashSet<>(List.of("plop"));
        return initSpy(restAPIAuthorizationFilterSpy, permissions);
    }

    private Set<String> initSpy(final RestAPIAuthorizationFilter restAPIAuthorizationFilterSpy, final Set<String> permissions) throws ServletException {
        doReturn("GET").when(request).getMethod();
        doReturn(permissions).when(httpSession).getAttribute(SessionUtil.PERMISSIONS_SESSION_PARAM_KEY);
        doReturn(resourcesPermissionsMapping).when(restAPIAuthorizationFilterSpy).getResourcesPermissionsMapping(1);
        doReturn(dynamicPermissionsChecks).when(restAPIAuthorizationFilterSpy).getDynamicPermissionsChecks(1);
        doReturn("").when(restAPIAuthorizationFilterSpy).getRequestBody(request);
        return permissions;
    }

    @Test
    public void should_checkPermissions_call_dynamic_check_if_secu_is_enabled() throws Exception {
        final RestAPIAuthorizationFilter restAPIAuthorizationFilterSpy = spy(restAPIAuthorizationFilter);
        final Set<String> permissions = initSpy(restAPIAuthorizationFilterSpy);
        doReturn(true).when(restAPIAuthorizationFilterSpy).isApiAuthorizationsCheckEnabled(1L);
        final Set<String> dynamicAuthorizations = new HashSet<>(List.of("check|className"));
        doReturn(dynamicAuthorizations).when(restAPIAuthorizationFilterSpy).getDeclaredPermissions(anyString(), anyString(),
                anyString(), any(APIID.class), isA(DynamicPermissionsChecks.class));
        doReturn(true).when(restAPIAuthorizationFilterSpy).dynamicCheck(any(APICallContext.class),
                anySetOf(String.class), anySetOf(String.class), any(APISession.class));
        //when
        final boolean isAuthorized = restAPIAuthorizationFilterSpy.checkPermissions(request, "bpm", "case", null);

        //then
        assertThat(isAuthorized).isTrue();
        verify(restAPIAuthorizationFilterSpy).dynamicCheck(new APICallContext("GET", "bpm", "case", null, "", ""), permissions, dynamicAuthorizations,
                apiSession);
        verify(restAPIAuthorizationFilterSpy, never()).staticCheck(any(APICallContext.class),
                anySetOf(String.class), anySetOf(String.class), anyString());
    }

    @Test
    public void should_checkPermissions_call_static_check_if_secu_is_enabled_and_no_dynamic_permissions_are_defined() throws Exception {
        final RestAPIAuthorizationFilter restAPIAuthorizationFilterSpy = spy(restAPIAuthorizationFilter);
        final Set<String> permissions = initSpy(restAPIAuthorizationFilterSpy);
        doReturn(true).when(restAPIAuthorizationFilterSpy).isApiAuthorizationsCheckEnabled(1L);
        final Set<String> emptyDynamicAuthorizations = new HashSet<>();
        doReturn(emptyDynamicAuthorizations).when(restAPIAuthorizationFilterSpy).getDeclaredPermissions(anyString(), anyString(),
                anyString(), any(APIID.class), isA(ResourcesPermissionsMapping.class));
        doReturn(true).when(restAPIAuthorizationFilterSpy).staticCheck(any(APICallContext.class), anySetOf(String.class),
                anySetOf(String.class), eq(username));
        //when
        final boolean isAuthorized = restAPIAuthorizationFilterSpy.checkPermissions(request, "bpm", "case", null);

        //then
        assertThat(isAuthorized).isTrue();
        verify(restAPIAuthorizationFilterSpy).staticCheck(new APICallContext("GET", "bpm", "case", null), permissions, new HashSet<>(), username);
        verify(restAPIAuthorizationFilterSpy, never()).dynamicCheck(any(APICallContext.class), anySetOf(String.class),
                anySetOf(String.class), any(APISession.class));
    }

    @Test
    public void should_not_call_static_check_if_secu_is_enabled_but_session_call_is_always_authorized() throws Exception {
        final RestAPIAuthorizationFilter restAPIAuthorizationFilterSpy = spy(restAPIAuthorizationFilter);
        doReturn("GET").when(request).getMethod();
        doReturn(true).when(restAPIAuthorizationFilterSpy).isApiAuthorizationsCheckEnabled(1L);
        final Set<String> emptyDynamicAuthorizations = new HashSet<>();
        doReturn(emptyDynamicAuthorizations).when(restAPIAuthorizationFilterSpy).getDeclaredPermissions(anyString(), anyString(),
                anyString(), any(APIID.class), isA(ResourcesPermissionsMapping.class));
        //when
        final boolean isAuthorized = restAPIAuthorizationFilterSpy.checkPermissions(request, "system", "session", null);

        //then
        assertThat(isAuthorized).isTrue();
        verify(restAPIAuthorizationFilterSpy).isAlwaysAuthorizedResource(any(APICallContext.class));
        verify(restAPIAuthorizationFilterSpy, never()).staticCheck(any(APICallContext.class), anySetOf(String.class),
                anySetOf(String.class), anyString());
        verify(restAPIAuthorizationFilterSpy, never()).dynamicCheck(any(APICallContext.class), anySetOf(String.class),
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
        verify(restAPIAuthorizationFilterSpy, never()).staticCheck(any(APICallContext.class),
                anySetOf(String.class), anySetOf(String.class), anyString());
        verify(restAPIAuthorizationFilterSpy, never()).dynamicCheck(any(APICallContext.class),
                anySetOf(String.class), anySetOf(String.class), any(APISession.class));
    }

    @Test
    public void should_checkPermissions_do_nothing_if_secu_is_disabled() throws Exception {
        final RestAPIAuthorizationFilter restAPIAuthorizationFilterSpy = spy(restAPIAuthorizationFilter);
        initSpy(restAPIAuthorizationFilterSpy);
        doReturn(false).when(restAPIAuthorizationFilterSpy).isApiAuthorizationsCheckEnabled(1L);
        final Set<String> dynamicAuthorizations = new HashSet<>(List.of("check|className"));
        doReturn(dynamicAuthorizations).when(restAPIAuthorizationFilterSpy).getDeclaredPermissions(anyString(), anyString(),
                anyString(), any(APIID.class), isA(DynamicPermissionsChecks.class));
        final List<String> resourcePermissions = Arrays.asList("CasePermission", "MyPermission");
        returnPermissionFor("GET", "bpm", "case", null, resourcePermissions);

        //when
        final boolean isAuthorized = restAPIAuthorizationFilterSpy.checkPermissions(request, "bpm", "case", null);

        //then
        assertThat(isAuthorized).isTrue();
        verify(restAPIAuthorizationFilterSpy, never()).staticCheck(any(APICallContext.class),
                anySetOf(String.class), anySetOf(String.class), anyString());
        verify(restAPIAuthorizationFilterSpy, never()).dynamicCheck(any(APICallContext.class),
                anySetOf(String.class), anySetOf(String.class), any(APISession.class));
    }

    @Test
    public void should_checkPermissions_return_true_if_static_authorized() throws Exception {
        final RestAPIAuthorizationFilter restAPIAuthorizationFilterSpy = spy(restAPIAuthorizationFilter);
        initSpy(restAPIAuthorizationFilterSpy);
        doReturn(true).when(restAPIAuthorizationFilterSpy).isApiAuthorizationsCheckEnabled(1L);
        doReturn(new HashSet<String>()).when(restAPIAuthorizationFilterSpy).getDeclaredPermissions(anyString(), anyString(),
                anyString(), any(APIID.class), isA(ResourcesPermissionsMapping.class));
        doReturn(true).when(restAPIAuthorizationFilterSpy).staticCheck(any(APICallContext.class), anySetOf(String.class),
                anySetOf(String.class), anyString());

        //when
        final boolean isAuthorized = restAPIAuthorizationFilterSpy.checkPermissions(request, "bpm", "case", null);

        //then
        assertThat(isAuthorized).isTrue();
    }

    @Test
    public void should_checkPermissions_return_false_if_static_not_authorized() throws Exception {
        final RestAPIAuthorizationFilter restAPIAuthorizationFilterSpy = spy(restAPIAuthorizationFilter);
        initSpy(restAPIAuthorizationFilterSpy);
        doReturn(true).when(restAPIAuthorizationFilterSpy).isApiAuthorizationsCheckEnabled(1L);
        doReturn(new HashSet<String>()).when(restAPIAuthorizationFilterSpy).getDeclaredPermissions(anyString(), anyString(),
                anyString(), any(APIID.class), isA(ResourcesPermissionsMapping.class));
        doReturn(false).when(restAPIAuthorizationFilterSpy).staticCheck(any(APICallContext.class), anySetOf(String.class),
                anySetOf(String.class), anyString());

        //when
        final boolean isAuthorized = restAPIAuthorizationFilterSpy.checkPermissions(request, "bpm", "case", null);

        //then
        assertThat(isAuthorized).isFalse();
    }

    @Test
    public void should_checkPermissions_return_true_if_dynamic_authorized() throws Exception {
        final RestAPIAuthorizationFilter restAPIAuthorizationFilterSpy = spy(restAPIAuthorizationFilter);
        initSpy(restAPIAuthorizationFilterSpy);
        doReturn(true).when(restAPIAuthorizationFilterSpy).isApiAuthorizationsCheckEnabled(1L);
        final Set<String> dynamicAuthorizations = new HashSet<>(List.of("check|className"));
        doReturn(dynamicAuthorizations).when(restAPIAuthorizationFilterSpy).getDeclaredPermissions(anyString(), anyString(),
                anyString(), any(APIID.class), isA(DynamicPermissionsChecks.class));
        doReturn(true).when(restAPIAuthorizationFilterSpy).dynamicCheck(any(APICallContext.class),
                anySetOf(String.class), anySetOf(String.class), any(APISession.class));

        //when
        final boolean isAuthorized = restAPIAuthorizationFilterSpy.checkPermissions(request, "bpm", "case", null);

        //then
        assertThat(isAuthorized).isTrue();
    }

    @Test
    public void should_checkPermissions_return_false_if_dynamic_not_authorized() throws Exception {
        final RestAPIAuthorizationFilter restAPIAuthorizationFilterSpy = spy(restAPIAuthorizationFilter);
        initSpy(restAPIAuthorizationFilterSpy);
        doReturn(true).when(restAPIAuthorizationFilterSpy).isApiAuthorizationsCheckEnabled(1L);
        final Set<String> dynamicAuthorizations = new HashSet<>(List.of("check|className"));
        doReturn(dynamicAuthorizations).when(restAPIAuthorizationFilterSpy).getDeclaredPermissions(anyString(), anyString(),
                anyString(), any(APIID.class), isA(DynamicPermissionsChecks.class));
        doReturn(false).when(restAPIAuthorizationFilterSpy).dynamicCheck(any(APICallContext.class),
                anySetOf(String.class), anySetOf(String.class), any(APISession.class));

        //when
        final boolean isAuthorized = restAPIAuthorizationFilterSpy.checkPermissions(request, "bpm", "case", null);

        //then
        assertThat(isAuthorized).isFalse();
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
    public void test_staticCheck_authorized() {
        final Set<String> userPermissions = new HashSet<>(Arrays.asList("MyPermission", "AnOtherPermission"));
        final List<String> resourcePermissions = Arrays.asList("CasePermission", "AnOtherPermission");
        returnPermissionFor("GET", "bpm", "case", null, resourcePermissions);

        final boolean isAuthorized = restAPIAuthorizationFilter.staticCheck(new APICallContext("GET", "bpm", "case", null), userPermissions,
                new HashSet<>(resourcePermissions), username);

        assertThat(isAuthorized).isTrue();

    }

    @Test
    public void test_staticCheck_unauthorized() {
        final Set<String> userPermissions = new HashSet<>(Arrays.asList("MyPermission", "AnOtherPermission"));
        final List<String> resourcePermissions = Arrays.asList("CasePermission", "SecondPermission");
        returnPermissionFor("GET", "bpm", "case", null, resourcePermissions);

        final boolean isAuthorized = restAPIAuthorizationFilter.staticCheck(new APICallContext("GET", "bpm", "case", null), userPermissions,
                new HashSet<>(resourcePermissions), username);

        assertThat(isAuthorized).isFalse();
    }

    @Test
    public void test_checkPermissions_unauthorized_on_resource_with_id_even_if_permission_in_general_is_there() throws Exception {
        final RestAPIAuthorizationFilter restAPIAuthorizationFilterSpy = spy(restAPIAuthorizationFilter);
        final Set<String> userPermissions = new HashSet<>(Arrays.asList("MyPermission", "AnOtherPermission"));
        initSpy(restAPIAuthorizationFilterSpy, userPermissions);
        returnPermissionFor("GET", "bpm", "case", null, Arrays.asList("CasePermission", "AnOtherPermission"));
        returnPermissionFor("GET", "bpm", "case", List.of("12"), Arrays.asList("CasePermission", "SecondPermission"));
        doReturn(true).when(restAPIAuthorizationFilterSpy).isApiAuthorizationsCheckEnabled(1L);
        final Set<String> emptyDynamicAuthorizations = new HashSet<>();
        doReturn(emptyDynamicAuthorizations).when(restAPIAuthorizationFilterSpy).getDeclaredPermissions(anyString(), anyString(),
                anyString(), any(APIID.class), isA(DynamicPermissionsChecks.class));

        final boolean isAuthorized = restAPIAuthorizationFilterSpy.checkPermissions(request, "bpm", "case", APIID.makeAPIID(12L));

        assertThat(isAuthorized).isFalse();
    }

    @Test
    public void test_checkPermissions_authorized_on_resource_with_id() throws Exception {
        final RestAPIAuthorizationFilter restAPIAuthorizationFilterSpy = spy(restAPIAuthorizationFilter);
        final Set<String> userPermissions = new HashSet<>(Arrays.asList("MyPermission", "AnOtherPermission"));
        initSpy(restAPIAuthorizationFilterSpy, userPermissions);
        returnPermissionFor("GET", "bpm", "case", List.of("12"), Arrays.asList("CasePermission", "MyPermission"));
        doReturn(true).when(restAPIAuthorizationFilterSpy).isApiAuthorizationsCheckEnabled(1L);
        final Set<String> emptyDynamicAuthorizations = new HashSet<>();
        doReturn(emptyDynamicAuthorizations).when(restAPIAuthorizationFilterSpy).getDeclaredPermissions(anyString(), anyString(),
                anyString(), any(APIID.class), isA(DynamicPermissionsChecks.class));

        final boolean isAuthorized = restAPIAuthorizationFilterSpy.checkPermissions(request, "bpm", "case", APIID.makeAPIID(12L));

        assertThat(isAuthorized).isTrue();
    }

    @Test
    public void test_checkPermissions_resource_with_id_should_check_parent_if_no_rule() throws Exception {
        final RestAPIAuthorizationFilter restAPIAuthorizationFilterSpy = spy(restAPIAuthorizationFilter);
        final Set<String> userPermissions = new HashSet<>(Arrays.asList("MyPermission", "AnOtherPermission"));
        initSpy(restAPIAuthorizationFilterSpy, userPermissions);
        final List<String> resourcePermissions = Arrays.asList("CasePermission", "MyPermission");
        returnPermissionFor("GET", "bpm", "case", null, resourcePermissions);
        doReturn(true).when(restAPIAuthorizationFilterSpy).isApiAuthorizationsCheckEnabled(1L);
        final Set<String> emptyDynamicAuthorizations = new HashSet<>();
        doReturn(emptyDynamicAuthorizations).when(restAPIAuthorizationFilterSpy).getDeclaredPermissions(anyString(), anyString(),
                anyString(), any(APIID.class), isA(DynamicPermissionsChecks.class));

        final boolean isAuthorized = restAPIAuthorizationFilterSpy.checkPermissions(request, "bpm", "case", APIID.makeAPIID(12L));

        assertThat(isAuthorized).isTrue();
    }

    @Test
    public void test_checkPermissions_authorized_on_resource_with_wildcard() throws Exception {
        final RestAPIAuthorizationFilter restAPIAuthorizationFilterSpy = spy(restAPIAuthorizationFilter);
        final Set<String> userPermissions = new HashSet<>(Arrays.asList("MyPermission", "AnOtherPermission"));
        initSpy(restAPIAuthorizationFilterSpy, userPermissions);
        final List<String> resourcePermissions = Arrays.asList("CasePermission", "MyPermission");
        doReturn(new HashSet<>(resourcePermissions)).when(resourcesPermissionsMapping).getResourcePermissionsWithWildCard("GET", "bpm", "case",
                Arrays.asList("12", "instantiation"));
        doReturn(true).when(restAPIAuthorizationFilterSpy).isApiAuthorizationsCheckEnabled(1L);
        final Set<String> emptyDynamicAuthorizations = new HashSet<>();
        doReturn(emptyDynamicAuthorizations).when(restAPIAuthorizationFilterSpy).getDeclaredPermissions(anyString(), anyString(),
                anyString(), any(APIID.class), isA(DynamicPermissionsChecks.class));

        final boolean isAuthorized = restAPIAuthorizationFilterSpy.checkPermissions(request, "bpm", "case", APIID.makeAPIID("12", "instantiation"));

        assertThat(isAuthorized).isTrue();
    }

    private void returnPermissionFor(final String method, final String apiName, final String resourceName, final List<String> resourceQualifiers,
                                     final List<String> toBeReturned) {
        if (resourceQualifiers != null) {
            doReturn(new HashSet<>(toBeReturned)).when(resourcesPermissionsMapping).getResourcePermissions(method, apiName, resourceName,
                    resourceQualifiers);
        } else {
            doReturn(new HashSet<>(toBeReturned)).when(resourcesPermissionsMapping).getResourcePermissions(method, apiName, resourceName);
        }
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
