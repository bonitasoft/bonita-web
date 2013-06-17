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
package org.bonitasoft.web.rest.server.datastore.converter;

import java.io.Serializable;

import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.server.search.ItemSearchResult;

/**
 * @author Vincent Elcrin
 * 
 */
public class ItemSearchResultConverter<I extends IItem, E extends Serializable> {
    
    private int page;
    private SearchResult<E> result;
    private ItemConverter<I, E> converter;
    private long total;
    private int nbResultsByPage;
    
    public ItemSearchResultConverter(int page, int nbResultsByPage, SearchResult<E> result, ItemConverter<I, E> converter) {
        this(page, nbResultsByPage, result, result.getCount(), converter);
    }

    public ItemSearchResultConverter(int page, int nbResultsByPage, SearchResult<E> result, long total, ItemConverter<I, E> converter) {
        this.page = page;
        this.nbResultsByPage = nbResultsByPage;
        this.result = result;
        this.total = total;
        this.converter = converter;
    }

    public ItemSearchResult<I> toItemSearchResult() {
        return new ItemSearchResult<I>(page, nbResultsByPage, total, converter.convert(result.getResult()));
    }
}
