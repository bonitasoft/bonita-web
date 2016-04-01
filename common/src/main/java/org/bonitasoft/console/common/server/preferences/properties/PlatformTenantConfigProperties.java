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

import java.util.Properties;

/**
 * @author Zhiheng Yang
 */
public class PlatformTenantConfigProperties {

    private static final String PLATFORM_DEFAULT_TENANT_ID = "platform.tenant.default.id";

    private static final String PLATFORM_TENANT_DEFAULT_USERNAME = "platform.tenant.default.username";

    private static final String PLATFORM_TENANT_DEFAULT_PASSWORD = "platform.tenant.default.password";

    private static final String PROPERTIES_FILE = "platform-tenant-config.properties";

    private Properties getProperties() {
        return ConfigurationFilesManager.getInstance().getPlatformProperties(PROPERTIES_FILE);
                    }

    public String getDefaultTenantId() {
        return getProperties().getProperty(PLATFORM_DEFAULT_TENANT_ID);
    }

    public String defaultTenantUserName() {
        return getProperties().getProperty(PLATFORM_TENANT_DEFAULT_USERNAME);
    }

    public String defaultTenantPassword() {
        return getProperties().getProperty(PLATFORM_TENANT_DEFAULT_PASSWORD);
    }

}
