package org.bonitasoft.forms.server.provider.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Map;

import org.bonitasoft.console.common.server.utils.BPMEngineAPIUtil;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.flownode.ActivityInstanceNotFoundException;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.forms.server.api.IFormWorkflowAPI;
import org.bonitasoft.forms.server.api.impl.FormWorkflowAPIImpl;
import org.bonitasoft.forms.server.provider.impl.util.FormServiceProviderUtil;
import org.bonitasoft.forms.server.util.FormContextUtil;
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
    Map<String, Object> context;

    @Mock
    IFormWorkflowAPI workflowAPI;

    @Mock
    Map<String, Object> urlContext;

    @Mock
    FormContextUtil formContextUtil;

    @Mock
    APISession apiSession;

    @Mock
    BPMEngineAPIUtil bpmEngineAPIUtil;

    @Mock
    ProcessAPI processAPI;

    @Mock
    FormWorkflowAPIImpl formWorkflowAPIImpl;

    @Before
    public void before() throws Exception {
        doReturn(urlContext).when(context).get(FormServiceProviderUtil.URL_CONTEXT);
        doReturn(processAPI).when(bpmEngineAPIUtil).getProcessAPI(any(APISession.class));
        doReturn(workflowAPI).when(formServiceProviderImpl).getFormWorkFlowApi();
        doReturn(apiSession).when(formContextUtil).getAPISessionFromContext();

    }

    @Test
    public void getProcessDefinitionId_with_process_UUID() throws Exception {
        final long expectedProcessDefinitionId = 123l;
        // given
        doReturn(expectedProcessDefinitionId).when(urlContext).get(FormServiceProviderUtil.PROCESS_UUID);

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
        doReturn(null).when(urlContext).get(FormServiceProviderUtil.PROCESS_UUID);
        doReturn("processName--1.0$entry").when(urlContext).get(FormServiceProviderUtil.FORM_ID);

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
}
