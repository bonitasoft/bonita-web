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

    private String consoleWorkFolderPath = null;

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

    public static final String looknfeelWorkFolderName = "looknfeel";

    private String looknfeelWorkFolderPath = null;

    public static final String profilesWorkFolderName = "profiles";

    private String profilesWorkFolderPath = null;

    public static final String reportsWorkFolderName = "reports";

    private String reportsWorkFolderPath = null;

    public static final String pdfWorkFolderName = "pdf";

    private String pdfWorkFolderPath = null;

    /**
     * Default constructor.
     * 
     * @param tenantId
     *            Tenant Id
     */
    public WebBonitaConstantsTenancyImpl(final long tenantId) {
        this.tenantFolderPath = getTenantsFolderPath() + tenantId + File.separator;
    }

    @Override
    public String getTenantsFolderPath() {
        if (this.tenantsFolderPath == null) {
            this.tenantsFolderPath = clientFolderPath + tenantsFolderName + File.separator;;
        }
        return this.tenantsFolderPath;
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
        if (this.tempFolderPath == null) {
            this.tempFolderPath = this.tenantFolderPath + tmpFolderName + File.separator;
        }
        return this.tempFolderPath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getConfFolderPath() {
        if (this.confFolderPath == null) {
            this.confFolderPath = this.tenantFolderPath + confFolderName + File.separator;
        }
        return this.confFolderPath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getWorkFolderPath() {
        if (this.workFolderPath == null) {
            this.workFolderPath = this.tenantFolderPath + workFolderName + File.separator;
        }
        return this.workFolderPath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProfilesWorkFolderPath() {
        if (this.profilesWorkFolderPath == null) {
            this.profilesWorkFolderPath = getWorkFolderPath() + profilesWorkFolderName + File.separator;
        }
        return this.profilesWorkFolderPath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getReportsWorkFolderPath() {
        if (this.reportsWorkFolderPath == null) {
            this.reportsWorkFolderPath = getWorkFolderPath() + reportsWorkFolderName + File.separator;
        }
        return this.reportsWorkFolderPath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFormsWorkFolderPath() {
        if (this.formsWorkFolderPath == null) {
            this.formsWorkFolderPath = getWorkFolderPath() + formsFolderName + File.separator;
        }
        return this.formsWorkFolderPath;
    }

    private String getIconsWorkFolderPath() {
        if (this.iconsWorkFolderPath == null) {
            this.iconsWorkFolderPath = getWorkFolderPath() + iconsWorkFolderName + File.separator;
        }
        return this.iconsWorkFolderPath;
    }

    @Override
    public String getConsoleDefaultIconsFolderPath() {
        if (this.defaultIconsWorkFolderPath == null) {
            this.defaultIconsWorkFolderPath = getIconsWorkFolderPath() + defaultIconsWorkFolderName + File.separator;
        }
        return this.iconsWorkFolderPath;
    }

    @Override
    public String getConsoleUserIconsFolderPath() {
        if (this.usersIconsWorkFolderPath == null) {
            this.usersIconsWorkFolderPath = getIconsWorkFolderPath() + usersIconsWorkFolderName + File.separator;
        }
        return this.usersIconsWorkFolderPath;
    }

    @Override
    public String getConsoleRoleIconsFolderPath() {
        if (this.rolesIconsWorkFolderPath == null) {
            this.rolesIconsWorkFolderPath = getIconsWorkFolderPath() + ROLES_ICONS_FOLDER_NAME + File.separator;
        }
        return this.rolesIconsWorkFolderPath;
    }

    @Override
    public String getConsoleProcessIconsFolderPath() {
        if (this.processesIconsWorkFolderPath == null) {
            this.processesIconsWorkFolderPath = getIconsWorkFolderPath() + processesIconsWorkFolderName + File.separator;
        }
        return this.processesIconsWorkFolderPath;
    }

    @Override
    public String getConsoleProfilesIconsFolderPath() {
        if (this.profilesIconsWorkFolderPath == null) {
            this.profilesIconsWorkFolderPath = getIconsWorkFolderPath() + profilesIconsWorkFolderName + File.separator;
        }
        return this.profilesIconsWorkFolderPath;
    }

    @Override
    public String getThemeConsoleFolderPath() {
        if (this.looknfeelWorkFolderPath == null) {
            this.looknfeelWorkFolderPath = getWorkFolderPath() + looknfeelWorkFolderName + File.separator;
        }
        return this.looknfeelWorkFolderPath;
    }

    @Override
    public String getPDFTemplateFolderPath() {
        if (this.pdfWorkFolderPath == null) {
            this.pdfWorkFolderPath = getWorkFolderPath() + pdfWorkFolderName + File.separator;
        }
        return this.pdfWorkFolderPath;
    }

    @Override
    public String getConsoleGroupIconsFolderPath() {
        if (this.groupsIconsWorkFolderPath == null) {
            this.groupsIconsWorkFolderPath = getIconsWorkFolderPath() + WebBonitaConstants.GROUPS_ICONS_FOLDER_NAME + File.separator;
        }
        return this.groupsIconsWorkFolderPath;
    }
}
