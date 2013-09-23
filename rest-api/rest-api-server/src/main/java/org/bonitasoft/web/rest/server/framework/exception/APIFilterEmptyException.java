/**
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
package org.bonitasoft.web.rest.server.framework.exception;

/**
 * @author Séverin Moussel
 * 
 */
public class APIFilterEmptyException extends APIFilterException {

    private static final long serialVersionUID = -3372478894238466120L;

    public APIFilterEmptyException(final String filterName, final String message, final Throwable cause) {
        super(filterName, message, cause);
    }

    public APIFilterEmptyException(final String filterName, final String message) {
        super(filterName, message);
    }

    public APIFilterEmptyException(final String filterName, final Throwable cause) {
        super(filterName, cause);
    }

    public APIFilterEmptyException(final String filterName) {
        super(filterName);
    }

    @Override
    protected String defaultMessage() {
        return "Filter " + getFilterName() + " mustn't be empty";
    }

}
