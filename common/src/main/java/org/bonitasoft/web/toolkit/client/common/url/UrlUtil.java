/**
 * Copyright (C) 2011 BonitaSoft S.A.
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
package org.bonitasoft.web.toolkit.client.common.url;

import java.util.HashMap;
import java.util.Map.Entry;

import com.google.gwt.http.client.URL;

/**
 * @author Séverin Moussel
 *
 */
public class UrlUtil {

    public static String escape(final String string) {
        return URL.encodeQueryString(string);
    }

    public static String unescape(final String string) {
        return URL.decodeQueryString(string);
    }

    public static String escapePathSegment(final String string) {
        return URL.encodePathSegment(string).replaceAll("%2F", "/");
    }

    public static HashMap<String, String> unescape(final HashMap<String, String> values) {
        final HashMap<String, String> unescapeMap = new HashMap<String, String>();
        for (final Entry<String, String> value : values.entrySet()) {
            unescapeMap.put(value.getKey(), unescape(value.getValue()));
        }
        return unescapeMap;
    }
}
