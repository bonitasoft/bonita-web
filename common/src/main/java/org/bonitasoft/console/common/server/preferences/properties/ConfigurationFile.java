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

import java.util.Properties;
import java.util.Set;

/**
 * @author Ruiheng Fan, Anthony Birembaut
 */
public class ConfigurationFile {

    private final String propertiesFilename;
    protected final long tenantId;

    public ConfigurationFile(String propertiesFilename, long tenantId) {
        this.propertiesFilename = propertiesFilename;
        this.tenantId = tenantId;
    }

    public String getProperty(final String propertyName) {
        final String propertyValue = getPropertiesOfScope().getProperty(propertyName);
        return propertyValue != null ? propertyValue.trim() : null;
    }

    public Properties getPropertiesOfScope() {
        if (tenantId > 0) {
            return ConfigurationFilesManager.getInstance().getTenantProperties(propertiesFilename);
        }
        return ConfigurationFilesManager.getInstance().getPlatformProperties(propertiesFilename);
    }

    public Set<String> getPropertyAsSet(final String propertyName) {
        final String propertyAsString = getProperty(propertyName);
        return stringToSet(propertyAsString);
    }

}
