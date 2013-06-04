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

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.bonitasoft.theme.exception.ThemeDescriptorNotFoundException;
import org.bonitasoft.theme.model.ThemeDescriptor;

/**
 * @author Cuisha Gai
 * 
 */
public class ThemeValidator {

    private static final String CONSOLE_TEMPLATE_FILE = "BonitaConsole.html";

    private static final String THEME_DESCRIPTOR_FILE = "themeDescriptor.xml";

    /**
     * Do theme package validate
     * 
     * @param themePath
     * @param isUpload
     * @throws IOException
     * @throws FileNotFoundException
     */
    public void doValidate(final long tenantId, final String themePath, final boolean isUpload) throws ThemeStructureException, IOException {
        final File themeFolder = new File(themePath);
        final String themeFolerName = themeFolder.getName();
        boolean HAS_CONSOLE_TEMPLATE_FILE = false;
        final boolean HAS_PEVIEW_IMAGE_FILE = false;
        boolean HAS_THEME_DESCRIPTOR_FILE = false;
        final ThemeStructureState state = new ThemeStructureState();

        final File[] themeFiles = themeFolder.listFiles(new FileFilter() {

            @Override
            public boolean accept(final File aFile) {
                return aFile.isFile();
            }
        });
        for (final File file : themeFiles) {
            final String fileName = file.getName();
            if (!HAS_CONSOLE_TEMPLATE_FILE) {
                HAS_CONSOLE_TEMPLATE_FILE = fileName.equals(CONSOLE_TEMPLATE_FILE);
            }
            if (!HAS_THEME_DESCRIPTOR_FILE) {
                HAS_THEME_DESCRIPTOR_FILE = fileName.equals(THEME_DESCRIPTOR_FILE);
            }
            if (HAS_PEVIEW_IMAGE_FILE && HAS_CONSOLE_TEMPLATE_FILE && HAS_THEME_DESCRIPTOR_FILE) {
                break;
            }
        }

        state.setMissBonitaConsoleHTML(!HAS_CONSOLE_TEMPLATE_FILE);
        state.setMissThemeDescriptor(!HAS_THEME_DESCRIPTOR_FILE);

        final ThemeManager manager = new ThemeManager();
        if (HAS_THEME_DESCRIPTOR_FILE) {
            try {
                final ThemeDescriptor aThemeDescriptor = manager.getThemeDescriptor(themeFolerName, themeFolder.getParentFile());
                if (aThemeDescriptor == null || aThemeDescriptor.getName() == null || aThemeDescriptor.getName().equals("null")) {
                    state.setThemeDescriptorError(true);
                } else if (isUpload && !state.hasError()) {
                    final File themeHome = WebBonitaConstantsUtils.getInstance(tenantId).getConsoleThemeFolder();
                    final File aimThemesFolder = new File(themeHome, aThemeDescriptor.getName());
                    if (aimThemesFolder.exists()) {
                        state.setThemeAlreadyExist(true);
                        throw new ThemeStructureException(state);
                    } else {
                        manager.copyToThemesFolder(tenantId, themeFolder, aThemeDescriptor.getName());
                    }
                }
            } catch (final ThemeDescriptorNotFoundException e) {
                state.setThemeDescriptorError(true);
            } catch (final IOException e) {
                state.setThemeDescriptorError(true);
            }
        }

        if (state.hasError()) {
            throw new ThemeStructureException(state);
        }

    }
}
