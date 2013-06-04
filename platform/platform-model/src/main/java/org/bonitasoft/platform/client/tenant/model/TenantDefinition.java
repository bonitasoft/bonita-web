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
package org.bonitasoft.platform.client.tenant.model;

import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ItemAttribute;
import org.bonitasoft.web.toolkit.client.data.item.attribute.modifier.DefaultValueModifier;

/**
 * @author Haojie Yuan
 */
public class TenantDefinition extends ItemDefinition<TenantItem> {

    /**
     * Singleton
     */
    public static final TenantDefinition get() {
        return (TenantDefinition) Definitions.get(TOKEN);
    }

    /**
     * the URL of logs resource
     */
    private static final String API_URL = "../API/platform/tenant";

    public static final String TOKEN = "tenant";

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    protected void definePrimaryKeys() {
        setPrimaryKeys(TenantItem.ATTRIBUTE_ID);
    }

    @Override
    protected String defineAPIUrl() {
        return API_URL;
    }

    @Override
    protected void defineAttributes() {
        createAttribute(TenantItem.ATTRIBUTE_ICON, ItemAttribute.TYPE.IMAGE);
        createAttribute(TenantItem.ATTRIBUTE_ID, ItemAttribute.TYPE.STRING);
        createAttribute(TenantItem.ATTRIBUTE_NAME, ItemAttribute.TYPE.STRING);
        createAttribute(TenantItem.ATTRIBUTE_DESCRIPTION, ItemAttribute.TYPE.TEXT);
        createAttribute(TenantItem.ATTRIBUTE_STATE, ItemAttribute.TYPE.STRING)
                .addInputModifier(new DefaultValueModifier("Disable"));
        createAttribute(TenantItem.ATTRIBUTE_CREATED_DATE, ItemAttribute.TYPE.STRING);
        createAttribute(TenantItem.ATTRIBUTE_USERNAME, ItemAttribute.TYPE.STRING);
        createAttribute(TenantItem.ATTRIBUTE_PASSWORD, ItemAttribute.TYPE.PASSWORD);

    }

    @Override
    public TenantItem _createItem() {
        return new TenantItem();
    }

    @Override
    public APICaller<TenantItem> getAPICaller() {
        return new APICaller<TenantItem>(this);
    }

}
