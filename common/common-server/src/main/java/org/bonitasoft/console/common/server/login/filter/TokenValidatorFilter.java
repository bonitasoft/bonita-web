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
public class TokenValidatorFilter implements Filter {

    private String excludePatterns = null;
    
    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        excludePatterns = filterConfig.getInitParameter("excludePatterns");
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final HttpServletResponse httpResponse = (HttpServletResponse) response;
        final String requestURL = httpRequest.getRequestURI();
        
        if (sessionIsNotNeeded(requestURL, excludePatterns)) {
            chain.doFilter(request, response);
        } else if (isValidToken(httpRequest, httpResponse)) {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
    
    private boolean isValidToken(final HttpServletRequest httpRequest, final HttpServletResponse httpResponse) {
        String headerFromRequest = httpRequest.getHeader("X-API-Token");
        String apiToken = httpRequest.getSession().getAttribute("api_token").toString();

        if (headerFromRequest == null || apiToken == null || (!apiToken.equals(headerFromRequest))) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        
        return true;
    }
    
    private boolean sessionIsNotNeeded(final String requestURL, final String excludePatterns) {
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
