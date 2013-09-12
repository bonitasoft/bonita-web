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
package org.bonitasoft.console.common.server.i18n;

import static junit.framework.Assert.assertEquals;

import java.io.File;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Vincent Elcrin
 * 
 */
public class POParserTest {

    Map<String, String> i18n;

    @Before
    public void setUp() {
        i18n = POParser.parse(new File("src/test/resources/test.po"));
    }

    @Test
    public void testToParseASimpleMsg() {
        assertEquals("theMsg", i18n.get("theId"));
    }

    @Test
    public void testToParseAMultiLinesMsg() {
        assertEquals("This is a multilines\nMessage", i18n.get("Multiline\nMessage\nId"));
    }

}
