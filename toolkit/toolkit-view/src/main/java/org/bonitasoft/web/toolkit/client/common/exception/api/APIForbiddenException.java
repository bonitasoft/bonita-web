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

import org.bonitasoft.web.toolkit.client.common.i18n._;

/**
 * @author Vincent Elcrin
 * 
 */
public class APIForbiddenException extends APIException {

    private static final long serialVersionUID = 4021139564084425851L;

    public APIForbiddenException(final String message) {
        super(message);
    }

    public APIForbiddenException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public APIForbiddenException(_ localizedMessage) {
        super(localizedMessage);
    }

    public APIForbiddenException(_ localizedMessage, Throwable cause) {
        super(localizedMessage, cause);
    }
}
