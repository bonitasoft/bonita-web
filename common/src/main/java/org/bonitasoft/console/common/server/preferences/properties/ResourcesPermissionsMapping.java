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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Anthony Birembaut
 * @author Baptiste Mesta
 * @author Fabio Lombardi
 */
public class ResourcesPermissionsMapping extends SimpleProperties {

    public static final String API_METHOD_SEPARATOR = "|";

    public static final String WILDCARD = "*";

    /**
     * Default name of the preferences file
     */
    public static final String PROPERTIES_FILENAME = "resources-permissions-mapping.properties";

    /**
     * Instances attribute
     */
    private static Map<Long, ResourcesPermissionsMapping> INSTANCES = new ConcurrentHashMap<Long, ResourcesPermissionsMapping>();

    /**
     * @return the {@link ResourcesPermissionsMapping} instance
     */
    protected static ResourcesPermissionsMapping getInstance(final long tenantId) {
        ResourcesPermissionsMapping tenancyProperties = INSTANCES.get(tenantId);
        if (tenancyProperties == null || SecurityProperties.getInstance(tenantId).isAPIAuthorizationsCheckInDebugMode()) {
            final File fileName = getTenantPropertiesFile(tenantId, PROPERTIES_FILENAME);
            tenancyProperties = new ResourcesPermissionsMapping(fileName);
            INSTANCES.put(tenantId, tenancyProperties);
        }
        return tenancyProperties;
    }

    ResourcesPermissionsMapping(final File fileName) {
        super(fileName);
    }

    public Set<String> getResourcePermissions(final String method, final String apiName, final String resourceName, final List<String> resourceQualifiers) {
        final String key = buildResourceKey(method, apiName, resourceName, resourceQualifiers);
        return getPropertyAsSet(key);
    }

    public Set<String> getResourcePermissionsWithWildCard(final String method, final String apiName, final String resourceName,
            final List<String> resourceQualifiers) {
        for (int i = resourceQualifiers.size() - 1; i >= 0; i--) {
            final List<String> resourceQualifiersWithWildCard = getResourceQualifiersWithWildCard(resourceQualifiers, i);
            final String key = buildResourceKey(method, apiName, resourceName, resourceQualifiersWithWildCard);
            final Set<String> permissions = getPropertyAsSet(key);
            if (!permissions.isEmpty()) {
                return permissions;
            }
        }
        return null;
    }

    protected List<String> getResourceQualifiersWithWildCard(final List<String> resourceQualifiers, final int wildCardPosition) {
        final List<String> resourceQualifiersWithWildCard = new ArrayList<String>(resourceQualifiers);
        resourceQualifiersWithWildCard.set(wildCardPosition, WILDCARD);
        return resourceQualifiersWithWildCard;
    }

    protected String buildResourceKey(final String method, final String apiName, final String resourceName, final List<String> resourceQualifiers) {
        String key = method + API_METHOD_SEPARATOR + apiName + "/" + resourceName;
        if (resourceQualifiers != null) {
            for (final String resourceQualifier : resourceQualifiers) {
                key += "/" + resourceQualifier;
            }
        }
        return key;
    }

    public Set<String> getResourcePermissions(final String method, final String apiName, final String resourceName) {
        return getResourcePermissions(method, apiName, resourceName, null);
    }

}
