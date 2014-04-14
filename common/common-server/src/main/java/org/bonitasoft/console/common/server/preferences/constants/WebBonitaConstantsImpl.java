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
public class WebBonitaConstantsImpl implements WebBonitaConstants {

    /**
     * platform folder
     */
    public static final String platformFolderName = "platform";

    public String platformFolderPath = null;

    /**
     * tenant template folder
     */
    public static final String tenantTemplateFolderName = "tenant-template";

    public String tenantTemplateFolderPath = null;

    /**
     * tenants folder
     */
    public String tenantsFolderPath = null;

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

    private String bdmWorkFolderPath;

    /**
     * Default constructor.
     */
    public WebBonitaConstantsImpl() {
    }

    private String getPlatformFolderPath() {
        if (this.platformFolderPath == null) {
            this.platformFolderPath = clientFolderPath + platformFolderName + File.separator;
        }
        return this.platformFolderPath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTenantsFolderPath() {
        if (this.tenantsFolderPath == null) {
            this.tenantsFolderPath = clientFolderPath + tenantsFolderName + File.separator;
        }
        return this.tenantsFolderPath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTenantTemplateFolderPath() {
        if (this.tenantTemplateFolderPath == null) {
            this.tenantTemplateFolderPath = getPlatformFolderPath() + tenantTemplateFolderName + File.separator;
        }
        return this.tenantTemplateFolderPath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTempFolderPath() {
        if (this.tempFolderPath == null) {
            this.tempFolderPath = getPlatformFolderPath() + tmpFolderName + File.separator;
        }
        return this.tempFolderPath;
    }

    @Override
    public String getThemePortalFolderPath() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getConfFolderPath() {
        if (this.confFolderPath == null) {
            this.confFolderPath = getPlatformFolderPath() + confFolderName + File.separator;
        }
        return this.confFolderPath;
    }

    @Override
    public String getPortalDefaultIconsFolderPath() {
        return null;
    }

    @Override
    public String getPortalProcessIconsFolderPath() {
        return null;
    }

    @Override
    public String getPortalUserIconsFolderPath() {
        return null;
    }

    @Override
    public String getPortalRoleIconsFolderPath() {
        return null;
    }

    @Override
    public String getPortalProfilesIconsFolderPath() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProfilesWorkFolderPath() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getReportsWorkFolderPath() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getWorkFolderPath() {
        if (this.workFolderPath == null) {
            this.workFolderPath = getPlatformFolderPath() + workFolderName + File.separator;
        }
        return this.workFolderPath;
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

    @Override
    public String getPDFTemplateFolderPath() {
        return null;
    }

    @Override
    public String getPortalGroupIconsFolderPath() {
        return null;
    }

    @Override
    public String getBDMWorkFolderPath() {
        if (this.bdmWorkFolderPath == null) {
            this.bdmWorkFolderPath = getWorkFolderPath() + File.separator + bdmFolderName + File.separator;
        }
        return this.bdmWorkFolderPath;
    }

}
