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
package org.bonitasoft.console.client.model.bpm.process;

import static org.bonitasoft.console.client.model.bpm.process.ProcessConnectorItem.ATTRIBUTE_CLASSNAME;
import static org.bonitasoft.console.client.model.bpm.process.ProcessConnectorItem.ATTRIBUTE_IMPLEMENTATION_NAME;
import static org.bonitasoft.console.client.model.bpm.process.ProcessConnectorItem.ATTRIBUTE_IMPLEMENTATION_VERSION;
import static org.bonitasoft.console.client.model.bpm.process.ProcessConnectorItem.ATTRIBUTE_NAME;
import static org.bonitasoft.console.client.model.bpm.process.ProcessConnectorItem.ATTRIBUTE_PROCESS_ID;
import static org.bonitasoft.console.client.model.bpm.process.ProcessConnectorItem.ATTRIBUTE_VERSION;

import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ItemAttribute;

/**
 * @author Séverin Moussel
 */
public class ProcessConnectorDefinition extends ItemDefinition {

    /**
     * Singleton
     */
    public static final ProcessConnectorDefinition get() {
        return (ProcessConnectorDefinition) Definitions.get(TOKEN);
    }

    public static final String TOKEN = "processconnector";

    private static final String API_URL = "../API/bpm/processConnector";

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    protected String defineAPIUrl() {
        return API_URL;
    }

    @Override
    protected void definePrimaryKeys() {
        setPrimaryKeys(
                ATTRIBUTE_PROCESS_ID,
                ATTRIBUTE_NAME,
                ATTRIBUTE_VERSION);
    }

    @Override
    protected void defineAttributes() {
        createAttribute(ATTRIBUTE_NAME, ItemAttribute.TYPE.STRING)
                .isMandatory();
        createAttribute(ATTRIBUTE_VERSION, ItemAttribute.TYPE.STRING)
                .isMandatory();
        createAttribute(ATTRIBUTE_PROCESS_ID, ItemAttribute.TYPE.ITEM_ID)
                .isMandatory();
        createAttribute(ATTRIBUTE_IMPLEMENTATION_NAME, ItemAttribute.TYPE.STRING)
                .isMandatory();
        createAttribute(ATTRIBUTE_IMPLEMENTATION_VERSION, ItemAttribute.TYPE.STRING)
                .isMandatory();
        createAttribute(ATTRIBUTE_CLASSNAME, ItemAttribute.TYPE.STRING)
                .isMandatory();
    }

    @Override
    protected void defineDeploys() {
        declareDeployable(ATTRIBUTE_PROCESS_ID, ProcessDefinition.get());
    }

    @Override
    protected IItem _createItem() {
        return new ProcessConnectorItem();
    }

    @Override
    public APICaller<ProcessConnectorItem> getAPICaller() {
        return new APICaller<ProcessConnectorItem>(this);
    }

}
