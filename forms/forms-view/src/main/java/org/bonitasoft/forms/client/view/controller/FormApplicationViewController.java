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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bonitasoft.forms.client.i18n.FormsResourceBundle;
import org.bonitasoft.forms.client.model.ReducedApplicationConfig;
import org.bonitasoft.forms.client.model.ReducedHtmlTemplate;
import org.bonitasoft.forms.client.model.exception.ForbiddenApplicationAccessException;
import org.bonitasoft.forms.client.model.exception.MigrationProductVersionNotIdenticalException;
import org.bonitasoft.forms.client.rpc.FormsServiceAsync;
import org.bonitasoft.forms.client.view.FormsAsyncCallback;
import org.bonitasoft.forms.client.view.common.DOMUtils;
import org.bonitasoft.forms.client.view.common.RpcFormsServices;
import org.bonitasoft.forms.client.view.common.URLUtils;
import org.bonitasoft.forms.client.view.common.URLUtilsFactory;
import org.bonitasoft.forms.client.view.widget.UserLogoutWidget;
import org.bonitasoft.web.rest.model.user.User;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

/**
 * View controller of the form module
 * 
 * @author Anthony Birembaut
 */
public class FormApplicationViewController {

    protected static final String DEFAULT_USER_XP_URL = GWT.getModuleBaseURL() + "homepage";

    /**
     * The PICTURE_PLACEHOLDER
     */
    protected static final String PICTURE_PLACEHOLDER = "images/cleardot.gif";

    /**
     * Id of the element of the application template in witch the title has to be injected
     */
    protected static final String APPLICATION_LABEL_ELEMENT_ID = "bonita_process_label";

    /**
     * Id of the element of the process template in witch the logout widget has to be injected
     */
    protected static final String LOGOUT_WIDGET_ELEMENT_ID = "bonita_logout_box";

    /**
     * Id of the element of the process template in witch the logout link has to be injected
     */
    protected static final String LOGOUT_BUTTON_ELEMENT_ID = "bonita_logout_button";

    /**
     * Id of the element of the process template in witch the username has to be injected
     */
    protected static final String USERNAME_ELEMENT_ID = "bonita_username";

    /**
     * Id of the element of the process template in witch the refresh button has to be injected
     */
    protected static final String REFRESH_BUTTON_ELEMENT_ID = "bonita_refresh_button";

    /**
     * Id of the element of the process template in witch the open to user experience button has to be injected
     */
    protected static final String OPEN_USER_XP_ELEMENT_ID = "bonita_user_xp_link";

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
     * Handler allowing to diplay the process forms config after the RPC call to retrieve its definition. this instance deals
     * with the process template display
     */
    protected ApplicationConfigHandler fullApplicationHandler = new ApplicationConfigHandler(true);

    /**
     * Handler allowing to diplay the process forms config after the RPC call to retrieve its definition this instance doesn't
     * include template display
     */
    protected ApplicationConfigHandler lightApplicationConfigHandler = new ApplicationConfigHandler(false);

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
     * The logged in user
     */
    protected User user;

    /**
     * Id of the element in which to insert the page
     */
    protected String elementId;

    /**
     * The form ID
     */
    protected String formId;

    /**
     * The context of URL parameters
     */
    protected Map<String, Object> urlContext;

    /**
     * Constructor.
     * 
     * @param formId
     * @param urlContext
     * @param user
     */
    public FormApplicationViewController(final String formId, final Map<String, Object> urlContext, final User user) {
        this.formId = formId;
        this.urlContext = urlContext;
        this.user = user;

        formsServiceAsync = RpcFormsServices.getFormsService();
    }

    /**
     * create the view for the process template and the form page template
     * 
     * @param formContainerId
     *            id of the element in which the form has to be placed (if null the form will be insterted at the
     *            end of the body)
     */
    public void createInitialView(final String formContainerId) {

        createView(formContainerId, true);
    }

    /**
     * create the view for the form page at the end of the body
     */
    public void createFormInitialView() {

        createFormInitialView(null);
    }

    /**
     * create the view for the form page in the specified node
     * 
     * @param formContainerId
     *            id of the element in which the form has to be placed (if null the form will be insterted at the
     *            end of the body)
     */
    public void createFormInitialView(final String formContainerId) {

        createView(formContainerId, false);
    }

    /**
     * create the view for the form page in the specified node
     * 
     * @param formContainerId
     *            id of the element in which the form has to be placed (if null the form will be insterted at the
     *            end of the body)
     * @param includeApplicationTemplate
     *            if true, include the process template otherwise just display the form
     */
    protected void createView(final String formContainerId, final boolean includeApplicationTemplate) {
        elementId = formContainerId;
        ApplicationConfigHandler applicationConfigHandler = null;
        if (includeApplicationTemplate) {
            applicationConfigHandler = fullApplicationHandler;
        } else {
            applicationConfigHandler = lightApplicationConfigHandler;
        }
        formsServiceAsync.getApplicationConfig(formId, urlContext, includeApplicationTemplate, applicationConfigHandler);
    }

    /**
     * Handler allowing to display the application template
     */
    protected class ApplicationConfigHandler extends FormsAsyncCallback<ReducedApplicationConfig> {

        protected boolean includeApplicationTemplate;

        /**
         * Constructor
         * 
         * @param includeProcessTemplate
         */
        public ApplicationConfigHandler(final boolean includeProcessTemplate) {

            includeApplicationTemplate = includeProcessTemplate;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onSuccess(final ReducedApplicationConfig applicationConfig) {
            try {
                if (includeApplicationTemplate) {
                    RequestBuilder theRequestBuilder;
                    final String theURL = urlUtils.buildLayoutURL(applicationConfig.getApplicationLayout().getBodyContentId(),
                            (String) urlContext.get(URLUtils.FORM_ID), (String) urlContext.get(URLUtils.TASK_ID_PARAM), false);
                    GWT.log("Calling the Form Layout Download Servlet with query: " + theURL);
                    theRequestBuilder = new RequestBuilder(RequestBuilder.GET, theURL);
                    theRequestBuilder.setCallback(new RequestCallback() {

                        @Override
                        public void onError(final Request aRequest, final Throwable exception) {
                            final String errorMessage = FormsResourceBundle.getErrors().applicationConfigRetrievalError();
                            formsServiceAsync.getApplicationErrorTemplate(formId, urlContext, new ErrorPageHandler(null, formId, errorMessage, exception,
                                    elementId));
                        }

                        @Override
                        public void onResponseReceived(final Request aRequest, final Response aResponse) {
                            HTMLPanel applicationHTMLPanel = null;

                            final ReducedHtmlTemplate applicationTemplate = applicationConfig.getApplicationLayout();
                            applicationTemplate.setBodyContent(aResponse.getText());
                            applicationHTMLPanel = new HTMLPanel(applicationTemplate.getBodyContent());
                            final String onloadAttributeValue = domUtils.insertApplicationTemplate(applicationTemplate.getHeadNodes(), applicationHTMLPanel,
                                    applicationTemplate.getBodyAttributes());

                            if (applicationConfig.getApplicationLabel() != null && DOM.getElementById(APPLICATION_LABEL_ELEMENT_ID) != null) {
                                domUtils.insertInElement(applicationHTMLPanel, APPLICATION_LABEL_ELEMENT_ID, applicationConfig.getApplicationLabel());
                            }
                            if (DOM.getElementById(LOGOUT_WIDGET_ELEMENT_ID) != null) {
                                final UserLogoutWidget logoutWidget = new UserLogoutWidget(user, urlContext);
                                applicationHTMLPanel.add(logoutWidget, LOGOUT_WIDGET_ELEMENT_ID);
                            }
                            if (DOM.getElementById(LOGOUT_BUTTON_ELEMENT_ID) != null) {
                                Anchor logoutLink = null;
                                if (user.isAnonymous()) {
                                    logoutLink = new Anchor(FormsResourceBundle.getMessages().loginButtonLabel());
                                } else {
                                    logoutLink = new Anchor(FormsResourceBundle.getMessages().logoutButtonLabel());
                                }
                                logoutLink.setStyleName("bonita_logout_label");

                                final List<String> paramsToRemove = new ArrayList<String>();
                                paramsToRemove.add(URLUtils.LOCALE_PARAM);
                                final List<String> hashParamsToRemove = new ArrayList<String>();

                                if (user.isAutoLogin()) {
                                    hashParamsToRemove.add(URLUtils.AUTO_LOGIN_PARAM);
                                } else {
                                    hashParamsToRemove.add(URLUtils.USER_CREDENTIALS_PARAM);
                                }

                                final Map<String, String> hashParamsToAdd = new HashMap<String, String>();
                                hashParamsToAdd.put(URLUtils.TODOLIST_PARAM, "true");
                                hashParamsToAdd.put(URLUtils.VIEW_MODE_PARAM, "app");
                                final String tenant = Window.Location.getParameter(URLUtils.TENANT_PARAM);
                                final String theRedirectURL = urlUtils.rebuildUrl(paramsToRemove, null, hashParamsToRemove, hashParamsToAdd);

                                // relative path of the logout service regarding web.xml configuration
                                String theURL = GWT.getModuleBaseURL() + "../logoutservice?" + URLUtils.REDIRECT_URL_PARAM + "=";
                                try {
                                    theURL += URL.encodeQueryString(theRedirectURL);
                                } catch (final Exception e) {
                                    Window.alert("Unable to redirect to login page: Invalid URL");
                                    theURL += GWT.getModuleBaseURL();
                                }
                                if (tenant != null) {
                                    theURL += "&" + URLUtils.TENANT_PARAM + "=" + tenant;
                                }
                                logoutLink.setHref(theURL);
                                applicationHTMLPanel.add(logoutLink, LOGOUT_BUTTON_ELEMENT_ID);
                            }
                            if (DOM.getElementById(USERNAME_ELEMENT_ID) != null) {
                                Label usernameLabel = null;
                                if (user.isAnonymous()) {
                                    usernameLabel = new Label("");
                                } else {
                                    usernameLabel = new Label(user.getUsername());
                                }
                                usernameLabel.setStyleName("bonita_username_label");
                                applicationHTMLPanel.add(usernameLabel, USERNAME_ELEMENT_ID);
                            }
                            if (DOM.getElementById(REFRESH_BUTTON_ELEMENT_ID) != null) {
                                final Label refreshButton = new Label(FormsResourceBundle.getMessages().refreshButtonLabel());
                                refreshButton.addClickHandler(new ClickHandler() {

                                    @Override
                                    public void onClick(final ClickEvent event) {
                                        Window.Location.reload();
                                    }
                                });
                                applicationHTMLPanel.add(refreshButton, REFRESH_BUTTON_ELEMENT_ID);
                            }
                            if (DOM.getElementById(OPEN_USER_XP_ELEMENT_ID) != null && !user.isAutoLogin()) {
                                final Label userXPLabel = new Label(FormsResourceBundle.getMessages().openUserXPButtonLabel());
                                userXPLabel.setStyleName("bonita_user_xp_label");
                                final Image userXPIcon = new Image(PICTURE_PLACEHOLDER);
                                userXPIcon.setStyleName("bonita_user_xp_icon");
                                userXPIcon.setTitle(FormsResourceBundle.getMessages().openUserXPButtonTitle());
                                userXPIcon.addClickHandler(new ClickHandler() {

                                    @Override
                                    public void onClick(final ClickEvent event) {

                                        String userXPURL = applicationConfig.getUserXPURL();
                                        if (userXPURL == null) {
                                            userXPURL = DEFAULT_USER_XP_URL;
                                        }
                                        if (user.useCredentialTransmission()) {
                                            formsServiceAsync.generateTemporaryToken(new GenerateTemporaryTokenHandler(userXPURL));
                                        } else {
                                            urlUtils.windowAssign(userXPURL + "#?" + URLUtils.CONSOLE_LOCALE_PARAM + "=" + urlUtils.getLocale());
                                        }
                                    }
                                });
                                applicationHTMLPanel.add(userXPLabel, OPEN_USER_XP_ELEMENT_ID);
                                applicationHTMLPanel.add(userXPIcon, OPEN_USER_XP_ELEMENT_ID);
                            }
                            if (onloadAttributeValue != null) {
                                domUtils.javascriptEval(onloadAttributeValue);
                            }
                            buildPageFlow(applicationConfig, applicationHTMLPanel);
                        }
                    });
                    theRequestBuilder.send();
                } else {
                    buildPageFlow(applicationConfig, null);
                }

            } catch (final Exception e) {
                Window.alert("Error while trying to query the form layout :" + e.getMessage());
            }
        }

        @Override
        public void onUnhandledFailure(final Throwable caught) {
            try {
                throw caught;
            } catch (final ForbiddenApplicationAccessException e) {
                formsServiceAsync.logout(new AsyncCallback<Void>() {

                    @Override
                    public void onFailure(final Throwable t) {
                        urlUtils.showLoginView();
                    }

                    @Override
                    public void onSuccess(final Void result) {
                        Window.alert(FormsResourceBundle.getMessages().forbiddenApplicationReadMessage());
                        urlUtils.showLoginView();
                    }
                });
            } catch (final MigrationProductVersionNotIdenticalException e) {
                final String errorMessage = FormsResourceBundle.getMessages().migrationProductVersionMessage();
                formsServiceAsync.getApplicationErrorTemplate(formId, urlContext, new ErrorPageHandler(null, formId, errorMessage, elementId));
            } catch (final Throwable t) {
                final String errorMessage = FormsResourceBundle.getErrors().applicationConfigRetrievalError();
                formsServiceAsync.getApplicationErrorTemplate(formId, urlContext, new ErrorPageHandler(null, formId, errorMessage, t, elementId));
            }
        }
    }

    void buildPageFlow(final ReducedApplicationConfig applicationConfig, final HTMLPanel applicationHTMLPanel) {
        mandatoryFieldSymbol = applicationConfig.getMandatorySymbol();
        mandatoryFieldLabel = applicationConfig.getMandatoryLabel();
        mandatoryFieldClasses = applicationConfig.getMandatoryStyle();

        final PageflowViewController formPagesViewController = FormViewControllerFactory.getPageflowViewController(formId, urlContext, user, elementId,
                applicationHTMLPanel);
        formPagesViewController.setMandatoryFieldSymbol(mandatoryFieldSymbol);
        formPagesViewController.setMandatoryFieldLabel(mandatoryFieldLabel);
        formPagesViewController.setMandatoryFieldClasses(mandatoryFieldClasses);

        if (formId != null) {
            formPagesViewController.createForm();
        } else {
            final String errorMessage = FormsResourceBundle.getMessages().inboxEmptyMessage();
            formsServiceAsync.getApplicationErrorTemplate(formId, urlContext, new ErrorPageHandler(applicationHTMLPanel, formId, errorMessage, elementId));
        }
    }

    /**
     * Handler allowing to get a temporary token to access the user XP
     */
    protected class GenerateTemporaryTokenHandler implements AsyncCallback<String> {

        protected String userXPURL;

        /**
         * Constructor
         * 
         * @param userXPURL
         */
        public GenerateTemporaryTokenHandler(final String userXPURL) {
            super();
            this.userXPURL = userXPURL;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onSuccess(final String temporaryToken) {
            urlUtils.windowAssign(userXPURL + "?" + URLUtils.USER_CREDENTIALS_PARAM + "=" + temporaryToken + "#?" + URLUtils.CONSOLE_LOCALE_PARAM + "="
                    + urlUtils.getLocale());
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onFailure(final Throwable t) {
            urlUtils.windowAssign(userXPURL + "#?" + URLUtils.CONSOLE_LOCALE_PARAM + "=" + urlUtils.getLocale());
        }

    }

}
