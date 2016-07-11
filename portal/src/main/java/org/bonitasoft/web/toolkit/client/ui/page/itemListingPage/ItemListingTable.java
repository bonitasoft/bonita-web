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

import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.component.Section;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTable;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemQuickDetailsPage;

/**
 * @author Séverin Moussel
 * 
 */
public final class ItemListingTable extends Section {

    private final ItemTable itemTable;

    private ItemQuickDetailsPage<? extends IItem> quickDetailsPage = null;

    private final Map<String, String> defaultHiddenFilters = new HashMap<String, String>();

    public ItemListingTable(final JsId jsid, final String title, final ItemTable itemTable, final ItemQuickDetailsPage<? extends IItem> quickDetailsPage) {
        super(jsid, title);
        this.itemTable = itemTable;
        this.quickDetailsPage = quickDetailsPage;

        final Map<String, String> filters = itemTable.getFilters();
        if (filters != null) {
            this.defaultHiddenFilters.putAll(filters);
        }

        addBody(itemTable);
        addClass("table_section");
    }

    /**
     * @return the itemTable
     */
    public ItemTable getItemTable() {
        return this.itemTable;
    }

    /**
     * @return the defaultHiddenFilters
     */
    public Map<String, String> getDefaultHiddenFilters() {
        return this.defaultHiddenFilters;
    }

    /**
     * @return the quickDetailsPage
     */
    protected final ItemQuickDetailsPage<? extends IItem> getQuickDetailsPage() {
        return this.quickDetailsPage;
    }

}
