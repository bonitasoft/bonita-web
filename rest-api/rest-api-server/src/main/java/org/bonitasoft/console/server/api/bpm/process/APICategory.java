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
package org.bonitasoft.console.server.api.bpm.process;

import java.util.List;
import java.util.Map;

import org.bonitasoft.console.server.api.ConsoleAPI;
import org.bonitasoft.console.server.datastore.bpm.process.CategoryDatastore;
import org.bonitasoft.engine.bpm.category.CategoryCriterion;
import org.bonitasoft.web.rest.api.model.bpm.process.CategoryDefinition;
import org.bonitasoft.web.rest.api.model.bpm.process.CategoryItem;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.server.api.APIHasAdd;
import org.bonitasoft.web.toolkit.server.api.APIHasDelete;
import org.bonitasoft.web.toolkit.server.api.APIHasGet;
import org.bonitasoft.web.toolkit.server.api.APIHasSearch;
import org.bonitasoft.web.toolkit.server.api.APIHasUpdate;
import org.bonitasoft.web.toolkit.server.search.ItemSearchResult;

/**
 * @author Nicolas Tith
 * 
 */
public class APICategory extends ConsoleAPI<CategoryItem> implements APIHasAdd<CategoryItem>, APIHasDelete, APIHasGet<CategoryItem>,
        APIHasSearch<CategoryItem>, APIHasUpdate<CategoryItem> {

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CONFIGURE
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected ItemDefinition defineItemDefinition() {
        return Definitions.get(CategoryDefinition.TOKEN);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // C.R.U.D.S
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public CategoryItem add(final CategoryItem item) {
        return new CategoryDatastore(getEngineSession()).add(item);
    }

    @Override
    public CategoryItem update(final APIID id, final Map<String, String> attributes) {
        return new CategoryDatastore(getEngineSession()).update(id, attributes);
    }

    @Override
    public CategoryItem get(final APIID id) {
        return new CategoryDatastore(getEngineSession()).get(id);
    }

    @Override
    public String defineDefaultSearchOrder() {
        return CategoryCriterion.NAME_ASC.toString();
    }

    @Override
    public ItemSearchResult<CategoryItem> search(final int page, final int resultsByPage, final String search, final String orders,
            final Map<String, String> filters) {
        final ItemSearchResult<CategoryItem> results = new CategoryDatastore(getEngineSession()).search(page, resultsByPage, search, orders, filters);
        return results;
    }

    @Override
    public void delete(final List<APIID> ids) {
        new CategoryDatastore(getEngineSession()).delete(ids);
    }

    @Override
    protected void fillDeploys(final CategoryItem item, final List<String> deploys) {
    }

    @Override
    protected void fillCounters(final CategoryItem item, final List<String> counters) {
    }
}
