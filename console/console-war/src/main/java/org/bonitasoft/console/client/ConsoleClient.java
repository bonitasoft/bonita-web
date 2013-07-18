/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.console.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bonitasoft.console.client.menu.view.LoginBox;
import org.bonitasoft.forms.client.view.FormsAsyncCallback;
import org.bonitasoft.forms.client.view.common.DOMUtils;
import org.bonitasoft.forms.client.view.common.RpcFormsServices;
import org.bonitasoft.forms.client.view.common.URLUtils;
import org.bonitasoft.forms.client.view.common.URLUtilsFactory;
import org.bonitasoft.forms.client.view.controller.FormViewControllerFactory;
import org.bonitasoft.web.rest.model.ModelFactory;
import org.bonitasoft.web.rest.model.user.User;
import org.bonitasoft.web.toolkit.client.ApplicationFactoryClient;
import org.bonitasoft.web.toolkit.client.ClientApplication;
import org.bonitasoft.web.toolkit.client.ItemDefinitionFactory;
import org.bonitasoft.web.toolkit.client.Session;
import org.bonitasoft.web.toolkit.client.ViewController;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * @author Yongtao Guo, Haojie Yuan, Ruiheng Fan
 */
public class ConsoleClient extends ClientApplication {

    protected boolean myAutoLogin = false;

    protected String formID;

    protected Map<String, Object> urlContext;

    protected String myApplicationMode;

    protected boolean themeLoaded = false;

    protected static final String CONSOLE_STATIC_CONTENT_ELEMENT_ID = "static_console";

    private final String CONSOLE_HEADER = "console_header";

    /**
     * Utility Class form DOM manipulation
     */
    protected URLUtils urlUtils = URLUtilsFactory.getInstance();

    @Override
    public ApplicationFactoryClient defineApplicationFactoryClient() {
        return new ConsoleFactoryClient();
    }

    @Override
    public ItemDefinitionFactory defineApplicationFactoryCommon() {
        return new ModelFactory();
    }

    @Override
    protected void onBeforeLoad() {
        // new JavaScripts(ConsoleJsResources.asList()).inject();
    }

    @Override
    protected void onLoad() {
        registerJSNIMethods();

        // Check if the application called is the forms application
        parseHashParameters();
        if (URLUtils.FORM_ONLY_APPLICATION_MODE.equals(myApplicationMode) || URLUtils.FULL_FORM_APPLICATION_MODE.equals(myApplicationMode)) {
            onFormsLoad();
        } else {
            onConsoleLoad();
        }
    }

    protected void registerJSNIMethods() {

    }

    protected void onConsoleLoad() {
        if ("true".equals(Session.getParameter("is_technical_user"))) {
            refreshView();
        }

        // The login box will initialize the view if the login works well
        ViewController.showView(getLoginBoxView(), "login");
    }

    protected LoginBox getLoginBoxView() {
        return new LoginBox();
    }

    /**
     * 
     */
    private void onFormsLoad() {
        if (RootPanel.get(CONSOLE_HEADER) != null) {
            RootPanel.get(CONSOLE_HEADER).setVisible(false);
        }
        final String autoLoginStr = Window.Location.getParameter(URLUtils.AUTO_LOGIN_PARAM);
        if (autoLoginStr != null) {
            myAutoLogin = Boolean.parseBoolean(autoLoginStr);
        }
        final String locale = urlUtils.getLocale();
        urlUtils.saveLocale(locale);

        GWT.runAsync(new RunAsyncCallback() {

            @Override
            public void onSuccess() {
                RpcFormsServices.getFormsService().getLoggedInUser(new FormsAsyncCallback<User>() {

                    @Override
                    public void onSuccess(final User user) {
                        ConsoleClient.this.loadFormView(user);
                    }

                    @Override
                    public void onUnhandledFailure(final Throwable caught) {
                        urlUtils.showLoginView();
                    }

                });
            }

            @Override
            public void onFailure(final Throwable aT) {
                GWT.log("Unable to load asynchronous script!", aT);
                Window.alert("Unable to load asynchronous script!" + aT.getMessage());
            }
        });
    }

    /**
     * Create the process application view
     * 
     * @param aUser
     */
    protected void createApplicationView(final User aUser) {
        DOMUtils.getInstance().cleanBody(CONSOLE_STATIC_CONTENT_ELEMENT_ID);
        if (URLUtils.FULL_FORM_APPLICATION_MODE.equals(myApplicationMode)) {
            FormViewControllerFactory.getFormApplicationViewController(formID, urlContext, aUser).createInitialView(DOMUtils.DEFAULT_FORM_ELEMENT_ID);
        } else {
            FormViewControllerFactory.getFormApplicationViewController(formID, urlContext, aUser).createFormInitialView();
        }
    }

    protected final void parseHashParameters() {
        formID = urlUtils.getFormID();
        urlContext = urlUtils.getHashParameters();
        myApplicationMode = (String) urlContext.get(URLUtils.VIEW_MODE_PARAM);
    }

    private void loadFormView(final User aUser) {
        if (aUser != null) {
            History.addValueChangeHandler(new ValueChangeHandler<String>() {

                @Override
                public void onValueChange(final ValueChangeEvent<String> event) {
                    ConsoleClient.this.parseHashParameters();
                    ConsoleClient.this.createApplicationView(aUser);
                }
            });
            final boolean isTodolist = Boolean.valueOf((String) urlContext.get(URLUtils.TODOLIST_PARAM));
            if (isTodolist) {
                final GetAnyTodolistFormHandler getAnyTodolistFormHandler = new GetAnyTodolistFormHandler();
                RpcFormsServices.getFormsService().getAnyTodoListForm(urlContext, getAnyTodolistFormHandler);

            } else {
                createApplicationView(aUser);
            }
        } else {
            urlUtils.showLoginView();
        }
    }

    /**
     * Get any todolist Form URL
     */
    protected final class GetAnyTodolistFormHandler extends FormsAsyncCallback<Map<String, Object>> {

        @Override
        public void onSuccess(final Map<String, Object> newUrlContext) {
            String urlString = null;
            String themeName = (String) newUrlContext.get(URLUtils.THEME);
            if (themeName == null || themeName.isEmpty()) {
                themeName = (String) urlContext.get(URLUtils.THEME);
            }
            final Map<String, String> paramsToAdd = new HashMap<String, String>();
            paramsToAdd.put(URLUtils.THEME, themeName);

            final List<String> hashParamsToRemove = new ArrayList<String>();
            hashParamsToRemove.add(URLUtils.VIEW_MODE_PARAM);
            hashParamsToRemove.add(URLUtils.TODOLIST_PARAM);
            hashParamsToRemove.add(URLUtils.FORM_ID);
            hashParamsToRemove.add(URLUtils.TASK_ID_PARAM);
            hashParamsToRemove.add(URLUtils.PROCESS_ID_PARAM);
            urlString = urlUtils.rebuildUrl(null, paramsToAdd, hashParamsToRemove, null);
            if (urlString.indexOf("#") < 0) {
                urlString += "#";
            } else {
                urlString += "&";
            }
            final String hash = urlUtils.getFormRedirectionHash(newUrlContext);
            urlString = urlString + hash;
            urlUtils.windowRedirect(urlString);
        }

        @Override
        public void onUnhandledFailure(final Throwable caught) {
            GWT.log("Unable to get any todolist form URL", caught);
            final Map<String, String> paramsToAdd = new HashMap<String, String>();
            paramsToAdd.put(URLUtils.UI, URLUtils.FORM_ONLY_APPLICATION_MODE);
            final List<String> hashParamsToRemove = new ArrayList<String>();
            hashParamsToRemove.add(URLUtils.TODOLIST_PARAM);
            final String urlString = urlUtils.rebuildUrl(null, paramsToAdd, hashParamsToRemove, null);
            urlUtils.windowRedirect(urlString);
        }

    }

}
