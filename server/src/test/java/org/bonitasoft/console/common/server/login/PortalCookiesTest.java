package org.bonitasoft.console.common.server.login;

import static org.assertj.core.api.Assertions.assertThat;

import javax.servlet.http.Cookie;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class PortalCookiesTest {

    private MockHttpServletRequest request = new MockHttpServletRequest();
    
    private PortalCookies portalCookies = new PortalCookies();

    @Test
    public void should_add_tenant_cookie_to_response() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();

        portalCookies.addTenantCookieToResponse(response, 123L);

        assertThat(response.getCookie("bonita.tenant").getValue()).isEqualTo("123");
    }

    @Test
    public void should_get_tenant_cookie_from_request() throws Exception {
        request.setCookies(new Cookie("bonita.tenant", "123"));

        String tenant = portalCookies.getTenantCookieFromRequest(request);

        assertThat(tenant).isEqualTo("123");
    }

    @Test
    public void should_get_cookie_by_name_from_request() throws Exception {
        Cookie cookie = new Cookie("bonita.tenant", "123");
        request.setCookies(cookie);

        Cookie fetchedCookie = portalCookies.getCookie(request, "bonita.tenant");

        assertThat(fetchedCookie).isEqualTo(cookie);
    }
}
