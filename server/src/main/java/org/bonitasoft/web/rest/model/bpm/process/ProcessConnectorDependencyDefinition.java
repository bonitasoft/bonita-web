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

/**
 * @author Gai Cuisha
 * 
 */
public class ProcessConnectorDependencyDefinition extends ItemDefinition {

    /**
     * Singleton
     */
    public static final ProcessConnectorDependencyDefinition get() {
        return (ProcessConnectorDependencyDefinition) Definitions.get(TOKEN);
    }

    public static final String TOKEN = "processconnectordependency";

    /**
     * the URL of user resource
     */
    private static final String API_URL = "../API/bpm/processConnectorDependency";

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    protected void definePrimaryKeys() {
        setPrimaryKeys(
                ProcessConnectorDependencyItem.ATTRIBUTE_PROCESS_ID,
                ProcessConnectorDependencyItem.ATTRIBUTE_CONNECTOR_NAME,
                ProcessConnectorDependencyItem.ATTRIBUTE_CONNECTOR_VERSION,
                ProcessConnectorDependencyItem.ATTRIBUTE_FILENAME);
    }

    @Override
    protected String defineAPIUrl() {
        return API_URL;
    }

    @Override
    protected void defineAttributes() {
        createAttribute(ProcessConnectorDependencyItem.ATTRIBUTE_PROCESS_ID, ItemAttribute.TYPE.ITEM_ID).isMandatory();
        createAttribute(ProcessConnectorDependencyItem.ATTRIBUTE_CONNECTOR_NAME, ItemAttribute.TYPE.STRING).isMandatory();
        createAttribute(ProcessConnectorDependencyItem.ATTRIBUTE_CONNECTOR_VERSION, ItemAttribute.TYPE.STRING).isMandatory();
        createAttribute(ProcessConnectorDependencyItem.ATTRIBUTE_FILENAME, ItemAttribute.TYPE.STRING).isMandatory();
    }

    @Override
    protected IItem _createItem() {
        return new ProcessConnectorDependencyItem();
    }
}
