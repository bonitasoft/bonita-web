package org.bonitasoft.console.server.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.io.File;

import javax.servlet.http.HttpServletResponse;

import org.bonitasoft.console.common.server.utils.BonitaHomeFolderAccessor;
import org.bonitasoft.engine.api.IdentityAPI;
import org.bonitasoft.engine.identity.InvalidOrganizationFileFormatException;
import org.bonitasoft.engine.session.InvalidSessionException;
import org.bonitasoft.console.common.server.i18n.I18n;
import org.bonitasoft.web.toolkit.server.ServiceException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author Julien Mege
 */
@RunWith(MockitoJUnitRunner.class)
public class OrganizationImportServiceTest {
    
    @Mock
    IdentityAPI identityAPI;
    
    @Mock
    HttpServletResponse httpServletResponse;
    
    @BeforeClass
    public static void setup() {
        I18n.getInstance();
    }

    @Test
    public void should_verify_authorisation_for_the_given_location_param() throws Exception {

        final OrganizationImportService organizationImportService = spy(new OrganizationImportService());
        
        doReturn(".." + File.separator + ".." + File.separator + ".." + File.separator + "file.txt").when(organizationImportService).getFileUploadParameter();
        doReturn(1L).when(organizationImportService).getTenantId();

        try {
            organizationImportService.run();
        } catch (final ServiceException e) {
            assertTrue(e.getCause().getMessage().startsWith("Unauthorized access to the file"));
        }
    }
    
    @Test(expected=ServiceException.class)
    public void should_genrate_401_when_session_expires() throws Exception {

        final OrganizationImportService organizationImportService = spy(new OrganizationImportService());
        
        doReturn(httpServletResponse).when(organizationImportService).getHttpResponse();
        doReturn(new byte[0]).when(organizationImportService).getOrganizationContent(any(BonitaHomeFolderAccessor.class));
        doReturn(1L).when(organizationImportService).getTenantId();
        doReturn(identityAPI).when(organizationImportService).getIdentityAPI();
        doThrow(new InvalidSessionException("session expired")).when(identityAPI).importOrganization(anyString());

        try {
            organizationImportService.run();
        } finally {
            verify(httpServletResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @Test(expected=ServiceException.class)
    public void should_genrate_400_when_file_is_invalid() throws Exception {

        final OrganizationImportService organizationImportService = spy(new OrganizationImportService());
        
        doReturn(httpServletResponse).when(organizationImportService).getHttpResponse();
        doReturn(new byte[0]).when(organizationImportService).getOrganizationContent(any(BonitaHomeFolderAccessor.class));
        doReturn(1L).when(organizationImportService).getTenantId();
        doReturn(identityAPI).when(organizationImportService).getIdentityAPI();
        doThrow(new InvalidOrganizationFileFormatException("invalid format")).when(identityAPI).importOrganization(anyString());

        try {
            organizationImportService.run();
        } finally {
            verify(httpServletResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
