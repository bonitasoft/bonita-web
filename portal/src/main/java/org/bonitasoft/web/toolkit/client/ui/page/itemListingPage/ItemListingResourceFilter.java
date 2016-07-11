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
package org.bonitasoft.web.toolkit.client.ui.page.itemListingPage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.api.request.APISearchRequest;

/**
 * @author Séverin Moussel
 * 
 */
public class ItemListingResourceFilter {

    private final APISearchRequest searchRequest;

    private final String labelAttributeName;

    private Map<String, String> filtersMapping;

    private final Map<String, String> filters = new HashMap<String, String>();

    protected final LinkedList<String> tablesToDisplay;

    public ItemListingResourceFilter(final APISearchRequest searchRequest, final String labelAttributeName,
            final String... tablesToDisplay) {
        super();
        this.searchRequest = searchRequest;
        this.labelAttributeName = labelAttributeName;
        this.tablesToDisplay = new LinkedList<String>(Arrays.asList(tablesToDisplay));
    }

    public ItemListingResourceFilter addFilterMapping(final String tableAttributeName, final String resourceFilterAttributeName) {
        if (this.filtersMapping == null) {
            this.filtersMapping = new HashMap<String, String>();
        }
        this.filtersMapping.put(tableAttributeName, resourceFilterAttributeName);
        return this;
    }

    public ItemListingResourceFilter addFilter(final String attributeName, final String attributeValue) {
        this.filters.put(attributeName, attributeValue);
        return this;
    }

    public ItemListingResourceFilter addFilter(final String attributeName, final APIID id) {
        this.filters.put(attributeName, id.toString());
        return this;
    }

    /**
     * @return the filters
     */
    public final Map<String, String> getFilters() {
        return this.filters;
    }

    /**
     * @return the searchRequest
     */
    public APISearchRequest getSearchRequest() {
        return this.searchRequest;
    }

    /**
     * @return the labelAttributeName
     */
    public String getLabelAttributeName() {
        return this.labelAttributeName;
    }

    /**
     * @return the filtersMapping
     */
    public Map<String, String> getFiltersMapping() {
        return this.filtersMapping;
    }

    /**
     * @return the tablesToDisplay
     */
    public LinkedList<String> getTablesToDisplay() {
        return this.tablesToDisplay;
    }

}
