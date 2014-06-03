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
package org.bonitasoft.web.rest.server.api.organization;

import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.web.rest.server.datastore.converter.AttributeConverter;

import static org.bonitasoft.engine.identity.CustomUserInfoValueSearchDescriptor.DEFINITION_ID;
import static org.bonitasoft.engine.identity.CustomUserInfoValueSearchDescriptor.USER_ID;
import static org.bonitasoft.engine.identity.CustomUserInfoValueSearchDescriptor.VALUE;
import static org.bonitasoft.web.rest.model.identity.CustomUserInfoItem.ATTRIBUTE_DEFINITION_ID;
import static org.bonitasoft.web.rest.model.identity.CustomUserInfoItem.ATTRIBUTE_USER_ID;
import static org.bonitasoft.web.rest.model.identity.CustomUserInfoItem.ATTRIBUTE_VALUE;

/**
 * @author Vincent Elcrin
 */
public class CustomUserInfoAttributeConverter implements AttributeConverter {

    private static Map<String, String> attributes = new HashMap<String, String>();

    static {
        attributes.put(ATTRIBUTE_USER_ID, USER_ID);
        attributes.put(ATTRIBUTE_DEFINITION_ID, DEFINITION_ID);
        attributes.put(ATTRIBUTE_VALUE, VALUE);
    }

    @Override
    public String convert(String attribute) {
        return attributes.get(attribute);
    }
}
