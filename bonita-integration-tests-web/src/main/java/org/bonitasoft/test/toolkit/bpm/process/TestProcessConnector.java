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
package org.bonitasoft.test.toolkit.bpm.process;

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.engine.bpm.connector.ConnectorEvent;

/**
 * @author Colin PUY
 * 
 */
public class TestProcessConnector {

    private String name;
    private String id;
    private String version;
    private String implementationClassname;
    private String implementationId;
    private ConnectorEvent connectorEvent;

    private String resourceFileName;
    private String resourceFilePath;
    
    private List<String> dependencies = new ArrayList<String>();

    public TestProcessConnector(String name, String id, String version, String implementationClassname, String implementationId, 
            ConnectorEvent connectorEvent, String resourceFileName, String resourceFilePath) {
        super();
        this.name = name;
        this.id = id;
        this.version = version;
        this.implementationClassname = implementationClassname;
        this.implementationId = implementationId;
        this.connectorEvent = connectorEvent;
        this.resourceFileName = resourceFileName;
        this.resourceFilePath = resourceFilePath;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getVersion() {
        return version;
    }

    public ConnectorEvent getConnectorEvent() {
        return connectorEvent;
    }

    public String getResourceFileName() {
        return resourceFileName;
    }

    public String getResourceFilePath() {
        return resourceFilePath;
    }
    
    public String getImplementationClassname() {
        return implementationClassname;
    }

    public String getImplementationId() {
        return implementationId;
    }
    
    public TestProcessConnector addDependency(String dependency) {
        dependencies.add(dependency);
        return this;
    }
    
    public List<String> getDependencies() {
        return dependencies;
    }
}
