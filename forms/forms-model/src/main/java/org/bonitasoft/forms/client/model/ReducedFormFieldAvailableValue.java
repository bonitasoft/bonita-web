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
public class ReducedFormFieldAvailableValue implements Serializable{

    /**
     * UID
     */
    private static final long serialVersionUID = 8852166987591645938L;

    /**
     * The label of the value as it's displayed
     */
    private String label;
    
    /**
     * the value
     */
    private String value;    
    
    /**
     * Constructor
     * @param label
     * @param value
     */
    public ReducedFormFieldAvailableValue(final String label, final String value) {
        this.label = label;
        this.value = value;
    }

    /**
     * Default constructor
     */
    public ReducedFormFieldAvailableValue() {
        super();
        // Mandatory for serialization
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(final String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }
}
