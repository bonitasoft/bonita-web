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
package org.bonitasoft.web.rest.server.datastore.bpm.connector;

import junit.framework.Assert;

import org.bonitasoft.engine.bpm.connector.ConnectorEvent;
import org.bonitasoft.engine.bpm.connector.ConnectorInstance;
import org.bonitasoft.engine.bpm.connector.ConnectorState;
import org.bonitasoft.engine.bpm.connector.impl.ConnectorInstanceImpl;
import org.bonitasoft.web.rest.api.model.bpm.connector.ConnectorInstanceItem;
import org.bonitasoft.web.rest.server.ConsoleAPIServlet;
import org.bonitasoft.web.rest.server.datastore.bpm.connector.ConnectorInstanceItemWrapper;
import org.bonitasoft.web.rest.server.datastore.bpm.connector.ConnectorInstanceStateConverter;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Colin PUY
 * 
 */
public class ConnectorInstanceItemWrapperTest {

    private static ConsoleAPIServlet consoleAPIServlet;

    @BeforeClass
    public static void initContextForDefinition() {
        consoleAPIServlet = new ConsoleAPIServlet();
    }
    
    @AfterClass
    public static void destroyContext() {
        consoleAPIServlet.destroy();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void cantWrapANullItem() throws Exception {
        new ConnectorInstanceItemWrapper(null);
    }

    @Test
    public void convertItemReturnRightItem() {
        final ConnectorInstance expected = createConnectorInstanceImpl(1L, "instance1");
        final ConnectorInstanceItem actual = new ConnectorInstanceItemWrapper(expected);
        Assert.assertTrue(areEquals(expected, actual));
    }

    private boolean areEquals(final ConnectorInstance expected, final ConnectorInstanceItem actual) {
        return APIID.makeAPIID(expected.getConnectorId()).equals(actual.getConnectorId())
                && APIID.makeAPIID(expected.getContainerId()).equals(actual.getContainerId())
                && expected.getContainerType().equals(actual.getContainerType())
                && APIID.makeAPIID(expected.getId()).equals(actual.getId())
                && expected.getName().equals(actual.getName())
                && expected.getState().equals(new ConnectorInstanceStateConverter().convert(actual.getState()))
                && expected.getVersion().equals(actual.getVersion());
    }

    private ConnectorInstance createConnectorInstanceImpl(final long id, final String name) {
        final ConnectorInstanceImpl connectorInstance = new ConnectorInstanceImpl(name, 2L, "containerType", String.valueOf(id), "version",
                ConnectorState.DONE,
                ConnectorEvent.ON_ENTER);
        connectorInstance.setId(1L);
        return connectorInstance;
    }

}
