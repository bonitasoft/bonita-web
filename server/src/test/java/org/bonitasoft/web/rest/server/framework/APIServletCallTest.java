/*
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.bonitasoft.web.rest.server.framework;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bonitasoft.console.common.server.preferences.properties.ResourcesPermissionsMapping;
import org.bonitasoft.console.common.server.utils.SessionUtil;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.server.framework.search.ItemSearchResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class APIServletCallTest {

    @Mock
    private ResourcesPermissionsMapping resourcesPermissionsMapping;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private APISession apiSession;
    @Mock
    private HttpSession httpSession;
    @Mock
    private API api;

    @Spy
    private final APIServletCall apiServletCall = new APIServletCall();

    @Before
    public void before() {
        doReturn(httpSession).when(request).getSession();
        doReturn(apiSession).when(httpSession).getAttribute(SessionUtil.API_SESSION_PARAM_KEY);
        doReturn(1l).when(apiSession).getTenantId();
        doReturn(false).when(apiSession).isTechnicalUser();
        doReturn("john").when(apiSession).getUserName();
        apiServletCall.api = api;
    }

    @Test
    public void should_parsePath_request_info_with_id() {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        doReturn("API/bpm/case/15").when(request).getPathInfo();

        apiServletCall.parsePath(request);

        assertThat(apiServletCall.getId().getPart(0)).isEqualTo("15");
        assertThat(apiServletCall.getResourceName()).isEqualTo("case");
        assertThat(apiServletCall.getApiName()).isEqualTo("bpm");

    }

    @Test
    public void doGet_On_Search_Should_Set_Content_Range_Headers_Correctly() throws Exception {
        doReturn(new ArrayList<String>()).when(apiServletCall).getParameterAsList("d");
        doReturn(new ArrayList<String>()).when(apiServletCall).getParameterAsList("n");
        doReturn("0").when(apiServletCall).getParameter("p");
        doReturn("0").when(apiServletCall).getParameter("c");
        doReturn("id ASC").when(apiServletCall).getParameter("o");
        doReturn("").when(apiServletCall).getParameter("s");
        doReturn(null).when(apiServletCall).getParameterAsList("f");
        doReturn(new ArrayList<String>()).when(apiServletCall).getParameterAsList("d");

        doNothing().when(apiServletCall).head(anyString(), anyString());
        doNothing().when(apiServletCall).output(any(List.class));
        doReturn(2).when(apiServletCall).countParameters();

        final ItemSearchResult itemSearchResult = mock(ItemSearchResult.class);
        when(itemSearchResult.getPage()).thenReturn(4);
        when(itemSearchResult.getLength()).thenReturn(8);
        when(itemSearchResult.getTotal()).thenReturn(789L);
        when(api.runSearch(anyInt(), anyInt(), anyString(), anyString(), any(Map.class), any(List.class), any(List.class))).thenReturn(itemSearchResult);

        apiServletCall.doGet();
        verify(apiServletCall).head(anyString(), anyString());
    }

}
