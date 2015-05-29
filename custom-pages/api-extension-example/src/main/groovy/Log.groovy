import groovy.json.JsonBuilder
import org.bonitasoft.console.common.server.page.*

import javax.servlet.http.HttpServletRequest
import java.util.logging.Logger

public class Log implements RestApiController {

    @Override
    RestApiResponse doHandle(HttpServletRequest request, PageResourceProvider pageResourceProvider, PageContext pageContext, RestApiResponseBuilder apiResponseBuilder, RestApiUtil restApiUtil) {

        Logger logger = restApiUtil.logger

        logger.info "info message from REST API extension example"
        logger.finest "finest message from REST API extension example"
        logger.severe "severe message from REST API extension example"

        Map<String, Serializable> response = [:]
        response.put "response", "hello with log"
        response.put "actual logger name", logger.name
        apiResponseBuilder.with {
            withResponse new JsonBuilder(response).toPrettyString()
            build()
        }

    }

}
