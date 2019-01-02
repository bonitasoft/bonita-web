/*******************************************************************************
 * Copyright (C) 2015 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation
 * version 2.1 of the License.
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth
 * Floor, Boston, MA 02110-1301, USA.
 ******************************************************************************/

package org.bonitasoft.console.common.server.page;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.bonitasoft.web.toolkit.client.common.util.StringUtil;

/**
 * @author Julien Mege
 */
public class CustomPageRequestModifier {
    
    /**
     * Logger
     */
    private static Logger LOGGER = Logger.getLogger(CustomPageRequestModifier.class.getName());

    public void redirectToValidPageUrl(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final StringBuilder taskURLBuilder = new StringBuilder(request.getContextPath());
        taskURLBuilder.append(request.getServletPath())
                .append(request.getPathInfo())
                .append("/");

        if(!StringUtil.isBlank(request.getQueryString())){
            taskURLBuilder.append("?").append(request.getQueryString());
        }
        response.sendRedirect(response.encodeRedirectURL(taskURLBuilder.toString()));
    }

    public void forwardIfRequestIsAuthorized(final HttpServletRequest request, final HttpServletResponse response, final String apiPathShouldStartWith, final String apiPath) throws IOException, ServletException {
        if (!FilenameUtils.normalize(apiPath).startsWith(apiPathShouldStartWith)) {
            final String message = "attempt to access unauthorized path " + apiPath;
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, message);
            }
            response.sendError(HttpServletResponse.SC_FORBIDDEN, message);
        } else {
            request.getRequestDispatcher(apiPath).forward(request, response);
        }
    }
}
