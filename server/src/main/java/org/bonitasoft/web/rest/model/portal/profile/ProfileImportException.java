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
package org.bonitasoft.web.rest.model.portal.profile;

/**
 * @author Cuisha Gai
 * 
 */
public class ProfileImportException extends Exception {

    /**
     * UID
     */
    private static final long serialVersionUID = 3900140020859917486L;

    /**
     * Default Constructor.
     */
    public ProfileImportException() {
        super();
    }

    /**
     * Default Constructor.
     * 
     * @param message
     */
    public ProfileImportException(final String message) {
        super(message);
    }

    /**
     * Default Constructor.
     * 
     * @param cause
     */
    public ProfileImportException(final Throwable cause) {
        super(cause);
    }

    /**
     * Default Constructor.
     * 
     * @param message
     * @param cause
     */
    public ProfileImportException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
