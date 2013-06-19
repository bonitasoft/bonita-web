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
package org.bonitasoft.console.client.user.task.view.more;

import java.util.Map;

import org.bonitasoft.console.client.common.view.PerformTaskPage;
import org.bonitasoft.console.client.user.task.action.TaskClaimAction;
import org.bonitasoft.console.client.user.task.action.TaskRelaseAction;
import org.bonitasoft.console.client.user.task.action.UserTasksHideAction;
import org.bonitasoft.console.client.user.task.action.UserTasksUnhideAction;
import org.bonitasoft.console.client.user.task.view.PluginTask;
import org.bonitasoft.console.client.user.task.view.TaskButtonFactory;
import org.bonitasoft.web.rest.model.bpm.flownode.HiddenUserTaskDefinition;
import org.bonitasoft.web.rest.model.bpm.flownode.HumanTaskDefinition;
import org.bonitasoft.web.rest.model.bpm.flownode.HumanTaskItem;
import org.bonitasoft.web.toolkit.client.Session;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.api.request.APIRequest;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.ui.action.ActionShowView;

/**
 * @author Séverin Moussel
 * @author Vincent Elcrin
 * @author Colin PUY
 */
public class HumanTaskMoreDetailsPage extends AbstractMoreTaskDetailPage<HumanTaskItem> implements PluginTask {

    public static String TOKEN = "taskdetails";

    public HumanTaskMoreDetailsPage() {
        super(HumanTaskDefinition.get());
    }

    public HumanTaskMoreDetailsPage(HumanTaskItem task) {
        super(HumanTaskDefinition.get(), task);
    }

    @Override
    protected void buildToolbar(HumanTaskItem item) {
        super.buildToolbar(item);
        final TaskButtonFactory factory = new TaskButtonFactory();
        if (!isTaskAssignedToOtherUser(item) && item.isUserTask()) {
            addToolbarLink(factory.createPerformTaskButton(createPerformAction(item)));
        }

        if (isTaskAssignedToCurrentUser(item) && item.isUserTask()) {
            addToolbarLink(factory.createUnassignedButton(new TaskRelaseAction(item.getId())));
        }

        if (item.isUnassigned()) {
            addToolbarLink(factory.createClaimButton(new TaskClaimAction(Session.getUserId(), item.getId())));
        }

        if (!isTaskAssignedToOtherUser(item)) {

            isTaskHidden(item, new APICallback() {

                @Override
                public void onSuccess(final int httpStatusCode, final String response,
                        final Map<String, String> headers) {
                    addRetrieveButton(factory);
                }

                @Override
                protected void on404NotFound(String message) {
                    addIgnoreButton(factory);
                }

            });

        }
    }

    private ActionShowView createPerformAction(final HumanTaskItem item) {
        return new ActionShowView(new PerformTaskPage(item.getId()));
    }

    private void addRetrieveButton(final TaskButtonFactory factory) {
        addToolbarLink(factory.createRetrieveButton(new UserTasksUnhideAction(Session.getUserId(), getItemId())));
    }

    private void addIgnoreButton(final TaskButtonFactory factory) {
        addToolbarLink(factory.createIgnoreButton(new UserTasksHideAction(Session.getUserId(), getItemId())));
    }

    private void isTaskHidden(HumanTaskItem item, APICallback callback) {
        final APIID compAPIId = APIID.makeAPIID(Session.getUserId().toLong(), item.getId().toLong());
        APIRequest.get(compAPIId, Definitions.get(HiddenUserTaskDefinition.TOKEN), callback).run();
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    public String getPluginToken() {
        return PLUGIN_TOKEN;
    }

}
