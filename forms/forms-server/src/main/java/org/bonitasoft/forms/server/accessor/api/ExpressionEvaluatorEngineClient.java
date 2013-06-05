/**
 * Copyright (C) 2012 BonitaSoft S.A.
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
package org.bonitasoft.forms.server.accessor.api;

import java.io.Serializable;
import java.util.Map;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.ProcessRuntimeAPI;
import org.bonitasoft.engine.expression.Expression;
import org.bonitasoft.forms.server.exception.BPMEngineException;

/**
 * @author Colin PUY
 * 
 */
public class ExpressionEvaluatorEngineClient {

    private ProcessAPI processApi;

    public ExpressionEvaluatorEngineClient(ProcessAPI processApi) {
        this.processApi = processApi;
    }

    public Map<String, Serializable> evaluateExpressionsOnActivityInstance(long activityInstanceID,
            Map<Expression, Map<String, Serializable>> expressionsWithContext) throws BPMEngineException {
        try {
            return getProcessAPI().evaluateExpressionsOnActivityInstance(activityInstanceID, expressionsWithContext);
        } catch (Exception e) {
            throw new BPMEngineException("Error when evaluating expressions on activity instance " + activityInstanceID, e);
        }
    }

    public Map<String, Serializable> evaluateExpressionsOnProcessInstance(long processInstanceID, Map<Expression, Map<String, Serializable>> expressions)
            throws BPMEngineException {
        try {
            return getProcessAPI().evaluateExpressionsOnProcessInstance(processInstanceID, expressions);
        } catch (Exception e) {
            throw new BPMEngineException("Error when evaluating expressions on process instance " + processInstanceID, e);
        }
    }

    public Map<String, Serializable> evaluateExpressionsOnProcessDefinition(long processDefinitionID,
            Map<Expression, Map<String, Serializable>> expressions) throws BPMEngineException {
        try {
            return getProcessAPI().evaluateExpressionsOnProcessDefinition(processDefinitionID, expressions);
        } catch (Exception e) {
            throw new BPMEngineException("Error when evaluating expressions on process definition " + processDefinitionID, e);
        }
    }

    public Map<String, Serializable> evaluateExpressionsOnCompletedActivityInstance(long activityInstanceID,
            Map<Expression, Map<String, Serializable>> expressions) throws BPMEngineException {
        try {
            return getProcessAPI().evaluateExpressionsOnCompletedActivityInstance(activityInstanceID, expressions);
        } catch (Exception e) {
            throw new BPMEngineException("Error when evaluating expressions on completed activity instance " + activityInstanceID, e);
        }
    }

    public Map<String, Serializable> evaluateExpressionsOnCompletedProcessInstance(long processInstanceID,
            Map<Expression, Map<String, Serializable>> expressions) throws BPMEngineException {
        try {
            return getProcessAPI().evaluateExpressionOnCompletedProcessInstance(processInstanceID, expressions);
        } catch (Exception e) {
            throw new BPMEngineException("Error when evaluating expressions on completed process instance " + processInstanceID, e);
        }
    }

    public Map<String, Serializable> evaluateExpressionsAtProcessInstanciation(long processInstanceID,
            Map<Expression, Map<String, Serializable>> expressions) throws BPMEngineException {
        try {
            return getProcessAPI().evaluateExpressionsAtProcessInstanciation(processInstanceID, expressions);
        } catch (Exception e) {
            throw new BPMEngineException("Error when evaluating expressions on completed process instance " + processInstanceID, e);
        }
    }

    private ProcessRuntimeAPI getProcessAPI() {
        return processApi;
    }
}
