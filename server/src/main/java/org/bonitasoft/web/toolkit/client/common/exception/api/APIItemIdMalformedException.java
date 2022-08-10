/**
 * Copyright (C) 2021 BonitaSoft S.A.
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
 * @author Dumitru Corini
 * 
 */
public class APIItemIdMalformedException extends APIItemException {

    private static final long serialVersionUID = 7963164445474080661L;

    public APIItemIdMalformedException(final String itemType, final String message) {
        super(itemType, message);
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
        return "The format of the id was incorrect for " + getItemType() + " for API " + getApi() + "#" + getResource();
    }

}
