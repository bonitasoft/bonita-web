/*
 * Copyright (C) 2013 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.bonitasoft.web.toolkit.server.servlet;

import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.server.ServletCall;
import org.bonitasoft.web.toolkit.server.utils.LocaleUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n.LOCALE;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by Vincent Elcrin
 * Date: 23/09/13
 * Time: 16:50
 */
public class ToolkitHttpServletTest {

    ToolkitHttpServlet toolkitHttpServlet = new ToolkitHttpServlet() {
        @Override
        protected ServletCall defineServletCall(HttpServletRequest req, HttpServletResponse resp) {
            return null;
        }
    };

    @Mock
    HttpServletRequest req;

    @Mock
    HttpServletResponse resp;

    @Mock
    PrintWriter writer;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void testOutputExceptionPrintProperJson() throws Exception {
        APIException exception = new APIException("message");
        doReturn(writer).when(resp).getWriter();

        toolkitHttpServlet.outputException(exception, req, resp, 500);

        verify(writer).print(exception.toJson());
    }

    @Test
    public void testLocaleIsPassedFromRequestToException() throws Exception {
        APIException exception = mock(APIException.class,
                withSettings().defaultAnswer(RETURNS_MOCKS));

        doReturn(writer).when(resp).getWriter();
        doReturn(new Cookie[] {
                new Cookie(LocaleUtils.BOS_LOCALE, "fr_FR")
        }).when(req).getCookies();

        toolkitHttpServlet.outputException(exception, req, resp, 500);

        verify(exception).setLocale(LOCALE.fr_FR);
    }

    @Test
    public void testIfLocaleIsNotInACookieThatNoLocaleIsPassedThrough() throws Exception {
        APIException exception = mock(APIException.class,
                withSettings().defaultAnswer(RETURNS_MOCKS));

        doReturn(writer).when(resp).getWriter();
        doReturn(new Cookie[0]).when(req).getCookies();

        toolkitHttpServlet.outputException(exception, req, resp, 500);

        verify(exception, never()).setLocale(any(LOCALE.class));
    }
}
