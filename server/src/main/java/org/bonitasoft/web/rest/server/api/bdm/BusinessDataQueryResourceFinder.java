package org.bonitasoft.web.rest.server.api.bdm;

import org.bonitasoft.web.rest.server.ResourceFinder;
import org.bonitasoft.web.rest.server.api.bdm.BusinessDataQueryResource;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.resource.ServerResource;

/**
* Created by fabiolombardi on 09/04/2015.
*/
public class BusinessDataQueryResourceFinder extends ResourceFinder {

    @Override
    public ServerResource create(final Request request, final Response response) {
        return new BusinessDataQueryResource(getCommandAPI(request));
    }
}
