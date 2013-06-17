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
package org.bonitasoft.forms.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bonitasoft.forms.client.rpc.FormsServiceAsync;
import org.bonitasoft.forms.client.view.FormsAsyncCallback;
import org.bonitasoft.forms.client.view.common.DOMUtils;
import org.bonitasoft.forms.client.view.common.RpcFormsServices;
import org.bonitasoft.forms.client.view.common.URLUtils;
import org.bonitasoft.forms.client.view.common.URLUtilsFactory;
import org.bonitasoft.forms.client.view.controller.FormViewControllerFactory;
import org.bonitasoft.web.rest.api.model.user.User;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 * 
 * @author Anthony Birembaut
 */
public class BonitaApplication implements EntryPoint {

    /**
     * forms RPC service
     */
    protected FormsServiceAsync formsServiceAsync;

    /**
     * Utility Class form URL manipulation
     */
    protected URLUtils urlUtils = URLUtilsFactory.getInstance();

    /**
     * Utility Class form DOM manipulation
     */
    protected DOMUtils domUtils = DOMUtils.getInstance();

    /**
     * the user temporary identity token
     */
    protected String temporaryToken;

    /**
     * Indicates the application mode (form or full)
     */
    protected String applicationMode;

    /**
     * Handler allowing to retrieve the logged in user
     */
    protected UserHandler userHandler = new UserHandler();

    /**
     * The form ID
     */
    protected String formID;

    /**
     * The URL context
     */
    protected Map<String, Object> urlContext;

    /**
     * This is the entry point method.
     */
    @Override
    public void onModuleLoad() {

        GWT.setUncaughtExceptionHandler(new ApplicationUncaughtExceptionHandler());

        formsServiceAsync = RpcFormsServices.getFormsService();

        final String historyToken = History.getToken();
        final Map<String, String> hashParameters = urlUtils.getHashParameters(historyToken);
        parseHashParameters(hashParameters);

        final String locale = urlUtils.getLocale();
        urlUtils.saveLocale(locale);

        formsServiceAsync.getLoggedInUser(userHandler);
    }

    /**
     * Parses the hash parameters
     * 
     * @param hashParameters
     */
    protected void parseHashParameters(final Map<String, String> hashParameters) {

        formID = urlUtils.getFormID();

        urlContext = urlUtils.getHashParameters();

        applicationMode = (String) urlContext.get(URLUtils.VIEW_MODE_PARAM);
        if (applicationMode == null || applicationMode.length() == 0) {
            applicationMode = URLUtils.FULL_FORM_APPLICATION_MODE;
        }
    }

    /**
     * Handler allowing to know if whether a user is already logged in or not
     */
    protected class UserHandler extends FormsAsyncCallback<User> {

        /**
         * {@inheritDoc}
         */
        @Override
        public void onSuccess(final User user) {
            handleUser(user);
        }

        @Override
        public void onUnhandledFailure(Throwable caught) {
            urlUtils.showLoginView();     
        }
    }

    /**
     * Performs the post-login operations
     * 
     * @param user
     */
    private void handleUser(final User user) {
        if (user != null) {
            // the user is already logged in
            if (temporaryToken != null) {
                final List<String> paramsToRemove = new ArrayList<String>();
                paramsToRemove.add(URLUtils.USER_CREDENTIALS_PARAM);
                urlContext.remove(URLUtils.USER_CREDENTIALS_PARAM);
                Window.Location.replace(urlUtils.rebuildUrl(paramsToRemove, null, null, null));
            }
            History.addValueChangeHandler(new ValueChangeHandler<String>() {

                @Override
                public void onValueChange(final ValueChangeEvent<String> event) {
                    final String newHistoryToken = event.getValue();
                    parseHashParameters(urlUtils.getHashParameters(newHistoryToken));
                    createApplicationView(user);
                }
            });
            final boolean isTodolist = Boolean.valueOf((String) urlContext.get(URLUtils.TODOLIST_PARAM));
            if (isTodolist) {
                final String themeName = Window.Location.getParameter(URLUtils.THEME);
                if (themeName != null) {
                    urlContext.put(URLUtils.THEME, themeName);
                }
                final GetAnyTodolistFormHandler getAnyTodolistFormHandler = new GetAnyTodolistFormHandler();
                RpcFormsServices.getFormsService().getAnyTodoListForm(urlContext, getAnyTodolistFormHandler);
            } else {
                createApplicationView(user);
            }
        } else {
            urlUtils.showLoginView();
        }
    }

    /**
     * Creates the process application view
     * 
     * @param user
     */
    protected void createApplicationView(final User user) {
        if (URLUtils.FULL_FORM_APPLICATION_MODE.equals(applicationMode)) {
            FormViewControllerFactory.getFormApplicationViewController(formID, urlContext, user).createInitialView(DOMUtils.DEFAULT_FORM_ELEMENT_ID);
        } else {
            FormViewControllerFactory.getFormApplicationViewController(formID, urlContext, user).createFormInitialView();
        }
    }

    /**
     * Get any todolist Form URL
     */
    protected final class GetAnyTodolistFormHandler extends FormsAsyncCallback<Map<String, Object>> {

        public void onSuccess(final Map<String, Object> newUrlContext) {
            final String hash = urlUtils.getFormRedirectionHash(newUrlContext);
            History.newItem(hash);
        }

        @Override
        public void onUnhandledFailure(Throwable caught) {
            GWT.log("Unable to get any todolist form URL", caught);
            final List<String> paramsToRemove = new ArrayList<String>();
            paramsToRemove.add(URLUtils.THEME);
            final List<String> hashParamsToRemove = new ArrayList<String>();
            hashParamsToRemove.add(URLUtils.VIEW_MODE_PARAM);
            hashParamsToRemove.add(URLUtils.TODOLIST_PARAM);
            final String urlString = urlUtils.rebuildUrl(paramsToRemove, null, hashParamsToRemove, null) + URLUtils.HASH_PARAMETERS_SEPARATOR
                    + URLUtils.DEFAULT_HISTORY_TOKEN;
            urlUtils.windowRedirect(urlString);
        }

    }

    /**
     * The GWT UncaughtExceptionHandler for BonitaApplication
     */
    protected class ApplicationUncaughtExceptionHandler implements UncaughtExceptionHandler {

        @Override
        public void onUncaughtException(final Throwable throwable) {
            GWT.log("<BonitaApplication uncaught exception>", throwable);
        }

    }
}
