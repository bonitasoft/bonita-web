package org.bonitasoft.web.rest.server.api.bdm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.bonitasoft.engine.business.data.impl.MultipleBusinessDataReferenceImpl;
import org.bonitasoft.engine.business.data.impl.SimpleBusinessDataReferenceImpl;
import org.bonitasoft.web.rest.server.api.bpm.flownode.ContextResultElement;
import org.junit.Test;
import org.restlet.Response;
import org.restlet.data.Status;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BusinessDataReferenceResourceFinderTest {

    private BusinessDataReferenceResourceFinder businessDataReferenceResourceFinder = new BusinessDataReferenceResourceFinder();


    @Test
    public void should_return_a_context_of_type_MultipleBusinessDataRef_for_a_given_task_instance() throws Exception {
        //given
        MultipleBusinessDataReferenceImpl bizDataRef = new MultipleBusinessDataReferenceImpl("Ticket", "com.acme.object.Ticket", Arrays.asList(7L, 8L));


        Serializable contextResultElement = businessDataReferenceResourceFinder.getContextResultElement(bizDataRef);

        assertThat(contextResultElement).isEqualTo(new ContextResultElement("com.acme.object.Ticket", "[7, 8]", "API/bdm/businessData/com.acme.object.Ticket/?q=findByIds&f=ids=7,8"));
    }

    @Test
    public void should_return_a_context_of_type_Simple_for_a_given_task_instance() throws Exception {
        //given
        SimpleBusinessDataReferenceImpl bizDataRef = new SimpleBusinessDataReferenceImpl("Ticket", "com.acme.object.Ticket", 8L);


        Serializable contextResultElement = businessDataReferenceResourceFinder.getContextResultElement(bizDataRef);

        assertThat(contextResultElement).isEqualTo(new ContextResultElement("com.acme.object.Ticket","8","API/bdm/businessData/com.acme.object.Ticket/8"));
    }
    @Test
    public void should_handle_multiple_business_data() throws Exception {
        //given
        MultipleBusinessDataReferenceImpl bizDataRef = new MultipleBusinessDataReferenceImpl("Ticket", "com.acme.object.Ticket", Arrays.asList(7L, 8L));


        boolean handlesResource = businessDataReferenceResourceFinder.handlesResource(bizDataRef);

        assertThat(handlesResource).isTrue();
    }

    @Test
    public void should_handle_simple_business_data() throws Exception {
        //given
        SimpleBusinessDataReferenceImpl bizDataRef = new SimpleBusinessDataReferenceImpl("Ticket", "com.acme.object.Ticket", 8L);

        boolean handlesResource = businessDataReferenceResourceFinder.handlesResource(bizDataRef);

        assertThat(handlesResource).isTrue();
    }

    @Test
    public void should_not_handle_other_types() throws Exception {

        boolean handlesResource = businessDataReferenceResourceFinder.handlesResource(12l);

        assertThat(handlesResource).isFalse();
    }

    @Test
    public void should_return_the_object_if_it_is_not_a_business_data() throws Exception {

        Serializable contextResultElement = businessDataReferenceResourceFinder.getContextResultElement(12l);

        assertThat(contextResultElement).isEqualTo(12l);
    }



}