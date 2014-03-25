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
package org.bonitasoft.web.rest.server.datastore.utils;

import static org.bonitasoft.web.rest.server.framework.utils.SearchOptionsBuilderUtil.computeIndex;

import org.bonitasoft.engine.search.SearchOptions;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.web.rest.server.datastore.filter.Filter;
import org.bonitasoft.web.rest.server.datastore.filter.Filters;
import org.bonitasoft.web.toolkit.client.common.util.StringUtil;

/**
 * @author Vincent Elcrin
 * 
 */
public class SearchOptionsCreator {

    private final SearchOptionsBuilder builder;


	public SearchOptionsCreator(int page, int resultsByPage, String search, Sorts sorts, Filters filters) {
        builder = new SearchOptionsBuilder(computeIndex(page, resultsByPage), resultsByPage);
        builder.searchTerm(search);
        addSorts(builder, sorts);
        addFilters(builder, filters);
    }

    private void addSorts(SearchOptionsBuilder builder, Sorts sorts) {
        for (Sort sort : sorts.asList()) {
            builder.sort(sort.getField(), sort.getOrder());
        }
    }

    private void addFilters(SearchOptionsBuilder builder, Filters filters) {
        for (Filter<?> filter : filters.asList()) {
            addFilter(builder, filter);
        }
    }

    private void addFilter(SearchOptionsBuilder builder, Filter<?> filter) {
        if (!StringUtil.isBlank(filter.getField())) {
            builder.filter(filter.getField(), filter.getValue());
        }
    }

    public SearchOptions create() {
        return builder.done();
    }

    public SearchOptionsBuilder getBuilder() {
		return builder;
	}
}
