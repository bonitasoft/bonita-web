package org.bonitasoft.web.rest.server.datastore.bpm.cases;

import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.process.ArchivedProcessInstance;
import org.bonitasoft.engine.bpm.process.ArchivedProcessInstanceNotFoundException;
import org.bonitasoft.engine.bpm.process.ProcessInstanceNotFoundException;
import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.bonitasoft.engine.exception.DeletionException;
import org.bonitasoft.engine.exception.ServerAPIException;
import org.bonitasoft.engine.exception.UnknownAPITypeException;
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
    public void initializeMocks() throws BonitaHomeNotSetException, ServerAPIException, UnknownAPITypeException, ArchivedProcessInstanceNotFoundException {
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

        doReturn(archivedProcessInstance1).when(processAPI).getArchivedProcessInstance(archivedProcessInstanceId1);
        doReturn(archivedProcessInstance2).when(processAPI).getArchivedProcessInstance(archivedProcessInstanceId2);
        doReturn(archivedProcessInstance3).when(processAPI).getArchivedProcessInstance(archivedProcessInstanceId3);
    }

    @Test
    public void should_delete_archive_case_call_right_engine_method() throws DeletionException, ArchivedProcessInstanceNotFoundException,
            ProcessInstanceNotFoundException {
        //given

        final List<APIID> idList = Arrays.asList(APIID.makeAPIID(archivedProcessInstanceId1), APIID.makeAPIID(archivedProcessInstanceId2),
                APIID.makeAPIID(archivedProcessInstanceId3));

        //when
        datastore.delete(idList);

        //then
        verify(processAPI, times(3)).getArchivedProcessInstance(anyLong());
        verify(processAPI).deleteArchivedProcessInstancesInAllStates(
                Arrays.asList(
                        sourceProcessInstanceId1,
                        sourceProcessInstanceId2,
                        sourceProcessInstanceId3)
                );
    }

    @Test
    public void should_delete_all_archived_cases_when_one_id_is_given() throws DeletionException, ProcessInstanceNotFoundException,
            ArchivedProcessInstanceNotFoundException {
        //given
        final List<APIID> idList = Collections.singletonList(APIID.makeAPIID(archivedProcessInstanceId1));

        //when
        datastore.delete(idList);

        //then
        verify(processAPI).getArchivedProcessInstance(archivedProcessInstanceId1);
        verify(processAPI, times(1)).getArchivedProcessInstance(archivedProcessInstanceId1);
        verify(processAPI).deleteArchivedProcessInstancesInAllStates(Collections.singletonList(sourceProcessInstanceId1));
    }

    @Test(expected = APIException.class)
    public void should_throw_an_api_exception_when_deletion_exception_is_rised() throws DeletionException {
        //given
        doThrow(new DeletionException("exception!")).when(processAPI).deleteArchivedProcessInstancesInAllStates(anyList());
        final List<APIID> idList = Collections.singletonList(APIID.makeAPIID(archivedProcessInstanceId1));

        //when
        datastore.delete(idList);

    }



}
