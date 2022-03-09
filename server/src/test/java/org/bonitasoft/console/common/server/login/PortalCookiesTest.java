package org.bonitasoft.console.common.server.login;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PortalCookiesTest {
    
    @Mock
    HttpServletRequest request;
    
    @Mock
    HttpServletResponse response;
    
    private PortalCookies portalCookies = spy(new PortalCookies());

    @Test
    public void should_add_tenant_cookie_to_response() throws Exception {

        portalCookies.addTenantCookieToResponse(response, 123L);
        
        ArgumentCaptor<Cookie> argument = ArgumentCaptor.forClass(Cookie.class);
        verify(response).addCookie(argument.capture());
        assertThat(argument.getValue().getValue()).isEqualTo("123");
    }

    @Test
    public void should_get_tenant_cookie_from_request() throws Exception {
        
        doReturn(new Cookie[]{new Cookie("bonita.tenant", "123")}).when(request).getCookies();

        String tenant = portalCookies.getTenantCookieFromRequest(request);

        assertThat(tenant).isEqualTo("123");
    }

    @Test
    public void should_get_cookie_by_name_from_request() throws Exception {
        
        Cookie cookie = new Cookie("bonita.tenant", "123");
        doReturn(new Cookie[]{cookie}).when(request).getCookies();

        Cookie fetchedCookie = portalCookies.getCookie(request, "bonita.tenant");

        assertThat(fetchedCookie).isEqualTo(cookie);
    }
    
    @Test
    public void should_add_csrf_cookie_to_response_with_context_path() throws Exception {
        
        doReturn(true).when(portalCookies).isCSRFTokenCookieSecure();
        doReturn("/bonita").when(request).getContextPath();

        portalCookies.addCSRFTokenCookieToResponse(request, response, "apiTokenFromClient");

        ArgumentCaptor<Cookie> argument = ArgumentCaptor.forClass(Cookie.class);
        verify(response).addCookie(argument.capture());
        assertThat(argument.getValue().getValue()).isEqualTo("apiTokenFromClient");
        assertThat(argument.getValue().getPath()).isEqualTo("/bonita");
        assertThat(argument.getValue().getSecure()).isTrue();
    }
    
    @Test
    public void should_add_csrf_cookie_to_response_with_empty_context_path() throws Exception {

        doReturn(false).when(portalCookies).isCSRFTokenCookieSecure();
        doReturn("").when(request).getContextPath();
        
        portalCookies.addCSRFTokenCookieToResponse(request, response, "apiTokenFromClient");

        ArgumentCaptor<Cookie> argument = ArgumentCaptor.forClass(Cookie.class);
        verify(response).addCookie(argument.capture());
        assertThat(argument.getValue().getValue()).isEqualTo("apiTokenFromClient");
        assertThat(argument.getValue().getPath()).isEqualTo("/");
        assertThat(argument.getValue().getSecure()).isFalse();
    }
    
    @Test
    public void should_add_csrf_cookie_to_response_with_specified_path() throws Exception {

        try {
            System.setProperty("bonita.csrf.cookie.path", "/");
            doReturn(false).when(portalCookies).isCSRFTokenCookieSecure();
            doReturn("/bonita").when(request).getContextPath();
            
            portalCookies.addCSRFTokenCookieToResponse(request, response, "apiTokenFromClient");
    
            ArgumentCaptor<Cookie> argument = ArgumentCaptor.forClass(Cookie.class);
            verify(response).addCookie(argument.capture());
            assertThat(argument.getValue().getValue()).isEqualTo("apiTokenFromClient");
            assertThat(argument.getValue().getPath()).isEqualTo("/");
            assertThat(argument.getValue().getSecure()).isFalse();
        } finally {
            System.clearProperty("bonita.csrf.cookie.path");
        }
    }
}
