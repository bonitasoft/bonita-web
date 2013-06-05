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
package org.bonitasoft.web.toolkit.client.ui.page;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bonitasoft.web.toolkit.client.ui.Page;
import org.bonitasoft.web.toolkit.client.ui.component.Link;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTable;
import org.bonitasoft.web.toolkit.client.ui.component.table.filters.TableFilter;

/**
 * @author Séverin Moussel
 * 
 */
public abstract class DatatablePage extends Page {

    private final LinkedList<ItemTable> itemTables = new LinkedList<ItemTable>();

    @Override
    public void defineTitle() {
    }

    protected abstract LinkedList<TableFilter> definePrimaryFilters();

    protected LinkedList<TableFilter> defineSecondaryFilters() {
        return null;
    }

    protected abstract ItemTable defineResourceFilter();

    protected abstract LinkedList<Link> defineTablesActions();

    protected final List<String> getSelectedIds() {
        final List<String> results = new ArrayList<String>();

        for (final ItemTable table : this.itemTables) {
            results.addAll(table.getSelectedIds());
        }

        return results;

    }

    protected void buildMainPanel() {

    }

}
