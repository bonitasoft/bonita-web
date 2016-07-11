/**
 * Copyright (C) 2013 BonitaSoft S.A.
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
package org.bonitasoft.console.client.menu.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bonitasoft.web.rest.model.portal.profile.ProfileMemberDefinition;
import org.bonitasoft.web.rest.model.portal.profile.ProfileMemberItem;
import org.bonitasoft.web.rest.model.system.TenantAdminDefinition;
import org.bonitasoft.web.rest.model.system.TenantAdminItem;
import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.common.json.JSonItemReader;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.api.request.APIRequest;

/**
 * @author Fabio Lombardi
 * 
 */
public class TechnicalUserMessageHandler {

    public void check() {
        requestBPMServiceState(createServiceStateMessage());
    }
    
    protected void requestBPMServiceState(final APICallback callback) {
        APIRequest.get(TenantAdminDefinition.UNUSED_ID, TenantAdminDefinition.get(), callback).run();
    }

    protected APICallback createServiceStateMessage() {
        return new APICallback() {

            @Override
            public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
                final TenantAdminItem tenantAdminItem = JSonItemReader.parseItem(response, TenantAdminDefinition.get());
                if (tenantAdminItem.isPaused()) {
                    ViewController.showPopup(TechnicalUserServicePausedView.TOKEN);
                } else {
                    checkIsFirstVisit();
                }

            }
        };
    }
    
    private void checkIsFirstVisit() {
        requestProfileMapping(ProfileMemberItem.VALUE_MEMBER_TYPE_GROUP, createTechnicalUserMessage(ProfileMemberItem.VALUE_MEMBER_TYPE_GROUP));
    }

    private void requestProfileMapping(final String type, final APICallback callback) {
        HashMap<String, String> filterMap = new HashMap<String, String>();
        filterMap.put(ProfileMemberItem.FILTER_MEMBER_TYPE, type);
        APIRequest.search(0, 10, null, null, filterMap, ProfileMemberDefinition.get(), callback).run();
    }

    private APICallback createTechnicalUserMessage(final String currentProfileMemeberTypeToCheck) {

        return new APICallback() {

            @Override
            public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
                final List<ProfileMemberItem> items = JSonItemReader.parseItems(response, ProfileMemberDefinition.get());
                if (items.size() == 0) {
                    if (ProfileMemberItem.VALUE_MEMBER_TYPE_ROLE.equals(currentProfileMemeberTypeToCheck)) {
                        ViewController.showPopup(TechnicalUserWarningView.TOKEN);
                    } else {
                        String nextProfileMemeberTypeToCheck = getNextProfileMemberTypeToCheck(currentProfileMemeberTypeToCheck);
                        requestProfileMapping(nextProfileMemeberTypeToCheck, createTechnicalUserMessage(nextProfileMemeberTypeToCheck));
                    }
                }
            }
        };
    }
    
    private String getNextProfileMemberTypeToCheck(String currentType) {
        
        if (ProfileMemberItem.VALUE_MEMBER_TYPE_GROUP.equals(currentType)) {
            return ProfileMemberItem.VALUE_MEMBER_TYPE_USER;
        }
        
        if (ProfileMemberItem.VALUE_MEMBER_TYPE_USER.equals(currentType)) {
            return ProfileMemberItem.VALUE_MEMBER_TYPE_MEMBERSHIP;
        }
        
        if (ProfileMemberItem.VALUE_MEMBER_TYPE_MEMBERSHIP.equals(currentType)) {
            return ProfileMemberItem.VALUE_MEMBER_TYPE_ROLE;
        }
        
        return ProfileMemberItem.VALUE_MEMBER_TYPE_ROLE;
    }

}
