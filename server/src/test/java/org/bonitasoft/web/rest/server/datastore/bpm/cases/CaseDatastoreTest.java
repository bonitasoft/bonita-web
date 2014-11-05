/**
 * Copyright (C) 2014 BonitaSoft S.A.
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
package org.bonitasoft.web.rest.server.datastore.bpm.cases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.search.SearchException;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.process.ProcessInstanceSearchDescriptor;
import org.bonitasoft.engine.search.SearchOptions;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.engine.search.impl.SearchFilter;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.model.bpm.cases.CaseItem;
import org.bonitasoft.web.rest.server.datastore.SearchFilterProcessor;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.util.MapUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(MockitoJUnitRunner.class)
public class CaseDatastoreTest {

    Logger logger = LoggerFactory.getLogger(CaseDatastoreTest.class);

    private CaseDatastore caseDatastore;

    SearchFilterProcessor processor = mock(SearchFilterProcessor.class);

    @Mock
    private ProcessAPI processAPI;

    @Before
    public void setUp() throws Exception {
        caseDatastore = spy(new CaseDatastore(mock(APISession.class), processor));
    }

    @Test
    public void testSearch() throws Exception {
        final HashMap<String, String> filters = new HashMap<String, String>();
        final String processName = "Pool";
        filters.put(CaseItem.ATTRIBUTE_PROCESS_NAME, processName);
        final ArgumentCaptor<SearchOptionsBuilder> argument = ArgumentCaptor.forClass(SearchOptionsBuilder.class);
        doReturn(mock(SearchResult.class)).when(caseDatastore).runSearch(eq(filters), any(SearchOptionsBuilder.class));
        caseDatastore.search(0, 100, null, null, filters);
        verify(caseDatastore).runSearch(eq(filters), argument.capture());
        final List<SearchFilter> searchFilters = argument.getValue().done().getFilters();
        assertThat(searchFilters).hasSize(2);
        assertThat(searchFilters.get(0).getField()).isEqualToIgnoringCase(ProcessInstanceSearchDescriptor.NAME);
        assertThat(searchFilters.get(0).getValue()).isSameAs(processName);
        logger.info("{}", searchFilters.get(0).getField());
    }

    @Test
    public void buildSearchOptionsShouldAddFiltersOnDate() throws Exception {
        // when:
        final Map<String, String> filters = new HashMap<String, String>(0);
        caseDatastore.buildSearchOptions(0, 1, null, null, filters);
        // then:
        verify(processor).addFilter(eq(filters), any(SearchOptionsBuilder.class), eq(CaseItem.ATTRIBUTE_START_DATE),
                eq(ProcessInstanceSearchDescriptor.START_DATE));
        verify(processor)
                .addFilter(eq(filters), any(SearchOptionsBuilder.class), eq(CaseItem.ATTRIBUTE_END_DATE), eq(ProcessInstanceSearchDescriptor.END_DATE));
        verify(processor).addFilter(eq(filters), any(SearchOptionsBuilder.class), eq(CaseItem.ATTRIBUTE_LAST_UPDATE_DATE),
                eq(ProcessInstanceSearchDescriptor.LAST_UPDATE));
    }


    @Test
    public void shouldSearchWithAOpenStateFilterCallSearchOpenProcessInstances() throws Exception {
        // given
        doReturn(processAPI).when(caseDatastore).getProcessAPI();
        final Map<String, String> filters = new HashMap<String, String>();
        final SearchOptionsBuilder builder = new SearchOptionsBuilder(0, 10);
        final String state = "started";
        filters.put(CaseItem.FILTER_STATE, state);

        //when
        caseDatastore.runSearch(filters, builder);

        //then
        verify(processAPI).searchOpenProcessInstances(builder.done());

    }

    @Test
    public void shouldSearchWithoutAStateFilterCallSearchProcessInstances() throws Exception {
        // given
        doReturn(processAPI).when(caseDatastore).getProcessAPI();
        final Map<String, String> filters = new HashMap<String, String>();
        final SearchOptionsBuilder builder = new SearchOptionsBuilder(0, 10);

        //when
        caseDatastore.runSearch(filters, builder);

        //then
        verify(processAPI).searchProcessInstances(builder.done());

    }

    @Test
    public void shouldSearchWithANullStateFilterCallSearchProcessInstances() throws Exception {
        // given
        doReturn(processAPI).when(caseDatastore).getProcessAPI();
        final Map<String, String> filters = new HashMap<String, String>();
        filters.put(CaseItem.FILTER_STATE, null);
        final SearchOptionsBuilder builder = new SearchOptionsBuilder(0, 10);

        //when
        caseDatastore.runSearch(filters, builder);

        //then
        verify(processAPI).searchProcessInstances(builder.done());

    }

    @Test
    public void shouldSearchWithAUnknowStateFilterCallSearchProcessInstances() throws Exception {
        // given
        doReturn(processAPI).when(caseDatastore).getProcessAPI();
        final Map<String, String> filters = new HashMap<String, String>();
        filters.put(CaseItem.FILTER_STATE, "unknown");
        final SearchOptionsBuilder builder = new SearchOptionsBuilder(0, 10);

        //when
        caseDatastore.runSearch(filters, builder);

        //then
        verify(processAPI).searchProcessInstances(builder.done());

    }

    @Test
    public void shouldSearchWithAFailedStateFilterCallSearchOpenProcessInstances() throws Exception {
        // given
        doReturn(processAPI).when(caseDatastore).getProcessAPI();
        final Map<String, String> filters = new HashMap<String, String>();
        final SearchOptionsBuilder builder = new SearchOptionsBuilder(0, 10);
        final String state = "failed";
        filters.put(CaseItem.FILTER_STATE, state);

        //when
        caseDatastore.runSearch(filters, builder);

        //then
        verify(processAPI).searchFailedProcessInstances(builder.done());

    }

    @Test
    public void shouldSearchWithAnErrorStateFilterCallSearchOpenProcessInstances() throws Exception {
        // given
        doReturn(processAPI).when(caseDatastore).getProcessAPI();
        final Map<String, String> filters = new HashMap<String, String>();
        final SearchOptionsBuilder builder = new SearchOptionsBuilder(0, 10);
        final String state = "error";
        filters.put(CaseItem.FILTER_STATE, state);

        //when
        caseDatastore.runSearch(filters, builder);

        //then
        verify(processAPI).searchFailedProcessInstances(builder.done());

    }

    @Test
    public void shouldSearchWithAUserIdFilterCallSearchOpenProcessInstancesInvolvingUser() throws Exception {
        // given
        doReturn(processAPI).when(caseDatastore).getProcessAPI();
        final Map<String, String> filters = new HashMap<String, String>();
        final SearchOptionsBuilder builder = new SearchOptionsBuilder(0, 10);
        filters.put(CaseItem.FILTER_USER_ID, "1");

        //when
        caseDatastore.runSearch(filters, builder);

        //then
        verify(processAPI).searchOpenProcessInstancesInvolvingUser(MapUtil.getValueAsLong(filters, CaseItem.FILTER_USER_ID), builder.done());

    }

    @Test
    public void shouldSearchWithASupervisorIdFilterCallSearchOpenProcessInstancesSupervisedBy() throws Exception {
        // given
        doReturn(processAPI).when(caseDatastore).getProcessAPI();
        final Map<String, String> filters = new HashMap<String, String>();
        final SearchOptionsBuilder builder = new SearchOptionsBuilder(0, 10);
        filters.put(CaseItem.FILTER_SUPERVISOR_ID, "1");

        //when
        caseDatastore.runSearch(filters, builder);

        //then
        verify(processAPI).searchOpenProcessInstancesSupervisedBy(MapUtil.getValueAsLong(filters, CaseItem.FILTER_SUPERVISOR_ID), builder.done());

    }

    ///////////////////////////////////////////////////////

    @Test
    public void shouldSearchWithAOpenStateFilterThrowAnAPIExceptionWhenASearchExceptionOccurs() throws Exception {
        // given
        doReturn(processAPI).when(caseDatastore).getProcessAPI();
        doThrow(SearchException.class).when(processAPI).searchOpenProcessInstances(any(SearchOptions.class));
        final Map<String, String> filters = new HashMap<String, String>();
        final SearchOptionsBuilder builder = new SearchOptionsBuilder(0, 10);
        final String state = "started";
        filters.put(CaseItem.FILTER_STATE, state);

        //when
        try{
            caseDatastore.runSearch(filters, builder);
            fail();
        } catch (final Exception e) {
            //then
            assertThat(e).isInstanceOf(APIException.class);
            assertThat(e.getCause()).isInstanceOf(SearchException.class);
        }
    }

    @Test
    public void shouldSearchWithoutAStateFilterThrowAnAPIExceptionWhenASearchExceptionOccurs() throws Exception {
        // given
        doReturn(processAPI).when(caseDatastore).getProcessAPI();
        doThrow(SearchException.class).when(processAPI).searchProcessInstances(any(SearchOptions.class));
        final Map<String, String> filters = new HashMap<String, String>();
        final SearchOptionsBuilder builder = new SearchOptionsBuilder(0, 10);

        //when
        try {
            caseDatastore.runSearch(filters, builder);
            fail();
        } catch (final Exception e) {
            //then
            assertThat(e).isInstanceOf(APIException.class);
            assertThat(e.getCause()).isInstanceOf(SearchException.class);
        }

    }

    @Test
    public void shouldSearchWithANullStateFilterThrowAnAPIExceptionWhenASearchExceptionOccurs() throws Exception {
        // given
        doReturn(processAPI).when(caseDatastore).getProcessAPI();
        doThrow(SearchException.class).when(processAPI).searchProcessInstances(any(SearchOptions.class));
        final Map<String, String> filters = new HashMap<String, String>();
        filters.put(CaseItem.FILTER_STATE, null);
        final SearchOptionsBuilder builder = new SearchOptionsBuilder(0, 10);

        //when
        try {
            caseDatastore.runSearch(filters, builder);
            fail();
        } catch (final Exception e) {
            //then
            assertThat(e).isInstanceOf(APIException.class);
            assertThat(e.getCause()).isInstanceOf(SearchException.class);
        }

    }

    @Test
    public void shouldSearchWithAUnknowStateFilterThrowAnAPIExceptionWhenASearchExceptionOccurs() throws Exception {
        // given
        doReturn(processAPI).when(caseDatastore).getProcessAPI();
        doThrow(SearchException.class).when(processAPI).searchProcessInstances(any(SearchOptions.class));
        final Map<String, String> filters = new HashMap<String, String>();
        filters.put(CaseItem.FILTER_STATE, "unknown");
        final SearchOptionsBuilder builder = new SearchOptionsBuilder(0, 10);

        //when
        try {
            caseDatastore.runSearch(filters, builder);
            fail();
        } catch (final Exception e) {
            //then
            assertThat(e).isInstanceOf(APIException.class);
            assertThat(e.getCause()).isInstanceOf(SearchException.class);
        }

    }

    @Test
    public void shouldSearchWithAFailedStateFilterThrowAnAPIExceptionWhenASearchExceptionOccurs() throws Exception {
        // given
        doReturn(processAPI).when(caseDatastore).getProcessAPI();
        doThrow(SearchException.class).when(processAPI).searchFailedProcessInstances(any(SearchOptions.class));
        final Map<String, String> filters = new HashMap<String, String>();
        final SearchOptionsBuilder builder = new SearchOptionsBuilder(0, 10);
        final String state = "failed";
        filters.put(CaseItem.FILTER_STATE, state);

        //when
        try {
            caseDatastore.runSearch(filters, builder);
            fail();
        } catch (final Exception e) {
            //then
            assertThat(e).isInstanceOf(APIException.class);
            assertThat(e.getCause()).isInstanceOf(SearchException.class);
        }

    }

    @Test
    public void shouldSearchWithAnErrorStateFilterThrowAnAPIExceptionWhenASearchExceptionOccurs() throws Exception {
        // given
        doReturn(processAPI).when(caseDatastore).getProcessAPI();
        doThrow(SearchException.class).when(processAPI).searchFailedProcessInstances(any(SearchOptions.class));
        final Map<String, String> filters = new HashMap<String, String>();
        final SearchOptionsBuilder builder = new SearchOptionsBuilder(0, 10);
        final String state = "error";
        filters.put(CaseItem.FILTER_STATE, state);

        //when
        try {
            caseDatastore.runSearch(filters, builder);
            fail();
        } catch (final Exception e) {
            //then
            assertThat(e).isInstanceOf(APIException.class);
            assertThat(e.getCause()).isInstanceOf(SearchException.class);
        }

    }

    @Test
    public void shouldSearchWithAUserIdFilterThrowAnAPIExceptionWhenASearchExceptionOccurs() throws Exception {
        // given
        doReturn(processAPI).when(caseDatastore).getProcessAPI();
        final Map<String, String> filters = new HashMap<String, String>();
        final SearchOptionsBuilder builder = new SearchOptionsBuilder(0, 10);
        filters.put(CaseItem.FILTER_USER_ID, "1");
        doThrow(SearchException.class).when(processAPI).searchOpenProcessInstancesInvolvingUser(MapUtil.getValueAsLong(filters, CaseItem.FILTER_USER_ID),
                builder.done());

        //when
        try {
            caseDatastore.runSearch(filters, builder);
            fail();
        } catch (final Exception e) {
            //then
            assertThat(e).isInstanceOf(APIException.class);
            assertThat(e.getCause()).isInstanceOf(SearchException.class);
        }

    }

    @Test
    public void shouldSearchWithAnSupervisorIdFilterThrowAnAPIExceptionWhenASearchExceptionOccurs() throws Exception {
        // given
        doReturn(processAPI).when(caseDatastore).getProcessAPI();
        final Map<String, String> filters = new HashMap<String, String>();
        final SearchOptionsBuilder builder = new SearchOptionsBuilder(0, 10);
        filters.put(CaseItem.FILTER_SUPERVISOR_ID, "1");
        doThrow(SearchException.class).when(processAPI).searchOpenProcessInstancesSupervisedBy(anyLong(), any(SearchOptions.class));
        //when
        try {
            caseDatastore.runSearch(filters, builder);
            fail();
        } catch (final Exception e) {
            //then
            assertThat(e).isInstanceOf(APIException.class);
            assertThat(e.getCause()).isInstanceOf(SearchException.class);
        }
    }
}
