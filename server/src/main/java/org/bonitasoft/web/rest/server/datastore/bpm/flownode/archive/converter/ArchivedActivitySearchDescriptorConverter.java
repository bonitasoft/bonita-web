/**
 * Copyright (C) 2012 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.web.rest.server.datastore.bpm.flownode.archive.converter;

import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.engine.bpm.flownode.ArchivedActivityInstanceSearchDescriptor;
import org.bonitasoft.web.rest.model.bpm.flownode.ArchivedActivityItem;
import org.bonitasoft.web.rest.model.bpm.flownode.ArchivedFlowNodeItem;
import org.bonitasoft.web.rest.model.bpm.flownode.ArchivedHumanTaskItem;

/**
 * @author Colin PUY, Vincent Elcrin
 */
public class ArchivedActivitySearchDescriptorConverter extends ArchivedFlowNodeSearchDescriptorConverter {

    private static Map<String, String> mapping = new HashMap<String, String>();

    static {
        mapping.put(ArchivedActivityItem.ATTRIBUTE_REACHED_STATE_DATE, ArchivedActivityInstanceSearchDescriptor.REACHED_STATE_DATE);
        mapping.put(ArchivedFlowNodeItem.ATTRIBUTE_CASE_ID, ArchivedActivityInstanceSearchDescriptor.PARENT_PROCESS_INSTANCE_ID);
        mapping.put(ArchivedFlowNodeItem.ATTRIBUTE_ROOT_CASE_ID, ArchivedActivityInstanceSearchDescriptor.ROOT_PROCESS_INSTANCE_ID);
        mapping.put(ArchivedFlowNodeItem.ATTRIBUTE_PARENT_CASE_ID, ArchivedActivityInstanceSearchDescriptor.PARENT_PROCESS_INSTANCE_ID);
        mapping.put(ArchivedFlowNodeItem.ATTRIBUTE_PROCESS_ID, ArchivedActivityInstanceSearchDescriptor.PROCESS_DEFINITION_ID);
        mapping.put(ArchivedFlowNodeItem.ATTRIBUTE_STATE, ArchivedActivityInstanceSearchDescriptor.STATE_NAME);
        mapping.put(ArchivedHumanTaskItem.ATTRIBUTE_ASSIGNED_USER_ID, ArchivedActivityInstanceSearchDescriptor.ASSIGNEE_ID);
        mapping.put(ArchivedHumanTaskItem.ATTRIBUTE_PRIORITY, ArchivedActivityInstanceSearchDescriptor.PRIORITY);
        mapping.put(ArchivedHumanTaskItem.ATTRIBUTE_TYPE, ArchivedActivityInstanceSearchDescriptor.ACTIVITY_TYPE);
    }

    public ArchivedActivitySearchDescriptorConverter() {
        extendsMapping(mapping);
    }
}
