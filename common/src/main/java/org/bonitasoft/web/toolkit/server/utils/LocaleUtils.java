/**
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

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Anthony Birembaut
 * 
 */
public class LocaleUtils {

    public static final String LOCALE_PARAM = "locale";
    
    public static final String DEFAULT_LOCALE = "en";

    public static final String LOCALE_COOKIE_NAME = "BOS_Locale";
    
    /**
     * Logger
     */
    private final static Logger LOGGER = Logger.getLogger(LocaleUtils.class.getName());
    
    /**
     * Return the user locale as set in the the request. If the locale code is invalid, returns the default locale (en).
     * If the locale is not passed in the request try to get it from the BOS_Locale cookie.
     * If the cookie is not set returns the default locale (en).
     * This method should be used sparingly for performances reasons (gather in one call when possible)
     * 
     * @param request
     *            the HTTP servlet request
     * @return the user locale
     */
    public static String getUserLocaleAsString(final HttpServletRequest request) {
        String locale = getLocaleFromRequestURL(request);
        if (locale == null) {
            locale = getLocaleFromCookie(request);
        }
        if (locale == null) {
            String browserLocale = getLocaleFromBrowser(request);
            locale = browserLocale != null ? browserLocale : DEFAULT_LOCALE;
        }
        return locale;
    }

    public static String getLocaleFromCookie(final HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (final Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(LOCALE_COOKIE_NAME)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
    
    public static String getLocaleFromBrowser(final HttpServletRequest request) {
        Locale browserLocale = request.getLocale();
        return browserLocale != null ? browserLocale.toString() : null;
    }
    
    public static Locale getUserLocale(final HttpServletRequest request) {
        return org.apache.commons.lang3.LocaleUtils.toLocale(getUserLocaleAsString(request));
    }
    
    public static String getLocaleFromRequestURL(final HttpServletRequest request) {
        String localeAsString = request.getParameter(LOCALE_PARAM);
        if (localeAsString != null) {
            try {
                org.apache.commons.lang3.LocaleUtils.toLocale(localeAsString);
            } catch (Exception e) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, "unsupported locale: " + localeAsString);
                }
                localeAsString = DEFAULT_LOCALE;
            }
        }
        return localeAsString;
    }

    public static void addLocaleCookieToResponse(final HttpServletResponse response, final String locale) {
        Cookie localeCookie = new Cookie(LOCALE_COOKIE_NAME, locale);
        localeCookie.setPath("/");
        response.addCookie(localeCookie);
    }
}
