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
package org.bonitasoft.console.client.admin.organization.users.view;


import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bonitasoft.console.client.common.component.button.MoreButton;
import org.bonitasoft.console.client.common.metadata.UserMetadataBuilder;
import org.bonitasoft.web.rest.model.identity.UserItem;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.action.ActionShowView;
import org.bonitasoft.web.toolkit.client.ui.action.CheckValidSessionBeforeAction;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemDetailsMetadata;
import org.bonitasoft.web.toolkit.client.ui.utils.DateFormat.FORMAT;

/**
 * @author Paul AMAR
 * @author Colin PUY
 */
public class UserQuickDetailsAdminPage extends UserQuickDetailsPage {

    public static final String TOKEN = "userquickdetailsadmin";
    
    public static final List<String> PRIVILEGES = new ArrayList<String>();
    
    static {
        PRIVILEGES.add(UserListingAdminPage.TOKEN);
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    protected void buildToolbar(UserItem item) {
        addToolbarLink(new MoreButton(_("Show more details about this user"), createMoreAction(item)));
    }

    protected Action createMoreAction(final UserItem item) {
        return new CheckValidSessionBeforeAction(new ActionShowView(new UserMoreDetailsAdminPage(item.getId())));
    }

    @Override
    protected LinkedList<ItemDetailsMetadata> defineMetadatas(final UserItem item) {
        UserMetadataBuilder metadatas = new UserMetadataBuilder();
        metadatas.addEmail();
        metadatas.addUserName();
        metadatas.addManager();
        metadatas.addLastConnectionDate(FORMAT.DISPLAY_RELATIVE);
        metadatas.addLastUpdateDate();
        return metadatas.build();
    }
}
