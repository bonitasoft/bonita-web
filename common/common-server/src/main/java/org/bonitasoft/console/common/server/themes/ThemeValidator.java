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

/**
 * @author Cuisha Gai
 * 
 */
public class ThemeValidator {

    private static final String CONSOLE_TEMPLATE_FILE = "BonitaConsole.html";

    private static final String MAIN_LESS_FILE = "styles.less";

    /**
     * Do theme package validate
     * 
     * @param themePath
     * @throws IOException
     * @throws FileNotFoundException
     */
    public void doValidate(final long tenantId, final String themePath) throws ThemeStructureException, IOException {
        final File themeFolder = new File(themePath);
        boolean hasConsoleTemplateFile = false;
        boolean hasMainLessFile = false;
        final ThemeStructureState state = new ThemeStructureState();

        final File[] themeFiles = themeFolder.listFiles(new FileFilter() {

            @Override
            public boolean accept(final File aFile) {
                return aFile.isFile();
            }
        });
        for (final File file : themeFiles) {
            final String fileName = file.getName();
            if (!hasConsoleTemplateFile) {
                hasConsoleTemplateFile = fileName.equals(CONSOLE_TEMPLATE_FILE);
            }
            if (!hasMainLessFile) {
                hasMainLessFile = fileName.equals(MAIN_LESS_FILE);
            }
            if (hasMainLessFile) {
                break;
            }
        }

        state.setMissBonitaConsoleHTML(!hasConsoleTemplateFile);
        state.setMissMainLessFile(!hasMainLessFile);

        if (state.hasError()) {
            throw new ThemeStructureException(state);
        }

    }
}
