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

import org.bonitasoft.console.common.server.utils.BPMExpressionEvaluationException;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.ProcessRuntimeAPI;
import org.bonitasoft.engine.expression.Expression;
import org.bonitasoft.engine.expression.ExpressionEvaluationException;
import org.bonitasoft.forms.server.accessor.widget.impl.XMLExpressionsUtil;
import org.bonitasoft.forms.server.api.impl.util.FormFieldValuesUtil;

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
            Map<Expression, Map<String, Serializable>> expressionsWithContext) throws BPMExpressionEvaluationException {
        try {
            return getProcessAPI().evaluateExpressionsOnActivityInstance(activityInstanceID, expressionsWithContext);
        } catch (ExpressionEvaluationException e) {
            throw new BPMExpressionEvaluationException("Error when evaluating expressions on activity instance " + activityInstanceID + ". " + buildEvaluationMessageLogDetail(e), e);
        }
    }

    public Map<String, Serializable> evaluateExpressionsOnProcessInstance(long processInstanceID, Map<Expression, Map<String, Serializable>> expressions)
            throws BPMExpressionEvaluationException {
        try {
            return getProcessAPI().evaluateExpressionsOnProcessInstance(processInstanceID, expressions);
        } catch (ExpressionEvaluationException e) {
            throw new BPMExpressionEvaluationException("Error when evaluating expressions on process instance " + processInstanceID + ". " + buildEvaluationMessageLogDetail(e), e);
        }
    }

    public Map<String, Serializable> evaluateExpressionsOnProcessDefinition(long processDefinitionID,
            Map<Expression, Map<String, Serializable>> expressions) throws BPMExpressionEvaluationException {
        try {
            return getProcessAPI().evaluateExpressionsOnProcessDefinition(processDefinitionID, expressions);
        } catch (ExpressionEvaluationException e) {
            throw new BPMExpressionEvaluationException("Error when evaluating expressions on process definition " + processDefinitionID + ". " + buildEvaluationMessageLogDetail(e), e);
        }
    }

    public Map<String, Serializable> evaluateExpressionsOnCompletedActivityInstance(long activityInstanceID,
            Map<Expression, Map<String, Serializable>> expressions) throws BPMExpressionEvaluationException {
        try {
            return getProcessAPI().evaluateExpressionsOnCompletedActivityInstance(activityInstanceID, expressions);
        } catch (ExpressionEvaluationException e) {
            throw new BPMExpressionEvaluationException("Error when evaluating expressions on completed activity instance " + activityInstanceID + ". " + buildEvaluationMessageLogDetail(e), e);
        }
    }

    public Map<String, Serializable> evaluateExpressionsOnCompletedProcessInstance(long processInstanceID,
            Map<Expression, Map<String, Serializable>> expressions) throws BPMExpressionEvaluationException {
        try {
            return getProcessAPI().evaluateExpressionOnCompletedProcessInstance(processInstanceID, expressions);
        } catch (ExpressionEvaluationException e) {
            throw new BPMExpressionEvaluationException("Error when evaluating expressions on completed process instance " + processInstanceID + ". " + buildEvaluationMessageLogDetail(e), e);
        }
    }

    public Map<String, Serializable> evaluateExpressionsAtProcessInstanciation(long processInstanceID,
            Map<Expression, Map<String, Serializable>> expressions) throws BPMExpressionEvaluationException {
        try {
            return getProcessAPI().evaluateExpressionsAtProcessInstanciation(processInstanceID, expressions);
    	}catch (ExpressionEvaluationException e) {
            throw new BPMExpressionEvaluationException("Error when evaluating expressions on completed process instance " + processInstanceID + ". " + buildEvaluationMessageLogDetail(e), e);
        }
    }

    private ProcessRuntimeAPI getProcessAPI() {
        return processApi;
    }
    
    private String buildEvaluationMessageLogDetail(final ExpressionEvaluationException e) {
    	String[] splitExpressionName = null;
    	String expressionParentName = "unknown";
    	String expressionParentAttribute = "unknown";
    	
    	if(e.getExpressionName()!=null){
    		splitExpressionName = e.getExpressionName().split(FormFieldValuesUtil.EXPRESSION_KEY_SEPARATOR);

        	if(splitExpressionName.length==2){
        		expressionParentName = splitExpressionName[0];
        		expressionParentAttribute = splitExpressionName[1];
        		return "Error on expression evaluation for the attribute ["+ expressionParentAttribute +"] of object ["+ expressionParentName +"].";
        	}
    	}
    	
    	return "Error on expression evaluation for the expression with name ["+ e.getExpressionName() +"].";
    }

}
