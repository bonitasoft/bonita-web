package org.bonitasoft.web.rest.server.api.organization;

import org.bonitasoft.engine.identity.CustomUserInfo;
import org.bonitasoft.engine.identity.CustomUserInfoDefinition;
import org.bonitasoft.web.rest.model.identity.CustomUserInfoDefinitionItem;
import org.bonitasoft.web.rest.model.identity.CustomUserInfoItem;
import org.bonitasoft.web.toolkit.client.data.APIID;

/**
 * @author Vincent Elcrin
 */
public class CustomUserInfoConverter {

    public CustomUserInfoDefinitionItem convert(CustomUserInfoDefinition definition) {
        CustomUserInfoDefinitionItem item = new CustomUserInfoDefinitionItem();
        item.setId(APIID.makeAPIID(definition.getId()));
        item.setName(definition.getName());
        item.setDescription(definition.getDescription());
        return item;
    }

    public CustomUserInfoItem convert(CustomUserInfo information) {
        CustomUserInfoItem item = new CustomUserInfoItem();
        item.setUserId(information.getUserId());
        item.setDefinition(convert(information.getDefinition()));
        item.setValue(information.getValue());
        return item;
    }
}
