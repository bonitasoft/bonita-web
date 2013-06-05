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
package org.bonitasoft.web.toolkit.client.ui;

import org.bonitasoft.web.toolkit.client.common.ToolkitException;

/**
 * @author Séverin Moussel
 * 
 */
public class UIException extends ToolkitException {

    private static final long serialVersionUID = -5595799416997452670L;

    public UIException() {
        super();
    }

    public UIException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UIException(final String message) {
        super(message);
    }

    public UIException(final Throwable cause) {
        super(cause);
    }

}
