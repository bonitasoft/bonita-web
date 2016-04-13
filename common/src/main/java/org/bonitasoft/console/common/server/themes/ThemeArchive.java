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
package org.bonitasoft.console.common.server.themes;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.bonitasoft.console.common.server.utils.UnzipUtil;

public class ThemeArchive {

    private byte[] zippedTheme;

    public void setZippedTheme(byte[] zippedTheme) {
        this.zippedTheme = zippedTheme;
    }

    public class ThemeModifier {

        private final File themeDirectory;

        private ThemeModifier(File themeDirectory) {
            this.themeDirectory = themeDirectory;
        }

        /**
         * Added files will end up in the theme directory.
         * If the file exist already then it will be overridden.
         *
         * @param fileName file's name.
         * @param content file's content.
         */
        public ThemeModifier add(String fileName, byte[] content) throws IOException {
            FileUtils.writeByteArrayToFile(new File(themeDirectory, fileName), content, false);
            return this;
        }

        public File resolve(String path) {
            return new File(themeDirectory, path);
        }

        public ThemeModifier compile(CompilableFile... files) {
            for (final CompilableFile file : files) {
                file.compile(this);
            }
            return this;
        }
    }

    public ThemeArchive() {
    }

    public ThemeArchive(byte[] zippedTheme) {
        this.zippedTheme = zippedTheme;
    }

    public ThemeModifier extract(File themeDirectory) throws IOException {
        checkZippedThemeIsSet();
        FileUtils.deleteDirectory(themeDirectory);
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(zippedTheme)) {
            UnzipUtil.unzip(byteArrayInputStream, themeDirectory.getPath());
        }
        return new ThemeModifier(themeDirectory);
    }

    private void checkZippedThemeIsSet() {
        if (zippedTheme == null) {
            throw new IllegalArgumentException("You must set zipped theme by calling setZippedTheme() before trying to access it");
        }
    }

    public byte[] asByteArray() {
        checkZippedThemeIsSet();
        return zippedTheme;
    }
}
