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
package org.bonitasoft.console.client.common.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bonitasoft.console.client.admin.bpm.cases.view.CaseListingAdminPage;
import org.bonitasoft.console.client.admin.bpm.task.view.TaskListingAdminPage;
import org.bonitasoft.console.client.admin.process.view.ProcessListingAdminPage;
import org.bonitasoft.console.client.user.application.view.ProcessListingPage;
import org.bonitasoft.console.client.user.cases.view.CaseListingPage;
import org.bonitasoft.console.client.user.task.model.TaskAPI;
import org.bonitasoft.console.client.user.task.view.TasksListingPage;
import org.bonitasoft.web.rest.model.bpm.flownode.HumanTaskDefinition;
import org.bonitasoft.web.rest.model.bpm.flownode.HumanTaskItem;
import org.bonitasoft.web.toolkit.client.Session;
import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.component.IFrame;
import org.bonitasoft.web.toolkit.client.ui.page.ItemNotFoundPopup;
import org.bonitasoft.web.toolkit.client.ui.page.PageOnItem;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;

/**
 * @author Séverin Moussel
 * 
 */
public class PerformTaskPage extends PageOnItem<HumanTaskItem> {

    public final static String TOKEN = "performTask";

    public static final List<String> PRIVILEGES = new ArrayList<String>();

    static {
        PRIVILEGES.add(TasksListingPage.TOKEN);
        PRIVILEGES.add(TaskListingAdminPage.TOKEN); // FIX ME: we should create a humantaskmoredetails admin page so ill never need this
        PRIVILEGES.add(CaseListingPage.TOKEN);
        PRIVILEGES.add(CaseListingAdminPage.TOKEN);
        PRIVILEGES.add(ProcessListingPage.TOKEN);
        PRIVILEGES.add(ProcessListingAdminPage.TOKEN);
        PRIVILEGES.add("reportlistingadminext");
    }

    public static final String PARAMETER_USER_ID = "userid";

    public final static String ASSIGN_AND_PERFORM_USER_TASK = "true";

    private final String UUID_SEPERATOR = "--";

    public PerformTaskPage() {
        super(Definitions.get(HumanTaskDefinition.TOKEN));
    }

    public PerformTaskPage(final APIID taskId) {
        this();
        addParameter(PARAMETER_ITEM_ID, taskId.toString());
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    public void defineTitle(final HumanTaskItem item) {
        this.setTitle(item.getDisplayName());
    }

    @Override
    public void buildView(final HumanTaskItem task) {
        if (task.getAssignedId() == null) {
            TaskAPI.claim(task.getId(), getUserId(), new Action() {

                @Override
                public void execute() {
                    addBody(createFormIframe(task));
                }
            });
        } else if (!task.getAssignedId().equals(getUserId())) {
            ViewController.showView(TasksListingPage.TOKEN);
            throw new APIException(_("You can't perform this task, it has already been assigned to someone else."));
        } else {
            addBody(createFormIframe(task));
        }
    }

    /**
     * @return
     */
    private APIID getUserId() {
        return Session.getUserId();
    }

    private IFrame createFormIframe(final HumanTaskItem item) {
        return new IFrame(buildTasksFormURL(item), "100%", "700px");
    }

    private String buildTasksFormURL(final HumanTaskItem item) {
        final StringBuilder frameURL = new StringBuilder()

                .append(GWT.getModuleBaseURL()).append("homepage")
                .append("?ui=form")

                .append("&locale=")
                .append(AbstractI18n.getDefaultLocale().toString())
                
                .append("#form=")
                .append(URL.decodeQueryString(item.getProcess().getName())).append(this.UUID_SEPERATOR)
                .append(item.getProcess().getVersion()).append(this.UUID_SEPERATOR)
                .append(URL.decodeQueryString(item.getName()))

                .append("$entry")

                .append("&task=").append(item.getId())
                .append("&mode=form");
        return frameURL.toString();
    }

    @Override
    protected void onItemNotFound() {
        ViewController.showPopup(new ItemNotFoundPopup(TasksListingPage.TOKEN));
    }

    @Override
    protected List<String> defineDeploys() {
        return Arrays.asList(HumanTaskItem.ATTRIBUTE_PROCESS_ID);
    }
}
