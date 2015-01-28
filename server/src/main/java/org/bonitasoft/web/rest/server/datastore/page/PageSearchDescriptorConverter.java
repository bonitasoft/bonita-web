/*******************************************************************************
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.web.rest.server.datastore.page;

import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.engine.page.PageSearchDescriptor;
import org.bonitasoft.web.rest.model.portal.page.PageItem;
import org.bonitasoft.web.rest.server.datastore.converter.AttributeConverter;
import org.bonitasoft.web.toolkit.client.common.util.MapUtil;

public class PageSearchDescriptorConverter implements AttributeConverter {

    private final Map<String, String> mapping;

    public PageSearchDescriptorConverter() {
        mapping = createMapping();
    }

    private Map<String, String> createMapping() {
        final Map<String, String> mapping = new HashMap<String, String>();
        mapping.put(PageItem.ATTRIBUTE_ID, PageSearchDescriptor.ID);
        mapping.put(PageItem.ATTRIBUTE_URL_TOKEN, PageSearchDescriptor.NAME);
        mapping.put(PageItem.ATTRIBUTE_DISPLAY_NAME, PageSearchDescriptor.DISPLAY_NAME);
        mapping.put(PageItem.ATTRIBUTE_IS_PROVIDED, PageSearchDescriptor.PROVIDED);
        mapping.put(PageItem.ATTRIBUTE_CREATED_BY_USER_ID, PageSearchDescriptor.INSTALLED_BY);
        mapping.put(PageItem.ATTRIBUTE_CREATION_DATE, PageSearchDescriptor.INSTALLATION_DATE);
        mapping.put(PageItem.ATTRIBUTE_LAST_UPDATE_DATE, PageSearchDescriptor.LAST_MODIFICATION_DATE);
        return mapping;
    }

    @Override
    public String convert(final String attribute) {
        return MapUtil.getMandatory(mapping, attribute);
    }

    protected final void extendsMapping(final Map<String, String> extension) {
        mapping.putAll(extension);
    }
}
