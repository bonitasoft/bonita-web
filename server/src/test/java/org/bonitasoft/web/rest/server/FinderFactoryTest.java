/*******************************************************************************
 * Copyright (C) 2009, 2013 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.web.rest.server;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import java.io.Serializable;
import java.util.Collections;

import org.bonitasoft.engine.api.BusinessDataAPI;
import org.bonitasoft.engine.api.CommandAPI;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.server.api.bdm.BusinessDataQueryResource;
import org.bonitasoft.web.rest.server.api.bdm.BusinessDataQueryResourceFinder;
import org.bonitasoft.web.rest.server.api.bdm.BusinessDataReferenceResource;
import org.bonitasoft.web.rest.server.api.bdm.BusinessDataReferenceResourceFinder;
import org.bonitasoft.web.rest.server.api.bdm.BusinessDataReferencesResource;
import org.bonitasoft.web.rest.server.api.bdm.BusinessDataReferencesResourceFinder;
import org.bonitasoft.web.rest.server.api.bdm.BusinessDataResource;
import org.bonitasoft.web.rest.server.api.bdm.BusinessDataResourceFinder;
import org.bonitasoft.web.rest.server.api.bpm.cases.CaseInfoResource;
import org.bonitasoft.web.rest.server.api.bpm.cases.CaseInfoResourceFinder;
import org.bonitasoft.web.rest.server.api.bpm.flownode.ActivityVariableResource;
import org.bonitasoft.web.rest.server.api.bpm.flownode.ActivityVariableResourceFinder;
import org.bonitasoft.web.rest.server.api.bpm.flownode.TimerEventTriggerResource;
import org.bonitasoft.web.rest.server.api.bpm.flownode.TimerEventTriggerResourceFinder;
import org.bonitasoft.web.rest.server.api.bpm.flownode.UserTaskContractResource;
import org.bonitasoft.web.rest.server.api.bpm.flownode.UserTaskContractResourceFinder;
import org.bonitasoft.web.rest.server.api.bpm.flownode.UserTaskExecutionResource;
import org.bonitasoft.web.rest.server.api.bpm.flownode.UserTaskExecutionResourceFinder;
import org.bonitasoft.web.rest.server.api.bpm.process.ProcessContractResource;
import org.bonitasoft.web.rest.server.api.bpm.process.ProcessContractResourceFinder;
import org.bonitasoft.web.rest.server.api.bpm.process.ProcessDefinitionDesignResource;
import org.bonitasoft.web.rest.server.api.bpm.process.ProcessDefinitionDesignResourceFinder;
import org.bonitasoft.web.rest.server.api.bpm.process.ProcessInstantiationResource;
import org.bonitasoft.web.rest.server.api.bpm.process.ProcessInstantiationResourceFinder;
import org.bonitasoft.web.rest.server.api.form.FormMappingResource;
import org.bonitasoft.web.rest.server.api.form.FormMappingResourceFinder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.resource.Finder;
import org.restlet.resource.ServerResource;

@RunWith(MockitoJUnitRunner.class)
public class FinderFactoryTest {

    private FinderFactory factory;

    @Mock
    private ProcessAPI processAPI;

    @Mock
    private BusinessDataAPI bdmAPI;

    @Mock
    private CommandAPI commandAPI;

    @Mock
    private APISession apiSession;

    private final Request request = new Request();
    private final Response response = new Response(request);

    @Before
    public void setUp() {
        factory = new FinderFactory();
    }

    @Test
    public void should_return_BusinessDataQueryFinder_for_BusinessDataQuery() throws Exception {

        final Finder finder = factory.create(BusinessDataQueryResource.class);

        assertThat(finder).isInstanceOf(BusinessDataQueryResourceFinder.class);
    }

    @Test
    public void should_return_BusinessDataResourceFinder_for_BusinessDataResource() throws Exception {

        final Finder finder = factory.create(BusinessDataResource.class);

        assertThat(finder).isInstanceOf(BusinessDataResourceFinder.class);
    }

    @Test
    public void should_return_BusinessDataReferenceResourceFinder_for_BusinessDataReferenceResource() throws Exception {

        final Finder finder = factory.create(BusinessDataReferenceResource.class);

        assertThat(finder).isInstanceOf(BusinessDataReferenceResourceFinder.class);
    }

    @Test
    public void should_return_BusinessDataReferencesResourceFinder_for_BusinessDataReferencesResource() throws Exception {

        final Finder finder = factory.create(BusinessDataReferencesResource.class);

        assertThat(finder).isInstanceOf(BusinessDataReferencesResourceFinder.class);
    }

    @Test(expected = RuntimeException.class)
    public void should_throw_RuntimeException_for_a_not_supported_class() throws Exception {

        factory.create(NotSupportedResource.class);
    }

    private class NotSupportedResource extends ServerResource {

    }

    @Test
    public void should_return_ProcessInstanciationResource_for_ProcessInstanciationResourceFinder() {
        final ProcessInstantiationResourceFinder processInstanciationResourceFinder = spy(new ProcessInstantiationResourceFinder());
        doReturn(processAPI).when(processInstanciationResourceFinder).getProcessAPI(any(Request.class));
        doReturn(apiSession).when(processInstanciationResourceFinder).getAPISession(any(Request.class));
        final ServerResource serverResource = processInstanciationResourceFinder.create(request, response);
        assertThat(serverResource).isInstanceOf(ProcessInstantiationResource.class);
    }

    @Test
    public void should_return_ActivityVariableResource_for_ActivityVariableResourceFinder() {
        final ActivityVariableResourceFinder activityVariableResourceFinder = spy(new ActivityVariableResourceFinder());
        doReturn(processAPI).when(activityVariableResourceFinder).getProcessAPI(any(Request.class));
        final ServerResource serverResource = activityVariableResourceFinder.create(request, response);
        assertThat(serverResource).isInstanceOf(ActivityVariableResource.class);
    }

    @Test
    public void should_return_TimerEventTriggerResource_for_TimerEventTriggerResourceFinder() {
        final TimerEventTriggerResourceFinder timerEventTriggerResourceFinder = spy(new TimerEventTriggerResourceFinder());
        doReturn(processAPI).when(timerEventTriggerResourceFinder).getProcessAPI(any(Request.class));
        final ServerResource serverResource = timerEventTriggerResourceFinder.create(request, response);
        assertThat(serverResource).isInstanceOf(TimerEventTriggerResource.class);
    }

    @Test
    public void should_return_CaseInfoResource_for_CaseInfoResourceFinder() {
        final CaseInfoResourceFinder caseInfoResourceFinder = spy(new CaseInfoResourceFinder());
        doReturn(processAPI).when(caseInfoResourceFinder).getProcessAPI(any(Request.class));
        final ServerResource serverResource = caseInfoResourceFinder.create(request, response);
        assertThat(serverResource).isInstanceOf(CaseInfoResource.class);
    }

    @Test
    public void should_return_BusinessDataReferenceResource_for_BusinessDataReferenceResourceFinder() {
        final BusinessDataReferenceResourceFinder businessDataReferenceResourceFinder = spy(new BusinessDataReferenceResourceFinder());
        doReturn(bdmAPI).when(businessDataReferenceResourceFinder).getBdmAPI(any(Request.class));
        final ServerResource serverResource = businessDataReferenceResourceFinder.create(request, response);
        assertThat(serverResource).isInstanceOf(BusinessDataReferenceResource.class);
    }

    @Test
    public void should_return_BusinessDataQueryResource_for_BusinessDataQueryResourceFinder() {
        final BusinessDataQueryResourceFinder businessDataQueryResourceFinder = spy(new BusinessDataQueryResourceFinder());
        doReturn(commandAPI).when(businessDataQueryResourceFinder).getCommandAPI(any(Request.class));
        final ServerResource serverResource = businessDataQueryResourceFinder.create(request, response);
        assertThat(serverResource).isInstanceOf(BusinessDataQueryResource.class);
    }

    @Test
    public void should_return_BusinessDataReferencesResource_for_BusinessDataReferencesResourceFinder() {
        final BusinessDataReferencesResourceFinder businessDataReferencesResourceFinder = spy(new BusinessDataReferencesResourceFinder());
        doReturn(bdmAPI).when(businessDataReferencesResourceFinder).getBdmAPI(any(Request.class));
        final ServerResource serverResource = businessDataReferencesResourceFinder.create(request, response);
        assertThat(serverResource).isInstanceOf(BusinessDataReferencesResource.class);
    }

    @Test
    public void should_return_BusinessDataResource_for_BusinessDataResourceFinder() {
        final BusinessDataResourceFinder businessDataResourceFinder = spy(new BusinessDataResourceFinder());
        doReturn(commandAPI).when(businessDataResourceFinder).getCommandAPI(any(Request.class));
        final ServerResource serverResource = businessDataResourceFinder.create(request, response);
        assertThat(serverResource).isInstanceOf(BusinessDataResource.class);
    }

    @Test
    public void should_return_TaskContractResource_for_TaskContractResourceFinder() {
        final UserTaskContractResourceFinder userTaskContractResourceFinder = spy(new UserTaskContractResourceFinder());
        doReturn(processAPI).when(userTaskContractResourceFinder).getProcessAPI(any(Request.class));
        final ServerResource serverResource = userTaskContractResourceFinder.create(request, response);
        assertThat(serverResource).isInstanceOf(UserTaskContractResource.class);
    }

    @Test
    public void should_return_TaskExecutionResource_for_TaskExecutionResourceFinder() {
        final UserTaskExecutionResourceFinder userTaskExecutionResourceFinder = spy(new UserTaskExecutionResourceFinder());
        doReturn(processAPI).when(userTaskExecutionResourceFinder).getProcessAPI(any(Request.class));
        doReturn(apiSession).when(userTaskExecutionResourceFinder).getAPISession(any(Request.class));
        final ServerResource serverResource = userTaskExecutionResourceFinder.create(request, response);
        assertThat(serverResource).isInstanceOf(UserTaskExecutionResource.class);
    }

    @Test
    public void should_return_ProcessDefinitionDesignResource_for_ProcessDefinitionDesignResourceFinder() {
        final ProcessDefinitionDesignResourceFinder processDefinitionDesignResourceFinder = spy(new ProcessDefinitionDesignResourceFinder());
        doReturn(processAPI).when(processDefinitionDesignResourceFinder).getProcessAPI(any(Request.class));
        final ServerResource serverResource = processDefinitionDesignResourceFinder.create(request, response);
        assertThat(serverResource).isInstanceOf(ProcessDefinitionDesignResource.class);
    }

    @Test
    public void should_return_ProcessContractResource_for_ProcessContractResourceFinder() {
        final ProcessContractResourceFinder processContractResourceFinder = spy(new ProcessContractResourceFinder());
        doReturn(processAPI).when(processContractResourceFinder).getProcessAPI(any(Request.class));
        final ServerResource serverResource = processContractResourceFinder.create(request, response);
        assertThat(serverResource).isInstanceOf(ProcessContractResource.class);
    }

    @Test
    public void should_return_FormMappingResource_for_FormMappingResourceFinder() {
        final FormMappingResourceFinder formMappingResourceFinder = spy(new FormMappingResourceFinder());
        doReturn(processAPI).when(formMappingResourceFinder).getProcessAPI(any(Request.class));
        final ServerResource serverResource = formMappingResourceFinder.create(request, response);
        assertThat(serverResource).isInstanceOf(FormMappingResource.class);
    }

    @Test
    public void should_getResourceFinderFor_return_result_of_first_handler(){
        final FinderFactory finderFactory = new FinderFactory(Collections.<Class<? extends ServerResource>,ResourceFinder>singletonMap(null, new ResourceFinder() {
            @Override
            public Serializable toClientObject(final Serializable object) {
                return "resultA";
            }

            @Override
            public boolean handlesResource(final Serializable object) {
                return object.equals("objectA");
            }
        }));

        final Serializable objectA = finderFactory.getContextResultElement("objectA");

        assertThat(objectA).isEqualTo("resultA");
    }

    @Test
    public void should_getResourceFinderFor_return_the_object_if_no_handler(){
        final FinderFactory finderFactory = new FinderFactory(Collections.<Class<? extends ServerResource>,ResourceFinder>singletonMap(null, new ResourceFinder() {
            @Override
            public Serializable toClientObject(final Serializable object) {
                return "resultA";
            }

            @Override
            public boolean handlesResource(final Serializable object) {
                return object.equals("objectB");
            }
        }));

        final Serializable objectA = finderFactory.getContextResultElement("objectA");

        assertThat(objectA).isEqualTo("objectA");
    }

}
