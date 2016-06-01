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
package org.bonitasoft.console.client.user.process.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bonitasoft.console.client.user.cases.view.IFrameView;
import org.bonitasoft.console.client.user.process.action.ProcessInstantiationCallbackBehavior;
import org.bonitasoft.console.client.user.task.action.TaskExecutionCallbackBehavior;
import org.bonitasoft.console.client.user.task.view.TaskExecutionEventListener;
import org.bonitasoft.web.rest.model.bpm.process.ProcessItem;
import org.bonitasoft.web.toolkit.client.ClientApplicationURL;
import org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.common.url.UrlUtil;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.ui.Page;
import org.bonitasoft.web.toolkit.client.ui.component.containers.Container;
import org.bonitasoft.web.toolkit.client.ui.component.core.AbstractComponent;
import org.bonitasoft.web.toolkit.client.ui.component.core.UiComponent;

import com.google.gwt.user.client.Element;

/**
 * @author Ruiheng Fan, Haojie Yuan
 *
 */
public class StartProcessFormPage extends Page {

    public static final String ATTRIBUTE_USER_ID = "userId";

    public final static String TOKEN = "StartProcess";

    public static final List<String> PRIVILEGES = new ArrayList<String>();

    static {
        PRIVILEGES.add(ProcessListingPage.TOKEN);
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
        final String encodedProcessName = UrlUtil.escapePathSegment(processName);
        final String processVersion = this.getParameter(ProcessItem.ATTRIBUTE_VERSION);
        final String encodedProcessVersion = UrlUtil.escapePathSegment(processVersion);
        final String processId = this.getParameter(ProcessItem.ATTRIBUTE_ID);

        final String locale = AbstractI18n.getDefaultLocale().toString();
        final String userId = this.getParameter(ATTRIBUTE_USER_ID);
        final String tenantId = ClientApplicationURL.getTenantId();

        this.setTitle(_("Start an instance of process %app_name%", new Arg("app_name", processName)));

        final StringBuilder frameURL = new StringBuilder();
        frameURL.append("resource/process/")
                .append(encodedProcessName)
                .append("/")
                .append(encodedProcessVersion)
                .append("/content/?id=")
                .append(processId)
                .append("&locale=")
                .append(locale)
                .append("&autoInstantiate=false");
        // if tenant is filled in portal url add tenant parameter to IFrame url
        if (tenantId != null && !tenantId.isEmpty()) {
            frameURL.append("&tenant=").append(tenantId);
        }
        if (userId != null && !userId.isEmpty()) {
            frameURL.append("&user=").append(userId);
        }

        addBody(new UiComponent(createIFrame(frameURL)));
    }

    protected IFrameView createIFrame(final StringBuilder frameURL) {
        //also adding TaskExecutionEventListener as with 6.x forms you may display a task form in the same iframe by following the confirmation message link
        return new IFrameView(frameURL.toString(), new ProcessInstantiationEventListener(new ProcessInstantiationCallbackBehavior()),
                new TaskExecutionEventListener(new TaskExecutionCallbackBehavior()));
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
        processParams.put(ProcessItem.ATTRIBUTE_DISPLAY_NAME, item.getAttributeValue(ProcessItem.ATTRIBUTE_DISPLAY_NAME));
        processParams.put(ProcessItem.ATTRIBUTE_VERSION, item.getAttributeValue(ProcessItem.ATTRIBUTE_VERSION));
        processParams.put(ProcessItem.ATTRIBUTE_ID, item.getAttributeValue(ProcessItem.ATTRIBUTE_ID));
        processParams.put("token", TOKEN);
        return processParams;
    }

}
