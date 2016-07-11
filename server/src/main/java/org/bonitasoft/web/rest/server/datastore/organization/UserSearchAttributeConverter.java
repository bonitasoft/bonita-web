/**
 * Copyright (C) 2012 BonitaSoft S.A.
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
package org.bonitasoft.web.rest.server.datastore.organization;

import static org.bonitasoft.web.rest.model.identity.UserItem.ATTRIBUTE_ENABLED;
import static org.bonitasoft.web.rest.model.identity.UserItem.ATTRIBUTE_FIRSTNAME;
import static org.bonitasoft.web.rest.model.identity.UserItem.ATTRIBUTE_LASTNAME;
import static org.bonitasoft.web.rest.model.identity.UserItem.ATTRIBUTE_MANAGER_ID;
import static org.bonitasoft.web.rest.model.identity.UserItem.ATTRIBUTE_USERNAME;
import static org.bonitasoft.web.rest.model.identity.UserItem.FILTER_GROUP_ID;
import static org.bonitasoft.web.rest.model.identity.UserItem.FILTER_ROLE_ID;
import static org.bonitasoft.web.toolkit.client.data.item.template.ItemHasUniqueId.ATTRIBUTE_ID;

import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.engine.identity.UserSearchDescriptor;
import org.bonitasoft.web.rest.server.datastore.converter.AttributeConverter;

/**
 * @author Colin PUY, Anthony Birembaut
 */
public class UserSearchAttributeConverter implements AttributeConverter {

    static Map<String, String> mapping = new HashMap<String, String>();

    static {
        mapping.put(ATTRIBUTE_ID, UserSearchDescriptor.ID);
        mapping.put(ATTRIBUTE_FIRSTNAME, UserSearchDescriptor.FIRST_NAME);
        mapping.put(ATTRIBUTE_LASTNAME, UserSearchDescriptor.LAST_NAME);
        mapping.put(ATTRIBUTE_USERNAME, UserSearchDescriptor.USER_NAME);
        mapping.put(ATTRIBUTE_ENABLED, UserSearchDescriptor.ENABLED);
        mapping.put(ATTRIBUTE_MANAGER_ID, UserSearchDescriptor.MANAGER_USER_ID);
        mapping.put(FILTER_GROUP_ID, UserSearchDescriptor.GROUP_ID);
        mapping.put(FILTER_ROLE_ID, UserSearchDescriptor.ROLE_ID);
    }

    @Override
    public String convert(final String attribute) {

        return mapping.get(attribute);
    }
}
