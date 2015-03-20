/*******************************************************************************
 * Copyright (C) 2009, 2013 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.web.rest.server;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.bonitasoft.engine.api.BusinessDataAPI;
import org.bonitasoft.engine.api.CommandAPI;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.ProcessConfigurationAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.session.APISession;
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
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.ext.servlet.ServletUtils;
import org.restlet.resource.Finder;
import org.restlet.resource.ServerResource;

public class FinderFactory {

    protected static Map<Class<? extends ServerResource>, Finder> finders;
    static {
        finders = new HashMap<Class<? extends ServerResource>, Finder>();
        finders.put(ActivityVariableResource.class, new ActivityVariableResourceFinder());
        finders.put(TimerEventTriggerResource.class, new TimerEventTriggerResourceFinder());
        finders.put(CaseInfoResource.class, new CaseInfoResourceFinder());
        finders.put(BusinessDataResource.class, new BusinessDataResourceFinder());
        finders.put(BusinessDataReferenceResource.class, new BusinessDataReferenceResourceFinder());
        finders.put(BusinessDataReferencesResource.class, new BusinessDataReferencesResourceFinder());
        finders.put(BusinessDataQueryResource.class, new BusinessDataQueryResourceFinder());
        finders.put(FormMappingResource.class, new FormMappingResourceFinder());
        finders.put(TaskContractResource.class, new TaskContractResourceFinder());
        finders.put(TaskExecutionResource.class, new TaskExecutionResourceFinder());
        finders.put(ProcessContractResource.class, new ProcessContractResourceFinder());
        finders.put(ProcessInstanciationResource.class, new ProcessInstanciationResourceFinder());
    }

    public Finder create(final Class<? extends ServerResource> clazz) {
        final Finder finder = finders.get(clazz);
        if (finder == null) {
            throw new RuntimeException("Finder unimplemented for class " + clazz);
        }
        return finder;
    }

    public abstract static class AbstractResourceFinder extends Finder {

        protected CommandAPI getCommandAPI(final Request request) {
            final APISession apiSession = getAPISession(request);
            try {
                return TenantAPIAccessor.getCommandAPI(apiSession);
            } catch (final Exception e) {
                throw new APIException(e);
            }
        }

        protected ProcessAPI getProcessAPI(final Request request) {
            final APISession apiSession = getAPISession(request);
            try {
                return TenantAPIAccessor.getProcessAPI(apiSession);
            } catch (final Exception e) {
                throw new APIException(e);
            }
        }

        protected ProcessConfigurationAPI getProcessConfigurationAPI(final Request request) {
            final APISession apiSession = getAPISession(request);
            try {
                return TenantAPIAccessor.getProcessConfigurationAPI(apiSession);
            } catch (final Exception e) {
                throw new APIException(e);
            }
        }

        protected BusinessDataAPI getBdmAPI(final Request request) {
            final APISession apiSession = getAPISession(request);
            try {
                return TenantAPIAccessor.getBusinessDataAPI(apiSession);
            } catch (final Exception e) {
                throw new APIException(e);
            }
        }

        protected APISession getAPISession(final Request request) {
            final HttpSession httpSession = ServletUtils.getRequest(request).getSession();
            return (APISession) httpSession.getAttribute("apiSession");
        }
    }

    public static class ActivityVariableResourceFinder extends AbstractResourceFinder {

        @Override
        public ServerResource create(final Request request, final Response response) {
            final ProcessAPI processAPI = getProcessAPI(request);
            return new ActivityVariableResource(processAPI);
        }
    }

    public static class TimerEventTriggerResourceFinder extends AbstractResourceFinder {

        @Override
        public ServerResource create(final Request request, final Response response) {
            final ProcessAPI processAPI = getProcessAPI(request);
            return new TimerEventTriggerResource(processAPI);
        }
    }

    public static class CaseInfoResourceFinder extends AbstractResourceFinder {

        @Override
        public ServerResource create(final Request request, final Response response) {
            final ProcessAPI processAPI = getProcessAPI(request);
            return new CaseInfoResource(processAPI);
        }
    }

    public static class BusinessDataReferenceResourceFinder extends AbstractResourceFinder {

        @Override
        public ServerResource create(final Request request, final Response response) {
            final BusinessDataAPI bdmAPI = getBdmAPI(request);
            return new BusinessDataReferenceResource(bdmAPI);
        }
    }

    public static class BusinessDataQueryResourceFinder extends AbstractResourceFinder {

        @Override
        public ServerResource create(final Request request, final Response response) {
            return new BusinessDataQueryResource(getCommandAPI(request));
        }
    }

    public static class BusinessDataReferencesResourceFinder extends AbstractResourceFinder {

        @Override
        public ServerResource create(final Request request, final Response response) {
            final BusinessDataAPI processAPI = getBdmAPI(request);
            return new BusinessDataReferencesResource(processAPI);
        }
    }

    public static class BusinessDataResourceFinder extends AbstractResourceFinder {

        @Override
        public ServerResource create(final Request request, final Response response) {
            final CommandAPI commandAPI = getCommandAPI(request);
            return new BusinessDataResource(commandAPI);
        }
    }

    public static class TaskContractResourceFinder extends AbstractResourceFinder {

        @Override
        public ServerResource create(final Request request, final Response response) {
            final ProcessAPI processAPI = getProcessAPI(request);
            return new TaskContractResource(processAPI);
        }
    }

    public static class TaskExecutionResourceFinder extends AbstractResourceFinder {

        @Override
        public ServerResource create(final Request request, final Response response) {
            final ProcessAPI processAPI = getProcessAPI(request);
            return new TaskExecutionResource(processAPI);
        }
    }


    public static class ProcessContractResourceFinder extends AbstractResourceFinder {

        @Override
        public ServerResource create(final Request request, final Response response) {
            final ProcessAPI processAPI = getProcessAPI(request);
            return new ProcessContractResource(processAPI);
        }
    }

    public static class ProcessInstanciationResourceFinder extends AbstractResourceFinder {

        @Override
        public ServerResource create(final Request request, final Response response) {
            final ProcessAPI processAPI = getProcessAPI(request);
            return new ProcessInstanciationResource(processAPI);
        }
    }


    public static class FormMappingResourceFinder extends AbstractResourceFinder {

        @Override
        public ServerResource create(final Request request, final Response response) {
            final ProcessConfigurationAPI processConfigurationAPI = getProcessConfigurationAPI(request);
            return new FormMappingResource(processConfigurationAPI);
        }
    }



}
