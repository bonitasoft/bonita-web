package org.bonitasoft.web.rest.server.api.bpm.flownode;

import static org.assertj.core.api.Assertions.assertThat;

import org.bonitasoft.web.rest.server.APITestWithMock;
import org.junit.Test;


public class APIUserTaskTest extends APITestWithMock {

    @Test
    public void should_have_default_search_order() throws Exception {
        //given
        final APIUserTask api = new APIUserTask();

        //when
        final String defineDefaultSearchOrder = api.defineDefaultSearchOrder();

        //then
        assertThat(defineDefaultSearchOrder).as("sould have a default search order").isEqualTo("priority DESC");
    }
}
