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
package org.bonitasoft.console.common.server.login.datastore;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bonitasoft.engine.api.LoginAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.platform.LoginException;
import org.bonitasoft.engine.platform.LogoutException;
import org.bonitasoft.engine.session.APISession;

/**
 * @author Yongtao Guo
 */
public class LoginDatastore {

    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(LoginDatastore.class.getName());

    /**
     * login.
     * 
     * @param username
     * @param password
     * @return APISession aAPISession
     * @throws BonitaException
     */
    public APISession login(final String username, final String password) throws BonitaException {
        APISession apiSession = null;
        try {
            if (username == null || password == null) {
                final String errorMessage = "Error while logging in the engine API.";
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE, errorMessage);
                }
                throw new LoginException(errorMessage);
            }
            apiSession = getLoginAPI().login(username, password);
        } catch (final LoginException e) {
            final String errorMessage = "Error while logging in the engine API.";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage);
            }
            throw new BonitaException(e);
        }
        return apiSession;
    }

    /**
     * logout .
     * 
     * @throws BonitaException
     */
    public void logout(final APISession apiSession) throws BonitaException {
        if (apiSession != null) {
            try {
                getLoginAPI().logout(apiSession);
            } catch (final LogoutException e) {
                final String errorMessage = "Logout error while calling the engine API.";
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE, errorMessage);
                }
                throw new BonitaException(errorMessage, e);
            }
        }
    }

    protected LoginAPI getLoginAPI() throws BonitaException {
        try {
            return TenantAPIAccessor.getLoginAPI();
        } catch (final BonitaException e) {
            final String errorMessage = "Error while getting the loginAPI.";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage);
            }
            throw new BonitaException(errorMessage, e);
        }
    }
}
