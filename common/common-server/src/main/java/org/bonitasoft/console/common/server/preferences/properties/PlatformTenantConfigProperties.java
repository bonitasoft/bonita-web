/**
 * Copyright (C) 2012 BonitaSoft S.A.
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
package org.bonitasoft.console.common.server.preferences.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;

/**
 * @author Zhiheng Yang
 */
public class PlatformTenantConfigProperties {

    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(PlatformTenantConfigProperties.class.getName());

    protected static final String PROPERTIES_FILENAME = "platform-tenant-config.properties";

    /**
     * Configurations of platform
     */
    public static final String PLATFORM_CREATE = "platform.create";

    protected static final String NODE_START = "node.start";

    protected static final String NODE_STOP = "node.stop";

    protected static final String PLATFORM_USERNAME = "platform.username";

    protected static final String PLATFORM_PASSWORD = "platform.password";

    /**
     * Configurations of tenant
     */
    public static final String PLATFORM_DEFAULT_TENANT_ID = "platform.tenant.default.id";

    protected static final String PLATFORM_TENANT_DEFAULT_USERNAME = "platform.tenant.default.username";

    protected static final String PLATFORM_TENANT_DEFAULT_PASSWORD = "platform.tenant.default.password";

    /**
     * Indicates that the preferences have been loaded
     */
    public static boolean preferencesLoaded = false;

    /**
     * The loaded properties
     */
    private final Properties properties = new Properties();

    /**
     * The properties file
     */
    private final File propertiesFile;

    /**
     * Platform properties instance
     */
    private static PlatformTenantConfigProperties instance = new PlatformTenantConfigProperties();

    /**
     * @return the PlatformProperties instance
     */
    protected static synchronized PlatformTenantConfigProperties getInstance() {
        return instance;
    }

    /**
     * Private contructor to prevent instantiation
     */
    private PlatformTenantConfigProperties() {
        propertiesFile = new File(WebBonitaConstantsUtils.getInstance().getConfFolder(), PROPERTIES_FILENAME);
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(propertiesFile);
            properties.load(inputStream);
        } catch (final IOException e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "Platform Tenant Config file " + propertiesFile.getPath() + " could not be loaded.", e);
            }
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (final IOException e) {
                    if (LOGGER.isLoggable(Level.SEVERE)) {
                        LOGGER.log(Level.SEVERE, "Platform Tenant Config file stream " + propertiesFile.getPath() + " could not be closed.", e);
                    }
                }
            }
        }
    }

    public boolean platformCreate() {
        final String needCreate = properties.getProperty(PLATFORM_CREATE);
        return Boolean.valueOf(needCreate);
    }

    public boolean platformStart() {
        final String start = properties.getProperty(NODE_START);
        return Boolean.valueOf(start);
    }

    public boolean platformStop() {
        final String stop = properties.getProperty(NODE_STOP);
        return Boolean.valueOf(stop);
    }

    public String platformUsername() {
        return properties.getProperty(PLATFORM_USERNAME);
    }

    public String platformPassword() {
        return properties.getProperty(PLATFORM_PASSWORD);
    }

    public String getDefaultTenantId() {
        return properties.getProperty(PLATFORM_DEFAULT_TENANT_ID);
    }

    public String defaultTenantUserName() {
        return properties.getProperty(PLATFORM_TENANT_DEFAULT_USERNAME);
    }

    public String defaultTenantPassword() {
        return properties.getProperty(PLATFORM_TENANT_DEFAULT_PASSWORD);
    }

}
