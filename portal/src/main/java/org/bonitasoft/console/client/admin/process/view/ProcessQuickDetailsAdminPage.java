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
package org.bonitasoft.console.client.admin.process.view;

import static java.util.Arrays.asList;
import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bonitasoft.console.client.admin.process.view.section.configuration.ConfigurationSection;
import org.bonitasoft.console.client.admin.process.view.section.statistics.StatisticsSection;
import org.bonitasoft.console.client.angular.AngularIFrameView;
import org.bonitasoft.console.client.common.component.button.MoreButton;
import org.bonitasoft.console.client.common.metadata.ProcessMetadataBuilder;
import org.bonitasoft.web.rest.model.bpm.process.ProcessDefinition;
import org.bonitasoft.web.rest.model.bpm.process.ProcessItem;
import org.bonitasoft.web.toolkit.client.common.TreeIndexed;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.DescriptionAttributeReader;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.NameAttributeReader;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.action.ActionShowView;
import org.bonitasoft.web.toolkit.client.ui.action.CheckValidSessionBeforeAction;
import org.bonitasoft.web.toolkit.client.ui.page.PageOnItem;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemDetailsMetadata;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemQuickDetailsPage;

/**
 * @author Séverin Moussel
 *
 */
public class ProcessQuickDetailsAdminPage extends ItemQuickDetailsPage<ProcessItem> {

    public static final String TOKEN = "processquickdetailsadmin";

    public static final List<String> PRIVILEGES = new ArrayList<String>();

    static {
        PRIVILEGES.add(ProcessListingAdminPage.TOKEN);
        PRIVILEGES.add("reportlistingadminext");
    }

    public ProcessQuickDetailsAdminPage() {
        super(ProcessDefinition.get());
    }

    @Override
    protected List<String> defineDeploys() {
        return asList(ProcessItem.ATTRIBUTE_DEPLOYED_BY_USER_ID);
    }

    @Override
    protected void defineTitle(final ProcessItem process) {
        setTitle(new NameAttributeReader().read(process) + " (" + process.getVersion() + ")");
        addDescription(new DescriptionAttributeReader().read(process));
    }

    @Override
    protected void buildToolbar(final ProcessItem process) {
        addToolbarLink(new MoreButton(_("Show more details about this process"), createAngularMoreDetailsAction(process)));

    }

    private Action createAngularMoreDetailsAction(final ProcessItem process) {
        final TreeIndexed<String> tree = new TreeIndexed<String>();
        tree.addValue(PageOnItem.PARAMETER_ITEM_ID, process.getId().toString());
        return new CheckValidSessionBeforeAction(new ActionShowView(AngularIFrameView.PROCESS_MORE_DETAILS_ADMIN_TOKEN, tree));
    }

    @Override
    protected LinkedList<ItemDetailsMetadata> defineMetadatas(final ProcessItem process) {
        final ProcessMetadataBuilder metadatas = new ProcessMetadataBuilder();
        metadatas.addInstalledDate();
        metadatas.addInstalledBy();
        return metadatas.build();
    }

    @Override
    protected void buildBody(final ProcessItem process) {
        addBody(new StatisticsSection(process));
        addBody(getConfigurationSection(process));
    }

    /** Overridden in SP */
    protected ConfigurationSection getConfigurationSection(final ProcessItem process) {
        return new ConfigurationSection(process);
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }
}
