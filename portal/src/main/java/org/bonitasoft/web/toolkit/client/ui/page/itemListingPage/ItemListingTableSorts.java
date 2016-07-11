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
package org.bonitasoft.web.toolkit.client.ui.page.itemListingPage;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Séverin Moussel
 * 
 */
public class ItemListingTableSorts {

    private final List<ItemListingTableSort> sorts = new LinkedList<ItemListingTableSort>();

    public ItemListingTableSorts addSort(final String label, final String tooltip, final String columnName) {
        return this.addSort(label, tooltip, columnName, false);
    }

    public ItemListingTableSorts addSort(final String label, final String tooltip, final String columnName, final boolean ascendant) {
        this.sorts.add(new ItemListingTableSort(label, tooltip, columnName, ascendant));
        return this;
    }

    /**
     * @return the sorts
     */
    public List<ItemListingTableSort> getSorts() {
        return this.sorts;
    }

}
