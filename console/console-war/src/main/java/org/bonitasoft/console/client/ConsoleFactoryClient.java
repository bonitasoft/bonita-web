package org.bonitasoft.console.client;

import org.bonitasoft.console.client.admin.bpm.cases.view.ArchivedCaseMoreDetailsAdminPage;
import org.bonitasoft.console.client.admin.bpm.cases.view.ArchivedCaseQuickDetailsAdminPage;
import org.bonitasoft.console.client.admin.bpm.cases.view.CaseListingAdminPage;
import org.bonitasoft.console.client.admin.bpm.cases.view.CaseMoreDetailsAdminPage;
import org.bonitasoft.console.client.admin.bpm.cases.view.CaseQuickDetailsAdminPage;
import org.bonitasoft.console.client.admin.bpm.task.view.SelectUserAndAssignTaskPage;
import org.bonitasoft.console.client.admin.bpm.task.view.TaskListingAdminPage;
import org.bonitasoft.console.client.admin.bpm.task.view.TaskMoreDetailsAdminPage;
import org.bonitasoft.console.client.admin.bpm.task.view.TaskQuickDetailsAdminPage;
import org.bonitasoft.console.client.admin.organization.group.view.AddGroupPage;
import org.bonitasoft.console.client.admin.organization.group.view.GroupListingAdminPage;
import org.bonitasoft.console.client.admin.organization.group.view.GroupQuickDetailsAdminPage;
import org.bonitasoft.console.client.admin.organization.role.AddRolePage;
import org.bonitasoft.console.client.admin.organization.role.RoleListingPage;
import org.bonitasoft.console.client.admin.organization.role.RoleQuickDetailsPage;
import org.bonitasoft.console.client.admin.organization.role.UpdateRolePage;
import org.bonitasoft.console.client.admin.organization.users.view.AddMembershipPage;
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
import org.bonitasoft.console.client.admin.process.view.StartProcessOnBehalfPage;
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
import org.bonitasoft.console.client.admin.profile.view.ListProfilePage;
import org.bonitasoft.console.client.admin.profile.view.ProfileMoreDetailsPage;
import org.bonitasoft.console.client.admin.profile.view.ProfileQuickDetailsPage;
import org.bonitasoft.console.client.admin.theme.view.EditThemePage;
import org.bonitasoft.console.client.admin.theme.view.ListThemePage;
import org.bonitasoft.console.client.admin.theme.view.UploadThemePage;
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
import org.bonitasoft.web.toolkit.client.ui.Page;
import org.bonitasoft.web.toolkit.client.ui.component.form.view.BlankPage;
import org.bonitasoft.web.toolkit.client.ui.page.ChangeLangPage;
import org.bonitasoft.web.toolkit.client.ui.page.ItemNotFoundPopup;

/**
 * console client page
 * 
 * @author Yongtao Guo, Haojie Yuan, Zhiheng Yang
 */
public class ConsoleFactoryClient extends ApplicationFactoryClient {

    @Override
    public Page defineViewTokens(final String token) {

        if (ItemNotFoundPopup.TOKEN.equals(token)) {
            return new ItemNotFoundPopup();

            // Admin page
        } else if (CaseListingAdminPage.TOKEN.equals(token)) {
            return new CaseListingAdminPage();
        } else if (CaseQuickDetailsAdminPage.TOKEN.equals(token)) {
            return new CaseQuickDetailsAdminPage();
        } else if (CaseMoreDetailsAdminPage.TOKEN.equals(token)) {
            return new CaseMoreDetailsAdminPage();
        } else if (ArchivedCaseQuickDetailsAdminPage.TOKEN.equals(token)) {
            return new ArchivedCaseQuickDetailsAdminPage();
        } else if (ArchivedCaseMoreDetailsAdminPage.TOKEN.equals(token)) {
            return new ArchivedCaseMoreDetailsAdminPage();
            // } else if (ListGroupPage.TOKEN.equals(token)) {
            // return new ListGroupPage();
        } else if (DisplayCaseFormPage.TOKEN.equals(token)) {
            return new DisplayCaseFormPage();
        
        } else if (ListMembershipPage.TOKEN.equals(token)) {
            return new ListMembershipPage();
        } else if (AddMembershipToProfileMemberPage.TOKEN.equals(token)) {
            return new AddMembershipToProfileMemberPage();
        } else if (AddMembershipPage.TOKEN.equals(token)) {
            return new AddMembershipPage();
        } else if (DeleteMembershipPage.TOKEN.equals(token)) {
            return new DeleteMembershipPage();
        } else if (ProcessListingAdminPage.TOKEN.equals(token)) {
            return new ProcessListingAdminPage();
        } else if (ProcessQuickDetailsAdminPage.TOKEN.equals(token)) {
            return new ProcessQuickDetailsAdminPage();
        } else if (StartProcessOnBehalfPage.TOKEN.equals(token)) {
            return new StartProcessOnBehalfPage();
        } else if (UploadProcessPage.TOKEN.equals(token)) {
            return new UploadProcessPage();
        } else if (CreateCategoryAndAddToProcessPage.TOKEN.equals(token)) {
            return new CreateCategoryAndAddToProcessPage();
        } else if (AddProcessCategoryPage.TOKEN.equals(token)) {
            return new AddProcessCategoryPage();
        } else if (ProcessMoreDetailsAdminPage.TOKEN.equals(token)) {
            return new ProcessMoreDetailsAdminPage();
        } else if (UpdateUserPage.TOKEN.equals(token)) {
            return new UpdateUserPage();
        } else if (RoleListingPage.TOKEN.equals(token)) {
            return new RoleListingPage();
        } else if (RoleQuickDetailsPage.TOKEN.equals(token)) {
            return new RoleQuickDetailsPage();
        } else if (AddRolePage.TOKEN.equals(token)) {
            return new AddRolePage();
        } else if (UpdateRolePage.TOKEN.equals(token)) {
            return new UpdateRolePage();
        } else if (AddGroupPage.TOKEN.equals(token)) {
            return new AddGroupPage();
        } else if (ListProcessActorUserPage.TOKEN.equals(token)) {
            return new ListProcessActorUserPage();
        } else if (ListProcessActorGroupPage.TOKEN.equals(token)) {
            return new ListProcessActorGroupPage();
        } else if (ListProcessActorRolePage.TOKEN.equals(token)) {
            return new ListProcessActorRolePage();
        } else if (ListProcessActorMembershipPage.TOKEN.equals(token)) {
            return new ListProcessActorMembershipPage();
        } else if (ProfileMoreDetailsPage.TOKEN.equals(token)) {
            return new ProfileMoreDetailsPage();
        } else if (ProfileQuickDetailsPage.TOKEN.equals(token)) {
            return new ProfileQuickDetailsPage();
        } else if (ListProfilePage.TOKEN.equals(token)) {
            return new ListProfilePage();
        } else if (EditProfilePage.TOKEN.equals(token)) {
            return new EditProfilePage();
        } else if (AddGroupToProfileMemberPage.TOKEN.equals(token)) {
            return new AddGroupToProfileMemberPage();
        } else if (AddRoleToProfileMemberPage.TOKEN.equals(token)) {
            return new AddRoleToProfileMemberPage();
        } else if (AddUserToProfileMemberPage.TOKEN.equals(token)) {
            return new AddUserToProfileMemberPage();
        } else if (ListThemePage.TOKEN.equals(token)) {
            return new ListThemePage();
        } else if (UploadThemePage.TOKEN.equals(token)) {
            return new UploadThemePage();
        } else if (DeleteActorMemberPage.TOKEN.equals(token)) {
            return new DeleteActorMemberPage();
        } else if (DeleteProfileMemberPage.TOKEN.equals(token)) {
            return new DeleteProfileMemberPage();
        } else if (EditThemePage.TOKEN.equals(token)) {
            return new EditThemePage();
        } else if (UserListingAdminPage.TOKEN.equals(token)) {
            return new UserListingAdminPage();
        } else if (UserQuickDetailsAdminPage.TOKEN.equals(token)) {
            return new UserQuickDetailsAdminPage();
        } else if (UserQuickDetailsPage.TOKEN.equals(token)) {
            return new UserQuickDetailsPage();
        } else if (UserMoreDetailsAdminPage.TOKEN.equals(token)) {
            return new UserMoreDetailsAdminPage();
        } else if (PopupAddUserPage.TOKEN.equals(token)) {
            return new PopupAddUserPage();
        } else if (TaskListingAdminPage.TOKEN.equals(token)) {
            return new TaskListingAdminPage();
        } else if (SelectUserAndAssignTaskPage.TOKEN.equals(token)) {
            return new SelectUserAndAssignTaskPage();
        } else if (TaskQuickDetailsAdminPage.TOKEN.equals(token)) {
            return new TaskQuickDetailsAdminPage();
        } else if (TaskMoreDetailsAdminPage.TOKEN.equals(token)) {
            return new TaskMoreDetailsAdminPage();

        } else if (SelectMembershipForActorPage.TOKEN.equals(token)) {
            return new SelectMembershipForActorPage();
        } else if (SelectUserForActorPage.TOKEN.equals(token)) {
            return new SelectUserForActorPage();
        } else if (SelectGroupForActorPage.TOKEN.equals(token)) {
            return new SelectGroupForActorPage();
        } else if (SelectRoleForActorPage.TOKEN.equals(token)) {
            return new SelectRoleForActorPage();

        } else if (GroupListingAdminPage.TOKEN.equals(token)) {
            return new GroupListingAdminPage();
        } else if (GroupQuickDetailsAdminPage.TOKEN.equals(token)) {
            return new GroupQuickDetailsAdminPage();

        } else if (TasksListingPage.TOKEN.equals(token)) {
            return new TasksListingPage();
        } else if (CaseListingPage.TOKEN.equals(token)) {
            return new CaseListingPage();
        } else if (ProcessListingPage.TOKEN.equals(token)) {
            return new ProcessListingPage();
        } else if (HumanTaskQuickDetailsPage.TOKEN.equals(token)) {
            return new HumanTaskQuickDetailsPage();
        } else if (ArchivedHumanTaskQuickDetailsPage.TOKEN.equals(token)) {
            return new ArchivedHumanTaskQuickDetailsPage();
        } else if (ArchivedHumanTaskMoreDetailsPage.TOKEN.equals(token)) {
            return new ArchivedHumanTaskMoreDetailsPage();
        } else if (HumanTaskMoreDetailsPage.TOKEN.equals(token)) {
            return new HumanTaskMoreDetailsPage();
        } else if (ProcessQuickDetailsPage.TOKEN.equals(token)) {
            return new ProcessQuickDetailsPage();
        } else if (ProcessMoreDetailsPage.TOKEN.equals(token)) {
            return new ProcessMoreDetailsPage();
        } else if (CaseQuickDetailsPage.TOKEN.equals(token)) {
            return new CaseQuickDetailsPage();
        } else if (ArchivedCaseQuickDetailsPage.TOKEN.equals(token)) {
            return new ArchivedCaseQuickDetailsPage();
        } else if (ArchivedCaseMoreDetailsPage.TOKEN.equals(token)) {
            return new ArchivedCaseMoreDetailsPage();
        } else if (PerformTaskPage.TOKEN.equals(token)) {
            return new PerformTaskPage();
        } else if (CaseMoreDetailsPage.TOKEN.equals(token)) {
            return new CaseMoreDetailsPage();
        } else if (StartProcessFormPage.TOKEN.equals(token)) {
            return new StartProcessFormPage();
        }  else if (TechnicalUserWarningView.TOKEN.equals(token)) {
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
}
