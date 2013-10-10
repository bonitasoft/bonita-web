/**
 * Copyright (C) 2012 BonitaSoft S.A.
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
package org.bonitasoft.web.rest.server.datastore.organization;

import static org.bonitasoft.web.rest.model.identity.UserItem.ATTRIBUTE_ENABLED;
import static org.bonitasoft.web.rest.model.identity.UserItem.ATTRIBUTE_FIRSTNAME;
import static org.bonitasoft.web.rest.model.identity.UserItem.ATTRIBUTE_LASTNAME;
import static org.bonitasoft.web.rest.model.identity.UserItem.ATTRIBUTE_MANAGER_ID;
import static org.bonitasoft.web.rest.model.identity.UserItem.ATTRIBUTE_USERNAME;
import static org.bonitasoft.web.rest.model.identity.UserItem.FILTER_GROUP_ID;
import static org.bonitasoft.web.rest.model.identity.UserItem.FILTER_ROLE_ID;

import org.bonitasoft.engine.identity.UserSearchDescriptor;
import org.bonitasoft.web.rest.model.identity.UserItem;
import org.bonitasoft.web.rest.server.datastore.converter.AttributeConverter;
import org.bonitasoft.web.rest.server.datastore.converter.AttributeConverterException;

/**
 * @author Colin PUY
 * 
 */
public class UserSearchAttributeConverter implements AttributeConverter {

    @Override
    public String convert(String attribute) {
        if (attribute == null) {
            return null;
        }
        if (ATTRIBUTE_FIRSTNAME.equals(attribute))
            return UserSearchDescriptor.FIRST_NAME;
        if (ATTRIBUTE_LASTNAME.equals(attribute))
            return UserSearchDescriptor.LAST_NAME;
        if (ATTRIBUTE_USERNAME.equals(attribute))
            return UserSearchDescriptor.USER_NAME;
        if (ATTRIBUTE_ENABLED.equals(attribute)) 
            return UserSearchDescriptor.ENABLED;
        if (ATTRIBUTE_MANAGER_ID.equals(attribute))
            return UserSearchDescriptor.MANAGER_USER_ID;
        if (FILTER_GROUP_ID.equals(attribute))
            return UserSearchDescriptor.GROUP_ID;
        if (FILTER_ROLE_ID.equals(attribute))
            return UserSearchDescriptor.ROLE_ID;
        throw new AttributeConverterException("Unable to convert search field " + attribute + ", unknown value");
    }
}
