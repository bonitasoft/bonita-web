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
package org.bonitasoft.platform.server.filter;

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
import javax.servlet.http.HttpSession;

import org.bonitasoft.engine.session.PlatformSession;

/**
 * @author Haojie Yuan
 */
public class PlatformFilter implements Filter {

    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(PlatformFilter.class.getName());

    /**
     * platform session
     */
    protected static final String PLATFORMSESSION = "platformSession";

    protected String PLATFORM_LOGIN_PAGE = "../platformLogin.jsp";

    /**
     * (non-Javadoc)
     * 
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
    }

    /**
     * (non-Javadoc)
     * 
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        try {
            pageRedirect(request, response, chain);
        } catch (final Exception e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
            throw new ServletException(e);
        }
    }

    private void pageRedirect(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws ServletException {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final HttpSession session = httpRequest.getSession();
        final PlatformSession platformSession = (PlatformSession) session.getAttribute(PLATFORMSESSION);
        final HttpServletResponse httpResponse = (HttpServletResponse) response;
        try {
            if (platformSession != null) {
                chain.doFilter(request, response);
            } else {
                if (httpRequest.getRequestURI().startsWith("/REST")) {
                    httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                } else {
                    session.removeAttribute(PLATFORMSESSION);
                    session.invalidate();

                    httpResponse.sendRedirect(this.PLATFORM_LOGIN_PAGE);
                }
            }
        } catch (final Exception e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
            throw new ServletException(e);
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

}
