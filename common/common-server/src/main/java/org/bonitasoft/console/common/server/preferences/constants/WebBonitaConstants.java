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

    public static final String BONITA_HOME = "bonita.home";

    public static final String JAVA_IO_TMPDIR = "java.io.tmpdir";

    /**
     * tenants folder
     */
    public static final String tenantsFolderName = "tenants";

    /**
     * Generics folders
     */

    public static final String clientFolderName = "client";

    public static final String confFolderName = "conf";

    public static final String scriptsFolderName = "scripts";

    public static final String tmpFolderName = "tmp";

    public static final String workFolderName = "work";

    public static final String formsFolderName = "forms";

    public static final String bdmFolderName = "bdm";

    public static final String ROLES_ICONS_FOLDER_NAME = "roles";

    public static final String GROUPS_ICONS_FOLDER_NAME = "groups";

    public static final String PROFILES_ICONS_FOLDER_NAME = "profiles";

    /**
     * Client
     */
    public static final String clientFolderPath = clientFolderName + File.separator;

    /**
     * Get Tenants Folder Path
     * 
     * @return path
     */
    public String getTenantsFolderPath();

    /**
     * Get Tenants Folder Path
     * 
     * @return path
     */
    public String getTenantTemplateFolderPath();

    /**
     * Get Tenant TempFolder Path
     * 
     * @return path
     */
    public String getTempFolderPath();

    /**
     * Get Tenant ThemeConsoleFolder Path
     * 
     * @return path
     */
    public String getThemePortalFolderPath();

    /**
     * Get Tenant ConfFolder Path
     * 
     * @return path
     */
    public String getConfFolderPath();

    /**
     * Get Tenant pdf template Path
     * 
     * @return path
     */
    public String getPDFTemplateFolderPath();

    /**
     * Get Tenant WorkFolder Path
     * 
     * @return path
     */
    public String getWorkFolderPath();

    /**
     * Get Tenant FormsWorkFolder Path
     * 
     * @return path
     */
    public String getFormsWorkFolderPath();

    /**
     * Get Tenant Default icons Path
     * 
     * @return
     */
    public String getPortalDefaultIconsFolderPath();

    /**
     * Get Tenant process icons Path
     * 
     * @return
     */
    public String getPortalProcessIconsFolderPath();

    /**
     * Get Tenant user icons Path
     * 
     * @return
     */
    public String getPortalUserIconsFolderPath();

    /**
     * Get Tenant user icons Path
     * 
     * @return
     */
    public String getPortalRoleIconsFolderPath();

    /**
     * Get Tenant profile icons Path
     * 
     * @return
     */
    public String getPortalProfilesIconsFolderPath();

    /**
     * Get profilesConsoleWorkFolder Path
     * 
     * @return path
     */
    public String getProfilesWorkFolderPath();

    /**
     * Get reportsConsoleWorkFolder Path
     * 
     * @return path
     */
    public String getReportsWorkFolderPath();

    /**
     * Get pagesConsoleWorkFolder Path
     * 
     * @return path
     */
    public String getPagesWorkFolderPath();

    /**
     * Get groupsConsoleWorkFolder Path
     * 
     * @return path
     */
    public String getPortalGroupIconsFolderPath();

    /**
     * Get BDMWorkFolderPath Path
     * 
     * @return path
     */
    public String getBDMWorkFolderPath();

}
