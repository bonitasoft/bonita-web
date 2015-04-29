package org.bonitasoft.console.server.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import java.io.File;

import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstants;
import org.bonitasoft.web.toolkit.client.common.util.StringUtil;
import org.bonitasoft.web.toolkit.server.ServiceException;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author Julien Mege
 */
@RunWith(MockitoJUnitRunner.class)
public class OrganizationImportServiceTest {
    private String savedBonitaHomeProperty;

    @Test
    public void should_verify_authorisation_for_the_given_location_param() throws
    Exception {

        final OrganizationImportService organizationImportService = spy(new OrganizationImportService());
        savedBonitaHomeProperty = System.getProperty(WebBonitaConstants.BONITA_HOME);
        System.setProperty(WebBonitaConstants.BONITA_HOME, "target/bonita-home");
        doReturn(".." + File.separator + ".." + File.separator + ".." + File.separator + "file.txt").when(organizationImportService).getFileUploadParameter();

        doReturn(1L).when(organizationImportService).getTenantId();

        try {
            organizationImportService.run();
        } catch (final ServiceException e) {
            assertTrue(e.getCause().getMessage().startsWith("Unauthorized access to the file"));
        }
    }

    @After
    public void teardown() throws Exception {
        if (StringUtil.isBlank(savedBonitaHomeProperty)) {
            System.clearProperty(WebBonitaConstants.BONITA_HOME);
        } else {
            System.setProperty(WebBonitaConstants.BONITA_HOME, savedBonitaHomeProperty);
        }
    }

}