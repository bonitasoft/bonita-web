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
package org.bonitasoft.console.common.server.preferences.constants;

import java.io.File;

/**
 * @author Nicolas Chabanoles
 */
public interface WebBonitaConstants {

    /**
     * tenants folder
     */
    String tenantsFolderName = "tenants";

    /**
     * Generics folders
     */

    String clientFolderName = "client";

    String confFolderName = "conf";

    String tmpFolderName = "bonita_portal_";

    String formsFolderName = "forms";

    String bdmFolderName = "bdm";

    /**
     * Client
     */
    String clientFolderPath = clientFolderName + File.separator;

    /**
     * Get Tenants Folder Path
     *
     * @return path
     */
    String getTenantsFolderPath();

    /**
     * Get Tenant TempFolder Path
     *
     * @return path
     */
    String getTempFolderPath();

    /**
     * Get Tenant ThemeConsoleFolder Path
     *
     * @return path
     */
    String getThemeFolderPath();

    /**
     * Get Tenant ConfFolder Path
     *
     * @return path
     */
    String getConfFolderPath();

    /**
     * Get Tenant FormsTempFolder Path
     *
     * @return path
     */
    String getFormsTempFolderPath();

    /**
     * Get reportsConsoleTempFolder Path
     *
     * @return path
     */
    String getReportsTempFolderPath();

    /**
     * Get pagesConsoleTempFolder Path
     *
     * @return path
     */
    String getPagesTempFolderPath();

    /**
     * Get BDMTempFolderPath Path
     *
     * @return path
     */
    String getBDMTempFolderPath();

}
