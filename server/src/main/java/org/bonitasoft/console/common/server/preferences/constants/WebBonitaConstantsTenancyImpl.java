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

    private static final String PAGES_WORK_FOLDER_NAME = "pages";
    private String tenantsFolderPath = null;
    private final String tempFolderPath;
    private String formsWorkFolderPath = null;
    private String pagesWorkFolderPath = null;
    private String bdmWorkFolderPath;

    /**
     * Default constructor.
     *
     */
    WebBonitaConstantsTenancyImpl() {
        tempFolderPath = rootTempDir + File.separator + tenantsFolderName + File.separator;
    }

    @Override
    public String getTenantsFolderPath() {
        if (tenantsFolderPath == null) {
            tenantsFolderPath = rootTempDir + File.separator + tenantsFolderName + File.separator;
        }
        return tenantsFolderPath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTempFolderPath() {
        return tempFolderPath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPagesTempFolderPath() {
        if (pagesWorkFolderPath == null) {
            pagesWorkFolderPath = getTempFolderPath() + PAGES_WORK_FOLDER_NAME + File.separator;
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

    @Override
    public String getBDMTempFolderPath() {
        if (bdmWorkFolderPath == null) {
            bdmWorkFolderPath = getTempFolderPath() + File.separator + bdmFolderName + File.separator;
        }
        return bdmWorkFolderPath;
    }
}
