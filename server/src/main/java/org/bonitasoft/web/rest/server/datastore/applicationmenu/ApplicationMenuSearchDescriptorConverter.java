/*******************************************************************************
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.web.rest.server.datastore.applicationmenu;

import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.engine.business.application.ApplicationMenuSearchDescriptor;
import org.bonitasoft.web.rest.model.applicationmenu.ApplicationMenuItem;
import org.bonitasoft.web.rest.server.datastore.converter.AttributeConverter;
import org.bonitasoft.web.toolkit.client.common.util.MapUtil;

/**
 * @author Julien Mege
 */
public class ApplicationMenuSearchDescriptorConverter implements AttributeConverter {

    private final Map<String, String> mapping;

    public ApplicationMenuSearchDescriptorConverter() {
        mapping = createMapping();
    }

    private Map<String, String> createMapping() {
        final Map<String, String> mapping = new HashMap<String, String>();
        mapping.put(ApplicationMenuItem.ATTRIBUTE_ID, ApplicationMenuSearchDescriptor.ID);
        mapping.put(ApplicationMenuItem.ATTRIBUTE_DISPLAY_NAME, ApplicationMenuSearchDescriptor.DISPLAY_NAME);
        mapping.put(ApplicationMenuItem.ATTRIBUTE_APPLICATION_ID, ApplicationMenuSearchDescriptor.APPLICATION_ID);
        mapping.put(ApplicationMenuItem.ATTRIBUTE_APPLICATION_PAGE_ID, ApplicationMenuSearchDescriptor.APPLICATION_PAGE_ID);
        mapping.put(ApplicationMenuItem.ATTRIBUTE_MENU_INDEX, ApplicationMenuSearchDescriptor.INDEX);
        mapping.put(ApplicationMenuItem.ATTRIBUTE_PARENT_MENU_ID, ApplicationMenuSearchDescriptor.PARENT_ID);
        return mapping;
    }

    @Override
    public String convert(final String attribute) {
        return MapUtil.getMandatory(mapping, attribute);
    }

}
