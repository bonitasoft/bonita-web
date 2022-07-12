package org.bonitasoft.console.common.server.login.servlet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;

import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;

import org.bonitasoft.console.common.server.auth.AuthenticationManager;
import org.bonitasoft.console.common.server.login.HttpServletRequestAccessor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class LogoutServletTest {
    
    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletRequestAccessor requestAccessor;
    
    @Mock
    AuthenticationManager authenticationManager;

    @Spy
    LogoutServlet logoutServlet = new LogoutServlet();

    @Before
    public void setup() throws Exception {
        doReturn(request).when(requestAccessor).asHttpServletRequest();
        doReturn(authenticationManager).when(logoutServlet).getAuthenticationManager();
    }
    
    @Test
    public void testtSanitizeLoginPageUrlEmptyLoginPageUrl() throws Exception {
        String loginPage = logoutServlet.sanitizeLoginPageUrl("");
        
        assertThat(loginPage).isEqualToIgnoringCase("");
    }

    @Test
    public void testtSanitizeLoginPageUrlFromMaliciousRedirectShouldReturnBrokenUrl() throws Exception {
        try {
            logoutServlet.sanitizeLoginPageUrl("http://www.test.com");
            fail("building a login page with a different host on the redirectURL should fail");
        } catch (Exception e) {
            if (!(e.getCause() instanceof URISyntaxException)) {
                fail("Exception root cause should be a URISyntaxException");
            }
        }
    }

    @Test
    public void testtSanitizeLoginPageUrlFromMaliciousRedirectShouldReturnBrokenUrl2() throws Exception {
        String loginPage = logoutServlet.sanitizeLoginPageUrl("test.com");
        
        assertThat(loginPage).isEqualToIgnoringCase("test.com");
    }

    @Test
    public void testSanitizeLoginPageUrlShouldReturnCorrectUrl() throws Exception {
        String loginPage = logoutServlet.sanitizeLoginPageUrl("apps/appDirectoryBonita?p=test#poutpout");
        
        assertThat(loginPage).isEqualToIgnoringCase("apps/appDirectoryBonita?p=test#poutpout");
    }

    @Test
    public void testGetURLToRedirectToFromAuthenticationManagerLogout() throws Exception {
        doReturn("redirectURL").when(logoutServlet).createRedirectUrl(requestAccessor);
        doReturn("logoutURL").when(authenticationManager).getLogoutPageURL(requestAccessor, "redirectURL");
        
        String loginPage = logoutServlet.getURLToRedirectTo(requestAccessor);
        
        assertThat(loginPage).isEqualTo("logoutURL");
    }
    
    @Test
    public void testGetURLToRedirectToFromAuthenticationManagerLogin() throws Exception {
        doReturn("loginURL").when(authenticationManager).getLoginPageURL(eq(requestAccessor), anyString());
        
        String loginPage = logoutServlet.getURLToRedirectTo(requestAccessor);
        
        assertThat(loginPage).isEqualTo("loginURL");
    }

    @Test
    public void testGetURLToRedirectToFromRequest() throws Exception {
        doReturn(null).when(authenticationManager).getLoginPageURL(eq(requestAccessor), anyString());
        doReturn("redirectURLFromRequest").when(request).getParameter(AuthenticationManager.LOGIN_URL_PARAM_NAME);
        
        String loginPage = logoutServlet.getURLToRedirectTo(requestAccessor);
        
        assertThat(loginPage).isEqualTo("redirectURLFromRequest");
    }
    
    @Test
    public void testCreateRedirectUrlWithDefaultRedirect() throws Exception {
        doReturn(null).when(requestAccessor).getRedirectUrl();
        
        String redirectURL = logoutServlet.createRedirectUrl(requestAccessor);
        
        assertThat(redirectURL).isEqualTo(AuthenticationManager.DEFAULT_DIRECT_URL);
    }
    
    @Test
    public void testCreateRedirectUrl() throws Exception {
        doReturn("redirectURL").when(requestAccessor).getRedirectUrl();
        
        String redirectURL = logoutServlet.createRedirectUrl(requestAccessor);
        
        assertThat(redirectURL).isEqualTo("redirectURL");
    }

}
