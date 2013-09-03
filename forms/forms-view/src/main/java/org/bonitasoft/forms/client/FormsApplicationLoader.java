package org.bonitasoft.forms.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import org.bonitasoft.forms.client.i18n.FormsResourceBundle;
import org.bonitasoft.forms.client.view.FormsAsyncCallback;
import org.bonitasoft.forms.client.view.common.BonitaUrlContext;
import org.bonitasoft.forms.client.view.common.DOMUtils;
import org.bonitasoft.forms.client.view.common.RpcFormsServices;
import org.bonitasoft.forms.client.view.common.URLUtils;
import org.bonitasoft.forms.client.view.controller.ErrorPageHandler;
import org.bonitasoft.forms.client.view.controller.FormApplicationViewController;
import org.bonitasoft.forms.client.view.controller.FormViewControllerFactory;
import org.bonitasoft.web.rest.model.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormsApplicationLoader {

    protected static final String CONSOLE_STATIC_CONTENT_ELEMENT_ID = "static_console";
    private final String CONSOLE_HEADER = "console_header";
    public static final String FORM_URL_PARAMETER_IS_MANDATORY = FormsResourceBundle.getErrors().formUrlParameterIsMandatoryError();



    private URLUtils urlUtils;
    private BonitaUrlContext bonitaUrlContext;

    public FormsApplicationLoader(URLUtils urlUtils, BonitaUrlContext bonitaUrlContext) {
        this.urlUtils = urlUtils;
        this.bonitaUrlContext = bonitaUrlContext;
    }

    public void load() {
        if (RootPanel.get(CONSOLE_HEADER) != null) {
            RootPanel.get(CONSOLE_HEADER).setVisible(false);
        }

        final String locale = urlUtils.getLocale();
        urlUtils.saveLocale(locale);

        GWT.runAsync(new RunAsyncCallback() {

            @Override
            public void onSuccess() {
                RpcFormsServices.getFormsService().getLoggedInUser(new FormsAsyncCallback<User>() {

                    @Override
                    public void onSuccess(final User user) {
                        FormsApplicationLoader.this.loadFormView(user);
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

    private void loadFormView(final User aUser) {
        if (aUser != null) {
            History.addValueChangeHandler(new ValueChangeHandler<String>() {

                @Override
                public void onValueChange(final ValueChangeEvent<String> event) {
                    FormsApplicationLoader.this.bonitaUrlContext = BonitaUrlContext.get();
                    FormsApplicationLoader.this.createApplicationView(aUser);
                }
            });
            if (bonitaUrlContext.isTodoList()) {
                final GetAnyTodolistFormHandler getAnyTodolistFormHandler = new GetAnyTodolistFormHandler();
                RpcFormsServices.getFormsService().getAnyTodoListForm(bonitaUrlContext.getHashParameters(), getAnyTodolistFormHandler);

            } else {
                createApplicationView(aUser);
            }
        } else {
            urlUtils.showLoginView();
        }
    }

    protected void createApplicationView(final User aUser) {
        if (bonitaUrlContext.getFormId() != null) {
            DOMUtils.getInstance().cleanBody(CONSOLE_STATIC_CONTENT_ELEMENT_ID);
            FormApplicationViewController formApplicationViewController = getFormApplicationViewController(aUser);
            if (bonitaUrlContext.isFormFullPageApplicationMode()) {
                formApplicationViewController.createInitialView(DOMUtils.DEFAULT_FORM_ELEMENT_ID);
            } else {
                formApplicationViewController.createFormInitialView();
            }
        } else {
            showFormIdMandatoryErrorPage();
        }
    }

    private void showFormIdMandatoryErrorPage() {
        getApplicationErrorTemplate(
                new ErrorPageHandler(null,
                        null,
                        FORM_URL_PARAMETER_IS_MANDATORY,
                        getFormElementId()));
    }

    private void getApplicationErrorTemplate(ErrorPageHandler callback) {
        // formId is not used in getApplicationErrorTemplate method
        RpcFormsServices.getFormsService().getApplicationErrorTemplate(null,
                bonitaUrlContext.getHashParameters(),
                callback);
    }

    private String getFormElementId() {
        if (bonitaUrlContext.isFormFullPageApplicationMode()) {
            return DOMUtils.DEFAULT_FORM_ELEMENT_ID;
        } else {
            return null;
        }
    }

    private FormApplicationViewController getFormApplicationViewController(User aUser) {
        return FormViewControllerFactory.getFormApplicationViewController(
                bonitaUrlContext.getFormId(),
                bonitaUrlContext.getHashParameters(),
                aUser);
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
                themeName = bonitaUrlContext.getThemeName();
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


