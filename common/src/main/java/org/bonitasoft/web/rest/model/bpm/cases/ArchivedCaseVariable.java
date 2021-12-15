package org.bonitasoft.web.rest.model.bpm.cases;

import java.io.Serializable;

import org.bonitasoft.engine.bpm.data.ArchivedDataInstance;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ArchivedCaseVariable extends ArchivedVariable {

    /**
     * ID of the case this variable belongs to
     */
    @JsonProperty(value = "case_id")
    private String caseId;

    public String getCaseId() {
        return caseId;
    }


    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }


    public static ArchivedCaseVariable create(ArchivedDataInstance archivedProcessDataInstance) {
        var instance = new ArchivedCaseVariable();
        instance.setName(archivedProcessDataInstance.getName());
        instance.setCaseId(String.valueOf(archivedProcessDataInstance.getContainerId()));
        instance.setDescription(archivedProcessDataInstance.getDescription());
        instance.setType(archivedProcessDataInstance.getClassName());
        Serializable value = archivedProcessDataInstance.getValue();
        instance.setValue(value == null ? null : String.valueOf(value));
        instance.setArchivedDate(archivedProcessDataInstance.getArchiveDate());
        instance.setSourceObjectId(String.valueOf(archivedProcessDataInstance.getSourceObjectId()));
        return instance;
    }


}
