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

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bonitasoft.console.common.server.api.token.APIToken;

/**
 * @author Paul AMAR
 */
public class TokenGeneratorFilter implements Filter {

    public static final String API_TOKEN = "api_token";

    public static final String X_BONITA_API_TOKEN = "X-Bonita-API-Token";

    protected static final Logger LOGGER = Logger.getLogger(TokenGeneratorFilter.class.getName());

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest req = (HttpServletRequest) request;
        final HttpServletResponse res = (HttpServletResponse) response;

        // Create
        Object apiTokenFromClient = req.getSession().getAttribute(API_TOKEN);
        if (apiTokenFromClient == null) {
            apiTokenFromClient = new APIToken().getToken();
            req.getSession().setAttribute(API_TOKEN, apiTokenFromClient);
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Bonita BPM API Token generated: " + apiTokenFromClient);
            }
        }
        if(res.containsHeader(X_BONITA_API_TOKEN)){
            res.setHeader(X_BONITA_API_TOKEN, apiTokenFromClient.toString());
        } else {
            res.addHeader(X_BONITA_API_TOKEN, apiTokenFromClient.toString());
        }
        final Cookie csrfCookie = new Cookie(X_BONITA_API_TOKEN, apiTokenFromClient.toString());
        csrfCookie.setPath(req.getContextPath());
        res.addCookie(csrfCookie);
        chain.doFilter(req, res);
    }

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

}
