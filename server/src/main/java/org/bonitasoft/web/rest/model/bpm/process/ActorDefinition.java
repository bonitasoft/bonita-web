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

import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ItemAttribute;

import static org.bonitasoft.web.rest.model.bpm.process.ActorItem.ATTRIBUTE_DESCRIPTION;
import static org.bonitasoft.web.rest.model.bpm.process.ActorItem.ATTRIBUTE_PROCESS_ID;
import static org.bonitasoft.web.toolkit.client.data.item.template.ItemHasDualName.ATTRIBUTE_DISPLAY_NAME;
import static org.bonitasoft.web.toolkit.client.data.item.template.ItemHasDualName.ATTRIBUTE_NAME;
import static org.bonitasoft.web.toolkit.client.data.item.template.ItemHasUniqueId.ATTRIBUTE_ID;

/**
 * @author Haojie Yuan
 * @author Séverin Moussel
 * 
 */
public class ActorDefinition extends ItemDefinition {

    /**
     * Singleton
     */
    public static final ActorDefinition get() {
        return (ActorDefinition) Definitions.get(TOKEN);
    }

    public static final String TOKEN = "actor";

    @Override
    public String defineToken() {
        return TOKEN;
    }

    private static final String API_URL = "../API/bpm/actor";

    @Override
    protected String defineAPIUrl() {
        return API_URL;
    }

    @Override
    protected void definePrimaryKeys() {
        setPrimaryKeys(ATTRIBUTE_ID);
    }

    /**
     * categoryList
     */
    @Override
    protected void defineAttributes() {
        createAttribute(ATTRIBUTE_ID, ItemAttribute.TYPE.ITEM_ID);
        createAttribute(ATTRIBUTE_NAME, ItemAttribute.TYPE.STRING).isMandatory();
        createAttribute(ATTRIBUTE_DISPLAY_NAME, ItemAttribute.TYPE.STRING);
        createAttribute(ATTRIBUTE_DESCRIPTION, ItemAttribute.TYPE.TEXT);
        createAttribute(ATTRIBUTE_PROCESS_ID, ItemAttribute.TYPE.ITEM_ID).isMandatory();
    }

    @Override
    public IItem _createItem() {
        return new ActorItem();
    }

    @Override
    protected void defineDeploys() {
        declareDeployable(ATTRIBUTE_PROCESS_ID, ProcessDefinition.get());
    }

}
