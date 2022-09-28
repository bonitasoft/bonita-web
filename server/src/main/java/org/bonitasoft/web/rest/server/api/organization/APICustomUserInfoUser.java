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

import static org.bonitasoft.web.rest.model.identity.CustomUserInfoItem.FILTER_USER_ID;
import static org.bonitasoft.web.rest.server.api.APIPreconditions.check;
import static org.bonitasoft.web.rest.server.api.APIPreconditions.containsOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bonitasoft.engine.identity.CustomUserInfo;
import org.bonitasoft.web.rest.model.identity.CustomUserInfoDefinition;
import org.bonitasoft.web.rest.model.identity.CustomUserInfoItem;
import org.bonitasoft.web.rest.server.api.ConsoleAPI;
import org.bonitasoft.web.rest.server.engineclient.CustomUserInfoEngineClient;
import org.bonitasoft.web.rest.server.engineclient.CustomUserInfoEngineClientCreator;
import org.bonitasoft.web.rest.server.framework.api.APIHasSearch;
import org.bonitasoft.web.rest.server.framework.search.ItemSearchResult;
import org.bonitasoft.web.toolkit.client.common.i18n.T_;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

/**
 * @author Vincent Elcrin
 */
public class APICustomUserInfoUser extends ConsoleAPI<CustomUserInfoItem> implements APIHasSearch<CustomUserInfoItem> {

    public static final String FIX_ORDER = "Fix order";

    private final CustomUserInfoEngineClientCreator engineClientCreator;

    private final CustomUserInfoConverter converter = new CustomUserInfoConverter();

    public APICustomUserInfoUser(CustomUserInfoEngineClientCreator engineClientCreator) {
        this.engineClientCreator = engineClientCreator;
    }

    @Override
    public ItemSearchResult<CustomUserInfoItem> search(int page, int resultsByPage, String search, String orders, Map<String, String> filters) {
        check(containsOnly(FILTER_USER_ID, filters), new T_("The only mandatory filter is %name%", new Arg("name", FILTER_USER_ID)));
        check(orders.equals(FIX_ORDER), new T_("Sorting is not supported by this API"));
        check(search == null, new T_("Search terms are not supported by this API"));

        CustomUserInfoEngineClient client = engineClientCreator.create(getEngineSession());
        List<CustomUserInfo> items = client.listCustomInformation(
                Long.parseLong(filters.get(FILTER_USER_ID)),
                page * resultsByPage,
                resultsByPage);

        List<CustomUserInfoItem> information = new ArrayList<>();
        for (CustomUserInfo item : items) {
            information.add(converter.convert(item));
        }
        return new ItemSearchResult<>(page, information.size(), client.countDefinitions(), information);
    }

    @Override
    protected ItemDefinition<CustomUserInfoItem> defineItemDefinition() {
        return CustomUserInfoDefinition.get();
    }

    @Override
    public String defineDefaultSearchOrder() {
        return FIX_ORDER;
    }
}
