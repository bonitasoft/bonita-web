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
package org.bonitasoft.web.rest.server.datastore.applicationmenu;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.engine.business.application.ApplicationMenuSearchDescriptor;
import org.bonitasoft.web.rest.model.applicationmenu.ApplicationMenuItem;
import org.bonitasoft.web.rest.server.datastore.converter.AttributeConverter;
import org.bonitasoft.web.toolkit.client.common.util.MapUtil;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ItemAttribute;

/**
 * @author Julien Mege
 */
public class ApplicationMenuSearchDescriptorConverter implements AttributeConverter {

    private final Map<String, String> mapping;

    ApplicationMenuSearchDescriptorConverter() {
        mapping = createMapping();
    }

    private Map<String, String> createMapping() {
        final Map<String, String> mapping = new HashMap<>();
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

    @Override
    public Map<String, ItemAttribute.TYPE> getValueTypeMapping() {
        return Collections.emptyMap();
    }

}
