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

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.bonitasoft.console.common.server.filter.ExcludingPatternFilter;

/**
 * Security filter setting the X-Content-Type-Options in the response headers
 * 
 * @author Paul AMAR
 * @author Anthony Birembaut
 */
public class ContentTypeSecurityFilter extends ExcludingPatternFilter {

    protected static final String X_CONTENT_TYPE_HEADER = "X-Content-Type-Options";

    protected String headerValue;

    @Override
    public String getDefaultExcludedPages() {
        return "";
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        headerValue = StringUtils.defaultIfEmpty(filterConfig.getInitParameter(X_CONTENT_TYPE_HEADER), "nosniff");
        super.init(filterConfig);
    }

    @Override
    public void proceedWithFiltering(final ServletRequest request, final ServletResponse response,
            final FilterChain chain) throws ServletException, IOException {
        // casting to HTTPServlet(Request|Response)
        final HttpServletRequest req = (HttpServletRequest) request;
        final HttpServletResponse res = (HttpServletResponse) response;

        chain.doFilter(req, res);

        // X-Content-Type-Options (Drive-by download attacks)
        res.setHeader(X_CONTENT_TYPE_HEADER, headerValue);
    }
}
