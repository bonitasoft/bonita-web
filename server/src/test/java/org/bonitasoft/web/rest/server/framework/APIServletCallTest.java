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

package org.bonitasoft.web.rest.server.framework;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bonitasoft.console.common.server.login.LoginManager;
import org.bonitasoft.console.common.server.preferences.properties.ResourcesPermissionsMapping;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIForbiddenException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIMalformedUrlException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class APIServletCallTest {

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

    private APIServletCall apiServletCall = new APIServletCall();

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
    public void testStaticCheck_authorized() throws Exception {
        List<String> userPermissions = Arrays.asList("MyPermission", "AnOtherPermission");
        returnPermissionFor("GET", "bpm", "case", null, Arrays.asList("CasePermission", "AnOtherPermission"));

        boolean isAuthorized = apiServletCall.staticCheck("GET", "bpm", "case", null, userPermissions, resourcesPermissionsMapping, username);

        assertThat(isAuthorized).isTrue();

    }

    @Test
    public void testStaticCheck_unauthorized() throws Exception {
        List<String> userPermissions = Arrays.asList("MyPermission", "AnOtherPermission");
        returnPermissionFor("GET", "bpm", "case", null, Arrays.asList("CasePermission", "SecondPermission"));

        boolean isAuthorized = apiServletCall.staticCheck("GET", "bpm", "case", null, userPermissions, resourcesPermissionsMapping, username);

        assertThat(isAuthorized).isFalse();

    }

    @Test
    public void testStaticCheck_unauthorized_on_resource_with_id_even_if_permission_in_general_is_there() throws Exception {
        List<String> userPermissions = Arrays.asList("MyPermission", "AnOtherPermission");
        returnPermissionFor("GET", "bpm", "case", null, Arrays.asList("CasePermission", "AnOtherPermission"));
        returnPermissionFor("GET", "bpm", "case", "12", Arrays.asList("CasePermission", "SecondPermission"));

        boolean isAuthorized = apiServletCall.staticCheck("GET", "bpm", "case", "12", userPermissions, resourcesPermissionsMapping, username);

        assertThat(isAuthorized).isFalse();

    }

    @Test
    public void testStaticCheck_authorized_on_resource_with_id() throws Exception {
        List<String> userPermissions = Arrays.asList("MyPermission", "AnOtherPermission");
        returnPermissionFor("GET", "bpm", "case", "12", Arrays.asList("CasePermission", "MyPermission"));

        boolean isAuthorized = apiServletCall.staticCheck("GET", "bpm", "case", "12", userPermissions, resourcesPermissionsMapping, username);

        assertThat(isAuthorized).isTrue();

    }

    @Test
    public void testStaticCheck_resource_with_id_should_check_parent_if_no_rule() throws Exception {
        List<String> userPermissions = Arrays.asList("MyPermission", "AnOtherPermission");
        returnPermissionFor("GET", "bpm", "case", null, Arrays.asList("CasePermission", "MyPermission"));

        boolean isAuthorized = apiServletCall.staticCheck("GET", "bpm", "case", "12", userPermissions, resourcesPermissionsMapping, username);

        assertThat(isAuthorized).isTrue();

    }

    private void returnPermissionFor(String method, String apiName, String resourceName, String resourceId, List<String> toBeReturned) {
        doReturn(toBeReturned).when(resourcesPermissionsMapping).getResourcePermissions(method, apiName, resourceName, resourceId);
    }

    @Test
    public void should_parsePath_request_info_with_id() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        doReturn("API/bpm/case/15").when(request).getPathInfo();

        apiServletCall.parsePath(request);

        assertThat(apiServletCall.getId().getPart(0)).isEqualTo("15");
        assertThat(apiServletCall.getResourceName()).isEqualTo("case");
        assertThat(apiServletCall.getApiName()).isEqualTo("bpm");

    }

    @Test
    public void should_parsePath_request_info() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        doReturn("API/bpm/case").when(request).getPathInfo();

        apiServletCall.parsePath(request);

        assertThat(apiServletCall.getId()).isNull();
        assertThat(apiServletCall.getResourceName()).isEqualTo("case");
        assertThat(apiServletCall.getApiName()).isEqualTo("bpm");

    }

    @Test(expected = APIMalformedUrlException.class)
    public void should_parsePath_with_bad_request() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        doReturn("API/bpm").when(request).getPathInfo();

        apiServletCall.parsePath(request);
    }

    @Test
    public void should_checkPermission_call_static_check_if_secu_is_enabled() throws IOException {
        APIServletCall apiServletCallSpy = spy(apiServletCall);
        List<String> permissions = initSpy(apiServletCallSpy);
        doReturn(true).when(apiServletCallSpy).isApiAuthorizationsCheckEnabled(1l);
        doReturn(true).when(apiServletCallSpy).staticCheck(anyString(), anyString(), anyString(), anyString(), anyList(),
                any(ResourcesPermissionsMapping.class), eq(username));
        //when
        apiServletCallSpy.checkPermissions(request);

        //then
        verify(apiServletCallSpy).staticCheck("GET", "bpm", "case", null, permissions, resourcesPermissionsMapping, username);
    }

    @Test
    public void should_checkPermission_do_not_call_check_if_technical() throws IOException {
        doReturn(true).when(apiSession).isTechnicalUser();
        APIServletCall apiServletCallSpy = spy(apiServletCall);
        List<String> permissions = initSpy(apiServletCallSpy);
        doReturn(true).when(apiServletCallSpy).isApiAuthorizationsCheckEnabled(1l);
        doReturn(true).when(apiServletCallSpy).staticCheck(anyString(), anyString(), anyString(), anyString(), anyList(),
                any(ResourcesPermissionsMapping.class), eq(username));
        //when
        apiServletCallSpy.checkPermissions(request);

        //then
        verify(apiServletCallSpy, times(0)).staticCheck("GET", "bpm", "case", null, permissions, resourcesPermissionsMapping, username);
    }

    private List<String> initSpy(APIServletCall apiServletCallSpy) {
        apiServletCallSpy.setApiName("bpm");
        apiServletCallSpy.setResourceName("case");
        doReturn("GET").when(request).getMethod();

        List<String> permissions = Arrays.asList("plop");
        doReturn(permissions).when(httpSession).getAttribute(LoginManager.PERMISSIONS_SESSION_PARAM_KEY);
        doReturn(resourcesPermissionsMapping).when(apiServletCallSpy).getResourcesPermissionsMapping(1);
        return permissions;
    }

    @Test
    public void should_checkPermission_do_nothing_if_secu_is_disabled() throws IOException {
        APIServletCall apiServletCallSpy = spy(apiServletCall);
        List<String> permissions = initSpy(apiServletCallSpy);
        doReturn(false).when(apiServletCallSpy).isApiAuthorizationsCheckEnabled(1l);

        //when
        apiServletCallSpy.checkPermissions(request);

        //then
        verify(apiServletCallSpy, times(0)).staticCheck("GET", "bpm", "case", null, permissions, resourcesPermissionsMapping, username);
    }

    @Test(expected = APIForbiddenException.class)
    public void should_checkPermission_throw_exception_if_not_authorized() throws IOException {
        APIServletCall apiServletCallSpy = spy(apiServletCall);
        List<String> permissions = initSpy(apiServletCallSpy);
        doReturn(false).when(apiServletCallSpy).staticCheck("GET", "bpm", "case", null, permissions, resourcesPermissionsMapping, username);
        doReturn(true).when(apiServletCallSpy).isApiAuthorizationsCheckEnabled(1l);

        //when
        apiServletCallSpy.checkPermissions(request);

        //then exception
    }
}
