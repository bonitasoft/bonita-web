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

import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ItemAttribute;

/**
 * @author Séverin Moussel
 * 
 */
public class I18nTranslationDefinition extends ItemDefinition {

    /**
     * Singleton
     */
    public static final I18nTranslationDefinition get() {
        return (I18nTranslationDefinition) Definitions.get(TOKEN);
    }

    public static final String TOKEN = "i18ntranslation";

    /**
     * the URL of i18nlocale resource
     */
    private static final String API_URL = "../API/system/i18ntranslation";

    private static final String PLURAL_RESOURCES_URL = "../API/system/i18ntranslation";

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    protected void definePrimaryKeys() {
        setPrimaryKeys(I18nTranslationItem.ATTRIBUTE_KEY);
    }

    @Override
    protected String defineAPIUrl() {
        return API_URL;
    }

    @Override
    protected void defineAttributes() {
        createAttribute(I18nTranslationItem.ATTRIBUTE_KEY, ItemAttribute.TYPE.TEXT);
        createAttribute(I18nTranslationItem.ATTRIBUTE_VALUE, ItemAttribute.TYPE.TEXT);
    }

    @Override
    protected IItem _createItem() {
        return new I18nTranslationItem();
    }
}
