/**
 * Copyright (C) 2014 BonitaSoft S.A.
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
        //CONTENT_TYPE is managed differently in order to accept a OR with form and page
        //mapping.put(PageItem.FILTER_CONTENT_TYPE, PageSearchDescriptor.CONTENT_TYPE);
        mapping.put(PageItem.ATTRIBUTE_PROCESS_ID, PageSearchDescriptor.PROCESS_DEFINITION_ID);
        return mapping;
    }

    @Override
    public String convert(final String attribute) {
        if (PageItem.FILTER_CONTENT_TYPE.equals(attribute)) {
            return MapUtil.getValue(mapping, attribute, "");
        } else {
            return MapUtil.getMandatory(mapping, attribute);
        }
    }

    protected final void extendsMapping(final Map<String, String> extension) {
        mapping.putAll(extension);
    }
}
