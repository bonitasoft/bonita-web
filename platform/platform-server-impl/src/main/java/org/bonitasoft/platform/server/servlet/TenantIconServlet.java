/**
 * Copyright (C) 2011 BonitaSoft S.A.
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
package org.bonitasoft.platform.server.servlet;

import javax.servlet.http.HttpServletRequest;

import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;

/**
 * @author Anthony Birembaut
 * 
 */
public class TenantIconServlet extends org.bonitasoft.web.toolkit.server.servlet.AttachmentImageServlet {

    /**
     * UID
     */
    private static final long serialVersionUID = 4152576581869952546L;

    @Override
    protected void defineDirectoryPath(final HttpServletRequest request) {
        setDirectoryPath(WebBonitaConstantsUtils.getInstance().getTenantsFolder().getPath());
    }
}
