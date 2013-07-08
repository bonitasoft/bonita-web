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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bonitasoft.forms.client.model.Expression;
import org.bonitasoft.forms.client.model.FormWidget;

/**
 * @author Vincent Elcrin
 * 
 */
public class DisplayExpressions {

    private List<FormWidget> widgets;

    protected static final String WIDGET_DISPLAY_CONDITION = "display-condition";

    public DisplayExpressions(List<FormWidget> widgets) {
        assert widgets != null;
        this.widgets = widgets;
    }

    public Map<WidgetExpressionEntry, Expression> asMap() {

        Map<WidgetExpressionEntry, Expression> expressions =
                new HashMap<WidgetExpressionEntry, Expression>();

        for (final FormWidget widget : widgets) {
            Expression expression = widget.getDisplayConditionExpression();
            if (expression != null) {
                WidgetExpressionEntry widgetExpressionEntry =
                        new WidgetExpressionEntry(widget.getId(), ExpressionId.WIDGET_DISPLAY_CONDITION);
                expression.setName(widgetExpressionEntry.toString());
                expressions.put(widgetExpressionEntry, expression);
            }

            List<FormWidget> children = widget.getChildWidgets();
            if (children != null) {
                expressions.putAll(new DisplayExpressions(children).asMap());
            }
        }
        return expressions;
    }

    public List<Expression> asList() {
        return new ArrayList<Expression>(asMap().values());
    }
}
