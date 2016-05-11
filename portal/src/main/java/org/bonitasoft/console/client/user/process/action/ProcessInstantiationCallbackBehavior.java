package org.bonitasoft.console.client.user.process.action;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.console.client.user.cases.view.CaseMoreDetailsPage;
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
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;

public class ProcessInstantiationCallbackBehavior {

    private static final int DELAY_BEFORE_REDIRECTION = 300;

    protected void redirectToCaseMoredetails(final String caseId) {
        History.newItem("?_p=" + CaseMoreDetailsPage.TOKEN + "&id=" + caseId + "&_pf=" + ClientApplicationURL.getProfileId());
    }

    protected void redirectToCurrentProfileHome() {
        History.newItem("?_pf=" + ClientApplicationURL.getProfileId());
    }

    public void onSuccess(final String responseContent) {
        if (ViewController.hasOpenedPopup()) {
            ViewController.closePopup();
        }
        final String caseId = getCaseId(responseContent);
        if (caseId != null) {
            showConfirmation(_("The case %caseId% has been started successfully.", new Arg(
                    "caseId", caseId)));
            //Wait a short delay before redirecting. This is useful in case the process completes right away (archiving may take a while)
            final Timer timer = new Timer() {

                @Override
                public void run() {
                    redirectToCaseMoredetails(caseId);
                }
            };
            timer.schedule(DELAY_BEFORE_REDIRECTION);
        } else {
            GWT.log("caseId is not set");
            showConfirmation(_("A case has been started successfully."));
            redirectToCurrentProfileHome();
        }
    }

    protected String getCaseId(final String responseContent) {
        String caseId = null;
        if (responseContent != null && !responseContent.isEmpty()) {
            final JSONValue root = JSONParser.parseLenient(responseContent);
            final JSONObject processInstance = root.isObject();
            final JSONValue caseIdValue = processInstance.get("caseId");
            if (caseIdValue != null) {
                caseId = Double.toString(caseIdValue.isNumber().doubleValue());
            }
        }
        return caseId;
    }

    public void onError(final String responseContent, final int errorCode) {
        if (errorCode == Response.SC_BAD_REQUEST) {
            GWT.log("Error while instantiating process : " + responseContent);
            final String errorMessage = _("Error while trying to start the case. Some required information is missing (contract not fulfilled).");
            ViewController.closePopup();
            showError(errorMessage);
        } else if (errorCode == Response.SC_UNAUTHORIZED) {
            Window.Location.reload();
        } else {
            showError(_("Error while trying to start the case."));
        }
    }

    protected void showConfirmation(final String confirmationMessage) {
        Message.success(confirmationMessage);
    }

    protected void showError(final String errorMessage) {
        Message.warning(errorMessage);
    }
}
