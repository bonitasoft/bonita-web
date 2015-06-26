/**
 * Copyright (C) 2011 BonitaSoft S.A.
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

import org.bonitasoft.forms.client.i18n.FormsResourceBundle;
import org.bonitasoft.forms.client.model.ReducedHtmlTemplate;
import org.bonitasoft.forms.client.model.exception.RPCException;
import org.bonitasoft.forms.client.view.FormsAsyncCallback;
import org.bonitasoft.forms.client.view.common.DOMUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Panel;

/**
 * Handler allowing to display the error template
 *
 * @author Anthony Birembaut , Ruiheng Fan
 *
 */
public class ErrorPageHandler extends FormsAsyncCallback<ReducedHtmlTemplate> {

    /**
     * Id of the message element on the error page
     */
    protected static final String ERROR_MESSAGE_ELEMENT_ID = "bonita_form_error_message";

    /**
     * Id of the message element on the error page
     */
    protected static final String CAUSE_MESSAGE_ELEMENT_ID = "bonita_form_cause_message";

    /**
     * Utility Class form DOM manipulation
     */
    protected DOMUtils domUtils;

    /**
     * process template panel (can be null in form only mode)
     */
    protected HTMLPanel applicationHTMLPanel;

    /**
     * Element Id
     */
    protected String formID;

    protected String elementId;

    /**
     * current page template panel
     */
    protected Panel currentPageHTMLPanel;

    /**
     * Error message
     */
    protected String errorMessage;

    /**
     * Cause message
     */
    protected String causeMessage;

    /**
     * Constructor.
     *
     * @param applicationHTMLPanel
     * @param formID
     * @param currentPageHTMLPanel
     * @param errorMessage
     */
    public ErrorPageHandler(final HTMLPanel applicationHTMLPanel, final String formID, final Panel currentPageHTMLPanel, final String errorMessage,
            final String elementId) {
        this(applicationHTMLPanel, formID, errorMessage, elementId);
        this.currentPageHTMLPanel = currentPageHTMLPanel;
    }

    /**
     * Constructor.
     *
     * @param applicationHTMLPanel
     * @param formID
     * @param errorMessage
     */
    public ErrorPageHandler(final HTMLPanel applicationHTMLPanel, final String formID, final String errorMessage, final String elementId) {

        domUtils = DOMUtils.getInstance();
        this.applicationHTMLPanel = applicationHTMLPanel;
        this.formID = formID;
        this.elementId = elementId;
        this.errorMessage = errorMessage;
        causeMessage = "";
    }

    /**
     * Constructor.
     *
     * @param processHTMLPanel
     * @param formID
     * @param currentPageHTMLPanel
     * @param errorMessage
     * @param throwable
     */
    public ErrorPageHandler(final HTMLPanel applicationHTMLPanel, final String formID, final Panel currentPageHTMLPanel, final String errorMessage,
            final Throwable throwable, final String elementId) {
        this(applicationHTMLPanel, formID, errorMessage, throwable, elementId);
        this.currentPageHTMLPanel = currentPageHTMLPanel;
    }

    /**
     * Constructor.
     *
     * @param applicationHTMLPanel
     * @param formID
     * @param errorMessage
     * @param throwable
     */
    public ErrorPageHandler(final HTMLPanel applicationHTMLPanel, final String formID, final String errorMessage, final Throwable throwable,
            final String elementId) {

        domUtils = DOMUtils.getInstance();
        this.applicationHTMLPanel = applicationHTMLPanel;
        this.formID = formID;
        this.elementId = elementId;
        this.errorMessage = errorMessage;
        if (throwable instanceof RPCException) {
            causeMessage = ((RPCException) throwable).getMessage();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSuccess(final ReducedHtmlTemplate result) {

        if (currentPageHTMLPanel != null) {
            applicationHTMLPanel.remove(currentPageHTMLPanel);
        }
        final HTMLPanel pageHTMLPanel = new HTMLPanel(result.getBodyContent());
        final String onloadAttributeValue = domUtils.insertPageTemplate(result.getHeadNodes(), pageHTMLPanel, result.getBodyAttributes(), applicationHTMLPanel,
                elementId);
        domUtils.insertInElement(pageHTMLPanel, ERROR_MESSAGE_ELEMENT_ID, errorMessage);
        domUtils.insertInElement(pageHTMLPanel, CAUSE_MESSAGE_ELEMENT_ID, causeMessage);
        // Hide the loading message.

        final Element loadingElement = DOM.getElementById("loading");
        if (loadingElement != null) {
            loadingElement.getStyle().setProperty("display", "none");
        }

        if (onloadAttributeValue != null) {
            domUtils.javascriptEval(onloadAttributeValue);
        }
    }

    @Override
    public void onUnhandledFailure(final Throwable t) {
        // Hide the loading message.
        DOM.getElementById("loading").getStyle().setProperty("display", "none");
        GWT.log(FormsResourceBundle.getErrors().errorTempateError());
    }
}
