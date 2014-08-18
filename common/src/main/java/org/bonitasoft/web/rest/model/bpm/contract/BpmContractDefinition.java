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
package org.bonitasoft.web.rest.model.bpm.contract;

import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ItemAttribute;

/**
 * @author Laurent Leseigneur
 *
 */
public class BpmContractDefinition extends ItemDefinition<BpmContractItem> {

    public static final String TOKEN = "contract";

    /**
     * the URL of user resource
     */
    private static final String API_URL = "../API/bpm/contract";

    @Override
    protected void definePrimaryKeys() {
        setPrimaryKeys(BpmContractItem.ATTRIBUTE_ID);
    }

    @Override
    protected BpmContractItem _createItem() {
        return new BpmContractItem();
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
        createAttribute(BpmContractItem.ATTRIBUTE_INPUTS, ItemAttribute.TYPE.STRING).isMandatory(false);
        createAttribute(BpmContractItem.ATTRIBUTE_RULES, ItemAttribute.TYPE.STRING).isMandatory(false);
    }

    public static BpmContractDefinition get() {
        return new BpmContractDefinition();
    }

}
