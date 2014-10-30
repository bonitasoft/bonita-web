/**
 * Copyright (C) 2014 BonitaSoft S.A.
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

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Anthony Birembaut
 */
public class CompoundPermissionsMapping extends SimpleProperties {

    /**
     * Default name of the preferences file
     */
    public static final String PROPERTIES_FILENAME = "compound-permissions-mapping.properties";

    /**
     * Instances attribute
     */
    private static Map<Long, CompoundPermissionsMapping> INSTANCES = new ConcurrentHashMap<Long, CompoundPermissionsMapping>();

    /**
     * @return the {@link CompoundPermissionsMapping} instance
     */
    protected static CompoundPermissionsMapping getInstance(final long tenantId) {
        CompoundPermissionsMapping tenancyProperties = INSTANCES.get(tenantId);
        if (tenancyProperties == null) {
            final File fileName = getTenantPropertiesFile(tenantId, PROPERTIES_FILENAME);
            tenancyProperties = new CompoundPermissionsMapping(fileName);
            INSTANCES.put(tenantId, tenancyProperties);
        }
        return tenancyProperties;
    }

    CompoundPermissionsMapping(final File fileName) {
        super(fileName);
    }

}
