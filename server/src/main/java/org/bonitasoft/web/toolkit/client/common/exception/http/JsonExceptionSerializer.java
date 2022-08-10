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

import static org.bonitasoft.web.toolkit.client.common.json.JSonUtil.quote;

import org.bonitasoft.web.toolkit.client.common.json.JSonSerializer;

/**
 * Created by Vincent Elcrin
 * Date: 23/09/13
 * Time: 17:56
 */
public class JsonExceptionSerializer {

    private StringBuilder json;

    public JsonExceptionSerializer(Throwable exception) {
        if (exception == null) {
            throw new IllegalArgumentException("exception cannot be null");
        }

        json = serialize(exception);
    }

    private StringBuilder serialize(final Throwable e) {
        final StringBuilder json = new StringBuilder().append("{");

        json.append(exceptionInnerJson(e));
        if (e.getCause() != null && e.getCause() != e) {
            json.append(",");
            // only add the first cause (used by some code in portal's frontend)
            json.append(quote("cause")).append(":{").append(exceptionInnerJson(e.getCause())).append("}");
        }

        return json;
    }

    private String exceptionInnerJson(final Throwable e) {
        final StringBuilder json = new StringBuilder();
        json.append(quote("exception")).append(":").append(quote(e.getClass().toString()));
        json.append(",");
        json.append(quote("message")).append(":").append(quote(e.getMessage()));
        return json.toString();
    }

    public String end() {
        return json.append("}").toString();
    }

    public JsonExceptionSerializer appendAttribute(final String name, final Object value) {
        addNextAttribute(json, name, value);
        return this;
    }

    private void addNextAttribute(final StringBuilder json, final String name, final Object value) {
        if (value != null) {
            json.append(",");
            json.append(quote(name))
                    .append(":")
                    .append(JSonSerializer.serialize(value));
        }
    }
}
