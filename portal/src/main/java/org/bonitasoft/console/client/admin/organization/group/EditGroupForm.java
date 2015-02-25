/**
 * Copyright (C) 2012 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.console.client.admin.organization.group;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;
import static org.bonitasoft.web.toolkit.client.ui.component.form.entry.Text.INCREASED_MAX_LENGTH;

import java.util.Arrays;
import java.util.List;

import org.bonitasoft.web.rest.model.identity.GroupDefinition;
import org.bonitasoft.web.rest.model.identity.GroupItem;
import org.bonitasoft.web.rest.model.identity.RoleItem;
import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.validator.MandatoryValidator;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.component.form.Form;
import org.bonitasoft.web.toolkit.client.ui.component.form.FormFiller;

/**
 * @author Colin PUY
 */
public class EditGroupForm extends Form {

    public EditGroupForm() {
        final ItemDefinition<GroupItem> def = GroupDefinition.get();
        addItemAttributeEntry(def.getAttribute(GroupItem.ATTRIBUTE_ICON), "Avatar", _("Select an avatar for this group"));
        addItemAttributeEntry(def.getAttribute(GroupItem.ATTRIBUTE_NAME), _("Name"), _("Enter the name of this group"), 125L);
        addItemAttributeEntry(def.getAttribute(GroupItem.ATTRIBUTE_DISPLAY_NAME), _("Display name"), _("Enter the display name of this group"),
                INCREASED_MAX_LENGTH);
        addAutoCompleteEntry(new JsId(GroupItem.ATTRIBUTE_PARENT_GROUP_ID), _("Parent Group"), _("Select the parent group"), def, GroupItem.ATTRIBUTE_NAME,
                GroupItem.ATTRIBUTE_ID);
        addItemAttributeEntry(def.getAttribute(GroupItem.ATTRIBUTE_DESCRIPTION), _("Description"), _("Enter the description of this group"));

        getEntry(new JsId(RoleItem.ATTRIBUTE_NAME)).addValidator(new MandatoryValidator());
    }

    public EditGroupForm addGroupFiller(final String groupId) {
        addFiller(new EditGroupFormFiller(groupId));
        return this;
    }

    /**
     * EditGroup form filler
     * fill form fields with group values
     * use it like this : editGroupForm.addFiller(new EditGroupFormFiller(itemId));
     */
    private class EditGroupFormFiller extends FormFiller {

        private final String groupId;

        private EditGroupFormFiller(final String groupId) {
            this.groupId = groupId;
        }

        @Override
        protected void getData(final APICallback callback) {
            final List<String> deploys = Arrays.asList(GroupItem.ATTRIBUTE_PARENT_GROUP_ID);
            new APICaller(GroupDefinition.get()).get(groupId, deploys, callback);
        }
    }
}
