package org.bonitasoft.console.common.server.form;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.engine.api.CommandAPI;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.flownode.ActivityInstance;
import org.bonitasoft.engine.bpm.flownode.ActivityInstanceNotFoundException;
import org.bonitasoft.engine.bpm.flownode.ArchivedActivityInstance;
import org.bonitasoft.engine.bpm.flownode.ArchivedHumanTaskInstance;
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance;
import org.bonitasoft.engine.bpm.process.ArchivedProcessInstance;
import org.bonitasoft.engine.bpm.process.ProcessInstance;
import org.bonitasoft.engine.bpm.process.ProcessInstanceNotFoundException;
import org.bonitasoft.engine.search.SearchOptions;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.engine.session.APISession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class ProcessFormServiceTest {

    @Spy
    ProcessFormService processFormService;

    @Mock
    ProcessAPI processAPI;

    @Mock
    CommandAPI commandAPI;

    @Mock
    APISession apiSession;

    @Before
    public void beforeEach() throws Exception {
        doReturn(processAPI).when(processFormService).getProcessAPI(apiSession);
        doReturn(commandAPI).when(processFormService).getCommandAPI(apiSession);
        when(apiSession.getUserId()).thenReturn(1L);
    }

    @Test
    public void ensureProcessDefinitionID_with_processDefinitionID() throws Exception {
        assertEquals(42L, processFormService.ensureProcessDefinitionID(apiSession, 42L, -1L, -1L));
    }

    @Test
    public void ensureProcessDefinitionID_with_processInstanceID() throws Exception {
        final ProcessInstance processInstance = mock(ProcessInstance.class);
        when(processInstance.getProcessDefinitionId()).thenReturn(1L);
        when(processAPI.getProcessInstance(42L)).thenReturn(processInstance);
        assertEquals(1L, processFormService.ensureProcessDefinitionID(apiSession, -1L, 42L, -1L));
    }

    @Test
    public void ensureProcessDefinitionID_with_processInstanceID_on_archived_instance() throws Exception {
        final ArchivedProcessInstance archivedProcessInstance = mock(ArchivedProcessInstance.class);
        when(archivedProcessInstance.getProcessDefinitionId()).thenReturn(1L);
        when(processAPI.getProcessInstance(42L)).thenThrow(ProcessInstanceNotFoundException.class);
        final List<ArchivedProcessInstance> result = new ArrayList<ArchivedProcessInstance>();
        result.add(archivedProcessInstance);
        final SearchResult<ArchivedProcessInstance> searchResult = mock(SearchResult.class);
        when(searchResult.getCount()).thenReturn(1L);
        when(searchResult.getResult()).thenReturn(result);
        when(processAPI.searchArchivedProcessInstancesInAllStates(any(SearchOptions.class))).thenReturn(searchResult);
        assertEquals(1L, processFormService.ensureProcessDefinitionID(apiSession, -1L, 42L, -1L));
    }

    @Test
    public void ensureProcessDefinitionID_with_taskInstanceID() throws Exception {
        final ActivityInstance activityInstance = mock(ActivityInstance.class);
        when(activityInstance.getProcessDefinitionId()).thenReturn(1L);
        when(processAPI.getActivityInstance(42L)).thenReturn(activityInstance);
        assertEquals(1L, processFormService.ensureProcessDefinitionID(apiSession, -1L, -1L, 42L));
    }

    @Test
    public void ensureProcessDefinitionID_with_taskInstanceID_on_archived_instance() throws Exception {
        final ArchivedActivityInstance archivedActivityInstance = mock(ArchivedActivityInstance.class);
        when(archivedActivityInstance.getProcessDefinitionId()).thenReturn(1L);
        when(processAPI.getActivityInstance(42L)).thenThrow(ActivityInstanceNotFoundException.class);
        when(processAPI.getArchivedActivityInstance(42L)).thenReturn(archivedActivityInstance);
        assertEquals(1L, processFormService.ensureProcessDefinitionID(apiSession, -1L, -1L, 42L));
    }

    @Test
    public void getTaskName_with_taskInstanceID() throws Exception {
        final ActivityInstance activityInstance = mock(ActivityInstance.class);
        when(activityInstance.getName()).thenReturn("myTask");
        when(processAPI.getActivityInstance(42L)).thenReturn(activityInstance);
        assertEquals("myTask", processFormService.getTaskName(apiSession, 42L));
    }

    @Test
    public void getTaskName_with_taskInstanceID_on_archived_instance() throws Exception {
        final ArchivedActivityInstance archivedActivityInstance = mock(ArchivedActivityInstance.class);
        when(archivedActivityInstance.getName()).thenReturn("myTask");
        when(processAPI.getActivityInstance(42L)).thenThrow(ActivityInstanceNotFoundException.class);
        when(processAPI.getArchivedActivityInstance(42L)).thenReturn(archivedActivityInstance);
        assertEquals("myTask", processFormService.getTaskName(apiSession, 42L));
    }

    @Test
    public void getTaskInstanceID_with_taskInstanceID() throws Exception {
        final HumanTaskInstance humanTaskInstance = mock(HumanTaskInstance.class);
        when(humanTaskInstance.getId()).thenReturn(1L);
        final List<HumanTaskInstance> result = new ArrayList<HumanTaskInstance>();
        result.add(humanTaskInstance);
        final SearchResult<HumanTaskInstance> searchResult = mock(SearchResult.class);
        when(searchResult.getCount()).thenReturn(1L);
        when(searchResult.getResult()).thenReturn(result);
        when(processAPI.searchMyAvailableHumanTasks(anyLong(), any(SearchOptions.class))).thenReturn(searchResult);
        assertEquals(1L, processFormService.getTaskInstanceID(apiSession, 42L, "myTask", -1L));
    }

    @Test
    public void getTaskInstanceID_with_taskInstanceID_on_archived_instance() throws Exception {
        when(processAPI.searchMyAvailableHumanTasks(anyLong(), any(SearchOptions.class))).thenReturn(mock(SearchResult.class));
        final ArchivedHumanTaskInstance archivedHumanTaskInstance = mock(ArchivedHumanTaskInstance.class);
        when(archivedHumanTaskInstance.getSourceObjectId()).thenReturn(1L);
        final List<ArchivedHumanTaskInstance> result = new ArrayList<ArchivedHumanTaskInstance>();
        result.add(archivedHumanTaskInstance);
        final SearchResult<ArchivedHumanTaskInstance> searchResult = mock(SearchResult.class);
        when(searchResult.getCount()).thenReturn(1L);
        when(searchResult.getResult()).thenReturn(result);
        when(processAPI.searchArchivedHumanTasks(any(SearchOptions.class))).thenReturn(searchResult);
        assertEquals(1L, processFormService.getTaskInstanceID(apiSession, 42L, "myTask", -1L));
    }

    @Test
    public void getProcessDefinitionID_with_no_version() throws Exception {
        assertEquals(-1L, processFormService.getProcessDefinitionID(apiSession, "myProcess", null));
    }

    @Test
    public void getProcessDefinitionID_with_no_name_and_version() throws Exception {
        assertEquals(-1L, processFormService.getProcessDefinitionID(apiSession, null, null));
    }
}
