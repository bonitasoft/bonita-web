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
import java.util.Arrays;
import java.util.List;

import org.bonitasoft.console.client.admin.organization.group.GroupListingAdminPage;
import org.bonitasoft.console.client.admin.organization.role.RoleListingPage;
import org.bonitasoft.console.client.admin.organization.users.action.UpdateUserFormAction;
import org.bonitasoft.web.rest.model.identity.AbstractContactDataItem;
import org.bonitasoft.web.rest.model.identity.PersonalContactDataDefinition;
import org.bonitasoft.web.rest.model.identity.PersonalContactDataItem;
import org.bonitasoft.web.rest.model.identity.ProfessionalContactDataDefinition;
import org.bonitasoft.web.rest.model.identity.ProfessionalContactDataItem;
import org.bonitasoft.web.rest.model.identity.UserDefinition;
import org.bonitasoft.web.rest.model.identity.UserItem;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.Page;
import org.bonitasoft.web.toolkit.client.ui.component.form.Form;
import org.bonitasoft.web.toolkit.client.ui.component.form.FormFiller;

import com.google.gwt.core.client.GWT;

/**
 * @author Yongtao Guo
 * @author Colin PUY
 */
public class UpdateUserPage extends Page {

    public static final String TOKEN = "updateuser";

    public static final List<String> PRIVILEGES = new ArrayList<String>();

    static {
        PRIVILEGES.add(UserListingAdminPage.TOKEN);
        PRIVILEGES.add(GroupListingAdminPage.TOKEN);
        PRIVILEGES.add(RoleListingPage.TOKEN);
    }

    private static final String PARAMETER_USER_ID = "id";

    public UpdateUserPage() {
        // used to define page in ConsoleFactoryClient
    }

    public UpdateUserPage(APIID userId) {
        addParameter(PARAMETER_USER_ID, userId.toString());
    }

    @Override
    public void defineTitle() {
        this.setTitle(_("Edit a user"));
    }

    @Override
    public void buildView() {
        addBody(updateUserForm());
    }

    private Form updateUserForm() {
        final ItemDefinition itemDef = UserDefinition.get();

        Form form = new Form()
                .addItemAttributeEntry(itemDef.getAttribute(UserItem.ATTRIBUTE_USERNAME), _("Username"), _("Enter the username for this user"))
                .addItemAttributeEntry(itemDef.getAttribute(UserItem.ATTRIBUTE_PASSWORD), _("Password"), _("Enter the password for this user"))
                .addPasswordEntry(new JsId(UserItem.ATTRIBUTE_PASSWORD + "_confirm"), _("Confirm password"), _("Confirm the password for this user"))
                .addItemAttributeEntry(itemDef.getAttribute(UserItem.ATTRIBUTE_ICON), _("Avatar"), _("Select an avatar for this user"),
                        GWT.getModuleBaseURL() + "imageUpload");

        form = addDetails(form);
        form = addProfessionalBusinessCard(form);

        form = addPersonalBusinessCard(form);

        final String itemId = this.getParameter(PARAMETER_USER_ID);
        form.addHiddenEntry(PARAMETER_USER_ID, itemId);
        form.addButton(new JsId("save"), _("Save"), _("Save this user modifications"), new UpdateUserFormAction());
        form.addFiller(new FormFiller() {

            @Override
            protected void getData(final APICallback callback) {
                itemDef.getAPICaller().get(itemId, Arrays.asList(UserItem.DEPLOY_PROFESSIONAL_DATA, UserItem.DEPLOY_PERSONNAL_DATA), callback);
            }
        });

        form.addCancelButton();
        return form;
    }

    private Form addPersonalBusinessCard(Form form) {
        final ItemDefinition persoContactItem = Definitions.get(PersonalContactDataDefinition.TOKEN);
        return form.openTab(_("Personal information"))
                .addItemAttributeEntry(
                        new JsId(UserItem.DEPLOY_PERSONNAL_DATA + "_" + PersonalContactDataItem.ATTRIBUTE_ADDRESS),
                        persoContactItem.getAttribute(PersonalContactDataItem.ATTRIBUTE_ADDRESS), _("Address"),
                        _("Enter the address of this user"))
                .addItemAttributeEntry(
                        new JsId(UserItem.DEPLOY_PERSONNAL_DATA + "_" + PersonalContactDataItem.ATTRIBUTE_CITY),
                        persoContactItem.getAttribute(PersonalContactDataItem.ATTRIBUTE_CITY), _("City"),
                        _("Enter the city of this user"))
                .addItemAttributeEntry(
                        new JsId(UserItem.DEPLOY_PERSONNAL_DATA + "_" + PersonalContactDataItem.ATTRIBUTE_COUNTRY),
                        persoContactItem.getAttribute(PersonalContactDataItem.ATTRIBUTE_COUNTRY), _("Country"),
                        _("Enter the country of this user"))
                .addItemAttributeEntry(
                        new JsId(UserItem.DEPLOY_PERSONNAL_DATA + "_" + PersonalContactDataItem.ATTRIBUTE_ZIPCODE),
                        persoContactItem.getAttribute(PersonalContactDataItem.ATTRIBUTE_ZIPCODE), _("Zip code"),
                        _("Enter the zip code of this user"))
                .addItemAttributeEntry(
                        new JsId(UserItem.DEPLOY_PERSONNAL_DATA + "_" + PersonalContactDataItem.ATTRIBUTE_STATE),
                        persoContactItem.getAttribute(PersonalContactDataItem.ATTRIBUTE_STATE), _("State"),
                        _("Enter the state of this user"))
                .addItemAttributeEntryWithMaxLength(
                        new JsId(UserItem.DEPLOY_PERSONNAL_DATA + "_" + PersonalContactDataItem.ATTRIBUTE_EMAIL),
                        persoContactItem.getAttribute(PersonalContactDataItem.ATTRIBUTE_EMAIL), _("Email"),
                        _("Enter the email of this user"), AbstractContactDataItem.MAX_EMAIL_LENGTH)
                .addItemAttributeEntry(
                        new JsId(UserItem.DEPLOY_PERSONNAL_DATA + "_" + PersonalContactDataItem.ATTRIBUTE_PHONE),
                        persoContactItem.getAttribute(PersonalContactDataItem.ATTRIBUTE_PHONE), _("Phone"),
                        _("Enter the phone of this user"))
                .addItemAttributeEntry(
                        new JsId(UserItem.DEPLOY_PERSONNAL_DATA + "_" + PersonalContactDataItem.ATTRIBUTE_MOBILE),
                        persoContactItem.getAttribute(PersonalContactDataItem.ATTRIBUTE_MOBILE), _("Mobile"),
                        _("Enter the mobile phone of this user"))
                .closeTab();

    }

    private Form addProfessionalBusinessCard(Form form) {
        final ItemDefinition proContactItem = Definitions.get(ProfessionalContactDataDefinition.TOKEN);
        JsId jsIdEmail = new JsId(UserItem.DEPLOY_PERSONNAL_DATA + "_" + ProfessionalContactDataItem.ATTRIBUTE_EMAIL);
        form.getEntry(jsIdEmail);
        Form businessForm = form.openTab(_("Business card"))
                .addItemAttributeEntry(
                        new JsId(UserItem.DEPLOY_PROFESSIONAL_DATA + "_" + ProfessionalContactDataItem.ATTRIBUTE_ADDRESS),
                        proContactItem.getAttribute(ProfessionalContactDataItem.ATTRIBUTE_ADDRESS), _("Address"),
                        _("Enter the address of this user"))
                .addItemAttributeEntry(
                        new JsId(UserItem.DEPLOY_PROFESSIONAL_DATA + "_" + ProfessionalContactDataItem.ATTRIBUTE_CITY),
                        proContactItem.getAttribute(ProfessionalContactDataItem.ATTRIBUTE_CITY), _("City"),
                        _("Enter the city of this user"))
                .addItemAttributeEntry(
                        new JsId(UserItem.DEPLOY_PROFESSIONAL_DATA + "_" + ProfessionalContactDataItem.ATTRIBUTE_COUNTRY),
                        proContactItem.getAttribute(ProfessionalContactDataItem.ATTRIBUTE_COUNTRY), _("Country"),
                        _("Enter the country of this user"))
                .addItemAttributeEntry(
                        new JsId(UserItem.DEPLOY_PROFESSIONAL_DATA + "_" + ProfessionalContactDataItem.ATTRIBUTE_ZIPCODE),
                        proContactItem.getAttribute(ProfessionalContactDataItem.ATTRIBUTE_ZIPCODE), _("Zip code"),
                        _("Enter the zip code of this user"))
                .addItemAttributeEntry(
                        new JsId(UserItem.DEPLOY_PROFESSIONAL_DATA + "_" + ProfessionalContactDataItem.ATTRIBUTE_STATE),
                        proContactItem.getAttribute(ProfessionalContactDataItem.ATTRIBUTE_STATE), _("State"),
                        _("Enter the state of this user"))
                .addItemAttributeEntryWithMaxLength(
                        new JsId(UserItem.DEPLOY_PROFESSIONAL_DATA + "_" + ProfessionalContactDataItem.ATTRIBUTE_EMAIL),
                        proContactItem.getAttribute(ProfessionalContactDataItem.ATTRIBUTE_EMAIL), _("Email"),
                        _("Enter the email of this user"), AbstractContactDataItem.MAX_EMAIL_LENGTH)
                .addItemAttributeEntry(
                        new JsId(UserItem.DEPLOY_PROFESSIONAL_DATA + "_" + ProfessionalContactDataItem.ATTRIBUTE_PHONE),
                        proContactItem.getAttribute(ProfessionalContactDataItem.ATTRIBUTE_PHONE), _("Phone"),
                        _("Enter the phone of this user"))
                .addItemAttributeEntry(
                        new JsId(UserItem.DEPLOY_PROFESSIONAL_DATA + "_" + ProfessionalContactDataItem.ATTRIBUTE_MOBILE),
                        proContactItem.getAttribute(ProfessionalContactDataItem.ATTRIBUTE_MOBILE), _("Mobile"),
                        _("Enter the mobile phone of this user"))
                .closeTab();
        return businessForm;

    }

    private Form addDetails(final Form form) {
        final ItemDefinition itemDef = UserDefinition.get();
        return form.openTab(_("Details"))
                .addItemAttributeEntry(itemDef.getAttribute(UserItem.ATTRIBUTE_FIRSTNAME), _("First name"), _("Enter the first name of this user"))
                .addItemAttributeEntry(itemDef.getAttribute(UserItem.ATTRIBUTE_LASTNAME), _("Last name"), _("Enter the last name of this user"))
                .addItemAttributeEntry(itemDef.getAttribute(UserItem.ATTRIBUTE_TITLE), _("Title"), _("Enter the title of this user"))
                .addItemAttributeEntry(itemDef.getAttribute(UserItem.ATTRIBUTE_JOB_TITLE), _("Job title"), _("Enter the job title of this user"))

                .addAutoCompleteEntry(
                        new JsId(UserItem.ATTRIBUTE_MANAGER_ID),
                        _("Manager"),
                        _("Select the manager of this user"),
                        itemDef,
                        UserItem.ATTRIBUTE_USERNAME,
                        UserItem.ATTRIBUTE_ID)
                .closeTab();
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }
}
