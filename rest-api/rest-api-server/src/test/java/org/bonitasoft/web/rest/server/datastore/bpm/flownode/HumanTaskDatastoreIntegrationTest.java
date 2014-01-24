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
package org.bonitasoft.web.rest.server.datastore.bpm.flownode;

import static org.bonitasoft.web.toolkit.client.data.APIID.makeAPIID;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance;
import org.bonitasoft.engine.bpm.flownode.TaskPriority;
import org.bonitasoft.test.toolkit.bpm.TestHumanTask;
import org.bonitasoft.test.toolkit.bpm.TestProcessFactory;
import org.bonitasoft.web.rest.model.bpm.flownode.HumanTaskItem;
import org.bonitasoft.web.rest.server.AbstractConsoleTest;
import org.junit.Test;

public class HumanTaskDatastoreIntegrationTest extends AbstractConsoleTest {

    private HumanTaskDatastore humanTaskDatastore;

    @Override
    public void consoleTestSetUp() throws Exception {
        this.humanTaskDatastore = new HumanTaskDatastore(getInitiator().getSession());
    }

    private HumanTaskItem fetchHumanTask(long taskId) throws Exception {
		HumanTaskInstance humanTaskInstance = 
				(HumanTaskInstance) TenantAPIAccessor.getProcessAPI(getInitiator().getSession()).getActivityInstance(taskId);
		return HumanTaskDatastore.fillConsoleItem(new HumanTaskItem(), humanTaskInstance);
	}

	@Test
    public void task_priority_can_be_changed() throws Exception {
    	TestHumanTask humanTask = TestProcessFactory.getDefaultHumanTaskProcess().addActor(getInitiator())
                .startCase().getNextHumanTask();
        Map<String, String> attributes = new HashMap<String, String>();
        attributes.put(HumanTaskItem.ATTRIBUTE_PRIORITY, TaskPriority.HIGHEST.name());

        this.humanTaskDatastore.update(makeAPIID(humanTask.getId()), attributes);
        
        HumanTaskItem fetchedTask = fetchHumanTask(humanTask.getId());
        assertEquals(HumanTaskItem.VALUE_PRIORITY_HIGHEST, fetchedTask.getPriority());
    }

}
