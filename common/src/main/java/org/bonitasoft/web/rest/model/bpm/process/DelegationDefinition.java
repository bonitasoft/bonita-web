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
 * Delegation definition
 * 
 * @author Qixiang Zhang
 * 
 */
public class DelegationDefinition extends ItemDefinition {

    /**
     * Singleton
     */
    public static final DelegationDefinition get() {
        return (DelegationDefinition) Definitions.get(TOKEN);
    }

    /**
     * token for delegate
     */
    public static final String TOKEN = "delegation";

    /**
     * the URL of delegate resource
     */
    private static final String API_URL = "../API/bpm/delegation";

    /**
     * {@inheritDoc}
     */
    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    protected String defineAPIUrl() {
        return API_URL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void defineAttributes() {
        createAttribute(DelegationItem.ATTRIBUTE_USER_NAME, ItemAttribute.TYPE.STRING);
        createAttribute(DelegationItem.ATTRIBUTE_ICON, ItemAttribute.TYPE.IMAGE);
        createAttribute(DelegationItem.ATTRIBUTE_START_DATE, ItemAttribute.TYPE.STRING);
        createAttribute(DelegationItem.ATTRIBUTE_END_DATE, ItemAttribute.TYPE.STRING);
        createAttribute(DelegationItem.ATTRIBUTE_DELEGATE_STATE, ItemAttribute.TYPE.TEXT);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void definePrimaryKeys() {
        setPrimaryKeys(DelegationItem.ATTRIBUTE_ID);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected IItem _createItem() {
        return new DelegationItem();
    }
}
