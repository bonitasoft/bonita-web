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

package org.bonitasoft.web.toolkit.server.utils;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

/**
 * Created by Vincent Elcrin
 * Date: 10/10/13
 * Time: 10:25
 */
public class LocaleUtilsTest {

    @Mock
    private HttpServletRequest request;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void testLocalCanBeRetrieveFromCookie() throws Exception {
        Cookie[] cookies = {
                new Cookie("aCookie", "value"),
                new Cookie(LocaleUtils.LOCALE_COOKIE_NAME, "fr"),
                new Cookie("aNullCookie", null),
        };
        doReturn(cookies).when(request).getCookies();

        String locale = LocaleUtils.getUserLocaleAsString(request);

        assertEquals("fr", locale);
    }

    @Test
    public void testANullCookieResultsWithBrowserLocale() throws Exception {
        doReturn(Locale.CANADA_FRENCH).when(request).getLocale();
        
        String locale = LocaleUtils.getUserLocaleAsString(request);

        assertEquals(Locale.CANADA_FRENCH.toString(), locale);
    }
    
    @Test
    public void testANullCookieAndBrowserLocaleResultsWithDefaultLocale() throws Exception {
        
        String locale = LocaleUtils.getUserLocaleAsString(request);

        assertEquals("en", locale);
    }

    @Test
    public void testWeCanRetrieveLocaleFromCookie() throws Exception {
        Cookie[] cookies = {
                new Cookie(LocaleUtils.LOCALE_COOKIE_NAME, "en_US"),
        };
        doReturn(cookies).when(request).getCookies();

        String locale = LocaleUtils.getUserLocaleAsString(request);

        assertEquals("en_US", locale);
    }
    
    @Test
    public void testWeCanRetrieveLocaleFromRequest() throws Exception {
        Cookie[] cookies = {
                new Cookie(LocaleUtils.LOCALE_COOKIE_NAME, "en_US"),
        };
        doReturn(cookies).when(request).getCookies();

        String locale = LocaleUtils.getUserLocaleAsString(request);

        assertEquals("en_US", locale);
    }

    @Test
    public void testWeGetLocaleFromRequestEvenIfCookieIsPresent() throws Exception {
        Cookie[] cookies = {
                new Cookie(LocaleUtils.LOCALE_COOKIE_NAME, "en_US"),
        };
        doReturn(cookies).when(request).getCookies();
        doReturn(Locale.CHINA.toString()).when(request).getParameter(LocaleUtils.LOCALE_PARAM);

        String locale = LocaleUtils.getUserLocaleAsString(request);

        assertEquals(Locale.CHINA.toString(), locale);
    }
    
    @Test
    public void testAnInvalidCookieResultsWithDefaultLocale() throws Exception {
        Cookie[] cookies = {
                new Cookie(LocaleUtils.LOCALE_COOKIE_NAME, "weirdvalue"),
        };
        String locale = LocaleUtils.getUserLocaleAsString(request);

        assertEquals("en", locale);
    }
    
    @Test
    public void testAnInvalidLocaleInRequestResultsWithDefaultLocale() throws Exception {
        doReturn("weirdvalue").when(request).getParameter(LocaleUtils.LOCALE_PARAM);
        
        String locale = LocaleUtils.getUserLocaleAsString(request);

        assertEquals("en", locale);
    }
}
