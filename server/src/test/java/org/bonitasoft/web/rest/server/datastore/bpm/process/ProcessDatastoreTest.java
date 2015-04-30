package org.bonitasoft.web.rest.server.datastore.bpm.process;

import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;

import java.io.IOException;

import org.bonitasoft.console.common.server.utils.BonitaHomeFolderAccessor;
import org.bonitasoft.console.common.server.utils.UnauthorizedFolderException;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.model.bpm.process.ProcessItem;
import org.bonitasoft.web.rest.server.APITestWithMock;
import org.bonitasoft.web.rest.server.engineclient.ProcessEngineClient;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIForbiddenException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class ProcessDatastoreTest extends APITestWithMock {

    private ProcessDatastore processDatastore;

    @Mock
    private APISession engineSession;

    @Mock
    private ProcessEngineClient processEngineClient;

    @Mock
    private BonitaHomeFolderAccessor tenantFolder;

    private final ProcessItem processItem = new ProcessItem();

    @Before
    public void setUp() throws Exception {
        processDatastore = spy(new ProcessDatastore(engineSession));
        doReturn(tenantFolder).when(processDatastore).getTenantFolder();
        doReturn(processEngineClient).when(processDatastore).getProcessEngineClient();
    }

    @Test(expected = APIForbiddenException.class)
    public void it_throws_an_exception_adding_a_bar_file_with_unauthorized_path() throws IOException {
        // Given
        doThrow(new UnauthorizedFolderException("")).when(tenantFolder).getTempFile(any(String.class), any(Long.class));

        // When
        processDatastore.add(processItem);

    }

    @Test(expected = APIException.class)
    public void it_throws_an_exception_adding_a_bar_file_with_ioException() throws IOException {
        // Given
        doThrow(new IOException("")).when(tenantFolder).getTempFile(any(String.class), any(Long.class));


        // When
        try {
            processDatastore.add(processItem);
        } catch (final APIForbiddenException e) {
            fail("Exception is not supposed to be an APIForbiddenException");
        }
    }

}
