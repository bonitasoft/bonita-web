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

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.web.rest.server.framework.json.JacksonDeserializer;
import org.bonitasoft.web.toolkit.client.common.util.StringUtil;

/**
 * Variables Mapper - Used for variables Json deserialization
 *  
 * @author Colin PUY
 */
public class VariablesMapper {
    
    private List<VariableMapper> variables;
    
    protected VariablesMapper(List<Variable> variables, JacksonDeserializer deserializer) {
        this.variables = convertToMappers(variables, deserializer);
    }
    
    public static VariablesMapper fromJson(String json) {
        JacksonDeserializer deserializer = new JacksonDeserializer();
        List<Variable> variables = deserializer.deserializeList(json, Variable.class);
        return new VariablesMapper(variables, deserializer);
    }

    private List<VariableMapper> convertToMappers(List<Variable> list, JacksonDeserializer deserializer) {
        ArrayList<VariableMapper> mappers = new ArrayList<VariableMapper>();
        for (Variable variable : list) {
            if (!StringUtil.isBlank(variable.getName())) {
                mappers.add(new VariableMapper(variable, deserializer));
            }
        }
        return mappers;
    }

    public List<VariableMapper> getVariables() {
        return variables;
    }

}
