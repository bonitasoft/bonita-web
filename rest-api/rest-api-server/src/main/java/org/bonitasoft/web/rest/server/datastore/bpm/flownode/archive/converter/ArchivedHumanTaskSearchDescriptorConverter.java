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

import org.bonitasoft.engine.bpm.flownode.ArchivedHumanTaskInstanceSearchDescriptor;
import org.bonitasoft.web.rest.model.bpm.flownode.ArchivedActivityItem;
import org.bonitasoft.web.rest.model.bpm.flownode.ArchivedFlowNodeItem;
import org.bonitasoft.web.rest.model.bpm.flownode.ArchivedHumanTaskItem;

/**
 * @author Vincent Elcrin
 * 
 */
public class ArchivedHumanTaskSearchDescriptorConverter extends ArchivedActivitySearchDescriptorConverter {

    private static Map<String, String> mapping = new HashMap<String, String>();

    static {
        mapping.put(ArchivedFlowNodeItem.ATTRIBUTE_CASE_ID, ArchivedHumanTaskInstanceSearchDescriptor.PROCESS_INSTANCE_ID);
        mapping.put(ArchivedFlowNodeItem.ATTRIBUTE_PROCESS_ID, ArchivedHumanTaskInstanceSearchDescriptor.PROCESS_DEFINITION_ID);
        mapping.put(ArchivedFlowNodeItem.ATTRIBUTE_STATE, ArchivedHumanTaskInstanceSearchDescriptor.STATE_NAME);
        mapping.put(ArchivedActivityItem.ATTRIBUTE_REACHED_STATE_DATE, ArchivedHumanTaskInstanceSearchDescriptor.REACHED_STATE_DATE);
        // FIXME Add this filter in the engine
        // mapping.put(ArchivedFlowNodeItem.ATTRIBUTE_TYPE, ArchivedHumanTaskInstanceSearchDescriptor.FLOW_NODE_TYPE);
        mapping.put(ArchivedHumanTaskItem.ATTRIBUTE_ASSIGNED_USER_ID, ArchivedHumanTaskInstanceSearchDescriptor.ASSIGNEE_ID);
        mapping.put(ArchivedHumanTaskItem.ATTRIBUTE_PRIORITY, ArchivedHumanTaskInstanceSearchDescriptor.PRIORITY);
        mapping.put(ArchivedHumanTaskItem.ATTRIBUTE_ARCHIVED_DATE, ArchivedHumanTaskInstanceSearchDescriptor.ARCHIVE_DATE);
        mapping.put(ArchivedHumanTaskItem.FILTER_USER_ID, "");

    }

    public ArchivedHumanTaskSearchDescriptorConverter() {
        extendsMapping(mapping);
    }
}
