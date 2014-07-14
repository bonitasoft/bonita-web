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
package org.bonitasoft.console.common.server.login;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ruiheng Fan
 * 
 */
public class LoginManagerFactory {

    static Map<Long, LoginManager> map = new HashMap<Long, LoginManager>();
    private static LoginManagerPropertiesFactory loginManagerPropertiesFactory = new LoginManagerPropertiesFactory();
    
    public static LoginManager getLoginManager(final long tenantId) throws LoginManagerNotFoundException {
        String loginManagerName = null;
        if (!map.containsKey(tenantId)) {
            try {
                loginManagerName = loginManagerPropertiesFactory.getProperties(tenantId).getLoginManagerImpl();
                final LoginManager loginManager = (LoginManager) Class.forName(loginManagerName).newInstance();
                map.put(tenantId, loginManager);
            } catch (final Exception e) {
                final String message = "The LoginManager implementation " + loginManagerName + " does not exist!";
                throw new LoginManagerNotFoundException(message);
            }
        }
        return map.get(tenantId);
    }

}
