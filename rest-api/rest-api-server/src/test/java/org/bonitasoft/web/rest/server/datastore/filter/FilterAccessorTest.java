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

import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.bonitasoft.web.rest.server.datastore.filter.FilterAccessor;
import org.bonitasoft.web.rest.server.datastore.profile.member.MemberTypeConverter;
import org.bonitasoft.web.rest.server.framework.exception.APIFilterMandatoryException;
import org.junit.Test;

/**
 * @author Vincent Elcrin
 * 
 */
public class FilterAccessorTest {

    @Test
    public void testWeCanRetrieveMandatoryValue() throws Exception {
        FilterAccessor filterAccess = new FilterAccessor(Collections.singletonMap("key", "value"));

        String value = filterAccess.getMandatory("key");

        assertEquals("value", value);
    }

    @Test(expected = APIFilterMandatoryException.class)
    public void testAccessToMandatoryValueWhichItDoesntExitThrowException() {
        FilterAccessor filterAccess = new FilterAccessor(Collections.<String, String> emptyMap());

        filterAccess.getMandatory("key");
    }

    @Test(expected = APIFilterMandatoryException.class)
    public void testAccessToMandatoryValueNotConvertibleThrowException() {
        FilterAccessor filterAccess = new FilterAccessor(Collections.singletonMap("key", "value"));

        filterAccess.getMandatory("key", new MemberTypeConverter());
    }
}
