/**
 * ****************************************************************************
 * Copyright (C) 2009, 2013 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 * *****************************************************************************
 */
package org.bonitasoft.web.rest.server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bonitasoft.web.rest.server.api.bdm.BusinessDataFindByIdsResource;
import org.bonitasoft.web.rest.server.api.bdm.BusinessDataFindByIdsResourceFinder;
import org.bonitasoft.web.rest.server.api.bdm.BusinessDataQueryResource;
import org.bonitasoft.web.rest.server.api.bdm.BusinessDataQueryResourceFinder;
import org.bonitasoft.web.rest.server.api.bdm.BusinessDataReferenceResource;
import org.bonitasoft.web.rest.server.api.bdm.BusinessDataReferenceResourceFinder;
import org.bonitasoft.web.rest.server.api.bdm.BusinessDataReferencesResource;
import org.bonitasoft.web.rest.server.api.bdm.BusinessDataReferencesResourceFinder;
import org.bonitasoft.web.rest.server.api.bdm.BusinessDataResource;
import org.bonitasoft.web.rest.server.api.bdm.BusinessDataResourceFinder;
import org.bonitasoft.web.rest.server.api.bpm.cases.ArchivedCaseContextResource;
import org.bonitasoft.web.rest.server.api.bpm.cases.ArchivedCaseContextResourceFinder;
import org.bonitasoft.web.rest.server.api.bpm.cases.CaseContextResource;
import org.bonitasoft.web.rest.server.api.bpm.cases.CaseContextResourceFinder;
import org.bonitasoft.web.rest.server.api.bpm.cases.CaseInfoResource;
import org.bonitasoft.web.rest.server.api.bpm.cases.CaseInfoResourceFinder;
import org.bonitasoft.web.rest.server.api.bpm.flownode.ActivityVariableResource;
import org.bonitasoft.web.rest.server.api.bpm.flownode.ActivityVariableResourceFinder;
import org.bonitasoft.web.rest.server.api.bpm.flownode.TimerEventTriggerResource;
import org.bonitasoft.web.rest.server.api.bpm.flownode.TimerEventTriggerResourceFinder;
import org.bonitasoft.web.rest.server.api.bpm.flownode.UserTaskContextResource;
import org.bonitasoft.web.rest.server.api.bpm.flownode.UserTaskContextResourceFinder;
import org.bonitasoft.web.rest.server.api.bpm.flownode.UserTaskContractResource;
import org.bonitasoft.web.rest.server.api.bpm.flownode.UserTaskContractResourceFinder;
import org.bonitasoft.web.rest.server.api.bpm.flownode.UserTaskExecutionResource;
import org.bonitasoft.web.rest.server.api.bpm.flownode.UserTaskExecutionResourceFinder;
import org.bonitasoft.web.rest.server.api.bpm.flownode.archive.ArchivedUserTaskContextResource;
import org.bonitasoft.web.rest.server.api.bpm.flownode.archive.ArchivedUserTaskContextResourceFinder;
import org.bonitasoft.web.rest.server.api.bpm.process.ProcessContractResource;
import org.bonitasoft.web.rest.server.api.bpm.process.ProcessContractResourceFinder;
import org.bonitasoft.web.rest.server.api.bpm.process.ProcessDefinitionDesignResource;
import org.bonitasoft.web.rest.server.api.bpm.process.ProcessDefinitionDesignResourceFinder;
import org.bonitasoft.web.rest.server.api.bpm.process.ProcessInstantiationResource;
import org.bonitasoft.web.rest.server.api.bpm.process.ProcessInstantiationResourceFinder;
import org.bonitasoft.web.rest.server.api.form.FormMappingResource;
import org.bonitasoft.web.rest.server.api.form.FormMappingResourceFinder;
import org.bonitasoft.web.rest.server.api.system.I18nTanslationResource;
import org.bonitasoft.web.rest.server.api.system.I18nTanslationResourceFinder;
import org.restlet.resource.Finder;
import org.restlet.resource.ServerResource;

public class FinderFactory {

    protected Map<Class<? extends ServerResource>, ResourceFinder> finders;
    List<ResourceFinder> resourceFinders = new ArrayList<>();

    public FinderFactory() {
        finders = getDefaultFinders();
        createResourceFinderList(finders);
    }

    public FinderFactory(final Map<Class<? extends ServerResource>, ResourceFinder> finders) {
        this.finders = finders;
        createResourceFinderList(finders);

    }

    private void createResourceFinderList(final Map<Class<? extends ServerResource>, ResourceFinder> finders) {
        for (final Map.Entry<Class<? extends ServerResource>, ResourceFinder> classFinderEntry : finders.entrySet()) {
            final ResourceFinder resourceFinder = classFinderEntry.getValue();
            resourceFinders.add(resourceFinder);
            resourceFinder.setFinderFactory(this);
        }
    }

    protected Map<Class<? extends ServerResource>, ResourceFinder> getDefaultFinders() {
        final Map<Class<? extends ServerResource>, ResourceFinder> finders = new HashMap<>();
        finders.put(ActivityVariableResource.class, new ActivityVariableResourceFinder());
        finders.put(TimerEventTriggerResource.class, new TimerEventTriggerResourceFinder());
        finders.put(CaseInfoResource.class, new CaseInfoResourceFinder());
        finders.put(CaseContextResource.class, new CaseContextResourceFinder());
        finders.put(ArchivedCaseContextResource.class, new ArchivedCaseContextResourceFinder());
        finders.put(BusinessDataResource.class, new BusinessDataResourceFinder());
        finders.put(BusinessDataReferenceResource.class, new BusinessDataReferenceResourceFinder());
        finders.put(BusinessDataFindByIdsResource.class, new BusinessDataFindByIdsResourceFinder());
        finders.put(BusinessDataReferencesResource.class, new BusinessDataReferencesResourceFinder());
        finders.put(BusinessDataQueryResource.class, new BusinessDataQueryResourceFinder());
        finders.put(FormMappingResource.class, new FormMappingResourceFinder());
        finders.put(UserTaskContractResource.class, new UserTaskContractResourceFinder());
        finders.put(UserTaskExecutionResource.class, new UserTaskExecutionResourceFinder());
        finders.put(UserTaskContextResource.class, new UserTaskContextResourceFinder());
        finders.put(ArchivedUserTaskContextResource.class, new ArchivedUserTaskContextResourceFinder());
        finders.put(ProcessContractResource.class, new ProcessContractResourceFinder());
        finders.put(ProcessDefinitionDesignResource.class, new ProcessDefinitionDesignResourceFinder());
        finders.put(ProcessInstantiationResource.class, new ProcessInstantiationResourceFinder());
        finders.put(I18nTanslationResource.class, new I18nTanslationResourceFinder());

        return finders;
    }

    public Finder create(final Class<? extends ServerResource> clazz) {
        final Finder finder = finders.get(clazz);
        if (finder == null) {
            throw new RuntimeException("Finder unimplemented for class " + clazz);
        }
        return finder;
    }

    public Finder createExtensionResource() {
        return new ApiExtensionResourceFinder();
    }

    public ResourceFinder getResourceFinderFor(final Serializable object) {
        for (final ResourceFinder resourceFinder : resourceFinders) {
            if (resourceFinder.handlesResource(object)) {
                return resourceFinder;
            }

        }
        return null;
    }

    public Serializable getContextResultElement(final Serializable object) {
        final ResourceFinder resourceFinderFor = getResourceFinderFor(object);
        if (resourceFinderFor != null) {
            return resourceFinderFor.toClientObject(object);
        }
        return object;
    }

}
