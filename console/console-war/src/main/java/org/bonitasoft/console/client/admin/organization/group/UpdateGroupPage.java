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
package org.bonitasoft.console.client.admin.organization.group;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;

import org.bonitasoft.web.rest.model.identity.GroupDefinition;
import org.bonitasoft.web.rest.model.identity.GroupItem;
import org.bonitasoft.web.rest.model.identity.UserItem;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.Page;
import org.bonitasoft.web.toolkit.client.ui.component.form.Form;
import org.bonitasoft.web.toolkit.client.ui.component.form.FormFiller;

import com.google.gwt.core.client.GWT;

/**
 * @author Paul AMAR
 */
public class UpdateGroupPage extends Page {

    public static final String TOKEN = "updategroup";

    private static final String PARAMETER_USER_ID = "id";

    public UpdateGroupPage() {
        // used to define page in ConsoleFactoryClient
    }

    public UpdateGroupPage(APIID groupId) {
        addParameter(PARAMETER_USER_ID, groupId.toString());
    }

    @Override
    public void defineTitle() {
        this.setTitle(_("Update a group"));
    }

    @Override
    public void buildView() {
        addBody(updateGroupForm());
    }

    private Form updateGroupForm() {
        final ItemDefinition itemDef = GroupDefinition.get();

        Form form = new Form()
                .addItemAttributeEntry(itemDef.getAttribute(GroupItem.ATTRIBUTE_NAME), _("Name"), _("Enter the name for this group"))
                .addItemAttributeEntry(itemDef.getAttribute(GroupItem.ATTRIBUTE_DISPLAY_NAME), _("Display Name"), _("Enter the display name for this group"))
                .addItemAttributeEntry(itemDef.getAttribute(GroupItem.ATTRIBUTE_DESCRIPTION), _("Description"), _("Enter a description for this group"))
                .addAutoCompleteEntry(new JsId(GroupItem.ATTRIBUTE_PARENT_GROUP_ID), _("Parent Group"), _("Select the parent group"), GroupDefinition.get(),
                        GroupItem.ATTRIBUTE_NAME, GroupItem.ATTRIBUTE_ID)       
                .addItemAttributeEntry(itemDef.getAttribute(UserItem.ATTRIBUTE_ICON), _("Avatar"), _("Select an avatar for this user"),
                                                GWT.getModuleBaseURL() + "imageUpload");

        
        final String itemId = this.getParameter(PARAMETER_USER_ID);
        form.addHiddenEntry(PARAMETER_USER_ID, itemId);
        form.addButton(new JsId("save"), _("Save"), _("Save this group changes"), new UpdateGroupFormAction());
        form.addFiller(new GroupFormFiller(itemId));

        form.addCancelButton();
        return form;
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }


    /**
     * @author Paul Amar
     *
     */
    private final class GroupFormFiller extends FormFiller {
    
        private final String groupId;
    
        private GroupFormFiller(String groupId) {
            this.groupId = groupId;
        }
    
        @Override
        protected void getData(final APICallback callback) {
            ArrayList<String> deploys = new ArrayList<String>();
            deploys.add(GroupItem.ATTRIBUTE_PARENT_GROUP_ID);
            
            GroupDefinition.get().getAPICaller().get(groupId, deploys, callback);
        }
       
    }
}
