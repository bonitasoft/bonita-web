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
import java.util.HashMap;
import java.util.Map;

/**
 * @author Anthony Birembaut
 */
public class WebBonitaConstantsUtils {

    protected static Map<Long, WebBonitaConstantsUtils> tenantConstantsUtils = new HashMap<Long, WebBonitaConstantsUtils>();

    protected WebBonitaConstants webBonitaConstants;

    public synchronized static WebBonitaConstantsUtils getInstance(final long tenantId) {
        WebBonitaConstantsUtils instance = tenantConstantsUtils.get(tenantId);
        if (instance == null) {
            instance = new WebBonitaConstantsUtils(tenantId);
            tenantConstantsUtils.put(tenantId, instance);
        }
        return instance;
    }

    public synchronized static WebBonitaConstantsUtils getInstance() {
        WebBonitaConstantsUtils instance = tenantConstantsUtils.get(null);
        if (instance == null) {
            instance = new WebBonitaConstantsUtils();
            tenantConstantsUtils.put(null, instance);
        }
        return instance;
    }

    protected WebBonitaConstantsUtils(final long tenantId) {
        webBonitaConstants = new WebBonitaConstantsTenancyImpl(tenantId);
    }

    protected WebBonitaConstantsUtils() {
        webBonitaConstants = new WebBonitaConstantsImpl();
    }

    /**
     * Get the folder where to write Tenant temporary files commons to all web
     * applications.
     * 
     * @throws ConsoleException
     */
    public File getTempFolder() {
        return getFolderFromBonitaHome(webBonitaConstants.getTempFolderPath());
    }

    /**
     * Get the folder where to get the Tenant conf files.
     * 
     * @throws ConsoleException
     */
    public File getConfFolder() {
        return getFolderFromBonitaHome(webBonitaConstants.getConfFolderPath());
    }

    /**
     * Get the folder of Tenant themes files (ie CSS files)
     * 
     * @throws ConsoleException
     */
    public File getPortalThemeFolder() {
        return getFolderFromBonitaHome(webBonitaConstants.getThemePortalFolderPath());
    }

    /**
     * Get the folder of the user Console default icons
     * 
     * @throws ConsoleException
     */
    public File getConsoleDefaultIconsFolder() {
        return getFolderFromBonitaHome(webBonitaConstants.getPortalDefaultIconsFolderPath());
    }

    /**
     * Get the folder of the user Console process icons
     * 
     * @throws ConsoleException
     */
    public File getConsoleProcessIconsFolder() {
        return getFolderFromBonitaHome(webBonitaConstants.getPortalProcessIconsFolderPath());
    }

    /**
     * /** Get the folder of the user Console user icons
     * 
     * @throws ConsoleException
     */
    public File getConsoleUserIconsFolder() {
        return getFolderFromBonitaHome(webBonitaConstants.getPortalUserIconsFolderPath());
    }

    /**
     * 
     * Get the folder of the user Console user icons
     * 
     * @throws ConsoleException
     */
    public File getConsoleRoleIconsFolder() {
        return getFolderFromBonitaHome(webBonitaConstants.getPortalRoleIconsFolderPath());
    }

    /**
     * Get the folder of Tenant report files
     * 
     * @throws ConsoleException
     */
    public File getReportFolder() {
        return getFolderFromBonitaHome(webBonitaConstants.getReportsWorkFolderPath());
    }

    /**
     * Get the profile folder to store the report dashboard configuration.
     * 
     * @throws ConsoleException
     */
    public File getProfileWorkFolder() {
        return getFolderFromBonitaHome(webBonitaConstants.getProfilesWorkFolderPath());
    }

    /**
     * Get the Tenant folder where to write Work files.
     * 
     * @throws ConsoleException
     */
    public File getFormsWorkFolder() {
        return getFolderFromBonitaHome(webBonitaConstants.getFormsWorkFolderPath());
    }

    /**
     * Get the folder of the tenants directories
     * 
     * @throws ConsoleException
     */
    public File getTenantsFolder() {
        return getFolderFromBonitaHome(webBonitaConstants.getTenantsFolderPath());
    }

    /**
     * Get the folder of the tenant-template file structure
     * 
     * @return
     */
    public File getTenantTemplateFolder() {
        return getFolderFromBonitaHome(webBonitaConstants.getTenantTemplateFolderPath());
    }

    /**
     * 
     * Get the folder of the Console group icons
     * 
     * @throws ConsoleException
     */
    public File getConsoleGroupIconsFolder() {
        return getFolderFromBonitaHome(webBonitaConstants.getPortalGroupIconsFolderPath());
    }

    /**
     * 
     * Get the folder of the Console profile icons
     * 
     * @throws ConsoleException
     */
    public File getConsoleProfileIconsFolder() {
        return getFolderFromBonitaHome(webBonitaConstants.getPortalProfilesIconsFolderPath());
    }

    // protected for test stubbing
    protected String getBonitaHomePath() {
        final String bonitaHomePath = System.getProperty(WebBonitaConstants.BONITA_HOME);
        if (bonitaHomePath == null) {
            throw new RuntimeException(WebBonitaConstants.BONITA_HOME + " system property not set!");
        }
        return bonitaHomePath;
    }

    private File getFolderFromBonitaHome(final String folderPath) {
        return getFolder(getBonitaHomePath(), folderPath);
    }

    private File getFolder(final String bonitaHomePath, final String folderPath) {
        final File folder = new File(bonitaHomePath, folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder;
    }
}
