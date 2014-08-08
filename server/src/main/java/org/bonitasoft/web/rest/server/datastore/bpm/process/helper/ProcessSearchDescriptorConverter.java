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
package org.bonitasoft.web.rest.server.datastore.bpm.process.helper;

import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfoSearchDescriptor;
import org.bonitasoft.web.rest.model.bpm.process.ProcessItem;
import org.bonitasoft.web.rest.server.datastore.converter.AttributeConverter;

/**
 * @author Vincent Elcrin
 * 
 */
public class ProcessSearchDescriptorConverter implements AttributeConverter {

    private static Map<String, String> mapping = new HashMap<String, String>();

    static {
        mapping.put(ProcessItem.ATTRIBUTE_ID, ProcessDeploymentInfoSearchDescriptor.ID);
        mapping.put(ProcessItem.ATTRIBUTE_ACTIVATION_STATE, ProcessDeploymentInfoSearchDescriptor.ACTIVATION_STATE);
        mapping.put(ProcessItem.ATTRIBUTE_CONFIGURATION_STATE, ProcessDeploymentInfoSearchDescriptor.CONFIGURATION_STATE);
        mapping.put(ProcessItem.ATTRIBUTE_DEPLOYED_BY_USER_ID, ProcessDeploymentInfoSearchDescriptor.DEPLOYED_BY);
        mapping.put(ProcessItem.ATTRIBUTE_DEPLOYMENT_DATE, ProcessDeploymentInfoSearchDescriptor.DEPLOYMENT_DATE);
        mapping.put(ProcessItem.ATTRIBUTE_DISPLAY_NAME, ProcessDeploymentInfoSearchDescriptor.DISPLAY_NAME);
        mapping.put(ProcessItem.ATTRIBUTE_LAST_UPDATE_DATE, ProcessDeploymentInfoSearchDescriptor.LAST_UPDATE_DATE);
        mapping.put(ProcessItem.ATTRIBUTE_NAME, ProcessDeploymentInfoSearchDescriptor.NAME);
        mapping.put(ProcessItem.ATTRIBUTE_VERSION, ProcessDeploymentInfoSearchDescriptor.VERSION);

        mapping.put(ProcessItem.FILTER_CATEGORY_ID, ProcessDeploymentInfoSearchDescriptor.CATEGORY_ID);
        mapping.put(ProcessItem.FILTER_RECENT_PROCESSES, ""); // code smell. Should return empty object (EmptyField)
        mapping.put(ProcessItem.FILTER_SUPERVISOR_ID, "");
        mapping.put(ProcessItem.FILTER_TEAM_MANAGER_ID, "");
        mapping.put(ProcessItem.FILTER_USER_ID, "");
        mapping.put(ProcessItem.FILTER_FOR_PENDING_OR_ASSIGNED_TASKS, "");
    }

    @Override
    public String convert(String attribute) {
        String value = mapping.get(attribute);
        if (value == null) {
            throw new RuntimeException("Can't find search descriptor corresponding to " + attribute);
        }
        return value;
    }

}
