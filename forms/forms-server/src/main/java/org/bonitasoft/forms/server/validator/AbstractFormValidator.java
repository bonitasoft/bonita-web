/**
 * Copyright (C) 2010 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.forms.server.validator;

import java.io.Serializable;
import java.util.Map;

import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.forms.client.model.Expression;

/**
 * Abstract class for field and page validators
 * 
 * @author Anthony Birembaut
 *
 */
public abstract class AbstractFormValidator {
    
    public static final String CLICKED_BUTTON_VARNAME = "clickedButton";
    
    private long processDefinitionID;
    
    private long processInstanceID;
    
    private long activityInstanceID;
    
    private APISession session;
    
    private Expression parameter;
    
    private String submitButtonId;
    
    private Map<String, Serializable> transientDataContext;

    public long getProcessDefinitionID() {
        return processDefinitionID;
    }

    public long getProcessInstanceID() {
        return processInstanceID;
    }

    public long getActivityInstanceID() {
        return activityInstanceID;
    }

	public Expression getParameter() {
		return parameter;
	}

    public void setProcessDefinitionID(final long processDefinitionID) {
        this.processDefinitionID = processDefinitionID;
    }

    public void setProcessInstanceID(final long processInstanceID) {
        this.processInstanceID = processInstanceID;
    }

    public void setActivityInstanceID(final long activityInstanceID) {
        this.activityInstanceID = activityInstanceID;
    }

	public void setParameter(final Expression parameter) {
		this.parameter = parameter;
	}

	public Map<String, Serializable> getTransientDataContext() {
		return transientDataContext;
	}

	public void setTransientDataContext(final Map<String, Serializable> transientDataContext) {
		this.transientDataContext = transientDataContext;
	}

    public void setSubmitButtonId(String submitButtonId) {
        this.submitButtonId = submitButtonId;
    }

    public String getSubmitButtonId() {
        return submitButtonId;
    }
    
    public APISession getSession() {
        return session;
    }
    
    public void setSession(APISession session) {
        this.session = session;
    }
}
