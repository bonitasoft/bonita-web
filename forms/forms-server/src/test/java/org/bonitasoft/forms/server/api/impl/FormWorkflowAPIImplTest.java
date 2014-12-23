package org.bonitasoft.forms.server.api.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import org.bonitasoft.console.common.server.utils.BPMEngineException;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.process.ProcessDefinitionNotFoundException;
import org.bonitasoft.engine.bpm.process.ProcessInstanceNotFoundException;
import org.bonitasoft.engine.identity.UserNotFoundException;
import org.bonitasoft.engine.session.APISession;
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

    @Mock
    private ProcessAPI processApi;

    @Spy
    private FormWorkflowAPIImpl formWorkflowAPIImpl;

    private long userId;
    private long processInstanceId;

    @Before
    public void before() throws Exception {
        doReturn(processApi).when(formWorkflowAPIImpl).getProcessApi(session);
    }

    @Test
    public void should_canUserSeeProcessInstance_call_engine_api() throws Exception {
        final boolean expected = true;
        checkWhenApiReturn(expected);

    }

    @Test
    public void should_canUserSeeProcessInstance_call_engine_api_false() throws Exception {
        final boolean expected = false;
        checkWhenApiReturn(expected);

    }

    private void checkWhenApiReturn(final boolean expected) throws ProcessInstanceNotFoundException, UserNotFoundException, BPMEngineException,
    ProcessDefinitionNotFoundException {
        userId = 25L;
        processInstanceId = 1L;

        //given
        doReturn(userId).when(session).getUserId();
        doReturn(expected).when(processApi).isInvolvedInProcessInstance(userId, processInstanceId);

        //when
        final boolean canUserSeeProcessInstance = formWorkflowAPIImpl.canUserSeeProcessInstance(session, processInstanceId);

        //then
        verify(processApi).isInvolvedInProcessInstance(userId, processInstanceId);
        assertThat(canUserSeeProcessInstance).as("should return " + expected).isEqualTo(expected);
    }
}
