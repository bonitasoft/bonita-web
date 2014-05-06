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
package org.bonitasoft.web.toolkit.server;

import org.bonitasoft.web.toolkit.client.common.exception.http.JsonExceptionSerializer;
import org.bonitasoft.web.toolkit.client.common.exception.http.ServerException;

/**
 * @author Séverin Moussel
 * 
 */
public class ServiceException extends ServerException {

    private static final long serialVersionUID = 4014993729649254349L;

    protected final String path;

    public ServiceException(final String path) {
        super();
        this.path = path;
    }

    public ServiceException(final String path, final String message, final Throwable cause) {
        super(message, cause);
        this.path = path;
    }

    public ServiceException(final String path, final String message) {
        super(message);
        this.path = path;
    }

    public ServiceException(final String path, final Throwable cause) {
        super(cause);
        this.path = path;
    }

    /**
     * Get the path of the service called
     * 
     * @return the path
     */
    public String getPath() {
        return this.path;
    }

    @Override
    protected String defaultMessage() {
        return "The service \"" + getPath() + "\" has encountered an unknown error";
    }

    @Override
    protected JsonExceptionSerializer buildJson() {
        return super.buildJson()
                .appendAttribute("path", getPath());
    }

}
