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

import static org.junit.Assert.assertFalse;

import org.bonitasoft.web.toolkit.client.common.i18n.I18n;
import org.bonitasoft.web.toolkit.client.data.item.attribute.modifier.RemoveHtmlModifier;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Paul AMAR
 * 
 */
public class RemoveHtmlModifierTest {

    @Before
    public void setUp() {
        I18n.getInstance();
    }

    // FIXME: Gérer la regular expression des HTML tags.
    // @Test
    /*
     * public void TestRemoveHTMLOk() {
     * final RemoveHtmlModifier test = new RemoveHtmlModifier();
     * final String result = test.clean("<script>Hey ! </script>");
     * assertTrue(result.equals("Hey ! "));
     * }
     */
    @Test
    public void TestRemoveHTMLErreur() {
        final RemoveHtmlModifier test = new RemoveHtmlModifier();
        final String result = test.clean("<script> Hey ! </script>");
        assertFalse(result.equals("<script> Hey ! </script>"));
    }
}
