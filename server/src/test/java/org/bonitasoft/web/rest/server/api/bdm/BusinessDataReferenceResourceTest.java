/*******************************************************************************
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.web.rest.server.api.bdm;

import static org.bonitasoft.web.rest.server.utils.ResponseAssert.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.engine.api.BusinessDataAPI;
import org.bonitasoft.engine.bpm.data.DataNotFoundException;
import org.bonitasoft.engine.business.data.MultipleBusinessDataReference;
import org.bonitasoft.engine.business.data.SimpleBusinessDataReference;
import org.bonitasoft.web.rest.server.api.bdm.BusinessDataReferenceResource;
import org.bonitasoft.web.rest.server.utils.RestletTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.restlet.Response;
import org.restlet.data.Status;
import org.restlet.resource.ServerResource;

@RunWith(MockitoJUnitRunner.class)
public class BusinessDataReferenceResourceTest extends RestletTest {

    @Mock
    private BusinessDataAPI bdmAPI;

    @Override
    protected ServerResource configureResource() {
        return new BusinessDataReferenceResource(bdmAPI);
    }

    private SimpleBusinessDataReference buildSimpleEmployeeReference(final String name, final long businessDataId) {
        return new org.bonitasoft.engine.business.data.impl.SimpleBusinessDataReferenceImpl(name,
                "com.bonitasoft.pojo.Employee", businessDataId);
    }

    private MultipleBusinessDataReference buildMultipleEmployeeReference(final String name, final long... businessDataIds) {
        final List<Long> ids = new ArrayList<Long>();
        for (final long businessDataId : businessDataIds) {
            ids.add(businessDataId);
        }
        return new org.bonitasoft.engine.business.data.impl.MultipleBusinessDataReferenceImpl(name,
                "com.bonitasoft.pojo.Employee", ids);
    }

    @Test
    public void should_return_the_simple_reference_of_the_business_data_of_the_process_instance() throws Exception {
        final SimpleBusinessDataReference reference= buildSimpleEmployeeReference("myEmployee", 487467354L);
        when(bdmAPI.getProcessBusinessDataReference("myEmployee", 486L)).thenReturn(reference);

        final Response response = request("/bdm/businessDataReference/486/myEmployee").get();

        assertThat(response).hasStatus(Status.SUCCESS_OK);
        assertThat(response).hasJsonEntityEqualTo(readFile("simpleRef.json"));
    }

    @Test
    public void should_return_the_multi_reference_of_the_business_data_of_the_process_instance() throws Exception {
        final MultipleBusinessDataReference reference= buildMultipleEmployeeReference("myEmployee", 487467354L, 48674634L);
        when(bdmAPI.getProcessBusinessDataReference("myEmployee", 486L)).thenReturn(reference);

        final Response response = request("/bdm/businessDataReference/486/myEmployee").get();

        assertThat(response).hasStatus(Status.SUCCESS_OK);
        assertThat(response).hasJsonEntityEqualTo(readFile("multiRef.json"));
    }

    @Test
    public void should_respond_bad_request_when_caseId_pathparam_is_not_a_number() throws Exception {

        final Response response = request("/bdm/businessDataReference/foo/myEmployee").get();

        assertThat(response).hasStatus(Status.CLIENT_ERROR_BAD_REQUEST);
    }

    @Test
    public void should_return_a_not_found_status() throws Exception {
        when(bdmAPI.getProcessBusinessDataReference("myEmployee", 486L)).thenThrow(new DataNotFoundException(new Exception("message")));

        final Response response = request("/bdm/businessDataReference/486/myEmployee").get();

        assertThat(response).hasStatus(Status.CLIENT_ERROR_NOT_FOUND);
        assertThat(response).hasJsonEntityEqualTo("{\"exception\":\"class org.bonitasoft.engine.bpm.data.DataNotFoundException\",\"message\":\"java.lang.Exception: message\"}");
    }

}
