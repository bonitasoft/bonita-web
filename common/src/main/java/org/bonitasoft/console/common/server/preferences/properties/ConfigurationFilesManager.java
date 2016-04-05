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
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;

/**
 * @author Baptiste Mesta
 */
public class ConfigurationFilesManager {

    private static ConfigurationFilesManager INSTANCE = new ConfigurationFilesManager();

    public static ConfigurationFilesManager getInstance() {
        return INSTANCE;
    }

    private Map<Long, Map<String, Properties>> tenantConfigurations = new HashMap<>();
    private Map<String, Properties> platformConfiguration = new HashMap<>();
    private Map<String, File> platformConfigurationFiles = new HashMap<>();

    public Properties getPlatformProperties(String propertiesFile) {
        Properties properties = platformConfiguration.get(propertiesFile);
        if (properties == null) {
            return new Properties();
        }
        return properties;
    }

    public Properties getTenantProperties(String propertiesFile, long tenantId) {
        Map<String, Properties> map = tenantConfigurations.get(tenantId);
        if (map != null && map.containsKey(propertiesFile)) {
            return map.get(propertiesFile);
        }
        return new Properties();
    }

    private Properties getProperties(byte[] content) throws IOException {
        Properties properties = new Properties();
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(content)) {
            properties.load(inputStream);
        }
        return properties;
    }

    public void setPlatformConfigurations(Map<String, byte[]> configurationFiles) throws IOException {
        platformConfiguration = new HashMap<>(configurationFiles.size());
        for (Map.Entry<String, byte[]> entry : configurationFiles.entrySet()) {
            if (entry.getKey().endsWith(".properties")) {
                platformConfiguration.put(entry.getKey(), getProperties(entry.getValue()));
            } else {
                File file = new File(WebBonitaConstantsUtils.getInstance().getTempFolder(), entry.getKey());
                FileUtils.writeByteArrayToFile(file, entry.getValue());
                platformConfigurationFiles.put(entry.getKey(), file);
            }
        }
    }

    public void setTenantConfigurations(Map<String, byte[]> configurationFiles, long tenantId) throws IOException {
        HashMap<String, Properties> tenantProperties = new HashMap<>(configurationFiles.size());
        for (Map.Entry<String, byte[]> entry : configurationFiles.entrySet()) {
            if (entry.getKey().endsWith(".properties")) {
                tenantProperties.put(entry.getKey(), getProperties(entry.getValue()));
            }
        }
        tenantConfigurations.put(tenantId, tenantProperties);
    }

    public void removeProperty(String propertiesFilename, long tenantId, String propertyName) throws IOException {
        //FIXME make it persisted in database
        Map<String, Properties> resources = getResources(tenantId);
        Properties properties = resources.get(propertiesFilename);
        properties.remove(propertyName);
    }

    private Map<String, Properties> getResources(long tenantId) {
        Map<String, Properties> resources;
        if (tenantId > 0) {
            resources = tenantConfigurations.get(tenantId);
        } else {
            resources = platformConfiguration;
        }
        return resources;
    }

    public void setProperty(String propertiesFilename, long tenantId, String propertyName, String propertyValue) throws IOException {
        //FIXME make it persisted in database
        Map<String, Properties> resources = getResources(tenantId);
        Properties properties = resources.get(propertiesFilename);
        properties.setProperty(propertyName, propertyValue);
    }

    public File getPlatformConfigurationFile(String fileName) {
        return platformConfigurationFiles.get(fileName);
    }
}
