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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.bonitasoft.engine.bpm.data.impl.DoubleDataInstanceImpl;

/**
 * @author Laurent Leseigneur
 */
public class DoubleDataInstanceSerializer extends JsonSerializer<DoubleDataInstanceImpl> {

    DataInstanceSerializerHelper dataInstanceSerializerHelper = new DataInstanceSerializerHelper();

    @Override
    public void serialize(DoubleDataInstanceImpl value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeStartObject();
        dataInstanceSerializerHelper.writeDataInstanceFields(jgen, value);
        dataInstanceSerializerHelper.writeValueAndStringValue(jgen, "value", value.getValue());
        jgen.writeEndObject();
    }

    @Override
    public Class<DoubleDataInstanceImpl> handledType() {
        return DoubleDataInstanceImpl.class;
    }
}
