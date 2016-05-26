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
import java.lang.management.ManagementFactory;
import java.nio.file.Paths;

/**
 * @author Ruiheng.Fan
 */
public class WebBonitaConstantsImpl implements WebBonitaConstants {

    /**
     * platform folder
     */
    private static final String platformFolderName = "platform";

    private String platformFolderPath = null;

    /**
     * tenants folder
     */
    private String tenantsFolderPath = null;

    /**
     * tmp
     */
    private String tempFolderPath = null;

    /**
     * conf
     */
    private String confFolderPath = null;

    private String formsWorkFolderPath = null;

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
            tenantsFolderPath = Paths.get(getTempFolderPath()).resolveSibling(tenantsFolderName).toString() + File.separator;
        }
        return tenantsFolderPath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTempFolderPath() {
        if (tempFolderPath == null) {
            // We use a tempFolder specific to the running JVM, so that 2 JVMs running on the same machine are isolated:
            tempFolderPath = System.getProperty("java.io.tmpdir") + File.separator + tmpFolderName + ManagementFactory.getRuntimeMXBean().getName()
                    + File.separator + platformFolderName + File.separator;
        }
        return tempFolderPath;
    }

    @Override
    public String getThemeFolderPath() {
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

    /**
     * {@inheritDoc}
     */
    @Override
    public String getReportsTempFolderPath() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPagesTempFolderPath() {
        return null;
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
        return null; // does not means anything at platform level
    }

}
