package org.bonitasoft.forms.server.provider.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bonitasoft.console.common.server.utils.BPMEngineAPIUtil;
import org.bonitasoft.console.common.server.utils.BPMEngineException;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.flownode.ActivityInstanceNotFoundException;
import org.bonitasoft.engine.bpm.process.ArchivedProcessInstanceNotFoundException;
import org.bonitasoft.engine.bpm.process.ProcessDefinitionNotFoundException;
import org.bonitasoft.engine.bpm.process.ProcessInstanceNotFoundException;
import org.bonitasoft.engine.identity.UserNotFoundException;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.session.InvalidSessionException;
import org.bonitasoft.forms.client.model.exception.AbortedFormException;
import org.bonitasoft.forms.client.model.exception.CanceledFormException;
import org.bonitasoft.forms.client.model.exception.ForbiddenFormAccessException;
import org.bonitasoft.forms.client.model.exception.FormAlreadySubmittedException;
import org.bonitasoft.forms.client.model.exception.FormInErrorException;
import org.bonitasoft.forms.client.model.exception.SessionTimeoutException;
import org.bonitasoft.forms.client.model.exception.SkippedFormException;
import org.bonitasoft.forms.client.model.exception.SuspendedFormException;
import org.bonitasoft.forms.server.api.IFormWorkflowAPI;
import org.bonitasoft.forms.server.api.impl.FormWorkflowAPIImpl;
import org.bonitasoft.forms.server.exception.FormNotFoundException;
import org.bonitasoft.forms.server.provider.impl.util.FormServiceProviderUtil;
import org.bonitasoft.web.rest.model.user.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FormServiceProviderImplTest {

    @Spy
    FormServiceProviderImpl formServiceProviderImpl;

    @Mock
    IFormWorkflowAPI workflowAPI;

    @Mock
    APISession apiSession;

    @Mock
    BPMEngineAPIUtil bpmEngineAPIUtil;

    @Mock
    ProcessAPI processAPI;

    @Mock
    FormWorkflowAPIImpl formWorkflowAPIImpl;

    @Mock
    private User user;

    Map<String, Object> context;

    Map<String, Object> urlContext;

    private List<Class<? extends Exception>> givenExceptions;

    private List<Class<? extends Exception>> expectedExceptions;

    @Before
    public void before() throws Exception {
        givenExceptions = new ArrayList<Class<? extends Exception>>();
        expectedExceptions = new ArrayList<Class<? extends Exception>>();

        context = new HashMap<>();
        urlContext = new HashMap<>();
        context.put(FormServiceProviderUtil.URL_CONTEXT, urlContext);
        doReturn(processAPI).when(bpmEngineAPIUtil).getProcessAPI(any(APISession.class));
        doReturn(workflowAPI).when(formServiceProviderImpl).getFormWorkFlowApi();
    }

    @Test
    public void getProcessDefinitionId_with_process_UUID() throws Exception {
        final long expectedProcessDefinitionId = 123l;
        // given
        urlContext.put(FormServiceProviderUtil.PROCESS_UUID, expectedProcessDefinitionId);

        // when
        final long processDefinitionID = formServiceProviderImpl.getProcessDefinitionID(context);

        // then exception
        verify(workflowAPI, never()).getProcessDefinitionIDFromActivityInstanceID(any(APISession.class), anyLong());
        verify(workflowAPI, never()).getProcessDefinitionIDFromUUID(any(APISession.class), any(String.class));
        assertThat(processDefinitionID).isEqualTo(expectedProcessDefinitionId);
    }

    @Test
    public void getProcessDefinitionId_with_form_ID() throws Exception {
        // given
        urlContext.put(FormServiceProviderUtil.PROCESS_UUID, null);
        urlContext.put(FormServiceProviderUtil.FORM_ID, "processName--1.0$entry");

        doThrow(ActivityInstanceNotFoundException.class).when(workflowAPI).getProcessDefinitionIDFromActivityInstanceID(any(APISession.class), anyLong());
        final long processDefinitionId = 123l;
        doReturn(processDefinitionId).when(workflowAPI).getProcessDefinitionIDFromUUID(any(APISession.class), any(String.class));
        // when
        final long processDefinitionID = formServiceProviderImpl.getProcessDefinitionID(context);

        // then exception
        verify(workflowAPI, never()).getProcessDefinitionIDFromActivityInstanceID(any(APISession.class), anyLong());
        verify(workflowAPI, times(1)).getProcessDefinitionIDFromUUID(any(APISession.class), any(String.class));
        assertThat(processDefinitionID).isEqualTo(processDefinitionId);
    }

    @Test
    public void testextractProcessDefinitionUUID() throws Exception {

        //given
        final String formId = "processName--1.0$entry";

        //when
        final String extractProcessDefinitionUUID = formServiceProviderImpl.extractProcessDefinitionUUID(formId);

        //then
        assertThat(extractProcessDefinitionUUID).isEqualTo("processName--1.0");

    }

    @Test
    public void testextractProcessDefinitionUUID_with_step_name() throws Exception {
        //given
        final String formId = "processName--1.0--Step1$entry";

        //when
        final String extractProcessDefinitionUUID = formServiceProviderImpl.extractProcessDefinitionUUID(formId);

        //then
        assertThat(extractProcessDefinitionUUID).isEqualTo("processName--1.0");

    }

    @Test(expected = ForbiddenFormAccessException.class)
    public void should_canUserViewInstanceForm_throw_exception_if_not_involved() throws Exception {
        final String formId = "formId";
        final long userId = 2L;
        final long processInstanceId = 3L;

        //given
        doReturn(1L).when(formWorkflowAPIImpl).getProcessDefinitionIDFromProcessInstanceID(apiSession, processInstanceId);
        doReturn(false).when(formWorkflowAPIImpl).isUserAdminOrProcessOwner(apiSession, 1L);
        doReturn(false).when(formWorkflowAPIImpl).canUserSeeProcessInstance(apiSession, processInstanceId);
        doReturn("john").when(user).getUsername();
        doReturn("form-type").when(formServiceProviderImpl).getFormType(formId, context);

        //when
        formServiceProviderImpl.canUserViewInstanceForm(apiSession, user, formWorkflowAPIImpl, processInstanceId, formId, userId, context);

        //exception
    }

    @Test
    public void should_canUserViewInstanceForm_return_true_for_involved_user() throws Exception {
        final String formId = "formId";
        final long userId = 2L;
        final long processInstanceId = 3L;

        //given
        doReturn(1L).when(formWorkflowAPIImpl).getProcessDefinitionIDFromProcessInstanceID(apiSession, processInstanceId);
        doReturn(false).when(formWorkflowAPIImpl).isUserAdminOrProcessOwner(apiSession, 1L);
        doReturn(true).when(formWorkflowAPIImpl).canUserSeeProcessInstance(apiSession, processInstanceId);

        //when
        formServiceProviderImpl.canUserViewInstanceForm(apiSession, user, formWorkflowAPIImpl, processInstanceId, formId, userId, context);

    }

    @Test
    public void should_canUserViewInstanceForm_return_true_for_admin() throws Exception {
        final String formId = "formId";
        final long userId = 2L;
        final long processInstanceId = 3L;

        //given
        doReturn(1L).when(formWorkflowAPIImpl).getProcessDefinitionIDFromProcessInstanceID(apiSession, processInstanceId);
        doReturn(true).when(formWorkflowAPIImpl).isUserAdminOrProcessOwner(apiSession, 1L);

        //when
        formServiceProviderImpl.canUserViewInstanceForm(apiSession, user, formWorkflowAPIImpl, processInstanceId, formId, userId, context);

    }

    @Test
    public void should_canUserViewInstanceForm_throw_exception() throws Exception {
        final String formId = "formId";
        final long userId = 2L;
        final long processInstanceId = 3L;

        addGivenAndExpectedException(ForbiddenFormAccessException.class, ForbiddenFormAccessException.class);
        addGivenAndExpectedException(InvalidSessionException.class, InvalidSessionException.class);
        addGivenAndExpectedException(BPMEngineException.class, BPMEngineException.class);
        addGivenAndExpectedException(ProcessInstanceNotFoundException.class, FormNotFoundException.class);
        addGivenAndExpectedException(ArchivedProcessInstanceNotFoundException.class, FormNotFoundException.class);
        addGivenAndExpectedException(ProcessDefinitionNotFoundException.class, FormNotFoundException.class);
        addGivenAndExpectedException(UserNotFoundException.class, SessionTimeoutException.class);

        givenExceptions.add(UserNotFoundException.class);
        expectedExceptions.add(SessionTimeoutException.class);

        doReturn("john").when(user).getUsername();
        doReturn("form-type").when(formServiceProviderImpl).getFormType(formId, context);

        for (int i = 0; i < givenExceptions.size(); i++) {
            //given
            doReturn(1L).when(formWorkflowAPIImpl).getProcessDefinitionIDFromProcessInstanceID(apiSession, processInstanceId);
            doThrow(givenExceptions.get(i)).when(formWorkflowAPIImpl).isUserAdminOrProcessOwner(apiSession, 1L);

            //when
            try {
                formServiceProviderImpl.canUserViewInstanceForm(apiSession, user, formWorkflowAPIImpl, processInstanceId, formId, userId, context);
            } catch (final Exception e) {
                assertThat(e.getClass()).as("bad exception with given exception " + givenExceptions.get(i).getClass()).isEqualTo(
                        expectedExceptions.get(i));
                if (e.getClass() != expectedExceptions.get(i)) {
                    fail("bad exception");
                }
            }
        }

    }

    private void addGivenAndExpectedException(final Class<? extends Exception> givenException, final Class<? extends Exception> expectedException) {
        givenExceptions.add(givenException);
        expectedExceptions.add(expectedException);
    }

    @Test(expected = ForbiddenFormAccessException.class)
    public void should_canUserViewActivityInstanceForm_throw_exception_if_not_involved() throws Exception {
        final String formId = "formId";
        final long userId = 2L;
        final long activityInstanceID = 3L;

        //given
        doReturn(1L).when(formWorkflowAPIImpl).getProcessDefinitionIDFromActivityInstanceID(apiSession, activityInstanceID);
        doReturn(false).when(formWorkflowAPIImpl).isUserAdminOrProcessOwner(apiSession, 1L);
        doReturn(false).when(formWorkflowAPIImpl).canUserSeeHumanTask(apiSession, userId, activityInstanceID);
        doReturn("john").when(user).getUsername();
        doReturn("form-type").when(formServiceProviderImpl).getFormType(formId, context);

        //when
        formServiceProviderImpl.canUserViewActivityInstanceForm(apiSession, user, formWorkflowAPIImpl, activityInstanceID, formId, userId, context);

        //exception
    }

    @Test
    public void should_canUserViewActivityInstanceForm_return_true_for_involved_user() throws Exception {
        final String formId = "formId";
        final long userId = 2L;
        final long activityInstanceID = 3L;

        //given
        doReturn(1L).when(formWorkflowAPIImpl).getProcessDefinitionIDFromActivityInstanceID(apiSession, activityInstanceID);
        doReturn(false).when(formWorkflowAPIImpl).isUserAdminOrProcessOwner(apiSession, 1L);
        doReturn(true).when(formWorkflowAPIImpl).canUserSeeHumanTask(apiSession, userId, activityInstanceID);

        //when
        formServiceProviderImpl.canUserViewActivityInstanceForm(apiSession, user, formWorkflowAPIImpl, activityInstanceID, formId, userId, context);

    }

    @Test
    public void should_canUserViewActivityInstanceForm_return_true_for_admin() throws Exception {
        final String formId = "formId";
        final long userId = 2L;
        final long activityInstanceID = 3L;

        //given
        doReturn(1L).when(formWorkflowAPIImpl).getProcessDefinitionIDFromActivityInstanceID(apiSession, activityInstanceID);
        doReturn(true).when(formWorkflowAPIImpl).isUserAdminOrProcessOwner(apiSession, 1L);

        //when
        formServiceProviderImpl.canUserViewActivityInstanceForm(apiSession, user, formWorkflowAPIImpl, activityInstanceID, formId, userId, context);

    }

    @Test
    public void should_allow_task_form_access() throws Exception {
        //given
        final String formId = "taskDefinitionUUID$entry";
        
        //when
        final boolean allowed = testAllowTaskFormAccess(formId, "taskDefinitionUUID", "taskDefinitionUUID");

        //then
        assertThat(allowed).as("should allow user").isTrue();
    }
    
    @Test(expected = ForbiddenFormAccessException.class)
    public void should_not_allow_task_form_access_with_wrong_instance_id() throws Exception {
        //given
        final String formId = "taskDefinitionUUID$entry";
        
        //when
        testAllowTaskFormAccess(formId, "taskDefinitionUUID", "anotherTaskDefinitionUUID");
    }

    @Test
    public void should_allow_task_form_access_with_custom_form_id_allowed() throws Exception {
        //given
        final String formId = "formId";
        doReturn(true).when(formServiceProviderImpl).isCustomFormIdAuthorized(apiSession);
        
        //when
        final boolean allowed = testAllowTaskFormAccess(formId, "taskDefinitionUUID", "taskDefinitionUUID");

        //then
        assertThat(allowed).as("should allow user").isTrue();
    }
    
    @Test(expected = ForbiddenFormAccessException.class)
    public void should_not_allow_task_form_access_with_invalid_form_id() throws Exception {
        //given
        final String formId = "taskDefinitionUUID$recap";
        
        //when
        testAllowTaskFormAccess(formId, "taskDefinitionUUID", "taskDefinitionUUID");
    }

    private boolean testAllowTaskFormAccess(final String formId, final String taskDefinitionUUIDStr, final String taskDefinitionUUIDFromActivityInstanceID) throws Exception {
        final String permissions = FormServiceProviderUtil.ACTIVITY_UUID + "#" + taskDefinitionUUIDStr;
        final String productVersion = "";
        final String migrationProductVersion = "";
        final boolean isFormPermissions = true;
        final long taskInstanceId = 1L;
        final long userId = 2L;

        urlContext.put(FormServiceProviderUtil.TASK_UUID, taskInstanceId);
        urlContext.put(FormServiceProviderUtil.USER_ID, userId);
        urlContext.put(FormServiceProviderUtil.ASSIGN_TASK, "true");
        context.put(FormServiceProviderUtil.API_SESSION, apiSession);
        context.put(FormServiceProviderUtil.USER, user);

        doReturn(taskDefinitionUUIDFromActivityInstanceID).when(workflowAPI).getActivityDefinitionUUIDFromActivityInstanceID(apiSession, taskInstanceId);
        doNothing().when(formServiceProviderImpl).canUserViewActivityInstanceForm(apiSession, user, workflowAPI, taskInstanceId, formId,
                userId, context);

        boolean isAllowed = formServiceProviderImpl.isAllowed(formId, permissions, productVersion, migrationProductVersion, context, isFormPermissions);
        if (isAllowed) {
            verify(workflowAPI).assignTaskIfNotAssigned(apiSession, taskInstanceId, userId);
        }
        
        return isAllowed;
    }
    
    @Test
    public void should_not_allow_task_form_access() throws Exception {
        //given
        final String formId = "taskDefinitionUUID$entry";
        final String taskDefinitionUUIDStr = "taskDefinitionUUID";
        final String permissions = FormServiceProviderUtil.TASK_UUID + "#" + taskDefinitionUUIDStr;
        final String productVersion = "";
        final String migrationProductVersion = "";
        final boolean isFormPermissions = true;
        final long taskInstanceId = 1L;
        final long userId = 2L;

        urlContext.put(FormServiceProviderUtil.TASK_UUID, taskInstanceId);
        urlContext.put(FormServiceProviderUtil.USER_ID, userId);
        context.put(FormServiceProviderUtil.API_SESSION, apiSession);
        context.put(FormServiceProviderUtil.USER, user);

        doReturn("taskDefinitionUUID").when(workflowAPI).getActivityDefinitionUUIDFromActivityInstanceID(apiSession, taskInstanceId);
        addGivenAndExpectedException(InvalidSessionException.class, SessionTimeoutException.class);
        addGivenAndExpectedException(BPMEngineException.class, FormNotFoundException.class);
        addGivenAndExpectedException(FormNotFoundException.class, FormNotFoundException.class);
        addGivenAndExpectedException(ForbiddenFormAccessException.class, ForbiddenFormAccessException.class);
        addGivenAndExpectedException(SuspendedFormException.class, SuspendedFormException.class);
        addGivenAndExpectedException(CanceledFormException.class, CanceledFormException.class);
        addGivenAndExpectedException(SuspendedFormException.class, SuspendedFormException.class);
        addGivenAndExpectedException(FormInErrorException.class, FormInErrorException.class);
        addGivenAndExpectedException(SkippedFormException.class, SkippedFormException.class);
        addGivenAndExpectedException(FormAlreadySubmittedException.class, FormAlreadySubmittedException.class);
        addGivenAndExpectedException(AbortedFormException.class, AbortedFormException.class);

        for (int i = 0; i < givenExceptions.size(); i++) {
            //given
            doThrow(givenExceptions.get(i)).when(formServiceProviderImpl).canUserViewActivityInstanceForm(apiSession, user, workflowAPI, taskInstanceId,
                    formId, userId, context);

            //when
            try {
                formServiceProviderImpl.isAllowed(formId, permissions, productVersion, migrationProductVersion, context, isFormPermissions);

            } catch (final Exception e) {
                assertThat(e.getClass()).as("bad exception with given exception " + givenExceptions.get(i).getClass()).isEqualTo(
                        expectedExceptions.get(i));
                if (e.getClass() != expectedExceptions.get(i)) {
                    fail("bad exception");
                }
            }
        }
    }
    
    @Test
    public void should_allow_instance_overview_access() throws Exception {
        //given
        final String formId = "processDefinitionUUID$recap";
        
        //when
        final boolean allowed = testAllowInstanceOverviewFormAccess(formId, 3L, 3L);

        //then
        assertThat(allowed).as("should allow user").isTrue();
    }
    
    @Test(expected = ForbiddenFormAccessException.class)
    public void should_not_allow_instance_overview_access_with_wrong_instance_id() throws Exception {
        //given
        final String formId = "processDefinitionUUID$recap";
        
        //when
        testAllowInstanceOverviewFormAccess(formId, 3L, 4L);
    }

    @Test
    public void should_allow_instance_overview_access_with_custom_form_id_allowed() throws Exception {
        //given
        final String formId = "formId";
        doReturn(true).when(formServiceProviderImpl).isCustomFormIdAuthorized(apiSession);
        
        //when
        final boolean allowed = testAllowInstanceOverviewFormAccess(formId, 3L, 3L);

        //then
        assertThat(allowed).as("should allow user").isTrue();
    }
    
    @Test(expected = ForbiddenFormAccessException.class)
    public void should_not_allow_instance_overview_access_with_invalid_form_id() throws Exception {
        //given
        final String formId = "processDefinitionUUID$entry";
        
        //when
        testAllowInstanceOverviewFormAccess(formId, 3L, 3L);
    }

    private boolean testAllowInstanceOverviewFormAccess(final String formId, final long processDefinitionIDFromUUID, final long processDefinitionIDFromProcessInstanceID) throws Exception {
        final String processDefinitionUUIDStr = "processDefinitionUUID";
        final String permissions = FormServiceProviderUtil.PROCESS_UUID + "#" + processDefinitionUUIDStr;
        final String productVersion = "";
        final String migrationProductVersion = "";
        final boolean isFormPermissions = true;
        final long processInstanceId = 1L;
        final long userId = 2L;

        urlContext.put(FormServiceProviderUtil.INSTANCE_UUID, processInstanceId);
        urlContext.put(FormServiceProviderUtil.USER_ID, userId);
        context.put(FormServiceProviderUtil.API_SESSION, apiSession);
        context.put(FormServiceProviderUtil.USER, user);

        doReturn(processDefinitionIDFromUUID).when(workflowAPI).getProcessDefinitionIDFromUUID(apiSession, processDefinitionUUIDStr);
        doReturn(processDefinitionIDFromProcessInstanceID).when(workflowAPI).getProcessDefinitionIDFromProcessInstanceID(apiSession, processInstanceId);
        doNothing().when(formServiceProviderImpl).canUserViewInstanceForm(apiSession, user, workflowAPI, processInstanceId, formId,
                userId, context);

        return formServiceProviderImpl.isAllowed(formId, permissions, productVersion, migrationProductVersion, context, isFormPermissions);
    }
    
    @Test
    public void should_not_allow_instance_overview_access() throws Exception {
        //given
        final String formId = "processDefinitionUUID$recap";
        final String processDefinitionUUIDStr = "processDefinitionUUID";
        final String permissions = FormServiceProviderUtil.PROCESS_UUID + "#" + processDefinitionUUIDStr;
        final String productVersion = "";
        final String migrationProductVersion = "";
        final boolean isFormPermissions = true;
        final long processInstanceId = 1L;
        final long userId = 2L;
        final long processDefinitionId = 3L;

        urlContext.put(FormServiceProviderUtil.INSTANCE_UUID, processInstanceId);
        urlContext.put(FormServiceProviderUtil.USER_ID, userId);
        context.put(FormServiceProviderUtil.API_SESSION, apiSession);
        context.put(FormServiceProviderUtil.USER, user);

        doReturn(processDefinitionId).when(workflowAPI).getProcessDefinitionIDFromUUID(apiSession, processDefinitionUUIDStr);
        doReturn(processDefinitionId).when(workflowAPI).getProcessDefinitionIDFromProcessInstanceID(apiSession, processInstanceId);

        addGivenAndExpectedException(InvalidSessionException.class, SessionTimeoutException.class);
        addGivenAndExpectedException(BPMEngineException.class, FormNotFoundException.class);
        addGivenAndExpectedException(FormNotFoundException.class, FormNotFoundException.class);
        addGivenAndExpectedException(ForbiddenFormAccessException.class, ForbiddenFormAccessException.class);
        addGivenAndExpectedException(SessionTimeoutException.class, SessionTimeoutException.class);

        for (int i = 0; i < givenExceptions.size(); i++) {
            //given
            doThrow(givenExceptions.get(i)).when(formServiceProviderImpl).canUserViewInstanceForm(apiSession, user, workflowAPI, processInstanceId,
                    formId, userId, context);

            //when
            try {
                formServiceProviderImpl.isAllowed(formId, permissions, productVersion, migrationProductVersion, context, isFormPermissions);

            } catch (final Exception e) {
                assertThat(e.getClass()).as("bad exception with given exception " + givenExceptions.get(i).getClass()).isEqualTo(
                        expectedExceptions.get(i));
                if (e.getClass() != expectedExceptions.get(i)) {
                    fail("bad exception");
                }
            }
        }
    }
    
    @Test
    public void should_allow_process_form_access() throws Exception {
        //given
        final String formId = "processDefinitionUUID$entry";
        
        //when
        final boolean allowed = testAllowProcessFormAccess(formId, 3L, 3L, true);

        //then
        assertThat(allowed).as("should allow user").isTrue();
    }

    @Test
    public void should_allow_process_form_access_with_custom_form_id_allowed() throws Exception {
        //given
        final String formId = "formId";
        doReturn(true).when(formServiceProviderImpl).isCustomFormIdAuthorized(apiSession);
        
        //when
        final boolean allowed = testAllowProcessFormAccess(formId, 3L, 3L, true);

        //then
        assertThat(allowed).as("should allow user").isTrue();
    }
    
    @Test(expected = ForbiddenFormAccessException.class)
    public void should_not_allow_process_form_access_with_invalid_form_id() throws Exception {
        //given
        final String formId = "processDefinitionUUID$recap";
        
        //when
        testAllowProcessFormAccess(formId, 3L, 3L, true);
    }

    private boolean testAllowProcessFormAccess(final String formId, final long processDefinitionIDFromUUID, final long processDefinitionIdFromURL, final boolean isProcessEnabled) throws Exception {
        final String processDefinitionUUIDStr = "processDefinitionUUID";
        final String permissions = FormServiceProviderUtil.PROCESS_UUID + "#" + processDefinitionUUIDStr;
        final String productVersion = "";
        final String migrationProductVersion = "";
        final boolean isFormPermissions = true;
        final long userId = 2L;

        urlContext.put(FormServiceProviderUtil.PROCESS_UUID, processDefinitionIdFromURL);
        urlContext.put(FormServiceProviderUtil.USER_ID, userId);
        context.put(FormServiceProviderUtil.API_SESSION, apiSession);
        context.put(FormServiceProviderUtil.USER, user);

        doReturn(processDefinitionIDFromUUID).when(workflowAPI).getProcessDefinitionIDFromUUID(apiSession, processDefinitionUUIDStr);
        doReturn(isProcessEnabled).when(workflowAPI).isProcessEnabled(apiSession, processDefinitionIdFromURL);
        doNothing().when(formServiceProviderImpl).canUserInstantiateProcess(apiSession, user, processDefinitionIdFromURL, userId, context);

        return formServiceProviderImpl.isAllowed(formId, permissions, productVersion, migrationProductVersion, context, isFormPermissions);
    }

    @Test
    public void should_assignForm_call_worflowAPI() throws Exception {
        final String formId = "formId";
        final String expectedTaskId = "42";
        final String userId = "3";

        urlContext.put(FormServiceProviderUtil.USER_ID, userId);
        urlContext.put(FormServiceProviderUtil.TASK_UUID, expectedTaskId);
        context.put(FormServiceProviderUtil.API_SESSION, apiSession);

        formServiceProviderImpl.assignForm(formId, context);

        verify(workflowAPI, times(1)).assignTaskIfNotAssigned(apiSession, Long.parseLong(expectedTaskId), 3L);
    }

}
