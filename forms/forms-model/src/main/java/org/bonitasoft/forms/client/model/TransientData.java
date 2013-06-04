/**
 * Copyright (C) 2010 BonitaSoft S.A.
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
 * Transient page flow data
 * 
 * @author Anthony Birembaut
 *
 */
public class TransientData implements Serializable {
    
    /**
     * UID
     */
    private static final long serialVersionUID = -4502741521433936879L;

    private String name;
    
    private String classname;
    
    private Expression expression;

    /**
     * @param name
     * @param type
     * @param initialValue
     */
    public TransientData(final String name, final String type, final Expression expression) {
        super();
        this.name = name;
        this.classname = type;
        this.expression = expression;
    }
    
    /**
     * Default Constructor
     */
    public TransientData(){
        super();
        // Mandatory for serialization
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(final String classname) {
        this.classname = classname;
    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(final Expression expression) {
        this.expression = expression;
    }

}
