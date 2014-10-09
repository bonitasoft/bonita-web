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
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * @author Anthony Birembaut
 */
public class ResourcesPermissionsMapping extends TenantProperties {

    /**
     * Default name of the preferences file
     */
    public static final String PROPERTIES_FILENAME = "resources-permissions-mapping.properties";

    /**
     * Instances attribute
     */
    private static Map<Long, ResourcesPermissionsMapping> INSTANCES = new HashMap<Long, ResourcesPermissionsMapping>();

    /**
     * @return the {@link SecurityProperties} instance
     */
    protected static synchronized ResourcesPermissionsMapping getInstance(final long tenantId) {
        ResourcesPermissionsMapping tenancyProperties = INSTANCES.get(tenantId);
        if (tenancyProperties == null) {
            tenancyProperties = new ResourcesPermissionsMapping(tenantId);
            INSTANCES.put(tenantId, tenancyProperties);
        }
        return tenancyProperties;
    }

    /**
     * Private contructor to prevent instantiation
     *
     * @throws IOException
     */
    protected ResourcesPermissionsMapping(final long tenantId) {
        super(tenantId, PROPERTIES_FILENAME);

    }

}
