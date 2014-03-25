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

import java.io.Serializable;

import org.bonitasoft.engine.expression.Expression;
import org.bonitasoft.engine.operation.LeftOperand;
import org.bonitasoft.engine.operation.LeftOperandBuilder;
import org.bonitasoft.engine.operation.Operation;
import org.bonitasoft.engine.operation.OperationBuilder;
import org.bonitasoft.engine.operation.OperatorType;
import org.bonitasoft.forms.client.model.FormAction;

/**
 * @author Ruiheng Fan, Anthony Birembaut
 * 
 */
public class FormActionAdapter implements Serializable {

    /**
     * UUID
     */
    private static final long serialVersionUID = -2689645004770517066L;

    public FormActionAdapter() {
        super();
    }

    public Operation getEngineOperation(final FormAction formAction) {
        final OperationBuilder operationBuilder = new OperationBuilder().createNewInstance();
        final LeftOperandBuilder leftOperandBuilder = new LeftOperandBuilder();
        final LeftOperand leftOperand = leftOperandBuilder.createNewInstance().setName(formAction.getDataName()).setExternal(formAction.isExternal()).done();
        final ExpressionAdapter expressionAdapter = new ExpressionAdapter();
        final Expression expression = expressionAdapter.getEngineExpression(formAction.getExpression());
        final OperatorType operatorType = OperatorType.valueOf(formAction.getType().name());

        operationBuilder.setLeftOperand(leftOperand)
                .setType(operatorType)
                .setRightOperand(expression);

        if (formAction.getOperator() != null) {
            operationBuilder
                    .setOperator(formAction.getOperator())
                    .setOperatorInputType(formAction.getOperatorInputType());
        }
        return operationBuilder.done();
    }

}
