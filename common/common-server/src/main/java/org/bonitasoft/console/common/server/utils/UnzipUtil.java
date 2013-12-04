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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.bonitasoft.engine.io.IOUtil;

/**
 * Utility class extracting zip file
 * 
 * @author Zhiheng Yang
 */
public class UnzipUtil {

    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(UnzipUtil.class.getName());

    static final int BUFFER = 2048;

    /**
     * Unzip a zip file from InputStream
     * 
     * @param InputStream
     *            of SourceFile
     * @param targetPath
     * @throws IOException 
     * @throws FileNotFoundException 
     */
    public static synchronized void unzip(final InputStream sourceFile, final String targetPath) throws FileNotFoundException, IOException {
        IOUtil.unzipToFolder(sourceFile, new File(targetPath));
        sourceFile.close();
    }
    
    /**
     * Unzip a zip file from InputStream
     * 
     * @param File
     *            of SourceFile
     * @param targetPath
     * @throws IOException 
     * @throws FileNotFoundException 
     */
    public static synchronized void  unzip(final File zipFile, final String unzipReportPath) throws FileNotFoundException, IOException {
    	final FileInputStream zipFileInputStream = new FileInputStream(zipFile);
    	unzip(zipFileInputStream, unzipReportPath);
        zipFile.delete();
    }
    
}
