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
package org.bonitasoft.forms.server.api.impl.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bonitasoft.engine.expression.Expression;
import org.bonitasoft.engine.expression.ExpressionBuilder;
import org.bonitasoft.engine.expression.InvalidExpressionException;

/**
 * @author Ruiheng Fan, Anthony Birembaut
 * 
 */
public class ExpressionAdapter implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -6070716477279432026L;

    /**
     * Logger
     */
    private static Logger LOGGER = Logger.getLogger(ExpressionAdapter.class.getName());

    public ExpressionAdapter() {
        super();
    }

    public Expression getEngineExpression(final org.bonitasoft.forms.client.model.Expression expression) {
        final ExpressionBuilder expressionBuilder = new ExpressionBuilder();

        try {
            if (expression == null) {
                return null;
            }
            String name;
            if (expression.getName() == null) {
                name = UUID.randomUUID().toString();
            } else {
                name = expression.getName();
            }
            return expressionBuilder.createNewInstance(name).setContent(expression.getContent()).setExpressionType(expression.getExpressionType())
                    .setReturnType(expression.getReturnType()).setInterpreter(expression.getInterpreter())
                    .setDependencies(getDependencies(expression.getDependencies())).done();
        } catch (InvalidExpressionException e) {
            final StringBuilder messageBuilder = new StringBuilder("The expression to evaluate is invalid or not supported.\n");
            messageBuilder.append("Expression type: ");
            messageBuilder.append(expression.getExpressionType());
            messageBuilder.append("\n");
            messageBuilder.append("Return type: ");
            messageBuilder.append(expression.getReturnType());
            messageBuilder.append("\n");
            messageBuilder.append("Interpreter: ");
            messageBuilder.append(expression.getInterpreter());
            final String message = messageBuilder.toString();
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, message);
            }
            throw new RuntimeException(message, e);
        }
    }

    protected List<Expression> getDependencies(final List<org.bonitasoft.forms.client.model.Expression> dependencies) {
        List<Expression> expressions = null;
        if (dependencies != null) {
            expressions = new ArrayList<Expression>();
            for (final org.bonitasoft.forms.client.model.Expression e : dependencies) {
                expressions.add(getEngineExpression(e));
            }
        }
        return expressions;
    }

}
