/**
 * Copyright (C) 2012 BonitaSoft S.A.
 * 
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

import java.io.Serializable;

import org.bonitasoft.web.rest.model.identity.UserItem;
import org.bonitasoft.web.rest.server.datastore.converter.AttributeConverter;
import org.bonitasoft.web.rest.server.datastore.filter.BooleanValue;
import org.bonitasoft.web.rest.server.datastore.filter.Field;
import org.bonitasoft.web.rest.server.datastore.filter.Filter;
import org.bonitasoft.web.rest.server.datastore.filter.FilterCreator;
import org.bonitasoft.web.rest.server.datastore.filter.StrValue;

public class UserFilterCreator implements FilterCreator {

    private AttributeConverter fieldConverter;

    public UserFilterCreator() {
        this.fieldConverter = new UserSearchAttributeConverter();
    }

    @Override
    public Filter<? extends Serializable> create(String attribute, String value) {
        if (UserItem.ATTRIBUTE_ENABLED.equals(attribute)) {
            return new Filter<Boolean>(new Field(attribute, fieldConverter), new BooleanValue(value));
        }
        return new Filter<String>(new Field(attribute, fieldConverter), new StrValue(value));
    }
}
