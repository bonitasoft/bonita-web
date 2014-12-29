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
package org.bonitasoft.web.rest.server.api.bpm.cases;

import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.web.rest.model.bpm.cases.CaseVariableItem;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Colin PUY
 * 
 */
public class APICaseVariableAttributeCheckerTest {

    private APICaseVariableAttributeChecker attributeChecker;

    @Before
    public void initChecker() {
        attributeChecker = new APICaseVariableAttributeChecker();
    }

    private Map<String, String> buildSuitableUpdateAttributes() {
        Map<String, String> attributes = new HashMap<String, String>();
        attributes.put(CaseVariableItem.ATTRIBUTE_TYPE, String.class.getName());
        attributes.put(CaseVariableItem.ATTRIBUTE_VALUE, "aValue");
        return attributes;
    }

    private Map<String, String> buildSuitableSearchFilters() {
        Map<String, String> filters = new HashMap<String, String>();
        filters.put(CaseVariableItem.ATTRIBUTE_CASE_ID, "1");
        return filters;
    }

    @Test
    public void checkUpdateAttributesPassForSuitableValues() throws Exception {
        Map<String, String> attributes = buildSuitableUpdateAttributes();

        attributeChecker.checkUpdateAttributes(attributes);
    }

    @Test(expected = APIException.class)
    public void checkUpdateAttributesCheckForAttributeType() throws Exception {
        Map<String, String> attributes = buildSuitableUpdateAttributes();
        attributes.remove(CaseVariableItem.ATTRIBUTE_TYPE);

        attributeChecker.checkUpdateAttributes(attributes);
    }

    @Test(expected = APIException.class)
    public void checkUpdateAttributesCheckForAttributeValue() throws Exception {
        Map<String, String> attributes = buildSuitableUpdateAttributes();
        attributes.remove(CaseVariableItem.ATTRIBUTE_VALUE);

        attributeChecker.checkUpdateAttributes(attributes);
    }

    @Test
    public void checkSearchFiltersPassForSuitableValues() throws Exception {
        Map<String, String> filters = buildSuitableSearchFilters();

        attributeChecker.checkSearchFilters(filters);
    }

    @Test(expected = APIException.class)
    public void checkSearchFiltersCheckForCaseIdFilters() throws Exception {
        Map<String, String> filters = buildSuitableSearchFilters();
        filters.remove(CaseVariableItem.ATTRIBUTE_CASE_ID);

        attributeChecker.checkSearchFilters(filters);
    }
    
    @Test(expected = APIException.class)
    public void should_throw_exception_when_filters_are_null() throws Exception {
        attributeChecker.checkSearchFilters(null);
    }
    
    @Test(expected = APIException.class)
    public void should_throw_exception_when_attributes_are_null() throws Exception {
        attributeChecker.checkUpdateAttributes(null);
    }
}

