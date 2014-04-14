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
 * @author Ruiheng.Fan
 */
public class WebBonitaConstantsTenancyImpl implements WebBonitaConstants {

    public String tenantsFolderPath = null;

    public String tenantFolderPath = null;

    /**
     * tmp
     */
    private String tempFolderPath = null;

    /**
     * conf
     */
    private String confFolderPath = null;

    /**
     * work
     */
    private String workFolderPath = null;

    private final String consoleWorkFolderPath = null;

    private String formsWorkFolderPath = null;

    public static final String gadgetsWorkFolderName = "gadgets";

    public static final String iconsWorkFolderName = "icons";

    private String iconsWorkFolderPath = null;

    public static final String defaultIconsWorkFolderName = "default";

    private String defaultIconsWorkFolderPath = null;

    public static final String processesIconsWorkFolderName = "processes";

    private String processesIconsWorkFolderPath = null;

    public static final String usersIconsWorkFolderName = "users";

    private String usersIconsWorkFolderPath = null;

    private String groupsIconsWorkFolderPath;

    private String rolesIconsWorkFolderPath = null;

    public static final String profilesIconsWorkFolderName = "profiles";

    private String profilesIconsWorkFolderPath = null;

    public static final String themeWorkFolderName = "theme";

    private String themeWorkFolderPath = null;

    public static final String profilesWorkFolderName = "profiles";

    private String profilesWorkFolderPath = null;

    public static final String reportsWorkFolderName = "reports";

    private String reportsWorkFolderPath = null;

    public static final String pdfWorkFolderName = "pdf";

    private String pdfWorkFolderPath = null;

    private String bdmWorkFolderPath;

    /**
     * Default constructor.
     * 
     * @param tenantId
     *            Tenant Id
     */
    public WebBonitaConstantsTenancyImpl(final long tenantId) {
        tenantFolderPath = getTenantsFolderPath() + tenantId + File.separator;
    }

    @Override
    public String getTenantsFolderPath() {
        if (tenantsFolderPath == null) {
            tenantsFolderPath = clientFolderPath + tenantsFolderName + File.separator;;
        }
        return tenantsFolderPath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTenantTemplateFolderPath() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTempFolderPath() {
        if (tempFolderPath == null) {
            tempFolderPath = tenantFolderPath + tmpFolderName + File.separator;
        }
        return tempFolderPath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getConfFolderPath() {
        if (confFolderPath == null) {
            confFolderPath = tenantFolderPath + confFolderName + File.separator;
        }
        return confFolderPath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getWorkFolderPath() {
        if (workFolderPath == null) {
            workFolderPath = tenantFolderPath + workFolderName + File.separator;
        }
        return workFolderPath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProfilesWorkFolderPath() {
        if (profilesWorkFolderPath == null) {
            profilesWorkFolderPath = getWorkFolderPath() + profilesWorkFolderName + File.separator;
        }
        return profilesWorkFolderPath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getReportsWorkFolderPath() {
        if (reportsWorkFolderPath == null) {
            reportsWorkFolderPath = getWorkFolderPath() + reportsWorkFolderName + File.separator;
        }
        return reportsWorkFolderPath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFormsWorkFolderPath() {
        if (formsWorkFolderPath == null) {
            formsWorkFolderPath = getWorkFolderPath() + formsFolderName + File.separator;
        }
        return formsWorkFolderPath;
    }

    private String getIconsWorkFolderPath() {
        if (iconsWorkFolderPath == null) {
            iconsWorkFolderPath = getWorkFolderPath() + iconsWorkFolderName + File.separator;
        }
        return iconsWorkFolderPath;
    }

    @Override
    public String getPortalDefaultIconsFolderPath() {
        if (defaultIconsWorkFolderPath == null) {
            defaultIconsWorkFolderPath = getIconsWorkFolderPath() + defaultIconsWorkFolderName + File.separator;
        }
        // FIXME: must return defaultIconsWorkFolderPath not iconsWorkFolderPath
        return iconsWorkFolderPath;
    }

    @Override
    public String getPortalUserIconsFolderPath() {
        if (usersIconsWorkFolderPath == null) {
            usersIconsWorkFolderPath = getIconsWorkFolderPath() + usersIconsWorkFolderName + File.separator;
        }
        return usersIconsWorkFolderPath;
    }

    @Override
    public String getPortalRoleIconsFolderPath() {
        if (rolesIconsWorkFolderPath == null) {
            rolesIconsWorkFolderPath = getIconsWorkFolderPath() + ROLES_ICONS_FOLDER_NAME + File.separator;
        }
        return rolesIconsWorkFolderPath;
    }

    @Override
    public String getPortalProcessIconsFolderPath() {
        if (processesIconsWorkFolderPath == null) {
            processesIconsWorkFolderPath = getIconsWorkFolderPath() + processesIconsWorkFolderName + File.separator;
        }
        return processesIconsWorkFolderPath;
    }

    @Override
    public String getPortalProfilesIconsFolderPath() {
        if (profilesIconsWorkFolderPath == null) {
            profilesIconsWorkFolderPath = getIconsWorkFolderPath() + profilesIconsWorkFolderName + File.separator;
        }
        return profilesIconsWorkFolderPath;
    }

    @Override
    public String getThemePortalFolderPath() {
        if (themeWorkFolderPath == null) {
            themeWorkFolderPath = getWorkFolderPath() + themeWorkFolderName + File.separator;
        }
        return themeWorkFolderPath;
    }

    @Override
    public String getPDFTemplateFolderPath() {
        if (pdfWorkFolderPath == null) {
            pdfWorkFolderPath = getWorkFolderPath() + pdfWorkFolderName + File.separator;
        }
        return pdfWorkFolderPath;
    }

    @Override
    public String getPortalGroupIconsFolderPath() {
        if (groupsIconsWorkFolderPath == null) {
            groupsIconsWorkFolderPath = getIconsWorkFolderPath() + WebBonitaConstants.GROUPS_ICONS_FOLDER_NAME + File.separator;
        }
        return groupsIconsWorkFolderPath;
    }

    @Override
    public String getBDMWorkFolderPath() {
        if (this.bdmWorkFolderPath == null) {
            this.bdmWorkFolderPath = getWorkFolderPath() + File.separator + bdmFolderName + File.separator;
        }
        return this.bdmWorkFolderPath;
    }
}
