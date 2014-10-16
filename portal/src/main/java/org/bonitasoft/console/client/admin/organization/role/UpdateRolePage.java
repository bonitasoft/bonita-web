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
package org.bonitasoft.console.client.admin.organization.role;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.web.rest.model.identity.RoleDefinition;
import org.bonitasoft.web.rest.model.identity.RoleItem;
import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.Page;
import org.bonitasoft.web.toolkit.client.ui.action.form.UpdateItemFormAction;
import org.bonitasoft.web.toolkit.client.ui.component.form.Form;
import org.bonitasoft.web.toolkit.client.ui.component.form.FormFiller;

/**
 * popup
 *
 * @author Colin PUY
 * 
 */
public class UpdateRolePage extends Page {

    private static final String PARAM_ROLE_ID = "roleId";

    public static final String TOKEN = "updaterole";
    
    public static final List<String> PRIVILEGES = new ArrayList<String>();
    
    static {
        PRIVILEGES.add(RoleListingPage.TOKEN);
    }

    public UpdateRolePage() {
        // needed by pageFactory
    }
    
    public UpdateRolePage(RoleItem roleItem) {
        addParameter(PARAM_ROLE_ID, roleItem.getId().toString());
    }
    
    @Override
    public void defineTitle() {
        setTitle(_("Update a role"));
    }

    @Override
    public void buildView() {
        addBody(updateRoleForm());
    }

    private Form updateRoleForm() {
        String roleId = getParameter(PARAM_ROLE_ID);
        Form form = new EditRoleForm()
                .addHiddenEntry("id", roleId)
                .addButton(new JsId("save"), _("Save"), _("Update this role"), new UpdateItemFormAction<RoleItem>(RoleDefinition.get()))
                .addCancelButton();
        form.addFiller(new RoleFormFiller(roleId));
        return form;
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }

    /** 
     * Role form filler 
     */
    private final class RoleFormFiller extends FormFiller {
        private String roleId;

        public RoleFormFiller(String roleId) {
            this.roleId = roleId;
        }
        
        @Override
        protected void getData(final APICallback callback) {
            new APICaller(RoleDefinition.get()).get(roleId, callback);
        }
    }
}
