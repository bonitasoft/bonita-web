/**
 * Copyright (C) 2011 BonitaSoft S.A.
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
package org.bonitasoft.console.client.admin.process.view;

import static org.bonitasoft.web.rest.model.bpm.process.ProcessResolutionProblemItem.FILTER_PROCESS_ID;
import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;
import static org.bonitasoft.web.toolkit.client.common.util.MapUtil.asMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bonitasoft.console.client.admin.process.action.DeleteProcessAction;
import org.bonitasoft.console.client.admin.process.action.DisableProcessAction;
import org.bonitasoft.console.client.admin.process.action.EnableProcessAction;
import org.bonitasoft.console.client.admin.process.view.section.ProcessResolutionProblemsCallout;
import org.bonitasoft.console.client.admin.process.view.section.cases.CasesSection;
import org.bonitasoft.console.client.admin.process.view.section.category.CategoriesSection;
import org.bonitasoft.console.client.admin.process.view.section.configuration.ProcessConfigurationStateResolver;
import org.bonitasoft.console.client.admin.process.view.section.connector.ConnectorSection;
import org.bonitasoft.console.client.admin.process.view.section.entitymapping.EntityMappingSection;
import org.bonitasoft.console.client.admin.process.view.section.parameter.ProcessParametersSection;
import org.bonitasoft.console.client.angular.AngularIFrameView;
import org.bonitasoft.console.client.common.metadata.ProcessMetadataBuilder;
import org.bonitasoft.web.rest.model.bpm.process.ProcessDefinition;
import org.bonitasoft.web.rest.model.bpm.process.ProcessItem;
import org.bonitasoft.web.rest.model.bpm.process.ProcessResolutionProblemDefinition;
import org.bonitasoft.web.rest.model.bpm.process.ProcessResolutionProblemItem;
import org.bonitasoft.web.toolkit.client.common.json.JSonItemReader;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.DescriptionAttributeReader;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.NameAttributeReader;
import org.bonitasoft.web.toolkit.client.ui.CssClass;
import org.bonitasoft.web.toolkit.client.ui.component.Clickable;
import org.bonitasoft.web.toolkit.client.ui.component.button.ButtonAction;
import org.bonitasoft.web.toolkit.client.ui.component.button.ButtonBack;
import org.bonitasoft.web.toolkit.client.ui.component.button.ButtonPrimaryAction;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemDetailsMetadata;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemQuickDetailsPage;

/**
 * @author Colin PUY
 */
public class ProcessMoreDetailsAdminPage extends ItemQuickDetailsPage<ProcessItem> {

    public static final String TOKEN = "gwtprocessmoredetailsadmin";

    public static final List<String> PRIVILEGES = new ArrayList<String>();

    static {
        PRIVILEGES.add(ProcessListingAdminPage.TOKEN);
        PRIVILEGES.add(AngularIFrameView.PROCESS_MORE_DETAILS_ADMIN_TOKEN);
        PRIVILEGES.add("reportlistingadminext");
    }

    public ProcessMoreDetailsAdminPage(final ProcessItem process) {
        this();
        addParameter(PARAMETER_ITEM_ID, process.getId().toString());
    }

    public ProcessMoreDetailsAdminPage() {
        super(ProcessDefinition.get());
        addClass(CssClass.MORE_DETAILS);
    }

    @Override
    protected List<String> defineDeploys() {
        return Arrays.asList(ProcessItem.ATTRIBUTE_DEPLOYED_BY_USER_ID);
    }

    @Override
    protected boolean isDescriptionBeforeMetadatas() {
        return false;
    }

    @Override
    protected LinkedList<ItemDetailsMetadata> defineMetadatas(final ProcessItem item) {
        final ProcessMetadataBuilder metadatas = new ProcessMetadataBuilder();
        metadatas.addVersion();
        metadatas.addConfigurationState();
        metadatas.addLastUpdateDate();
        metadatas.addActivationState();
        metadatas.addInstalledDate();
        metadatas.addInstalledBy();
        return metadatas.build();
    }

    @Override
    protected void defineTitle(final ProcessItem process) {
        setTitle(new NameAttributeReader().read(process));
        addDescription(new DescriptionAttributeReader().read(process));
    }

    @Override
    protected void buildToolbar(final ProcessItem process) {
        super.buildToolbar(process);
        contributeInToolbar(process);
    }

    protected void contributeInToolbar(final ProcessItem process) {
        addToolbarLink(new ButtonBack());
        if (!process.isEnabled()) {
            addToolbarLink(newDeleteProcessButton(process));
        }
        addToolbarLink(newChangeProcessActivationStateButton(process));
    }

    private Clickable newDeleteProcessButton(final ProcessItem process) {
        return new ButtonAction("btn-delete", _("Delete"), _("Delete this process"), new DeleteProcessAction(process.getId().toString(), process.getDisplayName()));
    }

    protected Clickable newChangeProcessActivationStateButton(final ProcessItem process) {
        if (process.isEnabled()) {
            return newDisableProcessButton(process);
        } else {
            final Clickable enableProcessButton = newEnableProcessButton(process);
            if (!process.isResolved()) {
                enableProcessButton.setEnabled(false);
            }
            return enableProcessButton;
        }
    }

    private Clickable newEnableProcessButton(final ProcessItem process) {
        return new ButtonPrimaryAction("btn-enable", _("Enable"), _("Enable this process"), new EnableProcessAction(process.getId()));
    }

    private Clickable newDisableProcessButton(final ProcessItem process) {
        return new ButtonAction("btn-disable", _("Disable"), _("Disable this process"), new DisableProcessAction(process.getId()));
    }

    @Override
    protected void buildBody(final ProcessItem item) {
        new APICaller(ProcessResolutionProblemDefinition.get())
        .search(0, 100, null, null, asMap(new Arg(FILTER_PROCESS_ID, item.getId())), new ProcessResolutionProblemCallback(item));
    }

    private class ProcessResolutionProblemCallback extends APICallback {

        private final ProcessItem process;

        public ProcessResolutionProblemCallback(final ProcessItem process) {
            this.process = process;
        }

        @Override
        public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
            final List<ProcessResolutionProblemItem> processResolutionErrors = JSonItemReader.parseItems(response, ProcessResolutionProblemDefinition.get());
            final ProcessConfigurationStateResolver stateResolver = new ProcessConfigurationStateResolver(processResolutionErrors);
            if (stateResolver.hasProblems()) {
                addHeader(buildProcessResolutionProblemsCallout(stateResolver));
            }
            buildBody(process, stateResolver);
        }

    }

    protected void buildBody(final ProcessItem process, final ProcessConfigurationStateResolver stateResolver) {
        addBody(new EntityMappingSection(process, stateResolver.getActorsConfigurationState()));
        addBody(new ProcessParametersSection(process, stateResolver.getParametersConfigurationState()));
        addBody(buildConnectorSection(process, stateResolver));
        addBody(new CategoriesSection(process));
        addBody(new CasesSection(process));
    }

    /** Overriden in SP */
    protected ConnectorSection buildConnectorSection(final ProcessItem process, final ProcessConfigurationStateResolver stateResolver) {
        return new ConnectorSection(process, stateResolver.getConnectorsConfigurationState());
    }

    /** Overriden in SP */
    protected ProcessResolutionProblemsCallout buildProcessResolutionProblemsCallout(final ProcessConfigurationStateResolver stateResolver) {
        return new ProcessResolutionProblemsCallout(stateResolver);
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }
}
