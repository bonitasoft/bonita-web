/**
 * Copyright (C) 2013 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
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

import java.util.Map;

import org.bonitasoft.forms.client.i18n.FormsResourceBundle;
import org.bonitasoft.forms.client.model.FormURLComponents;
import org.bonitasoft.forms.client.model.exception.ForbiddenFormAccessException;
import org.bonitasoft.forms.client.model.exception.SessionTimeoutException;
import org.bonitasoft.forms.client.rpc.FormsServiceAsync;
import org.bonitasoft.forms.client.view.common.DOMUtils;
import org.bonitasoft.forms.client.view.common.RpcFormsServices;
import org.bonitasoft.forms.client.view.common.URLUtils;
import org.bonitasoft.forms.client.view.common.URLUtilsFactory;
import org.bonitasoft.forms.client.view.controller.ErrorPageHandler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;

public class TodoListTaskWidget extends Composite {

    private static final String NO_TASK_AVAILABLE_CSS_CLASS = "bonita_form_no_task_available_label";

    private static final String AVAILABLE_TASK_TITLE_CSS_CLASS = "bonita_form_available_task_title";

    private static final String AVAILABLE_TASK_LINK_CSS_CLASS = "bonita_form_task_link";

    /**
     * the flow panel used to display the task list
     */
    protected FlowPanel flowPanel;

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
    protected Panel currentPageHTMLPanel;

    /**
     * The form ID retrieved from the request as a String
     */
    protected String formId;

    /**
     * The parameters map of the URL
     */
    protected Map<String, Object> urlContext;

    /**
     * Utility Class form URL manipulation
     */
    protected URLUtils urlUtils = URLUtilsFactory.getInstance();

    /**
     * Utility Class form DOM manipulation
     */
    protected DOMUtils domUtils = DOMUtils.getInstance();

    /**
     * Default Constructor.
     *
     * @param applicationHTMLPanel
     * @param elementId
     * @param currentPageHTMLPanel
     * @param formId
     * @param urlContext
     */
    public TodoListTaskWidget(final HTMLPanel applicationHTMLPanel, final String elementId, final Panel currentPageHTMLPanel, final String formId,
            final Map<String, Object> urlContext) {

        flowPanel = new FlowPanel();

        this.applicationHTMLPanel = applicationHTMLPanel;
        this.currentPageHTMLPanel = currentPageHTMLPanel;
        this.formId = formId;
        this.urlContext = urlContext;
        this.elementId = elementId;

        createWidget();

        initWidget(flowPanel);
    }

    private void createWidget() {
        createFetchTaskTimer().start();
    }

    /**
     * Fetch tasks timer creation
     *
     * @return
     */
    private FetchTaskTimer createFetchTaskTimer() {
        return new FetchTaskTimer(formId, urlContext) {

            @Override
            public void onTaskFound(final FormURLComponents nextFormURL) {
                addAvailableTaskMessage();
                addTaskFound(nextFormURL);
            }

            @Override
            public void onNoTaskFound() {
                addNoTaskAvailableMessage();
            }

            @Override
            public void onTaskFetchingFailed(final Throwable caught) {
                showErrorPage(FormsResourceBundle.getErrors().nextTaskRetrievalError());
            }
        };
    }

    private void addAvailableTaskMessage() {
        final Label label = new Label(FormsResourceBundle.getMessages().aTaskIsNowAvailableMessage());
        label.setStyleName(AVAILABLE_TASK_TITLE_CSS_CLASS);
        flowPanel.add(label);
    }

    /**
     * Add task widgets to the page
     *
     * @param nextFormURL
     */
    private void addTaskFound(final FormURLComponents nextFormURL) {
        String nextTaskName = nextFormURL.getTaskName();
        if (nextTaskName == null) {
            nextTaskName = "next task";
        }
        flowPanel.add(createTaskLink(nextTaskName, nextFormURL));
    }

    private void addNoTaskAvailableMessage() {
        final Label label = new Label(FormsResourceBundle.getMessages().noTaskAvailableMessage());
        label.setStyleName(NO_TASK_AVAILABLE_CSS_CLASS);
        flowPanel.add(label);
    }

    /**
     * Create task link to assign and redirect user
     *
     * @param nextTaskName
     * @param nextFormURL
     * @return
     */
    private Label createTaskLink(final String nextTaskName, final FormURLComponents nextFormURL) {
        final Label link = new Label(nextTaskName);
        link.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                buildUrlAndRedirectToNextTaskForm(nextFormURL);
            }
        });
        link.setStyleName(AVAILABLE_TASK_LINK_CSS_CLASS);
        return link;
    }

    /**
     * @param nextFormURL
     */
    private void buildUrlAndRedirectToNextTaskForm(final FormURLComponents nextFormURL) {
        changeUrlContextSafely(nextFormURL.getUrlContext());
        getFormService().assignForm((String) urlContext.get(URLUtils.FORM_ID), urlContext, new AsyncCallback<Void>() {

            @Override
            public void onSuccess(final Void result) {
                final String url = getUrlToNextTaskForm(nextFormURL);
                if (domUtils.isPageInFrame()) {
                    urlUtils.frameRedirect(DOMUtils.DEFAULT_FORM_ELEMENT_ID, url);
                } else {
                    urlUtils.windowAssign(url);
                }
            }

            @Override
            public void onFailure(final Throwable caught) {
                try {
                    throw caught;
                } catch (final ForbiddenFormAccessException e) {
                    showErrorPage(FormsResourceBundle.getMessages().forbiddenStepReadMessage());
                } catch (final SessionTimeoutException e) {
                    handleSessionTimeout(nextFormURL);
                } catch (final Throwable e) {
                    GWT.log(e.getMessage(), e);
                    showErrorPage(FormsResourceBundle.getErrors().nextTaskRetrievalError());
                }
            }
        });
    }

    protected void handleSessionTimeout(final FormURLComponents nextFormURL) {
        final String url = urlUtils.removeURLparameters(Window.Location.getHref());
        urlUtils.parentFrameRedirect(url);
    }

    /**
     * @param nextFormURL
     *
     * @return the next task form URL
     */
    private String getUrlToNextTaskForm(final FormURLComponents nextFormURL) {
        return urlUtils.getFormRedirectionUrl(urlContext);
    }

    private void changeUrlContextSafely(final Map<String, Object> urlContextMap) {
        if (urlContextMap != null) {
            urlContext.clear();
            urlContext.putAll(urlContextMap);
        }
    }

    private FormsServiceAsync getFormService() {
        return RpcFormsServices.getFormsService();
    }

    private void showErrorPage(final String message) {
        final FormsServiceAsync formsServiceAsync = getFormService();
        formsServiceAsync.getApplicationErrorTemplate(formId, urlContext, createErrorPageHandler(message));
    }

    private ErrorPageHandler createErrorPageHandler(final String message) {
        return new ErrorPageHandler(applicationHTMLPanel, formId,
                currentPageHTMLPanel,
                message,
                elementId);
    }
}
