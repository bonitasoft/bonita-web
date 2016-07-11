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
package org.bonitasoft.console.client.menu.view.technicaluser;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.console.client.admin.organization.OrganizationImportAndExportPage;
import org.bonitasoft.console.client.admin.organization.group.GroupListingAdminPage;
import org.bonitasoft.console.client.admin.organization.role.RoleListingPage;
import org.bonitasoft.console.client.admin.organization.users.view.UserListingAdminPage;
import org.bonitasoft.console.client.admin.profile.view.ProfileListingPage;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.component.menu.MenuFolder;
import org.bonitasoft.web.toolkit.client.ui.component.menu.MenuLink;

/**
 * @author Colin PUY
 * 
 */
public class OrganizationMenuItem extends MenuFolder {

    public OrganizationMenuItem() {
        super(new JsId("Organization"), _("Organization"));
        addMenuItem(new MenuLink(new JsId(UserListingAdminPage.TOKEN), _("Users"), _("Show the user list"), UserListingAdminPage.TOKEN));
        addMenuItem(new MenuLink(new JsId(GroupListingAdminPage.TOKEN), _("Groups"), _("Show the group list"), GroupListingAdminPage.TOKEN));
        addMenuItem(new MenuLink(new JsId(RoleListingPage.TOKEN), _("Roles"), _("Show the role list"), RoleListingPage.TOKEN));
        addMenuItem(new MenuLink(new JsId(OrganizationImportAndExportPage.TOKEN), _("Import / Export"), _("Import or export data"),
                OrganizationImportAndExportPage.TOKEN));
        addMenuItem(new MenuLink(new JsId(ProfileListingPage.TOKEN), _("Profiles"), _("Show all profiles of portal"), ProfileListingPage.TOKEN));
    }

}
