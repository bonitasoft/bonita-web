/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.forms.client.model;

import java.io.Serializable;
import java.util.Map;

/**
 * Object containing the components of an URL
 *
 * @author Anthony Birembaut, Chong Zhao
 */
public class FormURLComponents implements Serializable {

    /**
     * UID
     */
    private static final long serialVersionUID = 5255416377673207717L;

    /**
     * The Application URL
     */
    protected String applicationURL;

    /**
     * true if the application URL is different from the current URL
     */
    protected boolean changeApplication;

    /**
     * Indicates the task id of the next task
     */
    private long taskId;

    /**
     * Indicates the task name of the next task
     */
    protected String taskName;

    /**
     * indicates the process name of the next task
     */
    protected String processName;

    /**
     * indicates the process version of the next task
     */
    protected String processVersion;

    /**
     * the context of additionnal URL parameters
     */
    private Map<String, Object> urlContext;

    /**
     * Default Constructor
     */
    public FormURLComponents() {
        super();
        // Mandatory for serialization
    }

    public String getApplicationURL() {
        return applicationURL;
    }

    public void setApplicationURL(final String applicationURL) {
        this.applicationURL = applicationURL;
    }

    public boolean isChangeApplication() {
        return changeApplication;
    }

    public void setChangeApplication(final boolean changeApplication) {
        this.changeApplication = changeApplication;
    }

    public Map<String, Object> getUrlContext() {
        return urlContext;
    }

    public void setUrlContext(final Map<String, Object> urlContext) {
        this.urlContext = urlContext;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(final String taskName) {
        this.taskName = taskName;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(final long taskId) {
        this.taskId = taskId;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(final String processName) {
        this.processName = processName;
    }

    public String getProcessVersion() {
        return processVersion;
    }

    public void setProcessVersion(final String processVersion) {
        this.processVersion = processVersion;
    }

}
