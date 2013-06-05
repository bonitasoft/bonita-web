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
package org.bonitasoft.web.toolkit.client.data.api.request;

import java.util.ArrayList;

import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

/**
 * @author Julien Mege
 * 
 */
abstract class AbstractAPIManyIdsRequest extends AbstractAPIRequest {

    protected ArrayList<APIID> ids = new ArrayList<APIID>();

    public AbstractAPIManyIdsRequest(final ItemDefinition itemDefinition) {
        super(itemDefinition);
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(final APIID id) {
        this.ids.clear();
        this.ids.add(id);
    }

    /**
     * @param ids
     *            the ids to set
     */
    public void setIds(final ArrayList<APIID> ids) {
        this.ids.clear();
        this.ids.addAll(ids);
    }

}
