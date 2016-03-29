package org.bonitasoft.console.common.server.auth.impl.standard;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;

import org.bonitasoft.console.common.server.login.HttpServletRequestAccessor;
import org.bonitasoft.console.common.server.login.datastore.UserLogger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StandardAuthenticationManagerImplTest {

    @Spy
    StandardAuthenticationManagerImpl standardLoginManagerImpl = new StandardAuthenticationManagerImpl();

    @Mock
    UserLogger userLogger;

    @Mock
    HttpServletRequest request;

    HttpServletRequestAccessor requestAccessor;

    @Before
    public void setUp() throws Exception {
        when(request.getContextPath()).thenReturn("bonita");
        when(request.getParameter("tenant")).thenReturn("1");
        requestAccessor = new HttpServletRequestAccessor(request);
    }

    @Test
    public void testGetSimpleLoginpageURL() throws Exception {
        final String redirectUrl = "%2Fportal%2Fhomepage";
        final String loginURL = standardLoginManagerImpl.getLoginPageURL(requestAccessor, redirectUrl);
        assertThat(loginURL).isEqualToIgnoringCase("bonita/login.jsp?tenant=1&redirectUrl=%2Fportal%2Fhomepage");
    }

    @Test
    public void testGetLoginpageURLFromPortal() throws Exception {
        final String redirectUrl = "%2Fportal%2Fhomepage";
        when(request.getServletPath()).thenReturn("/portal/");
        final String loginURL = standardLoginManagerImpl.getLoginPageURL(requestAccessor, redirectUrl);
        assertThat(loginURL).isEqualToIgnoringCase("bonita/login.jsp?tenant=1&redirectUrl=%2Fportal%2Fhomepage");
    }

    @Test
    public void testGetLoginpageURLFromMobile() throws Exception {
        final String redirectUrl = "%2Fmobile%2F";
        when(request.getServletPath()).thenReturn("/mobile/#login");
        final String loginURL = standardLoginManagerImpl.getLoginPageURL(requestAccessor, redirectUrl);
        assertThat(loginURL).isEqualToIgnoringCase("bonita/mobile/login.jsp?tenant=1&redirectUrl=%2Fmobile%2F");
    }

}
