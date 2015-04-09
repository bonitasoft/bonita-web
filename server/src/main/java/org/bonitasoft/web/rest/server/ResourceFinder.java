package org.bonitasoft.web.rest.server;

import org.bonitasoft.engine.api.BusinessDataAPI;
import org.bonitasoft.engine.api.CommandAPI;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.ProcessConfigurationAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.restlet.Request;
import org.restlet.ext.servlet.ServletUtils;
import org.restlet.resource.Finder;

import javax.servlet.http.HttpSession;
import java.io.Serializable;

/**
* Created by fabiolombardi on 09/04/2015.
*/
public abstract class ResourceFinder extends Finder {


    private ResourceHandler resourceHandler;

    public boolean handlesResource(Serializable object){
        return false;
    }


    protected CommandAPI getCommandAPI(final Request request) {
        final APISession apiSession = getAPISession(request);
        try {
            return TenantAPIAccessor.getCommandAPI(apiSession);
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    protected ProcessAPI getProcessAPI(final Request request) {
        final APISession apiSession = getAPISession(request);
        try {
            return TenantAPIAccessor.getProcessAPI(apiSession);
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    protected ProcessConfigurationAPI getProcessConfigurationAPI(final Request request) {
        final APISession apiSession = getAPISession(request);
        try {
            return TenantAPIAccessor.getProcessConfigurationAPI(apiSession);
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    protected BusinessDataAPI getBdmAPI(final Request request) {
        final APISession apiSession = getAPISession(request);
        try {
            return TenantAPIAccessor.getBusinessDataAPI(apiSession);
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    protected APISession getAPISession(final Request request) {
        final HttpSession httpSession = ServletUtils.getRequest(request).getSession();
        return (APISession) httpSession.getAttribute("apiSession");
    }

    public void setResourceHandler(ResourceHandler resourceHandler) {
        this.resourceHandler = resourceHandler;
    }

    public ResourceHandler getResourceHandler() {
        return resourceHandler;
    }

    public Serializable getContextResultElement(Serializable object) {
        return object;
    }
}
