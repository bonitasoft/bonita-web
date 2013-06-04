/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bonitasoft.console.common.server.login.LoginManager;
import org.bonitasoft.engine.session.APISession;

/**
 * @author Zhiheng Yang, Chong Zhao
 */
public class FilterManager implements Filter {

    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(FilterManager.class.getName());

    private String excludePatterns = null;

    /**
     * (non-Javadoc)
     * 
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        this.excludePatterns = filterConfig.getInitParameter("excludePatterns");
    }

    /**
     * (non-Javadoc)
     * 
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final String requestURL = httpRequest.getRequestURI();

        if (matchURL(requestURL, this.excludePatterns)) {
            chain.doFilter(request, response);
        } else {
            pageRedirect(request, response, chain);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see javax.servlet.Filter#destroy()
     */
    @Override
    public void destroy() {

    }

    private void pageRedirect(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws ServletException {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final APISession apiSession = (APISession) httpRequest.getSession().getAttribute(LoginManager.API_SESSION_PARAM_KEY);
        final HttpServletResponse httpResponse = (HttpServletResponse) response;
        try {
            if (apiSession != null) {
                chain.doFilter(request, response);
            } else {
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        } catch (final Exception e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
            throw new ServletException(e);
        }
    }

    private boolean matchURL(final String requestURL, final String excludePatterns) {
        boolean isMatched = false;
        if (excludePatterns != null) {
            final String[] patterns = excludePatterns.split(",");
            for (int i = 0, size = patterns.length; i < size; i++) {
                if (requestURL.contains(patterns[i])) {
                    isMatched = true;
                    break;
                }
            }
        }

        return isMatched;
    }
}
