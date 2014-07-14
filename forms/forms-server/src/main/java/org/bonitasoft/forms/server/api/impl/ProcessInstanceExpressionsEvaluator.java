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
package org.bonitasoft.forms.server.api.impl;

import java.io.Serializable;
import java.util.Map;

import org.bonitasoft.console.common.server.utils.BPMEngineException;
import org.bonitasoft.console.common.server.utils.BPMExpressionEvaluationException;
import org.bonitasoft.engine.expression.Expression;
import org.bonitasoft.forms.server.accessor.api.ExpressionEvaluatorEngineClient;
import org.bonitasoft.forms.server.accessor.api.utils.ProcessInstanceAccessor;

/**
 * @author Vincent Elcrin
 * 
 */
public class ProcessInstanceExpressionsEvaluator {

    private ExpressionEvaluatorEngineClient engineEvaluator;

    public ProcessInstanceExpressionsEvaluator(ExpressionEvaluatorEngineClient engineEvaluator) {
        this.engineEvaluator = engineEvaluator;

    }

    public Map<String, Serializable> evaluate(ProcessInstanceAccessor instance,
            final Map<Expression, Map<String, Serializable>> expressions,
            boolean atProcessInstanciation)

            throws BPMEngineException, BPMExpressionEvaluationException {

        if (atProcessInstanciation) {
            return evaluateExpressionsAtProcessInstanciation(instance.getId(), expressions);
        } else if (instance.isArchived()) {
            return evaluateExpressionsOnCompleted(instance.getId(), expressions);
        } else {
            return evaluateExpressionsOnProcessInstance(instance.getId(), expressions);
        }
    }

    private Map<String, Serializable> evaluateExpressionsAtProcessInstanciation(final long processInstanceID,
            final Map<Expression, Map<String, Serializable>> expressions)
            throws BPMEngineException, BPMExpressionEvaluationException {
        return engineEvaluator.evaluateExpressionsAtProcessInstanciation(processInstanceID, expressions);
    }

    private Map<String, Serializable> evaluateExpressionsOnCompleted(final long processInstanceID,
            final Map<Expression, Map<String, Serializable>> expressions) throws BPMEngineException, BPMExpressionEvaluationException {
        return engineEvaluator.evaluateExpressionsOnCompletedProcessInstance(processInstanceID,
                expressions);
    }

    private Map<String, Serializable> evaluateExpressionsOnProcessInstance(final long processInstanceID,
            final Map<Expression, Map<String, Serializable>> expressions) throws BPMEngineException, BPMExpressionEvaluationException {
        return engineEvaluator.evaluateExpressionsOnProcessInstance(processInstanceID,
                expressions);
    }
}
