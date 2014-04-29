/*******************************************************************************
 * Copyright (C) 2009, 2013 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.web.rest.server.api.bpm.cases;

import java.util.List;
import java.util.Map;

import org.bonitasoft.web.rest.model.bpm.cases.CaseVariableDefinition;
import org.bonitasoft.web.rest.model.bpm.cases.CaseVariableItem;
import org.bonitasoft.web.rest.server.api.ConsoleAPI;
import org.bonitasoft.web.rest.server.datastore.bpm.cases.CaseVariableDatastore;
import org.bonitasoft.web.rest.server.framework.api.APIHasGet;
import org.bonitasoft.web.rest.server.framework.api.APIHasSearch;
import org.bonitasoft.web.rest.server.framework.api.APIHasUpdate;
import org.bonitasoft.web.rest.server.framework.api.Datastore;
import org.bonitasoft.web.rest.server.framework.search.ItemSearchResult;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

/**
 * @author Colin PUY
 * 
 */
public class APICaseVariable extends ConsoleAPI<CaseVariableItem> implements APIHasSearch<CaseVariableItem>,
        APIHasUpdate<CaseVariableItem>, APIHasGet<CaseVariableItem> {

    private APICaseVariableAttributeChecker attributeChecker = new APICaseVariableAttributeChecker();

    @Override
    public CaseVariableItem runUpdate(APIID id, Map<String, String> attributes) {
        attributeChecker.checkUpdateAttributes(attributes);
        id.setItemDefinition(CaseVariableDefinition.get());
        CaseVariableItem item = CaseVariableItem.fromIdAndAttributes(id, attributes);
        ((CaseVariableDatastore) getDefaultDatastore()).updateVariableValue(item.getCaseId(), item.getName(), item.getType(), item.getValue());
        return item;
    }

    @Override
    public ItemSearchResult<CaseVariableItem> runSearch(int page, int resultsByPage, String search, String orders,
            Map<String, String> filters, List<String> deploys, List<String> counters) {
        attributeChecker.checkSearchFilters(filters);
        long caseId = Long.valueOf(filters.get(CaseVariableItem.ATTRIBUTE_CASE_ID));
        return ((CaseVariableDatastore) getDefaultDatastore()).findByCaseId(caseId, page, resultsByPage);
    }

    @Override
    public CaseVariableItem runGet(APIID id, List<String> deploys, List<String> counters) {
        id.setItemDefinition(CaseVariableDefinition.get());
        long caseId = id.getPartAsLong(CaseVariableItem.ATTRIBUTE_CASE_ID);
        String variableName = id.getPart(CaseVariableItem.ATTRIBUTE_NAME);
        return ((CaseVariableDatastore) getDefaultDatastore()).findById(caseId, variableName);
    }

    @Override
    protected Datastore defineDefaultDatastore() {
        return new CaseVariableDatastore(getEngineSession());
    }

    @Override
    protected ItemDefinition defineItemDefinition() {
        return CaseVariableDefinition.get();
    }

    /*
     * Only used for tests because we cannot pass anything to constructor due to design restrictions
     */
    public void setAttributeChecker(APICaseVariableAttributeChecker attributeChecker) {
        this.attributeChecker = attributeChecker;
    }
}
