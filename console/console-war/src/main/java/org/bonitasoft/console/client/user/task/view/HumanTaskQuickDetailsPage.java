/**
 * Copyright (C) 2012 BonitaSoft S.A.
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
package org.bonitasoft.console.client.user.task.view;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bonitasoft.console.client.admin.bpm.cases.view.CaseListingAdminPage;
import org.bonitasoft.console.client.admin.bpm.task.view.TaskListingAdminPage;
import org.bonitasoft.console.client.admin.process.view.ProcessListingAdminPage;
import org.bonitasoft.console.client.common.component.snippet.CommentSectionSnippet;
import org.bonitasoft.console.client.common.metadata.MetadataTaskBuilder;
import org.bonitasoft.console.client.common.view.PerformTaskPage;
import org.bonitasoft.console.client.user.application.view.ProcessListingPage;
import org.bonitasoft.console.client.user.cases.view.CaseListingPage;
import org.bonitasoft.console.client.user.task.view.more.HumanTaskMoreDetailsPage;
import org.bonitasoft.web.rest.model.bpm.flownode.HumanTaskDefinition;
import org.bonitasoft.web.rest.model.bpm.flownode.HumanTaskItem;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.action.ActionShowView;
import org.bonitasoft.web.toolkit.client.ui.action.CheckValidSessionBeforeAction;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemDetailsMetadata;
import org.bonitasoft.web.toolkit.client.ui.utils.DateFormat;

/**
 * @author Séverin Moussel
 * 
 */
public class HumanTaskQuickDetailsPage extends AbstractTaskDetailsPage<HumanTaskItem> implements PluginTask {

    public static final String TOKEN = "taskquickdetails";

    public static final List<String> PRIVILEGES = new ArrayList<String>();
    
    static {
        PRIVILEGES.add(TasksListingPage.TOKEN);
        PRIVILEGES.add(TaskListingAdminPage.TOKEN); //FIX ME: we should create a humantaskmoredetails admin page so ill never need this
        PRIVILEGES.add(CaseListingPage.TOKEN);
        PRIVILEGES.add(CaseListingAdminPage.TOKEN);
        PRIVILEGES.add(ProcessListingPage.TOKEN);
        PRIVILEGES.add(ProcessListingAdminPage.TOKEN);
        PRIVILEGES.add("reportlistingadminext");
    }

    public HumanTaskQuickDetailsPage() {
        super(Definitions.get(HumanTaskDefinition.TOKEN));
    }

    @Override
    protected void buildToolbar(final HumanTaskItem item) {
        final TaskButtonFactory factory = new TaskButtonFactory();
        if (!isTaskAssignedToOtherUser(item) && item.isUserTask()) {
            addToolbarLink(factory.createPerformTaskButton(createPerformAction(item)));
        }
        // MORE
        addToolbarLink(factory.createMoreDetailsButton(createMoreDetailsAction(item)));
    }

    protected Action createMoreDetailsAction(final HumanTaskItem item) {
        return new CheckValidSessionBeforeAction(new ActionShowView(new HumanTaskMoreDetailsPage(item)));
    }

    private Action createPerformAction(final HumanTaskItem item) {
        return new CheckValidSessionBeforeAction(new ActionShowView(new PerformTaskPage(item.getId())));
    }

    @Override
    protected void buildBody(final HumanTaskItem item) {
        addBody(new CommentSectionSnippet(item.getCaseId()).setNbLinesByPage(3)
                .build());
    }

    @Override
    protected LinkedList<ItemDetailsMetadata> defineMetadatas(final HumanTaskItem task) {
        MetadataTaskBuilder metadatas = new MetadataTaskBuilder();
        metadatas.addCaseId(task, false);
        metadatas.addAppsName();
        metadatas.addDueDate(DateFormat.FORMAT.DISPLAY_RELATIVE);
        metadatas.addPriority();
        return metadatas.build();
    }

    @Override
    public String getPluginToken() {
        return PLUGIN_TOKEN;
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }
}
