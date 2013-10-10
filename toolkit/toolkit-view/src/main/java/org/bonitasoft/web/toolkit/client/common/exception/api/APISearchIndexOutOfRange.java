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

import org.bonitasoft.web.toolkit.client.common.exception.http.JsonExceptionSerializer;

/**
 * @author Séverin Moussel
 * 
 */
public class APISearchIndexOutOfRange extends APIException {

    private static final long serialVersionUID = 2618198001118478389L;

    private final int pageNumber;

    public APISearchIndexOutOfRange(final int pageNumber) {
        super();
        this.pageNumber = pageNumber;
    }

    /**
     * @return the pageNumber
     */
    public int getPageNumber() {
        return this.pageNumber;
    }

    @Override
    protected String defaultMessage() {
        return "Page index (" + String.valueOf(getPageNumber()) + ") out of range for API " + getApi() + "#" + getResource();
    }

    @Override
    protected JsonExceptionSerializer buildJson() {
        return super.buildJson()
                .appendAttribute("page", getPageNumber());
    }

}
