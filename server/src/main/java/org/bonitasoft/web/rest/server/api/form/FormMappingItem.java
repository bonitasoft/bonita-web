package org.bonitasoft.web.rest.server.api.form;

import org.bonitasoft.engine.form.FormMapping;
import org.bonitasoft.engine.form.FormMappingTarget;
import org.bonitasoft.engine.form.FormMappingType;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Fabio Lombardi
 */
public class FormMappingItem implements Serializable {

    private String id;
    private String processDefinitionId;
    private FormMappingType type;
    private FormMappingTarget target;
    private String task;
    private String pageId;
    private String pageURL;
    private String pageMappingKey;
    private String lastUpdatedBy;
    private Date lastUpdateDate;

    public FormMappingItem(FormMapping item) {
        this.id = String.valueOf(item.getId());
        this.processDefinitionId = String.valueOf(item.getProcessDefinitionId());
        this.type = item.getType();
        this.target = item.getTarget();
        this.task = item.getTask();
        this.pageId = null;
        if (item.getPageId() != null) {
            this.pageId = String.valueOf(item.getPageId());
        }
        this.pageURL = item.getURL();
        this.pageMappingKey = item.getPageMappingKey();
        this.lastUpdatedBy = String.valueOf(item.getLastUpdatedBy());
        this.lastUpdateDate = item.getLastUpdateDate();
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public String getPageMappingKey() {
        return pageMappingKey;
    }

    public void setPageMappingKey(String pageMappingKey) {
        this.pageMappingKey = pageMappingKey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getURL() {
        return pageURL;
    }

    public void setPageURL(String pageURL) {
        this.pageURL = pageURL;
    }

    public FormMappingType getType() {
        return type;
    }

    public void setType(FormMappingType type) {
        this.type = type;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public FormMappingTarget getTarget() {
        return target;
    }

    public void setTarget(FormMappingTarget target) {
        this.target = target;
    }

}
