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
 * A Widget's available values
 * 
 * @author Anthony Birembaut
 *
 */
public class FormFieldAvailableValue implements Serializable{

    /**
     * UID
     */
    private static final long serialVersionUID = 7742166987591645938L;

    /**
     * The label of the value as it's displayed
     */
    private Expression labelExpression;
    
    /**
     * the value
     */
    private Expression valueExpression;
    
    /**
     * The reduced version of FormFieldAvailableValue
     */
    private ReducedFormFieldAvailableValue reducedFieldAvailableValue;
    
    /**
     * Constructor
     * @param label
     * @param value
     */
    public FormFieldAvailableValue(final String label, final String value) {
        reducedFieldAvailableValue = new ReducedFormFieldAvailableValue(label, value);
    }

    /**
     * Default constructor
     * Mandatory for serialization
     */
    public FormFieldAvailableValue() {
        super();
        reducedFieldAvailableValue = new ReducedFormFieldAvailableValue();
    }

    public String getLabel() {
        return reducedFieldAvailableValue.getLabel();
    }

    public void setLabel(final String label) {
        reducedFieldAvailableValue.setLabel(label);
    }

    public String getValue() {
        return reducedFieldAvailableValue.getValue();
    }

    public void setValue(final String value) {
        reducedFieldAvailableValue.setValue(value);
    }
    
    public Expression getLabelExpression() {
        return labelExpression;
    }
    
    public void setLabelExpression(Expression labelExpression) {
        this.labelExpression = labelExpression;
    }
    
    public Expression getValueExpression() {
        return valueExpression;
    }

    public void setValueExpression(Expression valueExpression) {
        this.valueExpression = valueExpression;
    }

    public ReducedFormFieldAvailableValue getReducedFieldAvailableValue() {
        return reducedFieldAvailableValue;
    }

    public void setReducedFieldAvailableValue(ReducedFormFieldAvailableValue reducedFieldAvailableValue) {
        this.reducedFieldAvailableValue = reducedFieldAvailableValue;
    }
    
}
