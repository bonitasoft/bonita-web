/**
 * Copyright (C) 2016 Bonitasoft S.A.
 * Bonitasoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation
 * version 2.1 of the License.
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth
 * Floor, Boston, MA 02110-1301, USA.
 **/

package org.bonitasoft.console.common.server.preferences.properties;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.bonitasoft.console.common.server.utils.PlatformManagementUtils;
import org.bonitasoft.console.common.server.utils.TenantsManagementUtils;
import org.bonitasoft.engine.exception.BonitaException;

/**
 * @author Baptiste Mesta, Emmanuel Duchastenier, Anthony Birembaut
 */
public class ConfigurationFilesManager {

    private static final ConfigurationFilesManager INSTANCE = new ConfigurationFilesManager();

    public static ConfigurationFilesManager getInstance() {
        return INSTANCE;
    }

    private static final Logger LOGGER = Logger.getLogger(ConfigurationFilesManager.class.getName());

    /*
     * Map<realConfigurationFileName, File>
     */
    private Map<String, File> tenantsConfigurationFiles = new HashMap<>();

    /*
     * Map<propertiesFileName, Properties>
     */
    private Map<String, Properties> platformConfigurations = new HashMap<>();

    /*
     * Map<configurationFileName, File>
     */
    private Map<String, File> platformConfigurationFiles = new HashMap<>();

    public Properties getPlatformProperties(String propertiesFile) {
        Properties properties = platformConfigurations.get(propertiesFile);
        if (properties == null) {
            return new Properties();
        }
        return properties;
    }

    Properties getAlsoCustomAndInternalPropertiesFromFilename(String propertiesFileName) {
        Properties properties = new Properties();
        try {
            final Map<String, Properties> propertiesByFilename = getResources();
            if (propertiesByFilename.containsKey(propertiesFileName)) {
                properties.putAll(propertiesByFilename.get(propertiesFileName));
                // if -internal properties also exists, merge key/value pairs:
                final String internalSuffixedVersion = getSuffixedPropertyFilename(propertiesFileName, "-internal");
                if (propertiesByFilename.containsKey(internalSuffixedVersion)) {
                    properties.putAll(propertiesByFilename.get(internalSuffixedVersion));
                }
                // if -custom properties also exists, merge key/value pairs (and overwrite previous values if same key name):
                final String customSuffixedVersion = getSuffixedPropertyFilename(propertiesFileName, "-custom");
                if (propertiesByFilename.containsKey(customSuffixedVersion)) {
                    properties.putAll(propertiesByFilename.get(customSuffixedVersion));
                }
            } else {
                if (LOGGER.isLoggable(Level.FINER)) {
                    LOGGER.log(Level.FINER, "File " + propertiesFileName + " not found. Returning empty properties object.");
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Cannot retrieve tenant configurations", e);
        }
        return properties;
    }

    public Properties getTenantProperties(String propertiesFileName) {
        return getAlsoCustomAndInternalPropertiesFromFilename(propertiesFileName);
    }

    /**
     * Parses the content as a Properties object.
     * If content is null, return empty properties.
     */
    public static Properties getProperties(byte[] content) {
        Properties properties = new Properties();
        if (content != null) {
            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(content)) {
                properties.load(inputStream);
            } catch (IOException ioe) {
                LOGGER.log(Level.SEVERE, "Cannot parse properties file content", ioe);
            }
        }
        return properties;
    }

    public void setPlatformConfigurations(Map<String, byte[]> configurationFiles) throws IOException {
        platformConfigurations = new HashMap<>(configurationFiles.size());
        for (Map.Entry<String, byte[]> entry : configurationFiles.entrySet()) {
            if (entry.getKey().endsWith(".properties")) {
                platformConfigurations.put(entry.getKey(), getProperties(entry.getValue()));
            } else {
                File file = new File(WebBonitaConstantsUtils.getPlatformInstance().getTempFolder(), entry.getKey());
                FileUtils.writeByteArrayToFile(file, entry.getValue());
                platformConfigurationFiles.put(entry.getKey(), file);
            }
        }
    }

    public void setTenantConfigurationFiles(Map<String, byte[]> configurationFiles, long tenantId) throws IOException {
        Map<String, File> tenantFiles = new HashMap<>();
        for (Map.Entry<String, byte[]> entry : configurationFiles.entrySet()) {
            if (!entry.getKey().endsWith(".properties")) {
                File file = new File(WebBonitaConstantsUtils.getTenantInstance().getTempFolder(), entry.getKey());
                FileUtils.writeByteArrayToFile(file, entry.getValue());
                tenantFiles.put(entry.getKey(), file);
            }
        }
        tenantsConfigurationFiles = tenantFiles;
    }

    public void removeProperty(String propertiesFilename, long tenantId, String propertyName) throws IOException {
        // Now internal behavior stores and removes from -internal file:
        final String internalFilename = getSuffixedPropertyFilename(propertiesFilename, "-internal");
        Map<String, Properties> resources = getResources();
        Properties properties = resources.get(internalFilename);
        if (properties != null) {
            properties.remove(propertyName);
            update(tenantId, internalFilename, properties);
        } else {
            if (LOGGER.isLoggable(Level.FINER)) {
                LOGGER.log(Level.FINER, "File " + internalFilename + " not found. Cannot remove property '" + propertyName + "'.");
            }
        }
    }

    private String getSuffixedPropertyFilename(String propertiesFilename, String suffix) {
        return propertiesFilename.replaceAll("\\.properties$", suffix + ".properties");
    }

    private void update(long tenantId, String propertiesFilename, Properties properties) throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            properties.store(byteArrayOutputStream, "");
            getPlatformManagementUtils().updateConfigurationFile(tenantId, propertiesFilename, byteArrayOutputStream.toByteArray());
        } catch (BonitaException e) {
            throw new IOException(e);
        }
    }

    PlatformManagementUtils getPlatformManagementUtils() {
        return new PlatformManagementUtils();
    }

    Map<String, Properties> getResources() throws IOException {
        return getPlatformManagementUtils().getTenantConfigurations().get(TenantsManagementUtils.getDefaultTenantId());
    }

    public void setProperty(String propertiesFilename, long tenantId, String propertyName, String propertyValue) throws IOException {
        Map<String, Properties> resources = getResources();
        // Now internal behavior stores and removes from -internal file:
        final String internalFilename = getSuffixedPropertyFilename(propertiesFilename, "-internal");
        Properties properties = resources.get(internalFilename);
        if (properties != null) {
            properties.setProperty(propertyName, propertyValue);
            update(tenantId, internalFilename, properties);
        } else {
            if (LOGGER.isLoggable(Level.FINER)) {
                LOGGER.log(Level.FINER, "File " + internalFilename + " not found. Cannot remove property '" + propertyName + "'.");
            }
        }
    }

    public File getTenantConfigurationFile(String fileName) {
        if (tenantsConfigurationFiles != null) {
            return tenantsConfigurationFiles.get(fileName);
        }
        return null;
    }

    public File getPlatformConfigurationFile(String fileName) {
        return platformConfigurationFiles.get(fileName);
    }

}
