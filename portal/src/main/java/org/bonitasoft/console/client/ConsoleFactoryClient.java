package org.bonitasoft.console.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.shared.GWT;
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
import org.bonitasoft.console.client.admin.organization.users.view.*;
import org.bonitasoft.console.client.admin.page.view.*;
import org.bonitasoft.console.client.admin.process.view.ProcessListingAdminPage;
import org.bonitasoft.console.client.admin.process.view.ProcessMoreDetailsAdminPage;
import org.bonitasoft.console.client.admin.process.view.ProcessQuickDetailsAdminPage;
import org.bonitasoft.console.client.admin.process.view.UploadProcessPage;
import org.bonitasoft.console.client.admin.process.view.section.category.AddProcessCategoryPage;
import org.bonitasoft.console.client.admin.process.view.section.category.CreateCategoryAndAddToProcessPage;
import org.bonitasoft.console.client.admin.process.view.section.entitymapping.*;
import org.bonitasoft.console.client.admin.profile.view.*;
import org.bonitasoft.console.client.admin.tenant.view.TenantMaintenancePage;
import org.bonitasoft.console.client.angular.AngularIFrameView;
import org.bonitasoft.console.client.common.system.view.PopupAboutPage;
import org.bonitasoft.console.client.common.view.CustomPageWithFrame;
import org.bonitasoft.console.client.menu.view.TechnicalUserServicePausedView;
import org.bonitasoft.console.client.menu.view.TechnicalUserWarningView;
import org.bonitasoft.console.client.user.cases.view.*;
import org.bonitasoft.console.client.user.process.view.ProcessQuickDetailsPage;
import org.bonitasoft.console.client.user.process.view.StartProcessFormPage;
import org.bonitasoft.console.client.user.task.view.ArchivedHumanTaskQuickDetailsPage;
import org.bonitasoft.console.client.user.task.view.HumanTaskQuickDetailsPage;
import org.bonitasoft.console.client.user.task.view.PerformTaskPage;
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

/**
 * console client page
 *
 * @author Yongtao Guo, Haojie Yuan, Zhiheng Yang
 */
public class ConsoleFactoryClient extends ApplicationFactoryClient {

    //All custom pages with this token suffix are authorized (should be sync with CustomPageAuthorizationsHelper)
    public static final String BONITA_LABS_PAGE_TOKEN_EXTENSION = "BonitaLabs";

    protected AngularIFrameView angularFrame = new AngularIFrameView();

    private List<String> currentUserAccessRights = null;

    /**
     * Default Constructor.
     */
    public ConsoleFactoryClient() {
        AngularIFrameView.addTokenSupport(AngularIFrameView.BDM_TOKEN, "/admin/bdm");
        AngularIFrameView.addTokenSupport(AngularIFrameView.CASE_LISTING_TOKEN, "/user/cases/list");
        AngularIFrameView.addTokenSupport(AngularIFrameView.CASE_LISTING_ADMIN_TOKEN, "/admin/cases/list");
        AngularIFrameView.addTokenSupport(AngularIFrameView.APPLICATION_LISTING_PAGE, "/admin/applications");
        AngularIFrameView.addTokenSupport(AngularIFrameView.PROCESS_MORE_DETAILS_ADMIN_TOKEN, "/admin/processes/details");
        AngularIFrameView.addTokenSupport(AngularIFrameView.TASK_LISTING_TOKEN, "/user/tasks/list");
        AngularIFrameView.addTokenSupport(AngularIFrameView.TASK_LISTING_TOKEN, "/user/tasks/list");
        AngularIFrameView.addTokenSupport(AngularIFrameView.USER_MORE_DETAILS_ADMIN, "/admin/organisation/users",
                UserListingAdminPage.TOKEN, GroupListingAdminPage.TOKEN, RoleListingPage.TOKEN);

        CustomPageWithFrame.addTokenSupport(CustomPageWithFrame.PROCESS_LIST_USER,
                CaseMoreDetailsPage.TOKEN, DisplayCaseFormPage.TOKEN, ProcessQuickDetailsPage.TOKEN,
                StartProcessFormPage.TOKEN, ArchivedHumanTaskQuickDetailsPage.TOKEN, HumanTaskQuickDetailsPage.TOKEN,
                PerformTaskPage.TOKEN,TasksListingPage.TOKEN, ArchivedHumanTaskMoreDetailsPage.TOKEN,
                HumanTaskMoreDetailsPage.TOKEN);

        CustomPageWithFrame.addTokenSupport(CustomPageWithFrame.IMPORT_EXPORT_ORGANIZATION);
        
        CustomPageWithFrame.addTokenSupport(CustomPageWithFrame.TENANT_STATUS);
    }

    protected List<String> getCurrentUserAccessRights() {
        if (currentUserAccessRights == null) {
            currentUserAccessRights = new ArrayList<String>(AvailableTokens.tokens);
        }
        return currentUserAccessRights;
    }

    private static final Action emptyAction = new Action() {

        @Override
        public void execute() {
        }
    };

    /**
     * @param token
     * @return
     */
    public RawView prepareAngularPage(final String token) {
        new CheckValidSessionBeforeAction(emptyAction).execute();
        final AngularIFrameView ngView = angularFrame;
        ngView.setUrl("#" + AngularIFrameView.getRoute(token), token);
        return ngView;
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

            // Manage Users pages
        } else if (UserListingAdminPage.TOKEN.equals(token) && isUserAuthorized(UserListingAdminPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new UserListingAdminPage();
        } else if (UserQuickDetailsAdminPage.TOKEN.equals(token) && isUserAuthorized(UserQuickDetailsAdminPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new UserQuickDetailsAdminPage();
        } else if (UserQuickDetailsPage.TOKEN.equals(token) && isUserAuthorized(UserQuickDetailsPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new UserQuickDetailsPage();
        } else if (PopupAddUserPage.TOKEN.equals(token) && isUserAuthorized(PopupAddUserPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new PopupAddUserPage();

            // Manage processes pages For Admin
        } else if (ProcessListingAdminPage.TOKEN.equals(token) && isUserAuthorized(ProcessListingAdminPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new ProcessListingAdminPage();
        } else if (ProcessQuickDetailsAdminPage.TOKEN.equals(token) && isUserAuthorized(ProcessQuickDetailsAdminPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new ProcessQuickDetailsAdminPage();
        } else if (ProcessMoreDetailsAdminPage.TOKEN.equals(token) && isUserAuthorized(ProcessMoreDetailsAdminPage.PRIVILEGES,
                getCurrentUserAccessRights())) {
            // No action is necessary as an unauthorized request will result in a page reload.
            return new ProcessMoreDetailsAdminPage();
        } else if (AngularIFrameView.PROCESS_MORE_DETAILS_ADMIN_TOKEN.equals(token) && isUserAuthorized(ProcessMoreDetailsAdminPage.PRIVILEGES,
                getCurrentUserAccessRights())) {
            return prepareAngularPage(token);
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
        } else if (AngularIFrameView.TASK_LISTING_TOKEN.equals(token) && isUserAuthorized(TasksListingPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return prepareAngularPage(token);
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
        } else if (ProcessQuickDetailsPage.TOKEN.equals(token) && isUserAuthorized(ProcessQuickDetailsPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new ProcessQuickDetailsPage();
        } else if (StartProcessFormPage.TOKEN.equals(token) && isUserAuthorized(StartProcessFormPage.PRIVILEGES, getCurrentUserAccessRights())) {
            return new StartProcessFormPage();

            // Visualize Cases
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
            if (isCustomPageAuthorizedToken(token)) {
                return new CustomPageWithFrame(token);
            } else {
                return new BlankPage();
            }
        } else if (AngularIFrameView.supportsToken(token) && isPortalJSAuthorizedToken(token)) {
            // No action is necessary as an unauthorized request will result in a page reload.
            return prepareAngularPage(token);
        } else {
            return new BlankPage();
        }
    }

    private boolean isPortalJSAuthorizedToken(String token) {
        // whenever a token ends with BonitaLabs, it is authorized by default.
        // this is done to ensure feature flipping and embed in development portal-js pages
        List<String> privileges = AngularIFrameView.getPrivileges(token);
        privileges.addAll(CustomPageWithFrame.getPrivileges(token));
        return isUserAuthorized(privileges, getCurrentUserAccessRights()) || token.endsWith(BONITA_LABS_PAGE_TOKEN_EXTENSION);
    }

    private boolean isCustomPageAuthorizedToken(String token) {
        // whenever a token ends with -labs, it is authorized by default.
        return isUserAuthorized(token, getCurrentUserAccessRights()) || token.endsWith(BONITA_LABS_PAGE_TOKEN_EXTENSION);
    }

    public native void print(String content) /*-{
        console.log(content);
    }-*/;

    protected boolean isUserAuthorized(final String token, final List<String> accessRights) {

        final String sessionId = new String(Session.getParameter("session_id"));

        final String calcSHA1 = SHA1.calcSHA1(token.concat(sessionId));

        if (accessRights.contains(calcSHA1.toUpperCase())) {
            return true;
        }

        return false;
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
