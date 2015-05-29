import groovy.json.JsonBuilder
import org.bonitasoft.console.common.server.page.*

import javax.servlet.http.HttpServletRequest

public class Get implements RestApiController {

    @Override
    RestApiResponse doHandle(HttpServletRequest request, PageResourceProvider pageResourceProvider, PageContext pageContext, RestApiResponseBuilder apiResponseBuilder, RestApiUtil restApiUtil) {
        Map<String, String> response = [:]
        response.put "response", "hello from get resource"
        response.putAll request.parameterMap
        apiResponseBuilder.with {
            withResponse new JsonBuilder(response).toPrettyString()
            build()
        }
    }
}
