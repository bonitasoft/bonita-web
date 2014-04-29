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

import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.ui.component.form.AbstractForm;

/**
 * @author Julien Mege
 */
public abstract class ItemFormAction<T extends IItem> extends FormAction {

    protected ItemDefinition itemDefinition;

    protected IItem item = null;

    public ItemFormAction(final ItemDefinition itemDefinition, final AbstractForm form) {
        super(form);
        this.itemDefinition = itemDefinition;
    }

    public ItemFormAction(final ItemDefinition itemDefinition) {
        super();
        this.itemDefinition = itemDefinition;
    }

}
