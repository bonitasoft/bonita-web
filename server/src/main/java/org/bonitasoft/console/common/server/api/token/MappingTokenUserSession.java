/**
 * Copyright (C) 2013 BonitaSoft S.A.
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
package org.bonitasoft.console.common.server.api.token;

import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.console.common.server.api.token.*;


/**
 * @author Paul AMAR
 *
 */
public class MappingTokenUserSession {

    private static Map<String, APIToken> MappingTokenUserSessionMap = new HashMap<String, APIToken>();
    
    public static void addSessionIdAndToken(String name, APIToken value) {
        removeSessionId(name);
        MappingTokenUserSessionMap.put(name, value);
    }
    
    public static APIToken getToken(String name) {
        return MappingTokenUserSessionMap.get(name);
    }
    
    public static void removeSessionId(String name) {
        MappingTokenUserSessionMap.remove(name);
    }
    
    public static boolean isTokenValid(String sessionId, APIToken token) {
        if (MappingTokenUserSessionMap.get(sessionId) != null) {
            String expectedToken = MappingTokenUserSessionMap.get(sessionId).getToken();
            if (expectedToken.equals(token.getToken())) {
                return true;
            }
        }
        return false;
    }
    
    public static int getSize() {
        return MappingTokenUserSessionMap.size();
    }
}
