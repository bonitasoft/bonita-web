/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.console.common.server.themes;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.bonitasoft.console.common.server.utils.UnzipUtil;

/**
 * @author Fabio Lombardi
 * 
 */
public class ThemeManager {

    public static final String THEME_PORTAL_FOLDER_NAME = "portal";
    private static final String COMPILED_CSS_FILE = "bonita.css";
    public final WebBonitaConstantsUtils constants;

    /**
     * Default Constructor.
     * 
     * @param tenantId
     */
    public ThemeManager(final WebBonitaConstantsUtils constants) {
        this.constants = constants;
    }

    public void applyAlreadyCompiledTheme(byte[] zipContent, byte[] cssContent) throws FileNotFoundException, IOException {
        String pathToDestination = constants.getPortalThemeFolder().getPath() + File.separator + getThemeTypeFolderName();
        UnzipUtil.unzip(new ByteArrayInputStream(zipContent), pathToDestination);
        if (cssContent != null) {
            FileUtils.writeByteArrayToFile(new File(pathToDestination, getCssDestinationFileName()), cssContent);
        }
    }

    protected String getThemeTypeFolderName() {
        return THEME_PORTAL_FOLDER_NAME;
    }

    protected String getCssDestinationFileName() {
        return COMPILED_CSS_FILE;
    }

    protected void deleteUnzippedThemeFolder(File unzippedThemeFolder) throws IOException {
        if(unzippedThemeFolder!=null){
            //clean the temp folder if the validation failed or if the move failed
            FileUtils.deleteDirectory(unzippedThemeFolder);
        }
    }

}
