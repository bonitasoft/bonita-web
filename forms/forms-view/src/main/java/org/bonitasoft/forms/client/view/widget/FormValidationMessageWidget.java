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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.forms.client.view.widget;

import org.bonitasoft.forms.client.i18n.FormsResourceBundle;
import org.bonitasoft.forms.client.model.ReducedFormValidator;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;

/**
 * Widget displaying form validation error messages
 * 
 * @author Anthony Birembaut
 */
public class FormValidationMessageWidget extends Composite {

    /**
     * the flow panel used to display the widget
     */
    protected FlowPanel flowPanel;

    /**
     * indicates whether the message to display is related to a page validation or not
     */
    protected boolean isPageValidation;

    /**
     * Constructor
     * 
     * @param formWidget the widget data object
     * @param isPageValidation indicates whether the message to display is related to a page validation or not
     */
    public FormValidationMessageWidget(final ReducedFormValidator formValidatorData, final boolean isPageValidation) {

        this.isPageValidation = isPageValidation;

        flowPanel = new FlowPanel();

        createWidget(formValidatorData);

        initWidget(flowPanel);
    }

    /**
     * Create the validation message widget
     * 
     * @param formValidatorData the widget data object
     */
    protected void createWidget(final ReducedFormValidator formValidatorData) {

        final HTML validationMessage = new HTML();
        validationMessage.setHTML(getValidatorLabel(formValidatorData));
        if (isPageValidation) {
            validationMessage.setStyleName("bonita_form_page_validation_message");
        } else {
            validationMessage.setStyleName("bonita_form_field_validation_message");
        }
        if (formValidatorData.getStyle() != null && formValidatorData.getStyle().length() > 0) {
            validationMessage.addStyleName(formValidatorData.getStyle());
        }
        flowPanel.add(validationMessage);
    }

    /**
     * Retrieve the validator's label
     * 
     * @param formValidatorData
     * @return
     */
    protected String getValidatorLabel(final ReducedFormValidator formValidatorData) {

        if (formValidatorData.getLabel().equals("#dateFieldValidatorLabel")) {
            return FormsResourceBundle.getMessages().dateFieldValidatorDefaultLabel();
        } else if (formValidatorData.getLabel().equals("#numericLongFieldValidatorLabel")) {
            return FormsResourceBundle.getMessages().longFieldValidatorDefaultLabel();
        } else if (formValidatorData.getLabel().equals("#numericDoubleFieldValidatorLabel")) {
            return FormsResourceBundle.getMessages().doubleFieldValidatorDefaultLabel();
        } else if (formValidatorData.getLabel().equals("#numericIntegerFieldValidatorLabel")) {
            return FormsResourceBundle.getMessages().integerFieldValidatorDefaultLabel();
        } else if (formValidatorData.getLabel().equals("#numericFloatFieldValidatorLabel")) {
            return FormsResourceBundle.getMessages().floatFieldValidatorDefaultLabel();
        } else if (formValidatorData.getLabel().equals("#numericShortFieldValidatorLabel")) {
            return FormsResourceBundle.getMessages().shortFieldValidatorDefaultLabel();
        } else if (formValidatorData.getLabel().equals("#charFieldValidatorLabel")) {
            return FormsResourceBundle.getMessages().charFieldValidatorDefaultLabel();
        } else {
            return formValidatorData.getLabel();
        }
    }
}
