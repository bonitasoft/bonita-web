package org.bonitasoft.console.common.server.auth.impl.standard;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import javax.servlet.http.Cookie;

import org.bonitasoft.console.common.server.login.HttpServletRequestAccessor;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

public class StandardAuthenticationManagerImplTest {

    private MockHttpServletRequest request;
    private HttpServletRequestAccessor requestAccessor;

    private StandardAuthenticationManagerImpl standardLoginManagerImpl = new StandardAuthenticationManagerImpl();


    @Before
    public void setUp() throws Exception {
        request = new MockHttpServletRequest();
        request.setContextPath("bonita");
        request.setParameter("tenant", "1");

        requestAccessor = new HttpServletRequestAccessor(request);
    }

    @Test
    public void testGetSimpleLoginpageURL() throws Exception {
        String redirectUrl = "%2Fportal%2Fhomepage";

        String loginURL = standardLoginManagerImpl.getLoginPageURL(requestAccessor, redirectUrl);

        assertThat(loginURL).isEqualToIgnoringCase("bonita/login.jsp?tenant=1&redirectUrl=%2Fportal%2Fhomepage");
    }

    @Test
    public void testGetLoginpageURLFromPortal() throws Exception {
        String redirectUrl = "%2Fportal%2Fhomepage";
        request.setServletPath("/portal/");

        String loginURL = standardLoginManagerImpl.getLoginPageURL(requestAccessor, redirectUrl);

        assertThat(loginURL).isEqualToIgnoringCase("bonita/login.jsp?tenant=1&redirectUrl=%2Fportal%2Fhomepage");
    }

    @Test
    public void testGetLoginpageURLFromMobile() throws Exception {
        String redirectUrl = "%2Fmobile%2F";
        request.setServletPath("/mobile/#login");

        String loginURL = standardLoginManagerImpl.getLoginPageURL(requestAccessor, redirectUrl);

        assertThat(loginURL).isEqualToIgnoringCase("bonita/mobile/login.jsp?tenant=1&redirectUrl=%2Fmobile%2F");
    }

    @Test
    public void should_add_tenant_parameter_contained_in_request_params() throws Exception {
        request.setParameter("tenant", "4");
        request.setCookies(new Cookie("bonita.tenant", "123"));
        String redirectUrl = "%2Fportal%2Fhomepage";

        String loginURL = standardLoginManagerImpl.getLoginPageURL(requestAccessor, redirectUrl);

        assertThat(loginURL).isEqualToIgnoringCase("bonita/login.jsp?tenant=4&redirectUrl=%2Fportal%2Fhomepage");
    }

    @Test
    public void should_add_tenant_parameter_from_cookie_if_not_in_request() throws Exception {
        request.removeAllParameters();
        request.setCookies(new Cookie("bonita.tenant", "123"));
        String redirectUrl = "%2Fportal%2Fhomepage";

        String loginURL = standardLoginManagerImpl.getLoginPageURL(requestAccessor, redirectUrl);

        assertThat(loginURL).isEqualToIgnoringCase("bonita/login.jsp?tenant=123&redirectUrl=%2Fportal%2Fhomepage");
    }
}
