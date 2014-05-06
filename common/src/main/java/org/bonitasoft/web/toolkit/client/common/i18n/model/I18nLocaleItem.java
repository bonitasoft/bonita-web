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

import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.Item;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

/**
 * @author Séverin Moussel
 * 
 */
public class I18nLocaleItem extends Item {

    public static final String ATTRIBUTE_NAME = "name";
    public static final String ATTRIBUTE_LOCALE = "locale";

    public I18nLocaleItem() {
        super();
    }

    public I18nLocaleItem(final IItem item) {
        super(item);
    }

    public I18nLocaleItem(final String locale, final String name) {
        this();
        this.setAttribute(ATTRIBUTE_LOCALE, locale);
        this.setAttribute(ATTRIBUTE_NAME, name);
    }

    @Override
    public ItemDefinition<I18nLocaleItem> getItemDefinition() {
        return new I18nLocaleDefinition();
    }

    public String getName() {
        return getAttributeValue(ATTRIBUTE_NAME);
    }
    
    public String getLocale() {
        return getAttributeValue(ATTRIBUTE_LOCALE);
    }
}
