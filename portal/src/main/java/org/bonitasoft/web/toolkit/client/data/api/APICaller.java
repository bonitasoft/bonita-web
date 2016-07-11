/**
 * Copyright (C) 2011 BonitaSoft S.A.
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
package org.bonitasoft.web.toolkit.client.data.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bonitasoft.web.toolkit.client.common.UrlBuilder;
import org.bonitasoft.web.toolkit.client.common.json.JSonItemWriter;
import org.bonitasoft.web.toolkit.client.common.json.JSonSerializer;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.api.request.APIGetRequest;
import org.bonitasoft.web.toolkit.client.data.api.request.APISearchRequest;
import org.bonitasoft.web.toolkit.client.data.api.request.HttpRequest;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.Item;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ModifierEngine;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ValidatorEngine;
import org.bonitasoft.web.toolkit.client.ui.component.form.AbstractForm;

import com.google.gwt.http.client.RequestBuilder;

/**
 * This class allow to easily make API calls
 * 
 * @author Séverin Moussel
 */
public class APICaller<T extends IItem> extends HttpRequest {

    /**
     * The URL for single resource related calls
     * <p>
     * <b>Example :</b>/REST/API/user
     */
    private String url = null;

    private ItemDefinition itemDefinition = null;

    /**
     * @param url
     *            The URL of the API<br>
     *            <b>Example :</b>/API/organization/user
     */

    public APICaller(final String url) {
        this.url = url;
    }

    public APICaller(final ItemDefinition itemDefinition) {
        this(itemDefinition.getAPIUrl());
        this.itemDefinition = itemDefinition;
    }

    // ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // SEARCH
    // ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Construct an APISearchRequest ready to use.<br>
     * To launch the request, you will have to call APISearchRequest.run(ApiCallback)
     * 
     * @param page
     *            The page number to display. The first page is page 0.
     * @param resultsPerPage
     *            Number of results to retrieve.
     * 
     * @return This method returns
     */
    public APISearchRequest getSearchRequest(final int page, final int resultsPerPage) {
        return new APISearchRequest(this.itemDefinition)
                .setPage(page)
                .setResultsPerPage(resultsPerPage);
    }

    /**
     * Transform an array of orders into a single string with orders separated by commas.
     * 
     * @param orders
     *            The orders to transform
     * @return This method returns the orders as an SQL compatible order string
     */
    public static final String orderArrayToString(final List<String> orders) {
        final StringBuilder sb = new StringBuilder();

        for (final String order : orders) {
            sb.append(order).append(",");
        }

        return sb.toString().replaceAll(",$", "");
    }

    /**
     * Search for a list of items
     * 
     * @param page
     *            The page number to display. The first page is page 0.
     * @param resultsPerPage
     *            Number of results to retrieve.
     */
    public void search(final int page, final int resultsPerPage, final APICallback callback) {
        search(page, resultsPerPage, null, null, null, null, null, callback);
    }

    /**
     * Search for a list of items
     * 
     * @param page
     *            The page number to display. The first page is page 0.
     * @param resultsPerPage
     *            Number of results to retrieve.
     * @param order
     *            The sorting order as an SQL compatible String
     */
    public void search(final int page, final int resultsPerPage, final String order, final APICallback callback) {
        search(page, resultsPerPage, order, "", null, null, null, callback);
    }

    /**
     * Search for a list of items
     * 
     * @param page
     *            The page number to display. The first page is page 0.
     * @param resultsPerPage
     *            Number of results to retrieve.
     * @param order
     *            The sorting order as an SQL compatible String
     * @param search
     *            The search query
     */
    public void search(final int page, final int resultsPerPage, final String order, final String search, final APICallback callback) {
        search(page, resultsPerPage, order, search, null, null, null, callback);
    }

    /**
     * Search for a list of items
     * 
     * @param page
     *            The page number to display. The first page is page 0.
     * @param resultsPerPage
     *            Number of results to retrieve.
     * @param order
     *            The sorting order as an SQL compatible String
     * @param search
     *            The search query
     * @param filters
     *            A set of filters formated as a HashMap<attributeName, value).<br>
     *            The value can be a string or an array of Strings for multiple values filters.
     */
    public void search(final int page, final int resultsPerPage, final String order, final String search, final Map<String, String> filters,
            final APICallback callback)
    {
        search(page, resultsPerPage, order, search, filters, null, null, callback);
    }

    /**
     * Search for a list of items
     * 
     * @param page
     *            The page number to display. The first page is page 0.
     * @param resultsPerPage
     *            Number of results to retrieve.
     * @param order
     *            The sorting order as an SQL compatible String
     * @param search
     *            The search query
     * @param filters
     *            A set of filters formated as a HashMap<attributeName, value).<br>
     *            The value can be a string or an array of Strings for multiple values filters.
     * @param deploys
     *            A set of deploys.<br>
     *            A deploys is an attribute name. The attribute must be the id of another resource.<br>
     *            Deploying it will return the full object of the resource instead of the id.
     */
    public void search(final int page, final int resultsPerPage, final String order, final String search, final Map<String, String> filters,
            final List<String> deploys, final APICallback callback)
    {

        search(page, resultsPerPage, order, search, filters, deploys, null, callback);
    }

    /**
     * Search for a list of items
     * 
     * @param page
     *            The page number to display. The first page is page 0.
     * @param resultsPerPage
     *            Number of results to retrieve.
     * @param order
     *            The sorting order as an SQL compatible String
     * @param search
     *            The search query
     * @param filters
     *            A set of filters formated as a HashMap<attributeName, value).<br>
     *            The value can be a string or an array of Strings for multiple values filters.
     * @param deploys
     *            A set of deploys.<br>
     *            A deploys is an attribute name. The attribute must be the id of another resource.<br>
     *            Deploying it will return the full object of the resource instead of the id.
     * @param counters
     *            A set of counters.<br>
     *            A counter is a static from the resource Item (FILTER_xxx), it allows to add fake attributes with the result of a particular count on linked
     *            resources.
     */
    public void search(final int page, final int resultsPerPage, final String order, final String search, final Map<String, String> filters,
            final List<String> deploys, final List<String> counters, final APICallback callback)
    {
        getSearchRequest(page, resultsPerPage)
                .setSearch(search)
                .setOrder(order)
                .setFilters(filters)
                .setDeploys(deploys)
                .setCounters(counters)

                .run(callback);
    }

    // ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // GET
    // ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Get a single item using its ID
     */
    public void get(final String id, final APICallback callback) {
        this.get(id, null, null, callback);
    }

    /**
     * Get a single item using its ID
     */
    public void get(final APIID id, final APICallback callback) {
        this.get(id, null, null, callback);
    }

    /**
     * Get a single item using its ID
     */
    public void get(final String id, final List<String> deploys, final APICallback callback) {
        this.get(id, deploys, null, callback);
    }

    /**
     * Get a single item using its ID
     */
    public void get(final APIID id, final List<String> deploys, final APICallback callback) {
        this.get(id, deploys, null, callback);
    }

    /**
     * Get a single item using its ID
     */
    public void get(final String id, final List<String> deploys, final List<String> counters, final APICallback callback) {
        this.get(APIID.makeAPIID(id), deploys, counters, callback);
    }

    /**
     * Get a single item using its ID
     */
    public void get(final APIID id, final List<String> deploys, final List<String> counters, final APICallback callback) {
        new APIGetRequest(this.itemDefinition)
                .setId(id)
                .setDeploys(deploys)
                .setCounters(counters)

                .run(callback);
    }

    // ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // UPDATE
    // ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Update an item using its ID by submitting a form
     */
    public void update(final String id, final AbstractForm form, final APICallback callback) {
        this.update(APIID.makeAPIID(id), form, callback);
    }

    /**
     * Update an item using its ID by submitting a HashMap
     */
    public void update(final String id, final HashMap<String, String> attributesToUpdate, final APICallback callback) {
        this.update(APIID.makeAPIID(id), attributesToUpdate, callback);
    }

    /**
     * Update an item using its ID by submitting a form
     */
    public void update(final IItem item, final APICallback callback) {
        ValidatorEngine.validate(item, true);

        this.send(RequestBuilder.PUT, this.url + "/" + item.getId(), JSonItemWriter.itemToJSON(item), HttpRequest.CONTENT_TYPE_JSON, callback);
    }

    /**
     * Update an item using its ID by submitting a form
     */
    public void update(final APIID id, final AbstractForm form, final APICallback callback) {
        this.update(id, form.getValues().getValues(), callback);
    }

    /**
     * Update an item using its ID by submitting a HashMap
     */
    public void update(final APIID apiId, final Map<String, String> attributesToUpdate, final APICallback callback) {
        // Apply validators
        if (this.itemDefinition != null) {
            ModifierEngine.modify(attributesToUpdate, this.itemDefinition.getInputModifiers());
            ValidatorEngine.validate(attributesToUpdate, this.itemDefinition.getValidators());
        }

        this.send(RequestBuilder.PUT, this.url + "/" + apiId.toString(), JSonItemWriter.mapToJSON(attributesToUpdate),
                HttpRequest.CONTENT_TYPE_JSON,
                callback);
    }

    // ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ADD
    // ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Add a new item by submitting a form
     * 
     * @param form
     *            The form to submit
     */
    public void add(final AbstractForm form, final APICallback callback) {
        final Map<String, String> values = form.getValues().getValues();

        if (this.itemDefinition != null) {
            ModifierEngine.modify(values, this.itemDefinition.getInputModifiers());
            ValidatorEngine.validate(values, this.itemDefinition.getValidators());
        }

        this.send(RequestBuilder.POST, this.url + "/", JSonSerializer.serialize(values), HttpRequest.CONTENT_TYPE_JSON, callback);
    }

    /**
     * Add a new item by submitting a form
     * 
     * @param item
     *            The item to submit
     */
    public void add(final IItem item, final APICallback callback) {
        ValidatorEngine.validate(item, true);

        this.send(RequestBuilder.POST, this.url + "/", new JSonItemWriter<Item>(item).toString(), HttpRequest.CONTENT_TYPE_JSON, callback);
    }

    // ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // DELETE
    // ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Delete an item by its ID
     */
    public void delete(final String id, final APICallback callback) {
        this.delete(APIID.makeAPIID(id), callback);
    }

    /**
     * Delete an item by its ID
     */
    public void delete(final APIID apiId, final APICallback callback) {
        this.send(RequestBuilder.DELETE, this.url + "/" + apiId.toString(), callback);
    }

    // ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // DELETE MANY
    // ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Delete multiple items by their IDs
     */
    public void delete(final List<String> id, final APICallback callback) {
        final UrlBuilder url = new UrlBuilder(this.url);

        String ids = "[";
        for (int i = 0; i < id.size(); i++) {
            ids += (i > 0 ? "," : "") + "\"" + id.get(i) + "\"";
        }
        ids += "]";

        this.send(RequestBuilder.DELETE, url.toString(), ids, CONTENT_TYPE_JSON, callback);
    }

}
