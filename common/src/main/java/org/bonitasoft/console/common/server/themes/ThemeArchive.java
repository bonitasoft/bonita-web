package org.bonitasoft.console.common.server.themes;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.bonitasoft.console.common.server.utils.UnzipUtil;

public class ThemeArchive {

    private byte[] zippedTheme;

    public class ThemeModifier {

        private File themeDirectory;

        private ThemeModifier (File themeDirectory) {
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
    }

    public ThemeArchive(byte[] zippedTheme) {
        this.zippedTheme = zippedTheme;
    }

    public ThemeModifier extract(File themeDirectory) throws IOException {
        FileUtils.deleteDirectory(themeDirectory);
        UnzipUtil.unzip(new ByteArrayInputStream(zippedTheme), themeDirectory.getPath());
        return new ThemeModifier(themeDirectory);
    }

    public byte[] asByteArray() {
        return zippedTheme;
    }
}
