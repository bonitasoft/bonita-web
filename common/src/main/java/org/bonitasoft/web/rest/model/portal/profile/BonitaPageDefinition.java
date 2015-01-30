/**
 * Copyright (C) 2009, 2013 BonitaSoft S.A.
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
package org.bonitasoft.web.rest.model.portal.profile;

import org.bonitasoft.web.rest.model.identity.UserItem;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ItemAttribute;

/**
 * @author Fabio Lombardi
 *
 */
public class BonitaPageDefinition extends ItemDefinition<BonitaPageItem> {

    /**
     * Singleton
     */
    public static final BonitaPageDefinition get() {
        return (BonitaPageDefinition) Definitions.get(TOKEN);
    }

    public static final String TOKEN = "bonitaPage";

    /**
     * the URL of profile resource
     */
    protected static final String API_URL = "../API/portal/bonitaPage";

    @Override
    protected String defineToken() {
        return TOKEN;
    }

    @Override
    protected String defineAPIUrl() {
        return API_URL;
    }

    @Override
    protected void defineAttributes() {
        createAttribute(UserItem.ATTRIBUTE_ID, ItemAttribute.TYPE.ITEM_ID);
        createAttribute(BonitaPageItem.ATTRIBUTE_TOKEN, ItemAttribute.TYPE.STRING);
        createAttribute(BonitaPageItem.ATTRIBUTE_NAME, ItemAttribute.TYPE.STRING);
        createAttribute(BonitaPageItem.ATTRIBUTE_DESCRIPTION, ItemAttribute.TYPE.STRING);
        createAttribute(BonitaPageItem.ATTRIBUTE_DISPLAY_NAME, ItemAttribute.TYPE.STRING);
    }

    @Override
    protected void definePrimaryKeys() {
        setPrimaryKeys(BonitaPageItem.ATTRIBUTE_TOKEN);
    }

    @Override
    protected BonitaPageItem _createItem() {
        return new BonitaPageItem();
    }

}
