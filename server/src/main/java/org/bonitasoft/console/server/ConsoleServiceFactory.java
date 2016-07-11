package org.bonitasoft.console.server;

import org.bonitasoft.console.server.service.ApplicationsImportService;
import org.bonitasoft.console.server.service.OrganizationImportService;
import org.bonitasoft.console.server.service.ProcessActorImportService;
import org.bonitasoft.web.toolkit.server.Service;
import org.bonitasoft.web.toolkit.server.ServiceFactory;
import org.bonitasoft.web.toolkit.server.ServiceNotFoundException;

public class ConsoleServiceFactory implements ServiceFactory {

    @Override
    public Service getService(final String calledToolToken) {
        if (OrganizationImportService.TOKEN.equals(calledToolToken)) {
            return new OrganizationImportService();
        } else if (ProcessActorImportService.TOKEN.equals(calledToolToken)) {
            return new ProcessActorImportService();
        } else if (ApplicationsImportService.TOKEN.equals(calledToolToken)) {
            return new ApplicationsImportService();
        }
        throw new ServiceNotFoundException(calledToolToken);
    }

}
