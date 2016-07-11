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

import java.util.Map;

import org.bonitasoft.web.toolkit.client.common.json.JSonItemWriter;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.ui.component.form.AbstractForm;

/**
 * @author Séverin Moussel
 * 
 */
abstract class AbstractAPIItemRequest extends AbstractAPIRequest {

    protected IItem item;

    protected AbstractForm form;

    protected Map<String, String> map;

    public AbstractAPIItemRequest(final ItemDefinition itemDefinition) {
        super(itemDefinition);
    }

    /**
     * @param item
     *            the item to set
     */
    public void setItem(final IItem item) {
        this.item = item;
    }

    public void setItem(final Map<String, String> map) {
        this.map = map;
    }

    @Override
    public void run() {
        if (this.item != null) {
            this.request.setRequestData(JSonItemWriter.itemToJSON(this.item));
        } else if (this.form != null) {
            this.request.setRequestData(this.form.toJson());
        } else if (this.map != null) {
            this.request.setRequestData(JSonItemWriter.mapToJSON(this.map));
        }

        super.run();
    }

}
