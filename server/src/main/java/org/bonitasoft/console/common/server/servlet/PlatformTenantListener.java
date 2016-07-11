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
package org.bonitasoft.console.common.server.servlet;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.bonitasoft.console.common.server.utils.PlatformManagementUtils;
import org.bonitasoft.console.common.server.utils.TenantsManagementUtils;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.bonitasoft.engine.exception.ServerAPIException;
import org.bonitasoft.engine.exception.UnknownAPITypeException;
import org.bonitasoft.engine.platform.LogoutException;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.session.SessionNotFoundException;
import org.bonitasoft.engine.theme.ThemeType;
import org.bonitasoft.forms.server.ThemeExtractor;

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
        PlatformManagementUtils platformManagementUtils = new PlatformManagementUtils();
        try {
            platformManagementUtils.initializePlatformConfiguration();
            // Create temporary folder specific to portal at startup:
            WebBonitaConstantsUtils.getInstance().getTempFolder();
        } catch (BonitaException e) {
            LOGGER.log(Level.SEVERE,
                    "Error initializing platform configuration. Engine most likely failed to start. Check previous error logs for more details.");
            return;
        } catch (IOException e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "Error while retrieving configuration", e);
            }
        }
        initializeDefaultTenant(new ThemeExtractor());
    }

    /**
     * Initialize default tenant configuration folder
     *
     * @param themeExtractor the theme extractor
     */
    protected void initializeDefaultTenant(ThemeExtractor themeExtractor) {
        try {
            final APISession session = login();
            final long tenantId = session.getTenantId();

            // retrieve active theme for default tenant:
            themeExtractor.retrieveAndExtractCurrentTheme(WebBonitaConstantsUtils.getInstance(tenantId).getPortalThemeFolder(), session, ThemeType.PORTAL);

            logout(session);
        } catch (final Throwable e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "Error while initializing the default tenant", e);
            }
        }
    }

    protected void logout(APISession session)
            throws SessionNotFoundException, LogoutException, BonitaHomeNotSetException, ServerAPIException, UnknownAPITypeException {
        TenantAPIAccessor.getLoginAPI().logout(session);
    }

    protected APISession login() throws Exception {
        return TenantAPIAccessor.getLoginAPI().login(TenantsManagementUtils.getTechnicalUserUsername(), TenantsManagementUtils.getTechnicalUserPassword());
    }

    @Override
    public void contextDestroyed(final ServletContextEvent sce) {
    }

}
