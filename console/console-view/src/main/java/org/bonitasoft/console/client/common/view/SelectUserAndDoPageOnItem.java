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

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.Arrays;
import java.util.List;

import org.bonitasoft.console.client.data.item.attribute.reader.UserAttributeReader;
import org.bonitasoft.console.client.model.identity.UserDefinition;
import org.bonitasoft.console.client.model.identity.UserItem;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

/**
 * @author Séverin Moussel
 * 
 */
public abstract class SelectUserAndDoPageOnItem<T extends IItem> extends SelectItemAndDoPageOnItem<T> {

    public static String USER_ID = "user_id";

    public SelectUserAndDoPageOnItem(final APIID itemId, final ItemDefinition itemDefinition) {
        super(itemId, itemDefinition);
    }

    public SelectUserAndDoPageOnItem(final ItemDefinition itemDefinition) {
        super(itemDefinition);
    }

    public SelectUserAndDoPageOnItem(final String itemId, final ItemDefinition itemDefinition) {
        super(itemId, itemDefinition);
    }

    /**
     * Override this method to change the name of the parameter passed to the callback action
     * 
     * @return This method must return the name of the parameter
     */
    protected String defineUserIdParameterName() {
        return USER_ID;
    }

    @Override
    protected List<SelectItemAndDoEntry> defineEntries() {
        return Arrays.asList(
                new SelectItemAndDoEntry(
                        defineUserIdParameterName(),
                        _("Select a user"),
                        _("Select a user by typing a part of his name."),
                        UserDefinition.get(),
                        new UserAttributeReader(),
                        UserItem.ATTRIBUTE_ID)
                );
    }
}
