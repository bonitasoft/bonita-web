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
package org.bonitasoft.web.rest.server.framework.search;

import java.util.List;

import org.bonitasoft.web.toolkit.client.common.exception.api.APISearchIndexOutOfRange;
import org.bonitasoft.web.toolkit.client.data.item.IItem;

/**
 * @author Séverin Moussel
 * 
 */
public class ItemSearchResult<T extends IItem> {

    private final int page;

    private final int length;

    private final long total;

    private final List<T> results;

    public ItemSearchResult(final int page, final int length, final long total, final List<T> results) {
        this.page = page;
        this.length = length;
        this.total = total;
        this.results = results;

        if (page < 0 || page > total) {
            throw new APISearchIndexOutOfRange(page);
        }
    }

    /**
     * @return the page
     */
    public int getPage() {
        return this.page;
    }

    /**
     * @return the length
     */
    public int getLength() {
        return this.length;
    }

    /**
     * @return the total
     */
    public long getTotal() {
        return this.total;
    }

    /**
     * @return the results
     */
    public List<T> getResults() {
        return this.results;
    }

}
