/*******************************************************************************
 * Copyright (C) 2009, 2013 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.console.client.user.cases.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import org.bonitasoft.console.client.admin.bpm.cases.view.CaseListingAdminPage;
import org.bonitasoft.console.client.admin.process.view.ProcessListingAdminPage;
import org.bonitasoft.console.client.user.application.view.ProcessListingPage;
import org.bonitasoft.web.rest.model.bpm.cases.ArchivedCaseItem;
import org.bonitasoft.web.rest.model.bpm.cases.CaseItem;
import org.bonitasoft.web.rest.model.bpm.process.ProcessItem;
import org.bonitasoft.web.toolkit.client.ClientApplicationURL;
import org.bonitasoft.web.toolkit.client.Session;
import org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.ui.Page;
import org.bonitasoft.web.toolkit.client.ui.component.IFrame;
import org.bonitasoft.web.toolkit.client.ui.component.button.ButtonBack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;


/**
 * @author Fabio Lombardi
 * 
 */
public class DisplayCaseFormPage extends Page {

    public final static String TOKEN = "DisplayCaseForm";

    public static final List<String> PRIVILEGES = new ArrayList<String>();

    static {
        PRIVILEGES.add(CaseListingAdminPage.TOKEN);
        PRIVILEGES.add(CaseListingPage.TOKEN);
        PRIVILEGES.add(ProcessListingPage.TOKEN);
        PRIVILEGES.add(ProcessListingAdminPage.TOKEN);
        PRIVILEGES.add("reportlistingadminext");
    }

    private final String UUID_SEPERATOR = "--";

    // legacy, needed by ConsoleFactoryClient
    public DisplayCaseFormPage() {
        this.addClass("moredetails");
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

        final String processName = this.getParameter(ProcessItem.ATTRIBUTE_NAME);
        // TODO remove this once the same method is used in the toolkit and in the studio to URL encode/decode
        final String decodedProcessName = URL.decodeQueryString(processName);
        final String processVersion = this.getParameter(ProcessItem.ATTRIBUTE_VERSION);
        String caseId = this.getParameter(ArchivedCaseItem.ATTRIBUTE_SOURCE_OBJECT_ID);
        if (caseId == null) {
            caseId = this.getParameter(CaseItem.ATTRIBUTE_ID);
        }
        final String locale = AbstractI18n.getDefaultLocale().toString();

        String userId = this.getParameter("userId");
        if (userId == null) {
            userId = Session.getUserId().toString();
        }
        this.setTitle(_("Display a case form of app %app_name%", new Arg("app_name", decodedProcessName)));

        final StringBuilder frameURL = new StringBuilder();
        frameURL.append(GWT.getModuleBaseURL())
                .append("homepage?ui=form&locale=")
                .append(locale);

        // if tenant is filled in portal url add tenant parameter to IFrame url
        final String tenantId = ClientApplicationURL.getTenantId();
        if (tenantId != null && !tenantId.isEmpty()) {
            frameURL.append("&tenant=").append(tenantId);
        }

        frameURL.append("#form=")
                .append(processName)
                .append(this.UUID_SEPERATOR)
                .append(processVersion)
                .append("$recap&mode=form&instance=")
                .append(caseId).append("&recap=true");

        this.addToolbarLink(new ButtonBack());
        this.addBody(new IFrame("formframe", frameURL.toString(), "100%", "700px"));
    }

    public static final Map<String, String> getItemParams(final CaseItem item) {
        if (item.getDeploy(CaseItem.ATTRIBUTE_PROCESS_ID) == null) {
            throw new RuntimeException(CaseItem.ATTRIBUTE_PROCESS_ID + " attribute need to be deployed");
        }
        final Map<String, String> processParams = new HashMap<String, String>();
        processParams.put(ProcessItem.ATTRIBUTE_NAME, item.getProcess().getName());
        processParams.put(ProcessItem.ATTRIBUTE_VERSION, item.getProcess().getVersion());
        processParams.put(CaseItem.ATTRIBUTE_ID, item.getId().toString());
        if (item instanceof ArchivedCaseItem) {
            processParams.put(ArchivedCaseItem.ATTRIBUTE_SOURCE_OBJECT_ID, ((ArchivedCaseItem) item).getSourceObjectId().toString());
        }
        processParams.put("token", TOKEN);
        return processParams;
    }

}
