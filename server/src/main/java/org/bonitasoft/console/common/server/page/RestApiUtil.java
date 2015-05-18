package org.bonitasoft.console.common.server.page;

import java.util.logging.Logger;

/**
 * @author Laurent Leseigneur
 */
public interface RestApiUtil {

    String DEFAULT_LOGGER_NAME = "org.bonitasoft.api.extension";

    /**
     * provide a default logger
     */
    Logger getLogger();


}
