/*******************************************************************************
 * Copyright (C) 2015 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation
 * version 2.1 of the License.
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth
 * Floor, Boston, MA 02110-1301, USA.
 ******************************************************************************/
package org.bonitasoft.web.rest.server.utils;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.bonitasoft.engine.bpm.data.impl.DataInstanceImpl;

import com.fasterxml.jackson.core.JsonGenerator;

/**
 * @author Laurent Leseigneur
 */
public class JacksonSerializerHelper {

    private final Set<String> numberTypes;

    public JacksonSerializerHelper() {
        numberTypes = new HashSet<>();
        numberTypes.add(Long.class.getCanonicalName());
        numberTypes.add(Float.class.getCanonicalName());
        numberTypes.add(Double.class.getCanonicalName());
    }

    protected String getStringValue(Serializable value) {
        if (value == null) {
            return null;
        }
        return String.valueOf(value);
    }

    protected void writeValueAndStringValue(JsonGenerator jgen, String fieldName, Serializable value) throws IOException {
        jgen.writeObjectField(fieldName, value);
        if (numberTypes.contains(value.getClass().getCanonicalName())) {
            jgen.writeObjectField(fieldName + "_string", getStringValue(value));
        }
    }

    public void serializeDataInstance(DataInstanceImpl value, JsonGenerator jgen) throws IOException {
        jgen.writeStartObject();
        jgen.writeObjectField("name", value.getName());
        jgen.writeObjectField("description", value.getDescription());
        jgen.writeObjectField("transientData", value.isTransientData());
        jgen.writeObjectField("className", value.getClassName());
        jgen.writeObjectField("containerType", value.getContainerType());

        writeValueAndStringValue(jgen, "tenantId", value.getTenantId());
        writeValueAndStringValue(jgen, "id", value.getId());
        writeValueAndStringValue(jgen, "containerId", value.getContainerId());
        writeValueAndStringValue(jgen, "value", value.getValue());
        jgen.writeEndObject();
    }
}
