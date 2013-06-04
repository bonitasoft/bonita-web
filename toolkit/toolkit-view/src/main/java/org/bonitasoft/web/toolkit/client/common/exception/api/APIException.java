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
package org.bonitasoft.web.toolkit.client.common.exception.api;

import org.bonitasoft.web.toolkit.client.common.exception.http.ServerException;
import org.bonitasoft.web.toolkit.client.common.json.JsonSerializable;

/**
 * @author Séverin Moussel
 * 
 */
public class APIException extends ServerException implements JsonSerializable {

    private static final long serialVersionUID = 1820639344042666872L;

    private String api = "...";

    private String resource = "...";

    protected APIException() {
        super();
    }

    public APIException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public APIException(final String message) {
        super(message);
    }

    public APIException(final Throwable cause) {
        super(cause);

    }

    /**
     * @return the api
     */
    public String getApi() {
        return this.api;
    }

    /**
     * @param api
     *            the api to set
     */
    public APIException setApi(final String api) {
        this.api = api;
        return this;
    }

    /**
     * @return the resource
     */
    public String getResource() {
        return this.resource;
    }

    /**
     * @param resource
     *            the resource to set
     */
    public APIException setResource(final String resource) {
        this.resource = resource;
        return this;
    }

    @Override
    protected void toJsonAdditionnalAttributes(final StringBuilder json) {
        super.toJsonAdditionnalAttributes(json);

        addJsonAdditionalAttribute("api", getApi(), json);
        addJsonAdditionalAttribute("resource", getResource(), json);
    }

    @Override
    protected String defaultMessage() {
        return "The API \"" + getApi() + "#" + getResource() + "\" has encountered an unknown error";
    }

}
