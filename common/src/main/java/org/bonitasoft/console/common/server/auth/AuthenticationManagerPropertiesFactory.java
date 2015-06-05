/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.console.common.server.auth;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.bonitasoft.console.common.server.utils.TenantsManagementUtils;

/**
 * @author Ruiheng.Fan
 *
 */
public class AuthenticationManagerPropertiesFactory {

    /**
     * Default name of the form definition file
     */
    protected static final String AUTHENTICATION_CONFIG_FILE_NAME = "authenticationManager-config.properties";

    private static final Map<Long, AuthenticationManagerProperties> map = new HashMap<Long, AuthenticationManagerProperties>();

    public AuthenticationManagerProperties getProperties(final long tenantId) {
        if (!map.containsKey(tenantId)) {
            map.put(tenantId, createAuthenticationManagerPropertiesForTenant(tenantId));
        }
        return map.get(tenantId);
    }

    private AuthenticationManagerProperties createAuthenticationManagerPropertiesForTenant(final long tenantId) {
        long resolvedTenantId = tenantId;
        if (tenantId == -1L) {
            resolvedTenantId = TenantsManagementUtils.getDefaultTenantId();
        }
        final WebBonitaConstantsUtils webConstantsUtils = WebBonitaConstantsUtils.getInstance(resolvedTenantId);
        final File propertiesFile = new File(webConstantsUtils.getConfFolder(), AUTHENTICATION_CONFIG_FILE_NAME);
        return new AuthenticationManagerProperties(propertiesFile);
    }

}
