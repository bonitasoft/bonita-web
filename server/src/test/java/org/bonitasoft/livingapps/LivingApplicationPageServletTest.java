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
import org.bonitasoft.console.common.server.page.PageResourceProvider;
import org.bonitasoft.console.common.server.page.ResourceRenderer;
import org.bonitasoft.console.common.server.utils.BonitaHomeFolderAccessor;
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
    PageResourceProvider pageResourceProvider;

    @Mock
    BonitaHomeFolderAccessor bonitaHomeFolderAccessor;

    @Mock
    CustomPageRequestModifier customPageRequestModifier;

    @Spy
    @InjectMocks
    LivingApplicationPageServlet servlet;


    @Before
    public void beforeEach() throws Exception {
        hsRequest.setSession(httpSession);
        doReturn(apiSession).when(httpSession).getAttribute("apiSession");
        doReturn(1L).when(apiSession).getTenantId();
        doReturn(customPageAuthorizationsHelper).when(servlet).getCustomPageAuthorizationsHelper(apiSession);
    }

    @Test
    public void should_get_Forbidden_Status_when_page_unAuthorize() throws Exception {
        hsRequest.setPathInfo("/AppToken/pageToken/content/");
        given(resourceRenderer.getPathSegments("/AppToken/pageToken/content/")).willReturn(Arrays.asList("AppToken", "pageToken", "content"));
        given(customPageAuthorizationsHelper.isPageAuthorized("AppToken", "pageToken")).willReturn(false);

        servlet.doGet(hsRequest, hsResponse);

        verify(hsResponse).sendError(403 ,"User not Authorized");
    }

    @Test
    public void should_get_badRequest_Status_when_page_name_is_not_set() throws Exception {
        hsRequest.setPathInfo("/AppToken/content/");

        servlet.doGet(hsRequest, hsResponse);

        verify(hsResponse).sendError(400, "The info path is suppose to contain the application token, the page token and one of the separator '/content', '/theme' or '/API'.");
    }

    @Test
    public void should_redirect_to_valide_url_on_missing_slash() throws Exception {
        hsRequest.setPathInfo("/AppToken/pageToken/content");

        servlet.doGet(hsRequest, hsResponse);

        verify(customPageRequestModifier).redirectToValidPageUrl(hsRequest, hsResponse);
    }

    @Test
    public void getPage_should_call_the_page_renderer() throws Exception {
        testPageIsWellCalled("AppToken", "contentcustompage_htmlexample1", "/AppToken/contentcustompage_htmlexample1/content/", Arrays.asList("AppToken","contentcustompage_htmlexample1","content"));
        testPageIsWellCalled("AppToken", "custompage_htmlexample2", "/AppToken/custompage_htmlexample2/content/index", Arrays.asList("AppToken","custompage_htmlexample2","content","index"));
        testPageIsWellCalled("AppToken", "custompage_htmlexample4", "/AppToken/custompage_htmlexample4/content/index.html", Arrays.asList("AppToken","custompage_htmlexample4","content","index.html"));
        testPageIsWellCalled("AppToken", "custompage_htmlexample5", "/AppToken/custompage_htmlexample5/content/Index.groovy", Arrays.asList("AppToken","custompage_htmlexample5","content","Index.groovy"));
    }

    private void testPageIsWellCalled(final String appToken, final String pageToken, final String path, final List<String> pathSegment) throws Exception {
        hsRequest.setPathInfo(path);
        given(resourceRenderer.getPathSegments(path)).willReturn(pathSegment);
        given(customPageAuthorizationsHelper.isPageAuthorized(appToken, pageToken)).willReturn(true);

        servlet.doGet(hsRequest, hsResponse);

        verify(pageRenderer, times(1)).displayCustomPage(hsRequest, hsResponse, apiSession, pageToken);
    }

    @Test
    public void getResource_should_call_the_resource_renderer() throws Exception {
        hsRequest.setPathInfo("/AppToken/custompage_htmlexample/content/css/file.css");
        final File pageDir = new File("/pageDir");
        given(resourceRenderer.getPathSegments("/AppToken/custompage_htmlexample/content/css/file.css")).willReturn(Arrays.asList("AppToken", "custompage_htmlexample", "content", "css", "file.css"));
        doReturn(pageResourceProvider).when(pageRenderer).getPageResourceProvider("custompage_htmlexample", 1L);
        doReturn(pageDir).when(pageResourceProvider).getPageDirectory();
        doReturn(true).when(bonitaHomeFolderAccessor).isInFolder(any(File.class), any(File.class));

        servlet.doGet(hsRequest, hsResponse);

        verify(resourceRenderer, times(1)).renderFile(hsRequest, hsResponse, new File(pageDir, File.separator+"resources"+File.separator+"css"+File.separator+"file.css"));
    }

    @Test(expected=ServletException.class)
    public void getResource_should_throw_exception_if_unauthorised() throws Exception {
        hsRequest.setPathInfo("/AppToken/custompage_htmlexample/content/css/../../../file.css");
        final File pageDir = new File(".");
        given(resourceRenderer.getPathSegments("/AppToken/custompage_htmlexample/content/css/../../../file.css")).willReturn(
                Arrays.asList("AppToken","custompage_htmlexample","content", "css", "..", "..", "..", "file.css"));
        doReturn(pageResourceProvider).when(pageRenderer).getPageResourceProvider("custompage_htmlexample", 1L);
        given(pageResourceProvider.getPageDirectory()).willReturn(pageDir);
        doReturn(false).when(bonitaHomeFolderAccessor).isInFolder(any(File.class), any(File.class));

        servlet.doGet(hsRequest, hsResponse);
    }



}