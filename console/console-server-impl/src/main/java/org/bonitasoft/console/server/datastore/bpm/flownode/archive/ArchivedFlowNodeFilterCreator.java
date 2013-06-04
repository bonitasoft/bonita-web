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
package org.bonitasoft.console.server.datastore.bpm.flownode.archive;

import java.io.Serializable;

import org.bonitasoft.console.client.model.bpm.flownode.ArchivedFlowNodeItem;
import org.bonitasoft.console.server.datastore.bpm.flownode.archive.converter.ArchivedFlowNodeSearchDescriptorConverter;
import org.bonitasoft.console.server.datastore.converter.BooleanValueConverter;
import org.bonitasoft.console.server.datastore.filter.Field;
import org.bonitasoft.console.server.datastore.filter.Filter;
import org.bonitasoft.console.server.datastore.filter.FilterCreator;
import org.bonitasoft.console.server.datastore.filter.StrValue;
import org.bonitasoft.console.server.datastore.filter.Value;

/**
 * @author Vincent Elcrin
 * 
 */
public class ArchivedFlowNodeFilterCreator implements FilterCreator {

    private ArchivedFlowNodeSearchDescriptorConverter converter;

    public ArchivedFlowNodeFilterCreator(ArchivedFlowNodeSearchDescriptorConverter converter) {
        this.converter = converter;
    }

    @Override
    public Filter<? extends Serializable> create(String attribute, String value) {
        if (ArchivedFlowNodeItem.FILTER_IS_TERMINAL.equals(attribute)) {
            return new Filter<Boolean>(createField(attribute),
                    createBooleanValue(value));

        } else {
            return new Filter<String>(createField(attribute),
                    new StrValue(value));
        }
    }

    private Value<Boolean> createBooleanValue(String value) {
        return new Value<Boolean>(value, new BooleanValueConverter());
    }

    private Field createField(String attribute) {
        return new Field(attribute, converter);
    }
}
