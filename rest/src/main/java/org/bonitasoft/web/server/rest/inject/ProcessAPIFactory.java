package org.bonitasoft.web.server.rest.inject;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.session.APISession;
import org.glassfish.hk2.api.Factory;

public class ProcessAPIFactory implements Factory<ProcessAPI> {

    private HttpServletRequest httpRequest;
    
    @Inject
    public ProcessAPIFactory(HttpServletRequest httpRequest) {
        this.httpRequest = httpRequest;
    }
    
    @Override
    public void dispose(ProcessAPI processAPI) {
        
    }

    @Override
    public ProcessAPI provide() {
        APISession apiSession = (APISession) httpRequest.getSession().getAttribute("apiSession");
        try {
            return TenantAPIAccessor.getProcessAPI(apiSession);
        } catch (Exception e) {
            throw new WebApplicationException(e, 500);
        }
    }
}
