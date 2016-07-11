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

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;

/**
 * Servlet allowing to upload a file in the platform common temp folder
 *
 * @author Anthony Birembaut
 */
public class PlatformFileUploadServlet extends FileUploadServlet {

    /**
     * UID
     */
    private static final long serialVersionUID = 58370675776169565L;

    @Override
    protected void defineUploadDirectoryPath(final HttpServletRequest request) {
        setUploadDirectoryPath(WebBonitaConstantsUtils.getInstance().getTempFolder().getPath());
    }

    @Override
    protected void setUploadSizeMax(final ServletFileUpload serviceFileUpload, final HttpServletRequest request) {
        //currently no limit check on the platform
    }

}
