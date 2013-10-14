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
package org.bonitasoft.web.rest.server.api.bpm.flownode;

import static org.bonitasoft.web.toolkit.client.data.APIID.makeAPIID;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.flownode.ActivityInstanceNotFoundException;
import org.bonitasoft.engine.bpm.flownode.ArchivedActivityInstance;
import org.bonitasoft.engine.bpm.process.impl.ProcessDefinitionBuilder;
import org.bonitasoft.engine.expression.ExpressionBuilder;
import org.bonitasoft.engine.expression.InvalidExpressionException;
import org.bonitasoft.test.toolkit.bpm.TestHumanTask;
import org.bonitasoft.test.toolkit.bpm.TestProcess;
import org.bonitasoft.test.toolkit.organization.TestUser;
import org.bonitasoft.test.toolkit.organization.TestUserFactory;
import org.bonitasoft.web.rest.model.bpm.flownode.ActivityItem;
import org.bonitasoft.web.rest.server.AbstractConsoleTest;
import org.bonitasoft.web.rest.server.WaitUntil;
import org.junit.Test;

public class APIActivityIntegrationTest extends AbstractConsoleTest {

    private static final String JSON_UPDATE_VARIABLES = "[" +
                "{\"name\": \"variable1\", \"value\": \"newValue\"}," +
                "{\"name\": \"variable2\", \"value\": 9}," +
                "{\"name\": \"variable3\", \"value\": 349246800000}" +
    		"]";
    
    private APIActivity apiActivity;

    @Override
    public void consoleTestSetUp() throws Exception {
        apiActivity = new APIActivity();
        apiActivity.setCaller(getAPICaller(TestUserFactory.getJohnCarpenter().getSession(), "API/bpm/activity"));
    }

    @Override
    protected TestUser getInitiator() {
        return TestUserFactory.getJohnCarpenter();
    }   
    
    /**
     * Variables :
     *  - variable1 : String
     *  - variable2 : Long
     *  - variable3 : Date
     */
    private TestHumanTask createActivityWithVariables() throws InvalidExpressionException {
        ProcessDefinitionBuilder processDefinitionBuidler = new ProcessDefinitionBuilder().createNewInstance("processName", "1.0");
        processDefinitionBuidler.addActor("Employees", true)
                .addDescription("This a default process")
                .addStartEvent("Start")
                .addUserTask("Activity 1", "Employees")
                
                .addData("variable1", String.class.getName(), new ExpressionBuilder().createConstantStringExpression("defaultValue"))
                .addData("variable2", Long.class.getName(), new ExpressionBuilder().createConstantLongExpression(1))
                .addData("variable3", Date.class.getName(), new ExpressionBuilder().createConstantDateExpression("428558400000"))
                
                .addEndEvent("Finish");
        return new TestProcess(processDefinitionBuidler).addActor(getInitiator()).setEnable(true).startCase().getNextHumanTask().assignTo(getInitiator());
    }

    @Test
    public void api_can_update_activity_variables() throws Exception {
        TestHumanTask activity = createActivityWithVariables();
        Map<String, String> attributes = new HashMap<String, String>();
        attributes.put(ActivityItem.ATTRIBUTE_VARIABLES, JSON_UPDATE_VARIABLES);
        
        apiActivity.runUpdate(makeAPIID(activity.getId()), attributes);
        
        assertThat(activity.getDataInstance("variable1").getValue(), is((Serializable) "newValue"));
        assertThat(activity.getDataInstance("variable2").getValue(), is((Serializable) 9L));
        assertThat(activity.getDataInstance("variable3").getValue(), is((Serializable) new Date(349246800000L)));
    }
    
    @Test
    public void api_can_update_variables_and_terminate_activity() throws Exception {
        TestHumanTask activity = createActivityWithVariables();
        Map<String, String> attributes = new HashMap<String, String>();
        attributes.put(ActivityItem.ATTRIBUTE_VARIABLES, JSON_UPDATE_VARIABLES);
        attributes.put(ActivityItem.ATTRIBUTE_STATE, ActivityItem.VALUE_STATE_COMPLETED);
        
        apiActivity.runUpdate(makeAPIID(activity.getId()), attributes);
        
        ArchivedActivityInstance archivedActivityInstance = getArchivedDataInstance(activity);
        assertThat(archivedActivityInstance.getState(), is(ActivityItem.VALUE_STATE_COMPLETED));
        
        // Can't manage to do variable verification because of asynchronous engine update ...
//        assertThat(getArchivedDataInstanceValue("variable1", archivedActivityInstance), is((Serializable) "newValue"));
//        assertThat(getArchivedDataInstanceValue("variable2", archivedActivityInstance), is((Serializable) 9L));
//        assertThat(getArchivedDataInstanceValue("variable3", archivedActivityInstance), is((Serializable) new Date(349246800000L)));
    }

    
//    private Serializable getArchivedDataInstanceValue(String dataName, ArchivedActivityInstance archivedActivityInstance) throws Exception {
//        return getProcessAPI().getArchivedActivityDataInstance(dataName, archivedActivityInstance.getSourceObjectId()).getValue();
//    }

    /**
     * Activity state is updated asynchronously - need to wait... :-( 
     */
    private ArchivedActivityInstance getArchivedDataInstance(final TestHumanTask activity) throws Exception {
        if (new WaitUntil(50, 3000) {
            
            @Override
            protected boolean check() throws Exception {
                try {
                    ArchivedActivityInstance instance = getProcessAPI().getArchivedActivityInstance(activity.getId());
                    return ActivityItem.VALUE_STATE_COMPLETED.equals(instance.getState());
                } catch (ActivityInstanceNotFoundException e) {
                    return false;
                }
            }
        }.waitUntil()) {
            return getProcessAPI().getArchivedActivityInstance(activity.getId());
        } else {
            throw new Exception("can't get archived task");
        }
    }

    private ProcessAPI getProcessAPI() throws Exception {
        return TenantAPIAccessor.getProcessAPI(getInitiator().getSession());
    }
}
