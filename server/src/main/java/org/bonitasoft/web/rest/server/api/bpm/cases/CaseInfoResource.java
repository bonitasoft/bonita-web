package org.bonitasoft.web.rest.server.api.bpm.cases;

import java.util.Map;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.data.DataNotFoundException;
import org.bonitasoft.web.rest.model.bpm.cases.CaseInfo;
import org.bonitasoft.web.rest.server.api.resource.CommonResource;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.restlet.resource.Get;

public class CaseInfoResource extends CommonResource {

    public static final String CASE_ID = "caseId";

    private final ProcessAPI processAPI;

    public CaseInfoResource(final ProcessAPI processAPI) {
        this.processAPI = processAPI;
    }

    @Get("json")
    public CaseInfo getCaseInfo() {
        try {
            final long caseId = Long.parseLong(getAttribute(CASE_ID));
            final CaseInfo caseInfo = new CaseInfo();
            caseInfo.setId(caseId);
            caseInfo.setFlowNodeStatesCounters(getFlownodeCounters(caseId));
            return caseInfo;
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }


    protected Map<String, Map<String, Long>> getFlownodeCounters(final Long caseId) throws DataNotFoundException {
        return processAPI.getFlownodeStateCounters(caseId);
    }
}
