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
package org.bonitasoft.forms.server.accessor.widget.impl;

import org.bonitasoft.forms.client.model.*;
import org.bonitasoft.forms.client.model.ReducedFormSubtitle.SubTitlePosition;
import org.bonitasoft.forms.client.model.ReducedFormValidator.ValidatorPosition;
import org.bonitasoft.forms.client.model.ReducedFormWidget.ItemPosition;
import org.bonitasoft.forms.client.model.ReducedFormWidget.SelectMode;
import org.bonitasoft.forms.server.accessor.impl.util.XPathUtil;
import org.bonitasoft.forms.server.accessor.widget.IXMLWidgetBuilder;
import org.bonitasoft.forms.server.constants.XMLForms;
import org.bonitasoft.forms.server.exception.InvalidFormDefinitionException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Accessor used to read the page nodes in the XML definition file and retrieve the widgets and validators
 * 
 * @author Anthony Birembaut
 */
public class XMLWidgetBuilderImpl extends XPathUtil implements IXMLWidgetBuilder {

    /**
     * Logger
     */
    private static Logger LOGGER = Logger.getLogger(XMLWidgetBuilderImpl.class.getName());

    /**
     * Util class to parse expressions
     */
    protected XMLExpressionsUtil xmlExpressionsUtil;

    /**
     * Instance attribute
     */
    protected static XMLWidgetBuilderImpl INSTANCE = null;

    /**
     * @return the XMLWidgetBuilderImpl instance
     */
    public static synchronized XMLWidgetBuilderImpl getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new XMLWidgetBuilderImpl();
        }
        return INSTANCE;
    }

    /**
     * Private contructor to prevent instantiation
     */
    protected XMLWidgetBuilderImpl() {
        xmlExpressionsUtil = new XMLExpressionsUtil();
    }

    /**
     * Read a page node and return the list of {@link FormValidator} it contains
     * 
     * @param pageNode
     *            the page node
     * @return a {@link List} of {@link FormValidator} Object
     * @throws InvalidFormDefinitionException
     */
    @Override
    public List<FormValidator> getPageValidators(final Node pageNode) throws InvalidFormDefinitionException {

        final List<FormValidator> pageValidators = new ArrayList<FormValidator>();

        final String xpath = XMLForms.PAGE_VALIDATORS + "/" + XMLForms.VALIDATOR;
        final NodeList pageValidatorNodes = getNodeListByXpath(pageNode, xpath);
        if (pageValidatorNodes == null) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, "Failed to parse the form definition file. query : " + xpath);
            }
        } else {
            for (int i = 0; i < pageValidatorNodes.getLength(); i++) {
                final Node pageValidatorNode = pageValidatorNodes.item(i);
                final String validatorId = getStringByXpath(pageValidatorNode, "@" + XMLForms.ID);
                final String validatorClass = getStringByXpath(pageValidatorNode, XMLForms.CLASSNAME);
                final String validatorStyle = getStringByXpath(pageValidatorNode, XMLForms.STYLE);
                final ValidatorPosition validatorPosition = getValidatorPositionValue(getNodeByXpath(pageValidatorNode, XMLForms.POSITION));
                final FormValidator formValidator = new FormValidator(validatorId, validatorClass, validatorStyle);
                final Node validatorLabelNode = getNodeByXpath(pageValidatorNode, XMLForms.LABEL);
                if (validatorLabelNode != null) {
                    final Expression labelExpression = xmlExpressionsUtil.parseExpression(validatorId, validatorLabelNode);
                    formValidator.setLabelExpression(labelExpression);
                }
                formValidator.setPosition(validatorPosition);
                final Node validatorParameterNode = getNodeByXpath(pageValidatorNode, XMLForms.PARAMETER);
                if (validatorParameterNode != null) {
                    final Expression parameterExpression = xmlExpressionsUtil.parseExpression(validatorId, validatorParameterNode);
                    formValidator.setParameterExpression(parameterExpression);
                }
                pageValidators.add(formValidator);
            }
        }
        return pageValidators;
    }

    /**
     * Read a page node and return the list of {@link FormWidget} it contains
     * 
     * @param pageNode
     *            the page node
     * @param isEditMode
     * @return a {@link List} of {@link FormWidget} Object
     */
    @Override
    public List<FormWidget> getPageWidgets(final Node pageNode, final boolean isEditMode) {

        final List<FormWidget> widgets = new ArrayList<FormWidget>();

        final String xpath = XMLForms.WIDGETS + "/" + XMLForms.WIDGET;

        final NodeList widgetNodes = getNodeListByXpath(pageNode, xpath);
        if (widgetNodes == null) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, "Failed to parse the form definition file. query : " + xpath);
            }
        } else {
            for (int i = 0; i < widgetNodes.getLength(); i++) {
                try {
                    final FormWidget widget = parseWidget(widgetNodes.item(i), isEditMode);
                    widget.setViewPageWidget(!isEditMode);
                    widgets.add(widget);
                } catch (final InvalidFormDefinitionException e) {
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER.log(Level.WARNING, "invalid widget definition", e);
                    }
                }
            }
        }
        return widgets;
    }

    protected void parsefileUploadPaths(final Node initialValueNode, final FormWidget formWidget) {
        final Node resourceNode = getNodeByXpath(initialValueNode, XMLForms.RESOURCE);
        if (resourceNode != null) {
            final String path = getStringByXpath(resourceNode, XMLForms.PATH);
            formWidget.setFilePaths(path);
        }
    }

    /**
     * Read a widget and return a {@link FormWidget}
     * 
     * @param widgetNode
     *            the widget node
     * @param isEditMode
     * @return a {@link FormWidget} object
     * @throws InvalidFormDefinitionException
     */
    protected FormWidget parseWidget(final Node widgetNode, final boolean isEditMode) throws InvalidFormDefinitionException {

        final String id = getStringByXpath(widgetNode, "@" + XMLForms.ID);
        final WidgetType type = getWidgetTypeValue(getStringByXpath(widgetNode, "@" + XMLForms.TYPE));
        final int maxLength = getIntValue(getNodeByXpath(widgetNode, XMLForms.MAX_LENGTH));
        final int maxHeight = getIntValue(getNodeByXpath(widgetNode, XMLForms.MAX_HEIGHT));
        final String styleNames = getStringByXpath(widgetNode, XMLForms.STYLE);
        final String labelStyleNames = getStringByXpath(widgetNode, XMLForms.LABEL_STYLE);
        final String inputStyleNames = getStringByXpath(widgetNode, XMLForms.INPUT_STYLE);
        final ItemPosition labelPosition = getItemPositionValue(getNodeByXpath(widgetNode, XMLForms.LABEL_POSITION));
        final boolean mandatory = getBooleanValue(getNodeByXpath(widgetNode, XMLForms.MANDATORY));

        final FormWidget formWidget = new FormWidget(id, maxLength, maxHeight, type, styleNames, labelStyleNames, inputStyleNames, labelPosition, mandatory);

        formWidget.setFieldOutputType(getStringByXpath(widgetNode, XMLForms.FIELD_OUTPUT_TYPE));

        final Node labelNode = getNodeByXpath(widgetNode, XMLForms.LABEL);
        if (labelNode != null) {
            formWidget.setLabelExpression(xmlExpressionsUtil.parseExpression(id, labelNode));
        }
        final Node titleNode = getNodeByXpath(widgetNode, XMLForms.TITLE);
        if (titleNode != null) {
            formWidget.setTitleExpression(xmlExpressionsUtil.parseExpression(id, titleNode));
        }
        final Node initialValueNode = getNodeByXpath(widgetNode, XMLForms.INITIAL_VALUE);
        if (initialValueNode != null) {
            // add the fileUpload initialValue
            if (WidgetType.FILEUPLOAD.equals(type)) {
                parsefileUploadPaths(initialValueNode, formWidget);
            }
            final Node initialValueArrayNode = getNodeByXpath(initialValueNode, XMLForms.EXPRESSION_ARRAY);
            if (initialValueArrayNode != null) {
                formWidget.setInitialValueExpressionArray(xmlExpressionsUtil.parseExpressionsArray(id, initialValueArrayNode));
            } else {
                formWidget.setInitialValueExpression(xmlExpressionsUtil.parseExpression(id, initialValueNode));
            }
        }
        // add the fileUpload input type
        if (WidgetType.FILEUPLOAD.equals(type)) {
            final Node fileInputTypeNode = getNodeByXpath(widgetNode, XMLForms.FILE_INPUT_TYPE);
            formWidget.setFileWidgetInputType(getFileWidgetInputType(fileInputTypeNode));
        }
        final boolean labelButton = getBooleanValue(getNodeByXpath(widgetNode, XMLForms.LABEL_BUTTON));
        formWidget.setLabelButton(labelButton);
        final String itemsStyleNames = getStringByXpath(widgetNode, XMLForms.ITEMS_STYLE);
        formWidget.setItemsStyle(itemsStyleNames);

        final Node availableValuesNode = getNodeByXpath(widgetNode, XMLForms.AVAILABLE_VALUES);
        if (availableValuesNode != null) {
            final Node availableValuesListNode = getNodeByXpath(availableValuesNode, XMLForms.VALUES_LIST);
            if (availableValuesListNode != null) {
                formWidget.setAvailableValues(getAvailableValues(id, availableValuesListNode));
            } else {
                final Node availableValuesArrayNode = getNodeByXpath(availableValuesNode, XMLForms.VALUES_ARRAY);
                if (availableValuesArrayNode != null) {
                    final NodeList availableValuesRowNodes = getNodeListByXpath(availableValuesArrayNode, XMLForms.ROW);
                    final List<List<FormFieldAvailableValue>> availableValuesArray = new ArrayList<List<FormFieldAvailableValue>>();
                    for (int i = 0; i < availableValuesRowNodes.getLength(); i++) {
                        final Node availableValuesRowNode = availableValuesRowNodes.item(i);
                        availableValuesArray.add(getAvailableValues(id, availableValuesRowNode));
                    }
                    formWidget.setTableAvailableValues(availableValuesArray);
                } else {
                    formWidget.setAvailableValuesExpression(xmlExpressionsUtil.parseExpression(id, availableValuesNode));
                }
            }
        }

        final NodeList validatorNodes = getNodeListByXpath(widgetNode, XMLForms.VALIDATORS + "/" + XMLForms.VALIDATOR);
        final List<FormValidator> validators = new ArrayList<FormValidator>();
        for (int i = 0; i < validatorNodes.getLength(); i++) {
            final Node validatorNode = validatorNodes.item(i);
            final String validatorId = getStringByXpath(validatorNode, "@" + XMLForms.ID);
            final String validatorClass = getStringByXpath(validatorNode, XMLForms.CLASSNAME);
            final String validatorStyle = getStringByXpath(validatorNode, XMLForms.STYLE);
            final ValidatorPosition validatorPosition = getValidatorPositionValue(getNodeByXpath(validatorNode, XMLForms.POSITION));
            final FormValidator formValidator = new FormValidator(validatorId, validatorClass, validatorStyle);
            final Node validatorLabelNode = getNodeByXpath(validatorNode, XMLForms.LABEL);
            if (validatorLabelNode != null) {
                final Expression labelExpression = xmlExpressionsUtil.parseExpression(id, validatorLabelNode);
                formValidator.setLabelExpression(labelExpression);
            }
            formValidator.setPosition(validatorPosition);
            final Node validatorParameterNode = getNodeByXpath(validatorNode, XMLForms.PARAMETER);
            if (validatorParameterNode != null) {
                final Expression parameterExpression = xmlExpressionsUtil.parseExpression(id, validatorParameterNode);
                formValidator.setParameterExpression(parameterExpression);
            }
            validators.add(formValidator);
        }
        formWidget.setValidators(validators);

        if (WidgetType.TABLE.equals(type) || WidgetType.EDITABLE_GRID.equals(type)) {
            parseTableAttributes(widgetNode, formWidget);
        }
        if (WidgetType.IMAGE.equals(type)) {
            formWidget.setImageStyle(getStringByXpath(widgetNode, XMLForms.IMAGE_STYLE));
        }

        final Node displayConditionNode = getNodeByXpath(widgetNode, XMLForms.DISPLAY_CONDITION);
        if (displayConditionNode != null) {
            formWidget.setDisplayConditionExpression(xmlExpressionsUtil.parseExpression(id, displayConditionNode));
        }
        // formats for dates
        formWidget.setDisplayFormat(getStringByXpath(widgetNode, XMLForms.DISPLAY_FORMAT));
        // image preview for download widgets
        formWidget.setDisplayAttachmentImage(getBooleanValue(getNodeByXpath(widgetNode, XMLForms.DISPLAY_ATTACHMENT_IMAGE)));

        formWidget.setAllowHTMLInLabel(getBooleanValue(getNodeByXpath(widgetNode, XMLForms.ALLOW_HTML_IN_LABEL)));
        formWidget.setAllowHTMLInField(getBooleanValue(getNodeByXpath(widgetNode, XMLForms.ALLOW_HTML_IN_FIELD)));

        final Map<String, String> htmlAttributes = new HashMap<String, String>();
        final NodeList htmlAttributeNodes = getNodeListByXpath(widgetNode, XMLForms.HTML_ATTRIBUTES + "/" + XMLForms.HTML_ATTRIBUTE);
        for (int i = 0; i < htmlAttributeNodes.getLength(); i++) {
            final Node htmlAttributeNode = htmlAttributeNodes.item(i);
            final String attributeName = getStringByXpath(htmlAttributeNode, "@" + XMLForms.NAME);
            final String attributeValue = htmlAttributeNode.getTextContent();
            htmlAttributes.put(attributeName, attributeValue);
        }
        formWidget.setHtmlAttributes(htmlAttributes);

        formWidget.setMaxItems(getIntValue(getNodeByXpath(widgetNode, XMLForms.MAX_ITEMS)));

        boolean readOnly = false;
        if (!isEditMode || type.equals(WidgetType.TEXT) || type.equals(WidgetType.MESSAGE) || type.equals(WidgetType.FILEDOWNLOAD)
                || type.equals(WidgetType.IMAGE) || type.equals(WidgetType.IFRAME) || type.name().startsWith("BUTTON")
                || type.equals(WidgetType.TABLE) && SelectMode.NONE.equals(formWidget.getSelectMode())) {
            readOnly = true;
        } else {
            readOnly = getBooleanValue(getNodeByXpath(widgetNode, XMLForms.READ_ONLY));
        }
        formWidget.setReadOnly(readOnly);
        formWidget.setDelayMillis(getIntValue(getNodeByXpath(widgetNode, XMLForms.DELAY_MILLIS)));

        final Node popupTooltipNode = getNodeByXpath(widgetNode, XMLForms.POPUP_TOOLTIP);
        if (popupTooltipNode != null) {
            formWidget.setPopupTooltipExpression(xmlExpressionsUtil.parseExpression(id, popupTooltipNode));
        }

        final Node subTitleNode = getNodeByXpath(widgetNode, XMLForms.SUB_TITLE);
        if (subTitleNode != null) {
            final SubTitlePosition subTitlePosition = getSubTitlePositionValue(getNodeByXpath(subTitleNode, XMLForms.POSITION));
            final FormSubtitle formSubTitle = new FormSubtitle(subTitlePosition);
            final Node subtitleLabelNode = getNodeByXpath(subTitleNode, XMLForms.LABEL);
            formSubTitle.setLabelExpression(xmlExpressionsUtil.parseExpression(id, subtitleLabelNode));
            formWidget.setSubtitle(formSubTitle);
        }
        return formWidget;
    }

    protected void parseTableAttributes(final Node widgetNode, final FormWidget formWidget) throws InvalidFormDefinitionException {

        final Node tableStyleNode = getNodeByXpath(widgetNode, XMLForms.TABLE_STYLE);
        if (tableStyleNode != null) {
            formWidget.setTableStyle(tableStyleNode.getTextContent());
        }
        final Node cellsStyleNode = getNodeByXpath(widgetNode, XMLForms.CELL_STYLE);
        if (cellsStyleNode != null) {
            formWidget.setCellsStyle(cellsStyleNode.getTextContent());
        }
        final Node verticalHeaderNode = getNodeByXpath(widgetNode, XMLForms.VERTICAL_HEADER);
        if (verticalHeaderNode != null) {
            final Node verticalHeaderListNode = getNodeByXpath(verticalHeaderNode, XMLForms.EXPRESSION_LIST);
            if (verticalHeaderListNode != null) {
                formWidget.setVerticalHeaderExpressionList(xmlExpressionsUtil.parseExpressionsList(formWidget.getId(), verticalHeaderListNode));
            } else {
                formWidget.setVerticalHeaderExpression(xmlExpressionsUtil.parseExpression(formWidget.getId(), verticalHeaderNode));
            }
        }
        final Node horizontalHeaderNode = getNodeByXpath(widgetNode, XMLForms.HORIZONTAL_HEADER);
        if (horizontalHeaderNode != null) {
            final Node horizontalHeaderListNode = getNodeByXpath(horizontalHeaderNode, XMLForms.EXPRESSION_LIST);
            if (horizontalHeaderListNode != null) {
                formWidget.setHorizontalHeaderExpressionList(xmlExpressionsUtil.parseExpressionsList(formWidget.getId(), horizontalHeaderListNode));
            } else {
                formWidget.setHorizontalHeaderExpression(xmlExpressionsUtil.parseExpression(formWidget.getId(), horizontalHeaderNode));
            }
        }
        formWidget.setSelectMode(getSelectMode(getNodeByXpath(widgetNode, XMLForms.SELECT_MODE)));
        formWidget.setSelectedItemsStyle(getStringByXpath(widgetNode, XMLForms.SELECTED_ITEMS_STYLE));
        formWidget.setVariableRowNumber(getBooleanValue(getNodeByXpath(widgetNode, XMLForms.VARIABLE_ROWS)));
        formWidget.setVariableColumnNumber(getBooleanValue(getNodeByXpath(widgetNode, XMLForms.VARIABLE_COLUMNS)));
        final Node maxRowsNode = getNodeByXpath(widgetNode, XMLForms.MAX_ROWS);
        if (maxRowsNode != null) {
            formWidget.setMaxRowsExpression(xmlExpressionsUtil.parseExpression(formWidget.getId(), maxRowsNode));
        }
        final Node minRowsNode = getNodeByXpath(widgetNode, XMLForms.MIN_ROWS);
        if (minRowsNode != null) {
            formWidget.setMinRowsExpression(xmlExpressionsUtil.parseExpression(formWidget.getId(), minRowsNode));
        }
        final Node maxColumnsNode = getNodeByXpath(widgetNode, XMLForms.MAX_COLUMNS);
        if (maxColumnsNode != null) {
            formWidget.setMaxColumnsExpression(xmlExpressionsUtil.parseExpression(formWidget.getId(), maxColumnsNode));
        }
        final Node minColumnsNode = getNodeByXpath(widgetNode, XMLForms.MIN_COLUMNS);
        if (minColumnsNode != null) {
            formWidget.setMinColumnsExpression(xmlExpressionsUtil.parseExpression(formWidget.getId(), minColumnsNode));
        }
        final Node valueColumnIndexNode = getNodeByXpath(widgetNode, XMLForms.VALUE_COLUMN_INDEX);
        if (valueColumnIndexNode != null) {
            formWidget.setValueColumnIndexExpression(xmlExpressionsUtil.parseExpression(formWidget.getId(), valueColumnIndexNode));
        }
        final Node headingsStyleNode = getNodeByXpath(widgetNode, XMLForms.HEADINGS_STYLE);
        if (headingsStyleNode != null) {
            formWidget.setHeadingsStyle(headingsStyleNode.getTextContent());
        }
        final Node headingsPositionNode = getNodeByXpath(widgetNode, XMLForms.HEADINGS_POSITIONS);
        if (headingsPositionNode != null) {
            formWidget.setLeftHeadings(getBooleanValue(getNodeByXpath(headingsPositionNode, XMLForms.LEFT_HEADINGS)));
            formWidget.setTopHeadings(getBooleanValue(getNodeByXpath(headingsPositionNode, XMLForms.TOP_HEADINGS)));
            formWidget.setRightHeadings(getBooleanValue(getNodeByXpath(headingsPositionNode, XMLForms.RIGHT_HEADINGS)));
            formWidget.setBottomHeadings(getBooleanValue(getNodeByXpath(headingsPositionNode, XMLForms.BOTTOM_HEADINGS)));
        }
    }

    /**
     * Retrieve the available values from an available values list node
     * 
     * @param availableValuesListNode
     * @return a List of {@link FormFieldAvailableValue}
     * @throws InvalidFormDefinitionException
     */
    protected List<FormFieldAvailableValue> getAvailableValues(final String id, final Node availableValuesListNode) throws InvalidFormDefinitionException {
        final List<FormFieldAvailableValue> availableValues = new ArrayList<FormFieldAvailableValue>();
        final NodeList availableValuesNodes = getNodeListByXpath(availableValuesListNode, XMLForms.AVAILABLE_VALUE);
        for (int i = 0; i < availableValuesNodes.getLength(); i++) {
            final Node availableValueNode = availableValuesNodes.item(i);
            final FormFieldAvailableValue formFieldAvailableValue = new FormFieldAvailableValue();
            formFieldAvailableValue.setLabelExpression(xmlExpressionsUtil.parseExpression(id, getNodeByXpath(availableValueNode, XMLForms.LABEL)));
            formFieldAvailableValue.setValueExpression(xmlExpressionsUtil.parseExpression(id, getNodeByXpath(availableValueNode, XMLForms.VALUE)));
            availableValues.add(formFieldAvailableValue);
        }
        return availableValues;
    }

    /**
     * Read a node and return the list of {@link FormAction} it contains
     * 
     * @param parentNode
     *            the parent node of the actions
     * @param pageId
     *            page for which the actions are required
     * @return a {@link List} of {@link FormAction} objects
     * @throws InvalidFormDefinitionException
     */
    @Override
    public List<FormAction> getActions(final Node parentNode, final String pageId) throws InvalidFormDefinitionException {

        final List<FormAction> actions = new ArrayList<FormAction>();

        final String xpath = getActionsXpath(escapeSingleQuote(pageId));
        final NodeList actionNodes = getNodeListByXpath(parentNode, xpath);
        if (actionNodes == null) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, "Failed to parse the form definition file. query : " + xpath);
            }
            throw new InvalidFormDefinitionException("The actions were not found in the forms definition file");
        } else {
            for (int i = 0; i < actionNodes.getLength(); i++) {
                final Node actionNode = actionNodes.item(i);
                actions.add(parseAction(actionNode));
            }
        }
        return actions;
    }

    /**
     * Get the XPath query for the actions of a page
     * 
     * @param pageId
     *            the page ID
     * @return the XPath query
     */
    protected String getActionsXpath(final String pageId) {
        final StringBuilder actionsXpathBuilder = new StringBuilder();
        actionsXpathBuilder.append(XMLForms.PAGES);
        actionsXpathBuilder.append("/");
        actionsXpathBuilder.append(XMLForms.PAGE);
        actionsXpathBuilder.append("[@");
        actionsXpathBuilder.append(XMLForms.ID);
        actionsXpathBuilder.append("='");
        actionsXpathBuilder.append(pageId);
        actionsXpathBuilder.append("']/");
        actionsXpathBuilder.append(XMLForms.ACTIONS);
        actionsXpathBuilder.append("/");
        actionsXpathBuilder.append(XMLForms.ACTION);
        return actionsXpathBuilder.toString();
    }

    /**
     * Read an action node and return an action
     * 
     * @param actionNode
     *            an action node
     * @return a {@link FormAction}
     * @throws InvalidFormDefinitionException
     */
    protected FormAction parseAction(final Node actionNode) throws InvalidFormDefinitionException {
        final ActionType actionType = getActionTypeValue(getStringByXpath(actionNode, "@" + XMLForms.TYPE));
        final String variableId = getStringByXpath(actionNode, XMLForms.VARIABLE);
        final boolean isExternal = getBooleanValue(getNodeByXpath(actionNode, XMLForms.IS_EXTERNAL));
        final String operator = getStringByXpath(actionNode, XMLForms.OPERATOR);
        final String inputType = getStringByXpath(actionNode, XMLForms.INPUT_TYPE);
        final Expression expression = xmlExpressionsUtil.parseExpression(actionNode);
        final String submitButtonId = getStringByXpath(actionNode, XMLForms.SUBMIT_BUTTON);
        return new FormAction(actionType, variableId, isExternal, operator, inputType, expression, submitButtonId);
    }

    protected ItemPosition getItemPositionValue(final Node node) throws InvalidFormDefinitionException {
        if (node != null) {
            try {
                return ItemPosition.valueOf(node.getTextContent());
            } catch (final IllegalArgumentException e) {
                final String message = "the property " + node.getNodeName() + " should be one of " + Arrays.toString(ItemPosition.values()) + ".";
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE, message, e);
                }
                throw new InvalidFormDefinitionException(message, e);
            }
        }
        return ItemPosition.LEFT;
    }

    protected ValidatorPosition getValidatorPositionValue(final Node node) throws InvalidFormDefinitionException {
        if (node != null) {
            try {
                return ValidatorPosition.valueOf(node.getTextContent());
            } catch (final IllegalArgumentException e) {
                final String message = "the property " + node.getNodeName() + " should be one of " + Arrays.toString(ValidatorPosition.values()) + ".";
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE, message, e);
                }
                throw new InvalidFormDefinitionException(message, e);
            }
        }
        return ValidatorPosition.BOTTOM;
    }

    protected SubTitlePosition getSubTitlePositionValue(final Node node) throws InvalidFormDefinitionException {
        if (node != null) {
            try {
                return SubTitlePosition.valueOf(node.getTextContent());
            } catch (final IllegalArgumentException e) {
                final String message = "the property " + node.getNodeName() + " should be one of " + Arrays.toString(SubTitlePosition.values()) + ".";
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE, message, e);
                }
                throw new InvalidFormDefinitionException(message, e);
            }
        }
        return SubTitlePosition.BOTTOM;
    }

    protected SelectMode getSelectMode(final Node node) throws InvalidFormDefinitionException {
        if (node != null) {
            try {
                return SelectMode.valueOf(node.getTextContent());
            } catch (final IllegalArgumentException e) {
                final String message = "the property " + node.getNodeName() + " should be one of " + Arrays.toString(SelectMode.values()) + ".";
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE, message, e);
                }
                throw new InvalidFormDefinitionException(message, e);
            }
        }
        return SelectMode.NONE;
    }

    protected FileWidgetInputType getFileWidgetInputType(final Node node) throws InvalidFormDefinitionException {
        if (node != null) {
            try {
                return FileWidgetInputType.valueOf(node.getTextContent());
            } catch (final IllegalArgumentException e) {
                final String message = "the property " + node.getNodeName() + " should be one of " + Arrays.toString(FileWidgetInputType.values()) + ".";
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE, message, e);
                }
                throw new InvalidFormDefinitionException(message, e);
            }
        }
        return FileWidgetInputType.ALL;
    }

    /**
     * @param value
     *            the string value of the widget type
     * @return the {@link WidgetType} value of the node if it exists. null
     *         otherwise
     * @throws InvalidFormDefinitionException
     *             when the type is not int the definition of the widget or is invalid
     */
    protected WidgetType getWidgetTypeValue(final String value) throws InvalidFormDefinitionException {
        if (value != null) {
            try {
                return WidgetType.valueOf(value);
            } catch (final IllegalArgumentException e) {
                final String message = "the widget type should be one of " + Arrays.toString(WidgetType.values()) + ".";
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE, message, e);
                }
                throw new InvalidFormDefinitionException(message, e);
            }
        } else {
            final String message = "the widget attribute \"type\" is mandatory.";
            LOGGER.log(Level.SEVERE, message);
            throw new InvalidFormDefinitionException(message);
        }
    }

    /**
     * @param value
     *            the string value of the action type
     * @return the {@link ActionType} value of the node if it exists. null
     *         otherwise
     * @throws InvalidFormDefinitionException
     *             when the type is not int the definition of the widget or is invalid
     */
    protected ActionType getActionTypeValue(final String value) throws InvalidFormDefinitionException {
        if (value != null) {
            try {
                return ActionType.valueOf(value);
            } catch (final IllegalArgumentException e) {
                final String message = "the action type should be one of " + Arrays.toString(ActionType.values()) + ".";
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE, message, e);
                }
                throw new InvalidFormDefinitionException(message, e);
            }
        } else {
            final String message = "the action attribute \"type\" is mandatory.";
            LOGGER.log(Level.SEVERE, message);
            throw new InvalidFormDefinitionException(message);
        }
    }

}
