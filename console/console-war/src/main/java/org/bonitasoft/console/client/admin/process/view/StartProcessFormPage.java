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
package org.bonitasoft.console.client.admin.process.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bonitasoft.console.client.user.application.view.ProcessListingPage;
import org.bonitasoft.console.client.user.cases.view.FormsView;
import org.bonitasoft.web.rest.model.bpm.process.ProcessItem;
import org.bonitasoft.web.toolkit.client.ClientApplicationURL;
import org.bonitasoft.web.toolkit.client.Session;
import org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.ui.Page;
import org.bonitasoft.web.toolkit.client.ui.component.containers.Container;
import org.bonitasoft.web.toolkit.client.ui.component.core.AbstractComponent;
import org.bonitasoft.web.toolkit.client.ui.component.core.UiComponent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Element;

/**
 * @author Ruiheng Fan, Haojie Yuan
 * 
 */
public class StartProcessFormPage extends Page {

    public final static String TOKEN = "StartProcess";

    public static final List<String> PRIVILEGES = new ArrayList<String>();

    static {
        PRIVILEGES.add(ProcessListingPage.TOKEN);
    }

    private final String UUID_SEPERATOR = "--";

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
        final String decodedProcessName = URL.encodeQueryString(processName);
        final String processVersion = URL.encodeQueryString(this.getParameter(ProcessItem.ATTRIBUTE_VERSION));
        final String processId = URL.encodeQueryString(this.getParameter(ProcessItem.ATTRIBUTE_ID));

        final String locale = AbstractI18n.getDefaultLocale().toString();

        String userId = this.getParameter("userId");
        if (userId == null) {
            userId = Session.getUserId().toString();
        }
        this.setTitle(_("Start an instance of app %app_name%", new Arg("app_name", decodedProcessName)));
        StringBuilder frameURL = new StringBuilder();

        frameURL.append(GWT.getModuleBaseURL())
                .append("homepage?ui=form&locale=")
                .append(locale);

        // if tenant is filled in portal url add tenant parameter to IFrame url
        String tenantId = ClientApplicationURL.getTenantId();
        if (tenantId != null && !tenantId.isEmpty()) {
            frameURL.append("&tenant=").append(tenantId);
        }

        frameURL.append("#form=")
                .append(processName)
                .append(UUID_SEPERATOR)
                .append(processVersion)
                .append("$entry&process=")
                .append(processId)
                .append("&autoInstantiate=false&mode=form&user=")
                .append(userId);

        addBody(new UiComponent(new FormsView(frameURL.toString())));
    }

    @Override
    protected List<Element> makeHeaderElements(final Container<AbstractComponent> header) {
        return null;
    }

    @Override
    protected List<Element> makeFooterElements(final Container<AbstractComponent> footer) {
        return null;
    }

    public static final Map<String, String> getItemParams(final IItem item) {
        final Map<String, String> processParams = new HashMap<String, String>();
        processParams.put(ProcessItem.ATTRIBUTE_NAME, item.getAttributeValue(ProcessItem.ATTRIBUTE_NAME));
        processParams.put(ProcessItem.ATTRIBUTE_VERSION, item.getAttributeValue(ProcessItem.ATTRIBUTE_VERSION));
        processParams.put(ProcessItem.ATTRIBUTE_ID, item.getAttributeValue(ProcessItem.ATTRIBUTE_ID));
        processParams.put("token", TOKEN);
        return processParams;
    }

}
