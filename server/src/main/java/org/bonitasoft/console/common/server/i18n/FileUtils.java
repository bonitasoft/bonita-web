/**
 * Copyright (C) 2012-2016 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.console.common.server.i18n;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Vincent Elcrin
 */
public class FileUtils {

    public static List<File> getMatchingFiles(final String regex, List<File> files) {
        List<File> locales = new ArrayList<>();
        for (File file : files) {
            if (isFileMatching(file, regex)) {
                locales.add(file);
            }
        }
        return locales;
    }

    private static boolean isFileMatching(File file, String regex) {
        return file.isFile() && file.getName().matches(regex);
    }

    public static List<File> listDir(File dir) {
        File[] files = dir.listFiles();
        if (files == null) {
            files = new File[0];
        }
        return Arrays.asList(files);
    }

}
