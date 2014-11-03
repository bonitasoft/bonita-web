package org.bonitasoft.web.rest.server.datastore;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.engine.search.SearchFilterOperation;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.engine.search.impl.SearchFilter;
import org.junit.Test;

public class SearchFilterProcessorTest {

    @Test
    public void addFilterShouldDoNothingIfFiltersNull() throws Exception {
        // given:
        final SearchOptionsBuilder searchOptionsBuilder = new SearchOptionsBuilder(0, 10);
        // when:
        new SearchFilterProcessor().addFilter(null, searchOptionsBuilder, null, null);
        // then:
        assertThat(searchOptionsBuilder.done().getFilters()).isEmpty();
    }

    @Test
    public void addFilterShouldDoNothingIfFiltersEmpty() throws Exception {
        // given:
        final Map<String, String> filters = new HashMap<String, String>(0);
        final SearchOptionsBuilder searchOptionsBuilder = new SearchOptionsBuilder(0, 10);
        // when:
        new SearchFilterProcessor().addFilter(filters, searchOptionsBuilder, null, null);
        // then:
        assertThat(searchOptionsBuilder.done().getFilters()).isEmpty();
    }

    @Test
    public void addFilterShouldAddGreaterThanFilterIfApplicable() throws Exception {
        // given:
        final String filterName = "someFilterKey";
        final String engineAttributeName = "EngineNumericKey";
        final Map<String, String> filters = new HashMap<String, String>(1);
        filters.put(filterName, "> someNumericValue ");
        final SearchOptionsBuilder searchOptionsBuilder = new SearchOptionsBuilder(0, 10);
        // when:
        new SearchFilterProcessor().addFilter(filters, searchOptionsBuilder, filterName, engineAttributeName);
        // then:
        assertThat(searchOptionsBuilder.done().getFilters().size()).isEqualTo(1);
        final SearchFilter searchFilter = searchOptionsBuilder.done().getFilters().get(0);
        assertThat(searchFilter.getOperation()).isEqualTo(SearchFilterOperation.GREATER_THAN);
        assertThat(searchOptionsBuilder.done().getFilters().get(0).getField()).isEqualTo(engineAttributeName);
        assertThat(searchOptionsBuilder.done().getFilters().get(0).getValue()).isEqualTo("someNumericValue");
    }

    @Test
    public void addFilterShouldDoNothingIfFilterValueIsNull() throws Exception {
        // given:
        final String filterName = "someFilterKey";
        final Map<String, String> filters = new HashMap<String, String>(1);
        filters.put(filterName, null);
        final SearchOptionsBuilder searchOptionsBuilder = new SearchOptionsBuilder(0, 10);
        // when:
        new SearchFilterProcessor().addFilter(filters, searchOptionsBuilder, filterName, null);
        // then:
        assertThat(searchOptionsBuilder.done().getFilters()).isEmpty();
    }

    @Test
    public void addFilterShouldAddLessThanFilterIfApplicable() throws Exception {
        // given:
        final String filterName = "someFilterKey";
        final String engineAttributeName = "EngineNumericKey";
        final Map<String, String> filters = new HashMap<String, String>(1);
        filters.put(filterName, "< someNumericValue ");
        final SearchOptionsBuilder searchOptionsBuilder = new SearchOptionsBuilder(0, 10);
        // when:
        new SearchFilterProcessor().addFilter(filters, searchOptionsBuilder, filterName, engineAttributeName);
        // then:
        assertThat(searchOptionsBuilder.done().getFilters().size()).isEqualTo(1);
        final SearchFilter searchFilter = searchOptionsBuilder.done().getFilters().get(0);
        assertThat(searchFilter.getOperation()).isEqualTo(SearchFilterOperation.LESS_THAN);
        assertThat(searchOptionsBuilder.done().getFilters().get(0).getField()).isEqualTo(engineAttributeName);
        assertThat(searchOptionsBuilder.done().getFilters().get(0).getValue()).isEqualTo("someNumericValue");
    }

    @Test
    public void addFilterShouldEqualFilterIfApplicable() throws Exception {
        // given:
        final String filterName = "someFilterKey";
        final String engineAttributeName = "EngineNumericKey";
        final String filterValue = "174";
        final Map<String, String> filters = new HashMap<String, String>(1);
        filters.put(filterName, filterValue);
        final SearchOptionsBuilder searchOptionsBuilder = new SearchOptionsBuilder(0, 10);
        // when:
        new SearchFilterProcessor().addFilter(filters, searchOptionsBuilder, filterName, engineAttributeName);
        // then:
        assertThat(searchOptionsBuilder.done().getFilters().size()).isEqualTo(1);
        final SearchFilter searchFilter = searchOptionsBuilder.done().getFilters().get(0);
        assertThat(searchFilter.getOperation()).isEqualTo(SearchFilterOperation.EQUALS);
        assertThat(searchOptionsBuilder.done().getFilters().get(0).getField()).isEqualTo(engineAttributeName);
        assertThat(searchOptionsBuilder.done().getFilters().get(0).getValue()).isEqualTo(Long.valueOf(filterValue));
    }
}
