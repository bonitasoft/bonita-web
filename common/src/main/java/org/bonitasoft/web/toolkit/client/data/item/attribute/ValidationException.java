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
package org.bonitasoft.web.toolkit.client.data.item.attribute;

import java.util.List;

import org.bonitasoft.web.toolkit.client.common.exception.KnownException;

/**
 * @author Séverin Moussel
 * 
 */
@SuppressWarnings("serial")
public class ValidationException extends KnownException {

    private final List<ValidationError> errors;

    public ValidationException(final List<ValidationError> errors) {
        super(errorsToMessage(errors));
        this.errors = errors;
    }

    private static String errorsToMessage(final List<ValidationError> errors) {
        final StringBuilder sb = new StringBuilder();
        for (final ValidationError error : errors) {
            sb.append(error.getMessage());
            sb.append("\r\n");
        }

        return sb.toString();
    }

    /**
     * @return the errors
     */
    public List<ValidationError> getErrors() {
        return this.errors;
    }

}
