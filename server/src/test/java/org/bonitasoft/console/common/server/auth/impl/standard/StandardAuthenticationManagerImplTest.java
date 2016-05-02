package org.bonitasoft.console.common.server.auth.impl.standard;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import javax.servlet.http.Cookie;

import org.bonitasoft.console.common.server.login.HttpServletRequestAccessor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;

@RunWith(MockitoJUnitRunner.class)
public class StandardAuthenticationManagerImplTest {

    private MockHttpServletRequest request;
    private HttpServletRequestAccessor requestAccessor;

    private StandardAuthenticationManagerImpl standardLoginManagerImpl = spy(new StandardAuthenticationManagerImpl());


    @Before
    public void setUp() throws Exception {
        request = new MockHttpServletRequest();
        request.setContextPath("bonita");
        request.setParameter("tenant", "1");
        doReturn(2L).when(standardLoginManagerImpl).getDefaultTenantId();

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

    @Test
    public void should_not_add_tenantid_when_it_is_default_one() throws Exception {
        when(standardLoginManagerImpl.getDefaultTenantId()).thenReturn(123L);
        request.setParameter("tenant", "123");

        String loginURL = standardLoginManagerImpl.getLoginPageURL(requestAccessor, "%2Fportal%2Fhomepage");

        assertThat(loginURL).isEqualToIgnoringCase("bonita/login.jsp?redirectUrl=%2Fportal%2Fhomepage");
    }
}
