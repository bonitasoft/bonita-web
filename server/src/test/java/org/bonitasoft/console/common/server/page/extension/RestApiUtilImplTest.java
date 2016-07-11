package org.bonitasoft.console.common.server.page.extension;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.logging.Logger;

import org.bonitasoft.console.common.server.page.RestApiUtil;
import org.bonitasoft.console.common.server.page.extension.RestApiUtilImpl;
import org.junit.Test;

/**
 * @author Laurent Leseigneur
 */
public class RestApiUtilImplTest {

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