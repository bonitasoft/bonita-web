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
 * @author Julien Mege
 */
public class AddItemFormAction<T extends IItem> extends ItemFormAction<T> {

    /**
     * Default Constructor.
     * 
     * @param itemDefinition
     * @param form
     */
    public AddItemFormAction(final ItemDefinition itemDefinition, final AbstractForm form) {
        super(itemDefinition, form);
    }

    public AddItemFormAction(final ItemDefinition itemDefinition) {
        super(itemDefinition);
    }

    @Override
    public void execute() {
        execute(new APICallback() {

            @Override
            public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
                ViewController.getInstance().historyBack();
            }

        });
    }

    public void execute(final APICallback callback) {
        ValidatorEngine.validate(this.form, this.itemDefinition.getValidators());
        new APICaller<T>(this.itemDefinition).add(this.form, callback);
    }

}
