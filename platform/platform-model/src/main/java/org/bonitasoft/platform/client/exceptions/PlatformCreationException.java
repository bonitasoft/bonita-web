/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.platform.client.exceptions;

/**
 * 
 * @author Haojie Yuan
 * 
 */
public class PlatformCreationException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 4148560122909364800L;

    /**
     * contructor
     */
    public PlatformCreationException() {
        super();
    }

    /**
     * @param message
     *            message associated with the exception
     * @param cause
     *            cause of the exception
     */
    public PlatformCreationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     *            message associated with the exception
     */
    public PlatformCreationException(final String message) {
        super(message);
    }

    /**
     * @param cause
     *            cause of the exception
     */
    public PlatformCreationException(final Throwable cause) {
        super(cause);
    }

}
