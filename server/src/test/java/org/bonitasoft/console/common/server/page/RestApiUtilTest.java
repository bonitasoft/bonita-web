package org.bonitasoft.console.common.server.page;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.logging.Logger;

import org.junit.Test;

/**
 * @author Laurent Leseigneur
 */
public class RestApiUtilTest {

    @Test
    public void testGetLogger() throws Exception {
        //given
        RestApiUtil restApiUtil = new RestApiUtilImpl();

        //when
        final Logger logger = restApiUtil.getLogger();

        //then
        assertThat(logger.getName()).as("should get").isEqualTo("org.bonitasoft.api.extension");
    }
}