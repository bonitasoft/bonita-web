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

/**
 * @author Yongtao Guo
 * 
 */
public class DataFieldDefinition implements Serializable {

    private static final long serialVersionUID = 2605422059200194713L;

    private String label;

    private String description;

    private String type;

    private boolean transientData;

    private String className;

    private Expression defaultValueExpression;

    /**
     * Default Constructor
     */
    public DataFieldDefinition() {
        super();
        // Mandatory for serialization
    }

    public DataFieldDefinition(final String label, final String description, final String type, final String className,
            final Expression defaultValueExpression, final boolean transientData) {
        super();
        this.label = label;
        this.description = description;
        this.className = className;
        this.type = type;
        this.defaultValueExpression = defaultValueExpression;
        this.transientData = transientData;
    }

    
    public String getLabel() {
        return label;
    }

    
    public void setLabel(String label) {
        this.label = label;
    }

    
    public String getDescription() {
        return description;
    }

    
    public void setDescription(String description) {
        this.description = description;
    }

    
    public String getType() {
        return type;
    }

    
    public void setType(String type) {
        this.type = type;
    }

    
    public boolean isTransientData() {
        return transientData;
    }

    
    public void setTransientData(boolean transientData) {
        this.transientData = transientData;
    }

    
    public String getClassName() {
        return className;
    }

    
    public void setClassName(String className) {
        this.className = className;
    }

    
    public Expression getDefaultValueExpression() {
        return defaultValueExpression;
    }

    
    public void setDefaultValueExpression(Expression defaultValueExpression) {
        this.defaultValueExpression = defaultValueExpression;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (className == null ? 0 : className.hashCode());
        result = prime * result + (defaultValueExpression == null ? 0 : defaultValueExpression.hashCode());
        result = prime * result + (description == null ? 0 : description.hashCode());
        result = prime * result + (label == null ? 0 : label.hashCode());
        result = prime * result + (transientData ? 1231 : 1237);
        result = prime * result + (type == null ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DataFieldDefinition other = (DataFieldDefinition) obj;
        if (className == null) {
            if (other.className != null) {
                return false;
            }
        } else if (!className.equals(other.className)) {
            return false;
        }
        if (defaultValueExpression == null) {
            if (other.defaultValueExpression != null) {
                return false;
            }
        } else if (!defaultValueExpression.equals(other.defaultValueExpression)) {
            return false;
        }
        if (description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!description.equals(other.description)) {
            return false;
        }
        if (label == null) {
            if (other.label != null) {
                return false;
            }
        } else if (!label.equals(other.label)) {
            return false;
        }
        if (transientData != other.transientData) {
            return false;
        }
        if (type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!type.equals(other.type)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("DataDefinitionImpl [label=");
        builder.append(label);
        builder.append(", description=");
        builder.append(description);
        builder.append(", type=");
        builder.append(type);
        builder.append(", transientData=");
        builder.append(transientData);
        builder.append(", className=");
        builder.append(className);
        builder.append(", defaultValueExpression=");
        builder.append(defaultValueExpression);
        builder.append("]");
        return builder.toString();
    }

}
