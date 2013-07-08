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

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bonitasoft.forms.client.model.Expression;
import org.bonitasoft.forms.client.model.FormWidget;
import org.bonitasoft.forms.server.provider.FormServiceProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

/**
 * @author Vincent Elcrin
 * 
 */
public class DisplayConfigurationsTest {

    static final Boolean DISPLAYED = true;

    static final Boolean NOT_DISPLAYED = false;

    static final Boolean DISPLAY_EXPRESSION = true;

    @Mock
    FormServiceProvider formServiceProvider;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void testWeCanGetWidgetDisplayConfigurations() throws Exception {
        List<FormWidget> widgets = Arrays.asList(
                aWidget("widget", DISPLAY_EXPRESSION),
                aGroup("group", DISPLAY_EXPRESSION,
                        Arrays.asList(aWidget("child"))));
        mockExpressionResults(
                new Result("widget", DISPLAYED),
                new Result("group", NOT_DISPLAYED));

        Map<String, Boolean> configurations = new DisplayConfigurations(widgets, formServiceProvider, null).asMap();

        assertEquals(3, configurations.size());
        assertEquals(DISPLAYED, configurations.get("widget"));
        assertEquals(NOT_DISPLAYED, configurations.get("group"));
        assertEquals(DISPLAYED, configurations.get("child"));
    }

    @Test
    public void testWeCanGetDisplayConfigurationsOfNestedGroups() throws Exception {
        List<FormWidget> widgets = Arrays.asList(
                aGroup("group1", DISPLAY_EXPRESSION,
                        Arrays.asList(aGroup("group2", DISPLAY_EXPRESSION,
                                Arrays.asList(aWidget("child"))))));
        mockExpressionResults(
                new Result("group1", DISPLAYED),
                new Result("group2", NOT_DISPLAYED));

        Map<String, Boolean> configurations = new DisplayConfigurations(widgets, formServiceProvider, null).asMap();

        assertEquals(3, configurations.size());
        assertEquals(DISPLAYED, configurations.get("group1"));
        assertEquals(NOT_DISPLAYED, configurations.get("group2"));
        assertEquals(DISPLAYED, configurations.get("child"));
    }

    public class Result {

        private String widgetId;

        private Boolean isDisplayed;

        public Result(String widgetId, Boolean isDisplayed) {
            this.widgetId = widgetId;
            this.isDisplayed = isDisplayed;

        }

        public String getWidgetId() {
            return widgetId;
        }

        public Boolean isDisplayed() {
            return isDisplayed;
        }

    }

    void mockExpressionResults(Result... resolutions)
            throws Exception {
        Map<String, Serializable> results = new HashMap<String, Serializable>();
        for (Result resolution : resolutions) {
            results.put(resolution.getWidgetId(), resolution.isDisplayed());
        }
        when(formServiceProvider.resolveExpressions(
                anyListOf(Expression.class),
                anyMapOf(String.class, Object.class)))
                .thenReturn(results);
    }

    FormWidget aWidget(String id) {
        return aWidget(id, false);
    }

    FormWidget aWidget(String id, boolean hasDisplayExpression) {
        FormWidget fw = new FormWidget();
        fw.setId(id);
        if (hasDisplayExpression) {
            fw.setDisplayConditionExpression(new Expression());
        }
        return fw;
    }

    FormWidget aGroup(String id, boolean hasDisplayExpression, List<FormWidget> children) {
        FormWidget gw = new FormWidget();
        gw.setId(id);
        if (hasDisplayExpression) {
            gw.setDisplayConditionExpression(new Expression());
        }
        gw.setChildWidgets(children);
        return gw;
    }

}
