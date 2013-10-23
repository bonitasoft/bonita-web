/**
 * Copyright (C) 2012 BonitaSoft S.A.
 * 
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.web.rest.server.engineclient;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.process.ProcessActivationException;
import org.bonitasoft.engine.bpm.process.ProcessDefinitionNotFoundException;
import org.bonitasoft.engine.bpm.process.ProcessExecutionException;
import org.bonitasoft.web.rest.server.APITestWithMock;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class CaseEngineClientTest extends APITestWithMock {

    @Mock
    private ProcessAPI processAPI;
    private CaseEngineClient caseEngineClient;
    
    @Before
    public void setUp() {
        initMocks(this);
        caseEngineClient = new CaseEngineClient(processAPI);
    }
    
    private Map<String, Serializable> someVariables() {
        Map<String, Serializable> map = new HashMap<String, Serializable>();
        map.put("variable", 1L);
        return map;
    }
    
    @Test
    public void a_process_can_be_started_without_variables() throws Exception {
        long expectedProcessId = 1L;
        
        caseEngineClient.start(expectedProcessId);
        
        verify(processAPI).startProcess(expectedProcessId);
    }
    
    @Test
    public void a_process_can_be_started_with_variables() throws Exception {
        long expectedProcessId = 1L;
        Map<String, Serializable> variables = someVariables();
        
        caseEngineClient.start(expectedProcessId, variables);
        
        verify(processAPI).startProcess(expectedProcessId, variables);
    }

    @Test(expected = APIException.class)
    public void cant_create_case_if_process_definition_is_not_found() throws Exception {
        when(processAPI.startProcess(anyLong())).thenThrow(new ProcessDefinitionNotFoundException(""));
        
        caseEngineClient.start(1L);
    }
    
    @Test(expected = APIException.class)
    public void cant_create_case_if_process_is_not_activated() throws Exception {
        when(processAPI.startProcess(anyLong())).thenThrow(new ProcessActivationException(""));
        
        caseEngineClient.start(1L);
    }
    
    @Test(expected = APIException.class)
    public void we_get_an_exception_if_process_fail_to_start() throws Exception {
        when(processAPI.startProcess(anyLong())).thenThrow(new ProcessExecutionException(""));
        
        caseEngineClient.start(1L);
    }
    
    @SuppressWarnings("unchecked")
    @Test(expected = APIException.class)
    public void cant_create_case_with_variables_if_process_definition_is_not_found() throws Exception {
        when(processAPI.startProcess(anyLong(), anyMap())).thenThrow(new ProcessDefinitionNotFoundException(""));
        
        caseEngineClient.start(1L, someVariables());
    }
    
    @SuppressWarnings("unchecked")
    @Test(expected = APIException.class)
    public void cant_create_case_with_variables_if_process_is_not_activated() throws Exception {
        when(processAPI.startProcess(anyLong(), anyMap())).thenThrow(new ProcessActivationException(""));
        
        caseEngineClient.start(1L, someVariables());
    }
    
    @SuppressWarnings("unchecked")
    @Test(expected = APIException.class)
    public void we_get_an_exception_if_process_fail_to_start_with_variables() throws Exception {
        when(processAPI.startProcess(anyLong(), anyMap())).thenThrow(new ProcessExecutionException(""));
        
        caseEngineClient.start(1L, someVariables());
    }

}
