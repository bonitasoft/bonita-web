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
package org.bonitasoft.web.toolkit.client.common.exception;

import org.bonitasoft.web.toolkit.client.common.util.StringUtil;

/**
 * @author Séverin Moussel
 * 
 */
public abstract class KnownException extends RuntimeException {

    private static final long serialVersionUID = 4288951779031159740L;

    public KnownException() {
        super();
    }

    public KnownException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public KnownException(final String message) {
        super(message);
    }

    public KnownException(final Throwable cause) {
        super(cause);
    }

    /**
     * Override this method to define a default message.<br />
     * Default message will be the getMessage() result if no other message is defined.
     * 
     * @return This method returns the default message of the exception.
     */
    protected String defaultMessage() {
        return "";
    }

    @Override
    public String getMessage() {
        String message = super.getMessage();
        if (StringUtil.isBlank(message)) {
            message = defaultMessage();
        }
        return message;
    }
}
