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
package org.bonitasoft.web.rest.model.bpm.cases;

import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ItemAttribute;

/**
 * @author Gai Cuisha
 * 
 */
public class CommentDefinition extends ItemDefinition<CommentItem> {

    /**
     * Singleton
     */
    public static final CommentDefinition get() {
        return (CommentDefinition) Definitions.get(TOKEN);
    }

    public static final String TOKEN = "comment";

    /**
     * the URL of user resource
     */
    private static final String API_URL = "../API/bpm/comment";

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    protected void definePrimaryKeys() {
        setPrimaryKeys(CommentItem.ATTRIBUTE_ID);
    }

    @Override
    protected String defineAPIUrl() {
        return API_URL;
    }

    @Override
    protected void defineAttributes() {
        createAttribute(CommentItem.ATTRIBUTE_ID, ItemAttribute.TYPE.ITEM_ID);
        createAttribute(CommentItem.ATTRIBUTE_TENANT_ID, ItemAttribute.TYPE.ITEM_ID);
        createAttribute(CommentItem.ATTRIBUTE_USER_ID, ItemAttribute.TYPE.ITEM_ID);
        createAttribute(CommentItem.ATTRIBUTE_PROCESS_INSTANCE_ID, ItemAttribute.TYPE.ITEM_ID);
        createAttribute(CommentItem.ATTRIBUTE_POST_DATE, ItemAttribute.TYPE.DATETIME);
        createAttribute(CommentItem.ATTRIBUTE_CONTENT, ItemAttribute.TYPE.TEXT)
                .isMandatory();
    }

    @Override
    public CommentItem _createItem() {
        return new CommentItem();
    }

}
