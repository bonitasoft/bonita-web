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
package org.bonitasoft.console.client.menu.view;

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.console.client.SHA1;
import org.bonitasoft.console.client.admin.organization.OrganizationImportAndExportPage;
import org.bonitasoft.console.client.admin.organization.role.RoleListingPage;
import org.bonitasoft.console.client.admin.organization.users.view.UserListingAdminPage;
import org.bonitasoft.console.client.admin.profile.view.ListProfilePage;
import org.bonitasoft.console.client.menu.view.technicaluser.OrganizationMenuItem;
import org.bonitasoft.console.client.menu.view.technicaluser.PortalMenuItem;
import org.bonitasoft.web.toolkit.client.ClientApplicationURL;
import org.bonitasoft.web.toolkit.client.Session;
import org.bonitasoft.web.toolkit.client.ui.RawView;
import org.bonitasoft.web.toolkit.client.ui.component.form.view.BlankPage;
import org.bonitasoft.web.toolkit.client.ui.component.menu.Menu;

/**
 * @author Julien Mege
 */
public class TechnicalUserMenuView extends RawView {

    public static final String TOKEN = "technicalusermenu";

    @Override
    public void buildView() {
        addBody(buildMenu());

        // Open the first page if no page already displayed
        if (BlankPage.TOKEN.equals(ClientApplicationURL.getPageToken()) || ClientApplicationURL.getPageToken() == null) {
            ClientApplicationURL.setPageToken(UserListingAdminPage.TOKEN, true);
        }

    }

    protected Menu buildMenu() {
        return new Menu(new OrganizationMenuItem(), new PortalMenuItem());
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }
}
