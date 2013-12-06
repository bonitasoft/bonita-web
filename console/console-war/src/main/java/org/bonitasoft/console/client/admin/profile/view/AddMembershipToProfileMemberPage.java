package org.bonitasoft.console.client.admin.profile.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bonitasoft.console.client.admin.organization.users.action.AddMembershipAction;
import org.bonitasoft.console.client.admin.organization.users.view.UserListingAdminPage;
import org.bonitasoft.web.rest.model.identity.GroupDefinition;
import org.bonitasoft.web.rest.model.identity.GroupItem;
import org.bonitasoft.web.rest.model.identity.MembershipItem;
import org.bonitasoft.web.rest.model.identity.RoleDefinition;
import org.bonitasoft.web.rest.model.identity.RoleItem;
import org.bonitasoft.web.rest.model.portal.profile.ProfileDefinition;
import org.bonitasoft.web.rest.model.portal.profile.ProfileItem;
import org.bonitasoft.web.rest.model.portal.profile.ProfileMemberDefinition;
import org.bonitasoft.web.rest.model.portal.profile.ProfileMemberItem;
import org.bonitasoft.web.toolkit.client.common.json.JSonItemReader;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.attribute.validator.MandatoryValidator;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.Page;
import org.bonitasoft.web.toolkit.client.ui.component.Strong;
import org.bonitasoft.web.toolkit.client.ui.component.form.Form;

public class AddMembershipToProfileMemberPage extends Page {

    /**
     * 
     */
    private static final String PARAMETER_PROFILE_ID = "profileId";
    
    /**
     * the token of this page
     */
    public static final String TOKEN = "addMembershipToProfileMember";
    
    public static final List<String> PRIVILEGES = new ArrayList<String>();
    
    static {
        PRIVILEGES.add(ProfileListingPage.TOKEN);
    }
    
    public AddMembershipToProfileMemberPage() {
    }

    public AddMembershipToProfileMemberPage(APIID profileId) {
        addParameter(PARAMETER_PROFILE_ID, profileId.toString());
    }

    @Override
    public void defineTitle() {
        this.setTitle("");
        Definitions.get(ProfileDefinition.TOKEN).getAPICaller().get(this.getParameter(PARAMETER_PROFILE_ID), new APICallback() {

            @Override
            public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
                final ProfileItem item = (ProfileItem) new JSonItemReader().getItem(response, Definitions.get(ProfileDefinition.TOKEN));
                final String profileName = item.getAttributeValue(ProfileItem.ATTRIBUTE_NAME);
                setTitle(_("Add a membership to profile %%"), new Strong(_(profileName)));
            }
        });
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    public void buildView() {
        final JsId groupJsId = new JsId(MembershipItem.ATTRIBUTE_GROUP_ID);
        final JsId roleJsId = new JsId(MembershipItem.ATTRIBUTE_ROLE_ID);
        final String profileId = this.getParameter(PARAMETER_PROFILE_ID);

        addBody(new Form(new JsId("addmembership"))

                .addHiddenEntry(ProfileMemberItem.ATTRIBUTE_PROFILE_ID, profileId)

                .addAutoCompleteEntry(
                        groupJsId,
                        _("Group"),
                        _("Select a group to set to this profile"),
                        Definitions.get(GroupDefinition.TOKEN),
                        GroupItem.ATTRIBUTE_DISPLAY_NAME,
                        GroupItem.ATTRIBUTE_ID
                )
                .addAutoCompleteEntry(
                        roleJsId,
                        _("Role"),
                        _("Select the role of this profile in the selected group"),
                        Definitions.get(RoleDefinition.TOKEN),
                        RoleItem.ATTRIBUTE_DISPLAY_NAME,
                        RoleItem.ATTRIBUTE_ID
                )
                .addValidator(groupJsId, new MandatoryValidator())
                .addValidator(roleJsId, new MandatoryValidator())
                .addButton(new JsId("add"), _("Add"), _("Add this membership"), new AddMembershipAction(ProfileMemberDefinition.get()))
                .addCancelButton());
    }

}
