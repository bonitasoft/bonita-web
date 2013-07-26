package org.bonitasoft.web.rest.server.datastore.organization;

import org.bonitasoft.engine.identity.UserCreator;
import org.bonitasoft.web.rest.model.identity.UserItem;
import org.bonitasoft.web.toolkit.client.data.APIID;

public class UserCreatorConverter {

    public UserCreator convert(UserItem user) {
        if (user == null) {
            return null;
        }

        final UserCreator userCreator = new UserCreator(user.getUserName(), user.getPassword())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setTitle(user.getTitle())
                .setIconPath(user.getIcon())
                .setJobTitle(user.getJobTitle())
                .setEnabled(user.isEnabled());

        final APIID managerId = user.getManagerId();
        if (managerId != null) {
            userCreator.setManagerUserId(managerId.toLong());
        }
        return userCreator;
    }
}
