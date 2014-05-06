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
public class I18nTranslationItem extends Item {

    public I18nTranslationItem() {
        super();
    }

    public I18nTranslationItem(final IItem item) {
        super(item);
    }

    public static final String ATTRIBUTE_KEY = "key";

    public static final String ATTRIBUTE_VALUE = "value";

    public I18nTranslationItem(final String key, final String value) {
        this();
        this.setAttribute(ATTRIBUTE_KEY, key);
        this.setAttribute(ATTRIBUTE_VALUE, value);
    }

    @Override
    public ItemDefinition getItemDefinition() {
        return new I18nTranslationDefinition();
    }

}
