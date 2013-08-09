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
package org.bonitasoft.web.rest.server.engineclient;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.process.ProcessDefinition;
import org.bonitasoft.engine.bpm.process.ProcessDefinitionNotFoundException;
import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfo;
import org.bonitasoft.test.toolkit.bpm.TestProcess;
import org.bonitasoft.test.toolkit.bpm.TestProcessFactory;
import org.bonitasoft.test.toolkit.organization.TestUser;
import org.bonitasoft.test.toolkit.organization.TestUserFactory;
import org.bonitasoft.web.rest.server.AbstractConsoleTest;
import org.bonitasoft.web.rest.server.engineclient.ProcessEngineClient;
import org.junit.Test;

/**
 * @author Colin PUY
 * 
 */
public class ProcessEngineClientIntegrationTest extends AbstractConsoleTest {

    private ProcessEngineClient processEngineClient;

    @Override
    public void consoleTestSetUp() throws Exception {
        processEngineClient = new ProcessEngineClient(TenantAPIAccessor.getProcessAPI(getInitiator().getSession()));
    }

    @Override
    protected TestUser getInitiator() {
        return TestUserFactory.getJohnCarpenter();
    }

    private ProcessDefinition getProcessDefinition(long processId) throws Exception {
        try {
            return TenantAPIAccessor.getProcessAPI(getInitiator().getSession()).getProcessDefinition(processId);
        } catch (ProcessDefinitionNotFoundException e) {
            return null;
        }
    }

    @Test
    public void testCountResolvedProcesses() throws Exception {
        create2resolvedProcesses();

        long resolvedProcesses = processEngineClient.countResolvedProcesses();

        assertEquals(2L, resolvedProcesses);
    }

    private void create2resolvedProcesses() {
        TestProcessFactory.createRandomResolvedProcess(getInitiator());
        TestProcessFactory.createRandomResolvedProcess(getInitiator());
    }

    @Test
    public void testDeleteProcesses() throws Exception {
        TestProcess deployedProcess = TestProcessFactory.getDefaultHumanTaskProcess().addActor(getInitiator());

        processEngineClient.deleteProcesses(asList(deployedProcess.getId()));

        assertNull(getProcessDefinition(deployedProcess.getId()));
    }
    
    @Test public void 
    getProcessDeploymentInfo_return_null_if_process_is_not_found() throws Exception {
        long unknownProcessId = 1L;
        
        ProcessDeploymentInfo processDeploymentInfo = processEngineClient.getProcessDeploymentInfo(unknownProcessId);
        
        assertNull(processDeploymentInfo);
    }
    
    @Test public void 
    getProcessDeploymentInfo_return_info_if_process_is_found() throws Exception {
        TestProcess deployedProcess = TestProcessFactory.getDefaultHumanTaskProcess().addActor(getInitiator());
        
        ProcessDeploymentInfo processDeploymentInfo = processEngineClient.getProcessDeploymentInfo(deployedProcess.getId());
        
        assertNotNull(processDeploymentInfo);
    }
}
