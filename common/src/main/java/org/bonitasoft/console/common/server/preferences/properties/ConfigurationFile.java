/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
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

import static org.bonitasoft.console.common.server.preferences.properties.PropertiesWithSet.stringToSet;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;

/**
 * @author Ruiheng Fan, Anthony Birembaut
 */
public class ConfigurationFile {

    private final String propertiesFilename;
    private final long tenantId;

    public ConfigurationFile(final String propertiesFilename) {
        this.propertiesFilename = propertiesFilename;
        tenantId = -1L;
    }

    public ConfigurationFile(String propertiesFilename, long tenantId) {
        this.propertiesFilename = propertiesFilename;
        this.tenantId = tenantId;
    }

    public String getProperty(final String propertyName) {
        return getPropertiesOfScope().getProperty(propertyName);
    }

    private Properties getPropertiesOfScope() {
        if (tenantId > 0) {
            return ConfigurationFilesManager.getInstance().getTenantProperties(propertiesFilename, tenantId);
        }
        return ConfigurationFilesManager.getInstance().getPlatformProperties(propertiesFilename);
    }

    public void removeProperty(final String propertyName) {
        try {
            ConfigurationFilesManager.getInstance().removeProperty(propertiesFilename, tenantId, propertyName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setProperty(final String propertyName, final String propertyValue) {
        try {
            ConfigurationFilesManager.getInstance().setProperty(propertiesFilename, tenantId, propertyName, propertyValue);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<String> getPropertyAsSet(final String propertyName) {
        final String propertyAsString = getProperty(propertyName);
        return stringToSet(propertyAsString);
    }

    public void setPropertyAsSet(final String property, final Set<String> permissions) throws IOException {
        setProperty(property, permissions.toString());
    }
}
