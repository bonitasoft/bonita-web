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

import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

/**
 * @author Séverin Moussel
 * 
 */
class AbstractAPIRequest extends QueuableRequest {

    protected APICallback callback;

    protected final ItemDefinition<?> itemDefinition;

    public AbstractAPIRequest(final ItemDefinition<?> itemDefinition) {
        super();
        this.itemDefinition = itemDefinition;
    }

    @Override
    public void run() {
        this.request.setHeader("Content-Type", HttpRequest.CONTENT_TYPE_JSON);
        super.run();
    }

    /**
     * @return the itemDefinition
     */
    public ItemDefinition<?> getItemDefinition() {
        return this.itemDefinition;
    }

}
