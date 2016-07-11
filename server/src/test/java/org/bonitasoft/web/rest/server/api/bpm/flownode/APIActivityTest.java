package org.bonitasoft.web.rest.server.api.bpm.flownode;

import static org.assertj.core.api.Assertions.assertThat;

import org.bonitasoft.web.rest.server.APITestWithMock;
import org.junit.Test;

public class APIActivityTest extends APITestWithMock {

    @Test
    public void should_have_default_search_order() throws Exception {
        final APIActivity apiActivity = new APIActivity();

        final String defineDefaultSearchOrder = apiActivity.defineDefaultSearchOrder();

        assertThat(defineDefaultSearchOrder).as("sould have a default search order").isEqualTo("state ASC");
    }

}
