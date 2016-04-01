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

import static org.bonitasoft.engine.io.IOUtil.createTempDirectory;

import java.io.File;
import java.lang.management.ManagementFactory;

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

    public static final String pagesWorkFolderName = "pages";

    private String pagesWorkFolderPath = null;

    public static final String pdfWorkFolderName = "pdf";

    private String pdfWorkFolderPath = null;

    private String bdmWorkFolderPath;

    /**
     * Default constructor.
     *
     * @param tenantId Tenant Id
     */
    public WebBonitaConstantsTenancyImpl(final long tenantId) {
        tenantFolderPath = getTenantsFolderPath() + tenantId + File.separator;
        tempFolderPath = getTempFolder() + File.separator
                + tenantsFolderName + File.separator + tenantId + File.separator;
    }

    private String getTempFolder() {
        final String tempDir = System.getProperty("java.io.tmpdir") + File.separator + tmpFolderName + ManagementFactory.getRuntimeMXBean().getName();
        createTempDirectory(new File(tempDir).toURI());
        return tempDir;
    }

    @Override
    public String getTenantsFolderPath() {
        if (tenantsFolderPath == null) {
            tenantsFolderPath = clientFolderPath + tenantsFolderName + File.separator;
        }
        return tenantsFolderPath;
    }

    public String getTenantFolderPath() {
        return tenantFolderPath;
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
        return tempFolderPath;
    }

    private String getWorkFolderPath() {
        return getTenantFolderPath() + "work" + File.separator;
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
    public String getProfilesTempFolderPath() {
        if (profilesWorkFolderPath == null) {
            profilesWorkFolderPath = getTempFolderPath() + profilesWorkFolderName + File.separator;
        }
        return profilesWorkFolderPath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getReportsTempFolderPath() {
        if (reportsWorkFolderPath == null) {
            reportsWorkFolderPath = getTempFolderPath() + reportsWorkFolderName + File.separator;
        }
        return reportsWorkFolderPath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPagesTempFolderPath() {
        if (pagesWorkFolderPath == null) {
            pagesWorkFolderPath = getTempFolderPath() + pagesWorkFolderName + File.separator;
        }
        return pagesWorkFolderPath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFormsTempFolderPath() {
        if (formsWorkFolderPath == null) {
            formsWorkFolderPath = getTempFolderPath() + formsFolderName + File.separator;
        }
        return formsWorkFolderPath;
    }

    private String getIconsWorkFolderPath() {
        if (iconsWorkFolderPath == null) {
            // icons are now stored inside default theme:
            iconsWorkFolderPath = getThemePortalFolderPath() + iconsWorkFolderName + File.separator;
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
            themeWorkFolderPath = getTempFolderPath() + File.separator + themeWorkFolderName;
        }
        return themeWorkFolderPath;
    }

    @Override
    public String getPDFTemplateFolderPath() {
        if (pdfWorkFolderPath == null) {
            pdfWorkFolderPath = getTempFolderPath() + pdfWorkFolderName + File.separator;
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
    public String getBDMTempFolderPath() {
        if (bdmWorkFolderPath == null) {
            bdmWorkFolderPath = getTempFolderPath() + File.separator + bdmFolderName + File.separator;
        }
        return bdmWorkFolderPath;
    }
}
