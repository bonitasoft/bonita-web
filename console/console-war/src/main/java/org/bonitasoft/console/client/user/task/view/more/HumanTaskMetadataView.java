/**
 * Copyright (C) 2014 BonitaSoft S.A.
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

import static org.bonitasoft.console.client.uib.databinder.DataBinder.bind;
import static org.bonitasoft.console.client.uib.databinder.DataFactory.createAssignedTo;
import static org.bonitasoft.console.client.uib.databinder.DataFactory.createDate;
import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;
import static org.bonitasoft.web.toolkit.client.ui.utils.DateFormat.FORMAT.DISPLAY;
import static org.bonitasoft.web.toolkit.client.ui.utils.DateFormat.FORMAT.DISPLAY_RELATIVE;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import org.bonitasoft.console.client.uib.databinder.Data;
import org.bonitasoft.console.client.uib.formatter.FormatterFactory;
import org.bonitasoft.forms.client.view.common.URLUtils;
import org.bonitasoft.web.rest.model.bpm.flownode.IHumanTaskItem;
import org.bonitasoft.web.toolkit.client.common.url.UrlOption;

public class HumanTaskMetadataView extends Composite {

    @UiField(provided = true)
    MetadataMessages messages = new MetadataMessages();
    @UiField
    DivElement appName;
    @UiField
    DivElement appVersion;
    @UiField
    AnchorElement caseId;
    @UiField
    DivElement state;
    @UiField
    DivElement priority;
    @UiField
    DivElement assignedTo;
    @UiField
    DivElement dueDate;
    @UiField
    DivElement lastUpdateDate;
    @UiField
    DivElement assignedDate;
    @UiField
    ParagraphElement description;
    @UiField
    HeadingElement name;

    interface Binder extends UiBinder<HTMLPanel, HumanTaskMetadataView> {
    }

    private static Binder binder = GWT.create(Binder.class);

    public HumanTaskMetadataView(final IHumanTaskItem task) {
        initWidget(binder.createAndBindUi(this));
        render(task);
    }

    private HumanTaskMetadataView render(final IHumanTaskItem task) {
        bind(new Data(task.ensureName())).to(name);
        bind(new Data(task.getProcess().ensureName())).to(appName);
        bind(new Data(task.getProcess().getVersion())).to(appVersion);
        bind(new Data(task.getCaseId())).to(caseId);
        bind(new Data(task.getState())).to(state);
        bind(new Data(FormatterFactory.formatPriority(task.getPriority()))).to(priority);
        bind(createAssignedTo(task.getAssignedUser())).to(assignedTo);
        bind(createDate(task.getDueDate(), DISPLAY_RELATIVE)).to(dueDate);
        bind(createDate(task.getLastUpdateDate(), DISPLAY)).to(lastUpdateDate);
        bind(createDate(task.getAssignedDate(), DISPLAY)).to(assignedDate);
        bind(new Data(task.ensureDescription()).or(_("No description."))).to(description);

        caseId.setHref("#?id=" + task.getCaseId() +"&_p=casemoredetails&_pf=" + URLUtils.getInstance().getHashParameter(UrlOption.PROFILE));
        return this;
    }
}