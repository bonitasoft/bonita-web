/**
 * Copyright (C) 2014 BonitaSoft S.A.
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
package org.bonitasoft.web.rest.server.api.organization;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bonitasoft.engine.identity.CustomUserInfoDefinition;
import org.bonitasoft.engine.identity.CustomUserInfoDefinitionCreator;
import org.bonitasoft.web.rest.model.identity.CustomUserInfoDefinitionDefinition;
import org.bonitasoft.web.rest.model.identity.CustomUserInfoDefinitionItem;
import org.bonitasoft.web.rest.server.api.ConsoleAPI;
import org.bonitasoft.web.rest.server.engineclient.CustomUserInfoEngineClient;
import org.bonitasoft.web.rest.server.engineclient.CustomUserInfoEngineClientCreator;
import org.bonitasoft.web.rest.server.framework.api.APIHasAdd;
import org.bonitasoft.web.rest.server.framework.api.APIHasDelete;
import org.bonitasoft.web.rest.server.framework.api.APIHasSearch;
import org.bonitasoft.web.rest.server.framework.search.ItemSearchResult;
import org.bonitasoft.web.toolkit.client.common.i18n._;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

import static org.bonitasoft.web.rest.server.api.APIPreconditions.check;

/**
 * @author Vincent Elcrin
 */
public class APICustomUserInfoDefinition extends ConsoleAPI<CustomUserInfoDefinitionItem> implements
        APIHasAdd<CustomUserInfoDefinitionItem>,
        APIHasSearch<CustomUserInfoDefinitionItem>,
        APIHasDelete {

    public static final String FIX_ORDER = "Fix order";

    private final CustomUserInfoConverter converter = new CustomUserInfoConverter();

    private CustomUserInfoEngineClientCreator engineClientCreator;

    public APICustomUserInfoDefinition(CustomUserInfoEngineClientCreator engineClientCreator) {
        this.engineClientCreator = engineClientCreator;
    }

    public CustomUserInfoDefinitionItem add(CustomUserInfoDefinitionItem definition) {
        return converter.convert(engineClientCreator.create(getEngineSession())
                .createDefinition(new CustomUserInfoDefinitionCreator(
                        definition.getName(),
                        definition.getDescription())));
    }

    public void delete(final List<APIID> ids) {
        for (APIID id : ids) {
            engineClientCreator.create(getEngineSession()).deleteDefinition(id.toLong());
        }
    }

    public ItemSearchResult<CustomUserInfoDefinitionItem> search(
            final int page,
            final int resultsByPage,
            final String search,
            final String orders,
            final Map<String, String> filters) {

        check(search == null, new _("Search terms are not supported by this API"));
        check(filters == null || filters.isEmpty(), new _("Filters are not supported by this API"));
        check(orders.equals(FIX_ORDER), new _("Sorting is not supported by this API"));

        CustomUserInfoEngineClient client = engineClientCreator.create(getEngineSession());
        List<CustomUserInfoDefinitionItem> result = new ArrayList<CustomUserInfoDefinitionItem>();
        for (CustomUserInfoDefinition definition : client.listDefinitions(page * resultsByPage, resultsByPage)) {
            result.add(converter.convert(definition));
        }
        return new ItemSearchResult<CustomUserInfoDefinitionItem>(page, resultsByPage, client.countDefinitions(), result);
    }

    @Override
    protected ItemDefinition<CustomUserInfoDefinitionItem> defineItemDefinition() {
        return CustomUserInfoDefinitionDefinition.get();
    }

    public String defineDefaultSearchOrder() {
        return FIX_ORDER;
    }
}
