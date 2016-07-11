package org.bonitasoft.forms.server.api.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.bonitasoft.console.common.server.utils.BPMEngineAPIUtil;
import org.bonitasoft.engine.api.IdentityAPI;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.ProfileAPI;
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance;
import org.bonitasoft.engine.profile.Profile;
import org.bonitasoft.engine.profile.ProfileCriterion;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.forms.client.model.Expression;
import org.bonitasoft.forms.client.model.FormAction;
import org.bonitasoft.forms.client.model.exception.ForbiddenFormAccessException;
import org.bonitasoft.forms.server.api.IFormExpressionsAPI;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FormWorkflowAPIImplTest {

    @Mock
    private APISession session;

    private final Locale locale = new Locale("en");

    @Mock
    private Map<String, Serializable> context;

    @Mock
    private List<FormAction> actions;

    @Mock
    private ProcessAPI processApi;

    @Mock
    private IdentityAPI identityApi;

    @Mock
    private BPMEngineAPIUtil bpmEngineAPIUtil;

    @Mock
    private IFormExpressionsAPI formExpressionsAPI;

    @Spy
    private FormWorkflowAPIImpl formWorkflowAPIImpl;

    @Mock
    private List<Expression> expressions;

    @Mock
    private HumanTaskInstance humanTaskInstance;

    @Before
    public void setUp() throws Exception {
        formWorkflowAPIImpl = spy(new FormWorkflowAPIImpl());
        expressions = new ArrayList<Expression>();
        doReturn(bpmEngineAPIUtil).when(formWorkflowAPIImpl).getBpmEngineAPIUtil();
        doReturn(processApi).when(bpmEngineAPIUtil).getProcessAPI(session);
    }

    @Test
    public void should_canUserSeeProcessInstance_call_engine_api() throws Exception {
        checkCanUserSeeProcessInstanceWhenApiReturn(true);
    }

    @Test
    public void should_canUserSeeProcessInstance_call_engine_api_false() throws Exception {
        checkCanUserSeeProcessInstanceWhenApiReturn(false);
    }

    private void checkCanUserSeeProcessInstanceWhenApiReturn(final boolean expected) throws Exception {
        final long userId = 25L;
        final long processInstanceId = 1L;
        //given
        doReturn(userId).when(session).getUserId();
        doReturn(expected).when(processApi).isInvolvedInProcessInstance(userId, processInstanceId);
        //when
        final boolean canUserSeeProcessInstance = formWorkflowAPIImpl.canUserSeeProcessInstance(session, processInstanceId);

        //then
        verify(processApi).isInvolvedInProcessInstance(userId, processInstanceId);
        assertThat(canUserSeeProcessInstance).as("should return " + expected).isEqualTo(expected);
    }

    @Test
    public void should_canUserSeeHumanTask_call_engine_api() throws Exception {
        final boolean expected = true;
        checkCanUserSeeHumanTaskWhenApiReturn(expected);
    }

    @Test
    public void should_canUserSeeHumanTask_call_engine_api_false() throws Exception {
        final boolean expected = false;
        checkCanUserSeeHumanTaskWhenApiReturn(expected);
    }

    private void checkCanUserSeeHumanTaskWhenApiReturn(final boolean expected) throws Exception {
        final long userId = 25L;
        final long humanTaskInstanceId = 1L;
        //given
        doReturn(userId).when(session).getUserId();
        doReturn(expected).when(processApi).isInvolvedInHumanTaskInstance(userId, humanTaskInstanceId);
        //when
        final boolean canUserSeeHumanTask = formWorkflowAPIImpl.canUserSeeHumanTask(session, -1L, humanTaskInstanceId);

        //then
        verify(processApi).isInvolvedInHumanTaskInstance(userId, humanTaskInstanceId);
        assertThat(canUserSeeHumanTask).as("should return " + expected).isEqualTo(expected);
    }

    @Test
    public void it_should_call_evaluateActivityInitialExpressions() throws Exception {
        // Given
        formWorkflowAPIImpl = spy(new FormWorkflowAPIImpl());
        final long processDefinitionID = -1;
        final long activityInstanceID = 1;
        expressions = new ArrayList<Expression>();
        // When
        formWorkflowAPIImpl.getEvaluateConditionExpressions(session, actions, locale, context,
                processDefinitionID, activityInstanceID, formExpressionsAPI);

        // Then
        verify(formExpressionsAPI).evaluateActivityInitialExpressions(session, activityInstanceID, expressions, locale, true, context);
        verify(formExpressionsAPI, never()).evaluateProcessInitialExpressions(session, processDefinitionID, expressions, locale, context);
    }

    @Test
    public void it_should_call_evaluateProcessInitialExpressions() throws Exception {
        // Given
        final long processDefinitionID = 1;
        final long activityInstanceID = -1;
        // When
        formWorkflowAPIImpl.getEvaluateConditionExpressions(session, actions, locale, context,
                processDefinitionID, activityInstanceID, formExpressionsAPI);

        // Then
        verify(formExpressionsAPI).evaluateProcessInitialExpressions(session, processDefinitionID, expressions, locale, context);
        verify(formExpressionsAPI, never()).evaluateActivityInitialExpressions(session, activityInstanceID, expressions, locale, true, context);
    }

    @Test
    public void it_should_not_call_evaluateProcessInitialExpressions_nor_evaluateActivityInitialExpressions() throws Exception {
        // Given
        final long processDefinitionID = -1;
        final long activityInstanceID = -1;
        // When
        formWorkflowAPIImpl.getEvaluateConditionExpressions(session, actions, locale, context,
                processDefinitionID, activityInstanceID, formExpressionsAPI);
        // Then
        verify(formExpressionsAPI, never()).evaluateProcessInitialExpressions(session, processDefinitionID, expressions, locale, context);
        verify(formExpressionsAPI, never()).evaluateActivityInitialExpressions(session, activityInstanceID, expressions, locale, true, context);
    }

    @Test
    public void asignTaskIfNotAssigned_should_call_engine() throws Exception {
        // Given
        final long userId = 25L;
        final long activityInstanceID = 42L;
        doReturn(humanTaskInstance).when(processApi).getHumanTaskInstance(activityInstanceID);
        doReturn(0L).when(humanTaskInstance).getAssigneeId();
        doReturn(userId).when(session).getUserId();
        // When
        formWorkflowAPIImpl.assignTaskIfNotAssigned(session, activityInstanceID, session.getUserId());
        // Then
        verify(processApi).assignUserTask(activityInstanceID, userId);
    }

    @Test
    public void asignTaskIfNotAssigned_should_not_call_engine_if_the_task_is_already_assigned() throws Exception {
        // Given
        final long userId = 25L;
        final long activityInstanceID = 42L;
        doReturn(humanTaskInstance).when(processApi).getHumanTaskInstance(activityInstanceID);
        doReturn(25L).when(humanTaskInstance).getAssigneeId();
        doReturn(userId).when(session).getUserId();
        // When
        formWorkflowAPIImpl.assignTaskIfNotAssigned(session, activityInstanceID, session.getUserId());
        // Then
        verify(processApi, never()).assignUserTask(activityInstanceID, userId);
    }

    @Test(expected = ForbiddenFormAccessException.class)
    public void asignTaskIfNotAssigned_should_throw_exception_if_the_task_is_already_assigned_to_someone_else() throws Exception {
        // Given
        final long userId = 25L;
        final long activityInstanceID = 42L;
        doReturn(humanTaskInstance).when(processApi).getHumanTaskInstance(activityInstanceID);
        doReturn(33L).when(humanTaskInstance).getAssigneeId();
        doReturn(userId).when(session).getUserId();
        // When
        formWorkflowAPIImpl.assignTaskIfNotAssigned(session, activityInstanceID, session.getUserId());
    }

    @Test
    public void hasUserAdminProfileShouldReturnFalseForNOTAdminProfile() throws Exception {
        final ProfileAPI profileAPI = mock(ProfileAPI.class);
        final long userId = 488L;
        final Profile profile = mock(Profile.class);
        doReturn(Collections.singletonList(profile)).when(profileAPI).getProfilesForUser(userId, 0, 10, ProfileCriterion.NAME_ASC);
        doReturn(userId).when(session).getUserId();
        doReturn("User_Profile").when(profile).getName();

        final boolean adminProfile = formWorkflowAPIImpl.hasUserAdminProfile(session, profileAPI);

        assertThat(adminProfile).isFalse();
    }

    @Test
    public void hasUserAdminProfileShouldReturnTrueForAdminProfileOnFirstPage() throws Exception {
        final ProfileAPI profileAPI = mock(ProfileAPI.class);
        final long userId = 488L;
        final Profile profile = mock(Profile.class);
        doReturn(Collections.singletonList(profile)).when(profileAPI).getProfilesForUser(userId, 0, 10, ProfileCriterion.NAME_ASC);
        doReturn(userId).when(session).getUserId();
        doReturn(formWorkflowAPIImpl.ADMIN_PROFILE_NAME).when(profile).getName();

        final boolean adminProfile = formWorkflowAPIImpl.hasUserAdminProfile(session, profileAPI);

        assertThat(adminProfile).isTrue();
    }

    @Test
    public void hasUserAdminProfileShouldReturnTrueForAdminProfileOnSecondPage() throws Exception {
        final ProfileAPI profileAPI = mock(ProfileAPI.class);
        final long userId = 488L;
        final Profile badProfile = mock(Profile.class);
        doReturn(Collections.singletonList(badProfile)).when(profileAPI).getProfilesForUser(userId, 0, 10, ProfileCriterion.NAME_ASC);
        doReturn("badProfileName").when(badProfile).getName();
        final Profile adminProfile = mock(Profile.class);
        doReturn(Collections.singletonList(adminProfile)).when(profileAPI).getProfilesForUser(userId, 10, 10, ProfileCriterion.NAME_ASC);
        doReturn(formWorkflowAPIImpl.ADMIN_PROFILE_NAME).when(adminProfile).getName();

        doReturn(userId).when(session).getUserId();

        final boolean foundProfile = formWorkflowAPIImpl.hasUserAdminProfile(session, profileAPI);

        assertThat(foundProfile).isTrue();
    }
}
