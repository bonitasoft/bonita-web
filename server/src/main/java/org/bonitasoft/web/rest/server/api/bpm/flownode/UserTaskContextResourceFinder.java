package org.bonitasoft.web.rest.server.api.bpm.flownode;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.web.rest.server.ResourceFinder;
import org.bonitasoft.web.rest.server.api.bpm.flownode.UserTaskContextResource;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.resource.ServerResource;

/**
* Created by fabiolombardi on 09/04/2015.
*/
public class UserTaskContextResourceFinder extends ResourceFinder {

    @Override
    public ServerResource create(final Request request, final Response response) {
        final ProcessAPI processAPI = getProcessAPI(request);
        return new UserTaskContextResource(processAPI, getResourceHandler());
    }
}
