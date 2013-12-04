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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.lesscss.LessCompiler;
import org.lesscss.LessException;

/**
 * @author Ruiheng.Fan
 * 
 */
public class ThemeManager {

    /**
     * logger
     */
    private final Logger LOGGER = Logger.getLogger(ThemeManager.class.getName());

    /**
     * @param themeID
     * @param themesFolder
     * @throws ThemeStructureException
     * @throws IOException
     */
    public void applyTheme(final String type, final File themesFolder) throws ThemeStructureException, IOException {
        final ThemeValidator validator = new ThemeValidator();
        validator.doValidate(themesFolder.getAbsolutePath() + File.separator + type);
        // TODO engine call
    }

    public void compileLess(final File themesFolder, final String lessFileName, final String cssFileName) throws IOException, LessException {
        // Instantiate the LESS compiler
        final LessCompiler lessCompiler = new LessCompiler();
        // compile LESS input file to CSS output file
        try {
            lessCompiler.compile(new File(themesFolder, lessFileName), new File(themesFolder, cssFileName));
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
        deleteFolder(themeFolder.getAbsolutePath());
        // TODO engine call
    }

    /**
     * Delete the directory
     * 
     * @param folderName
     * @throws IOException
     */
    public void deleteFolder(final String folderPath) throws IOException {
        // the theme directory
        final File dir = new File(folderPath);
        if (dir.exists()) {
            final File delFile[] = dir.listFiles();
            final int i = dir.listFiles().length;
            for (int j = 0; j < i; j++) {
                if (delFile[j].isDirectory()) {
                    deleteFolder(delFile[j].getAbsolutePath());
                } else {
                    delFile[j].delete();
                }
            }
            dir.delete();
        } else {
            final String theErrorMessage = "The directory does not exist: " + folderPath;
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, theErrorMessage);
            }
            throw new IOException(theErrorMessage);
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
