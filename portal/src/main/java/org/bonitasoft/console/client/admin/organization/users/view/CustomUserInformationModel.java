/**
 * Copyright (C) 2014 Bonitasoft S.A.
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
package org.bonitasoft.console.client.admin.organization.users.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bonitasoft.console.client.mvp.Paginate;
import org.bonitasoft.console.client.mvp.model.RequestFactory;
import org.bonitasoft.web.rest.model.identity.CustomUserInfoDefinition;
import org.bonitasoft.web.rest.model.identity.CustomUserInfoItem;
import org.bonitasoft.web.rest.model.identity.CustomUserInfoValueDefinition;
import org.bonitasoft.web.toolkit.client.common.json.JSonItemReader;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.api.request.APISearchRequest;
import org.bonitasoft.web.toolkit.client.data.api.request.APIUpdateRequest;
import org.bonitasoft.web.toolkit.client.data.api.request.ApiSearchResultPager;
import org.bonitasoft.web.toolkit.client.data.item.IItem;

/**
 * @author Vincent Elcrin
 */
public class CustomUserInformationModel {

    private RequestFactory requestFactory;

    private String userId;

    private Map<APIID, IItem> changes = new HashMap<APIID, IItem>();

    public static abstract class Callback extends APICallback {

        @Override
        public void onSuccess(int httpStatusCode, String response, Map<String, String> headers) {
            ApiSearchResultPager pagination = ApiSearchResultPager.parse(headers.get("Content-Range"));
            onSuccess(
                    JSonItemReader.parseItems(response, CustomUserInfoDefinition.get()),
                    pagination.getCurrentPage(),
                    pagination.getNbResultsByPage(),
                    pagination.getNbTotalResults());
        }

        abstract void onSuccess(List<CustomUserInfoItem> information, int page, int pageSize, int total);

    }

    public CustomUserInformationModel(RequestFactory requestFactory, String userId) {
        this.requestFactory = requestFactory;
        this.userId = userId;
    }

    public void update(CustomUserInfoItem item, String value) {
        CustomUserInfoItem change = new CustomUserInfoItem();
        change.setValue(value);
        changes.put(item.getId(), change);
    }

    public void flushChanges() {
        for (APIID id : changes.keySet()) {
            APIUpdateRequest request = requestFactory.createUpdate(CustomUserInfoValueDefinition.get());
            request.setId(id);
            request.setItem(changes.get(id));
            request.run();
        }
        changes.clear();
    }

    public Paginate search(int page, int size, final Callback callback) {
        Paginate paginate = new Paginate() {

            @Override
            public void loadPage(int page, int size) {
                APISearchRequest request = requestFactory.createSearch(CustomUserInfoDefinition.get());
                request.setPage(page);
                request.setResultsPerPage(size);
                request.addFilter(CustomUserInfoItem.ATTRIBUTE_USER_ID, userId);
                request.run(callback);
            }
        };
        paginate.loadPage(page, size);
        return paginate;

    }
}
