package org.bonitasoft.console.common.server.page;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bonitasoft.console.common.server.page.extension.PageResourceProviderImpl;
import org.bonitasoft.console.common.server.utils.BonitaHomeFolderAccessor;
import org.bonitasoft.engine.exception.NotFoundException;
import org.bonitasoft.engine.exception.UnauthorizedAccessException;
import org.bonitasoft.engine.page.Page;
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
public class PageServletTest {

    public static final long PAGE_ID = 42L;
    @Mock
    PageRenderer pageRenderer;

    @Mock
    ResourceRenderer resourceRenderer;

    @Mock
    PageMappingService pageMappingService;

    @Mock
    CustomPageService customPageService;

    @Mock
    Page page;

    @Mock
    BonitaHomeFolderAccessor bonitaHomeFolderAccessor;

    @Spy
    @InjectMocks
    PageServlet pageServlet;

    @Mock(answer = Answers.RETURNS_MOCKS)
    HttpServletRequest hsRequest;

    @Mock
    HttpServletResponse hsResponse;

    @Mock
    HttpSession httpSession;

    @Mock
    APISession apiSession;

    Locale locale;

    @Before
    public void beforeEach() throws Exception {
        when(hsRequest.getMethod()).thenReturn("GET");
        when(hsRequest.getContextPath()).thenReturn("/bonita");
        when(hsRequest.getSession()).thenReturn(httpSession);
        when(httpSession.getAttribute("apiSession")).thenReturn(apiSession);
        when(apiSession.getUserId()).thenReturn(1L);
        locale = new Locale("en");
        when(pageRenderer.getCurrentLocale(hsRequest)).thenReturn(new Locale("en"));
    }

    @Test
    public void should_get_Forbidden_Status_when_unauthorized() throws Exception {
        when(hsRequest.getPathInfo()).thenReturn("/process/processName/processVersion/content/");
        doThrow(UnauthorizedAccessException.class).when(pageMappingService).getPage(hsRequest, apiSession, "process/processName/processVersion", locale, true);

        pageServlet.service(hsRequest, hsResponse);

        verify(hsResponse, times(1)).sendError(403, "User not Authorized");
    }

    @Test
    public void should_get_Bad_Request_when_invalid_parameters() throws Exception {
        when(hsRequest.getPathInfo()).thenReturn("");
        when(hsRequest.getParameter(anyString())).thenReturn(null);
        pageServlet.service(hsRequest, hsResponse);
        verify(hsResponse, times(1)).sendError(400, "/content is expected in the URL after the page mapping key");
    }

    @Test
    public void should_display_externalPage() throws Exception {
        when(hsRequest.getPathInfo()).thenReturn("/process/processName/processVersion/content/");
        when(pageMappingService.getPage(hsRequest, apiSession, "process/processName/processVersion", locale, true)).thenReturn(
                new PageReference(null, "/externalPage"));

        pageServlet.service(hsRequest, hsResponse);

        verify(pageServlet, times(1)).displayExternalPage(hsRequest, hsResponse, "/externalPage");
        verify(hsResponse, times(1)).encodeRedirectURL("/externalPage");
        verify(hsResponse, times(1)).sendRedirect(anyString());
    }

    @Test
    public void should_display_customPage() throws Exception {
        when(hsRequest.getPathInfo()).thenReturn("/process/processName/processVersion/content/");
        when(pageMappingService.getPage(hsRequest, apiSession, "process/processName/processVersion", locale, true)).thenReturn(new PageReference(PAGE_ID, null));

        pageServlet.service(hsRequest, hsResponse);

        verify(pageRenderer, times(1)).displayCustomPage(hsRequest, hsResponse, apiSession, PAGE_ID);
    }

    @Test
    public void should_display_customPage_resource() throws Exception {
        //given
        final String pageName="custompage_name";
        when(hsRequest.getPathInfo()).thenReturn("/process/processName/processVersion/content/path/of/resource.css");
        final PageReference pageReference = new PageReference(PAGE_ID, null);
        when(pageMappingService.getPage(hsRequest, apiSession, "process/processName/processVersion", locale, false)).thenReturn(pageReference);
        when(customPageService.getPage(apiSession, PAGE_ID)).thenReturn(page);
        when(page.getName()).thenReturn(pageName);

        final PageResourceProviderImpl pageResourceProvider = mock(PageResourceProviderImpl.class);
        final File resourceFile = mock(File.class);
        when(pageResourceProvider.getResourceAsFile("resources" +File.separator + "path/of/resource.css")).thenReturn(resourceFile);
        when(pageRenderer.getPageResourceProvider(PAGE_ID, apiSession)).thenReturn(pageResourceProvider);
        when(bonitaHomeFolderAccessor.isInFolder(resourceFile, null)).thenReturn(true);

        //when
        pageServlet.service(hsRequest, hsResponse);

        //then
        verify(pageServlet, times(1)).displayPageOrResource(hsRequest, hsResponse, apiSession, PAGE_ID, "path/of/resource.css");
        verify(pageServlet, times(1)).getResourceFile(hsResponse, apiSession, PAGE_ID, "path/of/resource.css");
        verify(pageRenderer, times(1)).ensurePageFolderIsPresent(apiSession, pageResourceProvider);
        verify(resourceRenderer, times(1)).renderFile(hsRequest, hsResponse, resourceFile, apiSession);
    }

    @Test
    public void should_get_not_found_when_engine_throw_not_found() throws Exception {
        final String key = "process/processName/processVersion";
        when(hsRequest.getPathInfo()).thenReturn("/" + key + "/content/");
        doThrow(NotFoundException.class).when(pageMappingService).getPage(hsRequest, apiSession, key, locale, true);

        pageServlet.service(hsRequest, hsResponse);

        verify(hsResponse, times(1)).sendError(404, "Form mapping not found");
    }

    @Test
    public void should_not_get_not_found_when_empty_mapping() throws Exception {
        when(hsRequest.getPathInfo()).thenReturn("/process/processName/processVersion/content/");
        final PageReference pageReference = new PageReference(null, null);
        doReturn(pageReference).when(pageMappingService).getPage(hsRequest, apiSession, "process/processName/processVersion", locale, true);

        pageServlet.service(hsRequest, hsResponse);

        verify(hsResponse, never()).sendError(anyInt(), anyString());
    }

    @Test
    public void should_get_not_found_if_the_page_does_not_exist() throws Exception {
        final String key = "process/processName/processVersion";
        when(hsRequest.getPathInfo()).thenReturn("/" + key + "/content/");
        when(pageMappingService.getPage(hsRequest, apiSession, key, locale, true)).thenReturn(new PageReference(PAGE_ID, null));
        doThrow(PageNotFoundException.class).when(pageRenderer).displayCustomPage(hsRequest, hsResponse, apiSession, PAGE_ID);

        pageServlet.service(hsRequest, hsResponse);

        verify(hsResponse, times(1)).sendError(404, "Page not found");
    }

    @Test
    public void should_redirect_for_missing_slash() throws Exception {
        when(hsRequest.getPathInfo()).thenReturn("/process/processName/processVersion/content");
        when(hsRequest.getContextPath()).thenReturn("/bonita");
        when(hsRequest.getServletPath()).thenReturn("/portal/resource");

        pageServlet.service(hsRequest, hsResponse);

        verify(hsResponse, times(1)).encodeRedirectURL("/bonita/portal/resource/process/processName/processVersion/content/");
        verify(hsResponse, times(1)).sendRedirect(anyString());
    }

    @Test
    public void should_get_server_error_when_issue_with_customPage() throws Exception {
        when(hsRequest.getPathInfo()).thenReturn("/process/processName/processVersion/content/");
        when(pageMappingService.getPage(hsRequest, apiSession, "process/processName/processVersion", locale, true)).thenReturn(new PageReference(PAGE_ID, null));
        final InstantiationException instantiationException = new InstantiationException("instatiation exception");
        doThrow(instantiationException).when(pageRenderer).displayCustomPage(hsRequest, hsResponse, apiSession, PAGE_ID);

        pageServlet.service(hsRequest, hsResponse);

        verify(pageServlet, times(1)).handleException(hsResponse, "process/processName/processVersion", instantiationException);
        verify(hsResponse, times(1)).sendError(500, "instatiation exception");
    }

    @Test
    public void should_get_bad_request_when_issue_with_parameters() throws Exception {
        when(hsRequest.getPathInfo()).thenReturn("/process/processName/processVersion/content/");
        when(pageMappingService.getPage(hsRequest, apiSession, "process/processName/processVersion", locale, true)).thenReturn(new PageReference(PAGE_ID, null));
        final IllegalArgumentException illegalArgumentException = new IllegalArgumentException();
        doThrow(illegalArgumentException).when(pageMappingService).getPage(hsRequest, apiSession, "process/processName/processVersion", locale, true);

        pageServlet.service(hsRequest, hsResponse);

        verify(pageServlet, times(1)).handleException(hsResponse, "process/processName/processVersion", illegalArgumentException);
        verify(hsResponse, times(1)).sendError(400);
    }

    @Test
    public void should_forward_when_API_call() throws Exception {
        when(hsRequest.getPathInfo()).thenReturn("/process/processName/processVersion/API/bpm/process/1");

        pageServlet.service(hsRequest, hsResponse);

        verify(hsRequest, times(1)).getRequestDispatcher("/API/bpm/process/1");
    }

    @Test
    public void should_return_200_when_THEME_call() throws Exception {
        when(hsRequest.getPathInfo()).thenReturn("/resource/process/Test/1.0/theme/theme.css");

        pageServlet.service(hsRequest, hsResponse);

        verify(hsRequest, never()).getRequestDispatcher(anyString());
    }
}