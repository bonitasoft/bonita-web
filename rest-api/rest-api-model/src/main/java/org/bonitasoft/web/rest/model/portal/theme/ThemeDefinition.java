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
package org.bonitasoft.web.rest.model.portal.theme;

import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ItemAttribute;

/**
 * @author Gai Cuisha
 * 
 */
public class ThemeDefinition extends ItemDefinition<ThemeItem> {

    /**
     * Singleton
     */
    public static final ThemeDefinition get() {
        return (ThemeDefinition) Definitions.get(TOKEN);
    }

    public static final String TOKEN = "theme";

    /**
     * the URL of user resource
     */
    private static final String API_URL = "../API/userXP/theme";

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    protected String defineAPIUrl() {
        return API_URL;
    }

    @Override
    protected void defineAttributes() {
        createAttribute(ThemeItem.ATTRIBUTE_ID, ItemAttribute.TYPE.ITEM_ID);
        createAttribute(ThemeItem.ATTRIBUTE_TYPE, ItemAttribute.TYPE.STRING);
        createAttribute(ThemeItem.ATTRIBUTE_INSTALLEDDATE, ItemAttribute.TYPE.STRING);
        createAttribute(ThemeItem.ATTRIBUTE_ISDEFAULT, ItemAttribute.TYPE.STRING);
        // this.createAttribute(ThemeItem.ATTRIBUTE_imagePreview, ItemAttribute.TYPE.IMAGE);
    }

    @Override
    protected void definePrimaryKeys() {
        setPrimaryKeys(ThemeItem.ATTRIBUTE_ID);
    }

    @Override
    protected ThemeItem _createItem() {
        return new ThemeItem();
    }

    @Override
    public APICaller<ThemeItem> getAPICaller() {
        return new APICaller<ThemeItem>(this);
    }
}
