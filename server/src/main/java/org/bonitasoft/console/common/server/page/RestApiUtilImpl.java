package org.bonitasoft.console.common.server.page;

import java.util.logging.Logger;

/**
 * @author Laurent Leseigneur
 */
public class RestApiUtilImpl implements  RestApiUtil{

    @Override
    public Logger getLogger() {
        return Logger.getLogger(DEFAULT_LOGGER_NAME);
    }
}
