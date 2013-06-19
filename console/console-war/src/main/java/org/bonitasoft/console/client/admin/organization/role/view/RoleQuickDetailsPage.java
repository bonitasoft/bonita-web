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
package org.bonitasoft.console.client.admin.organization.role.view;

import static java.util.Arrays.asList;
import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.LinkedList;
import java.util.List;

import org.bonitasoft.web.rest.model.identity.RoleDefinition;
import org.bonitasoft.web.rest.model.identity.RoleItem;
import org.bonitasoft.web.toolkit.client.common.util.StringUtil;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.AttributeReader;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.DateAttributeReader;
import org.bonitasoft.web.toolkit.client.ui.component.Definition;
import org.bonitasoft.web.toolkit.client.ui.component.Section;
import org.bonitasoft.web.toolkit.client.ui.component.Text;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemDetailsAction;
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
    protected LinkedList<ItemDetailsAction> defineActions(RoleItem item) {
        return null;
    }

    @Override
    protected LinkedList<ItemDetailsMetadata> defineMetadatas(RoleItem item) {
        final LinkedList<ItemDetailsMetadata> metadatas = new LinkedList<ItemDetailsMetadata>();
        metadatas.add(name());
        metadatas.add(creationDate());
        metadatas.add(lastUpdateDate());
        return metadatas;
    }

    private ItemDetailsMetadata name() {
        return new ItemDetailsMetadata(new AttributeReader(RoleItem.ATTRIBUTE_NAME),
                _("Name"), _("Name of the role"));
    }

    private ItemDetailsMetadata creationDate() {
        return new ItemDetailsMetadata(new DateAttributeReader(RoleItem.ATTRIBUTE_CREATION_DATE),
                _("Creation date"), _("The date of the creation of the group"));
    }

    private ItemDetailsMetadata lastUpdateDate() {
        return new ItemDetailsMetadata(new DateAttributeReader(RoleItem.ATTRIBUTE_LAST_UPDATE_DATE),
                _("Last update"), _("The date of the last update of the group"));
    }

    @Override
    protected void buildBody(RoleItem item) {
        addBody(technicalInformationsSection(item));
    }

    private Section technicalInformationsSection(RoleItem item) {
        return new Section(_("Technical informations"))
                .addBody(numberOfUsersDefinition(item));
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
