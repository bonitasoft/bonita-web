/**
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.web.rest.server.datastore;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.engine.search.SearchFilterOperation;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.engine.search.impl.SearchFilter;
import org.bonitasoft.engine.session.APISession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author Celine Souchet
 */
@RunWith(MockitoJUnitRunner.class)
public class CommonDatastoreTest {

    @Mock
    private APISession engineSession;

    @InjectMocks
    private FakeCommonDatastore commonDatastore;

    /**
     * Test method for {@link CommonDatastore#addStringFilterToSearchBuilder(java.util.Map, org.bonitasoft.engine.search.SearchOptionsBuilder, String, String).
     */
    @Test
    public void addStringFilterToSearchBuilder_should_do_nothing_when_filters_is_null() {
        // given:
        final SearchOptionsBuilder searchOptionsBuilder = new SearchOptionsBuilder(0, 10);
        // when:
        commonDatastore.addStringFilterToSearchBuilder(null, searchOptionsBuilder, null, null);
        // then:
        assertThat(searchOptionsBuilder.done().getFilters()).isEmpty();
    }

    /**
     * Test method for {@link CommonDatastore#addStringFilterToSearchBuilder(java.util.Map, org.bonitasoft.engine.search.SearchOptionsBuilder, String, String).
     */
    @Test
    public void addStringFilterToSearchBuilder_should_do_nothing_when_filters_is_empty() {
        // given:
        final Map<String, String> filters = new HashMap<String, String>(0);
        final SearchOptionsBuilder searchOptionsBuilder = new SearchOptionsBuilder(0, 10);
        // when:
        commonDatastore.addStringFilterToSearchBuilder(filters, searchOptionsBuilder, null, null);
        // then:
        assertThat(searchOptionsBuilder.done().getFilters()).isEmpty();
    }

    /**
     * Test method for {@link CommonDatastore#addStringFilterToSearchBuilder(java.util.Map, org.bonitasoft.engine.search.SearchOptionsBuilder, String, String).
     */
    @Test
    public void addStringFilterToSearchBuilder_should_add_greater_than_filter_when_is_applicable() {
        // given:
        final String filterName = "someFilterKey";
        final String engineAttributeName = "EngineNumericKey";
        final Map<String, String> filters = new HashMap<String, String>(1);
        filters.put(filterName, "> someNumericValue ");
        final SearchOptionsBuilder searchOptionsBuilder = new SearchOptionsBuilder(0, 10);
        // when:
        commonDatastore.addStringFilterToSearchBuilder(filters, searchOptionsBuilder, filterName, engineAttributeName);
        // then:
        assertThat(searchOptionsBuilder.done().getFilters().size()).isEqualTo(1);
        final SearchFilter searchFilter = searchOptionsBuilder.done().getFilters().get(0);
        assertThat(searchFilter.getOperation()).isEqualTo(SearchFilterOperation.GREATER_THAN);
        assertThat(searchOptionsBuilder.done().getFilters().get(0).getField()).isEqualTo(engineAttributeName);
        assertThat(searchOptionsBuilder.done().getFilters().get(0).getValue()).isEqualTo("someNumericValue");
    }

    /**
     * Test method for {@link CommonDatastore#addStringFilterToSearchBuilder(java.util.Map, org.bonitasoft.engine.search.SearchOptionsBuilder, String, String).
     */
    @Test
    public void addStringFilterToSearchBuilder_should_do_nothing_when_filter_value_is_null() {
        // given:
        final String filterName = "someFilterKey";
        final Map<String, String> filters = new HashMap<String, String>(1);
        filters.put(filterName, null);
        final SearchOptionsBuilder searchOptionsBuilder = new SearchOptionsBuilder(0, 10);
        // when:
        commonDatastore.addStringFilterToSearchBuilder(filters, searchOptionsBuilder, filterName, null);
        // then:
        assertThat(searchOptionsBuilder.done().getFilters()).isEmpty();
    }

    /**
     * Test method for {@link CommonDatastore#addStringFilterToSearchBuilder(java.util.Map, org.bonitasoft.engine.search.SearchOptionsBuilder, String, String).
     */
    @Test
    public void addStringFilterToSearchBuilder_should_add_less_than_filter_when_is_applicable() {
        // given:
        final String filterName = "someFilterKey";
        final String engineAttributeName = "EngineNumericKey";
        final Map<String, String> filters = new HashMap<String, String>(1);
        filters.put(filterName, "< someNumericValue ");
        final SearchOptionsBuilder searchOptionsBuilder = new SearchOptionsBuilder(0, 10);
        // when:
        commonDatastore.addStringFilterToSearchBuilder(filters, searchOptionsBuilder, filterName, engineAttributeName);
        // then:
        assertThat(searchOptionsBuilder.done().getFilters().size()).isEqualTo(1);
        final SearchFilter searchFilter = searchOptionsBuilder.done().getFilters().get(0);
        assertThat(searchFilter.getOperation()).isEqualTo(SearchFilterOperation.LESS_THAN);
        assertThat(searchOptionsBuilder.done().getFilters().get(0).getField()).isEqualTo(engineAttributeName);
        assertThat(searchOptionsBuilder.done().getFilters().get(0).getValue()).isEqualTo("someNumericValue");
    }

    /**
     * Test method for {@link CommonDatastore#addStringFilterToSearchBuilder(java.util.Map, org.bonitasoft.engine.search.SearchOptionsBuilder, String, String).
     */
    @Test
    public void addStringFilterToSearchBuilder_should_add_equals_filter_when_is_applicable() {
        // given:
        final String filterName = "someFilterKey";
        final String engineAttributeName = "EngineNumericKey";
        final String filterValue = "174";
        final Map<String, String> filters = new HashMap<String, String>(1);
        filters.put(filterName, filterValue);
        final SearchOptionsBuilder searchOptionsBuilder = new SearchOptionsBuilder(0, 10);
        // when:
        commonDatastore.addStringFilterToSearchBuilder(filters, searchOptionsBuilder, filterName, engineAttributeName);
        // then:
        assertThat(searchOptionsBuilder.done().getFilters().size()).isEqualTo(1);
        final SearchFilter searchFilter = searchOptionsBuilder.done().getFilters().get(0);
        assertThat(searchFilter.getOperation()).isEqualTo(SearchFilterOperation.EQUALS);
        assertThat(searchOptionsBuilder.done().getFilters().get(0).getField()).isEqualTo(engineAttributeName);
        assertThat(searchOptionsBuilder.done().getFilters().get(0).getValue()).isEqualTo(filterValue);
    }

    /**
     * Test method for {@link CommonDatastore#addLongFilterToSearchBuilder(java.util.Map, org.bonitasoft.engine.search.SearchOptionsBuilder, String, String).
     */
    @Test
    public void addLongFilterToSearchBuilder_should_do_nothing_when_filters_is_null() {
        // given:
        final SearchOptionsBuilder searchOptionsBuilder = new SearchOptionsBuilder(0, 10);
        // when:
        commonDatastore.addLongFilterToSearchBuilder(null, searchOptionsBuilder, null, null);
        // then:
        assertThat(searchOptionsBuilder.done().getFilters()).isEmpty();
    }

    /**
     * Test method for {@link CommonDatastore#addLongFilterToSearchBuilder(java.util.Map, org.bonitasoft.engine.search.SearchOptionsBuilder, String, String).
     */
    @Test
    public void addLongFilterToSearchBuilder_should_do_nothing_when_filters_is_empty() {
        // given:
        final Map<String, String> filters = new HashMap<String, String>(0);
        final SearchOptionsBuilder searchOptionsBuilder = new SearchOptionsBuilder(0, 10);
        // when:
        commonDatastore.addLongFilterToSearchBuilder(filters, searchOptionsBuilder, null, null);
        // then:
        assertThat(searchOptionsBuilder.done().getFilters()).isEmpty();
    }

    /**
     * Test method for {@link CommonDatastore#addLongFilterToSearchBuilder(java.util.Map, org.bonitasoft.engine.search.SearchOptionsBuilder, String, String).
     */
    @Test
    public void addLongFilterToSearchBuilder_should_add_greater_than_filter_when_is_applicable() {
        // given:
        final String filterName = "someFilterKey";
        final String engineAttributeName = "EngineNumericKey";
        final Map<String, String> filters = new HashMap<String, String>(1);
        filters.put(filterName, "> 2 ");
        final SearchOptionsBuilder searchOptionsBuilder = new SearchOptionsBuilder(0, 10);
        // when:
        commonDatastore.addLongFilterToSearchBuilder(filters, searchOptionsBuilder, filterName, engineAttributeName);
        // then:
        assertThat(searchOptionsBuilder.done().getFilters().size()).isEqualTo(1);
        final SearchFilter searchFilter = searchOptionsBuilder.done().getFilters().get(0);
        assertThat(searchFilter.getOperation()).isEqualTo(SearchFilterOperation.GREATER_THAN);
        assertThat(searchOptionsBuilder.done().getFilters().get(0).getField()).isEqualTo(engineAttributeName);
        assertThat(searchOptionsBuilder.done().getFilters().get(0).getValue()).isEqualTo(2L);
    }

    /**
     * Test method for {@link CommonDatastore#addLongFilterToSearchBuilder(java.util.Map, org.bonitasoft.engine.search.SearchOptionsBuilder, String, String).
     */
    @Test
    public void addLongFilterToSearchBuilder_should_do_nothing_when_filter_value_is_null() {
        // given:
        final String filterName = "someFilterKey";
        final Map<String, String> filters = new HashMap<String, String>(1);
        filters.put(filterName, null);
        final SearchOptionsBuilder searchOptionsBuilder = new SearchOptionsBuilder(0, 10);
        // when:
        commonDatastore.addLongFilterToSearchBuilder(filters, searchOptionsBuilder, filterName, null);
        // then:
        assertThat(searchOptionsBuilder.done().getFilters()).isEmpty();
    }

    /**
     * Test method for {@link CommonDatastore#addLongFilterToSearchBuilder(java.util.Map, org.bonitasoft.engine.search.SearchOptionsBuilder, String, String).
     */
    @Test
    public void addLongFilterToSearchBuilder_should_add_less_than_filter_when_is_applicable() {
        // given:
        final String filterName = "someFilterKey";
        final String engineAttributeName = "EngineNumericKey";
        final Map<String, String> filters = new HashMap<String, String>(1);
        filters.put(filterName, "< 8 ");
        final SearchOptionsBuilder searchOptionsBuilder = new SearchOptionsBuilder(0, 10);
        // when:
        commonDatastore.addLongFilterToSearchBuilder(filters, searchOptionsBuilder, filterName, engineAttributeName);
        // then:
        assertThat(searchOptionsBuilder.done().getFilters().size()).isEqualTo(1);
        final SearchFilter searchFilter = searchOptionsBuilder.done().getFilters().get(0);
        assertThat(searchFilter.getOperation()).isEqualTo(SearchFilterOperation.LESS_THAN);
        assertThat(searchOptionsBuilder.done().getFilters().get(0).getField()).isEqualTo(engineAttributeName);
        assertThat(searchOptionsBuilder.done().getFilters().get(0).getValue()).isEqualTo(8L);
    }

    /**
     * Test method for {@link CommonDatastore#addLongFilterToSearchBuilder(java.util.Map, org.bonitasoft.engine.search.SearchOptionsBuilder, String, String).
     */
    @Test
    public void addLongFilterToSearchBuilder_should_add_equals_filter_when_is_applicable() {
        // given:
        final String filterName = "someFilterKey";
        final String engineAttributeName = "EngineNumericKey";
        final String filterValue = "174";
        final Map<String, String> filters = new HashMap<String, String>(1);
        filters.put(filterName, filterValue);
        final SearchOptionsBuilder searchOptionsBuilder = new SearchOptionsBuilder(0, 10);
        // when:
        commonDatastore.addLongFilterToSearchBuilder(filters, searchOptionsBuilder, filterName, engineAttributeName);
        // then:
        assertThat(searchOptionsBuilder.done().getFilters().size()).isEqualTo(1);
        final SearchFilter searchFilter = searchOptionsBuilder.done().getFilters().get(0);
        assertThat(searchFilter.getOperation()).isEqualTo(SearchFilterOperation.EQUALS);
        assertThat(searchOptionsBuilder.done().getFilters().get(0).getField()).isEqualTo(engineAttributeName);
        assertThat(searchOptionsBuilder.done().getFilters().get(0).getValue()).isEqualTo(174L);
    }
}
