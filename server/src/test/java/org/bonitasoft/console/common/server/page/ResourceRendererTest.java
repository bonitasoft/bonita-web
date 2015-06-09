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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bonitasoft.console.common.server.utils.BonitaHomeFolderAccessor;
import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.session.APISession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author Julien Mege
 */
@RunWith(MockitoJUnitRunner.class)
public class ResourceRendererTest {

    @Mock
    private HttpServletRequest req;

    @Mock
    private HttpServletResponse res;

    @Mock
    private ServletContext servletContext;

    @Mock
    ServletOutputStream outputStream;

    @Mock
    HttpSession httpSession;

    @Mock
    BonitaHomeFolderAccessor bonitaHomeFolderAccessor;

    @Mock
    private CustomPageService customPageService;

    @Spy
    @InjectMocks
    ResourceRenderer resourceRenderer;

    @Mock
    private APISession apiSession;

    @Before
    public void setup() throws IOException {

//        doReturn(customPageService).when(resourceRenderer).getCustomPageService();

        when(req.getSession()).thenReturn(httpSession);
        when(res.getOutputStream()).thenReturn(outputStream);
        when(httpSession.getServletContext()).thenReturn(servletContext);
    }

    @Test
    public void renderFile_should_build_a_valid_response() throws BonitaException, URISyntaxException, IOException, IllegalAccessException,
            InstantiationException {
        final File resourceFile = getResourceFile();
        final long contentLength = resourceFile.length();
        when(servletContext.getMimeType("file.css")).thenReturn("text/css");
        resourceRenderer.renderFile(req, res, resourceFile, apiSession);

        verify(res).setCharacterEncoding("UTF-8");
        verify(servletContext).getMimeType("file.css");
        verify(res).setContentType("text/css");
        verify(res).setContentLength((int) contentLength);
        verify(res).setBufferSize((int) contentLength);
        verify(res).setHeader("Cache-Control", "no-cache");
        verify(outputStream).write(any(byte[].class), eq(0), eq((int) contentLength));
        verify(res).flushBuffer();
        verify(outputStream).close();
    }

    private File getResourceFile() throws URISyntaxException {
        return new File(ResourceRendererTest.class.getResource("file.css").toURI());
    }

    @Test(expected = BonitaException.class)
    public void renderFile_should_throw_bonita_exception_on_ioexception() throws BonitaException, URISyntaxException, IOException, IllegalAccessException,
            InstantiationException {
        final File resourceFile = getResourceFile();
        doThrow(new IOException()).when(outputStream).write(any(byte[].class), any(int.class), any(int.class));

        resourceRenderer.renderFile(req, res, resourceFile, apiSession);
    }

    @Test(expected = BonitaException.class)
    public void getResourceFile_should_throw_BonitaException_on_passing_null_resources_folder() throws
            Exception {
        resourceRenderer.renderFile(req, res, null, apiSession);
    }

    @Test
    public void getResourceFile_should_sendError404_on_passing_none_existing_resources() throws
            Exception {
        final File noneExistingFile = new File("NoneExistingFile.css");
        resourceRenderer.renderFile(req, res, noneExistingFile, apiSession);
        verify(res).sendError(HttpServletResponse.SC_NOT_FOUND, "Cannot find the resource file " + noneExistingFile.getName());
    }

    @Test
    public void getPathSegments_should_return_expected_token_list() throws UnsupportedEncodingException {
        when(req.getPathInfo()).thenReturn("a/b");

        final List<String> tokens = resourceRenderer.getPathSegments("a/b");
        assertThat(tokens).hasSize(2).containsExactly("a", "b");
    }

    @Test
    public void getPathSegments_should_return_expected_token_list_ondouble_slash() throws UnsupportedEncodingException {
        when(req.getPathInfo()).thenReturn("a//b");

        final List<String> tokens = resourceRenderer.getPathSegments("a//b");
        assertThat(tokens).hasSize(2).containsExactly("a", "b");
    }

    @Test
    public void getPathSegments_should_return_expected_token_list_if_no_slash() throws UnsupportedEncodingException {
        when(req.getPathInfo()).thenReturn("a");

        final List<String> tokens = resourceRenderer.getPathSegments("a");
        assertThat(tokens).hasSize(1).containsExactly("a");
    }

}
