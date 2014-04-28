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

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstants;

/**
 * Check the bonita home
 * 
 * @author Haojie Yuan
 */
public class BonitaHomeFilter implements Filter {

    /**
     * BONITA_HOME not specified
     */
    protected static final String NO_BONITA_HOME_MESSAGE = "noBonitaHomeMessage";

    /**
     * bonita-client conf file not found
     */
    protected static final String NO_BONITA_CLIENT_MESSAGE = "noBonitaClientFileMessage";

    /**
     * bonita-client conf file
     */
    protected static final String BONITA_CLIENT_CONF_FILENAME = "bonita-client.properties";

    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(BonitaHomeFilter.class.getName());

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
        final String theBonitaHome = System.getProperty(WebBonitaConstants.BONITA_HOME);
        final File bonitaClientFile = new File(theBonitaHome + File.separator + "client" + File.separator + "conf" + File.separator
                + BONITA_CLIENT_CONF_FILENAME);

        if (theBonitaHome == null || theBonitaHome.isEmpty()) {
            request.setAttribute(NO_BONITA_HOME_MESSAGE, NO_BONITA_HOME_MESSAGE);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "The Bonita Home property is not set or the target directory is empty");
            }
        } else if (!bonitaClientFile.isFile()) {
            request.setAttribute(NO_BONITA_CLIENT_MESSAGE, NO_BONITA_CLIENT_MESSAGE);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "Bonita client file is missing (" + bonitaClientFile.getPath() + ")");
            }
        }
        chain.doFilter(request, response);
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
