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
package org.bonitasoft.console.client.admin.organization.users.action;

import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.web.rest.model.identity.UserDefinition;
import org.bonitasoft.web.rest.model.identity.UserItem;
import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.ui.action.Action;

/**
 * Change users state to ENABLE or DISABLE
 *  
 * @author Colin PUY
 */
public class ChangeUsersStateAction extends Action {

    public enum STATE { ENABLED, DISABLED }

    private STATE newState;
    private String userId;

    public ChangeUsersStateAction(APIID userId, STATE newState) {
        this(userId.toString(), newState);
    }
    
    public ChangeUsersStateAction(String userId, STATE newState) {
        this.userId = userId;
        this.newState = newState;
    }
    
    @Override
    public void execute() {
        HashMap<String, String> updateAttributes = buildAttributesMap();
        UserDefinition.get().getAPICaller().update(userId, updateAttributes, new ChangeUserStateCallback());
    }
    
    private HashMap<String, String> buildAttributesMap() {
        HashMap<String, String> map = new HashMap<String, String>();
        if (STATE.ENABLED.equals(newState)) {
            map.put(UserItem.ATTRIBUTE_ENABLED, "true");
        } else {
            map.put(UserItem.ATTRIBUTE_ENABLED, "false");
        }
        return map;
    }

    private final class ChangeUserStateCallback extends APICallback {
        @Override
        public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
            ViewController.closePopup();
            ViewController.refreshCurrentPage();
        }
    
        @Override
        public void onError(final String message, final Integer errorCode) {
            // Do nothing
        }
    }
}
