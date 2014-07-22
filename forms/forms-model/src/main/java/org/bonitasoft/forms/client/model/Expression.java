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
package org.bonitasoft.forms.client.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @author Anthony Birembaut
 */
public class Expression implements Serializable {

    private static final long serialVersionUID = 1663953452464781859L;

    private String name;

    private String content;

    private String expressionType;

    private String returnType;

    private String interpreter;

    private List<Expression> dependencies;
    
    /**
     * Constructor.
     * @param name
     * @param content
     * @param expressionType
     * @param returnType
     * @param interpreter
     * @param dependencies
     */
    public Expression(String name, String content, String expressionType, String returnType, String interpreter, List<Expression> dependencies) {
        super();
        this.name = name;
        this.content = content;
        this.expressionType = expressionType;
        this.returnType = returnType;
        this.interpreter = interpreter;
        this.dependencies = dependencies;
    }
    
    /**
     * Default Constructor
     */
    public Expression() {
        super();
        // Mandatory for serialization
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public String getExpressionType() {
        return expressionType;
    }

    public String getReturnType() {
        return returnType;
    }

    public String getInterpreter() {
        return interpreter;
    }

    public List<Expression> getDependencies() {
        if (dependencies == null) {
            return Collections.emptyList();
        }
        return dependencies;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public void setExpressionType(final String expressionType) {
        this.expressionType = expressionType;
    }

    public void setReturnType(final String returnType) {
        this.returnType = returnType;
    }

    public void setInterpreter(final String interpreter) {
        this.interpreter = interpreter;
    }

    public void setDependencies(final List<Expression> dependencies) {
        this.dependencies = dependencies;
    }

}
