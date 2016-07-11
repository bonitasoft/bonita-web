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

class ItemListingTableSort {

    private String label;

    private String tooltip;

    private String attributeName;

    private boolean ascendant = false;

    public ItemListingTableSort(final String label, final String tooltip, final String attributeName, final boolean ascendant) {
        super();
        this.label = label;
        this.tooltip = tooltip;
        this.attributeName = attributeName;
        this.ascendant = ascendant;
    }

    public ItemListingTableSort(final String label, final String tooltip, final String attributeName) {
        this(label, tooltip, attributeName, false);
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * @param label
     *            the label to set
     */
    public ItemListingTableSort setLabel(final String label) {
        this.label = label;
        return this;
    }

    /**
     * @return the tooltip
     */
    public String getTooltip() {
        return this.tooltip;
    }

    /**
     * @param tooltip
     *            the tooltip to set
     */
    public ItemListingTableSort setTooltip(final String tooltip) {
        this.tooltip = tooltip;
        return this;
    }

    /**
     * @return the attributeName
     */
    public String getAttributeName() {
        return this.attributeName;
    }

    /**
     * @param attributeName
     *            the attributeName to set
     */
    public ItemListingTableSort setAttributeName(final String attributeName) {
        this.attributeName = attributeName;
        return this;
    }

    /**
     * @return the ascendent
     */
    public boolean isAscendant() {
        return this.ascendant;
    }

    /**
     * @param ascendant
     *            the ascendent to set
     */
    public ItemListingTableSort setAscendant(final boolean ascendant) {
        this.ascendant = ascendant;
        return this;
    }

}
