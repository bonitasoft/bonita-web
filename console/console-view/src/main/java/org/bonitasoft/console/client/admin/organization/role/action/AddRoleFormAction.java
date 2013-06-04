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
package org.bonitasoft.console.client.admin.organization.role.action;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.console.client.model.identity.RoleItem;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.form.AddItemFormAction;

import com.google.gwt.user.client.Window;

/**
 * @author Gai Cuisha
 * 
 */
public class AddRoleFormAction extends AddItemFormAction<RoleItem> {

    /**
     * Default Constructor.
     * 
     * @param itemDefinition
     */
    public AddRoleFormAction(final ItemDefinition itemDefinition) {
        super(itemDefinition);
    }

    @Override
    public void execute() {
        final String roleName = this.getParameter(new JsId(RoleItem.ATTRIBUTE_NAME));
        if (roleName == null || roleName.isEmpty()) {
            Window.alert(_("Enter the name of this role"));
        } else {
            super.execute();
        }
    }
}
