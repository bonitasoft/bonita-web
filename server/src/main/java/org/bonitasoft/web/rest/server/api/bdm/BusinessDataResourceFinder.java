package org.bonitasoft.web.rest.server.api.bdm;

import org.bonitasoft.engine.api.CommandAPI;
import org.bonitasoft.web.rest.server.ResourceFinder;
import org.bonitasoft.web.rest.server.api.bdm.BusinessDataResource;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.resource.ServerResource;

/**
* Created by fabiolombardi on 09/04/2015.
*/
public class BusinessDataResourceFinder extends ResourceFinder {

    @Override
    public ServerResource create(final Request request, final Response response) {
        final CommandAPI commandAPI = getCommandAPI(request);
        return new BusinessDataResource(commandAPI);
    }
}
