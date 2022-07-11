/**
 * Copyright (C) 2013 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.console.common.server.login.filter;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bonitasoft.console.common.server.api.token.APIToken;
import org.bonitasoft.console.common.server.login.PortalCookies;

/**
 * @author Julien Reboul
 *
 */
public class TokenGenerator {

    protected static final Logger LOGGER = LoggerFactory.getLogger(TokenGenerator.class.getName());

    public static final String API_TOKEN = "api_token";
    public static final String X_BONITA_API_TOKEN = "X-Bonita-API-Token";
    
    /**
     * set the CSRF security token to the HTTP response as cookie.
     * @deprecated use {@link PortalCookies#addCSRFTokenCookieToResponse(HttpServletRequest, HttpServletResponse, Object)} instead
     */
    @Deprecated
    public void setTokenToResponseCookie(HttpServletRequest request, HttpServletResponse res, Object apiTokenFromClient) {
        PortalCookies portalCookies = new PortalCookies();
        portalCookies.addCSRFTokenCookieToResponse(request, res, apiTokenFromClient);
    }
    
    /**
     * generate and store the CSRF security inside HTTP session
     * or retrieve it token from the HTTP session
     *
     * @param req the HTTP session
     * @return the CSRF security token
     */
    public String createOrLoadToken(final HttpSession session) {
        Object apiTokenFromClient = session.getAttribute(API_TOKEN);
        if (apiTokenFromClient == null) {
            apiTokenFromClient = new APIToken().getToken();
            session.setAttribute(API_TOKEN, apiTokenFromClient);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Bonita API Token generated: " + apiTokenFromClient);
            }
        }
        return apiTokenFromClient.toString();
    }

    /**
     * set the CSRF security token to the HTTP response as HTTP Header.
     *
     * @param res the http response
     * @param apiTokenFromClient the security token to set
     */
    public void setTokenToResponseHeader(final HttpServletResponse res, final String token) {
        if(res.containsHeader(X_BONITA_API_TOKEN)){
            res.setHeader(X_BONITA_API_TOKEN, token);
        } else {
            res.addHeader(X_BONITA_API_TOKEN, token);
        }
    }

}
