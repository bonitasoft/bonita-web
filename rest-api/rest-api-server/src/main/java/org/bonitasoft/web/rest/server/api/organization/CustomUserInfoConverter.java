package org.bonitasoft.web.rest.server.api.organization;

import org.bonitasoft.engine.identity.CustomUserInfoDefinition;
import org.bonitasoft.web.rest.model.identity.CustomUserInfoDefinitionItem;
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
}
