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
package org.bonitasoft.web.rest.server.datastore.filter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Vincent Elcrin
 * 
 */
public class Filters {

    private List<Filter<?>> filters = new ArrayList<Filter<?>>();

    public Filters(Map<String, String> filters, FilterCreator filterCreator) {
        if (filters != null) {
            addFilters(filters, filterCreator);
        }
    }

    private void addFilters(Map<String, String> filters, FilterCreator filterCreator) {
        Iterator<Entry<String, String>> it = filters.entrySet().iterator();
        while (it.hasNext()) {
            addEntry(it.next(), filterCreator);
        }
    }

    private void addEntry(Entry<String, String> entry, FilterCreator filterCreator) {
        filters.add(createFilter(filterCreator, entry));
    }

    private Filter<?> createFilter(FilterCreator filterCreator, Entry<String, String> entry) {
        return filterCreator.create(entry.getKey(), entry.getValue());
    }

    public List<Filter<?>> asList() {
        return filters;
    }

}
