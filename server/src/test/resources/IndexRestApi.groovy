import org.bonitasoft.console.common.server.page.PageContext
import org.bonitasoft.console.common.server.page.PageResourceProvider
import org.bonitasoft.console.common.server.page.RestApiController
import org.bonitasoft.console.common.server.page.RestApiResponse
import org.bonitasoft.console.common.server.page.RestApiResponseBuilder
import org.bonitasoft.console.common.server.page.RestApiUtil

import javax.servlet.http.HttpServletRequest

public class IndexRestApi implements RestApiController {



    @Override
    RestApiResponse doHandle(HttpServletRequest request, PageResourceProvider pageResourceProvider, PageContext pageContext, RestApiResponseBuilder apiResponseBuilder, RestApiUtil restApiUtil) {

        return apiResponseBuilder.withResponse("result").build()

    }
}
