/**
 * Copyright (C) 2013 BonitaSoft S.A.
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
package org.bonitasoft.console.common.server.login.filter;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author Paul AMAR
 *
 */
public abstract class AbstractAuthorizationFilter implements Filter {


    /**
     * Logger
     */
    protected static final Logger LOGGER = Logger.getLogger(AbstractAuthorizationFilter.class.getName());

    private String excludePatterns = null;

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        excludePatterns = filterConfig.getInitParameter("excludePatterns");
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest httpRequest = getRequest(request);
        final HttpServletResponse httpResponse = (HttpServletResponse) response;
        final String requestURL = httpRequest.getRequestURI();

        if (sessionIsNotNeeded(requestURL, excludePatterns)) {
            chain.doFilter(httpRequest, httpResponse);
        } else if (checkValidCondition(httpRequest, httpResponse)) {
            chain.doFilter(httpRequest, httpResponse);
        }
    }

    /**
     * Override this to be able to wrap the servlet (this is useful if the filter needs to read the body for example)
     * 
     * @param request the servlet request
     * @return the HttpServletRequest
     */
    protected HttpServletRequest getRequest(final ServletRequest request) {
        return (HttpServletRequest) request;
    }

    @Override
    public void destroy() {

    }

    protected boolean sessionIsNotNeeded(final String requestURL, final String excludePatterns) {
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

    abstract boolean checkValidCondition(final HttpServletRequest httpRequest, final HttpServletResponse httpResponse) throws ServletException;
}
