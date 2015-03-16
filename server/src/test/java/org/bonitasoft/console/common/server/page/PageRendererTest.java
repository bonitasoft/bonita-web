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

package org.bonitasoft.console.common.server.page;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import javax.servlet.http.HttpSession;

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
/**
 * @author Julien Mege
 */
@RunWith(MockitoJUnitRunner.class)
public class PageRendererTest{

    MockHttpServletRequest hsRequest = new MockHttpServletRequest();

    MockHttpServletResponse hsResponse = new MockHttpServletResponse();

    @Mock
    HttpSession httpSession;

    @Mock
    APISession apiSession;

    @Mock
    ResourceRenderer resourceRenderer;

    @Mock
    CustomPageService customPageService;

    @Mock
    PageResourceProvider pageResourceProvider;

    @Spy
    @InjectMocks
    PageRenderer pageRenderer = new PageRenderer(resourceRenderer);

    @Before
    public void beforeEach(){
        hsRequest.setSession(httpSession);
        doReturn(apiSession).when(httpSession).getAttribute("apiSession");
        doReturn(1L).when(apiSession).getTenantId();
    }

    @Test
    public void should_display_html_page() throws Exception {
        String pageName = "my_html_page_v_7";
        File pageDir = new File(PageRenderer.class.getResource(pageName).toURI());
        File indexFile = new File(pageDir, "resources"+File.separator+"index.html");

        doReturn(pageResourceProvider).when(pageRenderer).getPageResourceProvider(pageName, 1L);
        doReturn(pageDir).when(pageResourceProvider).getPageDirectory();

        when(customPageService.getGroovyPageFile(any(File.class))).thenReturn(new File("none_existing_file"));

        pageRenderer.displayCustomPage(hsRequest, hsResponse, apiSession, pageName);

        verify(resourceRenderer, times(1)).renderFile(hsRequest, hsResponse, indexFile);
    }

    @Test
    public void should_display_fallback_index_if_no_index_in_resource_folder() throws Exception {

        String pageName = "my_html_page_v_6";
        File pageDir = new File(PageRenderer.class.getResource(pageName).toURI());
        File indexFile = new File(pageDir, "index.html");

        doReturn(pageResourceProvider).when(pageRenderer).getPageResourceProvider(pageName, 1L);
        doReturn(pageDir).when(pageResourceProvider).getPageDirectory();

        when(customPageService.getGroovyPageFile(any(File.class))).thenReturn(new File("none_existing_file"));

        pageRenderer.displayCustomPage(hsRequest, hsResponse, apiSession, pageName);

        verify(resourceRenderer, times(1)).renderFile(hsRequest, hsResponse, indexFile);
    }

}
