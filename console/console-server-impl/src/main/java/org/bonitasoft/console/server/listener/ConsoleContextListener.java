/**
 * Copyright (C) 2011 BonitaSoft S.A.
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
package org.bonitasoft.console.server.listener;

import org.bonitasoft.console.client.model.ConsoleFactoryCommon;
import org.bonitasoft.console.server.ConsoleFactoryServer;
import org.bonitasoft.console.server.datastore.bpm.flownode.FlowNodeConverter;
import org.bonitasoft.web.toolkit.client.ApplicationFactoryCommon;
import org.bonitasoft.web.toolkit.server.ApplicationFactoryServer;
import org.bonitasoft.web.toolkit.server.listener.ApplicationContextListener;

/**
 * @author Yongtao Guo
 * 
 */
public class ConsoleContextListener extends ApplicationContextListener {

    @Override
    public ApplicationFactoryCommon defineApplicationFactoryCommon() {
        return new ConsoleFactoryCommon();
    }

    @Override
    public ApplicationFactoryServer defineApplicationFactoryServer() {
        return new ConsoleFactoryServer();
    }

    @Override
    public void init() {
        FlowNodeConverter.setFlowNodeConverter(new FlowNodeConverter());
    }

}
