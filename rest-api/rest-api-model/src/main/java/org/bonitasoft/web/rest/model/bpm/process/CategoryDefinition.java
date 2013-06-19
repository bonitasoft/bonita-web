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
package org.bonitasoft.web.rest.model.bpm.process;

import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ItemAttribute;

/**
 * @author Zhiheng Yang
 * 
 */
public class CategoryDefinition extends ItemDefinition {

    /**
     * Singleton
     */
    public static final CategoryDefinition get() {
        return (CategoryDefinition) Definitions.get(TOKEN);
    }

    public static final String TOKEN = "categories";

    /**
     * the URL of categories resource
     */
    private static final String API_URL = "../API/bpm/category";

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    protected String defineAPIUrl() {
        return API_URL;
    }

    @Override
    protected void defineAttributes() {
        createAttribute(CategoryItem.ATTRIBUTE_NAME, ItemAttribute.TYPE.STRING).isMandatory();
        createAttribute(CategoryItem.ATTRIBUTE_CREATED_BY_USER_ID, ItemAttribute.TYPE.STRING);
        createAttribute(CategoryItem.ATTRIBUTE_DESCRIPTION, ItemAttribute.TYPE.TEXT);
    }

    @Override
    protected void definePrimaryKeys() {
        setPrimaryKeys(CategoryItem.ATTRIBUTE_ID);
    }

    @Override
    protected IItem _createItem() {
        return new CategoryItem();
    }

    @Override
    public APICaller<CategoryItem> getAPICaller() {
        return new APICaller<CategoryItem>(this);
    }

}
