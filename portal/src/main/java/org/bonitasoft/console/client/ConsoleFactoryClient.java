package org.bonitasoft.console.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bonitasoft.console.client.admin.bpm.cases.view.ArchivedCaseMoreDetailsAdminPage;
import org.bonitasoft.console.client.admin.bpm.cases.view.ArchivedCaseQuickDetailsAdminPage;
import org.bonitasoft.console.client.admin.bpm.cases.view.CaseMoreDetailsAdminPage;
import org.bonitasoft.console.client.admin.bpm.cases.view.CaseQuickDetailsAdminPage;
import org.bonitasoft.console.client.admin.bpm.task.view.SelectUserAndAssignTaskPage;
import org.bonitasoft.console.client.admin.bpm.task.view.TaskListingAdminPage;
import org.bonitasoft.console.client.admin.bpm.task.view.TaskMoreDetailsAdminPage;
import org.bonitasoft.console.client.admin.bpm.task.view.TaskQuickDetailsAdminPage;
import org.bonitasoft.console.client.admin.organization.OrganizationImportAndExportPage;
import org.bonitasoft.console.client.admin.organization.group.AddGroupPage;
import org.bonitasoft.console.client.admin.organization.group.GroupListingAdminPage;
import org.bonitasoft.console.client.admin.organization.group.GroupQuickDetailsAdminPage;
import org.bonitasoft.console.client.admin.organization.group.UpdateGroupPage;
import org.bonitasoft.console.client.admin.organization.role.AddRolePage;
import org.bonitasoft.console.client.admin.organization.role.RoleListingPage;
import org.bonitasoft.console.client.admin.organization.role.RoleQuickDetailsPage;
import org.bonitasoft.console.client.admin.organization.role.UpdateRolePage;
import org.bonitasoft.console.client.admin.organization.users.view.AddMembershipPage;
import org.bonitasoft.console.client.admin.organization.users.view.DeactivateUserWarningPopUp;
import org.bonitasoft.console.client.admin.organization.users.view.DeleteMembershipPage;
import org.bonitasoft.console.client.admin.organization.users.view.ListMembershipPage;
import org.bonitasoft.console.client.admin.organization.users.view.PopupAddUserPage;
import org.bonitasoft.console.client.admin.organization.users.view.UpdateUserPage;
import org.bonitasoft.console.client.admin.organization.users.view.UserListingAdminPage;
import org.bonitasoft.console.client.admin.organization.users.view.UserMoreDetailsAdminPage;
import org.bonitasoft.console.client.admin.organization.users.view.UserQuickDetailsAdminPage;
import org.bonitasoft.console.client.admin.organization.users.view.UserQuickDetailsPage;
import org.bonitasoft.console.client.admin.page.view.AddCustomPage;
import org.bonitasoft.console.client.admin.page.view.CustomPagePermissionsValidationPopupPage;
import org.bonitasoft.console.client.admin.page.view.EditCustomPage;
import org.bonitasoft.console.client.admin.page.view.PageListingPage;
import org.bonitasoft.console.client.admin.page.view.PageQuickDetailsPage;
import org.bonitasoft.console.client.admin.process.view.ProcessListingAdminPage;
import org.bonitasoft.console.client.admin.process.view.ProcessMoreDetailsAdminPage;
import org.bonitasoft.console.client.admin.process.view.ProcessQuickDetailsAdminPage;
import org.bonitasoft.console.client.admin.process.view.StartProcessFormPage;
import org.bonitasoft.console.client.admin.process.view.UploadProcessPage;
import org.bonitasoft.console.client.admin.process.view.section.category.AddProcessCategoryPage;
import org.bonitasoft.console.client.admin.process.view.section.category.CreateCategoryAndAddToProcessPage;
import org.bonitasoft.console.client.admin.process.view.section.entitymapping.DeleteActorMemberPage;
import org.bonitasoft.console.client.admin.process.view.section.entitymapping.ListProcessActorGroupPage;
import org.bonitasoft.console.client.admin.process.view.section.entitymapping.ListProcessActorMembershipPage;
import org.bonitasoft.console.client.admin.process.view.section.entitymapping.ListProcessActorRolePage;
import org.bonitasoft.console.client.admin.process.view.section.entitymapping.ListProcessActorUserPage;
import org.bonitasoft.console.client.admin.process.view.section.entitymapping.SelectGroupForActorPage;
import org.bonitasoft.console.client.admin.process.view.section.entitymapping.SelectMembershipForActorPage;
import org.bonitasoft.console.client.admin.process.view.section.entitymapping.SelectRoleForActorPage;
import org.bonitasoft.console.client.admin.process.view.section.entitymapping.SelectUserForActorPage;
import org.bonitasoft.console.client.admin.profile.view.AddGroupToProfileMemberPage;
import org.bonitasoft.console.client.admin.profile.view.AddMembershipToProfileMemberPage;
import org.bonitasoft.console.client.admin.profile.view.AddRoleToProfileMemberPage;
import org.bonitasoft.console.client.admin.profile.view.AddUserToProfileMemberPage;
import org.bonitasoft.console.client.admin.profile.view.DeleteProfileMemberPage;
import org.bonitasoft.console.client.admin.profile.view.ProfileListingPage;
import org.bonitasoft.console.client.admin.profile.view.ProfileMoreDetailsPage;
import org.bonitasoft.console.client.admin.profile.view.ProfileQuickDetailsPage;
import org.bonitasoft.console.client.admin.tenant.view.TenantMaintenancePage;
import org.bonitasoft.console.client.angular.AngularIFrameView;
import org.bonitasoft.console.client.common.system.view.PopupAboutPage;
import org.bonitasoft.console.client.common.view.CustomPage;
import org.bonitasoft.console.client.common.view.CustomPageWithFrame;
import org.bonitasoft.console.client.common.view.PerformTaskPage;
import org.bonitasoft.console.client.menu.view.TechnicalUserServicePausedView;
import org.bonitasoft.console.client.menu.view.TechnicalUserWarningView;
import org.bonitasoft.console.client.technicaluser.businessdata.BDMImportPage;
import org.bonitasoft.console.client.technicaluser.businessdata.BDMImportWarningPopUp;
import org.bonitasoft.console.client.user.application.view.ProcessListingPage;
import org.bonitasoft.console.client.user.cases.view.ArchivedCaseMoreDetailsPage;
import org.bonitasoft.console.client.user.cases.view.ArchivedCaseQuickDetailsPage;
import org.bonitasoft.console.client.user.cases.view.CaseListingPage;
import org.bonitasoft.console.client.user.cases.view.CaseMoreDetailsPage;
import org.bonitasoft.console.client.user.cases.view.CaseQuickDetailsPage;
import org.bonitasoft.console.client.user.cases.view.DisplayCaseFormPage;
import org.bonitasoft.console.client.user.task.view.ArchivedHumanTaskQuickDetailsPage;
import org.bonitasoft.console.client.user.task.view.HumanTaskQuickDetailsPage;
import org.bonitasoft.console.client.user.task.view.ProcessQuickDetailsPage;
import org.bonitasoft.console.client.user.task.view.TasksListingPage;
import org.bonitasoft.console.client.user.task.view.more.ArchivedHumanTaskMoreDetailsPage;
import org.bonitasoft.console.client.user.task.view.more.HumanTaskMoreDetailsPage;
import org.bonitasoft.web.toolkit.client.ApplicationFactoryClient;
import org.bonitasoft.web.toolkit.client.AvailableTokens;
import org.bonitasoft.web.toolkit.client.SHA1;
import org.bonitasoft.web.toolkit.client.Session;
import org.bonitasoft.web.toolkit.client.ui.RawView;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.action.CheckValidSessionBeforeAction;
import org.bonitasoft.web.toolkit.client.ui.component.form.view.BlankPage;
import org.bonitasoft.web.toolkit.client.ui.page.ChangeLangPage;
import org.bonitasoft.web.toolkit.client.ui.page.ItemNotFoundPopup;

import com.google.gwt.core.shared.GWT;

/**
 * console client page
 *
 * @author Yongtao Guo, Haojie Yuan, Zhiheng Yang
 */
public class ConsoleFactoryClient extends ApplicationFactoryClient {

    protected Map<String, String> angularViewsMap = new HashMap<String, String>();

    protected AngularIFrameView angularFrame = new AngularIFrameView();

    private List<String> currentUserAccessRights = null;

    private final Action emptyAction = new Action() {

        @Override
        public void execute() {
        }
    };

    /**
     * Default Constructor.
     */
    public ConsoleFactoryClient() {
        angularViewsMap.put(AngularIFrameView.CASE_LISTING_ADMIN_TOKEN, "/admin/cases/list");
        angularViewsMap.put(AngularIFrameView.APPLICATION_LISTING_PAGE, "/admin/applications");
    }

    protected List<String> getCurrentUserAccessRights() {
        if (currentUserAccessRights == null) {
            currentUserAccessRights = new ArrayList<String>(AvailableTokens.tokens);
            GWT.log("Current log user as access to (with SP pages) :" + listAUthorizedTokens(AvailableTokens.tokens));
        }
        return currentUserAccessRights;
    }

    @Override
    public RawView defineViewTokens(final String token) {

        if (ItemNotFoundPopup.TOKEN.equals(token)) {
            return new ItemNotFoundPopup();
        } else if (DeactivateUserWarningPopUp.TOKEN.equals(token)) {
            return new DeactivateUserWarningPopUp();

            // Manage Cases pages
        } else if (CaseQuickDetailsAdminPage.TOKEN.equals(token) && isUserAuthorized(CaseQuickDetailsAdminPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new CaseQuickDetailsAdminPage();
        } else if (CaseMoreDetailsAdminPage.TOKEN.equals(token) && isUserAuthorized(CaseMoreDetailsAdminPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new CaseMoreDetailsAdminPage();
        } else if (ArchivedCaseQuickDetailsAdminPage.TOKEN.equals(token)
                && isUserAuthorized(ArchivedCaseQuickDetailsAdminPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new ArchivedCaseQuickDetailsAdminPage();
        } else if (ArchivedCaseMoreDetailsAdminPage.TOKEN.equals(token)
                && isUserAuthorized(ArchivedCaseMoreDetailsAdminPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new ArchivedCaseMoreDetailsAdminPage();
            // } else if (ListGroupPage.TOKEN.equals(token)) {
            // return new ListGroupPage();
        } else if (DisplayCaseFormPage.TOKEN.equals(token) && isUserAuthorized(DisplayCaseFormPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new DisplayCaseFormPage();

            // Import export organization page
        } else if (OrganizationImportAndExportPage.TOKEN.equals(token)
                && isUserAuthorized(OrganizationImportAndExportPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new OrganizationImportAndExportPage();

            // Manage Users pages
        } else if (UserListingAdminPage.TOKEN.equals(token) && isUserAuthorized(UserListingAdminPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new UserListingAdminPage();
        } else if (UserQuickDetailsAdminPage.TOKEN.equals(token) && isUserAuthorized(UserQuickDetailsAdminPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new UserQuickDetailsAdminPage();
        } else if (UserQuickDetailsPage.TOKEN.equals(token) && isUserAuthorized(UserQuickDetailsPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new UserQuickDetailsPage();
        } else if (UserMoreDetailsAdminPage.TOKEN.equals(token) && isUserAuthorized(UserMoreDetailsAdminPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new UserMoreDetailsAdminPage();
        } else if (PopupAddUserPage.TOKEN.equals(token) && isUserAuthorized(PopupAddUserPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new PopupAddUserPage();
        } else if (AddMembershipPage.TOKEN.equals(token) && isUserAuthorized(AddMembershipPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new AddMembershipPage();
        } else if (DeleteMembershipPage.TOKEN.equals(token) && isUserAuthorized(DeleteMembershipPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new DeleteMembershipPage();
        } else if (ListMembershipPage.TOKEN.equals(token) && isUserAuthorized(ListMembershipPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new ListMembershipPage();
        } else if (UpdateUserPage.TOKEN.equals(token) && isUserAuthorized(UpdateUserPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new UpdateUserPage();

            // Manage processes pages For Admin
        } else if (ProcessListingAdminPage.TOKEN.equals(token) && isUserAuthorized(ProcessListingAdminPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new ProcessListingAdminPage();
        } else if (ProcessQuickDetailsAdminPage.TOKEN.equals(token) && isUserAuthorized(ProcessQuickDetailsAdminPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new ProcessQuickDetailsAdminPage();
        } else if (ProcessMoreDetailsAdminPage.TOKEN.equals(token) && isUserAuthorized(ProcessMoreDetailsAdminPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new ProcessMoreDetailsAdminPage();
            /*
             * } else if (StartProcessOnBehalfPage.TOKEN.equals(token)) {
             * return new StartProcessOnBehalfPage();
             */
        } else if (UploadProcessPage.TOKEN.equals(token) && isUserAuthorized(UploadProcessPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new UploadProcessPage();
        } else if (CreateCategoryAndAddToProcessPage.TOKEN.equals(token)
                && isUserAuthorized(CreateCategoryAndAddToProcessPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new CreateCategoryAndAddToProcessPage();
        } else if (AddProcessCategoryPage.TOKEN.equals(token) && isUserAuthorized(AddProcessCategoryPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new AddProcessCategoryPage();
        } else if (ListProcessActorUserPage.TOKEN.equals(token) && isUserAuthorized(ListProcessActorUserPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new ListProcessActorUserPage();
        } else if (ListProcessActorGroupPage.TOKEN.equals(token) && isUserAuthorized(ListProcessActorGroupPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new ListProcessActorGroupPage();
        } else if (ListProcessActorRolePage.TOKEN.equals(token) && isUserAuthorized(ListProcessActorRolePage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new ListProcessActorRolePage();
        } else if (ListProcessActorMembershipPage.TOKEN.equals(token)
                && isUserAuthorized(ListProcessActorMembershipPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new ListProcessActorMembershipPage();
        } else if (SelectMembershipForActorPage.TOKEN.equals(token) && isUserAuthorized(SelectMembershipForActorPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new SelectMembershipForActorPage();
        } else if (SelectUserForActorPage.TOKEN.equals(token) && isUserAuthorized(SelectUserForActorPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new SelectUserForActorPage();
        } else if (SelectGroupForActorPage.TOKEN.equals(token) && isUserAuthorized(SelectGroupForActorPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new SelectGroupForActorPage();
        } else if (SelectRoleForActorPage.TOKEN.equals(token) && isUserAuthorized(SelectRoleForActorPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new SelectRoleForActorPage();

            // Manage Roles pages
        } else if (RoleListingPage.TOKEN.equals(token) && isUserAuthorized(RoleListingPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new RoleListingPage();
        } else if (RoleQuickDetailsPage.TOKEN.equals(token) && isUserAuthorized(RoleQuickDetailsPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new RoleQuickDetailsPage();
        } else if (AddRolePage.TOKEN.equals(token) && isUserAuthorized(AddRolePage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new AddRolePage();
        } else if (UpdateRolePage.TOKEN.equals(token) && isUserAuthorized(UpdateRolePage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new UpdateRolePage();

            // Manage Groups pages
        } else if (GroupListingAdminPage.TOKEN.equals(token) && isUserAuthorized(GroupListingAdminPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new GroupListingAdminPage();
        } else if (GroupQuickDetailsAdminPage.TOKEN.equals(token) && isUserAuthorized(GroupQuickDetailsAdminPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new GroupQuickDetailsAdminPage();
        } else if (AddGroupPage.TOKEN.equals(token) && isUserAuthorized(AddGroupPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new AddGroupPage();
        } else if (UpdateGroupPage.TOKEN.equals(token) && isUserAuthorized(UpdateGroupPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new UpdateGroupPage();

            // User Privileges settings pages
        } else if (ProfileListingPage.TOKEN.equals(token) && isUserAuthorized(ProfileListingPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new ProfileListingPage();
        } else if (ProfileMoreDetailsPage.TOKEN.equals(token) && isUserAuthorized(ProfileMoreDetailsPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new ProfileMoreDetailsPage();
        } else if (ProfileQuickDetailsPage.TOKEN.equals(token) && isUserAuthorized(ProfileQuickDetailsPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new ProfileQuickDetailsPage();
        } else if (AddGroupToProfileMemberPage.TOKEN.equals(token) && isUserAuthorized(AddGroupToProfileMemberPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new AddGroupToProfileMemberPage();
        } else if (AddRoleToProfileMemberPage.TOKEN.equals(token) && isUserAuthorized(AddRoleToProfileMemberPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new AddRoleToProfileMemberPage();
        } else if (AddUserToProfileMemberPage.TOKEN.equals(token) && isUserAuthorized(AddUserToProfileMemberPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new AddUserToProfileMemberPage();
        } else if (AddMembershipToProfileMemberPage.TOKEN.equals(token)
                && isUserAuthorized(AddMembershipToProfileMemberPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new AddMembershipToProfileMemberPage();
        } else if (DeleteProfileMemberPage.TOKEN.equals(token) && isUserAuthorized(DeleteProfileMemberPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new DeleteProfileMemberPage();

        } else if (DeleteActorMemberPage.TOKEN.equals(token)) {
            return new DeleteActorMemberPage();

            // Manage Tasks pages
        } else if (TaskListingAdminPage.TOKEN.equals(token) && isUserAuthorized(TaskListingAdminPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new TaskListingAdminPage();
        } else if (SelectUserAndAssignTaskPage.TOKEN.equals(token) && isUserAuthorized(SelectUserAndAssignTaskPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new SelectUserAndAssignTaskPage();
        } else if (TaskQuickDetailsAdminPage.TOKEN.equals(token) && isUserAuthorized(TaskQuickDetailsAdminPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new TaskQuickDetailsAdminPage();
        } else if (TaskMoreDetailsAdminPage.TOKEN.equals(token) && isUserAuthorized(TaskMoreDetailsAdminPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new TaskMoreDetailsAdminPage();

            /*
             * THEME pages
             * } else if (ListThemePage.TOKEN.equals(token)) {
             * return new ListThemePage();
             * } else if (UploadThemePage.TOKEN.equals(token)) {
             * return new UploadThemePage();
             * } else if (EditThemePage.TOKEN.equals(token)) {
             * return new EditThemePage();
             */

            // Visualize & do tasks
        } else if (TasksListingPage.TOKEN.equals(token) && isUserAuthorized(TasksListingPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new TasksListingPage();
        } else if (HumanTaskQuickDetailsPage.TOKEN.equals(token) && isUserAuthorized(HumanTaskQuickDetailsPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new HumanTaskQuickDetailsPage();
        } else if (HumanTaskMoreDetailsPage.TOKEN.equals(token) && isUserAuthorized(HumanTaskMoreDetailsPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new HumanTaskMoreDetailsPage();
        } else if (ArchivedHumanTaskQuickDetailsPage.TOKEN.equals(token)
                && isUserAuthorized(ArchivedHumanTaskQuickDetailsPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new ArchivedHumanTaskQuickDetailsPage();
        } else if (ArchivedHumanTaskMoreDetailsPage.TOKEN.equals(token)
                && isUserAuthorized(ArchivedHumanTaskMoreDetailsPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new ArchivedHumanTaskMoreDetailsPage();
        } else if (PerformTaskPage.TOKEN.equals(token) && isUserAuthorized(PerformTaskPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new PerformTaskPage();

            // Visualize & Start processes
        } else if (ProcessListingPage.TOKEN.equals(token) && isUserAuthorized(ProcessListingPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new ProcessListingPage();
        } else if (ProcessQuickDetailsPage.TOKEN.equals(token) && isUserAuthorized(ProcessQuickDetailsPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new ProcessQuickDetailsPage();
        } else if (StartProcessFormPage.TOKEN.equals(token) && isUserAuthorized(StartProcessFormPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new StartProcessFormPage();

            // Visualize Cases
        } else if (CaseListingPage.TOKEN.equals(token) && isUserAuthorized(CaseListingPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new CaseListingPage();
        } else if (CaseQuickDetailsPage.TOKEN.equals(token) && isUserAuthorized(CaseQuickDetailsPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new CaseQuickDetailsPage();
        } else if (ArchivedCaseQuickDetailsPage.TOKEN.equals(token) && isUserAuthorized(ArchivedCaseQuickDetailsPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new ArchivedCaseQuickDetailsPage();
        } else if (ArchivedCaseMoreDetailsPage.TOKEN.equals(token) && isUserAuthorized(ArchivedCaseMoreDetailsPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new ArchivedCaseMoreDetailsPage();
        } else if (CaseMoreDetailsPage.TOKEN.equals(token) && isUserAuthorized(CaseMoreDetailsPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new CaseMoreDetailsPage();

        } else if (TechnicalUserWarningView.TOKEN.equals(token)) {
            return new TechnicalUserWarningView();
            // System
        } else if (PopupAboutPage.TOKEN.equals(token)) {
            return new PopupAboutPage();
        } else if (TechnicalUserServicePausedView.TOKEN.equals(token)) {
            return new TechnicalUserServicePausedView();
        } else if (ChangeLangPage.TOKEN.equals(token)) {
            return new ChangeLangPage();
        } else if (TenantMaintenancePage.TOKEN.equals(token) && isUserAuthorized(TenantMaintenancePage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new TenantMaintenancePage();
            // Custom pages
        } else if (PageListingPage.TOKEN.equals(token) && isUserAuthorized(PageListingPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new PageListingPage();
        } else if (AddCustomPage.TOKEN.equals(token) && isUserAuthorized(AddCustomPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new AddCustomPage();
        } else if (EditCustomPage.TOKEN.equals(token) && isUserAuthorized(EditCustomPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new EditCustomPage();
        } else if (PageQuickDetailsPage.TOKEN.equals(token) && isUserAuthorized(PageListingPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new PageQuickDetailsPage();
        } else if (CustomPagePermissionsValidationPopupPage.TOKEN.equals(token) && isUserAuthorized(PageListingPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new CustomPagePermissionsValidationPopupPage();

            // Custom pages
        } else if (token != null && token.startsWith(CustomPageWithFrame.TOKEN)) {
            if (isUserAuthorized(token, getCurrentUserAccessRights())) {
                return new CustomPageWithFrame(token);
            } else {
                return new BlankPage();
            }
        } else if (token != null && token.startsWith(CustomPage.TOKEN)) {
            if (isUserAuthorized(token, getCurrentUserAccessRights())) {
                return new CustomPage(token);
            } else {
                return new BlankPage();
            }
            // BDM
        } else if (BDMImportPage.TOKEN.equals(token) && isUserAuthorized(BDMImportPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new BDMImportPage();
        } else if (BDMImportWarningPopUp.TOKEN.equals(token) && isUserAuthorized(BDMImportPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new BDMImportWarningPopUp();

        } else if (angularViewsMap.containsKey(token) && isUserAuthorized(Arrays.asList(token), getCurrentUserAccessRights())) {
            // No action is necessary as an unauthorized request will result in a page reload.
            new CheckValidSessionBeforeAction(emptyAction).execute();
            angularFrame.setUrl("#" + angularViewsMap.get(token), token);
            return angularFrame;
        } else {
            return new BlankPage();
        }
    }

    protected String listAUthorizedTokens(final List<String> currentUserAccessRights) {
        String result = "";

        final Map<String, List<String>> pagePrivileges = buildApplicationPagesPrivileges();

        for (final Map.Entry<String, List<String>> entry : pagePrivileges.entrySet()) {
            result = isUserAuthorized(entry.getValue(), currentUserAccessRights) ? result + entry.getKey() + ", " : result;
        }

        return result;

    }

    protected boolean isUserAuthorized(final String token, final List<String> accessRights) {

        final String sessionId = new String(Session.getParameter("session_id"));

        final String calcSHA1 = SHA1.calcSHA1(token.concat(sessionId));

        if (accessRights.contains(calcSHA1.toUpperCase())) {
            return true;
        }

        return false;
    }

    protected Map<String, List<String>> buildApplicationPagesPrivileges() {
        final Map<String, List<String>> pagePrivileges = new HashMap<String, List<String>>();
        pagePrivileges.put(CaseQuickDetailsAdminPage.TOKEN, CaseQuickDetailsAdminPage.PRIVILEGES);
        pagePrivileges.put(CaseMoreDetailsAdminPage.TOKEN, CaseMoreDetailsAdminPage.PRIVILEGES);
        pagePrivileges.put(ArchivedCaseQuickDetailsAdminPage.TOKEN, ArchivedCaseQuickDetailsAdminPage.PRIVILEGES);
        pagePrivileges.put(ArchivedCaseMoreDetailsAdminPage.TOKEN, ArchivedCaseMoreDetailsAdminPage.PRIVILEGES);
        pagePrivileges.put(DisplayCaseFormPage.TOKEN, DisplayCaseFormPage.PRIVILEGES);
        pagePrivileges.put(OrganizationImportAndExportPage.TOKEN, OrganizationImportAndExportPage.PRIVILEGES);
        pagePrivileges.put(UserListingAdminPage.TOKEN, UserListingAdminPage.PRIVILEGES);
        pagePrivileges.put(UserQuickDetailsAdminPage.TOKEN, UserQuickDetailsAdminPage.PRIVILEGES);
        pagePrivileges.put(UserQuickDetailsPage.TOKEN, UserQuickDetailsPage.PRIVILEGES);
        pagePrivileges.put(UserMoreDetailsAdminPage.TOKEN, UserMoreDetailsAdminPage.PRIVILEGES);
        pagePrivileges.put(PopupAddUserPage.TOKEN, PopupAddUserPage.PRIVILEGES);
        pagePrivileges.put(AddMembershipPage.TOKEN, AddMembershipPage.PRIVILEGES);
        pagePrivileges.put(DeleteMembershipPage.TOKEN, DeleteMembershipPage.PRIVILEGES);
        pagePrivileges.put(ListMembershipPage.TOKEN, ListMembershipPage.PRIVILEGES);
        pagePrivileges.put(UpdateUserPage.TOKEN, UpdateUserPage.PRIVILEGES);
        pagePrivileges.put(ProcessListingAdminPage.TOKEN, ProcessListingAdminPage.PRIVILEGES);
        pagePrivileges.put(ProcessQuickDetailsAdminPage.TOKEN, ProcessQuickDetailsAdminPage.PRIVILEGES);
        pagePrivileges.put(ProcessMoreDetailsAdminPage.TOKEN, ProcessMoreDetailsAdminPage.PRIVILEGES);
        pagePrivileges.put(UploadProcessPage.TOKEN, UploadProcessPage.PRIVILEGES);
        pagePrivileges.put(CreateCategoryAndAddToProcessPage.TOKEN, CreateCategoryAndAddToProcessPage.PRIVILEGES);
        pagePrivileges.put(AddProcessCategoryPage.TOKEN, AddProcessCategoryPage.PRIVILEGES);
        pagePrivileges.put(ListProcessActorUserPage.TOKEN, ListProcessActorUserPage.PRIVILEGES);
        pagePrivileges.put(ListProcessActorGroupPage.TOKEN, ListProcessActorGroupPage.PRIVILEGES);
        pagePrivileges.put(ListProcessActorRolePage.TOKEN, ListProcessActorRolePage.PRIVILEGES);
        pagePrivileges.put(ListProcessActorMembershipPage.TOKEN, ListProcessActorMembershipPage.PRIVILEGES);
        pagePrivileges.put(SelectMembershipForActorPage.TOKEN, SelectMembershipForActorPage.PRIVILEGES);
        pagePrivileges.put(SelectUserForActorPage.TOKEN, SelectUserForActorPage.PRIVILEGES);
        pagePrivileges.put(SelectGroupForActorPage.TOKEN, SelectGroupForActorPage.PRIVILEGES);
        pagePrivileges.put(SelectRoleForActorPage.TOKEN, SelectRoleForActorPage.PRIVILEGES);
        pagePrivileges.put(RoleListingPage.TOKEN, RoleListingPage.PRIVILEGES);
        pagePrivileges.put(RoleQuickDetailsPage.TOKEN, RoleQuickDetailsPage.PRIVILEGES);
        pagePrivileges.put(AddRolePage.TOKEN, AddRolePage.PRIVILEGES);
        pagePrivileges.put(UpdateRolePage.TOKEN, UpdateRolePage.PRIVILEGES);
        pagePrivileges.put(GroupListingAdminPage.TOKEN, GroupListingAdminPage.PRIVILEGES);
        pagePrivileges.put(GroupQuickDetailsAdminPage.TOKEN, GroupQuickDetailsAdminPage.PRIVILEGES);
        pagePrivileges.put(AddGroupPage.TOKEN, AddGroupPage.PRIVILEGES);
        pagePrivileges.put(UpdateGroupPage.TOKEN, UpdateGroupPage.PRIVILEGES);
        pagePrivileges.put(ProfileListingPage.TOKEN, ProfileListingPage.PRIVILEGES);
        pagePrivileges.put(ProfileMoreDetailsPage.TOKEN, ProfileMoreDetailsPage.PRIVILEGES);
        pagePrivileges.put(ProfileQuickDetailsPage.TOKEN, ProfileQuickDetailsPage.PRIVILEGES);
        pagePrivileges.put(AddGroupToProfileMemberPage.TOKEN, AddGroupToProfileMemberPage.PRIVILEGES);
        pagePrivileges.put(AddRoleToProfileMemberPage.TOKEN, AddRoleToProfileMemberPage.PRIVILEGES);
        pagePrivileges.put(AddUserToProfileMemberPage.TOKEN, AddUserToProfileMemberPage.PRIVILEGES);
        pagePrivileges.put(AddMembershipToProfileMemberPage.TOKEN, AddMembershipToProfileMemberPage.PRIVILEGES);
        pagePrivileges.put(DeleteProfileMemberPage.TOKEN, DeleteProfileMemberPage.PRIVILEGES);
        pagePrivileges.put(TaskListingAdminPage.TOKEN, TaskListingAdminPage.PRIVILEGES);
        pagePrivileges.put(SelectUserAndAssignTaskPage.TOKEN, SelectUserAndAssignTaskPage.PRIVILEGES);
        pagePrivileges.put(TaskQuickDetailsAdminPage.TOKEN, TaskQuickDetailsAdminPage.PRIVILEGES);
        pagePrivileges.put(TaskMoreDetailsAdminPage.TOKEN, TaskMoreDetailsAdminPage.PRIVILEGES);
        pagePrivileges.put(TasksListingPage.TOKEN, TasksListingPage.PRIVILEGES);
        pagePrivileges.put(HumanTaskQuickDetailsPage.TOKEN, HumanTaskQuickDetailsPage.PRIVILEGES);
        pagePrivileges.put(HumanTaskMoreDetailsPage.TOKEN, HumanTaskMoreDetailsPage.PRIVILEGES);
        pagePrivileges.put(ArchivedHumanTaskQuickDetailsPage.TOKEN, ArchivedHumanTaskQuickDetailsPage.PRIVILEGES);
        pagePrivileges.put(ArchivedHumanTaskMoreDetailsPage.TOKEN, ArchivedHumanTaskMoreDetailsPage.PRIVILEGES);
        pagePrivileges.put(PerformTaskPage.TOKEN, PerformTaskPage.PRIVILEGES);
        pagePrivileges.put(ProcessListingPage.TOKEN, ProcessListingPage.PRIVILEGES);
        pagePrivileges.put(ProcessQuickDetailsPage.TOKEN, ProcessQuickDetailsPage.PRIVILEGES);
        pagePrivileges.put(StartProcessFormPage.TOKEN, StartProcessFormPage.PRIVILEGES);
        pagePrivileges.put(CaseListingPage.TOKEN, CaseListingPage.PRIVILEGES);
        pagePrivileges.put(CaseQuickDetailsPage.TOKEN, CaseQuickDetailsPage.PRIVILEGES);
        pagePrivileges.put(ArchivedCaseQuickDetailsPage.TOKEN, ArchivedCaseQuickDetailsPage.PRIVILEGES);
        pagePrivileges.put(ArchivedCaseMoreDetailsPage.TOKEN, ArchivedCaseMoreDetailsPage.PRIVILEGES);
        pagePrivileges.put(CaseMoreDetailsPage.TOKEN, CaseMoreDetailsPage.PRIVILEGES);

        return pagePrivileges;
    }

    protected boolean isUserAuthorized(final List<String> privileges, final List<String> accessRights) {

        final String sessionId = new String(Session.getParameter("session_id"));

        for (final String privilege : privileges) {

            final String calcSHA1 = SHA1.calcSHA1(privilege.concat(sessionId));

            if (accessRights.contains(calcSHA1.toUpperCase())) {
                GWT.log("User is granted access to targeted page thanks to : " + privilege);
                return true;
            }

        }

        return false;
    }

}
