package org.bonitasoft.web.rest.server.datastore.bpm.cases;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.web.rest.server.APITestWithMock;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

/**
 * @author Colin PUY
 * 
 */
public class CaseVariableDatastoreTest extends APITestWithMock {

    private CaseVariableDatastore datastore;

    @Mock
    private ProcessAPI processAPI;

    @Before
    public void initializeMocks() {
        initMocks(this);

        datastore = spy(new CaseVariableDatastore(null));
        doReturn(processAPI).when(datastore).getEngineProcessAPI();
    }

    @Test
    public void testUpdateVariableValue() throws Exception {
        long caseId = 1L;
        String name = "aName";
        String newValue = "newValue";
        
        datastore.updateVariableValue(caseId, name, String.class.getName(), newValue);

        verify(processAPI).updateProcessDataInstance(name, caseId, newValue);
    }

}
