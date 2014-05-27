package org.bonitasoft.console.client.admin.profile.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bonitasoft.web.rest.model.portal.profile.ProfileItem;
import org.bonitasoft.web.rest.model.portal.profile.ProfileMemberDefinition;
import org.bonitasoft.web.rest.model.portal.profile.ProfileMemberItem;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.common.util.MapUtil;
import org.bonitasoft.web.toolkit.client.common.util.StringUtil;
import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.api.request.ApiSearchResultPager;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.RedirectionAction;
import org.bonitasoft.web.toolkit.client.ui.component.Definition;
import org.bonitasoft.web.toolkit.client.ui.component.Section;
import org.bonitasoft.web.toolkit.client.ui.component.Text;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemDetailsAction;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemDetailsMetadata;
import org.bonitasoft.web.toolkit.client.ui.utils.Filler;

/**
 * @author Bastien ROHART
 */
public class ProfileQuickDetailsPage extends AbstractProfileDetailsPage {

    public static final String TOKEN = "profilequickdetails";

    public static final List<String> PRIVILEGES = new ArrayList<String>();

    static {
        PRIVILEGES.add(ProfileListingPage.TOKEN);
    }

    @Override
    protected LinkedList<ItemDetailsMetadata> defineMetadatas(final ProfileItem item) {
        final LinkedList<ItemDetailsMetadata> metadatas = new LinkedList<ItemDetailsMetadata>();
        return metadatas;
    }

    @Override
    protected void defineTitle(final ProfileItem item) {
        setTitle(_(item.getName()));
        addDescription(StringUtil.isBlank(item.getDescription()) ? _("No description.") : _(item.getDescription()));
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    protected LinkedList<ItemDetailsAction> defineActions(final ProfileItem item) {
        final LinkedList<ItemDetailsAction> actions = super.defineActions(item);
        // MORE
        actions.add(new ItemDetailsAction(new JsId("btn-more"), _("More"), _("Show more details about this profile"), new RedirectionAction(
                ProfileMoreDetailsPage.TOKEN, new Arg("id", item.getId()))));

        return actions;
    }

    @Override
    protected List<String> defineDeploys() {
        return null;
    }

    @Override
    protected void buildBody(final ProfileItem item) {
        addBody(technicalSection(item));
    }

    protected Section technicalSection(final ProfileItem item) {
        return new Section(_("Technical details")).addBody(nbUsersDefinition(item)).addBody(nbGroupDefinition(item)).addBody(nbRolesDefinition(item))
                .addBody(nbMembershipsDefinition(item));
    }

    private Definition nbMembershipsDefinition(final ProfileItem item) {
        final Text nbMemberships = new Text("?");
        nbMemberships.addFiller(new MembershipsFiller(item));
        return new Definition(_("Memberships: %nb_memberships%", new Arg("nb_memberships", "")), "%%", nbMemberships);
    }

    private Definition nbRolesDefinition(final ProfileItem item) {
        final Text nbRoles = new Text("?");
        nbRoles.addFiller(new RolesFiller(item));
        return new Definition(_("Roles: %nb_roles%", new Arg("nb_roles", "")), "%%", nbRoles);
    }

    private Definition nbGroupDefinition(final ProfileItem item) {
        final Text nbGroups = new Text("?");
        nbGroups.addFiller(new GroupsFiller(item));
        return new Definition(_("Groups: %nb_groups%", new Arg("nb_groups", "")), "%%", nbGroups);
    }

    private Definition nbUsersDefinition(final ProfileItem item) {
        final Text nbUsers = new Text("?");
        nbUsers.addFiller(new UsersFiller(item));
        return new Definition(_("Users: %nb_users%", new Arg("nb_users", "")), "%%", nbUsers);
    }

    /**
     * @author Bastien ROHART
     * 
     */
    private final class UsersFiller extends Filler<Text> {

        private final ProfileItem profile;

        private UsersFiller(final ProfileItem profile) {
            this.profile = profile;
        }

        @Override
        protected void getData(final APICallback callback) {
            new APICaller(ProfileMemberDefinition.get()).search(0, 1, null, null, MapUtil.asMap(
                    new Arg(ProfileMemberItem.ATTRIBUTE_PROFILE_ID, profile.getId()), new Arg(ProfileMemberItem.FILTER_MEMBER_TYPE,
                            ProfileMemberItem.VALUE_MEMBER_TYPE_USER)), callback);
        }

        @Override
        protected void setData(final String json, final Map<String, String> headers) {
            final ApiSearchResultPager resultPager = ApiSearchResultPager.parse(headers);
            target.getElement().setInnerText(String.valueOf(resultPager.getNbTotalResults()));
        }
    }

    /**
     * @author Bastien ROHART
     * 
     */
    private final class GroupsFiller extends Filler<Text> {

        private final ProfileItem profile;

        private GroupsFiller(final ProfileItem profile) {
            this.profile = profile;
        }

        @Override
        protected void getData(final APICallback callback) {
            new APICaller(ProfileMemberDefinition.get()).search(0, 2, null, null, MapUtil.asMap(
                    new Arg(ProfileMemberItem.ATTRIBUTE_PROFILE_ID, profile.getId()), new Arg(ProfileMemberItem.FILTER_MEMBER_TYPE,
                            ProfileMemberItem.VALUE_MEMBER_TYPE_GROUP)), callback);
        }

        @Override
        protected void setData(final String json, final Map<String, String> headers) {
            final ApiSearchResultPager resultPager = ApiSearchResultPager.parse(headers);
            target.getElement().setInnerText(String.valueOf(resultPager.getNbTotalResults()));
        }
    }

    /**
     * @author Bastien ROHART
     * 
     */
    private final class RolesFiller extends Filler<Text> {

        private final ProfileItem profile;

        private RolesFiller(final ProfileItem profile) {
            this.profile = profile;
        }

        @Override
        protected void getData(final APICallback callback) {
            new APICaller(ProfileMemberDefinition.get()).search(0, 2, null, null, MapUtil.asMap(
                    new Arg(ProfileMemberItem.ATTRIBUTE_PROFILE_ID, profile.getId()), new Arg(ProfileMemberItem.FILTER_MEMBER_TYPE,
                            ProfileMemberItem.VALUE_MEMBER_TYPE_ROLE)), callback);
        }

        @Override
        protected void setData(final String json, final Map<String, String> headers) {
            final ApiSearchResultPager resultPager = ApiSearchResultPager.parse(headers);
            target.getElement().setInnerText(String.valueOf(resultPager.getNbTotalResults()));
        }
    }

    /**
     * @author Bastien ROHART
     * 
     */
    private final class MembershipsFiller extends Filler<Text> {

        private final ProfileItem profile;

        private MembershipsFiller(final ProfileItem profile) {
            this.profile = profile;
        }

        @Override
        protected void getData(final APICallback callback) {
            new APICaller(ProfileMemberDefinition.get()).search(0, 2, null, null, MapUtil.asMap(
                    new Arg(ProfileMemberItem.ATTRIBUTE_PROFILE_ID, profile.getId()), new Arg(ProfileMemberItem.FILTER_MEMBER_TYPE,
                            ProfileMemberItem.VALUE_MEMBER_TYPE_MEMBERSHIP)), callback);
        }

        @Override
        protected void setData(final String json, final Map<String, String> headers) {
            final ApiSearchResultPager resultPager = ApiSearchResultPager.parse(headers);
            target.getElement().setInnerText(String.valueOf(resultPager.getNbTotalResults()));
        }
    }
}
