/**
 * Copyright (C) 2020 BonitaSoft S.A.
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.bonitasoft.console.common.server.auth.AuthenticationManager;
import org.bonitasoft.console.common.server.login.HttpServletRequestAccessor;

public class RedirectUrlHandler {
	
    public static boolean shouldRedirectAfterLogin(final HttpServletRequest request) {
        final String redirectAfterLogin = request.getParameter(AuthenticationManager.REDIRECT_AFTER_LOGIN_PARAM_NAME);
        final String redirectURL = request.getParameter(AuthenticationManager.REDIRECT_URL);
        // If there is a redirect param in the request use it otherwise check if there is a redirect URL
        return redirectAfterLogin != null ? Boolean.parseBoolean(redirectAfterLogin) : redirectURL != null;
    }
    
    public static boolean shouldRedirectAfterLogout(final HttpServletRequest request) {
        final String redirectAfterLogin = request.getParameter(AuthenticationManager.REDIRECT_AFTER_LOGIN_PARAM_NAME);
        final String redirectURL = request.getParameter(AuthenticationManager.REDIRECT_URL);
        final String loginPageURL = request.getParameter(AuthenticationManager.LOGIN_URL_PARAM_NAME);
        // If there is a redirect param in the request use it otherwise check if there is a redirect or login URL
        return redirectAfterLogin != null ? Boolean.parseBoolean(redirectAfterLogin) : (redirectURL != null || loginPageURL != null);
    }
    
    public static String retrieveRedirectUrl(final HttpServletRequestAccessor request, String... parametersToRemove) throws ServletException {
        final String redirectUrlFromRequest = request.getRedirectUrl();
        String redirectUrl = redirectUrlFromRequest != null ? redirectUrlFromRequest : getDefaultRedirectUrl();
        RedirectUrlBuilder redirectUrlBuilder = new RedirectUrlBuilder(redirectUrl);
        for (String parameterToRemove : parametersToRemove) {
            redirectUrlBuilder.removeParameter(parameterToRemove);
        }
        return redirectUrlBuilder.build().getUrl();
    }

    protected static String getDefaultRedirectUrl() {
        return AuthenticationManager.DEFAULT_DIRECT_URL;
    }
}
