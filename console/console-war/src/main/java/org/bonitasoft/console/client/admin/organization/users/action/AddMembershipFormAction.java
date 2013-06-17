/**
 * Copyright (C) 2011 BonitaSoft S.A.
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
package org.bonitasoft.console.client.admin.organization.users.action;

import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.console.client.admin.organization.users.view.ListMembershipPage;
import org.bonitasoft.web.rest.api.model.identity.MembershipItem;
import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.ui.action.form.AddItemFormAction;
import org.bonitasoft.web.toolkit.client.ui.component.form.AbstractForm;

/**
 * @author Gai Cuisha, Yongtao Guo
 * 
 */
public class AddMembershipFormAction extends AddItemFormAction<MembershipItem> {

    private String userId;

    private String userName;

    /**
     * Default Constructor.
     * 
     * @param itemDefinition
     * @param form
     */
    public AddMembershipFormAction(final ItemDefinition itemDefinition, final AbstractForm form) {
        super(itemDefinition, form);
    }

    public AddMembershipFormAction(final ItemDefinition itemDefinition, final String userId, final String userName) {
        super(itemDefinition);
        this.userId = userId;
        this.userName = userName;
    }

    @Override
    public void execute() {
        new APICaller<MembershipItem>(this.itemDefinition).add(this.form, new APICallback() {

            @Override
            public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
                final HashMap<String, String> userIdMap = new HashMap<String, String>();
                // userIdMap.put(UserListingAdminPage.USER_ID, AddMembershipFormAction.this.userId);
                // userIdMap.put(UserListingAdminPage.USER_NAME, AddMembershipFormAction.this.userName);
                ViewController.showPopup(ListMembershipPage.TOKEN, userIdMap);
            }
        });

    }
}
