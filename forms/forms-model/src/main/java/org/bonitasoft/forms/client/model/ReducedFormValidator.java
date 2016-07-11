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
 * a form validator
 * 
 * @author Anthony Birembaut
 */
public class ReducedFormValidator implements Serializable {

    /**
     * Possible validation message positions
     */
    public static enum ValidatorPosition {TOP, BOTTOM};
    
    /**
     * UID
     */
    private static final long serialVersionUID = -7788147637501777696L;

    /**
     * validator ID
     */
    private String id;
    
    /**
     * validator className
     */
    private String validatorClass;
    
    /**
     * validator label
     */
    private String label;
    
    /**
     * validator CSS classNames
     */
    private String style;
    
    /**
     * label Position
     */
    private ValidatorPosition position;
    
    /**
     * Constructor
     * @param id validator id
     * @param validatorClass validator className
     * @param style validator label CSS classNames
     */
    public ReducedFormValidator(final String id, final String validatorClass, final String style) {
        this.id = id;
        this.validatorClass = validatorClass;
        this.style = style;
    }

    /**
     * Default constructor
     */
    public ReducedFormValidator() {
        super();
       // Mandatory for serialization.
    }
    
    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getValidatorClass() {
        return validatorClass;
    }

    public void setValidatorClass(final String validatorClass) {
        this.validatorClass = validatorClass;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(final String label) {
        this.label = label;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(final String style) {
        this.style = style;
    }

    public ValidatorPosition getPosition() {
        return position;
    }

    public void setPosition(final ValidatorPosition position) {
        this.position = position;
    }
	
}
