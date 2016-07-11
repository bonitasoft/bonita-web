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
package org.bonitasoft.web.toolkit.client.common.exception.api;

import org.bonitasoft.web.toolkit.client.common.exception.http.JsonExceptionSerializer;
import org.bonitasoft.web.toolkit.client.data.APIID;

/**
 * @author Séverin Moussel
 * 
 */
public class APIItemNotFoundException extends APIItemException {

    private static final long serialVersionUID = -1325943431826471828L;

    private final APIID id;

    public APIItemNotFoundException(final String itemType, final APIID id) {
        super(itemType);
        this.id = id;
    }

    /**
     * @return the id
     */
    public APIID getId() {
        return this.id;
    }

    @Override
    protected JsonExceptionSerializer buildJson() {
        return super.buildJson()
                .appendAttribute("id", getId());
    }

    @Override
    protected String defaultMessage() {
        return this.itemType.substring(0, 1).toUpperCase() + this.itemType.substring(1) + " with id (" + this.id.toString() + ") not found for API " + getApi()
                + "#" + getResource();
    }

}
