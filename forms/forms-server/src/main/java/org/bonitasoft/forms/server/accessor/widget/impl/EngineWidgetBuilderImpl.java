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

import org.bonitasoft.engine.bpm.data.DataDefinition;
import org.bonitasoft.engine.bpm.document.DocumentDefinition;
import org.bonitasoft.engine.expression.ExpressionType;
import org.bonitasoft.forms.client.model.*;
import org.bonitasoft.forms.client.model.ReducedFormWidget.ItemPosition;
import org.bonitasoft.forms.server.accessor.widget.IEngineWidgetBuilder;
import org.bonitasoft.forms.server.api.IFormExpressionsAPI;
import org.bonitasoft.forms.server.exception.InvalidFormDefinitionException;
import org.bonitasoft.forms.server.exception.NotHandledTypeException;
import org.bonitasoft.forms.server.validator.*;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Anthony Birembaut
 * 
 */
public class EngineWidgetBuilderImpl implements IEngineWidgetBuilder {

    /**
     * validator id suffix
     */
    protected static final String VALIDATOR_ID_SUFFIX = "_validator";

    /**
     * Logger
     */
    private static Logger LOGGER = Logger.getLogger(EngineWidgetBuilderImpl.class.getName());

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<FormWidget, FormAction> createWidgets(final Set<DataDefinition> dataFieldDefinitions, final String widgetIdPrefix, final boolean isEditMode,
            final boolean includeInitialValue) {

        final Map<FormWidget, FormAction> formWidgets = new HashMap<FormWidget, FormAction>();
        for (final DataDefinition dataDefinition : dataFieldDefinitions) {
            try {
                final String dataFieldLabel = dataDefinition.getName();
                final String dataFieldName = dataDefinition.getName();
                final String dataFieldDescription = dataDefinition.getDescription();
                final String dataFieldClassName = dataDefinition.getClassName();

                final String id = widgetIdPrefix + "_" + dataFieldName;
                String label = null;
                if (dataFieldLabel != null && dataFieldLabel.length() != 0) {
                    label = dataFieldLabel;
                } else {
                    label = dataFieldName;
                }
                String title = null;
                if (dataFieldDescription != null && dataFieldDescription.length() != 0) {
                    title = dataFieldDescription;
                } else {
                    title = label;
                }
                final List<FormValidator> fieldValidators = new ArrayList<FormValidator>();
                WidgetType type = null;
                String validatorClassname = null;
                final String validatorId = id + VALIDATOR_ID_SUFFIX;
                String validatorLabel = null;
                String modifier = null;
                if (Date.class.getName().equals(dataFieldClassName)) {
                    type = WidgetType.DATE;
                    validatorClassname = DateFieldValidator.class.getName();
                    validatorLabel = "#dateFieldValidatorLabel";
                } else if (Boolean.class.getName().equals(dataFieldClassName)) {
                    type = WidgetType.CHECKBOX;
                } else if (Long.class.getName().equals(dataFieldClassName)) {
                    type = WidgetType.TEXTBOX;
                    modifier = dataFieldClassName;
                    validatorClassname = NumericLongFieldValidator.class.getName();
                    validatorLabel = "#numericLongFieldValidatorLabel";
                } else if (Double.class.getName().equals(dataFieldClassName)) {
                    type = WidgetType.TEXTBOX;
                    modifier = dataFieldClassName;
                    validatorClassname = NumericDoubleFieldValidator.class.getName();
                    validatorLabel = "#numericDoubleFieldValidatorLabel";
                } else if (Integer.class.getName().equals(dataFieldClassName)) {
                    type = WidgetType.TEXTBOX;
                    modifier = dataFieldClassName;
                    validatorClassname = NumericIntegerFieldValidator.class.getName();
                    validatorLabel = "#numericIntegerFieldValidatorLabel";
                } else if (Float.class.getName().equals(dataFieldClassName)) {
                    type = WidgetType.TEXTBOX;
                    modifier = dataFieldClassName;
                    validatorClassname = NumericFloatFieldValidator.class.getName();
                    validatorLabel = "#numericFloatFieldValidatorLabel";
                } else if (Short.class.getName().equals(dataFieldClassName)) {
                    type = WidgetType.TEXTBOX;
                    modifier = dataFieldClassName;
                    validatorClassname = NumericShortFieldValidator.class.getName();
                    validatorLabel = "#numericShortFieldValidatorLabel";
                } else if (Character.class.getName().equals(dataFieldClassName)) {
                    type = WidgetType.TEXTBOX;
                    modifier = dataFieldClassName;
                    validatorClassname = CharFieldValidator.class.getName();
                    validatorLabel = "#charFieldValidatorLabel";
                } else if (String.class.getName().equals(dataFieldClassName)) {
                    type = WidgetType.TEXTBOX;
                } else {
                    final String message = "Type " + dataFieldClassName + " is not handled by the automatic form generation.";
                    throw new NotHandledTypeException(message);
                }

                final FormWidget formWidget = new FormWidget(id, 0, 0, type, null, null, null, ItemPosition.LEFT, false);
                formWidget.setLabel(label + " :");
                formWidget.setTitle(title);
                if (includeInitialValue) {
                    final Expression expression = new Expression("formWidgetValue", dataFieldName, ExpressionType.TYPE_VARIABLE.name(), dataFieldClassName,
                            null, null);
                    formWidget.setInitialValueExpression(expression);
                }
                formWidget.setViewPageWidget(!isEditMode);
                if (!isEditMode) {
                    formWidget.setReadOnly(true);
                } else {
                    formWidget.setReadOnly(false);
                }

                if (validatorClassname != null) {
                    final FormValidator formValidator = new FormValidator(validatorId, validatorClassname, null);
                    // formValidator.setLabel(validatorLabel); // will be interpreted during expression evaluation
                    formValidator.setLabelExpression(new Expression("validator", validatorLabel, ExpressionType.TYPE_CONSTANT.name(), String.class.getName(),
                            null, null));
                    fieldValidators.add(formValidator);
                }
                formWidget.setValidators(fieldValidators);

                FormAction formAction = null;
                if (isEditMode) {
                    formAction = getWidgetAction(id, dataFieldName, dataFieldClassName, isEditMode);
                }

                formWidget.setFieldOutputType(modifier);

                formWidget.setLabelPosition(ItemPosition.TOP);

                formWidgets.put(formWidget, formAction);
            } catch (final NotHandledTypeException e) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, e.getMessage());
                }
            }
        }
        return formWidgets;
    }

    /**
     * get the form action associated with the widget
     * 
     * @param widgetId
     * @param dataFieldName
     * @param dataFieldClassName
     * @param isEditMode
     * @return
     */
    protected FormAction getWidgetAction(final String widgetId, final String dataFieldName, final String dataFieldClassName, final boolean isEditMode) {
        final Expression actionExpression = new Expression("widgetAction", IFormExpressionsAPI.FIELDID_PREFIX + widgetId,
                ExpressionType.TYPE_INPUT.name(), dataFieldClassName, null, null);
        return new FormAction(ActionType.ASSIGNMENT, dataFieldName, false, "=", null, actionExpression, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<FormWidget, FormAction> createAttachmentWidgets(final Set<DocumentDefinition> attachments, final String widgetIdPrefix,
            final boolean isEditMode) {

        final Map<FormWidget, FormAction> formWidgets = new HashMap<FormWidget, FormAction>();
        for (final DocumentDefinition attachment : attachments) {
            // final String attachmentLabel = attachment.getLabel();
            final String attachmentName = attachment.getName();
            // final String attachmentDescription = attachment.getDescription();
            //
            final String id = widgetIdPrefix + "_" + attachmentName;
            String label = null;
            // if (attachmentLabel != null && attachmentLabel.length() != 0) {
            // label = attachmentLabel;
            // } else {
            label = attachmentName;
            // }
            String title = null;
            // if (attachmentDescription != null && attachmentDescription.length() != 0)
            // title = attachmentDescription;
            // else {
            title = label;
            // }
            WidgetType widgetType = null;
            if (!isEditMode) {
                widgetType = WidgetType.FILEDOWNLOAD;
            } else {
                widgetType = WidgetType.FILEUPLOAD;
            }
            final FormWidget formWidget = new FormWidget(id, 0, 0, widgetType, null, null, null, ItemPosition.LEFT, false);
            formWidget.setLabel(label + " :");
            formWidget.setTitle(title);
            // formWidget.setInitialValueExpression(attachmentName);

            formWidgets.put(formWidget, null);
        }
        return formWidgets;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FormWidget> getPageWidgets(final String pageId, final int nbOfPages, final List<FormWidget> widgets, final String widgetIdPrefix,
            final boolean isEditMode) throws InvalidFormDefinitionException {
        final List<FormWidget> pageWidgets = new ArrayList<FormWidget>();
        final int pageIndex = Integer.parseInt(pageId);
        final int lastPageIndex = nbOfPages - 1;
        if (pageIndex >= 0 && pageIndex <= nbOfPages) {
            pageWidgets.addAll(widgets);
            String id = null;
            String label = null;
            String title = null;
            if (pageIndex > 0) {
                id = widgetIdPrefix + "_" + pageId + "_previous";
                label = "#previousPageButtonLabel";
                title = "#previousPageButtonTitle";
                final FormWidget previousButton = new FormWidget(id, 0, 0, WidgetType.BUTTON_PREVIOUS, null, null, null, null, false);
                previousButton.setLabel(label);
                previousButton.setTitle(title);
                pageWidgets.add(previousButton);
            }
            if (pageIndex < lastPageIndex) {
                id = widgetIdPrefix + "_" + pageId + "_next";
                label = "#nextPageButtonLabel";
                title = "#nextPageButtonTitle";
                final FormWidget nextButton = new FormWidget(id, 0, 0, WidgetType.BUTTON_NEXT, null, null, null, null, false);
                nextButton.setLabel(label);
                nextButton.setTitle(title);
                pageWidgets.add(nextButton);
            }
            if (pageIndex == lastPageIndex) {
                id = widgetIdPrefix + "_submit";
                label = "#submitButtonLabel";
                title = "#submitButtonTitle";
                final FormWidget submitButton = new FormWidget(id, 0, 0, WidgetType.BUTTON_SUBMIT, null, null, null, null, false);
                submitButton.setLabel(label);
                submitButton.setTitle(title);
                pageWidgets.add(submitButton);
            }
        } else {
            final String errorMessage = "Page " + pageId + " not found.";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "Page " + pageId + " not found.");
            }
            throw new InvalidFormDefinitionException(errorMessage);
        }
        return pageWidgets;
    }

}
