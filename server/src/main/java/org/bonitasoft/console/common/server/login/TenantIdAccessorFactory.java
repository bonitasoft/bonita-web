/**
 * Copyright (C) 2018 BonitaSoft S.A.
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
package org.bonitasoft.console.common.server.login;

import java.lang.reflect.Constructor;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bonitasoft.console.common.server.preferences.properties.ServerProperties;

/**
 * @author Anthony Birembaut
 */
public class TenantIdAccessorFactory {

    private static final Logger LOGGER = Logger.getLogger(TenantIdAccessorFactory.class.getName());
    
    private static final String TENANTIDACCESSOR_PROPERTY_NAME = "auth.TenantIdAccessor";

    @SuppressWarnings("unchecked")
    public static TenantIdAccessor getTenantIdAccessor() {
        ServerProperties serverProperties = ServerProperties.getInstance();
        String tenantIdAccessorClassName = serverProperties.getValue(TENANTIDACCESSOR_PROPERTY_NAME);
        TenantIdAccessor tenantIdAccessor;
        if (tenantIdAccessorClassName == null || tenantIdAccessorClassName.isEmpty()) {
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.log(Level.FINEST, "auth.TenantIdAccessor is undefined. Using default implementation : " + TenantIdAccessor.class.getName());
            }
            tenantIdAccessor = new TenantIdAccessor();
        } else {
            try {
                Constructor<TenantIdAccessor> constructor = (Constructor<TenantIdAccessor>) Class.forName(tenantIdAccessorClassName).getConstructor(HttpServletRequestAccessor.class);
                tenantIdAccessor = constructor.newInstance();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "The TenantIdAccessor specified " + tenantIdAccessorClassName + " could not be instantiated! Using default implementation : " + TenantIdAccessor.class.getName(), e);
                tenantIdAccessor = new TenantIdAccessor();
            }
        }
        return tenantIdAccessor;
    }
}
