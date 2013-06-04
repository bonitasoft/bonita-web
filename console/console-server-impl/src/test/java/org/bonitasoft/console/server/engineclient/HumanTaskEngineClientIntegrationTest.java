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
package org.bonitasoft.console.server.engineclient;

import static junit.framework.Assert.assertEquals;

import org.bonitasoft.console.server.AbstractConsoleTest;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.test.toolkit.bpm.TestCaseFactory;
import org.bonitasoft.test.toolkit.organization.TestUser;
import org.bonitasoft.test.toolkit.organization.TestUserFactory;
import org.junit.Test;

/**
 * @author Colin PUY
 * 
 */
public class HumanTaskEngineClientIntegrationTest extends AbstractConsoleTest {

    private HumanTaskEngineClient humanTaskEngineClient;
    
    @Override
    public void consoleTestSetUp() throws Exception {
        humanTaskEngineClient = new HumanTaskEngineClient(TenantAPIAccessor.getProcessAPI(getInitiator().getSession()));
    }

    @Override
    protected TestUser getInitiator() {
        return TestUserFactory.getJohnCarpenter();
    }

    @Test
    public void testCountOpenedTasks() throws Exception {
        create2openedTasks();
        
        long openedTasks = humanTaskEngineClient.countOpenedHumanTasks();
        
        assertEquals(2L, openedTasks);
    }

    private void create2openedTasks() {
        TestCaseFactory.createRandomCase(getInitiator()).getNextHumanTask().assignTo(getInitiator());
        TestCaseFactory.createRandomCase(getInitiator()).getNextHumanTask().assignTo(getInitiator());
    }

}
