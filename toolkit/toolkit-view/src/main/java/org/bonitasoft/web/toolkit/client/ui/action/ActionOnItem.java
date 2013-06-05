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
package org.bonitasoft.web.toolkit.client.ui.action;

import java.util.Map;

import org.bonitasoft.web.toolkit.client.common.json.JSonItemReader;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

import com.google.gwt.user.client.Window;

/**
 * @author Séverin Moussel
 * 
 */
public abstract class ActionOnItem<T extends IItem> extends ActionOnItemId {

    protected final ItemDefinition itemDefinition;

    public ActionOnItem(final ItemDefinition itemDefinition) {
        this(null, itemDefinition);
    }

    public ActionOnItem(final APIID itemId, final ItemDefinition itemDefinition) {
        super(itemId);
        this.itemDefinition = itemDefinition;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ITEM PRELOADING
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private T __pload = null;

    public final void __pload(final T item) {
        this.__pload = item;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // EXECUTION
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    protected abstract void execute(T item);

    /**
     * Override this method to execute something while the item loading has failed.
     */
    protected void fail(final String message) {
        // TODO replace with the clean alert mechanism while available
        Window.alert(message);
    }

    @Override
    protected final void execute(final APIID itemId) {
        if (this.__pload != null) {
            execute(this.__pload);
            return;
        }

        this.itemDefinition.getAPICaller().get(itemId, new APICallback() {

            @SuppressWarnings("unchecked")
            @Override
            public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
                try {
                    execute((T) JSonItemReader.parseItem(response, ActionOnItem.this.itemDefinition));
                } catch (final Exception e) {
                    fail(e.getMessage());
                }
            }

            @Override
            public void onError(final String message, final Integer errorCode) {
                fail(message);
            }

        });
    }
}
