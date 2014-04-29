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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.web.rest.server.datastore.filter;

import java.io.Serializable;

import org.bonitasoft.engine.bpm.flownode.ActivityInstanceSearchDescriptor;
import org.bonitasoft.engine.bpm.flownode.FlowNodeType;
import org.bonitasoft.web.rest.model.bpm.flownode.ActivityItem;
import org.bonitasoft.web.rest.server.datastore.bpm.flownode.FlowNodeTypeConverter;
import org.bonitasoft.web.rest.server.datastore.converter.ActivityAttributeConverter;

/**
 * @author Florine Boudin
 *
 */
public class ActivityFilterCreator implements FilterCreator {

	/* (non-Javadoc)
	 * @see org.bonitasoft.web.rest.server.datastore.filter.FilterCreator#create(java.lang.String, java.lang.String)
	 */
	@Override
	public Filter<? extends Serializable> create(String attribute, String value) {
		if(ActivityItem.ATTRIBUTE_TYPE.equals(attribute)){
       return new Filter<FlowNodeType>(new Field(attribute, new ActivityAttributeConverter()), new Value<FlowNodeType>(value, new FlowNodeTypeConverter()));
		}
		return new GenericFilterCreator(new ActivityAttributeConverter()).create(attribute, value);
	}

}
