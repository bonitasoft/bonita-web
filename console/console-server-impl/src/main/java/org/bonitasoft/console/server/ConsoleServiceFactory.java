package org.bonitasoft.console.server;

import org.bonitasoft.console.server.service.OrganizationImportService;
import org.bonitasoft.console.server.service.ProcessActorImportService;
import org.bonitasoft.web.toolkit.client.common.exception.service.ServiceNotFoundException;
import org.bonitasoft.web.toolkit.server.Service;
import org.bonitasoft.web.toolkit.server.ServiceFactory;

public class ConsoleServiceFactory implements ServiceFactory {

	@Override
	public Service getService(String calledToolToken) {
		if (OrganizationImportService.TOKEN.equals(calledToolToken)) {
            return new OrganizationImportService();
        } else if (ProcessActorImportService.TOKEN.equals(calledToolToken)) {
            return new ProcessActorImportService();
        }
		throw new ServiceNotFoundException(calledToolToken);
	}

}
