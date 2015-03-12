/*******************************************************************************
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.web.rest.server.api.bdm;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bonitasoft.engine.api.BusinessDataAPI;
import org.bonitasoft.engine.business.data.BusinessDataReference;
import org.bonitasoft.web.rest.server.api.resource.CommonResource;
import org.bonitasoft.web.rest.server.framework.APIServletCall;
import org.restlet.resource.Get;

/**
 * @author Matthieu Chaffotte
 * @author Colin Puy
 */
public class BusinessDataReferencesResource extends CommonResource {

    private final BusinessDataAPI bdmAPI;

    public BusinessDataReferencesResource(final BusinessDataAPI bdmAPI) {
        this.bdmAPI = bdmAPI;
    }

    @Get("json")
    public List<BusinessDataReference> getProcessBusinessDataReferences() {
        final Long processInstanceId = getCaseId();
        final Integer p = getSearchPageNumber();
        final Integer c = getSearchPageSize();
        return bdmAPI.getProcessBusinessDataReferences(processInstanceId, p * c, c);
    }

    private Long getCaseId() {
        final String[] values = getQuery().getValuesArray(APIServletCall.PARAMETER_FILTER);
        final Map<String, String> filters = parseFilters(Arrays.asList(values));
        final String caseId = filters.get("caseId");
        if (caseId == null) {
            throw new IllegalArgumentException("filter caseId is mandatory");
        }
        try {
            return Long.parseLong(caseId);
        } catch (final NumberFormatException e) {
            throw new IllegalArgumentException("filter caseId should be a number");
        }
    }

}
