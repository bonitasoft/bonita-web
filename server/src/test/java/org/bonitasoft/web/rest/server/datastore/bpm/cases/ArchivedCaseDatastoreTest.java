package org.bonitasoft.web.rest.server.datastore.bpm.cases;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.process.ArchivedProcessInstance;
import org.bonitasoft.engine.bpm.process.ArchivedProcessInstancesSearchDescriptor;
import org.bonitasoft.engine.bpm.process.impl.internal.ArchivedProcessInstanceImpl;
import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.bonitasoft.engine.exception.DeletionException;
import org.bonitasoft.engine.exception.SearchException;
import org.bonitasoft.engine.exception.ServerAPIException;
import org.bonitasoft.engine.exception.UnknownAPITypeException;
import org.bonitasoft.engine.search.SearchFilterOperation;
import org.bonitasoft.engine.search.SearchOptions;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.engine.search.impl.SearchFilter;
import org.bonitasoft.web.rest.server.APITestWithMock;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ArchivedCaseDatastoreTest extends APITestWithMock {

    private ArchivedCaseDatastore datastore;

    @Mock
    private ProcessAPI processAPI;

    @Mock
    private ArchivedProcessInstance archivedProcessInstance1;
    @Mock
    private ArchivedProcessInstance archivedProcessInstance2;
    @Mock
    private ArchivedProcessInstance archivedProcessInstance3;

    private long archivedProcessInstanceId1;

    private long archivedProcessInstanceId2;

    private long archivedProcessInstanceId3;

    private long sourceProcessInstanceId1;

    private long sourceProcessInstanceId2;

    private long sourceProcessInstanceId3;

    @Before
    public void initializeMocks() throws BonitaHomeNotSetException, ServerAPIException, UnknownAPITypeException, SearchException {
        initMocks(this);
        datastore = spy(new ArchivedCaseDatastore(null));
        doReturn(processAPI).when(datastore).getProcessApi();

        archivedProcessInstanceId1 = 1L;
        archivedProcessInstanceId2 = 2L;
        archivedProcessInstanceId3 = 3L;
        sourceProcessInstanceId1 = 11L;
        sourceProcessInstanceId2 = 12L;
        sourceProcessInstanceId3 = 13L;

        doReturn(sourceProcessInstanceId1).when(archivedProcessInstance1).getSourceObjectId();
        doReturn(sourceProcessInstanceId2).when(archivedProcessInstance2).getSourceObjectId();
        doReturn(sourceProcessInstanceId3).when(archivedProcessInstance3).getSourceObjectId();

        final ArchivedProcessInstance archivedProcessInstance1 = buildArchivedProcessInstance(archivedProcessInstanceId1, sourceProcessInstanceId1);
        final ArchivedProcessInstance archivedProcessInstance2 = buildArchivedProcessInstance(archivedProcessInstanceId2, sourceProcessInstanceId2);
        final ArchivedProcessInstance archivedProcessInstance3 = buildArchivedProcessInstance(archivedProcessInstanceId3, sourceProcessInstanceId3);
        final SearchResult<ArchivedProcessInstance> searchResult = mock(SearchResult.class);
        doReturn(searchResult).when(processAPI).searchArchivedProcessInstances(any(SearchOptions.class));
        doReturn(Arrays.asList(archivedProcessInstance1, archivedProcessInstance2, archivedProcessInstance3)).when(searchResult).getResult();
    }

    private ArchivedProcessInstance buildArchivedProcessInstance(final long id, final long sourceObjectId) {
        final ArchivedProcessInstanceImpl archivedProcessInstance1 = new ArchivedProcessInstanceImpl("process" + id);
        archivedProcessInstance1.setId(id);
        archivedProcessInstance1.setSourceObjectId(sourceObjectId);
        return archivedProcessInstance1;
    }

    @Test
    public void should_delete_archive_case_call_right_engine_method() throws DeletionException, SearchException {
        //given
        final List<APIID> idList = Arrays.asList(APIID.makeAPIID(archivedProcessInstanceId1), APIID.makeAPIID(archivedProcessInstanceId2),
                APIID.makeAPIID(archivedProcessInstanceId3));

        //when
        datastore.delete(idList);

        //then
        verify(processAPI).searchArchivedProcessInstances(any(SearchOptions.class));
        verify(processAPI).deleteArchivedProcessInstancesInAllStates(
                Arrays.asList(
                        sourceProcessInstanceId1,
                        sourceProcessInstanceId2,
                        sourceProcessInstanceId3)
                );
        verify(datastore).buildSearchOptionsToFilterOnId(idList);
    }

    @Test(expected = APIException.class)
    public void should_throw_an_api_exception_when_deletion_exception_is_rised() throws DeletionException {
        //given
        doThrow(new DeletionException("exception!")).when(processAPI).deleteArchivedProcessInstancesInAllStates(anyList());
        final List<APIID> idList = Arrays.asList(APIID.makeAPIID(archivedProcessInstanceId1));

        //when
        datastore.delete(idList);
    }

    @Test
    public void delete_archive_case_should_not_call_searchArchivedProcessInstances() throws DeletionException, SearchException {
        // When
        datastore.delete(Collections.<APIID> emptyList());

        // Then
        verify(processAPI, never()).searchArchivedProcessInstances(any(SearchOptions.class));
        verify(processAPI).deleteArchivedProcessInstancesInAllStates(Collections.<Long> emptyList());
        verify(datastore, never()).buildSearchOptionsToFilterOnId(anyListOf(APIID.class));
    }

    @Test
    public void buildSearchOptionsToFilterOnId_should_chain_filters_and_Or() {
        // Given
        final List<APIID> idList = Arrays.asList(APIID.makeAPIID(archivedProcessInstanceId1), APIID.makeAPIID(archivedProcessInstanceId2),
                APIID.makeAPIID(archivedProcessInstanceId3));

        // When
        final SearchOptions searchOptions = datastore.buildSearchOptionsToFilterOnId(idList);

        // Then
        final List<SearchFilter> filters = searchOptions.getFilters();
        assertEquals(5, filters.size());
        checkFilterOnId(filters.get(0), archivedProcessInstanceId1);
        checkFilterOr(filters.get(1));
        checkFilterOnId(filters.get(2), archivedProcessInstanceId2);
        checkFilterOr(filters.get(3));
        checkFilterOnId(filters.get(4), archivedProcessInstanceId3);
    }

    private void checkFilterOnId(final SearchFilter filter, final long id) {
        assertEquals(ArchivedProcessInstancesSearchDescriptor.ID, filter.getField());
        assertEquals(id, filter.getValue());
    }

    private void checkFilterOr(final SearchFilter filter) {
        assertEquals(SearchFilterOperation.OR, filter.getOperation());
    }
}
