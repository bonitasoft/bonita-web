/*******************************************************************************
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.web.rest.server.api.bdm;

import org.bonitasoft.engine.api.BusinessDataAPI;
import org.bonitasoft.engine.bpm.data.DataNotFoundException;
import org.bonitasoft.engine.business.data.BusinessDataReference;
import org.bonitasoft.web.rest.server.api.resource.CommonResource;
import org.restlet.data.Status;
import org.restlet.resource.Get;

/**
 * @author Matthieu Chaffotte
 */
public class BusinessDataReferenceResource extends CommonResource {

    private final BusinessDataAPI bdmAPI;

    public BusinessDataReferenceResource(final BusinessDataAPI bdmAPI) {
        this.bdmAPI = bdmAPI;
    }

    @Get("json")
    public BusinessDataReference getProcessBusinessDataReference() throws DataNotFoundException {
        final String businessDataName = getPathParam("dataName");
        final Long processInstanceId = getPathParamAsLong("caseId");
        return bdmAPI.getProcessBusinessDataReference(businessDataName, processInstanceId);
    }

    @Override
    protected void doCatch(final Throwable throwable) {
        super.doCatch(throwable);
        if (throwable.getCause() instanceof DataNotFoundException) {
            getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
        }
    }
}
