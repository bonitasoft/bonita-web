/**
 * Copyright (C) 2014 BonitaSoft S.A.
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
package org.bonitasoft.web.rest.server;

import java.util.logging.Level;

import org.bonitasoft.web.rest.server.api.bdm.BusinessDataQueryResource;
import org.bonitasoft.web.rest.server.api.bdm.BusinessDataReferenceResource;
import org.bonitasoft.web.rest.server.api.bdm.BusinessDataReferencesResource;
import org.bonitasoft.web.rest.server.api.bdm.BusinessDataResource;
import org.bonitasoft.web.rest.server.api.bpm.cases.CaseInfoResource;
import org.bonitasoft.web.rest.server.api.bpm.flownode.ActivityVariableResource;
import org.bonitasoft.web.rest.server.api.bpm.flownode.TaskContractResource;
import org.bonitasoft.web.rest.server.api.bpm.flownode.TaskExecutionResource;
import org.bonitasoft.web.rest.server.api.bpm.flownode.TimerEventTriggerResource;
import org.bonitasoft.web.rest.server.api.bpm.process.ProcessContractResource;
import org.bonitasoft.web.rest.server.api.bpm.process.ProcessInstanciationResource;
import org.bonitasoft.web.rest.server.api.form.FormMappingResource;
import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.CharacterSet;
import org.restlet.data.MediaType;
import org.restlet.engine.Engine;
import org.restlet.routing.Router;

/**
 *
 * @author Matthieu Chaffotte
 *
 */
public class BonitaRestletApplication extends Application {

    private final FinderFactory factory;

    public BonitaRestletApplication(final FinderFactory finderFactory) {
        super();
        factory = finderFactory;
        getMetadataService().setDefaultMediaType(MediaType.APPLICATION_JSON);
        getMetadataService().setDefaultCharacterSet(CharacterSet.UTF_8);
    }

    /**
     * Creates a root Restlet that will receive all incoming calls.
     */
    @Override
    public synchronized Restlet createInboundRoot() {
        return buildRouter();
    }

    protected Router buildRouter() {
        final Context context = getContext();
        final Router router = new Router(context);
        // WARNING: if you add a route you need to declare a static finder class in org.bonitasoft.web.rest.server.FinderFactory

        // GET an activityData:
        router.attach("/bpm/activityVariable/{" + ActivityVariableResource.ACTIVITYDATA_ACTIVITY_ID + "}/{" + ActivityVariableResource.ACTIVITYDATA_DATA_NAME
                + "}", factory.create(ActivityVariableResource.class));
        // GET to search timer event triggers:
        router.attach("/bpm/timerEventTrigger", factory.create(TimerEventTriggerResource.class));
        // PUT to update timer event trigger date:
        router.attach("/bpm/timerEventTrigger/{" + TimerEventTriggerResource.ID_PARAM_NAME + "}", factory.create(TimerEventTriggerResource.class));
        // GET to case info (with task state counter)
        router.attach("/bpm/caseInfo/{" + CaseInfoResource.CASE_ID + "}", factory.create(CaseInfoResource.class));
        // GET a task contract:
        router.attach("/bpm/tasks/{taskId}/contract", factory.create(TaskContractResource.class));
        // POST to execute a task contract:
        router.attach("/bpm/tasks/{taskId}/execution", factory.create(TaskExecutionResource.class));
        // GET a process contract:
        router.attach("/bpm/process/{processDefinitionId}/contract", factory.create(ProcessContractResource.class));
        // POST to execute a process contract:
        router.attach("/bpm/process/{processDefinitionId}/instanciation", factory.create(ProcessInstanciationResource.class));
        // GET to search form mappings:
        router.attach("/form/mapping", factory.create(FormMappingResource.class));
        // PUT to update form mapping:
        router.attach("/form/mapping/{" + FormMappingResource.ID_PARAM_NAME + "}", factory.create(FormMappingResource.class));
        //BDM
        router.attach("/bdm/businessData/{className}", factory.create(BusinessDataQueryResource.class));
        router.attach("/bdm/businessData/{className}/{id}", factory.create(BusinessDataResource.class));
        router.attach("/bdm/businessData/{className}/{id}/{fieldName}", factory.create(BusinessDataResource.class));
        router.attach("/bdm/businessDataReference", factory.create(BusinessDataReferencesResource.class));
        router.attach("/bdm/businessDataReference/{caseId}/{dataName}", factory.create(BusinessDataReferenceResource.class));

        return router;
    }

    @Override
    public void handle(final Request request, final Response response) {
        request.setLoggable(false);
        Engine.setLogLevel(Level.OFF);
        Engine.setRestletLogLevel(Level.OFF);
        // New Restlet APIs:
        super.handle(request, response);
    }

}
