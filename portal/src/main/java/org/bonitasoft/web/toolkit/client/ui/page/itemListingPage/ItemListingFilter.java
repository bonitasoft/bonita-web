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

import static org.bonitasoft.web.toolkit.client.common.util.StringUtil.isBlank;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.ui.component.Link;

/**
 * @author Séverin Moussel
 *
 */
public class ItemListingFilter {

    private Map<String, String> additionalFilters = null;

    protected String name;

    protected String label;

    protected String tooltip;

    protected LinkedList<String> tablesToDisplay;

    protected String imageUrl = null;

    protected boolean isResourceFilter = false;

    private Link link = null;

    private String additionnalInfo;

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Default constructor with String... for tables listing.
     *
     * @param name
     *            The name of the table (used for filtering)
     * @param label
     *            The label of the filter button
     * @param tooltip
     *            The tooltip to display hover the filter button
     * @param tablesToDisplay
     *            The tables to show while clicking on this filter
     */
    public ItemListingFilter(final String name, final String label, final String tooltip, final String... tablesToDisplay) {
        this(name, label, tooltip, new LinkedList<String>(Arrays.asList(tablesToDisplay)));
    }

    /**
     * Default constructor with a List<String> for tables listing.
     *
     * @param name
     *            The name of the table (used for filtering)
     * @param label
     *            The label of the filter button
     * @param tooltip
     *            The tooltip to display hover the filter button
     * @param tablesToDisplay
     *            The tables to show while clicking on this filter
     */
    public ItemListingFilter(final String name, final String label, final String tooltip, final List<String> tablesToDisplay) {
        super();
        this.name = name;
        this.label = label;
        this.tooltip = tooltip;
        this.tablesToDisplay = new LinkedList<String>(tablesToDisplay);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // SETTING OPTIONS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Add a filter to set to tables while displaying this filter.
     *
     * @param name
     *            The name of the filter. Can be an attribute or a filter form the IItem to display
     * @param value
     *            The value of the filter
     * @return This method returns "this" to allow cascading calls.
     */
    public ItemListingFilter addFilter(final String name, final String value) {
        if (this.additionalFilters == null) {
            this.additionalFilters = new HashMap<String, String>();
        }
        this.additionalFilters.put(name, value);
        return this;
    }

    /**
     * Add a filter to set to tables while displaying this filter.
     *
     * @param name
     *            The name of the filter. Can be an attribute or a filter form the IItem to display
     * @param value
     *            The value of the filter
     * @return This method returns "this" to allow cascading calls.
     */
    public ItemListingFilter addFilter(final String name, final APIID value) {
        return this.addFilter(name, value != null ? value.toString() : null);
    }

    /**
     * Set a list of filters to set to tables while displaying this filter.<br>
     * This method removes previously added filters.
     *
     * @param filters
     *            A map of filters under the form <filterName; filterValue>
     * @return This method returns "this" to allow cascading calls.
     */

    public ItemListingFilter setFilters(final Map<String, String> filters) {
        if (this.additionalFilters == null) {
            this.additionalFilters = new HashMap<String, String>();
        }
        if (filters != null) {
            this.additionalFilters.putAll(filters);
        }
        return this;
    }

    /**
     * Define an image to display in the filter button.
     *
     * @param imageUrl
     *            The url of the image to display
     * @return This method returns "this" to allow cascading calls.
     */
    public ItemListingFilter setImage(final String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // LIFE CYCLE
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @return the additionalFilters
     */
    public Map<String, String> getAdditionalFilters() {
        return this.additionalFilters;
    }

    /**
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * @return the tooltip
     */
    public String getTooltip() {
        return this.tooltip;
    }

    /**
     * @return the tablesToDisplay
     */
    public LinkedList<String> getTablesToDisplay() {
        return this.tablesToDisplay;
    }

    /**
     * @return the imageUrl
     */
    public String getImageUrl() {
        return this.imageUrl;
    }

    public boolean isResourceFilter() {
        return this.isResourceFilter;
    }

    /**
     * set a boolean to know if the filter is a ressource or a custom filter
     *
     * @param boolean isRessourceFilter
     */
    public void setIsResourceFilter(final boolean isRessourceFilter) {
        this.isResourceFilter = isRessourceFilter;
    }

    /**
     * @return the link
     */
    public Link getLink() {
        return this.link;
    }

    /**
     * @param link
     *            the link to set
     */
    public void setLink(final Link link) {
        this.link = link;
    }

    public boolean hasAdditionnalInfo() {
        return !isBlank(additionnalInfo);
    }

    public String getAdditionnalInfo() {
        return additionnalInfo;
    }

    public void setAdditionnalInfo(String additionnalInfo) {
        this.additionnalInfo = additionnalInfo;
    }

}
