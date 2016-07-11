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

import org.bonitasoft.engine.bpm.data.impl.DataInstanceImpl;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * @author Laurent Leseigneur
 */
public class DataInstanceSerializer extends JsonSerializer<DataInstanceImpl> {

    JacksonSerializerHelper jacksonSerializerHelper = new JacksonSerializerHelper();

    @Override
    public void serialize(final DataInstanceImpl value, final JsonGenerator jgen, final SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeStartObject();
        jgen.writeObjectField("name", value.getName());
        jgen.writeObjectField("description", value.getDescription());
        jgen.writeObjectField("transientData", value.isTransientData());
        jgen.writeObjectField("className", value.getClassName());
        jgen.writeObjectField("containerType", value.getContainerType());

        jacksonSerializerHelper.writeNumberField(jgen, "tenantId", value.getTenantId());
        jacksonSerializerHelper.writeNumberField(jgen, "id", value.getId());
        jacksonSerializerHelper.writeNumberField(jgen, "containerId", value.getContainerId());
        jacksonSerializerHelper.writeNumberField(jgen, "value", value.getValue());
        jgen.writeEndObject();
    }

    @Override
    public Class<DataInstanceImpl> handledType() {
        return DataInstanceImpl.class;
    }
}
