/**
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.console.client.admin.page.action;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.console.client.admin.page.view.DeleteCustomPage;
import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.ui.action.ActionOnItemIds;

/**
 * @author Vincent Elcrin
 *
 */
public class DeleteMultipleItemsPopupCustomPageAction extends ActionOnItemIds {

    private final String itemName;

    private final String itemNamePlural;

    private final String definitionToken;

    /**
     * Default Constructor.
     */
    public DeleteMultipleItemsPopupCustomPageAction(final ItemDefinition definition, final String itemName, final String itemNamePlural) {
        definitionToken = definition.getToken();
        this.itemName = itemName;
        this.itemNamePlural = itemNamePlural;
    }

    @Override
    protected void execute(final List<APIID> itemIds) {
        this.addParameter("itemDef", definitionToken);
        if (itemIds.size() > 1) {
            // multiple selection
            this.addParameter("message",
                    _("These %item_name_plural% and related profile entries will be permanently deleted.", new Arg("item_name_plural", itemNamePlural)));
        } else {
            this.addParameter("message", _("This %item_name% and related profile entries will be permanently deleted.", new Arg("item_name", itemName)));
        }

        final ArrayList<String> idsAsString = new ArrayList<String>();
        for (final APIID apiId : itemIds) {
            idsAsString.add(apiId.toString());
        }
        ViewController.showPopup(new DeleteCustomPage(idsAsString));
    }

}
