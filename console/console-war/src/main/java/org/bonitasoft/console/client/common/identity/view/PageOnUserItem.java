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
package org.bonitasoft.console.client.common.identity.view;

import org.bonitasoft.web.rest.api.model.identity.UserDefinition;
import org.bonitasoft.web.rest.api.model.identity.UserItem;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.ui.page.PageOnItem;

/**
 * @author Séverin Moussel
 * 
 */
public abstract class PageOnUserItem extends PageOnItem<UserItem> {

    public PageOnUserItem(final APIID itemId) {
        super(itemId, Definitions.get(UserDefinition.TOKEN));
    }

    public PageOnUserItem() {
        super(Definitions.get(UserDefinition.TOKEN));
    }

    public PageOnUserItem(final String itemId) {
        super(itemId, Definitions.get(UserDefinition.TOKEN));
    }

}
