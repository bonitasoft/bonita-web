package org.bonitasoft.web.rest.server.api.form;

import org.bonitasoft.engine.api.ProcessConfigurationAPI;
import org.bonitasoft.web.rest.server.ResourceFinder;
import org.bonitasoft.web.rest.server.api.form.FormMappingResource;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.resource.ServerResource;

/**
* Created by fabiolombardi on 09/04/2015.
*/
public class FormMappingResourceFinder extends ResourceFinder {

    @Override
    public ServerResource create(final Request request, final Response response) {
        final ProcessConfigurationAPI processConfigurationAPI = getProcessConfigurationAPI(request);
        return new FormMappingResource(processConfigurationAPI);
    }
}
