package org.bonitasoft.console.common.server.login.servlet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bonitasoft.console.common.server.auth.AuthenticationFailedException;
import org.bonitasoft.console.common.server.auth.AuthenticationManager;
import org.bonitasoft.console.common.server.auth.AuthenticationManagerNotFoundException;
import org.bonitasoft.console.common.server.login.LoginFailedException;
import org.bonitasoft.console.common.server.login.LoginManager;
import org.bonitasoft.console.common.server.login.filter.TokenGenerator;
import org.bonitasoft.console.common.server.utils.SessionUtil;
import org.bonitasoft.engine.exception.TenantStatusException;
import org.bonitasoft.engine.session.APISession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


/**
 * Created by Vincent Elcrin
 * Date: 10/09/13
 * Time: 15:18
 */
@RunWith(MockitoJUnitRunner.class)
public class LoginServletTest {

    @Mock
    HttpServletRequest req;

    @Mock
    HttpServletResponse resp;

    @Mock
    HttpSession httpSession;

    @Mock
    APISession apiSession;

    @Mock
    TokenGenerator tokenGenerator;

    @Before
    public void setup() {
        doReturn("application/x-www-form-urlencoded").when(req).getContentType();
    }
    
    @Test
    public void testPasswordIsDroppedWhenParameterIsLast() throws Exception {
        final LoginServlet servlet = new LoginServlet();

        final String cleanQueryString = servlet.dropPassword("?username=walter.bates&password=bpm");

        assertThat(cleanQueryString, is("?username=walter.bates"));
    }

    @Test
    public void testPasswordIsDroppedWhenParameterIsBeforeHash() throws Exception {
        final LoginServlet servlet = new LoginServlet();

        final String cleanQueryString = servlet.dropPassword("?username=walter.bates&password=bpm#hash");

        assertThat(cleanQueryString, is("?username=walter.bates#hash"));
    }

    @Test
    public void testUrlIsDroppedWhenParameterIsFirstAndBeforeHash() throws Exception {
        final LoginServlet servlet = new LoginServlet();

        final String cleanQueryString = servlet.dropPassword("?username=walter.bates&password=bpm#hash");

        assertThat(cleanQueryString, is("?username=walter.bates#hash"));
    }

    @Test
    public void testUrlStayTheSameIfNoPasswordArePresent() throws Exception {
        final LoginServlet servlet = new LoginServlet();

        final String cleanQueryString = servlet.dropPassword("?param=value#dhash");

        assertThat(cleanQueryString, is("?param=value#dhash"));
    }

    @Test
    public void testPasswordIsDroppedEvenIfQueryMarkUpIsntThere() throws Exception {
        final LoginServlet servlet = new LoginServlet();

        final String cleanQueryString = servlet.dropPassword("password=bpm#dhash1&dhash2");

        assertThat(cleanQueryString, is("#dhash1&dhash2"));
    }

    @Test
    public void testUrlStayEmptyIfParameterIsEmpty() throws Exception {
        final LoginServlet servlet = new LoginServlet();

        final String cleanQueryString = servlet.dropPassword("");

        assertThat(cleanQueryString, is(""));
    }

    @Test
    public void testDropPasswordOnRealUrl() throws Exception {
        final LoginServlet servlet = new LoginServlet();

        final String cleanUrl = servlet
            .dropPassword(
                "?username=walter.bates&password=bpm&redirectUrl=http%3A%2F%2Flocalhost%3A8080%2Fbonita%2Fportal%2Fhomepage%3Fui%3Dform%26locale%3Den%23form%3DPool-\n"
                    +
                    "-1.0%24entry%26process%3D8506394779365952706%26mode%3Dapp");

        assertThat(cleanUrl,
            is("?username=walter.bates&redirectUrl=http%3A%2F%2Flocalhost%3A8080%2Fbonita%2Fportal%2Fhomepage%3Fui%3Dform%26locale%3Den%23form%3DPool-\n" +
                "-1.0%24entry%26process%3D8506394779365952706%26mode%3Dapp"));
    }

    @Test
    public void testDoGetShouldDropPassowrdWhenLoggingQueryString() throws Exception {
        final Logger logger = Logger.getLogger(LoginServlet.class.getName());
        logger.setLevel(Level.FINEST);

        //given
        final LoginServlet servlet = spy(new LoginServlet());
        doReturn("query string").when(req).getQueryString();
        doNothing().when(servlet).doPost(req, resp);

        //when
        servlet.doGet(req, resp);

        //then
        verify(req).getQueryString();
        verify(servlet).dropPassword(anyString());
    }

    @Test
    public void testDoGetShouldNotLogQueryString() throws Exception {
        final Logger logger = Logger.getLogger(LoginServlet.class.getName());
        logger.setLevel(Level.INFO);

        //given
        final LoginServlet servlet = spy(new LoginServlet());
        doNothing().when(servlet).doPost(req, resp);

        //when
        servlet.doGet(req, resp);

        //then
        verify(req, never()).getQueryString();
    }

    @Test
    public void testDoPostShouldNotUseQueryString() throws Exception {
        final Logger logger = Logger.getLogger(LoginServlet.class.getName());
        logger.setLevel(Level.FINEST);

        //given
        final LoginServlet servlet = spy(new LoginServlet());
        doReturn(httpSession).when(req).getSession();
        doReturn(apiSession).when(httpSession).getAttribute(SessionUtil.API_SESSION_PARAM_KEY);
        doReturn(true).when(apiSession).isTechnicalUser();
        doReturn(null).when(req).getParameter(AuthenticationManager.REDIRECT_AFTER_LOGIN_PARAM_NAME);
        doNothing().when(servlet).doLogin(req, resp);

        //when
        servlet.doPost(req, resp);

        //then
        verify(req, never()).getQueryString();
    }

    @Test
    public void should_send_error_401_when_login_with_wrong_credentials_and_no_redirect() throws Exception {
        final LoginServlet servlet = spy(new LoginServlet());
        doReturn("false").when(req).getParameter(AuthenticationManager.REDIRECT_AFTER_LOGIN_PARAM_NAME);
        doThrow(new LoginFailedException("")).when(servlet).doLogin(req, resp);

        servlet.doPost(req, resp);

        verify(resp).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
    
    @Test
    public void should_send_error_415_when_login_with_wrong_content_type() throws Exception {
        final LoginServlet servlet = spy(new LoginServlet());
        doReturn("application/json").when(req).getContentType();

        servlet.doPost(req, resp);

        verify(resp).setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
    }
    
    @Test
    public void should_login_with_no_content_type() throws Exception {
        final LoginServlet servlet = spy(new LoginServlet());
        final LoginManager loginManager = mock(LoginManager.class);
        doReturn(null).when(req).getContentType();
        doReturn("false").when(req).getParameter(AuthenticationManager.REDIRECT_AFTER_LOGIN_PARAM_NAME);
        doReturn(httpSession).when(req).getSession();
        doReturn(apiSession).when(httpSession).getAttribute(SessionUtil.API_SESSION_PARAM_KEY);
        doReturn(loginManager).when(servlet).getLoginManager();
        doNothing().when(loginManager).login(req, resp);

        servlet.doPost(req, resp);

        verify(loginManager).login(req, resp);
        verify(resp, never()).setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
    }

    @Test(expected = ServletException.class)
    public void should_send_servlet_exception_when_login_with_wrong_credentials_and_redirect() throws Exception {
        final LoginServlet servlet = spy(new LoginServlet());
        doReturn("true").when(req).getParameter(AuthenticationManager.REDIRECT_AFTER_LOGIN_PARAM_NAME);
        doThrow(new LoginFailedException("")).when(servlet).doLogin(req, resp);

        servlet.doPost(req, resp);
    }

    @Test(expected = ServletException.class)
    public void should_throw_tenant_status_exception() throws Exception {
        final LoginServlet servlet = spy(new LoginServlet());
        doReturn("true").when(req).getParameter(AuthenticationManager.REDIRECT_AFTER_LOGIN_PARAM_NAME);
        doThrow(new TenantStatusException("")).when(servlet).doLogin(req, resp);

        servlet.doPost(req, resp);
    }

    @Test(expected = ServletException.class)
    public void should_return_servlet_exception_when_throw_authentication_failed_exception() throws Exception {
        final LoginServlet servlet = spy(new LoginServlet());
        doReturn("true").when(req).getParameter(AuthenticationManager.REDIRECT_AFTER_LOGIN_PARAM_NAME);
        doThrow(new AuthenticationFailedException("")).when(servlet).doLogin(req, resp);

        servlet.doPost(req, resp);
    }

    @Test(expected = ServletException.class)
    public void should_return_servlet_exception_when_authentication_manager_not_found_exception() throws Exception {
        final LoginServlet servlet = spy(new LoginServlet());
        doReturn("true").when(req).getParameter(AuthenticationManager.REDIRECT_AFTER_LOGIN_PARAM_NAME);
        doThrow(new AuthenticationManagerNotFoundException("")).when(servlet).doLogin(req, resp);

        servlet.doPost(req, resp);
    }

    @Test(expected = LoginFailedException.class)
    public void testTenantInMaintenance() throws Exception {
        final LoginManager loginManager = mock(LoginManager.class);
        final LoginServlet servlet = spy(new LoginServlet());
        doReturn(loginManager).when(servlet).getLoginManager();
        
        final TenantStatusException tenantInMaintenanceException = new TenantStatusException("Tenant is in pause, unable to log in with other user than the technical user.");
        doThrow(tenantInMaintenanceException).when(loginManager).login(req, resp);

        try {
            servlet.doLogin(req, resp);
        } finally {
            verify(req, times(1)).setAttribute(LoginServlet.TENANT_IN_MAINTENACE_MESSAGE, LoginServlet.TENANT_IN_MAINTENACE_MESSAGE);
        }
    }

    @Test
    public void testTenantNotInMaintenance() throws Exception {
        final LoginManager loginManager = mock(LoginManager.class);
        final LoginServlet servlet = spy(new LoginServlet());
        doReturn("false").when(req).getParameter(AuthenticationManager.REDIRECT_AFTER_LOGIN_PARAM_NAME);
        doReturn(httpSession).when(req).getSession();
        doReturn(apiSession).when(httpSession).getAttribute(SessionUtil.API_SESSION_PARAM_KEY);
        doReturn(loginManager).when(servlet).getLoginManager();
        doNothing().when(loginManager).login(req, resp);
        
        servlet.doLogin(req, resp);
        
        verify(req, times(0)).setAttribute(LoginServlet.TENANT_IN_MAINTENACE_MESSAGE, LoginServlet.TENANT_IN_MAINTENACE_MESSAGE);
        verify(loginManager).login(req, resp);
    }

}
