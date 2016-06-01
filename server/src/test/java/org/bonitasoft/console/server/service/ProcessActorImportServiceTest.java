package org.bonitasoft.console.server.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import java.io.File;

import org.bonitasoft.web.toolkit.server.ServiceException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author Julien Mege
 */
@RunWith(MockitoJUnitRunner.class)
public class ProcessActorImportServiceTest {

    @Test
    public void should_verify_authorisation_for_the_given_location_param() throws Exception {

        final ProcessActorImportService processActorImportService = spy(new ProcessActorImportService());
        doReturn(".." + File.separator + ".." + File.separator + ".." + File.separator + "file.txt").when(processActorImportService).getFileUploadParameter();

        doReturn(1L).when(processActorImportService).getTenantId();

        try {
            processActorImportService.run();
        } catch (final ServiceException e) {
            assertTrue(e.getMessage().startsWith("Unauthorized access to the file"));
        }
    }

}
