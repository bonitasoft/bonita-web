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
package org.bonitasoft.web.toolkit.client.data.api.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

public abstract class APIRequest {

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // GET
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static APIGetRequest get(final String id, final ItemDefinition itemDefinition, final APICallback callback) {
        return get(APIID.makeAPIID(id), itemDefinition, callback);
    }

    public static APIGetRequest get(final APIID id, final ItemDefinition itemDefinition, final APICallback callback) {
        final APIGetRequest request = new APIGetRequest(itemDefinition);
        request.setId(id);
        request.setCallback(callback);
        return request;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // SEARCH
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static APISearchRequest search(final Integer page, final Integer resultsPerPage, final ItemDefinition itemDefinition, final APICallback callback) {

        return search(page, resultsPerPage, null, null, null, itemDefinition, callback);
    }

    public static APISearchRequest search(final Integer page, final Integer resultsPerPage, final String order, final ItemDefinition itemDefinition,
            final APICallback callback) {

        return search(page, resultsPerPage, order, null, null, itemDefinition, callback);
    }

    public static APISearchRequest search(final Integer page, final Integer resultsPerPage, final String order, final String search,
            final ItemDefinition itemDefinition, final APICallback callback) {

        return search(page, resultsPerPage, order, search, null, itemDefinition, callback);
    }

    public static APISearchRequest search(final Integer page, final Integer resultsPerPage, final String order, final String search,
            final HashMap<String, String> filters, final ItemDefinition itemDefinition, final APICallback callback) {

        final APISearchRequest request = new APISearchRequest(itemDefinition);
        request.setPage(page);
        request.setResultsPerPage(resultsPerPage);
        request.setOrder(order);
        request.setSearch(search);
        request.setFilters(filters);
        request.setCallback(callback);

        return request;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ADD
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static APIAddRequest add(final IItem item, final ItemDefinition itemDefinition, final APICallback callback) {
        final APIAddRequest request = new APIAddRequest(itemDefinition);
        request.setItem(item);
        request.setCallback(callback);
        return request;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // UPDATE
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static APIUpdateRequest update(final String id, final IItem item, final ItemDefinition itemDefinition, final APICallback callback) {
        return update(APIID.makeAPIID(id), item, itemDefinition, callback);
    }

    public static APIUpdateRequest update(final APIID id, final IItem item, final ItemDefinition itemDefinition, final APICallback callback) {
        final APIUpdateRequest request = new APIUpdateRequest(itemDefinition);

        request.setId(id);
        request.setItem(item);
        request.setCallback(callback);

        return request;
    }

    public static APIUpdateRequest update(final String id, final Map<String, String> item, final ItemDefinition itemDefinition, final APICallback callback) {
        return update(APIID.makeAPIID(id), item, itemDefinition, callback);
    }

    public static APIUpdateRequest update(final APIID id, final Map<String, String> item, final ItemDefinition itemDefinition, final APICallback callback) {
        final APIUpdateRequest request = new APIUpdateRequest(itemDefinition);

        request.setId(id);
        request.setItem(item);
        request.setCallback(callback);

        return request;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // DELETE
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static APIDeleteRequest delete(final String id, final ItemDefinition itemDefinition, final APICallback callback) {
        return delete(APIID.makeAPIID(id), itemDefinition, callback);
    }

    public static APIDeleteRequest delete(final APIID id, final ItemDefinition itemDefinition, final APICallback callback) {
        final APIDeleteRequest request = new APIDeleteRequest(itemDefinition);
        request.setId(id);
        request.setCallback(callback);

        return request;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // DELETE MANY
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static APIDeleteRequest delete(final ArrayList<APIID> ids, final ItemDefinition itemDefinition, final APICallback callback) {
        final APIDeleteRequest request = new APIDeleteRequest(itemDefinition);
        request.setIds(ids);
        request.setCallback(callback);

        return request;
    }
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // TODO Add updateMany
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
