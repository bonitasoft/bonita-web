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
package org.bonitasoft.console.client.admin.organization.users.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.console.client.admin.organization.users.action.AddUserFormAction;
import org.bonitasoft.web.rest.model.identity.UserDefinition;
import org.bonitasoft.web.rest.model.identity.UserItem;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.validator.MandatoryValidator;
import org.bonitasoft.web.toolkit.client.data.item.attribute.validator.StringNoSpaceValidator;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.Page;
import org.bonitasoft.web.toolkit.client.ui.component.form.Form;

/**
 * @author Paul Amar
 * 
 */
public class PopupAddUserPage extends Page {

    public static final String TOKEN = "popupadduserpage";
    
    public static final List<String> PRIVILEGES = new ArrayList<String>();
    
    static {
        PRIVILEGES.add(UserListingAdminPage.TOKEN);
    }

    @Override
    public void defineTitle() {
        this.setTitle(_("Create a user"));
    }

    @Override
    public void buildView() {

        final ItemDefinition<UserItem> definition = UserDefinition.get();

        final Form form = new Form()
                .addHiddenEntry(UserItem.ATTRIBUTE_ENABLED, "true")
                .addItemAttributeEntry(definition.getAttribute(UserItem.ATTRIBUTE_USERNAME), _("Login"), _("Enter the login for this user"))
                .addItemAttributeEntry(definition.getAttribute(UserItem.ATTRIBUTE_PASSWORD), _("Password"), _("Enter the password for this user"))
                .addPasswordEntry(new JsId(UserItem.ATTRIBUTE_PASSWORD + "_confirm"), _("Confirm password"), _("Confirm the password for this user"))

                .addItemAttributeEntry(definition.getAttribute(UserItem.ATTRIBUTE_FIRSTNAME), _("First name"), _("Enter the first name of this user"))
                .addItemAttributeEntry(definition.getAttribute(UserItem.ATTRIBUTE_LASTNAME), _("Last name"), _("Enter the last name of this user"))

                .addValidator(new JsId(UserItem.ATTRIBUTE_USERNAME), new StringNoSpaceValidator())
                .addValidator(new JsId(UserItem.ATTRIBUTE_PASSWORD), new MandatoryValidator())
                .addValidator(new JsId(UserItem.ATTRIBUTE_PASSWORD + "_confirm"), new MandatoryValidator(_("Confirm password")));

        form.addButton(new JsId("create"), _("Create"), _("Create this user"), new AddUserFormAction());

        form.addCancelButton();

        addBody(form);

    }

    @Override
    public String defineToken() {
        return TOKEN;
    }
}
