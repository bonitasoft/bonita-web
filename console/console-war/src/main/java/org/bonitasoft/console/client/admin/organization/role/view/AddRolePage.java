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
package org.bonitasoft.console.client.admin.organization.role.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.console.client.admin.organization.role.action.AddRoleFormAction;
import org.bonitasoft.web.rest.model.identity.RoleDefinition;
import org.bonitasoft.web.rest.model.identity.RoleItem;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.validator.MandatoryValidator;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.Page;
import org.bonitasoft.web.toolkit.client.ui.component.form.Form;

/**
 * @author Colin PUY
 * 
 */
public class AddRolePage extends Page {

    public static final String TOKEN = "addrole";

    @Override
    public void defineTitle() {
        this.setTitle(_("Create a role"));
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    public void buildView() {
        addBody(addRoleForm());
    }

    private Form addRoleForm() {
        final ItemDefinition roleDefinition = RoleDefinition.get();
        return new Form()
                .addItemAttributeEntry(roleDefinition.getAttribute(RoleItem.ATTRIBUTE_ICON), "Avatar", _("Select an avatar for this role"))
                .addItemAttributeEntry(roleDefinition.getAttribute(RoleItem.ATTRIBUTE_NAME), _("Name"), _("Enter the name of this role"))
                .addItemAttributeEntry(roleDefinition.getAttribute(RoleItem.ATTRIBUTE_DISPLAY_NAME), _("Display name"),
                        _("Enter the display name of this role"))
                .addItemAttributeEntry(roleDefinition.getAttribute(RoleItem.ATTRIBUTE_DESCRIPTION), _("Description"), _("Enter the description of this role"))
                .addValidator(new JsId(RoleItem.ATTRIBUTE_NAME), new MandatoryValidator())
                .addButton(new JsId("create"), _("Create"), _("Create this role"), new AddRoleFormAction(roleDefinition))
                .addCancelButton();
    }

}
