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
package org.bonitasoft.console.common.server.preferences.properties;

import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Ruiheng.Fan
 * 
 */
public class TenantProperties {

    /**
     * Default name of the preferences file
     */
    public static final String PROPERTIES_FILENAME = "bonita-web-preferences.properties";

    /**
     * Key for the case list layout preference
     */
    public static final String CASE_LIST_LAYOUT_KEY = "userXP.caseList.layout";

    /**
     * Key for the case list stretched column preference
     */
    public static final String CASE_LIST_STRETCHED_COLUMN_KEY = "userXP.caseList.stretchedColumn";

    /**
     * key for Current theme
     */
    public static final String CURRENT_THEME_KEY = "userXP.currentTheme";

    /**
     * Indicates that the preferences have been loaded
     */
    public static boolean preferencesLoaded = false;

    /**
     * Instances attribute
     */
    private static Map<Long, TenantProperties> INSTANCES = new HashMap<Long, TenantProperties>();

    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(TenantProperties.class.getName());

    /**
     * The loaded properties
     */
    protected Properties properties = new Properties();

    /**
     * The properties file
     */
    protected File propertiesFile;

    /**
     * @return the {@link SecurityProperties} instance
     */
    protected static synchronized TenantProperties getInstance(final long tenantId) {
        TenantProperties tenancyProperties = INSTANCES.get(tenantId);
        if (tenancyProperties == null) {
            tenancyProperties = new TenantProperties(tenantId);
            INSTANCES.put(tenantId, tenancyProperties);
        }
        return tenancyProperties;
    }

    /**
     * Private contructor to prevent instantiation
     * 
     * @throws IOException
     */
    protected TenantProperties(final long tenantId) {
        this.propertiesFile = new File(WebBonitaConstantsUtils.getInstance(tenantId).getConfFolder(), PROPERTIES_FILENAME);
        InputStream inputStream = null;
        try {
            if (!this.propertiesFile.exists()) {
                initProperties(this.propertiesFile);
            }
            inputStream = new FileInputStream(this.propertiesFile);
            this.properties.load(inputStream);
        } catch (final IOException e) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, "Bonita web preferences file " + this.propertiesFile.getPath() + " could not be loaded.", e);
            }
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (final IOException e) {
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER.log(Level.WARNING, "Bonita web preferences file stream " + this.propertiesFile.getPath() + " could not be closed.", e);
                    }
                }
            }
        }
    }

    protected void initProperties(final File aPropertiesFile) throws IOException {
        // Create the file.
        aPropertiesFile.createNewFile();
        // Add default content.
        setProperty(CASE_LIST_STRETCHED_COLUMN_KEY, "10");
    }

    public String getProperty(final String propertyName) {
        if (this.properties == null) {
            return null;
        }
        return this.properties.getProperty(propertyName);
    }

    public String getProperty(final String propertyName, final String defaultValue) {
        if (this.properties == null) {
            return defaultValue;
        }
        return this.properties.getProperty(propertyName, defaultValue);
    }

    public void removeProperty(final String propertyName) throws IOException {
        if (this.properties != null) {
            this.properties.remove(propertyName);
            OutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(this.propertiesFile);
                this.properties.store(outputStream, null);
            } catch (final IOException e) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, "Bonita web preferences file " + this.propertiesFile.getPath() + " could not be loaded.", e);
                }
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (final IOException e) {
                        if (LOGGER.isLoggable(Level.WARNING)) {
                            LOGGER.log(Level.WARNING, "Bonita web preferences file stream " + this.propertiesFile.getPath() + " could not be closed.", e);
                        }
                    }
                }
            }
        }
    }

    public void setProperty(final String propertyName, final String propertyValue) throws IOException {
        if (this.properties != null) {
            this.properties.setProperty(propertyName, propertyValue);
            OutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(this.propertiesFile);
                this.properties.store(outputStream, null);
            } catch (final IOException e) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, "Bonita web preferences file " + this.propertiesFile.getPath() + " could not be loaded.", e);
                }
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (final IOException e) {
                        if (LOGGER.isLoggable(Level.WARNING)) {
                            LOGGER.log(Level.WARNING, "Bonita web preferences file stream " + this.propertiesFile.getPath() + " could not be closed.", e);
                        }
                    }
                }
            }
        }
    }
}
