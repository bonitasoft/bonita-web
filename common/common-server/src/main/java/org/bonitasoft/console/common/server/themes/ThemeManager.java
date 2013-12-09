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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.bonitasoft.console.common.server.utils.UnzipUtil;
import org.bonitasoft.engine.io.IOUtil;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.lesscss.LessCompiler;
import org.lesscss.LessException;

/**
 * @author Ruiheng.Fan
 * 
 */
public class ThemeManager {

    public static final String THEME_FOLDER_NAME = "theme";

    /**
     * logger
     */
    private final Logger LOGGER = Logger.getLogger(ThemeManager.class.getName());

    /**
     * @param unzipThemeFolder
     * @param themeFolder
     * @throws ThemeStructureException
     * @throws IOException
     */
    public void applyTheme(final File unzipThemeFolder, final String themeFolder) throws ThemeStructureException, IOException {
        copyThemeInBonitahome(unzipThemeFolder, themeFolder);
    }

    public File unzipThemeInTempFolder(final File themeZip, final String tempFolderPath, String themeType) throws FileNotFoundException, IOException {
        String tmpFolderPath = tempFolderPath + File.separator + THEME_FOLDER_NAME + File.separator + themeType;
        UnzipUtil.unzip(themeZip, tmpFolderPath);

        return new File(tmpFolderPath);
    }

    /**
     * Move the folder in the bonitaHome
     * 
     * @param zipFile
     * @throws IOException
     */
    private void copyThemeInBonitahome(final File unzipThemeFolder, final String themeFolder) {
        try {
            FileUtils.copyDirectoryToDirectory(unzipThemeFolder, new File(themeFolder));
            unzipThemeFolder.delete();
        } catch (final IOException e) {
            throw new APIException(e);
        }
    }

    /**
     * @param themesFolder
     *            the folder of the theme
     * @param lessFileName
     *            the name of the less file to compile
     * @param cssFileName
     *            the name of the css file in which to write the less
     * @return the CSS file
     * @throws IOException
     *             if the less file doesn't exists or if the CSS file cannot be written
     * @throws LessException
     *             if the less file is invalid
     */
    public File compileLess(final File themesFolder, final String lessFileName, final String cssFileName) throws IOException, LessException {
        File cssFile = null;
        // Instantiate the LESS compiler
        final LessCompiler lessCompiler = new LessCompiler();
        // compile LESS input file to CSS output file
        try {
            cssFile = new File(themesFolder, cssFileName);
            // cssFile.createNewFile();
            lessCompiler.compile(new File(themesFolder, lessFileName), cssFile);
            return cssFile;
        } catch (final IOException e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "Error while compiling the less.", e);
            }
            throw e;
        } catch (final LessException e) {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO, "Error in the less file.", e);
            }
            throw e;
        }
    }

    /**
     * @param themeName
     * @param themesFolder
     * @throws IOException
     * @throws SessionTimeOutException
     */
    public void deleteTheme(final String themeName, final File themesFolder) throws IOException {
        final File themeFolder = new File(themesFolder, themeName);
        if (themeFolder.exists()) {
            try {
                if (!themeFolder.getCanonicalPath().startsWith(themesFolder.getCanonicalPath())) {
                    throw new IOException();
                }
            } catch (final IOException e) {
                final String errorMessage = "Error while deleting the theme " + themeName + " For security reasons, access to paths other than "
                        + themesFolder.getName() + " is restricted";
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE, errorMessage, e);
                }
                throw new IOException(errorMessage);
            }
        } else {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, "The theme directory does not exist.");
            }
            throw new IOException("The theme directory does not exist.");
        }
        deleteTempDirectory(themeFolder);
    }

    public void deleteTempDirectory(final File unzipReport) {
        try {
            IOUtil.deleteDir(unzipReport);
        } catch (final IOException e) {
            throw new APIException(e);
        }

    }

    public void copyDirectory(final File sourceLocation, final File targetLocation) throws IOException {

        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }

            final String[] children = sourceLocation.list();
            for (int i = 0; i < children.length; i++) {
                copyDirectory(new File(sourceLocation, children[i]), new File(targetLocation, children[i]));
            }
        } else {

            final InputStream in = new FileInputStream(sourceLocation);
            final OutputStream out = new FileOutputStream(targetLocation);

            // Copy the bits from instream to outstream
            final byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    }
}
