package org.bonitasoft.console.common.server.login.filter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TokenGeneratorFilterTest {

    @Mock
    private FilterChain filterChain;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;

    final String contextPath = "bonitaTest";
    final String bonitaTokenName = "X-Bonita-API-Token";
    final String bonitaTokenValue = "sdfsdfjhvèzv";

    private final TokenGeneratorFilter tokenGeneratorFilter = new TokenGeneratorFilter();

    @Before
    public void setUp() {
        when(request.getSession()).thenReturn(session);
        when(request.getContextPath()).thenReturn(contextPath);
    }

    @Test
    public void should_add_security_token_in_headers_and_in_cookies_when_token_already_exists() throws Exception {
        when(session.getAttribute(TokenGeneratorFilter.API_TOKEN)).thenReturn(bonitaTokenValue);
        when(response.containsHeader(bonitaTokenName)).thenReturn(true);

        tokenGeneratorFilter.doFilter(request, response, filterChain);

        final ArgumentCaptor<Cookie> cookieCaptor = ArgumentCaptor.forClass(Cookie.class);
        verify(response).addCookie(cookieCaptor.capture());
        final Cookie csrfCookie = cookieCaptor.getValue();

        assertThat(csrfCookie.getName()).isEqualTo(bonitaTokenName);
        assertThat(csrfCookie.getPath()).isEqualTo(contextPath);
        assertThat(csrfCookie.getValue()).isEqualTo(bonitaTokenValue);

        verify(session, never()).setAttribute(anyString(), anyObject());
        verify(response).setHeader(bonitaTokenName, bonitaTokenValue);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    public void should_add_security_token_in_headers_and_in_cookies_when_no_previous_call() throws Exception {
        tokenGeneratorFilter.doFilter(request, response, filterChain);

        final ArgumentCaptor<Cookie> cookieCaptor = ArgumentCaptor.forClass(Cookie.class);
        verify(response).addCookie(cookieCaptor.capture());
        final Cookie csrfCookie = cookieCaptor.getValue();

        assertThat(csrfCookie.getName()).isEqualTo(bonitaTokenName);
        assertThat(csrfCookie.getPath()).isEqualTo(contextPath);
        assertThat(csrfCookie.getValue()).isNotEqualTo(bonitaTokenValue);

        verify(session).setAttribute(TokenGeneratorFilter.API_TOKEN, cookieCaptor.getValue().getValue());
        verify(response).addHeader(bonitaTokenName, cookieCaptor.getValue().getValue());

        verify(filterChain).doFilter(request, response);
    }
}