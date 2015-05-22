package org.bonitasoft.web.rest.server.api.bdm;

import static org.assertj.core.api.Assertions.assertThat;

import org.bonitasoft.engine.business.data.impl.MultipleBusinessDataReferenceImpl;
import org.bonitasoft.engine.business.data.impl.SimpleBusinessDataReferenceImpl;
import org.bonitasoft.web.rest.server.api.bpm.flownode.ContextResultElement;
import org.junit.Test;

import java.io.Serializable;
import java.util.Arrays;

public class BusinessDataReferenceResourceFinderTest {

    private BusinessDataReferenceResourceFinder businessDataReferenceResourceFinder = new BusinessDataReferenceResourceFinder();


    @Test
    public void should_return_a_context_of_type_MultipleBusinessDataRef_for_a_given_task_instance() throws Exception {
        //given
        MultipleBusinessDataReferenceImpl bizDataRef = new MultipleBusinessDataReferenceImpl("Ticket", "com.acme.object.Ticket", Arrays.asList(7L, 8L));


        Serializable contextResultElement = businessDataReferenceResourceFinder.toClientObject(bizDataRef);

        assertThat(contextResultElement).isInstanceOf(MultipleBusinessDataReferenceClient.class);
        final MultipleBusinessDataReferenceClient businessDataReferenceClient = (MultipleBusinessDataReferenceClient) contextResultElement;
        assertThat(businessDataReferenceClient.getName()).isEqualTo("Ticket");
        assertThat(businessDataReferenceClient.getType()).isEqualTo("com.acme.object.Ticket");
        assertThat(businessDataReferenceClient.getStorageIds()).containsExactly(7L, 8L);
        assertThat(businessDataReferenceClient.getLink()).isEqualTo("API/bdm/businessData/com.acme.object.Ticket/?q=findByIds&f=ids=7,8");
    }

    @Test
    public void should_return_a_context_of_type_Simple_for_a_given_task_instance() throws Exception {
        //given
        SimpleBusinessDataReferenceImpl bizDataRef = new SimpleBusinessDataReferenceImpl("Ticket", "com.acme.object.Ticket", 8L);


        Serializable contextResultElement = businessDataReferenceResourceFinder.toClientObject(bizDataRef);

        assertThat(contextResultElement).isInstanceOf(SimpleBusinessDataReferenceClient.class);
        final SimpleBusinessDataReferenceClient businessDataReferenceClient = (SimpleBusinessDataReferenceClient) contextResultElement;
        assertThat(businessDataReferenceClient.getName()).isEqualTo("Ticket");
        assertThat(businessDataReferenceClient.getType()).isEqualTo("com.acme.object.Ticket");
        assertThat(businessDataReferenceClient.getStorageId()).isEqualTo(8L);
        assertThat(businessDataReferenceClient.getLink()).isEqualTo("API/bdm/businessData/com.acme.object.Ticket/8");
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



}