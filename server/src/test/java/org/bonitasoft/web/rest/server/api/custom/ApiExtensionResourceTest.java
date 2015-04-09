/**
 * ****************************************************************************
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 * *****************************************************************************
 */
package org.bonitasoft.web.rest.server.api.custom;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.bonitasoft.console.common.server.page.RestApiRenderer;
import org.bonitasoft.web.rest.server.BonitaRestletApplication;
import org.bonitasoft.web.rest.server.api.extension.ApiExtensionResource;
import org.bonitasoft.web.rest.server.api.extension.ResourceExtensionDescriptor;
import org.bonitasoft.web.rest.server.api.extension.ResourceExtensionDescriptorImpl;
import org.bonitasoft.web.rest.server.api.extension.TenantSpringBeanAccessor;
import org.bonitasoft.web.rest.server.utils.RestletTest;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.restlet.Response;
import org.restlet.data.Status;
import org.restlet.resource.ServerResource;

@RunWith(MockitoJUnitRunner.class)
public class ApiExtensionResourceTest extends RestletTest {

    public static final String URL_TEMPLATE = "helloworld";

    public static final String URL = BonitaRestletApplication.ROUTER_EXTENSION_PREFIX + URL_TEMPLATE;

    public static final String JSON_RESPONSE = "{\"name\":\"Matti\"}";

    public static final String PAGE_NAME = "custompage_helloworld";


    @Mock
    private RestApiRenderer restApiRenderer;

    @Mock
    TenantSpringBeanAccessor tenantSpringBeanAccessor;


    @Override
    protected TenantSpringBeanAccessor configureTenantSpringBeanAccessor() {
        when(tenantSpringBeanAccessor.getResourceExtensionConfiguration()).thenReturn(Arrays.asList(getResourceExtensionDescriptor()));
        return tenantSpringBeanAccessor;
    }

    @Override
    protected ServerResource configureResource() {
        return new ApiExtensionResource(getResourceExtensionDescriptor(), restApiRenderer);
    }

    @Test
    @Ignore
    public void should_call_custom_query() throws Exception {

        //when
        final Response response = request(URL).get();

        //then
        assertThat(response.getEntityAsText()).isEqualTo(JSON_RESPONSE);
        assertThat(response.getStatus()).isEqualTo(Status.SUCCESS_OK);

    }

    public ResourceExtensionDescriptor getResourceExtensionDescriptor() {
        return new ResourceExtensionDescriptorImpl(URL_TEMPLATE, "GET", PAGE_NAME);

    }
}
