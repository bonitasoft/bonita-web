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
package org.bonitasoft.web.toolkit.client.common.i18n.model;

import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ItemAttribute;

/**
 * @author Séverin Moussel
 * 
 */
public class I18nLocaleDefinition extends ItemDefinition<I18nLocaleItem> {

    /**
     * Singleton
     */
    public static final I18nLocaleDefinition get() {
        return (I18nLocaleDefinition) Definitions.get(TOKEN);
    }

    public static final String TOKEN = "i18nlocale";

    /**
     * the URL of i18nlocale resource
     */
    private static final String API_URL = "../API/system/i18nlocale";

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    protected void definePrimaryKeys() {
        setPrimaryKeys(I18nLocaleItem.ATTRIBUTE_LOCALE);
    }

    @Override
    protected String defineAPIUrl() {
        return API_URL;
    }

    @Override
    protected void defineAttributes() {
        createAttribute(I18nLocaleItem.ATTRIBUTE_LOCALE, ItemAttribute.TYPE.STRING);
        createAttribute(I18nLocaleItem.ATTRIBUTE_NAME, ItemAttribute.TYPE.STRING);
    }

    @Override
    protected I18nLocaleItem _createItem() {
        return new I18nLocaleItem();
    }

    @Override
    public APICaller<I18nLocaleItem> getAPICaller() {
        return new APICaller<I18nLocaleItem>(this);
    }

}
