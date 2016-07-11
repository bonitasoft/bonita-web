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

import org.bonitasoft.console.client.admin.organization.users.view.UserListingAdminPage;
import org.bonitasoft.console.client.menu.view.navigation.NavigationMenuView;
import org.bonitasoft.console.client.menu.view.technicaluser.BDMMenuItem;
import org.bonitasoft.console.client.menu.view.technicaluser.BPMServicesMenuItem;
import org.bonitasoft.console.client.menu.view.technicaluser.OrganizationMenuItem;
import org.bonitasoft.console.client.menu.view.technicaluser.ResourcesMenuItem;
import org.bonitasoft.web.toolkit.client.ClientApplicationURL;
import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.ui.component.form.view.BlankPage;

/**
 * @author Julien Mege
 */

public class TechnicalUserMenuView extends NavigationMenuView {

    @Override
    public void buildView() {
        buildMenu();
        displayFirstPage();
        addBody(navigationMenu);
        listenViewChangeEvent(selectMenuOnChange());
        navigationMenu.select(ViewController.getInstance().getCurrentPageToken());
    }

    protected void displayFirstPage() {
        // Open the first page if no page already displayed
        if (BlankPage.TOKEN.equals(ClientApplicationURL.getPageToken()) || ClientApplicationURL.getPageToken() == null) {
            ClientApplicationURL.setPageToken(UserListingAdminPage.TOKEN, true);
        }
    }

    protected void buildMenu() {
        navigationMenu.addMenuItem(new BPMServicesMenuItem(), new OrganizationMenuItem(), new BDMMenuItem(), new ResourcesMenuItem());
    }
}
