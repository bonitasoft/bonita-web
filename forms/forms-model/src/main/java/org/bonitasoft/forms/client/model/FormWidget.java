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
package org.bonitasoft.forms.client.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bonitasoft.forms.client.model.ReducedFormWidget.ItemPosition;
import org.bonitasoft.forms.client.model.ReducedFormWidget.SelectMode;

/**
 * Object representing a widget to place in a page of a task form flow
 * 
 * @author Anthony Birembaut
 */
public class FormWidget implements Serializable, Comparable<FormWidget> {

    /**
     * UID
     */
    private static final long serialVersionUID = -2420889539040109129L;

    /**
     * label expression of the widget
     */
    private Expression labelExpression;

    /**
     * title expression of the widget
     */
    private Expression titleExpression;

    /**
     * initial value of the field under the form of an expression
     */
    private Expression initialValueExpression = null;

    /**
     * initial value of the field under the form of a list of list of expression (for grids)
     */
    private List<List<Expression>> initialValueExpressionArray = null;

    /**
     * Connectors to execute to initialize the field
     */
    private List<Connector> initialValueConnectors = null;

    /**
     * Widget available values expression used to evaluate to fill in the available values. The evaluation of the expression has to return a collection
     */
    private Expression availableValuesExpression = null;

    /**
     * Connectors to execute to initialize the availableValues
     */
    private List<Connector> availableValuesConnectors = null;

    /**
     * The horizontal header for tables as an expression
     */
    private Expression horizontalHeaderExpression = null;

    /**
     * The horizontal header for tables as an expression list
     */
    private List<Expression> horizontalHeaderExpressionList = null;

    /**
     * The horizontal header for tables as an expression
     */
    private Expression verticalHeaderExpression = null;

    /**
     * The horizontal header for tables as an expression list
     */
    private List<Expression> verticalHeaderExpressionList = null;

    /**
     * The expression for the number of rows for tables
     */
    private Expression maxRowsExpression;

    /**
     * The expression for the min number of rows for tables
     */
    private Expression minRowsExpression;

    /**
     * The max number of columns for tables
     */
    private Expression maxColumnsExpression;

    /**
     * The min number of columns for tables
     */
    private Expression minColumnsExpression;

    /**
     * Expression specifying the index of column which is used as the value of the selected row(s) (for table widgets)
     */
    private Expression valueColumnIndexExpression;

    /**
     * Expression specifying the max number of instances
     */
    private Expression maxInstancesExpression;

    /**
     * Expression specifying the min number of instances
     */
    private Expression minInstancesExpression;

    /**
     * Condition Expression to display or not a data field
     */
    private Expression displayConditionExpression;

    /**
     * Condition to display or not a data field before an event
     */
    private Expression displayBeforeEventExpression;

    /**
     * Condition to display or not a data field after an event
     */
    private Expression displayAfterEventExpression;

    /**
     * Widget available values expression after an event triggered by a dependency
     */
    private Expression availableValuesAfterEventExpression;

    /**
     * Connectors to execute to change the field's available values after a dependency event
     */
    private List<Connector> availableValuesAfterEventConnectors;

    /**
     * value of the field after an event triggered by a dependency
     */
    private Expression valueAfterEventExpression;

    /**
     * the add item label expression
     */
    private Expression addItemLabelExpression;

    /**
     * the add item title expression
     */
    private Expression addItemTitleExpression;

    /**
     * the remove item label expression
     */
    private Expression removeItemLabelExpression;

    /**
     * the remove item label expression
     */
    private Expression removeItemTitleExpression;

    /**
     * Connectors to execute to change the field's value after a dependency event
     */
    private List<Connector> valueAfterEventConnectors;

    /**
     * Popup tooltip expression
     */
    private Expression popupTooltipExpression;

    /**
     * Object containing the field of the form widget that are not expressions
     */
    private ReducedFormWidget reducedFormWidget;

    /**
     * The form subtitle
     */
    private FormSubtitle subtitle;

    /**
     * List of validators to apply to the field
     */
    private List<FormValidator> validators;

    /**
     * child widgets
     */
    private List<FormWidget> childWidgets;

    /**
     * Widget available values useful for selectboxes and radiobuttons
     */
    private List<FormFieldAvailableValue> availableValues;

    /**
     * table available values
     */
    private List<List<FormFieldAvailableValue>> tableAvailableValues;

    /**
     * Widget available values after an event triggered by a dependency
     */
    private List<FormFieldAvailableValue> availableValuesAfterEvent;

    /**
     * table available values after an event triggered by a dependency
     */
    private List<List<FormFieldAvailableValue>> tableAvailableValuesAfterEvent;

    /**
     * file paths
     */
    private String filePaths;

    /**
     * Constructor
     * 
     * @param id
     * @param maxLength
     * @param maxHeight
     * @param type
     * @param style
     * @param labelStyle
     * @param inputStyle
     * @param labelPosition
     * @param mandatory
     * @param popupToolTip
     */
    public FormWidget(final String id, final int maxLength, final int maxHeight, final WidgetType type, final String style, final String labelStyle,
            final String inputStyle, final ItemPosition labelPosition, final boolean mandatory) {
        reducedFormWidget = new ReducedFormWidget(id, maxLength, maxHeight, type, style, labelStyle, inputStyle, labelPosition, mandatory);
    }

    /**
     * Default Constructor
     * Mandatory for serialization
     */
    public FormWidget() {
        super();
        reducedFormWidget = new ReducedFormWidget();
    }

    /**
     * @return the filePaths
     */
    public String getFilePaths() {
        return filePaths;
    }

    /**
     * @param filePaths
     *            the filePaths to set
     */
    public void setFilePaths(final String filePaths) {
        if (filePaths != null) {
            this.filePaths = filePaths;
        }
    }

    public String getTitle() {
        return reducedFormWidget.getTitle();
    }

    public void setTitle(final String title) {
        if (title != null) {
            reducedFormWidget.setTitle(title);
        }
    }

    public String getId() {
        return reducedFormWidget.getId();
    }

    public void setId(final String id) {
        reducedFormWidget.setId(id);
    }

    public int getMaxLength() {
        return reducedFormWidget.getMaxLength();
    }

    public void setMaxLength(final int maxLength) {
        reducedFormWidget.setMaxLength(maxLength);
    }

    public int getMaxHeight() {
        return reducedFormWidget.getMaxHeight();
    }

    public void setMaxHeight(final int maxHeight) {
        reducedFormWidget.setMaxHeight(maxHeight);
    }

    public WidgetType getType() {
        return reducedFormWidget.getType();
    }

    public void setType(final WidgetType type) {
        reducedFormWidget.setType(type);
    }

    public String getStyle() {
        return reducedFormWidget.getStyle();
    }

    public void setStyle(final String style) {
        reducedFormWidget.setStyle(style);
    }

    public Expression getInitialValueExpression() {
        return initialValueExpression;
    }

    public void setInitialValueExpression(final Expression initialValueExpression) {
        this.initialValueExpression = initialValueExpression;
    }

    public List<List<Expression>> getInitialValueExpressionArray() {
        return initialValueExpressionArray;
    }

    public void setInitialValueExpressionArray(final List<List<Expression>> initialValueExpressionArray) {
        this.initialValueExpressionArray = initialValueExpressionArray;
    }

    public List<FormValidator> getValidators() {
        return validators;
    }

    public void setValidators(final List<FormValidator> validators) {
        this.validators = validators;
        final List<ReducedFormValidator> reducedValidators = new ArrayList<ReducedFormValidator>();
        if (validators != null) {
            for (final FormValidator formValidator : validators) {
                reducedValidators.add(formValidator.getReducedFormValidator());
            }
        }
        reducedFormWidget.setValidators(reducedValidators);
    }

    public String getLabel() {
        return reducedFormWidget.getLabel();
    }

    public void setLabel(final String label) {
        if (label != null) {
            reducedFormWidget.setLabel(label);
        }
    }

    public String getLabelStyle() {
        return reducedFormWidget.getLabelStyle();
    }

    public void setLabelStyle(final String labelStyle) {
        reducedFormWidget.setLabelStyle(labelStyle);
    }

    public String getInputStyle() {
        return reducedFormWidget.getInputStyle();
    }

    public void setInputStyle(final String inputStyle) {
        reducedFormWidget.setInputStyle(inputStyle);
    }

    public ItemPosition getLabelPosition() {
        return reducedFormWidget.getLabelPosition();
    }

    public void setLabelPosition(final ItemPosition labelPosition) {
        reducedFormWidget.setLabelPosition(labelPosition);
    }

    public List<FormFieldAvailableValue> getAvailableValues() {
        return availableValues;
    }

    public void setAvailableValues(final List<FormFieldAvailableValue> availableValues) {
        this.availableValues = availableValues;
        final List<ReducedFormFieldAvailableValue> reducedFormFieldAvailableValues = new ArrayList<ReducedFormFieldAvailableValue>();
        for (final FormFieldAvailableValue formFieldAvailableValue : availableValues) {
            reducedFormFieldAvailableValues.add(formFieldAvailableValue.getReducedFieldAvailableValue());
        }
        reducedFormWidget.setAvailableValues(reducedFormFieldAvailableValues);
    }

    public void setReducedAvailableValues(final List<ReducedFormFieldAvailableValue> reducedFormFieldAvailableValues) {
        reducedFormWidget.setAvailableValues(reducedFormFieldAvailableValues);
    }

    public boolean isMandatory() {
        return reducedFormWidget.isMandatory();
    }

    public void setMandatory(final boolean mandatory) {
        reducedFormWidget.setMandatory(mandatory);
    }

    public FormFieldValue getInitialFieldValue() {
        return reducedFormWidget.getInitialFieldValue();
    }

    public void setInitialFieldValue(final FormFieldValue initialFieldValue) {
        reducedFormWidget.setInitialFieldValue(initialFieldValue);
    }

    public String getDisplayFormat() {
        return reducedFormWidget.getDisplayFormat();
    }

    public void setDisplayFormat(final String displayFormat) {
        reducedFormWidget.setDisplayFormat(displayFormat);
    }

    public boolean isViewPageWidget() {
        return reducedFormWidget.isViewPageWidget();
    }

    public void setViewPageWidget(final boolean viewPageWidget) {
        reducedFormWidget.setViewPageWidget(viewPageWidget);
    }

    public String getItemsStyle() {
        return reducedFormWidget.getItemsStyle();
    }

    public void setItemsStyle(final String itemsStyle) {
        reducedFormWidget.setItemsStyle(itemsStyle);
    }

    public Expression getAvailableValuesExpression() {
        return availableValuesExpression;
    }

    public void setAvailableValuesExpression(final Expression availableValuesExpression) {
        this.availableValuesExpression = availableValuesExpression;
    }

    public boolean isLabelButton() {
        return reducedFormWidget.isLabelButton();
    }

    public void setLabelButton(final boolean labelButton) {
        reducedFormWidget.setLabelButton(labelButton);
    }

    public boolean allowHTMLInLabel() {
        return reducedFormWidget.allowHTMLInLabel();
    }

    public void setAllowHTMLInLabel(final boolean allowHTMLInLabel) {
        reducedFormWidget.setAllowHTMLInLabel(allowHTMLInLabel);
    }

    public boolean allowHTMLInField() {
        return reducedFormWidget.allowHTMLInField();
    }

    public void setAllowHTMLInField(final boolean allowHTMLInField) {
        reducedFormWidget.setAllowHTMLInField(allowHTMLInField);
    }

    public String getImageStyle() {
        return reducedFormWidget.getImageStyle();
    }

    public void setImageStyle(final String imageStyle) {
        reducedFormWidget.setImageStyle(imageStyle);
    }

    public String getHeadingsStyle() {
        return reducedFormWidget.getHeadingsStyle();
    }

    public void setHeadingsStyle(final String headingsStyle) {
        reducedFormWidget.setHeadingsStyle(headingsStyle);
    }

    public boolean hasLeftHeadings() {
        return reducedFormWidget.hasLeftHeadings();
    }

    public void setLeftHeadings(final boolean leftHeadings) {
        reducedFormWidget.setLeftHeadings(leftHeadings);
    }

    public boolean hasTopHeadings() {
        return reducedFormWidget.hasTopHeadings();
    }

    public void setTopHeadings(final boolean topHeadings) {
        reducedFormWidget.setTopHeadings(topHeadings);
    }

    public boolean hasRightHeadings() {
        return reducedFormWidget.hasRightHeadings();
    }

    public void setRightHeadings(final boolean rightHeadings) {
        reducedFormWidget.setRightHeadings(rightHeadings);
    }

    public boolean hasBottomHeadings() {
        return reducedFormWidget.hasBottomHeadings();
    }

    public void setBottomHeadings(final boolean bottomHeadings) {
        reducedFormWidget.setBottomHeadings(bottomHeadings);
    }

    public String getTableStyle() {
        return reducedFormWidget.getTableStyle();
    }

    public void setTableStyle(final String tableStyle) {
        reducedFormWidget.setTableStyle(tableStyle);
    }

    public String getCellsStyle() {
        return reducedFormWidget.getCellsStyle();
    }

    public void setCellsStyle(final String cellsStyle) {
        reducedFormWidget.setCellsStyle(cellsStyle);
    }

    public void setReadOnly(final boolean readOnly) {
        reducedFormWidget.setReadOnly(readOnly);
    }

    public boolean isReadOnly() {
        return reducedFormWidget.isReadOnly();
    }

    public void setMultiple(final boolean multiple) {
        reducedFormWidget.setMultiple(multiple);
    }
	
    public boolean isMultiple() {
        return reducedFormWidget.isMultiple();
    }

    public int getValueColumnIndex() {
        return reducedFormWidget.getValueColumnIndex();
    }

    public void setValueColumnIndex(final int valueColumnIndex) {
        reducedFormWidget.setValueColumnIndex(valueColumnIndex);
    }

    public int getMaxInstances() {
        return reducedFormWidget.getMaxInstances();
    }

    public void setMaxInstances(final int maxInstances) {
        reducedFormWidget.setMaxInstances(maxInstances);
    }

    public int getMinInstances() {
        return reducedFormWidget.getMinInstances();
    }

    public void setMinInstances(final int minInstances) {
        reducedFormWidget.setMinInstances(minInstances);
    }

    public String getIteratorName() {
        return reducedFormWidget.getIteratorName();
    }

    public void setIteratorName(final String iteratorName) {
        reducedFormWidget.setIteratorName(iteratorName);
    }

    public void setDisplayAttachmentImage(final boolean displayAttachmentImage) {
        reducedFormWidget.setDisplayAttachmentImage(displayAttachmentImage);
    }

    public boolean isDisplayAttachmentImage() {
        return reducedFormWidget.isDisplayAttachmentImage();
    }

    public boolean isDisplayCondition() {
        return reducedFormWidget.isDisplayCondition();
    }

    public void setDisplayCondition(final boolean displayCondition) {
        reducedFormWidget.setDisplayCondition(displayCondition);
    }

    public String getAddItemLabel() {
        return reducedFormWidget.getAddItemLabel();
    }

    public void setAddItemLabel(final String addItemLabel) {
        reducedFormWidget.setAddItemLabel(addItemLabel);
    }

    public String getAddItemTitle() {
        return reducedFormWidget.getAddItemTitle();
    }

    public void setAddItemTitle(final String addItemTitle) {
        reducedFormWidget.setAddItemTitle(addItemTitle);
    }

    public String getAddItemLabelStyle() {
        return reducedFormWidget.getAddItemLabelStyle();
    }

    public void setAddItemLabelStyle(final String addItemLabelStyle) {
        reducedFormWidget.setAddItemLabelStyle(addItemLabelStyle);
    }

    public String getRemoveItemLabel() {
        return reducedFormWidget.getRemoveItemLabel();
    }

    public void setRemoveItemLabel(final String removeItemLabel) {
        reducedFormWidget.setRemoveItemLabel(removeItemLabel);
    }

    public String getRemoveItemTitle() {
        return reducedFormWidget.getRemoveItemTitle();
    }

    public void setRemoveItemTitle(final String removeItemTitle) {
        reducedFormWidget.setRemoveItemTitle(removeItemTitle);
    }

    public String getRemoveItemLabelStyle() {
        return reducedFormWidget.getRemoveItemLabelStyle();
    }

    public void setRemoveItemLabelStyle(final String removeItemLabelStyle) {
        reducedFormWidget.setRemoveItemLabelStyle(removeItemLabelStyle);
    }

    public WidgetPosition getWidgetPositionInGroup() {
        return reducedFormWidget.getWidgetPositionInGroup();
    }

    public void setWidgetPositionInGroup(final WidgetPosition widgetPositionInGroup) {
        reducedFormWidget.setWidgetPositionInGroup(widgetPositionInGroup);
    }

    public Map<Integer, String> getRowsStyles() {
        return reducedFormWidget.getRowsStyles();
    }

    public void setRowsStyles(final Map<Integer, String> rowsStyles) {
        reducedFormWidget.setRowsStyles(rowsStyles);
    }

    public Map<Integer, String> getColumnsStyles() {
        return reducedFormWidget.getColumnsStyles();
    }

    public void setColumnsStyles(final Map<Integer, String> columnsStyles) {
        reducedFormWidget.setColumnsStyles(columnsStyles);
    }

    public List<List<FormFieldAvailableValue>> getTableAvailableValues() {
        return tableAvailableValues;
    }

    public void setTableAvailableValues(final List<List<FormFieldAvailableValue>> tableAvailableValues) {
        this.tableAvailableValues = tableAvailableValues;
        final List<List<ReducedFormFieldAvailableValue>> reducedTableAvailableValues = new ArrayList<List<ReducedFormFieldAvailableValue>>();
        for (final List<FormFieldAvailableValue> tableAvailableValueRow : tableAvailableValues) {
            final List<ReducedFormFieldAvailableValue> reducedAvailableValuesRow = new ArrayList<ReducedFormFieldAvailableValue>();
            for (final FormFieldAvailableValue tableAvailableValue : tableAvailableValueRow) {
                reducedAvailableValuesRow.add(tableAvailableValue.getReducedFieldAvailableValue());
            }
            reducedTableAvailableValues.add(reducedAvailableValuesRow);
        }
        reducedFormWidget.setTableAvailableValues(reducedTableAvailableValues);
    }

    public void setReducedTableAvailableValues(final List<List<ReducedFormFieldAvailableValue>> reducedTableAvailableValues) {
        reducedFormWidget.setTableAvailableValues(reducedTableAvailableValues);
    }

    public Expression getHorizontalHeaderExpression() {
        return horizontalHeaderExpression;
    }

    public void setHorizontalHeaderExpression(final Expression horizontalHeaderExpression) {
        this.horizontalHeaderExpression = horizontalHeaderExpression;
    }

    public List<Expression> getHorizontalHeaderExpressionList() {
        return horizontalHeaderExpressionList;
    }

    public void setHorizontalHeaderExpressionList(final List<Expression> horizontalHeaderExpressionList) {
        this.horizontalHeaderExpressionList = horizontalHeaderExpressionList;
    }

    public List<String> getHorizontalHeader() {
        return reducedFormWidget.getHorizontalHeader();
    }

    public void setHorizontalHeader(final List<String> horizontalHeader) {
        reducedFormWidget.setHorizontalHeader(horizontalHeader);
    }

    public Expression getVerticalHeaderExpression() {
        return verticalHeaderExpression;
    }

    public void setVerticalHeaderExpression(final Expression verticalHeaderExpression) {
        this.verticalHeaderExpression = verticalHeaderExpression;
    }

    public List<Expression> getVerticalHeaderExpressionList() {
        return verticalHeaderExpressionList;
    }

    public void setVerticalHeaderExpressionList(final List<Expression> verticalHeaderExpressionList) {
        this.verticalHeaderExpressionList = verticalHeaderExpressionList;
    }

    public List<String> getVerticalHeader() {
        return reducedFormWidget.getVerticalHeader();
    }

    public void setVerticalHeader(final List<String> verticalHeader) {
        reducedFormWidget.setVerticalHeader(verticalHeader);
    }

    public SelectMode getSelectMode() {
        return reducedFormWidget.getSelectMode();
    }

    public void setSelectMode(final SelectMode selectMode) {
        reducedFormWidget.setSelectMode(selectMode);
    }

    public String getSelectedItemsStyle() {
        return reducedFormWidget.getSelectedItemsStyle();
    }

    public void setSelectedItemsStyle(final String selectedItemsStyle) {
        reducedFormWidget.setSelectedItemsStyle(selectedItemsStyle);
    }

    public int getMaxRows() {
        return reducedFormWidget.getMaxRows();
    }

    public void setMaxRows(final int maxRows) {
        reducedFormWidget.setMaxRows(maxRows);
    }

    public int getMinRows() {
        return reducedFormWidget.getMinRows();
    }

    public void setMinRows(final int minRows) {
        reducedFormWidget.setMinRows(minRows);
    }

    public Expression getMaxColumnsExpression() {
        return maxColumnsExpression;
    }

    public void setMaxColumnsExpression(final Expression maxColumnsExpression) {
        this.maxColumnsExpression = maxColumnsExpression;
    }

    public Expression getMinColumnsExpression() {
        return minColumnsExpression;
    }

    public void setMinColumnsExpression(final Expression minColumnsExpression) {
        this.minColumnsExpression = minColumnsExpression;
    }

    public boolean isVariableRowNumber() {
        return reducedFormWidget.isVariableRowNumber();
    }

    public void setVariableRowNumber(final boolean isVariableRowNumber) {
        reducedFormWidget.setVariableRowNumber(isVariableRowNumber);
    }

    public boolean isVariableColumnNumber() {
        return reducedFormWidget.isVariableColumnNumber();
    }

    public void setVariableColumnNumber(final boolean isVariableColumnNumber) {
        reducedFormWidget.setVariableColumnNumber(isVariableColumnNumber);
    }

    public void setHtmlAttributes(final Map<String, String> htmlAttributes) {
        reducedFormWidget.setHtmlAttributes(htmlAttributes);
    }

    public Map<String, String> getHtmlAttributes() {
        if (reducedFormWidget.getHtmlAttributes() == null) {
            reducedFormWidget.setHtmlAttributes(new HashMap<String, String>());
        }
        return reducedFormWidget.getHtmlAttributes();
    }

    public void setChildWidgets(final List<FormWidget> childWidgets) {
        this.childWidgets = childWidgets;
        final List<ReducedFormWidget> reducedChildWidgets = new ArrayList<ReducedFormWidget>();
        for (final FormWidget formWidget : childWidgets) {
            reducedChildWidgets.add(formWidget.getReducedFormWidget());
        }
        reducedFormWidget.setChildWidgets(reducedChildWidgets);
    }

    public List<FormWidget> getChildWidgets() {
        if (childWidgets == null) {
            childWidgets = new ArrayList<FormWidget>();
        }
        return childWidgets;
    }

    public Set<String> getDependsOnWidgets() {
        if (reducedFormWidget.getDependsOnWidgets() == null) {
            reducedFormWidget.setDependsOnWidgets(new HashSet<String>());
        }
        return reducedFormWidget.getDependsOnWidgets();
    }

    public void setDependsOnWidgets(final Set<String> dependsOnWidgets) {
        reducedFormWidget.setDependsOnWidgets(dependsOnWidgets);
    }

    public Set<String> getIsUpdatedByWidgets() {
        if (reducedFormWidget.getIsUpdatedByWidgets() == null) {
            reducedFormWidget.setIsUpdatedByWidgets(new HashSet<String>());
        }
        return reducedFormWidget.getIsUpdatedByWidgets();
    }

    public void setIsUpdatedByWidgets(final Set<String> isUpdatedByWidgets) {
        reducedFormWidget.setIsUpdatedByWidgets(isUpdatedByWidgets);
    }

    public Expression getDisplayBeforeEventExpression() {
        return displayBeforeEventExpression;
    }

    public void setDisplayBeforeEventExpression(final Expression displayBeforeEventExpression) {
        this.displayBeforeEventExpression = displayBeforeEventExpression;
    }

    public Expression getDisplayAfterEventExpression() {
        return displayAfterEventExpression;
    }

    public void setDisplayAfterEventExpression(final Expression displayAfterEventExpression) {
        this.displayAfterEventExpression = displayAfterEventExpression;
    }

    public List<FormFieldAvailableValue> getAvailableValuesAfterEvent() {
        return availableValuesAfterEvent;
    }

    public void setAvailableValuesAfterEvent(final List<FormFieldAvailableValue> availableValuesAfterEvent) {
        this.availableValuesAfterEvent = availableValuesAfterEvent;
        final List<ReducedFormFieldAvailableValue> reducedAvailableValuesAfterEvent = new ArrayList<ReducedFormFieldAvailableValue>();
        for (final FormFieldAvailableValue formFieldAvailableValue : availableValuesAfterEvent) {
            reducedAvailableValuesAfterEvent.add(formFieldAvailableValue.getReducedFieldAvailableValue());
        }
        reducedFormWidget.setAvailableValuesAfterEvent(reducedAvailableValuesAfterEvent);
    }

    public Expression getAvailableValuesAfterEventExpression() {
        return availableValuesAfterEventExpression;
    }

    public void setAvailableValuesAfterEventExpression(final Expression availableValuesAfterEventExpression) {
        this.availableValuesAfterEventExpression = availableValuesAfterEventExpression;
    }

    public List<List<FormFieldAvailableValue>> getTableAvailableValuesAfterEvent() {
        return tableAvailableValuesAfterEvent;
    }

    public void setTableAvailableValuesAfterEvent(final List<List<FormFieldAvailableValue>> tableAvailableValuesAfterEvent) {
        this.tableAvailableValuesAfterEvent = tableAvailableValuesAfterEvent;
        final List<List<ReducedFormFieldAvailableValue>> reducedAvailableValues = new ArrayList<List<ReducedFormFieldAvailableValue>>();
        for (final List<FormFieldAvailableValue> tableAvailableValueRow : tableAvailableValuesAfterEvent) {
            final List<ReducedFormFieldAvailableValue> reducedAvailableValuesRow = new ArrayList<ReducedFormFieldAvailableValue>();
            for (final FormFieldAvailableValue tableAvailableValue : tableAvailableValueRow) {
                reducedAvailableValuesRow.add(tableAvailableValue.getReducedFieldAvailableValue());
            }
            reducedAvailableValues.add(reducedAvailableValuesRow);
        }
        reducedFormWidget.setTableAvailableValuesAfterEvent(reducedAvailableValues);
    }

    public Expression getValueAfterEventExpression() {
        return valueAfterEventExpression;
    }

    public void setValueAfterEventExpression(final Expression valueAfterEventExpression) {
        this.valueAfterEventExpression = valueAfterEventExpression;
    }

    public List<Connector> getInitialValueConnectors() {
        return initialValueConnectors;
    }

    public void setInitialValueConnectors(final List<Connector> initialValueConnectors) {
        this.initialValueConnectors = initialValueConnectors;
    }

    public List<Connector> getAvailableValuesConnectors() {
        return availableValuesConnectors;
    }

    public void setAvailableValuesConnectors(final List<Connector> availableValuesConnectors) {
        this.availableValuesConnectors = availableValuesConnectors;
    }

    public List<Connector> getAvailableValuesAfterEventConnectors() {
        return availableValuesAfterEventConnectors;
    }

    public void setAvailableValuesAfterEventConnectors(final List<Connector> availableValuesAfterEventConnectors) {
        this.availableValuesAfterEventConnectors = availableValuesAfterEventConnectors;
    }

    public List<Connector> getValueAfterEventConnectors() {
        return valueAfterEventConnectors;
    }

    public void setValueAfterEventConnectors(final List<Connector> valueAfterEventConnectors) {
        this.valueAfterEventConnectors = valueAfterEventConnectors;
    }

    public int getMaxItems() {
        return reducedFormWidget.getMaxItems();
    }

    public void setMaxItems(final int maxItems) {
        reducedFormWidget.setMaxItems(maxItems);
    }

    public int getMaxColumns() {
        return reducedFormWidget.getMaxColumns();
    }

    public void setMaxColumns(final int maxColumns) {
        reducedFormWidget.setMaxColumns(maxColumns);
    }

    public int getMinColumns() {
        return reducedFormWidget.getMinColumns();
    }

    public void setMinColumns(final int minColumns) {
        reducedFormWidget.setMinColumns(minColumns);
    }

    public boolean isDisplayBeforeEvent() {
        return reducedFormWidget.isDisplayBeforeEvent();
    }

    public void setDisplayBeforeEvent(final boolean displayBeforeEvent) {
        reducedFormWidget.setDisplayBeforeEvent(displayBeforeEvent);
    }

    public boolean isDisplayAfterEvent() {
        return reducedFormWidget.isDisplayAfterEvent();
    }

    public void setDisplayAfterEvent(final boolean displayAfterEvent) {
        reducedFormWidget.setDisplayAfterEvent(displayAfterEvent);
    }

    public int getDelayMillis() {
        return reducedFormWidget.getDelayMillis();
    }

    public void setDelayMillis(final int delayMillis) {
        reducedFormWidget.setDelayMillis(delayMillis);
    }

    public FormSubtitle getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(final FormSubtitle subtitle) {
        this.subtitle = subtitle;
        reducedFormWidget.setSubTitle(subtitle.getReducedFormSubTitle());
    }

    public void setPopupTooltip(final String popupToolTip) {
        if (popupToolTip != null) {
            reducedFormWidget.setPopupTooltip(popupToolTip);
        }
    }

    public String getPopupTooltip() {
        return reducedFormWidget.getPopupTooltip();
    }

    public Expression getLabelExpression() {
        return labelExpression;
    }

    public void setLabelExpression(final Expression labelExpression) {
        this.labelExpression = labelExpression;
    }

    public Expression getTitleExpression() {
        return titleExpression;
    }

    public void setTitleExpression(final Expression titleExpression) {
        this.titleExpression = titleExpression;
    }

    public Expression getPopupTooltipExpression() {
        return popupTooltipExpression;
    }

    public void setPopupTooltipExpression(final Expression popupTooltipExpression) {
        this.popupTooltipExpression = popupTooltipExpression;
    }

    public Expression getMaxInstancesExpression() {
        return maxInstancesExpression;
    }

    public void setMaxInstancesExpression(final Expression maxInstancesExpression) {
        this.maxInstancesExpression = maxInstancesExpression;
    }

    public Expression getMinInstancesExpression() {
        return minInstancesExpression;
    }

    public void setMinInstancesExpression(final Expression minInstancesExpression) {
        this.minInstancesExpression = minInstancesExpression;
    }

    public Expression getDisplayConditionExpression() {
        return displayConditionExpression;
    }

    public void setDisplayConditionExpression(final Expression displayConditionExpression) {
        this.displayConditionExpression = displayConditionExpression;
    }

    public Expression getValueColumnIndexExpression() {
        return valueColumnIndexExpression;
    }

    public void setValueColumnIndexExpression(final Expression valueColumnIndexExpression) {
        this.valueColumnIndexExpression = valueColumnIndexExpression;
    }

    public Expression getMaxRowsExpression() {
        return maxRowsExpression;
    }

    public void setMaxRowsExpression(final Expression maxRowsExpression) {
        this.maxRowsExpression = maxRowsExpression;
    }

    public Expression getMinRowsExpression() {
        return minRowsExpression;
    }

    public void setMinRowsExpression(final Expression minRowsExpression) {
        this.minRowsExpression = minRowsExpression;
    }

    public String getValidatorsCacheId() {
        return reducedFormWidget.getValidatorsCacheId();
    }

    public void setValidatorsCacheId(final String validatorsCacheId) {
        reducedFormWidget.setValidatorsCacheId(validatorsCacheId);
    }

    public ReducedFormWidget getReducedFormWidget() {
        return reducedFormWidget;
    }

    public void setReducedFormWidget(final ReducedFormWidget reducedFormWidget) {
        this.reducedFormWidget = reducedFormWidget;
    }

    public Expression getAddItemLabelExpression() {
        return addItemLabelExpression;
    }

    public void setAddItemLabelExpression(final Expression addItemLabelExpression) {
        this.addItemLabelExpression = addItemLabelExpression;
    }

    public Expression getAddItemTitleExpression() {
        return addItemTitleExpression;
    }

    public void setAddItemTitleExpression(final Expression addItemTitleExpression) {
        this.addItemTitleExpression = addItemTitleExpression;
    }

    public Expression getRemoveItemLabelExpression() {
        return removeItemLabelExpression;
    }

    public void setRemoveItemLabelExpression(final Expression removeItemLabelExpression) {
        this.removeItemLabelExpression = removeItemLabelExpression;
    }

    public Expression getRemoveItemTitleExpression() {
        return removeItemTitleExpression;
    }

    public void setRemoveItemTitleExpression(final Expression removeItemTitleExpression) {
        this.removeItemTitleExpression = removeItemTitleExpression;
    }

    public boolean hasDynamicValue() {
        return reducedFormWidget.hasDynamicValue();
    }

    public void setHasDynamicValue(final boolean hasDynamicValue) {
        reducedFormWidget.setHasDynamicValue(hasDynamicValue);
    }

    public boolean hasAvailableValuesAfterEvent() {
        return reducedFormWidget.hasAvailableValuesAfterEvent();
    }

    public void setHasAvailableValuesAfterEvent(final boolean hasAvailableValuesAfterEvent) {
        reducedFormWidget.setHasAvailableValuesAfterEvent(hasAvailableValuesAfterEvent);
    }

    public String getFormWidgetCacheId() {
        return reducedFormWidget.getFormWidgetCacheId();
    }

    public void setFormWidgetCacheId(final String formWidgetCacheId) {
        reducedFormWidget.setFormWidgetCacheId(formWidgetCacheId);
    }

    public FileWidgetInputType getFileWidgetInputType() {
        return reducedFormWidget.getFileWidgetInputType();
    }

    public void setFileWidgetInputType(final FileWidgetInputType fileInputType) {
        reducedFormWidget.setFileWidgetInputType(fileInputType);
    }

    public String getFieldOutputType() {
        return reducedFormWidget.getFieldOutputType();
    }

    public void setFieldOutputType(final String fieldOutputType) {
        reducedFormWidget.setFieldOutputType(fieldOutputType);
    }

    /**
     * Compare this form widget to another form widget using alphabetical order on their Id
     * 
     * {@inheritDoc}
     */
    @Override
    public int compareTo(final FormWidget otherFormWidget) {
        return reducedFormWidget.getId().toLowerCase().compareTo(otherFormWidget.getId().toLowerCase());
    }

}
