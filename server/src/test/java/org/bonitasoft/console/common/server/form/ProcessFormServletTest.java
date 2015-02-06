package org.bonitasoft.console.common.server.form;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyLong;
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

    @Mock
    ProcessFormService processFormService;

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
        when(hsRequest.getParameter("task")).thenReturn(null);
        when(hsRequest.getParameter("taskInstance")).thenReturn(null);
        when(hsRequest.getParameter("processInstance")).thenReturn(null);
        when(hsRequest.getParameter("taskInstance")).thenReturn(null);
        when(hsRequest.getParameter("user")).thenReturn(null);
        when(processFormService.getProcessDefinitionID(apiSession, "process", "version")).thenReturn(1L);
        when(processFormService.getTaskInstanceID(apiSession, -1L, null, -1L, -1L)).thenReturn(-1L);
        when(servlet.isAuthorized(any(APISession.class), anyLong(), anyLong(), anyLong(), anyLong())).thenReturn(false);

        servlet.doGet(hsRequest, hsResponse);

        verify(hsResponse, times(1)).sendError(403, "User not Authorized");
    }

    @Test
    public void should_get_Bad_Request_when_invalid_parameters() throws Exception {
        when(hsRequest.getParameter(anyString())).thenReturn(null);
        when(processFormService.getProcessDefinitionID(apiSession, null, null)).thenReturn(-1L);
        when(processFormService.getTaskInstanceID(apiSession, -1L, null, -1L, -1L)).thenReturn(-1L);
        servlet.doGet(hsRequest, hsResponse);
        verify(hsResponse, times(1)).sendError(400,
                "Either process and version parameters are required or processInstance (with or without task) or taskInstance.");
    }

    @Test
    public void should_display_externalPage() throws Exception {
        when(hsRequest.getParameter("process")).thenReturn("processName");
        when(hsRequest.getParameter("version")).thenReturn("processVersion");
        when(hsRequest.getParameter("task")).thenReturn(null);
        when(processFormService.getProcessDefinitionID(apiSession, "processName", "processVersion")).thenReturn(1L);
        when(processFormService.getTaskInstanceID(apiSession, -1L, null, -1L, -1L)).thenReturn(-1L);
        when(processFormService.getForm(any(APISession.class), anyLong(), anyString(), anyBoolean())).thenReturn(new FormReference("/externalPage", true));
        when(servlet.isAuthorized(any(APISession.class), anyLong(), anyLong(), anyLong(), anyLong())).thenReturn(true);
        doThrow(PageNotFoundException.class).when(pageRenderer).displayCustomPage(hsRequest, hsResponse, apiSession, "/externalPage");

        servlet.doGet(hsRequest, hsResponse);

        verify(servlet, times(1)).displayExternalPage(hsRequest, hsResponse, "/externalPage");
    }

    @Test
    public void should_display_customPage() throws Exception {
        when(hsRequest.getParameter("process")).thenReturn("processName");
        when(hsRequest.getParameter("version")).thenReturn("processVersion");
        when(hsRequest.getParameter("task")).thenReturn(null);
        when(processFormService.getProcessDefinitionID(apiSession, "processName", "processVersion")).thenReturn(1L);
        when(processFormService.getTaskInstanceID(apiSession, -1L, null, -1L, -1L)).thenReturn(-1L);
        when(processFormService.getForm(any(APISession.class), anyLong(), anyString(), anyBoolean())).thenReturn(new FormReference("custompage_form", false));
        when(servlet.isAuthorized(any(APISession.class), anyLong(), anyLong(), anyLong(), anyLong())).thenReturn(true);

        servlet.doGet(hsRequest, hsResponse);

        verify(pageRenderer, times(1)).displayCustomPage(hsRequest, hsResponse, apiSession, "custompage_form");
    }

    @Test
    public void should_display_lecgacyForm() throws Exception {
        when(hsRequest.getParameter("process")).thenReturn("processName");
        when(hsRequest.getParameter("version")).thenReturn("processVersion");
        when(hsRequest.getParameter("task")).thenReturn(null);
        when(processFormService.getProcessDefinitionID(apiSession, "processName", "processVersion")).thenReturn(1L);
        when(processFormService.getTaskInstanceID(apiSession, -1L, null, -1L, -1L)).thenReturn(-1L);
        when(processFormService.getForm(any(APISession.class), anyLong(), anyString(), anyBoolean())).thenReturn(new FormReference(null, false));
        when(servlet.isAuthorized(any(APISession.class), anyLong(), anyLong(), anyLong(), anyLong())).thenReturn(true);

        servlet.doGet(hsRequest, hsResponse);

        verify(servlet, times(1)).displayLegacyForm(hsRequest, hsResponse, 1L, -1L, -1L);
    }
}