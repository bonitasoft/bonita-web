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
package org.bonitasoft.console.client.admin.organization.users.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.bonitasoft.console.client.common.metadata.UserMetadataBuilder;
import org.bonitasoft.web.rest.model.identity.GroupItem;
import org.bonitasoft.web.rest.model.identity.MembershipDefinition;
import org.bonitasoft.web.rest.model.identity.MembershipItem;
import org.bonitasoft.web.rest.model.identity.RoleItem;
import org.bonitasoft.web.rest.model.identity.UserDefinition;
import org.bonitasoft.web.rest.model.identity.UserItem;
import org.bonitasoft.web.rest.model.portal.profile.ProfileDefinition;
import org.bonitasoft.web.rest.model.portal.profile.ProfileItem;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.CompoundAttributeReader;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.DeployedAttributeReader;
import org.bonitasoft.web.toolkit.client.ui.CssId;
import org.bonitasoft.web.toolkit.client.ui.component.Section;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTable;
import org.bonitasoft.web.toolkit.client.ui.component.table.Table.VIEW_TYPE;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemDetailsMetadata;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemQuickDetailsPage;

/**
 * @author Paul AMAR
 * @author Colin PUY
 */
public class UserQuickDetailsPage extends ItemQuickDetailsPage<UserItem> {

    public static final String TOKEN = "userquickdetails";

    public static final List<String> PRIVILEGES = new ArrayList<String>();

    static {
        PRIVILEGES.add(UserListingAdminPage.TOKEN);
    }

    public UserQuickDetailsPage() {
        super(Definitions.get(UserDefinition.TOKEN));
    }

    @Override
    protected LinkedList<ItemDetailsMetadata> defineMetadatas(final UserItem item) {
        UserMetadataBuilder metadatas = new UserMetadataBuilder();
        metadatas.addEmail(item.getProfessionalData());
        metadatas.addManager();
        metadatas.addUserName();
        metadatas.addJobTitle();
        metadatas.addLastConnectionDate();
        metadatas.addLastUpdateDate();
        return metadatas.build();
    }

    @Override
    protected void defineTitle(final UserItem item) {
        setTitle(item.getTitle() + " " + item.getFirstName() + " " + item.getLastName());
    }

    @Override
    protected List<String> defineDeploys() {
        return Arrays.asList(UserItem.DEPLOY_PROFESSIONAL_DATA, UserItem.ATTRIBUTE_MANAGER_ID);
    }

    @Override
    protected void buildBody(final UserItem item) {
        addBody(profileSection(item));
        addBody(membershipSection(item));
    }

    protected Section profileSection(final UserItem item) {
        Section userProfileSection = new Section(_("Profile"), profileTable(item).setView(VIEW_TYPE.VIEW_LIST));
        userProfileSection.setId(CssId.SECTION_USER_PROFILE);
        return userProfileSection;
    }

    protected ItemTable profileTable(final UserItem item) {
        return new ItemTable(ProfileDefinition.get())
                .addColumn(ProfileItem.ATTRIBUTE_NAME, _("Profile"))
                .addHiddenFilter(ProfileItem.FILTER_USER_ID, item.getId())
                .setShowSearch(false);
    }

    protected Section membershipSection(final UserItem item) {
        Section membershipSection = new Section(_("Membership"), membershipTable(item).setView(VIEW_TYPE.VIEW_LIST));
        membershipSection.setId(CssId.QD_SECTION_USER_MEMBERSHIP);
        return membershipSection;
    }

    protected ItemTable membershipTable(final UserItem item) {
        return new ItemTable(MembershipDefinition.get())
                .addColumn(
                        new CompoundAttributeReader(_("Role"), _("%rolename% of %groupname%"))
                                .addReader("rolename", new DeployedAttributeReader(MembershipItem.ATTRIBUTE_ROLE_ID, RoleItem.ATTRIBUTE_NAME))
                                .addReader("groupname", new DeployedAttributeReader(MembershipItem.ATTRIBUTE_GROUP_ID, GroupItem.ATTRIBUTE_NAME)),
                        _("Membership"))
                .addHiddenFilter(MembershipItem.ATTRIBUTE_USER_ID, item.getId())
                .setShowSearch(false);
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }
}
