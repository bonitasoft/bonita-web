/**
 * Copyright (C) 2011 BonitaSoft S.A.
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
package org.bonitasoft.web.rest.model.bpm.process;

import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ItemAttribute;
import org.bonitasoft.web.toolkit.client.data.item.attribute.validator.EnumValidator;

/**
 * @author Vincent Elcrin
 */
public class ProcessDefinition extends ItemDefinition<ProcessItem> {

    /**
     * Singleton
     */
    public static final ProcessDefinition get() {
        return (ProcessDefinition) Definitions.get(TOKEN);
    }

    public static final String TOKEN = "process";

    /**
     * the URL of users resource
     */
    private static final String API_URL = "../API/bpm/process";

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    protected void definePrimaryKeys() {
        setPrimaryKeys(ProcessItem.ATTRIBUTE_ID);
    }

    @Override
    protected String defineAPIUrl() {
        return API_URL;
    }

    /**
     * categoryList
     */
    @Override
    protected void defineAttributes() {
        createAttribute(ProcessItem.ATTRIBUTE_ID, ItemAttribute.TYPE.ITEM_ID);
        createAttribute(ProcessItem.ATTRIBUTE_NAME, ItemAttribute.TYPE.STRING)
                .isMandatory();
        createAttribute(ProcessItem.ATTRIBUTE_DISPLAY_NAME, ItemAttribute.TYPE.STRING);
        createAttribute(ProcessItem.ATTRIBUTE_DESCRIPTION, ItemAttribute.TYPE.TEXT);
        createAttribute(ProcessItem.ATTRIBUTE_DISPLAY_DESCRIPTION, ItemAttribute.TYPE.TEXT);
        createAttribute(ProcessItem.ATTRIBUTE_VERSION, ItemAttribute.TYPE.STRING)
                .isMandatory();
        createAttribute(ProcessItem.ATTRIBUTE_ACTIVATION_STATE, ItemAttribute.TYPE.ENUM)
                .addValidator(new EnumValidator(ProcessItem.VALUE_ACTIVATION_STATE_DISABLED, ProcessItem.VALUE_ACTIVATION_STATE_ENABLED));
        createAttribute(ProcessItem.ATTRIBUTE_CONFIGURATION_STATE, ItemAttribute.TYPE.ENUM)
                .addValidator(new EnumValidator(ProcessItem.VALUE_CONFIGURATION_STATE_UNRESOLVED, ProcessItem.VALUE_CONFIGURATION_STATE_RESOLVED));
        createAttribute(ProcessItem.ATTRIBUTE_DEPLOYED_BY_USER_ID, ItemAttribute.TYPE.ITEM_ID);
        createAttribute(ProcessItem.ATTRIBUTE_DEPLOYMENT_DATE, ItemAttribute.TYPE.DATETIME);
        createAttribute(ProcessItem.ATTRIBUTE_LAST_UPDATE_DATE, ItemAttribute.TYPE.DATETIME);
        createAttribute(ProcessItem.ATTRIBUTE_ACTOR_INITIATOR_ID, ItemAttribute.TYPE.ITEM_ID);
    }

    @Override
    public ProcessItem _createItem() {
        return new ProcessItem();
    }

}
