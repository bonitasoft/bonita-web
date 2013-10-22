/**
 * Copyright (C) 2012 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.console.common.server.servlet;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.bonitasoft.console.common.server.utils.TenantsManagementUtils;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.session.APISession;

/**
 * @author Zhiheng Yang, Anthony Birembaut
 */
public class PlatformTenantListener implements ServletContextListener {

    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(PlatformTenantListener.class.getName());

    @Override
    public void contextInitialized(final ServletContextEvent sce) {
        try {
            initializeDefaultTenant();
        } catch (final Throwable e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "Error while initializing the default tenant", e);
            }
        }

    }

    /**
     * Initialize default tenant configuration folder
     * 
     * @throws IOException
     * @throws BonitaException
     */
    protected void initializeDefaultTenant() throws Exception {
        try {
            final APISession session = TenantAPIAccessor.getLoginAPI().login(TenantsManagementUtils.getTechnicalUserUsername(),
                    TenantsManagementUtils.getTechnicalUserPassword());
            final long tenantId = session.getTenantId();
            TenantsManagementUtils.addDirectoryForTenant(tenantId);
            TenantAPIAccessor.getLoginAPI().logout(session);
        } catch (final NumberFormatException e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "Error while casting default tenant id", e);
            }
            throw e;
        } catch (final IOException e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "Error while creating tenant directory", e);
            }
            throw e;
        } catch (final BonitaException e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "Bonita exception while creating tenant directory", e);
            }
            throw e;
        }
    }

    @Override
    public void contextDestroyed(final ServletContextEvent sce) {

    }

}
