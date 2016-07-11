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
package org.bonitasoft.console.common.server.login.localization;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.ServletException;

import org.bonitasoft.console.common.server.auth.AuthenticationManager;
import org.bonitasoft.console.common.server.auth.ConsumerNotFoundException;
import org.bonitasoft.console.common.server.login.HttpServletRequestAccessor;

/**
 * @author Vincent Elcrin
 *
 */
public class LoginUrl implements Locator {

    private final String location;

    /**
     * @throws ServletException
     * @throws LoginUrlException
     *         If the login page Url couldn't be retrieved
     */
    public LoginUrl(final AuthenticationManager loginManager, final String redirectUrl, final HttpServletRequestAccessor request) throws LoginUrlException,
            ServletException {
        location = getLoginPageUrl(loginManager, redirectUrl, request);
    }

    @Override
    public String getLocation() {
        return location;
    }

    private String getLoginPageUrl(final AuthenticationManager loginManager, final String redirectURL, final HttpServletRequestAccessor request)
            throws LoginUrlException, ServletException {
        try {
            return loginManager.getLoginPageURL(request, URLEncoder.encode(redirectURL, "UTF-8"));
        } catch (final UnsupportedEncodingException e) {
            throw new LoginUrlException(e);
        } catch (final ConsumerNotFoundException e) {
            throw new LoginUrlException(e);
        }
    }

}
