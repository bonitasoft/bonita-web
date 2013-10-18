/**
 * Copyright (C) 2013 BonitaSoft S.A.
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
package org.bonitasoft.console.client.admin.organization.users.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bonitasoft.console.client.admin.organization.group.GroupListingAdminPage;
import org.bonitasoft.console.client.admin.organization.role.RoleListingPage;
import org.bonitasoft.console.client.common.metadata.UserMetadataBuilder;
import org.bonitasoft.web.rest.model.identity.MembershipItem;
import org.bonitasoft.web.rest.model.identity.PersonalContactDataItem;
import org.bonitasoft.web.rest.model.identity.ProfessionalContactDataItem;
import org.bonitasoft.web.rest.model.identity.UserItem;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.ui.CssClass;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.action.ActionShowPopup;
import org.bonitasoft.web.toolkit.client.ui.action.CheckValidSessionBeforeAction;
import org.bonitasoft.web.toolkit.client.ui.component.Clickable;
import org.bonitasoft.web.toolkit.client.ui.component.Definition;
import org.bonitasoft.web.toolkit.client.ui.component.Link;
import org.bonitasoft.web.toolkit.client.ui.component.Section;
import org.bonitasoft.web.toolkit.client.ui.component.Text;
import org.bonitasoft.web.toolkit.client.ui.component.button.ButtonAction;
import org.bonitasoft.web.toolkit.client.ui.component.button.ButtonBack;
import org.bonitasoft.web.toolkit.client.ui.component.containers.ContainerStyled;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTable;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTableAction;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTableActionSet;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemDetailsMetadata;

/**
 * @author Nicolas Tith
 * @author Colin Puy
 * @author Paul Amar
 */
public class UserMoreDetailsAdminPage extends UserQuickDetailsAdminPage {

    public static final String TOKEN = "usermoredetailsadmin";
    
    public static final List<String> PRIVILEGES = new ArrayList<String>();
    
    static {
        PRIVILEGES.add(UserListingAdminPage.TOKEN);
        PRIVILEGES.add(GroupListingAdminPage.TOKEN);
        PRIVILEGES.add(RoleListingPage.TOKEN);
    }

    public UserMoreDetailsAdminPage() {
        addClass(CssClass.MORE_DETAILS);
    }

    public UserMoreDetailsAdminPage(final APIID userId) {
        this();
        addParameter(PARAMETER_ITEM_ID, userId.toString());
    }

    @Override
    protected void defineTitle(final UserItem user) {
        if (user.isEnabled()) {
            setTitle(user.getTitle() + " " + user.getFirstName() + " " + user.getLastName());
        } else {
            setTitle(user.getTitle() + " " + user.getFirstName() + " " + user.getLastName(), 
                    new Text("inactive").addClass("inactive-user").setTooltip(_("Inactive user")));
        }
    }
    
    @Override
    protected boolean isDescriptionBeforeMetadatas() {
        return false;
    }

    @Override
    protected LinkedList<ItemDetailsMetadata> defineMetadatas(final UserItem item) {
        final UserMetadataBuilder metadatas = new UserMetadataBuilder();
        metadatas.addEmail();
        metadatas.addUserName();
        metadatas.addJobTitle();
        metadatas.addManager();
        metadatas.addLastConnectionDate();
        metadatas.addCreationDate();
        return metadatas.build();
    }

    @Override
    protected void buildToolbar(final UserItem user) {
        addToolbarLink(new ButtonBack());
        if (user.isEnabled()) {
            addToolbarLink(newEditUserButton());
        }
    }

    private Clickable newEditUserButton() {
        return new ButtonAction("btn-edituser", _("Update user"), _("Update the user"), new CheckValidSessionBeforeAction(createEditUserAction()));
    }

    private Action createEditUserAction() {
        return new CheckValidSessionBeforeAction(new ActionShowPopup(new UpdateUserPage(getItemId())));
    }

    @Override
    protected void buildBody(final UserItem user) {
        addBody(profileSection(user));
        addBody(membershipSection(user));
        addBody(businessCardSection(user.getProfessionalData()));
        addBody(personalInformationSection(user.getPersonnalData()));
    }

    @Override
    protected Section membershipSection(final UserItem user) {
        final Section section = new Section(new JsId("membershipSection"), _("Membership"));
        section.addBody(membershipTable(user));
        if (user.isEnabled()) {
            section.addBody(addMembershipLink());
        }
        return section;
    }

    protected Link addMembershipLink() {
        return new ButtonAction("btn-add", _("Add"), _("Add a new membership to this user"),
                new CheckValidSessionBeforeAction(new ActionShowPopup(new AddMembershipPage(getItemId()))));
    }

    @Override
    protected ItemTable membershipTable(final UserItem item) {
        final ItemTable membershipTable = super.membershipTable(item);
        return membershipTable.addActions(new ItemTableActionSet<MembershipItem>() {

            @Override
            protected void defineActions(final MembershipItem item) {
                this.addAction(new ItemTableAction(_("Delete"), _("Delete this membership"), new CheckValidSessionBeforeAction(
                        createDeleteMembershipAction(item))));
            }

            private Action createDeleteMembershipAction(final MembershipItem item) {
                return new CheckValidSessionBeforeAction(new ActionShowPopup(new DeleteMembershipPage(item.getId())));
            }
        });
    }

    private Section businessCardSection(final ProfessionalContactDataItem professionalData) {
        final Section businessCardSection = new Section(new JsId("businessCardSection"), _("Business card"));
        final ContainerStyled<Definition> definitions = new ContainerStyled<Definition>(new JsId("definitions"));
        definitions.append(new Definition(_("Address") + ": ", _("the company address of the user"), professionalData.getAddress()));
        definitions.append(new Definition(_("City") + ": ", _("the city"), professionalData.getCity()));
        definitions.append(new Definition(_("Country") + ": ", _("the country of the user company"), professionalData.getCountry()));
        definitions.append(new Definition(_("Zip code") + ": ", _("the zip code of the user company"), professionalData.getZipCode()));
        definitions.append(new Definition(_("State") + ": ", _("the state of the user company"), professionalData.getState()));
        definitions.append(new Definition(_("Email") + ": ", _("the professional email the user"), professionalData.getEmail()));
        definitions.append(new Definition(_("Phone") + ": ", _("the professional phone number of the user"), professionalData.getPhoneNumber()));
        definitions.append(new Definition(_("Mobile") + ": ", _("the professional mobile phone number of the user"), professionalData
                .getMobileNumber()));
        return businessCardSection.addBody(definitions);
    }

    private Section personalInformationSection(final PersonalContactDataItem item) {
        final Section personalInformationSection = new Section(new JsId("personalInformationSection"), _("Personal information"));
        final ContainerStyled<Definition> definitions = new ContainerStyled<Definition>(new JsId("definitions"));
        definitions.append(new Definition(_("Address") + ": ", _("the address of the user"), item.getAddress()));
        definitions.append(new Definition(_("City") + ": ", _("the city"), item.getCity()));
        definitions.append(new Definition(_("Country") + ": ", _("the country of the user"), item.getCountry()));
        definitions.append(new Definition(_("Zip code") + ": ", _("the zip code of the user"), item.getZipCode()));
        definitions.append(new Definition(_("State") + ": ", _("the state of the user"), item.getState()));
        definitions.append(new Definition(_("Email") + ": ", _("the personnal email the user"), item.getEmail()));
        definitions.append(new Definition(_("Phone") + ": ", _("the personnal phone number of the user"), item.getPhoneNumber()));
        definitions.append(new Definition(_("Mobile") + ": ", _("the personnal mobile phone number of the user"), item.getMobileNumber()));
        return personalInformationSection.addBody(definitions);
    }

    @Override
    protected List<String> defineDeploys() {
        final List<String> deploys = new ArrayList<String>(super.defineDeploys());
        deploys.add(UserItem.DEPLOY_PERSONNAL_DATA);
        return deploys;
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }
}
