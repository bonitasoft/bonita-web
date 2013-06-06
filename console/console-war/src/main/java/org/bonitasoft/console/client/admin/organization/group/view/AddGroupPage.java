/**
 * Copyright (C) 2011 BonitaSoft S.A.
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

import org.bonitasoft.console.client.admin.organization.group.action.AddGroupFormAction;
import org.bonitasoft.console.client.model.identity.GroupDefinition;
import org.bonitasoft.console.client.model.identity.GroupItem;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.validator.MandatoryValidator;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.Page;
import org.bonitasoft.web.toolkit.client.ui.component.form.Form;

/**
 * @author Julien Mege
 * 
 */
public class AddGroupPage extends Page {

    public static final String TOKEN = "addGroup";

    @Override
    public void defineTitle() {
        this.setTitle(_("Create a group"));
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    public void buildView() {
        addBody(addGroupForm());
    }

    private Form addGroupForm() {
        final ItemDefinition groupDefinition = GroupDefinition.get();
        return new Form() 
                .addItemAttributeEntry(groupDefinition.getAttribute(GroupItem.ATTRIBUTE_ICON), "Avatar", _("Select an avatar for this group"))
                .addItemAttributeEntry(groupDefinition.getAttribute(GroupItem.ATTRIBUTE_NAME), _("Name"), _("Enter the name of this group"))
                .addItemAttributeEntry(groupDefinition.getAttribute(GroupItem.ATTRIBUTE_DISPLAY_NAME), _("Display name"),
                        _("Enter the display name of this group"))
                .addAutoCompleteEntry(new JsId(GroupItem.ATTRIBUTE_PARENT_GROUP_ID), _("Parent Group"), _("Select the parent group"), groupDefinition,
                        GroupItem.ATTRIBUTE_NAME, GroupItem.ATTRIBUTE_ID)
                .addItemAttributeEntry(groupDefinition.getAttribute(GroupItem.ATTRIBUTE_DESCRIPTION), _("Description"),
                        _("Enter the description of this group"))
                .addValidator(new JsId(GroupItem.ATTRIBUTE_NAME), new MandatoryValidator())
                .addButton(new JsId("create"), _("Create"), _("Create this group"), new AddGroupFormAction(groupDefinition))
                .addCancelButton();

    }

}
