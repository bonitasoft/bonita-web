package org.bonitasoft.web.rest.server.api.bpm.flownode.archive;

import static org.assertj.core.api.Assertions.assertThat;

import org.bonitasoft.web.rest.server.APITestWithMock;
import org.junit.Test;

public class APIArchivedUserTaskTest extends APITestWithMock {

    @Test
    public void should_have_default_search_order() throws Exception {
        final APIArchivedUserTask apiActivity = new APIArchivedUserTask();

        final String defineDefaultSearchOrder = apiActivity.defineDefaultSearchOrder();

        assertThat(defineDefaultSearchOrder).as("sould have a default search order").isEqualTo("priority DESC");
    }

}
