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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;

import org.bonitasoft.web.rest.server.framework.json.JacksonDeserializer;
import org.junit.Test;

public class VariablesMapperTest {

    private static final String JSON_VARIABLES = "[" +
            "{\"name\": \"variable1\", \"value\": \"newValue\"}," +
            "{\"name\": \"variable2\", \"value\": 9}," +
            "{\"name\": \"variable3\", \"value\": 349246800000}" +
        "]";
    
    @Test
    public void variablesMapper_convert_json_variable_list_to_variableMappers() throws Exception {

        VariablesMapper variablesMapper = VariablesMapper.fromJson(JSON_VARIABLES);
        
        assertThat(variablesMapper.getVariables(), hasItems(
                aVariableMapper("variable1", "newValue"), 
                aVariableMapper("variable2", 9), 
                aVariableMapper("variable3", 349246800000L)));
    }

    private VariableMapper aVariableMapper(String name, Object value) {
        Variable variable = new Variable();
        variable.set("name", name);
        variable.set("value", value);
        return new VariableMapper(variable, new JacksonDeserializer());
    }
}
