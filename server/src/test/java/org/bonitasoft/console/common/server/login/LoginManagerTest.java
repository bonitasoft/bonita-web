package org.bonitasoft.console.common.server.login;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anySetOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.bonitasoft.console.common.server.auth.AuthenticationFailedException;
import org.bonitasoft.console.common.server.auth.AuthenticationManager;
import org.bonitasoft.console.common.server.login.credentials.Credentials;
import org.bonitasoft.console.common.server.login.credentials.StandardCredentials;
import org.bonitasoft.console.common.server.login.credentials.UserLogger;
import org.bonitasoft.console.common.server.utils.PermissionsBuilder;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.model.user.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletResponse;

@RunWith(MockitoJUnitRunner.class)
public class LoginManagerTest {

    @Spy
    LoginManager loginManager = new LoginManager();

    @Mock
    UserLogger userLogger;

    @Mock
    HttpServletRequest request;

    MockHttpServletResponse response = new MockHttpServletResponse();

    @Mock
    HttpSession session;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    APISession apiSession;

    @Mock
    PermissionsBuilder permissionsBuilder;

    HttpServletRequestAccessor requestAccessor;

    @Before
    public void setUp() throws Exception {
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("tenant")).thenReturn("1");
        requestAccessor = new HttpServletRequestAccessor(request);
    }

    @Test
    public void login_should_initSession() throws Exception {
        final Credentials credentials = new StandardCredentials("name", "password", 1L);
        doReturn(authenticationManager).when(loginManager).getAuthenticationManager(1L);
        doReturn(apiSession).when(userLogger).doLogin(credentials);
        doReturn(permissionsBuilder).when(loginManager).createPermissionsBuilder(apiSession);

        loginManager.login(requestAccessor, response, userLogger, credentials);

        verify(loginManager).initSession(eq(requestAccessor), eq(apiSession), any(User.class), anySetOf(String.class));
    }

    @Test
    public void login_should_perform_engine_login() throws Exception {
        final Credentials credentials = new StandardCredentials("name", "password", 1L);
        doReturn(authenticationManager).when(loginManager).getAuthenticationManager(1L);
        doReturn(apiSession).when(userLogger).doLogin(credentials);
        doReturn(permissionsBuilder).when(loginManager).createPermissionsBuilder(apiSession);

        loginManager.login(requestAccessor, response, userLogger, credentials);

        verify(userLogger).doLogin(credentials);
    }

    @Test
    public void login_should_perform_engine_login_with_credentials_map() throws Exception {
        final Credentials credentials = new StandardCredentials("name", "password", 1L);
        doReturn(authenticationManager).when(loginManager).getAuthenticationManager(1L);
        final Map<String, Serializable> credentialsMap = new HashMap<>();
        credentialsMap.put("principal", "userId");
        doReturn(credentialsMap).when(authenticationManager).authenticate(requestAccessor, credentials);
        doReturn(apiSession).when(userLogger).doLogin(credentialsMap);
        doReturn(permissionsBuilder).when(loginManager).createPermissionsBuilder(apiSession);

        loginManager.login(requestAccessor, response, userLogger, credentials);

        verify(userLogger).doLogin(credentialsMap);
    }

    @Test(expected = LoginFailedException.class)
    public void login_should_throw_exception_when_login_fails() throws Exception {
        final Credentials credentials = new StandardCredentials("name", "password", 1L);
        doReturn(authenticationManager).when(loginManager).getAuthenticationManager(1L);
        doThrow(LoginFailedException.class).when(userLogger).doLogin(credentials);

        loginManager.login(requestAccessor, response, userLogger, credentials);
    }

    @Test(expected = AuthenticationFailedException.class)
    public void login_should_throw_exception_when_authentication_fails() throws Exception {
        final Credentials credentials = new StandardCredentials("name", "password", 1L);
        doReturn(authenticationManager).when(loginManager).getAuthenticationManager(1L);
        doThrow(AuthenticationFailedException.class).when(authenticationManager).authenticate(requestAccessor, credentials);

        loginManager.login(requestAccessor, response, userLogger, credentials);
    }

    @Test
    public void should_store_tenant_id_in_cookies() throws Exception {
        Credentials credentials = new StandardCredentials("name", "password", 123L);
        doReturn(authenticationManager).when(loginManager).getAuthenticationManager(123L);
        doReturn(apiSession).when(userLogger).doLogin(credentials);
        when(apiSession.getTenantId()).thenReturn(123L);
        doReturn(permissionsBuilder).when(loginManager).createPermissionsBuilder(apiSession);

        loginManager.login(requestAccessor, response, userLogger, credentials);

        assertThat(response.getCookie("bonita.tenant").getValue()).isEqualTo("123");
    }
}
