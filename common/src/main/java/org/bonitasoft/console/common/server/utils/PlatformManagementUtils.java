/**
 * Copyright (C) 2016 Bonitasoft S.A.
 * Bonitasoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation
 * version 2.1 of the License.
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth
 * Floor, Boston, MA 02110-1301, USA.
 **/

package org.bonitasoft.console.common.server.utils;

import java.io.IOException;
import java.util.Map;

import org.bonitasoft.console.common.server.preferences.properties.ConfigurationFilesManager;
import org.bonitasoft.engine.api.ApiAccessType;
import org.bonitasoft.engine.api.PlatformAPI;
import org.bonitasoft.engine.api.PlatformAPIAccessor;
import org.bonitasoft.engine.api.PlatformLoginAPI;
import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.bonitasoft.engine.exception.ServerAPIException;
import org.bonitasoft.engine.exception.UnknownAPITypeException;
import org.bonitasoft.engine.session.PlatformSession;
import org.bonitasoft.engine.util.APITypeManager;

/**
 * @author Baptiste Mesta
 */
public class PlatformManagementUtils {

    private ConfigurationFilesManager configurationFilesManager = ConfigurationFilesManager.getInstance();

    private boolean isLocal() throws UnknownAPITypeException, ServerAPIException, IOException {
        return ApiAccessType.LOCAL.equals(APITypeManager.getAPIType());
    }

    PlatformSession platformLogin() throws BonitaException, IOException {
        if (isLocal()) {
            try {
                Class<?> api = Class.forName("org.bonitasoft.engine.LocalLoginMechanism");
                return (PlatformSession) api.getDeclaredMethod("login").invoke(api.newInstance());
            } catch (Exception e) {
                throw new ServerAPIException("unable to do the local login", e);
            }
        } else {
            return PlatformAPIAccessor.getPlatformLoginAPI().login(System.getProperty("org.bonitasoft.platform.username"),
                    System.getProperty("org.bonitasoft.platform.password"));
        }
    }

    void platformLogout(PlatformSession platformSession) throws BonitaException {
        PlatformLoginAPI platformLoginAPI = PlatformAPIAccessor.getPlatformLoginAPI();
        platformLoginAPI.logout(platformSession);
    }

    private void retrieveTenantsConfiguration(PlatformAPI platformAPI) throws IOException {
        Map<Long, Map<String, byte[]>> clientPlatformConfigurations = platformAPI.getClientTenantConfigurations();
        for (Map.Entry<Long, Map<String, byte[]>> tenantConfiguration : clientPlatformConfigurations.entrySet()) {
            configurationFilesManager.setTenantConfigurations(tenantConfiguration.getValue(), tenantConfiguration.getKey());
        }
    }

    private void retrievePlatformConfiguration(PlatformAPI platformAPI) throws IOException {
        Map<String, byte[]> clientPlatformConfigurations = platformAPI.getClientPlatformConfigurations();
        configurationFilesManager.setPlatformConfigurations(clientPlatformConfigurations);
    }

    public void initializePlatformConfiguration() throws BonitaException, IOException {
        PlatformSession platformSession = platformLogin();
        PlatformAPI platformAPI = getPlatformAPI(platformSession);
        retrievePlatformConfiguration(platformAPI);
        retrieveTenantsConfiguration(platformAPI);
        platformLogout(platformSession);
    }

    public void updateConfigurationFile(long tenantId, String file, byte[] content) throws IOException, BonitaException {
        PlatformSession platformSession = platformLogin();
        PlatformAPI platformAPI = getPlatformAPI(platformSession);
        platformAPI.updateClientTenantConfigurationFile(tenantId, file, content);
        platformLogout(platformSession);
    }

    PlatformAPI getPlatformAPI(PlatformSession platformSession) throws BonitaHomeNotSetException, ServerAPIException, UnknownAPITypeException {
        return PlatformAPIAccessor.getPlatformAPI(platformSession);
    }
}
