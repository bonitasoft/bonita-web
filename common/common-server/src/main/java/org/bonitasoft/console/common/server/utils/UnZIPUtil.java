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
package org.bonitasoft.console.common.server.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Utility class extracting zip file
 * 
 * @author Zhiheng Yang
 */
public class UnZIPUtil {

    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(UnZIPUtil.class.getName());

    static final int BUFFER = 2048;

    /**
     * Unzip a zip file from InputStream
     * 
     * @param InputStream
     *            of SourceFile
     * @param targetPath
     */
    public static synchronized void unzip(final InputStream sourceFile, final String targetPath) {
        final File outFile = new File(targetPath);
        if (!outFile.exists()) {
            outFile.mkdirs();
        }
        BufferedOutputStream dest = null;
        ZipInputStream zis = null;
        File f = null;
        try {
            zis = new ZipInputStream(new BufferedInputStream(sourceFile));
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    String dirName = entry.getName();
                    dirName = dirName.substring(0, dirName.length() - 1);
                    f = new File(outFile.getPath() + File.separator + dirName);
                    f.mkdirs();
                } else {
                    f = new File(outFile.getPath() + File.separator + entry.getName());
                    if (!f.getParentFile().exists()) {
                        f.getParentFile().mkdirs();
                    }
                    f.createNewFile();
                    int count;
                    final byte data[] = new byte[BUFFER];
                    final FileOutputStream fos = new FileOutputStream(f);
                    dest = new BufferedOutputStream(fos, BUFFER);
                    while ((count = zis.read(data, 0, BUFFER)) != -1) {
                        dest.write(data, 0, count);
                        dest.flush();
                    }
                    fos.close();
                }
            }
        } catch (final Exception e) {
            final String theErrorMessage = "Exception while uploading file.";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, theErrorMessage, e);
            }
        } finally {
            try {
                if (dest != null) {
                    dest.close();
                }
                if (zis != null) {
                    zis.close();
                }
            } catch (final IOException e) {
                final String theErrorMessage = "Exception while uploading file.";
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE, theErrorMessage, e);
                }
            }
        }
    }
}
