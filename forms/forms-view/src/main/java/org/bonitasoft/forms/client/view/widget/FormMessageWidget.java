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
package org.bonitasoft.forms.client.view.widget;

import java.util.Date;

import org.bonitasoft.forms.client.model.FormFieldValue;
import org.bonitasoft.forms.client.model.ReducedFormWidget;
import org.bonitasoft.forms.client.view.SupportedFieldTypes;
import org.bonitasoft.forms.client.view.common.DOMUtils;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;

/**
 * Form flow message widget for text messages inside forms
 * 
 * @author Anthony Birembaut
 */
public class FormMessageWidget extends Composite {

    /**
     * the flow panel used to display the widget
     */
    protected FlowPanel flowPanel;

    /**
     * The message widget
     */
    protected HTML message;

    /**
     * indicates if HTML is allowed as value of the widget
     */
    protected boolean allowHTMLInField;

    /**
     * Utility Class form DOM manipulation
     */
    protected DOMUtils domUtils = DOMUtils.getInstance();

    /**
     * Constructor
     * 
     * @param formWidget
     *            the widget data object
     */
    public FormMessageWidget(final ReducedFormWidget formWidget) {

        this(formWidget, formWidget.getInitialFieldValue());
    }

    /**
     * Constructor
     * 
     * @param formWidget
     *            the widget data object
     * @param fieldValue
     *            the widget field value object
     */
    public FormMessageWidget(final ReducedFormWidget formWidget, final FormFieldValue fieldValue) {

        allowHTMLInField = formWidget.allowHTMLInField();

        flowPanel = new FlowPanel();

        createWidget(formWidget, fieldValue);

        initWidget(flowPanel);
    }

    /**
     * Create the message widget
     * 
     * @param formMessageData
     *            the widget data object
     * @param fieldValue
     *            the widget field value object
     */
    protected void createWidget(final ReducedFormWidget formMessageData, final FormFieldValue fieldValue) {

        message = new HTML();
        if (SupportedFieldTypes.JAVA_DATE_CLASSNAME.equals(fieldValue.getValueType())) {
            message.setText(getDateAsText(formMessageData, fieldValue));
        } else if (allowHTMLInField) {
            setHTMLContent(fieldValue);
        } else {
            message.setText(getStringValue(fieldValue));
        }
        message.setStyleName("bonita_form_message");
        if (formMessageData.getInputStyle() != null && formMessageData.getInputStyle().length() > 0) {
            message.addStyleName(formMessageData.getInputStyle());
        }
        if (formMessageData.getTitle() != null && formMessageData.getTitle().length() > 0) {
            message.setTitle(formMessageData.getTitle());
        }
        flowPanel.add(message);
        DOMUtils.getInstance().addScriptElementToDOM(message.getElement(), flowPanel.getElement());
    }

    protected void setHTMLContent(final FormFieldValue fieldValue) {
        final String content = getStringValue(fieldValue);
        message.setHTML(content);
        // Fix Javascript execution on IE7 and IE8
        if (domUtils.isIE7() || domUtils.isIE8()) {
            final String scriptPattern = "<script.*javascript.*>(.*)</script>";
            final RegExp regExp = RegExp.compile(scriptPattern, "gim");
            for (MatchResult result = regExp.exec(content); result != null; result = regExp.exec(content)) {
                domUtils.javascriptEval(result.getGroup(1));
            }
        }
    }

    /**
     * Get the string display value for a date
     * 
     * @param widgetData
     *            the widget data object
     * @param fieldValue
     *            the field value
     * @return the date as a String
     */
    protected String getDateAsText(final ReducedFormWidget widgetData, final FormFieldValue fieldValue) {

        DateTimeFormat displayFormat = null;
        if (widgetData.getDisplayFormat() != null && widgetData.getDisplayFormat().length() > 0) {
            displayFormat = DateTimeFormat.getFormat(widgetData.getDisplayFormat());
        } else {
            displayFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_LONG);
        }
        Date initialDate = null;
        if (fieldValue.getValue() != null) {
            initialDate = (Date) fieldValue.getValue();
        } else {
            initialDate = new Date();
        }
        return displayFormat.format(initialDate);
    }

    /**
     * Get the string value of a {@link FormFieldValue}
     * 
     * @param fieldValue
     *            the {@link FormFieldValue}
     * @return a String
     */
    protected String getStringValue(final FormFieldValue fieldValue) {
        String value = null;
        if (SupportedFieldTypes.JAVA_STRING_CLASSNAME.equals(fieldValue.getValueType())) {
            value = (String) fieldValue.getValue();
        } else if (fieldValue.getValue() != null) {
            value = fieldValue.getValue().toString();
        }
        return value;
    }

    /**
     * Set the value of the message
     * 
     * @param fieldValue
     */
    public void setValue(final FormFieldValue fieldValue) {
        if (fieldValue.getValue() != null) {
            if (allowHTMLInField) {
                setHTMLContent(fieldValue);
            } else {
                message.setText(getStringValue(fieldValue));
            }
        }
    }
}
