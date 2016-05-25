/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.web.rest.server.datastore.organization;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.bonitasoft.console.common.server.utils.BonitaHomeFolderAccessor;
import org.bonitasoft.engine.identity.UserUpdater;
import org.bonitasoft.web.rest.model.identity.UserItem;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIForbiddenException;

public class UserUpdaterConverter {

    public UserUpdater convert(Map<String, String> attributes, long tenantId) {
        UserUpdater userUpdater = new UserUpdater();

        if (attributes.containsKey(UserItem.ATTRIBUTE_FIRSTNAME)) {
            userUpdater.setFirstName(attributes.get(UserItem.ATTRIBUTE_FIRSTNAME));
        }
        if (attributes.containsKey(UserItem.ATTRIBUTE_LASTNAME)) {
            userUpdater.setLastName(attributes.get(UserItem.ATTRIBUTE_LASTNAME));
        }
        if (attributes.containsKey(UserItem.ATTRIBUTE_PASSWORD)) {
            userUpdater.setPassword(attributes.get(UserItem.ATTRIBUTE_PASSWORD));
        }
        if (attributes.containsKey(UserItem.ATTRIBUTE_USERNAME)) {
            userUpdater.setUserName(attributes.get(UserItem.ATTRIBUTE_USERNAME));
        }
        if (attributes.containsKey(UserItem.ATTRIBUTE_MANAGER_ID)) {
            Long managerId = getManagerId(attributes);
            userUpdater.setManagerId(managerId);
        }
        if (attributes.containsKey(UserItem.ATTRIBUTE_ICON)) {
            setIcon(attributes, userUpdater, tenantId);
        }
        if (attributes.containsKey(UserItem.ATTRIBUTE_TITLE)) {
            userUpdater.setTitle(attributes.get(UserItem.ATTRIBUTE_TITLE));
        }
        if (attributes.containsKey(UserItem.ATTRIBUTE_JOB_TITLE)) {
            userUpdater.setJobTitle(attributes.get(UserItem.ATTRIBUTE_JOB_TITLE));
        }
        if (attributes.containsKey(UserItem.ATTRIBUTE_ENABLED)) {
            userUpdater.setEnabled("true".equals(attributes.get(UserItem.ATTRIBUTE_ENABLED)));
        }
        return userUpdater;
    }

    void setIcon(Map<String, String> attributes, UserUpdater userUpdater, long tenantId) {
        try {
            final BonitaHomeFolderAccessor tenantFolder = new BonitaHomeFolderAccessor();
            String completeTempFilePath = tenantFolder.getCompleteTempFilePath(attributes.get(UserItem.ATTRIBUTE_ICON), tenantId);
            File tempFile = new File(completeTempFilePath);
            byte[] content = FileUtils.readFileToByteArray(tempFile);
            userUpdater.setIcon(tempFile.getName(), content);
        } catch (IOException e) {
            throw new APIForbiddenException("Forbidden access to " + attributes.get(UserItem.ATTRIBUTE_ICON), e);
        }
    }

    private Long getManagerId(final Map<String, String> attributes) {
        try {
            return Long.valueOf(attributes.get(UserItem.ATTRIBUTE_MANAGER_ID));
        } catch (NumberFormatException e) {
            return 0L;
        }
    }
    //
    //    @Test(expected = APIForbiddenException.class)
    //    public void should_verify_authorisation_for_the_given_icon_path() throws Exception {
    //        doReturn("../../../userIcon.jpg").when(userItem).getIcon();
    //        doThrow(new UnauthorizedFolderException("error")).when(apiUser).getCompleteTempFilePath("../../../userIcon.jpg");
    //
    //        apiUser.add(userItem);
    //
    //    }
    //    if (item != null) {
    //        // Finish the upload of the icon
    //        final String icon = item.get(UserItem.ATTRIBUTE_ICON);
    //        if (!MapUtil.removeIfBlank(item, UserItem.ATTRIBUTE_ICON)) {
    //
    //            deleteOldIconFileIfExists(id);
    //            String tmpIconPath;
    //            try {
    //                tmpIconPath = getCompleteTempFilePath(icon);
    //            } catch (final UnauthorizedFolderException e) {
    //                throw new APIForbiddenException(e.getMessage());
    //            } catch (final IOException e) {
    //                throw new APIException(e);
    //            }
    //
    //            final String newIcon = uploadIcon(tmpIconPath);
    //            item.put(UserItem.ATTRIBUTE_ICON, newIcon);
    //        }
    //
    //        // Do not update password if not set
    //        MapUtil.removeIfBlank(item, UserItem.ATTRIBUTE_PASSWORD);
    //        if (item.get(UserItem.ATTRIBUTE_PASSWORD) != null) {
    //            checkPasswordRobustness(item.get(UserItem.ATTRIBUTE_PASSWORD));
    //        }
    //    }
}
