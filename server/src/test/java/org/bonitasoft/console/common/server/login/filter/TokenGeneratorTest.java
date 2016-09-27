package org.bonitasoft.console.common.server.login.filter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.RestoreSystemProperties;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

@RunWith(MockitoJUnitRunner.class)
public class TokenGeneratorTest {

    private final HttpSession session = new MockHttpSession();
    private final MockHttpServletResponse response = new MockHttpServletResponse();

    final String contextPath = "bonitaTest";
    final String bonitaTokenName = "X-Bonita-API-Token";
    final String bonitaTokenValue = "sdfsdfjhvèzv";

    @Spy
    TokenGenerator tokenGenerator = new TokenGenerator();

    @Rule
    public final RestoreSystemProperties restoreSystemProperties = new RestoreSystemProperties();

    @Test
    public void should_create_token_and_store_it_in_session() throws Exception {
        final String token = tokenGenerator.createOrLoadToken(session);
        assertThat(token).isNotEmpty();
        assertThat(session.getAttribute(TokenGenerator.API_TOKEN)).isEqualTo(token);
    }

    @Test
    public void should_load_token_from_session() throws Exception {
        session.setAttribute(TokenGenerator.API_TOKEN, bonitaTokenValue);
        final String token = tokenGenerator.createOrLoadToken(session);
        assertThat(token).isNotEmpty();
        assertThat(token).isEqualTo(bonitaTokenValue);
    }

    @Test
    public void testSetTokenToResponseCookie() throws Exception {
        tokenGenerator.setTokenToResponseCookie(contextPath, response, bonitaTokenValue);

        final Cookie csrfCookie = response.getCookie(bonitaTokenName);

        assertThat(csrfCookie.getName()).isEqualTo(bonitaTokenName);
        assertThat(csrfCookie.getPath()).isEqualTo(contextPath);
        assertThat(csrfCookie.getValue()).isEqualTo(bonitaTokenValue);
        assertThat(csrfCookie.getSecure()).isFalse();
    }

    @Test
    public void should_set_csrf_token_cookie_path_specified_via_system_property() throws Exception {
        System.setProperty("bonita.csrf.cookie.path", "/");

        tokenGenerator.setTokenToResponseCookie("somepath", response, "sdfsdfjhvèzv");

        Cookie csrfCookie = response.getCookie(bonitaTokenName);
        assertThat(csrfCookie.getPath()).isEqualTo("/");
    }
    
    @Test
    public void should_set_secure_csrf_token_cookie_when_specified() throws Exception {
        doReturn(true).when(tokenGenerator).isCSRFTokenCookieSecure();

        tokenGenerator.setTokenToResponseCookie(contextPath, response, bonitaTokenValue);

        Cookie csrfCookie = response.getCookie(bonitaTokenName);
        assertThat(csrfCookie.getName()).isEqualTo(bonitaTokenName);
        assertThat(csrfCookie.getPath()).isEqualTo(contextPath);
        assertThat(csrfCookie.getValue()).isEqualTo(bonitaTokenValue);
        assertThat(csrfCookie.getSecure()).isTrue();
    }

    @Test
    public void testSetTokenToResponseHeader() throws Exception {
        tokenGenerator.setTokenToResponseHeader(response, bonitaTokenValue);

        assertThat(response.getHeader(bonitaTokenName)).isEqualTo(bonitaTokenValue);
    }

}
