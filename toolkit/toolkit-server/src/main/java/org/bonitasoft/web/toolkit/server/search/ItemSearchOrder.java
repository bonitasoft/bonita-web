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
package org.bonitasoft.web.toolkit.server.search;

/**
 * @author Séverin Moussel
 * 
 */
public class ItemSearchOrder {

    private final String attributeName;

    private final boolean asc;

    public ItemSearchOrder(final String attributeName) {
        this(attributeName, true);
    }

    public ItemSearchOrder(final String attributeName, final boolean asc) {
        super();
        this.attributeName = attributeName;
        this.asc = asc;
    }

    /**
     * @return the attributeName
     */
    public String getAttributeName() {
        return this.attributeName;
    }

    /**
     * @return the asc
     */
    public boolean isAsc() {
        return this.asc;
    }

    @Override
    public String toString() {
        return this.attributeName + " " + (this.asc ? "asc" : "desc");
    }

}
