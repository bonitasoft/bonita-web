/*******************************************************************************
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.web.rest.server.datastore.applicationpage;

import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.engine.business.application.ApplicationPageSearchDescriptor;
import org.bonitasoft.web.rest.model.applicationpage.ApplicationPageItem;
import org.bonitasoft.web.rest.server.datastore.converter.AttributeConverter;
import org.bonitasoft.web.toolkit.client.common.util.MapUtil;


/**
 * @author Elias Ricken de Medeiros
 */
public class ApplicationPageSearchDescriptorConverter implements AttributeConverter {

    private final Map<String, String> mapping;

    public ApplicationPageSearchDescriptorConverter() {
        mapping = createMapping();
    }

    private Map<String, String> createMapping() {
        final Map<String, String> mapping = new HashMap<String, String>();
        mapping.put(ApplicationPageItem.ATTRIBUTE_ID, ApplicationPageSearchDescriptor.ID);
        mapping.put(ApplicationPageItem.ATTRIBUTE_TOKEN, ApplicationPageSearchDescriptor.TOKEN);
        mapping.put(ApplicationPageItem.ATTRIBUTE_APPLICATION_ID, ApplicationPageSearchDescriptor.APPLICATION_ID);
        mapping.put(ApplicationPageItem.ATTRIBUTE_PAGE_ID, ApplicationPageSearchDescriptor.PAGE_ID);
        return mapping;
    }

    @Override
    public String convert(final String attribute) {
        return MapUtil.getMandatory(mapping, attribute);
    }

}
