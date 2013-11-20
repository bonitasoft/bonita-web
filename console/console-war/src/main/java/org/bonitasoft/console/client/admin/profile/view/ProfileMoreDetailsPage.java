package org.bonitasoft.console.client.admin.profile.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.bonitasoft.console.client.data.item.attribute.reader.MemberAttributeReader;
import org.bonitasoft.web.rest.model.identity.UserItem;
import org.bonitasoft.web.rest.model.portal.profile.ProfileItem;
import org.bonitasoft.web.rest.model.portal.profile.ProfileMemberDefinition;
import org.bonitasoft.web.rest.model.portal.profile.ProfileMemberItem;
import org.bonitasoft.web.toolkit.client.common.util.StringUtil;
import org.bonitasoft.web.toolkit.client.ui.CssClass;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.ActionShowPopup;
import org.bonitasoft.web.toolkit.client.ui.action.CheckValidSessionBeforeAction;
import org.bonitasoft.web.toolkit.client.ui.component.Button;
import org.bonitasoft.web.toolkit.client.ui.component.DoubleSection;
import org.bonitasoft.web.toolkit.client.ui.component.Section;
import org.bonitasoft.web.toolkit.client.ui.component.button.ButtonAction;
import org.bonitasoft.web.toolkit.client.ui.component.button.ButtonBack;
import org.bonitasoft.web.toolkit.client.ui.component.core.AbstractComponent;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTable;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTableAction;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTableActionSet;
import org.bonitasoft.web.toolkit.client.ui.component.table.Table;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemDetailsAction;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemDetailsMetadata;

public class ProfileMoreDetailsPage extends AbstractProfileDetailsPage {

    public static final String TOKEN = "profilemoredetails";

    public static final List<String> PRIVILEGES = new ArrayList<String>();

    static {
        PRIVILEGES.add(ProfileListingPage.TOKEN);
    }

    public ProfileMoreDetailsPage() {
        addClass(CssClass.MORE_DETAILS);
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }
 
    @Override
    protected boolean isDescriptionBeforeMetadatas() {
        return false;
    }

    @Override
    protected LinkedList<ItemDetailsMetadata> defineMetadatas(final ProfileItem item) {
        final LinkedList<ItemDetailsMetadata> metadatas = new LinkedList<ItemDetailsMetadata>();
        return metadatas;
    }

    /*
     * Need to override to not display the version in the title
     */
    @Override
    protected void defineTitle(final ProfileItem item) {
        setTitle(_(item.getName()));
        addDescription(StringUtil.isBlank(item.getDescription()) ? _("No description.") : _(item.getDescription()));
    }

    @Override
    protected LinkedList<ItemDetailsAction> defineActions(final ProfileItem process) {
        ProfileMoreDetailsPage.this.addToolbarLink(new ButtonBack());
        return super.defineActions(process);
    }

    @Override
    protected void buildBody(final ProfileItem item) {
        if (item.isDefault().equals("false")) {
            DoubleSection mappingSection = new DoubleSection(new JsId("Mapping"), _("Mapping"));
            mappingSection.addDescription(_("This profile will be available to the users mapped upon next login."));
            mappingSection.addBody(createUsersSection(item), createGroupsSection(item), createRolesSection(item), createMembershipsSection(item));
            addBody(mappingSection);
        } else {
            addBody(createUsersSection(item), createGroupsSection(item), createRolesSection(item), createMembershipsSection(item));
        }     
    }

    private Section createUsersSection(final ProfileItem profile) {
        final HashMap<String, String> params = new HashMap<String, String>();
        params.put(UserItem.FILTER_PROFILE_ID, profile.getId().toString());
        
        final Section userSection = new Section(new JsId("Users"), _("Users mapping"));
        userSection.addBody(createUserTable(profile));
        userSection.addBody(createAddUserButton(profile));
        return userSection;
    }

    private ItemTable createUserTable(final ProfileItem profile) {
        return new ItemTable(ProfileMemberDefinition.get())
                .addHiddenFilter(ProfileMemberItem.ATTRIBUTE_PROFILE_ID, profile.getId())
                .addHiddenFilter(ProfileMemberItem.FILTER_MEMBER_TYPE, ProfileMemberItem.VALUE_MEMBER_TYPE_USER)
                .addColumn(new MemberAttributeReader(ProfileMemberItem.VALUE_MEMBER_TYPE_USER), _("Member"))
                .addActions(userDeleteAction(profile))
                // Define the default view
                .setView(Table.VIEW_TYPE.VIEW_TABLE)
                .setShowSearch(false);
    }

    private ItemTableActionSet<ProfileMemberItem> userDeleteAction(final ProfileItem profile) {
        return new ItemTableActionSet<ProfileMemberItem>() {
            @Override
            public void defineActions(final ProfileMemberItem profileMember) {
                this.addAction(new ItemTableAction(_("remove"), _("Delete this member"),
                        new CheckValidSessionBeforeAction(new ActionShowPopup(new DeleteProfileMemberPage(profile.getId(), profileMember.getId())))));
            }
        };
    }

    private Button createAddUserButton(final ProfileItem profile) {
        return new ButtonAction("btn-adduser", _("Add a user"), "",
                new CheckValidSessionBeforeAction(new ActionShowPopup(new AddUserToProfileMemberPage(profile.getId()))));
    }

    private Section createGroupsSection(final ProfileItem profile) {
        Section groupSection = new Section(new JsId("Groups"), _("Groups mapping"), createGroupTable(profile));
        groupSection.addBody(createAddGroupButton(profile));
        return groupSection;
    }

    private ItemTable createGroupTable(final ProfileItem profile) {
        return new ItemTable(ProfileMemberDefinition.get())
                .addHiddenFilter(ProfileMemberItem.ATTRIBUTE_PROFILE_ID, profile.getId())
                .addHiddenFilter(ProfileMemberItem.FILTER_MEMBER_TYPE, ProfileMemberItem.VALUE_MEMBER_TYPE_GROUP)
                .addColumn(new MemberAttributeReader(ProfileMemberItem.VALUE_MEMBER_TYPE_GROUP), _("Member"))
                .addActions(new ItemTableActionSet<ProfileMemberItem>() {
                    @Override
                    public void defineActions(final ProfileMemberItem profileMember) {
                        this.addAction(deleteMemberAction(profile, profileMember));
                    }

                })
                // Define the default view
                .setView(Table.VIEW_TYPE.VIEW_TABLE)
                .setShowSearch(false);
    }

    private ItemTableAction deleteMemberAction(final ProfileItem profile, final ProfileMemberItem profileMember) {
        return new ItemTableAction(_("remove"), _("Delete this member"),
                new CheckValidSessionBeforeAction(new ActionShowPopup(new DeleteProfileMemberPage(profile.getId(), profileMember.getId()))));
    }
    
    private ButtonAction createAddGroupButton(final ProfileItem profile) {
        return new ButtonAction("btn-addgroup", _("Add a group"), "",
                new CheckValidSessionBeforeAction(new ActionShowPopup(new AddGroupToProfileMemberPage(profile.getId()))));
    }

    private Section createRolesSection(final ProfileItem profile) {
        Section roleSection = new Section(new JsId("Roles"), _("Roles mapping"), createRoleTable(profile));
        roleSection.addBody(createAddRoleButton(profile));
        return roleSection;
          
    }

    private ItemTable createRoleTable(final ProfileItem profile) {
        return new ItemTable(ProfileMemberDefinition.get())
                .addHiddenFilter(ProfileMemberItem.ATTRIBUTE_PROFILE_ID, profile.getId())
                .addHiddenFilter(ProfileMemberItem.FILTER_MEMBER_TYPE, ProfileMemberItem.VALUE_MEMBER_TYPE_ROLE)
                .addColumn(new MemberAttributeReader(ProfileMemberItem.VALUE_MEMBER_TYPE_ROLE), _("Member"))
                .addActions(new ItemTableActionSet<ProfileMemberItem>() {
                    @Override
                    public void defineActions(final ProfileMemberItem profileMember) {
                        this.addAction(deleteMemberAction(profile, profileMember));
                    }
                })
                // Define the default view
                .setView(Table.VIEW_TYPE.VIEW_TABLE)
                .setShowSearch(false);
    }

    private Button createAddRoleButton(final ProfileItem profile) {
        return new ButtonAction("btn-addrole", _("Add a role"), "",
                new CheckValidSessionBeforeAction(new ActionShowPopup(new AddRoleToProfileMemberPage(profile.getId()))));
    }

    private Section createMembershipsSection(final ProfileItem profile) {
        return new Section(new JsId("Memberships"), _("Memberships mapping"),createMembershipTable(profile))
                .addBody(createAddMembershipButton(profile));
    }

    private ItemTable createMembershipTable(final ProfileItem profile) {
        return new ItemTable(ProfileMemberDefinition.get())
                .addHiddenFilter(ProfileMemberItem.ATTRIBUTE_PROFILE_ID, profile.getId())
                .addHiddenFilter(ProfileMemberItem.FILTER_MEMBER_TYPE, ProfileMemberItem.VALUE_MEMBER_TYPE_MEMBERSHIP)
                .addColumn(new MemberAttributeReader(ProfileMemberItem.VALUE_MEMBER_TYPE_MEMBERSHIP), _("Member"))
                .addActions(new ItemTableActionSet<ProfileMemberItem>() {
                    @Override
                    public void defineActions(final ProfileMemberItem profileMember) {
                        this.addAction(deleteMemberAction(profile, profileMember));
                    }

                })
                // Define the default view
                .setView(Table.VIEW_TYPE.VIEW_TABLE)
                .setShowSearch(false);
    }

    private Button createAddMembershipButton(final ProfileItem profile) {
        return new ButtonAction("btn-addmembership", _("Add a membership"), "",
                new CheckValidSessionBeforeAction(new ActionShowPopup(new AddMembershipToProfileMemberPage(profile.getId()))));
    }

}
