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

import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.bonitasoft.console.common.server.preferences.properties.PropertiesFactory;
import org.bonitasoft.console.common.server.preferences.properties.TenantProperties;
import org.bonitasoft.theme.ThemeDescriptorManager;
import org.bonitasoft.theme.exception.ThemeDescriptorNotFoundException;
import org.bonitasoft.theme.impl.ThemeDescriptorManagerImpl;
import org.bonitasoft.theme.model.ThemeDescriptor;

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
     * Theme descriptor file name
     */
    private static final String THEME_DESCRIPTOR = "themeDescriptor.xml";

    /**
     * Get the theme ThemeDescriptor
     * 
     * @param themeName
     * @throws ThemeNotFoundException
     */
    public ThemeDescriptor getThemeDescriptor(final String themeName, final File themesFolder) throws ThemeDescriptorNotFoundException, IOException {
        String themePath = null;
        ThemeDescriptor themeDescriptor = null;
        final ThemeDescriptorManager manager = ThemeDescriptorManagerImpl.getInstance();
        if (themesFolder.exists()) {
            themePath = themesFolder.getAbsolutePath() + File.separator + themeName + File.separator + THEME_DESCRIPTOR;
            final File file = new File(themePath);
            try {
                if (!file.getCanonicalPath().startsWith(themesFolder.getCanonicalPath())) {
                    throw new IOException();
                }
            } catch (final IOException e) {
                final String errorMessage = "Error while getting the theme " + themeName + " For security reasons, access to paths other than "
                        + themesFolder.getName() + " is restricted";
                if (this.LOGGER.isLoggable(Level.SEVERE)) {
                    this.LOGGER.log(Level.SEVERE, errorMessage, e);
                }
                throw new ThemeDescriptorNotFoundException(errorMessage);
            }
            themeDescriptor = manager.getThemeDescriptor(file);
        } else {
            if (this.LOGGER.isLoggable(Level.WARNING)) {
                this.LOGGER.log(Level.WARNING, "The themes directory does not exist.");
            }
            throw new IOException("The themes directory does not exist.");
        }
        return themeDescriptor;
    }

    /**
     * @param tenantId
     *            the tenant ID
     * @param themeName
     * @param themesFolder
     * @throws ThemeStructureException
     * @throws IOException
     */
    public void applyTheme(final long tenantId, final String themeName, final File themesFolder) throws ThemeStructureException, IOException {
        final ThemeValidator validator = new ThemeValidator();
        validator.doValidate(tenantId, themesFolder.getAbsolutePath() + File.separator + themeName, false);
        ThemeConfigManager.getInstance(tenantId).setApplyTheme(themeName);
    }

    /**
     * @param tenantId
     *            the tenantID
     * @param themeName
     * @param themesFolder
     * @throws IOException
     * @throws SessionTimeOutException
     */
    public void deleteTheme(final long tenantId, final String themeName, final File themesFolder) throws IOException {
        final File themeFolder = new File(themesFolder, themeName);
        if (themeFolder.exists()) {
            try {
                if (!themeFolder.getCanonicalPath().startsWith(themesFolder.getCanonicalPath())) {
                    throw new IOException();
                }
            } catch (final IOException e) {
                final String errorMessage = "Error while deleting the theme " + themeName + " For security reasons, access to paths other than "
                        + themesFolder.getName() + " is restricted";
                if (this.LOGGER.isLoggable(Level.SEVERE)) {
                    this.LOGGER.log(Level.SEVERE, errorMessage, e);
                }
                throw new IOException(errorMessage);
            }
        } else {
            if (this.LOGGER.isLoggable(Level.WARNING)) {
                this.LOGGER.log(Level.WARNING, "The theme directory does not exist.");
            }
            throw new IOException("The theme directory does not exist.");
        }
        try {
            final String themePath = themesFolder.getAbsolutePath() + File.separator + themeName + File.separator + THEME_DESCRIPTOR;
            final File themeDescriptor = new File(themePath);
            final ThemeDescriptorManager manager = ThemeDescriptorManagerImpl.getInstance();
            manager.deleteThemeDescriptor(themeDescriptor);
        } catch (final ThemeDescriptorNotFoundException e) {
            if (this.LOGGER.isLoggable(Level.WARNING)) {
                this.LOGGER.log(Level.WARNING, "The theme descriptor file was not found for theme " + themeName);
            }
        }
        deleteFolder(themeFolder.getAbsolutePath());
        final String currentTheme = PropertiesFactory.getTenantProperties(tenantId).getProperty(TenantProperties.CURRENT_THEME_KEY);
        if (currentTheme != null && currentTheme.equals(themeName)) {
            PropertiesFactory.getTenantProperties(tenantId).removeProperty(TenantProperties.CURRENT_THEME_KEY);
        }
    }

    /**
     * Delete the directory
     * 
     * @param folderName
     * @throws IOException
     */
    protected void deleteFolder(final String folderPath) throws IOException {
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
            final String theErrorMessage = "The directory does not exist: "
                    + folderPath;
            if (this.LOGGER.isLoggable(Level.SEVERE)) {
                this.LOGGER.log(Level.SEVERE, theErrorMessage);
            }
            throw new IOException(theErrorMessage);
        }
    }

    /**
     * @param tenantId
     *            the tenant Id
     * @param themeFolder
     * @param themeFolderName
     * @throws IOException
     */
    public void copyToThemesFolder(final long tenantId, final File themeFolder, final String themeFolderName) throws IOException {
        final File themeHome = WebBonitaConstantsUtils.getInstance(tenantId).getConsoleThemeFolder();
        copyDirectory(themeFolder, new File(themeHome, themeFolderName));
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
