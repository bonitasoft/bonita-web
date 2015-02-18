/**
 * Copyright (C) 2011 BonitaSoft S.A.
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
package org.bonitasoft.console.client.admin.organization.role;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;
import static org.bonitasoft.web.toolkit.client.ui.component.form.entry.Text.INCREASED_MAX_LENGTH;

import org.bonitasoft.web.rest.model.identity.RoleDefinition;
import org.bonitasoft.web.rest.model.identity.RoleItem;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.validator.MandatoryValidator;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.component.form.Form;

public class EditRoleForm extends Form {

    public EditRoleForm() {
        final ItemDefinition<RoleItem> roleDefinition = RoleDefinition.get();
        addItemAttributeEntry(roleDefinition.getAttribute(RoleItem.ATTRIBUTE_ICON), "Avatar", _("Select an avatar for this role"));
        addItemAttributeEntry(roleDefinition.getAttribute(RoleItem.ATTRIBUTE_NAME), _("Name"), _("Enter the name of this role"), INCREASED_MAX_LENGTH);
        addItemAttributeEntry(roleDefinition.getAttribute(RoleItem.ATTRIBUTE_DISPLAY_NAME), _("Display name"), _("Enter the display name of this role"),
                INCREASED_MAX_LENGTH);
        addItemAttributeEntry(roleDefinition.getAttribute(RoleItem.ATTRIBUTE_DESCRIPTION), _("Description"), _("Enter the description of this role"));

        getEntry(new JsId(RoleItem.ATTRIBUTE_NAME)).addValidator(new MandatoryValidator());
    }
}
