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
package org.bonitasoft.forms.client.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author Anthony Birembaut
 *
 */
public class Connector implements Serializable {

    /** 
     * UID 
     */
    private static final long serialVersionUID = -4190294573271008139L;

    /**
     * The connector's input parameters
     */
    private Map<String, Expression> inputParameters;
    
    /**
     * The connector's output operations
     */
    private List<FormAction> outputOperations;

    /**
     * indicates if any exception occuring should be thrown or ignored
     */
    private boolean throwingException = true;

    private String connectorId;
    
    private String connectorName;
    
    private String connectorVersion;
    
    /**
     * Connector
     * @param connectorId
     * @param connectorName
     * @param connectorVersion
     * @param connectorEvent
     * @param throwingException
     */
    public Connector(final String connectorId, final String connectorName, final String connectorVersion, final boolean throwingException) {
        super();
        this.connectorId = connectorId;
        this.connectorName = connectorName;
        this.connectorVersion = connectorVersion;
        this.throwingException = throwingException;
    }
    
    /**
     * Default Constructor
     */
    public Connector() {
        super();
        // Mandatory for serialization
    }
    
    public boolean isThrowingException() {
        return throwingException;
    }

    public void setThrowingException(final boolean throwingException) {
        this.throwingException = throwingException;
    }

    public Map<String, Expression> getInputParameters() {
        return inputParameters;
    }

    public void setInputParameters(final Map<String, Expression> inputParameters) {
        this.inputParameters = inputParameters;
    }

    public List<FormAction> getOutputOperations() {
        return outputOperations;
    }

    public void setOutputOperations(final List<FormAction> outputOperations) {
        this.outputOperations = outputOperations;
    }

    /**
     * @return the connectorId
     */
    public String getConnectorId() {
        return connectorId;
    }
    
    /**
     * @return the connectorVersion
     */
    public String getConnectorVersion() {
        return connectorVersion;
    }
    
    /**
     * @return the connectorName
     */
    public String getConnectorName() {
        return connectorName;
    }
}
