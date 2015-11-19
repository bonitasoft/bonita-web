/*******************************************************************************
 * Copyright (C) 2015 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation
 * version 2.1 of the License.
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth
 * Floor, Boston, MA 02110-1301, USA.
 ******************************************************************************/

package org.bonitasoft.livingapps;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.bonitasoft.console.common.server.page.CustomPageAuthorizationsHelper;
import org.bonitasoft.console.common.server.page.CustomPageRequestModifier;
import org.bonitasoft.console.common.server.page.PageRenderer;
import org.bonitasoft.console.common.server.page.ResourceRenderer;
import org.bonitasoft.console.common.server.page.extension.PageResourceProviderImpl;
import org.bonitasoft.console.common.server.utils.BonitaHomeFolderAccessor;
import org.bonitasoft.engine.api.ApplicationAPI;
import org.bonitasoft.engine.api.PageAPI;
import org.bonitasoft.engine.business.application.ApplicationPage;
import org.bonitasoft.engine.page.Page;
import org.bonitasoft.engine.session.APISession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

@RunWith(MockitoJUnitRunner.class)
public class LivingApplicationPageServletTest {

    MockHttpServletRequest hsRequest = new MockHttpServletRequest();

    @Mock
    MockHttpServletResponse hsResponse = new MockHttpServletResponse();

    @Mock
    HttpSession httpSession;

    @Mock
    APISession apiSession;

    @Mock
    CustomPageAuthorizationsHelper customPageAuthorizationsHelper;

    @Mock
    PageRenderer pageRenderer;

    @Mock
    ResourceRenderer resourceRenderer;

    @Mock
    PageResourceProviderImpl pageResourceProvider;

    @Mock
    BonitaHomeFolderAccessor bonitaHomeFolderAccessor;

    @Mock
    CustomPageRequestModifier customPageRequestModifier;

    @Mock
    ApplicationAPI applicationAPI;

    @Mock
    Page page;

    @Mock
    ApplicationPage applicationPage;

    @Mock
    PageAPI pageAPI;

    @Spy
    @InjectMocks
    LivingApplicationPageServlet servlet;


    @Before
    public void beforeEach() throws Exception {
        hsRequest.setSession(httpSession);
        hsRequest.setMethod("GET");
        doReturn(apiSession).when(httpSession).getAttribute("apiSession");
        doReturn(applicationAPI).when(servlet).getApplicationApi(apiSession);
        doReturn(pageAPI).when(servlet).getPageApi(apiSession);
        doReturn(1L).when(apiSession).getTenantId();
        doReturn(customPageAuthorizationsHelper).when(servlet).getCustomPageAuthorizationsHelper(apiSession);
    }

    @Test
    public void should_get_Forbidden_Status_when_page_unAuthorize() throws Exception {
        hsRequest.setPathInfo("/AppToken/pageToken/content/");
        given(resourceRenderer.getPathSegments("/AppToken/pageToken/content/")).willReturn(Arrays.asList("AppToken", "pageToken", "content"));
        given(customPageAuthorizationsHelper.isPageAuthorized("AppToken", "customPageName")).willReturn(false);

        doReturn(applicationPage).when(applicationAPI).getApplicationPage("AppToken","pageToken");
        doReturn(2L).when(applicationPage).getPageId();
        doReturn(page).when(pageAPI).getPage(2L);
        doReturn("customPageName").when(page).getName();

        servlet.service(hsRequest, hsResponse);

        verify(hsResponse).sendError(403 ,"User not Authorized");
    }

    @Test
    public void should_get_badRequest_Status_when_page_name_is_not_set() throws Exception {
        hsRequest.setPathInfo("/AppToken/content/");

        servlet.service(hsRequest, hsResponse);

        verify(hsResponse).sendError(400, "The info path is suppose to contain the application token, the page token and one of the separator '/content', '/theme' or '/API'.");
    }

    @Test
    public void should_redirect_to_valide_url_on_missing_slash() throws Exception {
        hsRequest.setPathInfo("/AppToken/pageToken/content");

        servlet.service(hsRequest, hsResponse);

        verify(customPageRequestModifier).redirectToValidPageUrl(hsRequest, hsResponse);
    }

    @Test
    public void getPage_should_call_the_page_renderer() throws Exception {
        testPageIsWellCalled("AppToken", "htmlexample1", "/AppToken/htmlexample1/content/", Arrays.asList("AppToken","htmlexample1","content"));
        testPageIsWellCalled("AppToken", "htmlexample2", "/AppToken/htmlexample2/content/index", Arrays.asList("AppToken","htmlexample2","content","index"));
        testPageIsWellCalled("AppToken", "htmlexample4", "/AppToken/htmlexample4/content/index.html", Arrays.asList("AppToken","htmlexample4","content","index.html"));
        testPageIsWellCalled("AppToken", "htmlexample5", "/AppToken/htmlexample5/content/Index.groovy", Arrays.asList("AppToken","htmlexample5","content","Index.groovy"));
    }

    private void testPageIsWellCalled(final String appToken, final String pageToken, final String path, final List<String> pathSegment) throws Exception {
        hsRequest.setPathInfo(path);
        given(resourceRenderer.getPathSegments(path)).willReturn(pathSegment);
        given(customPageAuthorizationsHelper.isPageAuthorized(appToken, "customPage_"+pageToken)).willReturn(true);

        doReturn(applicationPage).when(applicationAPI).getApplicationPage(appToken,pageToken);
        doReturn(2L).when(applicationPage).getPageId();
        doReturn(page).when(pageAPI).getPage(2L);
        doReturn("customPage_"+pageToken).when(page).getName();

        servlet.service(hsRequest, hsResponse);

        verify(pageRenderer, times(1)).displayCustomPage(hsRequest, hsResponse, apiSession, "customPage_"+pageToken);
    }

    @Test
    public void getResource_should_call_the_resource_renderer() throws Exception {
        hsRequest.setPathInfo("/AppToken/htmlexample/content/css/file.css");
        final File pageDir = new File("/pageDir");
        given(resourceRenderer.getPathSegments("/AppToken/htmlexample/content/css/file.css")).willReturn(Arrays.asList("AppToken", "htmlexample", "content", "css", "file.css"));
        final String pageName = "customPage_htmlexample";
        doReturn(pageResourceProvider).when(pageRenderer).getPageResourceProvider(pageName, 1L);
        doReturn(pageDir).when(pageResourceProvider).getPageDirectory();
        doReturn(true).when(bonitaHomeFolderAccessor).isInFolder(any(File.class), any(File.class));

        doReturn(applicationPage).when(applicationAPI).getApplicationPage("AppToken","htmlexample");
        doReturn(2L).when(applicationPage).getPageId();
        doReturn(page).when(pageAPI).getPage(2L);
        doReturn(pageName).when(page).getName();

        servlet.service(hsRequest, hsResponse);

        verify(pageRenderer, times(1)).ensurePageFolderIsPresent(apiSession, pageResourceProvider);
        verify(resourceRenderer, times(1)).renderFile(hsRequest, hsResponse,
                new File(pageDir, File.separator + "resources" + File.separator + "css" + File.separator + "file.css"), apiSession);
    }

    @Test(expected=ServletException.class)
    public void getResource_should_throw_exception_if_unauthorised() throws Exception {
        hsRequest.setPathInfo("/AppToken/htmlexample/content/css/../../../file.css");
        final File pageDir = new File(".");
        given(resourceRenderer.getPathSegments("/AppToken/htmlexample/content/css/../../../file.css")).willReturn(
                Arrays.asList("AppToken","htmlexample","content", "css", "..", "..", "..", "file.css"));
        doReturn(pageResourceProvider).when(pageRenderer).getPageResourceProvider("htmlexample", 1L);
        given(pageResourceProvider.getPageDirectory()).willReturn(pageDir);
        doReturn(false).when(bonitaHomeFolderAccessor).isInFolder(any(File.class), any(File.class));

        doReturn(applicationPage).when(applicationAPI).getApplicationPage("AppToken","htmlexample");
        doReturn(2L).when(applicationPage).getPageId();
        doReturn(page).when(pageAPI).getPage(2L);
        doReturn("customPage_"+"htmlexample").when(page).getName();

        servlet.service(hsRequest, hsResponse);
    }

    @Test
    public void should_forward_when_API_call() throws Exception {
        hsRequest.setPathInfo("/AppToken/htmlexample/API/bpm/process/1");
        given(resourceRenderer.getPathSegments("/AppToken/htmlexample/API/bpm/process/1")).willReturn(
                Arrays.asList("AppToken", "htmlexample", "API", "bpm", "process", "1"));

        servlet.service(hsRequest, hsResponse);

        verify(servlet, never()).doGet(hsRequest, hsResponse);
    }

}