/**
 * Copyright (C) 2015 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 **/

package org.bonitasoft.web.rest.server.api.bdm;

import org.bonitasoft.engine.api.BusinessDataAPI;
import org.bonitasoft.engine.business.data.BusinessDataReference;
import org.bonitasoft.engine.business.data.MultipleBusinessDataReference;
import org.bonitasoft.engine.business.data.SimpleBusinessDataReference;
import org.bonitasoft.web.rest.server.BonitaRestletApplication;
import org.bonitasoft.web.rest.server.ResourceFinder;
import org.bonitasoft.web.rest.server.api.bpm.flownode.ContextResultElement;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.resource.ServerResource;

import java.io.Serializable;

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
    public Serializable getContextResultElement(Serializable object) {
        String type;
        String value;
        String link;
        if (object instanceof SimpleBusinessDataReference) {
            type = ((SimpleBusinessDataReference) object).getType();
            value = ((SimpleBusinessDataReference) object).getStorageId().toString();
            link =  getUrl(type, value);
        } else if (object instanceof MultipleBusinessDataReference) {
            type = ((MultipleBusinessDataReference) object).getType();
            value = ((MultipleBusinessDataReference) object).getStorageIds().toString();
            link =  getUrl(type, "?q=findByIds&f=ids="+value.replaceAll("[\\[\\] ]",""));
        } else {
            return object;
        }
        return new ContextResultElement(type, value, link);
    }

    private String getUrl(String type, String value) {
        return "API"+ BonitaRestletApplication.BDM_BUSINESS_DATA_URL + "/" + type + "/" + value;
    }
}
