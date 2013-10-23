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
package org.bonitasoft.forms.client.view.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bonitasoft.forms.client.i18n.FormsResourceBundle;
import org.bonitasoft.forms.client.model.FormFieldValue;
import org.bonitasoft.forms.client.model.FormType;
import org.bonitasoft.forms.client.model.FormWidget;
import org.bonitasoft.forms.client.model.ReducedFormPage;
import org.bonitasoft.forms.client.model.ReducedFormValidator;
import org.bonitasoft.forms.client.model.ReducedFormWidget;
import org.bonitasoft.forms.client.model.ReducedHtmlTemplate;
import org.bonitasoft.forms.client.model.WidgetType;
import org.bonitasoft.forms.client.model.exception.AbortedFormException;
import org.bonitasoft.forms.client.model.exception.CanceledFormException;
import org.bonitasoft.forms.client.model.exception.FileTooBigException;
import org.bonitasoft.forms.client.model.exception.FormAlreadySubmittedException;
import org.bonitasoft.forms.client.model.exception.FormInErrorException;
import org.bonitasoft.forms.client.model.exception.SkippedFormException;
import org.bonitasoft.forms.client.rpc.FormsServiceAsync;
import org.bonitasoft.forms.client.view.FormsAsyncCallback;
import org.bonitasoft.forms.client.view.SupportedFieldTypes;
import org.bonitasoft.forms.client.view.common.DOMUtils;
import org.bonitasoft.forms.client.view.common.RpcFormsServices;
import org.bonitasoft.forms.client.view.common.URLUtils;
import org.bonitasoft.forms.client.view.common.URLUtilsFactory;
import org.bonitasoft.forms.client.view.widget.ElementAttributeSupport;
import org.bonitasoft.forms.client.view.widget.FormButtonWidget;
import org.bonitasoft.forms.client.view.widget.FormFieldWidget;
import org.bonitasoft.forms.client.view.widget.FormMessageWidget;
import org.bonitasoft.forms.client.view.widget.FormValidationMessageWidget;
import org.bonitasoft.web.rest.model.user.User;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Pages view controller (handles the page flow for processes or tasks)
 * 
 * @author Anthony Birembaut
 */
public class FormPagesViewController {

    /**
     * action type after a form validation
     */
    protected static enum ACTION_TYPE {
        PREVIOUS, NEXT, SUBMIT
    };

    /**
     * mandatory form field symbol
     */
    protected String mandatoryFieldSymbol;

    /**
     * mandatory form field label
     */
    protected String mandatoryFieldLabel;

    /**
     * mandatory form field symbol classes
     */
    protected String mandatoryFieldClasses;

    /**
     * forms RPC service
     */
    protected FormsServiceAsync formsServiceAsync;

    /**
     * Utility Class form DOM manipulation
     */
    protected DOMUtils domUtils = DOMUtils.getInstance();

    /**
     * Utility Class form URL manipulation
     */
    protected URLUtils urlUtils = URLUtilsFactory.getInstance();

    /**
     * index of the currently displayed page in the list of pages ids
     */
    protected int currentPageIndex = 0;

    /**
     * the pressed submit button
     */
    protected Widget pressedButton;

    /**
     * the pressed label buttons
     */
    protected Set<Label> disabledLabelButtons = new HashSet<Label>();

    /**
     * indicates whether the current page is valid or not
     */
    protected boolean isCurrentPageValid;

    /**
     * List of submitted pages ids
     */
    protected List<String> followedPagesIds = new ArrayList<String>();

    /**
     * Maintained Map of already displayed pages (allow to diplay the previous
     * page without reloading everything)
     */
    protected Map<String, ReducedFormPage> formPages = new HashMap<String, ReducedFormPage>();

    /**
     * Map of form fields already displayed in the page flow
     */
    protected Map<String, FormFieldWidget> fieldWidgets = new HashMap<String, FormFieldWidget>();

    /**
     * Map of form buttons already displayed in the page flow
     */
    protected Map<String, FormButtonWidget> buttonWidgets = new HashMap<String, FormButtonWidget>();

    /**
     * Map of form messages already displayed in the page flow
     */
    protected Map<String, FormMessageWidget> messageWidgets = new HashMap<String, FormMessageWidget>();

    /**
     * Map of form iframes already displayed in the page flow
     */
    protected Map<String, Frame> frameWidgets = new HashMap<String, Frame>();

    /**
     * Map of the flow's widget values
     */
    protected Map<String, FormFieldValue> widgetValues = new HashMap<String, FormFieldValue>();

    /**
     * Handler allowing to display a page after the RPC call retrieving its
     * definition
     */
    protected FormPageHandler formsPageHandler = new FormPageHandler();

    /**
     * Click Handler dealing with form submission
     */
    protected SubmitClickHandler submitClickHandler = new SubmitClickHandler();

    /**
     * application template panel (can be null in form only mode)
     */
    protected HTMLPanel applicationHTMLPanel;

    /**
     * current page template panel
     */
    protected HTMLPanel pageHTMLPanel;

    /**
     * The logged in user
     */
    protected User user;

    /**
     * The current form ID
     */
    protected String formID;

    /**
     * The element ID in which to insert the form
     */
    protected String elementId;

    /**
     * The map of URL parameters
     */
    protected Map<String, Object> urlContext;

    /**
     * Constructor
     */
    public FormPagesViewController(final String formID, final Map<String, Object> contextMap, final ReducedFormPage firstPage,
            final HTMLPanel applicationHTMLPanel, final User user, final String elementId) {

        this.user = user;
        this.formID = formID;
        this.elementId = elementId;
        urlContext = contextMap;
        this.applicationHTMLPanel = applicationHTMLPanel;
        final String pageId = firstPage.getPageId();
        followedPagesIds.add(pageId);
        formPages.put(pageId, firstPage);

        formsServiceAsync = RpcFormsServices.getFormsService();
    }

    /**
     * Display the page at the given index
     * 
     * @param newIndex
     *            index of the page in the page list
     */
    public void displayPage(final int newIndex) throws IndexOutOfBoundsException {

        if (newIndex >= 0) {
            if (newIndex < currentPageIndex) {
                followedPagesIds.remove(currentPageIndex);
                currentPageIndex = newIndex;
                final String newPageId = followedPagesIds.get(newIndex);
                final ReducedFormPage formPage = formPages.get(newPageId);
                buildPage(formPage, true, false);
            } else {
                if (newIndex == 0) {
                    final String newPageId = followedPagesIds.get(currentPageIndex);
                    final ReducedFormPage formPage = formPages.get(newPageId);
                    buildPage(formPage, false, true);
                } else {
                    final String currentPageId = followedPagesIds.get(currentPageIndex);
                    final String nextPageExpressionId = formPages.get(currentPageId).getNextPageExpressionId();
                    currentPageIndex = newIndex;
                    final Map<String, FormFieldValue> fieldValues = new HashMap<String, FormFieldValue>();
                    fieldValues.putAll(widgetValues);
                    if (formID != null) {
                        formsServiceAsync.getFormNextPage(formID, urlContext, nextPageExpressionId, fieldValues, formsPageHandler);
                    }
                }
            }
        } else {
            enableButtons(true);
            throw new IndexOutOfBoundsException("No form available");
        }
    }

    /**
     * Handler allowing to display the form
     */
    protected class FormPageHandler extends FormsAsyncCallback<ReducedFormPage> {

        /**
         * {@inheritDoc}
         */
        @Override
        public void onSuccess(final ReducedFormPage reducedFormPage) {

            try {
                RequestBuilder theRequestBuilder;
                final String theURL = urlUtils.buildLayoutURL(reducedFormPage.getPageTemplate().getBodyContentId(), (String) urlContext.get(URLUtils.FORM_ID),
                        (String) urlContext.get(URLUtils.TASK_ID_PARAM), true);
                GWT.log("Calling the Form Layout Download Servlet with query: " + theURL);
                theRequestBuilder = new RequestBuilder(RequestBuilder.GET, theURL);
                theRequestBuilder.setCallback(new RequestCallback() {

                    @Override
                    public void onError(final Request aRequest, final Throwable exception) {
                        final String errorMessage = FormsResourceBundle.getErrors().applicationConfigRetrievalError();
                        formsServiceAsync.getApplicationErrorTemplate(reducedFormPage.getPageId(), urlContext,
                                new ErrorPageHandler(null, reducedFormPage.getPageId(), errorMessage, exception, elementId));
                    }

                    @Override
                    public void onResponseReceived(final Request request, final Response response) {
                        final String pageId = reducedFormPage.getPageId();
                        followedPagesIds.add(pageId);
                        boolean hasAlreadyBeenDisplayed = false;
                        if (formPages.get(pageId) != null) {
                            hasAlreadyBeenDisplayed = true;
                        }
                        reducedFormPage.getPageTemplate().setBodyContent(response.getText());
                        formPages.put(pageId, reducedFormPage);
                        buildPage(reducedFormPage, hasAlreadyBeenDisplayed, true);
                    }
                });
                theRequestBuilder.send();
            } catch (final Exception e) {
                Window.alert("Error while trying to query the form layout :" + e.getMessage());
            }

        }

        @Override
        public void onUnhandledFailure(final Throwable t) {
            String errorMessage = null;
            try {
                throw t;
            } catch (final CanceledFormException e) {
                errorMessage = FormsResourceBundle.getMessages().cancelledTaskMessage();
                // } catch (SuspendedFormException e) {
                // errorMessage = FormsResourceBundle.getMessages().suspendedTaskMessage();
            } catch (final AbortedFormException e) {
                errorMessage = FormsResourceBundle.getMessages().abortedFormMessage();
            } catch (final FormInErrorException e) {
                errorMessage = FormsResourceBundle.getMessages().errorTaskMessage();
            } catch (final SkippedFormException e) {
                errorMessage = FormsResourceBundle.getMessages().skippedFormMessage();
            } catch (final FormAlreadySubmittedException e) {
                errorMessage = FormsResourceBundle.getErrors().formAlreadySubmittedError();
            } catch (final Throwable e) {
                errorMessage = FormsResourceBundle.getErrors().pageRetrievalError();
            }
            if (formID != null) {
                formsServiceAsync.getApplicationErrorTemplate(formID, urlContext, new ErrorPageHandler(applicationHTMLPanel, formID, pageHTMLPanel,
                        errorMessage, t, elementId));
            }
            enableButtons(true);

        }
    }

    /**
     * Build the page (template + form fields)
     * 
     * @param formPage
     *            the page definition
     * @param hasAlreadyBeenDisplayed
     *            indicates whether the page has already been displayed or not
     * @param isNextPage
     *            indicate if the page to display is the next page
     */
    protected void buildPage(final ReducedFormPage formPage, final boolean hasAlreadyBeenDisplayed, final boolean isNextPage) {

        enableButtons(false);
        final ReducedHtmlTemplate pageTemplate = formPage.getPageTemplate();
        if (pageHTMLPanel != null) {
            if (applicationHTMLPanel != null) {
                applicationHTMLPanel.remove(pageHTMLPanel);
            }
        }
        pageHTMLPanel = new HTMLPanel(pageTemplate.getBodyContent());

        final String onloadAttributeValue = domUtils.insertPageTemplate(pageTemplate.getHeadNodes(), pageHTMLPanel, pageTemplate.getBodyAttributes(),
                applicationHTMLPanel, elementId);
        final Element pageLabelElement = DOM.getElementById(DOMUtils.PAGE_LABEL_ELEMENT_ID);
        if (pageLabelElement != null) {
            String pageLabel = formPage.getPageLabel();
            if (pageLabel.startsWith("#")) {
                pageLabel = FormsResourceBundle.getMessages().caseStartLabelPrefix() + " " + pageLabel.substring(1);
            }
            domUtils.insertInElement(pageHTMLPanel, DOMUtils.PAGE_LABEL_ELEMENT_ID, pageLabel, !formPage.allowHTMLInLabel());
        }

        if (FormType.entry == formPage.getFormType()) {
            buildEditMode(pageHTMLPanel, formPage, hasAlreadyBeenDisplayed, isNextPage, onloadAttributeValue);
            final String autoSubmit = (String) urlContext.get(URLUtils.AUTO_SUBMIT_PARAM);
            if (autoSubmit != null) {
                final FormButtonWidget autoSubmitButton = buttonWidgets.get(autoSubmit);
                if (autoSubmitButton != null && autoSubmitButton.getWidgetType().equals(WidgetType.BUTTON_SUBMIT)) {
                    disableButtons(autoSubmitButton.getButton());
                    validatePage(ACTION_TYPE.SUBMIT);
                }
            }
        } else {
            buildViewMode(pageHTMLPanel, formPage, hasAlreadyBeenDisplayed, isNextPage, onloadAttributeValue);
        }
        domUtils.overrideBrowserNativeInputs();
        resizeFrame();
        domUtils.hideLoading();

    }

    /**
     * Insert the widgets in the page for the view mode
     * 
     * @param pageHTMLPanel
     *            the HTMLPanel
     * @param formPage
     *            the page definition
     * @param hasAlreadyBeenDisplayed
     *            indicates whether the page has already been displayed or not
     * @param isNextPage
     *            indicate if the page to display is the next page
     * @param onloadAttributeValue
     *            the onload attribute value if it exists
     */
    protected void buildViewMode(final HTMLPanel pageHTMLPanel, final ReducedFormPage formPage, final boolean hasAlreadyBeenDisplayed,
            final boolean isNextPage, final String onloadAttributeValue) {

        final List<ReducedFormWidget> formWidgets = formPage.getFormWidgets();
        for (final ReducedFormWidget formWidgetData : formWidgets) {
            final String widgetId = formWidgetData.getId();
            // for buttons
            if (formWidgetData.getType().name().startsWith("BUTTON") && !formWidgetData.getType().equals(WidgetType.BUTTON_SUBMIT)) {
                FormButtonWidget formButtonWidget = null;
                if (hasAlreadyBeenDisplayed) {
                    formButtonWidget = buttonWidgets.get(widgetId);
                } else {
                    setButtonLabel(formWidgetData);
                    formButtonWidget = new FormButtonWidget(formWidgetData);
                    if (!formWidgetData.getType().equals(WidgetType.BUTTON)) {
                        addClickListener(formButtonWidget, false);
                    }
                    buttonWidgets.put(widgetId, formButtonWidget);
                }
                insertWidget(pageHTMLPanel, formWidgetData, formButtonWidget, "bonita_form_button_entry");
                // for text messages
            } else if (formWidgetData.getType().equals(WidgetType.MESSAGE)) {
                FormMessageWidget formMessageWidget = null;
                if (getValueFromHistory(hasAlreadyBeenDisplayed, isNextPage, formWidgetData)) {
                    formMessageWidget = messageWidgets.get(widgetId);
                } else {
                    formMessageWidget = new FormMessageWidget(formWidgetData);
                    messageWidgets.put(widgetId, formMessageWidget);
                }
                insertWidget(pageHTMLPanel, formWidgetData, formMessageWidget, "bonita_form_entry");
                // for iframes
            } else if (formWidgetData.getType().equals(WidgetType.IFRAME)) {
                Frame formFrameWidget = null;
                if (getValueFromHistory(hasAlreadyBeenDisplayed, isNextPage, formWidgetData)) {
                    formFrameWidget = frameWidgets.get(widgetId);
                } else {
                    formFrameWidget = new Frame((String) formWidgetData.getInitialFieldValue().getValue());
                    frameWidgets.put(widgetId, formFrameWidget);
                }
                formFrameWidget.setStyleName("bonita_iframe");
                final String widgetStyle = formWidgetData.getInputStyle();
                if (widgetStyle != null && widgetStyle.length() > 0) {
                    formFrameWidget.addStyleName(widgetStyle);
                }
                final Element formFrameElement = formFrameWidget.getElement();
                formFrameElement.setAttribute("frameBorder", "0");
                formFrameElement.setAttribute("allowTransparency", "true");
                addHTMLAttributes(formFrameWidget, formWidgetData);
                insertWidget(pageHTMLPanel, formWidgetData, formFrameWidget, "bonita_form_entry");
                // for form fields (Widgets other that buttons and text)
            } else {
                if (formWidgetData.isViewPageWidget()) {
                    FormFieldWidget formFieldWidget = null;
                    if (getValueFromHistory(hasAlreadyBeenDisplayed, isNextPage, formWidgetData)) {
                        formFieldWidget = fieldWidgets.get(widgetId);
                    } else {
                        formFieldWidget = new FormFieldWidget(formWidgetData, urlContext, mandatoryFieldSymbol, mandatoryFieldClasses);
                        fieldWidgets.put(widgetId, formFieldWidget);
                    }
                    insertWidget(pageHTMLPanel, formWidgetData, formFieldWidget, "bonita_form_entry");
                }
            }
        }
        if (onloadAttributeValue != null) {
            domUtils.javascriptEval(onloadAttributeValue);
        }
    }

    protected void addHTMLAttributes(final Widget fieldWidget, final ReducedFormWidget formWidgetData) {
        final ElementAttributeSupport elementAttributeSupport = new ElementAttributeSupport();
        elementAttributeSupport.addHtmlAttributes(fieldWidget, formWidgetData.getHtmlAttributes());
    }

    /**
     * Insert the widgets in the page for the edit mode
     * 
     * @param pageHTMLPanel
     *            the HTMLPanel
     * @param formPage
     *            the page definition
     * @param hasAlreadyBeenDisplayed
     *            indicates whether the page has already been displayed or not
     * @param isNextPage
     *            indicate if the page to display is the next page
     * @param onloadAttributeValue
     *            the onload attribute value if it exists
     */
    protected void buildEditMode(final HTMLPanel pageHTMLPanel, final ReducedFormPage formPage, final boolean hasAlreadyBeenDisplayed,
            final boolean isNextPage, final String onloadAttributeValue) {

        final List<ReducedFormWidget> formWidgets = formPage.getFormWidgets();
        for (final ReducedFormWidget formWidgetData : formWidgets) {
            final String widgetId = formWidgetData.getId();
            // for buttons
            if (formWidgetData.getType().name().startsWith("BUTTON")) {
                FormButtonWidget formButtonWidget = null;
                if (hasAlreadyBeenDisplayed) {
                    formButtonWidget = buttonWidgets.get(widgetId);
                } else {
                    setButtonLabel(formWidgetData);
                    formButtonWidget = new FormButtonWidget(formWidgetData);
                    if (!formWidgetData.getType().equals(WidgetType.BUTTON)) {
                        addClickListener(formButtonWidget, true);
                    }
                    buttonWidgets.put(widgetId, formButtonWidget);
                }
                insertWidget(pageHTMLPanel, formWidgetData, formButtonWidget, "bonita_form_button_entry");
                // for text messages
            } else if (formWidgetData.getType().equals(WidgetType.MESSAGE)) {
                FormMessageWidget formMessageWidget = null;
                if (getValueFromHistory(hasAlreadyBeenDisplayed, isNextPage, formWidgetData)) {
                    formMessageWidget = messageWidgets.get(widgetId);
                } else {
                    formMessageWidget = new FormMessageWidget(formWidgetData);
                    messageWidgets.put(widgetId, formMessageWidget);
                }
                insertWidget(pageHTMLPanel, formWidgetData, formMessageWidget, "bonita_form_entry");

                // for iframes
            } else if (formWidgetData.getType().equals(WidgetType.IFRAME)) {
                Frame formFrameWidget = null;
                if (getValueFromHistory(hasAlreadyBeenDisplayed, isNextPage, formWidgetData)) {
                    formFrameWidget = frameWidgets.get(widgetId);
                } else {
                    formFrameWidget = new Frame((String) formWidgetData.getInitialFieldValue().getValue());
                    frameWidgets.put(widgetId, formFrameWidget);
                }
                formFrameWidget.setStyleName("bonita_iframe");
                final String widgetStyle = formWidgetData.getInputStyle();
                if (widgetStyle != null && widgetStyle.length() > 0) {
                    formFrameWidget.addStyleName(widgetStyle);
                }
                final Element formFrameElement = formFrameWidget.getElement();
                formFrameElement.setAttribute("frameBorder", "0");
                formFrameElement.setAttribute("allowTransparency", "true");

                addHTMLAttributes(formFrameWidget, formWidgetData);
                insertWidget(pageHTMLPanel, formWidgetData, formFrameWidget, "bonita_form_entry");

                // for form fields (Widgets other that buttons and text)
            } else {
                // set field value from the URL
                if (!formWidgetData.isReadOnly()) {
                    setFieldValueFromURL(formWidgetData, widgetId);
                }
                FormFieldWidget formFieldWidget = null;
                if (getValueFromHistory(hasAlreadyBeenDisplayed, isNextPage, formWidgetData)) {
                    formFieldWidget = fieldWidgets.get(widgetId);
                } else {
                    formFieldWidget = new FormFieldWidget(formWidgetData, urlContext, mandatoryFieldSymbol, mandatoryFieldClasses);
                    fieldWidgets.put(widgetId, formFieldWidget);
                }
                insertWidget(pageHTMLPanel, formWidgetData, formFieldWidget, "bonita_form_entry");

            }
        }

        if (onloadAttributeValue != null) {
            domUtils.javascriptEval(onloadAttributeValue);
        }
    }

    /**
     * Check if the value of the field should be retrieved from the history or recalculated
     * 
     * @param hasAlreadyBeenDisplayed
     *            indicates whether the page has already been displayed or not
     * @param isNextPage
     *            indicate if the page to display is the next page
     * @param formWidgetData
     *            the widget definition
     * @return true if the value of the field should be retrieved from the history, false otherwise
     */
    protected boolean getValueFromHistory(final boolean hasAlreadyBeenDisplayed, final boolean isNextPage, final ReducedFormWidget formWidgetData) {
        if (isNextPage) {
            return hasAlreadyBeenDisplayed && !formWidgetData.hasDynamicValue();
        } else {
            return true;
        }
    }

    /**
     * Insert the widget in the page
     * 
     * @param pageHTMLPanel
     *            the HTMLPanel
     * @param formWidgetData
     *            the widget definition
     * @param widget
     *            the widget to insert
     * @param containerStyle
     *            the style to apply to the container
     */
    protected void insertWidget(final HTMLPanel pageHTMLPanel, final ReducedFormWidget formWidgetData, final Widget widget, final String containerStyle) {
        if (formWidgetData.isDisplayCondition()) {
            final Element widgetParentElement = pageHTMLPanel.getElementById(formWidgetData.getId());
            final String widgetStyle;
            if (formWidgetData.getStyle() != null && formWidgetData.getStyle().length() > 0) {
                widgetStyle = containerStyle + " " + formWidgetData.getStyle();
            } else {
                widgetStyle = containerStyle;
            }
            if (widgetParentElement != null) {
                pageHTMLPanel.add(widget, widgetParentElement);
                widgetParentElement.addClassName(widgetStyle);
            } else {
                Window.alert("An element with id " + formWidgetData.getId() + " is missing from the page template.");
            }
        }
    }

    /**
     * Check in the URL if the initial value of the field is specified and override it
     * 
     * @param formWidgetData
     *            the widget data
     * @param widgetId
     *            the id of the widget
     */
    protected void setFieldValueFromURL(final ReducedFormWidget formWidgetData, final String widgetId) {
        final Object widgetValueObjectInURL = urlContext.get(widgetId);
        if (widgetValueObjectInURL != null) {
            final String widgetValueInURL = (String) widgetValueObjectInURL;
            try {
                switch (formWidgetData.getType()) {
                    case TEXTBOX:
                    case TEXTAREA:
                    case LISTBOX_SIMPLE:
                    case RADIOBUTTON_GROUP:
                    case PASSWORD:
                    case SUGGESTBOX:
                    case SUGGESTBOX_ASYNC:
                    case HIDDEN:
                        formWidgetData.getInitialFieldValue().setValue(widgetValueInURL);
                        break;
                    case DATE:
                        final Long millisWidgetValueInURL = Long.valueOf(widgetValueInURL);
                        final Date dateFieldValue = new Date(millisWidgetValueInURL);
                        formWidgetData.getInitialFieldValue().setValue(dateFieldValue);
                        break;
                    case DURATION:
                        final Long longWidgetValueInURL = Long.valueOf(widgetValueInURL);
                        formWidgetData.getInitialFieldValue().setValue(longWidgetValueInURL);
                        break;
                    case CHECKBOX:
                        final Boolean booleanWidgetValueInURL = Boolean.valueOf(widgetValueInURL);
                        formWidgetData.getInitialFieldValue().setValue(booleanWidgetValueInURL);
                        break;
                    case LISTBOX_MULTIPLE:
                    case CHECKBOX_GROUP:
                    case TABLE:
                        final String[] arrayWidgetValueInURL = widgetValueInURL.split(",");
                        final List<String> listWidgetValueInURL = Arrays.asList(arrayWidgetValueInURL);
                        formWidgetData.getInitialFieldValue().setValue((Serializable) listWidgetValueInURL);
                        break;
                    default:
                        // file upload widgets are not supported
                        break;
                }
            } catch (final Exception e) {
                // Do nothing (the field will be set with it's original initialvalue)
            }
        }
    }

    /**
     * Set a button's label and title
     * 
     * @param formWidgetData
     */
    protected void setButtonLabel(final ReducedFormWidget formWidgetData) {

        if (formWidgetData.getLabel().equals("#previousPageButtonLabel")) {
            final String previousButtonLabel = FormsResourceBundle.getMessages().previousPageButtonLabel();
            formWidgetData.setLabel(previousButtonLabel);
            final String previousButtonTitle = FormsResourceBundle.getMessages().previousPageButtonTitle();
            formWidgetData.setTitle(previousButtonTitle);
        } else if (formWidgetData.getLabel().equals("#nextPageButtonLabel")) {
            final String nextButtonLabel = FormsResourceBundle.getMessages().nextPageButtonLabel();
            formWidgetData.setLabel(nextButtonLabel);
            final String nextButtonTitle = FormsResourceBundle.getMessages().nextPageButtonTitle();
            formWidgetData.setTitle(nextButtonTitle);
        } else if (formWidgetData.getLabel().equals("#submitButtonLabel")) {
            final String submitButtonLabel = FormsResourceBundle.getMessages().submitButtonLabel();
            formWidgetData.setLabel(submitButtonLabel);
            final String submitButtonTitle = FormsResourceBundle.getMessages().submitButtonTitle();
            formWidgetData.setTitle(submitButtonTitle);
        }
    }

    /**
     * Associate a button with the correct click handler
     * 
     * @param formButtonWidget
     * @param isEditMode
     */
    protected void addClickListener(final FormButtonWidget formButtonWidget, final boolean isEditMode) {

        final WidgetType buttonType = formButtonWidget.getWidgetType();
        switch (buttonType) {
            case BUTTON_PREVIOUS:
                formButtonWidget.addClickHandler(new PreviousPageClickHandler(isEditMode));
                break;
            case BUTTON_NEXT:
                formButtonWidget.addClickHandler(new NextPageClickHandler(isEditMode));
                break;
            case BUTTON_SUBMIT:
                formButtonWidget.addClickHandler(submitClickHandler);
                break;
            default:
                break;
        }
    }

    /**
     * Handler for the next page button
     */
    protected class NextPageClickHandler implements ClickHandler {

        protected boolean editMode;

        /**
         * @param editMode
         */
        public NextPageClickHandler(final boolean editMode) {

            this.editMode = editMode;
        }

        /**
         * hide the current page and display the next page
         */
        @Override
        public void onClick(final ClickEvent event) {

            final Object source = event.getSource();
            if (!(source instanceof Label && disabledLabelButtons.contains(source))) {
                disableButtons((Widget) source);
                if (editMode) {
                    validatePage(ACTION_TYPE.NEXT);
                } else {
                    final ReducedFormPage formPage = formPages.get(followedPagesIds.get(currentPageIndex));
                    recordValues(formPage.getFormWidgets());
                    final int newIndex = currentPageIndex + 1;
                    try {
                        detachPageWidgets();
                        displayPage(newIndex);
                    } catch (final IndexOutOfBoundsException e) {
                        final String errorMessage = FormsResourceBundle.getErrors().pageIndexError(newIndex);
                        formsServiceAsync.getApplicationErrorTemplate(errorMessage, urlContext, new ErrorPageHandler(applicationHTMLPanel, formID,
                                pageHTMLPanel, errorMessage, elementId));
                        enableButtons(true);
                    }
                }
            }
        }
    }

    /**
     * Handler for the previous page button
     */
    protected class PreviousPageClickHandler implements ClickHandler {

        protected boolean editMode;

        /**
         * @param isEditMode
         */
        public PreviousPageClickHandler(final boolean editMode) {
            this.editMode = editMode;
        }

        /**
         * hide the current page and display the previous page
         */
        @Override
        public void onClick(final ClickEvent event) {

            final Object source = event.getSource();
            if (!(source instanceof Label && disabledLabelButtons.contains(source))) {
                disableButtons((Widget) source);
                final ReducedFormPage formPage = formPages.get(followedPagesIds.get(currentPageIndex));
                recordValues(formPage.getFormWidgets());
                final int newIndex = currentPageIndex - 1;
                try {
                    detachPageWidgets();
                    displayPage(newIndex);
                } catch (final IndexOutOfBoundsException e) {
                    final String errorMessage = FormsResourceBundle.getErrors().pageIndexError(newIndex);
                    formsServiceAsync.getApplicationErrorTemplate(errorMessage, urlContext, new ErrorPageHandler(applicationHTMLPanel, formID, pageHTMLPanel,
                            errorMessage, elementId));
                    enableButtons(true);
                }
            }
        }
    }

    /**
     * Handler for the submission of the form
     */
    protected class SubmitClickHandler implements ClickHandler {

        /**
         * submit the form
         */
        @Override
        public void onClick(final ClickEvent event) {

            final Object source = event.getSource();
            if (!(source instanceof Label && disabledLabelButtons.contains(source))) {
                disableButtons((Widget) source);
                validatePage(ACTION_TYPE.SUBMIT);
            }

        }
    }

    /**
     * Disable a button
     * 
     * @param button
     *            the button to disable
     */
    protected void disableButton(final Widget button) {
        if (button instanceof Button) {
            ((Button) button).setEnabled(false);
        } else if (button instanceof Label) {
            disabledLabelButtons.add((Label) button);
        }
    }

    /**
     * disable the buttons
     * 
     * @param button
     *            the button that was pressed
     */
    protected void disableButtons(final Widget pressedButton) {
        this.pressedButton = pressedButton;
        for (final Entry<String, FormButtonWidget> entry : buttonWidgets.entrySet()) {
            if (isActionButton(entry.getValue())) {
                disableButton(entry.getValue().getButton());
            }
        }
    }

    protected boolean isActionButton(final FormButtonWidget formButtonWidget) {
        return formButtonWidget.getWidgetType().equals(WidgetType.BUTTON_SUBMIT) || formButtonWidget.getWidgetType().equals(WidgetType.BUTTON_NEXT)
                || formButtonWidget.getWidgetType().equals(WidgetType.BUTTON_PREVIOUS);
    }

    /**
     * Enable a button
     * 
     * @param button
     *            to enable
     */
    protected void enableButton(final Widget button) {
        if (button != null) {
            if (button instanceof Button) {
                ((Button) button).setEnabled(true);
            } else if (button instanceof Label) {
                disabledLabelButtons.remove(button);
            }
        }
    }

    /**
     * Enable the buttons
     * 
     * @param hideLoader
     *            boolean to specify if we had need to hide the loader
     */
    protected void enableButtons(final boolean hideLoader) {
        pressedButton = null;
        for (final Entry<String, FormButtonWidget> entry : buttonWidgets.entrySet()) {
            if (isActionButton(entry.getValue())) {
                enableButton(entry.getValue().getButton());
            }
        }
        if (hideLoader) {
            domUtils.hideLoading();
        }
    }

    /**
     * Records a page's fields
     * 
     * @param formWidgets
     *            the list of form widgets
     */
    protected void recordValues(final List<ReducedFormWidget> formWidgets) {

        for (final ReducedFormWidget formWidget : formWidgets) {
            final String widgetId = formWidget.getId();
            if (fieldWidgets.containsKey(widgetId)) {
                final FormFieldValue widgetValue = fieldWidgets.get(widgetId).getValue();
                widgetValues.put(widgetId, widgetValue);
            }
        }
    }

    /**
     * Records a page's fields and validate it
     * 
     * @param actionAfterValidation
     *            type of action to execute after the validation step
     */
    protected void validatePage(final ACTION_TYPE actionAfterValidation) {

        domUtils.displayLoading();
        isCurrentPageValid = true;
        final ReducedFormPage formPage = formPages.get(followedPagesIds.get(currentPageIndex));

        // deals with the mandatory fields
        validateMandatoryFieldWidgets(formPage.getFormWidgets());

        // evaluates the number of widget validations to performs and store the
        // widgets to validate
        final List<ReducedFormWidget> formWidgetsToValidate = getFormWidgetsToValidate(formPage.getFormWidgets());

        final List<ReducedFormValidator> pageValidators = formPage.getPageValidators();

        final String submitButtonId = pressedButton.getElement().getParentElement().getParentElement().getId();

        // fields validation
        if (!formWidgetsToValidate.isEmpty()) {
            final Map<String, String> validators = new HashMap<String, String>();
            for (final ReducedFormWidget formWidget : formWidgetsToValidate) {
                cleanValidatorsMessages(formWidget.getValidators());
                if (formWidget.isDisplayCondition()) {
                    validators.put(formWidget.getId(), formWidget.getValidatorsCacheId());
                }
            }

            formsServiceAsync.validateFormFields(formID, urlContext, validators, widgetValues, submitButtonId, new FormFieldValidatorHandler(
                    actionAfterValidation, formPage.getPageValidators(), formPage.getPageValidatorsId()));
        } else if (isCurrentPageValid) {
            if (!pageValidators.isEmpty()) {
                cleanValidatorsMessages(pageValidators);
                formsServiceAsync.validateFormPage(formID, urlContext, formPage.getPageValidatorsId(), widgetValues, submitButtonId,
                        new FormPageValidatorHandler(actionAfterValidation));
            } else {
                submitForm(actionAfterValidation);
            }
        } else {
            resizeFrame();
            enableButtons(true);
        }
    }

    /**
     * Validate the compliance of a list of widgets with their mandatory attributes
     * 
     * @param formWidget
     */
    protected void validateMandatoryFieldWidgets(final List<ReducedFormWidget> formWidgets) {
        for (final ReducedFormWidget formWidget : formWidgets) {
            if (formWidget.isMandatory() && !formWidget.isReadOnly() && formWidget.isDisplayCondition()) {
                final FormFieldWidget mandatoryFieldWidget = fieldWidgets.get(formWidget.getId());
                validateMandatoryField(mandatoryFieldWidget);
            }
        }
    }

    /**
     * Validate the compliance of a widget with its mandatory attribute
     * 
     * @param mandatoryFieldWidget
     */
    protected void validateMandatoryField(final FormFieldWidget mandatoryFieldWidget) {
        if (!mandatoryFieldWidget.isVisible()) {
            return;
        }
        if (mandatoryFieldSymbol == null || mandatoryFieldSymbol.equals("#defaultMandatoryFieldSymbol")) {
            mandatoryFieldSymbol = FormsResourceBundle.getMessages().defaultMandatoryFieldSymbol();
        }
        mandatoryFieldWidget.setMandatoryLabel(mandatoryFieldSymbol);
        final FormFieldValue fieldValue = mandatoryFieldWidget.getValue();
        if (isEmptyField(fieldValue)) {
            if (mandatoryFieldLabel.equals("#defaultMandatoryFieldLabel")) {
                mandatoryFieldLabel = FormsResourceBundle.getMessages().defaultMandatoryFieldLabel();
            }
            mandatoryFieldWidget.setMandatoryLabel(mandatoryFieldLabel);
            isCurrentPageValid = false;
        }
    }

    private boolean isEmptyField(final FormFieldValue fieldValue) {
        return isEmptyString(fieldValue) || isEmptyFile(fieldValue) || isEmptyBoolean(fieldValue) || isEmptyCollection(fieldValue) || isEmptyLong(fieldValue)
                || isEmptyDate(fieldValue);
    }

    private boolean isEmptyDate(final FormFieldValue fieldValue) {
        return fieldValue.getValueType().equals(SupportedFieldTypes.JAVA_DATE_CLASSNAME) && fieldValue.getValue() == null;
    }

    private boolean isEmptyLong(final FormFieldValue fieldValue) {
        return fieldValue.getValueType().equals(SupportedFieldTypes.JAVA_LONG_CLASSNAME) && ((Long) fieldValue.getValue()).equals(Long.valueOf(0L));
    }

    private boolean isEmptyCollection(final FormFieldValue fieldValue) {
        return fieldValue.getValueType().equals(SupportedFieldTypes.JAVA_COLLECTION_CLASSNAME)
                && (fieldValue.getValue() == null || ((Collection<?>) fieldValue.getValue()).isEmpty());
    }

    private boolean isEmptyBoolean(final FormFieldValue fieldValue) {
        return fieldValue.getValueType().equals(SupportedFieldTypes.JAVA_BOOLEAN_CLASSNAME) && ((Boolean) fieldValue.getValue()).equals(Boolean.FALSE);
    }

    private boolean isEmptyString(final FormFieldValue fieldValue) {
        return fieldValue.getValueType().equals(SupportedFieldTypes.JAVA_STRING_CLASSNAME)
                && (fieldValue.getValue() == null || ((String) fieldValue.getValue()).length() == 0);
    }

    private boolean isEmptyFile(final FormFieldValue fieldValue) {
        return fieldValue.getValueType().equals(SupportedFieldTypes.JAVA_FILE_CLASSNAME)
                && (fieldValue.getDisplayedValue() == null || fieldValue.getDisplayedValue().length() == 0);
    }

    /**
     * Get the form field widgets to validate
     * 
     * @param formWidgets
     * @return the {@link List} of {@link FormWidget} to validate
     */
    protected List<ReducedFormWidget> getFormWidgetsToValidate(final List<ReducedFormWidget> formWidgets) {
        final List<ReducedFormWidget> formWidgetsToValidate = new ArrayList<ReducedFormWidget>();
        for (final ReducedFormWidget formWidget : formWidgets) {
            final String widgetId = formWidget.getId();
            if (fieldWidgets.containsKey(widgetId)) {
                final FormFieldValue widgetValue = fieldWidgets.get(widgetId).getValue();
                widgetValues.put(widgetId, widgetValue);
                if (!formWidget.getValidators().isEmpty() && !formWidget.isReadOnly()) {
                    formWidgetsToValidate.add(formWidget);
                }
            }
        }
        return formWidgetsToValidate;
    }

    /**
     * Remove the validation messages of the given validators from the page
     * 
     * @param validators
     */
    protected void cleanValidatorsMessages(final List<ReducedFormValidator> validators) {

        for (final ReducedFormValidator formValidator : validators) {
            final Element validatorEle = DOM.getElementById(formValidator.getId());
            if (validatorEle != null) {
                DOM.setInnerHTML(validatorEle, "");
            }
        }
    }

    protected void submitForm(final ACTION_TYPE actionAfterValidation) {
        if (actionAfterValidation.equals(ACTION_TYPE.SUBMIT)) {
            final String submitButtonId = pressedButton.getElement().getParentElement().getParentElement().getId();
            formsServiceAsync.executeActions(formID, urlContext, widgetValues, followedPagesIds, submitButtonId, new FormSubmissionHandler());
        } else {
            int newIndex = currentPageIndex;
            if (actionAfterValidation.equals(ACTION_TYPE.PREVIOUS)) {
                newIndex--;
            } else if (actionAfterValidation.equals(ACTION_TYPE.NEXT)) {
                newIndex++;
            }
            try {
                detachPageWidgets();
                displayPage(newIndex);
            } catch (final IndexOutOfBoundsException e) {
                final String errorMessage = FormsResourceBundle.getErrors().pageIndexError(newIndex);
                if (formID != null) {
                    formsServiceAsync.getApplicationErrorTemplate(errorMessage, urlContext, new ErrorPageHandler(applicationHTMLPanel, formID, pageHTMLPanel,
                            errorMessage, elementId));
                } else {
                    formsServiceAsync.getApplicationErrorTemplate(formID, urlContext, new ErrorPageHandler(applicationHTMLPanel, formID, pageHTMLPanel,
                            errorMessage, elementId));
                }
            }
        }
    }

    /**
     * Handler allowing to validate a form field
     */
    protected class FormFieldValidatorHandler extends FormsAsyncCallback<Map<String, List<ReducedFormValidator>>> {

        protected ACTION_TYPE actionAfterValidation;

        protected List<ReducedFormValidator> pageValidators;

        protected String pageValidatorsId;

        public FormFieldValidatorHandler(final ACTION_TYPE actionAfterValidation, final List<ReducedFormValidator> pageValidators, final String pageValidatorsId) {

            this.pageValidatorsId = pageValidatorsId;
            this.actionAfterValidation = actionAfterValidation;
            this.pageValidators = pageValidators;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onSuccess(final Map<String, List<ReducedFormValidator>> fieldValidators) {

            if (!fieldValidators.isEmpty()) {
                for (final Entry<String, List<ReducedFormValidator>> fieldValidatorsEntry : fieldValidators.entrySet()) {
                    // set the focus on the first invalid form field
                    final FormFieldWidget fieldWidget = fieldWidgets.get(fieldValidatorsEntry.getKey());
                    if (isCurrentPageValid) {
                        if (fieldWidget != null) {
                            fieldWidget.setFocusOn();
                        }
                        isCurrentPageValid = false;
                    }
                    for (final ReducedFormValidator fieldValidator : fieldValidatorsEntry.getValue()) {
                        final FormValidationMessageWidget formValidationMessageWidget = new FormValidationMessageWidget(fieldValidator, false);
                        final String validatorId = fieldValidator.getId();
                        final Element validatorElement = DOM.getElementById(validatorId);
                        if (validatorElement != null) {
                            DOM.appendChild(validatorElement, formValidationMessageWidget.getElement());
                        } else {
                            Window.alert("An element with id " + validatorId + " is missing from the page template.");
                        }
                    }
                }
            }
            if (isCurrentPageValid) {
                if (!pageValidators.isEmpty()) {
                    // once the fields validation is over perform the page validation
                    cleanValidatorsMessages(pageValidators);
                    final String submitButtonId = pressedButton.getElement().getParentElement().getParentElement().getId();
                    formsServiceAsync.validateFormPage(formID, urlContext, pageValidatorsId, widgetValues, submitButtonId, new FormPageValidatorHandler(
                            actionAfterValidation));
                } else {
                    submitForm(actionAfterValidation);
                }
            } else {
                resizeFrame();
                enableButtons(true);
            }
        }

        @Override
        public void onUnhandledFailure(final Throwable caught) {
            final String errorMessage = FormsResourceBundle.getErrors().fieldValidationError();
            if (formID != null) {
                formsServiceAsync.getApplicationErrorTemplate(errorMessage, urlContext, new ErrorPageHandler(applicationHTMLPanel, formID, pageHTMLPanel,
                        errorMessage, elementId));
            } else {
                formsServiceAsync.getApplicationErrorTemplate(formID, urlContext, new ErrorPageHandler(applicationHTMLPanel, formID, pageHTMLPanel,
                        errorMessage, elementId));
            }
            enableButtons(true);
        }
    }

    /**
     * Handler allowing to validate a form page
     */
    protected class FormPageValidatorHandler extends FormsAsyncCallback<List<ReducedFormValidator>> {

        protected ACTION_TYPE actionAfterValidation;

        public FormPageValidatorHandler(final ACTION_TYPE actionAfterValidation) {

            this.actionAfterValidation = actionAfterValidation;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onSuccess(final List<ReducedFormValidator> pageValidators) {

            if (!pageValidators.isEmpty()) {
                isCurrentPageValid = false;
                for (final ReducedFormValidator pageValidator : pageValidators) {
                    final FormValidationMessageWidget formValidationMessageWidget = new FormValidationMessageWidget(pageValidator, true);
                    final String validatorId = pageValidator.getId();
                    if (RootPanel.get(validatorId) != null) {
                        RootPanel.get(validatorId).add(formValidationMessageWidget);
                    } else {
                        Window.alert("An element with id " + validatorId + " is missing from the page template.");
                    }
                }
                resizeFrame();
                enableButtons(true);
            } else {
                submitForm(actionAfterValidation);
            }
        }

        @Override
        public void onUnhandledFailure(final Throwable caught) {
            final String errorMessage = FormsResourceBundle.getErrors().pageValidationError();
            if (formID != null) {
                formsServiceAsync.getApplicationErrorTemplate(errorMessage, urlContext, new ErrorPageHandler(applicationHTMLPanel, formID, pageHTMLPanel,
                        errorMessage, elementId));
            } else {
                formsServiceAsync.getApplicationErrorTemplate(formID, urlContext, new ErrorPageHandler(applicationHTMLPanel, formID, pageHTMLPanel,
                        errorMessage, elementId));
            }
            enableButtons(true);
        }
    }

    /**
     * Detach the data fields of the page
     */
    protected void detachPageWidgets() {
        final ReducedFormPage formPage = formPages.get(followedPagesIds.get(currentPageIndex));
        for (final ReducedFormWidget formWidget : formPage.getFormWidgets()) {
            final String widgetId = formWidget.getId();
            Widget widget;
            if (formWidget.getType().name().startsWith("BUTTON")) {
                widget = buttonWidgets.get(widgetId);
            } else if (formWidget.getType().equals(WidgetType.MESSAGE)) {
                widget = messageWidgets.get(widgetId);
            } else if (formWidget.getType().equals(WidgetType.IFRAME)) {
                widget = frameWidgets.get(widgetId);
            } else {
                widget = fieldWidgets.get(widgetId);
            }
            if (widget != null) {
                widget.removeFromParent();
            }
        }
    }

    /**
     * Handler to deal with what happens after a form submission
     */
    protected class FormSubmissionHandler extends FormsAsyncCallback<Map<String, Object>> {

        /**
         * {@inheritDoc}
         */
        @Override
        public void onSuccess(final Map<String, Object> newContext) {
            urlContext.clear();
            urlContext.putAll(newContext);
            redirectToConfirmationPage();
        }

        @Override
        public void onUnhandledFailure(final Throwable caught) {
            try {
                throw caught;
            } catch (final FormAlreadySubmittedException e) {
                final String errorMessage = FormsResourceBundle.getErrors().formAlreadySubmittedOrCancelledError();
                formsServiceAsync.getApplicationErrorTemplate(formID, urlContext, new ErrorPageHandler(applicationHTMLPanel, formID, pageHTMLPanel,
                        errorMessage, elementId));
            } catch (final FileTooBigException e) {
                final String fileName = e.getFileName();
                final String maxSize = e.getMaxSize();
                if (fileName != null) {
                    Window.alert(FormsResourceBundle.getErrors().fileTooBigErrorWithNameSize(fileName, maxSize));
                } else {
                    Window.alert(FormsResourceBundle.getErrors().fileTooBigErrorWithSize(maxSize));
                }
            } catch (final Throwable t) {
                final String errorMessage = FormsResourceBundle.getErrors().formSubmissionError();
                formsServiceAsync.getApplicationErrorTemplate(formID, urlContext, new ErrorPageHandler(applicationHTMLPanel, formID, pageHTMLPanel,
                        errorMessage, elementId));
            }

            enableButtons(true);
        }
    }

    protected class GetTokenAsyncCallback implements AsyncCallback<String> {

        protected String url;

        public GetTokenAsyncCallback(final String url) {
            this.url = url;
        }

        @Override
        public void onSuccess(final String temporaryToken) {
            url += "&" + URLUtils.USER_CREDENTIALS_PARAM + "=" + temporaryToken;
            if (domUtils.isPageInFrame()) {
                urlUtils.frameRedirect(DOMUtils.DEFAULT_FORM_ELEMENT_ID, url);
            } else {
                urlUtils.windowAssign(url);
            }
        }

        @Override
        public void onFailure(final Throwable t) {
            if (domUtils.isPageInFrame()) {
                urlUtils.frameRedirect(DOMUtils.DEFAULT_FORM_ELEMENT_ID, url);
            } else {
                urlUtils.windowAssign(url);
            }
        }
    }

    /**
     * If the page is containned in a form, resize the frame to fit the page height
     */
    protected void resizeFrame() {

        final Timer timer = new Timer() {

            @Override
            public void run() {
                if (domUtils.isPageInFrame()) {
                    if (formID != null) {
                        final String frameIdString = formID.split("$")[0];
                        for (int i = frameIdString.length(); i > 0; i--) {
                            final String frameId = frameIdString.substring(0, i);
                            if (domUtils.resizeFrame(frameId)) {
                                break;
                            };
                        }
                    }
                }
            }
        };
        timer.schedule(300);
    }

    public void setMandatoryFieldSymbol(final String mandatoryFieldSymbol) {

        if (mandatoryFieldSymbol != null && mandatoryFieldSymbol.length() > 0) {
            this.mandatoryFieldSymbol = mandatoryFieldSymbol;
        }
    }

    public void setMandatoryFieldClasses(final String mandatoryFieldClasses) {
        this.mandatoryFieldClasses = mandatoryFieldClasses;
    }

    public void setMandatoryFieldLabel(final String mandatoryFieldLabel) {
        this.mandatoryFieldLabel = mandatoryFieldLabel;
    }

    private void redirectToConfirmationPage() {
        final String defaultConfirmationMessage = FormsResourceBundle.getMessages().submissionConfirmationMessage();
        formsServiceAsync.getFormConfirmationTemplate(formID, urlContext, createConfirmationPageHandler(defaultConfirmationMessage));
    }

    private ConfirmationPageHandler createConfirmationPageHandler(final String defaultConfirmationMessage) {
        return new ConfirmationPageHandler(applicationHTMLPanel, elementId, defaultConfirmationMessage, formID, urlContext)
                .setCurrentPageHTMLPanel(pageHTMLPanel);
    }

}
