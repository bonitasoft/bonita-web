package org.bonitasoft.console.common.server.form;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bonitasoft.console.common.server.page.PageRenderer;
import org.bonitasoft.engine.page.PageNotFoundException;
import org.bonitasoft.engine.session.APISession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ProcessFormServletTest {

    @Mock
    PageRenderer pageRenderer;

    @Spy
    @InjectMocks
    ProcessFormServlet servlet;

    @Mock(answer = Answers.RETURNS_MOCKS)
    HttpServletRequest hsRequest;

    @Mock
    HttpServletResponse hsResponse;

    @Mock
    HttpSession httpSession;

    @Mock
    APISession apiSession;

    @Before
    public void beforeEach() throws Exception {
        when(hsRequest.getContextPath()).thenReturn("/bonita");
        when(hsRequest.getSession()).thenReturn(httpSession);
        when(httpSession.getAttribute("apiSession")).thenReturn(apiSession);
    }

    @Test
    public void should_get_Forbidden_Status_when_unauthorized() throws Exception {
        when(hsRequest.getParameter("process")).thenReturn("processName");
        when(hsRequest.getParameter("version")).thenReturn("processVersion");
        when(servlet.isAuthorized(any(APISession.class), anyString(), anyString(), anyString(), anyString())).thenReturn(false);

        servlet.doGet(hsRequest, hsResponse);

        verify(hsResponse, times(1)).sendError(403, "User not Authorized");
    }

    @Test
    public void should_get_Bad_Request_when_invalid_parameters() throws Exception {
        when(hsRequest.getParameter("process")).thenReturn(null);

        servlet.doGet(hsRequest, hsResponse);

        verify(hsResponse, times(1)).sendError(400, "Missing process name and/or version");
    }

    @Test
    public void should_display_externalPage() throws Exception {
        when(hsRequest.getParameter("process")).thenReturn("processName");
        when(hsRequest.getParameter("version")).thenReturn("processVersion");
        when(servlet.getForm(any(APISession.class), anyString(), anyString(), anyString(), anyBoolean())).thenReturn("/externalPage");
        when(servlet.isAuthorized(any(APISession.class), anyString(), anyString(), anyString(), anyString())).thenReturn(true);
        doThrow(PageNotFoundException.class).when(pageRenderer).displayCustomPage(hsRequest, hsResponse, apiSession, "/externalPage");

        servlet.doGet(hsRequest, hsResponse);

        verify(servlet, times(1)).displayExternalPage(hsRequest, hsResponse, "/externalPage");
    }

    @Test
    public void should_display_customPage() throws Exception {
        when(hsRequest.getParameter("process")).thenReturn("processName");
        when(hsRequest.getParameter("version")).thenReturn("processVersion");
        when(servlet.getForm(any(APISession.class), anyString(), anyString(), anyString(), anyBoolean())).thenReturn("custompage_form");
        when(servlet.isAuthorized(any(APISession.class), anyString(), anyString(), anyString(), anyString())).thenReturn(true);

        servlet.doGet(hsRequest, hsResponse);

        verify(pageRenderer, times(1)).displayCustomPage(hsRequest, hsResponse, apiSession, "custompage_form");
    }
}