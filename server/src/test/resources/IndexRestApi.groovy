import org.bonitasoft.console.common.server.page.PageContext
import org.bonitasoft.console.common.server.page.PageResourceProvider
import org.bonitasoft.console.common.server.page.RestApiController

import javax.servlet.http.HttpServletRequest

public class IndexRestApi implements RestApiController {



    @Override
    Serializable doHandle(HttpServletRequest request, PageResourceProvider pageResourceProvider, PageContext pageContext) {
        return "result"
    }
}
