/**
 * Copyright (C) 2012 BonitaSoft S.A.
 * 
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
package org.bonitasoft.web.rest.server.datastore.utils;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.io.Serializable;

import org.bonitasoft.web.rest.server.framework.json.JacksonDeserializer;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;

/**
 * Variable Mapper - Used for variable Json deserialization
 *  
 * @author Colin PUY
 */
public class VariableMapper {

    private JacksonDeserializer deserializer;
    private Variable variable;

    public VariableMapper(Variable variable, JacksonDeserializer jacksonDeserializer) {
        this.variable = variable;
        this.deserializer = jacksonDeserializer;
    }
    
    public Serializable getSerializableValue(String className) {
        try {
            return (Serializable) deserializer.convertValue(variable.getValue(), Class.forName(className));
        } catch (IllegalArgumentException e) {
            throw new APIException(_("%value% is not a valid value for %className%", new Arg("value", variable.getValue()), 
                    new Arg("className", className)));
        } catch (ClassNotFoundException e) {
            throw new APIException(_("%className% not found. Only jdk types are supported", new Arg("className", className)));
        } catch (ClassCastException e) {
            throw new APIException(_("%className% is not Serializable", new Arg("className", className)));
        }
    }
    
    public String getName() {
        return variable.getName();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((variable == null) ? 0 : variable.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        VariableMapper other = (VariableMapper) obj;
        return variable != null && variable.equals(other.variable);
    }
    
    
}
