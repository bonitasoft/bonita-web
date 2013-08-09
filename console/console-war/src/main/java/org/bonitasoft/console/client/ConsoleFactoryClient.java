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
import org.bonitasoft.web.toolkit.client.Session;
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
        
        List<String> currentUserAccessRights = new ArrayList<String>(Session.getArrayParameter("conf"));
         
        if (ItemNotFoundPopup.TOKEN.equals(token)) {
            return new ItemNotFoundPopup();

            // Admin page
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
            
        } else if (OrganizationImportAndExportPage.TOKEN.equals(token) && isUserAuthorized(OrganizationImportAndExportPage.PRIVILEGES, currentUserAccessRights)) {
            return new OrganizationImportAndExportPage();
            
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
        } else if (AddMembershipToProfileMemberPage.TOKEN.equals(token) && isUserAuthorized(AddMembershipToProfileMemberPage.PRIVILEGES, currentUserAccessRights)) {
            return new AddMembershipToProfileMemberPage();
        } else if (AddMembershipPage.TOKEN.equals(token) && isUserAuthorized(AddMembershipPage.PRIVILEGES, currentUserAccessRights)) {
            return new AddMembershipPage();
        } else if (DeleteMembershipPage.TOKEN.equals(token) && isUserAuthorized(DeleteMembershipPage.PRIVILEGES, currentUserAccessRights)) {
            return new DeleteMembershipPage();
        } else if (ListMembershipPage.TOKEN.equals(token) && isUserAuthorized(ListMembershipPage.PRIVILEGES, currentUserAccessRights)) {
            return new ListMembershipPage();
            
        } else if (ProcessListingAdminPage.TOKEN.equals(token) && isUserAuthorized(token, currentUserAccessRights)) {
            return new ProcessListingAdminPage();
        } else if (ProcessQuickDetailsAdminPage.TOKEN.equals(token) && isUserAuthorized(token, currentUserAccessRights)) {
            return new ProcessQuickDetailsAdminPage();
        
            
        } else if (StartProcessOnBehalfPage.TOKEN.equals(token)) {
            return new StartProcessOnBehalfPage();
            
        } else if (UploadProcessPage.TOKEN.equals(token) && isUserAuthorized(token, currentUserAccessRights)) {
            return new UploadProcessPage();
            
        } else if (CreateCategoryAndAddToProcessPage.TOKEN.equals(token) && isUserAuthorized(token, currentUserAccessRights)) {
            return new CreateCategoryAndAddToProcessPage();
        } else if (AddProcessCategoryPage.TOKEN.equals(token) && isUserAuthorized(token, currentUserAccessRights)) {
            return new AddProcessCategoryPage();
        } else if (ProcessMoreDetailsAdminPage.TOKEN.equals(token) && isUserAuthorized(token, currentUserAccessRights)) {
            return new ProcessMoreDetailsAdminPage();
        } else if (UpdateUserPage.TOKEN.equals(token) && isUserAuthorized(token, currentUserAccessRights)) {
            return new UpdateUserPage();
        } else if (RoleListingPage.TOKEN.equals(token) && isUserAuthorized(token, currentUserAccessRights) ) {
            return new RoleListingPage();
        } else if (RoleQuickDetailsPage.TOKEN.equals(token) && isUserAuthorized(token, currentUserAccessRights)) {
            return new RoleQuickDetailsPage();
        } else if (AddRolePage.TOKEN.equals(token) && isUserAuthorized(token, currentUserAccessRights)) {
            return new AddRolePage();
        } else if (UpdateRolePage.TOKEN.equals(token) && isUserAuthorized(token, currentUserAccessRights)) {
            return new UpdateRolePage();
        } else if (AddGroupPage.TOKEN.equals(token) && isUserAuthorized(token, currentUserAccessRights)) {
            return new AddGroupPage();
        } else if (UpdateGroupPage.TOKEN.equals(token) && isUserAuthorized(token, currentUserAccessRights)) {
            return new UpdateGroupPage();
        } else if (ListProcessActorUserPage.TOKEN.equals(token) && isUserAuthorized(token, currentUserAccessRights)) {
            return new ListProcessActorUserPage();
        } else if (ListProcessActorGroupPage.TOKEN.equals(token) && isUserAuthorized(token, currentUserAccessRights)) {
            return new ListProcessActorGroupPage();
        } else if (ListProcessActorRolePage.TOKEN.equals(token) && isUserAuthorized(token, currentUserAccessRights)) {
            return new ListProcessActorRolePage();
        } else if (ListProcessActorMembershipPage.TOKEN.equals(token) && isUserAuthorized(token, currentUserAccessRights)) {
            return new ListProcessActorMembershipPage();
        
        } else if (ProfileMoreDetailsPage.TOKEN.equals(token) && isUserAuthorized(token, currentUserAccessRights)) {
            return new ProfileMoreDetailsPage();
        } else if (ProfileQuickDetailsPage.TOKEN.equals(token) && isUserAuthorized(token, currentUserAccessRights)) {
            return new ProfileQuickDetailsPage();
        } else if (ListProfilePage.TOKEN.equals(token) && isUserAuthorized(token, currentUserAccessRights)) {
            return new ListProfilePage();
        
        } else if (EditProfilePage.TOKEN.equals(token) && isUserAuthorized(token, currentUserAccessRights)) {
            return new EditProfilePage();
        } else if (AddGroupToProfileMemberPage.TOKEN.equals(token) && isUserAuthorized(token, currentUserAccessRights)) {
            return new AddGroupToProfileMemberPage();
        } else if (AddRoleToProfileMemberPage.TOKEN.equals(token) && isUserAuthorized(token, currentUserAccessRights)) {
            return new AddRoleToProfileMemberPage();
        } else if (AddUserToProfileMemberPage.TOKEN.equals(token) && isUserAuthorized(token, currentUserAccessRights)) {
            return new AddUserToProfileMemberPage();
            
        } else if (DeleteActorMemberPage.TOKEN.equals(token)) {
            return new DeleteActorMemberPage();
        
        } else if (DeleteProfileMemberPage.TOKEN.equals(token) && isUserAuthorized(token, currentUserAccessRights)) {
            return new DeleteProfileMemberPage();
        
                   
        } else if (TaskListingAdminPage.TOKEN.equals(token) && isUserAuthorized(token, currentUserAccessRights)) {
            return new TaskListingAdminPage();
        } else if (SelectUserAndAssignTaskPage.TOKEN.equals(token) && isUserAuthorized(token, currentUserAccessRights)) {
            return new SelectUserAndAssignTaskPage();
        } else if (TaskQuickDetailsAdminPage.TOKEN.equals(token) && isUserAuthorized(token, currentUserAccessRights)) {
            return new TaskQuickDetailsAdminPage();
        } else if (TaskMoreDetailsAdminPage.TOKEN.equals(token) && isUserAuthorized(token, currentUserAccessRights)) {
            return new TaskMoreDetailsAdminPage();
        
       
        } else if (SelectMembershipForActorPage.TOKEN.equals(token) && isUserAuthorized(token, currentUserAccessRights)) {
            return new SelectMembershipForActorPage();
        } else if (SelectUserForActorPage.TOKEN.equals(token) && isUserAuthorized(token, currentUserAccessRights)) {
            return new SelectUserForActorPage();
        } else if (SelectGroupForActorPage.TOKEN.equals(token) && isUserAuthorized(token, currentUserAccessRights)) {
            return new SelectGroupForActorPage();
        } else if (SelectRoleForActorPage.TOKEN.equals(token) && isUserAuthorized(token, currentUserAccessRights)) {
            return new SelectRoleForActorPage();
        
        /* THEME pages
        } else if (ListThemePage.TOKEN.equals(token)) {
            return new ListThemePage();
        } else if (UploadThemePage.TOKEN.equals(token)) {
            return new UploadThemePage();
        } else if (EditThemePage.TOKEN.equals(token)) {
            return new EditThemePage();
        */
            
        } else if (GroupListingAdminPage.TOKEN.equals(token) && isUserAuthorized(token, currentUserAccessRights)) {
            return new GroupListingAdminPage();
        } else if (GroupQuickDetailsAdminPage.TOKEN.equals(token) && isUserAuthorized(token, currentUserAccessRights)) {
            return new GroupQuickDetailsAdminPage();

        } else if (TasksListingPage.TOKEN.equals(token) && isUserAuthorized(token, currentUserAccessRights) ) {
            return new TasksListingPage();
        } else if (CaseListingPage.TOKEN.equals(token) && isUserAuthorized(token, currentUserAccessRights)) {
            return new CaseListingPage();
        } else if (ProcessListingPage.TOKEN.equals(token) && isUserAuthorized(token, currentUserAccessRights)) {
            return new ProcessListingPage();
        } else if (HumanTaskQuickDetailsPage.TOKEN.equals(token) && isUserAuthorized(token, currentUserAccessRights)) {
            return new HumanTaskQuickDetailsPage();
        } else if (ArchivedHumanTaskQuickDetailsPage.TOKEN.equals(token) && isUserAuthorized(token, currentUserAccessRights)) {
            return new ArchivedHumanTaskQuickDetailsPage();
        } else if (ArchivedHumanTaskMoreDetailsPage.TOKEN.equals(token) && isUserAuthorized(token, currentUserAccessRights)) {
            return new ArchivedHumanTaskMoreDetailsPage();
        } else if (HumanTaskMoreDetailsPage.TOKEN.equals(token) && isUserAuthorized(token, currentUserAccessRights)) {
            return new HumanTaskMoreDetailsPage();
        } else if (ProcessQuickDetailsPage.TOKEN.equals(token) && isUserAuthorized(token, currentUserAccessRights)) {
            return new ProcessQuickDetailsPage();
        } else if (ProcessMoreDetailsPage.TOKEN.equals(token) && isUserAuthorized(token, currentUserAccessRights)) {
            return new ProcessMoreDetailsPage();
        } else if (CaseQuickDetailsPage.TOKEN.equals(token) && isUserAuthorized(token, currentUserAccessRights)) {
            return new CaseQuickDetailsPage();
        } else if (ArchivedCaseQuickDetailsPage.TOKEN.equals(token) && isUserAuthorized(token, currentUserAccessRights)) {
            return new ArchivedCaseQuickDetailsPage();
        } else if (ArchivedCaseMoreDetailsPage.TOKEN.equals(token) && isUserAuthorized(token, currentUserAccessRights)) {
            return new ArchivedCaseMoreDetailsPage();
        } else if (PerformTaskPage.TOKEN.equals(token) && isUserAuthorized(token, currentUserAccessRights)) {
            return new PerformTaskPage();
        } else if (CaseMoreDetailsPage.TOKEN.equals(token) && isUserAuthorized(token, currentUserAccessRights)) {
            return new CaseMoreDetailsPage();
        } else if (StartProcessFormPage.TOKEN.equals(token) && isUserAuthorized(token, currentUserAccessRights)) {
            return new StartProcessFormPage();
            
        }  else if (TechnicalUserWarningView.TOKEN.equals(token) ) {
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

    private boolean isUserAuthorized(final List<String> privileges, List<String> accessRights) {
       
        if (accessRights.contains(privileges)) {
            return true;
        }
        
        
       
        return accessRights.contains(privileges);
    }
}
