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
package org.bonitasoft.console.client.admin.bpm.cases.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.console.client.common.component.button.MoreButton;
import org.bonitasoft.console.client.user.cases.view.component.CaseOverviewButton;
import org.bonitasoft.web.rest.model.bpm.cases.CaseItem;
import org.bonitasoft.web.rest.model.bpm.flownode.HumanTaskDefinition;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.action.ActionShowView;
import org.bonitasoft.web.toolkit.client.ui.action.CheckValidSessionBeforeAction;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemQuickDetailsPage;

/**
 * @author Nicolas Tith
 *
 */
public class CaseQuickDetailsAdminPage extends AbstractCaseQuickDetailsAdminPage<CaseItem> {

    public static String TOKEN = "casequickdetailsadmin";

    public static final List<String> PRIVILEGES = new ArrayList<String>();

    static {
        PRIVILEGES.add(CaseListingAdminPage.TOKEN);
    }

    public CaseQuickDetailsAdminPage() {
        super(false);
    }

    @Override
    protected void buildToolbar(final CaseItem item) {
        addToolbarLink(new CaseOverviewButton(item));
        addToolbarLink(new MoreButton(_("Getting more information about the specified case"),
                createMoreDetailsAction(item)));
    }

    protected Action createMoreDetailsAction(final CaseItem item) {
        return new CheckValidSessionBeforeAction(new ActionShowView(getCaseMoreDetailsPage(item)));
    }

	protected ItemQuickDetailsPage<CaseItem> getCaseMoreDetailsPage(final CaseItem item) {
		return new CaseMoreDetailsAdminPage(item.getId());
	}

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    protected String getMoreDetailsPageToken() {
        return CaseMoreDetailsAdminPage.TOKEN;
    }

    @Override
    protected ItemDefinition getHumanTasksDefinition() {
        return Definitions.get(HumanTaskDefinition.TOKEN);
    }

}
