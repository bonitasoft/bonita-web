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

import org.bonitasoft.web.toolkit.client.common.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Julien Mege
 */
public class CustomPageRequestModifier {

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

}
