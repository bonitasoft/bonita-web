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

    static final String ICONS_WORK_FOLDER_NAME = "icons";
    private static final String THEME_WORK_FOLDER_NAME = "theme";
    private static final String REPORTS_WORK_FOLDER_NAME = "reports";
    private static final String PAGES_WORK_FOLDER_NAME = "pages";
    private String tenantsFolderPath = null;
    private String tenantFolderPath = null;
    private String tempFolderPath = null;
    private String confFolderPath = null;
    private String formsWorkFolderPath = null;
    private String themeWorkFolderPath = null;
    private String reportsWorkFolderPath = null;
    private String pagesWorkFolderPath = null;
    private String bdmWorkFolderPath;

    /**
     * Default constructor.
     *
     * @param tenantId Tenant Id
     */
    public WebBonitaConstantsTenancyImpl(final long tenantId) {
        tenantFolderPath = getTenantsFolderPath() + tenantId + File.separator;
        tempFolderPath = getTempFolder() + File.separator + tenantsFolderName + File.separator + tenantId + File.separator;
    }

    private String getTempFolder() {
        final String tempDir = System.getProperty("java.io.tmpdir") + File.separator + tmpFolderName + ManagementFactory.getRuntimeMXBean().getName();
        createTempDirectory(new File(tempDir).toURI());
        return tempDir;
    }

    @Override
    public String getTenantsFolderPath() {
        if (tenantsFolderPath == null) {
            tenantsFolderPath = getTempFolder() + File.separator + tenantsFolderName + File.separator;
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
    public String getReportsTempFolderPath() {
        if (reportsWorkFolderPath == null) {
            reportsWorkFolderPath = getTempFolderPath() + REPORTS_WORK_FOLDER_NAME + File.separator;
        }
        return reportsWorkFolderPath;
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
    public String getThemeFolderPath() {
        if (themeWorkFolderPath == null) {
            themeWorkFolderPath = getTempFolderPath() + File.separator + THEME_WORK_FOLDER_NAME + File.separator;
        }
        return themeWorkFolderPath;
    }

    @Override
    public String getBDMTempFolderPath() {
        if (bdmWorkFolderPath == null) {
            bdmWorkFolderPath = getTempFolderPath() + File.separator + bdmFolderName + File.separator;
        }
        return bdmWorkFolderPath;
    }
}
