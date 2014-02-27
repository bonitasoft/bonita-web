/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.web.toolkit.client.ui.action.form;

import java.util.Map;

import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ValidatorEngine;
import org.bonitasoft.web.toolkit.client.ui.component.form.AbstractForm;

/**
 * Execute an update for an ItemForm.
 * 
 * @param <ITEM_TYPE>
 *            The type of the item that will be updated
 * 
 * @author Julien Mege
 */
public class UpdateItemFormAction<ITEM_TYPE extends IItem> extends ItemFormAction<ITEM_TYPE> {

    /**
     * Default constructor.
     * 
     * @param itemDefinition
     *            The definition of the item to update.
     * @param form
     *            The form that contains the attribute to updates
     */
    public UpdateItemFormAction(final ItemDefinition itemDefinition, final AbstractForm form) {
        super(itemDefinition, form);
    }

    /**
     * Default constructor.
     * 
     * @param itemDefinition
     *            The definition of the item to update.
     */
    public UpdateItemFormAction(final ItemDefinition itemDefinition) {
        super(itemDefinition);
    }

    @Override
    public void execute() {
        // Validate input from the form (mandatory, text format, ...)
        ValidatorEngine.validate(this.form.getValues(), this.itemDefinition.getValidators());

        // Call the REST API
        new APICaller(itemDefinition).update(this.getParameter("id"), this.form, new APICallback() {

            @Override
            public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
                ViewController.getInstance().historyBack();
            }
        });

    }

}
