/**
 * Copyright (C) 2014 BonitaSoft S.A.
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
        mapping.put(ApplicationItem.ATTRIBUTE_LAYOUT_ID, ApplicationSearchDescriptor.LAYOUT_ID);
        mapping.put(ApplicationItem.ATTRIBUTE_THEME_ID, ApplicationSearchDescriptor.THEME_ID);

        return mapping;
    }

    @Override
    public String convert(final String attribute) {
        return MapUtil.getMandatory(mapping, attribute);
    }

}
