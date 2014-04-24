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

    private String formsWorkFolderPath = null;

    private final String workLibFolderPath = null;

    private String bdmWorkFolderPath;

    /**
     * Default constructor.
     */
    public WebBonitaConstantsImpl() {
    }

    private String getPlatformFolderPath() {
        if (platformFolderPath == null) {
            platformFolderPath = clientFolderPath + platformFolderName + File.separator;
        }
        return platformFolderPath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTenantsFolderPath() {
        if (tenantsFolderPath == null) {
            tenantsFolderPath = clientFolderPath + tenantsFolderName + File.separator;
        }
        return tenantsFolderPath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTenantTemplateFolderPath() {
        if (tenantTemplateFolderPath == null) {
            tenantTemplateFolderPath = getPlatformFolderPath() + tenantTemplateFolderName + File.separator;
        }
        return tenantTemplateFolderPath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTempFolderPath() {
        if (tempFolderPath == null) {
            tempFolderPath = getPlatformFolderPath() + tmpFolderName + File.separator;
        }
        return tempFolderPath;
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
        if (confFolderPath == null) {
            confFolderPath = getPlatformFolderPath() + confFolderName + File.separator;
        }
        return confFolderPath;
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
    public String getPagesWorkFolderPath() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getWorkFolderPath() {
        if (workFolderPath == null) {
            workFolderPath = getPlatformFolderPath() + workFolderName + File.separator;
        }
        return workFolderPath;
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
        if (bdmWorkFolderPath == null) {
            bdmWorkFolderPath = getWorkFolderPath() + File.separator + bdmFolderName + File.separator;
        }
        return bdmWorkFolderPath;
    }

}
