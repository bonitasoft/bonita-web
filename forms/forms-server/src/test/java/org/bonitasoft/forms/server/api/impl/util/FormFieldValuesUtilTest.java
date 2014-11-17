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
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.bonitasoft.engine.expression.ExpressionType;
import org.bonitasoft.forms.client.model.Expression;
import org.bonitasoft.forms.client.model.FormValidator;
import org.bonitasoft.forms.client.model.FormWidget;
import org.bonitasoft.forms.server.accessor.impl.util.FormCacheUtil;
import org.bonitasoft.forms.server.accessor.impl.util.FormCacheUtilFactory;
import org.junit.Test;

/**
 * @author Vincent Elcrin
 *
 */
public class FormFieldValuesUtilTest {

    FormFieldValuesUtil util = new FormFieldValuesUtil();

    @Test
    public void testWeRetrieveExpressionOfDisplayedWidgetOnly() throws Exception {
        final List<FormWidget> widgets = Arrays.asList(
                aWidgetWithLabelExpression("widget1"),
                aWidgetWithLabelExpression("widget2"));

        final Map<String, Serializable> resolvedDisplayExp = new HashMap<String, Serializable>();
        resolvedDisplayExp.put(new WidgetExpressionEntry("widget1", ExpressionId.WIDGET_DISPLAY_CONDITION)
                .toString(), true);
        resolvedDisplayExp.put(new WidgetExpressionEntry("widget2", ExpressionId.WIDGET_DISPLAY_CONDITION)
                .toString(), false);

        final List<Expression> expressions = util.getExpressionsToEvaluation(
                widgets,
                resolvedDisplayExp,
                new HashMap<String, Object>());

        assertEquals(1, expressions.size());
        assertEquals("widget1:label", expressions.get(0).getName());
    }

    @Test
    public void testWeRetrieveExpressionOfWidgetWithoutDisplayExpressions() throws Exception {
        final List<FormWidget> widgets = Arrays.asList(aWidgetWithLabelExpression("widget"));

        final List<Expression> expressions = util.getExpressionsToEvaluation(
                widgets,
                new HashMap<String, Serializable>(),
                new HashMap<String, Object>());

        assertEquals(1, expressions.size());
        assertEquals("widget:label", expressions.get(0).getName());
    }

    @Test
    public void testWeDoNotRetrieveDisplayExpressionOfWidgetNotDisplayed() {
        final FormWidget widget = new FormWidget();
        widget.setId("widget");
        final Expression expression = new Expression();
        expression.setName("expression");
        widget.setDisplayConditionExpression(expression);

        final Map<String, Serializable> resolvedDisplayExp = new HashMap<String, Serializable>();
        resolvedDisplayExp.put(new WidgetExpressionEntry("widget", ExpressionId.WIDGET_DISPLAY_CONDITION)
                .toString(), false);

        final List<Expression> expressions = util.getExpressionsToEvaluation(
                Arrays.asList(widget),
                resolvedDisplayExp,
                new HashMap<String, Object>());

        assertTrue(expressions.isEmpty());
    }

    FormWidget aWidgetWithLabelExpression(final String id) {
        final FormWidget fw = new FormWidget();
        fw.setId(id);
        fw.setLabelExpression(new Expression());
        return fw;
    }

    @Test
    public void testStoreWidgetsInCacheAndSetCacheID() {
        final List<FormWidget> formWidgets = new ArrayList<FormWidget>();
        final FormWidget widget1 = new FormWidget();
        widget1.setId("widget1");
        formWidgets.add(widget1);
        final Date processDeploymentDate = new Date();
        util.storeWidgetsInCacheAndSetCacheID(1, "formID", "pageID", Locale.ENGLISH.toString(), processDeploymentDate, formWidgets);
        final String formWidgetCacheID = widget1.getFormWidgetCacheId();
        final FormWidget formWidget1RetrievedFromCacheByCacheID = FormCacheUtilFactory.getTenantFormCacheUtil(1).getFormWidget(formWidgetCacheID);
        assertNotNull(formWidget1RetrievedFromCacheByCacheID);
        assertEquals("widget1", formWidget1RetrievedFromCacheByCacheID.getId());
        assertFalse(formWidget1RetrievedFromCacheByCacheID.hasDynamicValue());
    }

    @Test
    public void testStoreWidgetsInCacheAndSetCacheIDWithValidators() {
        final List<FormWidget> formWidgets = new ArrayList<FormWidget>();
        final FormWidget widget1 = new FormWidget();
        widget1.setId("widget1");
        widget1.setInitialValueExpression(new Expression("name", "initial value", ExpressionType.TYPE_READ_ONLY_SCRIPT.name(), String.class.getName(),
                "GROOVY", null));
        final List<FormValidator> validators = new ArrayList<FormValidator>();
        final FormValidator validator1 = new FormValidator();
        validator1.setId("validator1");
        validators.add(validator1);
        widget1.setValidators(validators);
        formWidgets.add(widget1);
        final Date processDeploymentDate = new Date();
        util.storeWidgetsInCacheAndSetCacheID(1, "formID", "pageID", Locale.ENGLISH.toString(), processDeploymentDate, formWidgets);
        final String formWidgetCacheID = widget1.getFormWidgetCacheId();

        final FormCacheUtil formCacheUtil = FormCacheUtilFactory.getTenantFormCacheUtil(1);

        final FormWidget formWidget1RetrievedFromCacheByCacheID = formCacheUtil.getFormWidget(formWidgetCacheID);
        assertNotNull(formWidget1RetrievedFromCacheByCacheID);
        assertEquals("widget1", formWidget1RetrievedFromCacheByCacheID.getId());
        assertTrue(formWidget1RetrievedFromCacheByCacheID.hasDynamicValue());
        final List<FormValidator> fieldValidatorsRetrievedFromCacheByCacheID = formCacheUtil.getFieldValidators(widget1.getValidatorsCacheId());
        assertNotNull(fieldValidatorsRetrievedFromCacheByCacheID);
        assertThat(fieldValidatorsRetrievedFromCacheByCacheID, hasSize(1));
        assertEquals("validator1", fieldValidatorsRetrievedFromCacheByCacheID.get(0).getId());
    }
}
