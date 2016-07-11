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

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.web.rest.server.datastore.converter.ItemConverter;
import org.bonitasoft.web.rest.server.datastore.converter.ItemSearchResultConverter;
import org.bonitasoft.web.toolkit.client.common.exception.api.APISearchIndexOutOfRange;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;

/**
 * @author Vincent Elcrin
 * 
 */
public class ItemSearchResultConverterTest {

    @Mock
    SearchResult<String> result;

    @Mock
    ItemConverter<IItem, String> converter;

    @Before
    public void initilizeMocks() {
        initMocks(this);
    }
    
    @Test
    public void testTotalCanBeRetrieved() {
        when(result.getCount()).thenReturn(5L);

        ItemSearchResultConverter<IItem, String> itemSearchResult = new ItemSearchResultConverter<IItem, String>(3, 2, result, converter);

        assertEquals(5L, itemSearchResult.toItemSearchResult().getTotal());
    }

    @Test
    public void testTotalSettedCanBeRetrieved() {
        ItemSearchResultConverter<IItem, String> itemSearchResult = new ItemSearchResultConverter<IItem, String>(3, 2, result, 8L, converter);

        assertEquals(8L, itemSearchResult.toItemSearchResult().getTotal());
    }

    @Test
    @Ignore("itemSearchResult return nbResultsByPage for length, need to be fixed in ItemSearchResult")
    public void testNumberOfResultCanBeRetrieved() {
        when(result.getResult()).thenReturn(asList("item1", "item2", "item3"));

        ItemSearchResultConverter<IItem, String> itemSearchResult = new ItemSearchResultConverter<IItem, String>(3, 10, result, 4L, converter);

        assertEquals(3, itemSearchResult.toItemSearchResult().getLength());
    }

    @Test(expected = APISearchIndexOutOfRange.class)
    public void testPageOutOfResultNumberThrowsExecption() throws Exception {
        when(result.getCount()).thenReturn(1L);

        new ItemSearchResultConverter<IItem, String>(2, 10, result, converter).toItemSearchResult();
    }

    @Test
    public void testPageNumberCanBeRetrieved() {
        ItemSearchResultConverter<IItem, String> itemSearchResult = new ItemSearchResultConverter<IItem, String>(5, 10, result, 8L, converter);

        assertEquals(5, itemSearchResult.toItemSearchResult().getPage());
    }

    @Test
    public void testResultingItemsCanBeRetrieved() {
        IItem item1 = mock(IItem.class);
        IItem item2 = mock(IItem.class);
        when(result.getResult()).thenReturn(asList("item1", "item2"));
        when(converter.convert(result.getResult())).thenReturn(asList(item1, item2));

        ItemSearchResultConverter<IItem, String> itemSearchResult = new ItemSearchResultConverter<IItem, String>(1, 10, result, 2, converter);

        assertEquals(item1, itemSearchResult.toItemSearchResult().getResults().get(0));
        assertEquals(item2, itemSearchResult.toItemSearchResult().getResults().get(1));
    }
}
