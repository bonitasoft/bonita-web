/**
 * Copyright (C) 2013 BonitaSoft S.A.
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
package org.bonitasoft.web.rest.server.api.bonitaPage;

import java.util.List;
import java.util.Map;

import org.bonitasoft.web.rest.model.portal.profile.BonitaPageDefinition;
import org.bonitasoft.web.rest.model.portal.profile.BonitaPageItem;
import org.bonitasoft.web.rest.server.api.ConsoleAPI;
import org.bonitasoft.web.rest.server.datastore.profile.BonitaPageDatastore;
import org.bonitasoft.web.rest.server.framework.search.ItemSearchResult;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

/**
 * @author Fabio Lombardi
 *
 */
public class APIBonitaPage extends ConsoleAPI<BonitaPageItem> {

    @Override
    protected ItemDefinition<BonitaPageItem> defineItemDefinition() {
        return BonitaPageDefinition.get();
    }

    @Override
    public String defineDefaultSearchOrder() {
        return BonitaPageItem.ATTRIBUTE_NAME;
    }

    @Override
    public BonitaPageItem add(final BonitaPageItem item) {
        // Add
        return null;

    }

    @Override
    public BonitaPageItem update(final APIID id, final Map<String, String> item) {
        // Update
        // return new UserDatastore(getEngineSession()).update(id, item);
        return null;
    }

    @Override
    public BonitaPageItem get(final APIID id) {
        return new BonitaPageDatastore().get(id);
    }

    @Override
    public ItemSearchResult<BonitaPageItem> search(final int page, final int resultsByPage, final String search, final String orders,
            final Map<String, String> filters) {
        return new BonitaPageDatastore().search(page, resultsByPage, search, filters, orders);
    }

    @Override
    public void delete(final List<APIID> ids) {
        // new UserDatastore(getEngineSession()).delete(ids);
    }

    @Override
    protected void fillDeploys(final BonitaPageItem item, final List<String> deploys) {
    }

    @Override
    protected void fillCounters(final BonitaPageItem item, final List<String> counters) {
    }
}
