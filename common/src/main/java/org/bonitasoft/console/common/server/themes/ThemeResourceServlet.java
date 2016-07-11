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
package org.bonitasoft.console.common.server.themes;

import java.io.File;

import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.bonitasoft.console.common.server.servlet.ResourceServlet;

/**
 * @author Minghui Dai
 * @author Anthony Birembaut
 */
public class ThemeResourceServlet extends ResourceServlet {

    /**
     * UID
     */
    private static final long serialVersionUID = 6745970275852563050L;

    /**
     * theme name : the theme folder's name
     */
    protected final static String THEME_PARAM_NAME = "theme";

    protected final static String PORTAL_THEME_NAME = "portal";

    @Override
    protected String getResourceParameterName() {
        return THEME_PARAM_NAME;
    }

    @Override
    protected File getResourcesParentFolder(final long tenantId) {
        return WebBonitaConstantsUtils.getInstance(tenantId).getPortalThemeFolder();
    }

    @Override
    protected String getSubFolderName() {
        return null;
    }

    @Override
    protected String getDefaultResourceName() {
        return PORTAL_THEME_NAME;
    }

}
