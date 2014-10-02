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

/**
 * Object representing a widget to place in a page of a task form flow
 * 
 * @author Anthony Birembaut
 */
public class ReducedFormWidget implements Serializable, Comparable<ReducedFormWidget> {

    /**
     * UID
     */
    private static final long serialVersionUID = -1320889539040109129L;

    /**
     * Possible label positions
     */
    public static enum ItemPosition {
        LEFT, TOP, RIGHT, BOTTOM
    };

    /**
     * Possible selection mode
     */
    public static enum SelectMode {
        NONE, SINGLE, MULTIPLE
    };

    /**
     * ID of the widget
     */
    private String id;

    /**
     * label of the widget
     */
    private String label;

    /**
     * CSS classes for the label
     */
    private String labelStyle;

    /**
     * CSS classes for the input
     */
    private String inputStyle;

    /**
     * label position
     */
    private ItemPosition labelPosition;

    /**
     * max length if the widget is a text input or a text area
     */
    private int maxLength;

    /**
     * max height if the widget is a text area
     */
    private int maxHeight;

    /**
     * type of the widget
     */
    private WidgetType type;

    /**
     * CSS classes for the widget
     */
    private String style;

    /**
     * title of the widget
     */
    private String title;

    /**
     * indicate whether a field is mandatory or not
     */
    private boolean mandatory;

    /**
     * indicates that this widget is part of a view page
     */
    private boolean viewPageWidget;

    /**
     * initial value of the field interpreted
     */
    private FormFieldValue initialFieldValue = new FormFieldValue();

    /**
     * List of validators to apply to the field
     */
    private List<ReducedFormValidator> validators;

    /**
     * The date display format
     */
    private String displayFormat;

    /**
     * CSS class names for the items of a radiobutton or checkbox group widget
     */
    private String itemsStyle;

    /**
     * Widget available values useful for selectboxes and radiobuttons
     */
    private List<ReducedFormFieldAvailableValue> reducedAvailableValues;

    /**
     * table available values
     */
    private List<List<ReducedFormFieldAvailableValue>> reducedTableAvailableValues;

    /**
     * if true indicates that a label should replace the button
     */
    private boolean labelButton;

    /**
     * if true indicates that HTML is allowed in the label
     */
    private boolean allowHTMLInLabel;

    /**
     * if true indicates that HTML is allowed in the field
     */
    private boolean allowHTMLInField;

    /**
     * The style for the img element of the widgets containing an image
     */
    private String imageStyle;

    /**
     * The style for the table element of the widgets containing a table
     */
    private String tableStyle;

    /**
     * The style for a table widget's cells
     */
    private String cellsStyle;

    /**
     * The horizontal header for tables
     */
    private List<String> horizontalHeader;

    /**
     * The vertical header for tables
     */
    private List<String> verticalHeader;

    /**
     * The Headings syle for table widgets
     */
    private String headingsStyle;

    /**
     * indicates whether the left column of a table widget should be considered as header or not
     */
    private boolean leftHeadings;

    /**
     * indicates whether the top column of a table widget should be considered as header or not
     */
    private boolean topHeadings;

    /**
     * indicates whether the right column of a table widget should be considered as header or not
     */
    private boolean rightHeadings;

    /**
     * indicates whether the bottom row of a table widget should be considered as header or not
     */
    private boolean bottomHeadings;

    /**
     * Selection mode for static tables
     */
    private SelectMode selectMode;

    /**
     * CSS classes for the selected items
     */
    private String selectedItemsStyle;

    /**
     * indicates if the row number is variable or not
     */
    private boolean isVariableRowNumber;

    /**
     * indicates if the column number is variable or not
     */
    private boolean isVariableColumnNumber;

    /**
     * The max number of rows for tables
     */
    private int maxRows;

    /**
     * The min number of rows for tables
     */
    private int minRows;

    /**
     * The max number of columns for tables
     */
    private int maxColumns;

    /**
     * The min number of columns for tables
     */
    private int minColumns;

    /**
     * specify the index of column which is used as the value of the selected row(s) (for table widgets)
     */
    private int valueColumnIndex;

    /**
     * if true indicates that a label should replace the button
     */
    private boolean readOnly;

    /**
     * if true indicates that the widget can be duplicated
     */
    private boolean multiple;

    /**
     * if true indicates that the widget is bound to a document list
     */
    private boolean documentList;
    
    /**
     * The max number of instances
     */
    private int maxInstances = -1;

    /**
     * The min number of instances
     */
    private int minInstances = -1;

    /**
     * The the iterator name
     */
    private String iteratorName;

    /**
     * The max number of items
     */
    private int maxItems;

    /**
     * try to display a preview if the attachment is an image (for file download widgets)
     */
    private boolean displayAttachmentImage;

    /**
     * Condition to display or not a data field
     */
    private boolean displayCondition;

    /**
     * HTML attributes
     */
    private Map<String, String> htmlAttributes;

    /**
     * the add item label
     */
    private String addItemLabel;

    /**
     * the add item title
     */
    private String addItemTitle;

    /**
     * the add item label style
     */
    private String addItemLabelStyle;

    /**
     * the remove item label
     */
    private String removeItemLabel;

    /**
     * the remove item label
     */
    private String removeItemTitle;

    /**
     * the remove item label style
     */
    private String removeItemLabelStyle;

    /**
     * child widgets
     */
    private List<ReducedFormWidget> childWidgets;

    /**
     * The widget position inside a group
     */
    private WidgetPosition widgetPositionInGroup;

    /**
     * rows styles for group widgets
     */
    private Map<Integer, String> rowsStyles;

    /**
     * columns styles for group widgets
     */
    private Map<Integer, String> columnsStyles;

    /**
     * List of ids of widgets this widget depends on
     */
    private Set<String> dependsOnWidgets;

    /**
     * List of ids of widgets this widget is updated by
     */
    private Set<String> isUpdatedByWidgets;

    /**
     * Condition to display or not a data field before an event
     */
    private boolean displayBeforeEvent;

    /**
     * Condition to display or not a data field after an event
     */
    private boolean displayAfterEvent;

    /**
     * Widget available values after an event triggered by a dependency
     */
    private List<ReducedFormFieldAvailableValue> reducedAvailableValuesAfterEvent;

    /**
     * table available values after an event triggered by a dependency
     */
    private List<List<ReducedFormFieldAvailableValue>> reducedTableAvailableValuesAfterEvent;

    /**
     * Display asnySuggestBox delay time
     */
    private int delayMillis;

    /**
     * Display a tool tip
     */
    private String popupTooltip;

    /**
     * display a sub title
     */
    private ReducedFormSubtitle subtitle;

    /**
     * use this id to get the field validators from the cache
     */
    private String validatorsCacheId;

    /**
     * use this id to get form widget from cache
     */
    private String formWidgetCacheId;

    /**
     * indicate if the widget has a dynamic initial value or available values
     */
    private boolean hasDynamicValue;

    /**
     * indicates that the widget has some available values after event
     */
    private boolean hasAvailableValuesAfterEvent;

    /**
     * the input type for file widgets
     */
    private FileWidgetInputType fileWidgetInputType;

    /**
     * the type in which the value will be convert when submitted
     */
    private String fieldOutputType;

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
     * @param popupTooltip
     */
    public ReducedFormWidget(final String id, final int maxLength, final int maxHeight, final WidgetType type, final String style,
            final String labelStyle, final String inputStyle, final ItemPosition labelPosition, final boolean mandatory) {
        this.id = id;
        this.maxLength = maxLength;
        this.maxHeight = maxHeight;
        this.type = type;
        this.style = style;
        this.labelStyle = labelStyle;
        setInputStyle(inputStyle);
        this.mandatory = mandatory;
        this.labelPosition = labelPosition;
    }

    /**
     * Default Constructor
     */
    public ReducedFormWidget() {
        super();
        // Mandatory for serialization
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(final int maxLength) {
        this.maxLength = maxLength;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(final int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public WidgetType getType() {
        return type;
    }

    public void setType(final WidgetType type) {
        this.type = type;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(final String style) {
        this.style = style;
    }

    public List<ReducedFormValidator> getValidators() {
        if (validators == null) {
            validators = new ArrayList<ReducedFormValidator>();
        }
        return validators;
    }

    public void setValidators(final List<ReducedFormValidator> validators) {
        this.validators = validators;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(final String label) {
        this.label = label;
    }

    public String getLabelStyle() {
        return labelStyle;
    }

    public void setLabelStyle(final String labelStyle) {
        this.labelStyle = labelStyle;
    }

    public String getInputStyle() {
        return inputStyle;
    }

    public void setInputStyle(final String inputStyle) {
        this.inputStyle = inputStyle;
    }

    public ItemPosition getLabelPosition() {
        return labelPosition;
    }

    public void setLabelPosition(final ItemPosition labelPosition) {
        this.labelPosition = labelPosition;
    }

    public List<ReducedFormFieldAvailableValue> getAvailableValues() {
        if (reducedAvailableValues == null) {
            reducedAvailableValues = new ArrayList<ReducedFormFieldAvailableValue>();
        }
        return reducedAvailableValues;
    }

    public void setAvailableValues(final List<ReducedFormFieldAvailableValue> availableValues) {
        reducedAvailableValues = availableValues;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(final boolean mandatory) {
        this.mandatory = mandatory;
    }

    public FormFieldValue getInitialFieldValue() {
        return initialFieldValue;
    }

    public void setInitialFieldValue(final FormFieldValue initialFieldValue) {
        this.initialFieldValue = initialFieldValue;
    }

    public String getDisplayFormat() {
        return displayFormat;
    }

    public void setDisplayFormat(final String displayFormat) {
        this.displayFormat = displayFormat;
    }

    public boolean isViewPageWidget() {
        return viewPageWidget;
    }

    public void setViewPageWidget(final boolean viewPageWidget) {
        this.viewPageWidget = viewPageWidget;
    }

    public String getItemsStyle() {
        return itemsStyle;
    }

    public void setItemsStyle(final String itemsStyle) {
        this.itemsStyle = itemsStyle;
    }

    public boolean isLabelButton() {
        return labelButton;
    }

    public void setLabelButton(final boolean labelButton) {
        this.labelButton = labelButton;
    }

    public boolean allowHTMLInLabel() {
        return allowHTMLInLabel;
    }

    public void setAllowHTMLInLabel(final boolean allowHTMLInLabel) {
        this.allowHTMLInLabel = allowHTMLInLabel;
    }

    public boolean allowHTMLInField() {
        return allowHTMLInField;
    }

    public void setAllowHTMLInField(final boolean allowHTMLInField) {
        this.allowHTMLInField = allowHTMLInField;
    }

    public String getImageStyle() {
        return imageStyle;
    }

    public void setImageStyle(final String imageStyle) {
        this.imageStyle = imageStyle;
    }

    public String getHeadingsStyle() {
        return headingsStyle;
    }

    public void setHeadingsStyle(final String headingsStyle) {
        this.headingsStyle = headingsStyle;
    }

    public boolean hasLeftHeadings() {
        return leftHeadings;
    }

    public void setLeftHeadings(final boolean leftHeadings) {
        this.leftHeadings = leftHeadings;
    }

    public boolean hasTopHeadings() {
        return topHeadings;
    }

    public void setTopHeadings(final boolean topHeadings) {
        this.topHeadings = topHeadings;
    }

    public boolean hasRightHeadings() {
        return rightHeadings;
    }

    public void setRightHeadings(final boolean rightHeadings) {
        this.rightHeadings = rightHeadings;
    }

    public boolean hasBottomHeadings() {
        return bottomHeadings;
    }

    public void setBottomHeadings(final boolean bottomHeadings) {
        this.bottomHeadings = bottomHeadings;
    }

    public String getTableStyle() {
        return tableStyle;
    }

    public void setTableStyle(final String tableStyle) {
        this.tableStyle = tableStyle;
    }

    public String getCellsStyle() {
        return cellsStyle;
    }

    public void setCellsStyle(final String cellsStyle) {
        this.cellsStyle = cellsStyle;
    }

    public void setReadOnly(final boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setMultiple(final boolean multiple) {
        this.multiple = multiple;
    }
	
    public boolean isMultiple() {
        return multiple;
    }

    public int getValueColumnIndex() {
        return valueColumnIndex;
    }

    public void setValueColumnIndex(final int valueColumnIndex) {
        this.valueColumnIndex = valueColumnIndex;
    }

    public int getMaxInstances() {
        return maxInstances;
    }

    public void setMaxInstances(final int maxInstances) {
        this.maxInstances = maxInstances;
    }

    public int getMinInstances() {
        return minInstances;
    }

    public void setMinInstances(final int minInstances) {
        this.minInstances = minInstances;
    }

    public String getIteratorName() {
        return iteratorName;
    }

    public void setIteratorName(final String iteratorName) {
        this.iteratorName = iteratorName;
    }

    public void setDisplayAttachmentImage(final boolean displayAttachmentImage) {
        this.displayAttachmentImage = displayAttachmentImage;
    }

    public boolean isDisplayAttachmentImage() {
        return displayAttachmentImage;
    }

    public boolean isDisplayCondition() {
        return displayCondition;
    }

    public void setDisplayCondition(final boolean displayCondition) {
        this.displayCondition = displayCondition;
    }

    public String getAddItemLabel() {
        return addItemLabel;
    }

    public void setAddItemLabel(final String addItemLabel) {
        this.addItemLabel = addItemLabel;
    }

    public String getAddItemTitle() {
        return addItemTitle;
    }

    public void setAddItemTitle(final String addItemTitle) {
        this.addItemTitle = addItemTitle;
    }

    public String getAddItemLabelStyle() {
        return addItemLabelStyle;
    }

    public void setAddItemLabelStyle(final String addItemLabelStyle) {
        this.addItemLabelStyle = addItemLabelStyle;
    }

    public String getRemoveItemLabel() {
        return removeItemLabel;
    }

    public void setRemoveItemLabel(final String removeItemLabel) {
        this.removeItemLabel = removeItemLabel;
    }

    public String getRemoveItemTitle() {
        return removeItemTitle;
    }

    public void setRemoveItemTitle(final String removeItemTitle) {
        this.removeItemTitle = removeItemTitle;
    }

    public String getRemoveItemLabelStyle() {
        return removeItemLabelStyle;
    }

    public void setRemoveItemLabelStyle(final String removeItemLabelStyle) {
        this.removeItemLabelStyle = removeItemLabelStyle;
    }

    public WidgetPosition getWidgetPositionInGroup() {
        return widgetPositionInGroup;
    }

    public void setWidgetPositionInGroup(final WidgetPosition widgetPositionInGroup) {
        this.widgetPositionInGroup = widgetPositionInGroup;
    }

    public Map<Integer, String> getRowsStyles() {
        return rowsStyles;
    }

    public void setRowsStyles(final Map<Integer, String> rowsStyles) {
        this.rowsStyles = rowsStyles;
    }

    public Map<Integer, String> getColumnsStyles() {
        return columnsStyles;
    }

    public void setColumnsStyles(final Map<Integer, String> columnsStyles) {
        this.columnsStyles = columnsStyles;
    }

    public List<List<ReducedFormFieldAvailableValue>> getTableAvailableValues() {
        return reducedTableAvailableValues;
    }

    public void setTableAvailableValues(final List<List<ReducedFormFieldAvailableValue>> tableAvailableValues) {
        reducedTableAvailableValues = tableAvailableValues;
    }

    public List<String> getHorizontalHeader() {
        return horizontalHeader;
    }

    public void setHorizontalHeader(final List<String> horizontalHeader) {
        this.horizontalHeader = horizontalHeader;
    }

    public List<String> getVerticalHeader() {
        return verticalHeader;
    }

    public void setVerticalHeader(final List<String> verticalHeader) {
        this.verticalHeader = verticalHeader;
    }

    public SelectMode getSelectMode() {
        return selectMode;
    }

    public void setSelectMode(final SelectMode selectMode) {
        this.selectMode = selectMode;
    }

    public String getSelectedItemsStyle() {
        return selectedItemsStyle;
    }

    public void setSelectedItemsStyle(final String selectedItemsStyle) {
        this.selectedItemsStyle = selectedItemsStyle;
    }

    public int getMaxRows() {
        return maxRows;
    }

    public void setMaxRows(final int maxRows) {
        this.maxRows = maxRows;
    }

    public int getMinRows() {
        return minRows;
    }

    public void setMinRows(final int minRows) {
        this.minRows = minRows;
    }

    public boolean isVariableRowNumber() {
        return isVariableRowNumber;
    }

    public void setVariableRowNumber(final boolean isVariableRowNumber) {
        this.isVariableRowNumber = isVariableRowNumber;
    }

    public boolean isVariableColumnNumber() {
        return isVariableColumnNumber;
    }

    public void setVariableColumnNumber(final boolean isVariableColumnNumber) {
        this.isVariableColumnNumber = isVariableColumnNumber;
    }

    public void setHtmlAttributes(final Map<String, String> htmlAttributes) {
        this.htmlAttributes = htmlAttributes;
    }

    public Map<String, String> getHtmlAttributes() {
        if (htmlAttributes == null) {
            htmlAttributes = new HashMap<String, String>();
        }
        return htmlAttributes;
    }

    public void setChildWidgets(final List<ReducedFormWidget> childWidgets) {
        this.childWidgets = childWidgets;
    }

    public List<ReducedFormWidget> getChildWidgets() {
        if (childWidgets == null) {
            childWidgets = new ArrayList<ReducedFormWidget>();
        }
        return childWidgets;
    }

    public Set<String> getDependsOnWidgets() {
        if (dependsOnWidgets == null) {
            dependsOnWidgets = new HashSet<String>();
        }
        return dependsOnWidgets;
    }

    public void setDependsOnWidgets(final Set<String> dependsOnWidgets) {
        this.dependsOnWidgets = dependsOnWidgets;
    }

    public Set<String> getIsUpdatedByWidgets() {
        if (isUpdatedByWidgets == null) {
            isUpdatedByWidgets = new HashSet<String>();
        }
        return isUpdatedByWidgets;
    }

    public void setIsUpdatedByWidgets(final Set<String> isUpdatedByWidgets) {
        this.isUpdatedByWidgets = isUpdatedByWidgets;
    }

    public List<ReducedFormFieldAvailableValue> getAvailableValuesAfterEvent() {
        return reducedAvailableValuesAfterEvent;
    }

    public void setAvailableValuesAfterEvent(final List<ReducedFormFieldAvailableValue> availableValuesAfterEvent) {
        reducedAvailableValuesAfterEvent = availableValuesAfterEvent;
    }

    public List<List<ReducedFormFieldAvailableValue>> getTableAvailableValuesAfterEvent() {
        return reducedTableAvailableValuesAfterEvent;
    }

    public void setTableAvailableValuesAfterEvent(final List<List<ReducedFormFieldAvailableValue>> tableAvailableValuesAfterEvent) {
        reducedTableAvailableValuesAfterEvent = tableAvailableValuesAfterEvent;
    }

    public int getMaxItems() {
        return maxItems;
    }

    public void setMaxItems(final int maxItems) {
        this.maxItems = maxItems;
    }

    public int getDelayMillis() {
        return delayMillis;
    }

    public void setDelayMillis(final int delayMillis) {
        this.delayMillis = delayMillis;
    }

    public ReducedFormSubtitle getSubtitle() {
        return subtitle;
    }

    public void setSubTitle(final ReducedFormSubtitle subtitle) {
        this.subtitle = subtitle;
    }

    public void setPopupTooltip(final String popupTooltip) {
        this.popupTooltip = popupTooltip;
    }

    public String getPopupTooltip() {
        return popupTooltip;
    }

    public int getMaxColumns() {
        return maxColumns;
    }

    public void setMaxColumns(final int maxColumns) {
        this.maxColumns = maxColumns;
    }

    public int getMinColumns() {
        return minColumns;
    }

    public void setMinColumns(final int minColumns) {
        this.minColumns = minColumns;
    }

    public boolean isDisplayBeforeEvent() {
        return displayBeforeEvent;
    }

    public void setDisplayBeforeEvent(final boolean displayBeforeEvent) {
        this.displayBeforeEvent = displayBeforeEvent;
    }

    public boolean isDisplayAfterEvent() {
        return displayAfterEvent;
    }

    public void setDisplayAfterEvent(final boolean displayAfterEvent) {
        this.displayAfterEvent = displayAfterEvent;
    }

    public String getValidatorsCacheId() {
        return validatorsCacheId;
    }

    public void setValidatorsCacheId(final String validatorsCacheId) {
        this.validatorsCacheId = validatorsCacheId;
    }

    public String getFormWidgetCacheId() {
        return formWidgetCacheId;
    }

    public void setFormWidgetCacheId(final String formWidgetCacheId) {
        this.formWidgetCacheId = formWidgetCacheId;
    }

    public boolean hasDynamicValue() {
        return hasDynamicValue;
    }

    public void setHasDynamicValue(final boolean hasDynamicValue) {
        this.hasDynamicValue = hasDynamicValue;
    }

    public boolean hasAvailableValuesAfterEvent() {
        return hasAvailableValuesAfterEvent;
    }

    public void setHasAvailableValuesAfterEvent(final boolean hasAvailableValuesAfterEvent) {
        this.hasAvailableValuesAfterEvent = hasAvailableValuesAfterEvent;
    }

    public FileWidgetInputType getFileWidgetInputType() {
        return fileWidgetInputType;
    }

    public void setFileWidgetInputType(final FileWidgetInputType fileWidgetInputType) {
        this.fileWidgetInputType = fileWidgetInputType;
    }

    public String getFieldOutputType() {
        return fieldOutputType;
    }

    public void setFieldOutputType(final String fieldOutputType) {
        this.fieldOutputType = fieldOutputType;
    }

    /**
     * Compare this form widget to another form widget using alphabetical order on their Id
     * 
     * {@inheritDoc}
     */
    @Override
    public int compareTo(final ReducedFormWidget otherFormWidget) {
        return id.toLowerCase().compareTo(otherFormWidget.getId().toLowerCase());
    }

}
