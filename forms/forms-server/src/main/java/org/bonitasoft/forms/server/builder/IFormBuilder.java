/**
 * Copyright (C) 2012 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation
 * version 2.1 of the License.
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth
 * Floor, Boston, MA 02110-1301, USA.
 **/
package org.bonitasoft.forms.server.builder;

import java.io.File;
import java.io.IOException;

import org.bonitasoft.forms.client.model.ActionType;
import org.bonitasoft.forms.client.model.FileWidgetInputType;
import org.bonitasoft.forms.client.model.ReducedFormSubtitle.SubTitlePosition;
import org.bonitasoft.forms.client.model.ReducedFormValidator.ValidatorPosition;
import org.bonitasoft.forms.client.model.ReducedFormWidget.ItemPosition;
import org.bonitasoft.forms.client.model.ReducedFormWidget.SelectMode;
import org.bonitasoft.forms.client.model.WidgetType;
import org.bonitasoft.forms.server.exception.InvalidFormDefinitionException;

/**
 * Form definition builder
 *
 * @author Aurelien Pupier, Anthony Birembaut, Chong Zhao, Zhiheng Yang
 */
public interface IFormBuilder {

    /**
     * Build a XML form definition file.
     * This is the last method to call once the form has been built.
     * It perform the XSD validation and generates the XML file
     *
     * @return a {@link File}
     * @throws InvalidFormDefinitionException
     *             if the generated document is not valid
     * @throws IOException
     */
    File done() throws IOException, InvalidFormDefinitionException;

    /**
     * Initiate the form definition
     *
     * @return an implementation of {@link IFormBuilder}
     */
    IFormBuilder createFormDefinition();

    /**
     * Add an application
     *
     * @param applicationName
     *            the name of the application
     * @param applicationVersion
     *            the version of the application
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addApplication(String applicationName, String applicationVersion) throws InvalidFormDefinitionException;

    /**
     * Add a label on an application, page or widget
     *
     * @param name
     * @param content
     * @param expressionType
     * @param returnType
     * @param interpreter
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addLabelExpression(String name, String content, String expressionType, String returnType, String interpreter)
            throws InvalidFormDefinitionException;

    /**
     * Add a layout on an application or a page
     *
     * @param layoutUri
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addLayout(String layoutUri) throws InvalidFormDefinitionException;

    /**
     * Add a mandatory field symbol on an application
     *
     * @param name
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addMandatorySymbolExpression(String name, String content, String expressionType, String returnType, String interpreter)
            throws InvalidFormDefinitionException;

    /**
     * Add a mandatory field label expession on an application
     *
     * @param name
     * @param content
     * @param expressionType
     * @param returnType
     * @param interpreter
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addMandatoryLabelExpression(String name, String content, String expressionType, String returnType, String interpreter)
            throws InvalidFormDefinitionException;

    /**
     * Add a mandatory field label and symbol style (css class names) on an application
     *
     * @param mandatoryStyle
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addMandatoryStyle(String mandatoryStyle) throws InvalidFormDefinitionException;

    /**
     * Add a confirmation layout on an application
     *
     * @param layoutUri
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addConfirmationLayout(String layoutUri) throws InvalidFormDefinitionException;

    /**
     * Add a confirmation message on an application
     *
     * @param name
     * @param content
     * @param expressionType
     * @param returnType
     * @param interpreter
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addConfirmationMessageExpression(String name, String content, String expressionType, String returnType, String interpreter)
            throws InvalidFormDefinitionException;

    /**
     * Add an error template on an application
     *
     * @param templateUri
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addErrorTemplate(String templateUri) throws InvalidFormDefinitionException;

    /**
     * Add an entry form on an application
     * If an application has no entry form, it means that it hasn't been defined,
     * and the form for the application will be automatically generated.
     * Whereas if it has an empty entry form, the application will be automatically instantiated.
     *
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addEntryForm(String formId) throws InvalidFormDefinitionException;

    /**
     * Add a view form on an application
     * If an application has no form, it means that it hasn't been defined,
     * and the form for the application will be automatically generated.
     *
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addViewForm(String formId) throws InvalidFormDefinitionException;

    /**
     * Add a page in the edition page flows and create the form if it doesn't exist yet
     *
     * @param pageId
     * @return
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addPage(String pageId) throws InvalidFormDefinitionException;

    /**
     * Add a widget on pages
     *
     * @param widgetId
     * @param widgetType
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addWidget(String widgetId, WidgetType widgetType) throws InvalidFormDefinitionException;

    /**
     * Add a max length number of characters property to a widget for textbox and textarea widgets
     *
     * @param maxLength
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addMaxLength(int maxLength) throws InvalidFormDefinitionException;

    /**
     * Add a max height number of characters property to a widget for textarea and multiple listbox widgets
     *
     * @param maxHeight
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addMaxHeight(int maxHeight) throws InvalidFormDefinitionException;

    /**
     * Add a title (tooltip) to a widget field
     *
     * @param name
     * @param content
     * @param expressionType
     * @param returnType
     * @param interpreter
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addTitleExpression(String name, String content, String expressionType, String returnType, String interpreter)
            throws InvalidFormDefinitionException;

    /**
     * Add CSS classes names to a widget
     *
     * @param cssClasses
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addStyle(String cssClasses) throws InvalidFormDefinitionException;

    /**
     * Add an initial value to a widget (can be a groovy expression)
     *
     * @param name
     * @param content
     * @param expressionType
     * @param returnType
     * @param interpreter
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addInitialValueExpression(String name, String content, String expressionType, String returnType, String interpreter)
            throws InvalidFormDefinitionException;

    /**
     * Add CSS class names to a widget label
     *
     * @param cssClasses
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addLabelStyle(String cssClasses) throws InvalidFormDefinitionException;

    /**
     * Specify the position of a widget label
     *
     * @param labelPosition
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addLabelPosition(ItemPosition labelPosition) throws InvalidFormDefinitionException;

    /**
     * Add a mandatory property to a widget
     *
     * @param isMandatory
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addMandatoryBehavior(boolean isMandatory) throws InvalidFormDefinitionException;

    /**
     * Add an available value to a widget and create the list of available values if it doesn't exist yet
     * (for radiobutton group, simple and multiple selectbox, checkbox group).
     *
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addAvailableValue() throws InvalidFormDefinitionException;

    /**
     * Add available values array to a widget and create the array of available values if it doesn't exist yet
     * (for table widget).
     *
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addAvailableValuesArray() throws InvalidFormDefinitionException;

    /**
     * Add initial values array to a widget and create the array if it doesn't exist yet
     * (for grid widget).
     *
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addInitialValuesArray() throws InvalidFormDefinitionException;

    /**
     * Add a row to an available values array or to an initial value array
     * (for table widget and grid widget).
     *
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addRow() throws InvalidFormDefinitionException;

    /**
     * Add an available values expression property to a widget
     *
     * @param name
     * @param content
     * @param expressionType
     * @param returnType
     * @param interpreter
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addAvailableValuesExpression(String name, String content, String expressionType, String returnType, String interpreter)
            throws InvalidFormDefinitionException;

    /**
     * Add CSS class names to the items of a radiobutton or checkbox group widget
     *
     * @param cssClasses
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addItemsStyle(String cssClasses) throws InvalidFormDefinitionException;

    /**
     * Indicates that the button should be displayed as a label instead of an html button
     *
     * @param isLabelButton
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addLabelButtonBehavior(boolean isLabelButton) throws InvalidFormDefinitionException;

    /**
     * Add a validator on a page or a widget and create the list of validators if it doesn't exist yet
     *
     * @param validatorId
     *            the validator Id
     * @param className
     *            the classname of the validator
     * @param cssClasses
     *            the css classes for the error label
     * @param position
     *            the position of the error label
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addValidator(String validatorId, String className, String cssClasses, ValidatorPosition position) throws InvalidFormDefinitionException;

    /**
     * Add an action on an application and create the list of actions if it doesn't exist yet
     *
     * @param actionType
     *            the action type
     * @param variableName
     *            the name of the variable (if it's a set variable action)
     * @param variableName
     *            the type of the variable, it can be document, data, and other types handled by the engine
     * @param operator
     *            the operator for the action
     * @param operatorInputType
     *            the operator in put type (for java methods operations)
     * @param submitButtonId
     *            the submit button associated with the action
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addAction(ActionType actionType, String variableName, String variableType, String operator, String operatorInputType,
            String submitButtonId)
            throws InvalidFormDefinitionException;

    /**
     * Add a display format pattern for the display value of date widgets
     *
     * @param displayFormat
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addDisplayFormat(String displayFormat) throws InvalidFormDefinitionException;

    /**
     * Add an attachement image behavior for the display of image previews on file download widgets or the display of attachments in image widgets
     *
     * @param attachmentImage
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addAttachmentImageBehavior(boolean attachmentImage) throws InvalidFormDefinitionException;

    /**
     * Add allow HTML in label behavior
     *
     * @param allowHTMLInLabel
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addAllowHTMLInLabelBehavior(boolean allowHTMLInLabel) throws InvalidFormDefinitionException;

    /**
     * Add allow HTML in field behavior
     *
     * @param allowHTMLInField
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addAllowHTMLInFieldBehavior(boolean allowHTMLInField) throws InvalidFormDefinitionException;

    /**
     * Add a Html attribute to a widget
     *
     * @param name
     *            the name of the attribute
     * @param value
     *            the valueof the attribute
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addHTMLAttribute(String name, String value) throws InvalidFormDefinitionException;

    /**
     * Add table style for table widgets
     *
     * @param cssClasses
     *            the CSS classes for the table
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addTableStyle(String cssClasses) throws InvalidFormDefinitionException;

    /**
     * Add image style for image widgets
     *
     * @param cssClasses
     *            the CSS classes for the image
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addImageStyle(String cssClasses) throws InvalidFormDefinitionException;

    /**
     * Add cells style for table widgets
     *
     * @param cssClasses
     *            the CSS classes for the cells
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addCellsStyle(String cssClasses) throws InvalidFormDefinitionException;

    /**
     * Add headings style for table widgets
     *
     * @param cssClasses
     *            the CSS classes for the headings
     * @param leftHeadings
     *            if true, indicates that the left column of the grid should be considered as a header
     * @param topHeadings
     *            if true, indicates that the top row of the grid should be considered as a header
     * @param rightHeadings
     *            if true, indicates that the right column of the grid should be considered as a header
     * @param bottomHeadings
     *            if true, indicates that the bottom row of the grid should be considered as a header
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addHeadingsStyle(String cssClasses, boolean leftHeadings, boolean topHeadings, boolean rightHeadings, boolean bottomHeadings)
            throws InvalidFormDefinitionException;

    /**
     * Add a vertical header list of expressions
     *
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addVerticalHeaderExpressionList() throws InvalidFormDefinitionException;

    /**
     * Add a vertical header under the form of an expression or add a vertical header cell expression if called after addVerticalHeaderExpressionList
     *
     * @param name
     * @param content
     * @param expressionType
     * @param returnType
     * @param interpreter
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addVerticalHeaderExpression(String name, String content, String expressionType, String returnType, String interpreter)
            throws InvalidFormDefinitionException;

    /**
     * Add a horizontal header list of expressions
     *
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addHorizontalHeaderExpressionList() throws InvalidFormDefinitionException;

    /**
     * Add an horizontal header under the form of an expression
     *
     * @param name
     * @param content
     * @param expressionType
     * @param returnType
     * @param interpreter
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addHorizontalHeaderExpression(String name, String content, String expressionType, String returnType, String interpreter)
            throws InvalidFormDefinitionException;

    /**
     * Add a selection mode to a widget (for table widgets for example)
     *
     * @param selectMode
     *            the selection mode. {@link SelectMode#NONE} if the selection should be disabled
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addSelectMode(SelectMode selectMode) throws InvalidFormDefinitionException;

    /**
     * Add a selected items style to a widget (for table widgets for example)
     *
     * @param selectedItemsStyle
     *            the selection mode. {@link SelectMode#NONE} if the selection should be disabled
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addSelectedItemsStyle(String selectedItemsStyle) throws InvalidFormDefinitionException;

    /**
     * add a minimum number of rows to a widget (for editable tables)
     *
     * @param name
     * @param content
     * @param expressionType
     * @param returnType
     * @param interpreter
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addMinRowsExpression(String name, String content, String expressionType, String returnType, String interpreter)
            throws InvalidFormDefinitionException;

    /**
     * add a maximum number of rows to a widget (for tables)
     *
     * @param name
     * @param content
     * @param expressionType
     * @param returnType
     * @param interpreter
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addMaxRowsExpression(String name, String content, String expressionType, String returnType, String interpreter)
            throws InvalidFormDefinitionException;

    /**
     * add a variable rows number behavior (for editable tables)
     *
     * @param variableRowsNumber
     *            the variable rows number
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addVariableRowsNumber(boolean variableRowsNumber) throws InvalidFormDefinitionException;

    /**
     * add a minimum number of columns to a widget (for editable tables)
     *
     * @param name
     * @param content
     * @param expressionType
     * @param returnType
     * @param interpreter
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addMinColumnsExpression(String name, String content, String expressionType, String returnType, String interpreter)
            throws InvalidFormDefinitionException;

    /**
     * add a maximum number of columns to a widget (for editable tables)
     *
     * @param name
     * @param content
     * @param expressionType
     * @param returnType
     * @param interpreter
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addMaxColumnsExpression(String name, String content, String expressionType, String returnType, String interpreter)
            throws InvalidFormDefinitionException;

    /**
     * add a variable columns number behavior (for editable tables)
     *
     * @param variableColumnsNumber
     *            the variable columns number
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addVariableColumnsNumber(boolean variableColumnsNumber) throws InvalidFormDefinitionException;

    /**
     * specify the index of column which is used as the value of the selected row(s)
     *
     * @param name
     * @param content
     * @param expressionType
     * @param returnType
     * @param interpreter
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addValueColumnIndexExpression(String name, String content, String expressionType, String returnType, String interpreter)
            throws InvalidFormDefinitionException;

    /**
     * Add a readonly property to a widget
     *
     * @param isReadOnly
     *            the readonly behavior
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addReadOnlyBehavior(boolean isReadOnly) throws InvalidFormDefinitionException;

    /**
     * Add a max items property to a widget for suggestbox widgets
     *
     * @param maxItems
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addMaxItems(int maxItems) throws InvalidFormDefinitionException;

    /**
     * Add a transient data on a page flow
     *
     * @param name
     *            name of the transient data
     * @param className
     *            classnameof the transient data
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addTransientData(String name, String className) throws InvalidFormDefinitionException;

    /**
     * Add a first page Id on a page flow
     *
     * @param name
     * @param content
     * @param expressionType
     * @param returnType
     * @param interpreter
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addFirstPageIdExpression(String name, String content, String expressionType, String returnType, String interpreter)
            throws InvalidFormDefinitionException;

    /**
     * Add a next page id on a page flow
     *
     * @param name
     * @param content
     * @param expressionType
     * @param returnType
     * @param interpreter
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addNextPageIdExpression(String name, String content, String expressionType, String returnType, String interpreter)
            throws InvalidFormDefinitionException;

    /**
     * Add a display condition expression to display or not a widget
     *
     * @param name
     * @param content
     * @param expressionType
     * @param interpreter
     * @param returnType
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addDisplayConditionExpression(String name, String content, String expressionType, String returnType, String interpreter)
            throws InvalidFormDefinitionException;

    /**
     * Add a delay millisecond property to a widget for asynchronous suggestbox widgets
     *
     * @param delayMillis
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addDelayMillis(int delayMillis) throws InvalidFormDefinitionException;

    /**
     * Add a sub title property to a widget to accept an "example" parameter
     *
     * @param position
     *            sub title position
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addSubTitle(SubTitlePosition position) throws InvalidFormDefinitionException;

    /**
     * Add a popup tooltip, that will be displayed to help the user when he clicks on the
     * bulb icon that is placed beside of a widget.
     *
     * @param name
     * @param content
     * @param expressionType
     * @param returnType
     * @param interpreter
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addPopupToolTipExpression(String name, String content, String expressionType, String returnType, String interpreter)
            throws InvalidFormDefinitionException;

    /**
     * Add permissions, that will be decide whether a user has right to view/submit the form.
     *
     * @param permissions
     *            The permissions
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addPermissions(String permissions) throws InvalidFormDefinitionException;

    /**
     * Add migration product version, that will be generated with the current version of the product used.
     *
     * @param migrationProductVersion
     *            The migrationProductVersion
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    @Deprecated
    IFormBuilder addMigrationProductVersion(String migrationProductVersion) throws InvalidFormDefinitionException;

    /**
     * Add a next form id on entry form or view form.
     *
     * @param name
     * @param content
     * @param expressionType
     * @param returnType
     * @param interpreter
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addNextFormIdExpression(String name, String content, String expressionType, String returnType, String interpreter)
            throws InvalidFormDefinitionException;

    /**
     * Add dependent expressions
     *
     * @param name
     * @param content
     * @param expressionType
     * @param returnType
     * @param interpreter
     * @return
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addDependentExpression(String name, String content, String expressionType, String returnType, String interpreter)
            throws InvalidFormDefinitionException;

    /**
     * Add dependent expressions
     *
     * @param name
     * @param content
     * @param expressionType
     * @param returnType
     * @param interpreter
     * @param isSameLevelDependency
     * @return
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addDependentExpression(String name, String content, String expressionType, String returnType, String interpreter, boolean isSameLevelDependency)
            throws InvalidFormDefinitionException;

    /**
     * End an expression dependencies group
     *
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder endExpressionDependencies() throws InvalidFormDefinitionException;

    /**
     * Add parameter expression
     *
     * @param name
     * @param content
     * @param expressionType
     * @param returnType
     * @param interpreter
     * @return
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addParameterExpression(String name, String content, String expressionType, String returnType, String interpreter)
            throws InvalidFormDefinitionException;

    /**
     * Add action expression
     *
     * @param name
     * @param content
     * @param expressionType
     * @param returnType
     * @param interpreter
     * @return
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addActionExpression(String name, String content, String expressionType, String returnType, String interpreter)
            throws InvalidFormDefinitionException;

    /**
     * Add action condition expression
     * 
     * @param name
     * @param content
     * @param expressionType
     * @param returnType
     * @param interpreter
     * @return
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addConditionExpression(String name, String content, String expressionType, String returnType, String interpreter)
            throws InvalidFormDefinitionException;

    /**
     * Add transient data expression
     *
     * @param name
     * @param content
     * @param expressionType
     * @param returnType
     * @param interpreter
     * @return
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addTransientDataExpression(String name, String content, String expressionType, String returnType, String interpreter)
            throws InvalidFormDefinitionException;

    /**
     * Add value expression
     *
     * @param name
     * @param content
     * @param expressionType
     * @param returnType
     * @param interpreter
     * @return
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addValueExpression(String name, String content, String expressionType, String returnType, String interpreter)
            throws InvalidFormDefinitionException;

    /**
     * Add initial value Resource for file widgets on instantiation form
     *
     * @param resourcePath
     * @return
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addInitialValueResource(String resourcePath) throws InvalidFormDefinitionException;

    /**
     * Add an input type to a file widget
     *
     * @param fileWidgetInputType
     * @return
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addFileWidgetInputType(FileWidgetInputType fileWidgetInputType) throws InvalidFormDefinitionException;

    /**
     * Add field output type to convert when form is submitted
     *
     * @param fieldOutputType
     * @return
     */
    IFormBuilder addFieldOutputType(String fieldOutputType) throws InvalidFormDefinitionException;

    /**
     * Add CSS classes names to a widget input
     *
     * @param cssClasses
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    IFormBuilder addInputStyle(String cssClasses) throws InvalidFormDefinitionException;
}
