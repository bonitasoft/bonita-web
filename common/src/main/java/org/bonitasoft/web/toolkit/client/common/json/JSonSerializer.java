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

import com.google.gwt.i18n.shared.DateTimeFormat;
import org.bonitasoft.web.toolkit.client.common.exception.http.JsonExceptionSerializer;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * @author Séverin Moussel
 */
public class JSonSerializer extends JSonUtil {

    public static String serialize(final JsonSerializable object) {
        return serializeInternal(object).toString();
    }

    private static StringBuilder serializeInternal(JsonSerializable object) {
        if (object == null) {
            return new StringBuilder("null");
        }
        return new StringBuilder(object.toJson());
    }

    public static String serialize(final Object object) {
        return serializeInternal(object).toString();
    }

    private static StringBuilder serializeInternal(Object object) {
        if (object == null) {
            return new StringBuilder("null");
        } else if (object instanceof JsonSerializable) {
            return serializeInternal((JsonSerializable) object);
        } else if (object instanceof Collection<?>) {
            return serializeCollectionInternal((Collection<?>) object);
        } else if (object instanceof Map<?, ?>) {
            return serializeMapInternal((Map<?, ?>) object);
        } else if (object instanceof Number) {
            return new StringBuilder(object.toString());
        } else if (object instanceof Boolean) {
            return new StringBuilder((Boolean) object ? "true" : "false");
        } else if (object instanceof Date) {
            final DateTimeFormat sdf = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss.SSS");
            return quoteInternal(sdf.format((Date) object));
        } else if (object instanceof Throwable) {
            return new StringBuilder(serializeException((Throwable) object));
        }

        return quoteInternal(object.toString());
    }

    public static String serialize(final Object key, final Object value) {
        return serializeInternal(key, value).toString();
    }

    private static StringBuilder serializeInternal(Object key, Object value) {
        return quoteInternal(key.toString()).append(":").append(serialize(value));
    }

    public static String serializeCollection(final Collection<? extends Object> list) {
        return serializeCollectionInternal(list).toString();
    }

    private static StringBuilder serializeCollectionInternal(Collection<?> list) {
        final StringBuilder json = new StringBuilder("[");

        boolean first = true;
        for (final Object item : list) {
            json.append(!first ? "," : "").append(serializeInternal(item));
            first = false;
        }

        json.append("]");

        return json;
    }

    public static String serializeMap(final Map<? extends Object, ? extends Object> map) {
        return serializeMapInternal(map).toString();
    }

    private static StringBuilder serializeMapInternal(Map<?, ?> map) {
        final StringBuilder json = new StringBuilder().append("{");

        boolean first = true;
        for (final Object key : map.keySet()) {
            json.append(!first ? "," : "").append(quoteInternal(key.toString())).append(":").append(serializeInternal(map.get(key)));
            first = false;
        }

        json.append("}");

        return json;
    }

    public static String serializeException(final Throwable e) {
        return new JsonExceptionSerializer(e).end();
    }

    public static String serializeStringMap(final Map<? extends Object, String> map) {
        return serializeStringMapInternal(map).toString();
    }

    private static StringBuilder serializeStringMapInternal(Map<?, String> map) {
        final StringBuilder json = new StringBuilder("{");

        boolean first = true;
        for (final Object key : map.keySet()) {
            json.append(!first ? "," : "").append(quoteInternal(key.toString())).append(":").append(quoteInternal(map.get(key)));
            first = false;
        }

        json.append("}");

        return json;
    }

}
