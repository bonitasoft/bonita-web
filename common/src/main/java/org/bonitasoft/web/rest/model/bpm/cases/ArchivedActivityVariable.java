package org.bonitasoft.web.rest.model.bpm.cases;

import java.io.Serializable;

import org.bonitasoft.engine.bpm.data.ArchivedDataInstance;

public class ArchivedActivityVariable extends ArchivedVariable {

    /**
     * ID of the container this variable belongs to
     */
    private String containerId;
    
    /**
     * Type of the container this variable belongs to
     */
    private String containerType;

    public String getContainerType() {
        return containerType;
    }

    public void setContainerType(String containerType) {
        this.containerType = containerType;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public static ArchivedActivityVariable create(ArchivedDataInstance archivedProcessDataInstance) {
        var instance = new ArchivedActivityVariable();
        instance.setName(archivedProcessDataInstance.getName());
        instance.setContainerId(String.valueOf(archivedProcessDataInstance.getContainerId()));
        instance.setDescription(archivedProcessDataInstance.getDescription());
        instance.setType(archivedProcessDataInstance.getClassName());
        instance.setContainerType(archivedProcessDataInstance.getContainerType());
        Serializable value = archivedProcessDataInstance.getValue();
        instance.setValue(value == null ? null : String.valueOf(value));
        instance.setArchivedDate(archivedProcessDataInstance.getArchiveDate());
        archivedProcessDataInstance.getContainerType();
        instance.setSourceObjectId(String.valueOf(archivedProcessDataInstance.getSourceObjectId()));
        return instance;
    }

}
