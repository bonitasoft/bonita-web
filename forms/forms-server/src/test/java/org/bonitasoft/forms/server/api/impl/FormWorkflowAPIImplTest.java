package org.bonitasoft.forms.server.api.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.bonitasoft.console.common.server.utils.BPMEngineAPIUtil;
import org.bonitasoft.engine.api.IdentityAPI;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance;
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
        final boolean expected = true;
        checkCanUserSeeProcessInstanceWhenApiReturn(expected);
    }

    @Test
    public void should_canUserSeeProcessInstance_call_engine_api_false() throws Exception {
        final boolean expected = false;
        checkCanUserSeeProcessInstanceWhenApiReturn(expected);
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
}
