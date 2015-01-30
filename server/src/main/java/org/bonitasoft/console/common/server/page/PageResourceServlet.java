/**
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.console.common.server.page;

import java.io.File;

import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.bonitasoft.console.common.server.servlet.ResourceServlet;

/**
 * @author Anthony Birembaut
 *
 */
public class PageResourceServlet extends ResourceServlet {

    /**
     * UID
     */
    private static final long serialVersionUID = 6745970275852563050L;

    /**
     * theme name : the theme folder's name
     */
    protected final static String PAGE_PARAM_NAME = "page";

    /**
     * resources subfolder name
     */
    protected final static String RESOURCES_SUBFOLDER_NAME = "resources";

    @Override
    protected String getResourceParameterName() {
        return PAGE_PARAM_NAME;
    }

    @Override
    protected File getResourcesParentFolder(final long tenantId) {
        return WebBonitaConstantsUtils.getInstance(tenantId).getPagesFolder();
    }

    @Override
    protected String getSubFolderName() {
        return RESOURCES_SUBFOLDER_NAME;
    }

}
