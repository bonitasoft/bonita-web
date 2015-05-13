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
package org.bonitasoft.web.rest.model.portal.page;

import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ItemAttribute;

/**
 * @author Fabio Lombardi
 *
 */
public class PageDefinition extends ItemDefinition<PageItem> {
	public static final String TOKEN = "page";

    protected static final String API_URL = "../API/portal/page";

    public static final PageDefinition get() {
        return (PageDefinition) Definitions.get(TOKEN);
    }

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
        createAttribute(PageItem.ATTRIBUTE_ID, ItemAttribute.TYPE.ITEM_ID);
        createAttribute(PageItem.ATTRIBUTE_URL_TOKEN, ItemAttribute.TYPE.STRING)
                .isMandatory(true);
        createAttribute(PageItem.ATTRIBUTE_DISPLAY_NAME, ItemAttribute.TYPE.TEXT)
                .isMandatory(true);
        createAttribute(PageItem.ATTRIBUTE_CONTENT_NAME, ItemAttribute.TYPE.STRING)
                .isMandatory(true);
        createAttribute(PageItem.ATTRIBUTE_DESCRIPTION, ItemAttribute.TYPE.TEXT);
        createAttribute(PageItem.ATTRIBUTE_CREATION_DATE, ItemAttribute.TYPE.DATE);
        createAttribute(PageItem.ATTRIBUTE_CREATED_BY_USER_ID, ItemAttribute.TYPE.ITEM_ID);
        createAttribute(PageItem.ATTRIBUTE_IS_PROVIDED, ItemAttribute.TYPE.BOOLEAN);
        createAttribute(PageItem.ATTRIBUTE_LAST_UPDATE_DATE, ItemAttribute.TYPE.DATETIME);
        createAttribute(PageItem.ATTRIBUTE_UPDATED_BY_USER_ID, ItemAttribute.TYPE.ITEM_ID);
        createAttribute(PageItem.ATTRIBUTE_PROCESS_ID, ItemAttribute.TYPE.ITEM_ID);
        createAttribute(PageItem.ATTRIBUTE_CONTENT_TYPE, ItemAttribute.TYPE.STRING);
    }

    @Override
    protected void definePrimaryKeys() {
        setPrimaryKeys(PageItem.ATTRIBUTE_ID);
    }

    @Override
    protected PageItem _createItem() {
        return new PageItem();
    }

}
