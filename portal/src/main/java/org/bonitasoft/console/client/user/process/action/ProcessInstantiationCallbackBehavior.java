/**
 * Copyright (C) 2016 BonitaSoft S.A.
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
package org.bonitasoft.console.client.user.process.action;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.console.client.user.process.view.ProcessListingPage;
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

    protected void redirectAfterSubmission(final String caseId) {
        redirectToDefaultPage();
    }

    protected void redirectToDefaultPage() {
        History.newItem("?_p=" + ProcessListingPage.TOKEN + "&_pf=" + ClientApplicationURL.getProfileId());
    }

    public void onSuccess(final String responseContent) {
        final String version = getAttributeValue(responseContent, "version");
        if (!"6.x".equals(version)) {
            if (ViewController.hasOpenedPopup()) {
                ViewController.closePopup();
            }
            final String caseId = getAttributeValue(responseContent, "caseId");
            if (caseId != null) {
                showConfirmation(_("The case %caseId% has been started successfully.", new Arg("caseId", caseId)));
                //Wait a short delay before redirecting. This is useful in case the process completes right away (archiving may take a while)
                final Timer timer = new Timer() {

                    @Override
                    public void run() {
                        redirectAfterSubmission(caseId);
                    }
                };
                timer.schedule(DELAY_BEFORE_REDIRECTION);
            } else {
                GWT.log("caseId is not set");
                showConfirmation(_("A case has been started successfully."));
                redirectToDefaultPage();
            }
        }
    }

    protected String getAttributeValue(final String responseContent, final String attributeName) {
        String attributeValueAsString = null;
        if (responseContent != null && !responseContent.isEmpty()) {
            final JSONValue root = JSONParser.parseLenient(responseContent);
            final JSONObject dataObject = root.isObject();
            if (dataObject != null) {
                final JSONValue attributeValue = dataObject.get(attributeName);
                if (attributeValue != null) {
                    attributeValueAsString = Double.toString(attributeValue.isNumber().doubleValue());
                }
            }
        }
        return attributeValueAsString;
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
