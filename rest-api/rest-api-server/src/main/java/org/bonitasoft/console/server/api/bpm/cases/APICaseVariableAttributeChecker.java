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
package org.bonitasoft.console.server.api.bpm.cases;

import static java.lang.String.format;

import java.util.Map;

import org.bonitasoft.web.rest.api.model.bpm.cases.CaseVariableItem;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;

/**
 * @author Colin PUY
 * 
 */
public class APICaseVariableAttributeChecker {

    public void checkUpdateAttributes(Map<String, String> attributes) {
        if (!attributes.containsKey(CaseVariableItem.ATTRIBUTE_TYPE)) {
            throw new APIException(format("Attribute '%s' must be specified", CaseVariableItem.ATTRIBUTE_TYPE));
        }
        if (!attributes.containsKey(CaseVariableItem.ATTRIBUTE_VALUE)) {
            throw new APIException(format("Attribute '%s' must be specified", CaseVariableItem.ATTRIBUTE_VALUE));
        }
    }
    
    public void checkSearchFilters(Map<String, String> filters) {
        if (!filters.containsKey(CaseVariableItem.ATTRIBUTE_CASE_ID)) {
            throw new APIException(format("Filter '%s' must be specified", CaseVariableItem.ATTRIBUTE_CASE_ID));
        }
    }
}
