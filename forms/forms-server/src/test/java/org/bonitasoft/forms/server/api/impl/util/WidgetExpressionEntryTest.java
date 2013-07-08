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
package org.bonitasoft.forms.server.api.impl.util;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * @author Vincent Elcrin
 * 
 */
public class WidgetExpressionEntryTest {

    @Test
    public void testTwoWidgetExpressionEntryEqualAREEquals() {
        WidgetExpressionEntry widget1 =
                new WidgetExpressionEntry("widgetId", ExpressionId.WIDGET_DISPLAY_CONDITION);
        WidgetExpressionEntry widget2 =
                new WidgetExpressionEntry("widgetId", ExpressionId.WIDGET_DISPLAY_CONDITION);

        boolean result = widget1.equals(widget2);

        assertTrue(result);
    }

    @Test
    public void testWidgetExpressionEntryIsNotEqualsToNull() {
        WidgetExpressionEntry widget =
                new WidgetExpressionEntry("widgetId", ExpressionId.WIDGET_DISPLAY_CONDITION);

        boolean result = widget.equals(null);

        assertFalse(result);
    }

    @Test
    public void testWeCanRetrieveWidgetExpressionEntryFromMap() {
        WidgetExpressionEntry widget =
                new WidgetExpressionEntry("widgetId", ExpressionId.WIDGET_DISPLAY_CONDITION);
        Map<WidgetExpressionEntry, Boolean> map = new HashMap<WidgetExpressionEntry, Boolean>();

    }
}
