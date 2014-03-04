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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Anthony Birembaut
 * 
 */
public class LocaleUtils {

    public static final String BOS_LOCALE = "BOS_Locale";

    /**
     * Return the user locale as set in the BOS_Locale cookie.
     * If the cookie does not exist, return the locale of the request
     * This method should be used sparingly for performances reasons (gather in one call when possible)
     * 
     * @param request
     *            the HTTP servlet request
     * @return the user locale as a string
     */
    public static String getUserLocale(final HttpServletRequest request) {
        String userLocaleStr = getUserLocale(request.getCookies());
        if (userLocaleStr == null && request.getLocale() != null) {
            userLocaleStr = request.getLocale().toString();
        }
        return userLocaleStr;
    }

    public static String getUserLocale(Cookie[] cookies) {
        if (cookies != null) {
            for(Cookie cookie : cookies) {
                if(BOS_LOCALE.equals(cookie.getName())) {
                    return cookie.getValue().toString();
                }
            }
        }
        return null;
    }
}
