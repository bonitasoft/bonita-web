/**
 * Copyright (C) 2012 BonitaSoft S.A.
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
package org.bonitasoft.forms.server.accessor.api.utils;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.bonitasoft.engine.bpm.process.ArchivedProcessInstance;
import org.bonitasoft.engine.bpm.process.ProcessInstance;
import org.bonitasoft.engine.bpm.process.ProcessInstanceNotFoundException;
import org.bonitasoft.forms.server.accessor.api.ProcessInstanceAccessorEngineClient;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

/**
 * @author Vincent Elcrin, Anthony Birembaut
 * 
 */
public class ProcessInstanceAccessorTest {

    @Mock
    ProcessInstanceAccessorEngineClient engineClient;

    @Mock
    ProcessInstance processInstance;

    @Mock
    ArchivedProcessInstance archivedProcessInstance;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void testWeCanGetAProcessInstanceInformation() throws Exception {
        when(engineClient.getProcessInstance(1L)).thenReturn(processInstance);

        final ProcessInstanceAccessor accessor = new ProcessInstanceAccessor(engineClient, 1L);
        final long id = accessor.getId();

        assertEquals(1L, id);
    }

    @Test
    public void testWeCanGetAnArchivedProcessInstanceInformation() throws Exception {
        when(engineClient.getProcessInstance(2L)).thenThrow(new ProcessInstanceNotFoundException(2L));

        final ProcessInstanceAccessor accessor = new ProcessInstanceAccessor(engineClient, 2L);
        final boolean isArchived = accessor.isArchived();

        assertEquals(true, isArchived);
    }
}
