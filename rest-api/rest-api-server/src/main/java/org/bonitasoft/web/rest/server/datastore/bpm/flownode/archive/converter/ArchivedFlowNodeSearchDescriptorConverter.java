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
package org.bonitasoft.web.rest.server.datastore.bpm.flownode.archive.converter;

import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.engine.bpm.flownode.ArchivedFlowNodeInstanceSearchDescriptor;
import org.bonitasoft.web.rest.model.bpm.flownode.ArchivedFlowNodeItem;
import org.bonitasoft.web.rest.server.datastore.converter.AttributeConverter;
import org.bonitasoft.web.toolkit.client.common.util.MapUtil;

/**
 * @author Vincent Elcrin
 * 
 */
public class ArchivedFlowNodeSearchDescriptorConverter implements AttributeConverter {

    private final Map<String, String> mapping;

    public ArchivedFlowNodeSearchDescriptorConverter() {
        mapping = createMapping();
    }

    private Map<String, String> createMapping() {
        Map<String, String> mapping = new HashMap<String, String>();
        mapping.put(ArchivedFlowNodeItem.ATTRIBUTE_CASE_ID, ArchivedFlowNodeInstanceSearchDescriptor.ROOT_PROCESS_INSTANCE_ID);
        mapping.put(ArchivedFlowNodeItem.ATTRIBUTE_NAME, ArchivedFlowNodeInstanceSearchDescriptor.NAME);
        mapping.put(ArchivedFlowNodeItem.ATTRIBUTE_DISPLAY_NAME, ArchivedFlowNodeInstanceSearchDescriptor.DISPLAY_NAME);
        mapping.put(ArchivedFlowNodeItem.ATTRIBUTE_PROCESS_ID, ArchivedFlowNodeInstanceSearchDescriptor.PROCESS_DEFINITION_ID);
        mapping.put(ArchivedFlowNodeItem.ATTRIBUTE_STATE, ArchivedFlowNodeInstanceSearchDescriptor.STATE_NAME);
        mapping.put(ArchivedFlowNodeItem.ATTRIBUTE_TYPE, ArchivedFlowNodeInstanceSearchDescriptor.FLOW_NODE_TYPE);
        mapping.put(ArchivedFlowNodeItem.FILTER_IS_TERMINAL, ArchivedFlowNodeInstanceSearchDescriptor.TERMINAL);
        mapping.put(ArchivedFlowNodeItem.ATTRIBUTE_ARCHIVED_DATE, ArchivedFlowNodeInstanceSearchDescriptor.ARCHIVE_DATE);
        return mapping;
    }

    @Override
    public String convert(String attribute) {
        return MapUtil.getMandatory(mapping, attribute);
    }

    protected final void extendsMapping(Map<String, String> extension) {
        mapping.putAll(extension);
    }
}
