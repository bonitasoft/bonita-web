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
package org.bonitasoft.test.toolkit.exception;

/**
 * @author Vincent Elcrin
 * 
 */
public class TestToolkitException extends RuntimeException {

    /**
     * ID used by serialization.
     */
    private static final long serialVersionUID = -2678731898708770494L;

    /**
     * Default constructor.
     */
    public TestToolkitException() {
        super();
    }

    /**
     * Default constructor.
     * 
     * @param message
     */
    public TestToolkitException(final String message) {
        super(message);
    }

    /**
     * Default constructor.
     * 
     * @param message
     */
    public TestToolkitException(final String message, final Exception e) {
        super(message, e);
    }
}
