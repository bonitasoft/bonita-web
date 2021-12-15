package org.bonitasoft.web.rest.model.bpm.cases;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public abstract class ArchivedVariable {

    /**
     * Name of the variable in the case
     */
    private String name;
    /**
     * Detailed description of the case variable, as set in the definition at
     * design-time
     */
    private String description;
    /**
     * The value of the archived case variable
     */
    private String value;


    /**
     * The Java type of the variable
     */
    private String type;

    /**
     * The date and time when this variable was archived
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss.SSS")
    private Date archivedDate;

    /**
     * ID of the variable before it was archived
     */
    private String sourceObjectId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getArchivedDate() {
        return archivedDate;
    }

    public void setArchivedDate(Date archivedDate) {
        this.archivedDate = archivedDate;
    }

    public String getSourceObjectId() {
        return sourceObjectId;
    }

    public void setSourceObjectId(String sourceObjectId) {
        this.sourceObjectId = sourceObjectId;
    }


}
