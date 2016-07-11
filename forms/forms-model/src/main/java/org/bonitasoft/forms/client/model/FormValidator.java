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

import org.bonitasoft.forms.client.model.ReducedFormValidator.ValidatorPosition;

/**
 * a form validator
 * 
 * @author Anthony Birembaut
 */
public class FormValidator implements Serializable {
    
    /**
     * UID
     */
    private static final long serialVersionUID = -7688147637501777696L;
    
    /**
     * validator label expression
     */
    private Expression labelExpression;
    
    /**
     * The validator's parameter
     */
    private Expression parameterExpression;
    
    /**
     * The reduced version of the form validator
     */
    private ReducedFormValidator reducedFormValidator;

    /**
     * Constructor
     * @param id validator id
     * @param validatorClass validator className
     * @param style validator label CSS classNames
     */
    public FormValidator(final String id, final String validatorClass, final String style) {
        this.reducedFormValidator = new ReducedFormValidator(id, validatorClass, style);
    }

    /**
     * Default constructor
     * Mandatory for serialization
     */
    public FormValidator() {
        super();
        reducedFormValidator = new ReducedFormValidator();
    }
    
    public String getId() {
        return reducedFormValidator.getId();
    }

    public void setId(final String id) {
        reducedFormValidator.setId(id);
    }

    public String getValidatorClass() {
        return reducedFormValidator.getValidatorClass();
    }

    public void setValidatorClass(final String validatorClass) {
        reducedFormValidator.setValidatorClass(validatorClass);
    }

    public String getLabel() {
        return reducedFormValidator.getLabel();
    }

    public void setLabel(final String label) {
        reducedFormValidator.setLabel(label);
    }
    
    public Expression getLabelExpression() {
        return labelExpression;
    }

    public void setLabelExpression(final Expression labelExpression) {
        this.labelExpression = labelExpression;
    }

    public String getStyle() {
        return reducedFormValidator.getStyle();
    }

    public void setStyle(final String style) {
        reducedFormValidator.setStyle(style);
    }

    public ValidatorPosition getPosition() {
        return reducedFormValidator.getPosition();
    }

    public void setPosition(final ValidatorPosition position) {
        reducedFormValidator.setPosition(position);
    }

	public Expression getParameterExpression() {
		return parameterExpression;
	}

	public void setParameterExpression(final Expression parameterExpression) {
		this.parameterExpression = parameterExpression;
	}

    public ReducedFormValidator getReducedFormValidator() {
        return reducedFormValidator;
    }

    public void setReducedFormValidator(ReducedFormValidator reducedFormValidator) {
        this.reducedFormValidator = reducedFormValidator;
    }
}
