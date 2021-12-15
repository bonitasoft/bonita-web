package org.bonitasoft.web.rest.server.api.bpm.cases;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.data.ArchivedDataInstance;
import org.bonitasoft.web.rest.model.bpm.cases.ArchivedCaseVariable;
import org.bonitasoft.web.rest.model.bpm.cases.CaseVariableItem;
import org.bonitasoft.web.rest.server.api.resource.CommonResource;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;

public class ArchivedCaseVariablesResource extends CommonResource {

    private APICaseVariableAttributeChecker attributeChecker = new APICaseVariableAttributeChecker();
    private final ProcessAPI processAPI;

    private List<ArchivedDataInstance> result;

    private int searchPageNumber;

    private int searchPageSize;

    public ArchivedCaseVariablesResource(final ProcessAPI processAPI) {
        this.processAPI = processAPI;
    }

    @Get("json")
    public List<ArchivedCaseVariable> getArchivedCaseVariables() {
        searchPageNumber = getSearchPageNumber();
        searchPageSize = getSearchPageSize();
        Map<String, String> searchFilters = getSearchFilters();
        attributeChecker.checkSearchFilters(searchFilters);
        long caseId = Long.parseLong(searchFilters.get(CaseVariableItem.ATTRIBUTE_CASE_ID));
        result = processAPI.getArchivedProcessDataInstances(caseId, 0, Integer.MAX_VALUE);
        return result.stream().skip((searchPageNumber * searchPageSize)).limit(searchPageSize)
                .map(ArchivedCaseVariable::create).collect(Collectors.toList());
    }

    @Override
    public Representation handle() {
        Representation representation = super.handle();
        setContentRange(searchPageNumber, searchPageSize, result.size());
        return representation;
    }

}
