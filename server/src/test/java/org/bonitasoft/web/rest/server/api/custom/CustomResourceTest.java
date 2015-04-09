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

import org.bonitasoft.console.common.server.page.RestApiRenderer;
import org.bonitasoft.engine.api.CommandAPI;
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
public class CustomResourceTest extends RestletTest {

    public static final String URL = "/custom/helloworld";

    public static final String JSON_RESPONSE = "{\"name\":\"Matti\"}";

    @Mock
    protected CommandAPI commandAPI;

    @Mock
    private RestApiRenderer restApiRenderer;

    @Override
    protected ServerResource configureResource() {
    	CustomResourceDescriptorImpl customResourceDescriptor = new CustomResourceDescriptorImpl("helloworld", "GET", "custompage_helloworld");
    	CustomResource resource = new CustomResource(customResourceDescriptor, restApiRenderer);
    	return resource;
    }

    @Test @Ignore
    public void should_call_custom_query() throws Exception {
        //then
        final Response response = request(URL).get();
        assertThat(response.getEntityAsText()).isEqualTo(JSON_RESPONSE);
        assertThat(response.getStatus()).isEqualTo(Status.SUCCESS_OK);
    }

}
