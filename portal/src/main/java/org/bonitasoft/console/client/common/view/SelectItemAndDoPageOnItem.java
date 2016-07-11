/**
 * Copyright (C) 2012 BonitaSoft S.A.
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
package org.bonitasoft.console.client.common.view;

import java.util.List;

import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.ui.action.form.FormAction;
import org.bonitasoft.web.toolkit.client.ui.page.PageOnItem;

/**
 * @author Séverin Moussel
 * 
 */
public abstract class SelectItemAndDoPageOnItem<T extends IItem> extends PageOnItem<T> {

    public SelectItemAndDoPageOnItem(final APIID itemId, final ItemDefinition itemDefinition) {
        super(itemId, itemDefinition);
    }

    public SelectItemAndDoPageOnItem(final ItemDefinition itemDefinition) {
        super(itemDefinition);
    }

    public SelectItemAndDoPageOnItem(final String itemId, final ItemDefinition itemDefinition) {
        super(itemId, itemDefinition);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CONFIGURATION
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    protected abstract FormAction defineSubmitButtonAction();

    protected abstract String defineSubmitButtonLabel(final T item);

    protected abstract String defineSubmitButtonTooltip(final T item);

    protected abstract List<SelectItemAndDoEntry> defineEntries();

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // BUILD CONTENT
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public final void buildView(final T item) {

        // Before
        buildBefore(item);

        // Build form
        final SelectItemAndDoForm selectItemAndDoForm = new SelectItemAndDoForm(
                defineEntries(),
                defineSubmitButtonLabel(item),
                defineSubmitButtonTooltip(item),
                defineSubmitButtonAction());
        selectItemAndDoForm.addHiddenEntries(getParameters().getValues());

        addBody(selectItemAndDoForm);
    }

    /**
     * Override if you need to display something before selecting an Item
     * 
     * @param item
     */
    protected void buildBefore(final T item) {
        // Do nothing if not overridden
    }

}
