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
package org.bonitasoft.console.common.server.servlet;

import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.bonitasoft.console.common.server.preferences.properties.ConsoleProperties;
import org.bonitasoft.console.common.server.preferences.properties.PropertiesFactory;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.forms.server.exception.FileTooBigException;

/**
 * Servlet allowing to upload a file in the tenant common temp folder
 *
 * @author Anthony Birembaut
 */
public class TenantFileUploadServlet extends FileUploadServlet {

    /**
     * UID
     */
    private static final long serialVersionUID = 58370675886169565L;

    @Override
    protected void defineUploadDirectoryPath(final HttpServletRequest request) {
        final long tenantId = getAPISession(request).getTenantId();
        setUploadDirectoryPath(WebBonitaConstantsUtils.getInstance(tenantId).getTempFolder().getPath());
    }

    protected APISession getAPISession(final HttpServletRequest request) {
        final HttpSession session = request.getSession();
        return (APISession) session.getAttribute("apiSession");
    }

    @Override
    protected void checkUploadSize(final HttpServletRequest request, final FileItem item) throws FileTooBigException {
        final long contentSize = item.getSize();
        final long maxSize = getConsoleProperties(getAPISession(request).getTenantId()).getMaxSize();
        if (contentSize > maxSize * 1048576) {
            final String errorMessage = "file " + item.getName() + " too big !";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage);
            }
            throw new FileTooBigException(errorMessage, item.getName(), String.valueOf(maxSize));
        }
    }

    protected ConsoleProperties getConsoleProperties(final long tenantId) {
        return PropertiesFactory.getConsoleProperties(tenantId);
    }

}
