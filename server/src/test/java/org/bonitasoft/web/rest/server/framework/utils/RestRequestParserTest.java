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

package org.bonitasoft.web.rest.server.framework.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

import javax.servlet.http.HttpServletRequest;

import org.bonitasoft.web.toolkit.client.common.exception.api.APIMalformedUrlException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RestRequestParserTest {

    @Mock
    private HttpServletRequest httpServletRequest;

    @InjectMocks
    private RestRequestParser restRequestParser;

    @Test
    public void should_parsePath_request_info_with_id() {
        doReturn("API/bpm/case/15").when(httpServletRequest).getPathInfo();

        restRequestParser.invoke();

        assertThat(restRequestParser.getResourceQualifiers().getPart(0)).isEqualTo("15");
        assertThat(restRequestParser.getResourceName()).isEqualTo("case");
        assertThat(restRequestParser.getApiName()).isEqualTo("bpm");

    }

    @Test
    public void should_parsePath_request_info() {
        doReturn("API/bpm/case").when(httpServletRequest).getPathInfo();

        restRequestParser.invoke();

        assertThat(restRequestParser.getResourceQualifiers()).isNull();
        assertThat(restRequestParser.getResourceName()).isEqualTo("case");
        assertThat(restRequestParser.getApiName()).isEqualTo("bpm");

    }

    @Test(expected = APIMalformedUrlException.class)
    public void should_parsePath_with_bad_request() {
        doReturn("API/bpm").when(httpServletRequest).getPathInfo();

        restRequestParser.invoke();
    }
}
