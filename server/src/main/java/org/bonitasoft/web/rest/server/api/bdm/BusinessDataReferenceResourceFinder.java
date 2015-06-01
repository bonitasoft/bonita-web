/**
 * Copyright (C) 2015 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 **/

package org.bonitasoft.web.rest.server.api.bdm;

import java.io.Serializable;

import org.bonitasoft.engine.api.BusinessDataAPI;
import org.bonitasoft.engine.business.data.BusinessDataReference;
import org.bonitasoft.web.rest.server.ResourceFinder;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.resource.ServerResource;

/**
 * @author Baptiste Mesta
 */
public class BusinessDataReferenceResourceFinder extends ResourceFinder {

    @Override
    public ServerResource create(final Request request, final Response response) {
        final BusinessDataAPI bdmAPI = getBdmAPI(request);
        return new BusinessDataReferenceResource(bdmAPI);
    }

    @Override
    public boolean handlesResource(Serializable object) {
        return object instanceof BusinessDataReference;
    }

    @Override
    public Serializable toClientObject(Serializable object) {
        return BusinessDataReferenceResource.toClient((BusinessDataReference) object);
    }

}
