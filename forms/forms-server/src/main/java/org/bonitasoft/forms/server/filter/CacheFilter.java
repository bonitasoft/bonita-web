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

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Haojie Yuan
 * 
 */
public class CacheFilter implements Filter {

	protected FilterConfig filterConfig = null;
	
	protected HashMap expiresMap = new HashMap();
	
	/**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(CacheFilter.class.getName());

	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		expiresMap.clear();
		Enumeration names = filterConfig.getInitParameterNames();
		while (names.hasMoreElements()) {
            final String name = (String) names.nextElement();
            final String value = filterConfig.getInitParameter(name);
            try {
                final Integer expire = Integer.valueOf(value);
                expiresMap.put(name, expire);
            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, name + " parameter value should be an integer");
            }
        }
    }

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		final String uri = req.getRequestURI();
		if (!uri.endsWith("nocache.js")) {
    		String ext = null;
    		int dot = uri.lastIndexOf(".");
    		if (dot != -1) {
    			ext = uri.substring(dot + 1);
    		}
    		setResponseHeader(res, ext);
	    }
		chain.doFilter(req, res);
	}

	public void destroy() {
		this.filterConfig = null;
	}

	private void setResponseHeader(HttpServletResponse response, String ext) {
		if (ext != null && ext.length() > 0) {
			final Integer expires = (Integer) expiresMap.get(ext);
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
