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
package org.bonitasoft.web.toolkit.client.data.api.request;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bonitasoft.web.toolkit.client.common.UrlBuilder;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

import com.google.gwt.http.client.RequestBuilder;

/**
 * @author Séverin Moussel
 * 
 */
public class APISearchRequest extends AbstractAPIReadRequest {

    private int page = 0;

    private int resultsPerPage = 10;

    private String order = null;

    private String search = null;

    private final Map<String, String> filters = new HashMap<String, String>();

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTOR
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public APISearchRequest(final ItemDefinition itemDefinition) {
        super(itemDefinition);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // GETTERS AND SETTERS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @param page
     *            the page to set
     */
    public APISearchRequest setPage(final int page) {
        this.page = page;
        return this;
    }

    /**
     * @param resultsPerPage
     *            the resultsPerPage to set
     */
    public APISearchRequest setResultsPerPage(final int resultsPerPage) {
        this.resultsPerPage = resultsPerPage;
        return this;
    }

    /**
     * @param order
     *            the order to set
     */
    public APISearchRequest setOrder(final String order) {
        this.order = order;
        return this;
    }

    /**
     * @param search
     *            the search to set
     */
    public APISearchRequest setSearch(final String search) {
        this.search = search;
        return this;
    }

    /**
     * @return the page
     */
    protected final int getPage() {
        return this.page;
    }

    /**
     * @return the resultsPerPage
     */
    protected final int getResultsPerPage() {
        return this.resultsPerPage;
    }

    /**
     * @return the order
     */
    protected final String getOrder() {
        return this.order;
    }

    /**
     * @return the search
     */
    protected final String getSearch() {
        return this.search;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // FILTERS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @return the filters
     */
    protected final Map<String, String> getFilters() {
        return this.filters;
    }

    /**
     * Set the filters to send. This method will remove previously added filters.
     * 
     * @param filters
     *            The filters to set as a map <name, value> where name can be an ATTRIBUTE_ or a FILTER_ of the Item.
     * @return This method returns "this" to allow cascading calls.
     */
    public APISearchRequest setFilters(final Map<String, String> filters) {
        this.filters.clear();
        return addFilters(filters);
    }

    /**
     * Add filters to send.
     * 
     * @param filters
     *            The filters to add as a map <name, value> where name can be an ATTRIBUTE_ or a FILTER_ of the Item.
     * @return This method returns "this" to allow cascading calls.
     */
    public APISearchRequest addFilters(final Map<String, String> filters) {
        if (filters != null) {
            this.filters.putAll(filters);
        }
        return this;
    }

    /**
     * Add a filter to send.
     * 
     * @param name
     *            The name of the filter. Can be an ATTRIBUTE_ or a FILTER_ of the Item
     * @param value
     *            The value of the filter.
     * 
     * @return This method returns "this" to allow cascading calls.
     */
    public APISearchRequest addFilter(final String name, final String value) {
        this.filters.put(name, value);
        return this;
    }

    /**
     * Remove an already defined filter
     * 
     * @param filter
     *            The filter to remove.
     * @return This method returns "this" to allow cascading calls.
     */
    public AbstractAPIReadRequest removeFilter(final String filter) {
        this.filters.remove(filter);
        return this;
    }

    /**
     * Remove multiple filters.
     * 
     * @param filters
     *            The list of filters to remove.
     * @return This method returns "this" to allow cascading calls.
     */
    public AbstractAPIReadRequest removeFilters(final String... filters) {
        removeFilters(Arrays.asList(filters));
        return this;
    }

    /**
     * Remove multiple filters.
     * 
     * @param filters
     *            The list of filters to remove.
     * @return This method returns "this" to allow cascading calls.
     */
    public AbstractAPIReadRequest removeFilters(final List<String> filters) {
        for (final String filter : filters) {
            removeFilter(filter);
        }
        return this;
    }

    /**
     * Remove all filters.
     * 
     * @return This method returns "this" to allow cascading calls.
     */
    public AbstractAPIReadRequest clearFilters() {
        this.filters.clear();
        return this;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // DEPLOYS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public APISearchRequest addDeploy(final String deploy) {
        return (APISearchRequest) super.addDeploy(deploy);
    }

    @Override
    public APISearchRequest setDeploys(final String... deploys) {
        return (APISearchRequest) super.setDeploys(deploys);
    }

    @Override
    public APISearchRequest setDeploys(final List<String> deploys) {
        return (APISearchRequest) super.setDeploys(deploys);
    }

    @Override
    public APISearchRequest addDeploys(final String... deploys) {
        return (APISearchRequest) super.addDeploys(deploys);
    }

    @Override
    public APISearchRequest addDeploys(final List<String> deploys) {
        return (APISearchRequest) super.addDeploys(deploys);
    }

    @Override
    public APISearchRequest removeDeploy(final String deploy) {
        return (APISearchRequest) super.removeDeploy(deploy);
    }

    @Override
    public APISearchRequest removeDeploys(final String... deploys) {
        return (APISearchRequest) super.removeDeploys(deploys);
    }

    @Override
    public APISearchRequest removeDeploys(final List<String> deploys) {
        return (APISearchRequest) super.removeDeploys(deploys);
    }

    @Override
    public APISearchRequest clearDeploys() {
        return (APISearchRequest) super.clearDeploys();
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // COUNTERS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public APISearchRequest addCounter(final String counter) {
        return (APISearchRequest) super.addCounter(counter);
    }

    @Override
    public APISearchRequest setCounters(final String... counters) {
        return (APISearchRequest) super.setCounters(counters);
    }

    @Override
    public APISearchRequest setCounters(final List<String> counters) {
        return (APISearchRequest) super.setCounters(counters);
    }

    @Override
    public APISearchRequest addCounters(final String... counters) {
        return (APISearchRequest) super.addCounters(counters);
    }

    @Override
    public APISearchRequest addCounters(final List<String> counters) {
        return (APISearchRequest) super.addCounters(counters);
    }

    @Override
    public APISearchRequest removeCounter(final String counter) {
        return (APISearchRequest) super.removeCounter(counter);
    }

    @Override
    public APISearchRequest removeCounters(final String... counters) {
        return (APISearchRequest) super.removeCounters(counters);
    }

    @Override
    public APISearchRequest removeCounters(final List<String> counters) {
        return (APISearchRequest) super.removeCounters(counters);
    }

    @Override
    public APISearchRequest clearCounters() {
        return (APISearchRequest) super.clearCounters();
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // RUN
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static final String PARAMETER_FILTER = "f";

    public static final String PARAMETER_SEARCH = "s";

    public static final String PARAMETER_ORDER = "o";

    public static final String PARAMETER_LIMIT = "c";

    public static final String PARAMETER_PAGE = "p";

    @Override
    public void run() {

        final UrlBuilder url = new UrlBuilder(this.itemDefinition.getAPIUrl());
        url.addParameter(PARAMETER_PAGE, getPage());
        url.addParameter(PARAMETER_LIMIT, getResultsPerPage());
        if (getOrder() != null && getOrder().length() > 0) {
            url.addParameter(PARAMETER_ORDER, getOrder());
        }
        if (getSearch() != null && getSearch().length() > 0) {
            url.addParameter(PARAMETER_SEARCH, getSearch());
        }
        if (getFilters() != null && getFilters().size() > 0) {
            url.addParameter(PARAMETER_FILTER, getFilters());
        }
        if (getDeploys() != null && getDeploys().size() > 0) {
            url.addParameter(PARAMETER_DEPLOY, getDeploys());
        }
        if (getCounters() != null && getCounters().size() > 0) {
            url.addParameter(PARAMETER_COUNTER, getCounters());
        }

        this.request = new RequestBuilder(RequestBuilder.GET, url.toString());

        super.run();
    }

}
