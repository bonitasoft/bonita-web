package org.bonitasoft.web.rest.server.datastore.organization;

import org.bonitasoft.engine.identity.Group;
import org.bonitasoft.web.rest.model.identity.GroupItem;
import org.bonitasoft.web.rest.server.datastore.converter.ItemConverter;

public class GroupItemConverter extends ItemConverter<GroupItem, Group> {

    @Override
    public GroupItem convert(Group group) {
        GroupItem groupItem = new GroupItem();
        groupItem.setCreatedByUserId(group.getCreatedBy());
        groupItem.setCreationDate(group.getCreationDate());
        groupItem.setDescription(group.getDescription());
        groupItem.setDisplayName(group.getDisplayName());
        groupItem.setIcon(group.getIconId() == null ? "" : "../avatars/" + group.getIconId());
        groupItem.setId(group.getId());
        groupItem.setLastUpdateDate(group.getLastUpdate());
        groupItem.setName(group.getName());
        groupItem.setParentPath(group.getParentPath());
        groupItem.setPath(group.getPath());
        return groupItem;
    }

}
