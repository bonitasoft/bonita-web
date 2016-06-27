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
package org.bonitasoft.console.common.server.auth;

import java.io.Serializable;
import java.util.Map;

import javax.servlet.ServletException;

import org.bonitasoft.console.common.server.login.HttpServletRequestAccessor;
import org.bonitasoft.console.common.server.login.credentials.Credentials;

/**
 * Interface to implement in order to delegate the authentication to an external provider
 *
 * @author Ruiheng Fan, Anthony Birembaut
 */
public interface AuthenticationManager {

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
     * the URL of the default login page
     */
    static final String LOGIN_PAGE = "/login.jsp";

    /**
     * The default redirect URL.
     */
    static final String DEFAULT_DIRECT_URL = "portal/homepage";

    /**
     * Get Login Page URL
     *
     * @param request
     *        HTTP request accessor object
     * @param redirectURL
     *        redirect url
     * @return new redirect url
     * @throws ConsumerNotFoundException
     * @throws ServletException
     */
    String getLoginPageURL(final HttpServletRequestAccessor requestAccessor, final String redirectURL) throws ConsumerNotFoundException, ServletException;

    /**
     * Authenticate the user (If no exception is thrown, an engine login will then be performed with the credentials)
     *
     * @param request
     *        HTTP request accessor object
     * @param credentials
     *        credentials extracted from the request or from the auto-login config
     * @return a map of credententials which if not null or empty will be used to login on the engine. Otherwise, the username and password contained in the
     *         credentials will be used
     * @throws AuthenticationFailedException
     * @throws ServletException
     */
    Map<String, Serializable> authenticate(final HttpServletRequestAccessor requestAccessor, final Credentials credentials)
            throws AuthenticationFailedException, ServletException;

    /**
     * Get Logout Page URL
     * If the LoginManager implementation of this method is to return null the default login page will be displayed
     *
     * @param request
     *        HTTP request accessor object
     * @param redirectURL
     *        redirect url
     * @return new redirect url
     * @throws ConsumerNotFoundException
     * @throws ServletException
     */
    String getLogoutPageURL(final HttpServletRequestAccessor requestAccessor, final String redirectURL) throws ConsumerNotFoundException, ServletException;
}
