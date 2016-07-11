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

/**
 * @author Séverin Moussel
 * 
 */
public class APIItemException extends APIException {

    private static final long serialVersionUID = -5137218346282966251L;

    protected final String itemType;

    public APIItemException(final String itemType) {
        this.itemType = itemType.toLowerCase();
    }

    public APIItemException(final String itemType, final String message, final Throwable cause) {
        super(message, cause);
        this.itemType = itemType.toLowerCase();
    }

    public APIItemException(final String itemType, final String message) {
        super(message);
        this.itemType = itemType.toLowerCase();
    }

    public APIItemException(final String itemType, final Throwable cause) {
        super(cause);
        this.itemType = itemType.toLowerCase();
    }

    /**
     * @return the itemType
     */
    public String getItemType() {
        return this.itemType;
    }

    @Override
    protected JsonExceptionSerializer buildJson() {
        return super.buildJson()
                .appendAttribute("itemtype", getItemType());
    }

    @Override
    protected String defaultMessage() {
        return "An iunknown error occured on item " + getItemType() + " for API " + getApi() + "#" + getResource();
    }

}
