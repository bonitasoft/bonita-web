/*******************************************************************************
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.web.rest.server.datastore.application;

import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.engine.business.application.ApplicationSearchDescriptor;
import org.bonitasoft.web.rest.model.application.ApplicationItem;
import org.bonitasoft.web.rest.server.datastore.converter.AttributeConverter;
import org.bonitasoft.web.toolkit.client.common.util.MapUtil;

public class ApplicationSearchDescriptorConverter implements AttributeConverter {

    private final Map<String, String> mapping;

    public ApplicationSearchDescriptorConverter() {
        mapping = createMapping();
    }

    private Map<String, String> createMapping() {
        final Map<String, String> mapping = new HashMap<String, String>();
        mapping.put(ApplicationItem.ATTRIBUTE_ID, ApplicationSearchDescriptor.ID);
        mapping.put(ApplicationItem.ATTRIBUTE_TOKEN, ApplicationSearchDescriptor.TOKEN);
        mapping.put(ApplicationItem.ATTRIBUTE_DISPLAY_NAME, ApplicationSearchDescriptor.DISPLAY_NAME);
        mapping.put(ApplicationItem.ATTRIBUTE_STATE, ApplicationSearchDescriptor.STATE);
        mapping.put(ApplicationItem.ATTRIBUTE_CREATED_BY, ApplicationSearchDescriptor.CREATED_BY);
        mapping.put(ApplicationItem.ATTRIBUTE_CREATION_DATE, ApplicationSearchDescriptor.CREATION_DATE);
        mapping.put(ApplicationItem.ATTRIBUTE_ICON_PATH, ApplicationSearchDescriptor.ICON_PATH);
        mapping.put(ApplicationItem.ATTRIBUTE_LAST_UPDATE_DATE, ApplicationSearchDescriptor.LAST_UPDATE_DATE);
        mapping.put(ApplicationItem.ATTRIBUTE_UPDATED_BY, ApplicationSearchDescriptor.UPDATED_BY);
        mapping.put(ApplicationItem.ATTRIBUTE_VERSION, ApplicationSearchDescriptor.VERSION);
        mapping.put(ApplicationItem.ATTRIBUTE_PROFILE_ID, ApplicationSearchDescriptor.PROFILE_ID);
        return mapping;
    }

    @Override
    public String convert(final String attribute) {
        return MapUtil.getMandatory(mapping, attribute);
    }

}
