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
package org.bonitasoft.forms.client.model.exception;

/**
 * When session time out throw this exception
 * 
 * @author Qixiang Zhang
 * 
 */
public class SessionTimeoutException extends Exception {

    /**
     * Serial version uid
     */
    private static final long serialVersionUID = 8241594536013453297L;

    /**
     * 
     * Default Constructor.
     */
    public SessionTimeoutException() {
        super();
    }

    /**
     * 
     * Use message as param constructor.
     * 
     * @param message
     *            message associated with the exception
     */
    public SessionTimeoutException(final String message) {
        super(message);
    }

    /**
     * 
     * Use cause as param constructor.
     * 
     * @param cause
     *            cause of the exception
     */
    public SessionTimeoutException(final Throwable cause) {
        super(cause);
    }

    /**
     * 
     * Use cause and cause as param constructor.
     * 
     * @param message
     *            message associated with the exception
     * @param cause
     *            cause of the exception
     */
    public SessionTimeoutException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
