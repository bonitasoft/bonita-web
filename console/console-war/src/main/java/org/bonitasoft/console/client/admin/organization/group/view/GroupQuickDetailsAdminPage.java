/**
 * Copyright (C) 2012 BonitaSoft S.A.
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
package org.bonitasoft.console.client.admin.organization.group.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.LinkedList;
import java.util.List;

import org.bonitasoft.console.client.common.component.button.EditButton;
import org.bonitasoft.web.rest.model.identity.GroupDefinition;
import org.bonitasoft.web.rest.model.identity.GroupItem;
import org.bonitasoft.web.toolkit.client.common.util.StringUtil;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.DateAttributeReader;
import org.bonitasoft.web.toolkit.client.ui.action.ActionShowPopup;
import org.bonitasoft.web.toolkit.client.ui.action.CheckValidSessionBeforeAction;
import org.bonitasoft.web.toolkit.client.ui.component.Definition;
import org.bonitasoft.web.toolkit.client.ui.component.Section;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemDetailsMetadata;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemQuickDetailsPage;


/**
 * @author Julien Mege
 */
public class GroupQuickDetailsAdminPage extends ItemQuickDetailsPage<GroupItem> {

    public static final String TOKEN = "groupquickdetailsadmin";

    public GroupQuickDetailsAdminPage() {
        super(Definitions.get(GroupDefinition.TOKEN));
    }

    @Override
    protected void defineTitle(final GroupItem item) {
        setTitle(item.getDisplayName());
        final String description = item.getDescription();
        addDescription(StringUtil.isBlank(description) ? _("No description.") : description);
    }

    @Override
    protected void buildToolbar(GroupItem item) {
        addToolbarLink(new EditButton(_("Show more details about this user"), 
                new CheckValidSessionBeforeAction(new ActionShowPopup(new UpdateGroupPage(item.getId())))));
    }

    @Override
    protected LinkedList<ItemDetailsMetadata> defineMetadatas(final GroupItem item) {
        final LinkedList<ItemDetailsMetadata> metadatas = new LinkedList<ItemDetailsMetadata>();
        metadatas.add(creationDate());
        metadatas.add(lastUpdateDate());
        return metadatas;
    }

    protected ItemDetailsMetadata creationDate() {
        return new ItemDetailsMetadata(
                new DateAttributeReader(GroupItem.ATTRIBUTE_CREATION_DATE),
                _("Creation date"), _("The date of the group creation"));
    }

    protected ItemDetailsMetadata lastUpdateDate() {
        return new ItemDetailsMetadata(
                new DateAttributeReader(GroupItem.ATTRIBUTE_LAST_UPDATE_DATE),
                _("Last update"), _("The date of the last update of the user"));
    }

    @Override
    protected void buildBody(final GroupItem group) {
        final String nbOfUser = group.getAttributeValue(GroupItem.COUNTER_NUMBER_OF_USERS);
        String parentGroup = group.getParentPath();
        if (StringUtil.isBlank(parentGroup)) {
            parentGroup = _("N/A");
        }

        addBody(new Section(_("Technical details"))
                .addBody(new Definition(_("Number of users: "), "%%", nbOfUser))
                .addBody(new Definition(_("Parent group: "), "%%", parentGroup)));
    }

    @Override
    protected final List<String> defineCounters() {
        final List<String> deploys = super.defineDeploys();
        deploys.add(GroupItem.COUNTER_NUMBER_OF_USERS);
        return deploys;
    }
    
    @Override
    public String defineToken() {
        return TOKEN;
    }

}
