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

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bonitasoft.console.client.admin.bpm.cases.view.CaseListingAdminPage;
import org.bonitasoft.console.client.admin.bpm.task.view.TaskListingAdminPage;
import org.bonitasoft.console.client.admin.process.view.ProcessListingAdminPage;
import org.bonitasoft.console.client.common.component.snippet.CommentSectionSnippet;
import org.bonitasoft.console.client.common.metadata.MetadataTaskBuilder;
import org.bonitasoft.console.client.user.application.view.ProcessListingPage;
import org.bonitasoft.console.client.user.cases.view.CaseListingPage;
import org.bonitasoft.console.client.user.task.view.more.ArchivedHumanTaskMoreDetailsPage;
import org.bonitasoft.web.rest.model.bpm.flownode.ArchivedHumanTaskDefinition;
import org.bonitasoft.web.rest.model.bpm.flownode.ArchivedHumanTaskItem;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.action.ActionShowView;
import org.bonitasoft.web.toolkit.client.ui.action.CheckValidSessionBeforeAction;
import org.bonitasoft.web.toolkit.client.ui.component.Button;
import org.bonitasoft.web.toolkit.client.ui.component.Section;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemDetailsMetadata;
import org.bonitasoft.web.toolkit.client.ui.utils.DateFormat;

/**
 * @author Séverin Moussel
 * 
 */
public class ArchivedHumanTaskQuickDetailsPage extends AbstractTaskDetailsPage<ArchivedHumanTaskItem> implements PluginTask {

    public static final String TOKEN = "archivedtaskquickdetails";

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

    public ArchivedHumanTaskQuickDetailsPage() {
        super(ArchivedHumanTaskDefinition.get());
    }

    @Override
    protected void buildToolbar(final ArchivedHumanTaskItem task) {
        addToolbarLink(moreButton(task));
    }

    private Button moreButton(final ArchivedHumanTaskItem task) {
        return new Button("btn-more", _("More"), _("Show more details about this task"), moreAction(task));
    }

    private Action moreAction(final ArchivedHumanTaskItem task) {
        return new CheckValidSessionBeforeAction(new ActionShowView(new ArchivedHumanTaskMoreDetailsPage(task)));
    }

    @Override
    protected LinkedList<ItemDetailsMetadata> defineMetadatas(final ArchivedHumanTaskItem task) {
        MetadataTaskBuilder metadatas = new MetadataTaskBuilder();
        metadatas.addCaseId(task, false);
        metadatas.addAppsName();
        metadatas.addDueDate(DateFormat.FORMAT.DISPLAY_RELATIVE);
        metadatas.addPriority();
        return metadatas.build();
    }

    @Override
    protected void buildBody(final ArchivedHumanTaskItem item) {
        addBody(commentSection(item));
    }

    private Section commentSection(final ArchivedHumanTaskItem task) {
        return new CommentSectionSnippet(task.getCaseId()).setNbLinesByPage(3).build();
    }

    @Override
    protected List<String> defineDeploys() {
        final List<String> deploys = super.defineDeploys();
        deploys.add(ArchivedHumanTaskItem.ATTRIBUTE_EXECUTED_BY_USER_ID);
        return deploys;
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
