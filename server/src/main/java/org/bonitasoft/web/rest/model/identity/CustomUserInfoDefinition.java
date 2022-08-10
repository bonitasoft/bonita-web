/**
 * Copyright (C) 2014 BonitaSoft S.A.
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

import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ItemAttribute;

/**
 * @author Vincent Elcrin
 */
public class CustomUserInfoDefinition extends ItemDefinition<CustomUserInfoItem> {

    public static final String TOKEN = "customuserinfo";

    @Override
    protected String defineToken() {
        return TOKEN;
    }

    @Override
    protected String defineAPIUrl() {
        return "../API/customuserinfo/user";
    }

    @Override
    protected void defineAttributes() {
        createAttribute(CustomUserInfoItem.ATTRIBUTE_DEFINITION_ID, ItemAttribute.TYPE.ITEM_ID);
        createAttribute(CustomUserInfoItem.ATTRIBUTE_USER_ID, ItemAttribute.TYPE.ITEM_ID);
        createAttribute(CustomUserInfoItem.ATTRIBUTE_VALUE, ItemAttribute.TYPE.STRING);
    }

    @Override
    protected void defineDeploys() {
        declareDeployable(CustomUserInfoItem.ATTRIBUTE_DEFINITION_ID, CustomUserInfoDefinitionDefinition.get());
    }

    @Override
    protected void definePrimaryKeys() {
        setPrimaryKeys(CustomUserInfoItem.ATTRIBUTE_USER_ID, CustomUserInfoItem.ATTRIBUTE_DEFINITION_ID);
    }

    @Override
    protected CustomUserInfoItem _createItem() {
        return new CustomUserInfoItem();
    }

    public static CustomUserInfoDefinition get() {
        return (CustomUserInfoDefinition) Definitions.get(TOKEN);
    }
}
