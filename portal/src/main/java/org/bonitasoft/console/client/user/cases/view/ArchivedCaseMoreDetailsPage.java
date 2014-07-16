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
package org.bonitasoft.console.client.user.cases.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bonitasoft.console.client.common.component.snippet.CommentSectionSnippet;
import org.bonitasoft.console.client.common.metadata.MetadataCaseBuilder;
import org.bonitasoft.console.client.user.application.view.ProcessListingPage;
import org.bonitasoft.console.client.user.cases.view.component.CaseOverviewButton;
import org.bonitasoft.console.client.user.cases.view.snippet.ArchivedTasksSection;
import org.bonitasoft.web.rest.model.bpm.cases.ArchivedCaseDefinition;
import org.bonitasoft.web.rest.model.bpm.cases.ArchivedCaseItem;
import org.bonitasoft.web.toolkit.client.ui.CssClass;
import org.bonitasoft.web.toolkit.client.ui.component.button.ButtonBack;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemDetailsMetadata;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemQuickDetailsPage;

/**
 * @author Séverin Moussel
 *
 */
public class ArchivedCaseMoreDetailsPage extends ItemQuickDetailsPage<ArchivedCaseItem> implements PluginCase {

    public static final String TOKEN = "archivedcasemoredetails";

    public static final List<String> PRIVILEGES = new ArrayList<String>();

    static {
        PRIVILEGES.add(CaseListingPage.TOKEN);
        PRIVILEGES.add(ProcessListingPage.TOKEN);
    }

    public ArchivedCaseMoreDetailsPage() {
        super(ArchivedCaseDefinition.get());
        addClass(CssClass.MORE_DETAILS);
    }

    public ArchivedCaseMoreDetailsPage(final ArchivedCaseItem item) {
        this();
        addParameter("id", item.getId().toString());
    }

    @Override
    protected List<String> defineDeploys() {
        final List<String> defineDeploys = new ArrayList<String>();
        defineDeploys.add(ArchivedCaseItem.ATTRIBUTE_STARTED_BY_USER_ID);
        defineDeploys.add(ArchivedCaseItem.ATTRIBUTE_STARTED_BY_SUBSTITUTE_USER_ID);
        defineDeploys.add(ArchivedCaseItem.ATTRIBUTE_PROCESS_ID);
        return defineDeploys;
    }

    @Override
    protected void defineTitle(final ArchivedCaseItem item) {
        setTitle(_("Case id: ") + item.getSourceObjectId() + " - App: " + item.getProcess().getDisplayName());
    }

    @Override
    protected boolean isDescriptionBeforeMetadatas() {
        return false;
    }

    @Override
    protected LinkedList<ItemDetailsMetadata> defineMetadatas(final ArchivedCaseItem item) {
        final MetadataCaseBuilder metadatas = new MetadataCaseBuilder();
        metadatas.addAppsVersion();
        metadatas.addStartDate();
        metadatas.addStartedBy(item);
        metadatas.addLastUpdateDate();
        metadatas.addState();
        return metadatas.build();
    }

    @Override
    protected void buildToolbar(final ArchivedCaseItem item) {
        addToolbarLink(new ButtonBack());
        addToolbarLink(new CaseOverviewButton(item));
    }

    @Override
    protected void buildBody(final ArchivedCaseItem item) {
        addBody(new ArchivedTasksSection(_("Done tasks"), item, 10));
        addBody(new CommentSectionSnippet(item.getSourceObjectId(), true)
                .build());
    }

    @Override
    public String getPluginToken() {
        return PLUGIN_TOKEN;
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }
}
