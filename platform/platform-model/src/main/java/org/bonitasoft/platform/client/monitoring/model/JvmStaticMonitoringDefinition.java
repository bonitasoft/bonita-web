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
 * @author Gai Cuisha
 * 
 */
public class JvmStaticMonitoringDefinition extends ItemDefinition<JvmStaticMonitoringItem> {

    /**
     * Singleton
     */
    public static final JvmStaticMonitoringDefinition get() {
        return (JvmStaticMonitoringDefinition) Definitions.get(TOKEN);
    }

    public static final String TOKEN = "JvmStaticMonitoring";

    /**
     * the URL of user resource
     */
    private static final String API_URL = "../API/platform/jvmStatic";

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    protected void definePrimaryKeys() {
        setPrimaryKeys(JvmStaticMonitoringItem.ATTRIBUTE_JVMNAME);
    }

    @Override
    protected String defineAPIUrl() {
        return API_URL;
    }

    /**
     * Default Constructor.
     */
    public JvmStaticMonitoringDefinition() {
    }

    @Override
    protected void defineAttributes() {
        createAttribute(JvmStaticMonitoringItem.ATTRIBUTE_JVMNAME, ItemAttribute.TYPE.STRING);
        createAttribute(JvmStaticMonitoringItem.ATTRIBUTE_JVMVENDOR, ItemAttribute.TYPE.STRING);
        createAttribute(JvmStaticMonitoringItem.ATTRIBUTE_JVMVERSION, ItemAttribute.TYPE.STRING);
        createAttribute(JvmStaticMonitoringItem.ATTRIBUTE_OSARCH, ItemAttribute.TYPE.STRING);
        createAttribute(JvmStaticMonitoringItem.ATTRIBUTE_OSNAME, ItemAttribute.TYPE.STRING);
        createAttribute(JvmStaticMonitoringItem.ATTRIBUTE_OSVERSION, ItemAttribute.TYPE.STRING);
        createAttribute(JvmStaticMonitoringItem.ATTRIBUTE_AVAILABLEPROCESSORS, ItemAttribute.TYPE.STRING);
    }

    @Override
    protected JvmStaticMonitoringItem _createItem() {
        return new JvmStaticMonitoringItem();
    }

    @Override
    public APICaller<JvmStaticMonitoringItem> getAPICaller() {
        return new APICaller<JvmStaticMonitoringItem>(this);
    }

}
