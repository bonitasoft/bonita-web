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
package org.bonitasoft.web.rest.server.datastore.filter;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.bonitasoft.web.rest.server.datastore.converter.AttributeConverter;
import org.bonitasoft.web.rest.server.datastore.filter.Filter;
import org.bonitasoft.web.rest.server.datastore.filter.FilterCreator;
import org.bonitasoft.web.rest.server.datastore.filter.Filters;
import org.bonitasoft.web.rest.server.datastore.filter.GenericFilterCreator;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.common.util.MapUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

/**
 * @author Vincent Elcrin
 * 
 */
public class FiltersTest {

    @Mock
    FilterCreator filterCreator;

    @Mock
    Filter<Long> longFilter;

    @Mock
    Filter<String> strFilter;

    @Mock
    AttributeConverter fiedConverter;

    @Before
    public void initFilters() {
        initMocks(this);

        doReturn("field1").when(longFilter).getField();
        doReturn(3L).when(longFilter).getValue();

        doReturn("field2").when(strFilter).getField();
        doReturn("str").when(strFilter).getValue();
    }

    @Test
    public void testFiltersListWithoutFilterCreator() throws Exception {
        when(fiedConverter.convert("field"))
                .thenReturn("field");
        Filters filters = new Filters(aMapWith(new Arg("field", "value")), new GenericFilterCreator(fiedConverter));

        List<Filter<?>> filterList = filters.asList();

        Assert.assertEquals("field", filterList.get(0).getField());
        Assert.assertEquals("value", filterList.get(0).getValue());
    }

    @Test
    public void testFilterListWithMultiTypeFilterCreator() throws Exception {
        Map<String, String> map = aMapWith(new Arg("field1", "value"),
                new Arg("field2", "value"));

        doReturn(longFilter).when(filterCreator).create(eq("field1"), anyString());
        doReturn(strFilter).when(filterCreator).create(eq("field2"), anyString());

        Filters filters = new Filters(map, filterCreator);

        assertTrue(IsRightValue(filters.asList().get(0)));
        assertTrue(IsRightValue(filters.asList().get(1)));
    }

    private boolean IsRightValue(Filter<?> filter) {
        if ("field1".equals(filter.getField())) {
            return filter.getValue().equals(3L);
        } else if ("field2".equals(filter.getField())) {
            return filter.getValue().equals("str");
        } else {
            return false;
        }
    }

    private Map<String, String> aMapWith(Arg... args) {
        return MapUtil.asMap(args);
    }

}
