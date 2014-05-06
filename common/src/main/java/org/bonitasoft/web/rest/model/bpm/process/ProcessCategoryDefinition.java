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
package org.bonitasoft.web.rest.model.bpm.process;

import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ItemAttribute.TYPE;

/**
 * @author Séverin Moussel
 * 
 */
public class ProcessCategoryDefinition extends ItemDefinition {

    /**
     * Singleton
     */
    public static final ProcessCategoryDefinition get() {
        return (ProcessCategoryDefinition) Definitions.get(TOKEN);
    }

    public static final String TOKEN = "processcategory";

    public static final String API_URL = "../API/bpm/processCategory";

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
        createAttribute(ProcessCategoryItem.ATTRIBUTE_PROCESS_ID, TYPE.ITEM_ID).isMandatory();
        createAttribute(ProcessCategoryItem.ATTRIBUTE_CATEGORY_ID, TYPE.ITEM_ID).isMandatory();
    }

    @Override
    protected void definePrimaryKeys() {
        setPrimaryKeys(
                ProcessCategoryItem.ATTRIBUTE_PROCESS_ID,
                ProcessCategoryItem.ATTRIBUTE_CATEGORY_ID);
    }

    @Override
    protected IItem _createItem() {
        return new ProcessCategoryItem();
    }
}
