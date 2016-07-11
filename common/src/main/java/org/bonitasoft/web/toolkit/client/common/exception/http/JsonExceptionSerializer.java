/*
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

package org.bonitasoft.web.toolkit.client.common.exception.http;

import org.bonitasoft.web.toolkit.client.common.json.JSonSerializer;

import java.util.Arrays;

/**
 * Created by Vincent Elcrin
 * Date: 23/09/13
 * Time: 17:56
 */
public class JsonExceptionSerializer {

    public static final String EXCEPTION_ATTRIBUTE = "exception";
    public static final String CAUSE_ATTRIBUTE = "cause";
    public static final String STACK_TRACE_ATTRIBUTE = "stacktrace";
    public static final String MESSAGE_ATTRIBUTE = "message";
    public static final String JSON_OPEN = "{";
    public static final String ATTRIBUTE_SEPARATOR = ",";
    public static final String VALUE_SEPARATOR = ":";
    public static final String JSON_CLOSE = "}";

    private StringBuilder json;

    public JsonExceptionSerializer(Exception exception) {
        if (exception == null) {
            throw new IllegalArgumentException("exception cannot be null");
        }

        json = serialize(exception);
    }

    public String end() {
        json.append(JSON_CLOSE);
        return json.toString();
    }

    public JsonExceptionSerializer appendAttribute(final String name, final Object value) {
        addNextAttribute(json, name, value);
        return this;
    }

    private StringBuilder serialize(Exception exception) {
        final StringBuilder json = new StringBuilder().append(JSON_OPEN);

        addAttribute(json, EXCEPTION_ATTRIBUTE, exception.getClass());
        addMessage(json, exception.getMessage());
        addNextAttribute(json, STACK_TRACE_ATTRIBUTE, Arrays.toString(exception.getStackTrace()));
        addNextAttribute(json, CAUSE_ATTRIBUTE, exception.getCause());

        return json;
    }

    private void addMessage(final StringBuilder json, String message) {
        addNextAttribute(json, MESSAGE_ATTRIBUTE, message);
    }

    private void addNextAttribute(final StringBuilder json, final String name, final Object value) {
        if (value != null) {
            json.append(ATTRIBUTE_SEPARATOR);
            addAttribute(json, name, value);
        }
    }

    private void addAttribute(StringBuilder json, String name, Object value) {
        json.append(JSonSerializer.quote(name))
                .append(VALUE_SEPARATOR)
                .append(JSonSerializer.serialize(value));
    }

}
