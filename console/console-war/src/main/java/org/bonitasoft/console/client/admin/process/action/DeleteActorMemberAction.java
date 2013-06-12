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
package org.bonitasoft.console.client.admin.process.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bonitasoft.console.client.common.view.ViewParameter;
import org.bonitasoft.console.client.model.bpm.process.ActorMemberDefinition;
import org.bonitasoft.console.client.model.bpm.process.ActorMemberItem;
import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.ui.action.ItemAction;

import com.google.gwt.user.client.Window;

/**
 * @author Yongtao Guo
 */
public class DeleteActorMemberAction extends ItemAction {

    private final List<String> actorMemberIds;

    private final String actorId;

    private final String processId;

    private final String redirectionToken;

    public DeleteActorMemberAction(final String actorId, final String actorMemberId, final String processId, final String redirectionToken) {
        super(Definitions.get(ActorMemberDefinition.TOKEN));
        this.actorMemberIds = new ArrayList<String>();
        this.actorMemberIds.add(actorMemberId);
        this.actorId = actorId;
        this.processId = processId;
        this.redirectionToken = redirectionToken;
    }

    @Override
    public void execute() {

        final HashMap<String, String> params = new HashMap<String, String>();
        params.put(ViewParameter.ID, this.actorId);
        params.put(ViewParameter.PROCESS_ID, this.processId);
        new APICaller<ActorMemberItem>(Definitions.get(ActorMemberDefinition.TOKEN)).delete(this.actorMemberIds, new APICallback() {

            @Override
            public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
                ViewController.showPopup(DeleteActorMemberAction.this.redirectionToken, params);
            }

            @Override
            public void onError(final String message, final Integer errorCode) {
                Window.alert(message);
            }
        });
    }

}
