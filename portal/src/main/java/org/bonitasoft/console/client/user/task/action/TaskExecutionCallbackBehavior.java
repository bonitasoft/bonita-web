package org.bonitasoft.console.client.user.task.action;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.console.client.user.task.view.TasksListingPage;
import org.bonitasoft.web.toolkit.client.ClientApplicationURL;
import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.ui.utils.Message;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;

public class TaskExecutionCallbackBehavior {

    private String taskDisplayName = null;

    public TaskExecutionCallbackBehavior(final String taskDisplayName) {
        this.taskDisplayName = taskDisplayName;
    }

    public TaskExecutionCallbackBehavior() {
    }

    protected void redirectToTaskList() {
        History.newItem("?_p=" + TasksListingPage.TOKEN + "&_pf=" + ClientApplicationURL.getProfileId());
    }

    public void onSuccess(final String responseContent) {
        String version = null;
        if (responseContent != null && !responseContent.isEmpty()) {
            final JSONValue root = JSONParser.parseLenient(responseContent);
            final JSONObject dataFromSuccess = root.isObject();
            final JSONValue versionValue = dataFromSuccess.get("version");
            if (versionValue != null) {
                version = versionValue.isString().stringValue();
            }
        }
        if (!"6.x".equals(version)) {
            final String confirmationMessage;
            if (taskDisplayName != null) {
                confirmationMessage = _("The task %taskName% has been executed. The task list is being refreshed.", new Arg("taskName", taskDisplayName));
            } else {
                confirmationMessage = _("The task has been executed. The task list is being refreshed.");
            }
            if (ViewController.hasOpenedPopup()) {
                ViewController.closePopup();
            }
            showConfirmation(confirmationMessage);
            redirectToTaskList();
        }
    }

    public void onError(final String responseContent, final Integer errorCode) {
        if (errorCode == Response.SC_BAD_REQUEST) {
            GWT.log("Error while executing the task : " + responseContent);
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
