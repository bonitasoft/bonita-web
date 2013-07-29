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
package org.bonitasoft.console.client.admin.organization.role;

import static java.util.Arrays.asList;
import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.LinkedList;
import java.util.List;

import org.bonitasoft.console.client.common.metadata.RoleMetadataBuilder;
import org.bonitasoft.web.rest.model.identity.RoleDefinition;
import org.bonitasoft.web.rest.model.identity.RoleItem;
import org.bonitasoft.web.toolkit.client.common.util.StringUtil;
import org.bonitasoft.web.toolkit.client.ui.action.ActionShowPopup;
import org.bonitasoft.web.toolkit.client.ui.action.CheckValidSessionBeforeAction;
import org.bonitasoft.web.toolkit.client.ui.component.Definition;
import org.bonitasoft.web.toolkit.client.ui.component.Section;
import org.bonitasoft.web.toolkit.client.ui.component.Text;
import org.bonitasoft.web.toolkit.client.ui.component.button.ButtonAction;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemDetailsMetadata;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemQuickDetailsPage;

/**
 * @author Colin PUY
 * 
 */
public class RoleQuickDetailsPage extends ItemQuickDetailsPage<RoleItem> {

    public static final String TOKEN = "rolequickdetails";

    public RoleQuickDetailsPage() {
        super(RoleDefinition.get());
    }

    @Override
    protected void defineTitle(RoleItem role) {
        setTitle(role.getDisplayName() != null ? role.getDisplayName() : role.getName());
        addDescription(StringUtil.isBlank(role.getDescription()) ? _("No description.") : role.getDescription());
    }

    @Override
    protected void buildToolbar(RoleItem role) {
        addToolbarLink(editButton(role));
    }

    private ButtonAction editButton(RoleItem role) {
        return new ButtonAction(_("Edit"), _("Edit a role"), 
                new CheckValidSessionBeforeAction(new ActionShowPopup(new UpdateRolePage(role))));
    }
    
    @Override
    protected LinkedList<ItemDetailsMetadata> defineMetadatas(RoleItem item) {
        RoleMetadataBuilder metadatas = new RoleMetadataBuilder();
        metadatas.addName();
        metadatas.addCreationDate();
        metadatas.addLastUpdateDate();
        return metadatas.build();
    }

    @Override
    protected void buildBody(RoleItem item) {
        addBody(technicalInformationsSection(item));
    }

    private Section technicalInformationsSection(RoleItem item) {
        return new Section(_("Technical informations")).addBody(numberOfUsersDefinition(item));
    }

    private Definition numberOfUsersDefinition(RoleItem item) {
        return new Definition(_("Number of users : "), new Text(String.valueOf(item.getNumberOfUsers())));
    }

    @Override
    protected List<String> defineCounters() {
        return asList(RoleItem.COUNTER_NUMBER_OF_USERS);
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }
}
