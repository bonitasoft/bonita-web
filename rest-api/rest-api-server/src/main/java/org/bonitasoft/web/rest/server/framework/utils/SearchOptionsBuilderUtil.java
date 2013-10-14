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
package org.bonitasoft.web.rest.server.framework.utils;

import org.bonitasoft.engine.search.Order;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.web.rest.server.datastore.utils.SearchOptionsCreator;

/**
 * @author Haojie Yuan
 * 
 */
public class SearchOptionsBuilderUtil {

    /**
     * build SearchOptionsBuilder
     * 
     * @deprecated use {@link SearchOptionsCreator}
     */
	@Deprecated
    public static SearchOptionsBuilder buildSearchOptions(final int pageIndex, final int numberOfResults, final String sort, final String search) {
        final SearchOptionsBuilder builder = new SearchOptionsBuilder(computeIndex(pageIndex, numberOfResults), numberOfResults);
        if (sort != null) {
            final String[] order = sort.split(" ");
            if (order.length == 2) {
                builder.sort(order[0], Order.valueOf(order[1].toUpperCase()));
            }
        }
        if (search != null && !search.isEmpty()) {
            builder.searchTerm(search);
        }
        return builder;
    }

    public static int computeIndex(int page, int resultsByPage) {
        return page * resultsByPage;
    }
}
