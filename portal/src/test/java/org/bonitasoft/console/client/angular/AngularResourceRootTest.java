package org.bonitasoft.console.client.angular;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class AngularResourceRootTest {

    @Test
    public void should_add_slash_to_url_as_context() throws Exception {
        AngularResourceRoot root = new AngularResourceRoot();

        assertEquals("../portal.js/path/to/resource", root.contextualize("path/to/resource"));
    }
}
