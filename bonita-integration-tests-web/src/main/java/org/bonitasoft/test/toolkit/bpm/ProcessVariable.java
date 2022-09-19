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
package org.bonitasoft.test.toolkit.bpm;

import java.util.Date;

import org.bonitasoft.engine.expression.Expression;
import org.bonitasoft.engine.expression.ExpressionBuilder;
import org.bonitasoft.engine.expression.InvalidExpressionException;

/**
 * @author Colin PUY
 * 
 */
public class ProcessVariable {

    private final String name;

    private final Class<?> clazz;

    private final Expression defaultValue;
    
    public static ProcessVariable aStringVariable(String name, String defaultValue) throws InvalidExpressionException {
        return new ProcessVariable(name, String.class, 
                new ExpressionBuilder().createConstantStringExpression(defaultValue));
    }
    
    public static ProcessVariable aLongVariable(String name, long defaultValue) throws InvalidExpressionException {
        return new ProcessVariable(name, Long.class, 
                new ExpressionBuilder().createConstantLongExpression(defaultValue));
    }
    
    public static ProcessVariable aDateVariable(String name, String defaultValue) throws InvalidExpressionException {
        return new ProcessVariable(name, Date.class, 
                new ExpressionBuilder().createConstantDateExpression(defaultValue));
    }
    
    public ProcessVariable(String name, Class<?> clazz, Expression defaultValue) {
        this.name = name;
        this.clazz = clazz;
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return name;
    }

    public String getClassName() {
        return clazz.getName();
    }

    public Expression getDefaultValue() {
        return defaultValue;
    }

    public static ProcessVariable createLongVariable(long value) throws InvalidExpressionException {
        return new ProcessVariable("aLongVariable", Long.class, new ExpressionBuilder().createConstantLongExpression(value));
    }

    public static ProcessVariable createIntVariable(int value) throws InvalidExpressionException {
        return new ProcessVariable("aIntVariable", Integer.class, new ExpressionBuilder().createConstantIntegerExpression(value));
    }

    public static ProcessVariable createStringVariable(String value) throws InvalidExpressionException {
        return new ProcessVariable("aStringVariable", String.class, new ExpressionBuilder().createConstantStringExpression(value));
    }

    public static ProcessVariable createBooleanVariable(Boolean value) throws InvalidExpressionException {
        return new ProcessVariable("aBooleanVariable", Boolean.class, new ExpressionBuilder().createConstantBooleanExpression(value));
    }

    public static ProcessVariable createDoubleVariable(Double value) throws InvalidExpressionException {
        return new ProcessVariable("aDoubleVariable", Double.class, new ExpressionBuilder().createConstantDoubleExpression(value));
    }

}
