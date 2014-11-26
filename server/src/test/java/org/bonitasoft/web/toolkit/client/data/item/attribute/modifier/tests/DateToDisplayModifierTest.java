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
package org.bonitasoft.web.toolkit.client.data.item.attribute.modifier.tests;

import org.bonitasoft.console.common.server.i18n.I18n;
import org.bonitasoft.web.toolkit.client.common.CommonDateFormater;
import org.bonitasoft.web.toolkit.client.data.item.attribute.modifier.DateToDisplayModifier;
import org.bonitasoft.web.toolkit.server.utils.ServerDateFormater;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @author Paul AMAR
 * 
 */
public class DateToDisplayModifierTest {

    @Before
    public void setUp() {
        I18n.getInstance();
        CommonDateFormater.setDateFormater(new ServerDateFormater());
    }

    @Test
    public void testDateToDisplayModifierOK() {
        final DateToDisplayModifier test = new DateToDisplayModifier();
        final String result = test.clean("2012-09-28 01:13:37.666");
        assertTrue(result.equals("09/28/2012 1:13 AM"));
    }

}
