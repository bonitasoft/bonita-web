/**
 * Copyright (C) 2015 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 **/

package org.bonitasoft.web.rest.server;

import org.bonitasoft.engine.api.BusinessDataAPI;
import org.bonitasoft.engine.api.CommandAPI;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.restlet.Request;
import org.restlet.ext.servlet.ServletUtils;
import org.restlet.resource.Finder;

import javax.servlet.http.HttpSession;
import java.io.Serializable;

public abstract class ResourceFinder extends Finder {


    private FinderFactory finderFactory;

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

    public void setFinderFactory(FinderFactory finderFactory) {
        this.finderFactory = finderFactory;
    }

    public FinderFactory getFinderFactory() {
        return finderFactory;
    }

    public Serializable toClientObject(Serializable object) {
        return object;
    }
}
