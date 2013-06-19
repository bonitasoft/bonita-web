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
package org.bonitasoft.web.rest.model.identity;

import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ItemAttribute;

/**
 * @author Yongtao Guo
 * 
 */
public class RoleDefinition extends ItemDefinition {

    /**
     * Singleton
     */
    public static final RoleDefinition get() {
        return (RoleDefinition) Definitions.get(TOKEN);
    }

    public static final String TOKEN = "role";

    /**
     * the URL of user resource
     */
    private static final String API_URL = "../API/identity/role";

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    protected void definePrimaryKeys() {
        setPrimaryKeys(RoleItem.ATTRIBUTE_ID);
    }

    @Override
    protected String defineAPIUrl() {
        return API_URL;
    }

    @Override
    protected void defineAttributes() {
        createAttribute(RoleItem.ATTRIBUTE_ID, ItemAttribute.TYPE.ITEM_ID);

        createAttribute(RoleItem.ATTRIBUTE_NAME, ItemAttribute.TYPE.STRING);

        createAttribute(GroupItem.ATTRIBUTE_DISPLAY_NAME, ItemAttribute.TYPE.STRING);

        createAttribute(RoleItem.ATTRIBUTE_DESCRIPTION, ItemAttribute.TYPE.TEXT);

        createAttribute(RoleItem.ATTRIBUTE_CREATION_DATE, ItemAttribute.TYPE.DATETIME);

        createAttribute(RoleItem.ATTRIBUTE_CREATED_BY_USER_ID, ItemAttribute.TYPE.ITEM_ID);

        createAttribute(RoleItem.ATTRIBUTE_LAST_UPDATE_DATE, ItemAttribute.TYPE.DATETIME);

        createAttribute(RoleItem.ATTRIBUTE_ICON, ItemAttribute.TYPE.IMAGE);
    }

    @Override
    public IItem _createItem() {
        return new RoleItem();
    }

    @Override
    public APICaller<RoleItem> getAPICaller() {
        return new APICaller<RoleItem>(this);
    }

}
