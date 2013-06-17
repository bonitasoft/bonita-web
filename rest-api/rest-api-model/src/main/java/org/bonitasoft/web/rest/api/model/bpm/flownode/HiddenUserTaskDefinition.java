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
package org.bonitasoft.web.rest.api.model.bpm.flownode;

import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ItemAttribute;

/**
 * HiddenActivity definition
 * 
 * @author Julien Mege
 * 
 */
public class HiddenUserTaskDefinition extends ItemDefinition {

    /**
     * Singleton
     */
    public static final HiddenUserTaskDefinition get() {
        return (HiddenUserTaskDefinition) Definitions.get(TOKEN);
    }

    public static final String TOKEN = "hiddenUserTask";

    /**
     * the URL of user resource
     */
    protected static final String API_URL = "../API/bpm/hiddenUserTask";

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    public void definePrimaryKeys() {
        setPrimaryKeys(HiddenUserTaskItem.ATTRIBUTE_USER_ID, HiddenUserTaskItem.ATTRIBUTE_TASK_ID);
    }

    @Override
    protected String defineAPIUrl() {
        return API_URL;
    }

    @Override
    protected void defineAttributes() {
        createAttribute(HiddenUserTaskItem.ATTRIBUTE_USER_ID, ItemAttribute.TYPE.ITEM_ID).isMandatory();
        createAttribute(HiddenUserTaskItem.ATTRIBUTE_TASK_ID, ItemAttribute.TYPE.ITEM_ID).isMandatory();
    }

    @Override
    public IItem _createItem() {
        return new HiddenUserTaskItem();
    }

    @Override
    public APICaller<HiddenUserTaskItem> getAPICaller() {
        return new APICaller<HiddenUserTaskItem>(this);
    }

}
