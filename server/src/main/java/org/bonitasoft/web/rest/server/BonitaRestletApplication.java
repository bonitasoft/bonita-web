/**
 * Copyright (C) 2014, 2015 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.web.rest.server;

import java.util.logging.Level;

import org.bonitasoft.web.rest.server.api.bdm.BusinessDataQueryResource;
import org.bonitasoft.web.rest.server.api.bdm.BusinessDataReferenceResource;
import org.bonitasoft.web.rest.server.api.bdm.BusinessDataReferencesResource;
import org.bonitasoft.web.rest.server.api.bdm.BusinessDataResource;
import org.bonitasoft.web.rest.server.api.bpm.cases.ArchivedCaseContextResource;
import org.bonitasoft.web.rest.server.api.bpm.cases.CaseContextResource;
import org.bonitasoft.web.rest.server.api.bpm.cases.CaseInfoResource;
import org.bonitasoft.web.rest.server.api.bpm.flownode.ActivityVariableResource;
import org.bonitasoft.web.rest.server.api.bpm.flownode.TimerEventTriggerResource;
import org.bonitasoft.web.rest.server.api.bpm.flownode.UserTaskContextResource;
import org.bonitasoft.web.rest.server.api.bpm.flownode.UserTaskContractResource;
import org.bonitasoft.web.rest.server.api.bpm.flownode.UserTaskExecutionResource;
import org.bonitasoft.web.rest.server.api.bpm.flownode.archive.ArchivedUserTaskContextResource;
import org.bonitasoft.web.rest.server.api.bpm.process.ProcessContractResource;
import org.bonitasoft.web.rest.server.api.bpm.process.ProcessDefinitionDesignResource;
import org.bonitasoft.web.rest.server.api.bpm.process.ProcessInstantiationResource;
import org.bonitasoft.web.rest.server.api.extension.ResourceExtensionDescriptor;
import org.bonitasoft.web.rest.server.api.extension.TenantSpringBeanAccessor;
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

    public static final String ROUTER_EXTENSION_PREFIX = "/extension/";

    public static final String BDM_BUSINESS_DATA_URL = "/bdm/businessData";

    public static final String BDM_BUSINESS_DATA_REFERENCE_URL = "/bdm/businessDataReference";

    public static final String FORM_MAPPING_URL = "/form/mapping";

    public static final String BPM_PROCESS_URL = "/bpm/process";

    public static final String BPM_USER_TASK_URL = "/bpm/userTask";

    public static final String BPM_ARCHIVED_USER_TASK_URL = "/bpm/archivedUserTask";

    public static final String BPM_TIMER_EVENT_TRIGGER_URL = "/bpm/timerEventTrigger";

    public static final String BPM_ACTIVITY_VARIABLE_URL = "/bpm/activityVariable";

    public static final String BPM_CASE_INFO_URL = "/bpm/caseInfo";

    public static final String BPM_CASE_CONTEXT = "/bpm/case";

    private static final String BPM_ARCHIVED_CASE_CONTEXT = "/bpm/archivedCase";

    private final FinderFactory factory;

    private final TenantSpringBeanAccessor beanAccessor;

    public BonitaRestletApplication(final FinderFactory finderFactory, final TenantSpringBeanAccessor tenantSpringBeanAccessor) {
        super();
        factory = finderFactory;
        beanAccessor = tenantSpringBeanAccessor;
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
        // WARNING: if you add a route you need to declare it in org.bonitasoft.web.rest.server.FinderFactory

        // GET an activityData:
        router.attach(BPM_ACTIVITY_VARIABLE_URL + "/{" + ActivityVariableResource.ACTIVITYDATA_ACTIVITY_ID + "}/{"
                + ActivityVariableResource.ACTIVITYDATA_DATA_NAME
                + "}", factory.create(ActivityVariableResource.class));

        // GET to search timer event triggers:
        router.attach(BPM_TIMER_EVENT_TRIGGER_URL, factory.create(TimerEventTriggerResource.class));
        // PUT to update timer event trigger date:
        router.attach(BPM_TIMER_EVENT_TRIGGER_URL + "/{" + TimerEventTriggerResource.ID_PARAM_NAME + "}", factory.create(TimerEventTriggerResource.class));

        // GET to case info (with task state counter)
        router.attach(BPM_CASE_INFO_URL + "/{" + CaseInfoResource.CASE_ID + "}", factory.create(CaseInfoResource.class));

        // GET to retrieve a case context:
        router.attach(BPM_CASE_CONTEXT + "/{caseId}/context", factory.create(CaseContextResource.class));

        // GET to retrieve an archived case context
        router.attach(BPM_ARCHIVED_CASE_CONTEXT + "/{archivedCaseId}/context", factory.create(ArchivedCaseContextResource.class));

        // GET a task contract:
        router.attach(BPM_USER_TASK_URL + "/{taskId}/contract", factory.create(UserTaskContractResource.class));
        // POST to execute a task with contract:
        router.attach(BPM_USER_TASK_URL + "/{taskId}/execution", factory.create(UserTaskExecutionResource.class));
        // GET to retrieve a task context:
        router.attach(BPM_USER_TASK_URL + "/{taskId}/context", factory.create(UserTaskContextResource.class));

        // GET an archived task context:
        router.attach(BPM_ARCHIVED_USER_TASK_URL + "/{archivedTaskId}/context", factory.create(ArchivedUserTaskContextResource.class));

        // GET a process defintion design :
        router.attach(BPM_PROCESS_URL + "/{processDefinitionId}/design", factory.create(ProcessDefinitionDesignResource.class));
        // GET a process contract:
        router.attach(BPM_PROCESS_URL + "/{processDefinitionId}/contract", factory.create(ProcessContractResource.class));
        // POST to instantiate a process with contract:
        router.attach(BPM_PROCESS_URL + "/{processDefinitionId}/instantiation", factory.create(ProcessInstantiationResource.class));

        // GET to search form mappings:
        router.attach(FORM_MAPPING_URL, factory.create(FormMappingResource.class));

        // GET a BusinessData
        router.attach(BDM_BUSINESS_DATA_URL + "/{className}", factory.create(BusinessDataQueryResource.class));
        router.attach(BDM_BUSINESS_DATA_URL + "/{className}/{id}", factory.create(BusinessDataResource.class));
        router.attach(BDM_BUSINESS_DATA_URL + "/{className}/{id}/{fieldName}", factory.create(BusinessDataResource.class));

        // GET a Multiple BusinessDataReference
        router.attach(BDM_BUSINESS_DATA_REFERENCE_URL, factory.create(BusinessDataReferencesResource.class));
        // GET a Simple BusinessDataReference
        router.attach(BDM_BUSINESS_DATA_REFERENCE_URL + "/{caseId}/{dataName}", factory.create(BusinessDataReferenceResource.class));

        buildRouterExtension(router);
        return router;
    }

    private void buildRouterExtension(final Router router) {
        for (final ResourceExtensionDescriptor resourceExtensionDescriptor : beanAccessor.getResourceExtensionConfiguration()) {
            router.attach(ROUTER_EXTENSION_PREFIX + resourceExtensionDescriptor.getPathTemplate(), factory.createExtensionResource(resourceExtensionDescriptor));
        }
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
