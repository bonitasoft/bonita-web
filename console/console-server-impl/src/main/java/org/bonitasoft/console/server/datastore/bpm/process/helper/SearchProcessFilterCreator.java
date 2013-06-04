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
package org.bonitasoft.console.server.datastore.bpm.process.helper;

import java.io.Serializable;

import org.bonitasoft.console.server.datastore.filter.Field;
import org.bonitasoft.console.server.datastore.filter.Filter;
import org.bonitasoft.console.server.datastore.filter.FilterCreator;
import org.bonitasoft.console.server.datastore.filter.StrValue;

/**
 * @author Vincent Elcrin
 * 
 */
public class SearchProcessFilterCreator implements FilterCreator {

    @Override
    public Filter<? extends Serializable> create(String attribute, String value) {
        return new Filter<String>(createConvertedField(attribute), new StrValue(value));
    }

    private Field createConvertedField(String attribute) {
        return new Field(attribute, new ProcessSearchDescriptorConverter());
    }
}
