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
package org.bonitasoft.test.toolkit.bpm;

import static junit.framework.Assert.assertTrue;

import java.util.List;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.connector.ConnectorCriterion;
import org.bonitasoft.engine.bpm.connector.ConnectorImplementationDescriptor;
import org.bonitasoft.test.toolkit.EngineSetup;
import org.bonitasoft.test.toolkit.bpm.process.TestProcessConnectorFactory;
import org.bonitasoft.test.toolkit.organization.TestToolkitCtx;
import org.bonitasoft.test.toolkit.organization.TestUserFactory;
import org.junit.After;
import org.junit.Test;

/**
 * @author Colin PUY
 * 
 */
public class TestProcessFactoryTest extends EngineSetup {

    @After
    public void clearSession() throws Exception {
        TestToolkitCtx.getInstance().clearSession();
    }

    @Test
    public void testCreateProcessWithConnector() throws Exception {

        final TestProcess processWithConnector = TestProcessFactory.createProcessWithConnector(TestProcessConnectorFactory.getDefaultConnector());

        final List<ConnectorImplementationDescriptor> connectorImplementations =
                getProcessAPI().getConnectorImplementations(processWithConnector.getId(), 0, 10, ConnectorCriterion.DEFINITION_ID_ASC);

        assertTrue(connectorImplementations.size() > 0);
    }

    private ProcessAPI getProcessAPI() throws Exception {
        return TenantAPIAccessor.getProcessAPI(TestUserFactory.getJohnCarpenter().getSession());
    }

}
