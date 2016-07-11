package org.bonitasoft.web.rest.server.datastore.organization;

import org.bonitasoft.console.common.server.utils.BonitaHomeFolderAccessor;
import org.bonitasoft.console.common.server.utils.IconDescriptor;
import org.bonitasoft.engine.identity.UserCreator;
import org.bonitasoft.web.rest.model.identity.UserItem;
import org.bonitasoft.web.toolkit.client.data.APIID;

public class UserCreatorConverter {

    public UserCreator convert(UserItem user, long tenantId) {
        if (user == null) {
            return null;
        }

        final UserCreator userCreator = new UserCreator(user.getUserName(), user.getPassword())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setTitle(user.getTitle())
                .setJobTitle(user.getJobTitle())
                .setEnabled(user.isEnabled());

        if (user.getIcon() != null && !user.getIcon().isEmpty()) {
            IconDescriptor iconDescriptor = new BonitaHomeFolderAccessor().getIconFromFileSystem(user.getIcon(), tenantId);
            userCreator.setIcon(iconDescriptor.getFilename(), iconDescriptor.getContent());
        }

        final APIID managerId = user.getManagerId();
        if (managerId != null) {
            userCreator.setManagerUserId(managerId.toLong());
        }
        return userCreator;
    }
}
