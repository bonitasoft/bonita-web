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

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bonitasoft.console.client.admin.process.action.DeleteProcessAction;
import org.bonitasoft.console.client.admin.process.action.DisableProcessAction;
import org.bonitasoft.console.client.admin.process.action.EnableProcessAction;
import org.bonitasoft.console.client.admin.process.view.section.cases.CasesSection;
import org.bonitasoft.console.client.admin.process.view.section.category.CategoriesSection;
import org.bonitasoft.console.client.admin.process.view.section.configuration.ProcessConfigurationStateResolver;
import org.bonitasoft.console.client.admin.process.view.section.connector.ConnectorSection;
import org.bonitasoft.console.client.admin.process.view.section.entitymapping.EntityMappingSection;
import org.bonitasoft.console.client.common.metadata.ProcessMetadataBuilder;
import org.bonitasoft.web.rest.model.bpm.process.ProcessDefinition;
import org.bonitasoft.web.rest.model.bpm.process.ProcessItem;
import org.bonitasoft.web.rest.model.bpm.process.ProcessResolutionProblemDefinition;
import org.bonitasoft.web.rest.model.bpm.process.ProcessResolutionProblemItem;
import org.bonitasoft.web.toolkit.client.common.json.JSonItemReader;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.common.util.MapUtil;
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

    public static final String TOKEN = "processmoredetailsadmin";
    
    public static final List<String> PRIVILEGES = new ArrayList<String>();
    
    static {
        PRIVILEGES.add(ProcessListingAdminPage.TOKEN);
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
        addToolbarLink(new ButtonBack());
        if (!process.isEnabled()) {
            addToolbarLink(newDeleteProcessButton(process));
        }
        addToolbarLink(newChangeProcessActivationStateButton(process));
    }

    private Clickable newDeleteProcessButton(final ProcessItem process) {
        return new ButtonAction("btn-delete", _("Delete"), _("Delete this app"),
                new DeleteProcessAction(process.getId().toString(), process.getDisplayName()));
    }

    private Clickable newChangeProcessActivationStateButton(final ProcessItem process) {
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
        return new ButtonPrimaryAction("btn-enable", _("Enable"), _("Enable this app"), new EnableProcessAction(process.getId()));
    }

    private Clickable newDisableProcessButton(final ProcessItem process) {
        return new ButtonAction("btn-disable", _("Disable"), _("Disable this app"), new DisableProcessAction(process.getId()));
    }

    @Override
    protected void buildBody(final ProcessItem item) {
        ProcessResolutionProblemDefinition.get().getAPICaller()
                .search(0, 100, null, null, MapUtil.asMap(new Arg(ProcessResolutionProblemItem.FILTER_PROCESS_ID, item.getId())),
                        new APICallback() {

                            @Override
                            public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
                                final List<ProcessResolutionProblemItem> processResolutionErrors = JSonItemReader.parseItems(response,
                                        ProcessResolutionProblemDefinition.get());
                                buildBody(item, new ProcessConfigurationStateResolver(processResolutionErrors));
                            }
                        });
    }

    protected void buildBody(final ProcessItem process, final ProcessConfigurationStateResolver stateResolver) {
        addBody(new EntityMappingSection(process, stateResolver.getActorsConfigurationState()));
        addBody(getConnectorSection(process, stateResolver));
        addBody(new CategoriesSection(process));
        addBody(new CasesSection(process));
    }

    /**
     * Overriden in SP
     */
    protected ConnectorSection getConnectorSection(final ProcessItem process, final ProcessConfigurationStateResolver stateResolver) {
        return new ConnectorSection(process, stateResolver.getConnectorsConfigurationState());
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }
}
