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

import org.bonitasoft.engine.bpm.data.impl.DataInstanceImpl;

import com.fasterxml.jackson.core.JsonGenerator;

/**
 * @author Laurent Leseigneur
 */
public class DataInstanceSerializerHelper {

    protected String getStringValue(Serializable value) {
        if (value == null) {
            return null;
        }
        return String.valueOf(value);
    }

    protected void writeLongAndStringField(JsonGenerator jgen, String fieldName, Long longValue) throws IOException {
        jgen.writeObjectField(fieldName, longValue);
        jgen.writeObjectField(fieldName + "_string", getStringValue(longValue));
    }

    public void writeDataInstanceFields(JsonGenerator jgen, DataInstanceImpl value) throws IOException {
        jgen.writeObjectField("name", value.getName());
        jgen.writeObjectField("description", value.getDescription());
        jgen.writeObjectField("transientData", value.isTransientData());
        jgen.writeObjectField("className", value.getClassName());
        jgen.writeObjectField("containerType", value.getContainerType());

        writeLongAndStringField(jgen, "tenantId", value.getTenantId());
        writeLongAndStringField(jgen, "id", value.getId());
        writeLongAndStringField(jgen, "containerId", value.getContainerId());
    }
}
