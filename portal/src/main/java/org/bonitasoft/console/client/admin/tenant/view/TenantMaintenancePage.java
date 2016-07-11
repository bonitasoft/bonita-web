/*******************************************************************************
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.console.client.admin.tenant.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bonitasoft.web.rest.model.system.TenantAdminDefinition;
import org.bonitasoft.web.rest.model.system.TenantAdminItem;
import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.common.json.JSonItemReader;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.api.request.APIRequest;
import org.bonitasoft.web.toolkit.client.ui.Page;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.action.ClosePopUpAction;
import org.bonitasoft.web.toolkit.client.ui.component.Button;
import org.bonitasoft.web.toolkit.client.ui.component.Text;
import org.bonitasoft.web.toolkit.client.ui.component.button.ButtonAction;
import org.bonitasoft.web.toolkit.client.ui.component.containers.Container;
import org.bonitasoft.web.toolkit.client.ui.component.core.UiComponent;
import org.bonitasoft.web.toolkit.client.ui.component.event.ActionEvent;
import org.bonitasoft.web.toolkit.client.ui.utils.Loader;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * @author Anthony Birembaut
 */
public class TenantMaintenancePage extends Page {

    public static final String TOKEN = "tenantMaintenance";

    public static final List<String> PRIVILEGES = new ArrayList<String>();
    static {
        PRIVILEGES.add(TenantMaintenancePage.TOKEN);
    }

    interface Binder extends UiBinder<HTMLPanel, TenantMaintenancePage> {
    }

    private static Binder ui = GWT.create(Binder.class);

    @UiField(provided = true)
    static final String PAGE_TITLE = _("BPM services");

    @UiField(provided = true)
    static final String PAGE_DESCRIPTION = _("You need to pause the BPM services when you make an update to your environment that cannot be done while there are active users. When the services are paused:");

    @UiField(provided = true)
    static final String PAGE_DESCRIPTION_LIST_ELMENT1 = _("Only the technical user can log in to the Portal.");

    @UiField(provided = true)
    static final String PAGE_DESCRIPTION_LIST_ELMENT2 = _("Users who are currently logged in, including Administrator users, are automatically logged out.");

    @UiField(provided = true)
    static final String PAGE_DESCRIPTION_LIST_ELMENT3 = _("Users who are filling in forms when the services are paused will lose any information that has not been submitted.");

    @UiField(provided = true)
    static final String PAGE_DESCRIPTION_LIST_ELMENT4 = _("All processes are automatically paused.");

    @UiField(provided = true)
    static final String PAGE_DESCRIPTION_END = _("When you resume the services, tell your users that they can log back in to the Portal.");

    @UiField(provided = true)
    static final String STATUS = _("Status: ");

    static final String RESUME = _("Resume");

    static final String PAUSE = _("Pause");

    static final String CANCEL = _("Cancel");

    static final String PAUSED = _("Paused");

    static final String RUNNING = _("Running");

    static final String CONFIRMATION_PAUSE_POPUP_TITLE = _("Confirm services pause?");

    static final String CONFIRMATION_PAUSE_POPUP_MSG = _("Services will be paused for maintenance.\n");

    static final String CONFIRMATION_RESUME_POPUP_TITLE = _("Confirm services resumption?");

    static final String CONFIRMATION_RESUME_POPUP_MSG = _("Services will resume. Users will be able to log in.\n");

    @UiField
    SpanElement message;

    @UiField
    ButtonAction actionButton;

    @UiField
    ImageElement image;

    private final HTMLPanel uiBinded;

    protected boolean isPaused;

    public TenantMaintenancePage() {
        uiBinded = ui.createAndBindUi(this);
    }

    @Override
    public void buildView() {
        addBody(new UiComponent(uiBinded));
        requestTenantState(createShowTenantStateCallback());
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    public void defineTitle() {
        setTitle(PAGE_TITLE);
    }

    protected void changeTenantState(final boolean pause) {
        isPaused = pause;
        if (pause) {
            message.setInnerText(PAUSED.toUpperCase());
            actionButton.setLabel(RESUME);
            image.setSrc("themeResource?theme=portal&location=skin/images/pause.jpg");
        } else {
            message.setInnerText(RUNNING.toUpperCase());
            actionButton.setLabel(PAUSE);
            image.setSrc("themeResource?theme=portal&location=skin/images/running.jpg");
        }
    }

    @UiHandler("actionButton")
    protected void changeTenantState(final ActionEvent e) {
        if (isPaused) {
            ViewController.showPopup(confirmResumePopUp());
        } else {
            ViewController.showPopup(confirmPausePopUp());
        }
    }

    protected Page confirmResumePopUp() {
        return new Page() {

            @Override
            public String defineToken() {
                return "confirmResumePopup";
            }

            @Override
            public void defineTitle() {
                this.setTitle(CONFIRMATION_RESUME_POPUP_TITLE);
            }

            @Override
            public void buildView() {
                addBody(new Text(CONFIRMATION_RESUME_POPUP_MSG));
                addBody(popupButtons(RESUME));
            }

        };
    }

    protected Page confirmPausePopUp() {
        return new Page() {

            @Override
            public String defineToken() {
                return "confirmPausePopup";
            }

            @Override
            public void defineTitle() {
                this.setTitle(CONFIRMATION_PAUSE_POPUP_TITLE);
            }

            @Override
            public void buildView() {
                addBody(new Text(CONFIRMATION_PAUSE_POPUP_MSG));
                addBody(popupButtons(PAUSE));
            }
        };
    }

    private Container<Button> popupButtons(final String actionButtonValue) {
        final Container<Button> formactions = new Container<Button>();
        formactions.addClass("formactions");
        formactions.append(popupActionButton(actionButtonValue), popupCloseButon());
        return formactions;
    }

    private ButtonAction popupActionButton(final String actionButtonValue) {
        return new ButtonAction(actionButtonValue, actionButtonValue, createOnClickAction());
    }

    private Button popupCloseButon() {
        return new Button(CANCEL, CANCEL, new ClosePopUpAction());
    }

    protected Action createOnClickAction() {
        return new Action() {

            @Override
            public void execute() {
                Loader.showLoader();
                requestChangeTenantState(createChangeTenantStateCallback());
                ViewController.closePopup();
            }
        };
    }

    protected APICallback createChangeTenantStateCallback() {
        return new APICallback() {

            @Override
            public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
                Loader.hideLoader();
                requestTenantState(createShowTenantStateCallback());
            }

            @Override
            public void onError(final String message, final Integer errorCode) {
                Loader.hideLoader();
                super.onError(message, errorCode);
            }

        };
    }

    protected void requestChangeTenantState(final APICallback callback) {
        final TenantAdminItem tenantAdminItem = new TenantAdminItem();
        if (isPaused) {
            tenantAdminItem.setAttribute(TenantAdminItem.ATTRIBUTE_IS_PAUSED, Boolean.FALSE.toString());
        } else {
            tenantAdminItem.setAttribute(TenantAdminItem.ATTRIBUTE_IS_PAUSED, Boolean.TRUE.toString());
        }
        APIRequest.update(TenantAdminDefinition.UNUSED_ID, tenantAdminItem, TenantAdminDefinition.get(), callback).run();
    }

    protected APICallback createShowTenantStateCallback() {
        return new APICallback() {

            @Override
            public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
                final TenantAdminItem tenantAdminItem = JSonItemReader.parseItem(response, TenantAdminDefinition.get());
                changeTenantState(tenantAdminItem.isPaused());
            }
        };
    }

    protected void requestTenantState(final APICallback callback) {
        APIRequest.get(TenantAdminDefinition.UNUSED_ID, TenantAdminDefinition.get(), callback).run();
    }

}
