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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.bonitasoft.web.toolkit.client.common.i18n.I18n;
import org.bonitasoft.web.toolkit.client.data.item.attribute.modifier.CapitalizeModifier;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Paul AMAR
 * 
 */
public class CapitalizeModifierTest {

    @Before
    public void setUp() {
        I18n.getInstance();
    }

    @Test
    public void testCapitalize() {
        final CapitalizeModifier test = new CapitalizeModifier();
        final String result = test.clean("capitalize");
        assertEquals(result, "Capitalize");

    }

    @Test
    public void testCapitalizeErreur() {
        final CapitalizeModifier test = new CapitalizeModifier();
        final String result = test.clean("capitalize");
        assertFalse(result.equals("capitalize"));
    }
}
