/**
 * Copyright (C) 2011 BonitaSoft S.A.
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
package org.bonitasoft.console.common.server.sso.servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bonitasoft.console.common.server.login.LoginManager;
import org.bonitasoft.console.common.server.sso.InternalSSOManager;
import org.bonitasoft.engine.session.APISession;

/**
 * @author Yongtao Guo
 * 
 */
public class RedirectServlet extends HttpServlet {

    /**
     * Logger
     */
    private static Logger LOGGER = Logger.getLogger(RedirectServlet.class.getName());

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -7641091088530357874L;

    /**
     * the URL param for the redirection URL
     */
    protected static final String REDIRECT_URL_PARAM = "url";

    /**
     * the URL param for the encrypted credentials
     */
    protected static final String USER_TOKEN_PARAM = "token";

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        doPost(request, response);
    }

    /**
     * get temporary token
     */
    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {

        final String redirectURL = request.getParameter(REDIRECT_URL_PARAM);
        final HttpSession httpSession = request.getSession();
        final APISession aAPISession = (APISession) httpSession.getAttribute(LoginManager.API_SESSION_PARAM_KEY);
        if (aAPISession != null) {
            // InternalSSO
            final String userToken = InternalSSOManager.getInstance().add(aAPISession);
            if (redirectURL != null) {
                try {
                    response.sendRedirect(buildRedirectionUrl(redirectURL, userToken));
                } catch (final IOException e) {
                    if (LOGGER.isLoggable(Level.SEVERE)) {
                        LOGGER.log(Level.SEVERE, e.getMessage(), e);
                    }
                    throw new ServletException(e);
                }
            }
        } else {
            final String errMsg = "Can not redirect to another app!";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errMsg);
            }
            throw new ServletException(errMsg);
        }

    }

    /**
     * Build the redirection URL
     * 
     * @param redirectURL
     * @param locale
     * @param temporaryToken
     * @return
     */
    protected String buildRedirectionUrl(final String redirectURL, final String temporaryToken) {

        try {
            String theURL;
            if (redirectURL.contains("#")) {
                theURL = redirectURL.substring(0, redirectURL.lastIndexOf("#") - 1);
            } else {
                theURL = redirectURL;
            }

            String tokenSuffix = "";
            if (temporaryToken != null) {
                if (theURL.contains("?")) {
                    tokenSuffix = "&" + USER_TOKEN_PARAM + "=" + temporaryToken;
                } else {
                    tokenSuffix = "?" + USER_TOKEN_PARAM + "=" + temporaryToken;
                }
            }
            return URLEncoder.encode(theURL + tokenSuffix, "UTF-8");
        } catch (final Exception e) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, e.getMessage(), e);
            }
            return "";
        }
    }
}
