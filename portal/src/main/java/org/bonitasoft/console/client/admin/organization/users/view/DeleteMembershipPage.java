/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.console.client.admin.organization.users.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.console.client.admin.organization.group.GroupListingAdminPage;
import org.bonitasoft.console.client.admin.organization.role.RoleListingPage;
import org.bonitasoft.console.client.admin.organization.users.action.DeleteMembershipAction;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.ui.Page;
import org.bonitasoft.web.toolkit.client.ui.action.HistoryBackAction;
import org.bonitasoft.web.toolkit.client.ui.component.Button;
import org.bonitasoft.web.toolkit.client.ui.component.Paragraph;
import org.bonitasoft.web.toolkit.client.ui.component.button.ButtonAction;

/**
 * @author Séverin Moussel
 */
public class DeleteMembershipPage extends Page {

    private static final String PARAMETER_MEMBERSHIP_ID = "id";

    public final static String TOKEN = "deletemembership";
    
    public static final List<String> PRIVILEGES = new ArrayList<String>();
    
    static {
        PRIVILEGES.add(UserListingAdminPage.TOKEN);
        PRIVILEGES.add(GroupListingAdminPage.TOKEN);
        PRIVILEGES.add(RoleListingPage.TOKEN);
    }

    public DeleteMembershipPage() {
    }

    public DeleteMembershipPage(APIID membershipId) {
        addParameter(PARAMETER_MEMBERSHIP_ID, membershipId.toString());
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    public void defineTitle() {
        this.setTitle(_("Confirm deletion ?"));
    }

    @Override
    public void buildView() {
        final DeleteMembershipAction deleteAction = new DeleteMembershipAction();
        deleteAction.setParameters(getParameters());

        addBody(
                new Paragraph(_("Are you sure you want to delete this membership ?")),
                new Paragraph("\n\n"),
                new ButtonAction(_("Delete"), _("Delete this membership"), deleteAction),
                new Button(_("Cancel"), _("Do not delete this membership"), new HistoryBackAction()));

    }
}
