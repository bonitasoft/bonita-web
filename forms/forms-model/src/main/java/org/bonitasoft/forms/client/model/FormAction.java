/**
 * Copyright (C) 2009 BonitaSoft S.A.
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

/**
 * An action to execute at form submission
 *
 * @author Anthony Birembaut
 */
public class FormAction implements Serializable {

    /**
     * UID
     */
    private static final long serialVersionUID = 1265444634263925221L;

    /**
     * type of action
     */
    private ActionType type;

    /**
     * Operation operator
     */
    private String operator;

    /**
     * Operation operator input type (for java methods)
     */
    private String operatorInputType;

    /**
     * Data Name
     */
    private String variableName;

    /**
     * expression
     */
    private Expression expression;

    /**
     * if not null, the action has to be executed only if the button with the corresponding ID has been pressed
     */
    private String submitButtonId;

    /**
     * connectors to execute for EXECUTE_CONNECTOR actions
     */
    private Connector connector;

    /**
     * Variable Type
     */
    private String variableType;

    /**
     * condition expression
     */
    private Expression conditionExpression;

    /**
     * Constructor.
     *
     * @param type
     * @param dataName
     * @param operator
     * @param expression
     */
    public FormAction(final ActionType type, final String dataName, final String variableType, final String operator,
            final String operatorInputType, final Expression expression) {
        this(type, dataName, variableType, operator, operatorInputType, expression, null, null);
    }

    /**
     * Constructor.
     *
     * @param type
     * @param variableName
     * @param variableType
     * @param operator
     * @param operatorInputType
     * @param expression
     * @param submitButtonId
     * @param conditionExpression
     */
    public FormAction(final ActionType type, final String variableName, final String variableType, final String operator,
            final String operatorInputType, final Expression expression, final String submitButtonId, final Expression conditionExpression) {
        this.type = type;
        this.variableName = variableName;
        this.variableType = variableType;
        this.operator = operator;
        this.operatorInputType = operatorInputType;
        this.expression = expression;
        this.submitButtonId = submitButtonId;
        this.conditionExpression = conditionExpression;
    }

    /**
     * Default constructor
     */
    public FormAction() {
        super();
       // Mandatory for serialization
    }

    public ActionType getType() {
        return type;
    }

    public void setType(final ActionType type) {
        this.type = type;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(final String dataName) {
        variableName = dataName;
    }

    public String getVariableType() {
        return variableType;
    }

    public void setVariableType(final String variableType) {
        this.variableType = variableType;
    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(final Expression expression) {
        this.expression = expression;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(final String operator) {
        this.operator = operator;
    }

    public String getSubmitButtonId() {
        return submitButtonId;
    }

    public void setSubmitButtonId(final String submitButtonId) {
        this.submitButtonId = submitButtonId;
    }

    public Connector getConnector() {
        return connector;
    }

    public void setConnector(final Connector connector) {
        this.connector = connector;
    }

    public String getOperatorInputType() {
        return operatorInputType;
    }

    public void setOperatorInputType(final String operatorInputType) {
        this.operatorInputType = operatorInputType;
    }

    public Expression getConditionExpression() {
        return conditionExpression;
    }

    public void setConditionExpression(final Expression conditionExpression) {
        this.conditionExpression = conditionExpression;
    }
}
