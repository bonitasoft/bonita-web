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

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ruiheng Fan
 *
 */
public class AuthenticationManagerFactory {

    static Map<Long, AuthenticationManager> map = new HashMap<Long, AuthenticationManager>();
    private static AuthenticationManagerPropertiesFactory authenticationManagerPropertiesFactory = new AuthenticationManagerPropertiesFactory();

    public static AuthenticationManager getAuthenticationManager(final long tenantId) throws AuthenticationManagerNotFoundException {
        String authenticationManagerName = null;
        if (!map.containsKey(tenantId)) {
            try {
                authenticationManagerName = authenticationManagerPropertiesFactory.getProperties(tenantId).getAuthenticationManagerImpl();
                final AuthenticationManager authenticationManager = (AuthenticationManager) Class.forName(authenticationManagerName).newInstance();
                map.put(tenantId, authenticationManager);
            } catch (final Exception e) {
                final String message = "The AuthenticationManager implementation " + authenticationManagerName + " does not exist!";
                throw new AuthenticationManagerNotFoundException(message);
            }
        }
        return map.get(tenantId);
    }

}
