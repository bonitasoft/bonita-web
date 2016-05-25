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
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import javax.servlet.http.HttpServletResponse;

import org.bonitasoft.engine.api.IdentityAPI;
import org.bonitasoft.engine.exception.NotFoundException;
import org.bonitasoft.engine.identity.impl.IconImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * @author Baptiste Mesta
 */
@RunWith(MockitoJUnitRunner.class)
public class IconServletTest {

    private static final long ICON_ID = 1238970432L;
    @Spy
    private IconServlet iconServlet;
    private MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
    private MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();
    @Mock
    private IdentityAPI identityAPI;

    @Before
    public void before() throws Exception {
        doReturn(identityAPI).when(iconServlet).getIdentityApi(httpServletRequest);
    }

    private void havingIcon(long iconId, byte[] content) throws NotFoundException {
        doReturn(new IconImpl(iconId, "mime-type", content)).when(identityAPI).getIcon(iconId);
    }

    @Test
    public void should_return_icon_content_when_valid_icon_id_is_given() throws Exception {
        havingIcon(ICON_ID, "content".getBytes());
        httpServletRequest.setPathInfo("/" + String.valueOf(ICON_ID));

        iconServlet.doGet(httpServletRequest, httpServletResponse);

        assertThat(httpServletResponse.getContentAsByteArray()).isEqualTo("content".getBytes());
    }

    @Test
    public void should_set_special_headers_when_user_agent_is_firefox() throws Exception {
        havingIcon(ICON_ID, "content".getBytes());
        httpServletRequest.addHeader("User-Agent", "Firefox-1238941");
        httpServletRequest.setPathInfo("/" + String.valueOf(ICON_ID));

        iconServlet.doGet(httpServletRequest, httpServletResponse);

        assertThat(httpServletResponse.getHeader("Content-Disposition")).isEqualTo("inline; filename*=UTF-8''" + String.valueOf(ICON_ID));
    }

    @Test
    public void should_set_normal_headers_when_user_agent_is_not_firefox() throws Exception {
        havingIcon(ICON_ID, "content".getBytes());
        httpServletRequest.addHeader("User-Agent", "Chrome-1238941");
        httpServletRequest.setPathInfo("/" + String.valueOf(ICON_ID));

        iconServlet.doGet(httpServletRequest, httpServletResponse);

        assertThat(httpServletResponse.getHeader("Content-Disposition")).isEqualTo("inline; filename=\"" + String.valueOf(ICON_ID) + "\"; filename*=UTF-8''"
                + String.valueOf(ICON_ID));
    }

    @Test
    public void should_status_be_BAD_REQUEST_when_id_is_missing_in_url() throws Exception {
        httpServletRequest.setPathInfo("");

        iconServlet.doGet(httpServletRequest, httpServletResponse);

        assertThat(httpServletResponse.getStatus()).isEqualTo(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    public void should_status_be_BAD_REQUEST_when_id_is_not_a_long_in_url() throws Exception {
        httpServletRequest.setPathInfo("notALong");

        iconServlet.doGet(httpServletRequest, httpServletResponse);

        assertThat(httpServletResponse.getStatus()).isEqualTo(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    public void should_status_be_BAD_REQUEST_when_null_is_set_as_id_in_url() throws Exception {
        iconServlet.doGet(httpServletRequest, httpServletResponse);

        assertThat(httpServletResponse.getStatus()).isEqualTo(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    public void should_status_be_NOT_FOUND_when_icon_does_not_exists_in_engine() throws Exception {
        doThrow(NotFoundException.class).when(identityAPI).getIcon(ICON_ID);
        httpServletRequest.setPathInfo("/" + String.valueOf(ICON_ID));

        iconServlet.doGet(httpServletRequest, httpServletResponse);

        assertThat(httpServletResponse.getStatus()).isEqualTo(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    public void should_content_type_be_set_to_what_is_return_from_the_engine() throws Exception {
        doReturn(new IconImpl(ICON_ID, "theMimeTypeOfTheIcon", "content".getBytes())).when(identityAPI).getIcon(ICON_ID);
        httpServletRequest.setPathInfo("/" + String.valueOf(ICON_ID));

        iconServlet.doGet(httpServletRequest, httpServletResponse);

        assertThat(httpServletResponse.getContentType()).isEqualTo("theMimeTypeOfTheIcon");
    }
}
