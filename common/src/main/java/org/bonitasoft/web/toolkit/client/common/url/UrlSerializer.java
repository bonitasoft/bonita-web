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

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Séverin Moussel
 * 
 */
public class UrlSerializer extends UrlUtil {

    public static String serialize(final UrlSerializable object) {
        return serialize(null, object);
    }

    public static String serialize(final String key, final UrlSerializable object) {
        return object.toUrl(null);
    }

    public static String serialize(final Object object) {
        return serialize(null, object);
    }

    public static String serialize(final String key, final Object object) {
        if (object == null) {
            return "";
        } else if (object instanceof UrlSerializable) {
            return ((UrlSerializable) object).toUrl(key);
        } else if (object instanceof List<?>) {
            return serializeList(key, (List<?>) object);
        } else if (object instanceof Map<?, ?>) {
            return serializeMap(key, (Map<?, ?>) object);
        } else if (object instanceof Boolean) {
            return "=" + ((Boolean) object ? "true" : "false");
        }

        return (key.length() > 0 ? key + "=" : "") + escape(object.toString());
    }

    public static String serializeList(final String key, final List<? extends Object> list) {
        final StringBuilder res = new StringBuilder();

        final String realKey = key != null && key.length() > 0 ? key + "[]" : "";

        boolean first = true;
        for (final Object item : list) {
            res.append(!first ? "&" : "").append(serialize(realKey, item));
            first = false;
        }
        return res.toString();
    }

    public static String serializeMap(final String key, final Map<? extends Object, ? extends Object> map) {
        final StringBuilder res = new StringBuilder();

        for (final Entry<? extends Object, ? extends Object> entry : map.entrySet()) {
            final String realKey = key != null && key.length() > 0
                    ? key + "[" + escape(entry.getKey().toString()) + "]"
                    : escape(entry.getKey().toString());

            res.append("&").append(serialize(realKey, entry.getValue()));
        }

        return res.length() > 0 ? res.toString().substring(1) : "";
    }
}
