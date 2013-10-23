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
package org.bonitasoft.web.rest.server.framework.exception;

import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.exception.http.JsonExceptionSerializer;

/**
 * @author Séverin Moussel
 * 
 */
public class APIAttributeException extends APIException {

    private static final long serialVersionUID = -4611825491187153300L;

    private String attributeName;

    public APIAttributeException(final String attributeName) {
        super((Exception) null);
    }

    public APIAttributeException(final String attributeName, final String message, final Throwable cause) {
        super(message, cause);
    }

    public APIAttributeException(final String attributeName, final String message) {
        super(message);
    }

    public APIAttributeException(final String attributeName, final Throwable cause) {
        super(cause);
    }

    /**
     * @return the attributeName
     */
    public String getAttributeName() {
        return this.attributeName;
    }

    @Override
    protected JsonExceptionSerializer buildJson() {
        return super.buildJson()
                .appendAttribute("attributeName", getAttributeName());
    }

    @Override
    protected String defaultMessage() {
        return "Malformed attribute : " + this.attributeName;
    }

}
