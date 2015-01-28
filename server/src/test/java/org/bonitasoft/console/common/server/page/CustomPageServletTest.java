package org.bonitasoft.console.common.server.page;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bonitasoft.engine.session.APISession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CustomPageServletTest {

    @Spy
    CustomPageServlet servlet;

    @Mock(answer = Answers.RETURNS_MOCKS)
    HttpServletRequest hsRequest;

    @Mock
    HttpServletResponse hsResponse;

    @Mock
    HttpSession httpSession;

    @Mock
    APISession apiSession;

    @Mock
    CustomPageAuthorizationsHelper customPageAuthorizationsHelper;

    @Before
    public void beforeEach() throws Exception {
        given(hsRequest.getContextPath()).willReturn("/bonita");
        given(hsRequest.getSession()).willReturn(httpSession);
        given(httpSession.getAttribute("apiSession")).willReturn(apiSession);
        doReturn(customPageAuthorizationsHelper).when(servlet).getCustomPageAuthorizationsHelper(apiSession);
    }

    @Test
    public void should_get_IllegalAccessException_when_page_unAuthorize() throws Exception {
        given(hsRequest.getParameter("page")).willReturn("pageToken");
        given(hsRequest.getParameter("applicationId")).willReturn("1");
        given(customPageAuthorizationsHelper.isPageAuthorized("1", "pageToken")).willReturn(false);

        try {
            servlet.doGet(hsRequest, hsResponse);
            fail("Expected ServletException exception with User not Authorized message.");
        } catch (final ServletException e) {
            assertThat(e).hasMessage("User not Authorized");
        }

    }
}