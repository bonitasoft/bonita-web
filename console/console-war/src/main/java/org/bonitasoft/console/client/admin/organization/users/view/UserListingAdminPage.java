/**
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * Copyright (C) 2012 BonitaSoft S.A.
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
package org.bonitasoft.console.client.admin.organization.users.view;

import static java.util.Arrays.asList;
import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.console.client.admin.organization.OrganizationImportAndExportPage;
import org.bonitasoft.web.toolkit.client.ui.action.ActionShowPopup;
import org.bonitasoft.web.toolkit.client.ui.action.CheckValidSessionBeforeAction;
import org.bonitasoft.web.toolkit.client.ui.component.Clickable;
import org.bonitasoft.web.toolkit.client.ui.component.Link;

/**
 * @author Paul AMAR
 * 
 */
public class UserListingAdminPage extends AbstractUserListingPage {

    public static final String TOKEN = "userlistingadmin";
    
    public static final List<String> PRIVILEGES = new ArrayList<String>();
    
    static {
        PRIVILEGES.add(UserListingAdminPage.TOKEN);
    }

    @Override
    protected List<Clickable> defineFilterPanelActions() {
        return asList(addUserLink());
    }

    private Clickable addUserLink() {
        return new Link(_("Create a user"), _("Opens a popup to create a user"), new CheckValidSessionBeforeAction(new ActionShowPopup(new PopupAddUserPage())));
    }

    @Override
    protected UserQuickDetailsPage getItemQuickDetailPage() {
        return new UserQuickDetailsAdminPage();
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }
}
