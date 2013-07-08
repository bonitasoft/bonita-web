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

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bonitasoft.forms.client.model.Expression;
import org.bonitasoft.forms.client.model.FormWidget;
import org.bonitasoft.forms.client.model.exception.SessionTimeoutException;
import org.bonitasoft.forms.server.api.impl.util.MapUtils.Action;
import org.bonitasoft.forms.server.exception.FileTooBigException;
import org.bonitasoft.forms.server.exception.FormNotFoundException;
import org.bonitasoft.forms.server.provider.FormServiceProvider;

/**
 * @author Vincent Elcrin
 * 
 */
public class DisplayConfigurations {

    private List<FormWidget> widgets;

    private FormServiceProvider formServiceProvider;

    private Map<String, Object> context;

    abstract class DisplayCondition {

        abstract public boolean isDisplayed(List<Expression> lazyEval);
    }

    public DisplayConfigurations(
            List<FormWidget> widgets,
            FormServiceProvider formServiceProvider,
            Map<String, Object> context) {
        this.widgets = widgets;
        this.formServiceProvider = formServiceProvider;
        this.context = context;
    }

    public Map<String, Boolean> asMap()
            throws FormNotFoundException,
            SessionTimeoutException,
            FileTooBigException,
            IOException {
        final List<Expression> lazyEvals = new ArrayList<Expression>();

        final Map<String, Boolean> widgetDisplayConfigurations =
                buildDisplayedWidgetsConfiguration(
                        buildDisplayConditions(widgets),
                        lazyEvals);

        adjustDisplayWidgetConfiguration(formServiceProvider.resolveExpressions(lazyEvals, context),
                widgetDisplayConfigurations,
                lazyEvals);

        return widgetDisplayConfigurations;
    }

    private Map<String, Boolean> buildDisplayedWidgetsConfiguration(Map<String, DisplayCondition> conditions,
            final List<Expression> expressions) {

        final Map<String, Boolean> displayConfigurations = new HashMap<String, Boolean>();
        MapUtils.foreach(conditions,
                new Action<String, DisplayCondition>() {

                    @Override
                    public void apply(Entry<String, DisplayCondition> entry) {
                        displayConfigurations.put(entry.getKey(),
                                entry.getValue().isDisplayed(expressions));
                    }
                });

        return displayConfigurations;
    }

    /**
     * Adjust insertionResult with expressions resolution result
     * 
     */
    private void adjustDisplayWidgetConfiguration(Map<String, Serializable> resolveExpressions,
            final Map<String, Boolean> widgetDisplayConfigurations,
            List<Expression> expressions) {

        MapUtils.foreach(resolveExpressions,
                new Action<String, Serializable>() {

                    @Override
                    public void apply(Entry<String, Serializable> result) {
                        widgetDisplayConfigurations.put(
                                result.getKey(),
                                Boolean.valueOf(result.getValue().toString()));
                    }

                });
    }

    private Map<String, DisplayCondition> buildDisplayConditions(final List<FormWidget> widgets) {
        Map<String, DisplayCondition> displayConditions = new HashMap<String, DisplayCondition>();

        for (final FormWidget widget : widgets) {
            displayConditions.put(widget.getId(), new DisplayCondition() {

                @Override
                public boolean isDisplayed(List<Expression> lazyEval) {
                    Expression expression = widget.getDisplayConditionExpression();
                    if (expression != null) {
                        expression.setName(widget.getId());
                        lazyEval.add(expression);
                    }
                    /*
                     * All widgets are supposedly displayed.
                     * Only expression evaluation can stop a
                     * widget to be displayed (inserted).
                     */
                    return true;
                }
            });

            /*
             * Add group's children display conditions recursively.
             */
            List<FormWidget> children = widget.getChildWidgets();
            if (children != null) {
                displayConditions.putAll(buildDisplayConditions(children));
            }
        }
        return displayConditions;
    }
}
