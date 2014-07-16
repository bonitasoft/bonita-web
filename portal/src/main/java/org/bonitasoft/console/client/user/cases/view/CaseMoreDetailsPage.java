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

import org.bonitasoft.console.client.user.application.view.ProcessListingPage;
import org.bonitasoft.console.client.user.cases.view.component.CaseOverviewButton;
import org.bonitasoft.console.client.user.task.view.more.HumanTaskMoreDetailsPage;
import org.bonitasoft.web.rest.model.bpm.cases.CaseItem;
import org.bonitasoft.web.toolkit.client.ui.CssClass;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.HistoryBackAction;
import org.bonitasoft.web.toolkit.client.ui.action.RedirectionAction;
import org.bonitasoft.web.toolkit.client.ui.component.Button;
import org.bonitasoft.web.toolkit.client.ui.component.button.ButtonBack;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTable;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemDetailsAction;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemDetailsMetadata;

/**
 * @author Paul AMAR
 *
 */
public class CaseMoreDetailsPage extends CaseQuickDetailsPage implements PluginCase {

    public static final String TOKEN = "casemoredetails";

    public static final List<String> PRIVILEGES = new ArrayList<String>();

    static {
        PRIVILEGES.add(CaseListingPage.TOKEN);
        PRIVILEGES.add(ProcessListingPage.TOKEN);
    }

    public CaseMoreDetailsPage() {
        addClass(CssClass.MORE_DETAILS);
    }

    public CaseMoreDetailsPage(final CaseItem caseItem) {
        this();
        addParameter(PARAMETER_ITEM_ID, caseItem.getId().toString());
    }

    @Override
    protected boolean isDescriptionBeforeMetadatas() {
        return false;
    }

    @Override
    protected LinkedList<ItemDetailsMetadata> defineMetadatas(final CaseItem item) {
        final LinkedList<ItemDetailsMetadata> metadatas = super.defineMetadatas(item);
        metadatas.add(lastUpdateDate());
        metadatas.add(state());
        return metadatas;
    }

    private ItemDetailsMetadata lastUpdateDate() {
        return new ItemDetailsMetadata(CaseItem.ATTRIBUTE_LAST_UPDATE_DATE, _("Last updated"),
        		_("The date when the case was updated"));
    }

    private ItemDetailsMetadata state() {
        return new ItemDetailsMetadata(CaseItem.ATTRIBUTE_STATE, _("State"), _("The current state of the case"));
    }

    @Override
    protected void prepareCommentsTable(final ItemTable commentsTable) {
        commentsTable.setNbLinesByPage(10);
    }

    @Override
    protected void preparetasksTable(final ItemTable subtasksTable) {
        subtasksTable.setNbLinesByPage(10);
        subtasksTable.setDefaultAction(new RedirectionAction(HumanTaskMoreDetailsPage.TOKEN));
    }

    @Override
    protected void buildToolbar(final CaseItem item) {
        addToolbarLink(new ButtonBack());
        addToolbarLink(new CaseOverviewButton(item));
    }

    @Override
    protected LinkedList<ItemDetailsAction> defineActions(final CaseItem item) {
        addToolbarLink(newBackButton());
        return new LinkedList<ItemDetailsAction>();
    }

    private Button newBackButton() {
        return new Button(new JsId("back"), _("Back"), _("Go back to previous view"), new HistoryBackAction());
    }

    @Override
    public String defineToken() {
        return TOKEN;

    }

    @Override
    public String getPluginToken() {
        return PLUGIN_TOKEN;
    }
}
