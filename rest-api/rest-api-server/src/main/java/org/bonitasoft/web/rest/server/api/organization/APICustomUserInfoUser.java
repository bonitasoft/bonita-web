package org.bonitasoft.web.rest.server.api.organization;

import org.bonitasoft.engine.identity.CustomUserInfo;
import org.bonitasoft.web.rest.model.identity.CustomUserInfoAssociationDefinition;
import org.bonitasoft.web.rest.model.identity.CustomUserInfoItem;
import org.bonitasoft.web.rest.server.engineclient.CustomUserInfoEngineClientCreator;
import org.bonitasoft.web.rest.server.framework.API;
import org.bonitasoft.web.toolkit.client.data.item.IItem;

/**
 * @author Vincent Elcrin
 */
public class APICustomUserInfoUser extends API<CustomUserInfoItem> {

    public APICustomUserInfoUser(CustomUserInfoEngineClientCreator engineClientCreator) {
    }
}
