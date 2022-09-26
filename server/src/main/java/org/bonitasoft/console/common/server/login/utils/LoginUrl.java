/**
 * Copyright (C) 2012 BonitaSoft S.A.
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
package org.bonitasoft.console.common.server.login.utils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;

import org.bonitasoft.console.common.server.auth.AuthenticationManager;
import org.bonitasoft.console.common.server.login.HttpServletRequestAccessor;

/**
 * @author Vincent Elcrin
 *
 */
public class LoginUrl {

    private final String location;

    /**
     * @throws ServletException
     */
    public LoginUrl(final AuthenticationManager authenticationManager, final String redirectUrl, final HttpServletRequestAccessor request) throws ServletException {
        location = getLoginPageUrl(authenticationManager, redirectUrl, request);
    }

    public String getLocation() {
        return location;
    }

    private String getLoginPageUrl(final AuthenticationManager authenticationManager, final String redirectURL, final HttpServletRequestAccessor request)
            throws ServletException {
        return authenticationManager.getLoginPageURL(request, URLEncoder.encode(redirectURL, StandardCharsets.UTF_8));
    }

}
