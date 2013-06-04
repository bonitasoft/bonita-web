/*******************************************************************************
 * Copyright (C) 2009, 2013 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.console.server.datastore.bpm.cases;

import static org.bonitasoft.console.common.server.utils.SearchOptionsBuilderUtil.computeIndex;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bonitasoft.console.client.model.bpm.cases.CaseVariableItem;
import org.bonitasoft.console.common.server.datastore.CommonDatastore;
import org.bonitasoft.console.server.utils.converter.TypeConverter;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.data.DataInstance;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.server.api.DatastoreHasSearch;
import org.bonitasoft.web.toolkit.server.api.DatastoreHasUpdate;
import org.bonitasoft.web.toolkit.server.search.ItemSearchResult;

/**
 * @author Colin PUY
 * 
 */
public class CaseVariableDatastore extends CommonDatastore<CaseVariableItem, DataInstance>
        implements DatastoreHasSearch<CaseVariableItem>, DatastoreHasUpdate<CaseVariableItem> {

    private TypeConverter converter = new TypeConverter();

    public CaseVariableDatastore(APISession engineSession) {
        super(engineSession);
    }

    @Override
    protected CaseVariableItem convertEngineToConsoleItem(DataInstance item) {
        return new CaseVariableItem(item.getContainerId(),
                item.getName(), item.getValue(), item.getClassName(), item.getDescription());
    }

    private List<CaseVariableItem> convert(List<DataInstance> dataInstances) {
        List<CaseVariableItem> caseVariables = new ArrayList<CaseVariableItem>();
        for (DataInstance dataInstance : dataInstances) {
            caseVariables.add(convertEngineToConsoleItem(dataInstance));
        }
        return caseVariables;
    }

    protected ProcessAPI getEngineProcessAPI() {
        try {
            return TenantAPIAccessor.getProcessAPI(getEngineSession());
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    @Override
    public CaseVariableItem update(APIID id, Map<String, String> attributes) {
        throw new RuntimeException("Not implemented / No need to / Not used");
    }

    @Override
    public ItemSearchResult<CaseVariableItem> search(int page, int resultsByPage, String search, String orders, Map<String, String> filters) {
        throw new RuntimeException("Not implemented / No need to / Not used");
    }

    public void updateVariableValue(long caseId, String variableName, String className, String newValue) {
        try {
            Serializable converteValue = converter.convert(className, newValue);
            getEngineProcessAPI().updateProcessDataInstance(variableName, caseId, converteValue);
        } catch (Exception e) {
            throw new APIException("Error when updating case variable", e);
        }
    }

    public ItemSearchResult<CaseVariableItem> findByCaseId(long caseId, int page, int resultsByPage) {
        try {
            List<DataInstance> processDataInstances =
                    getEngineProcessAPI().getProcessDataInstances(caseId, computeIndex(page, resultsByPage), resultsByPage);
            return new ItemSearchResult<CaseVariableItem>(page, resultsByPage,
                    countByCaseId(caseId), convert(processDataInstances));
        } catch (Exception e) {
            throw new APIException("Error when getting case variables");
        }
    }

    private long countByCaseId(long caseId) {
        try {
            return getEngineProcessAPI().getNumberOfProcessDataInstances(caseId);
        } catch (Exception e) {
            throw new APIException("Error while getting the number of case variables");
        }
    }

    public CaseVariableItem findById(long caseId, String variableName) {
        try {
            return convertEngineToConsoleItem(getEngineProcessAPI().getProcessDataInstance(variableName, caseId));
        } catch (Exception e) {
            throw new APIException("Error while getting case variable");
        }
    }
}
