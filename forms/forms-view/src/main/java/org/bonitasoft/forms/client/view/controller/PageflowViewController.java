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

import java.util.Map;

import com.google.gwt.user.client.Timer;
import org.bonitasoft.forms.client.i18n.FormsResourceBundle;
import org.bonitasoft.forms.client.model.ReducedFormPage;
import org.bonitasoft.forms.client.model.ReducedHtmlTemplate;
import org.bonitasoft.forms.client.model.exception.AbortedFormException;
import org.bonitasoft.forms.client.model.exception.CanceledFormException;
import org.bonitasoft.forms.client.model.exception.ForbiddenFormAccessException;
import org.bonitasoft.forms.client.model.exception.FormAlreadySubmittedException;
import org.bonitasoft.forms.client.model.exception.FormInErrorException;
import org.bonitasoft.forms.client.model.exception.IllegalActivityTypeException;
import org.bonitasoft.forms.client.model.exception.MigrationProductVersionNotIdenticalException;
import org.bonitasoft.forms.client.model.exception.SkippedFormException;
import org.bonitasoft.forms.client.rpc.FormsServiceAsync;
import org.bonitasoft.forms.client.view.FormsAsyncCallback;
import org.bonitasoft.forms.client.view.common.DOMUtils;
import org.bonitasoft.forms.client.view.common.RpcFormsServices;
import org.bonitasoft.forms.client.view.common.URLUtils;
import org.bonitasoft.forms.client.view.common.URLUtilsFactory;
import org.bonitasoft.web.rest.model.user.User;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Controller dealing with Task form controls before display
 * 
 * @author Ruiheng Fan
 */
public class PageflowViewController {

    /**
     * forms RPC service
     */
    protected FormsServiceAsync formsServiceAsync;

    /**
     * Pages view controller (handles the page flow)
     */
    protected FormPagesViewController formPagesViewController;

    /**
     * Utility Class form DOM manipulation
     */
    protected DOMUtils domUtils = DOMUtils.getInstance();

    /**
     * Utility Class form URL manipulation
     */
    protected URLUtils urlUtils = URLUtilsFactory.getInstance();

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
     * The formID UUID retrieved from the request as a String
     */
    protected String formID;

    /**
     * Application template panel (can be null in form only mode)
     */
    protected HTMLPanel applicationHTMLPanel;

    /**
     * The logged in user
     */
    protected User user;

    /**
     * Handler allowing to retrieve the first page
     */
    protected FirstPageHandler firstPageHandler = new FirstPageHandler();

    /**
     * The context of URL parameters
     */
    protected Map<String, Object> urlContext;

    /**
     * Id of the element in which to insert the page
     */
    protected String elementId;

    /**
     * Constructor
     * 
     * @param formID
     * @param urlContext
     * @param user
     * @param applicationHTMLPanel
     */
    public PageflowViewController(final String formID, final Map<String, Object> urlContext, final User user, final String elementId,
            final HTMLPanel applicationHTMLPanel) {
        this.formID = formID;
        this.urlContext = urlContext;
        this.user = user;
        this.elementId = elementId;
        this.applicationHTMLPanel = applicationHTMLPanel;

        formsServiceAsync = RpcFormsServices.getFormsService();
    }

    /**
     * create the view for the form
     */
    public void createForm() {
        formsServiceAsync.getFormFirstPage(formID, urlContext, firstPageHandler);
    }

    /**
     * Handler allowing to retrieve the page list
     */
    protected class FirstPageHandler extends FormsAsyncCallback<ReducedFormPage> {

        /**
         * {@inheritDoc}
         */
        @Override
        public void onSuccess(final ReducedFormPage firstPage) {

            if (firstPage != null) {
                try {
                    RequestBuilder theRequestBuilder;
                    final String theURL = urlUtils.buildLayoutURL(firstPage.getPageTemplate().getBodyContentId(),
                            (String) urlContext.get(URLUtils.FORM_ID), (String) urlContext.get(URLUtils.TASK_ID_PARAM), true);
                    GWT.log("Calling the Form Layout Download Servlet with query: " + theURL);
                    theRequestBuilder = new RequestBuilder(RequestBuilder.GET, theURL);
                    theRequestBuilder.setCallback(new RequestCallback() {

                        @Override
                        public void onError(final Request aRequest, final Throwable anException) {
                            final String errorMessage = FormsResourceBundle.getErrors().applicationConfigRetrievalError();
                            formsServiceAsync.getApplicationErrorTemplate(firstPage.getPageId(), urlContext, new ErrorPageHandler(null, firstPage.getPageId(),
                                    errorMessage, anException, elementId));
                        }

                        @Override
                        public void onResponseReceived(final Request request, final Response response) {

                            firstPage.getPageTemplate().setBodyContent(response.getText());
                            formPagesViewController = FormViewControllerFactory.getFormPagesViewController(formID, urlContext, firstPage, applicationHTMLPanel,
                                    user, elementId);
                            formPagesViewController.setMandatoryFieldSymbol(mandatoryFieldSymbol);
                            formPagesViewController.setMandatoryFieldLabel(mandatoryFieldLabel);
                            formPagesViewController.setMandatoryFieldClasses(mandatoryFieldClasses);
                            formPagesViewController.displayPage(0);
                        }
                    });
                    theRequestBuilder.send();
                } catch (final Exception e) {
                    Window.alert("Error while trying to query the form layout :" + e.getMessage());
                }
            } else {
                final String processUUIDStr = (String) urlContext.get(URLUtils.PROCESS_ID_PARAM);
                final String instanceUUIDStr = (String) urlContext.get(URLUtils.INSTANCE_ID_PARAM);
                if (processUUIDStr != null) {
                    final String autoInstantiate = (String) urlContext.get(URLUtils.AUTO_INSTANTIATE);
                    final ConfirmationPageHandler confirmationPageHandler = createConfirmationPageHandler();
                    final FormTerminationHandler formTerminationHandler = new FormTerminationHandler(confirmationPageHandler);
                    // if the parameter autoInstanciate is set explicitly to false, the we skip the form
                    if (!Boolean.FALSE.toString().equals(autoInstantiate)) {
                        formsServiceAsync.skipForm(formID, urlContext, formTerminationHandler);
                    } else {
                        final FlowPanel buttonContainer = new FlowPanel();
                        confirmationPageHandler.setCurrentPageHTMLPanel(buttonContainer);
                        buttonContainer.setStyleName("bonita_form_button_container");
                        buttonContainer.add(createStartProcessInstanceButton(formTerminationHandler));
                        addStartProcessInstanceContainer(buttonContainer);
                    }
                } else if (instanceUUIDStr != null) {
                    final String errorMessage = FormsResourceBundle.getErrors().nothingToDisplay();
                    formsServiceAsync.getApplicationErrorTemplate(formID, urlContext, new ErrorPageHandler(applicationHTMLPanel, formID, errorMessage,
                            elementId));
                } else {
                    formsServiceAsync.skipForm(formID, urlContext, new FormTerminationHandler(createConfirmationPageHandler()));
                }
            }
        }

        @Override
        public void onUnhandledFailure(final Throwable caught) {

            try {
                throw caught;
            } catch (final ForbiddenFormAccessException e) {
                String errorMessage;
                if (urlContext.containsKey(URLUtils.PROCESS_ID_PARAM)) {
                    errorMessage = FormsResourceBundle.getMessages().forbiddenProcessStartMessage();
                } else {
                    errorMessage = FormsResourceBundle.getMessages().forbiddenStepReadMessage();
                }
                formsServiceAsync.getApplicationErrorTemplate(formID, urlContext, new ErrorPageHandler(applicationHTMLPanel, formID, errorMessage, elementId));
            } catch (final MigrationProductVersionNotIdenticalException e) {
                final String errorMessage = FormsResourceBundle.getMessages().migrationProductVersionMessage();
                formsServiceAsync.getApplicationErrorTemplate(formID, urlContext, new ErrorPageHandler(applicationHTMLPanel, formID, errorMessage, elementId));
            } catch (final CanceledFormException e) {
                final String errorMessage = FormsResourceBundle.getMessages().cancelledTaskMessage();
                formsServiceAsync.getApplicationErrorTemplate(formID, urlContext, new ErrorPageHandler(applicationHTMLPanel, formID, errorMessage, elementId));
                // } catch (SuspendedFormException e) {
                // final String errorMessage = FormsResourceBundle.getMessages().suspendedTaskMessage();
                // formsServiceAsync.getApplicationErrorTemplate(formID, urlContext, new ErrorPageHandler(applicationHTMLPanel, formID, errorMessage,
                // elementId));
            } catch (final AbortedFormException e) {
                final String errorMessage = FormsResourceBundle.getMessages().abortedFormMessage();
                formsServiceAsync.getApplicationErrorTemplate(formID, urlContext, new ErrorPageHandler(applicationHTMLPanel, formID, errorMessage, elementId));
            } catch (final FormInErrorException e) {
                final String errorMessage = FormsResourceBundle.getMessages().errorTaskMessage();
                formsServiceAsync.getApplicationErrorTemplate(formID, urlContext, new ErrorPageHandler(applicationHTMLPanel, formID, errorMessage, elementId));
            } catch (final SkippedFormException e) {
                final String errorMessage = FormsResourceBundle.getMessages().skippedFormMessage();
                formsServiceAsync.getApplicationErrorTemplate(formID, urlContext, new ErrorPageHandler(applicationHTMLPanel, formID, errorMessage, elementId));
            } catch (final FormAlreadySubmittedException e) {
                final String errorMessage = FormsResourceBundle.getErrors().formAlreadySubmittedError();
                formsServiceAsync.getApplicationErrorTemplate(formID, urlContext, new ErrorPageHandler(applicationHTMLPanel, formID, errorMessage, elementId));
            } catch (final Throwable t) {
                final String errorMessage = FormsResourceBundle.getErrors().pageListRetrievalError();
                formsServiceAsync.getApplicationErrorTemplate(formID, urlContext, new ErrorPageHandler(applicationHTMLPanel, formID, errorMessage, elementId));
            }

        }
    }

    protected Button createStartProcessInstanceButton(final FormTerminationHandler formTerminationHandler) {
        final Button startInstanceButton = new Button(FormsResourceBundle.getMessages().caseStartButtonLabel());
        startInstanceButton.setStyleName("bonita_form_button");
        startInstanceButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                startInstanceButton.setEnabled(false);
                formsServiceAsync.skipForm(formID, urlContext, formTerminationHandler);
            }
        });
        return startInstanceButton;
    }

    protected void addStartProcessInstanceContainer(final FlowPanel buttonContainer) {
        domUtils.hideLoading();
        if (applicationHTMLPanel != null) {
            applicationHTMLPanel.add(buttonContainer, DOMUtils.DEFAULT_FORM_ELEMENT_ID);
        } else {
            RootPanel.get(DOMUtils.STATIC_CONTENT_ELEMENT_ID).add(buttonContainer);
        }
    }

    /**
     * Handler to deal with what happens after a form has been submitted or skipped
     */
    protected class FormTerminationHandler extends FormsAsyncCallback<Map<String, Object>> {

        private ConfirmationPageHandler confirmationPageHandler;

        public FormTerminationHandler(final ConfirmationPageHandler confirmationPageHandler) {
            setConfirmationPageHandler(confirmationPageHandler);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onSuccess(final Map<String, Object> newContext) {
            urlContext.putAll(newContext);
            redirectToConfirmationPage(getConfirmationPageHandler());
        }

        /**
         * @return the confirmationPageHandler
         */
        public ConfirmationPageHandler getConfirmationPageHandler() {
            return confirmationPageHandler;
        }

        /**
         * @param confirmationPageHandler
         *            the confirmationPageHandler to set
         */
        public void setConfirmationPageHandler(final ConfirmationPageHandler confirmationPageHandler) {
            this.confirmationPageHandler = confirmationPageHandler;
        }

        @Override
        public void onUnhandledFailure(final Throwable caught) {
            try {
                throw caught;
            } catch (final IllegalActivityTypeException t) {
                final String errorMessage = FormsResourceBundle.getErrors().taskFormSkippedError();
                formsServiceAsync.getApplicationErrorTemplate(formID, urlContext, new ErrorPageHandler(applicationHTMLPanel, formID, errorMessage, elementId));
            } catch (final FormAlreadySubmittedException t) {
                final String errorMessage = FormsResourceBundle.getErrors().formAlreadySubmittedOrCancelledError();
                formsServiceAsync.getApplicationErrorTemplate(formID, urlContext, new ErrorPageHandler(applicationHTMLPanel, formID, errorMessage, elementId));
            } catch (final Throwable t) {
                final String errorMessage = FormsResourceBundle.getErrors().taskExecutionError();
                formsServiceAsync.getApplicationErrorTemplate(formID, urlContext,
                        new ErrorPageHandler(applicationHTMLPanel, formID, errorMessage, t, elementId));
            }
        }
    }

    protected class GetTokenAsyncCallback implements AsyncCallback<String> {

        protected String applicationURL;

        protected Map<String, Object> urlContext;

        public GetTokenAsyncCallback(final String applicationURL, final Map<String, Object> urlContext) {
            this.applicationURL = applicationURL;
            this.urlContext = urlContext;
        }

        @Override
        public void onSuccess(final String temporaryToken) {
            urlContext.put(URLUtils.USER_CREDENTIALS_PARAM, temporaryToken);
            final String url = urlUtils.getFormRedirectionUrl(applicationURL, urlContext);
            if (domUtils.isPageInFrame()) {
                urlUtils.frameRedirect(DOMUtils.DEFAULT_FORM_ELEMENT_ID, url);
            } else {
                urlUtils.windowRedirect(url);
            }
        }

        @Override
        public void onFailure(final Throwable t) {
            final String url = urlUtils.getFormRedirectionUrl(applicationURL, urlContext);
            if (domUtils.isPageInFrame()) {
                urlUtils.frameRedirect(DOMUtils.DEFAULT_FORM_ELEMENT_ID, url);
            } else {
                urlUtils.windowRedirect(url);
            }
        }
    }

    public void setMandatoryFieldSymbol(final String mandatoryFieldSymbol) {
        this.mandatoryFieldSymbol = mandatoryFieldSymbol;
    }

    public void setMandatoryFieldClasses(final String mandatoryFieldClasses) {
        this.mandatoryFieldClasses = mandatoryFieldClasses;
    }

    public void setMandatoryFieldLabel(final String mandatoryFieldLabel) {
        this.mandatoryFieldLabel = mandatoryFieldLabel;
    }

    protected void redirectToConfirmationPage(final ConfirmationPageHandler confirmationPageHandler) {
        formsServiceAsync.getFormConfirmationTemplate(formID, urlContext, confirmationPageHandler);
    }

    /**
     * If the page is containned in a form, resize the frame to fit the page height
     */
    protected void resizeFrame() {

        final Timer timer = new Timer() {

            @Override
            public void run() {
                if (domUtils.isPageInFrame()) {
                }
            }
        };
        timer.schedule(300);
    }

    protected ConfirmationPageHandler createConfirmationPageHandler() {
        return new ConfirmationPageHandler(applicationHTMLPanel, elementId, getDefaultConfirmationMessage(), formID, urlContext) {
            @Override
            public void onSuccess(final ReducedHtmlTemplate result) {
                super.onSuccess(result);
                resizeFrame();
            }
        };
    }

    private String getDefaultConfirmationMessage() {
        String confirmationMessage = null;
        if(urlContext.containsKey(URLUtils.INSTANCE_ID_PARAM)) {
            confirmationMessage = FormsResourceBundle.getMessages().instanceSubmissionConfirmationMessage((String)urlContext.get(URLUtils.INSTANCE_ID_PARAM));
        } else {
            confirmationMessage = FormsResourceBundle.getMessages().submissionConfirmationMessage();
        }
        return confirmationMessage;
    }

}
