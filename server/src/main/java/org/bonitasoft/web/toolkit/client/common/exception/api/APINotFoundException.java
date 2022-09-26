/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.web.toolkit.client.common.exception.api;

import org.bonitasoft.web.toolkit.client.common.i18n.T_;

/**
 * @author Séverin Moussel
 */
public class APINotFoundException extends APIException {

    private static final long serialVersionUID = 7709846894799079466L;

    public APINotFoundException(final Throwable cause) {
        super(cause);
    }

    public APINotFoundException(final String api, final String resource) {
        super((Exception) null);
        setApi(api);
        setResource(resource);
    }

    public APINotFoundException(final T_ localizedMessage, final Throwable cause) {
        super(localizedMessage, cause);
    }

    public APINotFoundException(final T_ localizedMessage) {
        super(localizedMessage);
    }

    @Override
    protected String defaultMessage() {
        return "API " + getApi() + " or resource " + getResource() + " doesn't exist or is not declared";
    }

}
