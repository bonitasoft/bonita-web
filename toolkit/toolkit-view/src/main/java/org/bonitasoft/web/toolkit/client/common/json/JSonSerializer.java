/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.web.toolkit.client.common.json;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * @author Séverin Moussel
 */
public class JSonSerializer extends JSonUtil {

    public static String serialize(final JsonSerializable object) {
        if (object == null) {
            return "null";
        }
        return object.toJson();
    }

    public static String serialize(final Object object) {
        if (object == null) {
            return "null";
        } else if (object instanceof JsonSerializable) {
            return ((JsonSerializable) object).toJson();
        } else if (object instanceof Collection<?>) {
            return serializeCollection((Collection<?>) object);
        } else if (object instanceof Map<?, ?>) {
            return serializeMap((Map<?, ?>) object);
        } else if (object instanceof Number) {
            return object.toString();
        } else if (object instanceof Boolean) {
            return (Boolean) object ? "true" : "false";
        } else if (object instanceof Date) {
            final DateTimeFormat sdf = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss.SSS");
            return quote(sdf.format((Date) object));
        } else if (object instanceof Throwable) {
            return serializeException((Throwable) object);
        }

        return quote(object.toString());
    }

    public static String serialize(final Object key, final Object value) {
        return quote(key.toString()) + ":" + serialize(value);
    }

    public static String serializeCollection(final Collection<? extends Object> list) {
        String json = "[";

        boolean first = true;
        for (final Object item : list) {
            json += (!first ? "," : "") + serialize(item);
            first = false;
        }

        json += "]";

        return json;
    }

    public static String serializeMap(final Map<? extends Object, ? extends Object> map) {
        final StringBuilder json = new StringBuilder().append("{");

        boolean first = true;
        for (final Object key : map.keySet()) {
            json.append(!first ? "," : "").append(quote(key.toString())).append(":").append(serialize(map.get(key)));
            first = false;
        }

        json.append("}");

        return json.toString();
    }

    public static String serializeException(final Throwable e) {
        final StringBuilder json = new StringBuilder().append("{");

        json.append(quote("exception")).append(":").append(quote(e.getClass().toString()));
        json.append(",");
        json.append(quote("message")).append(":").append(quote(e.getMessage()));

        if (e.getStackTrace() != null) {

            json.append(",");
            json.append(quote("stacktrace")).append(":").append(serialize(Arrays.asList(e.getStackTrace())));
        }

        if (e.getCause() != null && e.getCause() != e) {
            json.append(",");
            json.append(quote("cause")).append(":").append(serialize(e.getCause()));
        }

        json.append("}");

        return json.toString();
    }

    public static String serializeStringMap(final Map<? extends Object, String> map) {
        String json = "{";

        boolean first = true;
        for (final Object key : map.keySet()) {
            json += (!first ? "," : "") + quote(key.toString()) + ":" + quote(map.get(key));
            first = false;
        }

        json += "}";

        return json;
    }

}
