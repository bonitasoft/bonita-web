/**
 * Copyright (C) 2016 Bonitasoft S.A.
 * Bonitasoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation
 * version 2.1 of the License.
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth
 * Floor, Boston, MA 02110-1301, USA.
 **/

package org.bonitasoft.console.common.server.servlet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import javax.servlet.http.HttpServletResponse;

import org.bonitasoft.engine.api.ApplicationAPI;
import org.bonitasoft.engine.business.application.ApplicationNotFoundException;
import org.bonitasoft.engine.exception.NotFoundException;
import org.bonitasoft.engine.business.application.impl.IconImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * @author Anthony Birembaut
 */
@RunWith(MockitoJUnitRunner.class)
public class ApplicationIconServletTest {

    private static final long APPLICATION_ID = 1328970423L;
    @Spy
    private ApplicationIconServlet applicationIconServlet;
    private MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
    private MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();
    @Mock
    private ApplicationAPI applicationAPI;

    @Before
    public void before() throws Exception {
        doReturn(applicationAPI).when(applicationIconServlet).getApplicationApi(any());
    }

    private void havingIcon(long applicationId, byte[] content) throws NotFoundException {
        doReturn(new IconImpl("mime-type", content)).when(applicationAPI).getIconOfApplication(applicationId);
    }

    @Test
    public void should_return_icon_content_when_valid_icon_id_is_given() throws Exception {
        havingIcon(APPLICATION_ID, "content".getBytes());
        httpServletRequest.setPathInfo("/" + String.valueOf(APPLICATION_ID));

        applicationIconServlet.doGet(httpServletRequest, httpServletResponse);

        assertThat(httpServletResponse.getContentAsByteArray()).isEqualTo("content".getBytes());
    }

    @Test
    public void should_set_special_headers_when_user_agent_is_firefox() throws Exception {
        havingIcon(APPLICATION_ID, "content".getBytes());
        httpServletRequest.addHeader("User-Agent", "Firefox-1238941");
        httpServletRequest.setPathInfo("/" + String.valueOf(APPLICATION_ID));

        applicationIconServlet.doGet(httpServletRequest, httpServletResponse);

        assertThat(httpServletResponse.getHeader("Content-Disposition")).isEqualTo("inline; filename*=UTF-8''" + String.valueOf(APPLICATION_ID));
    }

    @Test
    public void should_set_normal_headers_when_user_agent_is_not_firefox() throws Exception {
        havingIcon(APPLICATION_ID, "content".getBytes());
        httpServletRequest.addHeader("User-Agent", "Chrome-1238941");
        httpServletRequest.setPathInfo("/" + String.valueOf(APPLICATION_ID));

        applicationIconServlet.doGet(httpServletRequest, httpServletResponse);

        assertThat(httpServletResponse.getHeader("Content-Disposition")).isEqualTo("inline; filename=\"" + String.valueOf(APPLICATION_ID) + "\"; filename*=UTF-8''"
                + String.valueOf(APPLICATION_ID));
    }

    @Test
    public void should_status_be_BAD_REQUEST_when_id_is_missing_in_url() throws Exception {
        httpServletRequest.setPathInfo("");

        applicationIconServlet.doGet(httpServletRequest, httpServletResponse);

        assertThat(httpServletResponse.getStatus()).isEqualTo(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    public void should_status_be_BAD_REQUEST_when_id_is_not_a_long_in_url() throws Exception {
        httpServletRequest.setPathInfo("notALong");

        applicationIconServlet.doGet(httpServletRequest, httpServletResponse);

        assertThat(httpServletResponse.getStatus()).isEqualTo(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    public void should_status_be_BAD_REQUEST_when_null_is_set_as_id_in_url() throws Exception {
        applicationIconServlet.doGet(httpServletRequest, httpServletResponse);

        assertThat(httpServletResponse.getStatus()).isEqualTo(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    public void should_status_be_NOT_FOUND_when_application_does_not_exists_in_engine() throws Exception {
        doThrow(ApplicationNotFoundException.class).when(applicationAPI).getIconOfApplication(APPLICATION_ID);
        httpServletRequest.setPathInfo("/" + String.valueOf(APPLICATION_ID));

        applicationIconServlet.doGet(httpServletRequest, httpServletResponse);

        assertThat(httpServletResponse.getStatus()).isEqualTo(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    public void should_status_be_NOT_FOUND_when_icon_does_not_exists_in_engine_for_this_application() throws Exception {
        doReturn(null).when(applicationAPI).getIconOfApplication(APPLICATION_ID);
        httpServletRequest.setPathInfo("/" + String.valueOf(APPLICATION_ID));

        applicationIconServlet.doGet(httpServletRequest, httpServletResponse);

        assertThat(httpServletResponse.getStatus()).isEqualTo(HttpServletResponse.SC_NOT_FOUND);
    }
    
    @Test
    public void should_content_type_be_set_to_what_is_return_from_the_engine() throws Exception {
        doReturn(new IconImpl("theMimeTypeOfTheIcon", "content".getBytes())).when(applicationAPI).getIconOfApplication(APPLICATION_ID);
        httpServletRequest.setPathInfo("/" + String.valueOf(APPLICATION_ID));

        applicationIconServlet.doGet(httpServletRequest, httpServletResponse);

        assertThat(httpServletResponse.getContentType()).isEqualTo("theMimeTypeOfTheIcon");
    }
}
