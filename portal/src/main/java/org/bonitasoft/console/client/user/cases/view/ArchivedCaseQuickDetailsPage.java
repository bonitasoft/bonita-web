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
import org.bonitasoft.console.client.user.cases.view.component.CaseOverviewButton;
import org.bonitasoft.console.client.user.cases.view.snippet.ArchivedTasksSection;
import org.bonitasoft.web.rest.model.bpm.cases.ArchivedCaseDefinition;
import org.bonitasoft.web.rest.model.bpm.cases.ArchivedCaseItem;
import org.bonitasoft.web.toolkit.client.ui.action.ActionShowView;
import org.bonitasoft.web.toolkit.client.ui.component.Button;
import org.bonitasoft.web.toolkit.client.ui.component.Clickable;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemDetailsMetadata;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemQuickDetailsPage;

/**
 * @author Julien Mege
 *
 */
public class ArchivedCaseQuickDetailsPage extends ItemQuickDetailsPage<ArchivedCaseItem> implements PluginCase {

    public static String TOKEN = "archivedcasequickdetails";

    public static final List<String> PRIVILEGES = new ArrayList<String>();

    static {
        PRIVILEGES.add(CaseListingPage.TOKEN);
    }

    public ArchivedCaseQuickDetailsPage() {
        super(ArchivedCaseDefinition.get());
    }

    @Override
    protected void defineTitle(final ArchivedCaseItem item) {
        setTitle(_("Case id: ") + item.getSourceObjectId() + " - App: " + item.getProcess().getDisplayName());
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
    protected LinkedList<ItemDetailsMetadata> defineMetadatas(final ArchivedCaseItem item) {
        MetadataCaseBuilder metadatas = new MetadataCaseBuilder();
        metadatas.addAppsVersion();
        metadatas.addState();
        metadatas.addStartDate();
        metadatas.addStartedBy(item);
        return metadatas.build();
    }

    @Override
    protected void buildToolbar(ArchivedCaseItem item) {
        addToolbarLink(new CaseOverviewButton(item));
        addToolbarLink(moreButton(item));
    }

    private Clickable moreButton(final ArchivedCaseItem item) {
        return new Button("btn-more", _("More"), _("Getting more information about the specified case"),
                new ActionShowView(new ArchivedCaseMoreDetailsPage(item)));
    }

    @Override
    protected void buildBody(final ArchivedCaseItem item) {
        addBody(new ArchivedTasksSection(_("Done tasks"), item, 5));

        addBody(new CommentSectionSnippet(item.getSourceObjectId(), true)
                .setNbLinesByPage(5)
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
