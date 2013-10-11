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

import org.bonitasoft.console.client.admin.organization.users.action.ChangeUsersStateAction;
import org.bonitasoft.console.client.admin.organization.users.action.ChangeUsersStateAction.STATE;
import org.bonitasoft.console.client.common.component.button.MoreButton;
import org.bonitasoft.console.client.common.metadata.UserMetadataBuilder;
import org.bonitasoft.web.rest.model.identity.UserItem;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.action.ActionShowPopup;
import org.bonitasoft.web.toolkit.client.ui.action.ActionShowView;
import org.bonitasoft.web.toolkit.client.ui.action.CheckValidSessionBeforeAction;
import org.bonitasoft.web.toolkit.client.ui.component.button.ButtonAction;
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
    protected void buildToolbar(UserItem user) {
        if (user.isEnabled()) {
            addToolbarLink(disableButton());
        } else {
            addToolbarLink(enableButton());
        }
        addToolbarLink(new MoreButton(_("Show more details about this user"), createMoreAction(user)));
    }
    
    private ButtonAction enableButton() {
        return new ButtonAction(_("Activate"), _("Activate selected users"), new ChangeUsersStateAction(getItemId(), STATE.ENABLED));
    }
    
    private ButtonAction disableButton() {
        return new ButtonAction(_("Deactivate"), _("Deactivate selected users"), 
                new CheckValidSessionBeforeAction(new ActionShowPopup(new DeactivateUserWarningPopUp(getItemId()))));
        
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
