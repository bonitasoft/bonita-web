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
package org.bonitasoft.console.client.admin.organization.group.view;

import static java.util.Arrays.asList;
import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.List;

import org.bonitasoft.web.toolkit.client.ui.action.CheckValidSessionBeforeAction;
import org.bonitasoft.web.toolkit.client.ui.action.popup.PopupAction;
import org.bonitasoft.web.toolkit.client.ui.component.Clickable;
import org.bonitasoft.web.toolkit.client.ui.component.Link;

/**
 * @author Julien Mege
 * 
 */
public class GroupListingAdminPage extends GroupListingPage {

    public static final String TOKEN = "grouplistingadmin";

    @Override
    protected List<Clickable> defineFilterPanelActions() {
        return asList(addGroupLink());
    }

    private Clickable addGroupLink() {
        return new Link(_("Create a group"), _("Opens a popup to create a group"), new CheckValidSessionBeforeAction(new PopupAction(AddGroupPage.TOKEN)));
    }

    @Override
    protected GroupQuickDetailsPage getItemQuickDetailPage() {
        return new GroupQuickDetailsAdminPage();
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }
}
