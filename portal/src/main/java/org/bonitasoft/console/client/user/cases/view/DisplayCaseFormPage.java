/**
 * Copyright (C) 2009, 2013 BonitaSoft S.A.
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
package org.bonitasoft.console.client.user.cases.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bonitasoft.console.client.admin.process.view.ProcessListingAdminPage;
import org.bonitasoft.console.client.angular.AngularIFrameView;
import org.bonitasoft.console.client.user.process.view.ProcessListingPage;
import org.bonitasoft.web.rest.model.bpm.cases.ArchivedCaseItem;
import org.bonitasoft.web.rest.model.bpm.cases.CaseItem;
import org.bonitasoft.web.rest.model.bpm.process.ProcessItem;
import org.bonitasoft.web.toolkit.client.ClientApplicationURL;
import org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.common.url.UrlUtil;
import org.bonitasoft.web.toolkit.client.ui.Page;
import org.bonitasoft.web.toolkit.client.ui.component.Paragraph;
import org.bonitasoft.web.toolkit.client.ui.component.button.ButtonBack;
import org.bonitasoft.web.toolkit.client.ui.component.containers.Container;
import org.bonitasoft.web.toolkit.client.ui.component.core.AbstractComponent;
import org.bonitasoft.web.toolkit.client.ui.component.core.UiComponent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;

/**
 * @author Fabio Lombardi
 *
 */
public class DisplayCaseFormPage extends Page {

    public final static String TOKEN = "DisplayCaseForm";

    public static final List<String> PRIVILEGES = new ArrayList<String>();

    private IFrameView view;

    static {
        PRIVILEGES.add(AngularIFrameView.CASE_LISTING_ADMIN_TOKEN);
        PRIVILEGES.add(CaseListingPage.TOKEN);
        PRIVILEGES.add(ProcessListingPage.TOKEN);
        PRIVILEGES.add(ProcessListingAdminPage.TOKEN);
        PRIVILEGES.add("caselistingpm");
        PRIVILEGES.add("reportlistingadminext");
    }

    // legacy, needed by ConsoleFactoryClient
    public DisplayCaseFormPage() {
    }

    public DisplayCaseFormPage(final CaseItem item) {
        this();
        this.setParameters(getItemParams(item));
    }

    @Override
    public void defineTitle() {
        this.setTitle("");
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    public void buildView() {
        view = new IFrameView(getCaseOverviewUrl());
        addBody(new UiComponent(view));
        view.addTool(new ButtonBack());
        //Check form mapping to ensure there is an overview form to display
        checkMapping();
    }

    private void checkMapping() {
        final String processId = this.getParameter(CaseItem.ATTRIBUTE_PROCESS_ID);
        final AbstractOverviewFormMappingRequester overviewFormMappingRequester = new AbstractOverviewFormMappingRequester() {

            @Override
            public void onMappingNotFound() {
                GWT.log("There is no overview mapping for process " + processId);
                final Paragraph message = new Paragraph(_("No overview page has been defined for this process."));
                message.addClass("callout callout-info");
                view.addTool(message);
            }

            @Override
            public void onMappingFound() {
                //do nothing
            }
        };
        overviewFormMappingRequester.searchFormMappingForInstance(processId);
    }

    private String getCaseOverviewUrl() {
        final String processName = this.getParameter(ProcessItem.ATTRIBUTE_NAME);
        final String encodedProcessName = UrlUtil.escapePathSegment(processName);
        final String processVersion = this.getParameter(ProcessItem.ATTRIBUTE_VERSION);
        final String encodedProcessVersion = UrlUtil.escapePathSegment(processVersion);
        String caseId = this.getParameter(ArchivedCaseItem.ATTRIBUTE_SOURCE_OBJECT_ID);
        if (caseId == null) {
            caseId = this.getParameter(CaseItem.ATTRIBUTE_ID);
        }
        final String locale = AbstractI18n.getDefaultLocale().toString();
        final String tenantId = ClientApplicationURL.getTenantId();

        this.setTitle(_("Display a case form of process %app_name%", new Arg("app_name", encodedProcessName)));

        final StringBuilder frameURL = new StringBuilder();
        frameURL.append("resource/processInstance/")
                .append(encodedProcessName)
                .append("/")
                .append(encodedProcessVersion)
                .append("/content/?id=")
                .append(caseId)
                .append("&locale=")
                .append(locale);
        // if tenant is filled in portal url add tenant parameter to IFrame url
        if (tenantId != null && !tenantId.isEmpty()) {
            frameURL.append("&tenant=").append(tenantId);
        }
        return frameURL.toString();
    }

    public static final Map<String, String> getItemParams(final CaseItem item) {
        if (item.getDeploy(CaseItem.ATTRIBUTE_PROCESS_ID) == null) {
            throw new RuntimeException(CaseItem.ATTRIBUTE_PROCESS_ID + " attribute need to be deployed");
        }
        final Map<String, String> processParams = new HashMap<String, String>();
        processParams.put(ProcessItem.ATTRIBUTE_NAME, item.getProcess().getName());
        processParams.put(ProcessItem.ATTRIBUTE_VERSION, item.getProcess().getVersion());
        processParams.put(CaseItem.ATTRIBUTE_PROCESS_ID, item.getProcess().getId().toString());
        processParams.put(CaseItem.ATTRIBUTE_ID, item.getId().toString());
        if (item instanceof ArchivedCaseItem) {
            processParams.put(ArchivedCaseItem.ATTRIBUTE_SOURCE_OBJECT_ID, ((ArchivedCaseItem) item).getSourceObjectId().toString());
        }
        processParams.put("token", TOKEN);
        return processParams;
    }

    @Override
    protected List<Element> makeHeaderElements(final Container<AbstractComponent> header) {
        return null;
    }

    @Override
    protected List<Element> makeFooterElements(final Container<AbstractComponent> footer) {
        return null;
    }

}
