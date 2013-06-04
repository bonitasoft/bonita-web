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

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.bonitasoft.web.toolkit.client.ViewController;
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
 * @author Nicolas TITH
 */
public class AddItemWithDeployFormAction<ITEM_TYPE extends IItem> extends ItemFormAction<ITEM_TYPE> {

    private final String[] deploys;

    /**
     * Default constructor.
     * 
     * @param itemDefinition
     *            The definition of the item to update.
     * @param form
     *            The form that contains the attribute to updates
     */
    public AddItemWithDeployFormAction(final ItemDefinition itemDefinition, final AbstractForm form, final String... deploysToAdd) {
        super(itemDefinition, form);
        this.deploys = deploysToAdd;
    }

    @Override
    public void execute() {
        final HashMap<String, String> values = this.form.getValues().getValues();
        final LinkedHashMap<ItemDefinition, HashMap<String, String>> deploysToAdd = new LinkedHashMap<ItemDefinition, HashMap<String, String>>();

        // The whole form has to be valid before to update anything
        for (final String deploy : this.deploys) {
            final HashMap<String, String> deployValues = getValuesWithKeyStartingWith(deploy + "_", values);
            final ItemDefinition deployDefinition = this.itemDefinition.getDeployDefinition(deploy);
            // Validate input from the form (mandatory, text format, ...)
            ValidatorEngine.validate(deployValues, deployDefinition.getValidators());
            // no need to do it twice. save it for later
            deploysToAdd.put(deployDefinition, deployValues);
        }
        // Validate input from the form (mandatory, text format, ...)
        ValidatorEngine.validate(this.form, this.itemDefinition.getValidators());

        // Call REST API to update item following by deploys
        this.itemDefinition.getAPICaller().add(this.form, new AddDeploysCallback(this.form, deploysToAdd));

    }

    private class AddDeploysCallback extends APICallback {

        private final LinkedHashMap<ItemDefinition, HashMap<String, String>> itemsToAdd;

        private final AbstractForm form;

        /**
         * Default Constructor.
         */
        public AddDeploysCallback(final AbstractForm form, final LinkedHashMap<ItemDefinition, HashMap<String, String>> itemsToAdd) {
            this.itemsToAdd = itemsToAdd;
            this.form = form;
        }

        @Override
        public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
            if (this.itemsToAdd.isEmpty()) {
                ViewController.getInstance().historyBack();
            }

            final ItemDefinition itemDefinition = this.itemsToAdd.keySet().iterator().next();
            this.itemsToAdd.remove(itemDefinition);
            itemDefinition.getAPICaller().add(this.form, new AddDeploysCallback(this.form, this.itemsToAdd));
        }
    }

    private HashMap<String, String> getValuesWithKeyStartingWith(final String prefix, final HashMap<String, String> values) {
        final HashMap<String, String> filteredValues = new HashMap<String, String>();
        for (final String key : values.keySet()) {
            if (key.startsWith(prefix)) {
                final String value = values.get(key);
                filteredValues.put(key.replace(prefix, ""), value);
            }
        }
        return filteredValues;
    }
}
