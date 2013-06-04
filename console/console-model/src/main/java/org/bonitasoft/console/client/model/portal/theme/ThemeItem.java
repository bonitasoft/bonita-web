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
package org.bonitasoft.console.client.model.portal.theme;

import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.Item;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

/**
 * @author Gai Cuisha
 * 
 */
public class ThemeItem extends Item {

    public ThemeItem() {
        super();
    }

    public ThemeItem(final IItem item) {
        super(item);
    }

    public static final String ATTRIBUTE_ID = "id";

    public static final String ATTRIBUTE_ICON = "icon";

    public static final String ATTRIBUTE_NAME = "name";

    public static final String ATTRIBUTE_AUTHOR = "author";

    public static final String ATTRIBUTE_DESCRIPTION = "description";

    public static final String ATTRIBUTE_INSTALLEDBY = "installedBy";

    public static final String ATTRIBUTE_INSTALLEDDATE = "installedDate";

    public static final String ATTRIBUTE_ISDEFAULT = "isDefault";

    // public static final String ATTRIBUTE_imagePreview = "imagePreview";

    public ThemeItem(final String id, final String icon, final String name, final String author, final String description, final String installedBy,
            final String installedDate, final String isDefault) {
        this.setAttribute(ATTRIBUTE_ID, id);
        this.setAttribute(ATTRIBUTE_ICON, icon);
        this.setAttribute(ATTRIBUTE_NAME, name);
        this.setAttribute(ATTRIBUTE_AUTHOR, author);
        this.setAttribute(ATTRIBUTE_DESCRIPTION, description);
        this.setAttribute(ATTRIBUTE_INSTALLEDBY, installedBy);
        this.setAttribute(ATTRIBUTE_INSTALLEDDATE, installedDate);
        this.setAttribute(ATTRIBUTE_ISDEFAULT, isDefault);
    }

    public ThemeItem(final String id, final String name) {
        this.setAttribute(ATTRIBUTE_ID, id);
        this.setAttribute(ATTRIBUTE_NAME, name);
    }

    @Override
    public ItemDefinition<ThemeItem> getItemDefinition() {
        return new ThemeDefinition();
    }

}
