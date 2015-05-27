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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.forms.server.filter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
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

/**
 * @author Haojie Yuan
 *
 */
public class CacheFilter implements Filter {

	protected Map<String, Integer> expiresMap = new HashMap<String, Integer>();

	/**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(CacheFilter.class.getName());

	@Override
    public void init(final FilterConfig filterConfig) throws ServletException {
		final Enumeration<?> names = filterConfig.getInitParameterNames();
		while (names.hasMoreElements()) {
            final String name = (String) names.nextElement();
            final String value = filterConfig.getInitParameter(name);
            try {
                final Integer expire = Integer.valueOf(value);
                expiresMap.put(name, expire);
            } catch (final NumberFormatException e) {
                LOGGER.log(Level.WARNING, name + " parameter value should be an integer");
            }
        }
    }

	@Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
		final HttpServletRequest req = (HttpServletRequest) request;
		final HttpServletResponse res = (HttpServletResponse) response;

		chain.doFilter(req, res);

        final String uri = req.getRequestURI();
        if (!uri.endsWith("nocache.js")) {
            String ext = null;
            final int dot = uri.lastIndexOf(".");
            if (dot != -1) {
                ext = uri.substring(dot + 1);
            }
            setResponseHeader(res, ext);
        }
	}

    @Override
    public void destroy() {
    }

	private void setResponseHeader(final HttpServletResponse response, final String ext) {
		if (ext != null && ext.length() > 0) {
			final Integer expires = expiresMap.get(ext);
			if (expires != null) {
				if (expires.intValue() > 0) {
					response.setHeader("Cache-Control", "max-age=" + expires.intValue()); // HTTP 1.1
				} else {
					response.setHeader("Cache-Control", "no-cache");
					response.setHeader("Pragma", "no-cache"); // HTTP 1.0
					response.setDateHeader("Expires", 0);
				}
			}
		}
	}

}
