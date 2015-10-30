package org.bonitasoft.console.client.user.task.action;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.ui.utils.Message;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;

public abstract class TaskExecutionCallbackBehavior {

    public abstract void onSuccess(final String targetUrlOnSuccess);

    public void onError(final String message, final Integer errorCode) {
        if (errorCode == Response.SC_BAD_REQUEST) {
            GWT.log("Error while executing the task : " + message);
            final String errorMessage = _("Error while trying to execute the task. Some required information is missing (contract not fulfilled).");
            ViewController.closePopup();
            showError(errorMessage);
        } else if (errorCode == Response.SC_UNAUTHORIZED) {
            Window.Location.reload();
        } else {
            showError(_("Error while trying to execute the task."));
        }
    }

    protected void showConfirmation(final String confirmationMessage) {
        Message.success(confirmationMessage);
    }

    protected void showError(final String errorMessage) {
        Message.warning(errorMessage);
    }
}
