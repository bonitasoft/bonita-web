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
package org.bonitasoft.web.rest.server.datastore.filter;

import java.io.Serializable;
import java.util.Map;

import org.bonitasoft.web.rest.server.datastore.converter.ValueConverter;
import org.bonitasoft.web.rest.server.framework.exception.APIFilterMandatoryException;

/**
 * @author Vincent Elcrin
 * 
 */
public class FilterAccessor {

    private Map<String, String> filters;

    public FilterAccessor(Map<String, String> filters) {
        this.filters = filters;
    }

    /**
     * @throws APIFilterMandatoryException
     *             if the value of the filter is null
     */
    public String getMandatory(String filter) {
        ensureFilterValue(filter);
        return getFilters().get(filter);
    }

    /**
     * @throws APIFilterMandatoryException
     *             In case of any exception during conversion
     */
    public <S extends Serializable> S getMandatory(String filter, ValueConverter<S> converter) {
        String value = getMandatory(filter);
        try {
            return converter.convert(value);
        } catch (Exception e) {
            throw new APIFilterMandatoryException(filter, e);
        }
    }

    private Map<String, String> getFilters() {
        return filters;
    }

    private void ensureFilterValue(String filter) {
        if (getFilters() == null || !getFilters().containsKey(filter)) {
            throw new APIFilterMandatoryException(filter);
        }
    }
}
