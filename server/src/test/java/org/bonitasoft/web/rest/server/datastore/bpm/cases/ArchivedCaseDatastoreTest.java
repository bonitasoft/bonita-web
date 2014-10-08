package org.bonitasoft.web.rest.server.datastore.bpm.cases;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;
import java.util.List;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.bonitasoft.engine.exception.DeletionException;
import org.bonitasoft.engine.exception.ServerAPIException;
import org.bonitasoft.engine.exception.UnknownAPITypeException;
import org.bonitasoft.web.rest.server.APITestWithMock;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;


public class ArchivedCaseDatastoreTest extends APITestWithMock {

    private ArchivedCaseDatastore datastore;

    @Mock
    private ProcessAPI processAPI;

    @Before
    public void initializeMocks() throws BonitaHomeNotSetException, ServerAPIException, UnknownAPITypeException {
        initMocks(this);
        datastore = spy(new ArchivedCaseDatastore(null));
        doReturn(processAPI).when(datastore).getProcessApi();
    }

    @Test
    public void should_delete_archive_case_call_right_engine_method() throws DeletionException {
        //given
        final List<APIID> idList = Arrays.asList(APIID.makeAPIID(1L), APIID.makeAPIID(2L), APIID.makeAPIID(3L));
        //when
        datastore.delete(idList);

        //then
        verify(processAPI,times(3)).deleteArchivedProcessInstance(anyLong());
    }
}
