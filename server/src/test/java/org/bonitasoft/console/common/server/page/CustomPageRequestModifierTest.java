package org.bonitasoft.console.common.server.page;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CustomPageRequestModifierTest {
    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;


    @Test
    public void redirect_with_trailing_slash_should_not_encode_parameter() throws Exception {
        when(request.getContextPath()).thenReturn("bonita/");
        when(request.getServletPath()).thenReturn("apps/");
        when(request.getPathInfo()).thenReturn("myapp/mypage");
        when(request.getQueryString()).thenReturn("time=12:00");
        when(response.encodeRedirectURL("bonita/apps/myapp/mypage/?time=12:00")).thenReturn("bonita/apps/myapp/mypage/?time=12:00");

        CustomPageRequestModifier customPageRequestModifier = new CustomPageRequestModifier();
        customPageRequestModifier.redirectToValidPageUrl(request, response);

        verify(response).sendRedirect("bonita/apps/myapp/mypage/?time=12:00");
    }

    @Test
    public void redirect_with_trailing_slash_should_not_add_question_mark() throws Exception {
        when(request.getContextPath()).thenReturn("bonita/");
        when(request.getServletPath()).thenReturn("apps/");
        when(request.getPathInfo()).thenReturn("myapp/mypage");
        when(response.encodeRedirectURL("bonita/apps/myapp/mypage/")).thenReturn("bonita/apps/myapp/mypage/");

        CustomPageRequestModifier customPageRequestModifier = new CustomPageRequestModifier();
        customPageRequestModifier.redirectToValidPageUrl(request, response);

        verify(response).sendRedirect("bonita/apps/myapp/mypage/");
    }

}