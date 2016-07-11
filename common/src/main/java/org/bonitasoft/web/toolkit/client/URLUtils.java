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
package org.bonitasoft.web.toolkit.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;

public class URLUtils {

    public static final String HASH_PARAMETERS_SEPARATOR = "&";

    protected static URLUtils INSTANCE = null;

    /**
     * @return the view controller instance
     */
    public static URLUtils getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new URLUtils();
        }
        return INSTANCE;
    }

    protected URLUtils() {
        super();
    }

    public String getHashParameter(final String hashParameterName) {
        final Map<String, String> hashParameters = getHashParameters();
        return hashParameters.get(hashParameterName);
    }

    public Map<String, String> getHashParameters() {
        String hash = Window.Location.getHash();
        if (hash != null && hash.startsWith("#")) {
            hash = hash.substring(1);
        }
        return getHashParameters(hash);
    }

    private Map<String, String> getHashParameters(final String hash) {
        final Map<String, String> parametersMap = new HashMap<String, String>();
        final String[] parameters = hash.split(HASH_PARAMETERS_SEPARATOR);
        for (final String parameter : parameters) {
            final String[] parameterEntry = parameter.split("=");
            final String name = parameterEntry[0];
            String value = null;
            if (parameterEntry.length > 1) {
                value = URL.decodeQueryString(parameterEntry[1]);
            }
            parametersMap.put(name, value);
        }
        return parametersMap;
    }

}
