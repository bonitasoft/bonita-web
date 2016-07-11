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

import java.util.Map;

import org.bonitasoft.forms.client.i18n.FormsResourceBundle;
import org.bonitasoft.forms.client.model.ReducedHtmlTemplate;
import org.bonitasoft.forms.client.rpc.FormsServiceAsync;
import org.bonitasoft.forms.client.view.FormsAsyncCallback;
import org.bonitasoft.forms.client.view.common.DOMUtils;
import org.bonitasoft.forms.client.view.common.RpcFormsServices;
import org.bonitasoft.forms.client.view.widget.TodoListTaskWidget;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Panel;

/**
 * Handler allowing to display the confirmation template
 *
 * @author Anthony Birembaut
 *
 */
class ConfirmationPageHandler extends FormsAsyncCallback<ReducedHtmlTemplate> {

    /**
     * Id of the message element on the confirmation page
     */
    protected static final String CONFIRM_MESSAGE_ELEMENT_ID = "bonita_form_confirm_message";

    /**
     * Id of the element in which to insert the todo list on the confirmation page
     */
    protected static final String CONFIRM_TODOLIST_ELEMENT_ID = "bonita_form_confirm_todolist";

    /**
     * Utility Class form DOM manipulation
     */
    protected DOMUtils formsTemplateUtils;

    /**
     * forms RPC service
     */
    protected FormsServiceAsync formsServiceAsync;

    /**
     * process template panel (can be null in form only mode)
     */
    protected HTMLPanel applicationHTMLPanel;

    /**
     * Element Id
     */
    protected String elementId;

    /**
     * current page template panel
     */
    private Panel currentPageHTMLPanel;

    /**
     * Confirmation message
     */
    protected String defaultConfirmationMessage;

    /**
     * The form ID retrieved from the request as a String
     */
    protected String formId;

    /**
     * the parameters map of the URL
     */
    protected Map<String, Object> urlContext;

    /**
     * Constructor.
     *
     * @param applicationHTMLPanel
     * @param elementId
     * @param defaultConfirmationMessage
     * @param formId
     */
    public ConfirmationPageHandler(final HTMLPanel applicationHTMLPanel, final String elementId, final String defaultConfirmationMessage, final String formId,
            final Map<String, Object> urlContext) {
        formsServiceAsync = RpcFormsServices.getFormsService();
        formsTemplateUtils = DOMUtils.getInstance();
        this.applicationHTMLPanel = applicationHTMLPanel;
        this.formId = formId;
        this.urlContext = urlContext;
        this.elementId = elementId;
        this.defaultConfirmationMessage = defaultConfirmationMessage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSuccess(final ReducedHtmlTemplate result) {

        String confirmMessage = result.getDynamicMessage();
        if (confirmMessage == null) {
            confirmMessage = defaultConfirmationMessage;
        }
        if (getCurrentPageHTMLPanel() != null && applicationHTMLPanel != null) {
            applicationHTMLPanel.remove(getCurrentPageHTMLPanel());
        }
        final HTMLPanel pageHTMLPanel = new HTMLPanel(result.getBodyContent());
        final String onloadAttributeValue = formsTemplateUtils.insertPageTemplate(result.getHeadNodes(), pageHTMLPanel, result.getBodyAttributes(),
                applicationHTMLPanel, elementId);
        formsTemplateUtils.insertInElement(applicationHTMLPanel, CONFIRM_MESSAGE_ELEMENT_ID, confirmMessage);
        final TodoListTaskWidget taskListWidget = new TodoListTaskWidget(applicationHTMLPanel, elementId, getCurrentPageHTMLPanel(), formId, urlContext);
        pageHTMLPanel.add(taskListWidget, CONFIRM_TODOLIST_ELEMENT_ID);
        formsTemplateUtils.hideLoading();
        if (onloadAttributeValue != null) {
            formsTemplateUtils.javascriptEval(onloadAttributeValue);
        }
    }

    /**
     * @return the currentPageHTMLPanel
     */
    public Panel getCurrentPageHTMLPanel() {
        return currentPageHTMLPanel;
    }

    /**
     * @param currentPageHTMLPanel
     *            the currentPageHTMLPanel to set
     */
    public ConfirmationPageHandler setCurrentPageHTMLPanel(final Panel currentPageHTMLPanel) {
        this.currentPageHTMLPanel = currentPageHTMLPanel;
        return this;
    }

    @Override
    public void onUnhandledFailure(final Throwable t) {
        final String errorMessage = FormsResourceBundle.getErrors().confirmationTempateError();
        formsServiceAsync.getApplicationErrorTemplate(formId, urlContext, new ErrorPageHandler(applicationHTMLPanel, formId, getCurrentPageHTMLPanel(),
                errorMessage, t, elementId));
    }
}
