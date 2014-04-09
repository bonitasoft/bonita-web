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

import javax.servlet.http.HttpServletRequest;

import org.bonitasoft.console.common.server.login.LoginManager;
import org.bonitasoft.console.common.server.login.impl.oauth.OAuthConsumerNotFoundException;

/**
 * @author Vincent Elcrin
 * 
 */
public class LoginUrl implements Locator {

    private final String location;

    /**
     * @throws LoginUrlException
     *             If the login page Url couldn't be retrieved
     */
    public LoginUrl(final LoginManager loginManager, final long tenantId, final String redirectUrl, HttpServletRequest request) {
        location = getLoginPageUrl(loginManager, redirectUrl, tenantId, request);
    }

    @Override
    public String getLocation() {
        return location;
    }

    private String getLoginPageUrl(final LoginManager loginManager, final String redirectURL, final long tenantId, HttpServletRequest request)
            throws LoginUrlException {
        try {

            return loginManager.getLoginpageURL(request, tenantId, URLEncoder.encode(redirectURL, "UTF-8"));
        } catch (final UnsupportedEncodingException e) {
            throw new LoginUrlException(e);
        } catch (final OAuthConsumerNotFoundException e) {
            throw new LoginUrlException(e);
        }
    }

}
