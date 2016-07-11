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
package org.bonitasoft.console.client.common.view;

import java.util.HashMap;

import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.AbstractAttributeReader;

/**
 * @author Séverin Moussel
 */
public class SelectItemAndDoEntry {

    private final String name;

    private final String label;

    private final String tooltip;

    private final ItemDefinition definition;

    private final AbstractAttributeReader suggestionLabel;

    private final String suggestionValueAttributeName;
    
    private HashMap<String, String> autocompleteFilters = new HashMap<String, String>();


    public SelectItemAndDoEntry(final String name, final String label, final String tooltip, final ItemDefinition definition,
            final AbstractAttributeReader suggestionLabel,
            final String suggestionValueAttributeName) {
        super();
        this.name = name;
        this.label = label;
        this.tooltip = tooltip;
        this.definition = definition;
        this.suggestionLabel = suggestionLabel;
        this.suggestionValueAttributeName = suggestionValueAttributeName;
    }

    protected final String getName() {
        return this.name;
    }

    /**
     * @return the label
     */
    protected final String getLabel() {
        return this.label;
    }

    /**
     * @return the tooltip
     */
    protected final String getTooltip() {
        return this.tooltip;
    }

    /**
     * @return the definition
     */
    protected final ItemDefinition getItemDefinition() {
        return this.definition;
    }

    /**
     * @return the suggestionLabel
     */
    protected final AbstractAttributeReader getSuggestionLabel() {
        return this.suggestionLabel;
    }

    /**
     * @return the suggestionValueAttributeName
     */
    protected final String getSuggestionValueAttributeName() {
        return this.suggestionValueAttributeName;
    }

    public void addFilter(String filterName, String filterValue) {
        autocompleteFilters.put(filterName, filterValue);
    }
    
    public HashMap<String, String> getFilters() {
        return autocompleteFilters;
    }
}
