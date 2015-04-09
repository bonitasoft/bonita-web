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

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.bonitasoft.web.rest.server.api.bdm.BusinessDataQueryResource;
import org.bonitasoft.web.rest.server.api.bdm.BusinessDataReferenceResource;
import org.bonitasoft.web.rest.server.api.bdm.BusinessDataReferenceResourceFinder;
import org.bonitasoft.web.rest.server.api.bdm.BusinessDataResource;
import org.bonitasoft.web.rest.server.api.bpm.cases.CaseInfoResource;
import org.bonitasoft.web.rest.server.api.bpm.flownode.ActivityVariableResource;
import org.bonitasoft.web.rest.server.api.bpm.flownode.TimerEventTriggerResource;
import org.bonitasoft.web.rest.server.api.bpm.flownode.UserTaskContextResource;
import org.bonitasoft.web.rest.server.api.bpm.flownode.UserTaskContractResource;
import org.bonitasoft.web.rest.server.api.bpm.flownode.UserTaskExecutionResource;
import org.bonitasoft.web.rest.server.api.bpm.process.ProcessContractResource;
import org.bonitasoft.web.rest.server.api.bpm.process.ProcessInstantiationResource;
import org.bonitasoft.web.rest.server.api.form.FormMappingResource;
import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.CharacterSet;
import org.restlet.data.MediaType;
import org.restlet.engine.Engine;
import org.restlet.resource.Finder;
import org.restlet.resource.ServerResource;
import org.restlet.routing.Router;

/**
 *
 * @author Matthieu Chaffotte
 *
 */
public class BonitaRestletApplication extends Application {

    public static final String BDM_BUSINESS_DATA_URL = "/bdm/businessData";
    public static final String BDM_BUSINESS_DATA_REFERENCE_URL = "/bdm/businessDataReference";
    public static final String FORM_MAPPING_URL = "/form/mapping";
    public static final String BPM_PROCESS_URL = "/bpm/process";
    public static final String BPM_USER_TASK_URL = "/bpm/userTask";
    public static final String BPM_TIMER_EVENT_TRIGGER_URL = "/bpm/timerEventTrigger";
    public static final String BPM_ACTIVITY_VARIABLE_URL = "/bpm/activityVariable";
    public static final String BPM_CASE_INFO_URL = "/bpm/caseInfo";

    public final static Map<String, String> class2urls = new HashMap<String, String>();
    static {
        class2urls.put("org.bonitasoft.engine.business.data.impl.SimpleBusinessDataReferenceImpl",BDM_BUSINESS_DATA_URL);
        class2urls.put("org.bonitasoft.engine.business.data.impl.MultipleBusinessDataReferenceImpl",BDM_BUSINESS_DATA_URL);
    }

    private final FinderFactory factory;
    private final ResourceHandler resourceHandler;

    public BonitaRestletApplication(final FinderFactory finderFactory) {
        super();
        factory = finderFactory;
        getMetadataService().setDefaultMediaType(MediaType.APPLICATION_JSON);
        getMetadataService().setDefaultCharacterSet(CharacterSet.UTF_8);
        resourceHandler = new ResourceHandler();
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
        router.attach(BPM_ACTIVITY_VARIABLE_URL + "/{" + ActivityVariableResource.ACTIVITYDATA_ACTIVITY_ID + "}/{" + ActivityVariableResource.ACTIVITYDATA_DATA_NAME
                + "}", createResource(ActivityVariableResource.class));
        // GET to search timer event triggers:
        router.attach(BPM_TIMER_EVENT_TRIGGER_URL, createResource(TimerEventTriggerResource.class));
        // PUT to update timer event trigger date:
        router.attach(BPM_TIMER_EVENT_TRIGGER_URL + "/{" + TimerEventTriggerResource.ID_PARAM_NAME + "}", createResource(TimerEventTriggerResource.class));
        // GET to case info (with task state counter)
        router.attach(BPM_CASE_INFO_URL + "/{" + CaseInfoResource.CASE_ID + "}", createResource(CaseInfoResource.class));
        // GET a task contract:
        router.attach(BPM_USER_TASK_URL + "/{taskId}/contract", createResource(UserTaskContractResource.class));
        // POST to execute a task with contract:
        router.attach(BPM_USER_TASK_URL + "/{taskId}/execution", createResource(UserTaskExecutionResource.class));
        // GET to retrieve a task context:
        router.attach(BPM_USER_TASK_URL + "/{taskId}/context", createResource(UserTaskContextResource.class));
        // GET a process contract:
        router.attach(BPM_PROCESS_URL + "/{processDefinitionId}/contract", createResource(ProcessContractResource.class));
        // POST to instantiate a process with contract:
        router.attach(BPM_PROCESS_URL + "/{processDefinitionId}/instantiation", createResource(ProcessInstantiationResource.class));
        // GET to search form mappings:
        router.attach(FORM_MAPPING_URL, createResource(FormMappingResource.class));
        // PUT to update form mapping:
        router.attach(FORM_MAPPING_URL + "/{" + FormMappingResource.ID_PARAM_NAME + "}", createResource(FormMappingResource.class));
        //BDM
        router.attach(BDM_BUSINESS_DATA_URL + "/{className}", createResource(BusinessDataQueryResource.class));
        router.attach(BDM_BUSINESS_DATA_URL + "/{className}/{id}", createResource(BusinessDataResource.class));
        router.attach(BDM_BUSINESS_DATA_URL + "/{className}/{id}/{fieldName}", createResource(BusinessDataResource.class));
        router.attach(BDM_BUSINESS_DATA_REFERENCE_URL, createResource(new BusinessDataReferenceResourceFinder()));
        router.attach(BDM_BUSINESS_DATA_REFERENCE_URL + "/{caseId}/{dataName}", createResource(BusinessDataReferenceResource.class));

        return router;
    }

    private Restlet createResource(ResourceFinder finder) {
         resourceHandler.addResource(finder);
        finder.setResourceHandler(resourceHandler);
        return finder;
    }

    private Finder createResource(Class<? extends ServerResource> clazz) {
        return factory.create(clazz);
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
