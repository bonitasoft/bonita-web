package org.bonitasoft.web.rest.server.datastore.converter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.engine.bpm.flownode.ActivityInstanceSearchDescriptor;
import org.bonitasoft.engine.bpm.flownode.FlowNodeInstanceSearchDescriptor;
import org.bonitasoft.web.rest.model.bpm.flownode.ActivityItem;
import org.bonitasoft.web.rest.model.bpm.flownode.TaskItem;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ItemAttribute;

public class ActivityAttributeConverter implements AttributeConverter {

    protected static final Map<String, String> mapping = new HashMap<>();

    static {
    	mapping.put(ActivityItem.ATTRIBUTE_CASE_ID, ActivityInstanceSearchDescriptor.PROCESS_INSTANCE_ID);
        mapping.put(ActivityItem.ATTRIBUTE_NAME, ActivityInstanceSearchDescriptor.NAME);
        mapping.put(ActivityItem.ATTRIBUTE_STATE, ActivityInstanceSearchDescriptor.STATE_NAME);
        mapping.put(ActivityItem.ATTRIBUTE_PROCESS_ID, ActivityInstanceSearchDescriptor.PROCESS_DEFINITION_ID);
        mapping.put(ActivityItem.ATTRIBUTE_ROOT_CASE_ID, ActivityInstanceSearchDescriptor.PROCESS_INSTANCE_ID);
        mapping.put(ActivityItem.ATTRIBUTE_PARENT_CASE_ID, ActivityInstanceSearchDescriptor.PARENT_PROCESS_INSTANCE_ID);
        mapping.put(ActivityItem.ATTRIBUTE_TYPE, ActivityInstanceSearchDescriptor.ACTIVITY_TYPE);
        mapping.put(ActivityItem.FILTER_SUPERVISOR_ID, ActivityInstanceSearchDescriptor.SUPERVISOR_ID);
        mapping.put(TaskItem.ATTRIBUTE_LAST_UPDATE_DATE, FlowNodeInstanceSearchDescriptor.LAST_UPDATE_DATE);
    }

    @Override
    public String convert(final String attribute) {
        return mapping.get(attribute);
    }

    @Override
    public Map<String, ItemAttribute.TYPE> getValueTypeMapping() {
        return Collections.emptyMap();
    }

}
