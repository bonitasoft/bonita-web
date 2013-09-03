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
        String userLocaleStr = null;
        final String theLocaleCookieName = "BOS_Locale";
        final Cookie theCookies[] = request.getCookies();
        if (theCookies != null) {
            for (int i = 0; i < theCookies.length; i++) {
                if (theCookies[i].getName().equals(theLocaleCookieName)) {
                    final Cookie theCookie = theCookies[i];
                    userLocaleStr = theCookie.getValue().toString();
                    break;
                }
            }
        }
        if (userLocaleStr == null) {
            userLocaleStr = request.getLocale().toString();
        }
        return userLocaleStr;
    }
}
