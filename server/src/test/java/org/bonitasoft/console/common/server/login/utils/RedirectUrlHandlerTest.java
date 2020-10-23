package org.bonitasoft.console.common.server.login.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doReturn;

import javax.servlet.http.HttpServletRequest;

import org.bonitasoft.console.common.server.auth.AuthenticationManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RedirectUrlHandlerTest {
	
    @Mock
    HttpServletRequest req;

    @Test
    public void should_not_redirect_after_login_when_redirect_parameter_is_set_to_false() throws Exception {
        doReturn("false").when(req).getParameter(AuthenticationManager.REDIRECT_AFTER_LOGIN_PARAM_NAME);
        doReturn("anyurl").when(req).getParameter(AuthenticationManager.REDIRECT_URL);

        assertThat("should not redirect", !RedirectUrlHandler.shouldRedirectAfterLogin(req));
    }
    
    @Test
    public void should_redirect_after_login_when_redirect_url_is_set_in_request() throws Exception {
        doReturn("anyurl").when(req).getParameter(AuthenticationManager.REDIRECT_URL);

        assertThat("should redirect", RedirectUrlHandler.shouldRedirectAfterLogin(req));
    }
    
    @Test
    public void should_redirect_after_login_when_redirect_parameter_is_set_to_true() throws Exception {
        doReturn("true").when(req).getParameter(AuthenticationManager.REDIRECT_AFTER_LOGIN_PARAM_NAME);
        
        assertThat("should redirect", RedirectUrlHandler.shouldRedirectAfterLogin(req));
    }
    
    @Test
    public void should_not_redirect_after_logout_when_redirect_parameter_is_set_to_false() throws Exception {
        doReturn("false").when(req).getParameter(AuthenticationManager.REDIRECT_AFTER_LOGIN_PARAM_NAME);
        doReturn("anyurl").when(req).getParameter(AuthenticationManager.REDIRECT_URL);
        doReturn("anyurl").when(req).getParameter(AuthenticationManager.LOGIN_URL_PARAM_NAME);

        assertThat("should not redirect", !RedirectUrlHandler.shouldRedirectAfterLogout(req));
    }
    
    @Test
    public void should_redirect_after_logout_when_redirect_url_is_set_in_request() throws Exception {
        doReturn("anyurl").when(req).getParameter(AuthenticationManager.REDIRECT_URL);

        assertThat("should redirect", RedirectUrlHandler.shouldRedirectAfterLogout(req));
    }
    
    @Test
    public void should_redirect_after_logout_when_login_url_is_set_in_request() throws Exception {
        doReturn("anyurl").when(req).getParameter(AuthenticationManager.LOGIN_URL_PARAM_NAME);

        assertThat("should redirect", RedirectUrlHandler.shouldRedirectAfterLogout(req));
    }
    
    @Test
    public void should_redirect_after_logout_when_redirect_parameter_is_set_to_true() throws Exception {
        doReturn("true").when(req).getParameter(AuthenticationManager.REDIRECT_AFTER_LOGIN_PARAM_NAME);
        
        assertThat("should redirect", RedirectUrlHandler.shouldRedirectAfterLogout(req));
    }
}
