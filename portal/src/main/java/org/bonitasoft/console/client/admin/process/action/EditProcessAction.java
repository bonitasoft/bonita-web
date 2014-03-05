/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.console.client.admin.process.action;

import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.console.client.admin.process.view.ProcessMoreDetailsAdminPage;
import org.bonitasoft.web.rest.model.bpm.process.ProcessDefinition;
import org.bonitasoft.web.rest.model.bpm.process.ProcessItem;
import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.common.TreeIndexed;
import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.ui.action.form.FormAction;

/**
 * @author Chong Zhao, Haojie Yuan
 * 
 */
public class EditProcessAction extends FormAction {

    private final String processId;

    private String id = null;

    private boolean isFromProcessInfo = false;

    private static final String PROCESS_ID = "process_id";

    public EditProcessAction(final String processId, final boolean isFromProcessInfo) {
        this.processId = processId;
        this.isFromProcessInfo = isFromProcessInfo;
    }

    @Override
    public void execute() {

        if (this.id != null) {
            this.isFromProcessInfo = true;
        } else {
            this.id = this.getParameter("id");
        }

        final String processDisplayName = this.getParameter(ProcessItem.ATTRIBUTE_DISPLAY_NAME);
        final String processDisplayDescription = this.getParameter(ProcessItem.ATTRIBUTE_DISPLAY_DESCRIPTION);
        final String icon = this.getParameter(ProcessItem.ATTRIBUTE_ICON);

        final HashMap<String, String> attributesToUpdate = new HashMap<String, String>();

        if (icon != null && icon.length() > 0) {
            attributesToUpdate.put(ProcessItem.ATTRIBUTE_ICON, icon);
        }
        if (processDisplayDescription != null && processDisplayDescription.length() > 0) {
            attributesToUpdate.put(ProcessItem.ATTRIBUTE_DISPLAY_DESCRIPTION, processDisplayDescription);
        }
        if (processDisplayName != null && processDisplayName.length() > 0) {
            attributesToUpdate.put(ProcessItem.ATTRIBUTE_DISPLAY_NAME, processDisplayName);
        }

        new APICaller(ProcessDefinition.get()).update(this.processId, attributesToUpdate, new APICallback() {

            @Override
            public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
                ViewController.closePopup();
                if (EditProcessAction.this.isFromProcessInfo) {
                    final TreeIndexed<String> params = new TreeIndexed<String>();
                    params.addValue(PROCESS_ID, EditProcessAction.this.processId);
                    ViewController.showView(ProcessMoreDetailsAdminPage.TOKEN, params);
                }
                ViewController.refreshCurrentPage();
            }

        });

    }
}
