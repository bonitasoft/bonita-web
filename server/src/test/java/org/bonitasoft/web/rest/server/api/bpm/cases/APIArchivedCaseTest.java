package org.bonitasoft.web.rest.server.api.bpm.cases;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;
import java.util.List;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.process.ArchivedProcessInstance;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.server.APITestWithMock;
import org.bonitasoft.web.rest.server.datastore.bpm.cases.ArchivedCaseDatastore;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author Nicolas TITH
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class APIArchivedCaseTest extends APITestWithMock {

    @InjectMocks
    private APIArchivedCase apiArchivedCase;

    @Mock
    private APISession apiSession;

    @Spy
    private final ArchivedCaseDatastore archivedCaseDatastore = new ArchivedCaseDatastore(apiSession);

    @Mock
    private ProcessAPI processAPI;

    @Mock
    private ArchivedProcessInstance archivedProcessInstance;

    @Before
    public void initializeMocks() throws Exception {
        initMocks(this);
        apiArchivedCase = spy(new APIArchivedCase());
        doReturn(archivedCaseDatastore).when(apiArchivedCase).defineDefaultDatastore();
        doReturn(processAPI).when(archivedCaseDatastore).getProcessApi();
        doReturn(archivedProcessInstance).when(processAPI).getArchivedProcessInstance(anyLong());
    }


    @Test
    public void deleteShouldDeleteSeveralItem() throws Exception {
        //given
        final List<APIID> idList = Arrays.asList(APIID.makeAPIID(1L), APIID.makeAPIID(2L), APIID.makeAPIID(3L));
        //when
        apiArchivedCase.delete(idList);

        //then
        verify(archivedCaseDatastore).delete(idList);
    }


}
