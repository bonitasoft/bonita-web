/**
 * Copyright (C) 2014 BonitaSoft S.A.
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
package org.bonitasoft.web.rest.server.api;

import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.i18n._;

/**
 * @author Vincent Elcrin
 */
public class APIPreconditions {

    public static void check(boolean condition, _ message) {
        if(!condition) {
            throw new APIException(message);
        }
    }

    public static boolean containsOnly(String key, Map<String, String> map) {
        if(map == null) {
            return false;
        }
        HashMap<String, String> clone = new HashMap<String, String>(map);
        return clone.remove(key) != null && clone.isEmpty();
    }
}
