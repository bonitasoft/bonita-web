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
package org.bonitasoft.platform.client.monitoring.model;

import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ItemAttribute;

/**
 * @author Julien Mege
 * 
 */
public class SystemPropertyDefinition extends ItemDefinition<SystemPropertyItem> {

    /**
     * Singleton
     */
    public static final SystemPropertyDefinition get() {
        return (SystemPropertyDefinition) Definitions.get(TOKEN);
    }

    public static final String TOKEN = "SystemProperty";

    /**
     * the URL of user resource
     */
    private static final String API_URL = "../API/platform/systemProperty";

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    protected void definePrimaryKeys() {
        setPrimaryKeys(SystemPropertyItem.ATTRIBUTE_NAME);
    }

    @Override
    protected String defineAPIUrl() {
        return API_URL;
    }

    /**
     * Default Constructor.
     */
    public SystemPropertyDefinition() {
    }

    @Override
    protected void defineAttributes() {
        createAttribute(SystemPropertyItem.ATTRIBUTE_NAME, ItemAttribute.TYPE.STRING);
        createAttribute(SystemPropertyItem.ATTRIBUTE_VALUE, ItemAttribute.TYPE.STRING);
    }

    @Override
    protected SystemPropertyItem _createItem() {
        return new SystemPropertyItem();
    }

    @Override
    public APICaller<SystemPropertyItem> getAPICaller() {
        return new APICaller<SystemPropertyItem>(this);
    }

}
