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

/**
 * @author Anthony Birembaut
 */
public class PropertiesFactory {

    public static ResourcesPermissionsMapping getResourcesPermissionsMapping(final long tenantId) {
        return new ResourcesPermissionsMapping(tenantId);
    }

    public static CompoundPermissionsMapping getCompoundPermissionsMapping(final long tenantId) {
        return new CompoundPermissionsMapping(tenantId);
    }

    public static CustomPermissionsMapping getCustomPermissionsMapping(final long tenantId) {
        return new CustomPermissionsMapping(tenantId);
    }

    public static DynamicPermissionsChecks getDynamicPermissionsChecks(final long tenantId) {
        return new DynamicPermissionsChecks(tenantId);
    }

    public static SecurityProperties getSecurityProperties(final long tenantId) {
        return new SecurityProperties(tenantId);
    }

    public static SecurityProperties getSecurityProperties() {
        return new SecurityProperties();
    }

    public static ConsoleProperties getConsoleProperties(final long tenantId) {
        return new ConsoleProperties(tenantId);
    }

    public static PlatformTenantConfigProperties getPlatformTenantConfigProperties() {
        return new PlatformTenantConfigProperties();
    }

    public static PlatformPreferencesProperties getPlatformPreferencesProperties() {
        return new PlatformPreferencesProperties();
    }
}
