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
package org.bonitasoft.console.common.server.auth;

import org.slf4j.Logger;

import org.bonitasoft.console.common.server.auth.impl.standard.StandardAuthenticationManagerImpl;
import org.slf4j.LoggerFactory;

/**
 * @author Ruiheng Fan
 */
public class AuthenticationManagerFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationManagerFactory.class.getName());

    static AuthenticationManager authenticationManager;

    public static AuthenticationManager getAuthenticationManager() throws AuthenticationManagerNotFoundException {
        String authenticationManagerName = null;
        if (authenticationManager == null) {
            try {
                authenticationManagerName = getManagerImplementationClassName();
                authenticationManager = (AuthenticationManager) Class.forName(authenticationManagerName).newInstance();
            } catch (final Exception e) {
                final String message = "The AuthenticationManager implementation " + authenticationManagerName + " does not exist!";
                throw new AuthenticationManagerNotFoundException(message);
            }
        }
        return authenticationManager;
    }

    private static String getManagerImplementationClassName() {
        String authenticationManagerName = AuthenticationManagerProperties.getProperties().getAuthenticationManagerImpl();
        if (authenticationManagerName == null || authenticationManagerName.isEmpty()) {
            authenticationManagerName = StandardAuthenticationManagerImpl.class.getName();
            LOGGER.trace("The login manager implementation is undefined. Using default implementation : " + authenticationManagerName);
        }
        return authenticationManagerName;
    }
}
