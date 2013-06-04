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
package org.bonitasoft.web.toolkit.client.common.exception.http;

import org.bonitasoft.web.toolkit.client.common.exception.KnownException;
import org.bonitasoft.web.toolkit.client.common.json.JSonSerializer;
import org.bonitasoft.web.toolkit.client.common.json.JSonUtil;
import org.bonitasoft.web.toolkit.client.common.json.JsonSerializable;

/**
 * @author Séverin Moussel
 * 
 */
public class HttpException extends KnownException implements JsonSerializable {

    private static final long serialVersionUID = 4351214615157802308L;

    protected int statusCode = 503;

    public HttpException() {
        super();
    }

    public HttpException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public HttpException(final String message) {
        super(message);
    }

    public HttpException(final Throwable cause) {
        super(cause);
    }

    /**
     * @return the statusCode
     */
    public final int getStatusCode() {
        return this.statusCode;
    }

    /**
     * @param statusCode
     *            the statusCode to set
     */
    public final HttpException setStatusCode(final int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // JSON SERIALIZATION
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public final String toJson() {
        // Open Json
        final StringBuilder json = new StringBuilder().append("{");

        // Fill with local attributes
        json.append(JSonUtil.quote("exception")).append(":").append(JSonUtil.quote(getClass().toString()));

        addJsonAdditionalAttribute("message", getMessage(), json);
        addJsonAdditionalAttribute("stacktrace", getStackTrace(), json);

        if (getCause() != null && getCause() != this) {
            addJsonAdditionalAttribute("cause", getCause(), json);
        }

        // Add subclasses specific attributes
        toJsonAdditionnalAttributes(json);

        // Close Json
        json.append("}");

        return json.toString();
    }

    protected void toJsonAdditionnalAttributes(final StringBuilder json) {
    }

    /**
     * @param json
     */
    protected void addJsonAdditionalAttribute(final String name, final Object value, final StringBuilder json) {
        if (value != null) {
            json.append(",");
            json.append(JSonSerializer.quote(name)).append(":").append(JSonSerializer.serialize(value));
        }
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // DEFAULT MESSAGE
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected String defaultMessage() {
        return "The connection has encountered an unknowed error";
    }

}
