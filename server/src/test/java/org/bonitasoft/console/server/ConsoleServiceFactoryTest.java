package org.bonitasoft.console.server;

import static org.assertj.core.api.Assertions.assertThat;

import org.bonitasoft.console.server.service.OrganizationImportService;
import org.bonitasoft.console.server.service.ProcessActorImportService;
import org.bonitasoft.web.toolkit.server.Service;
import org.bonitasoft.web.toolkit.server.ServiceNotFoundException;
import org.junit.Test;


public class ConsoleServiceFactoryTest {

    @Test
    public void getService_should_return_OrganizationImportService_when_organization_import() throws Exception {
        // Given
        ConsoleServiceFactory consoleServiceFacotry = new ConsoleServiceFactory();
        
        // When
        Service service = consoleServiceFacotry.getService("/organization/import");
        
        // Then
        assertThat(service).isInstanceOf(OrganizationImportService.class);
    }
    
    @Test
    public void getService_should_return_ProcessActorImportService_when_bpm_process_importActors() throws Exception {
        // Given
        ConsoleServiceFactory consoleServiceFacotry = new ConsoleServiceFactory();
        
        // When
        Service service = consoleServiceFacotry.getService("/bpm/process/importActors");
        
        // Then
        assertThat(service).isInstanceOf(ProcessActorImportService.class);
    }
    
    @Test(expected=ServiceNotFoundException.class)
    public void getService_should_throw_ServiceNotFoundException_when_invalid_input(){
        // Given
        ConsoleServiceFactory consoleServiceFacotry = new ConsoleServiceFactory();
        
        // When
        consoleServiceFacotry.getService("invalidService");
    }

}
