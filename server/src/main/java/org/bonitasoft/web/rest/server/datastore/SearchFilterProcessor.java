/**
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation
 * version 2.1 of the License.
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth
 * Floor, Boston, MA 02110-1301, USA.
 **/
package org.bonitasoft.web.rest.server.datastore;

import java.util.Map;

import org.bonitasoft.engine.search.SearchOptionsBuilder;

/**
 * @author Emmanuel Duchastenier
 */
public class SearchFilterProcessor {

    public void addFilter(final Map<String, String> filters, final SearchOptionsBuilder searchOptionsBuilder, final String filterName,
            final String engineAttributeName) {
        if (filters != null && filters.containsKey(filterName)) {
            final String filterValue = filters.get(filterName);
            if (filterValue != null) {
                if (filterValue.startsWith(">")) {
                    searchOptionsBuilder.greaterThan(engineAttributeName, filterValue.substring(1).trim());
                } else if (filterValue.startsWith("<")) {
                    searchOptionsBuilder.lessThan(engineAttributeName, filterValue.substring(1).trim());
                } else {
                    searchOptionsBuilder.filter(engineAttributeName, Long.valueOf(filterValue));
                }
            }
        }
    }

}
