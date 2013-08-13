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
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.io.IOUtils;
import org.bonitasoft.console.common.server.api.CommandCaller;
import org.bonitasoft.console.common.server.preferences.properties.PlatformTenantConfigProperties;
import org.bonitasoft.console.common.server.utils.TenantsManagementUtils;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.identity.ImportPolicy;
import org.bonitasoft.engine.session.APISession;

/**
 * @author Zhiheng Yang
 */
public class PlatformTenantListener implements ServletContextListener {

    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(PlatformTenantListener.class.getName());

    private static PlatformTenantManager platformManager = null;

    private static PlatformTenantConfigProperties platformProperties = null;

    @Override
    public void contextInitialized(final ServletContextEvent sce) {
        try {
            platformManager = PlatformTenantManager.getInstance();
            platformManager.loginPlatform();
            platformProperties = platformManager.getPlatformProperties();
            if (platformProperties.platformCreate()) {
                platformManager.createPlatform();
            }
            if (platformProperties.platformStart()) {
                platformManager.startPlatform();
            }
            initializeDefaultTenant();
        } catch (final Throwable e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                final String msg = "Error while starting the platform and tenant";
                LOGGER.log(Level.SEVERE, msg, e);
            }
        } finally {
            platformManager.logoutPlatform();
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
            final boolean wasDirectoryCreated = TenantsManagementUtils.addDirectoryForTenant(tenantId, platformManager.getPlatformSession());
            if (wasDirectoryCreated) {
                createDefaultProfiles(session);
            }
            TenantAPIAccessor.getLoginAPI().logout(session);
        } catch (final NumberFormatException e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                final String msg = "Error while casting default tenant id";
                LOGGER.log(Level.SEVERE, msg, e);
            }
            throw e;
        } catch (final IOException e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                final String msg = "Error while creating tenant directory";
                LOGGER.log(Level.SEVERE, msg, e);
            }
            throw e;
        } catch (final BonitaException e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                final String msg = "Bonita exception while creating tenant directory";
                LOGGER.log(Level.SEVERE, msg, e);
            }
            throw e;
        }
    }

    @Override
    public void contextDestroyed(final ServletContextEvent sce) {
        try {
            if (platformProperties.platformStop()) {
                platformManager.loginPlatform();
                platformManager.stopPlatform();
            }
        } catch (final Throwable e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                final String msg = "Error while stopping the platform";
                LOGGER.log(Level.SEVERE, msg, e);
            }
        } finally {
            platformManager.logoutPlatform();
        }
    }

    protected void createDefaultProfiles(final APISession session) throws IOException {
        importProfilesFromResourceFile(session, "InitProfiles.xml");
    }

    @SuppressWarnings("unchecked")
    protected void importProfilesFromResourceFile(final APISession session, final String xmlFileName) throws IOException {
        final InputStream xmlStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(xmlFileName);
        final byte[] xmlContent = IOUtils.toByteArray(xmlStream);

        final CommandCaller addProfiles = new CommandCaller(session, "importProfilesCommand");
        addProfiles.addParameter("xmlContent", xmlContent);
        addProfiles.addParameter("importPolicy", ImportPolicy.MERGE_DUPLICATES);
        final List<String> warningMsgs = (List<String>) addProfiles.run();

    }

}
