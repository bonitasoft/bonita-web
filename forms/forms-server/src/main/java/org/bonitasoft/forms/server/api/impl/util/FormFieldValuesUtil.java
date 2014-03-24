/**
 * Copyright (C) 2009 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
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

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bonitasoft.engine.expression.ExpressionType;
import org.bonitasoft.forms.client.model.Expression;
import org.bonitasoft.forms.client.model.FileWidgetInputType;
import org.bonitasoft.forms.client.model.FormFieldAvailableValue;
import org.bonitasoft.forms.client.model.FormFieldValue;
import org.bonitasoft.forms.client.model.FormWidget;
import org.bonitasoft.forms.client.model.ReducedFormFieldAvailableValue;
import org.bonitasoft.forms.client.model.WidgetType;
import org.bonitasoft.forms.client.model.exception.SessionTimeoutException;
import org.bonitasoft.forms.server.accessor.impl.util.FormCacheUtil;
import org.bonitasoft.forms.server.accessor.impl.util.FormCacheUtilFactory;
import org.bonitasoft.forms.server.exception.FileTooBigException;
import org.bonitasoft.forms.server.exception.FormInitializationException;
import org.bonitasoft.forms.server.exception.FormNotFoundException;
import org.bonitasoft.forms.server.exception.FormServiceProviderNotFoundException;
import org.bonitasoft.forms.server.provider.FormServiceProvider;
import org.bonitasoft.forms.server.provider.impl.util.FormServiceProviderFactory;
import org.bonitasoft.forms.server.provider.impl.util.FormServiceProviderUtil;

/**
 * @author Anthony Birembaut, Haojie Yuan
 * 
 */
public class FormFieldValuesUtil {

    protected static final String INDEX_SEPARATOR = "_";

    protected static final String WIDGET_LABEL = "label";

    public static final String EXPRESSION_KEY_SEPARATOR = ":";

    protected static final String WIDGET_TITLE = "title";

    protected static final String WIDGET_SUBTITLE = "subtitle";

    protected static final String WIDGET_TOOLTIP = "tooltip";

    protected static final String WIDGET_DISPLAY_CONDITION = "display-condition";

    protected static final String WIDGET_AVAILABLE_VALUE_LABEL = "available-value-label";

    protected static final String WIDGET_AVAILABLE_VALUE_VALUE = "available-value-value";

    protected static final String WIDGET_AVAILABLE_VALUES = "available-values";

    protected static final String WIDGET_VALUE_COLUMN_INDEX = "value-column-index";

    protected static final String WIDGET_MAX_COLUMNS = "max-columns";

    protected static final String WIDGET_MIN_COLUMNS = "min-columns";

    protected static final String WIDGET_MAX_ROWS = "max-rows";

    protected static final String WIDGET_MIN_ROWS = "min-rows";

    protected static final String WIDGET_VERTICAL_HEADER = "verical-header";

    protected static final String WIDGET_HORIZONTAL_HEADER = "horizontal-header";

    protected static final String WIDGET_INITIAL_VALUE = "initial-value";

    protected static final String EXPRESSION_TYPE = "SCRIPT";

    /**
     * Logger
     */
    private static Logger LOGGER = Logger.getLogger(FormFieldValuesUtil.class.getName());

    /**
     * default dateformat pattern
     */
    protected String defaultDateFormatPattern;

    /**
     * Build a field value object from the process definition
     * 
     * @param value
     *            the value retrieved from the engine
     * @param formWidget
     *            the {@link FormWidget} associated with this field value
     * @return a {@link FormFieldValue} object
     */
    public FormFieldValue getFieldValue(final Object value, final FormWidget formWidget, final Locale locale) {
        FormFieldValue fieldValue = null;
        if (value != null) {
            // deals with the types whose corresponding form fields
            // input and output value is not a string
            if (value instanceof Date) {
                String formatPattern = null;
                if (formWidget.getDisplayFormat() != null && formWidget.getDisplayFormat().length() > 0) {
                    formatPattern = formWidget.getDisplayFormat();
                } else {
                    formatPattern = defaultDateFormatPattern;
                }
                fieldValue = new FormFieldValue((Date) value, Date.class.getName(), formatPattern);
            } else if (value instanceof Collection<?>) {
                // perform several levels of toString on the items of the collection
                fieldValue = new FormFieldValue((Serializable) processCollectionValue(value), Collection.class.getName());
            } else if (value instanceof String && formWidget.getType().equals(WidgetType.DATE)) {
                final String valueStr = (String) value;
                String formatPattern = null;
                if (formWidget.getDisplayFormat() != null && formWidget.getDisplayFormat().length() > 0) {
                    formatPattern = formWidget.getDisplayFormat();
                } else {
                    formatPattern = defaultDateFormatPattern;
                }
                Date dateValue = null;
                if (valueStr.length() > 0) {
                    try {
                        final DateFormat dateFormat = new SimpleDateFormat(formatPattern, locale);
                        dateValue = dateFormat.parse(valueStr);
                    } catch (final ParseException e) {
                        if (LOGGER.isLoggable(Level.SEVERE)) {
                            LOGGER.log(Level.SEVERE, "The initial value for widget " + formWidget.getId() + " is not consistent with the pattern "
                                    + formatPattern, e);
                        }
                    }
                }
                fieldValue = new FormFieldValue(dateValue, Date.class.getName(), formatPattern);
            } else if (value instanceof String && formWidget.getType().equals(WidgetType.CHECKBOX)) {
                Boolean booleanValue = null;
                try {
                    booleanValue = Boolean.parseBoolean((String) value);
                } catch (final Exception e) {
                    if (LOGGER.isLoggable(Level.SEVERE)) {
                        LOGGER.log(Level.SEVERE, "The initial value for widget " + formWidget.getId() + " is not consistent with a boolean.", e);
                    }
                    booleanValue = Boolean.valueOf(false);
                }
                fieldValue = new FormFieldValue(booleanValue, Boolean.class.getName());
            } else if (value instanceof Serializable) {
                fieldValue = new FormFieldValue((Serializable) value, value.getClass().getName());
            } else {
                fieldValue = new FormFieldValue(value.toString(), String.class.getName());
            }
        } else {
            fieldValue = getFieldNullValue(formWidget);
        }
        return fieldValue;
    }

    protected List<Serializable> processCollectionValue(final Object value) {
        final List<Serializable> valueList = new ArrayList<Serializable>();
        final Collection<?> collectionValues = (Collection<?>) value;
        for (final Object collectionValue : collectionValues) {
            if (collectionValue == null) {
                valueList.add(null);
            } else if (collectionValue instanceof Collection<?>) {
                valueList.add((Serializable) processCollectionValue(collectionValue));
            } else if (collectionValue instanceof Serializable) {
                valueList.add((Serializable) collectionValue);
            } else {
                valueList.add(collectionValue.toString());
            }
        }
        return valueList;
    }

    protected FormFieldValue getFieldNullValue(final FormWidget formWidget) {
        FormFieldValue fieldValue = null;
        if (formWidget.getType().equals(WidgetType.CHECKBOX)) {
            fieldValue = new FormFieldValue(Boolean.FALSE, Boolean.class.getName());
        } else if (formWidget.getType().equals(WidgetType.LISTBOX_SIMPLE) || formWidget.getType().equals(WidgetType.RADIOBUTTON_GROUP)) {
            if (formWidget.getAvailableValues() != null && formWidget.getAvailableValues().size() > 0) {
                fieldValue = new FormFieldValue(formWidget.getAvailableValues().get(0).getValue(), String.class.getName());
            } else {
                fieldValue = new FormFieldValue("", String.class.getName());
            }
        } else if (formWidget.getType().equals(WidgetType.DATE)) {
            String formatPattern = null;
            if (formWidget.getDisplayFormat() != null && formWidget.getDisplayFormat().length() > 0) {
                formatPattern = formWidget.getDisplayFormat();
            } else {
                formatPattern = defaultDateFormatPattern;
            }
            fieldValue = new FormFieldValue(null, Date.class.getName(), formatPattern);
        } else if (formWidget.getType().equals(WidgetType.DURATION)) {
            fieldValue = new FormFieldValue(Long.valueOf(0), Long.class.getName());
        } else {
            fieldValue = new FormFieldValue(null, Serializable.class.getName());
        }
        return fieldValue;
    }

    /**
     * Build a FormFieldAvailableValue List from a String {@link Collection}
     * 
     * @param collection
     * @return a List of {@link FormFieldAvailableValue}
     */
    protected List<ReducedFormFieldAvailableValue> getAvailableValuesFromCollection(final Collection<?> collection) {
        final List<ReducedFormFieldAvailableValue> availableValues = new ArrayList<ReducedFormFieldAvailableValue>();
        for (final Object availableValue : collection) {
            final String availableValueStr = getStringValue(availableValue);
            availableValues.add(new ReducedFormFieldAvailableValue(availableValueStr, availableValueStr));
        }
        return availableValues;
    }

    /**
     * Build a FormFieldAvailableValue List from a {@link Map}
     * 
     * @param availableValuesMap
     * @return a List of {@link FormFieldAvailableValue}
     */
    protected List<ReducedFormFieldAvailableValue> getAvailableValuesFromMap(final Map<?, ?> availableValuesMap) {
        final List<ReducedFormFieldAvailableValue> availableValues = new ArrayList<ReducedFormFieldAvailableValue>();
        for (final Entry<?, ?> availableValueEntry : availableValuesMap.entrySet()) {
            final Object key = availableValueEntry.getKey();
            final String keyStr = getStringValue(key);
            final Object value = availableValueEntry.getValue();
            final String valueStr = getStringValue(value);
            availableValues.add(new ReducedFormFieldAvailableValue(keyStr, valueStr));
        }
        return availableValues;
    }

    /**
     * Build a FormFieldAvailableValue List from a {@link Map} or a {@link List} and set it in the widget
     * 
     * @param availableValuesObject
     *            the {@link Map} or {@link List} of values
     * @param widgetId
     *            the widget Id
     * @return a List of {@link FormFieldAvailableValue}
     */
    public List<ReducedFormFieldAvailableValue> getAvailableValues(final Object availableValuesObject, final String widgetId) {

        List<ReducedFormFieldAvailableValue> availableValuesList = new ArrayList<ReducedFormFieldAvailableValue>();
        try {
            availableValuesList = getAvailableValues(availableValuesObject);
        } catch (final IllegalArgumentException e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "The available values expression for widget " + widgetId + " should return a Collection or a Map");
            }
        }
        return availableValuesList;
    }

    /**
     * Build a FormFieldAvailableValue List from a {@link Map} or a {@link List} and set it in the widget
     * 
     * @param availableValuesObject
     *            the {@link Map} or {@link List} of values
     * @param widget
     *            the widget to set
     * @return a List of {@link FormFieldAvailableValue}
     * @throws IllegalArgumentException
     */
    protected List<ReducedFormFieldAvailableValue> getAvailableValues(final Object availableValuesObject) throws IllegalArgumentException {

        List<ReducedFormFieldAvailableValue> availableValuesList = null;
        if (availableValuesObject != null && availableValuesObject instanceof Collection<?>) {
            final Collection<?> availableValuesCollection = (Collection<?>) availableValuesObject;
            availableValuesList = getAvailableValuesFromCollection(availableValuesCollection);
        } else if (availableValuesObject != null && availableValuesObject instanceof Map<?, ?>) {
            final Map<?, ?> availableValuesMap = (Map<?, ?>) availableValuesObject;
            availableValuesList = getAvailableValuesFromMap(availableValuesMap);
        } else {
            throw new IllegalArgumentException();
        }
        return availableValuesList;
    }

    /**
     * Build a FormFieldAvailableValue List of List from a {@link List} of {@link Map} or a {@link List} of {@link List} and set it in the widget
     * 
     * @param availableValuesObject
     *            the {@link List} of {@link Map} or {@link List} of {@link List} of values
     * @param widget
     *            the widget to set
     * @return a List of List of {@link FormFieldAvailableValue}
     */
    public List<List<ReducedFormFieldAvailableValue>> getTableAvailableValues(final Object tableAvailableValuesObject, final String widgetId) {

        final List<List<ReducedFormFieldAvailableValue>> tableAvailableValuesList = new ArrayList<List<ReducedFormFieldAvailableValue>>();
        try {
            final Collection<?> availableValuesObjects = (Collection<?>) tableAvailableValuesObject;
            for (final Object availableValuesObject : availableValuesObjects) {
                tableAvailableValuesList.add(getAvailableValues(availableValuesObject));
            }
        } catch (final ClassCastException e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "The available values expression for widget " + widgetId + " should return a Collection of Collections or Maps", e);
            }
        }
        return tableAvailableValuesList;
    }

    /**
     * Retrieve the String value of an object
     * 
     * @param object
     *            the object
     * @return the String representation of this Object
     */
    protected String getStringValue(final Object object) {
        if (object != null) {
            return object.toString();
        }
        return null;
    }

    /**
     * Get display condition
     * 
     * @param conditionExpression
     * @param condition
     * @throws FormNotFoundException
     * @throws FormServiceProviderNotFoundException
     */
    protected boolean getDisplayConditionStr(final Expression conditionExpression, final Object conditionObject) throws FormNotFoundException,
            FormServiceProviderNotFoundException {

        boolean condition = true;
        if (conditionObject != null) {
            if (conditionObject instanceof String) {
                if (Boolean.FALSE.toString().equals(conditionObject)) {
                    condition = false;
                } else if (Boolean.TRUE.toString().equals(conditionObject)) {
                    condition = true;
                } else {
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        String conditionStr = null;
                        if (conditionExpression != null) {
                            conditionStr = conditionExpression.getContent();
                        }
                        LOGGER.log(Level.WARNING, "the display condition constant is not true or false: " + conditionStr);
                    }
                    condition = false;
                }
            } else {
                try {
                    condition = (Boolean) conditionObject;
                } catch (final Exception e) {
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        String conditionStr = null;
                        if (conditionExpression != null) {
                            conditionStr = conditionExpression.getContent();
                        }
                        LOGGER.log(Level.WARNING, "the display condition expression does not return a boolean: " + conditionStr);
                    }
                    condition = false;
                }

            }
        }
        return condition;
    }

    /**
     * Add the widget value to evaluate to the Map of expression to evaluated
     * 
     * @param formWidget
     *            the widget
     * @param expressionsToEvaluate
     *            the list of expressions to evaluate
     * @param context
     *            the context including the URL parameters
     */
    protected void addFormWidgetValueExpressionToEvaluate(final FormWidget formWidget, final List<Expression> expressionsToEvaluate,
            final Map<String, Object> context) {
        if (formWidget.getInitialValueExpression() != null) {
            final Expression expression = formWidget.getInitialValueExpression();
            expression.setName(formWidget.getId() + EXPRESSION_KEY_SEPARATOR + WIDGET_INITIAL_VALUE);
            expressionsToEvaluate.add(expression);
        } else if (WidgetType.EDITABLE_GRID.equals(formWidget.getType()) && formWidget.getInitialValueExpressionArray() != null) {
            final List<List<Expression>> expressionArray = formWidget.getInitialValueExpressionArray();
            if (!expressionArray.isEmpty()) {
                int expressionArrayRowIndex = 0;
                for (final List<Expression> expressionArrayRow : expressionArray) {
                    int expressionArrayIndex = 0;
                    for (final Expression expression : expressionArrayRow) {
                        expression.setName(formWidget.getId() + EXPRESSION_KEY_SEPARATOR + WIDGET_INITIAL_VALUE + expressionArrayRowIndex + INDEX_SEPARATOR
                                + expressionArrayIndex);
                        expressionsToEvaluate.add(expression);
                        expressionArrayIndex++;
                    }
                    expressionArrayRowIndex++;
                }
            }
        }
    }

    /**
     * Generate the Map of groovy expressions to evaluate for a widget
     * 
     * @param formWidget
     *            the widget
     * @param context
     *            the context including the URL parameters
     * @return the Map of expressions to evaluate
     */
    protected List<Expression> getWidgetExpressions(final FormWidget formWidget, final Map<String, Object> context) {
        final String widgetId = formWidget.getId();
        final List<Expression> expressionsToEvaluate = new ArrayList<Expression>();
        if (formWidget.getLabelExpression() != null) {
            final Expression labelExpression = formWidget.getLabelExpression();
            labelExpression.setName(widgetId + EXPRESSION_KEY_SEPARATOR + WIDGET_LABEL);
            expressionsToEvaluate.add(labelExpression);
        }
        if (formWidget.getTitleExpression() != null) {
            final Expression titleExpression = formWidget.getTitleExpression();
            titleExpression.setName(widgetId + EXPRESSION_KEY_SEPARATOR + WIDGET_TITLE);
            expressionsToEvaluate.add(titleExpression);
        }
        if (formWidget.getSubtitle() != null) {
            final Expression subtitleLabelExpression = formWidget.getSubtitle().getLabelExpression();
            if (subtitleLabelExpression != null) {
                subtitleLabelExpression.setName(widgetId + EXPRESSION_KEY_SEPARATOR + WIDGET_SUBTITLE);
                expressionsToEvaluate.add(subtitleLabelExpression);
            }
        }
        if (formWidget.getPopupTooltipExpression() != null) {
            final Expression popupToolTipExpression = formWidget.getPopupTooltipExpression();
            popupToolTipExpression.setName(widgetId + EXPRESSION_KEY_SEPARATOR + WIDGET_TOOLTIP);
            expressionsToEvaluate.add(popupToolTipExpression);
        }
        addFormWidgetValueExpressionToEvaluate(formWidget, expressionsToEvaluate, context);
        addFormWidgetAvailableValuesExpressions(formWidget, expressionsToEvaluate);

        if (WidgetType.TABLE.equals(formWidget.getType()) || WidgetType.EDITABLE_GRID.equals(formWidget.getType())) {
            if (formWidget.getValueColumnIndexExpression() != null) {
                final Expression valueColumnIndexExpression = formWidget.getValueColumnIndexExpression();
                valueColumnIndexExpression.setName(widgetId + EXPRESSION_KEY_SEPARATOR + WIDGET_VALUE_COLUMN_INDEX);
                expressionsToEvaluate.add(valueColumnIndexExpression);
            }
            if (formWidget.getMaxColumnsExpression() != null) {
                final Expression valueColumnIndexExpression = formWidget.getMaxColumnsExpression();
                valueColumnIndexExpression.setName(widgetId + EXPRESSION_KEY_SEPARATOR + WIDGET_MAX_COLUMNS);
                expressionsToEvaluate.add(valueColumnIndexExpression);
            }
            if (formWidget.getMinColumnsExpression() != null) {
                final Expression minColumnsExpression = formWidget.getMinColumnsExpression();
                minColumnsExpression.setName(widgetId + EXPRESSION_KEY_SEPARATOR + WIDGET_MIN_COLUMNS);
                expressionsToEvaluate.add(minColumnsExpression);
            }
            if (formWidget.getMaxRowsExpression() != null) {
                final Expression maxRowsExpression = formWidget.getMaxRowsExpression();
                maxRowsExpression.setName(widgetId + EXPRESSION_KEY_SEPARATOR + WIDGET_MAX_ROWS);
                expressionsToEvaluate.add(maxRowsExpression);
            }
            if (formWidget.getMinRowsExpression() != null) {
                final Expression minRowsExpression = formWidget.getMinRowsExpression();
                minRowsExpression.setName(widgetId + EXPRESSION_KEY_SEPARATOR + WIDGET_MIN_ROWS);
                expressionsToEvaluate.add(minRowsExpression);
            }
            if (formWidget.getVerticalHeaderExpression() != null) {
                final Expression verticalHeaderExpression = formWidget.getVerticalHeaderExpression();
                verticalHeaderExpression.setName(widgetId + EXPRESSION_KEY_SEPARATOR + WIDGET_VERTICAL_HEADER);
                expressionsToEvaluate.add(verticalHeaderExpression);
            } else if (formWidget.getVerticalHeaderExpressionList() != null) {
                final List<Expression> verticalHeaderExpressionList = formWidget.getVerticalHeaderExpressionList();
                if (!verticalHeaderExpressionList.isEmpty()) {
                    int verticalHeaderExpressionListIndex = 0;
                    for (final Expression expression : verticalHeaderExpressionList) {
                        expression.setName(widgetId + EXPRESSION_KEY_SEPARATOR + WIDGET_VERTICAL_HEADER + verticalHeaderExpressionListIndex);
                        expressionsToEvaluate.add(expression);
                        verticalHeaderExpressionListIndex++;
                    }
                }
            }
            if (formWidget.getHorizontalHeaderExpression() != null) {
                final Expression horizontalHeaderExpression = formWidget.getHorizontalHeaderExpression();
                horizontalHeaderExpression.setName(widgetId + EXPRESSION_KEY_SEPARATOR + WIDGET_HORIZONTAL_HEADER);
                expressionsToEvaluate.add(horizontalHeaderExpression);
            } else if (formWidget.getHorizontalHeaderExpressionList() != null) {
                final List<Expression> horizontalHeaderExpressionList = formWidget.getHorizontalHeaderExpressionList();
                if (!horizontalHeaderExpressionList.isEmpty()) {
                    int horizontalHeaderExpressionListIndex = 0;
                    for (final Expression expression : horizontalHeaderExpressionList) {
                        expression.setName(widgetId + EXPRESSION_KEY_SEPARATOR + WIDGET_HORIZONTAL_HEADER + horizontalHeaderExpressionListIndex);
                        expressionsToEvaluate.add(expression);
                        horizontalHeaderExpressionListIndex++;
                    }
                }
            }
        }
        return expressionsToEvaluate;
    }

    /**
     * @param formWidget
     */
    protected void addFormWidgetAvailableValuesExpressions(final FormWidget formWidget, final List<Expression> expressionsToEvaluate) {
        final String widgetId = formWidget.getId();
        if (formWidget.getAvailableValuesExpression() != null) {
            final Expression availableValuesExpression = formWidget.getAvailableValuesExpression();
            availableValuesExpression.setName(widgetId + EXPRESSION_KEY_SEPARATOR + WIDGET_AVAILABLE_VALUES);
            expressionsToEvaluate.add(availableValuesExpression);
        } else {
            if (WidgetType.TABLE.equals(formWidget.getType())) {
                final List<List<FormFieldAvailableValue>> tableAvailableValues = formWidget.getTableAvailableValues();
                if (tableAvailableValues != null && !tableAvailableValues.isEmpty()) {
                    int availableValueRowIndex = 0;
                    for (final List<FormFieldAvailableValue> formFieldAvailableValuesRow : tableAvailableValues) {
                        int availableValueIndex = 0;
                        for (final FormFieldAvailableValue formFieldAvailableValue : formFieldAvailableValuesRow) {
                            final Expression availableValueLabelExpression = formFieldAvailableValue.getLabelExpression();
                            availableValueLabelExpression.setName(widgetId + EXPRESSION_KEY_SEPARATOR + WIDGET_AVAILABLE_VALUE_LABEL + availableValueRowIndex
                                    + INDEX_SEPARATOR + availableValueIndex);
                            expressionsToEvaluate.add(availableValueLabelExpression);
                            final Expression availableValueValueExpression = formFieldAvailableValue.getValueExpression();
                            availableValueValueExpression.setName(widgetId + EXPRESSION_KEY_SEPARATOR + WIDGET_AVAILABLE_VALUE_VALUE + availableValueRowIndex
                                    + INDEX_SEPARATOR + availableValueIndex);
                            expressionsToEvaluate.add(availableValueValueExpression);
                            availableValueIndex++;
                        }
                        availableValueRowIndex++;
                    }
                }
            } else {
                final List<FormFieldAvailableValue> availableValues = formWidget.getAvailableValues();
                if (availableValues != null && !availableValues.isEmpty()) {
                    int availableValueIndex = 0;
                    for (final FormFieldAvailableValue formFieldAvailableValue : availableValues) {
                        final Expression availableValueLabelExpression = formFieldAvailableValue.getLabelExpression();
                        availableValueLabelExpression.setName(widgetId + EXPRESSION_KEY_SEPARATOR + WIDGET_AVAILABLE_VALUE_LABEL + availableValueIndex);
                        expressionsToEvaluate.add(availableValueLabelExpression);
                        final Expression availableValueValueExpression = formFieldAvailableValue.getValueExpression();
                        availableValueValueExpression.setName(widgetId + EXPRESSION_KEY_SEPARATOR + WIDGET_AVAILABLE_VALUE_VALUE + availableValueIndex);
                        expressionsToEvaluate.add(availableValueValueExpression);
                        availableValueIndex++;
                    }
                }
            }
        }
    }

    /**
     * Set the values of the form widget
     * 
     * @param tenantID
     *            the tenantID
     * @param formWidget
     *            the widget
     * @param previousPagesFields
     * @param locale
     * @param isCurrentValue
     * @param context
     *            the context including the URL parameters
     * @throws FormNotFoundException
     * @throws FormServiceProviderNotFoundException
     * @throws SessionTimeoutException
     * @throws IOException
     * @throws FileTooBigException
     * @throws FormInitializationException 
     */
    public void setFormWidgetValues(final long tenantID, final FormWidget formWidget, final Map<String, Serializable> evaluatedExpressions,
            final Map<String, Object> context) throws FormNotFoundException, FormServiceProviderNotFoundException, SessionTimeoutException, IOException,
            FileTooBigException, FormInitializationException {
        final String widgetId = formWidget.getId();
        final FormServiceProvider formServiceProvider = FormServiceProviderFactory.getFormServiceProvider(tenantID);
        final Locale locale = (Locale) context.get(FormServiceProviderUtil.LOCALE);
        formWidget.setLabel(getStringValue(evaluatedExpressions.get(widgetId + EXPRESSION_KEY_SEPARATOR + WIDGET_LABEL)));
        formWidget.setTitle(getStringValue(evaluatedExpressions.get(widgetId + EXPRESSION_KEY_SEPARATOR + WIDGET_TITLE)));
        if (formWidget.getSubtitle() != null) {
            formWidget.getSubtitle().setLabel(getStringValue(evaluatedExpressions.get(widgetId + EXPRESSION_KEY_SEPARATOR + WIDGET_SUBTITLE)));
        }
        formWidget.setPopupTooltip(getStringValue(evaluatedExpressions.get(widgetId + EXPRESSION_KEY_SEPARATOR + WIDGET_TOOLTIP)));
        formWidget.setDisplayCondition(getDisplayConditionStr(formWidget.getDisplayConditionExpression(),
                evaluatedExpressions.get(widgetId + EXPRESSION_KEY_SEPARATOR + WIDGET_DISPLAY_CONDITION)));

        final Object value = getFormWidgetInitialValues(formWidget, evaluatedExpressions);
        if (formWidget.getType().name().startsWith("FILE") || formWidget.getType().equals(WidgetType.IMAGE) && formWidget.isDisplayAttachmentImage()) {
            final String filePaths = formWidget.getFilePaths();
            if (filePaths != null) {
                final FormFieldValue fileFieldValue = new FormFieldValue(filePaths, File.class.getName());
                formWidget.setInitialFieldValue(fileFieldValue);
            } else if (!FileWidgetInputType.URL.equals(formWidget.getFileWidgetInputType())) {
                formWidget.setInitialFieldValue(formServiceProvider.getAttachmentFormFieldValue(value, context));
            } else {
                formWidget.setInitialFieldValue(getFieldValue(value, formWidget, locale));
            }
        } else if (!formWidget.getType().name().startsWith("BUTTON")) {
            // convert the value object returned into a FormFieldValue object.
            formWidget.setInitialFieldValue(getFieldValue(value, formWidget, locale));
            // set the available values list from a groovy expression for listboxes, radiobutton groups, checkbox groups...
            setFormWidgetAvailableValues(formWidget, evaluatedExpressions);
        }
    }

    /**
     * @param formWidget
     * @param evaluatedExpressions
     * @return
     */
    protected Object getFormWidgetInitialValues(final FormWidget formWidget, final Map<String, Serializable> evaluatedExpressions) {
        final String widgetId = formWidget.getId();
        Object value = null;
        if (WidgetType.EDITABLE_GRID.equals(formWidget.getType())
                && evaluatedExpressions.containsKey(widgetId + EXPRESSION_KEY_SEPARATOR + WIDGET_INITIAL_VALUE + 0 + INDEX_SEPARATOR + 0)) {
            final List<List<Object>> initialValue = new ArrayList<List<Object>>();
            int initialValueRowIndex = 0;
            do {
                final List<Object> initialValueRow = new ArrayList<Object>();
                int initialValueColumnIndex = 0;
                do {
                    final Object initialValueCell = evaluatedExpressions.get(widgetId + EXPRESSION_KEY_SEPARATOR + WIDGET_INITIAL_VALUE
                            + initialValueRowIndex + INDEX_SEPARATOR + initialValueColumnIndex);
                    initialValueRow.add(initialValueCell);
                    initialValueColumnIndex++;
                } while (evaluatedExpressions.containsKey(widgetId + EXPRESSION_KEY_SEPARATOR + WIDGET_INITIAL_VALUE + initialValueRowIndex
                        + INDEX_SEPARATOR + initialValueColumnIndex));
                initialValue.add(initialValueRow);
                initialValueRowIndex++;
            } while (evaluatedExpressions.containsKey(widgetId + EXPRESSION_KEY_SEPARATOR + WIDGET_INITIAL_VALUE + initialValueRowIndex + INDEX_SEPARATOR
                    + 0));
            value = initialValue;
        } else {
            value = evaluatedExpressions.get(widgetId + EXPRESSION_KEY_SEPARATOR + WIDGET_INITIAL_VALUE);
        }
        return value;
    }

    /**
     * @param formWidget
     * @param evaluatedExpressions
     */
    protected void setFormWidgetAvailableValues(final FormWidget formWidget, final Map<String, Serializable> evaluatedExpressions) {
        final Object availableValuesObject = evaluatedExpressions.get(formWidget.getId() + EXPRESSION_KEY_SEPARATOR + WIDGET_AVAILABLE_VALUES);
        if (availableValuesObject != null) {
            if (formWidget.getType().equals(WidgetType.TABLE)) {
                final List<List<ReducedFormFieldAvailableValue>> availableValues = getTableAvailableValues(availableValuesObject, formWidget.getId());
                formWidget.setReducedTableAvailableValues(availableValues);
            } else {
                final List<ReducedFormFieldAvailableValue> availableValues = getAvailableValues(availableValuesObject, formWidget.getId());
                formWidget.setReducedAvailableValues(availableValues);
            }
        } else {
            setAvailableValuesListsAndArrays(formWidget, evaluatedExpressions);
        }
    }

    /**
     * @param widgetId
     * @param formWidget
     * @param evaluatedExpressions
     */
    protected void setAvailableValuesListsAndArrays(final FormWidget formWidget, final Map<String, Serializable> evaluatedExpressions) {
        final String widgetId = formWidget.getId();
        if (WidgetType.TABLE.equals(formWidget.getType())) {
            if (evaluatedExpressions.containsKey(widgetId + EXPRESSION_KEY_SEPARATOR + WIDGET_AVAILABLE_VALUE_VALUE + 0 + INDEX_SEPARATOR + 0)) {
                int availableValueRowIndex = 0;
                do {
                    int availableValueColumnIndex = 0;
                    do {
                        final Object availableValuelabel = evaluatedExpressions.get(widgetId + EXPRESSION_KEY_SEPARATOR + WIDGET_AVAILABLE_VALUE_LABEL
                                + availableValueRowIndex + INDEX_SEPARATOR + availableValueColumnIndex);
                        final Object availableValueValue = evaluatedExpressions.get(widgetId + EXPRESSION_KEY_SEPARATOR + WIDGET_AVAILABLE_VALUE_VALUE
                                + availableValueRowIndex + INDEX_SEPARATOR + availableValueColumnIndex);
                        final FormFieldAvailableValue availableValue = formWidget.getTableAvailableValues().get(availableValueRowIndex)
                                .get(availableValueColumnIndex);
                        availableValue.setLabel(getStringValue(availableValuelabel));
                        availableValue.setValue(getStringValue(availableValueValue));
                        availableValueColumnIndex++;
                    } while (evaluatedExpressions.containsKey(widgetId + EXPRESSION_KEY_SEPARATOR + WIDGET_AVAILABLE_VALUE_VALUE + availableValueRowIndex
                            + INDEX_SEPARATOR + availableValueColumnIndex));
                    availableValueRowIndex++;
                } while (evaluatedExpressions.containsKey(widgetId + EXPRESSION_KEY_SEPARATOR + WIDGET_AVAILABLE_VALUE_VALUE + availableValueRowIndex
                        + INDEX_SEPARATOR + 0));
            }
        } else {
            if (evaluatedExpressions.containsKey(widgetId + EXPRESSION_KEY_SEPARATOR + WIDGET_AVAILABLE_VALUE_VALUE + 0)) {
                int availableValueIndex = 0;
                do {
                    final Object availableValuelabel = evaluatedExpressions.get(widgetId + EXPRESSION_KEY_SEPARATOR + WIDGET_AVAILABLE_VALUE_LABEL
                            + availableValueIndex);
                    final Object availableValueValue = evaluatedExpressions.get(widgetId + EXPRESSION_KEY_SEPARATOR + WIDGET_AVAILABLE_VALUE_VALUE
                            + availableValueIndex);
                    final FormFieldAvailableValue availableValue = formWidget.getAvailableValues().get(availableValueIndex);
                    availableValue.setLabel(getStringValue(availableValuelabel));
                    availableValue.setValue(getStringValue(availableValueValue));
                    availableValueIndex++;
                } while (evaluatedExpressions.containsKey(widgetId + EXPRESSION_KEY_SEPARATOR + WIDGET_AVAILABLE_VALUE_VALUE + availableValueIndex));
            }
        }
    }

    /**
     * Set the tables parameters
     * 
     * @param formWidget
     *            the widget
     * @param evaluatedExpressions
     *            the map of evaluated expressions
     * @param context
     *            the context including the URL parameters
     * @throws FormNotFoundException
     * @throws FormServiceProviderNotFoundException
     */
    @SuppressWarnings("unchecked")
    public void setTablesParams(final FormWidget formWidget, final Map<String, Serializable> evaluatedExpressions, final Map<String, Object> context)
            throws FormNotFoundException, FormServiceProviderNotFoundException {
        final String widgetId = formWidget.getId();
        if (WidgetType.TABLE.equals(formWidget.getType()) || WidgetType.EDITABLE_GRID.equals(formWidget.getType())) {
            final Object valueColumnIndex = evaluatedExpressions.get(widgetId + EXPRESSION_KEY_SEPARATOR + WIDGET_VALUE_COLUMN_INDEX);
            if (valueColumnIndex != null) {
                try {
                    formWidget.setValueColumnIndex((Integer) valueColumnIndex);
                } catch (final Exception e) {
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER.log(Level.WARNING, "The max number of columns has to be set with an integer or a groovy expression returning an integer.");
                    }
                    formWidget.setValueColumnIndex(0);
                }
            }
            final Object maxColumns = evaluatedExpressions.get(widgetId + EXPRESSION_KEY_SEPARATOR + WIDGET_MAX_COLUMNS);
            if (maxColumns != null) {
                try {
                    formWidget.setMaxColumns((Integer) maxColumns);
                } catch (final Exception e) {
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER.log(Level.WARNING, "The max number of columns has to be set with an integer or a groovy expression returning an integer.");
                    }
                    formWidget.setMaxColumns(-1);
                }
            } else {
                formWidget.setMaxColumns(-1);
            }
            final Object minColumns = evaluatedExpressions.get(widgetId + EXPRESSION_KEY_SEPARATOR + WIDGET_MIN_COLUMNS);
            if (minColumns != null) {
                try {
                    formWidget.setMinColumns((Integer) minColumns);
                } catch (final Exception e) {
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER.log(Level.WARNING, "The min number of columns has to be set with an integer or a groovy expression returning an integer.");
                    }
                    formWidget.setMinColumns(-1);
                }
            } else {
                formWidget.setMinColumns(-1);
            }
            final Object maxRows = evaluatedExpressions.get(widgetId + EXPRESSION_KEY_SEPARATOR + WIDGET_MAX_ROWS);
            if (maxRows != null) {
                try {
                    formWidget.setMaxRows((Integer) maxRows);
                } catch (final Exception e) {
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER.log(Level.WARNING, "The max number of rows has to be set with an integer or a groovy expression returning an integer.");
                    }
                    formWidget.setMaxRows(-1);
                }
            } else {
                formWidget.setMaxRows(-1);
            }
            final Object minRows = evaluatedExpressions.get(widgetId + EXPRESSION_KEY_SEPARATOR + WIDGET_MIN_ROWS);
            if (minRows != null) {
                try {
                    formWidget.setMinRows((Integer) minRows);
                } catch (final Exception e) {
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER.log(Level.WARNING, "The max number of rows has to be set with an integer or a groovy expression returning an integer.");
                    }
                    formWidget.setMinRows(-1);
                }
            } else {
                formWidget.setMinRows(-1);
            }
            final Object verticalHeader = evaluatedExpressions.get(widgetId + EXPRESSION_KEY_SEPARATOR + WIDGET_VERTICAL_HEADER);
            List<String> verticalHeaderList = null;
            if (verticalHeader != null) {
                try {
                    verticalHeaderList = new ArrayList<String>((Collection<String>) verticalHeader);
                } catch (final ClassCastException e) {
                    if (LOGGER.isLoggable(Level.SEVERE)) {
                        LOGGER.log(Level.SEVERE, "The vertical header expression for widget " + formWidget.getId() + " should return a Collection.", e);
                    }
                }
            } else if (evaluatedExpressions.containsKey(widgetId + EXPRESSION_KEY_SEPARATOR + WIDGET_VERTICAL_HEADER + 0)) {
                verticalHeaderList = new ArrayList<String>();
                int verticalHeaderIndex = 0;
                do {
                    final Object verticalHeaderCell = evaluatedExpressions.get(widgetId + EXPRESSION_KEY_SEPARATOR + WIDGET_VERTICAL_HEADER
                            + verticalHeaderIndex);
                    try {
                        verticalHeaderList.add((String) verticalHeaderCell);
                    } catch (final ClassCastException e) {
                        if (LOGGER.isLoggable(Level.SEVERE)) {
                            LOGGER.log(Level.SEVERE, "All the vertical header expressions for widget " + formWidget.getId() + " should return a String.", e);
                        }
                    }
                    verticalHeaderIndex++;
                } while (evaluatedExpressions.containsKey(widgetId + EXPRESSION_KEY_SEPARATOR + WIDGET_VERTICAL_HEADER + verticalHeaderIndex));
            }
            formWidget.setVerticalHeader(verticalHeaderList);
            final Object horizontalHeader = evaluatedExpressions.get(widgetId + EXPRESSION_KEY_SEPARATOR + WIDGET_HORIZONTAL_HEADER);
            List<String> horizontalHeaderList = null;
            if (horizontalHeader != null) {
                try {
                    horizontalHeaderList = new ArrayList<String>((Collection<String>) horizontalHeader);
                } catch (final ClassCastException e) {
                    if (LOGGER.isLoggable(Level.SEVERE)) {
                        LOGGER.log(Level.SEVERE, "The horizontal header expression for widget " + formWidget.getId() + " should return a Collection.", e);
                    }
                }
            } else if (evaluatedExpressions.containsKey(widgetId + EXPRESSION_KEY_SEPARATOR + WIDGET_HORIZONTAL_HEADER + 0)) {
                horizontalHeaderList = new ArrayList<String>();
                int horizontalHeaderIndex = 0;
                do {
                    final Object horizontalHeaderCell = evaluatedExpressions.get(widgetId + EXPRESSION_KEY_SEPARATOR + WIDGET_HORIZONTAL_HEADER
                            + horizontalHeaderIndex);
                    try {
                        horizontalHeaderList.add((String) horizontalHeaderCell);
                    } catch (final ClassCastException e) {
                        if (LOGGER.isLoggable(Level.SEVERE)) {
                            LOGGER.log(Level.SEVERE, "All the horizontal header expressions for widget " + formWidget.getId() + " should return a String.", e);
                        }
                    }
                    horizontalHeaderIndex++;
                } while (evaluatedExpressions.containsKey(widgetId + EXPRESSION_KEY_SEPARATOR + WIDGET_HORIZONTAL_HEADER + horizontalHeaderIndex));
            }
            formWidget.setHorizontalHeader(horizontalHeaderList);
        }
    }

    /**
     * set the widget values of a form page
     * 
     * @param tenantID
     *            the tenant ID
     * @param widgets
     *            the widgets of the page
     * @param context
     *            the context including the URL parameters
     * @throws FormServiceProviderNotFoundException
     * @throws FormNotFoundException
     * @throws SessionTimeoutException
     * @throws IOException
     * @throws FileTooBigException
     * @throws FormInitializationException 
     */
    public void setFormWidgetsValues(final long tenantID,
            final List<FormWidget> widgets,
            final Map<String, Object> context)

            throws FormNotFoundException,
            FormServiceProviderNotFoundException,
            SessionTimeoutException,
            IOException,
            FileTooBigException, FormInitializationException {
        final FormServiceProvider formServiceProvider = FormServiceProviderFactory.getFormServiceProvider(tenantID);

        final Map<String, Serializable> evaluatedDisplayExpressions = resolveDisplayExpressions(widgets, context, formServiceProvider);
        final Map<String, Serializable> evaluatedExpressions = formServiceProvider.resolveExpressions(
                getExpressionsToEvaluation(widgets, evaluatedDisplayExpressions, context),
                context);
        evaluatedExpressions.putAll(evaluatedDisplayExpressions);

        for (final FormWidget formWidget : widgets) {
            setFormWidgetValues(tenantID, formWidget, evaluatedExpressions, context);
            setTablesParams(formWidget, evaluatedExpressions, context);
        }
    }

    private Map<String, Serializable> resolveDisplayExpressions(final List<FormWidget> widgets, final Map<String, Object> context,
            final FormServiceProvider formServiceProvider)
            throws FormNotFoundException, SessionTimeoutException, FileTooBigException, IOException, FormInitializationException {
        Map<String, Serializable> resolvedExpressions = formServiceProvider.resolveExpressions(
                new DisplayExpressions(widgets).asList(),
                context);
        return resolvedExpressions != null ? resolvedExpressions : new HashMap<String, Serializable>();
    }

    protected List<Expression> getExpressionsToEvaluation(final List<FormWidget> widgets,
            final Map<String, Serializable> resolvedDisplayExp,
            final Map<String, Object> context) {
        final List<Expression> expressionsToEvaluate = new ArrayList<Expression>();
        for (final FormWidget formWidget : widgets) {
            if (isAuthorized(resolvedDisplayExp, formWidget.getId())) {
                expressionsToEvaluate.addAll(getWidgetExpressions(formWidget, context));
            }
        }
        return expressionsToEvaluate;
    }

    private Boolean isAuthorized(final Map<String, Serializable> resolvedDisplayExp, final String widgetId) {
        String widgetExpressionEntry = new WidgetExpressionEntry(widgetId, ExpressionId.WIDGET_DISPLAY_CONDITION)
                .toString();
        return resolvedDisplayExp == null
                || !resolvedDisplayExp.containsKey(widgetExpressionEntry)
                || Boolean.valueOf(resolvedDisplayExp.get(widgetExpressionEntry).toString());
    }

    public void storeWidgetsInCacheAndSetCacheID(final long tenantID, final String formID, final String pageID, final String locale,
            final Date processDeployementDate, final List<FormWidget> formWidgets) {

        final FormCacheUtil formCacheUtil = FormCacheUtilFactory.getTenantFormCacheUtil(tenantID);
        for (final FormWidget formWidget : formWidgets) {
            if (isExpressionDynamic(formWidget.getInitialValueExpression()) || isArrayOfExpressionsDynamic(formWidget.getInitialValueExpressionArray())
                    || isExpressionDynamic(formWidget.getAvailableValuesExpression()) || isListOfExpressionsDynamic(formWidget.getAvailableValues())
                    || isListOfListOfExpressionsDynamic(formWidget.getTableAvailableValues())) {
                formWidget.setHasDynamicValue(true);
            }
            final String formWidgetCacheId = formCacheUtil.storeFormWidget(formID, pageID, locale, processDeployementDate, formWidget);
            formWidget.setFormWidgetCacheId(formWidgetCacheId);
            if (formWidget.getValidators() != null) {
                formWidget.setValidatorsCacheId(formCacheUtil.storeFieldValidators(formID, pageID, formWidget.getId(), locale, processDeployementDate,
                        formWidget.getValidators()));
            }
        }
    }

    protected boolean isArrayOfExpressionsDynamic(final List<List<Expression>> initialValueExpressionArray) {
        if (initialValueExpressionArray != null && initialValueExpressionArray.size() > 0) {
            for (final List<Expression> expressionList : initialValueExpressionArray) {
                for (final Expression expression : expressionList) {
                    if (isExpressionDynamic(expression)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    protected boolean isListOfExpressionsDynamic(final List<FormFieldAvailableValue> availableValues) {
        if (availableValues != null && availableValues.size() > 0) {
            for (final FormFieldAvailableValue formFieldAvailableValue : availableValues) {
                if (isExpressionDynamic(formFieldAvailableValue.getLabelExpression()) || isExpressionDynamic(formFieldAvailableValue.getValueExpression())) {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean isListOfListOfExpressionsDynamic(final List<List<FormFieldAvailableValue>> tableAvailableValues) {
        if (tableAvailableValues != null && tableAvailableValues.size() > 0) {
            for (final List<FormFieldAvailableValue> formFieldAvailableValuesList : tableAvailableValues) {
                if (isListOfExpressionsDynamic(formFieldAvailableValuesList)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean isExpressionDynamic(final Expression expression) {
        if (expression != null
                && !ExpressionType.TYPE_I18N.equals(expression.getExpressionType())
                && !ExpressionType.TYPE_CONSTANT.equals(expression.getExpressionType())) {
            return true;
        }
        return false;
    }

}
