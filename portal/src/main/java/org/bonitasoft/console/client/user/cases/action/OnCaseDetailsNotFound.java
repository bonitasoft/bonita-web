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
package org.bonitasoft.console.client.user.cases.action;

import java.util.List;
import java.util.Map;

import org.bonitasoft.web.rest.model.bpm.cases.ArchivedCaseDefinition;
import org.bonitasoft.web.rest.model.bpm.cases.ArchivedCaseItem;
import org.bonitasoft.web.toolkit.client.ClientApplicationURL;
import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.common.json.JSonItemReader;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.common.util.MapUtil;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.page.ItemNotFoundPopup;

import com.google.gwt.user.client.Window;

/**
 * Check if the case is archived and redirect to case more details
 *
 * @author Anthony Birembaut
 */
public class OnCaseDetailsNotFound {

    private final String archivedCaseDetailsPageToken;

    public OnCaseDetailsNotFound(final String archivedCaseDetailsPageToken) {
        super();
        this.archivedCaseDetailsPageToken = archivedCaseDetailsPageToken;
    }

    public void checkIfCaseIsArchived(final String caseId) {
        new APICaller<ArchivedCaseItem>(ArchivedCaseDefinition.get()).search(0, 1, null, null, MapUtil.asMap(new Arg(
                ArchivedCaseItem.ATTRIBUTE_SOURCE_OBJECT_ID, caseId)), new APICallback() {

            @Override
            public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
                final List<ArchivedCaseItem> archivedCaseItems = JSonItemReader.parseItems(response, ArchivedCaseDefinition.get());
                if (!archivedCaseItems.isEmpty()) {
                    final APIID archivedCaseId = archivedCaseItems.get(0).getId();
                    redirectToArchivedCaseMoredetails(archivedCaseId.toString());
                } else {
                    onArchivedCaseNotFound();
                }
            }
        });
    }

    private void onArchivedCaseNotFound() {
        ViewController.showPopup(new ItemNotFoundPopup() {

            @Override
            protected Action getRedirectionAction() {
                return new Action() {

                    @Override
                    public void execute() {
                        //remove hash so that the user is redirected to the homepage of the profile (profile is kept as it is stored in a cookie)
                        Window.Location.assign(Window.Location.getPath() + Window.Location.getQueryString());
                    }
                };
            }
        });
    }

    private void redirectToArchivedCaseMoredetails(final String archivedCaseId) {
        //replace the in browser history so that the back button continue working
        Window.Location.replace(Window.Location.getPath() + Window.Location.getQueryString() + "#?_p=" + archivedCaseDetailsPageToken + "&id="
                + archivedCaseId + "&_pf=" + ClientApplicationURL.getProfileId());

    }

}
