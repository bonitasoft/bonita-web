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
package org.bonitasoft.web.toolkit.client.common.json;

import java.util.LinkedList;
import java.util.List;

import org.bonitasoft.web.toolkit.client.data.item.IItem;

/**
 * This class is a set of functions used to write the JSon to return to the GWT client side
 * 
 * @author Séverin Moussel
 * @param <T>
 *            The class of the Items to write
 */
public class JSonItemWriter<T extends IItem> {

    /**
     * The items to write
     */
    private final List<IItem> itemList = new LinkedList<>();

    /**
     * If you use this constructor, you will have to use one of the append functions.
     */
    public JSonItemWriter() {

    }

    public JSonItemWriter<T> append(final List<T> datas) {
        this.itemList.addAll(datas);
        return this;
    }

    public JSonItemWriter<T> append(final IItem item) {
        this.itemList.add(item);
        return this;
    }

    @Override
    public String toString() {
        return JSonSerializer.serializeCollection(this.itemList);
    }

    public static String itemToJSON(final IItem item) {
        return JSonSerializer.serialize(item);
    }

}
