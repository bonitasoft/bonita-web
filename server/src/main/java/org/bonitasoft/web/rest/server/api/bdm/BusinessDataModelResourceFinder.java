package org.bonitasoft.web.rest.server.api.bdm;

import org.bonitasoft.console.common.server.utils.BonitaHomeFolderAccessor;
import org.bonitasoft.engine.api.TenantAdministrationAPI;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.server.ResourceFinder;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.resource.ServerResource;


public class BusinessDataModelResourceFinder extends ResourceFinder {

    @Override
    public ServerResource create(final Request request, final Response response) {
        final APISession apiSession = getAPISession(request);
        TenantAdministrationAPI tenantAdministrationAPI = getTenantAdministrationAPI(request);
        BonitaHomeFolderAccessor bonitaHomeFolderAccessor = new BonitaHomeFolderAccessor();
        return new BusinessDataModelResource(tenantAdministrationAPI, bonitaHomeFolderAccessor, apiSession);
    }
}
