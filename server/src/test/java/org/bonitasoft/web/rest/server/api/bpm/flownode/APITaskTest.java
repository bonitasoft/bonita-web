package org.bonitasoft.web.rest.server.api.bpm.flownode;

import static org.assertj.core.api.Assertions.assertThat;

import org.bonitasoft.web.rest.server.APITestWithMock;
import org.junit.Test;

public class APITaskTest extends APITestWithMock {

    @Test
    public void should_have_default_search_order() throws Exception {
        //given
        final APITask apiActivity = new APITask();

        //when
        final String defineDefaultSearchOrder = apiActivity.defineDefaultSearchOrder();

        //then
        assertThat(defineDefaultSearchOrder).as("sould have a default search order").isEqualTo("last_update_date DESC");
    }
}
