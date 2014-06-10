package org.bonitasoft.console.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bonitasoft.console.client.admin.bpm.cases.view.ArchivedCaseMoreDetailsAdminPage;
import org.bonitasoft.console.client.admin.bpm.cases.view.ArchivedCaseQuickDetailsAdminPage;
import org.bonitasoft.console.client.admin.bpm.cases.view.CaseListingAdminPage;
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
import org.bonitasoft.console.client.admin.profile.view.EditProfilePage;
import org.bonitasoft.console.client.admin.profile.view.ProfileListingPage;
import org.bonitasoft.console.client.admin.profile.view.ProfileMoreDetailsPage;
import org.bonitasoft.console.client.admin.profile.view.ProfileQuickDetailsPage;
import org.bonitasoft.console.client.angular.AngularIFrameView;
import org.bonitasoft.console.client.common.system.view.PopupAboutPage;
import org.bonitasoft.console.client.common.view.PerformTaskPage;
import org.bonitasoft.console.client.menu.view.TechnicalUserWarningView;
import org.bonitasoft.console.client.user.application.view.ProcessListingPage;
import org.bonitasoft.console.client.user.cases.view.ArchivedCaseMoreDetailsPage;
import org.bonitasoft.console.client.user.cases.view.ArchivedCaseQuickDetailsPage;
import org.bonitasoft.console.client.user.cases.view.CaseListingPage;
import org.bonitasoft.console.client.user.cases.view.CaseMoreDetailsPage;
import org.bonitasoft.console.client.user.cases.view.CaseQuickDetailsPage;
import org.bonitasoft.console.client.user.cases.view.DisplayCaseFormPage;
import org.bonitasoft.console.client.user.task.view.ArchivedHumanTaskQuickDetailsPage;
import org.bonitasoft.console.client.user.task.view.HumanTaskQuickDetailsPage;
import org.bonitasoft.console.client.user.task.view.ProcessMoreDetailsPage;
import org.bonitasoft.console.client.user.task.view.ProcessQuickDetailsPage;
import org.bonitasoft.console.client.user.task.view.TasksListingPage;
import org.bonitasoft.console.client.user.task.view.more.ArchivedHumanTaskMoreDetailsPage;
import org.bonitasoft.console.client.user.task.view.more.HumanTaskMoreDetailsPage;
import org.bonitasoft.web.toolkit.client.ApplicationFactoryClient;
import org.bonitasoft.web.toolkit.client.AvailableTokens;
import org.bonitasoft.web.toolkit.client.SHA1;
import org.bonitasoft.web.toolkit.client.Session;
import org.bonitasoft.web.toolkit.client.ui.Page;
import org.bonitasoft.web.toolkit.client.ui.RawView;
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

    @Override
    public RawView defineViewTokens(final String token) {

        List<String> currentUserAccessRights = new ArrayList<String>(AvailableTokens.tokens);

        GWT.log("Current log user as access to :" + listAUthorizedTokens(AvailableTokens.tokens));

        if("myprettypage".equals(token)) {
            return new AngularIFrameView("myprettypage");
        }

        if (ItemNotFoundPopup.TOKEN.equals(token)) {
            return new ItemNotFoundPopup();
        } else if (DeactivateUserWarningPopUp.TOKEN.equals(token)) {
            return new DeactivateUserWarningPopUp();

            // Manage Cases pages
        } else if (CaseListingAdminPage.TOKEN.equals(token) && isUserAuthorized(CaseListingAdminPage.PRIVILEGES, currentUserAccessRights)) {
            return new CaseListingAdminPage();
        } else if (CaseQuickDetailsAdminPage.TOKEN.equals(token) && isUserAuthorized(CaseQuickDetailsAdminPage.PRIVILEGES, currentUserAccessRights)) {
            return new CaseQuickDetailsAdminPage();
        } else if (CaseMoreDetailsAdminPage.TOKEN.equals(token) && isUserAuthorized(CaseMoreDetailsAdminPage.PRIVILEGES, currentUserAccessRights)) {
            return new CaseMoreDetailsAdminPage();
        } else if (ArchivedCaseQuickDetailsAdminPage.TOKEN.equals(token) && isUserAuthorized(ArchivedCaseQuickDetailsAdminPage.PRIVILEGES, currentUserAccessRights)) {
            return new ArchivedCaseQuickDetailsAdminPage();
        } else if (ArchivedCaseMoreDetailsAdminPage.TOKEN.equals(token) && isUserAuthorized(ArchivedCaseMoreDetailsAdminPage.PRIVILEGES, currentUserAccessRights)) {
            return new ArchivedCaseMoreDetailsAdminPage();
            // } else if (ListGroupPage.TOKEN.equals(token)) {
            // return new ListGroupPage();
        } else if (DisplayCaseFormPage.TOKEN.equals(token) && isUserAuthorized(DisplayCaseFormPage.PRIVILEGES, currentUserAccessRights)) {
            return new DisplayCaseFormPage();

            // Import export organization page
        } else if (OrganizationImportAndExportPage.TOKEN.equals(token) && isUserAuthorized(OrganizationImportAndExportPage.PRIVILEGES, currentUserAccessRights)) {
            return new OrganizationImportAndExportPage();

            // Manage Users pages
        } else if (UserListingAdminPage.TOKEN.equals(token) && isUserAuthorized(UserListingAdminPage.PRIVILEGES, currentUserAccessRights)) {
            return new UserListingAdminPage();
        } else if (UserQuickDetailsAdminPage.TOKEN.equals(token) && isUserAuthorized(UserQuickDetailsAdminPage.PRIVILEGES, currentUserAccessRights)) {
            return new UserQuickDetailsAdminPage();
        } else if (UserQuickDetailsPage.TOKEN.equals(token) && isUserAuthorized(UserQuickDetailsPage.PRIVILEGES, currentUserAccessRights)) {
            return new UserQuickDetailsPage();
        } else if (UserMoreDetailsAdminPage.TOKEN.equals(token) && isUserAuthorized(UserMoreDetailsAdminPage.PRIVILEGES, currentUserAccessRights)) {
            return new UserMoreDetailsAdminPage();
        } else if (PopupAddUserPage.TOKEN.equals(token) && isUserAuthorized(PopupAddUserPage.PRIVILEGES, currentUserAccessRights)) {
            return new PopupAddUserPage();
        } else if (AddMembershipPage.TOKEN.equals(token) && isUserAuthorized(AddMembershipPage.PRIVILEGES, currentUserAccessRights)) {
            return new AddMembershipPage();
        } else if (DeleteMembershipPage.TOKEN.equals(token) && isUserAuthorized(DeleteMembershipPage.PRIVILEGES, currentUserAccessRights)) {
            return new DeleteMembershipPage();
        } else if (ListMembershipPage.TOKEN.equals(token) && isUserAuthorized(ListMembershipPage.PRIVILEGES, currentUserAccessRights)) {
            return new ListMembershipPage();
        } else if (UpdateUserPage.TOKEN.equals(token) && isUserAuthorized(UpdateUserPage.PRIVILEGES, currentUserAccessRights)) {
            return new UpdateUserPage();

            // Manage Apps pages For Admin
        } else if (ProcessListingAdminPage.TOKEN.equals(token) && isUserAuthorized(ProcessListingAdminPage.PRIVILEGES, currentUserAccessRights)) {
            return new ProcessListingAdminPage();
        } else if (ProcessQuickDetailsAdminPage.TOKEN.equals(token) && isUserAuthorized(ProcessQuickDetailsAdminPage.PRIVILEGES, currentUserAccessRights)) {
            return new ProcessQuickDetailsAdminPage();
        } else if (ProcessMoreDetailsAdminPage.TOKEN.equals(token) && isUserAuthorized(ProcessMoreDetailsAdminPage.PRIVILEGES, currentUserAccessRights)) {
            return new ProcessMoreDetailsAdminPage();
        /*} else if (StartProcessOnBehalfPage.TOKEN.equals(token)) {
            return new StartProcessOnBehalfPage();*/
        } else if (UploadProcessPage.TOKEN.equals(token) && isUserAuthorized(UploadProcessPage.PRIVILEGES, currentUserAccessRights)) {
            return new UploadProcessPage();
        } else if (CreateCategoryAndAddToProcessPage.TOKEN.equals(token) && isUserAuthorized(CreateCategoryAndAddToProcessPage.PRIVILEGES, currentUserAccessRights)) {
            return new CreateCategoryAndAddToProcessPage();
        } else if (AddProcessCategoryPage.TOKEN.equals(token) && isUserAuthorized(AddProcessCategoryPage.PRIVILEGES, currentUserAccessRights)) {
            return new AddProcessCategoryPage();
        } else if (ListProcessActorUserPage.TOKEN.equals(token) && isUserAuthorized(ListProcessActorUserPage.PRIVILEGES, currentUserAccessRights)) {
            return new ListProcessActorUserPage();
        } else if (ListProcessActorGroupPage.TOKEN.equals(token) && isUserAuthorized(ListProcessActorGroupPage.PRIVILEGES, currentUserAccessRights)) {
            return new ListProcessActorGroupPage();
        } else if (ListProcessActorRolePage.TOKEN.equals(token) && isUserAuthorized(ListProcessActorRolePage.PRIVILEGES, currentUserAccessRights)) {
            return new ListProcessActorRolePage();
        } else if (ListProcessActorMembershipPage.TOKEN.equals(token) && isUserAuthorized(ListProcessActorMembershipPage.PRIVILEGES, currentUserAccessRights)) {
            return new ListProcessActorMembershipPage();
        } else if (SelectMembershipForActorPage.TOKEN.equals(token) && isUserAuthorized(SelectMembershipForActorPage.PRIVILEGES, currentUserAccessRights)) {
            return new SelectMembershipForActorPage();
        } else if (SelectUserForActorPage.TOKEN.equals(token) && isUserAuthorized(SelectUserForActorPage.PRIVILEGES, currentUserAccessRights)) {
            return new SelectUserForActorPage();
        } else if (SelectGroupForActorPage.TOKEN.equals(token) && isUserAuthorized(SelectGroupForActorPage.PRIVILEGES, currentUserAccessRights)) {
            return new SelectGroupForActorPage();
        } else if (SelectRoleForActorPage.TOKEN.equals(token) && isUserAuthorized(SelectRoleForActorPage.PRIVILEGES, currentUserAccessRights)) {
            return new SelectRoleForActorPage();

            // Manage Roles pages
        } else if (RoleListingPage.TOKEN.equals(token) && isUserAuthorized(RoleListingPage.PRIVILEGES, currentUserAccessRights) ) {
            return new RoleListingPage();
        } else if (RoleQuickDetailsPage.TOKEN.equals(token) && isUserAuthorized(RoleQuickDetailsPage.PRIVILEGES, currentUserAccessRights)) {
            return new RoleQuickDetailsPage();
        } else if (AddRolePage.TOKEN.equals(token) && isUserAuthorized(AddRolePage.PRIVILEGES, currentUserAccessRights)) {
            return new AddRolePage();
        } else if (UpdateRolePage.TOKEN.equals(token) && isUserAuthorized(UpdateRolePage.PRIVILEGES, currentUserAccessRights)) {
            return new UpdateRolePage();

            // Manage Groups pages
        } else if (GroupListingAdminPage.TOKEN.equals(token) && isUserAuthorized(GroupListingAdminPage.PRIVILEGES, currentUserAccessRights)) {
            return new GroupListingAdminPage();
        } else if (GroupQuickDetailsAdminPage.TOKEN.equals(token) && isUserAuthorized(GroupQuickDetailsAdminPage.PRIVILEGES, currentUserAccessRights)) {
            return new GroupQuickDetailsAdminPage();
        } else if (AddGroupPage.TOKEN.equals(token) && isUserAuthorized(AddGroupPage.PRIVILEGES, currentUserAccessRights)) {
            return new AddGroupPage();
        } else if (UpdateGroupPage.TOKEN.equals(token) && isUserAuthorized(UpdateGroupPage.PRIVILEGES, currentUserAccessRights)) {
            return new UpdateGroupPage();

            // User Privileges settings pages
        } else if (ProfileListingPage.TOKEN.equals(token) && isUserAuthorized(ProfileListingPage.PRIVILEGES, currentUserAccessRights)) {
            return new ProfileListingPage();
        } else if (ProfileMoreDetailsPage.TOKEN.equals(token) && isUserAuthorized(ProfileMoreDetailsPage.PRIVILEGES, currentUserAccessRights)) {
            return new ProfileMoreDetailsPage();
        } else if (ProfileQuickDetailsPage.TOKEN.equals(token) && isUserAuthorized(ProfileQuickDetailsPage.PRIVILEGES, currentUserAccessRights)) {
            return new ProfileQuickDetailsPage();
        } else if (EditProfilePage.TOKEN.equals(token) && isUserAuthorized(EditProfilePage.PRIVILEGES, currentUserAccessRights)) {
            return new EditProfilePage();
        } else if (AddGroupToProfileMemberPage.TOKEN.equals(token) && isUserAuthorized(AddGroupToProfileMemberPage.PRIVILEGES, currentUserAccessRights)) {
            return new AddGroupToProfileMemberPage();
        } else if (AddRoleToProfileMemberPage.TOKEN.equals(token) && isUserAuthorized(AddRoleToProfileMemberPage.PRIVILEGES, currentUserAccessRights)) {
            return new AddRoleToProfileMemberPage();
        } else if (AddUserToProfileMemberPage.TOKEN.equals(token) && isUserAuthorized(AddUserToProfileMemberPage.PRIVILEGES, currentUserAccessRights)) {
            return new AddUserToProfileMemberPage();
        } else if (AddMembershipToProfileMemberPage.TOKEN.equals(token) && isUserAuthorized(AddMembershipToProfileMemberPage.PRIVILEGES, currentUserAccessRights)) {
            return new AddMembershipToProfileMemberPage();
        } else if (DeleteProfileMemberPage.TOKEN.equals(token) && isUserAuthorized(DeleteProfileMemberPage.PRIVILEGES, currentUserAccessRights)) {
            return new DeleteProfileMemberPage();


        } else if (DeleteActorMemberPage.TOKEN.equals(token)) {
            return new DeleteActorMemberPage();

            // Manage Tasks pages
        } else if (TaskListingAdminPage.TOKEN.equals(token) && isUserAuthorized(TaskListingAdminPage.PRIVILEGES, currentUserAccessRights)) {
            return new TaskListingAdminPage();
        } else if (SelectUserAndAssignTaskPage.TOKEN.equals(token) && isUserAuthorized(SelectUserAndAssignTaskPage.PRIVILEGES, currentUserAccessRights)) {
            return new SelectUserAndAssignTaskPage();
        } else if (TaskQuickDetailsAdminPage.TOKEN.equals(token) && isUserAuthorized(TaskQuickDetailsAdminPage.PRIVILEGES, currentUserAccessRights)) {
            return new TaskQuickDetailsAdminPage();
        } else if (TaskMoreDetailsAdminPage.TOKEN.equals(token) && isUserAuthorized(TaskMoreDetailsAdminPage.PRIVILEGES, currentUserAccessRights)) {
            return new TaskMoreDetailsAdminPage();
        
        /* THEME pages
        } else if (ListThemePage.TOKEN.equals(token)) {
            return new ListThemePage();
        } else if (UploadThemePage.TOKEN.equals(token)) {
            return new UploadThemePage();
        } else if (EditThemePage.TOKEN.equals(token)) {
            return new EditThemePage();
        */

            // Visualize & do tasks
        } else if (TasksListingPage.TOKEN.equals(token) && isUserAuthorized(TasksListingPage.PRIVILEGES, currentUserAccessRights) ) {
            return new TasksListingPage();
        } else if (HumanTaskQuickDetailsPage.TOKEN.equals(token) && isUserAuthorized(HumanTaskQuickDetailsPage.PRIVILEGES, currentUserAccessRights)) {
            return new HumanTaskQuickDetailsPage();
        } else if (HumanTaskMoreDetailsPage.TOKEN.equals(token) && isUserAuthorized(HumanTaskMoreDetailsPage.PRIVILEGES, currentUserAccessRights)) {
            return new HumanTaskMoreDetailsPage();
        } else if (ArchivedHumanTaskQuickDetailsPage.TOKEN.equals(token) && isUserAuthorized(ArchivedHumanTaskQuickDetailsPage.PRIVILEGES, currentUserAccessRights)) {
            return new ArchivedHumanTaskQuickDetailsPage();
        } else if (ArchivedHumanTaskMoreDetailsPage.TOKEN.equals(token) && isUserAuthorized(ArchivedHumanTaskMoreDetailsPage.PRIVILEGES, currentUserAccessRights)) {
            return new ArchivedHumanTaskMoreDetailsPage();
        } else if (PerformTaskPage.TOKEN.equals(token) && isUserAuthorized(PerformTaskPage.PRIVILEGES, currentUserAccessRights)) {
            return new PerformTaskPage();

            // Visualize & Start Apps
        } else if (ProcessListingPage.TOKEN.equals(token) && isUserAuthorized(ProcessListingPage.PRIVILEGES, currentUserAccessRights)) {
            return new ProcessListingPage();
        } else if (ProcessQuickDetailsPage.TOKEN.equals(token) && isUserAuthorized(ProcessQuickDetailsPage.PRIVILEGES, currentUserAccessRights)) {
            return new ProcessQuickDetailsPage();
        } else if (ProcessMoreDetailsPage.TOKEN.equals(token) && isUserAuthorized(ProcessMoreDetailsPage.PRIVILEGES, currentUserAccessRights)) {
            return new ProcessMoreDetailsPage();
        } else if (StartProcessFormPage.TOKEN.equals(token) && isUserAuthorized(StartProcessFormPage.PRIVILEGES, currentUserAccessRights)) {
            return new StartProcessFormPage();

            // Visualize Cases
        } else if (CaseListingPage.TOKEN.equals(token) && isUserAuthorized(CaseListingPage.PRIVILEGES, currentUserAccessRights)) {
            return new CaseListingPage();
        } else if (CaseQuickDetailsPage.TOKEN.equals(token) && isUserAuthorized(CaseQuickDetailsPage.PRIVILEGES, currentUserAccessRights)) {
            return new CaseQuickDetailsPage();
        } else if (ArchivedCaseQuickDetailsPage.TOKEN.equals(token) && isUserAuthorized(ArchivedCaseQuickDetailsPage.PRIVILEGES, currentUserAccessRights)) {
            return new ArchivedCaseQuickDetailsPage();
        } else if (ArchivedCaseMoreDetailsPage.TOKEN.equals(token) && isUserAuthorized(ArchivedCaseMoreDetailsPage.PRIVILEGES, currentUserAccessRights)) {
            return new ArchivedCaseMoreDetailsPage();
        } else if (CaseMoreDetailsPage.TOKEN.equals(token) && isUserAuthorized(CaseMoreDetailsPage.PRIVILEGES, currentUserAccessRights)) {
            return new CaseMoreDetailsPage();

        } else if (TechnicalUserWarningView.TOKEN.equals(token)) {
            return new TechnicalUserWarningView();
            // System
        } else if (PopupAboutPage.TOKEN.equals(token)) {
            return new PopupAboutPage();
        } else if (ChangeLangPage.TOKEN.equals(token)) {
            return new ChangeLangPage();

        } else {
            return new BlankPage();
        }
    }



    protected String listAUthorizedTokens(List<String> currentUserAccessRights) {
        String result = "";

        Map<String, List<String>> pagePrivileges = buildApplicationPagesPrivileges();

        for (Map.Entry<String, List<String>> entry : pagePrivileges.entrySet()) {
            result = isUserAuthorized(entry.getValue(), currentUserAccessRights)? result+ entry.getKey() +", " : result;
        }

        return result;

    }

    protected Map<String, List<String>> buildApplicationPagesPrivileges() {
        Map<String, List<String>> pagePrivileges = new HashMap<String, List<String>>();
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
        pagePrivileges.put(EditProfilePage.TOKEN, EditProfilePage.PRIVILEGES);
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
        pagePrivileges.put(ProcessMoreDetailsPage.TOKEN, ProcessMoreDetailsPage.PRIVILEGES);
        pagePrivileges.put(StartProcessFormPage.TOKEN, StartProcessFormPage.PRIVILEGES);
        pagePrivileges.put(CaseListingPage.TOKEN, CaseListingPage.PRIVILEGES);
        pagePrivileges.put(CaseQuickDetailsPage.TOKEN, CaseQuickDetailsPage.PRIVILEGES);
        pagePrivileges.put(ArchivedCaseQuickDetailsPage.TOKEN, ArchivedCaseQuickDetailsPage.PRIVILEGES);
        pagePrivileges.put(ArchivedCaseMoreDetailsPage.TOKEN, ArchivedCaseMoreDetailsPage.PRIVILEGES);
        pagePrivileges.put(CaseMoreDetailsPage.TOKEN, CaseMoreDetailsPage.PRIVILEGES);




        return pagePrivileges;
    }

    protected boolean isUserAuthorized(final List<String> privileges, List<String> accessRights) {

        String sessionId = new String(Session.getParameter("session_id"));

        for (String privilege: privileges) {

            String calcSHA1 = SHA1.calcSHA1(privilege.concat(sessionId));

            if (accessRights.contains(calcSHA1.toUpperCase())) {
                GWT.log("User is granted access to targeted page thanks to : "+ privilege );
                return true;
            }

        }

        return false;
    }

}
