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
import org.bonitasoft.console.client.user.cases.view.FormsView;
import org.bonitasoft.console.client.user.task.view.TasksListingPage;
import org.bonitasoft.web.rest.model.bpm.flownode.HumanTaskDefinition;
import org.bonitasoft.web.rest.model.bpm.flownode.HumanTaskItem;
import org.bonitasoft.web.toolkit.client.ClientApplicationURL;
import org.bonitasoft.web.toolkit.client.Session;
import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.ui.component.containers.Container;
import org.bonitasoft.web.toolkit.client.ui.component.core.AbstractComponent;
import org.bonitasoft.web.toolkit.client.ui.component.core.Component;
import org.bonitasoft.web.toolkit.client.ui.component.core.UiComponent;
import org.bonitasoft.web.toolkit.client.ui.page.ItemNotFoundPopup;
import org.bonitasoft.web.toolkit.client.ui.page.PageOnItem;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Element;

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
        this.addParameter(PARAMETER_ITEM_ID, taskId.toString());
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
            addBody(createFormIframe(task, true));
        } else if (!task.getAssignedId().equals(getUserId())) {
            ViewController.showView(TasksListingPage.TOKEN);
            throw new APIException(_("This task is already assigned to someone else."));
        } else {
            addBody(createFormIframe(task, false));
        }
    }

    /**
     * @return
     */
    private APIID getUserId() {
        return Session.getUserId();
    }

    private Component createFormIframe(final HumanTaskItem item, final boolean assignTask) {
        return new UiComponent(new FormsView(buildTasksFormURL(item, assignTask)));
    }

    private String buildTasksFormURL(final HumanTaskItem item, final boolean assignTask) {
        final StringBuilder frameURL = new StringBuilder()

                .append(GWT.getModuleBaseURL()).append("homepage?ui=form&locale=")
                .append(AbstractI18n.getDefaultLocale().toString());

        // if tenant is filled in portal url add tenant parameter to IFrame url
        final String tenantId = ClientApplicationURL.getTenantId();
        if (tenantId != null && !tenantId.isEmpty()) {
            frameURL.append("&tenant=").append(tenantId);
        }

        frameURL.append("#form=")
                .append(URL.encodeQueryString(item.getProcess().getName())).append(UUID_SEPERATOR)
                .append(URL.encodeQueryString(item.getProcess().getVersion())).append(UUID_SEPERATOR)
                .append(URL.encodeQueryString(item.getName()))

                .append("$entry")

                .append("&task=").append(item.getId())
                .append("&mode=form");

        if (assignTask) {
            frameURL.append("&assignTask=true");
        }

        return frameURL.toString();
    }

    /**
     * We don't need any header and it screw up the page's size.
     * 
     * @param header
     * @return
     */
    @Override
    protected List<Element> makeHeaderElements(final Container<AbstractComponent> header) {
        return null;
    }

    @Override
    protected List<Element> makeFooterElements(final Container<AbstractComponent> footer) {
        return null;
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
