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
