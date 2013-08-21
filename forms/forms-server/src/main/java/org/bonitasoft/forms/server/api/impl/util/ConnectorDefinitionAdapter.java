/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.forms.server.api.impl.util;

import org.bonitasoft.engine.bpm.connector.ConnectorDefinition;
import org.bonitasoft.engine.bpm.connector.ConnectorEvent;
import org.bonitasoft.engine.bpm.connector.FailAction;
import org.bonitasoft.engine.expression.Expression;
import org.bonitasoft.engine.operation.Operation;
import org.bonitasoft.forms.client.model.Connector;
import org.bonitasoft.forms.client.model.FormAction;

import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Ruiheng Fan
 * 
 */
public class ConnectorDefinitionAdapter implements ConnectorDefinition {

    private static Logger LOGGER = Logger.getLogger(ConnectorDefinitionAdapter.class.getName());

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1080530426137044382L;

    private Connector connector = null;

    public ConnectorDefinitionAdapter(final Connector connector) {
        this.connector = connector;
    }

    /*
     * (non-Javadoc)
     * @see org.bonitasoft.engine.bpm.model.NamedElement#getName()
     */
    @Override
    public String getName() {
        return this.connector.getConnectorName();
    }

    /*
     * (non-Javadoc)
     * @see org.bonitasoft.engine.bpm.model.ConnectorDefinition#getConnectorId()
     */
    @Override
    public String getConnectorId() {
        return this.connector.getConnectorId();
    }

    /*
     * (non-Javadoc)
     * @see org.bonitasoft.engine.bpm.model.ConnectorDefinition#getVersion()
     */
    @Override
    public String getVersion() {
        return this.connector.getConnectorVersion();
    }

    /*
     * (non-Javadoc)
     * @see org.bonitasoft.engine.bpm.model.ConnectorDefinition#getActivationEvent()
     */
    @Override
    public ConnectorEvent getActivationEvent() {
        LOGGER.log(Level.WARNING, "getActivationEvent() in ConnectorDefinitionAdapter is not supported yet.");
        return null;
    }

    /*
     * (non-Javadoc)
     * @see org.bonitasoft.engine.bpm.model.ConnectorDefinition#getInputs()
     */
    @Override
    public Map<String, Expression> getInputs() {
        final Map<String, Expression> inputMap = new HashMap<String, Expression>();
        final Map<String, org.bonitasoft.forms.client.model.Expression> inputParameters = this.connector.getInputParameters();
        final Iterator<Entry<String, org.bonitasoft.forms.client.model.Expression>> it = inputParameters.entrySet().iterator();
        final ExpressionAdapter adapter = new ExpressionAdapter();
        while (it.hasNext()) {
            final Entry<String, org.bonitasoft.forms.client.model.Expression> entry = it.next();
            inputMap.put(entry.getKey(), adapter.getEngineExpression(entry.getValue()));
        }
        return inputMap;
    }

    /*
     * (non-Javadoc)
     * @see org.bonitasoft.engine.bpm.model.ConnectorDefinition#getOutputs()
     */
    @Override
    public List<Operation> getOutputs() {
        final List<FormAction> actions = this.connector.getOutputOperations();
        final FormActionAdapter adapter = new FormActionAdapter();
        final List<Operation> operations = new ArrayList<Operation>();
        for (final FormAction action : actions) {
            operations.add(adapter.getEngineOperation(action));
        }
        return operations;
    }

    @Override
    public String getErrorCode() {
        return null;
    }

    @Override
    public FailAction getFailAction() {
        if (this.connector.isThrowingException()) {
            return FailAction.FAIL;
        } else {
            return FailAction.IGNORE;
        }
    }

}
