/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.console.common.server.login;

import org.bonitasoft.console.common.server.login.datastore.Credentials;
import org.bonitasoft.console.common.server.login.impl.oauth.OAuthConsumerNotFoundException;

/**
 * @author Ruiheng Fan
 * 
 */
public interface LoginManager {

    static final String API_SESSION_PARAM_KEY = "apiSession";

    static final String USER_SESSION_PARAM_KEY = "user";

    /**
     * default locale
     */
    static final String DEFAULT_LOCALE = "en";

    /**
     * Tenant parameter name
     */
    static final String TENANT = "tenant";

    /**
     * Redirection URL parameter name
     */
    static final String REDIRECT_URL = "redirectUrl";

    /**
     * the URL param to indicate if we should redirect after login/logout (true by default)
     */
    static final String REDIRECT_AFTER_LOGIN_PARAM_NAME = "redirect";

    /**
     * the URL of the login page
     */
    static final String LOGIN_PAGE = "/login.jsp";

    /**
     * The default redirect URL.
     */
    static final String DEFAULT_DIRECT_URL = "portal/homepage";

    /**
     * the request param for the username
     */
    static final String USERNAME_SESSION_PARAM = "username";

    /**
     * Get Login Page URL
     * 
     * @param tenantId
     *            user tenantId
     * @param redirectURL
     *            redirect url
     * @return new redirect url
     * @throws OAuthConsumerNotFoundException
     */
    String getLoginpageURL(final String context, final long tenantId, final String redirectURL) throws OAuthConsumerNotFoundException;

    /**
     * Login the engine
     * 
     * @param request
     *            HTTP request
     * @param redirectURL
     *            redirect url
     * @param tenantId
     *            tenant id
     * @throws LoginFailedException
     * @throws OAuthConsumerNotFoundException
     */
    void login(final HttpServletRequestAccessor request, Credentials credentials) throws LoginFailedException;
}
