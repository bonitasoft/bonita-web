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
package org.bonitasoft.web.toolkit.client.ui.component.form;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.web.toolkit.client.common.exception.http.HttpException;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.ui.utils.Message;

/**
 * @author Séverin Moussel
 * 
 */
public class ItemFormFiller extends FormFiller {

    private APIID itemId = null;

    public ItemFormFiller(final ItemForm<?> target, final String itemId) {
        this(target, APIID.makeAPIID(itemId));
    }

    public ItemFormFiller(final String itemId) {
        this(null, itemId);
    }

    public ItemFormFiller(final ItemForm<?> target, final APIID itemId) {
        super(target);
        this.itemId = itemId;
    }

    public ItemFormFiller(final APIID itemId) {
        this(null, itemId);
    }

    @Override
    protected void getData(final APICallback callback) {
        assert this.target instanceof ItemForm<?> : "Target must be an ItemForm";

        try {
            new APICaller(((ItemForm<?>) this.target).getItemDefinition()).get(this.itemId, callback);
        } catch (final HttpException e) {
            Message.error(_("The item you try to edit can't be retrieved due to a connection problem. Please contact your administrator."));
        }
    }
}
