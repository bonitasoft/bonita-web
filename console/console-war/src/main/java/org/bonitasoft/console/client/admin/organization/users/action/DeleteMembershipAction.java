/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.console.client.admin.organization.users.action;

import java.util.Map;

import org.bonitasoft.web.rest.model.identity.MembershipDefinition;
import org.bonitasoft.web.rest.model.identity.MembershipItem;
import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.ui.action.Action;

/**
 * @author Séverin Moussel
 */
public class DeleteMembershipAction extends Action {

    @Override
    public void execute() {

        new APICaller<MembershipItem>(Definitions.get(MembershipDefinition.TOKEN)).delete(
                APIID.makeAPIID(this.getParameter("id")),
                new APICallback() {

                    @Override
                    public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
                        ViewController.getInstance().historyBack();
                    }

                });
    }
}
