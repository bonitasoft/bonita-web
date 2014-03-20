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
package org.bonitasoft.web.toolkit.client.data.api.request;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;


/**
 * @author Fabio Lombardi
 *
 */
public class BonitaRequestTimeoutException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default Constructor.
     * @param request
     * @param timeoutMillis
     */
    public BonitaRequestTimeoutException() {
    }

    @Override
    public String getMessage() {
        return _("No response has been received yet, but the background operation might still be in progress. Wait a few seconds then reload the page.");
    }

}
