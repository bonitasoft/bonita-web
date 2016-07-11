/**
 * Copyright (C) 2012 BonitaSoft S.A.
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
package org.bonitasoft.web.toolkit.client.data.item.attribute.reader;

import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.template.ItemHasDualName;

/**
 * @author Colin PUY
 * 
 */
public class ItemHasDualNameAttributeReader extends AbstractAttributeReader {

    public ItemHasDualNameAttributeReader() {
        super(ItemHasDualName.ATTRIBUTE_DISPLAY_NAME);
    }

    @Override
    protected String _read(IItem item) {
        if (item instanceof ItemHasDualName) {
            ItemHasDualName dualNameItem = (ItemHasDualName) item;
            return dualNameItem.getDisplayName() != null ? dualNameItem.getDisplayName() : dualNameItem.getName();
        }
        return null;
    }

}
