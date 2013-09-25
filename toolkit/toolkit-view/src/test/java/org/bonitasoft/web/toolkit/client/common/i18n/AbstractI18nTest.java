/*
 * Copyright (C) 2013 BonitaSoft S.A.
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

package org.bonitasoft.web.toolkit.client.common.i18n;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n.LOCALE;

/**
 * Created by Vincent Elcrin
 * Date: 23/09/13
 * Time: 16:25
 */
public class AbstractI18nTest {

    @Test
    public void testLOCALEConversion() throws Exception {
        assertEquals(LOCALE.fr_FR, LOCALE.valueOf("fr_FR"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyLocaleConversion() throws Exception {
        LOCALE.valueOf("");
    }
}
