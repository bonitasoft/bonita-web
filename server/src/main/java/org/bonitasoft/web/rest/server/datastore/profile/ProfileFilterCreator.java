/**
 * Copyright (C) 2017 Bonitasoft S.A.
 * Bonitasoft, 32 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.web.rest.server.datastore.profile;

import java.io.Serializable;

import org.bonitasoft.web.rest.model.portal.profile.ProfileItem;
import org.bonitasoft.web.rest.server.datastore.converter.AttributeConverter;
import org.bonitasoft.web.rest.server.datastore.filter.Field;
import org.bonitasoft.web.rest.server.datastore.filter.Filter;
import org.bonitasoft.web.rest.server.datastore.filter.Filter.Operator;
import org.bonitasoft.web.rest.server.datastore.filter.GenericFilterCreator;

public class ProfileFilterCreator extends GenericFilterCreator {

    public ProfileFilterCreator(AttributeConverter fieldConverter) {
        super(fieldConverter);
    }

    /*
     * (non-Javadoc)
     * @see org.bonitasoft.web.rest.server.datastore.filter.GenericFilterCreator#create(java.lang.String, java.lang.String)
     */
    @Override
    public Filter<? extends Serializable> create(String attribute, String value) {
        if (ProfileItem.FILTER_HAS_NAVIGATION.equals(attribute)) {
            return Boolean.valueOf(value)
                    ? new Filter<String>(new Field(attribute, fieldConverter), null, Operator.DIFFERENT_FROM)
                    : new Filter<String>(new Field(attribute, fieldConverter), null);
        }
        return super.create(attribute, value);
    }
}
