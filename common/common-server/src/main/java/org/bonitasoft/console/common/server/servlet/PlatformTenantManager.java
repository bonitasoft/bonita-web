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

import org.bonitasoft.console.common.server.preferences.properties.PlatformTenantConfigProperties;
import org.bonitasoft.console.common.server.preferences.properties.PropertiesFactory;
import org.bonitasoft.engine.api.PlatformAPI;
import org.bonitasoft.engine.api.PlatformAPIAccessor;
import org.bonitasoft.engine.api.PlatformLoginAPI;
import org.bonitasoft.engine.platform.PlatformLogoutException;
import org.bonitasoft.engine.session.PlatformSession;
import org.bonitasoft.engine.session.SessionNotFoundException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Zhiheng Yang
 */
public class PlatformTenantManager {

    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(PlatformTenantManager.class.getName());

    private static PlatformTenantManager instance;

    private static PlatformTenantConfigProperties platformProperties = null;

    private static PlatformLoginAPI platformLoginAPI = null;

    private static PlatformSession platformSession = null;

    private static PlatformAPI platformAPI = null;

    private static final String STOP = "stop";

    private static final String START = "start";

    protected PlatformTenantManager() throws Exception {
        platformProperties = PropertiesFactory.getPlatformTenantConfigProperties();
        platformLoginAPI = PlatformAPIAccessor.getPlatformLoginAPI();
    }

    public synchronized static PlatformTenantManager getInstance() throws Exception {
        if (instance == null) {
            instance = new PlatformTenantManager();
        }
        return instance;
    }

    public void loginPlatform() throws Exception {
        try {
            if (platformLoginAPI != null) {
                platformSession = platformLoginAPI.login(platformProperties.platformUsername(), platformProperties.platformPassword());
                platformAPI = PlatformAPIAccessor.getPlatformAPI(platformSession);
            }
        } catch (final Exception e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "Error while logging to the platform", e);
            }
            throw e;
        }
    }

    public boolean createPlatform() throws Exception {
        if (!platformAPI.isPlatformCreated()) {
            platformAPI.createAndInitializePlatform();
            return true;
        }
        return false;
    }

    private void updatePlatform(final String platformState) throws Exception {
        if (platformAPI.isPlatformCreated()) {
            if (platformState.equals(START)) {
                platformAPI.startNode();
            } else if (platformState.equals(STOP)) {
                platformAPI.stopNode();
            }
        }
    }

    public void startPlatform() throws Exception {
        updatePlatform(START);
    }

    public void stopPlatform() throws Exception {
        updatePlatform(STOP);
    }

    public void logoutPlatform() {
        try {
            if (platformLoginAPI != null) {
                platformLoginAPI.logout(platformSession);
            }
        } catch (final SessionNotFoundException e) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Error while logging out. The session may have expired and was removed.");
            }
        } catch (final PlatformLogoutException e) {
            final String errorMessage = "Error while loging out of the platform";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage);
            }
        }
    }

    public PlatformTenantConfigProperties getPlatformProperties() {
        return platformProperties;
    }

    protected PlatformSession getPlatformSession() {
        return platformSession;
    }

}
